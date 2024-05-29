package com.example.comprasmu.ui.gasto;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.comprasmu.EtiquetadoxCliente;
import com.example.comprasmu.data.dao.InformeGastoDetDao_Impl;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.DetalleCaja;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeEnvioDet;
import com.example.comprasmu.data.modelos.InformeEnvioPaq;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.modelos.InformeGastoDet;
import com.example.comprasmu.data.modelos.InformeTemp;
import com.example.comprasmu.data.modelos.Reactivo;
import com.example.comprasmu.data.remote.InformeEnvPaqEnv;
import com.example.comprasmu.data.remote.InformeEtapaEnv;
import com.example.comprasmu.data.remote.InformeGastoEnv;
import com.example.comprasmu.data.repositories.CatalogoDetalleRepositoryImpl;
import com.example.comprasmu.data.repositories.DetalleCajaRepoImpl;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InfEtapaDetRepoImpl;
import com.example.comprasmu.data.repositories.InfEtapaRepositoryImpl;
import com.example.comprasmu.data.repositories.InfGastoDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeEnvioRepositoryImpl;
import com.example.comprasmu.data.repositories.ReactivoRepositoryImpl;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.Event;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
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
        envio.setInformeGastoDet(this.getInformeDet(idNuevo));
        List<ImagenDetalle> imagenes=this.buscarImagenes(envio.getInformeGastoDet());

        envio.setImagenDetalles(imagenes);
        return envio;
    }
    public List<InformeGastoDet> getInformeDet(int id){
        return gasdetrepo.getAllSencillo(id);
    }
    public float calcularTotal(int idNuevo) {


        float total=0;
        for(InformeGastoDet detalle:this.getInformeDet(idNuevo)){

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
                ImagenDetalle imagen=imagenDetRepository.findsimple(fotoid);
                imagenes.add(imagen);
            }catch(NumberFormatException ex){
                compraslog.grabarError(TAG+" "+ex.getMessage());
            }


        }
        return  imagenes;
    }
}