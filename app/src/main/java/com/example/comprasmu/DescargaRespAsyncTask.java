package com.example.comprasmu;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.modelos.Contrato;
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

public class DescargaRespAsyncTask extends AsyncTask<String, Void, Void> {
    AlertDialog alertDialog;
    InformeCompraRepositoryImpl infrepo;
    InformeComDetRepositoryImpl infdrepo;
    ImagenDetRepositoryImpl imagenDetRepo;
    ListaCompraDetRepositoryImpl lcrepo;
    VisitaRepositoryImpl visrepo;
    ProductoExhibidoRepositoryImpl prodrepo;
    SimpleDateFormat sdfdias;
    String TAG="DescargarRespAsynTas";

    boolean notificar=false;
    Activity act;
    int actualiza;
    ProgresoListener proglist;

    public DescargaRespAsyncTask(InformeCompraRepositoryImpl infrepo, InformeComDetRepositoryImpl infdrepo, ImagenDetRepositoryImpl imagenDetRepo, VisitaRepositoryImpl visrepo, ProductoExhibidoRepositoryImpl prodrepo, ListaCompraDetRepositoryImpl lcrepo,Activity act) {
        this.infrepo = infrepo;
        this.infdrepo = infdrepo;
        this.imagenDetRepo = imagenDetRepo;
        this.visrepo = visrepo;
        this.prodrepo = prodrepo;
        this.act = act;
        this.lcrepo=lcrepo;
        sdfdias=new SimpleDateFormat("dd-MM-yyyy");
        this.proglist=proglist;
    }



    @Override
    protected Void doInBackground(String... indice) {

        getRespaldo();
        return null;

    }



    private void getRespaldo(){
        PeticionesServidor ps=new PeticionesServidor(Constantes.CLAVEUSUARIO);


        DescargaRespAsyncTask.DescargaIniListener listener=new DescargaIniListener();

        ps.pedirRespaldo(Constantes.INDICEACTUAL,listener);

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }



        public class DescargaIniListener {
            public DescargaIniListener() {
                //if(proglist!=null&&actualiza==1)
                //  proglist.cerrarAlerta();
            }

            public void noactualizar() {
                //a ver como la regresamos
            }

            public void actualizar(RespInformesResponse infoResp) {
                Log.d(TAG,"actualizando bd");
                //primero los inserts
                if (infoResp.getVisita() != null) {
                    //reviso cada uno y las inserto
                    for (Visita vis : infoResp.getVisita()) {
                        visrepo.insert(vis); //inserto blblbl
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


                }
                if (infoResp.getProductosEx() != null && infoResp.getProductosEx().size() > 0) {
                    //como puede que ya existan reviso primero e inserto unoxuno
                    prodrepo.insertAll(infoResp.getProductosEx());


                }
                //sumo en la lista de compra
             //   List<InformeCompraDetalle> detalles=infdrepo.getAllsimple();
                //ajusto cantidades
                if(infoResp.getInformeCompraDetalles()!=null)
                    for(InformeCompraDetalle det:infoResp.getInformeCompraDetalles()){
                        ListaCompraDetalle compradet=lcrepo.findsimple(det.getComprasId(),det.getId());
                        if(compradet!=null)
                        {   Log.d(TAG,"sss"+compradet.getProductoNombre()+"--"+compradet.getComprados());

                            int nvacant=compradet.getComprados()+1;
                            lcrepo.actualizarComprados(det.getId(),det.getComprasId(),nvacant);
                            Log.d(TAG,"sss"+compradet.getProductoNombre()+"--"+nvacant);

                        }

                    }


            }
        }
    public interface ProgresoListener {
         void cerrarAlerta(boolean res);
         void todoBien();

    }

}
