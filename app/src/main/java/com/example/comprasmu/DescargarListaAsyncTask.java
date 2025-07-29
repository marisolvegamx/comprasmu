package com.example.comprasmu;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.modelos.Contrato;
import com.example.comprasmu.data.modelos.Correccion;
import com.example.comprasmu.data.modelos.Geocerca;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.TablaVersiones;
import com.example.comprasmu.data.modelos.Visita;
import com.example.comprasmu.data.remote.ListaCompraResponse;
import com.example.comprasmu.data.remote.RespInfEtapaResponse;
import com.example.comprasmu.data.remote.RespInformesResponse;
import com.example.comprasmu.data.repositories.AtributoRepositoryImpl;
import com.example.comprasmu.data.repositories.CatalogoDetalleRepositoryImpl;
import com.example.comprasmu.data.repositories.CorreccionRepoImpl;
import com.example.comprasmu.data.repositories.DetalleCajaRepoImpl;
import com.example.comprasmu.data.repositories.GeocercaRepositoryImpl;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InfEtapaDetRepoImpl;
import com.example.comprasmu.data.repositories.InfEtapaRepositoryImpl;
import com.example.comprasmu.data.repositories.InfGastoDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeEnvioRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.ProductoExhibidoRepositoryImpl;
import com.example.comprasmu.data.repositories.SiglaRepositoryImpl;
import com.example.comprasmu.data.repositories.SolicitudCorRepoImpl;
import com.example.comprasmu.data.repositories.SustitucionRepositoryImpl;
import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;
import com.example.comprasmu.data.repositories.VisitaRepositoryImpl;
import com.example.comprasmu.ui.tiendas.PeticionMapaCd;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**********pedir las lista de compra por ciudad**/
public class DescargarListaAsyncTask extends AsyncTask<String, Void, Void>  {


    TablaVersionesRepImpl tvRepo;
    SimpleDateFormat sdfdias;
    ListaCompraDetRepositoryImpl lcdrepo;
    ListaCompraRepositoryImpl lcrepo;
    InformeComDetRepositoryImpl infdrepo;
    Activity act;
    private ComprasLog flog;
    DescargaIniListener listenprin;
    String ciudadActual;

    final String TAG="DescargarListaAsyncTask";
    private final ProgresoDLListener miproglis;
    RespInfEtapaResponse maininfoetaResp;
    RespInformesResponse maininfoResp; //para bajar las fotos desde la actividad
    List<Correccion> mainRespcor;

    PeticionesServidor peticionesServidor;


    public DescargarListaAsyncTask(Activity act,
                                   TablaVersionesRepImpl tvRepo,
                                   ListaCompraDetRepositoryImpl lcdrepo,
                                   ListaCompraRepositoryImpl lcrepo, ProgresoDLListener miproglis,
                                   PeticionesServidor peticionServ, String ciudadActual ) {

        this.tvRepo=tvRepo;
        this.lcdrepo=lcdrepo;
        this.lcrepo=lcrepo;
        this.act=act;
        sdfdias=new SimpleDateFormat("dd-MM-yyyy");
        this.miproglis=miproglis;
        flog = ComprasLog.getSingleton();
        flog.crearLog(act.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getPath());
        this.peticionesServidor=peticionServ;
        this.ciudadActual=ciudadActual;

    }

    @Override
    protected Void doInBackground(String... indice) {

        listenprin=new DescargaIniListener();
        if(!ComprasUtils.isOnlineNet(act)) {
            miproglis.notificarSinConexion();
            return null;
        }

        listacompras();
        return null;

    }


