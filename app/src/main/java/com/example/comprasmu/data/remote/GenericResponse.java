package com.example.comprasmu.data.remote;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;

public class GenericResponse<T> {
  //  LiveData<ArrayList<T>> listadedatos;

    ArrayList<T> listadedatos;

    public ArrayList<T> getDatos() {
        return listadedatos;
    }

    public void setDatos(ArrayList<T> datos) {
        this.listadedatos = datos;
    }
  /*  public LiveData<ArrayList<T>> getDatos() {
        return listadedatos;
    }

    public void setDatos(LiveData<ArrayList<T>> datos) {
        this.listadedatos = datos;
    }*/

}
