package com.example.comprasmu.ui.informe;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.Visita;
import com.example.comprasmu.data.repositories.CatalogoDetalleRepositoryImpl;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.VisitaRepositoryImpl;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.Event;

import java.util.HashMap;
import java.util.List;

public class VisitaViewModel extends AndroidViewModel {
    public VisitaViewModel(@NonNull Application application) {
        super(application);
    }



//
//    private HashMap<Integer,String> tomadoDe;
//    private HashMap<Integer,String> atributos;
//    private final MutableLiveData<Event<Integer>> mSnackbarText = new MutableLiveData<>();
//
//    private final VisiRepositoryImpl inforepository;
//    private final InformeComDetRepositoryImpl detrepository;
//    private ImagenDetRepositoryImpl imagenDetRepository;
//    private CatalogoDetalleRepositoryImpl catsRepo;
//
//    public InformeCompra informe;
//
//
//    public ImagenDetalle foto_codigo_produccion;
//
//    public ImagenDetalle energia;
//    public ImagenDetalle foto_num_tienda;
//    public ImagenDetalle foto_atributoa;
//    public ImagenDetalle foto_atributob;
//    public ImagenDetalle foto_atributoc;
//    public ImagenDetalle marca_traslape;
//    public ImagenDetalle etiqueta_evaluacion;
//    public ImagenDetalle ticket_compra;
//    public ImagenDetalle condiciones_traslado;
//    public String[] clientesFoto;
//
//
//    public InformeCompraDetalle icdNuevo;
//    //para activar la camara
//    private MutableLiveData<Boolean> isTomarFoto;
//    private int idInformeNuevo;
//
//
//    public VisitaViewModel(@NonNull Application application) {
//        super(application);
//        this.repository = new InformeCompraRepositoryImpl(application);
//        this.imagenDetRepository=new ImagenDetRepositoryImpl(application);
//        this.detalleRepo=new InformeComDetRepositoryImpl(application);
//        this.catsRepo=new CatalogoDetalleRepositoryImpl(application);
//        this.visitaRepository=new VisitaRepositoryImpl(application);
//        listaPlantas = new MutableLiveData<>();
//        listaClientes = new MutableLiveData<>();
//        tiposTienda = new MutableLiveData<>();
//        informe     =new InformeCompra();
//        HashMap<Integer,String> plantas=new HashMap();
//        plantas.put(1,"Planta 1");
//        plantas.put(3,"Planta 1");
//        HashMap<Integer,String> clientes=new HashMap();
//        clientes.put(1,"Cliente 1");
//        clientes.put(2,"Cliente 1");
//        HashMap<Integer,String> tipos=new HashMap();
//        tipos.put(1,"Grande");
//        tipos.put(2,"Pequeña");
//        listaPlantas.setValue(plantas );
//        listaClientes.setValue(clientes);
//        tiposTienda.setValue(tipos);
//
//
//    }
//
//    public MutableLiveData<HashMap<Integer, String>> getListaPlantas() {
//        return listaPlantas;
//    }
//
//    public MutableLiveData<HashMap<Integer, String>> getListaClientes() {
//        return listaClientes;
//    }
//
//    public MutableLiveData<HashMap<Integer, String>> getTiposTienda() {
//        return tiposTienda;
//    }
//
//    public int getConsecutivo() {
//        consecutivo=1;
//        LiveData<Integer> resp= repository.getLastConsecutivoInforme(Constantes.INDICEACTUAL);
//        resp.observeForever( new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//                if(integer>0){
//                    consecutivo=integer;
//
//                }
//            }
//        });
//        return consecutivo;
//    }
//    public void cargarCatsContinuar(){
//        tomadoDe = new HashMap<Integer,String>();
//        atributos = new HashMap<Integer,String>();
//
//        catsRepo.getAll(1).observeForever(new Observer<List<CatalogoDetalle>>() {
//            @Override
//            public void onChanged(List<CatalogoDetalle> catalogoDetalles) {
//                if(catalogoDetalles!=null)
//                    tomadoDe= ComprasUtils.catalogoToHashMap(catalogoDetalles);
//            }
//        });
//        catsRepo.getAll(2).observeForever(new Observer<List<CatalogoDetalle>>() {
//            @Override
//            public void onChanged(List<CatalogoDetalle> catalogoDetalles) {
//                if(catalogoDetalles!=null)
//                    atributos=ComprasUtils.catalogoToHashMap(catalogoDetalles);
//            }
//        });
//
//    }
//
//    public void guardarVisita() {
//        //validaciones
//
//
//
//
//      /*
//        // Realizar validaciones del estado
//        if (name.getValue() == null) {
//            mErrorText.setValue(new Event<>(R.string.category_name_error));
//            return;
//        }
//
//        if (name.getValue().isEmpty()) {
//            mErrorText.setValue(new Event<>(R.string.category_name_error));
//            return;
//        }*/
//
//
//
//        int id= (int) imagenDetRepository.insertImg(fotoFachada);
////TODO:actualizar el id reporte en la fotos insertadas
//        visita.setFotoFachada(id);
//        // Guardar visita
//        idVisita=(int)visitaRepository.insert(visita);
//
//     /*   if(idInformeNuevo>0) {
//            //crear los detalle y guardar
//            for(ImagenDetalle foto:fotoExhibicion) {
//                int idfoto= (int) imagenDetRepository.insertImg(foto);
//                 icdNuevo = new InformeCompraDetalle();
//                icdNuevo.setInformesId(idInformeNuevo);
//                icdNuevo.setProducto_exhibido(idfoto);
//                icdNuevo.setClientesId(1);
//                icdNuevo.setClienteNombre(clientesFoto[0]);
//                detalleRepo.insert(icdNuevo);
//
//
//            }
//            mSnackbarText.setValue(new Event<>(R.string.added_informe_message));
//        }*/
//    }
//    public void guardarInforme() {
//        //validaciones
//
//
//
//
//      /*
//        // Realizar validaciones del estado
//        if (name.getValue() == null) {
//            mErrorText.setValue(new Event<>(R.string.category_name_error));
//            return;
//        }
//
//        if (name.getValue().isEmpty()) {
//            mErrorText.setValue(new Event<>(R.string.category_name_error));
//            return;
//        }*/
//
//
//        idInformeNuevo=(int)repository.insertInformeCompra(informe);
//
//     /*   if(idInformeNuevo>0) {
//            //crear los detalle y guardar
//            for(ImagenDetalle foto:fotoExhibicion) {
//                int idfoto= (int) imagenDetRepository.insertImg(foto);
//                 icdNuevo = new InformeCompraDetalle();
//                icdNuevo.setInformesId(idInformeNuevo);
//                icdNuevo.setProducto_exhibido(idfoto);
//                icdNuevo.setClientesId(1);
//                icdNuevo.setClienteNombre(clientesFoto[0]);
//                detalleRepo.insert(icdNuevo);
//
//
//            }
//            mSnackbarText.setValue(new Event<>(R.string.added_informe_message));
//        }*/
//    }
//
//    public void actualizarInforme() {
//        //validaciones
//        int idt= (int) imagenDetRepository.insertImg(ticket_compra);
//
//        informe.setTicket_compra(idt);
//        int idc= (int) imagenDetRepository.insertImg(condiciones_traslado);
//
//        informe.setCondiciones_traslado(idc);
//        // Guardar categoría
//        int idInforme=(int)repository.insertInformeCompra(informe);
//
//        mSnackbarText.setValue(new Event<>(R.string.added_informe_message));
//
//
//    }
//
//    public void finalizarInforme() {
//
//        repository.actualizarEstatus(informe.getId(),2);
//
//        mSnackbarText.setValue(new Event<>(R.string.informe_finalizado));
//        //aqui se enviará
//
//
//    }
//    public void saveDetalle() {
//
//        //guardo las fotos
//        int ida= (int) imagenDetRepository.insertImg(foto_atributoa);
//
//        icdNuevo.setFoto_atributoa(ida);
//        int idb= (int) imagenDetRepository.insertImg(foto_atributob);
//
//        icdNuevo.setFoto_atributob(idb);
//        int idc= (int) imagenDetRepository.insertImg(foto_atributoc);
//
//        icdNuevo.setFoto_atributoc(idc);
//        int idcp= (int) imagenDetRepository.insertImg(foto_codigo_produccion);
//
//        icdNuevo.setFoto_codigo_produccion(idcp);
//        int idnum= (int) imagenDetRepository.insertImg(foto_num_tienda);
//
//        icdNuevo.setFoto_num_tienda(idnum);
//        int ide= (int) imagenDetRepository.insertImg(energia);
//
//        icdNuevo.setEnergia(ide);
//
//        int idm= (int) imagenDetRepository.insertImg(marca_traslape);
//
//        icdNuevo.setMarca_traslape(idm);
//        int idet= (int) imagenDetRepository.insertImg(etiqueta_evaluacion);
//
//        icdNuevo.setEtiqueta_evaluacion(idet);
//
//        detalleRepo.insert(icdNuevo);
//        mSnackbarText.setValue(new Event<>(R.string.added_informe_message));
//
//    }
//
//    public LiveData<InformeCompra> buscarInforme(int id){
//        return repository.getInformeCompra(id);
//    }
//    public LiveData<Visita> buscarVisita(int id){
//        return visitaRepository.find(id);
//    }
//    public ImagenDetalle getFotoFachada() {
//        return fotoFachada;
//    }
//
//    public void setFotoFachada(ImagenDetalle fotoFachada) {
//        this.fotoFachada = fotoFachada;
//    }
//
//    public List<ImagenDetalle> getFotoExhibicion() {
//        return fotoExhibicion;
//    }
//
//    public void setFotoExhibicion(List<ImagenDetalle> fotoExhibicion) {
//        this.fotoExhibicion = fotoExhibicion;
//    }
//
//    public MutableLiveData<Event<Integer>> getSnackbarText() {
//        return mSnackbarText;
//    }
//
//    public String[] getClientesFoto() {
//        return clientesFoto;
//    }
//
//    public void setClientesFoto(String[] clientesFoto) {
//        this.clientesFoto = clientesFoto;
//    }
//
//    public HashMap<Integer, String> getTomadoDe() {
//        return tomadoDe;
//    }
//
//    public void setTomadoDe(HashMap<Integer, String> tomadoDe) {
//        this.tomadoDe = tomadoDe;
//    }
//
//    public HashMap<Integer, String> getAtributos() {
//        return atributos;
//    }
//
//    public void setAtributos(HashMap<Integer, String> atributos) {
//        this.atributos = atributos;
//    }
//
//    public InformeCompraDetalle getIcdNuevo() {
//        return icdNuevo;
//    }
//
//    public void setIcdNuevo(InformeCompraDetalle icdNuevo) {
//        this.icdNuevo = icdNuevo;
//    }
//
//    public ImagenDetalle getFoto_codigo_produccion() {
//        return foto_codigo_produccion;
//    }
//
//    public void setFoto_codigo_produccion(ImagenDetalle foto_codigo_produccion) {
//        this.foto_codigo_produccion = foto_codigo_produccion;
//    }
//
//    public ImagenDetalle getEnergia() {
//        return energia;
//    }
//
//    public void setEnergia(ImagenDetalle energia) {
//        this.energia = energia;
//    }
//
//    public ImagenDetalle getFoto_num_tienda() {
//        return foto_num_tienda;
//    }
//
//    public void setFoto_num_tienda(ImagenDetalle foto_num_tienda) {
//        this.foto_num_tienda = foto_num_tienda;
//    }
//
//    public ImagenDetalle getFoto_atributoa() {
//        return foto_atributoa;
//    }
//
//    public void setFoto_atributoa(ImagenDetalle foto_atributoa) {
//        this.foto_atributoa = foto_atributoa;
//    }
//
//    public ImagenDetalle getFoto_atributob() {
//        return foto_atributob;
//    }
//
//    public void setFoto_atributob(ImagenDetalle foto_atributob) {
//        this.foto_atributob = foto_atributob;
//    }
//
//    public ImagenDetalle getFoto_atributoc() {
//        return foto_atributoc;
//    }
//
//    public void setFoto_atributoc(ImagenDetalle foto_atributoc) {
//        this.foto_atributoc = foto_atributoc;
//    }
//
//    public ImagenDetalle getEtiqueta_evaluacion() {
//        return etiqueta_evaluacion;
//    }
//
//    public void setEtiqueta_evaluacion(ImagenDetalle etiqueta_evaluacion) {
//        this.etiqueta_evaluacion = etiqueta_evaluacion;
//    }
//
//    public ImagenDetalle getTicket_compra() {
//        return ticket_compra;
//    }
//
//    public void setTicket_compra(ImagenDetalle ticket_compra) {
//        this.ticket_compra = ticket_compra;
//    }
//    public MutableLiveData<Boolean> isTomarFoto() {
//        return isTomarFoto;
//
//    }
//
//    public int getIdInformeNuevo() {
//        return idInformeNuevo;
//    }
//
//    public void setIdInformeNuevo(int idInformeNuevo) {
//        this.idInformeNuevo = idInformeNuevo;
//    }
//
//    public void setIsTomarFoto(boolean tomarFoto) {
//        this.isTomarFoto.setValue(tomarFoto);
//    }


}