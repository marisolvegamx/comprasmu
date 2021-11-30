package com.example.comprasmu.utils;

import android.app.Activity;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.DescripcionGenerica;

import java.util.HashMap;
import java.util.List;

public class CampoForm {
   public String label;
   public String nombre_campo;
   public int id;
    public String type;
   public boolean visible;
    public String value;
    public String required;
    public HashMap<Integer,String> select;
    public List<CatalogoDetalle> selectcat;
    public List<DescripcionGenerica> selectdes;
    public String readonly;
    public String disabled;
    public View.OnClickListener funcionOnClick;
    public View.OnClickListener funcionOnClick2;
    public Activity actividadAccion;
    public boolean tomarFoto;
    public RecyclerView.Adapter adapter;
    public List<CampoForm> listadatos;
    public int style;

    public CampoForm() {
    }

    public CampoForm(String label, String nombre_campo, int id, String type, String value, String required, HashMap select, String readonly, String disabled) {
        this.label = label;
        this.nombre_campo = nombre_campo;
        this.id = id;
        this.type = type;
        this.value = value;
        this.required = required;
        this.select = select;
        this.readonly = readonly;
        this.disabled = disabled;
    }
}
