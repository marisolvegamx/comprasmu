package com.example.comprasmu.ui.gasto;

//clase para consultar en el servidor los totales de muestra para gastos
public class TotalMuestra {
   private String cliente;
   private int clientesId;
   private int plantasId;
   private String planta;
    private int num_muestras;
    private int mues_solicitadas;
    private int mues_reembolsadas;
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

    public int getClientesId() {
        return clientesId;
    }

    public void setClientesId(int clientesId) {
        this.clientesId = clientesId;
    }

    public int getMues_solicitadas() {
        return mues_solicitadas;
    }

    public void setMues_solicitadas(int mues_solicitadas) {
        this.mues_solicitadas = mues_solicitadas;
    }

    public int getMues_reembolsadas() {
        return mues_reembolsadas;
    }

    public void setMues_reembolsadas(int mues_reembolsadas) {
        this.mues_reembolsadas = mues_reembolsadas;
    }

    public int getPlantasId() {
        return plantasId;
    }

    public void setPlantasId(int plantasId) {
        this.plantasId = plantasId;
    }

    public String getPlanta() {
        return planta;
    }

    public void setPlanta(String planta) {
        this.planta = planta;
    }
}
