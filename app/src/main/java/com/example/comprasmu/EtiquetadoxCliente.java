package com.example.comprasmu;

import androidx.annotation.NonNull;

public class EtiquetadoxCliente{
    public int consCaja;
    public int numMuestras;
    public int numCaja;

    public EtiquetadoxCliente() {

    }

    @NonNull
    @Override
    public String toString() {
        return "conscaja"+consCaja+" muestras"+numMuestras+" numcaja"+numCaja;
    }
}