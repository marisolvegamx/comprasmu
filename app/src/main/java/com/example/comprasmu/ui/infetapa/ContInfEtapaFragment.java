package com.example.comprasmu.ui.infetapa;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.databinding.ListaInformesFragmentBinding;

import com.example.comprasmu.ui.preparacion.NvaPreparacionViewModel;
import com.example.comprasmu.utils.Constantes;


import java.util.List;

public class ContInfEtapaFragment extends Fragment implements ContInfEtaAdapter.AdapterCallback {

    private static final String TAG = "ContInfEtapaFragment";
    public static final String ETAPA ="comprasmu.cie.etapasel" ;

    private ContInfEtaViewModel mViewModel;
    private String indice;
    int clientesel;
    int plantasel,etapa;
    private NvaPreparacionViewModel nViewModel;
    private ContInfEtaAdapter mListAdapter;
    LiveData<List<InformeEtapa>> informesEtapa;
   ListaInformesFragmentBinding mBinding;
    public static ContInfEtapaFragment newInstance() {
        return new ContInfEtapaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
         mBinding = DataBindingUtil.inflate(inflater,
                R.layout.lista_informes_fragment, container, false);

        mViewModel = new ViewModelProvider(this).get(ContInfEtaViewModel.class);
        nViewModel = new ViewModelProvider(this).get(NvaPreparacionViewModel.class);

        setHasOptionsMenu(true);
        if (getArguments() != null) {
           // clientesel = getArguments().getInt(BuscarInformeFragment.ARG_CLIENTE);
           // plantasel=getArguments().getInt(ListaCompraFragment.ARG_PLANTASEL);
            //  ciudad=getArguments().getString(BuscarInformeFragment.CIUDAD);
           // tienda=getArguments().getString(BuscarInformeFragment.NOMBRETIENDA);
            etapa=getArguments().getInt(ETAPA);

            indice = Constantes.INDICEACTUAL;
            //busco el ultimo informe para continuar
          informesEtapa = mViewModel.getInformesPend(Constantes.INDICEACTUAL,etapa);

        }

        //   Log.d(Constantes.TAG,"cliente y planta sel"+clienteid+"--"+plantaid);


        return    mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        setupContListAdapter();
       // setupSnackbar();
        cargarListaCont();
    }
    public void cargarListaCont(){

        informesEtapa.observe(getViewLifecycleOwner(), new Observer<List<InformeEtapa>>() {
            @Override
            public void onChanged(List<InformeEtapa> informeEtapas) {
                if(informeEtapas.size()>0) {
                    mBinding.emptyStateText.setVisibility(View.GONE);

                }
                else{
                    mBinding.emptyStateText.setVisibility(View.VISIBLE);
                }
                mListAdapter.setInformeCompraList(informeEtapas);
                mListAdapter.notifyDataSetChanged();
            }
        });


    }
    private void setupContListAdapter() {
        mListAdapter = new ContInfEtaAdapter(this);
        mBinding.detalleList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.detalleList.setHasFixedSize(true);
        mBinding.detalleList.setAdapter(mListAdapter);

    }

    @Override
    public void onClickContinuar(int informe) {


        Intent intento1 = new Intent(getActivity(), NuevoInfEtapaActivity.class);
        intento1.putExtra(NuevoInfEtapaActivity.INFORMESEL,informe );
        intento1.putExtra(ETAPA,etapa );
        startActivity(intento1);

        //  NavHostFragment.findNavController(this).navigate(R.id.nav_nvoprep,bundle);

    }

    @Override
    public void onClickEliminar(int idinf) {
        try{
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
            dialogo1.setTitle(R.string.importante);
            dialogo1.setMessage(R.string.pregunta_eliminar_mensaje);
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    eliminar(idinf);


                }
            });
            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    dialogo1.cancel();
                }
            });
            dialogo1.show();

        }
        catch (Exception ex){
            Log.e(TAG,"Error"+ ex.getMessage());
            Toast.makeText(getActivity(), "Hubo un error al eliminar", Toast.LENGTH_LONG).show();

        }

    }

    private void eliminar(int id) {
        Log.e(TAG,"eliminando "+ id);
        mViewModel.eliminarInformeEta(id);
      //  mListAdapter.setInformeCompraList(null);
        //mListAdapter.notifyDataSetChanged();
    }

}