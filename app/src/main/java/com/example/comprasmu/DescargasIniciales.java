package com.example.comprasmu;


import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.dao.ListaCompraDao;
import com.example.comprasmu.data.modelos.Contrato;
import com.example.comprasmu.data.modelos.Geocerca;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.SolicitudCor;
import com.example.comprasmu.data.modelos.TablaVersiones;
import com.example.comprasmu.data.modelos.Visita;
import com.example.comprasmu.data.remote.IActualListener;
import com.example.comprasmu.data.remote.ListaCompraResponse;
import com.example.comprasmu.data.remote.MuestraCancelada;
import com.example.comprasmu.data.remote.NotificacionResponse;
import com.example.comprasmu.data.remote.PostResponse;
import com.example.comprasmu.data.remote.RespInformesResponse;
import com.example.comprasmu.data.remote.SolCorreResponse;
import com.example.comprasmu.data.repositories.GeocercaRepositoryImpl;
import com.example.comprasmu.data.repositories.InfEtapaRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.SolicitudCorRepoImpl;
import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;
import com.example.comprasmu.data.repositories.VisitaRepositoryImpl;
import com.example.comprasmu.ui.gasto.IListenerRevRec;
import com.example.comprasmu.ui.notificaciones.NotificacionGen;
import com.example.comprasmu.ui.solcorreccion.ListaSolsViewModel;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.Constantes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/*****proceso para descargar de forma automatica y periodica los datos desde el servidor***/
public class DescargasIniciales {

        TablaVersionesRepImpl tvRepo;
        SimpleDateFormat sdfdias;
        ListaCompraDetRepositoryImpl lcdrepo;
        ListaCompraRepositoryImpl lcrepo;
        InformeComDetRepositoryImpl infdrepo;
        GeocercaRepositoryImpl georep;
        SolicitudCorRepoImpl solRepo;
        InfEtapaRepositoryImpl  infetarepo;
        VisitaRepositoryImpl visRepo;
        InformeCompraRepositoryImpl infrepo;
        Context act;
        int actualiza;
        int procesos=0;
        int procesos_lev=0; //para saber cuantos si se corrieron
        private ComprasLog flog;
        DescargaIniListener listenprin;
        final String TAG="DescargasIniciales";
        boolean notificar=false;
        public DescargasIniciales(Context act) {
            this.act=act;
            sdfdias=new SimpleDateFormat("dd-MM-yyyy");
            flog = ComprasLog.getSingleton();
            flog.crearLog(act.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getPath());
        }

        public void ejecutar() {

            flog.grabarError(TAG,"ejecutar", "iniciando descargas programadas");
            actualiza=1;
            tvRepo=new TablaVersionesRepImpl(this.act);
            ListaCompraDao dao= ComprasDataBase.getInstance(this.act).getListaCompraDao();
            lcdrepo=new ListaCompraDetRepositoryImpl(this.act);
            lcrepo=ListaCompraRepositoryImpl.getInstance(dao);
            georep=new GeocercaRepositoryImpl(this.act);
            listenprin=new DescargaIniListener();
            solRepo=new SolicitudCorRepoImpl(this.act);
            infetarepo=new InfEtapaRepositoryImpl(this.act);
            visRepo=new VisitaRepositoryImpl(this.act);
            infrepo=new InformeCompraRepositoryImpl(this.act);
          //  listacompras();
            pedirCorrecciones(0,0);
            notificacionesGenerales();
        }


        private void listacompras(){
            Log.d(TAG, "descargando listas"+actualiza);
            flog.grabarError(TAG,"listacompras","descargando listas actualiza="+actualiza);
            PeticionesServidor ps=new PeticionesServidor(Constantes.CLAVEUSUARIO);
           //por ahora no tiene caso la fecha de actualizacion porque no se registra en la tabla, falta mejorar esto en la app web
            // TablaVersiones comp=tvRepo.getVersionByNombreTablasmd(Contrato.TBLLISTACOMPRAS,Constantes.INDICEACTUAL);
           // TablaVersiones det=tvRepo.getVersionByNombreTablasmd(Contrato.TBLLISTACOMPRASDET,Constantes.INDICEACTUAL);
            DescargaIniListener listener=new DescargaIniListener();
            //siempre actualizo
            ps.getListasdeCompra(null,null,Constantes.INDICEACTUAL,listener);
            flog.grabarError(TAG,"listacompras"," siempre actualizo"+actualiza);

        }


