package com.example.comprasmu.ui;

import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.ui.informe.NuevoinformeFragment;
import com.example.comprasmu.ui.visita.AbririnformeFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class RevisarFotoActivity extends AppCompatActivity {
    private ImageView imagen1;

    String nombre_foto;
    Bitmap rotatedBitmap;
    private TextView foto1;
    private static final String IMG_PATH1 = "comprasmu.img_path1";
    private static final int IMG_RESULT_OK = 201;
    private static final String TAG = RevisarFotoActivity.class.getSimpleName();
    private static final int INTERVALO = 3000; //2 segundos para salir
    private long tiempoPrimerClick;
    private Toolbar myChildToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revisar_foto);
        myChildToolbar =
                (Toolbar) findViewById(R.id.my_child_toolbar3);
        setSupportActionBar(myChildToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        Bundle extras = getIntent().getExtras(); // Aquí es null
        nombre_foto = extras.getString("ei.archivo");
        imagen1=findViewById(R.id.ivrfimagen);
        foto1=findViewById(R.id.txtrffoto1);
        Log.d("algo******", nombre_foto);
        File file = new File(getExternalFilesDir(null) + "/" + nombre_foto);
        if (file.exists()) {
            //   Bitmap imageBitmap = (Bitmap) extras.get("data");
            Bitmap bitmap1 = BitmapFactory.decodeFile(getExternalFilesDir(null) + "/" + nombre_foto);

            imagen1.setImageBitmap(bitmap1);
            foto1.setText(nombre_foto);
        }
       Button  btnrotar=findViewById(R.id.btnrfrotar);
        btnrotar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotarImagen(getExternalFilesDir(null) + "/" + nombre_foto,imagen1);
            }
        });


        if (savedInstanceState != null) {
            // Restore value of counters from saved state
            foto1.setText(savedInstanceState.getString(IMG_PATH1));

            Bitmap bitmap1 = BitmapFactory.decodeFile(foto1.getText().toString());

            imagen1.setImageBitmap(bitmap1);
            Log.d(TAG, "Restoring Data in onCreate savedInstanceState != null");
        }
    }

        public void onSaveInstanceState(Bundle savedInstanceState) {
            // Save the user's current game state
            savedInstanceState.putString(IMG_PATH1, foto1.getText().toString());
            // Always call the superclass so it can save the view hierarchy state
            super.onSaveInstanceState(savedInstanceState);
        }

        @Override
        public void onBackPressed(){
            if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()){
               // super.onBackPressed();
                finish();
                return;
            }else {
                Toast.makeText(this, "Sí sale sin guardar la información se perderá. Vuelve a presionar para salir", Toast.LENGTH_LONG).show();
            }
            tiempoPrimerClick = System.currentTimeMillis();
        }


        public static void rotarImagen(String nombre_foto,ImageView imagen1){ //conla ruta


            Bitmap bitmapOrg=BitmapFactory.decodeFile(nombre_foto);

            int width=bitmapOrg.getWidth();
            int height=bitmapOrg.getHeight();

            Matrix matrix = new Matrix();

            matrix.postRotate(90);

            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmapOrg, width, height, true);

            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            //comprimir imagen
            File file = new File(nombre_foto);
            OutputStream os = null;
            try {
                os = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                Log.d("Compras",e.getMessage());
               // Toast.makeText(, "Error al guardar la foto", Toast.LENGTH_SHORT).show();
                return;
            }
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);

            imagen1.setImageBitmap(rotatedBitmap);


        }
    public void agregar(View v) {
        Log.d(TAG, "click en agregar");
        //reviso si hay datos
        try {


             ActivityManager.MemoryInfo memoryInfo = getAvailableMemory();

             if (!memoryInfo.lowMemory) {
                        Bitmap bitmapOrg = BitmapFactory.decodeFile(getExternalFilesDir(null) + "/" + nombre_foto);

                        //comprimir imagen
                        File file = new File(getExternalFilesDir(null) + "/" + nombre_foto);
                        OutputStream os = null;
                        try {
                            os = new FileOutputStream(file);
                        } catch (FileNotFoundException e) {
                            Log.d("RevisarFotoActivity", e.getMessage());
                            Toast.makeText(this,"No se encontró el archivo" , Toast.LENGTH_SHORT).show();

                        }
                        bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 45, os);
                        limpiarCampos();
                                                //volver a actividad principal
                         Intent resultIntent = new Intent();

                        resultIntent.putExtra(AbririnformeFragment.ARG_FOTONUEVA, nombre_foto);
                        setResult(IMG_RESULT_OK, resultIntent);
                        finish();


                    }
                    else
                    {
                        Toast.makeText(this, "Memoria insuficiente, no se guardó el reporte", Toast.LENGTH_SHORT).show();

                    }

        } catch (Exception ex) {
            Log.d(RevisarFotoActivity.class.getName(),ex.getMessage());
            Toast.makeText(this, "Algo salió mal", Toast.LENGTH_SHORT).show();
        }

    }
        public void limpiarCampos(){
            foto1.setText("");


        }
        // Get a MemoryInfo object for the device's current memory status.
        private ActivityManager.MemoryInfo getAvailableMemory() {
            ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            activityManager.getMemoryInfo(memoryInfo);
            return memoryInfo;
        }
        public void cancelar(View v) {
            if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()){
          /*  super.onBackPressed();
            return;*/
                Intent intento1 = new Intent(this, NavigationDrawerActivity.class);
                startActivity(intento1);
                //faltaria saber a que fragment
            }else {
                Toast.makeText(this, "Sí sale sin guardar la información esta se perderá. Vuelve a presionar para salir", Toast.LENGTH_LONG).show();
            }
            tiempoPrimerClick = System.currentTimeMillis();

        }

}