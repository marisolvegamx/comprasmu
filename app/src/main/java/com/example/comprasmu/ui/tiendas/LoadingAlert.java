package com.example.comprasmu.ui.tiendas;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.comprasmu.R;

public class LoadingAlert {

    Activity activity;
    AlertDialog dialog;
    boolean mostrando=false;
    public LoadingAlert(Activity activity){
        this.activity=activity;
    }
    public void startAlert(){
        if(!mostrando){
            AlertDialog.Builder builder=new AlertDialog.Builder(activity);
            LayoutInflater inflater=activity.getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.dialog_layout,null));
            builder.setCancelable(false);
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
}
