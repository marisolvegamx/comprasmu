package com.example.comprasmu.ui.envio;

/***para recibir el json de los documentos que se imprimen**/
public class DocumentosEnvio {
    private int inv_id;
    private int ciudadId;
    private int fda;
    private int recoleccion;
    private int anexo1;
    private int anexo2;
    private int anexo3;

    public int getInv_id() {
        return inv_id;
    }

    public void setInv_id(int inv_id) {
        this.inv_id = inv_id;
    }

    public int getCiudadId() {
        return ciudadId;
    }

    public void setCiudadId(int ciudadId) {
        this.ciudadId = ciudadId;
    }

    public int getFda() {
        return fda;
    }

    public void setFda(int fda) {
        this.fda = fda;
    }

    public int getRecoleccion() {
        return recoleccion;
    }

    public void setRecoleccion(int recoleccion) {
        this.recoleccion = recoleccion;
    }

    public int getAnexo1() {
        return anexo1;
    }

    public void setAnexo1(int anexo1) {
        this.anexo1 = anexo1;
    }

    public int getAnexo2() {
        return anexo2;
    }

    public void setAnexo2(int anexo2) {
        this.anexo2 = anexo2;
    }

    public int getAnexo3() {
        return anexo3;
    }

    public void setAnexo3(int anexo3) {
        this.anexo3 = anexo3;
    }
}
