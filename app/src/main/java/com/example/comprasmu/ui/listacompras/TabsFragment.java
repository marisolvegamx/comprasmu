package com.example.comprasmu.ui.listacompras;

import android.os.Bundle;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class TabsFragment extends Fragment {
    private static final String TAG = "TabsFragment";
    TabLayout tabs;
    ViewPager viewPager;
    private ListaDetalleViewModel mViewModel;
    String[] clientes;
    String[][] clientesplan;
    String ciudadSel;
    String ciudadNombre;
    String clienteSel;
    String clienteNombre;

    public static final String ARG_CLIENTESEL="comprasmu.lista.clienteSel";
    public static final String ARG_CLIENTENOMBRE="comprasmu.lista.clientenombre";
    public static final String ARG_MUESTRA="comprasmu.lista.agregarmuestra";//indica si se agregará muestra


    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lista_tabs, container, false);


        mViewModel = new ViewModelProvider(requireActivity()).get(com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel.class);


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

        Bundle bundle = getArguments();
        if (bundle != null) {
             ciudadSel = bundle.getInt("ciudadSel")+"";
             ciudadNombre  = bundle.getString("ciudadNombre");
            clienteSel = bundle.getInt(ARG_CLIENTESEL)+"";
            clienteNombre  = bundle.getString(ARG_CLIENTENOMBRE);
            //busco ciudad seleccionada
            mViewModel.ciudadSel=Integer.parseInt(ciudadSel);
            mViewModel.nombreCiudadSel=ciudadNombre;
            Log.d(Constantes.TAG,"arg_muestra"+bundle.getString(ARG_MUESTRA));

            if(bundle.getString(ARG_MUESTRA)!=null&&bundle.getString(ARG_MUESTRA).equals("true")) {
            //vengo de agregar muestra
                mViewModel.setClienteSel(Integer.parseInt(clienteSel));
                mViewModel.setNuevaMuestra(true);

            }

            Constantes.CIUDADSEL=Integer.parseInt(ciudadSel);
        }
        Log.d(Constantes.TAG,"cidad sel"+ciudadSel+"--"+ciudadNombre);
        viewPager = view.findViewById(R.id.view_pager);

        tabs = view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        //busco los clientes y las plantas

        mViewModel.cargarPestañas(ciudadNombre).observe(getViewLifecycleOwner(), words -> {
            Log.d(TAG,"Cargando pestañas "+Constantes.INDICEACTUAL+"--"+ciudadSel);
            convertirLista(words);
            configureTabLayout();

        });

    }

    private void configureTabLayout() {



    /*    tabs.addTab(tabs.newTab().setText("Tab 1 Item"));
        tabs.addTab(tabs.newTab().setText("Tab 2 Item"));
        tabs.addTab(tabs.newTab().setText("Tab 3 Item"));
        tabs.addTab(tabs.newTab().setText("Tab 4 Item"));*/
        //clientes2={"Cliente1","cliente2","cliente3"};
       // getFragmentManager()
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(   getChildFragmentManager(),clientes, mViewModel,clientesplan);
        viewPager.setAdapter(sectionsPagerAdapter);
        Log.d("clientes array", viewPager.getCurrentItem()+"--"+sectionsPagerAdapter.getCount());
        if(viewPager.getCurrentItem()==0&&sectionsPagerAdapter.getCount()>0){
            sectionsPagerAdapter.getItem(0);
        }

        viewPager.addOnPageChangeListener(new
                TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new
               TabLayout.OnTabSelectedListener() {
               @Override
               public void onTabSelected(TabLayout.Tab tab) {

                  mViewModel.setPlantaSel(Integer.parseInt(clientesplan[tab.getPosition()][0]));
                 //  mViewModel.setPlantaSel(tab.getPosition());
                   mViewModel.nombrePlantaSel=clientesplan[tab.getPosition()][1];

                   viewPager.setCurrentItem(tab.getPosition());
               }

               @Override
               public void onTabUnselected(TabLayout.Tab tab) {

               }

               @Override
               public void onTabReselected(TabLayout.Tab tab) {

               }

           });
    }
/****lista de clientes y plantas****/
    private  void convertirLista(List<ListaCompra> lista){

        int i=0;
        clientesplan=new String[lista.size()][2];

        clientes=new String[lista.size()];
        for (ListaCompra listaCompra: lista ) {
            clientesplan[i][0]=listaCompra.getPlantasId()+"";
            clientesplan[i][1]= listaCompra.getClienteNombre()+" "+listaCompra.getPlantaNombre();

            clientes[i]= listaCompra.getClienteNombre()+" "+listaCompra.getPlantaNombre();
           // Log.d(TAG,"convertir lista>"+clientes[i]);
            i++;

        }
    }


}