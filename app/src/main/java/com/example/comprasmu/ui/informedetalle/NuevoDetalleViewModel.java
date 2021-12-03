package com.example.comprasmu.ui.informedetalle;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.icu.text.IDNA;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.Atributo;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.Contrato;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeTemp;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.Reactivo;
import com.example.comprasmu.data.repositories.AtributoRepositoryImpl;
import com.example.comprasmu.data.repositories.CatalogoDetalleRepositoryImpl;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeTempRepositoryImpl;
import com.example.comprasmu.data.repositories.ReactivoRepositoryImpl;
import com.example.comprasmu.ui.informe.NuevoinformeFragment;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.utils.CampoForm;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;
import com.example.comprasmu.utils.Event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class NuevoDetalleViewModel extends AndroidViewModel {


    public InformeCompraDetalle icdNuevo;

    private ReactivoRepositoryImpl repositoryReac;
    CreadorFormulario cf;
    List<CampoForm> camposForm;

     public ImagenDetalle foto_codigo_produccion;
    public ImagenDetalle energia;
    public ImagenDetalle foto_num_tienda;
    public ImagenDetalle foto_atributoa;
    public ImagenDetalle foto_atributob;
    public ImagenDetalle foto_atributoc;
    public ImagenDetalle marca_traslape;
    public ImagenDetalle etiqueta_evaluacion;
    public ImagenDetalle fotoazucares;
    public ImagenDetalle fotoqr;
    private ImagenDetRepositoryImpl imagenDetRepository;
    private AtributoRepositoryImpl atrRepo;
    private int iddetalleNuevo;
    private InformeComDetRepositoryImpl detalleRepo;
    InformeTempRepositoryImpl itemprepo;
    private final MutableLiveData<Event<Integer>> mSnackbarText = new MutableLiveData<>();
    public ProductoSel productoSel;
    public LiveData<List<Atributo>> atributos;
    public List<Atributo> satributos;
    public List<CatalogoDetalle> tomadoDe;
    private CatalogoDetalleRepositoryImpl catRepo;
    private ReactivoRepositoryImpl reacRepo;
    public LiveData<List<Reactivo>> listaPreguntas;

    public NuevoDetalleViewModel(@NonNull Application application) {
        super(application);


        this.imagenDetRepository=new ImagenDetRepositoryImpl(application);
        this.detalleRepo=new InformeComDetRepositoryImpl(application);
        this.catRepo=new CatalogoDetalleRepositoryImpl(application);
        this.atrRepo=new AtributoRepositoryImpl(application);
        this.reacRepo=new ReactivoRepositoryImpl(application);
        itemprepo=new InformeTempRepositoryImpl(application);
    }
    public void cargarCatalogos(String empaque, int empaqueid, int cliente){
        satributos=atrRepo.getByEmpaque(empaqueid,cliente);
        tomadoDe=catRepo.getxCatalogo("ubicacion_muestra");

    }
    public List<CatalogoDetalle> cargarCatalogos(){


        return catRepo.getxCatalogo("ubicacion_muestra");

    }
    public LiveData<List<Atributo>> getAtributos() {
        return atrRepo.getAll();
    }
    public void buscarReactivos(){
        Log.d("NuevoDetViewM", "buscado reac");
        listaPreguntas= reacRepo.getAll();
    }
    public LiveData<Reactivo> buscarReactivo(int id){
        return reacRepo.find(id);
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
        if(fotoazucares!=null) { //guardo las fotos
            int idet = (int) imagenDetRepository.insertImg(fotoazucares);

            icdNuevo.setAzucares(idet);
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
    public boolean buscarMuestraCodigo(String indice, int planta, ProductoSel productosel, String codigonvo, Date caducidad, LifecycleOwner lco){


        //busco en los informes
        List<InformeCompraDetalle> informeCompraDetalles=detalleRepo.getByProductoAna(indice,planta,productosel.productoid,productosel.tipoAnalisis,productosel.idempaque,productosel.presentacion);

               for(InformeCompraDetalle det:informeCompraDetalles)
                   //recorro el informe buscando
                 if(det.getCaducidad().equals(caducidad)){

                      return true; //tengo uno

                 }

        return false;
    }
    public InformeCompraDetalle tempToID(){
        //consulto el informe
        InformeCompraDetalle nuevo=new InformeCompraDetalle();
        List<InformeTemp> temps=itemprepo.getAllByTabla("ID");
        for(InformeTemp info:temps){

            Class claseCargada = InformeCompraDetalle.class;

            Class params[] = new Class[1];
            if(info.getNombre_campo().equals(""))
                continue;
            if(info.getNombre_campo().equals("siglas"))
                continue;
            if(info.getNombre_campo().equals("caducidad")) {

                params[0] = Date.class;
                Date nval=null;
                try {
                    nval=Constantes.sdfsolofecha.parse(info.getValor());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    Method metodo = claseCargada.getDeclaredMethod("set"+ ComprasUtils.upperCaseFirst(info.getNombre_campo()),params);

                    metodo.invoke(nuevo,nval);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                Log.d("NuevoDetVM",">>>>"+nuevo.getCaducidad());
            }
            else {
                params[0] = String.class;
                try {

                    Method metodo = claseCargada.getDeclaredMethod("set"+ ComprasUtils.upperCaseFirst(info.getNombre_campo()),params);

                    metodo.invoke(nuevo,info.getValor());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }

        }
        InformeCompraDetalle x=nuevo;
        return nuevo;
    }
    public ImagenDetalle crearImagendeTmp(String descripcion){
        InformeTemp temps=itemprepo.findByNombre(descripcion);

        if(temps==null||temps.getValor().equals("")){
            return null;
        }
        ImagenDetalle     foto=new ImagenDetalle();
        foto.setRuta(temps.getValor());
        foto.setDescripcion(descripcion);
        foto.setEstatus(1);
        foto.setIndice(Constantes.INDICEACTUAL);
        foto.setEstatusSync(0);
        foto.setCreatedAt(new Date());
        return foto;
    }


    public int insertarMuestra(int idInformeNuevo, int numMuestra){
        this.icdNuevo=tempToID();
        this.icdNuevo.setInformesId(idInformeNuevo);
        //guardo la lista origen
        this.icdNuevo.setProductoId(this.productoSel.productoid);
        this.icdNuevo.setProducto(this.productoSel.producto);
        this.icdNuevo.setEmpaque(this.productoSel.empaque);
        this.icdNuevo.setEmpaquesId(this.productoSel.idempaque);
        //  siglas.setText(this.productoSel.siglas);

        this.icdNuevo.setTipoMuestra(this.productoSel.tipoMuestra);
        this.icdNuevo.setNombreTipoMuestra(this.productoSel.nombreTipoMuestra);
        this.icdNuevo.setTipoAnalisis(this.productoSel.tipoAnalisis);
        this.icdNuevo.setNombreAnalisis(this.productoSel.analisis);


        //

        this.icdNuevo.setPresentacion(this.productoSel.presentacion);
        this.icdNuevo.setComprasId(this.productoSel.compraSel);
        this.icdNuevo.setComprasDetId(this.productoSel.compradetalleSel);

        this.icdNuevo.setNumMuestra(numMuestra);

        //las fotos se agregan al model
        this.icdNuevo.setEstatusSync(0);
        this.icdNuevo.setEstatus(1);
        this.icdNuevo.setCreatedAt(new Date());
       //para las fotos

        this.energia = crearImagendeTmp( Contrato.TablaInformeDet.ENERGIA);

        this.foto_codigo_produccion = crearImagendeTmp(Contrato.TablaInformeDet.FOTOCODIGOPRODUCCION);
        this.foto_num_tienda = crearImagendeTmp(Contrato.TablaInformeDet.FOTONUMTIENDA);
        this.marca_traslape = crearImagendeTmp( Contrato.TablaInformeDet.MARCA_TRASLAPE);

        this.foto_atributoa = crearImagendeTmp( Contrato.TablaInformeDet.FOTO_ATRIBUTOA);
        this.foto_atributob = crearImagendeTmp(Contrato.TablaInformeDet.FOTO_ATRIBUTOB);
        this.foto_atributoc = crearImagendeTmp(Contrato.TablaInformeDet.FOTO_ATRIBUTOC);
        this.etiqueta_evaluacion = crearImagendeTmp( Contrato.TablaInformeDet.ETIQUETA_EVALUACION);
        this.fotoazucares = crearImagendeTmp( Contrato.TablaInformeDet.AZUCARES);
        int nuevoid = this.saveDetalle2();
        return nuevoid;
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
        this.productoSel.codigosnop=productoSel.getCodigosNoPermitidos();
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
        return listaPreguntas;
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

    public LiveData<List<Reactivo>> getListaPreguntas() {
        return listaPreguntas;
    }

    public void setListaPreguntas(LiveData<List<Reactivo>> listaPreguntas) {
        this.listaPreguntas = listaPreguntas;
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
        public String codigosnop;

        public ProductoSel() {

        }
    }
}