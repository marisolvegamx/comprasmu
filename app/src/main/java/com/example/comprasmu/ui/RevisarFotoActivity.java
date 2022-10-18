package com.example.comprasmu.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.comprasmu.R;
import com.github.chrisbanes.photoview.PhotoView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class RevisarFotoActivity extends AppCompatActivity {
    private ImageView imagen1;

    String nombre_foto;

    private TextView foto1;
    public static final String IMG_PATH1 = "comprasmu.img_path1";

    private static final String TAG = RevisarFotoActivity.class.getSimpleName();

    private long tiempoPrimerClick;
    private Toolbar myChildToolbar;
    ScaleGestureDetector objScaleGesturDetector;

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
     //   imagen1=findViewById(R.id.ivrfimagen);
        foto1=findViewById(R.id.txtrffoto1);
        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);

        Bundle extras = getIntent().getExtras(); // Aquí es null
        if(extras!=null) {
            nombre_foto = extras.getString(IMG_PATH1);
            Log.d("algo******", "lll"+nombre_foto);


            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + nombre_foto);
            if (file.exists()) {
                //   Bitmap imageBitmap = (Bitmap) extras.get("data");
            //   Bitmap bitmap1 = ComprasUtils.decodeSampledBitmapFromResource(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + nombre_foto,200,200);
                Bitmap bitmap1 = BitmapFactory.decodeFile(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + nombre_foto);

               // imagen1.setImageBitmap(bitmap1);
                photoView.setImageBitmap(bitmap1);
                foto1.setText(nombre_foto);
            }
        }
       // objScaleGesturDetector=new ScaleGestureDetector(this,new PinchZoomListener());


    }



    @Override
        public void onBackPressed(){

                super.onBackPressed();
                finish();

        }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
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

 /*   @Override
    public boolean onTouchEvent(MotionEvent event) {
        objScaleGesturDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }*/

   /* public class PinchZoomListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        RelativeLayout.LayoutParams params;
        int startwith;
        int startheight;
        float dx = 0, dy = 0, x = 0, y = 0;
        float angle = 0;
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
               float gesturefactor=detector.getScaleFactor();

                if(gesturefactor>1) {
                    Log.d(TAG,"Zoom out");
                }else{
                    Log.d(TAG,"zoom in");
                }
                return super.onScale(detector);

            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return super.onScaleBegin(detector);
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                super.onScaleEnd(detector);
            }
        }*/

}