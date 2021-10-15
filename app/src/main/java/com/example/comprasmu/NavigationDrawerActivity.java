package com.example.comprasmu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.services.SubirFotoService;
import com.example.comprasmu.ui.informe.BuscarInformeFragment;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
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
public class NavigationDrawerActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    SubirFotoProgressReceiver rcv;
    String TAG="NavigationDrawerActivity";
    private ListaDetalleViewModel mViewModel;
    SimpleDateFormat sdfparaindice=new SimpleDateFormat("MM-yyyy");
    SimpleDateFormat sdfparaindice2=new SimpleDateFormat("MMM yyyy");

    public static final String PROGRESS_UPDATE = "progress_update";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_darawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        inicializarVarsGlobales();
      //  FloatingActionButton fab = findViewById(R.id.fab);
        //busco el mes actual y le agrego 1



        mViewModel=new ViewModelProvider(this).get(ListaDetalleViewModel.class);

        definirTrabajo();
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
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.mobile_navigation)
                .setDrawerLayout(drawer)
                .build();
        Bundle extras = getIntent().getExtras(); // Aquí es null
        String inicio="";
        if(extras!=null) {
             inicio = extras.getString("comprasmu.inicio");
        }
        NavController navController;

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        IntentFilter filter = new IntentFilter();
        filter.addAction(SubirFotoService.ACTION_UPLOAD_IMG);

        rcv = new SubirFotoProgressReceiver();
        registerReceiver(rcv, filter);
      /*  if(inicio.equals("listainforme")){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = new BuscarInformeFragment();
            ft.add(R.id., fragment);
            ft.commit();
        }*/
    }
    public void inicializarVarsGlobales(){



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
                   subirImagenes();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
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

        //busco las imagenes pendientes de subir
        ImagenDetRepositoryImpl imagenRepo=new ImagenDetRepositoryImpl(this);
        LiveData<List<ImagenDetalle>> imagenesPend=imagenRepo.getImagenDetallePendientesSync();
        imagenesPend.observe(this, new Observer<List<ImagenDetalle>>() {
            @Override
            public void onChanged(List<ImagenDetalle> imagenDetalles) {
                if(imagenDetalles!=null&&imagenDetalles.size()>0){
                    for(ImagenDetalle imagen:imagenDetalles){
                        //subo cada una
                        Intent msgIntent = new Intent(NavigationDrawerActivity.this, SubirFotoService.class);
                        msgIntent.putExtra(SubirFotoService.EXTRA_IMAGE_ID, imagen.getId());
                        msgIntent.putExtra(SubirFotoService.EXTRA_IMG_PATH,imagen.getRuta());

                        startService(msgIntent);
                        //cambio su estatus a subiendo
                        imagen.setEstatusSync(1);
                        imagenRepo.insert(imagen);
                    }

                }
            }
        });


    }
    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(rcv);
    }
    public class SubirFotoProgressReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(SubirFotoService.ACTION_UPLOAD_IMG)) {
                int prog = intent.getIntExtra("uploadComplete", 0);

            }

        }
    }

    private void definirTrabajo(){
        SharedPreferences prefe=getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        Constantes.CIUDADTRABAJO=prefe.getString("ciudadtrabajo","");
      //  prefe.getString("ciudadtrabajo","");
    /*    Constantes.PAISTRABAJO=     prefe.getString("paistrabajo","");
        Constantes.IDCIUDADTRABAJO=prefe.getInt("idciudadtrabajo",0);
        Constantes.IDPAISTRABAJO=     prefe.getInt("idpaistrabajo",0);
        Constantes.CLAVEUSUARIO=prefe.getString("claveusuario","");
*/
        //obtengo solo mes
        String mesactual=sdfparaindice.format(new Date());

        Log.d(TAG, "***** hoy "+mesactual);
        String aux[]=mesactual.split("-");
        int mes=Integer.parseInt(aux[0]);
        int anio=Integer.parseInt(aux[1]);

        Constantes.listaindices=new String[4];
        int j=3;
        int nuevomes=mes;
        for (int i = 1; i < 5; i++) {
            Constantes.listaindices[j] =ComprasUtils.mesaLetra(nuevomes+"") +" "+anio+"";

            nuevomes=nuevomes-1;
            if(nuevomes==0) //empezo en 1
            {
                nuevomes = 12;
                anio = anio-1;
            }
            j--;
        }

       // Constantes.INDICEACTUAL=ComprasUtils.indiceLetra(mesactual);
        Constantes.INDICEACTUAL="junio 2021";
        //TODO falta pais trabajo
      //  Constantes.CIUDADTRABAJO="Cd Juarez";
        Constantes.PAISTRABAJO=     "Mexico";
        //Constantes.IDCIUDADTRABAJO=1;
        Constantes.IDPAISTRABAJO=   1;
        Constantes.CLAVEUSUARIO="marisol";
        //inicio catalogo clientes y plantas
        if( Constantes.CIUDADTRABAJO.equals("")){
            //cargo todos o le digo que la configure?


        }
        mViewModel.cargarClientes( Constantes.CIUDADTRABAJO).observe(this, data -> {
            Log.d(TAG,"regresó de la consulta "+ data.size());
            Constantes.clientesAsignados=ComprasUtils.convertirListaaClientes(data);


        });
        mViewModel.cargarPestañas(Constantes.CIUDADTRABAJO).observe(this, data -> {
            Log.d(TAG,"regresó de la consulta "+ data.size());
            Constantes.plantasAsignadas=ComprasUtils.convertirListaaPlantas(data);


        });

    }


}