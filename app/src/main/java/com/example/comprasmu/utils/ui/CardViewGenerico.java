package com.example.comprasmu.utils.ui;

import android.widget.Button;

import java.util.HashMap;
import java.util.List;

public class CardViewGenerico {
    private String titulo1;
    private List<String> textos;
    private Button botonIr;

    public String getTitulo1() {
        return titulo1;
    }

    public void setTitulo1(String titulo1) {
        this.titulo1 = titulo1;
    }

    public List<String> getTextos() {
        return textos;
    }

    public void setTextos(List<String> textos) {
        this.textos = textos;
    }

    public Button getBotonIr() {
        return botonIr;
    }

    public void setBotonIr(Button botonIr) {
        this.botonIr = botonIr;
    }

    public String toString(){

        return this.titulo1+"--"+textos.toString();
    }
}
