package unifacear.edu.br.rastreador;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import unifacear.edu.br.rastreador.dao.VeiculoDao;
import unifacear.edu.br.rastreador.entity.Veiculo;

public class VeiculoActivity extends AppCompatActivity {


    private EditText marca;
    private EditText modelo;
    private EditText placa;

    private VeiculoDao dao;

    private Button btnsalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastrar_veiculos);

        marca = (EditText) findViewById(R.id.editMarca);
        modelo = (EditText) findViewById(R.id.editModelo);
        placa = (EditText) findViewById(R.id.editPlaca);
        btnsalvar = (Button) findViewById(R.id.btnSalvar);
        dao = new VeiculoDao(this);


    }

    public void SalvarVeiculo(View view) {

        /*Aqui iremos criar um objeto do tipo veiculo que irá
        receber os valores atribudos aos campos do layout*/
        Veiculo v = new Veiculo();
        v.setMarca(marca.getText().toString());
        v.setModelo(modelo.getText().toString());
        v.setPlaca(placa.getText().toString());


        long id = dao.inserir(v);
        clear();
        Toast.makeText(this, "Veiculo inserido com id n°: " + id, Toast.LENGTH_LONG).show();
    }

    public void clear() {
        marca.setText("");
        modelo.setText("");
        placa.setText("");
    }


}
