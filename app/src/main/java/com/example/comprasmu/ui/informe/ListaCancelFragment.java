package com.example.comprasmu.ui.informe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.comprasmu.R;
import com.example.comprasmu.data.dao.InformeCompraDao;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;

import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.databinding.ListaGenericFragmentBinding;

import com.example.comprasmu.ui.infetapa.CancelEtaAdapter;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.ui.FiltrarListaActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/****muestro resumen de muestras canceladas o informes cancelados de la demas etapas**/
public class ListaCancelFragment extends Fragment implements CancelAdapter.AdapterCallback {
    private ListaInformesViewModel mViewModel;


    public static final String TAG = "ListaCancelFragment";
   private ListaGenericFragmentBinding mBinding;
    private CancelAdapter mListAdapter;
    private CancelEtaAdapter mEtaAdapter;
    private int clienteid;
    private String ciudad;
    private int plantaid;
    private String tienda;
    private String indice;
    String clientesel;
    int plantasel;
    CoordinatorLayout coordinator;

    public ListaCancelFragment() {
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        indice = Constantes.INDICEACTUAL;



     //   Log.d(Constantes.TAG,"cliente y planta sel"+clienteid+"--"+plantaid);

        mBinding= DataBindingUtil.inflate(inflater,
                R.layout.lista_generic_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(ListaInformesViewModel.class);
       // setHasOptionsMenu(true);
        return    mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

       // mBinding.setLcviewModel(mViewModel);
        mBinding.setLifecycleOwner(this);
        coordinator=view.findViewById(R.id.coordinator3);

        setupListAdapter();



        setupSnackbar();
    }

    public void cargarLista(){
        //muesmtras canceladas en compras
        mViewModel.cargarCancelados(indice).observe(getViewLifecycleOwner(), new Observer<List<InformeCompraDetalle>>() {
            @Override
            public void onChanged(List<InformeCompraDetalle> informeCompraDetalles) {

                Log.d(TAG, "compra YA CARGÓ " + informeCompraDetalles.size());


                mViewModel.cargarCancelados2(indice).observe(getViewLifecycleOwner(), new Observer<List<InformeCompraDao.InformeCompravisita>>() {
                    @Override
                    public void onChanged(List<InformeCompraDao.InformeCompravisita> informeCompravisitas) {
                        //busco los de las demas etapas


                        mViewModel.cargarCanceladosEta(indice).observe(getViewLifecycleOwner(), new Observer<List<InformeEtapa>>() {
                            @Override
                            public void onChanged(List<InformeEtapa> informes) {
                                if (informes.size() < 1) {
                                    mBinding.emptyStateText.setVisibility(View.VISIBLE);
                                }
                                Log.d(TAG, "YA CARGÓ " + informes.size());
                            //paso de informe etapa ainforme compra
                                InformeCompraDao.InformeCompravisita nvoinf= new InformeCompraDao.InformeCompravisita();
                                for (InformeEtapa infeta:informes
                                     ) {
                                    nvoinf.indice=infeta.getIndice();
                                    nvoinf.idinforme=infeta.getId();
                                    nvoinf.estatus= infeta.getEstatus();
                                    nvoinf.tiendaNombre=Constantes.ETAPAS[infeta.getEtapa()];
                                    if(infeta.getClienteNombre().equals("")){
                                        nvoinf.clienteNombre=infeta.getCiudadNombre();
                                    }else
                                    nvoinf.clienteNombre=infeta.getClienteNombre();
                                    if(infeta.getPlantaNombre()!=null&&!infeta.getPlantaNombre().equals("")) {
                                        nvoinf.plantaNombre = infeta.getPlantaNombre();
                                    }else
                                        nvoinf.plantaNombre = infeta.getCiudadNombre();
                                   // nvoinf.mo
                                    informeCompravisitas.add(nvoinf);
                                }
                                nvoinf=null;

                                if (informeCompravisitas.size() < 1) {
                                    mBinding.emptyStateText.setVisibility(View.VISIBLE);
                                }
                                Log.d(TAG, "YA CARGÓxx " + informeCompravisitas.size());



                                mListAdapter.setInformeCompraList(informeCompravisitas);
                        mListAdapter.setProductoList(informeCompraDetalles);
                        mListAdapter.notifyDataSetChanged();
                            }
                        });

                    }
                });
            }
            });
    }


    private void setupListAdapter() {
        mBinding.detalleList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.detalleList.setHasFixedSize(true);
        mListAdapter = new CancelAdapter(this);

        mBinding.detalleList.setAdapter(mListAdapter);
        cargarLista();


    }


    private void setupSnackbar() {
        // Mostrar snackbar en resultados positivos de operaciones (crear, editar y eliminar)
        mViewModel.getSnackbarText().observe(getActivity(), integerEvent -> {
            Integer stringId = integerEvent.getContentIfNotHandled();
            if (stringId != null) {
                Snackbar.make(coordinator,
                        stringId, Snackbar.LENGTH_LONG).show();
            }
        });
    }

 /*   @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        menu.clear();
        inflater.inflate(R.menu.menu_listainforme, menu);
        //  super.onCreateOptionsMenu(menu, inflater);


    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_searchinforme:

                Intent intento1 = new Intent(getActivity(), FiltrarListaActivity.class);
                intento1.putExtra("filtrarlista.fragment","buscarinforme");
                startActivityForResult(intento1, FiltrarListaActivity.REQUEST_CODE_INFORME);

                return true;

            default:
                break;
        }

        return false;
    }

    @Override
    public void onClickVer() {
        Log.d(TAG,"di click en nvo inf");
        NavHostFragment.findNavController(this).navigate(R.id.action_cantoinf);

    }









}