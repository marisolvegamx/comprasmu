package com.example.comprasmu.ui.informedetalle;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;

import com.example.comprasmu.data.repositories.CatalogoDetalleRepositoryImpl;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;

import com.example.comprasmu.utils.Event;

import java.util.List;

public class VerInformeDetViewModel extends AndroidViewModel {


    public  LiveData<InformeCompraDetalle> informeSel;

    public ImagenDetalle foto_codigo_produccion;
    public ImagenDetalle energia;
    public ImagenDetalle foto_num_tienda;
    public ImagenDetalle foto_atributoa;
    public ImagenDetalle foto_atributob;
    public ImagenDetalle foto_atributoc;
    public ImagenDetalle marca_traslape;
    public ImagenDetalle etiqueta_evaluacion;
    private final ImagenDetRepositoryImpl imagenDetRepository;

    private final InformeComDetRepositoryImpl detalleRepo;
    public List<CatalogoDetalle> atributos;
    public List<CatalogoDetalle> tomadoDe;
    private final CatalogoDetalleRepositoryImpl catRepo;

    public VerInformeDetViewModel(@NonNull Application application) {
        super(application);


        this.imagenDetRepository=new ImagenDetRepositoryImpl(application);
        this.detalleRepo=new InformeComDetRepositoryImpl(application);
        this.catRepo=new CatalogoDetalleRepositoryImpl(application);
    }

    public void cargarCatalogos(){
        atributos=catRepo.getxCatalogo("atributos");
        tomadoDe=catRepo.getxCatalogo("ubicacion_muestra");

    }



}