
package com.example.comprasmu;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.dao.InformeCompraDao;
import com.example.comprasmu.data.modelos.Contrato;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.SolicitudCor;
import com.example.comprasmu.data.modelos.TablaVersiones;
import com.example.comprasmu.data.modelos.Visita;
import com.example.comprasmu.data.remote.MuestraCancelada;
import com.example.comprasmu.data.remote.RespInformesResponse;
import com.example.comprasmu.data.remote.SolCorreResponse;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.SolicitudCorRepoImpl;
import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;
import com.example.comprasmu.data.repositories.VisitaRepositoryImpl;
import com.example.comprasmu.services.SubirFotoService;
import com.example.comprasmu.ui.home.HomeActivity;
import com.example.comprasmu.ui.home.MasPruebasActivity;
import com.example.comprasmu.ui.infetapa.ContInfEtaViewModel;
import com.example.comprasmu.ui.informe.DetalleCancelado;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.ui.mantenimiento.ConfiguracionCamFragment;
import com.example.comprasmu.ui.mantenimiento.LeerLogActivity;
import com.example.comprasmu.ui.solcorreccion.ListaSolsViewModel;
import com.example.comprasmu.ui.tiendas.MapaCdFragment;
import com.example.comprasmu.ui.visita.AbririnformeFragment;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.workmanager.SyncWork;
import com.google.android.material.navigation.NavigationView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;

import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;


