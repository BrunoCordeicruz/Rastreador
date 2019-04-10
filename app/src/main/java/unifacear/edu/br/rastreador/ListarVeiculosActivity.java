package unifacear.edu.br.rastreador;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import unifacear.edu.br.rastreador.dao.VeiculoDao;
import unifacear.edu.br.rastreador.dao.VeiculoRastreaDao;
import unifacear.edu.br.rastreador.entity.Veiculo;
import unifacear.edu.br.rastreador.entity.VeiculoRastrea;

public class ListarVeiculosActivity extends AppCompatActivity {


    private ListView lista;
    private VeiculoDao dao;
    private VeiculoRastreaDao daoRast;
    private List<Veiculo> veiculos;
    private List<Veiculo> veiculosFiltrados = new ArrayList<>();
    private String endereco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listar_veiculos);

        lista = (ListView) findViewById(R.id.lista_veiculos);

        dao = new VeiculoDao(this);
        veiculos = dao.obterTodos();
        veiculosFiltrados.addAll(veiculos);
        ArrayAdapter adptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, veiculosFiltrados);
        lista.setAdapter(adptador);

        registerForContextMenu(lista);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_principal, menu);

        SearchView sv = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                procuraVeiculo(newText);
                return false;
            }
        });
        return true;
    }

    public void procuraVeiculo(String marca) {

        veiculosFiltrados.clear();
        for (Veiculo v : veiculos) {
            if (v.getMarca().toLowerCase().contains(marca.toLowerCase()) || v.getPlaca().toLowerCase().contains(marca.toLowerCase()) || v.getModelo().toLowerCase().contains(marca.toLowerCase())) {
                veiculosFiltrados.add(v);
            }
        }
        lista.invalidateViews();
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_contexto, menu);
    }

    public void cadastrar(MenuItem item) {
        Intent it = new Intent(this, VeiculoActivity.class);
        startActivity(it);
    }

    public void listaRastreados(MenuItem item) {
        Intent it = new Intent(this, ListarVeiculosRastreadosActivity.class);
        startActivity(it);
    }

    public void excluir(MenuItem item) {

        AdapterView.AdapterContextMenuInfo menuInfo =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Veiculo veiculoExcluir = veiculosFiltrados.get(menuInfo.position);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Atenção")
                .setMessage("Realmente deseja excluir o veiculo?")
                .setNegativeButton("NÃO", null)
                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        veiculosFiltrados.remove(veiculoExcluir);
                        veiculos.remove(veiculoExcluir);
                        dao.excluir(veiculoExcluir);
                        lista.invalidateViews();

                    }
                }).create();
        dialog.show();

    }

    public void editar(MenuItem item) {

        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Veiculo veiculoEditar = veiculosFiltrados.get(menuInfo.position);

        Intent it = new Intent(this, EditarVeiculoActivity.class);
        it.putExtra("veiculo", veiculoEditar);
        startActivity(it);

    }

    public void rastrear(MenuItem item) {
        //pegando valores do item
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Veiculo veiculoRastrear = veiculosFiltrados.get(menuInfo.position);

        //pegar geolocalização
        LocationManager lm = (LocationManager)
                getSystemService(getApplicationContext().LOCATION_SERVICE);

        LocationListener ll = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Geocoder geocoder = new Geocoder(getApplicationContext(),
                        Locale.getDefault());

                try {
                    List<Address> enderecos = geocoder.getFromLocation(
                            location.getLatitude(),
                            location.getLongitude(),
                            1);

                    endereco = enderecos.get(0).getAddressLine(0);
                    Toast.makeText(getApplicationContext(),
                            endereco,
                            Toast.LENGTH_LONG).show();

                    //colocar essa informação no bd
                    VeiculoRastrea v_rastrear = new VeiculoRastrea();
                    v_rastrear.setMarca(veiculoRastrear.getMarca());
                    v_rastrear.setModelo(veiculoRastrear.getModelo());
                    v_rastrear.setPlaca(veiculoRastrear.getPlaca());
                    v_rastrear.setLocal(endereco);

                    //chamando a dao e inserindo
                    daoRast.inserir(v_rastrear);


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };


        String[] permissoes = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        ActivityCompat.requestPermissions(this,
                permissoes,
                1);


        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),
                    "Sem permissão para acessar localizações!!",
                    Toast.LENGTH_LONG).show();
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                5000, 5, ll);

    }


    @Override
    public void onResume() {
        super.onResume();
        veiculos = dao.obterTodos();
        veiculosFiltrados.clear();
        veiculosFiltrados.addAll(veiculos);
        lista.invalidateViews();
    }
}
