package com.example.comprasmu.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.comprasmu.R;
import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.ListaCompraDao;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.utils.ui.ListaSelecFragment;
import com.example.comprasmu.utils.Constantes;

import java.util.ArrayList;
import java.util.List;

/*** para buscar las listas por planta***/

public class SelPlantaFragment extends ListaSelecFragment {
    private LiveData<List<ListaCompra>> listaCiudades;
    private static ArrayList<DescripcionGenerica> listaCiudadesEnv;
    ListaCompraRepositoryImpl lcrepo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //busco los datos y los convierto al tipo String[]
        super.onCreate(savedInstanceState);
        ListaCompraDao dao=ComprasDataBase.getInstance(getContext()).getListaCompraDao();
         lcrepo = ListaCompraRepositoryImpl.getInstance(dao);


    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        /*aqui me dice que dato es*/
       /*   Bundle bundle = getArguments();
        if (bundle != null) {
           this.listaSeleccionable= bundle.getStringArrayList("listaSel");

        }
*/
        // Create the observer which updates the UI.
        final Observer< List<ListaCompra>> nameObserver = new Observer< List<ListaCompra>>() {
            @Override
            public void onChanged(@Nullable List<ListaCompra> lista) {
                Log.d(Constantes.TAG, "YA cargo la lista " + lista.size());
                SelPlantaFragment.convertirLista(lista);
                setLista(listaCiudadesEnv);
                if(lista.size()>1) {

                    // Update the UI, in this case, a TextView.

                    setupListAdapter();
                }else
                { //voy directo a la lista
                    siguiente(0);
                }
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        lcrepo.getCiudades(Constantes.INDICEACTUAL).observe(getViewLifecycleOwner(), nameObserver);
         /*  SelectAsyncTask selec=new SelectAsyncTask(lcrepo);
        selec.execute();*/
    /*    lcrepo.getCiudades(Constantes.INDICEACTUAL).observe(getViewLifecycleOwner(), new Observer<List<ListaCompra>>() {
            @Override
            public void onChanged(@Nullable List<ListaCompra> lista) {
                if (lista != null) {
                    SelPlantaFragment.convertirLista(lista);


                }

            }
        });*/
        getObjetosLV().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //  Toast.makeText(getContext(),listaSeleccionable.get(i).getId()+"", Toast.LENGTH_LONG).show();
                siguiente(i);
                //Abrir siguiente Activity

             /*   Intent intent = new Intent(getActivity(), ListaDetalleActivity.class);
                intent.putExtra("plantaSel",i);
                intent.putExtra("plantaNombre",listaSeleccionable.get(i));

                startActivity(intent);
*/
            }
        });

    }

    public void siguiente(int i){
        Bundle bundle = new Bundle();
        bundle.putInt("ciudadSel",listaSeleccionable.get(i).getId() );
        bundle.putString("ciudadNombre", listaSeleccionable.get(i).getNombre());
        NavHostFragment.findNavController(this).navigate(R.id.action_selclientetolistacompras,bundle);

    }

    private static void convertirLista(List<ListaCompra>lista){
        listaCiudadesEnv=new ArrayList<DescripcionGenerica>();
        for (ListaCompra listaCompra: lista ) {
          /*String tupla=Integer.toString(listaCompra.getCiudadesId())+";"+
          listaCompra.getPlantaNombre();*/

          listaCiudadesEnv.add(new DescripcionGenerica(listaCompra.getCiudadesId(), listaCompra.getCiudadNombre()));

        }

    }

    static class SelectAsyncTask extends AsyncTask<Void, Void, LiveData<List<ListaCompra>>> {

        private ListaCompraRepositoryImpl lcrepo;

        public SelectAsyncTask(ListaCompraRepositoryImpl lcrepo) {
            this.lcrepo = lcrepo;
        }


        @Override
        protected  LiveData<List<ListaCompra>> doInBackground(Void... voids) {

            LiveData<List<ListaCompra>> listaCiudades= lcrepo.getCiudades(Constantes.INDICEACTUAL);
            return listaCiudades ;
        }
    }




}