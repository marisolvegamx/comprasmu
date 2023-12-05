package com.example.comprasmu.ui.solcorreccion;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.comprasmu.ui.infetapa.ContInfEtapaFragment;
import com.example.comprasmu.ui.listadetalle.ListaCompraFragment;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.ui.solcorreccion.ListaSolsViewModel;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.ui.ListaSelecFragment;

import java.util.ArrayList;
import java.util.List;

/*** mostrará todas las plantas que tenga el recolector sin importar la ciudad de trabajo***/

public class SelPlantaFragment extends ListaSelecFragment {

    private  ArrayList<DescripcionGenerica> listaClientesEnv;
    ListaCompraRepositoryImpl lcrepo;
    private static final String TAG="SelPlantaFragment";
    private ListaDetalleViewModel mViewModel;
    public static String ARG_TIPOCONS="comprasmu.correselcli.tipocons";
    String tipoconsulta;

    LiveData<List<ListaCompra>> listacomp;
    private ListaSolsViewModel scViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //busco los datos y los convierto al tipo String[]
        super.onCreate(savedInstanceState);
        ListaCompraDao dao=ComprasDataBase.getInstance(getContext()).getListaCompraDao();
        lcrepo = ListaCompraRepositoryImpl.getInstance(dao);
        mViewModel=new ViewModelProvider(this).get(ListaDetalleViewModel.class);
        scViewModel = new ViewModelProvider(this).get(ListaSolsViewModel.class);


        tipoconsulta="";

        listacomp= mViewModel.cargarClientePlanta();

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
                // siguiente(0);
                  Log.d(TAG,"------- "+lista.size());
                if(lista.size()>1) {

                    //voy a buscar la solicitudes pendientes
                    convertirListaCor(lista);
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.menu_ver_listasolcor);

                    setLista(listaClientesEnv);
                    setupListAdapter();


                    adaptadorLista.setDesc2(true);

                }else if(lista.size()>0)
                { //solo hay una planta
                    convertirListaCor(lista);


                    setLista(listaClientesEnv);
                    //voy directo a la lista
                    siguiente(0);
                }
                else
                  Toast.makeText(getContext(),"Hubo un error al buscar las listas de compra, verifique o vuelva a actualizar", Toast.LENGTH_LONG).show();

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

    private int contarCorrecc(int plantaId){

       int  pendientes=scViewModel.getTotalSolsxplanta(Constantes.ETAPAACTUAL,Constantes.INDICEACTUAL,1, plantaId);
        return pendientes;
    }
    public void siguiente(int i){
        Log.d(TAG,"una planta "+tipoconsulta+"--"+listaSeleccionable.get(i).getId());

        Bundle bundle = new Bundle();
        bundle.putInt(ListaCompraFragment.ARG_PLANTASEL,listaSeleccionable.get(i).getId() );
        bundle.putString(ListaCompraFragment.ARG_NOMBREPLANTASEL, listaSeleccionable.get(i).getDescripcion2());


        bundle.putString(ARG_TIPOCONS,"action_selclitosolcor2" );
        NavHostFragment.findNavController(this).navigate(R.id.action_selclitosolcor2,bundle);



    }


    private  void convertirListaCor(List<ListaCompra>lista) {
        listaClientesEnv = new ArrayList<DescripcionGenerica>();
        for (ListaCompra listaCompra : lista) {
          /*String tupla=Integer.toString(listaCompra.getClienteId())+";"+
          listaCompra.getPlantaNombre();*/
            int pendientes=0;

                //voy a buscar la solicitudes pendientes
            pendientes=contarCorrecc(listaCompra.getPlantasId());

            Log.d(TAG,pendientes+"--"+listaCompra.getPlantasId());
            listaClientesEnv.add(new DescripcionGenerica(listaCompra.getPlantasId(), listaCompra.getClienteNombre() + " " + listaCompra.getPlantaNombre(), listaCompra.getClienteNombre(),pendientes+""));

        }
    }






}