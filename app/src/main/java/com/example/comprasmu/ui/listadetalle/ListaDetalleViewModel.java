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
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.ListaDetalleBu;
import com.example.comprasmu.data.modelos.ListaWithDetalle;
import com.example.comprasmu.data.repositories.CatalogoDetalleRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.ui.informedetalle.NuevoDetalleViewModel;
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

    private  LiveData<ListaCompra> listaCompra;
    private  LiveData<List<ListaCompraDetalle>> listas;
    private  LiveData<List<ListaCompraDetalle>> detallebu;

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
    public void cargarListaCompra()  {

        listaCompra = repository.getByFiltros(Constantes.INDICEACTUAL, plantaSel,clienteSel);

    }

    public void cargarDetalles(int idlista){
        listas =detRepo.getListaDetalleOrd(idlista);

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

    public  LiveData<List<ListaCompra>>  cargarClientePlanta(){

            return repository.getPlantas(Constantes.INDICEACTUAL);


    }
    public  LiveData<List<ListaCompra>>  cargarPestañasEta(String ciudadSel,int clienteSel){
        if(clienteSel>0){
            //ya elegi cliente vengo de muestra
            return repository.getAllByIndiceCiudadCliente(Constantes.INDICEACTUAL,ciudadSel,clienteSel);
        }else
            return repository.getAllByIndiceCiudadEta(Constantes.INDICEACTUAL,ciudadSel,Constantes.ETAPAACTUAL+"");


    }
    public  LiveData<List<ListaCompra>>  getCiudades(){

            return repository.getAllCdByIndice(Constantes.INDICEACTUAL);


    }
    public List<CatalogoDetalle> buscarTipoTienda(){
        CatalogoDetalleRepositoryImpl catrepo=new CatalogoDetalleRepositoryImpl(getApplication());
        return catrepo.getxCatalogo("tipo tienda");


    }
    public List<CatalogoDetalle>  buscarCadenaComer(){
        CatalogoDetalleRepositoryImpl catrepo=new CatalogoDetalleRepositoryImpl(getApplication());
       return catrepo.getxCatalogo("cadena_comercial");


    }

    public  List<ListaCompra>  cargarPestanasSimp(String ciudadSel){

            return repository.getAllByIndiceCiudadSimpl(Constantes.INDICEACTUAL,ciudadSel);


    }
    public  List<ListaCompra>  cargarPestanasxEtaSimp(String ciudadSel){
        Log.d(TAG,"etapa act"+Constantes.ETAPAACTUAL);
        return repository.getAllByIndiceCiudadEtaSimpl(Constantes.INDICEACTUAL,ciudadSel, Constantes.ETAPAACTUAL+"");


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

    public  List<ListaCompra>  cargarClientesSimplxet(String ciudadSel, int etapa){
        Log.d(TAG,"xxxx"+etapa);
        return repository.getClientesByIndiceCiudadSimplxet(Constantes.INDICEACTUAL,ciudadSel,etapa);


    }
    //trae la lsta menos el cliente enviado
    public  List<ListaCompra>  cargarClientesSimplsp(String ciudadSel, int cliente){

        return repository.getClientesByIndiceCiudadSimplsp(Constantes.INDICEACTUAL,ciudadSel,cliente);


    }
    public  LiveData<List<ListaCompra>>  cargarPlantas(String ciudadSel){

        return repository.getAllByIndiceCiudad(Constantes.INDICEACTUAL,ciudadSel);


    }

    public  int  getclientexPlanta(int planta){

        return repository.getClientexPlanta(Constantes.INDICEACTUAL,planta);
    }
    public List<DescripcionGenerica> cargarOpcionesAnalisis(int idanalisis){
        List<DescripcionGenerica> opciones=new ArrayList<>();
        opciones.add(new DescripcionGenerica(1,"Criterio 1"));
        opciones.add(new DescripcionGenerica(2,"Criterio 2"));

        switch (idanalisis){
            case 1: case 5: //fisico
                opciones.add(new DescripcionGenerica(3,"Criterio 3"));
                opciones.add(new DescripcionGenerica(4,"Criterio 4"));
                break;

            case 3: case 7://torque
                opciones.add(new DescripcionGenerica(3,"Criterio 3"));
                break;

        }
        return  opciones;
    }

    public int[] buscarClienCdxPlan(int planta){
        int[] devolver = new int[2];
        List<ListaCompra> res=repository.getByPlanta(planta);
        if(res!=null&&res.size()>0){
            devolver[0]=res.get(0).getClientesId();
            devolver[1]=res.get(0).getCiudadesId();

        }
        return devolver;
    }
    public String buscarClientexPlan(int planta){

        List<ListaCompra> res=repository.getByPlanta(planta);
        if(res!=null&&res.size()>0){
            return res.get(0).getClienteNombre();


        }
        return "";

    }

    //para las colsultas de bu
    public void consultasBackup(int idlista,int opcionsel,String categoria, String productoNombre, String empaque,int tamanio,int analisisid, String analisis,int iddetorig ){
      Log.d(TAG,"consuta bu params"+idlista+"--"+ opcionsel+"--"+ categoria+"--"+ productoNombre+"--"+ empaque+"--"+ analisis+"--"+tamanio+"--"+iddetorig+"--"+analisisid);
        switch (analisisid){
          case 1: case 5: //fisico
                consultaFisico(idlista, opcionsel, categoria, productoNombre, empaque, analisisid,tamanio,iddetorig);
                break;
          case 2: case 6: //sensorial
              consultaSensorial(idlista, opcionsel, categoria, productoNombre, empaque, analisisid,tamanio,iddetorig);
              break;
          case 3: case 7: //torque
              consultaTorque(idlista, opcionsel, categoria, productoNombre, empaque, analisisid,tamanio,iddetorig);
              break;
          case 4: case 8: //micro
              consultaMicro(idlista, opcionsel, categoria, productoNombre, empaque, analisisid,tamanio,iddetorig);

              break;
      }

    }
    public void consultaFisico(int idlista,int opcionsel,String categoria, String productoNombre, String empaque, int analisisid,int tamanio,int iddetorig ){
   //     Log.d(TAG,"criterio"+opcionsel);
        switch (opcionsel) {
            case 1:
                detallebu = detRepo.getDetalleByFiltrosUD(idlista,analisisid, categoria, productoNombre, empaque, tamanio);
                break;
            case 2:
                detallebu = detRepo.getDetalleByFiltrosUD(idlista,analisisid, categoria, productoNombre, empaque, 0);
                break;
            case 3:
                detallebu = detRepo.getDetalleByFiltrosUD(idlista, analisisid,categoria,productoNombre, "", 0);
                break;
            case 4: default: //la misma lista
               // detallebu = detRepo.getDetalleByFiltrosUD(idlista,categoria,"","",0);
                detallebu = detRepo.consultaFisico4(idlista,analisisid, categoria, productoNombre, empaque, tamanio,"",iddetorig);
               // detallebu = detRepo.getAllByLista(idlista);

                break;
        }



    }
    public void consultaSensorial(int idlista,int opcionsel,String categoria, String productoNombre, String empaque, int analisisid,int tamanio,int iddetorig ){
        switch (opcionsel) {
            case 1:
                detallebu = detRepo.getDetalleByFiltrosUD(idlista,analisisid, categoria, productoNombre, empaque, tamanio);
                break;
            case 2: default: //muestro toda la lista
                 detallebu = detRepo.getDetalleByFiltros(idlista,analisisid, categoria, productoNombre, empaque, tamanio,"",iddetorig);
              //  detallebu = detRepo.getAllByLista(idlista);
                break;

        }



    }
    public void consultaTorque(int idlista,int opcionsel,String categoria, String productoNombre, String empaque, int analisisid ,int tamanio,int iddetorig){
        switch (opcionsel) {
            case 1:
                detallebu = detRepo.getDetalleByFiltrosUD(idlista, analisisid,categoria, productoNombre, empaque, tamanio);
                break;
            case 2:
                detallebu = detRepo.consultaTorque2(idlista,analisisid, categoria, productoNombre, empaque);
                break;

            case 3: default:
              //  detallebu = detRepo.consultaTorque4(idlista, categoria, empaque);
                detallebu = detRepo.getDetalleByFiltros(idlista, analisisid,categoria, productoNombre, empaque, tamanio,"",iddetorig);

                //    detallebu = detRepo.getAllByLista(idlista);
                break;
        }



    }
    public void consultaMicro(int idlista,int opcionsel,String categoria, String productoNombre, String empaque, int analisis,int tamanio,int iddetorig ){
        switch (opcionsel) {
            case 1:
                detallebu = detRepo.getDetalleByFiltrosUDA2(idlista, analisis,categoria, analisis,productoNombre, "", 0);

               // detallebu = detRepo.getDetalleByFiltrosUDA(idlista, categoria, analisis,productoNombre, empaque, tamanio);
                break;
            case 2: default:
                detallebu = detRepo.getDetalleByFiltros(idlista,analisis, categoria, productoNombre, empaque, tamanio,analisis+"",0);

              //  detallebu = detRepo.getDetalleByFiltrosUDA(idlista, categoria, analisis,productoNombre, empaque,0);
                break;
       /*     case 3:
                detallebu = detRepo.getDetalleByFiltrosUDA(idlista, categoria, analisis,productoNombre, "", 0);
                break;

            case 4: default: //la misma lista
                detallebu = detRepo.getDetalleByFiltros(idlista, categoria, productoNombre, empaque, tamanio,analisis+"",0);

             //   detallebu = detRepo.getAllByLista(idlista);
                break;*/
        }

    }

    public List<InformeCompraDetalle> tieneBackup(int idcompra,int iddetalle){
        InformeComDetRepositoryImpl icrepo=new InformeComDetRepositoryImpl(context);
        return icrepo.findByCompraBu(idcompra,iddetalle);
    }
    //actualiza comprados y codigos no permitidos
    public int comprarMuestraPepsi(int idlista,int idDetalle,String nuevoCodigo, int isbu,int idlistabu,int iddetbu, int clienteSel){

        detRepo=new ListaCompraDetRepositoryImpl(context);

        ListaCompraDetalle listaCompraDetalle=detRepo.findsimple(idlista,idDetalle);

        //valido que se pueda comprar y no sea bu
        if(listaCompraDetalle.getCantidad()>=listaCompraDetalle.getComprados()+1){
          //  detRepo.actualizarComprados(idDetalle,1);
            listaCompraDetalle.setComprados(listaCompraDetalle.getComprados()+1);
        }
        String listaCodigos="";

        //no aumento el comprado solo el codigo
        if (listaCompraDetalle!=null&&listaCompraDetalle.getNvoCodigo() != null)//no es bu
            // reviso que no existe
            {
                if (!listaCompraDetalle.getNvoCodigo().contains(nuevoCodigo))
                    listaCodigos = nuevoCodigo + ";" + listaCompraDetalle.getNvoCodigo();
            } else
                listaCodigos = nuevoCodigo;
            listaCompraDetalle.setNvoCodigo(listaCodigos);
            int num=(int)detRepo.insert(listaCompraDetalle);

            //actualizo
        return num;


      //  Log.d(TAG,"Se actualizo la lista de compras id="+idDetalle);
    }

    public int comprarMuestraPen(int idlista, int idDetalle, String nuevoCodigo, int isbu, InformeCompraDetalle prodsel,int plantaSel,String indice){

        detRepo=new ListaCompraDetRepositoryImpl(context);

        ListaCompraDetalle listaCompraDetalle=detRepo.findsimple(idlista,idDetalle);

        //valido que se pueda comprar y no sea bu
        if(listaCompraDetalle.getCantidad()>=listaCompraDetalle.getComprados()+1){
            //  detRepo.actualizarComprados(idDetalle,1);
            listaCompraDetalle.setComprados(listaCompraDetalle.getComprados()+1);
        }
        int num=(int)detRepo.insert(listaCompraDetalle);
        String listaCodigos="";
        if(isbu==3){
            //busco la del reemplazo
            listaCompraDetalle=detRepo.getByProductoAna(prodsel.getProductoId(),prodsel.getEmpaquesId(),prodsel.getTamanioId(),prodsel.getTipoAnalisis(),indice,plantaSel);
        }
        //no aumento el comprado solo el codigo
        if (listaCompraDetalle!=null&&listaCompraDetalle.getNvoCodigo() != null)//no es bu
        //reviso que no existe
        {
            if (!listaCompraDetalle.getNvoCodigo().contains(nuevoCodigo))
                listaCodigos = nuevoCodigo + ";" + listaCompraDetalle.getNvoCodigo();
        } else
            listaCodigos = nuevoCodigo;
        if (listaCompraDetalle!=null) {
            listaCompraDetalle.setNvoCodigo(listaCodigos);
            num = (int) detRepo.insert(listaCompraDetalle);
        }
        //actualizo
        return num;
        //  Log.d(TAG,"Se actualizo la lista de compras id="+idDetalle);
    }
   // List<InformeCompraDetalle> listacomprasbu;

  /*  public void setListacomprasbu(List<InformeCompraDetalle> listacomprasbu) {
        this.listacomprasbu = listacomprasbu;
    }*/

   /* public InformeCompraDetalle buscarBU(ListaCompraDetalle det){
        Log.d(TAG, "listacomprabu " + listacomprasbu.size());


        for(InformeCompraDetalle icd: listacomprasbu) {
            Log.d(TAG, "--------------Se seleccionó a " + det.getListaId() + "--" + det.getId() + "--" + icd.getComprasId() + "--" + icd.getComprasDetId());


            if (icd.getComprasId() == det.getListaId() && icd.getComprasDetId() ==det.getId() )
            {
                Log.d(TAG, "2--------------Se seleccionó a " +icd.getComprasId()+ "--" + det.getId()+"--"+ det.getListaId() +"--"+icd.getComprasDetId());

                return icd;
            }
        }
        return null;

    }*/
    public String ordenarCodigosNoPermitidos(int numTienda, String nvoCodigos, String noPermitidos, int criterio, int analisis, ListaDetalleBu detalle,int plantasel) {
        SimpleDateFormat sdfcodigo= new SimpleDateFormat("dd-MM-yy");
        List<String> otodo= new ArrayList<String>();
        List<Date> fechas=new ArrayList<Date>();
        String resultado = "";

       // Log.d(TAG,"yyy  "+clienteSel+"--"+criterio+"--"+analisis);
        InformeComDetRepositoryImpl icrepo=new InformeComDetRepositoryImpl(context);
        nvoCodigos = "";
        if(clienteSel==4)
        {   if(criterio>0){
                nvoCodigos = "";

                }
                else{
                    //busco los nuevos codigos
                    List<InformeCompraDetalle> informeCompraDetalles=icrepo.getByProductoAna(Constantes.INDICEACTUAL,plantasel,detalle.getProductosId(),detalle.getAnalisisId(),detalle.getEmpaquesId(),detalle.getTamanio());
                    for(InformeCompraDetalle info:informeCompraDetalles){
                        nvoCodigos=nvoCodigos+sdfcodigo.format(info.getCaducidad())+";";
                    }
                }
        }else{
                //busco los nuevos codigos
                List<InformeCompraDetalle> informeCompraDetalles=icrepo.getByProductoAna(Constantes.INDICEACTUAL,plantasel,detalle.getProductosId(),detalle.getAnalisisId(),detalle.getEmpaquesId(),detalle.getTamanio());
                for(InformeCompraDetalle info:informeCompraDetalles){

                    nvoCodigos=nvoCodigos+ sdfcodigo.format(info.getCaducidad())+";";
                }
            }
      //  Log.d(TAG,"xxxxx "+numTienda+"--"+nvoCodigos+"--"+noPermitidos);
    if(nvoCodigos!=null&&nvoCodigos.length()>0) {
        String[] auxnvo = nvoCodigos.split(";");

        if(auxnvo.length>0) {
            List<String> lnvo= Arrays.asList(auxnvo);
            otodo.addAll(lnvo);
           // otodo = Arrays.asList();
        }
        else
            otodo.add(nvoCodigos);
        if (noPermitidos != null && noPermitidos.length() > 0) {
            String[] auxno = noPermitidos.split(";");
            if(auxno.length>0) {
               List<String> lperm= Arrays.asList(auxno);
                otodo.addAll(lperm);
            }
            else
                otodo.add(noPermitidos);
        }
       // Log.d(TAG,otodo.size()+"--"+ otodo);
        SimpleDateFormat sdfcaducidad = new SimpleDateFormat("dd-MM-yy");
        for (int i = 0; i < otodo.size(); i++) {

            try {
                if(fechas.contains(sdfcaducidad.parse(otodo.get(i)))){
                    continue; //para no meter duplicados
                }
                fechas.add(sdfcaducidad.parse(otodo.get(i)));

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

    public LiveData<List<ListaCompraDetalle>> getListas() {
        return listas;
    }

    public LiveData<ListaCompra> getListaCompra() {
        return listaCompra;
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
