package unifacear.edu.br.rastreador;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import unifacear.edu.br.rastreador.dao.VeiculoDao;
import unifacear.edu.br.rastreador.entity.Veiculo;

public class EditarVeiculoActivity extends AppCompatActivity {

    private EditText id;
    private EditText marca;
    private EditText modelo;
    private EditText placa;

    private VeiculoDao dao;

    private Button btnatualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_veiculos);

        id = (EditText) findViewById(R.id.editID);
        marca = (EditText) findViewById(R.id.editMarca);
        modelo = (EditText) findViewById(R.id.editModelo);
        placa = (EditText) findViewById(R.id.editPlaca);
        btnatualizar = (Button) findViewById(R.id.btnAtualizar);
        dao = new VeiculoDao(this);


        Intent intent = getIntent();
        Veiculo v = (Veiculo) intent.getSerializableExtra("veiculo");

        id.setText(v.getId().toString());
        id.setEnabled(false);
        marca.setText(v.getMarca().toString());
        modelo.setText(v.getModelo().toString());
        placa.setText(v.getPlaca().toString());
    }

    public void EditarVeiculo(View view) {


        Veiculo v = new Veiculo();
        v.setId(Integer.parseInt(id.getText().toString()));
        v.setMarca(marca.getText().toString());
        v.setModelo(modelo.getText().toString());
        v.setPlaca(placa.getText().toString());


        dao.editar(v);
        Toast.makeText(this, "Veiculo com id n°: " + v.getId().toString() + "" +
                " atualizado com sucesso", Toast.LENGTH_LONG).show();


        Intent it = new Intent(this, ListarVeiculosActivity.class);
        startActivity(it);

    }


}