    private void buscarListaDet(ListaCompraDetalle compra, List<ListaCompraDetalle> json){
        for (ListaCompraDetalle jcompra: json) {
            if(jcompra.getId()==compra.getId()&&compra.getListaId()==jcompra.getListaId()){
                // Log.d(TAG,compra.getProductoNombre()+"--"+jcompra.getProductoNombre()+".."+compra.getListaId()+"--"+ compra.getId());
                return;
            }
        }

        //si llego aqui, no lo encontré por lo que se eliminó
        //busco que no tenga informe
        infdrepo=new InformeComDetRepositoryImpl(act);
        List<InformeCompraDetalle> prods=infdrepo.findByCompra(compra.getListaId(), compra.getId());
        Log.d(TAG,"prbabbl elimine "+compra.getProductoNombre()+"--"+compra.getListaId()+"--"+ compra.getId());
        if(prods==null||prods.size()<1)
            lcdrepo.delete(compra);

    }
    public void pedirCorrecciones(int actualiza, int etapa) {
        PeticionesServidor ps = new PeticionesServidor(Constantes.CLAVEUSUARIO);
        TablaVersiones comp = tvRepo.getVersionByNombreTablasmd(Contrato.TBLSOLCORRECCIONES, Constantes.INDICEACTUAL);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String version;
        if (comp != null && comp.getVersion() != null) {
            version = sdf.format(comp.getVersion());
        } else //es la 1a vez
        {
            version = "1999-09-09"; //una fecha muy antigua
        }
        if (actualiza == 1) {
            version = "1999-09-09"; //una fecha muy antigua
        }
        //siempre actualizo
        if (NavigationDrawerActivity.isOnlineNet(this.act))
            ps.pedirSolicitudesCorr(Constantes.INDICEACTUAL, etapa, version, new DescargaIniListener());
        else
            notificar = true;

    }

    private void notificacionesGenerales() {

        Log.d(TAG, "notificacionesGenerales "+procesos_lev);

        PeticionesServidor ps=new PeticionesServidor(Constantes.CLAVEUSUARIO);
        DescargaIniListener listener=new DescargaIniListener();
        ps.getNotificacionesGen(Constantes.INDICEACTUAL,Constantes.CIUDADTRABAJO,listener);

    }
public class DescargaIniListener implements  IDescargaIniListener, IActualListener, IListenerRevRec {
    public DescargaIniListener(){


    }
    public void finalizar(){
        Log.d(TAG,"finalizo descarga"+procesos+"--"+procesos_lev);

    }


    public void insertarZonas(List<Geocerca> zonas){
        Log.d(TAG,"fin zonas ");
        if (zonas != null && zonas.size() > 0) {

            //insertar

            for(Geocerca geo:zonas) {
                georep.insert(geo);
            }
            //actualizo tabla versiones
            TablaVersiones tv=new TablaVersiones();
            tv.setNombreTabla("geocercas");
            tv.setTipo("C");
            tv.setVersion(new Date());
            tvRepo.insertUpdate(tv);

            tv=null;
        }

        finalizar();
    }


