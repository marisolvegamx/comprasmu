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
import com.example.comprasmu.ui.correccion.NvaCorreccionEmpFragment;
import com.example.comprasmu.ui.correccion.NvaCorreccionEtiqFragment;
import com.example.comprasmu.ui.correccion.NvaCorreccionFragment;
import com.example.comprasmu.ui.correccion.NvaCorreccionPreFragment;
import com.example.comprasmu.ui.empaque.NvoEmpaqueFragment;
import com.example.comprasmu.ui.etiquetado.EditEtiquetadoFragment;
import com.example.comprasmu.ui.etiquetado.NvoEtiqCajaFragment;
import com.example.comprasmu.ui.etiquetado.NvoEtiquetadoFragment;
import com.example.comprasmu.ui.preparacion.NvaPreparacionFragment;
import com.example.comprasmu.ui.preparacion.NvaPreparacionViewModel;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;

import java.text.SimpleDateFormat;


public class EditInfEtapaActivity extends AppCompatActivity  {
    Toolbar myChildToolbar;
    private ActivityNuevoInfetapaBinding mBinding;
    public final static String INFORMESEL = "comprasmu.nie_informesel";
    public final static String PLANTASEL = "comprasmu.nie_plantasel";
    public final static String NUMFOTO = "comprasmu.nie_numfoto";
    public final static String CORRECCION = "comprasmu.nie_correc"; //para saber que es correccion
    private static final String TAG = "EditInfEtapaActivity";

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
            this.finish();
            Intent intento1 = new Intent(this, NavigationDrawerActivity.class);

            startActivity(intento1);

        }
        // if(Constantes.NM_TOTALISTA>=16)
        loadData();

        Log.d(TAG,"WWWWWW"+isEdicion+"++"+idinformeSel+"--"+etapa);

        if(isEdicion) //es edicion
        {
              FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


            if (etapa == 3) {
                mBinding.row1.setVisibility(View.GONE);
                Bundle args = new Bundle();
                args.putInt(NvoEtiquetadoFragment.ARG_PREGACT,3 );
                args.putBoolean(NvoEtiquetadoFragment.ARG_ESEDI,true);

                args.putInt(NvoEtiquetadoFragment.ARG_INFORMESEL,idinformeSel);
                EditEtiquetadoFragment nvofrag = new EditEtiquetadoFragment();
                nvofrag.setArguments(args);

                ft.add(R.id.continfeta_fragment,nvofrag);

            }

            ft.commit();
        }else {


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



    public void actualizarBarraEtiq(InformeEtapa nvoInf) {
        mBinding.txtnietotmues.setVisibility(View.VISIBLE);

        mBinding.setInforme(nvoInf);
        mBinding.row1.setVisibility(View.GONE);
        mBinding.rowetiq.setVisibility(View.VISIBLE);
        mBinding.row2.setVisibility(View.VISIBLE);

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




    public void loadData() {
        Bundle datosRecuperados = getIntent().getExtras();

        if(datosRecuperados!=null) {
            idinformeSel = datosRecuperados.getInt(INFORMESEL);
            etapa = datosRecuperados.getInt(ContInfEtapaFragment.ETAPA);
            isEdicion = true;

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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putInt("informesel",idinformeSel );

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    public void onBackPressed() {



        if(etapa==3)//el regreso se maneja en el fragment
        {
            try {
                EditEtiquetadoFragment fragment = (EditEtiquetadoFragment) getSupportFragmentManager().findFragmentById(R.id.continfeta_fragment);
                fragment.atras();
            }catch(ClassCastException ex){
                NvoEtiqCajaFragment fragment = (NvoEtiqCajaFragment) getSupportFragmentManager().findFragmentById(R.id.continfeta_fragment);
                fragment.atras();
            }
            return;

        }
       else {
                super.onBackPressed();
                return;

        }



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

