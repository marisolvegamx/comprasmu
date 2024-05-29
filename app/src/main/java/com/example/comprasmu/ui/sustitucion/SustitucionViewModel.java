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
        SustitucionDao dao=ComprasDataBase.getInstance(application).getSustitucionDao();
        repository =new  SustitucionRepositoryImpl(application);
        context=application;
    }


    public void cargarListas(int plantasel,int categoria,int cliente,int empaque, int tamanio, int numTienda, int productoorigId){
      /*  if(cliente==7){
            cargarListasJum(plantasel,categoria,empaque,tamanio,productoorigId);
        }else*/
            listaSustitucion =repository.getByFiltros(categoria,"",0,0, cliente);
        size = Transformations.map(listaSustitucion,res->{ return listaSustitucion.getValue().size();});
        empty = Transformations.map(listaSustitucion, res->{return listaSustitucion.getValue().isEmpty();});
    }

    public void cargarListasJum(int plantaSel,int categoria,int empaque, int tamanio,int productoorigId){
       /* if(productoSel.getClientesId()==7) { //valido que no exista en la compra
            if(!productoSel.getNomproducto().equals("")&&productoSel.getNomproducto().contains("FRUTZZO")){
                //PUEDO COMPRAR
            }else
            if (mViewModel.validarProdJum(Constantes.INDICEACTUAL, plantaSel, productoSel)) {
                Toast.makeText(getActivity(), getString(R.string.err_mismo_prod), Toast.LENGTH_LONG).show();
                return;
            }
        }*/

        //ver que no se haya comprado
        // if(numTienda>=5)
        MutableLiveData listaTemp=new MutableLiveData();
        List<Sustitucion> array=new ArrayList<>();
        List<Sustitucion> listaProds =repository.getByFiltrosJumSim(categoria,"FRUTZZO",empaque ,tamanio,clienteSel, plantaSel,productoorigId);
        Log.d(TAG,"encontre "+listaProds.size());
        for (Sustitucion producto:
                listaProds) {
            if(!validarProdJum(Constantes.INDICEACTUAL, plantaSel, producto))
                array.add(producto);

        }
        Log.d(TAG,"quedaron"+array.size());
        //agrego los frutzo porque esto si se pueden volver a comprar
        List<Sustitucion> listafrut=repository.getByFiltrosFrut(categoria,"FRUTZZO", productoorigId, tamanio,empaque);
        array.addAll(listafrut);
        if(categoria==22) { //10-abr-24 para kermato no incluyo el tamanio
            List<Sustitucion> listakermato =repository.getByFiltrosKerm(categoria,"KERMATO", productoorigId,tamanio,empaque);
            array.addAll(listakermato);
        }
        listaTemp.setValue(array);
        listaSustitucion=listaTemp;
        // else
        //   listaSustitucion =repository.getByFiltros(categoria,"",empaque,tamanio);

    }


    public String ordenarCodigosNoPermitidos( Sustitucion detalle) {
        SimpleDateFormat sdfcodigo= new SimpleDateFormat("dd-MM-yy");


        InformeComDetRepositoryImpl icrepo=new InformeComDetRepositoryImpl(context);
        String nvoCodigos = "";

        //busco los nuevos codigos
        //  Log.d(TAG,"buscando cods "+Constantes.VarListCompra.plantaSel+"--"+detalle.getSu_producto()+"--"+Constantes.VarListCompra.detallebuSel.getAnalisisId()+"--"+detalle.getSu_tipoempaque()+"--"+detalle.getNomtamanio());
        List<InformeCompraDetalle> informeCompraDetalles=icrepo.getByProductoAna(Constantes.INDICEACTUAL,Constantes.VarListCompra.plantaSel,detalle.getSu_producto(),Constantes.VarListCompra.detallebuSel.getAnalisisId(),detalle.getSu_tipoempaque(),detalle.getNomtamanio());

        if(informeCompraDetalles!=null) {
            //     Log.d(TAG,"encontré " +informeCompraDetalles.size());
            for (InformeCompraDetalle info : informeCompraDetalles) {
                nvoCodigos = nvoCodigos + sdfcodigo.format(info.getCaducidad()) + "\n";

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
    //valido que no haya comprado el mismo sabor empaque y tamanio para jumex
    //devuelve true si ya hay
    public boolean validarProdJum(String indice,int planta,Sustitucion productosel ){
        InformeComDetRepositoryImpl icrepo=new InformeComDetRepositoryImpl(context);

        List<InformeCompraDetalle> informeCompraDetalles=icrepo.getByProducto(indice,planta,productosel.getSu_producto(),productosel.getSu_tipoempaque(),productosel.getNomtamanio());

        if(informeCompraDetalles!=null&&informeCompraDetalles.size()>0)
            return true;
        return false;


    }

}