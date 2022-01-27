package com.example.comprasmu;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.modelos.Contrato;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.TablaVersiones;
import com.example.comprasmu.data.remote.ListaCompraResponse;
import com.example.comprasmu.data.repositories.AtributoRepositoryImpl;
import com.example.comprasmu.data.repositories.CatalogoDetalleRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;
import com.example.comprasmu.utils.Constantes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DescargasIniAsyncTask extends AsyncTask<String, Void, Void> {
    AlertDialog alertDialog;
    CatalogoDetalleRepositoryImpl cdrepo;
    TablaVersionesRepImpl tvRepo;
    AtributoRepositoryImpl atRepo;
    SimpleDateFormat sdfdias;
    ListaCompraDetRepositoryImpl lcdrepo;
    ListaCompraRepositoryImpl lcrepo;
    boolean notificar=false;
    Activity act;
    int actualiza;
    ProgresoListener proglist;
    public DescargasIniAsyncTask(Activity act, CatalogoDetalleRepositoryImpl cdrepo,
                                 TablaVersionesRepImpl tvRepo,
                                 AtributoRepositoryImpl atRepo, ListaCompraDetRepositoryImpl lcdrepo, ListaCompraRepositoryImpl lcrepo,ProgresoListener proglist) {

        this.cdrepo=cdrepo;
        this.atRepo=atRepo;
        this.tvRepo=tvRepo;
        this.lcdrepo=lcdrepo;
        this.lcrepo=lcrepo;
        this.act=act;
        sdfdias=new SimpleDateFormat("dd-MM-yyyy");
        this.proglist=proglist;
    }

    @Override
    protected Void doInBackground(String... indice) {

      //  if(NavigationDrawerActivity.isOnlineNet()) {
            Log.d("DescargasIniAsyncTask","iniciando descarga");
            if (indice[1].equals("act")) //vengo del fragment de actualizar lista
                actualiza=1;
           // if (indice[0].equals("cat")) //descargo cats tmb
            catalogos();
            listacompras();
      /*  }else
              {
            notificar = true;
                  Log.d("DescargasIniAsyncTask"," sin internet");
          //  cancel(true);

        }*/
        return null;

    }

    private void catalogos()
    {
        PeticionesServidor ps=new PeticionesServidor(Constantes.CLAVEUSUARIO);


        TablaVersiones cats=tvRepo.getVersionByNombreTablasmd("catalogos");


        if(cats!=null){
            //si hoy ya se actualizó no actualizo
            Log.d("DescargasIniAsyncTask","comprobando vers catalog*"+sdfdias.format(cats.getVersion())+"--"+(sdfdias.format(new Date())));
            if(!sdfdias.format(cats.getVersion()).equals(sdfdias.format(new Date()))){
                if(NavigationDrawerActivity.isOnlineNet())
                    ps.getCatalogos(cdrepo,tvRepo, atRepo);
                else

                    notificar = true;

            }
                 else{
                //no actualice
                if(actualiza==1) {
                    if(NavigationDrawerActivity.isOnlineNet())
                        ps.getCatalogos(cdrepo,tvRepo, atRepo);
                else

                    notificar = true;
                  /*  act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("DescargasIniAsyncTask","estas al dia*");

                          //  proglist.cerrarAlerta();
                         //   proglist.todoBien();
                        }
                    });*/
                }
            }
        }else {   //primera vez
            Log.d("DescargasIniAsyncTask","iniciando descarga cats");
            if(NavigationDrawerActivity.isOnlineNet())
                ps.getCatalogos(cdrepo, tvRepo, atRepo);
            else
                notificar = true;
        }

    }

    private void listacompras(){
        PeticionesServidor ps=new PeticionesServidor(Constantes.CLAVEUSUARIO);


        TablaVersiones comp=tvRepo.getVersionByNombreTablasmd(Contrato.TBLLISTACOMPRAS);
        TablaVersiones det=tvRepo.getVersionByNombreTablasmd(Contrato.TBLLISTACOMPRASDET);


        DescargasIniAsyncTask.DescargaIniListener listener=new DescargaIniListener();
        if(comp!=null){


            Log.d("DescargasIniAsyncTask","comprobando vers liscomp*"+sdfdias.format(comp.getVersion())+"--"+(sdfdias.format(new Date())));
            if( !sdfdias.format(comp.getVersion()).equals(sdfdias.format(new Date()))){
                if(NavigationDrawerActivity.isOnlineNet())
                ps.getListasdeCompra(comp,det,Constantes.INDICEACTUAL,listener);
                else
                    notificar = true;

            }
            else{
                //no actualice
                if(actualiza==1) {
                    //siempre actualizo
                    if(NavigationDrawerActivity.isOnlineNet())
                    ps.getListasdeCompra(comp,det,Constantes.INDICEACTUAL,listener);
                    else
                        notificar = true;
                  /*  act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("DescargasIniAsyncTask","estas al dia*");

                            proglist.cerrarAlerta();
                            proglist.todoBien();
                        }
                    });*/
                }
            }

        }else {   //primera vez
            Log.d("DescargasIniAsyncTask", "primera vez");
            if(NavigationDrawerActivity.isOnlineNet())
            ps.getListasdeCompra(comp, det, Constantes.INDICEACTUAL, listener);
            else
                notificar = true;
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(notificar)
            notificarSinConexion();
        Log.e("DescargasIniAsyT","---"+actualiza+"---"+proglist);
        if(actualiza==1&&proglist!=null){
            proglist.cerrarAlerta(notificar);
        }
    }



        public class DescargaIniListener{
            public DescargaIniListener(){
                //if(proglist!=null&&actualiza==1)
                  //  proglist.cerrarAlerta();
            }
            public void noactualizar(String response){
                //a ver como la regresamos
            }
            public void actualizar(ListaCompraResponse compraResp) {
                //primero los inserts
                if (compraResp.getInserts() != null) {
                    if(compraResp.getInserts().getListaCompra()!=null) {

                        lcrepo.insertAll(compraResp.getInserts().getListaCompra()); //inserto blblbl
                    }
                        if(compraResp.getInserts().getListaCompraDetalle()!=null){
                            //como puede que ya existan reviso primero e inserto unoxuno
                            for(ListaCompraDetalle detalle:compraResp.getInserts().getListaCompraDetalle()){
                                ListaCompraDetalle existe=lcdrepo.findsimple(detalle.getListaId(),detalle.getId());
                                if(existe==null){
                                   // lcrepo.insert(detalle);

                                }else
                                {
                                    detalle.setComprados(existe.getComprados());
                                    //lcrepo.updateSC(compra);
                                }
                                lcdrepo.insert(detalle);

                            }
                        }
                       // lcdrepo.insertAll(compraResp.getInserts().getListaCompraDetalle());
                }
                //los updates
                if (compraResp.getUpdates() != null) {

                    if(compraResp.getUpdates().getListaCompra()!=null)
                        lcrepo.insertAll(compraResp.getUpdates().getListaCompra()); //inserto blblbl
                    if(compraResp.getUpdates().getListaCompraDetalle()!=null) {
                        //como puede que ya existan reviso primero e inserto unoxuno
                        for(ListaCompraDetalle detalle:compraResp.getInserts().getListaCompraDetalle()){
                            ListaCompraDetalle existe=lcdrepo.findsimple(detalle.getListaId(),detalle.getId());
                            if(existe==null){
                                // lcrepo.insert(detalle);

                            }else
                            {   //mantengo los comprados y codigos nevos
                                detalle.setComprados(existe.getComprados());
                                detalle.setNvoCodigo(existe.getNvoCodigo());
                                //lcrepo.updateSC(compra);
                            }
                            lcdrepo.insert(detalle);

                        }
                       // lcdrepo.updateAll(compraResp.getUpdates().getListaCompraDetalle());
                    }
                }

                //actualizar version en tabla
                TablaVersiones tinfo = new TablaVersiones();
                tinfo.setNombreTabla(Contrato.TBLLISTACOMPRAS);
                tinfo.setVersion(new Date());
                tinfo.setTipo("I");
                TablaVersiones tinfod = new TablaVersiones();
                tinfod.setNombreTabla(Contrato.TBLLISTACOMPRASDET);
                tinfod.setVersion(new Date());
                tinfod.setTipo("I");
                tvRepo.insertUpdate(tinfo);
                tvRepo.insertUpdate(tinfod);

            }

        }
    public interface ProgresoListener {
         void cerrarAlerta(boolean res);
         void todoBien();

    }
    public void notificarSinConexion(){
        AlertDialog.Builder builder=new AlertDialog.Builder(act);
        builder.setCancelable(true);
        builder.setIcon(android.R.drawable.stat_sys_download);
        builder.setTitle(act.getString(R.string.atencion));
        builder.setMessage("La aplicación necesita actualizarse. Por favor revise su conexión a internet o active sus datos");


        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton(act.getString(R.string.aceptar),new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }
}
