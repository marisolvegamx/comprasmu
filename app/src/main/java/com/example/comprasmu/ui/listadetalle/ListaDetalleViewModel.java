package com.example.comprasmu.ui.listadetalle;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.example.comprasmu.DescargaListaCompraAuto;
import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.dao.HistoricoMuestrasDao;
import com.example.comprasmu.data.dao.ListaCompraDao;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.Geocerca;
import com.example.comprasmu.data.modelos.HistoricoMuestras;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.ListaDetalleBu;
import com.example.comprasmu.data.modelos.Tienda;
import com.example.comprasmu.data.modelos.TiendaJson;
import com.example.comprasmu.data.remote.ListaCompraResponse;
import com.example.comprasmu.data.repositories.CatalogoDetalleRepositoryImpl;
import com.example.comprasmu.data.repositories.GeocercaRepositoryImpl;
import com.example.comprasmu.data.repositories.HistoricoMuestrasRepositoryImpl;
import com.example.comprasmu.data.repositories.InfEtapaRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;
import com.example.comprasmu.data.repositories.TiendaEstatusClienteRepositoryImpl;
import com.example.comprasmu.data.repositories.TiendaRepositoryImpl;
import com.example.comprasmu.services.DescargaHistoricoMuestras;
import com.example.comprasmu.ui.informedetalle.NuevoDetalleViewModel;
import com.example.comprasmu.ui.tiendas.PeticionMapaCd;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ListaDetalleViewModel extends AndroidViewModel {

    private final ListaCompraRepositoryImpl repository;
    private ListaCompraDetRepositoryImpl detRepo;

    private LiveData<ListaCompra> listaCompra;
    private LiveData<List<ListaCompraDetalle>> listas;
    private LiveData<List<ListaCompraDetalle>> detallebu;

    private final MutableLiveData<Event<Integer>> mOpenListaCompraEvent = new MutableLiveData<>();
    private LiveData<Integer> size;

    private LiveData<Boolean> empty;
    private int idListaSel;
    private ListaCompraDetalle detallebuSel;
    public ListaCompra listaSelec;
    private int clienteSel;
    private int plantaSel;
    public String nombrePlantaSel;
    public int ciudadSel;
    public String nombreCiudadSel;
    private static final String TAG = ListaDetalleViewModel.class.getCanonicalName();
    private boolean nuevaMuestra = false;  //indica si se agregará muestra
    Context context;
    private final InfEtapaRepositoryImpl infEtaRepository;

    public ListaDetalleViewModel(Application application) {
        super(application);
        this.infEtaRepository = new InfEtapaRepositoryImpl(application);
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
    public  LiveData<List<ListaCompra>>  cargarPestañasEta(String ciudadSel){

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

        return repository.getAllByIndiceCiudadEtaSimpl(Constantes.INDICEACTUAL,ciudadSel, Constantes.ETAPAACTUAL+"");


    }
    public  List<ListaCompra>  getAllByIndiceCiudadClienteSim(String ciudadSel,int clienteSel){

            return repository.getAllByIndiceCiudadClienteSim(Constantes.INDICEACTUAL,ciudadSel,clienteSel);


    }
    public  LiveData<List<ListaCompra>>  cargarClientes(String ciudadSel){

        return repository.getClientesByIndiceCiudad(Constantes.INDICEACTUAL,ciudadSel);


    }
    public  List<ListaCompra>  cargarClientesSimpl(String ciudadSel){

        return repository.getClientesByIndiceCiudadSimpl(Constantes.INDICEACTUAL,ciudadSel);


    }

    public  List<ListaCompra>  cargarClientesSimplxet(String ciudadSel, int etapa){
       // Log.d(TAG,"xxxx"+etapa);
        return repository.getClientesByIndiceCiudadSimplxet(Constantes.INDICEACTUAL,ciudadSel,etapa);


    }
    public  List<ListaCompra>  getTodosCliByIndiceCdSimplxet(String ciudadSel, int etapa){
      //  Log.d(TAG,"xxxx"+etapa);
        return repository.getTodosCliByIndiceCdSimplxet(Constantes.INDICEACTUAL,ciudadSel,etapa);


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
        //sensorial solo tiene 2 criterios
        switch (idanalisis){
            case 1:  case 4: case 8: case 5: //fisico
                opciones.add(new DescripcionGenerica(3,"Criterio 3"));
                opciones.add(new DescripcionGenerica(4,"Criterio 4"));
                break;

            case 3: case 7://torque
                opciones.add(new DescripcionGenerica(3,"Criterio 3"));
                break;

        }
        return  opciones;
    }

    public int[] buscarClienCdxPlan(int planta,String indice){
        int[] devolver = new int[2];
        List<ListaCompra> res=repository.getByPlanta(planta, indice);
        if(res!=null&&res.size()>0){
            devolver[0]=res.get(0).getClientesId();
            devolver[1]=res.get(0).getCiudadesId();

        }
        return devolver;
    }
    public String buscarClientexPlan(int planta, String indice){

        List<ListaCompra> res=repository.getByPlanta(planta, indice);
        if(res!=null&&res.size()>0){
            return res.get(0).getClienteNombre();
        }
        return "";

    }

    //para las colsultas de bu
    public void consultasBackup(int idlista,int opcionsel,String categoria, String productoNombre, String empaque,int tamanio,int analisisid, String analisis,int iddetorig ){
      Log.i(TAG,"consulta bu params"+idlista+"--"+ opcionsel+"--"+ categoria+"--"+ productoNombre+"--"+ empaque+"--"+ analisis+"--"+tamanio+"--"+iddetorig+"--"+analisisid);
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
                detallebu = detRepo.consultaFisico4(idlista,analisisid, categoria, productoNombre, empaque, tamanio);
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
                 detallebu = detRepo.getDetalleByFiltros(idlista, analisisid,categoria, productoNombre, empaque, tamanio,"",iddetorig);

                break;
        }


    }
    public void consultaMicro(int idlista,int opcionsel,String categoria, String productoNombre, String empaque, int analisis,int tamanio,int iddetorig ){
        switch (opcionsel) {
            case 1: //mismo analisis, producto, empaque y diferente tamaño
                detallebu = detRepo.getDetalleByFiltrosUDA2(idlista, analisis,categoria, analisis,productoNombre, empaque, tamanio);
                break;
            case 2: //mismo analisis, producto,  y diferente empaque por eso tamaño va en 0
                detallebu = detRepo.getDetalleByFiltrosUDA2(idlista, analisis,categoria, analisis,productoNombre, empaque, 0);
                break;
            case 3: //mismo analisis y misma categoria
                detallebu = detRepo.getDetalleByFiltrosUDA2(idlista, analisis,categoria, analisis,productoNombre, "", 0);
                break;
            case 4: default: //el mismo producto
                detallebu = detRepo.getDetalleByFiltros(idlista,analisis, categoria, productoNombre, empaque, tamanio,analisis+"",0);
                break;
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

    }
    //public String ordenarCodigosNoPermitidos(int numTienda, String nvoCodigos, String noPermitidos, int criterio, int analisis, ListaDetalleBu detalle,int plantasel) {

        public String ordenarCodigosNoPermitidos( String noPermitidos, int criterio,  ListaDetalleBu detalle,int plantasel) {
        SimpleDateFormat sdfcodigo= new SimpleDateFormat("dd-MM-yy");
        List<String> otodo= new ArrayList<String>();
        List<Date> fechas=new ArrayList<Date>();
        String resultado = "";
       // Log.d(TAG,"yyy  "+clienteSel+"--"+criterio+"--"+analisis);
        InformeComDetRepositoryImpl icrepo=new InformeComDetRepositoryImpl(context);
        String nvoCodigos = "";
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


    public String buscarCodigosComprados(int clienteId,String indice, int planta, NuevoDetalleViewModel.ProductoSel productosel){
        InformeComDetRepositoryImpl informeCompraDetRepository=new InformeComDetRepositoryImpl(this.context);
        List<InformeCompraDetalle> informeCompraDetalles;
        if(clienteId==5) //peñafiel busca la sigla
            informeCompraDetalles=informeCompraDetRepository.getByProductoAnaPen(indice,planta,productosel.productoid,productosel.tipoAnalisis,productosel.idempaque,productosel.presentacion, productosel.siglas);
        else
            informeCompraDetalles=informeCompraDetRepository.getByProductoAna(indice,planta,productosel.productoid,productosel.tipoAnalisis,productosel.idempaque,productosel.presentacion);
        String codigosnuevos="";
        for(InformeCompraDetalle det:informeCompraDetalles) {
            Log.d(TAG, "buscando codigo igual" + det.getCaducidad());
            //recorro el informe buscando
            codigosnuevos=codigosnuevos+";"+det.getCaducidad();
        }
        return codigosnuevos;
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
    //para buscar si hay un inf
    public List<InformeEtapa> getInfGastoxCiudad(String indiceSel, String ciudad){

        return infEtaRepository.getInfxEstatusCiuSim(indiceSel,6,2,ciudad);

    }

   public void actualizarEstatusGas(int idInf,int  estatus){
        infEtaRepository.actualizarEstatus(idInf,estatus);
    }

    //para buscar si hay un inf
    public List<InformeEtapa> getInfGasxCiudad(String indiceSel, String ciudad){

        return infEtaRepository.getInfGasxCiudad(indiceSel,6,ciudad);

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

    public LiveData<ListaCompraResponse> actualizarListaCompra(ComprasLog compraslog){
        TablaVersionesRepImpl tvRepo=new TablaVersionesRepImpl(context);
        InformeComDetRepositoryImpl informeCompraRepository=new InformeComDetRepositoryImpl(context);
        DescargaListaCompraAuto descargaListaCompraAuto=new DescargaListaCompraAuto(compraslog,repository,detRepo,informeCompraRepository);
        PeticionesServidor peticionesServidor=new PeticionesServidor(Constantes.CLAVEUSUARIO);
        PeticionesServidor.PeticionLista peticionLista=peticionesServidor.crearPeticion(null, null,Constantes.INDICEACTUAL);
        return peticionesServidor.pedirListaCompraxCiudad(peticionLista,Constantes.CIUDADTRABAJO, descargaListaCompraAuto);

    }

    public LiveData<Boolean> actualizarHistoricoMuestras(String dirLog,LifecycleOwner lifecycleOwner,int plantaId){
        HistoricoMuestrasDao historicoMuestrasDao=ComprasDataBase.getInstance(context).getHistoricoMuestrasDao();
        HistoricoMuestrasRepositoryImpl historicoMuestraRepo=HistoricoMuestrasRepositoryImpl.getInstance(historicoMuestrasDao);
        PeticionesServidor peticionesServidor=new PeticionesServidor(Constantes.CLAVEUSUARIO);

        DescargaHistoricoMuestras descargaHistoricoMuestras=new DescargaHistoricoMuestras( dirLog,  historicoMuestraRepo, Constantes.CLAVEUSUARIO,  Constantes.INDICEACTUAL,
                peticionesServidor,  lifecycleOwner);
        return descargaHistoricoMuestras.ejecutar(plantaId);
    }
    public LiveData<List<Tienda>> getTiendas(String ciudad, int periodo, int tipo, int cadena, int cliente) {
        TiendaRepositoryImpl tiendaRepository = TiendaRepositoryImpl.getInstance(ComprasDataBase.getInstance(context).getTiendaDao());

        return tiendaRepository.gettiendasByFiltros(ciudad, periodo, tipo, cadena, cliente);

    }
    public List<Tienda> getTiendasSimp(String ciudad, int periodo, int tipo, int cadena, int cliente) {
        TiendaRepositoryImpl tiendaRepository = TiendaRepositoryImpl.getInstance(ComprasDataBase.getInstance(context).getTiendaDao());

        return tiendaRepository.gettiendasByFiltrosSimp(ciudad);

    }


    public MutableLiveData<Boolean> descargarTiendas(String ciudad, String ffin, LifecycleOwner lifeCycleOwner){
        //veo si hay algo en la tabla
        MutableLiveData<Boolean> finProceso=new MutableLiveData<>();
        TiendaRepositoryImpl tiendaRepository = TiendaRepositoryImpl.getInstance(ComprasDataBase.getInstance(context).getTiendaDao());
        TiendaEstatusClienteRepositoryImpl tiendaEstatusRepository=TiendaEstatusClienteRepositoryImpl.getInstance(ComprasDataBase.getInstance(context).getTiendaEstatusClienteDao());
        List<Tienda> tiendas=tiendaRepository.getByCiudad(ciudad);
        if(tiendas==null||tiendas.size()<1) {
            PeticionMapaCd peticionmap = new PeticionMapaCd(Constantes.CLAVEUSUARIO);
            peticionmap.getTiendas("0", ciudad,   ffin); //se agregarian filtros despues
            peticionmap.getListatiendas().observe(lifeCycleOwner, new Observer<List<TiendaJson>>() {
                @Override
                public void onChanged(List<TiendaJson> tiendas) {
                    //guardo en la tabla local
                    if(tiendas!=null&& tiendas.size()>0)
                        for (TiendaJson tienda: tiendas
                             ) {
                            tiendaRepository.insert(tienda.crearTienda());
                            if(tienda.getTiendaEstatusCliente()!=null&&tienda.getTiendaEstatusCliente().size()>0){
                                tiendaEstatusRepository.insertAll(tienda.getTiendaEstatusCliente());
                            }
                        }
                    finProceso.setValue(true);
                }
            });
        }
        else{
            finProceso.setValue(true);
        }
        return finProceso;
    }

    public List<Geocerca> getGeocercas(String ciudad) {
        GeocercaRepositoryImpl geocercaRepository = GeocercaRepositoryImpl.getInstance(ComprasDataBase.getInstance(context).getGeocercaDao());
        return geocercaRepository.findsimplexCd(ciudad);

    }

    //aqui guardo los backups
    public List<ListaDetalleBu> pasarADetalleBU(List<ListaCompraDetalle> listalcd, HistoricoMuestrasDao historicoMuestrasDao, int plantaId){
        HistoricoMuestrasRepositoryImpl historicoMuestrasRepository=HistoricoMuestrasRepositoryImpl.getInstance(historicoMuestrasDao);
        List<ListaDetalleBu> listanueva=new ArrayList<>();
        String indice1="",indice2="";
        //calcular indices pasados
        try {

            indice1= ComprasUtils.restarIndice(Constantes.INDICEACTUAL,1);
            indice2= ComprasUtils.restarIndice(Constantes.INDICEACTUAL,2);
            String codigosNoPermitidos="";
            for (ListaCompraDetalle lcdo:listalcd
            ) {

                ListaDetalleBu nuevaitem= new ListaDetalleBu(lcdo);
                Log.d(TAG,"nuevaitem codigo>"+nuevaitem.getCodigosNoPermitidos());
                //aqui reviso si tiene codigos, si no los busco en el historico
                if(nuevaitem.getCodigosNoPermitidos().equals("")){
                    //busco
                    List<HistoricoMuestras> listaHistorico=historicoMuestrasRepository.getDetalleByFiltros(plantaId,nuevaitem.getAnalisisId(),nuevaitem.getProductoNombre(),nuevaitem.getEmpaque(),nuevaitem.getTamanioId(),indice1, indice2);
                    if(listaHistorico!=null&&listaHistorico.size()>0){
                        for (HistoricoMuestras muestrapasada: listaHistorico
                             ) {
                            codigosNoPermitidos=codigosNoPermitidos+";"+Constantes.sdfcaducidad.format(muestrapasada.getCaducidad());
                        }
                        nuevaitem.setCodigosNoPermitidos(codigosNoPermitidos);
                    }
                }
                listanueva.add(nuevaitem);
            }
        } catch (Exception e) {
            Log.e(TAG,"pasarADetalleBU Error al calcular los indices");
        }
        return listanueva;
    }
}