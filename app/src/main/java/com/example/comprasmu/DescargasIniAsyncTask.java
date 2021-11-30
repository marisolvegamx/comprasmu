package com.example.comprasmu;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.modelos.Contrato;
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
    public DescargasIniAsyncTask(Activity act, CatalogoDetalleRepositoryImpl cdrepo,
                                 TablaVersionesRepImpl tvRepo,
                                 AtributoRepositoryImpl atRepo, ListaCompraDetRepositoryImpl lcdrepo, ListaCompraRepositoryImpl lcrepo) {

        this.cdrepo=cdrepo;
        this.atRepo=atRepo;
        this.tvRepo=tvRepo;
        this.lcdrepo=lcdrepo;
        this.lcrepo=lcrepo;
        this.act=act;
        sdfdias=new SimpleDateFormat("dd-MM-yyyy");
    }

    @Override
    protected Void doInBackground(String... indice) {

        if(NavigationDrawerActivity.isOnlineNet()) {
            Log.d("DescargasIniAsyncTask","iniciando descarga");
            if (indice[0].equals("cat")) //descargo cats tmb
                catalogos();
            listacompras();
        }else
              {
            notificar = true;
                  Log.d("DescargasIniAsyncTask"," sin internet");
            cancel(true);
        }
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


            }
        }else {   //primera vez
            Log.d("DescargasIniAsyncTask","iniciando descarga cats");
            ps.getCatalogos(cdrepo, tvRepo, atRepo);
        }

    }

    private void listacompras(){
        PeticionesServidor ps=new PeticionesServidor(Constantes.CLAVEUSUARIO);


        TablaVersiones comp=tvRepo.getVersionByNombreTablasmd(Contrato.TBLLISTACOMPRAS);
        TablaVersiones det=tvRepo.getVersionByNombreTablasmd(Contrato.TBLLISTACOMPRASDET);


        DescargasIniAsyncTask.DescargaIniListener listener=new DescargaIniListener();
        if(comp!=null){


            Log.d("DescargasIniAsyncTask","comprobando vers liscomp*"+sdfdias.format(comp.getVersion())+"--"+(sdfdias.format(new Date())));
            if(!sdfdias.format(comp.getVersion()).equals(sdfdias.format(new Date()))){
                ps.getListasdeCompra(comp,det,Constantes.INDICEACTUAL,listener);

            }
        }else {   //primera vez
            Log.d("DescargasIniAsyncTask", "primera vez");

            ps.getListasdeCompra(comp, det, Constantes.INDICEACTUAL, listener);
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(notificar)
        notificarSinConexion();
    }



        public class DescargaIniListener{

            public void noactualizar(String response){
                //a ver como la regresamos
            }
            public void actualizar(ListaCompraResponse compraResp) {
                //primero los inserts
                if (compraResp.getInserts() != null) {
                    if(compraResp.getInserts().getListaCompra()!=null)
                        lcrepo.insertAll(compraResp.getInserts().getListaCompra()); //inserto blblbl
                    if(compraResp.getInserts().getListaCompraDetalle()!=null)
                        lcdrepo.insertAll(compraResp.getInserts().getListaCompraDetalle());
                }
                //los updates
                if (compraResp.getUpdates() != null) {

                    if(compraResp.getUpdates().getListaCompra()!=null)
                        lcrepo.insertAll(compraResp.getUpdates().getListaCompra()); //inserto blblbl
                    if(compraResp.getUpdates().getListaCompraDetalle()!=null) {

                        lcdrepo.updateAll(compraResp.getUpdates().getListaCompraDetalle());
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

        public void notificarSinConexion(){
            AlertDialog.Builder builder=new AlertDialog.Builder(act);
            builder.setCancelable(true);
            builder.setIcon(android.R.drawable.stat_sys_download);
            builder.setTitle(act.getString(R.string.atencion));
            builder.setMessage("La aplicación necesita actualizarse. Favor de revisar su conexión a internet o activar sus datos");


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
