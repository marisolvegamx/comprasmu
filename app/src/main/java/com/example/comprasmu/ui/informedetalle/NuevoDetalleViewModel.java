package com.example.comprasmu.ui.informedetalle;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.Reactivo;
import com.example.comprasmu.data.repositories.CatalogoDetalleRepositoryImpl;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ReactivoRepositoryImpl;
import com.example.comprasmu.ui.informe.NuevoinformeViewModel;
import com.example.comprasmu.utils.CampoForm;
import com.example.comprasmu.utils.CreadorFormulario;
import com.example.comprasmu.utils.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class NuevoDetalleViewModel extends AndroidViewModel {


    public InformeCompraDetalle icdNuevo;
    private ReactivoRepositoryImpl repositoryReac;
    CreadorFormulario cf;
    List<CampoForm> camposForm;
    LiveData<List<Reactivo>> reactivos;
     public ImagenDetalle foto_codigo_produccion;
    public ImagenDetalle energia;
    public ImagenDetalle foto_num_tienda;
    public ImagenDetalle foto_atributoa;
    public ImagenDetalle foto_atributob;
    public ImagenDetalle foto_atributoc;
    public ImagenDetalle marca_traslape;
    public ImagenDetalle etiqueta_evaluacion;
    private ImagenDetRepositoryImpl imagenDetRepository;
    private int iddetalleNuevo;
    private InformeComDetRepositoryImpl detalleRepo;
    private final MutableLiveData<Event<Integer>> mSnackbarText = new MutableLiveData<>();
    public ProductoSel productoSel;
    public LiveData<List<CatalogoDetalle>> atributos;
    public LiveData<List<CatalogoDetalle>> tomadoDe;
    private CatalogoDetalleRepositoryImpl catRepo;


    public NuevoDetalleViewModel(@NonNull Application application) {
        super(application);


        this.imagenDetRepository=new ImagenDetRepositoryImpl(application);
        this.detalleRepo=new InformeComDetRepositoryImpl(application);
        this.catRepo=new CatalogoDetalleRepositoryImpl(application);
    }
    public void cargarCatalogos(){
        atributos=catRepo.getxCatalogo("atributos");
        tomadoDe=catRepo.getxCatalogo("ubicacion_muestra");

    }
    public void saveDetalle1(){
        this.iddetalleNuevo=(int) detalleRepo.insert(this.icdNuevo);
        mSnackbarText.setValue(new Event<>(R.string.added_informe_message));
    }
    //devuelve el nuevo id
    public int saveDetalle2() {

        if(foto_atributoa!=null) { //guardo las fotos
            int ida = (int) imagenDetRepository.insertImg(foto_atributoa);

            icdNuevo.setFoto_atributoa(ida);
        }
        if(foto_atributob!=null) { //guardo las fotos
        int idb= (int) imagenDetRepository.insertImg(foto_atributob);

        icdNuevo.setFoto_atributob(idb);
        }
        if(foto_atributoc!=null) { //guardo las fotos
        int idc= (int) imagenDetRepository.insertImg(foto_atributoc);

        icdNuevo.setFoto_atributoc(idc);
        }
        if(foto_codigo_produccion!=null) { //guardo las fotos
        int idcp= (int) imagenDetRepository.insertImg(foto_codigo_produccion);

        icdNuevo.setFoto_codigo_produccion(idcp);
        }
        if(foto_num_tienda!=null) { //guardo las fotos
        int idnum= (int) imagenDetRepository.insertImg(foto_num_tienda);

        icdNuevo.setFoto_num_tienda(idnum);
        }
        if(energia!=null) { //guardo las fotos
        int ide= (int) imagenDetRepository.insertImg(energia);

        icdNuevo.setEnergia(ide);
        }
        if(marca_traslape!=null) { //guardo las fotos
        int idm= (int) imagenDetRepository.insertImg(marca_traslape);

        icdNuevo.setMarca_traslape(idm);
        }
        if(etiqueta_evaluacion!=null) { //guardo las fotos
            int idet = (int) imagenDetRepository.insertImg(etiqueta_evaluacion);

            icdNuevo.setEtiqueta_evaluacion(idet);
        }
       int id=(int) detalleRepo.insert(icdNuevo);
        Log.d("NuevoDetalleViewModel","El informe se creo correctamente");
        mSnackbarText.setValue(new Event<>(R.string.added_informe_message));
        return id;
    }
    public void eliminarMuestra(InformeCompraDetalle informeCompraDetalle){
        //reviso que el informe no esté finalizado y que no se haya enviado
      //  detalleRepo.find(iddetalle).observeForever(new Observer<InformeCompraDetalle>() {
       //     @Override
        //    public void onChanged(InformeCompraDetalle informeCompraDetalle) {
                if(informeCompraDetalle.getEstatusSync()==0){
                    //no se ha enviado y se puede eliminar
                    detalleRepo.delete(informeCompraDetalle);

                }
         //   }
        //});

        mSnackbarText.setValue(new Event<>(R.string.delete_muestra_message));

    }
    public void setProductoSel(ListaCompraDetalle productoSel,String nombrePlanta,int plantaSel,int clienteSel,String clienteNombre, String siglas) {
        this.productoSel=new ProductoSel();
        this.productoSel.producto=productoSel.getProductoNombre();
        this.productoSel.productoid=productoSel.getProductosId();
        this.productoSel.compraSel=productoSel.getListaId();
        this.productoSel.compradetalleSel=productoSel.getId();
        this.productoSel.presentacion=productoSel.getTamanio();
        this.productoSel.empaque=productoSel.getEmpaque();
        this.productoSel.idempaque=productoSel.getEmpaquesId();
        //para la planta y cliente busco el encabezado

         this.productoSel.plantaNombre=nombrePlanta;
         this.productoSel.plantaSel=plantaSel;
        this.productoSel.analisis=productoSel.getTipoAnalisis();
        this.productoSel.tipoAnalisis=productoSel.getAnalisisId();
        this.productoSel.tipoMuestra=productoSel.getTipoMuestra();
        this.productoSel.nombreTipoMuestra=productoSel.getNombreTipoMuestra();
        this.productoSel.clienteNombre=clienteNombre;
        this.productoSel.clienteSel=clienteSel;
        this.productoSel.siglas=siglas;
    }
    public void setProductoSel(InformeCompraDetalle productoSel,String nombrePlanta,int plantaSel) {
        this.productoSel=new ProductoSel();
        this.productoSel.producto=productoSel.getProducto();
        this.productoSel.productoid=productoSel.getProductoId();
        this.productoSel.plantaNombre=nombrePlanta;
        this.productoSel.plantaSel=plantaSel;
        this.productoSel.analisis=productoSel.getNombreAnalisis();
        this.productoSel.tipoAnalisis=productoSel.getTipoAnalisis();
        this.productoSel.presentacion=productoSel.getPresentacion();

       // this.productoSel.analisis=productoSel.geta;
        // this.productoSel.clienteNombre=productoSel.get
        //       this.productoSel.plantaNombre=
    }


    public LiveData<InformeCompraDetalle> getMuestra(int id){
        return detalleRepo.find(id);
    }

    public LiveData<List<Reactivo>> getReactivos() {
        return reactivos;
    }
    public ImagenDetalle getFoto_codigo_produccion() {
        return foto_codigo_produccion;
    }

    public void setFoto_codigo_produccion(ImagenDetalle foto_codigo_produccion) {
        this.foto_codigo_produccion = foto_codigo_produccion;
    }


    public ImagenDetalle getEnergia() {
        return energia;
    }

    public void setEnergia(ImagenDetalle energia) {
        this.energia = energia;
    }

    public ImagenDetalle getFoto_num_tienda() {
        return foto_num_tienda;
    }

    public void setFoto_num_tienda(ImagenDetalle foto_num_tienda) {
        this.foto_num_tienda = foto_num_tienda;
    }

    public ImagenDetalle getFoto_atributoa() {
        return foto_atributoa;
    }

    public void setFoto_atributoa(ImagenDetalle foto_atributoa) {
        this.foto_atributoa = foto_atributoa;
    }

    public ImagenDetalle getFoto_atributob() {
        return foto_atributob;
    }

    public void setFoto_atributob(ImagenDetalle foto_atributob) {
        this.foto_atributob = foto_atributob;
    }

    public ImagenDetalle getFoto_atributoc() {
        return foto_atributoc;
    }

    public void setFoto_atributoc(ImagenDetalle foto_atributoc) {
        this.foto_atributoc = foto_atributoc;
    }

    public ImagenDetalle getEtiqueta_evaluacion() {
        return etiqueta_evaluacion;
    }

    public void setEtiqueta_evaluacion(ImagenDetalle etiqueta_evaluacion) {
        this.etiqueta_evaluacion = etiqueta_evaluacion;
    }
    public class ProductoSel{
        public String producto;
        public int productoid;
        public int compraSel;
        public int compradetalleSel;
        public String presentacion;
        public String empaque;
        public int idempaque;

        public String clienteNombre;
        public String plantaNombre;

        public int plantaSel;
        public int clienteSel;
        public String analisis;
        public int tipoAnalisis;
        public int tipoMuestra;
        public String nombreTipoMuestra;
        public String siglas;

        public ProductoSel() {

        }
    }
}