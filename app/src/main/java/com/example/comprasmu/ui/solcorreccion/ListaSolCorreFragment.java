package com.example.comprasmu.ui.solcorreccion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.SolicitudCor;
import com.example.comprasmu.databinding.ListaInformesFragmentBinding;
import com.example.comprasmu.ui.infetapa.NuevoInfEtapaActivity;
import com.example.comprasmu.ui.listadetalle.ListaCompraFragment;
import com.example.comprasmu.utils.Constantes;

import java.util.List;


public class ListaSolCorreFragment extends Fragment implements SolCorreAdapter.AdapterCallback {

    private ListaSolsViewModel mViewModel;

    public static final String TAG = "ListaInformesFragment";
    private ListaInformesFragmentBinding mBinding;
    private SolCorreAdapter mListAdapter;
    private String indice;
    int plantasel;
    CoordinatorLayout coordinator;

    public ListaSolCorreFragment() {

    }

  /*  public  ListaInformesFragment(int planta, String onombrePlanta, String nomcliente) {
        //  ListaCompraFragment fragment = new ListaCompraFragment();
        plantaid=planta;
        plantasel=onombrePlanta;
        this.clientesel=nomcliente;

    }*/

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (getArguments() != null) {

            plantasel=getArguments().getInt(ListaCompraFragment.ARG_PLANTASEL);
           // indice=getArguments().getString(BuscarInformeFragment.INDICE);

        }
            indice = Constantes.INDICEACTUAL;



     //

        mBinding= DataBindingUtil.inflate(inflater,
                R.layout.lista_informes_fragment, container, false);

        mViewModel = new ViewModelProvider(this).get(ListaSolsViewModel.class);
        setHasOptionsMenu(true);
        return    mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        coordinator=view.findViewById(R.id.coordinator3);

        setupListAdapter();

       cargarLista();
    }

    public void cargarLista(){
        Log.d(Constantes.TAG,"etapa y planta sel"+Constantes.ETAPAACTUAL+"--"+plantasel);
    mViewModel.cargarDetalles(Constantes.ETAPAACTUAL,indice,plantasel,1).observe(getViewLifecycleOwner(), new Observer<List<SolicitudCor>>() {
        @Override
        public void onChanged(List<SolicitudCor> solicitudCors) {
           // Log.d(Constantes.TAG,"llego algo"+solicitudCors.get(0).getIndice());

            mListAdapter.setSolicitudCorList(solicitudCors);
            mListAdapter.notifyDataSetChanged();
            if(solicitudCors!=null&&solicitudCors.size()>0)
                mBinding.emptyStateText.setVisibility(View.INVISIBLE);
            else
                mBinding.emptyStateText.setVisibility(View.VISIBLE);
        }
    });



    }
    private void setupListAdapter() {
        mListAdapter = new SolCorreAdapter(this);
        mBinding.detalleList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.detalleList.setHasFixedSize(true);
        mBinding.detalleList.setAdapter(mListAdapter);

    }


    @Override
    public void onClickVer(int sol) {
        Intent intento1=new Intent(getActivity(), NuevoInfEtapaActivity.class);
        intento1.putExtra(NuevoInfEtapaActivity.INFORMESEL, sol);
        intento1.putExtra(NuevoInfEtapaActivity.CORRECCION,true);
        startActivity(intento1);
    }





}