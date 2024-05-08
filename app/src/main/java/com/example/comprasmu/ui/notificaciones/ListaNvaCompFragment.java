package com.example.comprasmu.ui.notificaciones;

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
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.databinding.ListaGenericFragmentBinding;
import com.example.comprasmu.ui.infetapa.CancelEtaAdapter;
import com.example.comprasmu.ui.infetapa.ContInfEtapaFragment;
import com.example.comprasmu.ui.infetapa.EditInfEtapaActivity;
import com.example.comprasmu.ui.infetapa.NuevoInfEtapaActivity;
import com.example.comprasmu.ui.informe.CancelAdapter;
import com.example.comprasmu.ui.informe.ListaInformesViewModel;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.ui.FiltrarListaActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

/****muestro resumen de muestras agregadas x reactivacion**/
public class ListaNvaCompFragment extends Fragment implements NotifEtiqAdapter.AdapterCallback {
    private ListaNotifEtiqViewModel mViewModel;
    public static final String TAG = "ListaNvaCompFragment";
    private ListaGenericFragmentBinding mBinding;
    private NotifEtiqAdapter mListAdapter;
    private String indice;
    CoordinatorLayout coordinator;

    public ListaNvaCompFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        indice = Constantes.INDICEACTUAL;

     //   Log.d(Constantes.TAG,"cliente y planta sel"+clienteid+"--"+plantaid);
        mBinding= DataBindingUtil.inflate(inflater,
                R.layout.lista_generic_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(ListaNotifEtiqViewModel.class);
       // setHasOptionsMenu(true);
        return    mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

       // mBinding.setLcviewModel(mViewModel);
        mBinding.setLifecycleOwner(this);
        coordinator=view.findViewById(R.id.coordinator3);
        setupListAdapter();
       // setupSnackbar();
    }

    public void cargarLista(){
        List<InformeEtapa> informesfinal=new ArrayList<>();
        //busco etiquetado
        mViewModel.getEtiquetadoAdicional(indice).observe(getViewLifecycleOwner(), new Observer<List<InformeEtapa>>() {
            @Override
            public void onChanged(List<InformeEtapa> informes) {

                Log.d(TAG, "YA CARGÓ " + informes.size());

                for (InformeEtapa infeta:informes
                ) {
                    //reviso que ya pueda hacer esa etapa
                    //busco los clientes x ciudad
                    List<ListaCompra> listacomp = mViewModel.cargarClientesSimplxet(Constantes.CIUDADTRABAJO, 3);
                    if(listacomp!=null&&listacomp.size()>0&&listacomp.get(0).getClientesId()==infeta.getClientesId()) {

                        informesfinal.add(infeta);
                    }
                }

                if (informesfinal.size() < 1) {
                    mBinding.emptyStateText.setVisibility(View.VISIBLE);
                }
                Log.d(TAG, "YA CARGÓxx " + informesfinal.size());




            }
        });
        //busco empaque
        mViewModel.getEmpaqueCancel(indice,"reactivacion2").observe(getViewLifecycleOwner(), new Observer<List<InformeEtapa>>() {
            @Override
            public void onChanged(List<InformeEtapa> informes) {
                for (InformeEtapa infeta:informes
                ) {
                    //reviso que ya pueda hacer esa etapa
                    //busco los clientes x ciudad
                    List<ListaCompra> listacomp = mViewModel.cargarClientesSimplxet(Constantes.CIUDADTRABAJO, 4);
                    if(listacomp!=null&&listacomp.size()>0&&listacomp.get(0).getClientesId()==infeta.getClientesId()) {

                        informesfinal.add(infeta);
                    }
                }

            }
        });
        mListAdapter.setInformeCompraList(informesfinal);
        //mListAdapter.setProductoList(informesfinal);
        mListAdapter.notifyDataSetChanged();

    }


    private void setupListAdapter() {
        mBinding.detalleList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.detalleList.setHasFixedSize(true);
        mListAdapter = new NotifEtiqAdapter(this);
        mBinding.detalleList.setAdapter(mListAdapter);
        cargarLista();

    }



    @Override
    public void onClickAgregar(int idinforme) {
        Log.d(TAG,"di click en continua inf");
        // NavHostFragment.findNavController(this).navigate(R.id.action_nottoact);
        Intent intento1 = new Intent(getActivity(), EditInfEtapaActivity.class);
        intento1.putExtra(NuevoInfEtapaActivity.INFORMESEL,idinforme );
        intento1.putExtra(ContInfEtapaFragment.ETAPA,3 );
        startActivity(intento1);
    }

    @Override
    public void onClickContinuar(int idinforme) {
       /* Intent intento1 = new Intent(getActivity(), NuevoInfEtapaActivity.class);
        intento1.putExtra(NuevoInfEtapaActivity.INFORMESEL,idinforme );
        intento1.putExtra(ContInfEtapaFragment.ETAPA,3 );
        startActivity(intento1);*/

        Intent intento1 = new Intent(getActivity(), EditInfEtapaActivity.class);
        intento1.putExtra(NuevoInfEtapaActivity.INFORMESEL,idinforme );
        intento1.putExtra(ContInfEtapaFragment.ETAPA,3 );
        startActivity(intento1);
    }





}