    public void actualizar(ListaCompraResponse compraResp) {
        //primero los inserts
        if(compraResp!=null) {
            if (compraResp.getInserts() != null) {
                if (compraResp.getInserts().getListaCompra() != null) {
                    Log.d("Descargaini","listacomp<"+compraResp.getInserts().getListaCompra());
                    flog.grabarError(TAG,"actualizar lista compra","listacomp<");

                    lcrepo.insertAll(compraResp.getInserts().getListaCompra()); //inserto blblbl
                }

                if (compraResp.getInserts().getListaCompraDetalle() != null) {
                    //como puede que ya existan reviso primero e inserto unoxuno
                    for (ListaCompraDetalle detalle : compraResp.getInserts().getListaCompraDetalle()) {
                        ListaCompraDetalle existe = lcdrepo.findsimple(detalle.getListaId(), detalle.getId());
                        if (existe == null) {
                            // lcrepo.insert(detalle);

                        } else {  //no reemplazo los comprados ni los nuevos codigos
                            detalle.setComprados(existe.getComprados());
                            detalle.setNvoCodigo(existe.getNvoCodigo());
                            //lcrepo.updateSC(compra);
                        }
                       long id=lcdrepo.insert(detalle);

                    }
                    //reviso los que se eliminaron
                    if(compraResp.getInserts().getListaCompraDetalle()!=null&&compraResp.getInserts().getListaCompraDetalle().size()>0) {
                        //  Log.d(TAG,"buscando elim");
                        List<ListaCompraDetalle> liscompapp = lcdrepo.getAllSimpl();
                        for (ListaCompraDetalle compra : liscompapp
                        ) {
                            //veo si está en el json si no es que se elimina, solo checo que no
                            //tenga informe
                            buscarListaDet(compra, compraResp.getInserts().getListaCompraDetalle());
                        }
                    }
                }
                // lcdrepo.insertAll(compraResp.getInserts().getListaCompraDetalle());
            }
            //los updates
            if (compraResp.getUpdates() != null) {

                if (compraResp.getUpdates().getListaCompra() != null)
                    lcrepo.insertAll(compraResp.getUpdates().getListaCompra()); //inserto blblbl
                if (compraResp.getUpdates().getListaCompraDetalle() != null) {
                    //como puede que ya existan reviso primero e inserto unoxuno
                    for (ListaCompraDetalle detalle : compraResp.getInserts().getListaCompraDetalle()) {
                        ListaCompraDetalle existe = lcdrepo.findsimple(detalle.getListaId(), detalle.getId());
                        if (existe == null) {
                            // lcrepo.insert(detalle);

                        } else {   //mantengo los comprados y codigos nevos
                            detalle.setComprados(existe.getComprados());
                            detalle.setNvoCodigo(existe.getNvoCodigo());
                            //lcrepo.updateSC(compra);
                        }
                        lcdrepo.insert(detalle);

                    }
                    //reviso los que se eliminaron
                            /*List<ListaCompraDetalle> liscompapp=lcdrepo.getAllSimpl();
                            for (ListaCompraDetalle compra:liscompapp
                            ) {
                                //veo si está en el json si no es que se elimina, solo checo que no
                                //tenga informe
                                buscarListaDet(compra,compraResp.getInserts().getListaCompraDetalle());
                            }*/
                    // lcdrepo.updateAll(compraResp.getUpdates().getListaCompraDetalle());
                }
            }

            //actualizar version en tabla
            TablaVersiones tinfo = new TablaVersiones();
            tinfo.setNombreTabla(Contrato.TBLLISTACOMPRAS);
            Date fecha1 = new Date();
            Log.d(TAG, "insertando fecha version 1" + fecha1);

            tinfo.setVersion(fecha1);
            tinfo.setIndice(Constantes.INDICEACTUAL);
            tinfo.setTipo("I");
            TablaVersiones tinfod = new TablaVersiones();
            tinfod.setNombreTabla(Contrato.TBLLISTACOMPRASDET);
            tinfod.setVersion(fecha1);
            tinfod.setTipo("I");
            tinfod.setIndice(Constantes.INDICEACTUAL);
            tvRepo.insertUpdate(tinfo);
            tvRepo.insertUpdate(tinfod);

        }

        finalizar();

    }
    public int actualizarCorre(SolCorreResponse corrResp, int etapa) {


        //primero los inserts
        if (corrResp != null) {

            if (corrResp != null && corrResp.getInserts() != null) {
                for (SolicitudCor sol : corrResp.getInserts()) {
                    //veo si ya existe
                  //   Log.d(TAG,"solcorreccion"+sol.getId()+"--"+ sol.getNumFoto());
                    SolicitudCor solt = solRepo.findsimple(sol.getId(), sol.getNumFoto());
                    if (solt != null) {
                        if (solt.getEstatus() !=4||solt.getEstatus()!=5) {
                            //actualizo
                           // Log.d(TAG,"actualizo solcorreccion"+solt.getId()+"--"+ solt.getEstatus());
                            solRepo.actualizarEst(sol.getMotivo(), sol.getContador(), sol.getCreatedAt(), sol.getEstatus(), sol.getId(), sol.getNumFoto());
                        } else if (sol.getContador() > 1)
                            solRepo.actualizarEst(sol.getMotivo(), sol.getContador(), sol.getCreatedAt(), sol.getEstatus(), sol.getId(), sol.getNumFoto());
                        else
                            solRepo.actualizar(sol.getMotivo(), sol.getContador(), sol.getCreatedAt(), sol.getId(), sol.getNumFoto());

                    } else
                        solRepo.insert(sol);

                }


            }

            //los updates
            if (corrResp != null && corrResp.getUpdates() != null) {

                if (corrResp.getUpdates() != null)
                    solRepo.insertAll(corrResp.getUpdates()); //inserto blblbl
            }

            //actualizar version en tabla
            TablaVersiones tinfo = new TablaVersiones();
            tinfo.setNombreTabla(Contrato.TBLSOLCORRECCIONES);
            Date fecha1 = new Date();

            tinfo.setVersion(fecha1);
            tinfo.setIndice(Constantes.INDICEACTUAL);
            tinfo.setTipo("I");

            tvRepo.insertUpdate(tinfo);
            //  Log.d(TAG,"dddddd"+corrResp.getCanceladas().size());
            //veo las muestras canceladas
            if (corrResp.getCanceladas() != null)

                    for (MuestraCancelada cancel :
                            corrResp.getCanceladas()) {
                        //busco el informedetalle y actualizo el estatus
                        if(cancel.getIne_etapa()==2)
                            this.procesarCanceladas(cancel);
                        else
                            this.procesarCanceladasEta(cancel); //canceladas será 0

                    }

        }
        return 1;

    }



