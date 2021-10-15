package com.example.comprasmu.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.example.comprasmu.R;
import com.example.comprasmu.ui.informe.NuevaFotoExhibFragment;
import com.example.comprasmu.ui.informe.VerInformeFragment;
import com.example.comprasmu.ui.informedetalle.DetalleProductoFragment1;
import com.example.comprasmu.ui.informe.NuevoinformeFragment;
import com.example.comprasmu.ui.listacompras.TabsFragment;

public class BackActivity extends AppCompatActivity {

    public static final String ARG_FRAGMENT="comprasmu.backactivity.fregmentsel";
    public static final String OP_DETALLE_PRODUCTO="detalle_producto";
    public static final String OP_LISTACOMPRA="lista_compra";
    public static final String OP_INFORME="informe";
    public static final String OP_INFORMEDET="informedet";
    public static final String TAG="BackActivity";
    public static final int REQUEST_CODE=1003;
    public static final String OP_PRODUCTOEX="productoex";
    Toolbar myChildToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back);
        // get fragment manager

        myChildToolbar =
                (Toolbar) findViewById(R.id.my_child_toolbar2);
        setSupportActionBar(myChildToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
// add
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


        //para saber que fragment cargar
        Bundle datosRecuperados = getIntent().getExtras();
        String opcionSel;
        if (datosRecuperados != null) {
            // No hay datos, manejar excepción
            //no debería estar aqui
            opcionSel= datosRecuperados.getString(ARG_FRAGMENT);
            switch (opcionSel){
                case OP_DETALLE_PRODUCTO:
                    myChildToolbar.setTitle(R.string.nuevo_informe);
                    ft.add(R.id.back_fragment, new DetalleProductoFragment1());
                    break;
                case OP_LISTACOMPRA:
                    myChildToolbar.setTitle(R.string.menu_ver_lista);
                    Bundle bundle=new Bundle();
                    bundle.putInt(NuevoinformeFragment.INFORMESEL,datosRecuperados.getInt(NuevoinformeFragment.INFORMESEL));
                    bundle.putString(TabsFragment.ARG_MUESTRA,"true");
                    bundle.putInt("ciudadSel",datosRecuperados.getInt("ciudadSel"));
                    bundle.putString("ciudadNombre",datosRecuperados.getString("ciudadNombre"));
                    TabsFragment detailFragment = new TabsFragment();
                    detailFragment.setArguments(bundle);
                    ft.add(R.id.back_fragment, detailFragment);
                    break;

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
                default:
                    break;
            }

// alternatively add it with a tag
// trx.add(R.id.your_placehodler, new YourFragment(), "detail");
            ft.commit();
        }else{
            //mandar error
            Log.d(TAG,"No llegó el parametro");
        }



    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "mmmmmmmmmmmm estoy muriendo");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"qqqqqqqqqqqqqq mori");
    }
}