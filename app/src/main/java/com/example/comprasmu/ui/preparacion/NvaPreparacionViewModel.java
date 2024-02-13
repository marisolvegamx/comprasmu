package com.example.comprasmu.ui.preparacion;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.comprasmu.EtiquetadoxCliente;
import com.example.comprasmu.data.modelos.DetalleCaja;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.modelos.InformeTemp;
import com.example.comprasmu.data.modelos.Reactivo;
import com.example.comprasmu.data.remote.InformeEtapaEnv;
import com.example.comprasmu.data.repositories.DetalleCajaRepoImpl;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InfEtapaDetRepoImpl;
import com.example.comprasmu.data.repositories.InfEtapaRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ReactivoRepositoryImpl;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//para preparacion, etiquetado y empaque
public class NvaPreparacionViewModel extends AndroidViewModel {
    private final InfEtapaRepositoryImpl infEtaRepository;
    private final InfEtapaDetRepoImpl infDetRepo;
    private final DetalleCajaRepoImpl cajaRepo;
    private int idNuevo;
    private int iddetalle;
    private InformeEtapa nvoinforme;
    private final MutableLiveData<Event<Integer>> mSnackbarText = new MutableLiveData<>();
    final String TAG="NvaPrepVM";
    Application application;
    public boolean variasClientes;//indica si tengo varias plantas
   public int preguntaAct;
    InformeEtapa informeEtiq;
    private final ReactivoRepositoryImpl reacRepo;
    public EtiquetadoxCliente cajaAct; //para saber el numero de caja en que estoy
    public int numMuestras; //saber total muestras
    public float altoCaja, anchoCaja,largoCaja,pesoCaja; //para el detalle de la caja
    private List<Reactivo> listaPreguntas;
    private final ImagenDetRepositoryImpl imagenDetRepository;
    ComprasLog compraslog;
    InformeComDetRepositoryImpl compRepo;
    public int totCajasEmp;
    public List<EtiquetadoxCliente> resumenEtiq; //es uno x indice
    public List<Integer> muestrasactEtiq; //para guardar las muestras que se actualizaron y se enviaran al serv
    public NvaPreparacionViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        this.infEtaRepository = new InfEtapaRepositoryImpl(application);
        this.infDetRepo = new InfEtapaDetRepoImpl(application);
        cajaRepo=new DetalleCajaRepoImpl(application);
        this.reacRepo=new ReactivoRepositoryImpl(application);
        this.imagenDetRepository=new ImagenDetRepositoryImpl(application);
        compraslog=ComprasLog.getSingleton();
        this.compRepo=new InformeComDetRepositoryImpl(application);
        this.cajaAct=new EtiquetadoxCliente();
        muestrasactEtiq=new ArrayList<>();
    }
    //es para la preparacion
    public int insertarInformeEtapa(String indice,String plantaNombre,int plantaId, String clienteNombre,int clienteId){
        InformeEtapa informe=new InformeEtapa();
        informe.setIndice(indice);
        informe.setPlantaNombre(plantaNombre);
        informe.setPlantasId(plantaId);
        informe.setClientesId(clienteId);
        informe.setClienteNombre(clienteNombre);
        informe.setEstatusSync(0);
        informe.setEstatus(1);
        informe.setEtapa(1);
        informe.setCreatedAt(new Date());
        idNuevo=(int)infEtaRepository.insert(informe);
        informe.setId(idNuevo);
        this.nvoinforme=informe;
        return idNuevo;

    }
    public int insertarInformeEtapae(String indice,String plantaNombre,int plantaId, String clienteNombre,int clienteId, int etapa){
        InformeEtapa informe=new InformeEtapa();
        informe.setIndice(indice);
        informe.setPlantaNombre(plantaNombre);
        informe.setPlantasId(plantaId);
        informe.setClientesId(clienteId);
        informe.setClienteNombre(clienteNombre);
        informe.setEstatusSync(0);
        informe.setEstatus(1);
        informe.setEtapa(etapa);
        informe.setCreatedAt(new Date());
        idNuevo=(int)infEtaRepository.insert(informe);
        informe.setId(idNuevo);
        this.nvoinforme=informe;
        return idNuevo;

    }
    public int insertarInformeEmp(String indice,int etapa,int clienteId,String clienteNombre,String ciudadNombre){
        InformeEtapa informe=new InformeEtapa();
        informe.setIndice(indice);
        informe.setClientesId(clienteId);
        informe.setClienteNombre(clienteNombre);
        informe.setEstatusSync(0);
        informe.setEstatus(1);
        informe.setEtapa(etapa);
        informe.setTotal_muestras(this.numMuestras);
        informe.setTotal_cajas(this.totCajasEmp);
        informe.setCreatedAt(new Date());
        informe.setCiudadNombre(ciudadNombre);
        idNuevo=(int)infEtaRepository.insert(informe);
        informe.setId(idNuevo);
        this.nvoinforme=informe;
        return idNuevo;

    }
    public int insertarInfEtaDet(int idinf,int descripcionid,String descripcion, String ruta ,int iddet){

        InformeEtapaDet detalle=new InformeEtapaDet();
        detalle.setDescripcion(descripcion);
        detalle.setInformeEtapaId(idinf);
        detalle.setRuta_foto(ruta);
        detalle.setDescripcionId(descripcionid);
        detalle.setEstatusSync(0);
        detalle.setEtapa(1);
        if(iddet>0)
        detalle.setId(iddet);
        iddetalle=(int)infDetRepo.insert(detalle);
        return iddetalle;
    }

    public int  actualizarImagenDet(int idfoto,String descripcion, String ruta, String indice ){
        ImagenDetalle imagen = new ImagenDetalle();
        //    this.ticket_compra.setIndice(Constantes.INDICEACTUAL);
        imagen.setRuta(ruta);
        imagen.setId(idfoto);
        imagen.setDescripcion(descripcion);
        imagen.setEstatus(1);
        imagen.setIndice(indice);
        imagen.setEstatusSync(0);
        imagen.setCreatedAt(new Date());
        return (int)imagenDetRepository.insert(imagen);
    }

    public int insertarInfEtaDetIm(int idinf,int descripcionid,String descripcion, String ruta ,int iddet,String indice){
        ImagenDetalle foto=new ImagenDetalle();
        foto.setRuta( ruta);
        foto.setDescripcion(descripcion);
        foto.setEstatus(1);
        foto.setEstatusSync(0);
        foto.setIndice(indice);
        foto.setCreatedAt(new Date());
        int nvoidimagem =(int)imagenDetRepository.insertImg(foto);
        InformeEtapaDet detalle=new InformeEtapaDet();
        detalle.setDescripcion(descripcion);
        detalle.setInformeEtapaId(idinf);
        detalle.setRuta_foto(nvoidimagem+"");
        detalle.setDescripcionId(descripcionid);
        detalle.setEstatusSync(0);
        detalle.setEtapa(1);
        if(iddet>0)
            detalle.setId(iddet);
        iddetalle=(int)infDetRepo.insert(detalle);
        return iddetalle;
    }




    public int insertarEtiq(String indice, String clienteNombre,int clienteId,int num_cajas, int tot_muestras,String ciudadNombre){
        InformeEtapa informe=new InformeEtapa();
        informe.setIndice(indice);
        informe.setClientesId(clienteId);
        informe.setClienteNombre(clienteNombre);
        informe.setEstatusSync(0);
        informe.setEstatus(1);
        informe.setEtapa(3);
        informe.setTotal_cajas(num_cajas);
        informe.setTotal_muestras(tot_muestras);
        informe.setCreatedAt(new Date());
        informe.setCiudadNombre(ciudadNombre);
        idNuevo=(int)infEtaRepository.insert(informe);
        informe.setId(idNuevo);
        this.nvoinforme=informe;
        return idNuevo;

    }
    public int insertarEtiqDet(int idinf,int descripcionid,String descripcion, String ruta ,int iddet, int numcaja,String qr,int num_muestra,String indice){
        ImagenDetalle foto=new ImagenDetalle();
        foto.setRuta( ruta);
        foto.setDescripcion(descripcion);
        foto.setEstatus(1);
        foto.setEstatusSync(0);
        foto.setIndice(indice);
        foto.setCreatedAt(new Date());
        int nvoidimagem =(int)imagenDetRepository.insertImg(foto);
        InformeEtapaDet detalle=new InformeEtapaDet();
        detalle.setDescripcion(descripcion);
        detalle.setInformeEtapaId(idinf);
        detalle.setRuta_foto(nvoidimagem+"");
        detalle.setDescripcionId(descripcionid);
        detalle.setNum_caja(numcaja);
        detalle.setQr(qr);
        detalle.setNum_muestra(num_muestra);
        detalle.setEstatusSync(0);
        detalle.setEtapa(3);
        if(iddet>0)
            detalle.setId(iddet);
        iddetalle=(int)infDetRepo.insert(detalle);
        return iddetalle;
    }

    public int actualizarEtiqDet(int idinf,int descripcionid,String descripcion, String ruta ,int iddet, int numcaja,String qr,int num_muestra,String idfoto,String indice){
        //busco la imagen detalle
        int numfoto=0;
        try {
             numfoto =Integer.parseInt(idfoto);
        }catch (NumberFormatException ex){
            Log.d(TAG,"actualizarEtiqDet NumberFormatException");
        }
        ImagenDetalle  foto=getFoto(numfoto);
        foto.setRuta( ruta);
        foto.setDescripcion(descripcion);
        foto.setEstatus(1);
        foto.setEstatusSync(0);
        foto.setIndice(indice);
        foto.setCreatedAt(new Date());
        int nvoidimagem =(int)imagenDetRepository.insertImg(foto);
        InformeEtapaDet detalle=new InformeEtapaDet();
        detalle.setDescripcion(descripcion);
        detalle.setInformeEtapaId(idinf);
        detalle.setRuta_foto(nvoidimagem+"");
        detalle.setDescripcionId(descripcionid);
        detalle.setNum_caja(numcaja);
        detalle.setQr(qr);
        detalle.setNum_muestra(num_muestra);
        detalle.setEstatusSync(0);
        detalle.setEtapa(3);

        detalle.setId(iddet);
        iddetalle=(int)infDetRepo.insert(detalle);
        return iddetalle;
    }

    public int insertarEmpDet(int idinf,int descripcionid,String descripcion, String ruta ,int iddet, int numcaja,String indice, int totmuestras,String descfoto){

        ImagenDetalle foto=new ImagenDetalle();
        foto.setRuta( ruta);
        foto.setDescripcion(descfoto);
        foto.setEstatus(1);
        foto.setEstatusSync(0);
        foto.setIndice(indice);
        foto.setCreatedAt(new Date());
        int nvoidimagem =(int)imagenDetRepository.insertImg(foto);
        InformeEtapaDet detalle=new InformeEtapaDet();
        detalle.setDescripcion(descripcion);
        detalle.setInformeEtapaId(idinf);
        detalle.setRuta_foto(nvoidimagem+"");
        detalle.setDescripcionId(descripcionid);
        detalle.setNum_caja(numcaja);
        detalle.setNum_muestra(totmuestras);

        detalle.setEstatusSync(0);
        detalle.setEtapa(4);
        if(iddet>0)
            detalle.setId(iddet);
        iddetalle=(int)infDetRepo.insert(detalle);
        return iddetalle;
    }
    public int insertarDetCaja(int idinf,int iddet,int numcaja ){
        DetalleCaja detalle=new DetalleCaja();
        detalle.setNum_caja(numcaja);
        detalle.setInformeEtapaId(idinf);


        detalle.setEstatusSync(0);

        if(iddet>0)
            detalle.setId(iddet);
        iddetalle=(int)cajaRepo.insert(detalle);
        return iddetalle;
    }
    public void actDetCaja(int idinf,int iddet,int numcaja,String ancho,String alto,String peso,String largo ){
        DetalleCaja detalle=new DetalleCaja();
        detalle.setNum_caja(numcaja);
        detalle.setInformeEtapaId(idinf);
        if(alto!=null&&!alto.equals(""))
        detalle.setAlto(alto);
        if(ancho!=null&&!ancho.equals(""))
        detalle.setAncho(ancho);
        if(peso!=null&&!peso.equals(""))
        detalle.setPeso(peso);
        if(largo!=null&&!largo.equals(""))
        detalle.setLargo(largo);

        detalle.setEstatusSync(0);
        detalle.setId(iddet);
        cajaRepo.actualizar(detalle);

    }


    public int actualizarInfEtaDet(InformeEtapaDet detalle){
        iddetalle=(int)infDetRepo.insert(detalle);
        return iddetalle;
    }
    public LiveData<InformeEtapa> getInformeEdit(int id){
     return   infEtaRepository.getInformeEtapa(id);
    }

    public InformeEtapa getInformexId(int id){
        return   infEtaRepository.findsimple(id);
    }
    public InformeEtapa getInformePend(String indice){
        return   infEtaRepository.getInformePend(indice, 1);
    }
    public InformeEtapa getInformePend(String indice, int etapa){
        return   infEtaRepository.getInformePend(indice, etapa);
    }
    public LiveData<InformeEtapaDet> getDetalleEtEdit(int idinf, int preguntaAct){

        return infDetRepo.getByDescripcion("foto_preparacion"+preguntaAct,idinf);
    }

    public InformeEtapaDet getDetalleEta(int iddet){

        return infDetRepo.findsimple(iddet);
    }
    public LiveData<InformeEtapaDet> getDetallexDesc(int idinf, String descripcion){

        return infDetRepo.getByDescripcion(descripcion,idinf);
    }
    public LiveData<InformeEtapaDet> getDetallexDescCaja(int idinf, String descripcion, int caja){

        return infDetRepo.getByDescripcionCaja(descripcion,idinf, caja);
    }
    public InformeEtapaDet getDetallexDescCajaSim(int idinf, String descripcion, int caja){
        Log.d("VIEM",idinf+"--"+descripcion+"--"+caja);
        return infDetRepo.getByDescripcionCajaSim(descripcion,idinf, caja);
    }
    public InformeEtapaDet getByDescripCajaSim(int idinf, int descripcion, int caja){

        return infDetRepo.getByDescripCajaSim(idinf,descripcion, caja);
    }



    public List<InformeEtapaDet> getDetEtaxCaja(int idinf, int etapa,int numcaja){
      // Log.d(TAG,"buscando a "+idinf+"--"+numcaja);
         return infDetRepo.getMuestraByCaja(idinf, etapa, numcaja);
    }
    public void borrarDetEtaxCaja(int idinf, int etapa,int numcaja){
        // Log.d(TAG,"buscando a "+idinf+"--"+numcaja);
        infDetRepo.deleteByCaja(idinf, etapa, numcaja);
    }
    public void actualizarComentarios(int idinf, String comentarios){
        infEtaRepository.actualizarComentariosPrep(idinf,comentarios);
    }
    public void actualizarComentEtiq(int idinf, String comentarios){
        infEtaRepository.actualizarcomentariosEtiq(idinf,comentarios);
    }
    public void actualizarMuestras(int idinf,int muestrastot){
        infEtaRepository.actualizarMuestrasEtiq(idinf,muestrastot);
    }
    public void actualizarComentEmp(int idinf, String comentarios){
        infEtaRepository.actualizarComentariosEmp(idinf,comentarios);
    }
    public void actualizarInfEtapa(InformeEtapa informe){
        infEtaRepository.insert(informe);
    }

    public List<InformeEtapaDet> getInformeDet(int id){
       return infDetRepo.getAllSencillo(id);
    }
    public List<InformeEtapaDet> getInfDetCalCaja(int id){
        return infDetRepo.getInfDetCalCaja(id);
    }

    public List<DetalleCaja> getdetalleCajaxInf(int idinf){
        return cajaRepo.getAllsimplexInf(idinf);
    }

    public int getTotCajasxInf(int id){
        return infDetRepo.getTotcajasxInf(id);
    }

    public InformeEtapaDet getUltimoInformeDet(int id, int etapa){
        return infDetRepo.getUltimo(id, etapa);


    }

    public InformeEtapaDet getUltimaMuestra(int id){
        return infDetRepo.getUltimaMuestra(id, 3);

    }
   /* public void listaCajasEtiqxCliCd(int cliente){
        List<InformeEtapaDet> muestras= infDetRepo.listaCajasEtiqxCli( 3, cliente);
        EtiquetadoxCliente resul=null;
        int i=1;
        listaCajasCli=new ArrayList<>();
        for (InformeEtapaDet muestra:muestras) {
            resul=new EtiquetadoxCliente();
            resul.consCaja=i;
            resul.numCaja=muestra.getNum_caja();
            resul.numMuestras=muestra.getNum_muestra();
            listaCajasCli.add(resul);
            i++;
        }


    }*/

    public void getCajasEtiq(int cliente, String ciudad){
        Log.d(TAG,Constantes.INDICEACTUAL+"--"+cliente+"--"+ciudad);
        List<InformeEtapaDet> muestras= infDetRepo.getResumenEtiq(3,Constantes.INDICEACTUAL,cliente, ciudad);
        EtiquetadoxCliente resul=null;
        int i=1;
        Log.d(TAG,"tot cajas"+muestras.size());
        resumenEtiq=new ArrayList<>();
        this.numMuestras=0;
        for(InformeEtapaDet muestra:muestras) {
            EtiquetadoxCliente caja=new EtiquetadoxCliente();
            caja.consCaja=i;
            caja.numCaja=muestra.getNum_caja();
            caja.numMuestras=muestra.getNum_muestra();
            resumenEtiq.add(caja);
            i++;
            //para obtener el total de muestras
            this.numMuestras+=muestra.getNum_muestra();
        }


    }
        //se usa en empaque para traer solo las del cliente
    public void getCajasEtiqCdCli(String ciudad, int cliente){
        List<InformeEtapaDet> muestras= infDetRepo.listaCajasEtiqxCdCli2( 3,ciudad,cliente);
        EtiquetadoxCliente resul=null;
        int i=1;

        resumenEtiq=new ArrayList<>();
        this.numMuestras=0;
        for(InformeEtapaDet muestra:muestras) {
            EtiquetadoxCliente caja=new EtiquetadoxCliente();
            caja.consCaja=i;
            caja.numCaja=muestra.getNum_caja();
            caja.numMuestras=muestra.getNum_muestra();
            resumenEtiq.add(caja);
            i++;
            //para obtener el total de muestras
            this.numMuestras+=muestra.getNum_muestra();
        }
        Log.d(TAG,"tot cajas"+muestras.size());

    }
    public int getTotCajasEtiqxCli(String ciudad, int cliente){
        int val= infDetRepo.totalCajasEtiqxCli(3, ciudad,cliente);
        Log.d(TAG,"getTotCajasEtiqxCli "+val);
        return val;


    }
    public int getTotCajasEtiqxInf( int infid){
        int val= infDetRepo.totalCajasEtiqxInf(3, infid);
        Log.d(TAG,"totalCajasEtiqxInf "+val);
        return val;


    }

    public int getMinCajaxCli(String ciudad, int cliente){
        int val= infDetRepo.getMinCajaxCli(3, ciudad,cliente);
        Log.d(TAG,"getMinCajaxCli "+val);
        return val;


    }

    public int getTotCajasEtiqxCd(String cd){
        int val= infDetRepo.totalCajasEtiq( cd);
        Log.d(TAG,"cajas "+val);
        return val;


    }
    public  List<InformeEtapaDet> listaCajasEtiqxCdCli(String ciudad,int cliente){
        return infDetRepo.listaCajasEtiqxCdCli( 3,ciudad,cliente);
    }




    public InformeEtapaDet getUltimoInformeDetCaj(int id, int etapa, int caja){
        return infDetRepo.getUltimoCaja(id, etapa, caja);


    }

    public List<InformeEtapa> getClientesconInf(String indice,String ciudad){
        return   infEtaRepository.getClientesconInf(indice,ciudad);
    }
    public DetalleCaja getUltimoDetalleCaja(int infid){
        return cajaRepo.getUltimo(infid);


    }
    public DetalleCaja getUltimoxCaja(int infid, int caja){
        return cajaRepo.getUltimoxCaja(infid, caja);


    }
    public DetalleCaja getDetalleCajaxCaja(int infid, int num_caja){
        return cajaRepo.getdetallexCaja(infid, num_caja);


    }
    public List<DetalleCaja> cargarDetCajas(int idinf){


        return cajaRepo.getAllsimplexInf(idinf);
    }
    public LiveData<Reactivo> buscarReactivo(int id){
        return reacRepo.find(id);
    }
    public Reactivo buscarReactivoSim(int id){
        return reacRepo.findsimple(id);
    }
    public Reactivo buscarReactivoxDesc(String campo, int cliente){
        return reacRepo.findByNombre(campo, cliente);
    }
    public void buscarReactivos(){
        Log.d(TAG, "buscado reac");
        if(listaPreguntas==null)
            listaPreguntas= reacRepo.getEmp(4);
    }
    public Reactivo buscarReactivoAnterior(int id){

            return reacRepo.findAnterior(id);

    }
    /*public List<InformeCompraDetalle> buscarProdsxQr(int idNuevo, int etapa, int numcaja) {
        List<InformeEtapaDet> qrs=infDetRepo.getByCaja(idNuevo,etapa,numcaja);
       InformeComDetRepositoryImpl comrepo=new InformeComDetRepositoryImpl(application);
       InformeCompraDetalle prod;
        List<InformeCompraDetalle> listaProds=new ArrayList<>();
        for(InformeEtapaDet detalle : qrs){
            //busco el producto en el informe
            prod=comrepo.findByQr(detalle.getQr(), Constantes.INDICEACTUAL);
            listaProds.add(prod);
        }
        return  listaProds;
    }*/

    public InformeEtapaDet buscarDetxQr(String qr) {
         return infDetRepo.getByQr(qr,3);

    }


    public List<InformeEtapa>  buscarInformesEtiq(String indice) {

        List<InformeEtapa> informes= infEtaRepository.getAllSimple(3, indice);
        return informes;
    }

    public List<InformeEtapa> buscarInformeEtiqxcli(String indice,  int cliente) {
         return infEtaRepository.getInformesxCli(indice, 3,cliente);
    }
    public void buscarInformeEtiq(String indice,  int planta) {
        if(informeEtiq==null)
            this.informeEtiq= infEtaRepository.getInformexPlan(indice, 3,planta);
    }

    public void buscarInformeEmp(String indice) {
        if(informeEtiq==null) {
            List<InformeEtapa> infos = infEtaRepository.getAllSimple(4, indice);
            if(infos!=null&&infos.size()>0)
            this.informeEtiq =infos.get(0);
        }
    }
    public Reactivo inftempToReac(InformeTemp inftemp){
        return reacRepo.findByNombre(inftemp.getNombre_campo(),inftemp.getClienteSel());

    }
    public void finalizarInf(){
        infEtaRepository.actualizarEstatus(idNuevo,2);
    }

    public int getIdNuevo() {
        return idNuevo;
    }

    public void setIdNuevo(int idNuevo) {
        this.idNuevo = idNuevo;
    }

    public int getIddetalle() {
        return iddetalle;
    }

    public void setIddetalle(int iddetalle) {
        this.iddetalle = iddetalle;
    }

    public MutableLiveData<Event<Integer>> getmSnackbarText() {
        return mSnackbarText;
    }

    public InformeEtapa getNvoinforme() {
        return nvoinforme;
    }


    public void setNvoinforme(InformeEtapa nvoinforme) {
        this.nvoinforme = nvoinforme;
    }

    public InformeEtapa getInformeEtiq() {
        return informeEtiq;
    }

    public void setInformeEtiq(InformeEtapa informeEtiq) {
        this.informeEtiq = informeEtiq;
    }

    public List<Reactivo> getListaPreguntas() {
        return listaPreguntas;
    }

    public List<ImagenDetalle> buscarImagenes(List<InformeEtapaDet> informeEtapaDet) {
        List<ImagenDetalle> imagenes=new ArrayList<>();
        for(InformeEtapaDet detalle:informeEtapaDet){
            int fotoid=0;
            try {
                 fotoid = Integer.parseInt(detalle.getRuta_foto());
                ImagenDetalle imagen=imagenDetRepository.findsimple(fotoid);
                imagenes.add(imagen);
            }catch(NumberFormatException ex){
                compraslog.grabarError(TAG+" "+ex.getMessage());
            }


        }
        return  imagenes;
    }

    public InformeEtapa getInformexPlantaEta(int plantasId, int etapa, String indice,int estatus) {
        return infEtaRepository.getInformexPlantEst(indice,etapa,plantasId,0);
    }

    public InformeEtapa getInformexPlantaEtaEst(int plantasId, int etapa, String indice,int estatus) {
        return infEtaRepository.getInformexPlantEst2(indice,etapa,plantasId,estatus);
    }
    public ImagenDetalle getFoto(int idfoto) {

        //return imagenDetRepository.find(visita.getFotoFachada());
        return imagenDetRepository.findsimpleInd(idfoto,Constantes.INDICEACTUAL);
    }

    public void buscatTMuesxCaj(int cajaAct, int informeEtiId) {
         this.numMuestras=infDetRepo.getTotMuesxCaja(cajaAct,informeEtiId);

    }

    public int getTotalMuestrasxCliXcd(int clienteSel,String cd) {
       return compRepo.getTotalMuesxCliCd(clienteSel,Constantes.INDICEACTUAL,cd);
    }


    //devuelvo lista de plantas con informe
    public Integer[] tieneInforme(int etapa){
        Integer[] clienteAnt=null;
        List<InformeEtapa> informes=infEtaRepository.getAllSimple(etapa,Constantes.INDICEACTUAL);
        if(informes!=null&&informes.size()>0) {
            clienteAnt=new Integer[informes.size()];
            for (int i = 0; i < informes.size(); i++) //y no estan cancelado
            {
              //  Log.d(TAG,"zzz"+informes.get(i).getEstatus());
                if(informes.get(i).getEstatus()>0&&informes.get(i).getCiudadNombre()!=null&&informes.get(i).getCiudadNombre().equals(Constantes.CIUDADTRABAJO)) //no cancelados y de la cd
                    clienteAnt[i] = informes.get(i).getClientesId();

            }
        }

        return clienteAnt;

    }
    public InformeEtapaEnv preparaInformeEtiq(int idnvo){
        InformeEtapaEnv envio=new InformeEtapaEnv();

        envio.setInformeEtapa(getInformexId(idnvo));

        envio.setClaveUsuario(Constantes.CLAVEUSUARIO);
        envio.setIndice(Constantes.INDICEACTUAL);
        envio.setInformeEtapaDet(this.getInformeDet(idnvo));
        List<ImagenDetalle> imagenes=this.buscarImagenes(envio.getInformeEtapaDet());

        envio.setImagenDetalles(imagenes);
        return envio;
    }

    public InformeEtapaEnv preparaInformeEtiqAct(int idnvo){
        InformeEtapaEnv envio=new InformeEtapaEnv();

        envio.setInformeEtapa(getInformexId(idnvo));

        envio.setClaveUsuario(Constantes.CLAVEUSUARIO);
        envio.setIndice(Constantes.INDICEACTUAL);
        List<InformeEtapaDet> muestras=new ArrayList<>();
        //busco los nuevo detalles
        if(muestrasactEtiq!=null)
            for (int iddet:
                 muestrasactEtiq) {
                muestras.add(infDetRepo.findsimple(iddet));
            }

        List<InformeEtapaDet> fotocaja=infDetRepo.getInfDetCalCaja(idnvo);
        muestras.addAll(fotocaja);
        envio.setInformeEtapaDet(muestras);
        List<ImagenDetalle> imagenes=this.buscarImagenes(envio.getInformeEtapaDet());

        envio.setImagenDetalles(imagenes);
        return envio;
    }


    public void corregirEtiquetado(InformeEtapaDet muestra,int numcaja){

            if(muestra!=null) {
                muestra.setNum_caja(numcaja);
                actualizarInfEtaDet(muestra);
            }
        }

    public InformeEtapaEnv preparaInformeEtiqCor(int idnvo,InformeEtapaDet detalle){
        InformeEtapaEnv envio=new InformeEtapaEnv();

        envio.setInformeEtapa(getInformexId(idnvo));

        envio.setClaveUsuario(Constantes.CLAVEUSUARIO);
        envio.setIndice(Constantes.INDICEACTUAL);
        List<InformeEtapaDet> temp=new ArrayList<>();
        temp.add(detalle);
        envio.setInformeEtapaDet(temp);

        return envio;
    }


}