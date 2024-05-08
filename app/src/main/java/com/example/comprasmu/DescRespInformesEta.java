package com.example.comprasmu;

import android.app.Activity;
import android.util.Log;
import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.modelos.Contrato;
import com.example.comprasmu.data.modelos.DetalleCaja;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.modelos.TablaVersiones;
import com.example.comprasmu.data.remote.RespInfEtapaResponse;
import com.example.comprasmu.data.remote.RespInformesResponse;
import com.example.comprasmu.data.remote.RespNotifEtiqResponse;
import com.example.comprasmu.data.repositories.DetalleCajaRepoImpl;
import com.example.comprasmu.data.repositories.InfEtapaDetRepoImpl;
import com.example.comprasmu.data.repositories.InfEtapaRepositoryImpl;
import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;
import com.example.comprasmu.utils.Constantes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/***para actualizar los informes despues de la supervision*******/
public class DescRespInformesEta {


    InfEtapaDetRepoImpl infdrepo;
    InfEtapaRepositoryImpl infrepo;
    DetalleCajaRepoImpl cajarepo;

    SimpleDateFormat sdfdias;
    TablaVersionesRepImpl tvRepo;
    String TAG="DescRespInformesEta";

    Activity act;

    private final DescRespInformesEta.ProgresoRespIEListener miproglis;
   // DescargaRespListener listener;

    public DescRespInformesEta(Activity act, DescRespInformesEta.ProgresoRespIEListener miproglis, TablaVersionesRepImpl tvRepo) {

        this.act = act;
        this.tvRepo=tvRepo;
        sdfdias=new SimpleDateFormat("dd-MM-yyyy");
        this.miproglis=miproglis;
       // this.listener=new DescargaRespListener();
       }
       //traigo todos los informes de etiq por si alguno cambio qr
    public void getCambiosSupEtiq(){


    }
    public void getCambiosEtiq(){

        Log.d(TAG, "descargando cambios etiq");
        infrepo = new InfEtapaRepositoryImpl(act);
        infdrepo = new InfEtapaDetRepoImpl(act);
        cajarepo=new DetalleCajaRepoImpl(act);
        PeticionesServidor ps=new PeticionesServidor(Constantes.CLAVEUSUARIO);
        DescargaRespieListener listener=new DescargaRespieListener();
        ps.getCambiosEtiq(Constantes.INDICEACTUAL,listener);

    }


    public interface ProgresoRespIEListener {
        void finalizarrespie();

    }

    public class DescargaRespieListener {
        public DescargaRespieListener() {

        }


/*este todavia no esta usandose*/
        public void actualizarQr(RespInfEtapaResponse infoResp) {
            Log.d(TAG, "actualizando bd informes");
            //primero los inserts
            if (infoResp != null) {

                if (infoResp.getInformeEtapaDet() != null && infoResp.getInformeEtapaDet().size() > 0) {
                   //solo actualizo qr
                    for (InformeEtapaDet det:infoResp.getInformeEtapaDet()
                         ) {
                        infdrepo.actQr(det.getId(),det.getQr());
                    }

                }

            }
            //actualizar version en tabla
            TablaVersiones tinfo = new TablaVersiones();
            tinfo.setNombreTabla(Contrato.TBLINFORMESETAPADET);
            Date fecha1 = new Date();
            Log.d(TAG, "insertando fecha version 1" + fecha1);

            tinfo.setVersion(fecha1);
            tinfo.setIndice(Constantes.INDICEACTUAL);
            tinfo.setTipo("I");
            tvRepo.insertUpdate(tinfo);

            miproglis.finalizarrespie();

    }
        public void actualizarInformesEtiq(RespNotifEtiqResponse infoResp) {
            Log.d(TAG, "actualizando bd informes");

            if (infoResp != null) {

                if (infoResp.getEtiq_cancel() != null && infoResp.getEtiq_cancel().size() > 0) {
                    for(InformeEtapaDet det:infoResp.getEtiq_cancel()){

                        infdrepo.actEstatus(det.getId(),0);

                        //elimino las fotos de las cajas
                        infdrepo.deleteCajaEtiq(det.getInformeEtapaId());
                        //vuelvo a abrir
                        infrepo.actualizarEstatus(det.getInformeEtapaId(), 6);

                    }

                }
                //por si se agregaron muestras
                if (infoResp.getEtiq_comp()!= null && infoResp.getEtiq_comp().size() > 0) {
                    for (InformeEtapa infemp : infoResp.getEtiq_comp()
                    ) {

                        //ahora si cambio estatus

                        infrepo.actualizarEstatus(infemp.getId(), 4);
                        //elimino las fotos de las cajas
                        infdrepo.deleteCajaEtiq(infemp.getId());
                    }
                }
                if (infoResp.getEmp_elim() != null && infoResp.getEmp_elim().size() > 0) {
                    for (InformeEtapa infemp:infoResp.getEmp_elim()
                         ) {
                        //cambio estatus det
                        //busco detalles
                        List<InformeEtapaDet> det=infdrepo.getAllSencillo(infemp.getId());
                        if(det!=null)
                            for (InformeEtapaDet infd : det) {
                               // complog.grabarError("borrando informe etapa det"+infd.getId());
                                //borro los detalles
                                infdrepo.actEstatus(infd.getId(),0);

                            }
                        //cancelo detalle caja
                        List<DetalleCaja> fotoscaja=cajarepo.getAllsimplexInf(infemp.getId());
                        if(fotoscaja!=null&fotoscaja.size()>0) {
                            for (DetalleCaja foto:fotoscaja
                                 ) {

                                cajarepo.actualizarEstatus(foto.getId(), 0);
                            }
                        }
                        //ahora si cancelo el informe

                        infrepo.actualizarEstatus(infemp.getId(),0);
                        infrepo.actMotivoCancel(infemp.getId(),infemp.getMotivoCancel());
                    }
                }
            }
            //actualizar version en tabla
            TablaVersiones tinfo = new TablaVersiones();
            tinfo.setNombreTabla(Contrato.TBLINFORMESETAPA);
            Date fecha1 = new Date();
            Log.d(TAG, "insertando fecha version 1" + fecha1);

            tinfo.setVersion(fecha1);
            tinfo.setIndice(Constantes.INDICEACTUAL);
            tinfo.setTipo("I");
            TablaVersiones tinfod = new TablaVersiones();
            tinfod.setNombreTabla(Contrato.TBLINFORMESETAPADET);
            tinfod.setVersion(fecha1);
            tinfod.setTipo("I");
            tinfod.setIndice(Constantes.INDICEACTUAL);
            tvRepo.insertUpdate(tinfo);
            tvRepo.insertUpdate(tinfod);
            tinfod = new TablaVersiones();
            tinfod.setNombreTabla(Contrato.TBLDETALLECAJA);
            tinfod.setVersion(fecha1);
            tinfod.setTipo("I");
            tinfod.setIndice(Constantes.INDICEACTUAL);
            tvRepo.insertUpdate(tinfo);
            miproglis.finalizarrespie();
        }
    }



}
