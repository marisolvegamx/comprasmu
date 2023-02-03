package com.example.comprasmu.ui.infetapa;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.modelos.Reactivo;
import com.example.comprasmu.data.modelos.SolicitudCor;
import com.example.comprasmu.databinding.ActivityNuevoInfetapaBinding;
import com.example.comprasmu.ui.correccion.NvaCorreccionFragment;
import com.example.comprasmu.ui.correccion.NvaCorreccionPreFragment;
import com.example.comprasmu.ui.empaque.NvoEmpaqueFragment;
import com.example.comprasmu.ui.etiquetado.NvoEtiquetadoFragment;
import com.example.comprasmu.ui.preparacion.NvaPreparacionFragment;
import com.example.comprasmu.ui.preparacion.NvaPreparacionViewModel;
import com.example.comprasmu.utils.Constantes;

import java.text.SimpleDateFormat;


public class NuevoInfEtapaActivity extends AppCompatActivity  {
    Toolbar myChildToolbar;
    private ActivityNuevoInfetapaBinding mBinding;
    public final static String INFORMESEL = "comprasmu.nie_informesel";
    public final static String PLANTASEL = "comprasmu.nie_plantasel";
    public final static String NUMFOTO = "comprasmu.nie_numfoto";
    public final static String CORRECCION = "comprasmu.nie_correc"; //para saber que es correccion
    private static final String TAG = "NvoInfEtapaAct";
    boolean noSalir;
    boolean isEdicion;
    int idinformeSel;
    int numfoto;
    int contfoto;
    private NvaPreparacionViewModel dViewModel;
    private NuevoInfEtapaViewModel infvm;
    private int etapa;
    private int plantaSel;
    private boolean isCor; //para saber si es correccion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_nuevo_infetapa);
        // get fragment manager
        myChildToolbar =
                findViewById(R.id.toolbarinf);
        setSupportActionBar(myChildToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
// add
        infvm =
                new ViewModelProvider(this).get(NuevoInfEtapaViewModel.class);
        dViewModel = new ViewModelProvider(this).get(NvaPreparacionViewModel.class);
        if (savedInstanceState != null) {    // Restore value of members from saved state
            //  idinformeSel = savedInstanceState.getInt("visitasel");
            //  if(idinformeSel>0) { //se salio y lo devuelvo al inicio
            this.finish();
            Intent intento1 = new Intent(this, NavigationDrawerActivity.class);
            intento1.putExtra(NavigationDrawerActivity.NAVINICIAL,"continuarinf");

            startActivity(intento1);
            //mando al inicio
            //  NavHostFragment.findNavController(this).navigate(R.id.action_continuar, bundle);
            //  }
        }
        // if(Constantes.NM_TOTALISTA>=16)
        loadData(savedInstanceState);

        Log.d(TAG,"WWWWWW"+isEdicion+"++"+idinformeSel+"--"+etapa);

        if(isEdicion) //es edicion
        {
            //busco el ultimo detalle
            InformeEtapaDet det = infvm.getDetalleEtEdit(idinformeSel, etapa);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            if (etapa == 1) {
                Bundle bundle=new Bundle();

                bundle.putInt(NuevoInfEtapaActivity.INFORMESEL, idinformeSel);


                if (det != null) {


                //busco la pregunta actual en la decripcion
                char preg = det.getDescripcion().charAt(det.getDescripcion().length() - 1);
                Log.d(TAG, "preg=" + preg);
                NvaPreparacionFragment frag=    new NvaPreparacionFragment(Character.getNumericValue(preg), true, det);
                 frag.setArguments(bundle);
                ft.add(R.id.continfeta_fragment,frag );

                 }else{
                 NvaPreparacionFragment frag=new NvaPreparacionFragment(0,true,det);
                 frag.setArguments(bundle);
                 ft.add(R.id.continfeta_fragment,frag );

                }
            }
            if (etapa == 3) {
                if (det != null) {
                    //busco la pregunta actual en la decripcion
                    //char preg = det.getDescripcion().charAt(det.getDescripcion().length() - 1);
                    //Log.d(TAG, "preg=" + preg);
                    ft.add(R.id.continfeta_fragment, new NvoEtiquetadoFragment(3,true,det,idinformeSel));

                }else
                    //todavía no capturaba detalle
                {  ft.add(R.id.continfeta_fragment, new NvoEtiquetadoFragment(3,true,null,idinformeSel));
                }
                 }
            if (etapa == 4) {
                if(plantaSel>1)//es una nueva caja
                {
                    isEdicion=false;
                    Bundle bundle=new Bundle();

                    bundle.putInt(NuevoInfEtapaActivity.INFORMESEL, idinformeSel);
                    bundle.putInt(NuevoInfEtapaActivity.PLANTASEL,plantaSel);
                    //busco la pregunta actual en la decripcion
                    //char preg = det.getDescripcion().charAt(det.getDescripcion().length() - 1);
                    //Log.d(TAG, "preg=" + preg);
                    NvoEmpaqueFragment empf=new NvoEmpaqueFragment(null,false);
                    empf.setArguments(bundle);
                    ft.add(R.id.continfeta_fragment,empf);
                }else
                if (det != null) {
                    Bundle bundle=new Bundle();

                    bundle.putInt(NuevoInfEtapaActivity.INFORMESEL, idinformeSel);
                    bundle.putInt(NuevoInfEtapaActivity.PLANTASEL,plantaSel);
                    //busco la pregunta actual en la decripcion
                    //char preg = det.getDescripcion().charAt(det.getDescripcion().length() - 1);
                    //Log.d(TAG, "preg=" + preg);
                    NvoEmpaqueFragment empf=new NvoEmpaqueFragment(null,true);
                    empf.setArguments(bundle);
                    ft.add(R.id.continfeta_fragment,empf);

                }
            }
            ft.commit();
        }else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Bundle bundle=new Bundle();
            if(isCor){
                bundle.putInt(NuevoInfEtapaActivity.INFORMESEL, idinformeSel);
                bundle.putInt(NuevoInfEtapaActivity.NUMFOTO, numfoto);
                if(etapa==1) {
                    NvaCorreccionPreFragment frag = new NvaCorreccionPreFragment();
                    frag.setArguments(bundle);
                    ft.add(R.id.continfeta_fragment, frag);
                }else
                {
                    NvaCorreccionFragment frag = new NvaCorreccionFragment();
                    frag.setArguments(bundle);
                    ft.add(R.id.continfeta_fragment, frag);
                }

            }else
            if(etapa==1)
                ft.add(R.id.continfeta_fragment, new NvaPreparacionFragment(1,false,null));
            else if(etapa==3)
                ft.add(R.id.continfeta_fragment, new NvoEtiquetadoFragment());
            else if (etapa == 4) {

                ft.add(R.id.continfeta_fragment, new NvoEmpaqueFragment(null,false));


            }
            ft.commit();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    public void actualizarBarra(InformeEtapa nvoInf) {
        mBinding.setInforme(nvoInf);
        mBinding.row2.setVisibility(View.VISIBLE);

    }
    public void actualizarBarraCor(SolicitudCor sol) {
        //convierto la solicitud en informeEtapa
      InformeEtapa temp=new InformeEtapa();
      temp.setIndice(sol.getIndice());
      temp.setPlantaNombre(sol.getPlantaNombre());
      temp.setClienteNombre(sol.getClienteNombre());
      actualizarBarra(temp);
      actualizarAtributo1(sol.getNombreTienda());
      SimpleDateFormat sdf=Constantes.vistasdf;
      if(sol.getCreatedAt()!=null)
        actualizarAtributo2(sdf.format(sol.getCreatedAt()));
      //actualizarAtributo3(sol.getContador()+"");
  //    actualizarAtributo3(sol.getMotivo());

    }


    public void reiniciarBarra() {

        mBinding.txtnieplanta.setVisibility(View.GONE);

        mBinding.row3.setVisibility(View.GONE);


    }

    public void actualizarAtributo1(String atributo) {
        mBinding.txtnieatr1.setText(atributo);


        mBinding.row3.setVisibility(View.VISIBLE);

    }
    public void actualizarAtributo2(String atributo) {
        mBinding.txtnieatr2.setText(atributo);
        //mBinding.row3.setVisibility(View.VISIBLE);
    }

    public void actualizarAtributo3(String atributo) {
        mBinding.txtnieatr3.setText(atributo);
        mBinding.row4.setVisibility(View.VISIBLE);
    }
    public void actualizarAtributo4(String atributo) {
        mBinding.txtnieatr4.setText(atributo);
        mBinding.row4.setVisibility(View.VISIBLE);
    }



    public void loadData(Bundle savedInstanceState) {
        Bundle datosRecuperados = getIntent().getExtras();

        if(datosRecuperados!=null) {
            idinformeSel = datosRecuperados.getInt(INFORMESEL);
            etapa = datosRecuperados.getInt(ContInfEtapaFragment.ETAPA);
            isCor = datosRecuperados.getBoolean(CORRECCION);
            plantaSel = datosRecuperados.getInt( NuevoInfEtapaActivity.PLANTASEL);
            numfoto = datosRecuperados.getInt(NuevoInfEtapaActivity.NUMFOTO);

            if(!isCor&&idinformeSel>0) {
                isEdicion = true;

            }

        }
        else { //lo recupero
            if (savedInstanceState != null) {    // Restore value of members from saved state
                //  idinformeSel = savedInstanceState.getInt("visitasel");
              //se salio y lo devuelvo al inicio
                Intent intento1 = new Intent(this, NavigationDrawerActivity.class);
                startActivity(intento1);
                //mando al inicio
                //  NavHostFragment.findNavController(this).navigate(R.id.action_continuar, bundle);
                //  }
            }

        }

        mBinding.setSdf(Constantes.sdfsolofecha);


    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    boolean salir;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_continuar, menu);
        return true;
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putInt("visitasel",idinformeSel );

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    public void onBackPressed() {

        if(etapa==3)//el regreso se maneja en el fragment
        {
            NvoEtiquetadoFragment fragment = (NvoEtiquetadoFragment) getSupportFragmentManager().findFragmentById(R.id.continfeta_fragment);
            fragment.atras();
            return;
        }
        if(etapa==4)//el regreso se maneja en el fragment
        {
            Log.d(TAG,"regresando de "+dViewModel.preguntaAct);
            //busco el siguiente
            Reactivo reactivo = dViewModel.buscarReactivoAnterior(dViewModel.preguntaAct);
            if(reactivo!=null)
            //busco el reactivo anterior
            {
                if (dViewModel.preguntaAct > 1) {
                    NvoEmpaqueFragment nvofrag = new NvoEmpaqueFragment(reactivo, true);
                    FragmentManager fragmentManager = getSupportFragmentManager();
// Definir una transacción
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
// Remplazar el contenido principal por el fragmento
                    fragmentTransaction.replace(R.id.continfeta_fragment, nvofrag);
                    // fragmentTransaction.addToBackStack(null);
// Cambiar
                    fragmentTransaction.commit();
                }
                if (dViewModel.preguntaAct == 1 && dViewModel.variasPlantas) {
                    NvoEmpaqueFragment nvofrag = new NvoEmpaqueFragment(reactivo, true);
                    FragmentManager fragmentManager = getSupportFragmentManager();
// Definir una transacción
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
// Remplazar el contenido principal por el fragmento
                    fragmentTransaction.replace(R.id.continfeta_fragment, nvofrag);
                    // fragmentTransaction.addToBackStack(null);
// Cambiar
                    fragmentTransaction.commit();
                }
            }else {
                super.onBackPressed();
                return;
            }
        }
        else if(etapa==1)
        {
            //infvm.cont es la preg actual
            if(infvm.cont==6){
                return; //no puedo regresar de los comentarios porque la ultima preg de fotospuede ser variables
            }
            if(infvm.cont==0) {
                super.onBackPressed();
                return;
            }
            if (infvm.cont > 1) {
                NvaPreparacionFragment nvofrag;
                if(infvm.cont>100){
                    nvofrag= new NvaPreparacionFragment(infvm.cont - 101, true, null);

                }

                else
                    nvofrag=new NvaPreparacionFragment(infvm.cont +100, true, null);

                FragmentManager fragmentManager = getSupportFragmentManager();
// Definir una transacción
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
// Remplazar el contenido principal por el fragmento
                fragmentTransaction.replace(R.id.continfeta_fragment, nvofrag);
                // fragmentTransaction.addToBackStack(null);
// Cambiar
                fragmentTransaction.commit();
            }
          if (infvm.cont == 1 && dViewModel.variasPlantas) {
                NvaPreparacionFragment nvofrag = new NvaPreparacionFragment(infvm.cont - 1, true, null);
                FragmentManager fragmentManager = getSupportFragmentManager();
// Definir una transacción
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
// Remplazar el contenido principal por el fragmento
                fragmentTransaction.replace(R.id.continfeta_fragment, nvofrag);
                // fragmentTransaction.addToBackStack(null);
// Cambiar
                fragmentTransaction.commit();
            }

        }
        else
                super.onBackPressed();

}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        noSalir=false;
        switch (item.getItemId()) {
            case R.id.csalir:
                int numpreg=0;
                //reviso que no sea de las que no debe salir
                if(etapa==3)
                if( dViewModel.preguntaAct==1||dViewModel.preguntaAct==3||dViewModel.preguntaAct==4) {

                        noSalir=true;
                    }


                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
                dialogo1.setTitle(R.string.importante);
                if(!noSalir) {
                    dialogo1.setMessage(R.string.pregunta_salir);


                    dialogo1.setCancelable(false);

                    dialogo1.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            dialogo1.cancel();
                        }
                    });
                    dialogo1.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            // DetalleProductoFragment fragment = (DetalleProductoFragment) getSupportFragmentManager().findFragmentById(R.id.continf_fragment);

                            //hay que guardar la ultima preguta
                            // fragment.guardarResp();
                            finish();


                        }
                    });
                } else
                {   dialogo1.setMessage("Debe guardar antes de salir");

                    dialogo1.setCancelable(false);

                    dialogo1.setNegativeButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            dialogo1.cancel();
                        }
                    });}
                dialogo1.show();


                return true;

            default:
                break;
        }

        return false;
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        mBinding = null;
        dViewModel = null;
        infvm = null;

    }

}

