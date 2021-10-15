package com.example.comprasmu.ui.informe;

import android.app.Application;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.Contrato;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeWithDetalle;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.ProductoExhibido;
import com.example.comprasmu.data.modelos.Visita;
import com.example.comprasmu.data.repositories.CatalogoDetalleRepositoryImpl;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.repositories.ProductoExhibidoRepositoryImpl;
import com.example.comprasmu.data.repositories.VisitaRepositoryImpl;
import com.example.comprasmu.ui.BackActivity;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.prefs.BackingStoreException;

public class NuevoinformeViewModel extends AndroidViewModel {

    private MutableLiveData<HashMap<Integer,String>> listaPlantas;
    private MutableLiveData<HashMap<Integer,String>> listaClientes;
    private MutableLiveData<HashMap<Integer,String>> tiposTienda;

    private final MutableLiveData<Event<Integer>> mSnackbarText = new MutableLiveData<>();

    private final InformeCompraRepositoryImpl repository;

    private ImagenDetRepositoryImpl imagenDetRepository;
    private  CatalogoDetalleRepositoryImpl catsRepo;
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

    public LiveData<List<InformeCompraDetalle>> muestras; //para guardar las muestras que se van creando
    private int idInformeNuevo;
    private int iddetalleNuevo;
    public Visita visita;
    public LiveData<Visita> visitaEdicion;
    public LiveData<InformeCompra> informeEdicion;
    public int idVisita;
    public boolean mIsNew;


    public NuevoinformeViewModel(@NonNull Application application) {
        super(application);
        this.repository = new InformeCompraRepositoryImpl(application);
        this.imagenDetRepository=new ImagenDetRepositoryImpl(application);
        this.detalleRepo=new InformeComDetRepositoryImpl(application);
        this.catsRepo=new CatalogoDetalleRepositoryImpl(application);
        this.visitaRepository=new VisitaRepositoryImpl(application);
        prodRepo=new ProductoExhibidoRepositoryImpl(application);
        listaPlantas = new MutableLiveData<>();
        listaClientes = new MutableLiveData<>();
        tiposTienda = new MutableLiveData<>();
        informe     =new InformeCompra();
        HashMap<Integer,String> plantas=new HashMap();
        plantas.put(1,"Planta 1");
        plantas.put(3,"Planta 1");
        HashMap<Integer,String> clientes=new HashMap();
        clientes.put(1,"Cliente 1");
        clientes.put(2,"Cliente 1");
        HashMap<Integer,String> tipos=new HashMap();
        tipos.put(1,"Grande");
        tipos.put(2,"Pequeña");
       listaPlantas.setValue(plantas );
       listaClientes.setValue(clientes);
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
    public LiveData<String> getRutaFoto(int idfoto){
        return imagenDetRepository.findRuta(idfoto);

    }
    public LiveData<ImagenDetalle> getFoto(int idfoto){
        return imagenDetRepository.find(idfoto);

    }

    public List<ImagenDetalle> buscarImagenes(Visita visita, InformeCompra informe, List<InformeCompraDetalle> detalles){
        //todas las fotos aqui
        List<ImagenDetalle> fotosinfo=new ArrayList<>();
        //la visita
        getFoto(visita.getFotoFachada()).observeForever(new Observer<ImagenDetalle>() {
            @Override
            public void onChanged(ImagenDetalle imagenDetalle) {
                fotosinfo.add(imagenDetalle);
            }
        });
        //las del informe
        List<Integer> arrFotos=new ArrayList<>();
        arrFotos.add(informe.getCondiciones_traslado());
        arrFotos.add(informe.getTicket_compra());
        imagenDetRepository.findList(arrFotos).observeForever(new Observer<List<ImagenDetalle>>() {
            @Override
            public void onChanged(List<ImagenDetalle> imagenDetalles) {
                fotosinfo.addAll(imagenDetalles);
            }
        });
        //las del los detalles
        for(InformeCompraDetalle detalle:detalles) {
            List<Integer> fotos=detalleRepo.getInformesWithImagen(detalle.getId());
            imagenDetRepository.findList(fotos).observeForever(new Observer<List<ImagenDetalle>>() {
                @Override
                public void onChanged(List<ImagenDetalle> imagenDetalles) {
                    fotosinfo.addAll(imagenDetalles);
                }
            });
        }

        //las de producto ex
        prodRepo.getImagenByVisita(visita.getId()).observeForever( new Observer<List<ImagenDetalle>>(){
                @Override
            public void onChanged(List<ImagenDetalle> productoExhibidos) {
                    fotosinfo.addAll(productoExhibidos);
            }
        });
    return fotosinfo;


    }


    public void cargarMuestras(){
        muestras=detalleRepo.getAll(idInformeNuevo);
    }

    public void guardarVisita() {

        int id= (int) imagenDetRepository.insertImg(fotoFachada);

        visita.setFotoFachada(id);
        // Guardar visita
         idVisita=(int)visitaRepository.insert(visita);

         if(idInformeNuevo>0) {
             //para pruebas
             ImagenDetalle fotoe=new ImagenDetalle();
             fotoe.setRuta("algo x aqui");
             fotoe.setDescripcion("preuba");
             fotoe.setCreatedAt(new Date());
             fotoe.setEstatusSync(0);
             fotoe.setEstatus(1);


             clientesFoto[0] = "pepsi";
             fotoExhibicion.add(fotoe);
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
            Log.e("NuevoInformeViewModel.actualizarvisita",ex.getMessage());
            mSnackbarText.setValue(new Event<>(R.string.added_informe_error));
        }

    }
    public long guardarInforme() {

        int idInforme=(int)repository.insertInformeCompra(informe);

      return idInforme;

    }

    public void actualizarInforme() {
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