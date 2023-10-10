package com.example.comprasmu.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.services.SubirColaFotoService;

import java.util.ArrayList;
import java.util.List;

public class MasPruebasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mas_pruebas);
        List<ImagenDetalle> lista=new ArrayList<>();
        String cadenarutas="";
        String indice;
        indice="10.2022";
        String ruta="";
        for(int i=1;i<13;i++) {
            ruta="foto"+i+".jpg";
            cadenarutas=cadenarutas+"¬"+ruta;
        }

            Log.d("MasPruebas",cadenarutas);
            //subo cada una
            Intent msgIntent = new Intent(this, SubirColaFotoService.class);
            msgIntent.putExtra(SubirColaFotoService.EXTRA_IMAGE_ID, 100);
            msgIntent.putExtra(SubirColaFotoService.EXTRA_IMG_PATH,cadenarutas);
            // Log.d(TAG,"error "+informe.get)
            msgIntent.putExtra(SubirColaFotoService.EXTRA_INDICE,indice);
            msgIntent.setAction(SubirColaFotoService.ACTION_UPLOAD_LISTA);

            // Constantes.INDICEACTUAL
            Log.d("MasPruebas", "subiendo fotos");
            startService(msgIntent);
            //cambio su estatus a subiendo


    }

   /* protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mas_pruebas);
        List<ImagenDetalle> lista=new ArrayList<>();
        String cadenarutas="";
        String indice;
        indice="10.2022";
        String ruta="";
        for(int i=1;i<13;i++) {
            ruta="foto"+i+".jpg";
            cadenarutas=cadenarutas+"¬"+ruta;
        }

        /*        imagen=new ImagenDetalle();
        imagen.setId(1);
        imagen.setIndice("10.2022");
        imagen.setRuta("montana.jpg");
        lista.add(imagen);
         imagen=new ImagenDetalle();
        imagen.setId(2);
        imagen.setIndice("10.2022");
        imagen.setRuta("nebulosa1.png");
        lista.add(imagen);
         imagen=new ImagenDetalle();
        imagen.setId(3);
        imagen.setIndice("10.2022");
        imagen.setRuta("2017-09-14194443.jpg");
        lista.add(imagen);
        imagen=new ImagenDetalle();
        imagen.setId(3);
        imagen.setIndice("10.2022");
        imagen.setRuta("IMG_20170827_202239.jpg");
        lista.add(imagen);
        imagen=new ImagenDetalle();
        imagen.setId(3);
        imagen.setIndice("10.2022");
        imagen.setRuta("IMG_20170819_113041.jpg");
        lista.add(imagen);*/
      /*  for(ImagenDetalle imagenc:lista) {
            //subo cada una
            Intent msgIntent = new Intent(this, SubirFotoService.class);
            msgIntent.putExtra(SubirFotoService.EXTRA_IMAGE_ID, imagenc.getId());
            msgIntent.putExtra(SubirFotoService.EXTRA_IMG_PATH, imagenc.getRuta());
            // Log.d(TAG,"error "+informe.get)
            msgIntent.putExtra(SubirFotoService.EXTRA_INDICE, imagenc.getIndice());
            msgIntent.setAction(SubirFotoService.ACTION_UPLOAD_FILA);

            // Constantes.INDICEACTUAL
            Log.d("Prueba fotos", "subiendo fotos");
            startService(msgIntent);
            //cambio su estatus a subiendo
        }

    }*/
}