/*esta es la clase principal con el menu***/
public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String ETAPA = "comprasmu.ndetapa";
    private AppBarConfiguration mAppBarConfiguration;
    SubirFotoProgressReceiver rcv;
    String TAG="NavigationDrawerActivity";
    private ListaDetalleViewModel mViewModel;
    VisitaRepositoryImpl visRepo;
    InformeComDetRepositoryImpl infdrepo;
    InformeCompraRepositoryImpl infrepo;
   // private static final String DOWNLOAD_PATH = "https://muesmerc.mx/comprasv1/fotografias";
    private   String DESTINATION_PATH ;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private ListaSolsViewModel scViewModel;
    public static final String PROGRESS_UPDATE = "progress_update";
    public static final String PROGRESS_PEND = "progress_pend";
    public static final String NAVINICIAL="nd_navinicial";
    TextView txtcancel,gallery;

    LiveData<Integer> totCorrecciones;

    MutableLiveData<Integer> totCancel;
    MutableLiveData<Integer> totMuestraAdic;
    SolicitudCorRepoImpl solRepo;
    TablaVersionesRepImpl tvRepo;
    boolean notificar=false;
    int desclis; int descinf; int descfoto;

    NavigationView navigationView;
    private ComprasLog flog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            flog = ComprasLog.getSingleton();
            flog.crearLog(this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getPath());

            definirTrabajo();

            setContentView(R.layout.activity_navigation_darawer);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            if (savedInstanceState != null) {    // Restore value of members from saved state
                buscarEtapa();
            }
            //  FloatingActionButton fab = findViewById(R.id.fab);
            //busco el mes actual y le agrego 1
            mViewModel = new ViewModelProvider(this).get(ListaDetalleViewModel.class);
            scViewModel = new ViewModelProvider(this).get(ListaSolsViewModel.class);



     /*   fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ir a nuevo informe
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


            Bundle extras = getIntent().getExtras(); // Aquí es null
            String inicio = "";
            if (Constantes.ETAPAACTUAL == 0) {
                //mando al menu inicial
                Intent homeIntent = new Intent(this, HomeActivity.class);

                startActivity(homeIntent);
                finish();
            }
            if (extras != null) {
                inicio = extras.getString(NAVINICIAL);
                if (Constantes.ETAPAACTUAL == 0)
                    Constantes.ETAPAACTUAL = extras.getInt(ETAPA);
            }

            Log.d(TAG, "pso x aqui" + Constantes.ETAPAACTUAL);
            navigationView = findViewById(R.id.nav_view);
            //cargo el menu lateral de acuerdo a la etapa
            if (Constantes.ETAPAACTUAL == 1) {
                navigationView.getMenu().clear();

                navigationView.inflateMenu(R.menu.activity_main_drawerprep);
                View header = navigationView.getHeaderView(0);
                header.setBackgroundResource(R.drawable.side_nav_barpre);
                TextView mNameTextView = header.findViewById(R.id.txthmmodulo);
                mNameTextView.setText(R.string.preparacion);
            }
            if (Constantes.ETAPAACTUAL == 2) {
                navigationView.getMenu().clear();

                navigationView.inflateMenu(R.menu.activity_main_drawer);
                View header = navigationView.getHeaderView(0);
                header.setBackgroundResource(R.drawable.side_nav_bar);
                TextView mNameTextView = header.findViewById(R.id.txthmmodulo);
                mNameTextView.setText(R.string.compra);
            }
            if (Constantes.ETAPAACTUAL == 3) {
                navigationView.getMenu().clear();

                navigationView.inflateMenu(R.menu.activity_main_draweretiq);
                View header = navigationView.getHeaderView(0);
                header.setBackgroundResource(R.drawable.side_nav_bareti);
                TextView mNameTextView = header.findViewById(R.id.txthmmodulo);
                mNameTextView.setText(R.string.etiquetado);


            }
            if (Constantes.ETAPAACTUAL == 4) {
                navigationView.getMenu().clear();

                navigationView.inflateMenu(R.menu.activity_main_draweremp);
//            navigationView.getMenu().setGroupVisible(R.id.HelpGroup,false);
                //navigationView.getMenu().setGroupVisible(R.id.SetupGroup,false);

                View header = navigationView.getHeaderView(0);
                header.setBackgroundResource(R.drawable.side_nav_baremp);
                TextView mNameTextView = header.findViewById(R.id.txthmmodulo);
                mNameTextView.setTextColor(Color.BLACK);
                mNameTextView.setText(R.string.empaque);
            }
            if (Constantes.ETAPAACTUAL == 5) {
                navigationView.getMenu().clear();

                navigationView.inflateMenu(R.menu.activity_main_drawerenv);
//
                View header = navigationView.getHeaderView(0);
                header.setBackgroundResource(R.drawable.side_nav_barenv);
                TextView mNameTextView = header.findViewById(R.id.txthmmodulo);
                mNameTextView.setTextColor(Color.BLACK);
                mNameTextView.setText(R.string.envio);
            }
            if (Constantes.ETAPAACTUAL == 6) {
                navigationView.getMenu().clear();

                navigationView.inflateMenu(R.menu.activity_main_drawergas);
//
                View header = navigationView.getHeaderView(0);
                header.setBackgroundResource(R.drawable.side_nav_bargas);
                TextView mNameTextView = header.findViewById(R.id.txthmmodulo);
                mNameTextView.setTextColor(Color.BLACK);
                mNameTextView.setText(R.string.gastos);
            }

            //navigationView.setNavigationItemSelectedListener(this);
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.mobile_navigation)
                    .setDrawerLayout(drawer)
                    .build();
            //inicio el log
            NavController navController;

            navController = Navigation.findNavController(this, R.id.nav_host_fragment);

            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);
            //  navigationView.setNavigationItemSelectedListener(this);
            NavInflater navInflater = navController.getNavInflater();
            NavGraph graph = navInflater.inflate(R.navigation.mobile_navigation);

            IntentFilter filter = new IntentFilter();
            filter.addAction(SubirFotoService.ACTION_UPLOAD_IMG);

            rcv = new SubirFotoProgressReceiver();
            registerReceiver(rcv, filter);
            LocalBroadcastManager.getInstance(this).registerReceiver(rcv, filter);
            Log.e(TAG, "wwww" + inicio);
            Log.d(TAG, "***** indice " + Constantes.CLAVEUSUARIO);


            if (Constantes.CLAVEUSUARIO.equals("")) {
                graph.setStartDestination(R.id.nav_configurar);
                //cargo todos o le digo que la configure?
//            AlertDialog.Builder builder=new AlertDialog.Builder(this);
//            builder.setCancelable(false);
//
//            builder.setTitle("IMPORTANTE");
//            builder.setMessage("Es necesario configurar su clave de usuario");
//            //builder.setInverseBackgroundForced(true);
//            builder.setNegativeButton(R.string.aceptar, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface builder, int id) {
//                    //  dialogo1.cancel();
//                    //envio a la lista
//                   // FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                    //HomeFragment fragconfig=new HomeFragment();
//                    //ft.add(R.id.nav_host_fragment, fragconfig);
//                    //ft.commit();
//                    graph.setStartDestination(R.id.nav_host_fragment);
//
//                }
//            });
//            AlertDialog alert=builder.create();
//
//            alert.show();


            } else if (inicio != null && inicio.equals("listainforme")) {
            /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = new BuscarInformeFragment();
            ft.add(R.id., fragment);
            ft.commit();*/
                graph.setStartDestination(R.id.nav_listar);
            }
            if (inicio != null && inicio.equals("continuarinf")) {
            /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = new BuscarInformeFragment();
            ft.add(R.id., fragment);
            ft.commit();*/
                graph.setStartDestination(R.id.nav_listarvisitas);
            }
            if (inicio != null && inicio.equals("nav_reubicetiq")) {
            /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = new BuscarInformeFragment();
            ft.add(R.id., fragment);
            ft.commit();*/
                graph.setStartDestination(R.id.nav_reubicetiq);
            } else {
                //descargasIniciales();
                pedirCorrecciones(0, Constantes.ETAPAACTUAL);
                graph.setStartDestination(R.id.nav_home);
            }
            navController.setGraph(graph);
            //pongo canceladas x etapa
            if (Constantes.ETAPAACTUAL == 1) {
                gallery = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                        findItem(R.id.nav_notificaciongen));
            }
            if (Constantes.ETAPAACTUAL == 2) {
                //   gallery = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                //         findItem(R.id.nav_solcor2));
                gallery = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                        findItem(R.id.nav_notificaciongen));
            /*txtcancel=(TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                    findItem(R.id.nav_cancel));*/

            }
            if (Constantes.ETAPAACTUAL == 3) {
                //Ya lo hago desde el menu
                // pedirInformes(0);
                gallery = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                        findItem(R.id.nav_notificaciongen));
            }
            if (Constantes.ETAPAACTUAL == 4) {
                gallery = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                        findItem(R.id.nav_notificaciongen));
            }
            if (Constantes.ETAPAACTUAL == 5) {

                gallery = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                        findItem(R.id.nav_notificaciongen));
            }
            if (Constantes.ETAPAACTUAL == 6) {
                //Ya lo hago desde el menu
                // pedirInformes(0);
                gallery = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                        findItem(R.id.nav_notificaciongen));
            }
            initializeCountDrawer();
            revisarCiudades();
            if (checkPermission()) {
                //main logic or main code

                // . write your main code to execute, It will execute if the permission is already given.
            } else {
                requestPermission();
            }
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresBatteryNotLow(true)
                    .build();
        }catch(Exception ex){


            flog.grabarError(TAG,"oncreate",ex.getMessage());
        }
       /* PeriodicWorkRequest simpleRequest =
                new PeriodicWorkRequest.Builder(SyncWork.class, 30, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .addTag("comprassync_worker")
                        .build();*/
      /*  WorkManager
                .getInstance(this)
                .enqueueUniquePeriodicWork(
                "comprassync_worker",
                ExistingPeriodicWorkPolicy.KEEP,
                simpleRequest);*/
       // flog.grabarError("archivo creado");
       /* ServicioCompras sbt = new ServicioCompras();


        try {
            sbt.iniciar(this.getApplicationContext(), "marisol");
        } catch (Exception e) {
            Log.d(TAG,"Error al iniciar el servicio");
            e.printStackTrace();
        }*/
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
   //   initializeCountDrawer();
        return super.onCreateView(parent, name, context, attrs);
    }

    //saber si tiene mas de una ciudad para mostrar seleccionar ciudad
    public void revisarCiudades(){

        Log.d(TAG,"ciudades"+Constantes.CIUDADTRABAJO);

            mViewModel.getCiudades().observe(this, data -> {
               // if(data!=null)
              //      Log.d(TAG,"....regresó de la consulta "+ data.size());
                if(data!=null&&data.size()>1)
                    Constantes.varciudades = true;


                else if(data.size()>0&&data.get(0)!=null) {
                    Log.d(TAG,"ciudades"+data.get(0).getCiudadNombre());
                    //para corregir erro de borrado
                    if(!Constantes.CIUDADTRABAJO.equals("")) //no está vacia y no es la que tengo
                    {
                        if(!Constantes.CIUDADTRABAJO.equals(data.get(0).getCiudadNombre())) {
                            Constantes.CIUDADTRABAJO = "";
                            Constantes.IDCIUDADTRABAJO = 0;
                            guardarCiudadPref("");
                        }
                    }
                    if (Constantes.CIUDADTRABAJO.equals("")){

                        Constantes.CIUDADTRABAJO = data.get(0).getCiudadNombre();
                        Log.d(TAG,"ciudades"+Constantes.CIUDADTRABAJO);

                        Constantes.IDCIUDADTRABAJO = data.get(0).getCiudadesId();
                         //guardo en pref

                         guardarCiudadPref(Constantes.CIUDADTRABAJO);
                }
                    Constantes.varciudades = false;
                 //   if(Constantes.ETAPAACTUAL==4||Constantes.ETAPAACTUAL==3)
                  //      return; //ya no importa la ciudad
                    NavigationView navigationView =  findViewById(R.id.nav_view);
                    navigationView.getMenu().findItem(R.id.nav_ciudad_trabajo).setVisible(false);

                }


            });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_darawer, menu);
        return true;
    }

    /*soporte para la bnavegacion hacia arriba*/
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavController navController;
        switch (item.getItemId()) {
            case R.id.action_settings:

                //  Log.d(TAG,"hice click en"+item.getItemId());
            //    pruebadescarga();
              //  startService(DownloadSongService.getDownloadService(this, IMAGE_DOWNLOAD_PATH, DirectoryHelper.ROOT_DIRECTORY_NAME.concat("/")));
               // NavHostFragment.findNavController(this).navigate(R.id.nav_nuevoinforme);
               // Navigation.findNavController( R.id.nav_host_fragment).navigate(action);
                // NavHostFragment.findNavController(this,R.id.nav_configurar);

               // NavHostFragment.findNavController(ConfiguracionCamFragment.this);


                navController = Navigation.findNavController(this, R.id.nav_host_fragment);

                navController.navigate(R.id.nav_configurar);
                return true;


            case R.id.action_mapa:
                Intent homeIntent=new Intent(this, LeerLogActivity.class);
                startActivity(homeIntent);
                return true;

           /* case R.id.action_log:
              //  Log.d(TAG,"hice click en"+item.getItemId());
                   subirImagenes();
                return true;*/
         /*   case R.id.cerrarsesion: //realmente es borrar datos

              // Intent pruebai =new Intent(this, MasPruebasActivity.class);
             //   startActivity(pruebai);


               /* navController = Navigation.findNavController(this, R.id.nav_host_fragment);

                navController.navigate(R.id.nav_borrarind);*/

              /*  return true;*/

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void LoadNavMenu(int iMenu){
       // navigationView.getMenu().clear();
        //navigationView.inflateMenu(iMenu);
       }

    public void borrarUsuario(){
        SharedPreferences prefe=getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=prefe.edit();

        editor.putString("usuario", "");
        editor.putString("password", "");
        editor.commit();

    }

    public void cerrarSesion(){

        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
        finish();
       // finishAndRemoveTask();
    }
    private boolean checkPermission() {
        // Permission is not granted
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                  //  Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    public static Boolean isOnlineNet(Context context) {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;

    }
   //para el envio forzoso pero ya es automatico y es por informe
    /*
    public void subirImagenes(){
        subirPendientes();
        //busco las imagenes pendientes de subir
        ImagenDetRepositoryImpl imagenRepo=new ImagenDetRepositoryImpl(this);
        LiveData<List<ImagenDetalle>> imagenesPend=imagenRepo.getImagenDetallePendientesSync();
        imagenesPend.observe(this, new Observer<List<ImagenDetalle>>() {
            @Override
            public void onChanged(List<ImagenDetalle> imagenDetalles) {
                if(imagenDetalles!=null&&imagenDetalles.size()>0){
                    for(ImagenDetalle imagen:imagenDetalles){
                        Log.d(TAG," subiendo a"+imagen.getDescripcion());
                        //subo cada una
                        Intent msgIntent = new Intent(NavigationDrawerActivity.this, SubirFotoService.class);
                        msgIntent.putExtra(SubirFotoService.EXTRA_IMAGE_ID, imagen.getId());
                        msgIntent.putExtra(SubirFotoService.EXTRA_IMG_PATH,imagen.getRuta());
                        msgIntent.putExtra(SubirFotoService.EXTRA_INDICE,imagen.getIndice());

                        msgIntent.setAction(SubirFotoService.ACTION_UPLOAD_IMG);
                        startService(msgIntent);
                        //cambio su estatus a subiendo
                        imagen.setEstatusSync(1);
                        imagenesPend.removeObserver(this);
                      //  imagenRepo.insert(imagen);

                    }

                }
            }
        });


    }*/

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(rcv);
    }
    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(rcv);
        Log.i(TAG," detuve");
    }
