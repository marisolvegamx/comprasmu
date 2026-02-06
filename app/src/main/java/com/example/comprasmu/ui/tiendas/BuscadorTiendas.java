package com.example.comprasmu.ui.tiendas;

import android.location.Location;
import android.util.Log;
import com.example.comprasmu.data.modelos.Tienda;
import com.example.comprasmu.utils.ComprasLog;

import java.util.List;

public class BuscadorTiendas {


    /***Revisa si hay tiendas cerca alrededor de xmts***/
    public boolean hayTiendas(List<Tienda> lista, double xact, double yact, ComprasLog compraslog){
        for (Tienda tienda:lista
             ) {
            //separo las coordenas
            String[] aux = tienda.getUne_coordenadasxy().split(",");
            try {
                double x = Double.parseDouble(aux[0]);
                double y = Double.parseDouble(aux[1]);
                if (dentroDelCirculo(x, y, xact, yact)) {
                    Log.d("tiendas", x + "," + y);
                    Log.d("tiendas hay una tienda", tienda.getUne_descripcion() + "," + xact + "," + yact);
                    compraslog.info("tiendas hay una tienda", ".nuevatienda ", "ya existe"+tienda.getUne_descripcion() + "," + xact + "," + yact);
                    return true; //con una tienda ya no puede ser nueva


                }
            }catch(NumberFormatException ex) {
                Log.d("bUSCADOR TIENDAS", "hayTiendas error al obtener coordenadas de la tienda " + tienda.getUne_descripcion());
            }
        }
        return false;
    }
    public Tienda  hayTiendas2(List<Tienda> lista, double xact, double yact){
        for (Tienda tienda:lista
        ) {
            //separo las coordenas
            String[] aux = tienda.getUne_coordenadasxy().split(",");

            double x=Double.parseDouble(aux[0]);
            double y= Double.parseDouble(aux[1]);
            if( dentroDelCirculo(x,y,xact,yact)) {
                Log.d("tiendas",x+","+y);
                Log.d("tiendas",tienda.getUne_descripcion()+","+xact+","+yact);

                return tienda; //con una tienda ya no puede ser nueva


            }
        }
        return null;
    }
    //x1, y1 es el centro de mi circunferencia ubicacion actual del usuario
    //devuelve true si está dentro de la circunferencia
   public boolean dentroDelCirculo(double x,double y,double x1, double y1) {
        int radio=100;
       // return (Math.pow((x1-x),2) + Math.pow((y1-y),2) <= Math.pow(radio,2));
       float[] results = new float[1];
       Location.distanceBetween(x, y, x1, y1, results);
       Log.d("tiendas res",results[0]+"");
       return (results[0]) <= radio;
    }
}
