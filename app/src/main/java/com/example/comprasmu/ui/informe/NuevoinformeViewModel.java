package com.example.comprasmu.ui.informe;

import android.app.Application;
import android.icu.text.IDNA;
import android.media.Image;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.comprasmu.R;

import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeTemp;
import com.example.comprasmu.data.modelos.InformeWithDetalle;

import com.example.comprasmu.data.modelos.Reactivo;
import com.example.comprasmu.data.modelos.Visita;

import com.example.comprasmu.data.remote.InformeEnvio;

import com.example.comprasmu.data.repositories.CatalogoDetalleRepositoryImpl;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.repositories.InformeTempRepositoryImpl;
import com.example.comprasmu.data.repositories.ProductoExhibidoRepositoryImpl;
import com.example.comprasmu.data.repositories.VisitaRepositoryImpl;

import com.example.comprasmu.ui.informedetalle.NuevoDetalleViewModel;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.Event;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class NuevoinformeViewModel extends AndroidViewModel {

    private MutableLiveData<HashMap<Integer,String>> listaPlantas;
    private MutableLiveData<HashMap<Integer,String>> listaClientes;
    private MutableLiveData<HashMap<Integer,String>> tiposTienda;

    private final MutableLiveData<Event<Integer>> mSnackbarText = new MutableLiveData<>();

    private final InformeCompraRepositoryImpl repository;

    private ImagenDetRepositoryImpl imagenDetRepository;

    private ProductoExhibidoRepositoryImpl prodRepo;
    private VisitaRepositoryImpl visitaRepository;
    public InformeCompra informe;
    public  int consecutivo;
    public ImagenDetalle ticket_compra;
    public ImagenDetalle condiciones_traslado;
    public List<ImagenDetalle> fotoExhibicion;
    public ImagenDetalle fotoFachada;

    public String[] clientesFoto;
    private InformeComDetRepositoryImpl detalleRepo;

    private int idInformeNuevo;
    public int clienteSel;
    public Visita visita;
    public LiveData<Visita> visitaEdicion;
    public LiveData<InformeCompra> informeEdicion;
    public int idVisita;
    public boolean mIsNew;
    public String TAG="NuevoInformeVM";

    InformeTempRepositoryImpl itemprepo;
    public int numMuestra;
    public List<InformeCompraDetalle> muestrasCap; //voy a gregando las muestras



    public NuevoinformeViewModel(@NonNull Application application) {
        super(application);
        this.repository = new InformeCompraRepositoryImpl(application);
        this.imagenDetRepository=new ImagenDetRepositoryImpl(application);
        this.detalleRepo=new InformeComDetRepositoryImpl(application);

        this.visitaRepository=new VisitaRepositoryImpl(application);
        prodRepo=new ProductoExhibidoRepositoryImpl(application);
        listaPlantas = new MutableLiveData<>();
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
        Log.d("NuevoInformeViewModel","************************creando viewmodel");


    }
    /*****para saber si es edición o uno nuevo*****/
    public int  start(int visitaId) {
        idVisita = visitaId;

        // ¿Es una nuevo informe?
        if (idVisita <= 0) {
            mIsNew= true;
            //busco el siguiente id
          idVisita=visitaRepository.getSiguienteId();
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
    public MutableLiveData<HashMap<Integer, String>> getListaPlantas() {
        return listaPlantas;
    }

    public MutableLiveData<HashMap<Integer, String>> getListaClientes() {
        return listaClientes;
    }

    public MutableLiveData<HashMap<Integer, String>> getTiposTienda() {
        return tiposTienda;
    }

    public int getConsecutivo(int clienteSel) {

        int resp=1;
        int ultimo=repository.getLastConsecutivoInforme(Constantes.INDICEACTUAL,clienteSel);

        return resp+ultimo;
    }
    public void agregarMuestra(InformeCompraDetalle det){
        muestrasCap.add(det);
    }
    public void cargarMuestras(int id){
        muestrasCap=detalleRepo.getAllSencillo(id);
    }

    public LiveData<String> getRutaFoto(int idfoto){
        return imagenDetRepository.findRuta(idfoto);

    }
    public ImagenDetalle getFoto(int idfoto){
        return imagenDetRepository.findsimple(idfoto);

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
                                if(Constantes.ESTATUSINFORME[revvis.getEstatus()].equals("ABIERTO")){
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
        Log.d(TAG,"wwwwwwwww"+info.informe.getId()+info.informe.getVisitasId());

        InformeEnvio envio=new InformeEnvio();
        //busco la visita pero tiene que estar en estatussync pendiente
        //   VisitaRepositoryImpl vrepo=new VisitaRepositoryImpl(getContext());
        Visita visita=visitaRepository.findsimple(info.informe.getVisitasId());
        Log.d(TAG,"wwwwwwwww"+info.informe.getId()+"---"+visita.getTiendaNombre());
        if(visita.getEstatusSync()==0)
            envio.setVisita(visita);
        envio.setInformeCompra(info.informe);
        envio.setProductosEx(prodRepo.getAllsimple(visita.getId()));
        //busco los detalles
        List<InformeCompraDetalle> detalles=detalleRepo.getAllSencillo(info.informe.getId());
        envio.setInformeCompraDetalles(detalles);
        envio.setImagenDetalles(buscarImagenes(visita,info.informe,detalles));
        return envio;
    }

    public List<ImagenDetalle> buscarImagenes(Visita visita, InformeCompra informe, List<InformeCompraDetalle> detalles){
        //todas las fotos aqui
        List<ImagenDetalle> fotosinfo=new ArrayList<>();
        //la visita
        ImagenDetalle imagenDetalle=getFoto(visita.getFotoFachada());
        fotosinfo.add(imagenDetalle);

        //las del informe
        List<Integer> arrFotos=new ArrayList<>();
        arrFotos.add(informe.getCondiciones_traslado());
        arrFotos.add(informe.getTicket_compra());
        List<ImagenDetalle> imagenDetalles=imagenDetRepository.findListsencillo(arrFotos);
        fotosinfo.addAll(imagenDetalles);

        //las del los detalles
        for(InformeCompraDetalle detalle:detalles) {
            List<Integer> fotos=detalleRepo.getInformesWithImagen(detalle.getId());
            List<ImagenDetalle> imagenDetalles2=imagenDetRepository.findListsencillo(fotos);
            fotosinfo.addAll(imagenDetalles2);

        }

        //las de producto ex
        List<ImagenDetalle> productoExhibidos=prodRepo.getImagenByVisitasimple(visita.getId());
        fotosinfo.addAll(productoExhibidos);

       return fotosinfo;


    }
    public boolean guardarResp(String resp,String nombrecampo,String tabla,int consecutivo, boolean isPregunta){
        InformeTemp temporal=new InformeTemp();
        temporal.setNombre_campo(nombrecampo);
        temporal.setValor(resp);
        temporal.setTabla(tabla);
        temporal.setConsecutivo(consecutivo);
        temporal.setPregunta(isPregunta);
        try {
            //reviso si ya existe
            InformeTemp editar=itemprepo.findByNombre(nombrecampo);
            if(editar!=null){
                editar.setNombre_campo(nombrecampo);
                editar.setValor(resp);
                editar.setTabla(tabla);
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



    public void guardarVisita() {

        int id= (int) imagenDetRepository.insertImg(fotoFachada);

        visita.setFotoFachada(id);
        // Guardar visita
         idVisita=(int)visitaRepository.insert(visita);

         if(idInformeNuevo>0) {


             clientesFoto[0] = "pepsi";

             int i=0;
            //crear los informes y guardar
            for(ImagenDetalle foto:fotoExhibicion) {
                int idfoto= (int) imagenDetRepository.insertImg(foto);
                informe=new InformeCompra();


                informe.setProducto_exhibido(idfoto);
                informe.setClientesId(1);
                informe.setClienteNombre(clientesFoto[i]);
                repository.insertInformeCompra(informe);
                i++;


            }

        }
        if(idVisita>0) {
            mSnackbarText.setValue(new Event<>(R.string.added_informe_message));
        }
    }



        public void actualizarVisita(){
        try {
            //actualizo la foto
            imagenDetRepository.insert(fotoFachada);
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
    public long insertarInfdeTemp(){
        this.informe=tempToIC();
        return repository.insertInformeCompra(this.informe);
    }
    public void eliminarTblTemp(){
        itemprepo.deleteAll();
    }
    public InformeCompra tempToIC(){
        //consulto el informe
        InformeCompra nuevo=new InformeCompra();
        List<InformeTemp> temps=itemprepo.getAllByTabla("I");
        for(InformeTemp info:temps){
            Class claseCargada = InformeCompra.class;
            Class params[] = new Class[1];
            params[0] = String.class;
            try {
                if(info.getNombre_campo().equals("segundaMuestra")) {
                    params[0] = Boolean.class;
                }

                if(info.getNombre_campo().equals("ticket_compra")) {
                    this.ticket_compra = new ImagenDetalle();
                //    this.ticket_compra.setIndice(Constantes.INDICEACTUAL);
                    this.ticket_compra.setRuta(info.getValor());

                    this.ticket_compra.setDescripcion("ticket compra");
                    this.ticket_compra.setEstatus(1);
                    this.ticket_compra.setIndice(visita.getIndice());
                    this.ticket_compra.setEstatusSync(0);
                    this.ticket_compra.setCreatedAt(new Date());
                }else
                if(info.getNombre_campo().equals("condiciones_traslado")) {
                    this.condiciones_traslado = new ImagenDetalle();
                    this.condiciones_traslado.setRuta(info.getValor());
                    this.condiciones_traslado.setDescripcion("condiciones traslado");
                    this.condiciones_traslado.setEstatus(1);
                    this.condiciones_traslado.setIndice(visita.getIndice());
                    this.condiciones_traslado.setEstatusSync(0);
                    this.condiciones_traslado.setCreatedAt(new Date());
                }else {
                    Method metodo = claseCargada.getDeclaredMethod("set" + ComprasUtils.upperCaseFirst(info.getNombre_campo()));

                    metodo.invoke(nuevo, info.getValor());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return nuevo;
    }

    public void actualizarInforme() {
        //conservo el id
        InformeCompra compra2=tempToIC();
        //recupero los comentarios
        informe.setComentarios(compra2.getComentarios());

        //validaciones
        int idt= (int) imagenDetRepository.insertImg(ticket_compra);

        informe.setTicket_compra(idt);
        int idc= (int) imagenDetRepository.insertImg(condiciones_traslado);

        informe.setCondiciones_traslado(idc);
        // Guardar categoría
        repository.insertInformeCompra(informe);

        mSnackbarText.setValue(new Event<>(R.string.added_informe_message));


    }

    public void finalizarInforme() {

       repository.actualizarEstatus(informe.getId(),2);

        mSnackbarText.setValue(new Event<>(R.string.informe_finalizado));
        //aqui se enviará


    }
    public void finalizarVisita(int idvisita) {
        Log.d("NuevoInformeViewModel", "finalizando"+idvisita);
        visitaRepository.actualizarEstatus(idvisita,2);
        this.eliminarTblTemp();
    }


    public LiveData<InformeCompra> buscarInforme(int id){
        return repository.getInformeCompra(id);
    }

    public LiveData<Visita> buscarVisita(int id){
        Log.d("NuevoInformeViewModel", "Buscando visita");
        return visitaRepository.find(id);
    }
    public ImagenDetalle getFotoFachada() {
        return fotoFachada;
    }

    public void setFotoFachada(ImagenDetalle fotoFachada) {
        this.fotoFachada = fotoFachada;
    }

    public List<ImagenDetalle> getFotoExhibicion() {
        return fotoExhibicion;
    }

    public void setFotoExhibicion(List<ImagenDetalle> fotoExhibicion) {
        this.fotoExhibicion = fotoExhibicion;
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


    public int getIdInformeNuevo() {
        return idInformeNuevo;
    }

    public void setIdInformeNuevo(int idInformeNuevo) {
        this.idInformeNuevo = idInformeNuevo;
    }




}