package com.example.comprasmu.ui.informedetalle;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.Reactivo;
import com.example.comprasmu.data.modelos.Visita;
import com.example.comprasmu.databinding.ActivityContinuarInformeBinding;
import com.example.comprasmu.databinding.FragmentNuevoinformeBinding;
import com.example.comprasmu.ui.BackActivity;
import com.example.comprasmu.ui.informe.NuevoinformeViewModel;
import com.example.comprasmu.utils.Constantes;

import java.util.List;

public class ContinuarInformeActivity extends AppCompatActivity  {
    Toolbar myChildToolbar;
    NuevoinformeViewModel nviewModel;
    private NuevoDetalleViewModel dViewModel;
    private ActivityContinuarInformeBinding mBinding;
    Visita visitaCont;
    private static boolean hayDatos = false;
    public final static String INFORMESEL = "comprasmu.ni_informesel";
    private static final String TAG = "ContInformeAct";
    private DetalleProductoFragment fragementAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_continuar_informe);

        // get fragment manager

        myChildToolbar =
                (Toolbar) findViewById(R.id.toolbarinf);
        setSupportActionBar(myChildToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
// add
        nviewModel =
                new ViewModelProvider(this).get(NuevoinformeViewModel.class);

        dViewModel = new ViewModelProvider(this).get(NuevoDetalleViewModel.class);
        // if(Constantes.NM_TOTALISTA>=16)
        loadData();
        buscarPreguntas();
        dViewModel.getReactivos().observe(this, new Observer<List<Reactivo>>() {
            @Override
            public void onChanged(List<Reactivo> reactivos) {
                Log.d(TAG, "reactivo:" + reactivos.get(0).getLabel());
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.continf_fragment, new DetalleProductoFragment(reactivos.get(0)));
                ft.commit();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


    }

    public void actualizarCliente(InformeCompra nvoInf) {
        mBinding.setInforme(nvoInf);
        mBinding.row2.setVisibility(View.VISIBLE);
    }

    public void actualizarProdSel(NuevoDetalleViewModel.ProductoSel prodsel) {
        mBinding.setProductoSel(prodsel);
        mBinding.row3.setVisibility(View.VISIBLE);
        mBinding.row4.setVisibility(View.VISIBLE);
    }
    public void actualizarAtributo1() {
        mBinding.setDanioa(Constantes.VarDetalleProd.nvoatra);
        mBinding.setDaniob(Constantes.VarDetalleProd.nvoatrb);

        mBinding.row5.setVisibility(View.VISIBLE);

    }
    public void actualizarAtributo2() {

        mBinding.setDanioc(Constantes.VarDetalleProd.nvoatrc);
        mBinding.row6.setVisibility(View.VISIBLE);
    }

    public void buscarPreguntas() {
        dViewModel.buscarReactivos();

    }

    public void loadData() {
        Bundle datosRecuperados = getIntent().getExtras();
        int idinformeSel = datosRecuperados.getInt(INFORMESEL);
        mBinding.setSdf(Constantes.sdfsolofecha);
        //Log.d(TAG, "informe creado=" + idinformeSel);
        //busco la visita
        nviewModel.buscarVisita(idinformeSel).observe(this, new Observer<Visita>() {
            @Override
            public void onChanged(Visita visita) {
                visitaCont = visita;
                Constantes.DP_TIPOTIENDA = visita.getTipoId();
                Log.d(TAG, "VISITA " + visita.getCreatedAt());
                Log.d(TAG, "tipo tienda -----------*" + Constantes.DP_TIPOTIENDA);
                nviewModel.visita = visita;
                mBinding.setVisita(visita);

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
    @Override
    public void onBackPressed() {
        DetalleProductoFragment fragment = (DetalleProductoFragment) getSupportFragmentManager().findFragmentById(R.id.continf_fragment);
        int numpreg=fragment.getNumPregunta();
        //veo que pregunta es
        if(numpreg==4||numpreg==3||numpreg==5||numpreg==43)
        {
            return; //no puedo regresar
        }
        super.onBackPressed();
    }



}