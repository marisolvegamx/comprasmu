package com.example.comprasmu.utils.micamara;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comprasmu.R;
import com.example.comprasmu.utils.ComprasUtils;
import com.github.chrisbanes.photoview.PhotoView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class RevisarPrevActivity extends AppCompatActivity {
    private ImageView imagen1;

    String nombre_foto;

    private TextView foto1;
    public static final String IMG_PATH1 = "comprasmu.img_path1";

    private static final String TAG = "RevisarPrevActivity";

    private long tiempoPrimerClick;

    ScaleGestureDetector objScaleGesturDetector;
    private int result=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revisar_prev);

        //   imagen1=findViewById(R.id.ivrfimagen);
        foto1=findViewById(R.id.txtrffoto1);
        ImageButton btnaceptar=findViewById(R.id.btnrfaceptar);
        PhotoView photoView = findViewById(R.id.photo_view);

        Bundle extras = getIntent().getExtras(); // Aquí es null
        if(extras!=null) {
            btnaceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    aceptar();
                }
            });
            nombre_foto = extras.getString(IMG_PATH1);
            Log.d("algo******", "lll"+nombre_foto);


            File file = new File( nombre_foto);
            if (file.exists()) {
               try {
                   //   Bitmap imageBitmap = (Bitmap) extras.get("data");
                      Bitmap bitmap1 = ComprasUtils.decodeSampledBitmapFromResource( nombre_foto,200,200);
                  // Bitmap bitmap1 = BitmapFactory.decodeFile( nombre_foto);

                   // imagen1.setImageBitmap(bitmap1);
                   photoView.setImageBitmap(bitmap1);


               }catch (Exception ex){
                   ex.printStackTrace();
                   result=0;
               }
            }
            else
                result=0;
        }else
            result=0;
        // objScaleGesturDetector=new ScaleGestureDetector(this,new PinchZoomListener());


    }


    public void cancelar(View v) {
        eliminarfoto();
        Intent resultIntent = new Intent();
        setResult(2, resultIntent);

        //regreso al main
        finish();

    }

    public void eliminarfoto(){
        try{
            File file = new File( nombre_foto);
            if (file.exists()) {
                boolean resp = file.delete();
            }
        }catch(Exception ex){
            ex.printStackTrace();
            result=0;
        }

    }
//devuelvo el control a la actividad principal con un ok=1 o error=0
    public void aceptar(){

        Intent resultIntent = new Intent();
        setResult(result, resultIntent);

        //regreso al main
        finish();
    }
}