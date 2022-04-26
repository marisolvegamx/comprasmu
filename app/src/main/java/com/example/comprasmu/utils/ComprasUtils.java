package com.example.comprasmu.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.ui.visita.AbririnformeFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ComprasUtils {
    Bitmap rotatedBitmap;

    public static Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
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

        Bitmap bitmapOrg=BitmapFactory.decodeFile(nombre_foto);

        int width=bitmapOrg.getWidth();
        int height=bitmapOrg.getHeight();


        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmapOrg, width, height, true);

        //comprimir imagen
        File file = new File(nombre_foto);
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            Log.d("Compras",e.getMessage());
            // Toast.makeText(, "Error al guardar la foto", Toast.LENGTH_SHORT).show();
            return null;
        }
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 45, os);

       return scaledBitmap;

    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource( String nombre_foto,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
       // BitmapFactory.decodeResource(res, resId, options);
        BitmapFactory.decodeFile(nombre_foto,options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

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
    public static  HashMap<Integer,String> convertirListaaPlantas(List<ListaCompra> lista){
        int i=0;
        HashMap<Integer,String> mapa=new HashMap<>();



        for (ListaCompra listaCompra: lista ) {
            mapa.put(listaCompra.getPlantasId(),listaCompra.getPlantaNombre());


        }
        return mapa;
    }
    static public String indiceLetra(String fecha){
       if(fecha.equals(""))
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
        return mifecha[1]+"-"+mifecha[0]+"-01";

    }

}


