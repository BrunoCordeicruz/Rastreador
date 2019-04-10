package unifacear.edu.br.rastreador;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import unifacear.edu.br.rastreador.dao.VeiculoRastreaDao;
import unifacear.edu.br.rastreador.entity.VeiculoRastrea;

public class ListarVeiculosRastreadosActivity extends AppCompatActivity {

    private ListView lista;
    private VeiculoRastreaDao daoRast;
    private List<VeiculoRastrea> rastreados;
    private List<VeiculoRastrea> rastredosFiltrados = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listar_veiculos_cadastrados);

        lista = (ListView) findViewById(R.id.lista_veiculos_rastreados);
        daoRast = new VeiculoRastreaDao(this);
        rastreados = daoRast.obterTodos();
        rastredosFiltrados.addAll(rastreados);
        ArrayAdapter adptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, rastredosFiltrados);
        lista.setAdapter(adptador);
        registerForContextMenu(lista);

    }

}
