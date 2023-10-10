package com.example.comprasmu.ui.informe;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.remote.InformeEnvio;
import com.example.comprasmu.services.SubirColaFotoService;
import com.example.comprasmu.services.SubirFotoService;
import com.example.comprasmu.utils.ComprasUtils;


/*****
 *
 */
public class NuevoinformeFragment {

    public static final String ARG_FOTOPRODUCTO ="comprasmu.ni.fotoprod" ;

    public static String NUMMUESTRA="comprasmu.ni.nummuestra";

    public final static String INFORMESEL="comprasmu.ni_informesel";
    public final static String ISEDIT="comprasmu.ni_isedit";
    public final static String ISCOMPLETE="comprasmu.ni_complete";
    public final static String ARG_NUEVOINFORME="comprasmu.ni_idinforme";
    public final static String ARG_CLIENTEINFORME="comprasmu.clienteinf";

    private static final String TAG="NuevoInformeFragment";


    public static void subirFotos(Activity activity, InformeEnvio informe){
        //las imagenes
        for(ImagenDetalle imagen:informe.getImagenDetalles()){
            //subo cada una
            Intent msgIntent = new Intent(activity, SubirFotoService.class);
            msgIntent.putExtra(SubirFotoService.EXTRA_IMAGE_ID, imagen.getId());
            msgIntent.putExtra(SubirFotoService.EXTRA_IMG_PATH,imagen.getRuta());
           // Log.d(TAG,"error "+informe.get)
            msgIntent.putExtra(SubirFotoService.EXTRA_INDICE,imagen.getIndice());
            msgIntent.setAction(SubirFotoService.ACTION_UPLOAD_IMG);

            // Constantes.INDICEACTUAL
            Log.d(TAG,"subiendo fotos"+activity.getLocalClassName());
            activity.startService(msgIntent);
            //cambio su estatus a subiendo



        }

    }

    public static void subirFotosFila(Activity activity, InformeEnvio informe){
        //las imagenes
        String cadenarutas="";
        if(informe.getImagenDetalles()!=null)
        cadenarutas= ComprasUtils.listaaCadena(informe.getImagenDetalles());

            //subo cada una
            Intent msgIntent = new Intent(activity, SubirColaFotoService.class);
            msgIntent.putExtra(SubirColaFotoService.EXTRA_IMAGE_ID, 100);
            msgIntent.putExtra(SubirColaFotoService.EXTRA_IMG_PATH,cadenarutas);
            // Log.d(TAG,"error "+informe.get)
            msgIntent.putExtra(SubirColaFotoService.EXTRA_INDICE,informe.getIndice());
            msgIntent.setAction(SubirColaFotoService.ACTION_UPLOAD_LISTA);

            // Constantes.INDICEACTUAL
            Log.d(TAG,"subiendo fotos"+activity.getLocalClassName());
            activity.startService(msgIntent);
            //cambio su estatus a subiendo


    }

}