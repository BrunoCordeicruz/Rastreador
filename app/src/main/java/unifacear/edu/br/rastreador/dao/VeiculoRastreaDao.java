package unifacear.edu.br.rastreador.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import unifacear.edu.br.rastreador.Conexao;
import unifacear.edu.br.rastreador.entity.VeiculoRastrea;

public class VeiculoRastreaDao {


    private Conexao conexao;
    private SQLiteDatabase banco;

    public VeiculoRastreaDao(Context context) {
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
    }

    public long inserir(VeiculoRastrea rastrea) {
        ContentValues values = new ContentValues();
        values.put("marca", rastrea.getMarca());
        values.put("modelo", rastrea.getModelo());
        values.put("placa", rastrea.getPlaca());
        values.put("local", rastrea.getLocal());

        return banco.insert("rastrear", null, values);
    }


    public List<VeiculoRastrea> obterTodos() {

        List<VeiculoRastrea> v_rasteados = new ArrayList<>();
      /*  Cursor cursor = banco.query("rastrear",new String[]{"id","marca","modelo","placa","local"},
                null,null,null,null,null);*/


        Cursor cursor = banco.rawQuery("SELECT * FROM rastrear", null);
        while (cursor.moveToNext()) {
            VeiculoRastrea v = new VeiculoRastrea();
            v.setId(cursor.getInt(0));
            v.setMarca(cursor.getString(1));
            v.setModelo(cursor.getString(2));
            v.setPlaca(cursor.getString(3));
            v.setLocal(cursor.getString(4));

            v_rasteados.add(v);
        }

        return v_rasteados;
    }

    public void excluir(VeiculoRastrea v) {
        banco.delete("rastrear", "id = ?", new String[]{v.getId().toString()});
    }

    public void editar(VeiculoRastrea v) {

        ContentValues values = new ContentValues();


        values.put("marca", v.getMarca().toString());
        values.put("modelo", v.getModelo().toString());
        values.put("placa", v.getPlaca().toString());


        banco.update("rastrear", values, "id = ?", new String[]{v.getId().toString()});


    }

}
