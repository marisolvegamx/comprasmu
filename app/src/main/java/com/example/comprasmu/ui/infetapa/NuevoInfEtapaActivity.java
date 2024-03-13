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
import com.example.comprasmu.ui.correccion.NvaCorreViewModel;
import com.example.comprasmu.ui.correccion.NvaCorrecCalCajaFragment;
import com.example.comprasmu.ui.correccion.NvaCorreccionEtiqFragment;
import com.example.comprasmu.ui.correccion.NvaCorreccionFragment;
import com.example.comprasmu.ui.correccion.NvaCorreccionPreFragment;
import com.example.comprasmu.ui.correccion.NvaCorreccionEmpFragment;
import com.example.comprasmu.ui.empaque.NvoEmpaqueFragment;
import com.example.comprasmu.ui.envio.NvoEnvioFragment;
import com.example.comprasmu.ui.etiquetado.NvoEtiqCajaFragment;
import com.example.comprasmu.ui.etiquetado.NvoEtiquetadoFragment;
import com.example.comprasmu.ui.preparacion.NvaPreparacionFragment;
import com.example.comprasmu.ui.preparacion.NvaPreparacionViewModel;
import com.example.comprasmu.utils.ComprasUtils;
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
           // intento1.putExtra(NavigationDrawerActivity.NAVINICIAL,"continuarinf");

            startActivity(intento1);
            //mando al inicio
            //  NavHostFragment.findNavController(this).navigate(R.id.action_continuar, bundle);
            //  }
        }
        // if(Constantes.NM_TOTALISTA>=16)
        loadData();

        Log.d(TAG,"WWWWWW"+isEdicion+"++"+idinformeSel+"--"+etapa);

        if(isEdicion) //es edicion
        {
            //busco el ultimo detalle
            InformeEtapaDet det = infvm.getDetalleEtEdit(idinformeSel, etapa);
        //    Log.d(TAG,"ahi esta el det "+det.getInformeEtapaId());
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            if (etapa == 1) {
                Bundle bundle=new Bundle();

                bundle.putInt(NuevoInfEtapaActivity.INFORMESEL, idinformeSel);


                if (det != null) {


                    //busco la pregunta actual en la decripcion
                    char preg = det.getDescripcion().charAt(det.getDescripcion().length() - 1);
                   int x= Character.getNumericValue(preg);
                    bundle.putInt(NvaPreparacionFragment.ARG_PREGACT,x);
                    bundle.putBoolean(NvaPreparacionFragment.ARG_ESEDI,true);
                    bundle.putInt(NvaPreparacionFragment.ARG_INFORMEDET,det.getId() );

                    NvaPreparacionFragment frag=    new NvaPreparacionFragment();
                     frag.setArguments(bundle);
                    ft.add(R.id.continfeta_fragment,frag );

                 }else{
                    bundle.putInt(NvaPreparacionFragment.ARG_PREGACT,0);
                    bundle.putBoolean(NvaPreparacionFragment.ARG_ESEDI,true);
                     //   bundle.putInt(NvaPreparacionFragment.ARG_INFORMEDET,det.getId() );
                     NvaPreparacionFragment frag=new NvaPreparacionFragment();
                     frag.setArguments(bundle);
                     ft.add(R.id.continfeta_fragment,frag );

                }
            }
            if (etapa == 3) {
                mBinding.row1.setVisibility(View.GONE);
                Bundle args = new Bundle();
                args.putInt(NvoEtiquetadoFragment.ARG_PREGACT,3 );
                args.putBoolean(NvoEtiquetadoFragment.ARG_ESEDI,true);

                args.putInt(NvoEtiquetadoFragment.ARG_INFORMESEL,idinformeSel);
                NvoEtiquetadoFragment nvofrag = new NvoEtiquetadoFragment();
                nvofrag.setArguments(args);
                if (det != null) {
                    //busco la pregunta actual en la decripcion
                    //char preg = det.getDescripcion().charAt(det.getDescripcion().length() - 1);
                    //Log.d(TAG, "preg=" + preg);
                    args.putInt(NvoEtiquetadoFragment.ARG_INFORMEDET,det.getId() );

                    ft.add(R.id.continfeta_fragment, nvofrag);

                }else
                    //todavía no capturaba detalle
                {  ft.add(R.id.continfeta_fragment,nvofrag);
                }
            }
            if (etapa == 4) {

                if (det != null) {
                    Bundle bundle=new Bundle();

                    bundle.putInt(NuevoInfEtapaActivity.INFORMESEL, idinformeSel);

                   // bundle.putInt(NvoEmpaqueFragment.ARG_PREGACT,0);
                    bundle.putBoolean(NvoEmpaqueFragment.ARG_ESEDI,true);

                    //busco la pregunta actual en la decripcion
                    //char preg = det.getDescripcion().charAt(det.getDescripcion().length() - 1);
                    //Log.d(TAG, "preg=" + preg);
                    NvoEmpaqueFragment empf=new NvoEmpaqueFragment();
                    empf.setArguments(bundle);
                    ft.add(R.id.continfeta_fragment,empf);

                }
            }
            if (etapa == 5) {
                mBinding.row1.setVisibility(View.GONE);
                Bundle args = new Bundle();
                args.putInt(NvoEnvioFragment.ARG_PREGACT,1 );
                args.putBoolean(NvoEnvioFragment.ARG_ESEDI,true);

                args.putInt(NvoEnvioFragment.ARG_INFORMESEL,idinformeSel);
                NvoEnvioFragment nvofrag = new NvoEnvioFragment();
                nvofrag.setArguments(args);
                ft.add(R.id.continfeta_fragment,nvofrag);

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
                }else  if(etapa==3) {
                    //veo la descripcion de la solicitud para enviar otro fragment
                    NvaCorreViewModel correViewModel=new ViewModelProvider(this).get(NvaCorreViewModel.class);

                    SolicitudCor sol=correViewModel.getSolicitud (idinformeSel,numfoto);
                    if(sol!=null&&sol.getDescripcionId()>11) //es de caja
                    {
                        NvaCorrecCalCajaFragment frag = new NvaCorrecCalCajaFragment();
                        frag.setArguments(bundle);
                        ft.add(R.id.continfeta_fragment, frag);
                    }
                    else {
                        NvaCorreccionEtiqFragment frag = new NvaCorreccionEtiqFragment();
                        frag.setArguments(bundle);
                        ft.add(R.id.continfeta_fragment, frag);
                    }
                }else  if(etapa==4) {
                    NvaCorreccionEmpFragment frag = NvaCorreccionEmpFragment.newInstance();
                    frag.setArguments(bundle);
                    ft.add(R.id.continfeta_fragment, frag);
                }else
                {
                    NvaCorreccionFragment frag = new NvaCorreccionFragment();
                    frag.setArguments(bundle);
                    ft.add(R.id.continfeta_fragment, frag);
                }

            }else
            if(etapa==1) {

                bundle.putInt(NvaPreparacionFragment.ARG_PREGACT,1);
                bundle.putBoolean(NvaPreparacionFragment.ARG_ESEDI,false);


                NvaPreparacionFragment frag=    new NvaPreparacionFragment();
                frag.setArguments(bundle);
                //ft.add(R.id.continfeta_fragment, new NvaPreparacionFragment(1, false, null));
                ft.add(R.id.continfeta_fragment,frag);
            }else if(etapa==3) {
                mBinding.row1.setVisibility(View.GONE);
                ft.add(R.id.continfeta_fragment, new NvoEtiquetadoFragment());

            }
            else if (etapa == 4) {

                ft.add(R.id.continfeta_fragment, new NvoEmpaqueFragment());


            }  else if (etapa == 5) {

                ft.add(R.id.continfeta_fragment, new NvoEnvioFragment());


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
        if(Constantes.ETAPAACTUAL==3)//para etiquetado pongo total de muestra
            mBinding.txtnietotmues.setVisibility(View.VISIBLE);

        mBinding.setInforme(nvoInf);
        mBinding.row1.setVisibility(View.VISIBLE);
        mBinding.row2.setVisibility(View.VISIBLE);

    }

    public void actualizarBarraEtiq(InformeEtapa nvoInf) {
        mBinding.txtnietotmues.setVisibility(View.VISIBLE);

        mBinding.setInforme(nvoInf);
        mBinding.row1.setVisibility(View.GONE);
        mBinding.rowetiq.setVisibility(View.VISIBLE);
        mBinding.row2.setVisibility(View.VISIBLE);

    }
    public void actualizarBarraEnv(InformeEtapa nvoInf) {

        mBinding.rowetiq.setVisibility(View.VISIBLE);
        mBinding.setInforme(nvoInf);
        mBinding.row1.setVisibility(View.GONE);
        mBinding.row4.setVisibility(View.VISIBLE);
        this.actualizarAtributo3(ComprasUtils.indiceLetra(nvoInf.getIndice()));
        this.actualizarAtributo4("TOT. CAJAS "+nvoInf.getTotal_cajas());
        mBinding.row2.setVisibility(View.GONE);

    }


    public void actualizarBarraEmp(InformeEtapa nvoInf,int mues, int cajas) {
        mBinding.setInforme(nvoInf);
        mBinding.row2.setVisibility(View.GONE);
        mBinding.row1.setVisibility(View.GONE);

        this.actualizarAtributo1(nvoInf.getClienteNombre());
       this.actualizarAtributo2(ComprasUtils.indiceLetra(nvoInf.getIndice()));
        this.actualizarAtributo3("TOT. MUESTRAS:"+mues);
        this.actualizarAtributo4("TOT. CAJAS:"+cajas);
    }
    //para acomodar barra de titulos de correcciones compras
    public void actualizarBarraCor(SolicitudCor sol,int constienda) {
        //convierto la solicitud en informeEtapa
      InformeEtapa temp=new InformeEtapa();
      temp.setIndice(sol.getIndice());
      temp.setPlantaNombre(sol.getPlantaNombre());
      temp.setClienteNombre(sol.getClienteNombre());
      temp.setConsecutivo(constienda);
      actualizarBarra(temp);
      actualizarAtributo1(sol.getNombreTienda());
      SimpleDateFormat sdf=Constantes.sdfsolofecha;
      if(sol.getCreatedAt()!=null)
        actualizarAtributo2(sdf.format(sol.getCreatedAt()));
      //actualizarAtributo3(sol.getContador()+"");
  //    actualizarAtributo3(sol.getMotivo());

    }
    //para acomodar barra de titulos de correcciones de otra etapas menos compras
    public void actualizarBarraCorEta(SolicitudCor sol, int numcaja) {
        //convierto la solicitud en informeEtapa
        InformeEtapa temp=new InformeEtapa();
        temp.setIndice(sol.getIndice());
        temp.setPlantaNombre(sol.getPlantaNombre());
        temp.setClienteNombre(sol.getClienteNombre());
      //  temp.setConsecutivo(constienda);
        actualizarBarra(temp);
        //oculto fila 2 y 3
        mBinding.row3.setVisibility(View.GONE);
        mBinding.row2.setVisibility(View.GONE);

        if(sol.getEtapa()==3&&numcaja>0) {
            mBinding.txtnieatr5.setText("CAJA NUM. " + numcaja);
            mBinding.row5.setVisibility(View.VISIBLE);
            mBinding.txtnieatr5.setVisibility(View.VISIBLE);
        }
      //  actualizarAtributo1(sol.getNombreTienda());
        SimpleDateFormat sdf=Constantes.sdfsolofecha;
        if(sol.getCreatedAt()!=null)
           // actualizarAtributo2(sdf.format(sol.getCreatedAt()));
        actualizarAtributo4(sdf.format(sol.getCreatedAt()));
        if(sol.getIndice()!=null)
            actualizarAtributo3(ComprasUtils.indiceLetra(sol.getIndice()));

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



    public void loadData() {
        Bundle datosRecuperados = getIntent().getExtras();

        if(datosRecuperados!=null) {
            idinformeSel = datosRecuperados.getInt(INFORMESEL);
            etapa = datosRecuperados.getInt(ContInfEtapaFragment.ETAPA);
            isCor = datosRecuperados.getBoolean(CORRECCION);
            plantaSel = datosRecuperados.getInt( NuevoInfEtapaActivity.PLANTASEL);
            numfoto = datosRecuperados.getInt(NuevoInfEtapaActivity.NUMFOTO);
            Log.d(TAG,"es correccion"+isCor);
            if(!isCor&&idinformeSel>0) {
                isEdicion = true;

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
        if(isCor) {
            if(etapa==4)//el regreso se maneja en el fragment
            {
                NvaCorreccionEmpFragment fragment = (NvaCorreccionEmpFragment) getSupportFragmentManager().findFragmentById(R.id.continfeta_fragment);
                fragment.atras();
                return;
            }
            if(etapa==3)//el regreso se maneja en el fragment
            {
                try {
                    NvaCorrecCalCajaFragment fragment = (NvaCorrecCalCajaFragment) getSupportFragmentManager().findFragmentById(R.id.continfeta_fragment);
                    fragment.atras();
                    return;
                }catch(ClassCastException ex){

                }

            }
            super.onBackPressed();

        }else


        if(etapa==3)//el regreso se maneja en el fragment
        {
            try {
                NvoEtiquetadoFragment fragment = (NvoEtiquetadoFragment) getSupportFragmentManager().findFragmentById(R.id.continfeta_fragment);
                fragment.atras();
            }catch(ClassCastException ex){
                NvoEtiqCajaFragment fragment = (NvoEtiqCajaFragment) getSupportFragmentManager().findFragmentById(R.id.continfeta_fragment);
                fragment.atras();
            }
            return;

        }
        if(etapa==4)
        {
            if(dViewModel.preguntaAct==92)
                return;
            Log.d(TAG,"regresando de "+dViewModel.preguntaAct);
            //busco el siguiente
            Reactivo reactivo = dViewModel.buscarReactivoAnterior(dViewModel.preguntaAct);
            if(reactivo!=null)
            //busco el reactivo anterior
            {
                if (dViewModel.preguntaAct ==93) {
                    //tal vez regrese a otra caja
                    if(dViewModel.cajaAct.consCaja>1){
                        reactivo=dViewModel.buscarReactivoSim(113);
                      //  dViewModel.cajaAct.consCaja=dViewModel.cajaAct.consCaja-1;
                        dViewModel.cajaAct=dViewModel.resumenEtiq.get(dViewModel.cajaAct.consCaja-2);
                    }
                }
                if (dViewModel.preguntaAct > 91) {
                    Bundle bundle=new Bundle();


                    bundle.putInt(NvoEmpaqueFragment.ARG_PREGACT,reactivo.getId());
                    bundle.putBoolean(NvoEmpaqueFragment.ARG_ESEDI,true);
                    NvoEmpaqueFragment nvofrag = new NvoEmpaqueFragment();
                    nvofrag.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
// Definir una transacción
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
// Remplazar el contenido principal por el fragmento
                    fragmentTransaction.replace(R.id.continfeta_fragment, nvofrag);
                    // fragmentTransaction.addToBackStack(null);
// Cambiar
                    fragmentTransaction.commit();
                }else
                if (dViewModel.preguntaAct == 92 && dViewModel.variasClientes) {
                    Bundle bundle=new Bundle();


                    bundle.putInt(NvoEmpaqueFragment.ARG_PREGACT,reactivo.getId());
                    bundle.putBoolean(NvoEmpaqueFragment.ARG_ESEDI,true);
                    NvoEmpaqueFragment nvofrag = new NvoEmpaqueFragment();
                    nvofrag.setArguments(bundle);

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
                Bundle bundle=new Bundle();
                if(infvm.cont>100){
                    bundle.putInt(NvaPreparacionFragment.ARG_PREGACT,infvm.cont - 101);
                    bundle.putBoolean(NvaPreparacionFragment.ARG_ESEDI,true);
                    nvofrag=    new NvaPreparacionFragment();
                    nvofrag.setArguments(bundle);

                    //nvofrag= new NvaPreparacionFragment(infvm.cont - 101, true, null);

                }

                else {
                    bundle.putInt(NvaPreparacionFragment.ARG_PREGACT,infvm.cont +100);
                    bundle.putBoolean(NvaPreparacionFragment.ARG_ESEDI,true);
                    nvofrag=    new NvaPreparacionFragment();
                    nvofrag.setArguments(bundle);
                   // nvofrag = new NvaPreparacionFragment(infvm.cont + 100, true, null);

                }

                FragmentManager fragmentManager = getSupportFragmentManager();
// Definir una transacción
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
// Remplazar el contenido principal por el fragmento
                fragmentTransaction.replace(R.id.continfeta_fragment, nvofrag);
                // fragmentTransaction.addToBackStack(null);
// Cambiar
                fragmentTransaction.commit();
            }
          if (infvm.cont == 1 && dViewModel.variasClientes) {
              Bundle bundle=new Bundle();
              bundle.putInt(NvaPreparacionFragment.ARG_PREGACT,infvm.cont - 1);
              bundle.putBoolean(NvaPreparacionFragment.ARG_ESEDI,true);
              NvaPreparacionFragment nvofrag=    new NvaPreparacionFragment();
              nvofrag.setArguments(bundle);
                //NvaPreparacionFragment nvofrag = new NvaPreparacionFragment(infvm.cont - 1, true, null);
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
        if(etapa==5)//el regreso se maneja en el fragment
        {

                NvoEnvioFragment fragment = (NvoEnvioFragment) getSupportFragmentManager().findFragmentById(R.id.continfeta_fragment);
                fragment.atras();

            return;

        }else
                super.onBackPressed();

}

public void cambiarTitulo(String titulo){

    myChildToolbar.setTitle(titulo);
}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        noSalir=false;
        switch (item.getItemId()) {
            case R.id.csalir:
                int numpreg=0;
                //reviso que no sea de las que no debe salir
                if(etapa==3)
                if(dViewModel.preguntaAct==3||dViewModel.preguntaAct==4) {

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

