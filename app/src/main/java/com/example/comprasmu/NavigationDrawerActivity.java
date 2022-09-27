
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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import android.view.Gravity;
import android.view.MenuItem;

import android.view.Menu;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comprasmu.data.ComprasDataBase;

import com.example.comprasmu.data.dao.ListaCompraDao;

import com.example.comprasmu.data.modelos.ImagenDetalle;

import com.example.comprasmu.data.repositories.AtributoRepositoryImpl;
import com.example.comprasmu.data.repositories.CatalogoDetalleRepositoryImpl;
import com.example.comprasmu.data.repositories.GeocercaRepositoryImpl;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.SolicitudCorRepoImpl;
import com.example.comprasmu.data.repositories.SustitucionRepositoryImpl;
import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;

import com.example.comprasmu.services.SubirFotoService;
import com.example.comprasmu.services.SubirPendService;

import com.example.comprasmu.ui.gallery.PruebaFotosFragment;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;

import com.example.comprasmu.ui.mantenimiento.BorrarDatosFragment;
import com.example.comprasmu.ui.mantenimiento.DescRespaldoFragment;
import com.example.comprasmu.ui.mantenimiento.LeerLogActivity;
import com.example.comprasmu.ui.solcorreccion.ListaSolsViewModel;
import com.example.comprasmu.ui.tiendas.FirstMapActivity;

import com.example.comprasmu.ui.tiendas.MapaCdActivity;
import com.example.comprasmu.ui.tiendas.MapaCdFragment;
import com.example.comprasmu.ui.visita.AbririnformeFragment;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.workmanager.SyncWork;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;


