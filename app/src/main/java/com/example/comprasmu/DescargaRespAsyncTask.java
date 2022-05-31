package com.example.comprasmu;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.modelos.Contrato;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.TablaVersiones;
import com.example.comprasmu.data.modelos.Visita;
import com.example.comprasmu.data.remote.ListaCompraResponse;
import com.example.comprasmu.data.remote.RespInformesResponse;
import com.example.comprasmu.data.repositories.AtributoRepositoryImpl;
import com.example.comprasmu.data.repositories.CatalogoDetalleRepositoryImpl;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.ProductoExhibidoRepositoryImpl;
import com.example.comprasmu.data.repositories.SustitucionRepositoryImpl;
import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;
import com.example.comprasmu.data.repositories.VisitaRepositoryImpl;
import com.example.comprasmu.utils.Constantes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static androidx.core.content.ContextCompat.getSystemService;

public class DescargaRespAsyncTask extends AsyncTask<String, Void, Void> {
    AlertDialog alertDialog;
    InformeCompraRepositoryImpl infrepo;
    InformeComDetRepositoryImpl infdrepo;
    ImagenDetRepositoryImpl imagenDetRepo;
    ListaCompraDetRepositoryImpl lcdrepo;
    VisitaRepositoryImpl visRepo;
    ProductoExhibidoRepositoryImpl prodrepo;
    SimpleDateFormat sdfdias;
    String TAG="DescargarRespAsynTask";
    private static final String DOWNLOAD_PATH = "https://muesmerc.mx/comprasv1/fotografias";
    private   String DESTINATION_PATH ;

    boolean notificar=false;
    Activity act;
    int actualiza;
    ProgresoRespListener proglist;
    DescargaRespListener listener;

    public DescargaRespAsyncTask(Activity act, int actualiza, ProgresoRespListener proglist) {

        this.act = act;

        sdfdias=new SimpleDateFormat("dd-MM-yyyy");
        this.proglist=proglist;
        this.listener=new DescargaRespListener();
        this.actualiza=actualiza; //indica si vengo del menu
        DESTINATION_PATH=act.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }

    @Override
    protected Void doInBackground(String... indice) {
        getRespaldo();
        return null;

    }



    private void getRespaldo(){

        infrepo = new InformeCompraRepositoryImpl(act);
        infdrepo = new InformeComDetRepositoryImpl(act);
        imagenDetRepo = new ImagenDetRepositoryImpl(act);
        prodrepo = new ProductoExhibidoRepositoryImpl(act);
        visRepo=new VisitaRepositoryImpl(act);
        lcdrepo=new ListaCompraDetRepositoryImpl(act);
        PeticionesServidor ps=new PeticionesServidor(Constantes.CLAVEUSUARIO);
    //    DescargaRespAsyncTask.DescargaRespListener listener=new DescargaRespListener();
        ps.pedirRespaldo(Constantes.INDICEACTUAL,listener);

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }

        public class DescargaRespListener {
            public DescargaRespListener() {
                //if(proglist!=null&&actualiza==1)
                //  proglist.cerrarAlerta();
            }

            public void noactualizar(String response){
                //a ver como la regresamos
                if(actualiza==1&&proglist!=null){
                    proglist.cerrarAlerta(notificar);
                }
            }

            public void actualizarInformes(RespInformesResponse infoResp) {
                Log.d(TAG,"actualizando bd informes");
                //primero los inserts
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


                if (infoResp.getImagenDetalles() != null && infoResp.getImagenDetalles().size() > 0) {
                    //como puede que ya existan reviso primero e inserto unoxuno
                    imagenDetRepo.insertAll(infoResp.getImagenDetalles());
                    descargarImagenes(infoResp.getImagenDetalles());

                }
                if (infoResp.getProductosEx() != null && infoResp.getProductosEx().size() > 0) {
                    //como puede que ya existan reviso primero e inserto unoxuno
                    prodrepo.insertAll(infoResp.getProductosEx());
                }
                //sumo en la lista de compra
                //   List<InformeCompraDetalle> detalles=infdrepo.getAllsimple();
                //ajusto cantidades
                Log.d(TAG,"ajusto cantidades");

                if(infoResp.getInformeCompraDetalles()!=null)
                    for(InformeCompraDetalle det:infoResp.getInformeCompraDetalles()){
                        Log.d(TAG,"ttttt"+det.getComprasId()+"--"+det.getComprasDetId());

                        ListaCompraDetalle compradet=lcdrepo.findsimple(det.getComprasId(),det.getComprasDetId());
                        if(compradet!=null)
                        {
                          //  Log.d(TAG,"ttttt"+det.getComprasId()+"--"+det.getId());
                            Log.d(TAG,"sss"+compradet.getProductoNombre()+"--"+compradet.getComprados());

                            int nvacant=compradet.getComprados()+1;
                          //  lcdrepo.actualizarComprados(det.getId(),det.getComprasId(),nvacant);
                            //actualizo lo codigos comprados
                            String listaCodigos="";
                            SimpleDateFormat sdfcodigo = new SimpleDateFormat("dd-MM-yy");
                            String nuevoCodigo= sdfcodigo.format(det.getCaducidad());
                            //no aumento el comprado solo el codigo
                            if(compradet.getNvoCodigo()!=null)
                            //reviso que no existe
                            {    if(!compradet.getNvoCodigo().contains(nuevoCodigo))
                                listaCodigos=nuevoCodigo+";"+compradet.getNvoCodigo();}
                            else
                                listaCodigos=nuevoCodigo;
                            compradet.setNvoCodigo(listaCodigos);
                            compradet.setComprados(nvacant);
                            //actualizo
                            lcdrepo.insert(compradet);
                            Log.d(TAG,"sss"+compradet.getProductoNombre()+"--"+nvacant);

                        }

                    }

                if(actualiza==1&&proglist!=null){
                    proglist.cerrarAlerta(notificar);
                }
            }
        }
    public interface ProgresoRespListener {
         void cerrarAlerta(boolean res);
         void todoBien();

    }
    private void descargarImagenes(List<ImagenDetalle> imagenes){
        for(ImagenDetalle img:imagenes){
            startDownload(DOWNLOAD_PATH+"/"+img.getIndice().replace(".","_")+"/"+img.getRuta(), DESTINATION_PATH);
            Log.d(TAG," descargando "+DOWNLOAD_PATH+"/"+img.getIndice().replace(".","_")+"/"+img.getRuta());
        }

    }
    private void startDownload(String downloadPath, String destinationPath) {
        Uri uri = Uri.parse(downloadPath); // Path where you want to download file.
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);  // Tell on which network you want to download file.
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);  // This will show notification on top when downloading the file.
        request.setTitle("Downloading a file"); // Title for notification.
        request.setVisibleInDownloadsUi(true);

        request.setDestinationInExternalFilesDir(act,Environment.DIRECTORY_PICTURES, uri.getLastPathSegment());  // Storage directory path
        ((DownloadManager) act.getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request); // This will start downloading
    }

}
