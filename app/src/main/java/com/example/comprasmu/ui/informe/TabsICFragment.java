package com.example.comprasmu.ui.informe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.ui.listacompras.SectionsPagerAdapter;
import com.example.comprasmu.ui.listadetalle.ListaCompraFragment;

import com.example.comprasmu.utils.Constantes;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class TabsICFragment extends Fragment {
    private static final String TAG = "TabsFragment";
    TabLayout tabs;
    ViewPager viewPager;
    private ListaInformesViewModel mViewModel;
    String[] clientes;
    String[][] clientesplan; //idplanta, nombreplanta, nombrecliente, siglas
    String planta;
    String tienda;
    int clienteSel;
    String clienteNombre;
    String indiceSel;

    public static final String ARG_TIENDA="comprasmu.linf.tienda";
    public static final String ARG_INDICE="comprasmu.linf.indice";

    public static final String ARG_CLIENTESEL="comprasmu.linf.clienteSel";
    public static final String ARG_CLIENTENOMBRE="comprasmu.linf.clientenombre";
    public static final String ARG_PLANTASEL="comprasmu.linf.plantaSel";//indica si se agregará muestra


    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lista_tabs, container, false);


        mViewModel = new ViewModelProvider(requireActivity()).get(ListaInformesViewModel.class);


    /*  FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        if(fab!=null)
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        indiceSel=Constantes.INDICEACTUAL;
        Bundle bundle = getArguments();
        if (bundle != null) {
             planta= bundle.getString(ARG_PLANTASEL);
            tienda  = bundle.getString(ARG_TIENDA);
            clienteSel = bundle.getInt(ARG_CLIENTESEL);
            clienteNombre  = bundle.getString(ARG_CLIENTENOMBRE);
            //busco ciudad seleccionada
          //  mViewModel.ciudadSel=Integer.parseInt(ciudadSel);
          //  mViewModel.nombreCiudadSel=ciudadNombre;
         //   Log.d(Constantes.TAG,"arg_muestra"+bundle.getString(ARG_MUESTRA));

        }
      //  Log.d(Constantes.TAG,"cidad sel"+ciudadSel+"--"+ciudadNombre);
        viewPager = view.findViewById(R.id.view_pager);

        tabs = view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        //busco los clientes y las plantas

        mViewModel.cargarPestañas(indiceSel,tienda,"",planta,clienteSel).observe(getViewLifecycleOwner(), words -> {
       //     Log.d(TAG,"Cargando pestañas "+words.size());
            convertirLista(words);
            configureTabLayout();

        });

    }

    private void configureTabLayout() {
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(   getChildFragmentManager(),clientes, null);
        for(int pos=0;pos<clientesplan.length;pos++) {
            Fragment fragment = new ListaInformesFragment(Integer.parseInt(clientesplan[pos][0]), clientesplan[pos][1],clientesplan[pos][2]);

            sectionsPagerAdapter.addFragment(fragment, clientesplan[pos][2]+" "+clientesplan[pos][1]);
        }
        viewPager.setAdapter(sectionsPagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

    }
/****lista de clientes y plantas****/
    private  void convertirLista(List<InformeCompra> lista){

        int i=0;
        clientesplan=new String[lista.size()][3];

        clientes=new String[lista.size()];
        for (InformeCompra listaCompra: lista ) {
            clientesplan[i][0]=listaCompra.getPlantasId()+"";

            clientesplan[i][1]= listaCompra.getPlantaNombre();
            clientesplan[i][2]= listaCompra.getClienteNombre();
       //     clientesplan[i][3]= listaCompra.getSiglas();

            clientes[i]= listaCompra.getClienteNombre()+" "+listaCompra.getPlantaNombre();
           // Log.d(TAG,"convertir lista>"+clientes[i]);
            i++;

        }
    }


}