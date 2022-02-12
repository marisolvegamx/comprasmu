package com.example.comprasmu.ui.informedetalle;

import android.util.Log;
import android.widget.Toast;

import com.example.comprasmu.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ValidadorDatos {
    public int mensaje;
    public boolean resp;
    SimpleDateFormat     sdf = new SimpleDateFormat("dd-MM-yy");
    SimpleDateFormat     sdfcod = new SimpleDateFormat("dd-MM-yy");

    public void validarFechaPep(String fecha, int tipoTienda){
        Date hoy=new Date();
        Date fechacad;
        if (!fecha.equals("")) {

            try {
                fechacad = sdf.parse(fecha);
            } catch (ParseException e) {

                mensaje= R.string.error_fecha_formato;
                resp= false;
                return ;
            }
            if (!sdf.format(fechacad).equals(fecha))
            {
                mensaje=R.string.error_fecha_formato;
                resp=false;
                return ;
            }
            Calendar cal = Calendar.getInstance(); // Obtenga un calendario utilizando la zona horaria y la configuración regional predeterminadas
            cal.setTime(hoy);
            cal.add(Calendar.DAY_OF_MONTH, +30);

            if (fechacad.getTime()<=hoy.getTime()) { //ya caducó fechacad>=hoy


                if (tipoTienda == 3) {
                        mensaje=R.string.error_fecha_caduca;
                        resp=false;
                        return ;
                }
                else {
                        //ImageButton bton=(ImageButton)view;
                        // bton.setSupportButtonTintList(ContextCompat.getColorStateList(getActivity(), R.color.botonvalido));
                        //bton.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.botonvalido));
                        resp=true;
                        return ;
                }

            }else if (fechacad.compareTo(cal.getTime())<0) { //hoy+30>fechacad
                    //compra si Date>arg->>0
                    Log.d("VAlidadorDatos","wwwwww"+fechacad.compareTo(cal.getTime()));
                    mensaje=R.string.error_fecha_caduca_prox;
                    resp=false;
                    return ;
            } else {
                    resp=true;
                    return;
                }
        }
    }
    //li_btnagregar
    //devuelve false si existe el codigo
    public boolean validarCodigoprodPep(String codigo,String codigonoper){
      //  codigo=codigo.replace("-","");
        Log.d("ValidadorDatos", "mensaje"+codigonoper);
        if(!codigo.equals("")&&codigonoper.length()>1){
                if(!validarCodigoPepRango(codigo,codigonoper)){
                    mensaje=R.string.error_codigo_per;

                    return false;
                }

                //comparo con los no permitido
                String arrecodigos[]=codigonoper.split(";");
                if(arrecodigos!=null&&arrecodigos.length>0) {
                    Log.d("ValidadorDatos","mi codigo"+codigo);
                    List<String> lista = Arrays.asList(arrecodigos);
                    if (lista.contains(codigo)) {
                        mensaje=R.string.error_codigo_per;

                        resp=false;
                        return resp;
                    }
                }

            }

        resp=true;
        return resp;

    }

    public boolean validarCodigoPepRango(String codigo, String codigonoper ){
        Date vfecha=null;
        try {
             vfecha=sdfcod.parse(codigo);

        //comparo con el rango
        //busco el signo
        String rango="";
        int possimb=codigonoper.indexOf("<=");

        if(possimb<1) {
            possimb = codigonoper.indexOf(">=");

            if (possimb < 1) {
                possimb = codigonoper.indexOf(">");

                if (possimb < 1) {
                    possimb = codigonoper.indexOf("<");
                    if (possimb > 0) {
                        rango = codigonoper.substring(possimb);
                        Date fecharan=sdfcod.parse(rango);
                        if(vfecha.before(fecharan) )
                            return false;
                    }
                } else {
                    rango = codigonoper.substring(possimb);
                    Date fecharan=sdfcod.parse(rango);
                    if(vfecha.after(fecharan) )
                        return false;
                }


            } else {
                rango = codigonoper.substring(possimb);
                Date fecharan=sdfcod.parse(rango);
                if(vfecha.after(fecharan) )
                    return false;
            }
        }
        else {
            rango = codigonoper.substring(possimb);
            Date fecharan=sdfcod.parse(rango);
            if(vfecha.before(fecharan) )
                return false;
        }
        return true; //todo bien
        } catch (ParseException e) {
            return false;
        }

    }
    //devuelve si fecha1<fecha2
    public boolean compararFecha(Date fecha1,Date fecha2){
        Date date1= null;
        Date date2=null;
        try {
            date1 = sdf.parse(sdf.format(fecha1));
             date2=sdf.parse(sdf.format(fecha2));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date1.before(date2);
    }

}
