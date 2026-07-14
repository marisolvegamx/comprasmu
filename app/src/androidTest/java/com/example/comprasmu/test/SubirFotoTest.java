package com.example.comprasmu.test;

import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.remote.SubirFoto;
import com.example.comprasmu.data.repositories.CorreccionRepoImpl;
import com.example.comprasmu.utils.Constantes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;



    /*SubirFoto sf = new SubirFoto();
    String dir = ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/";

    // Intentar subida
    boolean exito = sf.subirFotoSync(Constantes.CLAVEUSUARIO, dir, imagenSubir, indiceimagen, ctx);
*/


@RunWith(AndroidJUnit4.class)
public class SubirFotoTest {

    Context context;

    @Before
    public void createDb() {
        context = ApplicationProvider.getApplicationContext();

    }

    @Test
    public void subirFoto() {
        SubirFoto sf = new SubirFoto();
        String dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/";
        ImagenDetalle imagenSubir = new ImagenDetalle();
        imagenSubir.setRuta("img_29_20260714_191628.jpg");
        // Intentar subida
        boolean exito = sf.subirFotoSync("29", dir, imagenSubir, "12.2025", context);

        assertTrue(exito);
    }
}