    public static Boolean isOnlineNet() {

        try {
            Process p = Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }
    private void listacompras(){
        Log.i("DescargarListaAsyncTask", "descargando listas"+ciudadActual);
        flog.grabarError(TAG,"listacompras","descargando listas =");
     //   TablaVersiones comp=tvRepo.getVersionByNombreTablasmd(Contrato.TBLLISTACOMPRAS,Constantes.INDICEACTUAL);
       // TablaVersiones det=tvRepo.getVersionByNombreTablasmd(Contrato.TBLLISTACOMPRASDET,Constantes.INDICEACTUAL);
        DescargarListaAsyncTask.DescargaIniListener listener=new DescargaIniListener();

        PeticionesServidor.PeticionLista peticionLista=peticionesServidor.crearPeticion(null, null,Constantes.INDICEACTUAL);
        peticionesServidor.pedirListaCiu(peticionLista,ciudadActual,listener);


    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
      /*  if(notificar)
            notificarSinConexion();*/
      //  Log.e("DescargasIniAsyT","---"+actualiza);

    }


    public class DescargaIniListener implements  IDescargaIniListener{
        public DescargaIniListener(){

        }
       public void finalizar(){
            Log.i(TAG,"DescargaIniListener finalizando ");

               miproglis.todoBien(maininfoetaResp,maininfoResp,mainRespcor);


       }

        @Override
        public void insertarZonas(List<Geocerca> zonas) {
            //no hace nada
        }


        public void actualizar(ListaCompraResponse compraResp) {
                //primero los inserts
            Log.i(TAG,"actualizar lista");

            if(compraResp!=null) {
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
                    }
                }else
                    if (compraResp.getInserts() != null) {
                        if (compraResp.getInserts().getListaCompra() != null) {
                             Log.d(TAG,"listacomp<"+compraResp.getInserts().getListaCompra());
                            flog.grabarError(TAG,"actualizar lista compra","listacomp<");

                            lcrepo.insertAll(compraResp.getInserts().getListaCompra()); //inserto blblbl
                        }
                        // Log.d("Descargaini","resp3>>"+compraResp.getInserts().getListaCompraDetalle());

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
                               // Log.d(TAG,"insertando"+detalle.getListaId()+"--"+detalle.getId());
                                long id=lcdrepo.insert(detalle);
                                //Log.d(TAG,"**insertando"+id);



                            }
                            //reviso los que se eliminaron
                            if(compraResp.getInserts().getListaCompra()!=null&&compraResp.getInserts().getListaCompra().size()>0) {
                                //  Log.d(TAG,"buscando elim");
                                List<ListaCompra> liscompapp = lcrepo.getAllByIndicesimple(Constantes.INDICEACTUAL);
                                for (ListaCompra compra : liscompapp
                                ) {
                                    //veo si está en el json si no es que se elimina, solo checo que no
                                    //tenga informe
                                    eliminarListaCompra(compra, compraResp.getInserts().getListaCompra(), infdrepo, lcrepo);
                                }
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


                    //actualizar version en tabla
                    TablaVersiones tinfo = new TablaVersiones();
                    tinfo.setNombreTabla(Contrato.TBLLISTACOMPRAS);
                    Date fecha1 = new Date();
                    Log.d("DescargasAsyncTask", "insertando fecha version 1" + fecha1);

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



        }


    public void informes(){


    }


    private void eliminarListaCompra(ListaCompra compra, List<ListaCompra> json, InformeComDetRepositoryImpl infdrepo, ListaCompraRepositoryImpl lcrepo){
        for (ListaCompra jcompra: json) {
            if(jcompra.getId()==compra.getId()){
                // Log.d(TAG,compra.getProductoNombre()+"--"+jcompra.getProductoNombre()+".."+compra.getListaId()+"--"+ compra.getId());
                return;
            }
        }

        //si llego aqui, no lo encontré por lo que se eliminó
        //busco que no tenga informe

        List<InformeCompraDetalle> prods=infdrepo.findByCompra(compra.getId());
        Log.d(TAG,"prbabbl elimine "+compra.getId());
        if(prods==null||prods.size()<1)
            lcrepo.delete(compra);

    }
    public void buscarListaDet(ListaCompraDetalle compra, List<ListaCompraDetalle> json){
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
        Log.d(TAG,"eliminar? "+compra.getProductoNombre()+"--"+compra.getListaId()+"--"+ compra.getId());
        if(prods==null||prods.size()<1)
            lcdrepo.delete(compra);

    }
    public interface ProgresoDLListener {

         void todoBien(RespInfEtapaResponse maininfoetaResp,
                 RespInformesResponse maininfoResp, //para bajar las fotos desde la actividad
                       List<Correccion> mainRespcor);

        void notificarSinConexion();

    }


}