//esta funcion no funciona nunca se llama
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.e(TAG," hola");
        return false;
    }

   /* @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG,"click en el menu");
        switch (item.getItemId()) {

            case R.id.nav_cerrarsesion: {
                //borrarUsuario();
                cerrarSesion();
                break;
            }
        }
        if(Constantes.ETAPAACTUAL==4)
            this.onNavigationMenuEmp(item);
        //close navigation drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }*/




    public class SubirFotoProgressReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(SubirFotoService.ACTION_UPLOAD_IMG)) {
                int prog = intent.getIntExtra("uploadComplete", 0);

            }

        }
    }





    /*private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (archact == id) {

                 }
        }
    };*/

    private void contarCorrecc(){
       // totCorrecciones=scViewModel.getTotalSols(Constantes.ETAPAACTUAL,Constantes.INDICEACTUAL,1);
       /* if(Constantes.ETAPAACTUAL==4||Constantes.ETAPAACTUAL==3||Constantes.ETAPAACTUAL==5||Constantes.ETAPAACTUAL==6){
            //busco x ciudad
            totCorrecciones=scViewModel.getTotalSols(Constantes.ETAPAACTUAL,Constantes.INDICEACTUAL,1);

        }else
            totCorrecciones=scViewModel.getTotalSols(Constantes.ETAPAACTUAL,Constantes.INDICEACTUAL,1);*/
        /***mod marzo-24 ya no importa la etapa trae todas las solicitudes****/
        totCorrecciones=scViewModel.getTotalSols(Constantes.INDICEACTUAL,1);

        //la solicitudes de coreecciones ya no serán por cd sino x indice y etapa
        Log.d(TAG,"contarCorrecc"+totCorrecciones+"--"+Constantes.ETAPAACTUAL+","+Constantes.INDICEACTUAL);

    }

    private void contarCanceladas(){

        totCancel=scViewModel.getTotalCancell(Constantes.INDICEACTUAL);

        List<ListaCompra> listacomp = scViewModel.cargarClientesSimplxet(Constantes.CIUDADTRABAJO, 3);
        if(listacomp!=null&&listacomp.size()>0)
         setEtiquetadoCancel(3,6);
        else {
            //veo si ya puedo hacer empaque
            listacomp = scViewModel.cargarClientesSimplxet(Constantes.CIUDADTRABAJO, 4);
            InformeEtapa nvoinf = new InformeEtapa();
            List<InformeEtapa> listageneral = new ArrayList<>();
            if (listacomp != null && listacomp.size() > 0 && listacomp.get(0).getLis_reactivado() != null && listacomp.get(0).getLis_reactivado() == 1) {
                //veo que no haya hecho informe para no esperar a la supervisión
                ContInfEtaViewModel conViewModel = new ViewModelProvider(this).get(ContInfEtaViewModel.class);
                InformeEtapa informesEtapa = conViewModel.getInformeNoCancel(Constantes.INDICEACTUAL, 4);
                if (informesEtapa != null) {
                    nvoinf.setIndice(listacomp.get(0).getIndice());
                    // nvoinf.set = listacomp.get(0).getId();
                    nvoinf.setEstatus(listacomp.get(0).getEstatus());
                    nvoinf.setEtapa(4);

                    nvoinf.setCiudadNombre(listacomp.get(0).getCiudadNombre());
                    nvoinf.setClienteNombre(listacomp.get(0).getClienteNombre());

                    // nvoinf.mo
                    listageneral.add(nvoinf);
                }
            }
            if (listageneral.size() > 0)
                totCancel.setValue(listageneral.size());
        }

    }
    private void contarMuestraAdic(){

        // lista de compra pendiente
        List<ListaCompra> listacomp = scViewModel.cargarClientesSimplxetReac(Constantes.CIUDADTRABAJO, 2,2);
        if(listacomp.size()>0){

            int informesdetList=0;
            InformeCompraDao.InformeCompravisita informetemp=new InformeCompraDao.InformeCompravisita();
            InformeCompraDetalle detalleTemp=new InformeCompraDetalle();
            //busco el detalle
            for (ListaCompra compra:listacomp
            ) {

                List<ListaCompraDetalle> compraDetalles = scViewModel.getProductosPend(compra.getId());
                if (compraDetalles != null && compraDetalles.size() > 0) {
                    for (ListaCompraDetalle detalle : compraDetalles
                    ) {


                        informesdetList++;
                    }


                }
            }
            totMuestraAdic.setValue(informesdetList);

        }else {
            listacomp = scViewModel.cargarClientesSimplxet(Constantes.CIUDADTRABAJO, 3);
            if (listacomp != null && listacomp.size() > 0){
                //busco etiquetado
                List<InformeEtapa> informes = scViewModel.getEtiquetadoAdicional(Constantes.INDICEACTUAL);
                int informesfinal = 0;//contador para saber cuantos informes hay
                Log.d(TAG, "YA CARGÓ " + informes.size());

                for (InformeEtapa infeta : informes
                ) {
                    //reviso que ya pueda hacer esa etapa
                    //busco los clientes x ciudad
                    listacomp = scViewModel.cargarClientesSimplxet(Constantes.CIUDADTRABAJO, 3);
                    if (listacomp != null && listacomp.size() > 0 && listacomp.get(0) != null && listacomp.get(0).getClientesId() == infeta.getClientesId()) {

                        informesfinal++;
                    }
                }
                totMuestraAdic.setValue(informesfinal);
        }else
            //veo si ya puedo hacer empaque
            listacomp = scViewModel.cargarClientesSimplxet(Constantes.CIUDADTRABAJO, 4);

            int listageneral = 0; //para contar los informes
            if (listacomp != null && listacomp.size() > 0 && listacomp.get(0).getLis_reactivado() != null && listacomp.get(0).getLis_reactivado() == 2) {
                ContInfEtaViewModel conViewModel = new ViewModelProvider(this).get(ContInfEtaViewModel.class);
                InformeEtapa informesEtapa = conViewModel.getInformeNoCancel(Constantes.INDICEACTUAL, 4);
                if (informesEtapa == null) {

                    listageneral++;
                }
            }

            totMuestraAdic.setValue(listageneral);
        }
    }
    private void setEtiquetadoCancel(int etapa, int estatus) {
        List<InformeEtapa> listageneral=new ArrayList<>();
        //para ver si sigue etiquetado y empaque
        List<InformeEtapa> informes=scViewModel.getInfEtapaxEstatusSim(Constantes.INDICEACTUAL,etapa,estatus);

        //paso de informe etapa ainforme compra

        for (InformeEtapa infeta : informes
        ) {

            //reviso si ya estoy en etapa 3
            List<ListaCompra> listacomp = scViewModel.cargarClientesSimplxet(Constantes.CIUDADTRABAJO, 3);
            if (listacomp != null && listacomp.size() > 0 && listacomp.get(0)!=null&&listacomp.get(0).getClientesId() == infeta.getClientesId()) {

                listageneral.add(infeta);


            }

        }
        totCancel.setValue(listageneral.size());

    }
    private void initializeCountDrawer(){
        totCancel=new MutableLiveData<>();
        totMuestraAdic=new MutableLiveData<>();
        contarCorrecc();

        contarCanceladas();
        contarMuestraAdic();

        if(gallery!=null) {
            gallery.setGravity(Gravity.CENTER_VERTICAL);
            gallery.setTypeface(null, Typeface.BOLD);
            gallery.setTextColor(Color.RED);

            //  gallery.setTextSize(15);
            int totalnotif = 0;

        }
        totCorrecciones.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer totcor) {
                totCancel.observe(NavigationDrawerActivity.this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer totcan) {
                        totMuestraAdic.observe(NavigationDrawerActivity.this, new Observer<Integer>() {
                            @Override
                            public void onChanged(Integer totma) {
                                int totalnotif = totcor + totcan + totma;

                                gallery.setText(totalnotif + "");
                            }
                        });

                    }
                });



            }
        });

    }


    @Override
    public void onBackPressed() {
     //   Log.e(TAG,"aprete atras");
        try {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            if (fragment instanceof AbririnformeFragment){
          //     Log.e(TAG,"aprete atras***");
                ((AbririnformeFragment)fragment).saliendoSinguardar();
            }else
                 super.onBackPressed();

        }catch(Exception ex){
            super.onBackPressed();
        }


    }
    private void definirTrabajo() {
        SharedPreferences prefe = getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        Constantes.CIUDADTRABAJO = prefe.getString("ciudadtrabajo", "");
        Constantes.IDCIUDADTRABAJO=prefe.getInt("idciudadtrabajo",0);
        Constantes.CLAVEUSUARIO = prefe.getString("claveusuario", "");
        Constantes.INDICEACTUAL=prefe.getString("indiceact", "");
        Constantes.TIPOTIENDA=new HashMap<>();
        Constantes.TIPOTIENDA.put(0,getString(R.string.seleccione_opcion));
        Constantes.TIPOTIENDA.put(1,getString(R.string.grande));
        Constantes.TIPOTIENDA.put(2,getString(R.string.mediana));
        Constantes.TIPOTIENDA.put(3,getString(R.string.chica));
        Constantes.TIPOTIENDA.put(4,getString(R.string.otras));

        //  prefe.getString("ciudadtrabajo","");
    /*    Constantes.PAISTRABAJO=     prefe.getString("paistrabajo","");*/

    }

    private void buscarEtapa() {
        SharedPreferences prefe = getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
           Constantes.ETAPAACTUAL = prefe.getInt("etapaactual", 0); //no es la misma que estapaact que viene del servidor

    }
    public void guardarEtapaPref(int etapa){
        SharedPreferences prefe=getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=prefe.edit();

        editor.putInt("etapaactual",etapa );

        editor.commit();

    }

    public void guardarCiudadPref(String cd){
        SharedPreferences prefe=getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=prefe.edit();

        editor.putString("ciudadtrabajo",cd );
        editor.putInt("idciudadtrabajo",Constantes.IDCIUDADTRABAJO );//no es del catalogo ciudades es de plantas


        editor.commit();

    }
    public void pedirCorrecciones(int actualiza, int etapa) {
        tvRepo = new TablaVersionesRepImpl(this);
        solRepo = new SolicitudCorRepoImpl(this);
        PeticionesServidor ps = new PeticionesServidor(Constantes.CLAVEUSUARIO);
        TablaVersiones comp = tvRepo.getVersionByNombreTablasmd(Contrato.TBLSOLCORRECCIONES, Constantes.INDICEACTUAL);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String version;
        if (comp != null && comp.getVersion() != null) {
            version = sdf.format(comp.getVersion());
        } else //es la 1a vez
        {
            version = "1999-09-09"; //una fecha muy antigua
        }
        if (actualiza == 1) {
            version = "1999-09-09"; //una fecha muy antigua
        }
        //siempre actualizo
        if (NavigationDrawerActivity.isOnlineNet(this))
            ps.pedirSolicitudesCorr(Constantes.INDICEACTUAL, etapa, version, new ActualListener());
        else
            notificar = true;
                  /*  act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("DescargasIniAsyncTask","estas al dia*");

                            proglist.cerrarAlerta();
                            proglist.todoBien();
                        }
                    });*/

    }
    public void reInflateMenu(){
      //  NavigationView navigationView = findViewById(R.id.nav_view);

      //  navigationView.getMenu().clear();

     //   navigationView.inflateMenu(R.menu.activity_main_drawerprep);
        initializeCountDrawer();
    }
    ///se usaba para etiquetado actualiza visitas e informes ahora quedó en el home
   /* public void pedirInformes(int actualiza) {
        tvRepo=new TablaVersionesRepImpl(this);
        visRepo=new VisitaRepositoryImpl(this);
        infdrepo=new InformeComDetRepositoryImpl(this);
         infrepo=new InformeCompraRepositoryImpl(this);
        PeticionesServidor ps=new PeticionesServidor(Constantes.CLAVEUSUARIO);
         TablaVersiones comp = tvRepo.getVersionByNombreTablasmd(Contrato.TBLINFORMESCOMP, Constantes.INDICEACTUAL);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String version;
        if (comp != null && comp.getVersion() != null) {
            version = sdf.format(comp.getVersion());
            //

        } else //es la 1a vez
        {
            version = "1999-09-09"; //una fecha muy antigua


        }
        if (actualiza == 1) {
            version = "1999-09-09"; //una fecha muy antigua
        }
        //siempre actualizo
        if (NavigationDrawerActivity.isOnlineNet())
            ps.pedirInformes(Constantes.INDICEACTUAL,version,new ActualListener());

        else
            notificar = true;


    }*/
    public class ActualListener {

        public int actualizarCorre(SolCorreResponse corrResp, int etapa) {

            //primero los inserts
            if (corrResp != null) {

                if (corrResp != null && corrResp.getInserts() != null) {
                    for (SolicitudCor sol : corrResp.getInserts()) {
                        //veo si ya existe
                       // Log.d(TAG,"solcorreccion"+sol.getId()+"--"+ sol.getNumFoto());
                        SolicitudCor solt = solRepo.findsimple(sol.getId(), sol.getNumFoto());
                        if (solt != null) {
                            if (solt.getEstatus() !=4||solt.getEstatus()!=5) {
                                //actualizo
                                solRepo.actualizarEst(sol.getMotivo(), sol.getContador(), sol.getCreatedAt(), sol.getEstatus(), sol.getId(), sol.getNumFoto());
                            } else if (sol.getContador() > 1)
                                solRepo.actualizarEst(sol.getMotivo(), sol.getContador(), sol.getCreatedAt(), sol.getEstatus(), sol.getId(), sol.getNumFoto());
                            else
                                solRepo.actualizar(sol.getMotivo(), sol.getContador(), sol.getCreatedAt(), sol.getId(), sol.getNumFoto());

                        } else
                            solRepo.insert(sol);

                    }


                }

                //los updates
                if (corrResp != null && corrResp.getUpdates() != null) {

                    if (corrResp.getUpdates() != null)
                        solRepo.insertAll(corrResp.getUpdates()); //inserto blblbl
                }

                //actualizar version en tabla
                TablaVersiones tinfo = new TablaVersiones();
                tinfo.setNombreTabla(Contrato.TBLSOLCORRECCIONES);
                Date fecha1 = new Date();

                tinfo.setVersion(fecha1);
                tinfo.setIndice(Constantes.INDICEACTUAL);
                tinfo.setTipo("I");

                tvRepo.insertUpdate(tinfo);
          //  Log.d(TAG,"dddddd"+corrResp.getCanceladas().size());
                //veo las muestras canceladas
                if (corrResp.getCanceladas() != null)
                    if (etapa == 2)//solo para compra
                        for (MuestraCancelada cancel :
                                corrResp.getCanceladas()) {
                            //busco el informedetalle y actualizo el estatus
                            scViewModel.procesarCanceladas(cancel);

                        }
                    else
                        for (MuestraCancelada cancel :
                                corrResp.getCanceladas()) {
                           // Log.d(TAG,"dddddd"+cancel.getInf_id());
                            //busco el informedetalle y actualizo el estatus
                            scViewModel.procesarCanceladasEta(cancel); //canceladas será 0

                        }

            }
            return 1;

        }

        public void actualizarInformes(RespInformesResponse infoResp) {
           // Log.d(TAG, "actualizando bd informes");
            //primero los inserts
            if (infoResp != null) {

                if (infoResp.getVisita() != null) {
                    //reviso cada uno y las inserto
                    for (Visita vis : infoResp.getVisita()) {
                        visRepo.insert(vis); //inserto blblbl
                    }
                }
                if (infoResp.getInformeCompra() != null && infoResp.getInformeCompra().size() > 0) {
                    //como puede que ya existan reviso primero e inserto unoxuno
                    infrepo.insertAll(infoResp.getInformeCompra());
                }
                if (infoResp.getInformeCompraDetalles() != null && infoResp.getInformeCompraDetalles().size() > 0) {
                    //como puede que ya existan reviso primero e inserto unoxuno
                    infdrepo.insertAll(infoResp.getInformeCompraDetalles());
                }

            }
            //actualizar version en tabla
            TablaVersiones tinfo = new TablaVersiones();
            tinfo.setNombreTabla(Contrato.TBLINFORMESCOMP);
            Date fecha1 = new Date();
            Log.d("DescargasAsyncTask", "insertando fecha version 1" + fecha1);

            tinfo.setVersion(fecha1);
            tinfo.setIndice(Constantes.INDICEACTUAL);
            tinfo.setTipo("I");

            tvRepo.insertUpdate(tinfo);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG," mori");
        // unregisterReceiver(onDownloadComplete);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {



        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    //guardo la etapa seleccionada
        guardarEtapaPref(Constantes.ETAPAACTUAL);


    }
  /*  public boolean onNavigationMenuEmp(MenuItem item) {
        // Handle navigation view item clicks here.
        Log.d(TAG,"SI DI CLICK");
        int id = item.getItemId();
        navigationView.getMenu().setGroupVisible(R.id.HelpGroup,false);
       // navigationView.getMenu().setGroupVisible(R.id.SetupGroup,false);
        switch (item.getItemId()){
            case R.id.nav_etiqmen:
                Log.d(TAG,"SI DI CLICK");
                navigationView.getMenu().setGroupVisible(R.id.HelpGroup,true);
             //   navigationView.getMenu().setGroupVisible(R.id.SetupGroup,false);
                return true;
            /*case R.id.SetupGr:
                navigationView.getMenu().setGroupVisible(R.id.SetupGroup,true);
               // navigationView.getMenu().setGroupVisible(R.id.HelpGroup,false);
                return true;*/

     //   }
      //  base.closeDrawer(GravityCompat.START);
      /*  return true;
    }*/

    public void subirImagenes(){
        NavController navController;
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        navController.navigate(R.id.nav_borrarind);
    }
}