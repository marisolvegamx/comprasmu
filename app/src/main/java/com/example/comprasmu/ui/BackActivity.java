package com.example.comprasmu.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.os.Bundle;

import com.example.comprasmu.R;
import com.example.comprasmu.ui.infetapa.NuevoInfEtapaActivity;
import com.example.comprasmu.utils.ui.ListaInformesEtaFragment;
import com.example.comprasmu.utils.ui.VerInformeGenFragment;
import com.example.comprasmu.ui.informe.NuevaFotoExhibFragment;
import com.example.comprasmu.ui.informe.VerInformeFragment;
import com.example.comprasmu.ui.informedetalle.DetalleProductoFragment;

import com.example.comprasmu.ui.informe.NuevoinformeFragment;
import com.example.comprasmu.ui.listacompras.SelClienteFragment;
import com.example.comprasmu.ui.listadetalle.ListaCompraFragment;
import com.example.comprasmu.ui.sustitucion.SustitucionFragment;

public class BackActivity extends AppCompatActivity {

    public static final String ARG_FRAGMENT="comprasmu.backactivity.fregmentsel";
    public static final String OP_DETALLE_PRODUCTO="detalle_producto";
    public static final String OP_LISTACOMPRA="lista_compra";
    public static final String OP_INFORME="informe";
    public static final String OP_INFORMEDET="informedet";
    public static final String OP_SELPLANTA="selplanta";
    public static final String OP_SELPLANTAMUE="selplantamue";
    public static final String OP_SUSTITUCION="sustitucion";

    public static final String OP_INFORMECOR="informecor";
    public static final String TAG="BackActivity";

    public static final int REQUEST_CODE=1003;
    public static final String OP_PRODUCTOEX="productoex";
    Toolbar myChildToolbar;
    private static final int INTERVALO = 3000; //3 segundos para salir
    private long tiempoPrimerClick;
    String opcionSel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back);
        // get fragment manager

        myChildToolbar =
                findViewById(R.id.my_child_toolbar2);


