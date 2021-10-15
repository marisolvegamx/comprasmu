package com.example.comprasmu.ui.tiendas;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.comprasmu.R;
import com.example.comprasmu.databinding.BuscarTiendaFragmentBinding;

public class BuscarTiendaFragment extends Fragment implements DescripcionGenericaAdapter.AdapterCallback {

    private BuscarTiendaViewModel mViewModel;
    private BuscarTiendaFragmentBinding mBinding;
    private DescripcionGenericaAdapter mListAdapter;
    private RecyclerView mRecyclerView;
    private Button btnbuscar;


    public static BuscarTiendaFragment newInstance() {
        return new BuscarTiendaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding= DataBindingUtil.inflate(inflater,
                R.layout.buscar_tienda_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(BuscarTiendaViewModel.class);
        mBinding.setViewModel(mViewModel);
        mBinding.setLifecycleOwner(this);

        mRecyclerView=mBinding.btrvtiendas;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        initUI();
        return    mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BuscarTiendaViewModel.class);

    }

    public void initUI(){
        btnbuscar=getActivity().findViewById(R.id.btbtnbuscar);
        btnbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.getTiendas();
            }
        });
        setupListAdapter();

    }
    private void setupListAdapter() {


        mListAdapter = new DescripcionGenericaAdapter(mViewModel,this);
        //mBinding.detalleList.setAdapter(mListAdapter);
        mBinding.btrvtiendas.setAdapter(mListAdapter);

    }

    @Override
    public void onClickCallback(String codigos) {

    }
}