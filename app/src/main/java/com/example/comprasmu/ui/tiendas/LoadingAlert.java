package com.example.comprasmu.ui.tiendas;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.comprasmu.R;

public class LoadingAlert {

    Activity activity;
    AlertDialog dialog;
    String mensaje;
    boolean mostrando=false;
    TextView txtmensaje;
    public LoadingAlert(Activity activity){
        this.activity=activity;
    }
    public LoadingAlert(Activity activity, String mensaje){
        this.activity=activity;
        this.mensaje=mensaje;
    }
    public void startAlert(){
        if(!mostrando){
            AlertDialog.Builder builder=new AlertDialog.Builder(activity);
            LayoutInflater inflater=activity.getLayoutInflater();
            View root=inflater.inflate(R.layout.dialog_layout,null);
            builder.setView(root);
            builder.setCancelable(false);
            txtmensaje=root.findViewById(R.id.di_mensaje);
            if(this.mensaje!=null)
                txtmensaje.setText(mensaje);

            if(activity!=null) {
                dialog = builder.create();
                dialog.show();
            }
        }
        mostrando=true;

    }
    public void closeAlertDialog(){
        if(mostrando) {
            mostrando = false;
            dialog.dismiss();
        }
    }

    public boolean isMostrando() {
        return mostrando;
    }

    public void setMostrando(boolean mostrando) {
        this.mostrando = mostrando;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
