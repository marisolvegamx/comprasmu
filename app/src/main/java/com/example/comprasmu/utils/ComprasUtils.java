package com.example.comprasmu.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.comprasmu.R;
import com.example.comprasmu.data.dao.ConfiguracionRepositoryImpl;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.Configuracion;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.repositories.SolicitudCorRepoImpl;
import com.example.comprasmu.ui.visita.AbririnformeFragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

import static android.content.Context.ACTIVITY_SERVICE;

public class ComprasUtils {

    Bitmap rotatedBitmap;

  /*  public static Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String s;
            while ((s = br.readLine()) != null)
                Log.d("ComprasUtils","line: " + s);



            int val = p.waitFor();
            boolean reachable = (val == 0);
            Log.d("ComprasUtils","exit: " + p.exitValue());
            p.destroy();
            return reachable;

        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }*/
    public static Boolean isOnlineNet(Context context) {

       ConnectivityManager connectivityManager = (ConnectivityManager)
         context.getSystemService(Context.CONNECTIVITY_SERVICE);
     NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        RunnableFuture<Boolean> futureRun = new FutureTask<Boolean>(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if ((networkInfo .isAvailable()) && (networkInfo .isConnected())) {
                    try {
                        HttpURLConnection urlc = (HttpURLConnection) (new URL("https://espanol.yahoo.com").openConnection());
                        urlc.setRequestProperty("User-Agent", "Test");
                        urlc.setRequestProperty("Connection", "close");
                        urlc.setConnectTimeout(1500);
                        urlc.connect();
                        int res=urlc.getResponseCode();
                        Log.d("ComprasUtils","ssss "+res);
                        return (res== 200);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("ComprasUtils", "Error checking internet connection", e);
                    }
                } else {
                    Log.d("ComprasUtils", "No network available!");
                }
                return false;
            }
        });

        new Thread(futureRun).start();


        try {
            return futureRun.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }

    }

    public void rotarImagen(ImageView imagen1, String pathName) {

        Bitmap bitmapOrg = BitmapFactory.decodeFile(pathName);

        int width = bitmapOrg.getWidth();
        int height = bitmapOrg.getHeight();

        Matrix matrix = new Matrix();

        matrix.postRotate(90);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmapOrg, width, height, true);

        rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        //comprimir imagen
        File file = new File(pathName);
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            Log.d("Compras", e.getMessage());
            //   Toast.makeText(this, getResources().getString(R.string.errorImagen), Toast.LENGTH_SHORT).show();

        }
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);

        imagen1.setImageBitmap(rotatedBitmap);


    }

    public static String upperCaseFirst(String val) {
        char[] arr = val.toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        return new String(arr);
    }
    public Bitmap comprimirImagen(String nombre_foto){
        try {
            Bitmap bitmapOrg = BitmapFactory.decodeFile(nombre_foto);
            //return bitmapOrg;
            int width = bitmapOrg.getWidth();
            int height = bitmapOrg.getHeight();
            int tam=bitmapOrg.getByteCount();
            int quality=100;
            //Parámetros optimización, resolución máxima permitida
            int max_ancho = 1725;
            int max_alto = 2300;
            if(tam>6000000) {
                 quality=96;
                max_ancho = 1350;
                max_alto = 1800;
            }
          //  int max_ancho = 2250;
           // int max_alto = 3000;
           // max_ancho = 1125; //resoluciones probadas
           // max_alto = 1500;
          //  int max_ancho =900;
          //  int max_alto =  1200;


            double x_ratio = (double)max_ancho/(double)width;
            double y_ratio = (double)max_alto/(double)height;
            int ancho_final, alto_final;

            if( (width <= max_ancho) && (height <= max_alto) ){
                ancho_final = width;
                alto_final = height;
            }
            else if ((x_ratio * height) < max_alto){
                alto_final = (int)(x_ratio * height);
                ancho_final = max_ancho;
            }
            else{
                ancho_final = (int)(y_ratio * width);
                alto_final = max_alto;
            }


            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmapOrg, ancho_final, alto_final, false);

            //comprimir imagen
            File file = new File(nombre_foto);

            OutputStream os = null;
            try {
                os = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                Log.d("Compras", e.getMessage());
                // Toast.makeText(, "Error al guardar la foto", Toast.LENGTH_SHORT).show();
                return null;
            }
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, os);
            os.flush();;
            os.close();
            return scaledBitmap;

        }catch(Exception ex){
            ex.printStackTrace();
            Log.d("Compras","algo salió mal al comprimir la imagen");
            return null;
        }


    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight,int tam) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        int pot=3;
     /*   if(tam>200000000)
        {
            pot=4;
        }*/

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= pot;
            }
        }
     //  Log.d("imagenes","reducim tam orig "+height+"x"+width+" factor "+inSampleSize);

        //  ComprasLog clog=new ComprasLog();
     //   clog.crearLog();
      //  clog.grabarError("reducim tam orig "+height+"x"+width+" factor "+inSampleSize);
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource( String nombre_foto,
                                                         int reqWidth, int reqHeight) {
        File archivo = new File(nombre_foto);
        if (!archivo.exists()) {
            return null;
        }
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
       // BitmapFactory.decodeResource(res, resId, options);

      /*  Bitmap orig=BitmapFactory.decodeFile(nombre_foto,options);
        if(orig==null) {
          ComprasLog milog=ComprasLog.getSingleton();
          milog.grabarError("No encontre la foto "+nombre_foto);
          Log.d("ComprasUtils","falta foto"+nombre_foto);
            return null;

        }*/
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight, 0);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile( nombre_foto, options);
    }

    public static HashMap<Integer,String> catalogoToHashMap(List<CatalogoDetalle> lista){
        HashMap<Integer,String> mapa=new HashMap<>();
        for(CatalogoDetalle objeto:lista){
            //TODO en algun momento se tomará la descripción en inglés
            mapa.put(objeto.getCad_idopcion(),objeto.getCad_descripcionesp());
        }
        return mapa;
    }
    public static List<DescripcionGenerica> convertirListaaClientes(List<ListaCompra> lista){
        int i=0;
        List<DescripcionGenerica> mapa=new ArrayList<>();



        for (ListaCompra listaCompra: lista ) {

            DescripcionGenerica item=new DescripcionGenerica();
            item.setId(listaCompra.getClientesId());
            item.setNombre(listaCompra.getClienteNombre());
            mapa.add(item);

        }
        return mapa;
    }

    public static List<DescripcionGenerica> convertirListaaClientessinPep(List<ListaCompra> lista){
        int i=0;
        List<DescripcionGenerica> mapa=new ArrayList<>();



        for (ListaCompra listaCompra: lista ) {
            if(listaCompra.getClientesId()==4)//es pepsi
                continue;
            DescripcionGenerica item=new DescripcionGenerica();
            item.setId(listaCompra.getClientesId());
            item.setNombre(listaCompra.getClienteNombre());
            mapa.add(item);

        }
        return mapa;
    }
    public static  HashMap<Integer,String> convertirListaaPlantas(List<ListaCompra> lista){
        int i=0;
        HashMap<Integer,String> mapa=new HashMap<>();



        for (ListaCompra listaCompra: lista ) {
            mapa.put(listaCompra.getPlantasId(),listaCompra.getPlantaNombre());


        }
        return mapa;
    }
    static public String indiceLetra(String fecha){
       if(fecha==null||fecha.equals(""))
           return "";
        fecha=fecha.replace(".","-");
       String[] mifecha=fecha.split("-");
       String strMes="",lafecha;
        switch (mifecha[0])
        {
            case "01": case "1":
                strMes="ENERO";
                break;
            case "02": case "2":
                strMes="FEBRERO";
                break;
            case "03": case "3":
                strMes="MARZO";
                break;
            case "04": case "4":
                strMes="ABRIL";
                break;
            case "05": case "5":
                strMes="MAYO";
                break;
            case "06": case "6":
                strMes="JUNIO";
                break;
            case "07": case "7":
                strMes="JULIO";
                break;
            case "08": case "8":
                strMes="AGOSTO";
                break;
            case "09": case "9":
                strMes="SEPTIEMBRE";
                break;
            case "10":
                strMes="OCTUBRE";
                break;
            case "11":
                strMes="NOVIEMBRE";
                break;
            case "12":
                strMes="DICIEMBRE";
                break;
        }
        lafecha=strMes+" "+mifecha[1];
        return lafecha;
    }
    static public String mesaLetra(String fecha){

        String strMes="",lafecha;
        switch (fecha)
        {
            case "1":
                strMes="ENERO";
                break;
            case "2":
                strMes="FEBRERO";
                break;
            case "3":
                strMes="MARZO";
                break;
            case "4":
                strMes="ABRIL";
                break;
            case "5":
                strMes="MAYO";
                break;
            case "6":
                strMes="JUNIO";
                break;
            case "7":
                strMes="JULIO";
                break;
            case "8":
                strMes="AGOSTO";
                break;
            case "9":
                strMes="SEPTIEMBRE";
                break;
            case "10":
                strMes="OCTUBRE";
                break;
            case "11":
                strMes="NOVIEMBRE";
                break;
            case "12":
                strMes="DICIEMBRE";
                break;
        }

        return strMes;
    }

    public static void loopViews( LinearLayout layout ,boolean opcion){

        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);


            if (child instanceof EditText) {
                // Do something
                child.setEnabled(opcion);
            }  if (child instanceof Button) {
                // Do something
                child.setEnabled(opcion);
            }  if (child instanceof ImageButton) {
                // Do something
                child.setEnabled(opcion);
            }  if (child instanceof Spinner) {
                // Do something
                child.setEnabled(opcion);
            } else  if (child instanceof LinearLayout) {

                loopViews((LinearLayout) child,opcion);
            }
            /*if (child instanceof ViewGroup) {

                this.loopViews((ViewGroup) child);
            }*/


        }
    }
    //devuelve la fecha de la forma yyyy-mm-dd
    static public String indiceaFecha(String indice){
        if(indice.equals(""))
            return "";
        indice=indice.replace(" ","-");
        String[] mifecha=indice.split("-");
        String strMes="",lafecha;
        return mifecha[1]+"-"+ComprasUtils.cambiaMesaNum(mifecha[0])+"-01";

    }
    //devuelve la fecha de la forma yyyy-mm-dd
    static public String indiceaFecha2(String indice){
        if(indice.equals(""))
            return "";
        indice=indice.replace(".","-");
        String[] mifecha=indice.split("-");
        String strMes="",lafecha;
        return mifecha[1]+"-"+mifecha[0]+"-01";

    }

    static public int cambiaMesaNum(String mes) {
       int num=0;
        // cambia el mes
        switch (mes.toUpperCase()) {
            case "ENERO": num=1;
                break;
            case "FEBRERO":num=2;
                break;
            case "MARZO" :
                num=3;
                break;
            case "ABRIL" :
                num=4;
                break;
            case "MAYO" :
                num=5;
                break;
            case "JUNIO":
                num=6;
                break;
            case "JULIO" :
                num=7;
                break;
            case "AGOSTO" :
                num=8;
                break;
            case "SEPTIEMBRE" :
                num=9;
                break;
            case "OCTUBRE" :
                num=10;
                break;
            case "NOVIEMBRE" :
               num=11;
                break;
            case "DICIEMBRE" :
                num=12;
                break;
        }
        return num;
    }

    public static ActivityManager.MemoryInfo getAvailableMemory(Activity act) {
        ActivityManager activityManager = (ActivityManager) act.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        return memoryInfo;
    }
    //funcion para consultar en las configuraciones si se rota
    public static boolean debeRotar( Context application){
        ConfiguracionRepositoryImpl confRepo=new ConfiguracionRepositoryImpl(application);
        int rotar=0;
        //busco en la bd 1 roto 0 no
      Configuracion conf=confRepo.findsimple(Constantes.CONFROTAR);
      if(conf!=null) {
          try {
              rotar = Integer.parseInt(conf.getValor());
          }catch (NumberFormatException ex){
              ex.printStackTrace();
          }
      }
        if(rotar==1){
            return true;
        }
        return false;
    }


    public static String listaaCadena(List<ImagenDetalle> imagenes){
        String cadenarutas="";

        for(int i=0;i<imagenes.size();i++) {
            if(imagenes.get(i)!=null)
            cadenarutas=cadenarutas+"¬"+imagenes.get(i).getRuta();
        }
        //quito el ¬ inicial
        if(cadenarutas.length()>0){
            cadenarutas=cadenarutas.substring(1);
        }
        return cadenarutas;
    }


}


