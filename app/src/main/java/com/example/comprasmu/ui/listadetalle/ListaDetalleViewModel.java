package com.example.comprasmu.ui.listadetalle;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.ListaCompraDao;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.ListaWithDetalle;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ListaDetalleViewModel extends AndroidViewModel {

    private final ListaCompraRepositoryImpl repository;
    private ListaCompraDetRepositoryImpl detRepo;

  //  private final LiveData<ListaWithDetalle> filter = new LiveData<ListaWithDetalle>("*");

    private  LiveData<List<ListaWithDetalle>> listas;
    private  LiveData<List<ListaCompraDetalle>> detallebu;
    private  LiveData<List<ListaCompra>> listaSelbu;
    private final MutableLiveData<Event<Integer>> mOpenListaCompraEvent = new MutableLiveData<>();
    private  LiveData<Integer> size;

    private  LiveData<Boolean> empty;
    private int idListaSel;
    private  ListaCompraDetalle detallebuSel;
    public ListaCompra listaSelec;
    private int clienteSel;
    private int plantaSel;
    public String nombrePlantaSel;
    public int ciudadSel;
    public String nombreCiudadSel;
    private static final String TAG=ListaDetalleViewModel.class.getCanonicalName();
    private boolean nuevaMuestra=false;  //indica si se agregará muestra
    Context context;


    public ListaDetalleViewModel(Application application) {
        super(application);
        ListaCompraDao dao=ComprasDataBase.getInstance(application).getListaCompraDao();
        repository = ListaCompraRepositoryImpl.getInstance(dao);
        detRepo=new ListaCompraDetRepositoryImpl(application);
        context=application;
    }


    public void cargarDetalles(){


       listas =repository.getListaWithDetalleByFiltros(Constantes.INDICEACTUAL,plantaSel,clienteSel);

        size = Transformations.map(listas,res->{ return listas.getValue().size();});
        empty = Transformations.map(listas, res->{return listas.getValue().isEmpty();});
    }

    public  LiveData<List<ListaCompra>>  cargarPestañas(String ciudadSel,int clienteSel){
        if(clienteSel>0){
            //ya elegi cliente vengo de muestra
            return repository.getAllByIndiceCiudadCliente(Constantes.INDICEACTUAL,ciudadSel,clienteSel);
        }else
        return repository.getAllByIndiceCiudad(Constantes.INDICEACTUAL,ciudadSel);


    }
    public  List<ListaCompra>  cargarPlantas(String ciudadSel,int clienteSel){

            return repository.getAllByIndiceCiudadClienteSim(Constantes.INDICEACTUAL,ciudadSel,clienteSel);


    }
    public  LiveData<List<ListaCompra>>  cargarClientes(String ciudadSel){

        return repository.getClientesByIndiceCiudad(Constantes.INDICEACTUAL,ciudadSel);


    }
    public  List<ListaCompra>  cargarClientesSimpl(String ciudadSel){

        return repository.getClientesByIndiceCiudadSimpl(Constantes.INDICEACTUAL,ciudadSel);


    }
    public List<DescripcionGenerica> cargarOpcionesAnalisis(int idanalisis){
        List<DescripcionGenerica> opciones=new ArrayList<>();
        opciones.add(new DescripcionGenerica(1,"Criterio 1"));
        opciones.add(new DescripcionGenerica(2,"Criterio 2"));

        switch (idanalisis){
            case 1: case 4: //fisico
                opciones.add(new DescripcionGenerica(3,"Criterio 3"));
                opciones.add(new DescripcionGenerica(4,"Criterio 4"));
                break;

            case 3: //torque
                opciones.add(new DescripcionGenerica(3,"Criterio 3"));
                break;
        }
        return  opciones;
    }

    //para las colsultas de bu
    public void consultasBackup(int idlista,int opcionsel,String categoria, String productoNombre, String empaque,String tamanio,int analisisid, String analisis,int iddetorig ){
      switch (analisisid){
          case 1: //fisico
                consultaFisico(idlista, opcionsel, categoria, productoNombre, empaque, analisis,iddetorig);
                break;
          case 2: //sensorial
              consultaSensorial(idlista, opcionsel, categoria, productoNombre, empaque, analisis,iddetorig);
              break;
          case 3: //torque
              consultaTorque(idlista, opcionsel, categoria, productoNombre, empaque, analisis,iddetorig);
              break;
          case 4: //micro
              consultaMicro(idlista, opcionsel, categoria, productoNombre, empaque, analisis,iddetorig);
              break;
      }

    }
    public void consultaFisico(int idlista,int opcionsel,String categoria, String productoNombre, String empaque, String analisis,int iddetorig ){
        switch (opcionsel) {
            case 1:
                detallebu = detRepo.getDetalleByFiltros(idlista, categoria, productoNombre, empaque, "", "",iddetorig);
                break;
            case 2:
                detallebu = detRepo.getDetalleByFiltros(idlista, categoria, productoNombre, "", "", "",iddetorig);
                break;
            case 3:
                detallebu = detRepo.getDetalleByFiltros(idlista, categoria,"" , "", "", "",iddetorig);
                break;
            case 4: default: //la misma lista
                detallebu = detRepo.getAllByLista(idlista);
                break;
        }



    }
    public void consultaSensorial(int idlista,int opcionsel,String categoria, String productoNombre, String empaque, String analisis,int iddetorig ){
        switch (opcionsel) {
            case 1:
                detallebu = detRepo.getDetalleByFiltros(idlista, categoria, productoNombre, empaque, "", "",iddetorig);
                break;
            case 2: default:
                detallebu = detRepo.getAllByLista(idlista);
                break;

        }



    }
    public void consultaTorque(int idlista,int opcionsel,String categoria, String productoNombre, String empaque, String analisis ,int iddetorig){
        switch (opcionsel) {
            case 1:
                detallebu = detRepo.getDetalleByFiltros(idlista, categoria, productoNombre, empaque, "", "",iddetorig);
                break;
            case 2:
                detallebu = detRepo.getDetalleByFiltros(idlista, categoria, "", empaque, "", "",iddetorig);
                break;

            case 3: default: //la misma lista
                detallebu = detRepo.getAllByLista(idlista);
                break;
        }



    }
    public void consultaMicro(int idlista,int opcionsel,String categoria, String productoNombre, String empaque, String analisis,int iddetorig ){
        switch (opcionsel) {
            case 1:
                detallebu = detRepo.getDetalleByFiltros(idlista, categoria, productoNombre, empaque, "", analisis,iddetorig);
                break;
            case 2:
                detallebu = detRepo.getDetalleByFiltros(idlista, categoria, productoNombre, "", "", analisis,iddetorig);
                break;
            case 3:
                detallebu = detRepo.getDetalleByFiltros(idlista, categoria, "", "", "", analisis,iddetorig);
                break;

            case 4: default: //la misma lista
                detallebu = detRepo.getAllByLista(idlista);
                break;
        }



    }

    public List<InformeCompraDetalle> tieneBackup(int idcompra,int iddetalle){
        InformeComDetRepositoryImpl icrepo=new InformeComDetRepositoryImpl(context);
        return icrepo.findByCompraBu(idcompra,iddetalle);
    }
    //actualiza comprados y codigos no permitidos
    public void comprarMuestraPepsi(int idlista,int idDetalle,String nuevoCodigo, String isbu){
        detRepo=new ListaCompraDetRepositoryImpl(context);
        Log.d(TAG,"qqqqqqqqqqqqqqq"+idlista+"--"+idDetalle);
        ListaCompraDetalle listaCompraDetalle=detRepo.findsimple(idlista,idDetalle);
        Log.d(TAG,"1seguimiento muestras "+listaCompraDetalle.getCantidad()+" "+listaCompraDetalle.getComprados());
        //valido que se pueda comprar y no sea bu
        if(listaCompraDetalle.getCantidad()>=listaCompraDetalle.getComprados()+1){
          //  detRepo.actualizarComprados(idDetalle,1);
            listaCompraDetalle.setComprados(listaCompraDetalle.getComprados()+1);

        }
        String listaCodigos="";
            //no aumento el comprado solo el codigo
        if(listaCompraDetalle.getNvoCodigo()!=null)
            //reviso que no exite
        {    if(!listaCompraDetalle.getNvoCodigo().contains(nuevoCodigo))
                listaCodigos=nuevoCodigo+";"+listaCompraDetalle.getNvoCodigo();}
        else
             listaCodigos=nuevoCodigo;
        listaCompraDetalle.setNvoCodigo(listaCodigos);
            //actualizo
        detRepo.insert(listaCompraDetalle);



      //  Log.d(TAG,"Se actualizo la lista de compras id="+idDetalle);
    }

    public String ordenarCodigosNoPermitidos(int numTienda,String nvoCodigos,String noPermitidos) {
        List<String> otodo= new ArrayList<String>();
        List<Date> fechas=new ArrayList<Date>();
        String resultado = "";
        Log.d(TAG,"xxxxx "+numTienda+"--"+nvoCodigos+"--"+noPermitidos);
        if(numTienda>=11){
            nvoCodigos="";
        }
    if(nvoCodigos!=null&&nvoCodigos.length()>0) {
        String auxnvo[] = nvoCodigos.split(";");

        if(auxnvo.length>0) {
            List<String> lnvo= Arrays.asList(auxnvo);
            otodo.addAll(lnvo);
           // otodo = Arrays.asList();
        }
        else
            otodo.add(nvoCodigos);
        if (noPermitidos != null && noPermitidos.length() > 0) {
            String auxno[] = noPermitidos.split(";");
            if(auxno.length>0) {
               List<String> lperm= Arrays.asList(auxno);
                otodo.addAll(lperm);
            }
            else
                otodo.add(noPermitidos);
        }
        Log.d(TAG,otodo.size()+"--"+ otodo);
        SimpleDateFormat sdfcaducidad = new SimpleDateFormat("dd-MM-yy");
        for (int i = 0; i < otodo.size(); i++) {
            Log.d(TAG, otodo.get(i));
            try {
                fechas.add(sdfcaducidad.parse(otodo.get(i)));
                Log.d(TAG, "<<" + fechas);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(fechas,new SortItems());

        for (int i = 0; i < fechas.size(); i++) {
            resultado = resultado + "\n" + sdfcaducidad.format(fechas.get(i));
        }
    }else //ya están ordenados los no permitidos
        resultado=noPermitidos.replace(";","\n");

        return  resultado;

    }

    public LiveData<List<ListaWithDetalle>> getListas() {
        return listas;
    }

    public MutableLiveData<Event<Integer>> getmOpenListaCompraEvent() {
        return mOpenListaCompraEvent;
    }

    public LiveData<Integer> getSize() {
        return size;
    }

    public LiveData<Boolean> getEmpty() {
        return empty;
    }

    public int getClienteSel() {
        return clienteSel;
    }

    public int getPlantaSel() {
        return plantaSel;
    }

    public void setClienteSel(int clienteSel) {
        this.clienteSel = clienteSel;
    }

    public boolean isNuevaMuestra() {
        return nuevaMuestra;
    }

    public void setNuevaMuestra(boolean nuevaMuestra) {
        this.nuevaMuestra = nuevaMuestra;
    }

    public void setPlantaSel(int plantaSel) {
        this.plantaSel = plantaSel;
    }

    public int getIdListaSel() {
        return idListaSel;
    }

    public void setIdListaSel(int idListaSel) {
        this.idListaSel = idListaSel;
    }

    public ListaCompraDetalle getDetallebuSel() {
        return detallebuSel;
    }

    public void setDetallebuSel(ListaCompraDetalle detallebuSel) {
        this.detallebuSel = detallebuSel;
    }

    public LiveData<List<ListaCompraDetalle>> getDetallebu() {
        return detallebu;
    }

    public void setDetallebu(LiveData<List<ListaCompraDetalle>> detallebu) {
        this.detallebu = detallebu;
    }

    class SortItems implements Comparator<Date> {
        // @Override

        // Method of this class
        // To compare datetime objects
        public int compare(Date a, Date b)
        {

            // Returning the value after comparing the objects
            // this will sort the data in Descending order
            return b.compareTo(a);
        }
    }
}
