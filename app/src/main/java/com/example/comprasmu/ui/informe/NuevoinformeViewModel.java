package com.example.comprasmu.ui.informe;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.comprasmu.R;

import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.modelos.Contrato;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeTemp;
import com.example.comprasmu.data.modelos.InformeWithDetalle;

import com.example.comprasmu.data.modelos.ProductoExhibido;
import com.example.comprasmu.data.modelos.Visita;

import com.example.comprasmu.data.remote.InformeEnvio;

import com.example.comprasmu.data.remote.UltimoInfResponse;
import com.example.comprasmu.data.remote.UltimosIdsResponse;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.repositories.InformeTempRepositoryImpl;
import com.example.comprasmu.data.repositories.ProductoExhibidoRepositoryImpl;
import com.example.comprasmu.data.repositories.VisitaRepositoryImpl;

import com.example.comprasmu.ui.informedetalle.ValidadorDatos;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.Event;

import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class NuevoinformeViewModel extends AndroidViewModel {

    private final MutableLiveData<HashMap<Integer,String>> listaClientes;
    private final MutableLiveData<HashMap<Integer,String>> tiposTienda;

    private final MutableLiveData<Event<Integer>> mSnackbarText = new MutableLiveData<>();

    private final InformeCompraRepositoryImpl repository;

    private final ImagenDetRepositoryImpl imagenDetRepository;

    private final ProductoExhibidoRepositoryImpl prodRepo;
    private final VisitaRepositoryImpl visitaRepository;
    public InformeCompra informe;
    public  int consecutivo;
    public ImagenDetalle ticket_compra;
    public ImagenDetalle condiciones_traslado;

    public ImagenDetalle fotoFachada;

    public String[] clientesFoto;
    private final InformeComDetRepositoryImpl detalleRepo;

    private int idInformeNuevo;
    public int clienteSel;
    public String clienteNombre;
    public Visita visita;
    public LiveData<Visita> visitaEdicion;
    public LiveData<InformeCompra> informeEdicion;
    public int idVisita;
    public boolean mIsNew;
    public String TAG="NuevoInformeVM";

    InformeTempRepositoryImpl itemprepo;
    public int numMuestra;
    public List<InformeCompraDetalle> muestrasCap; //voy a gregando las muestras
    public int prefvisita;
    public int prefinf;
    public int prefimagen;
    public int prefcons;
    Application application;

    public NuevoinformeViewModel(@NonNull Application application) {
        super(application);
        this.repository = new InformeCompraRepositoryImpl(application);
        this.imagenDetRepository=new ImagenDetRepositoryImpl(application);
        this.detalleRepo=new InformeComDetRepositoryImpl(application);

        this.visitaRepository=new VisitaRepositoryImpl(application);
        prodRepo=new ProductoExhibidoRepositoryImpl(application);
        this.application=application;
        listaClientes = new MutableLiveData<>();
        tiposTienda = new MutableLiveData<>();
        informe     =new InformeCompra();
        itemprepo=new InformeTempRepositoryImpl(application);
        HashMap<Integer,String> tipos=new HashMap();
        tipos.put(1,"Grande");
        tipos.put(2,"Pequeña");
        numMuestra=0;
        muestrasCap=new ArrayList<>();
       tiposTienda.setValue(tipos);
       // Log.d("NuevoInformeViewModel","************************creando viewmodel");
        nvoid=new MutableLiveData<Integer>();

    }
    /*****para saber si es edición o uno nuevo*****/
    public int  start(int visitaId,Activity actividad) {
        idVisita = visitaId;

        // ¿Es una nuevo visita?
        if (idVisita <= 0) {
            mIsNew= true;
            //busco el siguiente id
            int idVisita=visitaRepository.getUltimo(Constantes.INDICEACTUAL);
            if(idVisita==0)//no hay nada busco en el serv
            {
                if (prefvisita == 0&&ComprasUtils.isOnlineNet()) {
                   /* PeticionesServidor ps = new PeticionesServidor(Constantes.CLAVEUSUARIO);
                    NuevoinformeViewModel.EnvioListener listener = new NuevoinformeViewModel.EnvioListener(actividad);
                    MutableLiveData<Boolean> resul=ps.getUltimaVisita(Constantes.INDICEACTUAL, listener);*/
                    recuperarIds(actividad);
                }

                idVisita=prefvisita+1;

            }else
                idVisita=idVisita+1;
      //    idVisita=visitaRepository.getSiguienteId();
            return idVisita;
        }

        mIsNew = false;
        visitaEdicion=buscarVisita(visitaId);
        return  idVisita;

    }

    /*****para saber si es edición o un nuevo informe*****/
    public void startInforme(int informeid) {
        idInformeNuevo = informeid;

        // ¿Es una nuevo informe?
        if (idInformeNuevo <= 0) {
            mIsNew= true;
            return;
        }

        mIsNew = false;
        //es edicion y busco el datos
        informeEdicion = repository.getInformeCompra(idInformeNuevo);


    }
      public MutableLiveData<HashMap<Integer, String>> getListaClientes() {
        return listaClientes;
    }

    public MutableLiveData<HashMap<Integer, String>> getTiposTienda() {
        return tiposTienda;
    }

    public  Integer getConsecutivo(int oplantaSel, Activity actividad, LifecycleOwner owner) {

        int respcon=1;
      //  Log.d(TAG, "ahorita es la planta"+oplantaSel);
        int ultimo=repository.getLastConsecutivoInforme(Constantes.INDICEACTUAL,oplantaSel);
     //   Log.d(TAG, "consecutivo encontrado"+ultimo);

        respcon=1+ultimo;
        return respcon;
    }
   // public void agregarMuestra(InformeCompraDetalle det){
     //   muestrasCap.add(det);
    //}
    public void cargarMuestras(int id){
        muestrasCap=detalleRepo.getAllSencillo(id);
    }

    /*public LiveData<String> getRutaFoto(int idfoto){
        return imagenDetRepository.findRuta(idfoto);

    }*/
    public ImagenDetalle getFoto(int idfoto){
        return imagenDetRepository.findsimple(idfoto);

    }
    //busca la foto si está pendiente
    public ImagenDetalle getFotoPend(int idfoto,String indice){
        return imagenDetRepository.getImagenxEstatusSync(idfoto,0,indice);

    }
    public LiveData<ImagenDetalle> getFotoLD(int idfoto){
        return imagenDetRepository.find(idfoto);

    }

    public MutableLiveData<Boolean> informesAbiertos(LifecycleOwner observer){
        MutableLiveData<Boolean> abiertos=new MutableLiveData<>();
        visitaRepository.getInformesByIndice(Constantes.INDICEACTUAL).observe(observer, new Observer<List<Visita>>() {
                    @Override
                    public void onChanged(List<Visita> visitas) {
                        if(visitas!=null)
                            for(Visita revvis:visitas){
                                if(Constantes.ESTATUSINFORME[revvis.getEstatus()].equals("ABIERTO")||revvis.getEstatus()==3){
                                    //ya tengo uno abierto mando aviso
                                    abiertos.setValue(true);
                                    break;
                                }
                            }
                    }
                }

        );
        return  abiertos;
    }
    /**para envio***/
    public InformeEnvio preparaInforme(int informe){
        InformeWithDetalle info=repository.getInformeWithDetalleByIdsimple(informe);
       // Log.d(TAG,"xxxx"+info.informe.getId()+info.informe.getVisitasId());

        InformeEnvio envio=new InformeEnvio();
        //busco la visita pero tiene que estar en estatussync pendiente
        //   VisitaRepositoryImpl vrepo=new VisitaRepositoryImpl(getContext());
        Visita visita=visitaRepository.findsimple(info.informe.getVisitasId());
       // Log.d(TAG,"wwwwwwwww"+info.informe.getId()+"---"+visita.getTiendaNombre());
        envio.setIndice(visita.getIndice());
        envio.setClaveUsuario(Constantes.CLAVEUSUARIO);
        //if(visita.getEstatusSync()==0)
            envio.setVisita(visita);
        envio.setInformeCompra(info.informe);
        //envio.setProductosEx(prodRepo.getAllsimplePendSync(visita.getId()));
        envio.setProductosEx(prodRepo.getAllsimple(visita.getId()));
        //busco los detalles
        List<InformeCompraDetalle> detalles=detalleRepo.getAllSencillo(info.informe.getId());
        envio.setInformeCompraDetalles(detalles);
      //  envio.setImagenDetalles(buscarImagenes(visita,info.informe,detalles));
        envio.setImagenDetalles(buscarImagenesRe(visita,info.informe,detalles));
        return envio;
    }

    /**para revisar el detalle del estatus
     * 0 algo pendiente
     * 2 todo enviado ***/
    public  int getEstatusSyncInforme(int informe){
        InformeWithDetalle info=repository.getInformeWithDetalleByIdsimple(informe);


        //busco la visita

        Visita visita=visitaRepository.findsimple(info.informe.getVisitasId());
      //  Log.d(TAG,"wwwwwwwww"+info.informe.getId()+"---"+visita.getTiendaNombre());

        //busco los detalles
        List<InformeCompraDetalle> detalles=detalleRepo.getAllSencillo(info.informe.getId());
        if(this.todasFotosEnviadas(visita,info.informe,detalles))
            return 2;
        return 0;

    }
    //reviso el estatus sync de todas las fotos del informe y la visita
    //si una está pendiente de envio devuelvo false
    public boolean todasFotosEnviadas(Visita visita, InformeCompra informe, List<InformeCompraDetalle> detalles){

        //la visita

        ImagenDetalle imagenDetalle = getFotoPend(visita.getFotoFachada(),visita.getIndice());
        if(imagenDetalle!=null) //esta pendiente
            return false;

        //las del informe

        imagenDetalle=getFotoPend(informe.getCondiciones_traslado(),visita.getIndice());
        if(imagenDetalle!=null) //esta pendiente
            return false;
        imagenDetalle=getFotoPend(informe.getTicket_compra(),visita.getIndice());
        if(imagenDetalle!=null) //esta pendiente
            return false;

        //las del los detalles
        if(detalles!=null)
            for(InformeCompraDetalle detalle:detalles) {
                List<Integer> fotos=detalleRepo.getInformesWithImagen(detalle.getId());
                for(int i=0;i<fotos.size();i++) {
                    if(fotos.get(i)!=null)
                    imagenDetalle=getFotoPend(fotos.get(i),visita.getIndice());
                    if(imagenDetalle!=null) //esta pendiente
                        return false;
                }

            }

        //las de producto ex
        List<ImagenDetalle> productoExhibidos = prodRepo.getImagenByVisitasimple(visita.getId());
        for(ImagenDetalle detalle:productoExhibidos) {
            imagenDetalle=getFotoPend(detalle.getId(),visita.getIndice());
            if(imagenDetalle!=null) //esta pendiente
                return false;


        }

        return true; //si llegue hasta aqui es que ya todo se envio


    }
    public List<ProductoExhibido> buscarProdExhi(){
        return prodRepo.getAllsimple(visita.getId());
    }
    public List<ProductoExhibido> buscarProdExhiPend(){
        return prodRepo.getAllsimplePendSync(visita.getId());
    }

    public List<ImagenDetalle> buscarImagenes(Visita visita, InformeCompra informe, List<InformeCompraDetalle> detalles){
        //todas las fotos aqui
        List<ImagenDetalle> fotosinfo=new ArrayList<>();
        //la visita
        if(visita.getEstatusSync()==0) {
            ImagenDetalle imagenDetalle = getFoto(visita.getFotoFachada());
            if(imagenDetalle!=null&&imagenDetalle.getEstatusSync()==0)
                 fotosinfo.add(imagenDetalle);
        }

        //las del informe
        List<Integer> arrFotos=new ArrayList<>();
        arrFotos.add(informe.getCondiciones_traslado());
        arrFotos.add(informe.getTicket_compra());
        List<ImagenDetalle> imagenDetalles=imagenDetRepository.findListsencillo(arrFotos);
       if(imagenDetalles!=null)
        fotosinfo.addAll(imagenDetalles);

        //las del los detalles
        if(detalles!=null)
            for(InformeCompraDetalle detalle:detalles) {
                List<Integer> fotos=detalleRepo.getInformesWithImagen(detalle.getId());
                List<ImagenDetalle> imagenDetalles2=imagenDetRepository.findListsencillo(fotos);
                fotosinfo.addAll(imagenDetalles2);

            }

        //las de producto ex
        if(visita.getEstatusSync()==0) {
            List<ImagenDetalle> productoExhibidos = prodRepo.getImagenByVisitasimplePend(visita.getId(),0);

            fotosinfo.addAll(productoExhibidos);
        }

       return fotosinfo;


    }
    //para reenviar imagenes
    public List<ImagenDetalle> buscarImagenesRe(Visita visita, InformeCompra informe, List<InformeCompraDetalle> detalles){
        //todas las fotos aqui
        List<ImagenDetalle> fotosinfo=new ArrayList<>();
        //la visita

        ImagenDetalle imagenDetalle = getFoto(visita.getFotoFachada());
        if(imagenDetalle!=null)
            fotosinfo.add(imagenDetalle);

        //las del informe
        List<Integer> arrFotos=new ArrayList<>();
        arrFotos.add(informe.getCondiciones_traslado());
        arrFotos.add(informe.getTicket_compra());
        List<ImagenDetalle> imagenDetalles=imagenDetRepository.findListsencillo(arrFotos);
        if(imagenDetalles!=null)
            fotosinfo.addAll(imagenDetalles);

        //las del los detalles
        if(detalles!=null)
            for(InformeCompraDetalle detalle:detalles) {
                List<Integer> fotos=detalleRepo.getInformesWithImagen(detalle.getId());
                List<ImagenDetalle> imagenDetalles2=imagenDetRepository.findListsencillo(fotos);
                fotosinfo.addAll(imagenDetalles2);

            }

        //las de producto ex
        List<ImagenDetalle> productoExhibidos = prodRepo.getImagenByVisitasimple(visita.getId());
        //  Log.d(TAG,"enviando prod "+productoExhibidos.get(0).getRuta());
            fotosinfo.addAll(productoExhibidos);

        return fotosinfo;


    }
    public boolean guardarResp(int informeid, int informedet,String resp,String nombrecampo,String tabla,int consecutivo, boolean isPregunta){
        InformeTemp temporal=new InformeTemp();
        temporal.setNombre_campo(nombrecampo);
        temporal.setValor(resp);
        temporal.setTabla(tabla);
        temporal.setConsecutivo(consecutivo);
        temporal.setPregunta(isPregunta);
        temporal.setVisitasId(this.visita.getId());
        temporal.setInformesId(informeid);
        temporal.setIddetalle(informedet);
        if(!nombrecampo.equals("clientesId"))
            temporal.setClienteSel(clienteSel);
        try {
          //  Log.d(TAG,"buscando a "+nombrecampo);
            //reviso si ya existe
            InformeTemp editar=null;
            if(!tabla.equals("I"))
            {     temporal.setNumMuestra(numMuestra);
                editar = itemprepo.findByNombreMues(nombrecampo, numMuestra);
               }
            else {
                editar=itemprepo.findByNombre(nombrecampo);
            }
            if(editar!=null){
                editar.setNombre_campo(nombrecampo);
                editar.setValor(resp);
                editar.setTabla(tabla);
                if(!nombrecampo.equals("clientesId"))
                    editar.setClienteSel(clienteSel);
                editar.setConsecutivo(consecutivo);
                itemprepo.insert(editar);
            }else
            itemprepo.insert(temporal);
            return true;
        }catch(Exception ex){
            Log.e(TAG,ex.getMessage());
            return false;
        }

    }

    public boolean guardarRespSust(int idvisita, int idcliente, int xnumMuestra, int informeid, int informedet,String resp,String nombrecampo,String tabla,int consecutivo, boolean isPregunta){
        InformeTemp temporal=new InformeTemp();
        temporal.setNombre_campo(nombrecampo);
        temporal.setValor(resp);
        temporal.setTabla(tabla);
        temporal.setConsecutivo(consecutivo);
        temporal.setPregunta(isPregunta);
        temporal.setVisitasId(idvisita);
        temporal.setInformesId(informeid);
        temporal.setIddetalle(informedet);
        if(!nombrecampo.equals("clientesId"))
            temporal.setClienteSel(idcliente);
        try {
            //  Log.d(TAG,"buscando a "+nombrecampo);
            //reviso si ya existe
            InformeTemp editar=null;
            if(!tabla.equals("I"))
            {     temporal.setNumMuestra(xnumMuestra);
                editar = itemprepo.findByNombreMues(nombrecampo, xnumMuestra);
            }
            else {
                editar=itemprepo.findByNombre(nombrecampo);
            }
            if(editar!=null){
                editar.setNombre_campo(nombrecampo);
                editar.setValor(resp);
                editar.setTabla(tabla);
                if(!nombrecampo.equals("clientesId"))
                    editar.setClienteSel(idcliente);
                editar.setConsecutivo(consecutivo);
                itemprepo.insert(editar);
            }else
                itemprepo.insert(temporal);
            return true;
        }catch(Exception ex){
            Log.e(TAG,ex.getMessage());
            return false;
        }

    }


    MutableLiveData<Integer> finalid;
    public  MutableLiveData<Integer>  guardarVisita(Activity actividad, LifecycleOwner owner) {
        if(fotoFachada!=null) {
            MutableLiveData<Integer> nvoidimage = this.getNvoIdImagen(actividad, owner);
            nvoidimage.observe(owner, new Observer<Integer>() {
                @Override
                public void onChanged(Integer nvoidimagem) {
                    fotoFachada.setId(nvoidimagem);
                    nvoidimagem = (int) imagenDetRepository.insertImg(fotoFachada);

                    visita.setFotoFachada(nvoidimagem);
                    // Guardar visita
                    //busco el id

                    //  visita.setId(nuevoid);
                    idVisita = (int) visitaRepository.insert(visita);

     /*    if(idInformeNuevo>0) {


             clientesFoto[0] = "pepsi";

             int i=0;
            //crear los informes y guardar
            for(ImagenDetalle foto:fotoExhibicion) {
                int idfoto= (int) imagenDetRepository.insertImg(foto);
                informe=new InformeCompra();


             //   informe.setProducto_exhibido(idfoto);
                informe.setClientesId(1);
                informe.setClienteNombre(clientesFoto[i]);
                repository.insertInformeCompra(informe);
                i++;


            }

        }*/
                    if (idVisita > 0) {
                        mSnackbarText.setValue(new Event<>(R.string.added_informe_message));
                    }
                    finalid = new MutableLiveData<Integer>();
                    finalid.setValue(idVisita);
                }
            });
        }else{
            //  visita.setId(nuevoid);
            //solo inserto la visita no hay foto
            idVisita = (int) visitaRepository.insert(visita);


            if (idVisita > 0) {
                mSnackbarText.setValue(new Event<>(R.string.added_informe_message));
            }
            finalid = new MutableLiveData<Integer>();
            finalid.setValue(idVisita);
        }
        return finalid;

    }
        //busco el id para la imagen
        public MutableLiveData<Integer> getNvoIdImagen(Activity actividad, LifecycleOwner owner) {
            MutableLiveData<Integer> nvoid=new MutableLiveData<Integer>();
        int nvoidimagem = (int) imagenDetRepository.getUltimo();
            if (nvoidimagem == 0) {

                nvoid.setValue( nvoidimagem + 1);
            } else
                nvoid.setValue( nvoidimagem + 1);
            return nvoid;
        }



    public void guardarIdsV(UltimosIdsResponse respuesta, Activity actividad){
        SharedPreferences prefe=actividad.getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=prefe.edit();

        editor.putInt("ultidvisita",respuesta.getVisita() );

        editor.putInt("ultimagen",respuesta.getImagen_detalle());
        editor.commit();


    }

    public void guardarIdsI(UltimoInfResponse respuesta, Activity actividad){
        SharedPreferences prefe=actividad.getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=prefe.edit();
        String cadinf= respuesta.getInforme();
        Log.d(TAG,"guardando en props");
        if(cadinf!=null)
        { String [] aux=cadinf.split(",");


        editor.putInt("ultidinforme",Integer.parseInt(aux[0]));
        editor.putInt("ultconsecutivo",Integer.parseInt(aux[1]));}

        editor.commit();


    }
    public void recuperarIds(Activity actividad) {
        Log.d(TAG, "recuperando prop");
        SharedPreferences prefe=actividad.getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        prefvisita=prefe.getInt("ultidvisita", 0);
        prefinf=prefe.getInt("ultidinforme", 0);
        prefcons=prefe.getInt("ultconsecutivo", 0);
        prefimagen=prefe.getInt("ultimagen", 0);


    }

        public void actualizarVisita(){
        try {
            //actualizo la foto
            if(fotoFachada!=null) {
                int foto =(int) imagenDetRepository.insert(fotoFachada);
                visita.setFotoFachada(foto);

            }
            visitaRepository.insert(this.visita);

            mSnackbarText.setValue(new Event<>(R.string.added_informe_message));
        }catch(Exception ex){
            Log.e("NuevoInformeVM",ex.getMessage());
            mSnackbarText.setValue(new Event<>(R.string.added_informe_error));
        }

    }
    public long guardarInforme() {

        int idInforme=(int)repository.insertInformeCompra(informe);

      return idInforme;

    }
    public  MutableLiveData<Integer> insertarInfdeTemp(Activity actividad, LifecycleOwner owner) {
        this.informe = tempToIC();

        this.informe.setEstatusSync(0);
        this.informe.setEstatus(1);
        nvoid=new MutableLiveData<>();
        nvoid=getNvoIdInforme(actividad,this.informe.getPlantasId());
       // nvoid.setValue(8);
        nvoid.observe(owner, new Observer<Integer>() {
            @Override
            public void onChanged(Integer nvoidd) {
                Log.d(TAG, "nvo informe" + nvoidd);
                if(nvoidd>0) {
                    informe.setId(nvoidd);
                  //  Log.d(TAG, "nvo informe" + nvoidd);
                    repository.insertInformeCompra(informe);
                }
               // nvoid.removeObservers(owner);
            }
        });
        return nvoid;
    }
    public  void limpiarVarInforme(){
        this.informe=null;
        this.nvoid=null;
        this.consecutivo=0;

    }
    MutableLiveData<Integer> nvoid;

     public MutableLiveData<Integer> getNvoIdInforme(Activity actividad, int planta) {


        int nvoid2 = (int) repository.getUltimo();
        if (nvoid2 == 0) {
        //busco en pref
             recuperarIds(actividad);

             if (prefinf == 0){
        //busco id
               /*  if (ComprasUtils.isOnlineNet()) {
                     //lo pido al servidor
                   //  PeticionesServidor ps = new PeticionesServidor(Constantes.CLAVEUSUARIO);
                    // EnvioListener listener = new EnvioListener(actividad);
                    // ps.getUltimoInforme(Constantes.INDICEACTUAL, planta, listener);
                 }
                 else*/
                     nvoid.postValue(1);
        }

             else
                 nvoid.postValue(prefinf+1);
        }else
            nvoid.postValue(nvoid2+1);

        return nvoid;

    }
    public void eliminarTblTemp(){
        itemprepo.deleteAll();
    }

    public void eliminarMuestra(int numMues){
        itemprepo.deleteMuestra(numMues);
    }
    public InformeTemp buscarCampo(String campo,List<InformeTemp> temps){
        for(InformeTemp info:temps){

                if(info.getNombre_campo().equals(campo)) {
                return info;
                }
        }
             return null;
    }
    public InformeCompra tempToIC(){
        //consulto el informe
        InformeCompra nuevo=new InformeCompra();
        List<InformeTemp> temps=itemprepo.getAllByTabla("I",0);
        InformeTemp inft=buscarCampo("plantasId",temps);
        if(inft!=null)
            nuevo.setPlantasId(Integer.parseInt(inft.getValor()));
         inft=buscarCampo("plantaNombre",temps);
        if(inft!=null)
         nuevo.setPlantaNombre(inft.getValor());
        inft=buscarCampo("clientesId",temps);
        if(inft!=null)
            nuevo.setClientesId(Integer.parseInt(inft.getValor()));
        inft=buscarCampo("clienteNombre",temps);
        if(inft!=null)
        nuevo.setClienteNombre(inft.getValor());
         inft=buscarCampo("comentarios",temps);
        if(inft!=null)
        nuevo.setComentarios(inft.getValor());


         inft=buscarCampo(Contrato.TablaInformeDet.causa_nocompra,temps);
     //    Log.d(TAG,"causa no compra="+inft.getNombre_campo());
        if(inft!=null) {
            nuevo.setSinproducto(true);
            nuevo.setCausa_nocompra(inft.getValor());
        }
        for(InformeTemp info:temps){
         /*   Class claseCargada = InformeCompra.class;
            Class params[] = new Class[1];
            params[0] = String.class;

            try {
                if(info.getNombre_campo().equals("segundaMuestra")) {
                    params[0] = Boolean.class;
                }*/
            nuevo.setVisitasId(info.getVisitasId());
            if(info.getNombre_campo().equals("plantasId")) {
                Log.d(TAG, "******otro cons" + info.getConsecutivo() + info.getNombre_campo());
                nuevo.setConsecutivo(info.getConsecutivo());
            }
            nuevo.setId(info.getInformesId());
                if(info.getNombre_campo().equals("ticket_compra")) {
                    if(info.getValor().equals("0")){

                    }else {
                        this.ticket_compra = new ImagenDetalle();
                        //    this.ticket_compra.setIndice(Constantes.INDICEACTUAL);
                        this.ticket_compra.setRuta(info.getValor());

                        this.ticket_compra.setDescripcion("ticket compra");
                        this.ticket_compra.setEstatus(1);
                        this.ticket_compra.setIndice(visita.getIndice());
                        this.ticket_compra.setEstatusSync(0);
                        this.ticket_compra.setCreatedAt(new Date());
                        nuevo.setId(info.getInformesId());
                    }
                }else
                if(info.getNombre_campo().equals("condiciones_traslado")) {
                //    Log.d(TAG,"******otro cons"+info.getConsecutivo()+info.getNombre_campo());
                    nuevo.setConsecutivo(info.getConsecutivo());
                    this.condiciones_traslado = new ImagenDetalle();
                    this.condiciones_traslado.setRuta(info.getValor());
                    this.condiciones_traslado.setDescripcion("condiciones traslado");
                    this.condiciones_traslado.setEstatus(1);
                    this.condiciones_traslado.setIndice(visita.getIndice());
                    this.condiciones_traslado.setEstatusSync(0);
                    this.condiciones_traslado.setCreatedAt(new Date());
                }else {
                 /*   Method metodo = claseCargada.getDeclaredMethod("set" + ComprasUtils.upperCaseFirst(info.getNombre_campo()),params);

                    metodo.invoke(nuevo, info.getValor());*/

                    continue;
                }
         /*   } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                params[0] = Integer.class;
                Method metodo  = null;
                try {
                    metodo = claseCargada.getDeclaredMethod("set" + ComprasUtils.upperCaseFirst(info.getNombre_campo()),params);


                metodo.invoke(nuevo, info.getValor());
                } catch (NoSuchMethodException noSuchMethodException) {
                    noSuchMethodException.printStackTrace();
                } catch (IllegalAccessException illegalAccessException) {
                    illegalAccessException.printStackTrace();
                } catch (InvocationTargetException invocationTargetException) {
                    invocationTargetException.printStackTrace();
                }
            }*/

        }
        return nuevo;
    }

    public void actualizarInforme() throws Exception { //inserta el informe desde temporal
        //conservo el id
        InformeCompra compra2=tempToIC();
        //Log.d(TAG,"dddddddddddddddddddd ya existe el informe"+compra2.getId());
        if(compra2.getId()==0)
        {
            //algo salio mal
            throw new Exception("no tengo informe");
        }
        //recupero el informe
        informe=repository.findSimple(compra2.getId());

        //recupero los comentarios

        informe.setComentarios(compra2.getComentarios());
        if(ticket_compra!=null) //si hubo producto//validaciones
        {

                int idt = (int) imagenDetRepository.insertImg(ticket_compra);

                informe.setTicket_compra(idt);

        }

        if(condiciones_traslado!=null) {
            int idc = (int) imagenDetRepository.insertImg(condiciones_traslado);

            informe.setCondiciones_traslado(idc);
        }

        informe.setEstatus(1);
        informe.setEstatusSync(0);


        repository.insertInformeCompra(informe);

//        mSnackbarText.setValue(new Event<>(R.string.added_informe_message));


    }

    public void finalizarInforme() {

       repository.actualizarEstatus(informe.getId(),2);

//        mSnackbarText.setValue(new Event<>(R.string.informe_finalizado));
        //aqui se enviará
     }
    public void finalizarVisita(int idvisita) {
        Log.d("NuevoInformeViewModel", "finalizando"+idvisita);
        visitaRepository.actualizarEstatus(idvisita,2);
        this.eliminarTblTemp();
    }
    public void actualizarVisita(int idvisita,int estatus) {
      //  Log.d("NuevoInformeViewModel", "finalizando"+idvisita);
        visitaRepository.actualizarEstatus(idvisita,estatus);
       this.visita.setEstatus(estatus);
    }

    public LiveData<InformeCompra> buscarInforme(int id){
        return repository.getInformeCompra(id);
    }

    public LiveData<Visita> buscarVisita(int id){
        Log.d("NuevoInformeViewModel", "Buscando visita");
        return visitaRepository.find(id);
    }

    public Visita buscarVisitaSimpl(int id){
        Log.d("NuevoInformeViewModel", "Buscando visita");
        return visitaRepository.findsimple(id);
    }
    public ImagenDetalle getFotoFachada() {
        return fotoFachada;
    }

    public void setFotoFachada(ImagenDetalle fotoFachada) {
        this.fotoFachada = fotoFachada;
    }



    public MutableLiveData<Event<Integer>> getSnackbarText() {
        return mSnackbarText;
    }

    public String[] getClientesFoto() {
        return clientesFoto;
    }

    public void setClientesFoto(String[] clientesFoto) {
        this.clientesFoto = clientesFoto;
    }
  public ImagenDetalle getTicket_compra() {
        return ticket_compra;
    }

    public void setTicket_compra(ImagenDetalle ticket_compra) {
        this.ticket_compra = ticket_compra;
    }

    public boolean validarQr(LifecycleOwner lo, String qr, int cliente){
        ValidadorDatos valdat = new ValidadorDatos();
        return valdat.validarQr(lo,qr,cliente,this.application);

    }

    public int getIdInformeNuevo() {
        return idInformeNuevo;
    }

    public void setIdInformeNuevo(int idInformeNuevo) {
        this.idInformeNuevo = idInformeNuevo;
    }

    public EnvioListener crearEnvioListener(Activity actividad) {
        return  new EnvioListener(actividad);
    }

    public InformeCompraDetalle getCancelada(int comprasId, int comprasDetId, int estatus) {
        return detalleRepo.getCancelda(comprasId,comprasDetId,estatus);

    }
    public InformeCompra getInformeCompra( int idinf){
       return repository.findSimple(idinf);
    }

    public void actualizarCancelada(int id, int estatus) {
         detalleRepo.actualizarEstatus(id, estatus);
    }

    public class EnvioListener {
        Activity actividad;

       public EnvioListener() {
       }

       public EnvioListener(Activity actividad) {
           this.actividad = actividad;
       }

       public MutableLiveData<Boolean> guardarRespuestaInf(UltimoInfResponse resp){
            guardarIdsI(resp,actividad);
           MutableLiveData<Boolean> resul=new MutableLiveData<Boolean>();
           resul.setValue(true);
           Log.d(TAG, "despues de guardar"+resul.getValue()+"--"+nvoid);

           recuperarIds(actividad);
           if(nvoid!=null) {
               nvoid.setValue(prefinf + 1);

           }
            /*if(respcon!=null) {
                respcon.setValue(prefcons + 1);
            }*/
            return resul;
        }
        public MutableLiveData<Boolean> guardarRespuestaVis(UltimosIdsResponse resp){
            guardarIdsV(resp,actividad);
            MutableLiveData<Boolean> resul=new MutableLiveData<Boolean>();
            resul.setValue(true);
            return resul;
        }

   }


}