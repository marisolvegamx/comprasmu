package com.example.comprasmu;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.modelos.Contrato;
import com.example.comprasmu.data.modelos.TablaVersiones;
import com.example.comprasmu.data.remote.RespInformesResponse;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;
import com.example.comprasmu.utils.Constantes;

import java.text.SimpleDateFormat;
import java.util.Date;

/***para actualizar los informes despues de la supervision*******/
public class DescRespInformes {


    InformeComDetRepositoryImpl infdrepo;

    InformeCompraRepositoryImpl infrepo;

    SimpleDateFormat sdfdias;
    TablaVersionesRepImpl tvRepo;
    String TAG="DescRespInfAsyncTask";



    Activity act;

    private final DescRespInformes.ProgresoRespListener miproglis;
   // DescargaRespListener listener;

    public DescRespInformes(Activity act, DescRespInformes.ProgresoRespListener miproglis, TablaVersionesRepImpl tvRepo) {

        this.act = act;
        this.tvRepo=tvRepo;
        sdfdias=new SimpleDateFormat("dd-MM-yyyy");
        this.miproglis=miproglis;
       // this.listener=new DescargaRespListener();
       }



    public void getInformes(){

        Log.d("DescargasIniAsyncTask", "descargando respaldo inf");
        infrepo = new InformeCompraRepositoryImpl(act);
        infdrepo = new InformeComDetRepositoryImpl(act);

        PeticionesServidor ps=new PeticionesServidor(Constantes.CLAVEUSUARIO);
        DescargaRespListener listener=new DescargaRespListener();
        ps.pedirRespaldoSup(Constantes.INDICEACTUAL,listener);

    }


    public interface ProgresoRespListener {
        void todoBien();

    }

    public class DescargaRespListener {
        public DescargaRespListener() {

        }

        public void finalizar() {
            miproglis.todoBien();


        }


        public void actualizarInformes(RespInformesResponse infoResp) {
            Log.d(TAG, "actualizando bd informes");
            //primero los inserts
            if (infoResp != null) {

                if (infoResp.getInformeCompra() != null && infoResp.getInformeCompra().size() > 0) {
                    //como puede que ya existan reviso primero e inserto unoxuno
                    infrepo.insertAll(infoResp.getInformeCompra());
                }
                if (infoResp.getInformeCompraDetalles() != null && infoResp.getInformeCompraDetalles().size() > 0) {
                    //como puede que ya existan reviso primero e inserto unoxuno
                    infdrepo.insertAll(infoResp.getInformeCompraDetalles());
                }
            }
            //actualizar version en tabla
            TablaVersiones tinfo = new TablaVersiones();
            tinfo.setNombreTabla(Contrato.TBLINFORMESCOMP);
            Date fecha1 = new Date();
            Log.d(TAG, "insertando fecha version 1" + fecha1);

            tinfo.setVersion(fecha1);
            tinfo.setIndice(Constantes.INDICEACTUAL);
            tinfo.setTipo("I");
            TablaVersiones tinfod = new TablaVersiones();
            tinfod.setNombreTabla(Contrato.TBLINFORMESDET);
            tinfod.setVersion(fecha1);
            tinfod.setTipo("I");
            tinfod.setIndice(Constantes.INDICEACTUAL);
            tvRepo.insertUpdate(tinfo);
            tvRepo.insertUpdate(tinfod);
            finalizar();
        }
    }



}
