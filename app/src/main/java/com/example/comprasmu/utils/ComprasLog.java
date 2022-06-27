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

    public ComprasLog() {
        ruta = Environment.getExternalStorageDirectory() + "/compraslog.txt";
        fichero = new File(ruta);

    }


    public void crearLog() {
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

    private boolean existe(String[] archivos, String archbusca) {
        for (int f = 0; f < archivos.length; f++)
            if (archbusca.equals(archivos[f]))
                return true;
        return false;
    }

    public void grabarError(String error) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(fichero, true));

            br.write(sdf.format(new Date()) + ":" + error);

            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


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
