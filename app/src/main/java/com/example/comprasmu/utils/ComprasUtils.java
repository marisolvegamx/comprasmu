package com.example.comprasmu.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.ListaCompra;

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
       String[] mifecha=fecha.split("-");
       String strMes="",lafecha;
        switch (mifecha[0])
        {
            case "01":
                strMes="Enero";
                break;
            case "02":
                strMes="Febrero";
                break;
            case "03":
                strMes="Marzo";
                break;
            case "04":
                strMes="Abril";
                break;
            case "05":
                strMes="Mayo";
                break;
            case "06":
                strMes="Junio";
                break;
            case "07":
                strMes="Julio";
                break;
            case "08":
                strMes="Agosto";
                break;
            case "09":
                strMes="Septiembre";
                break;
            case "10":
                strMes="Octubre";
                break;
            case "11":
                strMes="Noviembre";
                break;
            case "12":
                strMes="Diciembre";
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
                strMes="Enero";
                break;
            case "2":
                strMes="Febrero";
                break;
            case "3":
                strMes="Marzo";
                break;
            case "4":
                strMes="Abril";
                break;
            case "5":
                strMes="Mayo";
                break;
            case "6":
                strMes="Junio";
                break;
            case "7":
                strMes="Julio";
                break;
            case "8":
                strMes="Agosto";
                break;
            case "9":
                strMes="Septiembre";
                break;
            case "10":
                strMes="Octubre";
                break;
            case "11":
                strMes="Noviembre";
                break;
            case "12":
                strMes="Diciembre";
                break;
        }

        return strMes;
    }

}


