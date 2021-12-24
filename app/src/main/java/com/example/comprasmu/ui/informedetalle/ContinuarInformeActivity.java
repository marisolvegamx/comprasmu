package com.example.comprasmu.ui.informedetalle;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeTemp;
import com.example.comprasmu.data.modelos.Reactivo;
import com.example.comprasmu.data.modelos.Visita;
import com.example.comprasmu.databinding.ActivityContinuarInformeBinding;
import com.example.comprasmu.databinding.FragmentNuevoinformeBinding;
import com.example.comprasmu.ui.BackActivity;
import com.example.comprasmu.ui.informe.NuevoinformeViewModel;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.ui.FiltrarListaActivity;

import java.util.Calendar;
import java.util.Date;
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
    Reactivo preguntaAct;
    InformeTemp ultimares;
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
        //reviso si es edicion
        ultimares=dViewModel.getUltimoTemp();
        if(ultimares!=null) //es edicion
        {

             preguntaAct=dViewModel.inftempToReac(ultimares);


             if(preguntaAct.getTabla().equals(ultimares.getTabla())){
                //si es la misma
                Log.d(TAG, "reactivo:" + preguntaAct.getLabel());
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.continf_fragment, new DetalleProductoFragment(preguntaAct,true));
                ft.commit();

            }else{
                 buscarPreguntas();

                 dViewModel.getReactivos().observe(this, new Observer<List<Reactivo>>() {
                     @Override
                     public void onChanged(List<Reactivo> reactivos) {
                         Log.d(TAG, "reactivo:" + reactivos.get(0).getLabel());
                         FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                         ft.add(R.id.continf_fragment, new DetalleProductoFragment(reactivos.get(0),false));
                         ft.commit();
                     }
                 });
            }
        }else {
            buscarPreguntas();

            dViewModel.getReactivos().observe(this, new Observer<List<Reactivo>>() {
                @Override
                public void onChanged(List<Reactivo> reactivos) {
                    Log.d(TAG, "reactivo:" + reactivos.get(0).getLabel());
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.continf_fragment, new DetalleProductoFragment(reactivos.get(0),false));
                    ft.commit();
                }
            });
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

    public void actualizarCliente(InformeCompra nvoInf) {
        mBinding.setInforme(nvoInf);
        mBinding.row2.setVisibility(View.VISIBLE);
    }

    public void actualizarProdSel(NuevoDetalleViewModel.ProductoSel prodsel) {
        mBinding.setProductoSel(prodsel);
        mBinding.txtciplanta.setVisibility(View.VISIBLE);
        mBinding.row4.setVisibility(View.VISIBLE);
    }
    public void actualizarCodProd(String codprod) {
        mBinding.txtcicodpr.setText(codprod);

        mBinding.row45.setVisibility(View.VISIBLE);

    }
    public void actualizarAtributo1() {
        mBinding.txtcitomadode.setText(Constantes.VarDetalleProd.tomadode);
        mBinding.setDanioa(Constantes.VarDetalleProd.nvoatra);

        mBinding.row5.setVisibility(View.VISIBLE);

    }
    public void actualizarAtributo2() {
        mBinding.setDaniob(Constantes.VarDetalleProd.nvoatrb);

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
                ValidadorDatos valdat=new ValidadorDatos();
                if(valdat.compararFecha(visita.getCreatedAt(),new Date())){
                    //es un informe de ayer no puede continuar
                    //avisar
                     AlertDialog.Builder dialogo1 = new AlertDialog.Builder(ContinuarInformeActivity.this);
                    dialogo1.setTitle(R.string.importante);
                    dialogo1.setMessage(R.string.informe_ayer);
                    dialogo1.setCancelable(false);

                    dialogo1.setNegativeButton(R.string.cerrar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        //  dialogo1.cancel();
                        //envio a la lista
                       finish();
                    }
                });
                dialogo1.show();
                }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_continuar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.csalir:
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
                dialogo1.setTitle(R.string.importante);
                dialogo1.setMessage(R.string.pregunta_salir);
                dialogo1.setCancelable(false);

                dialogo1.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        dialogo1.cancel();
                    }
                });
                dialogo1.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        DetalleProductoFragment fragment = (DetalleProductoFragment) getSupportFragmentManager().findFragmentById(R.id.continf_fragment);

                        //hay que guardar la ultima preguta
                        fragment.guardarResp();
                        finish();


                    }
                });
                dialogo1.show();


                return true;

            default:
                break;
        }

        return false;
    }
}