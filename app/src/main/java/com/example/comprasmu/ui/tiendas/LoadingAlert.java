package com.example.comprasmu.ui.tiendas;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.comprasmu.R;

public class LoadingAlert {

    Activity activity;
    AlertDialog dialog;
    public LoadingAlert(Activity activity){
        this.activity=activity;
    }
    void startAlert(){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        LayoutInflater inflater=activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_layout,null));
        builder.setCancelable(false);
        dialog=builder.create();
        dialog.show();
    }
    void closeAlertDialog(){
        dialog.dismiss();
    }

}