// add
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


        //para saber que fragment cargar
        Bundle datosRecuperados = getIntent().getExtras();
        Bundle bundle6=new Bundle();
        if (datosRecuperados != null) {
            // No hay datos, manejar excepción
            //no debería estar aqui
            opcionSel = datosRecuperados.getString(ARG_FRAGMENT);
        }
            switch (opcionSel){
                case OP_DETALLE_PRODUCTO:
                    myChildToolbar.setTitle(R.string.nuevo_informe);
                  //  ft.add(R.id.back_fragment, new DetalleProductoFragment1());
                    break;
                case OP_LISTACOMPRA:
                    myChildToolbar.setTitle(R.string.menu_ver_lista);
                    Bundle bundle=new Bundle();
                    bundle.putInt(NuevoinformeFragment.INFORMESEL,datosRecuperados.getInt(NuevoinformeFragment.INFORMESEL));

                    bundle.putInt("ciudadSel",datosRecuperados.getInt("ciudadSel"));
                    bundle.putString("ciudadNombre",datosRecuperados.getString("ciudadNombre"));
                  //  intento1.putExtra(,plantaSel );
                    bundle.putInt(ListaCompraFragment.ARG_PLANTASEL,datosRecuperados.getInt(ListaCompraFragment.ARG_PLANTASEL));
                    //intento1.putExtra(, NOMBREPLANTASEL);
                    bundle.putString(ListaCompraFragment.ARG_NOMBREPLANTASEL,datosRecuperados.getString(ListaCompraFragment.ARG_NOMBREPLANTASEL));
                    bundle.putString(SelClienteFragment.ARG_TIPOCONS, datosRecuperados.getString(SelClienteFragment.ARG_TIPOCONS));
                    //intento1.putExtra(ARG_CLIENTESEL,mViewModel.clienteSel);
                    bundle.putInt(ListaCompraFragment.ARG_CLIENTESEL,datosRecuperados.getInt(ListaCompraFragment.ARG_CLIENTESEL));
                    bundle.putString(SustitucionFragment.ARG_CATEGORIA, datosRecuperados.getString(SustitucionFragment.ARG_CATEGORIA));
                    bundle.putString(SustitucionFragment.ARG_SIGLAS, datosRecuperados.getString(SustitucionFragment.ARG_SIGLAS));
                    bundle.putBoolean(ListaCompraFragment.ISBACKUP,datosRecuperados.getBoolean(ListaCompraFragment.ISBACKUP) );
                    //intento1.putExtra(DetalleProductoFragment.NUMMUESTRA,nummuestra );
                    bundle.putInt(DetalleProductoFragment.NUMMUESTRA,datosRecuperados.getInt(DetalleProductoFragment.NUMMUESTRA));


                    //  TabsFragment detailFragment = new TabsFragment();
                    ListaCompraFragment detailFragment = new ListaCompraFragment();
                  //  SelClienteFragment detailFragment = new SelClienteFragment();

                    detailFragment.setArguments(bundle);
                    ft.add(R.id.back_fragment, detailFragment);
                    break;

                case OP_SELPLANTA:
                    myChildToolbar.setTitle(R.string.menu_ver_lista);
                    Bundle bundle5=new Bundle();
                    bundle5.putInt(NuevoinformeFragment.INFORMESEL,datosRecuperados.getInt(NuevoinformeFragment.INFORMESEL));

                    bundle5.putInt("ciudadSel",datosRecuperados.getInt("ciudadSel"));
                    bundle5.putString("ciudadNombre",datosRecuperados.getString("ciudadNombre"));
                  //  intento1.putExtra(,plantaSel );
                    bundle5.putString(SelClienteFragment.ARG_TIPOCONS,datosRecuperados.getString(SelClienteFragment.ARG_TIPOCONS));
                    bundle5.putInt(ListaCompraFragment.ARG_CLIENTESEL,datosRecuperados.getInt(ListaCompraFragment.ARG_CLIENTESEL));

                    bundle5.putString(ListaCompraFragment.ARG_MUESTRA,datosRecuperados.getString(ListaCompraFragment.ARG_MUESTRA) );
                    bundle5.putInt(DetalleProductoFragment.NUMMUESTRA,datosRecuperados.getInt(DetalleProductoFragment.NUMMUESTRA));

                  //  SelClienteFragment detailFragment = new ListaCompraFragment();
                      SelClienteFragment detailFragment5 = new SelClienteFragment();

                      detailFragment5.setArguments(bundle5);
                    ft.add(R.id.back_fragment, detailFragment5);
                    break;
               /* case OP_SELPLANTAMUE:
                    myChildToolbar.setTitle(R.string.menu_ver_lista);
                    Bundle bundle6=new Bundle();
                    bundle6.putInt(NuevoinformeFragment.INFORMESEL,datosRecuperados.getInt(NuevoinformeFragment.INFORMESEL));

                    bundle6.putInt("ciudadSel",datosRecuperados.getInt("ciudadSel"));
                    bundle6.putString("ciudadNombre",datosRecuperados.getString("ciudadNombre"));
                    //  intento1.putExtra(,plantaSel );
                    bundle6.putString(SelClienteFragment.ARG_TIPOCONS,datosRecuperados.getString(SelClienteFragment.ARG_TIPOCONS));
                    bundle6.putInt(ListaCompraFragment.ARG_CLIENTESEL,datosRecuperados.getInt(ListaCompraFragment.ARG_CLIENTESEL));

                    bundle6.putString(ListaCompraFragment.ARG_MUESTRA,datosRecuperados.getString(ListaCompraFragment.ARG_MUESTRA) );
                    bundle6.putInt(DetalleProductoFragment.NUMMUESTRA,datosRecuperados.getInt(DetalleProductoFragment.NUMMUESTRA));

                    //  SelClienteFragment detailFragment = new ListaCompraFragment();
                    SelClienteFragment detailFragment6 = new SelClienteFragment();

                    detailFragment6.setArguments(bundle6);
                    ft.add(R.id.back_fragment, detailFragment6);
                    break;*/
                case OP_PRODUCTOEX:
                    myChildToolbar.setTitle(R.string.producto_exhibido);
                    Bundle bundle2=new Bundle();
                      NuevaFotoExhibFragment detailFragment2 = new NuevaFotoExhibFragment();

                    ft.add(R.id.back_fragment, detailFragment2);
                    break;
                case OP_INFORME:
                    myChildToolbar.setTitle(R.string.informes);

                    VerInformeFragment detailFragment3 = new VerInformeFragment();

                    ft.add(R.id.back_fragment, detailFragment3);
                    break;
                case OP_INFORMEDET:
                    myChildToolbar.setTitle(R.string.muestras);

                    NuevaFotoExhibFragment detailFragment4 = new NuevaFotoExhibFragment();

                    ft.add(R.id.back_fragment, detailFragment4);
                    break;
                case OP_SUSTITUCION:
                    myChildToolbar.setTitle(R.string.menu_ver_lista);
                     bundle6=new Bundle();
                 //   bundle6.putInt(NuevoinformeFragment.INFORMESEL,datosRecuperados.getInt(NuevoinformeFragment.INFORMESEL));

                    bundle6.putInt("ciudadSel",datosRecuperados.getInt("ciudadSel"));
                    bundle6.putString("ciudadNombre",datosRecuperados.getString("ciudadNombre"));
                    //  intento1.putExtra(,plantaSel );
                    bundle6.putInt(ListaCompraFragment.ARG_PLANTASEL,datosRecuperados.getInt(ListaCompraFragment.ARG_PLANTASEL));
                    //intento1.putExtra(, NOMBREPLANTASEL);
                    bundle6.putString(ListaCompraFragment.ARG_NOMBREPLANTASEL,datosRecuperados.getString(ListaCompraFragment.ARG_NOMBREPLANTASEL));
                  //  bundle6.putString(SelClienteFragment.ARG_TIPOCONS, datosRecuperados.getString(SelClienteFragment.ARG_TIPOCONS));
                    //intento1.putExtra(ARG_CLIENTESEL,mViewModel.clienteSel);
                    bundle6.putInt(ListaCompraFragment.ARG_CLIENTESEL,datosRecuperados.getInt(ListaCompraFragment.ARG_CLIENTESEL));
                    bundle6.putString(SustitucionFragment.ARG_CATEGORIA, datosRecuperados.getString(SustitucionFragment.ARG_CATEGORIA));
                    bundle6.putString(SustitucionFragment.ARG_SIGLAS, datosRecuperados.getString(SustitucionFragment.ARG_SIGLAS));
                    bundle6.putBoolean(ListaCompraFragment.ISBACKUP,datosRecuperados.getBoolean(ListaCompraFragment.ISBACKUP) );
                    //intento1.putExtra(DetalleProductoFragment.NUMMUESTRA,nummuestra );
                 //   bundle6.putInt(DetalleProductoFragment.NUMMUESTRA,datosRecuperados.getInt(DetalleProductoFragment.NUMMUESTRA));


                    //  TabsFragment detailFragment = new TabsFragment();
                    SustitucionFragment detailFragment6 = new SustitucionFragment();
                    //  SelClienteFragment detailFragment = new SelClienteFragment();

                    detailFragment6.setArguments(bundle6);
                    ft.add(R.id.back_fragment, detailFragment6);
                    break;
                case OP_INFORMECOR:

                    myChildToolbar.setTitle(R.string.informe);
                    VerInformeGenFragment detailFragment7 = new VerInformeGenFragment();
                    bundle6.putString(ListaInformesEtaFragment.ARG_TIPOCONS, datosRecuperados.getString(ListaInformesEtaFragment.ARG_TIPOCONS));
                    bundle6.putInt(ListaInformesEtaFragment.INFORMESEL,datosRecuperados.getInt(ListaInformesEtaFragment.INFORMESEL));
                   // bundle6.putInt(NuevoInfEtapaActivity.NUMFOTO,datosRecuperados.getInt(NuevoInfEtapaActivity.NUMFOTO));

                    detailFragment7.setArguments(bundle6);
                    ft.add(R.id.back_fragment, detailFragment7);
                    break;
                default:
                    break;
            }
        // Get a support ActionBar corresponding to this toolbar
        setSupportActionBar(myChildToolbar);
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        // alternatively add it with a tag
        // trx.add(R.id.your_placehodler, new YourFragment(), "detail");
            ft.commit();




    }
   /* @Override
    public void onBackPressed(){
        if(opcionSel.equals(OP_DETALLE_PRODUCTO)||opcionSel.equals(OP_PRODUCTOEX)) {
            if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()) {
                super.onBackPressed();
                return;
            } else {
                Toast.makeText(this, "Sí sale sin guardar la información  se perderá. Vuelve a presionar para salir", Toast.LENGTH_LONG).show();
            }
            tiempoPrimerClick = System.currentTimeMillis();
        }else
            super.onBackPressed();
    }*/

    @Override
    public void onBackPressed() {

        //revisar que esté en el frgment detalle producto y que haya capturado alas siglas
        //y la fecha
        if(opcionSel.equals(OP_DETALLE_PRODUCTO)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(R.string.app_name);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage("Sí sale sin guardar la información se perderá seguro?")
                    .setCancelable(false)
                    .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }
        super.onBackPressed();
    }
    // Create an anonymous implementation of OnClickListener

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}