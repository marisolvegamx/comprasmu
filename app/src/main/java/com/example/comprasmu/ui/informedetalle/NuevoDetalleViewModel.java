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
import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.modelos.Atributo;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.Contrato;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeTemp;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.Reactivo;
import com.example.comprasmu.data.modelos.Sigla;
import com.example.comprasmu.data.modelos.Sustitucion;
import com.example.comprasmu.data.modelos.Visita;
import com.example.comprasmu.data.remote.InformeEtapaEnv;
import com.example.comprasmu.data.repositories.AtributoRepositoryImpl;
import com.example.comprasmu.data.repositories.CatalogoDetalleRepositoryImpl;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeTempRepositoryImpl;
import com.example.comprasmu.data.repositories.ReactivoRepositoryImpl;

import com.example.comprasmu.data.repositories.SiglaRepositoryImpl;
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
import java.util.Locale;


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
    public ImagenDetalle foto_atributod;
    public ImagenDetalle fotoqr;
    private final ImagenDetRepositoryImpl imagenDetRepository;
    private final AtributoRepositoryImpl atrRepo;
    private int iddetalleNuevo;
    private final InformeComDetRepositoryImpl detalleRepo;
    InformeTempRepositoryImpl itemprepo;
    private final MutableLiveData<Event<Integer>> mSnackbarText = new MutableLiveData<>();
    public ProductoSel productoSel;
    public LiveData<List<Atributo>> atributos;
    public List<Atributo> satributos;
    public List<CatalogoDetalle> tomadoDe;
    public List<CatalogoDetalle> causas;
    private final CatalogoDetalleRepositoryImpl catRepo;
    private final ReactivoRepositoryImpl reacRepo;
    public LiveData<List<Reactivo>> listaPreguntas;
    final String TAG="NvoDetVM";
    Application application;
    public int reactivoAct;
   // public SiglaRepositoryImpl sigRepo;
    public NuevoDetalleViewModel(@NonNull Application application) {
        super(application);

        this.application=application;
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

    public void buscarCausas(){
        causas=catRepo.getxCatalogo("causas");

    }
    /**busca el catalogo mandado por el campo***/
    public List<CatalogoDetalle> buscarCatalogoGen(String campo){
        return catRepo.getxCatalogo(campo);

    }
    //ver si tiene informe la visita y devuelve el cliente o los clientes
   /* public Integer[] tieneInforme(Visita visita){
        Integer[] clienteAnt=null;
        InformeCompraRepositoryImpl infoRepo=new InformeCompraRepositoryImpl(application);
        List<InformeCompra> informeCompras=infoRepo.getAllByVisitasimple(visita.getId());
        if(informeCompras!=null&&informeCompras.size()>0) {
            clienteAnt=new Integer[informeCompras.size()];
            for (int i = 0; i < informeCompras.size(); i++)
                clienteAnt[i] = informeCompras.get(i).getClientesId();
        }


        return clienteAnt;

    }*/

    //ver si tiene informe la visita y devuelve el cliente o los clientes
    public Integer[] tieneInforme(Visita visita){
        Integer[] clienteAnt=null;
        InformeCompraRepositoryImpl infoRepo=new InformeCompraRepositoryImpl(application);
        List<InformeCompra> informeCompras=infoRepo.getAllByVisitasimple(visita.getId());
        if(informeCompras!=null&&informeCompras.size()>0) {
            clienteAnt=new Integer[informeCompras.size()];
            for (int i = 0; i < informeCompras.size(); i++)
                clienteAnt[i] = informeCompras.get(i).getPlantasId();
        }


        return clienteAnt;

    }

    public InformeTemp getUltimoTemp(){
        //reivos si hay respuestas temporales si no devuelvo null
        return itemprepo.getUltimo(true);
    }

    public InformeTemp getPenultimoTemp(){
        //reivos si hay respuestas temporales si no devuelvo null
        return itemprepo.getPenultimo(true);
    }

    public InformeTemp buscarxNombreCam(String campo){
        //reivos si hay respuestas temporales si no devuelvo null
        return itemprepo.findByNombre(campo);
    }
    public InformeTemp buscarxNombreCam(String campo,int nummuestra){
        //reivos si hay respuestas temporales si no devuelvo null
        return itemprepo.findByNombreMues(campo,nummuestra);
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

    public Reactivo buscarReactivoSimpl(int id){
        return reacRepo.findsimple(id);
    }

    public Reactivo buscarReactivoAnterior(int id,boolean isEdicion){
       InformeTemp inftemp;
        if(isEdicion){
            inftemp=itemprepo.getUltimoxId(id);
        }else
            inftemp=itemprepo.getUltimo(true);
        //busco el reactivo
        if(inftemp!=null) {

                return reacRepo.findByNombre(inftemp.getNombre_campo(), inftemp.getClienteSel());
        }else
            return null;
    }
    public InformeTemp buscarTempxId(int id){


            return itemprepo.findsimple(id);

    }
    public Reactivo inftempToReac(InformeTemp inftemp){
        int cli=inftemp.getClienteSel();

        return reacRepo.findByNombre(inftemp.getNombre_campo(),cli);

    }

    public Reactivo buscarReactivoxId(int campo){
        return reacRepo.findsimple(campo);

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
        if(foto_atributod!=null) { //guardo las fotos
            int idet = (int) imagenDetRepository.insertImg(foto_atributod);

            icdNuevo.setFoto_atributod(idet);
        }


       int id=(int) detalleRepo.insert(icdNuevo);
        Log.d("NuevoDetalleViewModel","El informe se creo correctamente");

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
    public boolean buscarMuestraCodigo(String indice, int planta, ProductoSel productosel, String codigonvo, Date caducidad, LifecycleOwner lco, String codigosperm){
        Log.d(TAG, "buscando codigo"+caducidad);
        //reviso si es fecha permitid
        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yy");

        if(!codigosperm.equals(""));
        {
            codigosperm=codigosperm.replace("=","");
            String[] fechas=codigosperm.split(";");

            for(int j=0;j<fechas.length;j++){
                try {
                    if(!fechas[j].equals("")) {
                        Date fechaperm = sdf.parse(fechas[j]);
                        if (fechaperm.equals(caducidad))
                            return false; //esta permitida
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

        }
        //busco en los informes
        List<InformeCompraDetalle> informeCompraDetalles=detalleRepo.getByProductoAna(indice,planta,productosel.productoid,productosel.tipoAnalisis,productosel.idempaque,productosel.presentacion);
        Log.d(TAG,"buscando codigo igual"+informeCompraDetalles.size());
        for(InformeCompraDetalle det:informeCompraDetalles) {
            Log.d(TAG, "buscando codigo igual" + det.getCaducidad());
            //recorro el informe buscando
            if (det.getCaducidad().equals(caducidad)) {

                return true; //tengo uno

                   }
               }

        return false;
    }
    public boolean buscarMuestraCodigoPen(String indice, int planta, ProductoSel productosel, String codigonvo, Date caducidad, LifecycleOwner lco, String codigosperm){
        Log.d(TAG, "buscando codigo"+caducidad);
        //reviso si es fecha permitid
        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yy");

        if(!codigosperm.equals(""));
        {
            codigosperm=codigosperm.replace("=","");
            String[] fechas=codigosperm.split(";");

            for(int j=0;j<fechas.length;j++){
                try {
                    if(!fechas[j].equals("")) {
                        Date fechaperm = sdf.parse(fechas[j]);
                        if (fechaperm.equals(caducidad))
                            return false; //esta permitida
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

        }
        Log.d(TAG,"SI TRAigo siglas"+productosel.siglas);
        //busco en los informes
        List<InformeCompraDetalle> informeCompraDetalles=detalleRepo.getByProductoAnaPen(indice,planta,productosel.productoid,productosel.tipoAnalisis,productosel.idempaque,productosel.presentacion, productosel.siglas);
        Log.d(TAG,"buscando codigo igual"+informeCompraDetalles.size());
        for(InformeCompraDetalle det:informeCompraDetalles) {
            Log.d(TAG, "buscando codigo igual" + det.getCaducidad());
            //recorro el informe buscando
            if (det.getCaducidad().equals(caducidad)) {

                return true; //tengo uno

            }
        }

        return false;
    }
   /* public void buscarPlantaPen(String siglas,int cliente, DetalleProductoPenFragment.EnvioListener listener ){
        try {
            PeticionesServidor ps = new PeticionesServidor(Constantes.CLAVEUSUARIO);
             ps.getPlantaPeniafiel(siglas,cliente,listener);

        }catch(Exception ex){
            Log.e(TAG,"Error al hacer peticion");
        }

    }*/

    public void buscarPlantaPen2(String siglas,int cliente, DetalleProductoPenFragment.EnvioListener listener ){
        try {
            SiglaRepositoryImpl sigRepo=new SiglaRepositoryImpl(this.application);
            Sigla siglaResp=sigRepo.getByClienteSig(siglas,cliente);
            listener.guardarRespuestaInf(siglaResp);
            //ps.getPlantaPeniafiel(siglas,cliente,listener);

        }catch(Exception ex){
            Log.e(TAG,"Error al hacer peticion");
        }

    }
    public InformeCompraDetalle tempToID(int numMuestra){
        //consulto el informe
        InformeCompraDetalle nuevo=new InformeCompraDetalle();
        List<InformeTemp> temps=itemprepo.getAllByTabla("ID",numMuestra);
        Log.d(TAG,"guardando  muestra"+numMuestra+"--"+temps.size());
        for(InformeTemp info:temps){

            Class claseCargada = InformeCompraDetalle.class;

            Class[] params = new Class[1];
            if(info.getNombre_campo().equals(""))
                continue;
            if(info.getNombre_campo().equals("foto_codigo_produccion"))
                continue;
            if(info.getNombre_campo().equals("foto_atributoa"))
                continue;
            if(info.getNombre_campo().equals("foto_atributob"))
                continue;
            if(info.getNombre_campo().equals("foto_atributoc"))
                continue;
            if(info.getNombre_campo().equals("caducidad")) {

                params[0] = Date.class;
                Date nval=null;
                try {
                    nval=Constantes.sdfcaducidad.parse(info.getValor());
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
             //   Log.d("NuevoDetVM",">>>>"+nuevo.getCaducidad());
            } else   if(info.getNombre_campo().equals("causaSustId")) {

                params[0] = Integer.class;
                Integer nval=null;
                try {
                    nval=Integer.parseInt(info.getValor());
                } catch (NumberFormatException e) {
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
                //   Log.d("NuevoDetVM",">>>>"+nuevo.getCaducidad());
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

        return nuevo;
    }
    public ImagenDetalle crearImagendeTmp(String descripcion){
        InformeTemp temps=itemprepo.findByNombre(descripcion);

        if(temps==null||temps.getValor().equals("")){
            return null;
        }
        ImagenDetalle     foto=new ImagenDetalle();
      //  foto.setRuta(temps.getValor().toLowerCase());
        foto.setRuta(temps.getValor());
        foto.setDescripcion(descripcion);
        foto.setEstatus(1);
        foto.setIndice(Constantes.INDICEACTUAL);
        foto.setEstatusSync(0);
        foto.setCreatedAt(new Date());
        return foto;
    }
    public String buscarCodigosIndice(String indice, int planta, ProductoSel productosel){
        List<InformeCompraDetalle> informeCompraDetalles=detalleRepo.getByProductoAna(indice,planta,productosel.productoid,productosel.tipoAnalisis,productosel.idempaque,productosel.presentacion);
       String codigosnuevos="";
        for(InformeCompraDetalle det:informeCompraDetalles) {
            Log.d(TAG, "buscando codigo igual" + det.getCaducidad());
            //recorro el informe buscando
            codigosnuevos=codigosnuevos+";"+det.getCaducidad();
        }
        return codigosnuevos;
    }

   public List<Integer> muestrasTotales(){
        return itemprepo.getMuestras();
   }

   public int insertarMuestra(int idInformeNuevo, int numMuestra){
        this.icdNuevo=tempToID(numMuestra);
        this.icdNuevo.setInformesId(idInformeNuevo);
        this.icdNuevo.setNumMuestra(numMuestra);

        //las fotos se agregan al model
        this.icdNuevo.setEstatusSync(0);
        this.icdNuevo.setEstatus(1);
        this.icdNuevo.setCreatedAt(new Date());
       //para las fotos

     //   this.energia = crearImagendeTmp( Contrato.TablaInformeDet.ENERGIA);

        this.foto_codigo_produccion = crearImagendeTmp(Contrato.TablaInformeDet.FOTOCODIGOPRODUCCION);
        this.foto_num_tienda = crearImagendeTmp(Contrato.TablaInformeDet.FOTONUMTIENDA);
      //  this.marca_traslape = crearImagendeTmp( Contrato.TablaInformeDet.MARCA_TRASLAPE);

        this.foto_atributoa = crearImagendeTmp( Contrato.TablaInformeDet.FOTO_ATRIBUTOA);
        this.foto_atributob = crearImagendeTmp(Contrato.TablaInformeDet.FOTO_ATRIBUTOB);
        this.foto_atributoc = crearImagendeTmp(Contrato.TablaInformeDet.FOTO_ATRIBUTOC);
        this.etiqueta_evaluacion = crearImagendeTmp( Contrato.TablaInformeDet.ETIQUETA_EVALUACION);
        //this.fotoazucares = crearImagendeTmp( Contrato.TablaInformeDet.AZUCARES);
       this.foto_atributod = crearImagendeTmp(Contrato.TablaInformeDet.FOTO_ATRIBUTOD);
        int nuevoid = this.saveDetalle2();


        return nuevoid;
    }
    public void limpiarVarsMuestra(){
        this.icdNuevo=null;
        this.energia=null;
        this.foto_codigo_produccion=null;
        this.foto_num_tienda = null;
        this.marca_traslape =null;

        this.foto_atributoa = null;
        this.foto_atributob =null;
        this.foto_atributoc = null;
        this.etiqueta_evaluacion = null;
        this.foto_atributod = null;

    }

    public void setProductoSel(ListaCompraDetalle productoSel,String nombrePlanta,int plantaSel,int clienteSel,String clienteNombre, String siglas, ListaCompraDetalle prodbu) {
        this.productoSel=new ProductoSel();
        this.productoSel.producto=productoSel.getProductoNombre();
        this.productoSel.productoid=productoSel.getProductosId();
        this.productoSel.compraSel=productoSel.getListaId();
        this.productoSel.compradetalleSel=productoSel.getId(); //el id del prod pedido
        this.productoSel.presentacion=productoSel.getTamanio();
        this.productoSel.tamanioId=productoSel.getTamanioId();
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
        this.productoSel.codigosperm=productoSel.getLid_fechapermitida();
        if(prodbu!=null) {
            this.productoSel.comprasDetIdbu = prodbu.getId();//el detalle por el cual se cambio de la misma lista
            this.productoSel.comprasIdbu = productoSel.getListaId(); //guardo datos del producto original el efectivamnete comprado va en el otro campo
        }
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
        this.productoSel.tamanioId=productoSel.getTamanioId();
       // this.productoSel.codigosperm=productoSel.getLid_fechapermitida();
       // this.productoSel.analisis=productoSel.geta;
        // this.productoSel.clienteNombre=productoSel.get
        //       this.productoSel.plantaNombre=
    }
    public void setProductoSelSust(ListaCompraDetalle productoSel, String nombrePlanta, int plantaSel, int clienteSel, String clienteNombre, String siglas, Sustitucion prodbu) {
        this.productoSel=new ProductoSel();
        this.productoSel.producto=productoSel.getProductoNombre();
        this.productoSel.productoid=productoSel.getProductosId();
        this.productoSel.compraSel=productoSel.getListaId();
        this.productoSel.compradetalleSel=productoSel.getId();
        this.productoSel.presentacion=productoSel.getTamanio();
        this.productoSel.tamanioId=productoSel.getTamanioId();
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
        this.productoSel.codigosperm=productoSel.getLid_fechapermitida();
        if(prodbu!=null) {
           // this.productoSel.comprasDetIdbu = prodbu.getId_sustitucion();
           this.productoSel.comprasIdbu = productoSel.getListaId(); //va a ser igual solo para indicar que hubo sust.
            this.productoSel.comprasDetIdbu=prodbu.getId_sustitucion();
        }
     //   this.productoSel.codigosnop=productoSel.getCodigosNoPermitidos();
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

    public ProductoSel fromTemp(){
        List<InformeTemp> respuestas= itemprepo.getProductoSel();
        this.productoSel=new ProductoSel();
        this.productoSel.producto=this.getValor(respuestas,"producto");
        try {
            this.productoSel.productoid = Integer.parseInt(this.getValor(respuestas, "productoId"));
        }catch(NumberFormatException ex){
            Log.e("NuevoDetVM","error al parasar a int el valor de productoId"+ex.getMessage());
        }
        try{
            this.productoSel.compraSel=Integer.parseInt(this.getValor(respuestas,"comprasId"));
         }catch(NumberFormatException ex){
            Log.e("NuevoDetVM","error al parasar a int el valor de comprasId"+ex.getMessage());
         }
        try{
            this.productoSel.compradetalleSel=Integer.parseInt(this.getValor(respuestas,"comprasDetId"));
        }catch(NumberFormatException ex){
            Log.e("NuevoDetVM","error al parasar a int el valor de comprasDetId"+ex.getMessage());
        }
        this.productoSel.presentacion=this.getValor(respuestas,"presentacion");
        try {
            this.productoSel.tamanioId = Integer.parseInt(this.getValor(respuestas, "tamanioId"));
        }catch(NumberFormatException ex){
            Log.e("NuevoDetVM","error al parasar a int el valor de tamanioId"+ex.getMessage());
        }
        try{ this.productoSel.idempaque=Integer.parseInt(this.getValor(respuestas,"empaquesId"));
        }catch(NumberFormatException ex){
        Log.e("NuevoDetVM","error al parasar a int el valor de empaquesId"+ex.getMessage());
        }
        //es el id
        try{ this.productoSel.tipoAnalisis=Integer.parseInt(this.getValor(respuestas,"tipoAnalisis"));
        }catch(NumberFormatException ex){
            Log.e("NuevoDetVM","error al parasar a int el valor de tipoAnalisis"+ex.getMessage());
        }
        this.productoSel.analisis=this.getValor(respuestas,"nombreAnalisis");

        //para la planta y cliente busco el encabezado
        this.productoSel.plantaNombre=this.getValor(respuestas,"plantaNombre");
        try{
            this.productoSel.plantaSel=Integer.parseInt(this.getValor(respuestas,"plantasId"));
        }catch(NumberFormatException ex){
            Log.e("NuevoDetVM","error al parasar a int el valor de plantasId"+ex.getMessage());
        }
        try{
             this.productoSel.tipoMuestra=Integer.parseInt(this.getValor(respuestas,"tipoMuestra"));
        }catch(NumberFormatException ex){
            Log.e("NuevoDetVM","error al parasar a int el valor de tipoMuestra"+ex.getMessage());
        }
        this.productoSel.nombreTipoMuestra=this.getValor(respuestas,"nombreTipoMuestra");
        this.productoSel.empaque=this.getValor(respuestas,"empaque");
        this.productoSel.codigosnop=this.getValor(respuestas,"codigosnop");
        this.productoSel.codigosperm=this.getValor(respuestas,"codigosperm");
        this.productoSel.clienteNombre=this.getValor(respuestas,"clienteNombre");
        try{this.productoSel.clienteSel=Integer.parseInt(this.getValor(respuestas,"clientesId"));
        }catch(NumberFormatException ex){
        Log.e("NuevoDetVM","error al parasar a int el valor de clientesId"+ex.getMessage());
        }
        this.productoSel.siglas=this.getValor(respuestas,"siglaspla");


        return this.productoSel;

    }
    public String getValor( List<InformeTemp> respuestas,String campo){
        for(InformeTemp respuesta:respuestas){
            if(respuesta.getNombre_campo().equals(campo)){
                return respuesta.getValor();
            }
        }
        return "";
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

    public void setCausas(List<CatalogoDetalle> causas) {
        this.causas = causas;
    }

    public List<CatalogoDetalle> getCausas() {
        return causas;
    }

    public ImagenDetalle getFoto_atributoc() {
        return foto_atributoc;
    }

    public void setFoto_atributoc(ImagenDetalle foto_atributoc) {
        this.foto_atributoc = foto_atributoc;
    }

    public int getIddetalleNuevo() {
        return iddetalleNuevo;
    }

    public void setIddetalleNuevo(int iddetalleNuevo) {
        this.iddetalleNuevo = iddetalleNuevo;
    }

    public ImagenDetalle getEtiqueta_evaluacion() {
        return etiqueta_evaluacion;
    }

    public void setEtiqueta_evaluacion(ImagenDetalle etiqueta_evaluacion) {
        this.etiqueta_evaluacion = etiqueta_evaluacion;
    }
    public static class ProductoSel{
        public String producto;
        public int productoid;
        public int compraSel;
        public int compradetalleSel;
        public String presentacion;
        public int tamanioId;
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
        public String codigosperm;
        public int comprasIdbu;
        public int comprasDetIdbu; //aqui guardaré de cuál se hizo backup


        public ProductoSel() {

        }
    }
}