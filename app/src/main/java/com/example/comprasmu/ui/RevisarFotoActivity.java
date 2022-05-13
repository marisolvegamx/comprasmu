package com.example.comprasmu.ui;

import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;

import android.os.Environment;
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
    public static final String IMG_PATH1 = "comprasmu.img_path1";
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
        nombre_foto = extras.getString(IMG_PATH1);
        imagen1=findViewById(R.id.ivrfimagen);
        foto1=findViewById(R.id.txtrffoto1);
        Log.d("algo******", nombre_foto);
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + nombre_foto);
        if (file.exists()) {
            //   Bitmap imageBitmap = (Bitmap) extras.get("data");
            Bitmap bitmap1 = BitmapFactory.decodeFile(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + nombre_foto);

            imagen1.setImageBitmap(bitmap1);
            foto1.setText(nombre_foto);
        }



    }

        @Override
        public void onBackPressed(){

                super.onBackPressed();
                finish();

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

        public void cancelar(View v) {
            super.onBackPressed();
            return;

        }

}