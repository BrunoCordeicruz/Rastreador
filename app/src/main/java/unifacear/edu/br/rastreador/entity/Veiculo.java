package unifacear.edu.br.rastreador.entity;

import java.io.Serializable;

public class Veiculo implements Serializable {

    private Integer id;
    private String marca;
    private String modelo;
    private String placa;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    @Override
    public String toString() {
        return id + " - " + marca + "  " + modelo + "  " + placa;
    }

}
