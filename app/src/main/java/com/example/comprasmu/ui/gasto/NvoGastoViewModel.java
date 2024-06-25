package com.example.comprasmu.ui.gasto;

import android.app.Application;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.PeticionesServidor;

import com.example.comprasmu.data.dao.ConfiguracionRepositoryImpl;
import com.example.comprasmu.data.dao.ListaCompraDao;
import com.example.comprasmu.data.modelos.CatalogoDetalle;

import com.example.comprasmu.data.modelos.Configuracion;
import com.example.comprasmu.data.modelos.ImagenDetalle;

import com.example.comprasmu.data.modelos.InformeEtapa;

import com.example.comprasmu.data.modelos.InformeGastoDet;

import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.remote.InformeGastoEnv;
import com.example.comprasmu.data.repositories.CatalogoDetalleRepositoryImpl;

import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;

import com.example.comprasmu.data.repositories.InfEtapaRepositoryImpl;
import com.example.comprasmu.data.repositories.InfGastoDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.Constantes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//para preparacion, etiquetado y empaque
public class NvoGastoViewModel extends AndroidViewModel {

    final String TAG="NvoGastoViewModel";
    Application application;
      private final InfGastoDetRepositoryImpl gasdetrepo;
    private final ImagenDetRepositoryImpl imagenDetRepository;
    private final InfEtapaRepositoryImpl infEtaRepository;
    ComprasLog compraslog;
    public NvoGastoViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        this.gasdetrepo=new InfGastoDetRepositoryImpl(application);
        imagenDetRepository = new ImagenDetRepositoryImpl(application);
        infEtaRepository = new InfEtapaRepositoryImpl(application);
        compraslog=ComprasLog.getSingleton();
    }
    public int insertarGastoDet(InformeGastoDet nvoDet) {
        int idnvo=(int)this.gasdetrepo.insert(nvoDet);
        return idnvo;

    }
    public List<CatalogoDetalle> cargarConceptos(){
        CatalogoDetalleRepositoryImpl catRepo=new CatalogoDetalleRepositoryImpl(application);

        return catRepo.getxCatalogo("tipo_gastos");

    }

    public void actualizarImagen(InformeGastoDet informe, String nvaruta)  {

        ImagenDetalle fotoSello = this.getFoto(informe.getFotocomprob());
        //borro la anterior
        if(!nvaruta.equals(fotoSello.getRuta()))
            this.eliminarImagen(fotoSello.getRuta());
        fotoSello.setRuta(nvaruta);

        imagenDetRepository.insert(fotoSello);

    }
    public void eliminarImagen(String rutaimg)  {
        File dir=application.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(dir!=null) {
            try {
                File fdelete2 = new File(dir, rutaimg);
                if (fdelete2.exists()) {
                    boolean resp = fdelete2.delete();
                   compraslog.grabarError(TAG,"eliminarImagen","*eliminando archivo " + Environment.DIRECTORY_PICTURES + rutaimg + "--" + resp);
                    //la tabla la borra en borrar activity
                }
            }catch(NullPointerException ex){
               compraslog.grabarError(TAG,"eliminarImagen", "error al borrar el archivo");

            }
        }
    }
    public ImagenDetalle getFoto(int idfoto) {

        //return imagenDetRepository.find(visita.getFotoFachada());
        return imagenDetRepository.findsimpleInd(idfoto,Constantes.INDICEACTUAL);
    }
    public void actualizarDet(InformeGastoDet informe){
        gasdetrepo.insert(informe);
    }
    public InformeEtapa getInformexId(int id){
        return   infEtaRepository.findsimple(id);
    }
    public InformeGastoEnv prepararInformeEnv(int idNuevo) {
        InformeGastoEnv envio=new InformeGastoEnv();

        envio.setInformeEtapa(getInformexId(idNuevo));

        envio.setClaveUsuario(Constantes.CLAVEUSUARIO);
        envio.setIndice(Constantes.INDICEACTUAL);
        envio.setInformeGastoDet(this.getGastoDetalles(idNuevo));
        List<ImagenDetalle> imagenes=this.buscarImagenes(envio.getInformeGastoDet());

        envio.setImagenDetalles(imagenes);
        return envio;
    }
    public List<InformeGastoDet> getGastoDetalles(int id){
        return gasdetrepo.getAllSencillo(id);
    }
    public float calcularTotal(int idNuevo) {


        float total=0;
        for(InformeGastoDet detalle:this.getGastoDetalles(idNuevo)){

            try {
                total =total+ detalle.getImporte();

            }catch(NumberFormatException ex){
                compraslog.grabarError(TAG+" "+ex.getMessage());
            }


        }
        return  total;
    }

    public List<ImagenDetalle>  buscarImagenes(List<InformeGastoDet> informeEtapaDet) {
        List<ImagenDetalle> imagenes=new ArrayList<>();
        for(InformeGastoDet detalle:informeEtapaDet){
            int fotoid=0;
            try {
                fotoid = detalle.getFotocomprob();
                if(fotoid>0) {
                    ImagenDetalle imagen = imagenDetRepository.findsimple(fotoid);
                    imagenes.add(imagen);
                }
            }catch(NumberFormatException ex){
                compraslog.grabarError(TAG+" "+ex.getMessage());
            }


        }
        return  imagenes;
    }

    public void getTotalMuestras(String ciudadInf, NvoGastoFragment.ListenerM listenerM){
        PeticionesServidor ps=new PeticionesServidor(Constantes.CLAVEUSUARIO);
        ps.getTotalMuestras(Constantes.INDICEACTUAL,ciudadInf,listenerM);

    }
    /**valido si ya puedo hacer gastos***/

    public  boolean  validarEtapa(String ciudadSel){
        ListaCompraRepositoryImpl repository;
        ListaCompraDao dao= ComprasDataBase.getInstance(application).getListaCompraDao();
        repository = ListaCompraRepositoryImpl.getInstance(dao);
        List<ListaCompra> listacomp= repository.getAllByIndiceCiudadSimpl(Constantes.INDICEACTUAL,ciudadSel);
        for (ListaCompra lista:listacomp
             ) {
            if(lista.getLis_etapaactual()!=6)//no está en la 6
            {
                return false;
            }
        }
        if(listacomp!=null)
        return true;
        return false;
    }

    public void guardarTotalmu(float respuesta) {
        ConfiguracionRepositoryImpl confrepo=new ConfiguracionRepositoryImpl(application);
        Configuracion conf=new Configuracion();
        conf.setClave("total_muestras");
        conf.setValor(respuesta+"");
        //veo si ya existe el campo
        Configuracion edit=confrepo.findsimple("total_muestras");
        if(edit!=null) {
            edit.setValor(respuesta+"");
            confrepo.insert(edit);
        }else
        confrepo.insert(conf);
    }

    public String getTotalmu() {
        ConfiguracionRepositoryImpl confrepo=new ConfiguracionRepositoryImpl(application);
        Configuracion conf=new Configuracion();

        Configuracion edit=confrepo.findsimple("total_muestras");
       if(edit!=null)
          return   edit.getValor();
       return  "";
    }
}