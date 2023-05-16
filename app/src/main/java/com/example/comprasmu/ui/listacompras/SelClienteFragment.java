package com.example.comprasmu.ui.listacompras;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import com.example.comprasmu.ui.sustitucion.SustitucionFragment;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.ui.ListaSelecFragment;

import java.util.ArrayList;
import java.util.List;

import static com.example.comprasmu.ui.listacompras.TabsFragment.ARG_CLIENTESEL;

/*** para buscar las listas por planta no x cliente***/

public class SelClienteFragment extends ListaSelecFragment {
    private LiveData<List<ListaCompra>> listaclientes;
    private  ArrayList<DescripcionGenerica> listaClientesEnv;
    ListaCompraRepositoryImpl lcrepo;
    private static final String TAG="SelClienteFragment";
    private ListaDetalleViewModel mViewModel;
    public static String ARG_TIPOCONS="comprasmu.selcli.tipocons";
    int ciudadSel;
    String ciudadNombre;
    String tipoconsulta;
    int numMuestra;
    String isMuestra;
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
        if (bundle != null) {
            ciudadSel = bundle.getInt("ciudadSel");
            ciudadNombre = bundle.getString("ciudadNombre");
            tipoconsulta=bundle.getString(ARG_TIPOCONS);
            //busco ciudad seleccionada
            mViewModel.ciudadSel = ciudadSel;
            mViewModel.nombreCiudadSel = ciudadNombre;
            numMuestra=bundle.getInt(DetalleProductoFragment.NUMMUESTRA);
            isMuestra=bundle.getString(ListaCompraFragment.ARG_MUESTRA);
            clienteSel=bundle.getInt(ListaCompraFragment.ARG_CLIENTESEL);
            Constantes.CIUDADSEL=ciudadSel;
            Log.d(TAG,"una cd sel  "+ciudadSel+"---"+isMuestra);

        }
        listacomp= mViewModel.cargarPestañas(ciudadNombre,clienteSel);

        Log.d(TAG,"otra vezzzzzzz");

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

                    if (lista.size() > 1) {

                        setupListAdapter();
                    } else if (lista.size() > 0) {
                        //voy directo a la lista
                        siguiente(0);
                    } else
                        Log.d(TAG, "algo salió mal con la consulta de listas");

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
        Log.d(TAG,"otra vezzzzzzz*******");
    }


    public void siguiente(int i){
        Log.d(TAG,"una planta "+listaSeleccionable.get(i).getId()+"--"+listaSeleccionable.get(i).getNombre());


        if(clienteSel==0){
            try {
                clienteSel = Integer.parseInt(listaSeleccionable.get(i).getDescripcion3());
            }catch (Exception ex){
                ex.printStackTrace();
                ComprasLog.getSingleton().grabarError(TAG+" No había cliente "+ex.getMessage());
            }
        }
        Bundle bundle = new Bundle();
        bundle.putInt(ListaCompraFragment.ARG_PLANTASEL,listaSeleccionable.get(i).getId() );
        bundle.putString(ListaCompraFragment.ARG_NOMBREPLANTASEL, listaSeleccionable.get(i).getDescripcion2());
        bundle.putString(SelClienteFragment.ARG_TIPOCONS, tipoconsulta);
        bundle.putInt(DetalleProductoFragment.NUMMUESTRA,numMuestra);
       bundle.putString(ListaCompraFragment.ARG_MUESTRA, isMuestra);
        bundle.putInt(ListaCompraFragment.ARG_CLIENTESEL,clienteSel );
   //     bundle.putString(ListaCompraFragment.ARG_CLIENTESEL, isMuestra);
        if(isMuestra!=null&&isMuestra.equals("true")){ //estoy en informe
            ListaCompraFragment detailFragment = new ListaCompraFragment();
            //  SelClienteFragment detailFragment = new SelClienteFragment();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();



            detailFragment.setArguments(bundle);
            ft.replace(R.id.back_fragment, detailFragment);
            ft.commit();
        }else

        //   bundle.putInt(BuscarInformeFragment.ARG_CLIENTE,listaSeleccionable.get(i).getId() );
      //  bundle.putString(ListaCompraFragment.ARG_NOMBREPLANTASEL, listaSeleccionable.get(i).getNombre());
        if(tipoconsulta.equals("action_selclitolista")) {
            if(i==0) //en automatico lo envié lo puedo cargar en la misma actividad
            {
                NavHostFragment.findNavController(this).navigate(R.id.action_selclitolista,bundle);


            }else {
                //voy auna nueva actividad
                Intent intento1 = new Intent(getActivity(), BackActivity.class);
                intento1.putExtra(BackActivity.ARG_FRAGMENT, BackActivity.OP_LISTACOMPRA);
                intento1.putExtra(ListaCompraFragment.ARG_PLANTASEL, listaSeleccionable.get(i).getId());
                intento1.putExtra(ListaCompraFragment.ARG_NOMBREPLANTASEL, listaSeleccionable.get(i).getDescripcion2());
                intento1.putExtra(SelClienteFragment.ARG_TIPOCONS, tipoconsulta);
                intento1.putExtra(DetalleProductoFragment.NUMMUESTRA, numMuestra);
                intento1.putExtra(ListaCompraFragment.ARG_MUESTRA, isMuestra);
                intento1.putExtra(ListaCompraFragment.ARG_CLIENTESEL, clienteSel);
                startActivity(intento1);
            }
         //   NavHostFragment.findNavController(this).navigate(R.id.action_selclitolista, bundle);


        }
        if(tipoconsulta.equals("action_selclitoinformes"))
            NavHostFragment.findNavController(this).navigate(R.id.action_selclitoinformes,bundle);


     //envio al fragment directo
    }

    private  void convertirLista(List<ListaCompra>lista){
        listaClientesEnv=new ArrayList<DescripcionGenerica>();
        for (ListaCompra listaCompra: lista ) {
          /*String tupla=Integer.toString(listaCompra.getClienteId())+";"+
          listaCompra.getPlantaNombre();*/

          listaClientesEnv.add(new DescripcionGenerica(listaCompra.getPlantasId(), listaCompra.getClienteNombre()+" "+listaCompra.getPlantaNombre(),listaCompra.getClienteNombre(),listaCompra.getPlantaNombre(),listaCompra.getClientesId()+""));

        }

    }





}