package com.example.comprasmu.ui.sustitucion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.comprasmu.R;

import com.example.comprasmu.data.modelos.Sustitucion;

import com.example.comprasmu.databinding.ListaGenericFragmentBinding;
import com.example.comprasmu.ui.informedetalle.DetalleProductoFragment;
import com.example.comprasmu.ui.informedetalle.NuevoDetalleViewModel;
import com.example.comprasmu.ui.listacompras.PlaceholderFragment;
import com.example.comprasmu.ui.mantenimiento.CiudadTrabajoFragment;
import com.example.comprasmu.utils.Constantes;


public class SustitucionFragment extends Fragment implements SustitucionAdapter.AdapterCallback {


    public static final String ISBACKUP = "comprasmu.sustitucion.isbackup";
    public static final String ARG_CATEGORIA = "comprasmu.sustitucion.categoria";


    private SustitucionViewModel mViewModel;
    private ListaGenericFragmentBinding mBinding;
    private SustitucionAdapter mListAdapter;
     String nombrePlanta;
     int plantaSel;

    private boolean isbu, ismuestra; //para saber si es reemplazo
    private static final String ARG_PLANTA = "comprasmu.sustitucion.planta";
    private static final String ARG_NOMBREPLANTA = "comprasmu.sustitucion.nomplanta";

    private static final String TAG="SUSTITUCIONFRAGMENT";
    private String categoriaSel;


    public static CiudadTrabajoFragment newInstance(int planta,String onombrePlanta) {
        //  ListaCompraFragment fragment = new ListaCompraFragment();

        CiudadTrabajoFragment fragment = new CiudadTrabajoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PLANTA, planta);
        bundle.putString(ARG_NOMBREPLANTA, onombrePlanta);
        fragment.setArguments(bundle);
        return fragment;

    }


    public SustitucionFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding= DataBindingUtil.inflate(inflater,
                R.layout.lista_generic_fragment, container, false);

       // mViewModel = new ViewModelProvider(this).get(com.example.comprasmu.ui.Sustitucion.SustitucionViewModel.class);
        mViewModel=new ViewModelProvider(requireActivity()).get(SustitucionViewModel.class);

        return    mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    //    Log.d("aquiiiiii","********"+mViewModel.isNuevaMuestra());
        Bundle bundle2 =getActivity().getIntent().getExtras();

        if(bundle2!=null)
         categoriaSel=bundle2.getString(ARG_CATEGORIA);
        if (getArguments() != null) {
            plantaSel = getArguments().getInt(ARG_PLANTA);
            nombrePlanta = getArguments().getString(ARG_NOMBREPLANTA);
        }
        mBinding.setLifecycleOwner(this);
        setupListAdapter();

        mViewModel.cargarListas(categoriaSel);

        mViewModel.getListas().observe(getViewLifecycleOwner(), myProducts -> {
            if (myProducts != null && myProducts.size() > 0) {

                    Log.d(Constantes.TAG, "en la consulta de sust=> " + myProducts.get(0).getId_sustitucion());
                    // mBinding.setIsLoading(false);

                    mListAdapter.setSustitucionList(myProducts);
                    mListAdapter.notifyDataSetChanged();

                }

            });


    }


    @Override
    public void onDestroyView() {
        mBinding = null;
        mListAdapter = null;
        super.onDestroyView();
    }
    private void setupListAdapter() {
       mListAdapter = new SustitucionAdapter(this);

        mBinding.detalleList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.detalleList.setHasFixedSize(true);
        mBinding.detalleList.setAdapter(mListAdapter);

    }

    @Override
    public void agregarMuestra(View view, Sustitucion productoSel) {

        //cambio al fragmento de captura del detalle
        if (view.getId() == R.id.btnldagregar) {
           Log.d(TAG, "agregar muestra"+productoSel.getNomproducto());

            NuevoDetalleViewModel nuevoInf=new ViewModelProvider(requireActivity()).get(NuevoDetalleViewModel.class);
                String clienteNombre=Constantes.ni_clientesel;//lo pongo hasta que se guarda el informe
            //para los bu

            nuevoInf.setProductoSel(productoSel,nombrePlanta,plantaSel, 2,"PEñAFIEL","");
            Constantes.productoSel=nuevoInf.productoSel;
            Constantes.NM_TOTALISTA=mListAdapter.getItemCount();
    /*        Fragment fragment = new DetalleProductoFragment1();
// Obtener el administrador de fragmentos a través de la actividad
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
// Definir una transacción
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
// Remplazar el contenido principal por el fragmento
            fragmentTransaction.replace(R.id.back_fragment, fragment);
            fragmentTransaction.addToBackStack(null);
// Cambiar
            fragmentTransaction.commit();
          //  fragmentManager.beginTransaction().remove(this).commitAllowingStateLoss();
*/
            Constantes.NM_TOTALISTA=mListAdapter.getItemCount();
            Intent resultIntent = new Intent();

           // resultIntent.putExtra(DetalleProductoFragment.ARG_NUEVOINFORME, mViewModel.informe.getId());
            getActivity().setResult(DetalleProductoFragment.NUEVO_RESULT_OK, resultIntent);

            //regreso al informe
            getActivity().finish();
        }
    }


}