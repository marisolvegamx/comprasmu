package com.example.comprasmu.ui.informedetalle;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeTemp;
import com.example.comprasmu.data.modelos.Reactivo;
import com.example.comprasmu.data.modelos.Visita;
import com.example.comprasmu.databinding.ActivityContinuarInformeBinding;
import com.example.comprasmu.ui.BackActivity;
import com.example.comprasmu.ui.informe.NuevoinformeViewModel;
import com.example.comprasmu.utils.Constantes;

import java.util.Date;
import java.util.List;

public class ContinuarInformeActivity extends AppCompatActivity  {
    Toolbar myChildToolbar;
    NuevoinformeViewModel nviewModel;
    private NuevoDetalleViewModel dViewModel;
    private ActivityContinuarInformeBinding mBinding;
    Visita visitaCont;
    private static final boolean hayDatos = false;
    public final static String INFORMESEL = "comprasmu.ni_informesel";
    private static final String TAG = "ContInformeAct";
    private DetalleProductoFragment fragementAct;
    Reactivo preguntaAct;
    InformeTemp ultimares;
    boolean noSalir;
    int idinformeSel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_continuar_informe);

        // get fragment manager

        myChildToolbar =
                findViewById(R.id.toolbarinf);
        setSupportActionBar(myChildToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
// add
        nviewModel =
                new ViewModelProvider(this).get(NuevoinformeViewModel.class);

        dViewModel = new ViewModelProvider(this).get(NuevoDetalleViewModel.class);
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
        //reviso si es edicion y ya tengo info en temp
        ultimares=dViewModel.getUltimoTemp();

        if(ultimares!=null) //es edicion
        {
            if(ultimares.getNombre_campo().equals("ticket_noemiten")){
                //me muevo a la anterior
                ultimares=dViewModel.buscarTempxId(ultimares.getId()-1);
            }
            preguntaAct=dViewModel.inftempToReac(ultimares);

            Log.d(TAG, "reactivo:" +preguntaAct.getId()+"--"+ultimares.getNombre_campo());

            nviewModel.numMuestra=ultimares.getNumMuestra();
            nviewModel.setIdInformeNuevo(ultimares.getInformesId());
            nviewModel.consecutivo=ultimares.getConsecutivo();
            nviewModel.clienteSel=ultimares.getClienteSel();
             if(preguntaAct.getTabla().equals(ultimares.getTabla())){
                //si es la misma

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                if(nviewModel.clienteSel==4||nviewModel.clienteSel==0)
                    ft.add(R.id.continf_fragment, new DetalleProductoFragment(preguntaAct,true));
                if(nviewModel.clienteSel==5)
                     ft.add(R.id.continf_fragment, new DetalleProductoPenFragment(preguntaAct,true));
                if(nviewModel.clienteSel==6)
                     ft.add(R.id.continf_fragment, new DetalleProductoElecFragment(preguntaAct,true));

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
            //e uno nuevo
            buscarPreguntas();
            nviewModel.numMuestra=0;
           // nviewModel.setIdInformeNuevo(ultimares.getInformesId());
          //  nviewModel.consecutivo=ultimares.getConsecutivo();
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
        mBinding.row45.setVisibility(View.VISIBLE);
    }

    public void reiniciarBarra() {
        mBinding.setProductoSel(null);
        mBinding.txtciplanta.setVisibility(View.GONE);
        mBinding.row4.setVisibility(View.GONE);
        mBinding.row45.setVisibility(View.GONE);
        mBinding.txtcicodpr.setText("");
        mBinding.txtcitomadode.setText("");
        mBinding.setDanioa("");

        mBinding.row5.setVisibility(View.GONE);
        mBinding.setDaniob("");
        Constantes.VarDetalleProd.nvoatrc="";
        Constantes.VarDetalleProd.nvoatra="";
        Constantes.VarDetalleProd.nvoatrb="";
        mBinding.setDanioc("");
        mBinding.row6.setVisibility(View.GONE);
        Constantes.VarDetalleProd.tomadode="";

    }
    public void actualizarCodProd(String codprod) {
        mBinding.txtcicodpr.setText(codprod);

        mBinding.row7.setVisibility(View.VISIBLE);

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

    public void loadData(Bundle savedInstanceState) {
        Bundle datosRecuperados = getIntent().getExtras();

        if(datosRecuperados!=null)
         idinformeSel = datosRecuperados.getInt(INFORMESEL); //es la visita id
        else { //lo recupero
            if (savedInstanceState != null) {    // Restore value of members from saved state
              //  idinformeSel = savedInstanceState.getInt("visitasel");
              //  if(idinformeSel>0) { //se salio y lo devuelvo al inicio
                    Intent intento1 = new Intent(this, NavigationDrawerActivity.class);
                    startActivity(intento1);
                    //mando al inicio
                  //  NavHostFragment.findNavController(this).navigate(R.id.action_continuar, bundle);
              //  }
            }

        }

        mBinding.setSdf(Constantes.sdfsolofecha);
        //Log.d(TAG, "informe creado=" + idinformeSel);
        //busco la visita
        visitaCont=nviewModel.buscarVisitaSimpl(idinformeSel);
                ValidadorDatos valdat=new ValidadorDatos();
              //  Constantes.DP_TIPOTIENDA=visita.getTipoId();
                if(valdat.compararFecha(visitaCont.getCreatedAt(),new Date())){
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
                Constantes.DP_TIPOTIENDA = visitaCont.getTipoId();
                Log.d(TAG, "VISITA " + visitaCont.getCreatedAt());
                Log.d(TAG, "tipo tienda -----------*" + Constantes.DP_TIPOTIENDA);
                nviewModel.visita = visitaCont;
                mBinding.setVisita(visitaCont);

    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
    boolean salir;
    @Override
    public void onBackPressed() {
        Log.e(TAG,"aprete atras"+dViewModel.reactivoAct);
        int numpreg =0;
        if(dViewModel.reactivoAct==1) {
          //  Integer[] clientesprev = dViewModel.tieneInforme(nviewModel.visita);
          //  Log.e(TAG,"aprete atras"+clientesprev+"--"+nviewModel.visita);

           // if (clientesprev != null || clientesprev.length > 0) {
          //      numpreg = 43;
         //   } else {
                //veo si ya tiene informe
                getFragmentManager().popBackStack();
                return;
           // }
        }
        else

        if( nviewModel.clienteSel==4||nviewModel.clienteSel==0||dViewModel.reactivoAct==1) {
            DetalleProductoFragment fragment = (DetalleProductoFragment) getSupportFragmentManager().findFragmentById(R.id.continf_fragment);
             numpreg = fragment.getNumPregunta();
        }else
        if( nviewModel.clienteSel==5) {
            DetalleProductoPenFragment fragment2 = (DetalleProductoPenFragment) getSupportFragmentManager().findFragmentById(R.id.continf_fragment);
             numpreg = fragment2.getNumPregunta();
        }else

        if( nviewModel.clienteSel==6) {
            DetalleProductoElecFragment fragment3 = (DetalleProductoElecFragment) getSupportFragmentManager().findFragmentById(R.id.continf_fragment);
             numpreg = fragment3.getNumPregunta();
        }


        salir=true;
     /*   if(numpreg==1) {
            super.onBackPressed();
            return;
        }*/

        //veo que pregunta es
        if(numpreg==4||numpreg==3||numpreg==5||numpreg==43||numpreg==7)
        {
            return; //no puedo regresar
        }
        if(numpreg==54||numpreg==53||numpreg==55||numpreg==68||numpreg==57||numpreg==67)
        {
            return; //no puedo regresar
        }
        if(numpreg==74||numpreg==73||numpreg==75||numpreg==88||numpreg==77)
        {
            return; //no puedo regresar
        }
        if(numpreg==23||numpreg==58||numpreg==78) //regreso a lista compra
        {
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle(R.string.importante);

            dialogo1.setMessage(R.string.muestra_atras);


            dialogo1.setCancelable(false);

            dialogo1.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        dialogo1.cancel();
                        salir=false;
                    }
            });
            dialogo1.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                       //eliminar muestra
                    nviewModel.eliminarMuestra(nviewModel.numMuestra);
                    //quito la info de la barra gris
                    reiniciarBarra();
                    dViewModel.setIddetalleNuevo(0);
                    dViewModel.icdNuevo=null;

                    //ContinuarInformeActivity.super.onBackPressed();
                    regresar();

                    }
                });
                dialogo1.show();

        }else
            regresar();
         //   super.onBackPressed();
    }
    public void regresar(){

        if( nviewModel.clienteSel==4||nviewModel.clienteSel==0) {
         regresarPep();
        }
        if( nviewModel.clienteSel==5) {
            regresarPen();
        }

        if( nviewModel.clienteSel==6) {
            regresarElec();
        }



    }

    public void regresarPep(){
        InformeTemp resact=null;
        int idreact=0;

        DetalleProductoFragment fragment = (DetalleProductoFragment) getSupportFragmentManager().findFragmentById(R.id.continf_fragment);

        if (fragment.isEdicion) {
            resact = fragment.getUltimares();
            idreact = resact.getId();
        }

        //busco el siguiente
        Reactivo reactivo = dViewModel.buscarReactivoAnterior(idreact,fragment.isEdicion);
        if(reactivo!=null) {
            DetalleProductoFragment nvofrag = new DetalleProductoFragment(reactivo, false);
            FragmentManager fragmentManager = getSupportFragmentManager();
// Definir una transacción
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
// Remplazar el contenido principal por el fragmento
            fragmentTransaction.replace(R.id.continf_fragment, nvofrag);
           // fragmentTransaction.addToBackStack(null);
// Cambiar
            fragmentTransaction.commit();
        }
        else
            super.onBackPressed();

    }
    public void regresarPen(){
        InformeTemp resact=null;
        int idreact=0;

        DetalleProductoPenFragment fragment = (DetalleProductoPenFragment) getSupportFragmentManager().findFragmentById(R.id.continf_fragment);

        if (fragment.isEdicion) {
                resact = fragment.getUltimares();
                idreact = resact.getId();
        }



        //busco el siguiente
        Reactivo reactivo = dViewModel.buscarReactivoAnterior(idreact,fragment.isEdicion);

        if(reactivo!=null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
// Definir una transacción
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
// Remplazar el contenido principal por el fragmento
            if(reactivo.getId()==1)
            {      DetalleProductoFragment nvofrag = new DetalleProductoFragment(reactivo, false);
                fragmentTransaction.replace(R.id.continf_fragment, nvofrag);
                }else {
                DetalleProductoPenFragment nvofrag = new DetalleProductoPenFragment(reactivo, false);
                fragmentTransaction.replace(R.id.continf_fragment, nvofrag);
            }

         //   fragmentTransaction.addToBackStack(null);
// Cambiar
            fragmentTransaction.commit();
        }
        else
            super.onBackPressed();

    }
    public void regresarElec(){
        InformeTemp resact=null;
        int idreact=0;

        DetalleProductoElecFragment fragment = (DetalleProductoElecFragment) getSupportFragmentManager().findFragmentById(R.id.continf_fragment);

        if (fragment.isEdicion) {
            resact = fragment.getUltimares();
            idreact = resact.getId();
        }


        //busco el siguiente
        Reactivo reactivo = dViewModel.buscarReactivoAnterior(idreact,fragment.isEdicion);
        if(reactivo!=null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
// Definir una transacción
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
// Remplazar el contenido principal por el fragmento
            if(reactivo.getId()==1)
            {      DetalleProductoFragment nvofrag = new DetalleProductoFragment(reactivo, false);
                fragmentTransaction.replace(R.id.continf_fragment, nvofrag);
            }else {
                DetalleProductoElecFragment nvofrag = new DetalleProductoElecFragment(reactivo, false);
                fragmentTransaction.replace(R.id.continf_fragment, nvofrag);
            }

           // fragmentTransaction.addToBackStack(null);
// Cambiar
            fragmentTransaction.commit();
        }
        else
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
        noSalir=false;
        switch (item.getItemId()) {
            case R.id.csalir:
                int numpreg=0;
                //reviso que no sea de las que no debe salir
                if( nviewModel.clienteSel==4||nviewModel.clienteSel==0||dViewModel.reactivoAct==1) {
                    DetalleProductoFragment fragment = (DetalleProductoFragment) getSupportFragmentManager().findFragmentById(R.id.continf_fragment);
                    numpreg = fragment.getNumPregunta();
                }else
                if( nviewModel.clienteSel==5) {
                    DetalleProductoPenFragment fragment2 = (DetalleProductoPenFragment) getSupportFragmentManager().findFragmentById(R.id.continf_fragment);
                    numpreg = fragment2.getNumPregunta();
                }else

                if( nviewModel.clienteSel==6) {
                    DetalleProductoElecFragment fragment3 = (DetalleProductoElecFragment) getSupportFragmentManager().findFragmentById(R.id.continf_fragment);
                    numpreg = fragment3.getNumPregunta();
                }
                if(nviewModel.clienteSel==4)
                    if(numpreg==2||numpreg==3||numpreg==4||numpreg==5){
                        noSalir=true;
                    }
                if(nviewModel.clienteSel==5)
                    if(numpreg==52||numpreg==53||numpreg==54||numpreg==55){
                        noSalir=true;
                    }
                if(nviewModel.clienteSel==6)
                    if(numpreg==72||numpreg==73||numpreg==74||numpreg==75){
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
    public void noSalir(boolean valor){
        this.noSalir=valor;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putInt("visitasel",idinformeSel );

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

  /*  @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
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
        boolean noSalir;
        int idinformeSel;
    }*/

    }