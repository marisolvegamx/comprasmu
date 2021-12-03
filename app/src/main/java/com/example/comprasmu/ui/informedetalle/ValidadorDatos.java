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

    public void validarFechaPep(String fecha, int tipoTienda){
        Date hoy=new Date();
        SimpleDateFormat     sdf = new SimpleDateFormat("dd-MM-yyyy");
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
                Log.d("ValidadorDatos","fechacad------------"+sdf.format(hoy));
                Log.d("ValidadorDatos","fechacad------------"+sdf.format(cal.getTime()));

                if (fechacad.getTime()<=hoy.getTime()) { //ya caducó fechacad>=hoy


                    if (tipoTienda == 2||tipoTienda == 3) {
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
    //devuelve false si existe el codigo
    public boolean validarCodigoprodPep(String codigo,String codigonoper){

        if(!codigo.equals("")){

                String arrecodigos[]=codigonoper.split(";");
                if(arrecodigos!=null&&arrecodigos.length>0) {
                    List<String> lista = Arrays.asList(arrecodigos);
                    if (lista.contains(codigo)) {
                        mensaje=R.string.error_codigo_repetido;

                        resp=false;
                        return resp;
                    }
                }

            }

        resp=true;
        return resp;

    }

}
