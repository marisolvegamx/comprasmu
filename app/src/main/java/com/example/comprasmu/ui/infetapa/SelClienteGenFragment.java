package com.example.comprasmu.ui.infetapa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.comprasmu.ui.BackActivity;
import com.example.comprasmu.ui.informe.BuscarInformeFragment;
import com.example.comprasmu.ui.informedetalle.DetalleProductoFragment;
import com.example.comprasmu.ui.listadetalle.ListaCompraFragment;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.ui.ListaSelecFragment;

import java.util.ArrayList;
import java.util.List;

/*** para buscar las listas por planta no x cliente***/

public class SelClienteGenFragment extends ListaSelecFragment {

    private  ArrayList<DescripcionGenerica> listaClientesEnv;
    ListaCompraRepositoryImpl lcrepo;
    private static String TAG="SelClienteGenFragment";
    private ListaDetalleViewModel mViewModel;
    public static String ARG_TIPOCONS="comprasmu.correselcli.tipocons";
    int ciudadSel;
    String ciudadNombre;
    String tipoconsulta;

    int clienteSel;
    LiveData<List<ListaCompra>> listacomp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //busco los datos y los convierto al tipo String[]
        super.onCreate(savedInstanceState);
        ListaCompraDao dao=ComprasDataBase.getInstance(getContext()).getListaCompraDao();
        lcrepo = ListaCompraRepositoryImpl.getInstance(dao);
        mViewModel=new ViewModelProvider(this).get(ListaDetalleViewModel.class);
        Bundle bundle = getArguments();
        tipoconsulta="";
        ciudadSel =  Constantes.IDCIUDADTRABAJO;
        ciudadNombre =  Constantes.CIUDADTRABAJO;
        if(bundle!=null)
            tipoconsulta=bundle.getString(ARG_TIPOCONS);
            //busco ciudad seleccionada
        mViewModel.ciudadSel = ciudadSel;
        mViewModel.nombreCiudadSel = ciudadNombre;
        Log.d(TAG,"una cd sel  "+ciudadSel+"---");


        listacomp= mViewModel.cargarPestañas(ciudadNombre,clienteSel);



    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        /*aqui me dice que dato es*/
        setIndicacion(getString(R.string.seleccione_planta));
        // Log.d(TAG,"indice..............."+Constantes.INDICEACTUAL);
        // Create the observer which updates the UI.
        final Observer< List<ListaCompra>> nameObserver = new Observer< List<ListaCompra>>() {
            @Override
            public void onChanged(@Nullable List<ListaCompra> lista) {

                convertirLista(lista);
                setLista(listaClientesEnv);
                // siguiente(0);
                //  Log.d(TAG,"------- "+lista.size());
                if(lista.size()>1) {

                    setupListAdapter();
                }else if(lista.size()>0)
                {
                    //voy directo a la lista
                    siguiente(0);
                }
                else
                    Log.d(TAG,"algo salió mal con la consulta de listas");
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        //   lcrepo.getClientesByIndiceCiudad(Constantes.INDICEACTUAL,ciudadNombre).observe(getViewLifecycleOwner(), nameObserver);
        listacomp.observe(getViewLifecycleOwner(),nameObserver);

        getObjetosLV().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //  Toast.makeText(getContext(),listaSeleccionable.get(i).getId()+"", Toast.LENGTH_LONG).show();
                siguiente(i);

            }
        });

    }


    public void siguiente(int i){
        Log.d(TAG,"una planta "+tipoconsulta+"--"+listaSeleccionable.get(i).getId());

        Bundle bundle = new Bundle();
        bundle.putInt(ListaCompraFragment.ARG_PLANTASEL,listaSeleccionable.get(i).getId() );
        bundle.putString(ListaCompraFragment.ARG_NOMBREPLANTASEL, listaSeleccionable.get(i).getNombre());
        bundle.putInt(ListaCompraFragment.ARG_CLIENTESEL,clienteSel );
      //  bundle.putString(BuscarInformeFragment.INDICE, Constantes.INDICEACTUAL);

        if(tipoconsulta.equals("action_selclitoinf1")) {
            bundle.putInt(ContInfEtapaFragment.ETAPA, 1);
            bundle.putString(ARG_TIPOCONS,"e" );

            NavHostFragment.findNavController(this).navigate(R.id.action_selclitoinf1,bundle);


        }if(tipoconsulta.equals("c")) {
            bundle.putString(ARG_TIPOCONS,"action_selclitosolcor2" );
            NavHostFragment.findNavController(this).navigate(R.id.action_selclitosolcor2,bundle);

        }
            if(tipoconsulta.equals("rescor")) {
                bundle.putString(ARG_TIPOCONS,"action_selclitocor2" );
                NavHostFragment.findNavController(this).navigate(R.id.action_selclitocor2,bundle);

            }


    }

    private  void convertirLista(List<ListaCompra>lista) {
        listaClientesEnv = new ArrayList<DescripcionGenerica>();
        for (ListaCompra listaCompra : lista) {
          /*String tupla=Integer.toString(listaCompra.getClienteId())+";"+
          listaCompra.getPlantaNombre();*/

            listaClientesEnv.add(new DescripcionGenerica(listaCompra.getPlantasId(), listaCompra.getClienteNombre() + " " + listaCompra.getPlantaNombre(), listaCompra.getClienteNombre()));

        }
    }








}