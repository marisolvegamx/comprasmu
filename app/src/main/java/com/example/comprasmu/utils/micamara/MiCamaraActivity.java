package com.example.comprasmu.utils.micamara;

import static androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.MeteringPoint;
import androidx.camera.core.Preview;
import androidx.camera.core.TorchState;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
//import androidx.camera.extensions.HdrImageCaptureExtender;
import androidx.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.ScaleGestureDetector;
import android.view.Surface;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.comprasmu.R;
import com.example.comprasmu.databinding.ActivityMicamaraBinding;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.ComprasUtils;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MiCamaraActivity extends AppCompatActivity {

    private ActivityMicamaraBinding viewBinding;
    private ExecutorService cameraExecutor;
    private Executor executor = Executors.newSingleThreadExecutor();
    private int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};

    PreviewView mPreviewView;
    ImageButton captureImage,btnflash;
    Camera camera;
    CameraSelector cameraSelector;
    int flasMode;
    String TAG="CameraApp";
    public static int REQUEST_CODE_TAKE_PHOTO=300;
    private String archivo_foto;
    int resultact =1;
    ComprasLog milog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_micamara);

        mPreviewView = findViewById(R.id.viewFinder);
        captureImage = findViewById(R.id.image_capture_button);
        btnflash = findViewById(R.id.flash_button);
        Bundle extras = getIntent().getExtras(); // Aquí es null
        milog=ComprasLog.getSingleton();

        if(extras!=null) {
            //llega el absolute path del archivo
            archivo_foto = extras.getString(MediaStore.EXTRA_OUTPUT);
        }
        if(ComprasUtils.getAvailableMemory(this).lowMemory)
        {
            Toast.makeText(this, "No hay memoria suficiente para esta acción", Toast.LENGTH_SHORT).show();

            return;
        }
        ImageButton btncancelar=findViewById(R.id.btnmccancelar);
        if(allPermissionsGranted()){
            startCamera(false); //start camera if permission has been granted by user
        } else{
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
        btncancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelar();
            }
        });
    }

    private void startCamera(boolean enableTorch) {

        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {

                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider,enableTorch);

                } catch (ExecutionException | InterruptedException e) {
                    // No errors need to be handled for this Future.
                    // This should never be reached.
                }
            }
        }, ContextCompat.getMainExecutor(this));


            }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider, boolean enableTorch) {

        try {
            Preview preview = new Preview.Builder()
                    //  .setTargetResolution(new Size(800, 600))
                    .build();


            cameraSelector = new CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build();

            ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                    .build();

            ImageCapture.Builder builder = new ImageCapture.Builder();

            //Vendor-Extensions (The CameraX extensions dependency in build.gradle)
           // HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(builder);

            // Query if extension is available (optional).
        //    if (hdrImageCaptureExtender.isExtensionAvailable(cameraSelector)) {
                // Enable the extension if available.
        //        hdrImageCaptureExtender.enableExtension(cameraSelector);
         //   }

            final ImageCapture imageCapture = builder
                    .setTargetRotation(this.getWindowManager().getDefaultDisplay().getRotation())
                    //   .setTargetResolution(new Size(800, 600))
                    .setCaptureMode(CAPTURE_MODE_MAXIMIZE_QUALITY)
                    .build();
     /*   ViewPort viewPort = new ViewPort.Builder(
                new Rational(800, 600),
                getDisplay().getRotation()).build();
        UseCaseGroup useCaseGroup = new UseCaseGroup.Builder()
                .addUseCase(preview)
                .addUseCase(imageAnalysis)
                .addUseCase(imageCapture)
                .setViewPort(viewPort)
                .build();*/
            preview.setSurfaceProvider(mPreviewView.createSurfaceProvider());
            cameraProvider.unbindAll();
            camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageAnalysis, imageCapture);
            camera.getCameraControl().enableTorch(enableTorch);
            CameraControl cameraControl = camera.getCameraControl();
            ScaleGestureDetector.OnScaleGestureListener listener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
                @Override
                public boolean onScale(ScaleGestureDetector detector) {

                    // Get the camera's current zoom ratio
                    //float currentZoomRatio = camera.getCameraInfo().getZoomState().getValue().getZoomRatio() ?: 0F;
                    float currentZoomRatio = camera.getCameraInfo().getZoomState().getValue().getZoomRatio();

                    // Get the pinch gesture's scaling factor
                    float delta = detector.getScaleFactor();

                    // Update the camera's zoom ratio. This is an asynchronous operation that returns
                    // a ListenableFuture, allowing you to listen to when the operation completes.
                    cameraControl.setZoomRatio(currentZoomRatio * delta);

                    // Return true, as the event was handled
                    return true;
                }
            };
            ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(this, listener);
            mPreviewView.setOnTouchListener((view, event) -> {
                scaleGestureDetector.onTouchEvent(event);

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        return true;
                    case MotionEvent.ACTION_UP:
                        MeteringPoint point = mPreviewView.createMeteringPointFactory(cameraSelector).createPoint(event.getX(), event.getY());
                        FocusMeteringAction action = new FocusMeteringAction.Builder(point).build();

                        camera.getCameraControl().startFocusAndMetering(action);

                        // HOW TO SHOW RECTANGLE SIGHT HERE? Thanx!

                        return true;
                    default:
                        return false;
                }
            });

            // Must unbind the use-cases before rebinding them
            //cameraProvider.unbindAll();

            OrientationEventListener orientationEventListener = new OrientationEventListener((Context) this) {
                @Override
                public void onOrientationChanged(int orientation) {
                    int rotation;

                    // Monitors orientation values to determine the target rotation value
                    if (orientation >= 45 && orientation < 135) {
                        rotation = Surface.ROTATION_270;
                    } else if (orientation >= 135 && orientation < 225) {
                        rotation = Surface.ROTATION_180;
                    } else if (orientation >= 225 && orientation < 315) {
                        rotation = Surface.ROTATION_90;
                    } else {
                        rotation = Surface.ROTATION_0;
                    }
                    //   System.out.println("giro"+rotation);
                    imageCapture.setTargetRotation(rotation);
                }
            };

            orientationEventListener.enable();

            captureImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    File file = new File(archivo_foto);
                    //  archivo_foto=file.getName();
                    ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
                    imageCapture.takePicture(outputFileOptions, executor, new ImageCapture.OnImageSavedCallback() {
                        @Override
                        public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                            //   new Handler().post(new Runnable() {
                            //     @Override
                            //     public void run() {
                            //   Toast.makeText(MainActivity.this, "Image Saved successfully", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "la imagen se tomo correctamente " + file.getName());
                            //veo la imagen
                            try {
                                // este no getRotacion(archivo_foto);
                                if (ComprasUtils.debeRotar(MiCamaraActivity.this)) {
                                    getRotacionConf(archivo_foto); //o sea no funcionará getrotacion2
                                } else
                                    getRotacion2(archivo_foto);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            vistaPrevia();
                            //    }
                            //      });
                            //veo la rotacion

                            //if(girarFoto()){
                            //     rotateImage(file.getPath(),90);
                            //  }

                        }

                        @Override
                        public void onError(@NonNull ImageCaptureException error) {
                            error.printStackTrace();
                        }
                    });
                }
            });
            btnflash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (camera.getCameraInfo().hasFlashUnit()) {
                        cameraControl.enableTorch(camera.getCameraInfo().getTorchState().getValue() == TorchState.OFF);
                    }
                }
            });
            camera.getCameraInfo().getTorchState().observe(this, new Observer<Integer>() {
                        @Override
                        public void onChanged(Integer integer) { //para cambiar imagen del boton
                            if (integer == TorchState.OFF) {
                                btnflash.setImageResource(R.drawable.flash_on);
                            } else {
                                btnflash.setImageResource(R.drawable.flash_off);
                            }
                        }
                    }
            );
        }catch(Exception ex){
            ex.printStackTrace();
           milog.grabarError(TAG+" "+ex.getMessage());
        }

    }

    public String getBatchDirectoryName() {

        String app_folder_path = "";
       // activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        //app_folder_path = Environment.getExternalStorageDirectory().toString() + "/images";
        app_folder_path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() ;

        File dir = new File(app_folder_path);
        if (!dir.exists() && !dir.mkdirs()) {
            Log.e(TAG,"no existe el directorio"+app_folder_path);
        }

        return app_folder_path;
    }

    private boolean allPermissionsGranted(){

        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if(allPermissionsGranted()){
                startCamera(false);
            } else{
                Toast.makeText(this, "Es necesario que de todos los permisos para el funcionamiento de la aplicación.", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }
    }


    public void getRotacion(String filePath) throws IOException {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inPurgeable = true;

     //   Bitmap cameraBitmap = BitmapFactory.decodeFile(filePath);//get file path from intent when you take iamge.
     //   ByteArrayOutputStream bos = new ByteArrayOutputStream();
     //   cameraBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);


        ExifInterface exif = new ExifInterface(filePath);
        float rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        System.out.println("rot*"+rotation);

        float rotationInDegrees = exifToDegrees(rotation);//queda igual aunque la gire
        System.out.println("indegres"+rotationInDegrees);

    }
    public void rotateImage(String filePath, float angle) {
        Bitmap source = BitmapFactory.decodeFile(filePath);//get file path from intent when you take iamge.

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap rotatedBitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
        //comprimir imagen
        File file = new File(filePath);
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            Log.d("Compras", e.getMessage());
            //   Toast.makeText(this, getResources().getString(R.string.errorImagen), Toast.LENGTH_SHORT).show();
            resultact=0;
        }
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);

    }
    private static float exifToDegrees(float exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }


    private void getRotacion2(String photoPath) throws IOException {
        ExifInterface ei = null;

        ei = new ExifInterface(photoPath);

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);
        System.out.println(">>>"+orientation);

        int rotate=0;
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90: //6
                rotate=90;

                break;

            case ExifInterface.ORIENTATION_ROTATE_180: //3
                // rotatedBitmap = rotateImage(bitmap, 180);
                rotate=180;
                break;

            case ExifInterface.ORIENTATION_ROTATE_270: //8
                //rotatedBitmap = rotateImage(bitmap, 270);
                rotate=270;
                break;


            default:
                System.out.println("sepa");
                rotate=0;
        }
        if(rotate!=0){
            rotateImage(photoPath, rotate);
        }
    }
    //para el caso de que no funcione rotar 2
    //lo hago en base a la configuración
    private void getRotacionConf(String photoPath) throws IOException {
        ExifInterface ei = null;

        ei = new ExifInterface(photoPath);

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);
        System.out.println(">>>"+orientation);



        System.out.println(">>>"+orientation);

        int rotate=0;
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90: //6
                rotate=0;

                break;

            case ExifInterface.ORIENTATION_ROTATE_180: //3
                // rotatedBitmap = rotateImage(bitmap, 180);
                rotate=270;
                break;

            case ExifInterface.ORIENTATION_ROTATE_270: //8
                //rotatedBitmap = rotateImage(bitmap, 270);
                rotate=180;
                break;


            default:
                System.out.println("sepa"); //para normal y undefined
                rotate=90;
        }
        if(rotate!=0){
            rotateImage(photoPath, rotate);
        }
    }


   /* @Override
    private void onError(ImageCaptureException exc) {
        Log.e(TAG, "Photo capture failed: ${exc.message}", exc);
    }*/

    private void vistaPrevia( ) {
        Intent intento=new Intent(this, RevisarPrevActivity.class);
        intento.putExtra(RevisarPrevActivity.IMG_PATH1,archivo_foto);
        startActivityForResult(intento,REQUEST_CODE_TAKE_PHOTO);
    }
        //se guarda 1 si hay que girarla
    public boolean  girarFoto() //devuelve tru si hay que girarla
    {
        SharedPreferences prefe = getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        String girar= prefe.getString("girarfoto", "1");

        if(girar.equals("1")){
            return true; //sale en paisaje
        }
      return false;

    }
    public void guardarGirar(){


        SharedPreferences prefe=getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=prefe.edit();

        editor.putString("girarfoto", "1");
         editor.commit();



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CODE_TAKE_PHOTO) && resultCode == 1) {
            //   super.onActivityResult(requestCode, resultCode, data);

            if (archivo_foto != null) {


                // Bitmap bitmap1 = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null) + "/" + nombre_foto);
                    /*    ComprasUtils cu = new ComprasUtils();
                        cu.comprimirImagen(archivofoto.getAbsolutePath());
                      */
                resultact =-1;

            } else {
                resultact =0;
                Log.e(TAG, "Algo salió mal???");
            }
        }else
            if(resultCode == 2) //se canceló
            {
                //no hago nada
                return;
            }else
            resultact =0;
        regresarResult();
        }

        public void regresarResult(){
            Intent resultIntent = new Intent();
            setResult(resultact, resultIntent);

            //regreso al main
            finish();
        }
    public void cancelar() {
       finish();
    }
}