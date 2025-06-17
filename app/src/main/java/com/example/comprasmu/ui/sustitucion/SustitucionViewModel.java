package com.example.comprasmu.ui.sustitucion;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.comprasmu.R;
import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.ListaCompraDao;
import com.example.comprasmu.data.dao.SustitucionDao;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.ListaDetalleBu;
import com.example.comprasmu.data.modelos.ListaWithDetalle;
import com.example.comprasmu.data.modelos.Sustitucion;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.SustitucionRepositoryImpl;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SustitucionViewModel extends AndroidViewModel {

    private final SustitucionRepositoryImpl repository;
    private  LiveData<List<Sustitucion>> listaSustitucion;
    private final MutableLiveData<Event<Integer>> mOpenSustitucionEvent = new MutableLiveData<>();
    private  LiveData<Integer> size;

    private  LiveData<Boolean> empty;
    private int idListaSel;
    public Sustitucion listaSelec;
    private int clienteSel;
    private static final String TAG= SustitucionViewModel.class.getCanonicalName();
    Context context;

    public SustitucionViewModel(Application application) {
        super(application);
        repository =new  SustitucionRepositoryImpl(application);
        context=application;
    }


    public void cargarListas(int categoria,int cliente){
        listaSustitucion =repository.getByFiltros(categoria,"",0,0, cliente);
        size = Transformations.map(listaSustitucion,res->{ return listaSustitucion.getValue().size();});
        empty = Transformations.map(listaSustitucion, res->{return listaSustitucion.getValue().isEmpty();});
    }


    public String ordenarCodigosNoPermitidos( Sustitucion detalle) {
        SimpleDateFormat sdfcodigo= new SimpleDateFormat("dd-MM-yy");
        InformeComDetRepositoryImpl icrepo=new InformeComDetRepositoryImpl(context);
        String nvoCodigos = "";
        //busco los nuevos codigos
        if(Constantes.VarListCompra.detallebuSel!=null) {
            List<InformeCompraDetalle> informeCompraDetalles = icrepo.getByProductoAna(Constantes.INDICEACTUAL, Constantes.VarListCompra.plantaSel, detalle.getSu_producto(), Constantes.VarListCompra.detallebuSel.getAnalisisId(), detalle.getSu_tipoempaque(), detalle.getNomtamanio());

            if (informeCompraDetalles != null) {
                //     Log.d(TAG,"encontré " +informeCompraDetalles.size());
                for (InformeCompraDetalle info : informeCompraDetalles) {
                    nvoCodigos = nvoCodigos + sdfcodigo.format(info.getCaducidad()) + "\n";

                }
            }
        }

        return  nvoCodigos;

    }


    public LiveData<List<Sustitucion>> getListas() {
        return listaSustitucion;
    }


    public LiveData<Integer> getSize() {
        return size;
    }

    public LiveData<Boolean> getEmpty() {
        return empty;
    }


}