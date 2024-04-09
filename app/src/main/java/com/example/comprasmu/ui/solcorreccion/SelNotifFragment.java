package com.example.comprasmu.ui.solcorreccion;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.comprasmu.R;
import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.ListaCompraDao;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.ui.informe.ListaCancelFragment;
import com.example.comprasmu.ui.informedetalle.DetalleProductoFragment;
import com.example.comprasmu.ui.listadetalle.ListaCompraFragment;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.ui.ListaSelScrollFragment;
import com.example.comprasmu.utils.ui.ListaSelecFragment;

import java.util.ArrayList;
import java.util.List;

/*** prueba para juntar correcciones, notificaciones y canceladas***/
public class SelNotifFragment extends ListaSelecFragment{

    private  ArrayList<DescripcionGenerica> listaClientesEnv;
    private static final String TAG="SelNotifFragment";

    public static String ARG_TIPOCONS="comprasmu.correselcli.tipocons";


    public SelNotifFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //busco los datos y los convierto al tipo String[]
        super.onCreate(savedInstanceState);


    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        /*aqui me dice que dato es*/


        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.menu_notificaciones);
        convertirListaCor();
        setLista(listaClientesEnv);
        setupListAdapter();
        adaptadorLista.setDesc2(true);
        getObjetosLV().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //  Toast.makeText(getContext(),listaSeleccionable.get(i).getId()+"", Toast.LENGTH_LONG).show();
                siguiente(i);

            }
        });


    }

    private int contarCorrecc(int plantaId){

     //  int  pendientes=scViewModel.getTotalSolsxplantaAll(Constantes.INDICEACTUAL,1, plantaId);
      //  return pendientes;
        return 0;
    }

    /****para correcciones de tienda****/
    private int contarCorreccGen(String ciudad){

      //  int  pendientes=scViewModel.getTotSolsVis(Constantes.INDICEACTUAL,1, ciudad);
      //  return pendientes;
        return 0;
    }
    public void siguiente(int i){
       // Log.d(TAG,"una planta "+tipoconsulta+"--"+listaSeleccionable.get(i).getId());
        int opcion=listaSeleccionable.get(i).getId();
        Bundle bundle = new Bundle();
        switch(opcion) {

            case 1:
               // bundle.putInt(ListaCompraFragment.ARG_PLANTASEL, listaSeleccionable.get(i).getId());
               // bundle.putString(ListaCompraFragment.ARG_NOMBREPLANTASEL, listaSeleccionable.get(i).getDescripcion2());
                if (listaSeleccionable.get(i).getDescripcion().equals("gen")) {
                    bundle.putBoolean(ListaSolCorreFragment.ARG_ESGEN, true);
                }

                bundle.putString(ARG_TIPOCONS, "action_selclitosolcor2");
                NavHostFragment.findNavController(this).navigate(R.id.action_notiftosolcor, bundle);
                break;
            case 2: //canceladas
              //  FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
               // ListaCancelFragment nvofrag = new ListaCancelFragment();
               // nvofrag.setArguments(args);

                //ft.add(R.id.continf_fragment, nvofrag);
                NavHostFragment.findNavController(this).navigate(R.id.action_notiftocan, bundle);
                break;

            case 3:
                NavHostFragment.findNavController(this).navigate(R.id.action_notiftomu, bundle);
                break;

        }

    }


    private  void convertirListaCor() {
        listaClientesEnv = new ArrayList<DescripcionGenerica>();
        //primero las generales

       // int pendientes=contarCorreccGen(detalle.getCiudadNombre());
        DescripcionGenerica gen = new DescripcionGenerica(1,"CORRECCIONES","gen","1");
                listaClientesEnv.add(gen);

      listaClientesEnv.add(new DescripcionGenerica(2, "CANCELADAS", "0","1"));

        listaClientesEnv.add(new DescripcionGenerica(3, "MUESTRA ADICIONAL", "0","1"));


    }

/*
    @Override
    public void onClickVer(int position) {
        siguiente(position);
    }*/
}