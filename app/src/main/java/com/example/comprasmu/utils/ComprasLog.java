package com.example.comprasmu.utils;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ComprasLog {
    private static final String TAG = "ComprasLog";
    String ruta;
    File fichero;
  public ComprasLog(){
        ruta = Environment.getExternalStorageDirectory() + "/compraslog.txt";
      fichero = new File(ruta);

  }


    protected void crearLog() {
        //talvez borrarlo cada semana

            try {

                FileOutputStream fileOutputStream = null;
                if (!fichero.exists()) {
                    try {
                        fichero.createNewFile();
                    } catch (IOException e) {
                        Log.e(TAG, "Error statusFile" + e.getMessage());
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


    }

    private boolean existe(String[] archivos, String archbusca){
        for(int f=0;f<archivos.length;f++)
            if(archbusca.equals(archivos[f]))
                return true;
        return false;
    }

    public void grabarError(String error){

        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yy HH:mm:ss");
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(fichero, true));

            br.write(sdf.format(new Date())+":"+error);

            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
