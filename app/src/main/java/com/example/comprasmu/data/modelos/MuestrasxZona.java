package com.example.comprasmu.data.modelos;

public class MuestrasxZona {

    int zona;
    float muestras;

    public MuestrasxZona(int zona, float muestras) {
        this.zona = zona;
        this.muestras = muestras;

    }

    public int getZona() {
        return zona;
    }

    public void setZona(int zona) {
        this.zona = zona;
    }

    public float getMuestras() {
        return muestras;
    }

    public void setMuestras(float muestras) {
        this.muestras = muestras;
    }
}
