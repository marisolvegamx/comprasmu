package com.example.comprasmu.utils;

import android.os.Environment;
import android.util.Log;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ComprasLog {
    private static final String TAG = "ComprasLog";
    String ruta;

    File fichero;
    static private ComprasLog singletonLog = null;



    static public ComprasLog getSingleton() {

        if (singletonLog == null) {
            singletonLog = new ComprasLog();
        }
        return singletonLog;
    }
    private ComprasLog() {



    }


    public void crearLog(String dir) {
        //talvez borrarlo cada semana

        try {
            ruta =dir + "/compraslog.txt";

            fichero = new File(ruta);
            FileOutputStream fileOutputStream = null;
            if (!fichero.exists()) {
                try {
                    fichero.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Error statusFile" + e.getMessage());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private boolean existe(String[] archivos, String archbusca) {
        for (int f = 0; f < archivos.length; f++)
            if (archbusca.equals(archivos[f]))
                return true;
        return false;
    }

    public void grabarError(String error) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
        try {
            if(fichero!=null) {
                BufferedWriter br = new BufferedWriter(new FileWriter(fichero, true));

                br.write(sdf.format(new Date()) + ":" + error+"\r");

                br.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void grabarError(String tag, String metodo, String mensaje){
        this.grabarError(tag+"."+metodo+" "+mensaje);
    }

    public String leerArch() {
        String todo = "";
        try {

            BufferedReader br = new BufferedReader(new FileReader(fichero));
            String linea = br.readLine();


            while (linea != null) {
                todo = todo + linea + "\n";
                linea = br.readLine();


            }
            br.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return todo;
    }
}