/*esta es la clase principal***/
public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String ETAPA = "comprasmu.ndetapa";
    private AppBarConfiguration mAppBarConfiguration;
    SubirFotoProgressReceiver rcv;
    String TAG="NavigationDrawerActivity";
    private ListaDetalleViewModel mViewModel;
    SimpleDateFormat sdfparaindice=new SimpleDateFormat("M-yyyy");

    private static final int PERMISSION_REQUEST_CODE = 200;
    private ListaSolsViewModel scViewModel;
    public static final String PROGRESS_UPDATE = "progress_update";
    public static final String PROGRESS_PEND = "progress_pend";
    public static final String NAVINICIAL="nd_navinicial";
    TextView slideshow,gallery;
    int totCorrecciones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation_darawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        definirTrabajo();
      //  FloatingActionButton fab = findViewById(R.id.fab);
        //busco el mes actual y le agrego 1
         mViewModel=new ViewModelProvider(this).get(ListaDetalleViewModel.class);
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
        String inicio="";

        if(extras!=null) {
             inicio = extras.getString(NAVINICIAL);
             if(Constantes.ETAPAACTUAL==0)
                 Constantes.ETAPAACTUAL=extras.getInt(ETAPA);
        }

        Log.d(TAG,"pso x aqui"+Constantes.ETAPAACTUAL);
        NavigationView navigationView = findViewById(R.id.nav_view);
        if(Constantes.ETAPAACTUAL==1) {
            navigationView.getMenu().clear();

            navigationView.inflateMenu(R.menu.activity_main_drawerprep);
            View header=navigationView.getHeaderView(0);
            header.setBackgroundResource(R.drawable.side_nav_barpre);
            TextView mNameTextView = (TextView) header.findViewById(R.id.txthmmodulo);
            mNameTextView.setText(R.string.preparacion);
        } if(Constantes.ETAPAACTUAL==2) {
            navigationView.getMenu().clear();

            navigationView.inflateMenu(R.menu.activity_main_drawer);
            View header=navigationView.getHeaderView(0);
            header.setBackgroundResource(R.drawable.side_nav_bar);
            TextView mNameTextView = (TextView) header.findViewById(R.id.txthmmodulo);
            mNameTextView.setText(R.string.compra);
        }
        if(Constantes.ETAPAACTUAL==3) {
            navigationView.getMenu().clear();

            navigationView.inflateMenu(R.menu.activity_main_draweretiq);
            View header=navigationView.getHeaderView(0);
            header.setBackgroundResource(R.drawable.side_nav_bareti);
            TextView mNameTextView = (TextView) header.findViewById(R.id.txthmmodulo);
            mNameTextView.setText(R.string.etiquetado);
        }
        if(Constantes.ETAPAACTUAL==4) {
            navigationView.getMenu().clear();

            navigationView.inflateMenu(R.menu.activity_main_draweremp);
            View header=navigationView.getHeaderView(0);
            header.setBackgroundResource(R.drawable.side_nav_baremp);
            TextView mNameTextView = (TextView) header.findViewById(R.id.txthmmodulo);
            mNameTextView.setText(R.string.empaque);
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
        ComprasLog flog=ComprasLog.getSingleton();
        flog.crearLog(this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getPath());
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
        Log.e(TAG,"wwww"+inicio);
        if( Constantes.CLAVEUSUARIO.equals("")){
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



        }else   if(inicio!=null&&inicio.equals("listainforme")){
            /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = new BuscarInformeFragment();
            ft.add(R.id., fragment);
            ft.commit();*/
            graph.setStartDestination(R.id.nav_listar);
        } if(inicio!=null&&inicio.equals("continuarinf")){
            /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = new BuscarInformeFragment();
            ft.add(R.id., fragment);
            ft.commit();*/
            graph.setStartDestination(R.id.nav_listarvisitas);
        }else{
            descargasIniciales();
            graph.setStartDestination(R.id.nav_home);
        }
        navController.setGraph(graph);
        if(Constantes.ETAPAACTUAL==1)
            gallery=(TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                    findItem(R.id.nav_solcor2));
        if(Constantes.ETAPAACTUAL==2)
        gallery=(TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_solcor2));
        if(Constantes.ETAPAACTUAL==3)
            gallery=(TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                    findItem(R.id.nav_solcor2));
        
            gallery=(TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                    findItem(R.id.nav_solcor2));

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
        PeriodicWorkRequest simpleRequest =
                new PeriodicWorkRequest.Builder(SyncWork.class, 5, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .addTag("comprassync_worker")
                        .build();
        WorkManager
                .getInstance(this)
                .enqueueUniquePeriodicWork(
                "comprassync_worker",
                ExistingPeriodicWorkPolicy.KEEP,
                simpleRequest);
        flog.grabarError("archivo creado");
       /* ServicioCompras sbt = new ServicioCompras();


        try {
            sbt.iniciar(this.getApplicationContext(), "marisol");
        } catch (Exception e) {
            Log.d(TAG,"Error al iniciar el servicio");
            e.printStackTrace();
        }*/
    }
    //saber si tiene mas de una ciudad para mostrar seleccionar ciudad
    public void revisarCiudades(){

            mViewModel.getCiudades().observe(this, data -> {
                 Log.d(TAG,"....regresó de la consulta "+ data.size());
                if(data.size()>1)
                Constantes.varciudades=true;
                else if(data.size()>0) {
                    Constantes.CIUDADTRABAJO=data.get(0).getCiudadNombre();
                    Constantes.IDCIUDADTRABAJO=data.get(0).getCiudadesId();
                    Constantes.varciudades = false;
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

        switch (item.getItemId()) {
            case R.id.action_settings:
                //  Log.d(TAG,"hice click en"+item.getItemId());
                pruebadescarga();
              //  startService(DownloadSongService.getDownloadService(this, IMAGE_DOWNLOAD_PATH, DirectoryHelper.ROOT_DIRECTORY_NAME.concat("/")));

                return true;
            case R.id.action_mapa:
                //  Log.d(TAG,"hice click en"+item.getItemId());
              //  Intent homeIntent=new Intent(this, MapaCdActivity.class);
              //  Intent homeIntent=new Intent(this, FirstMapActivity.class);

               // startActivity(homeIntent);
             //   finish();

             //   NavHostFragment.findNavController(AbririnformeFragment.this).navigate(R.id.action_nuevotolista);
           /*     FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                MapaCdFragment fragconfig=new MapaCdFragment();
                ft.add(R.id.nav_host_fragment, fragconfig);

                ft.commit();*/
               /* FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                BorrarDatosFragment fragconfig=new BorrarDatosFragment();
                ft.add(R.id.nav_host_fragment, fragconfig);

                ft.commit();*/
            //    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
              //  PruebaFotosFragment fragconfig=new PruebaFotosFragment();
                //ft.add(R.id.nav_host_fragment, fragconfig);

                //ft.commit();
                Intent homeIntent=new Intent(this, LeerLogActivity.class);

                startActivity(homeIntent);
                finish();
                return true;
          /*  case R.id.action_save:
                guardarInforme();
                return true;
*/
     /*       case R.id.action_enviar_fotos:
              //  Log.d(TAG,"hice click en"+item.getItemId());
                   subirImagenes();
                return true;*/
            case R.id.cerrarsesion:
                //  Log.d(TAG,"hice click en"+item.getItemId());
                borrarUsuario();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
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
    public static Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }
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


    }
    private void subirPendientes(){
        Intent msgIntent = new Intent(NavigationDrawerActivity.this, SubirPendService.class);

        msgIntent.setAction(SubirPendService.ACTION_UPLOAD_PEND);
        startService(msgIntent);
    }
    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(rcv);
    }
    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(rcv);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.i(TAG,"click en el menu");
        switch (item.getItemId()) {

            case R.id.nav_cerrarsesion: {
                //borrarUsuario();
                cerrarSesion();
                break;
            }
        }
        //close navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    public class SubirFotoProgressReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(SubirFotoService.ACTION_UPLOAD_IMG)) {
                int prog = intent.getIntExtra("uploadComplete", 0);

            }

        }
    }

    private void definirTrabajo() {
        SharedPreferences prefe = getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        Constantes.CIUDADTRABAJO = prefe.getString("ciudadtrabajo", "");
        Constantes.IDCIUDADTRABAJO=prefe.getInt("idciudadtrabajo",0);
        Constantes.CLAVEUSUARIO = prefe.getString("claveusuario", "");
        //  prefe.getString("ciudadtrabajo","");
    /*    Constantes.PAISTRABAJO=     prefe.getString("paistrabajo","");
        Constantes.IDCIUDADTRABAJO=prefe.getInt("idciudadtrabajo",0);
        Constantes.IDPAISTRABAJO=     prefe.getInt("idpaistrabajo",0);
        Constantes.CLAVEUSUARIO=prefe.getString("claveusuario","");
*/
        Constantes.TIPOTIENDA=new HashMap<>();

       Constantes.TIPOTIENDA.put(1,getString(R.string.grande));
        Constantes.TIPOTIENDA.put(2,getString(R.string.mediana));
        Constantes.TIPOTIENDA.put(3,getString(R.string.chica));
        Constantes.TIPOTIENDA.put(4,getString(R.string.otras));
        //obtengo solo mes

        Calendar cal = Calendar.getInstance(); // Obtenga un calendario utilizando la zona horaria y la configuración regional predeterminadas
        Date hoy=new Date();
        cal.setTime(hoy);
        cal.add(Calendar.MONTH, +1);
        String mesactual = sdfparaindice.format(cal.getTime());
        Log.d(TAG, "***** hoy " + mesactual);
        String aux[] = mesactual.split("-");
        int mes = Integer.parseInt(aux[0])+1;
        int anio = Integer.parseInt(aux[1]);

        Constantes.listaindices = new String[4];
        int j = 3;
        int nuevomes = mes;
        for (int i = 1; i < 5; i++) {
            Constantes.listaindices[j] = ComprasUtils.mesaLetra(nuevomes + "") + " " + anio + "";

            nuevomes = nuevomes - 1;
            if (nuevomes == 0) //empezo en 1
            {
                nuevomes = 12;
                anio = anio - 1;
            }
            j--;
        }

        Constantes.INDICEACTUAL=ComprasUtils.indiceLetra(mesactual);
       // Constantes.INDICEACTUAL=mesactual.replace('-','.');
        Constantes.INDICEACTUAL = "6.2022";
        if(Constantes.CLAVEUSUARIO.equals("4")){
            Constantes.INDICEACTUAL = "6.2022";
        }

        Log.d(TAG, "***** indice " + Constantes.INDICEACTUAL);

        //TODO falta pais trabajo
        //  Constantes.CIUDADTRABAJO="Cd Juarez";
        //  Constantes.PAISTRABAJO = "Mexico";
        //Constantes.IDCIUDADTRABAJO=1;
        // Constantes.IDPAISTRABAJO = 1;
        //  Constantes.CLAVEUSUARIO="marisol";
        //inicio catalogo clientes y plantas
    }


    public void descargasIniciales(){
        //pueda descargar
        //Inicio un servicio que se encargue de descargar
        CatalogoDetalleRepositoryImpl cdrepo=new CatalogoDetalleRepositoryImpl(getApplicationContext());
        TablaVersionesRepImpl tvRepo=new TablaVersionesRepImpl(getApplicationContext());

        AtributoRepositoryImpl atRepo=new AtributoRepositoryImpl(getApplicationContext());
        ListaCompraDao dao= ComprasDataBase.getInstance(getApplicationContext()).getListaCompraDao();
        ListaCompraDetRepositoryImpl lcdrepo=new ListaCompraDetRepositoryImpl(getApplicationContext());
        ListaCompraRepositoryImpl lcrepo=ListaCompraRepositoryImpl.getInstance(dao);
        SustitucionRepositoryImpl sustRepo=new SustitucionRepositoryImpl(getApplicationContext());
        GeocercaRepositoryImpl georep=new GeocercaRepositoryImpl(getApplicationContext());
        DescargasIniAsyncTask task = new DescargasIniAsyncTask(this,cdrepo,tvRepo,atRepo,lcdrepo,lcrepo,null,sustRepo,georep);

        task.execute("cat","");

        //descarga solicitudes compra
        SolicitudCorRepoImpl solcorRepo=new SolicitudCorRepoImpl(getApplicationContext());

        DescCorrecAsyncTask corTask=new DescCorrecAsyncTask(solcorRepo,tvRepo,this,Constantes.ETAPAACTUAL,Constantes.INDICEACTUAL);
        corTask.execute("");
      /*  AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setIcon(android.R.drawable.stat_sys_download);
        builder.setTitle("Descargando");
        builder.setMessage("Por favor mantengase en la aplicación hasta que termine la descarga");
        builder.setInverseBackgroundForced(true);

        AlertDialog alert=builder.create();
        alert.show();*/

      /*  Dialog builder = new Dialog(act);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.setCancelable(false);
*/

    }
    
    private void contarCorrecc(){
        totCorrecciones=scViewModel.getTotalSols(Constantes.ETAPAACTUAL,Constantes.INDICEACTUAL,1);
    }
    private void initializeCountDrawer(){
        contarCorrecc();
        //Gravity property aligns the text
        gallery.setGravity(Gravity.CENTER_VERTICAL);
        gallery.setTypeface(null, Typeface.BOLD);
        gallery.setTextColor(Color.RED);
      //  gallery.setTextSize(15);
        gallery.setText(totCorrecciones+"");

    }


    @Override
    public void onBackPressed() {
        Log.e(TAG,"aprete atras");
        try {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            if (fragment instanceof AbririnformeFragment){
               Log.e(TAG,"aprete atras***");
                ((AbririnformeFragment)fragment).saliendoSinguardar();
            }else
            super.onBackPressed();

        }catch(Exception ex){
            super.onBackPressed();
        }


    }

    public void pruebadescarga() {
        //pueda descargar
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Definir una transacción
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment fragment = new DescRespaldoFragment();
// Obtener el administrador de fragmentos a través de la actividad

      //  fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
// Cambiar
        fragmentTransaction.commit();

    }



}