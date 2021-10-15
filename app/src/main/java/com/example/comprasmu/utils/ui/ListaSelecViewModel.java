package com.example.comprasmu.utils.ui;

import androidx.lifecycle.ViewModel;

import com.example.comprasmu.data.modelos.DescripcionGenerica;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListaSelecViewModel extends ViewModel {

    private  int size=0;
    private  boolean empty=false;
    //la lista que se mostrará llega como un arreglo del tipo key, value
    private  List<DescripcionGenerica> lista ;



    public ListaSelecViewModel() {
        if(lista!=null) {
            size = lista.size();
            empty = lista.isEmpty();
        }
    }
    public void setLista( List<DescripcionGenerica> lista){
        this.lista=lista;
       /* this.lista=new ArrayList<DescripcionGenerica>();
       /* for (String tupla: lista) {
            String[] valores=tupla.split(";");
            DescripcionGenerica de=new DescripcionGenerica();
            de.setId(Integer.parseInt(valores[0]));
            de.setNombre(valores[1]);
            this.lista.add(de);
        }*/

    }


    public List<DescripcionGenerica> getLista() {
        return lista;
    }
}