package com.example.comprasmu;

import android.app.Activity;

import android.content.DialogInterface;

import android.os.AsyncTask;

import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.modelos.Contrato;

import com.example.comprasmu.data.modelos.TablaVersiones;

import com.example.comprasmu.data.remote.SolCorreResponse;
import com.example.comprasmu.data.repositories.SolicitudCorRepoImpl;
import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;

import com.example.comprasmu.utils.Constantes;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DescCorrecAsyncTask extends AsyncTask<String, Void, Void> {

    SimpleDateFormat sdfdias;
  SolicitudCorRepoImpl solRepo;
    boolean notificar=false;
    Activity act;
    int actualiza;
    int etapa;
    String indice;

    TablaVersionesRepImpl tvRepo;
    final String TAG="DescCorrecAsyncTask";
    DescargaRespAsyncTask.ProgresoRespListener proglist;

    public DescCorrecAsyncTask(SolicitudCorRepoImpl solRepo, TablaVersionesRepImpl tvRepo, Activity act, int etapa, String indice) {
        this.solRepo = solRepo;
        this.act = act;
        this.etapa = etapa;
        this.indice = indice;
        this.tvRepo=tvRepo;
        sdfdias=new SimpleDateFormat("dd-MM-yyyy");
    }

    @Override
    protected Void doInBackground(String... indice) {

      //  if(NavigationDrawerActivity.isOnlineNet()) {
            Log.d(TAG,"iniciando descarga");
        if (indice[0].equals("act")) //vengo del fragment de actualizar lista
            actualiza=1;

            correcciones();

        return null;

    }


    private void correcciones(){
        if(actualiza==1&&proglist!=null) {
            proglist.actualizando();
        }
        PeticionesServidor ps=new PeticionesServidor(Constantes.CLAVEUSUARIO);
        TablaVersiones comp=tvRepo.getVersionByNombreTablasmd(Contrato.TBLLISTACOMPRAS,Constantes.INDICEACTUAL);
        DescargaCorrListener listener=new DescargaCorrListener();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String version;
        if (comp != null && comp.getVersion() != null) {
            version = sdf.format(comp.getVersion());
            //

        }else //es la 1a vez
        {
            version ="1999-09-09"; //una fecha muy antigua


        }
        if(actualiza==1) {
                version ="1999-09-09"; //una fecha muy antigua
            }
                //siempre actualizo
        if(NavigationDrawerActivity.isOnlineNet())
            ps.pedirSolicitudesCorr(indice,etapa,version,listener);
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



    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
      /*  if(notificar)
            notificarSinConexion();*/

        if(actualiza==1&&proglist!=null) {
            proglist.cerrarAlerta(notificar);
        }
    }



        public class DescargaCorrListener{
            public DescargaCorrListener(){
                //if(proglist!=null&&actualiza==1)
                  //  proglist.cerrarAlerta();
            }

            public void noactualizar(String mensaje){
                //no se
            }
            public void actualizar(SolCorreResponse corrResp) {
                //primero los inserts
                Log.d(TAG,"resp1>>"+corrResp.getInserts());

                if (corrResp.getInserts() != null) {
                    if (corrResp.getInserts() != null) {
                        Log.d(TAG, "resp2>>" + corrResp.getInserts());

                        solRepo.insertAll(corrResp.getInserts()); //inserto blblbl
                    }
                }

                //los updates
                if (corrResp.getUpdates() != null) {

                    if(corrResp.getUpdates()!=null)
                        solRepo.insertAll(corrResp.getUpdates()); //inserto blblbl
                }

                //actualizar version en tabla
                TablaVersiones tinfo = new TablaVersiones();
                tinfo.setNombreTabla(Contrato.TBLSOLCORRECCIONES);
                Date fecha1=new Date();
                Log.d(TAG,"insertando fecha version 1"+fecha1);

                tinfo.setVersion(fecha1);
                tinfo.setIndice(Constantes.INDICEACTUAL);
                tinfo.setTipo("I");

                tvRepo.insertUpdate(tinfo);


            }



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
