package unifacear.edu.br.rastreador.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import unifacear.edu.br.rastreador.Conexao;
import unifacear.edu.br.rastreador.entity.Veiculo;

public class VeiculoDao {


    private Conexao conexao;
    private SQLiteDatabase banco;

    public VeiculoDao(Context context) {
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
    }

    public long inserir(Veiculo veiculo) {
        ContentValues values = new ContentValues();
        values.put("marca", veiculo.getMarca());
        values.put("modelo", veiculo.getModelo());
        values.put("placa", veiculo.getPlaca());

        return banco.insert("veiculos", null, values);
    }


    public List<Veiculo> obterTodos() {

        List<Veiculo> veiculos = new ArrayList<>();
        Cursor cursor = banco.query("veiculos", new String[]{"id", "marca", "modelo", "placa"},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            Veiculo v = new Veiculo();
            v.setId(cursor.getInt(0));
            v.setMarca(cursor.getString(1));
            v.setModelo(cursor.getString(2));
            v.setPlaca(cursor.getString(3));

            veiculos.add(v);
        }
        return veiculos;
    }

    public void excluir(Veiculo v) {
        banco.delete("veiculos", "id = ?", new String[]{v.getId().toString()});
    }

    public void editar(Veiculo v) {

        ContentValues values = new ContentValues();


        values.put("marca", v.getMarca().toString());
        values.put("modelo", v.getModelo().toString());
        values.put("placa", v.getPlaca().toString());


        banco.update("veiculos", values, "id = ?", new String[]{v.getId().toString()});


    }

}
