package com.example.comprasmu.test;

import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.util.Log;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.modelos.MenuVideo;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class FechaTest {

    Context context;

    @Test
    public void convertirFechaTest() {
        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yy HH:mm");

        Log.d("PRUEBAS", "termine:" +new Date(1781240400000L)+"--"+(new Date()).getTime());
        LocalDate fechaActual = Instant.ofEpochMilli(1781240400000L).atOffset(                        // Convert from `Instant` (always in UTC, an offset of zero) to `OffsetDateTime` which can have any offset.
                        ZoneOffset.UTC                // A constant representing an offset of zero hours-minutes-seconds, that is, UTC itself.
                )                                 // Returns a `OffsetDateTime` object.
                .toLocalDate()  ;
        // Formatear la fecha como una cadena
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yy");
        String fechaFormateada = fechaActual.format(formato);
        Log.d("PRUEBAS", fechaActual+" termine:" +fechaFormateada);

        assertTrue(true);
    }
}