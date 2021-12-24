package com.example.comprasmu;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.Window;
import android.widget.Toast;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.dao.ListaCompraDao;
import com.example.comprasmu.data.modelos.Atributo;
import com.example.comprasmu.data.modelos.Contrato;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.TablaVersiones;
import com.example.comprasmu.data.repositories.AtributoRepositoryImpl;
import com.example.comprasmu.data.repositories.CatalogoDetalleRepositoryImpl;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;
import com.example.comprasmu.services.ServicioCompras;
import com.example.comprasmu.services.SubirFotoService;
import com.example.comprasmu.services.SubirPendService;
import com.example.comprasmu.ui.home.HomeFragment;
import com.example.comprasmu.ui.informe.BuscarInformeFragment;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.ui.visita.AbririnformeFragment;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/*esta es la clase principal***/
public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    SubirFotoProgressReceiver rcv;
    String TAG="NavigationDrawerActivity";
    private ListaDetalleViewModel mViewModel;
    SimpleDateFormat sdfparaindice=new SimpleDateFormat("MM-yyyy");
    SimpleDateFormat sdfparaindice2=new SimpleDateFormat("MMM yyyy");
    private static final int PERMISSION_REQUEST_CODE = 200;

    public static final String PROGRESS_UPDATE = "progress_update";
    public static final String PROGRESS_PEND = "progress_pend";
    public static final String NAVINICIAL="nd_navinicial";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        definirTrabajo();
        setContentView(R.layout.activity_navigation_darawer);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

      //  FloatingActionButton fab = findViewById(R.id.fab);
        //busco el mes actual y le agrego 1
         mViewModel=new ViewModelProvider(this).get(ListaDetalleViewModel.class);


     /*   fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ir a nuevo informe
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.mobile_navigation)
                .setDrawerLayout(drawer)
                .build();
        Bundle extras = getIntent().getExtras(); // Aquí es null
        String inicio="";
        if(extras!=null) {
             inicio = extras.getString(NAVINICIAL);
        }
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



        }else   if(inicio.equals("listainforme")){
            /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = new BuscarInformeFragment();
            ft.add(R.id., fragment);
            ft.commit();*/
            graph.setStartDestination(R.id.nav_listar);
        }else{
            descargasIniciales();
            graph.setStartDestination(R.id.nav_home);
        }
        navController.setGraph(graph);

        if (checkPermission()) {
            //main logic or main code

            // . write your main code to execute, It will execute if the permission is already given.

        } else {
            requestPermission();
        }
        ServicioCompras sbt = new ServicioCompras();


        try {
            sbt.iniciar(this.getApplicationContext(), "marisol");
        } catch (Exception e) {
            Log.d(TAG,"Error al iniciar el servicio");
            e.printStackTrace();
        }
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
          /*  case R.id.action_save:
                guardarInforme();
                return true;
*/
            case R.id.action_enviar_fotos:
              //  Log.d(TAG,"hice click en"+item.getItemId());
                   subirImagenes();
                return true;
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
        Constantes.CLAVEUSUARIO = prefe.getString("claveusuario", "");
        //  prefe.getString("ciudadtrabajo","");
    /*    Constantes.PAISTRABAJO=     prefe.getString("paistrabajo","");
        Constantes.IDCIUDADTRABAJO=prefe.getInt("idciudadtrabajo",0);
        Constantes.IDPAISTRABAJO=     prefe.getInt("idpaistrabajo",0);
        Constantes.CLAVEUSUARIO=prefe.getString("claveusuario","");
*/
        //obtengo solo mes
        String mesactual = sdfparaindice.format(new Date());

        Log.d(TAG, "***** hoy " + mesactual);
        String aux[] = mesactual.split("-");
        int mes = Integer.parseInt(aux[0]);
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

        // Constantes.INDICEACTUAL=ComprasUtils.indiceLetra(mesactual);
        Constantes.INDICEACTUAL = "12.2021";
        //TODO falta pais trabajo
        //  Constantes.CIUDADTRABAJO="Cd Juarez";
        Constantes.PAISTRABAJO = "Mexico";
        //Constantes.IDCIUDADTRABAJO=1;
        Constantes.IDPAISTRABAJO = 1;
        //  Constantes.CLAVEUSUARIO="marisol";
        //inicio catalogo clientes y plantas
    }


    public void descargasIniciales(){
        //todo hacerlo en un servicio que esté constantemente verficando la conexion hasta que
        //pueda descargar
        //Inicio un servicio que se encargue de descargar
        CatalogoDetalleRepositoryImpl cdrepo=new CatalogoDetalleRepositoryImpl(getApplicationContext());
        TablaVersionesRepImpl tvRepo=new TablaVersionesRepImpl(getApplicationContext());

        AtributoRepositoryImpl atRepo=new AtributoRepositoryImpl(getApplicationContext());
        ListaCompraDao dao= ComprasDataBase.getInstance(getApplicationContext()).getListaCompraDao();
        ListaCompraDetRepositoryImpl lcdrepo=new ListaCompraDetRepositoryImpl(getApplicationContext());
        ListaCompraRepositoryImpl lcrepo=ListaCompraRepositoryImpl.getInstance(dao);

        DescargasIniAsyncTask task = new DescargasIniAsyncTask(this,cdrepo,tvRepo,atRepo,lcdrepo,lcrepo,null);

        task.execute("cat","");
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








}