    //el estatus viene como 2 pero en la app 0 es cancelado y 2 es finalizado
    public void procesarCanceladasEta(MuestraCancelada cancelada) {
        InformeEtapa det = infetarepo.findsimple(cancelada.getInf_id());

        if (det != null) {
            Log.e(TAG, "cancele");
            det.setMotivoCancel(cancelada.getVas_observaciones());
            det.setFechaCancel(cancelada.getVas_fecha());
            det.setEstatus(0);

            infetarepo.insert(det);
            infetarepo.actualizarEstatus(det.getId(), 0);


        }
    }

        public void procesarCanceladas(MuestraCancelada cancelada){

            InformeCompraDetalle det=infdrepo.findsimple(cancelada.getInd_id());

            if(det!=null) {
                if (det.getEstatus() != 2&&det.getEstatus()!=4)//no está cancelada
                {
                    Log.d(TAG,"procesando canceladas"+det.getId());
                    String codigo=Constantes.sdfcaducidad.format(det.getCaducidad());
                     ListaCompraDetalle compradet = lcdrepo.findsimple(cancelada.getInd_comprasid(), cancelada.getInd_compraddetid());
                    if (compradet != null) {
                        //quito la comprada
                        if (compradet.getComprados() > 0) {
                            flog.grabarError(TAG,"procesarCanceladas",det.getId()+"--"+det.getInformesId()+"--"+codigo);
                            int cantidad = compradet.getComprados() - 1;
                            lcdrepo.actualizarComprados(compradet.getId(), compradet.getListaId(), cantidad);
                        }
                        Log.d(TAG,"quitando el codigo"+compradet.getNvoCodigo());

                        //quito en nuevo codigo
                        if (compradet.getNvoCodigo()!=null&&compradet.getNvoCodigo() != "") {
                            flog.grabarError(TAG,"procesarCanceladas","quitando el codigo"+compradet.getNvoCodigo());


                            String nuevoscods = compradet.getNvoCodigo().replace(codigo + ";", "");//elimino elcodigo
                            nuevoscods = compradet.getNvoCodigo().replace(codigo, "");//elimino elcodigo

                            //Log.d(TAG,compradet.getId()+"--"+compradet.getListaId()+"--"+nuevoscods);
                            lcdrepo.actualizarNvosCodigos(compradet.getId(), compradet.getListaId(), nuevoscods);
                        }
                        det.setMotivoCancel(cancelada.getVas_observaciones());
                        det.setFechaCancel(cancelada.getVas_fecha());
                        det.setEstatus(2);

                        infdrepo.insert(det);
                        infdrepo.actualizarEstatus(det.getId(), 2);
                    }
                }




            }


        }

    @Override
    public void actualizarInformes(RespInformesResponse infoResp) {
        // Log.d(TAG, "actualizando bd informes");
        //primero los inserts
        if (infoResp != null) {

            if (infoResp.getVisita() != null) {
                //reviso cada uno y las inserto
                for (Visita vis : infoResp.getVisita()) {
                    visRepo.insert(vis); //inserto blblbl
                }
            }
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
        Log.d("DescargasAsyncTask", "insertando fecha version 1" + fecha1);

        tinfo.setVersion(fecha1);
        tinfo.setIndice(Constantes.INDICEACTUAL);
        tinfo.setTipo("I");

        tvRepo.insertUpdate(tinfo);

    }


    @Override
    public void guardarEstatus(PostResponse response) {

    }
    private  void convertirListaNotif(List<NotificacionGen> lista) {
        InfEtapaRepositoryImpl informeEtapaRepo=new InfEtapaRepositoryImpl(act);
        for (NotificacionGen noti:
                lista) {

            //modifico el estatus del informe
            if(noti.getTipo()==5&&noti.getTotal()>0){    //4-revisar recibo
                //5-ajustar recibo
                //6-estatus envio
                //busco el informe finalizado con estatus 2
                List<InformeEtapa> listaInf=informeEtapaRepo.getInfxEstatusCiuSim(Constantes.INDICEACTUAL,6,2, Constantes.CIUDADTRABAJO);
                if(listaInf!=null&&listaInf.size()>0){
                    informeEtapaRepo.actualizarEstatus(listaInf.get(0).getId(),5);
                    flog.grabarError(TAG,"convertirListaNotif","actualizando informe gastos ajuste"+listaInf.get(0).getId());
                }
            }


        }

    }

    @Override
    public void guardarRes(PostResponse respuesta) {

    }

    @Override
    public void guardarResNotif(NotificacionResponse response) {
        Log.d(TAG,"actualizando notificaciones");
        if(response!=null&&response.getData()!=null) {
            convertirListaNotif(response.getData());

        }
        Log.d(TAG,"finalizando notificaciones voy en el"+procesos);
        finalizar();
    }
}
}
