package com.example.comprasmu.ui.gasto;

//clase para consultar en el servisor los totales de muestra para gastos
public class TotalMuestra {
   private String cliente;
    private int num_muestras;
    private float costo;

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public int getNum_muestras() {
        return num_muestras;
    }

    public void setNum_muestras(int num_muestras) {
        this.num_muestras = num_muestras;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }
}
