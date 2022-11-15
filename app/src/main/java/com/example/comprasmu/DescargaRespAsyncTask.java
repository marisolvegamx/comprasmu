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

    InformeComDetRepositoryImpl infdrepo;
    ImagenDetRepositoryImpl imagenDetRepo;
    ListaCompraDetRepositoryImpl lcdrepo;
    VisitaRepositoryImpl visRepo;
    ProductoExhibidoRepositoryImpl prodrepo;
    SimpleDateFormat sdfdias;
    String TAG="DescargarRespAsynTask";
    private static final String DOWNLOAD_PATH = "https://muesmerc.mx/comprasv1/fotografias";
    private final String DESTINATION_PATH ;

    boolean notificar=false;
    Activity act;
    int actualiza;

   // DescargaRespListener listener;

    public DescargaRespAsyncTask(Activity act, int actualiza) {

        this.act = act;

        sdfdias=new SimpleDateFormat("dd-MM-yyyy");
       // this.proglist=proglist;
       // this.listener=new DescargaRespListener();
        this.actualiza=actualiza; //indica si vengo del menu
        DESTINATION_PATH=act.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }

    @Override
    protected Void doInBackground(String... indice) {
       // getRespaldo();
        return null;

    }



    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }




}
