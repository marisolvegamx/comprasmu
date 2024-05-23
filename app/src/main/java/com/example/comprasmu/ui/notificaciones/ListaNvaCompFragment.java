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
import com.example.comprasmu.databinding.ListaCancelFragmentBinding;
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
public class ListaNvaCompFragment extends Fragment implements CancelAdapter.AdapterCallback,NotifEtiqAdapter.AdapterCallback {
    private ListaNotifEtiqViewModel mViewModel;
    public static final String TAG = "ListaNvaCompFragment";
    private ListaCancelFragmentBinding mBinding;
    private NotifEtiqAdapter mEtaAdapter;
    private CancelAdapter mListAdapter;
    //private NotifEtiqAdapter mListAdapter;
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
                R.layout.lista_cancel_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(ListaNotifEtiqViewModel.class);
       // setHasOptionsMenu(true);
        return    mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

       // mBinding.setLcviewModel(mViewModel);
        mBinding.setLifecycleOwner(this);
        coordinator=view.findViewById(R.id.coordinator3);
        mBinding.lisinfeta.setVisibility(View.GONE);
        mBinding.lismuestras.setVisibility(View.GONE);
        setupListAdapters();
       // setupSnackbar();
    }

    public void cargarLista(){
        //todo lista de compra pendiente
        List<ListaCompra> listacomp = mViewModel.cargarClientesSimplxetReac(Constantes.CIUDADTRABAJO, 3,2);
        if(listacomp.size()>0){
            //busco el detalle
            for (ListaCompra compra:listacomp
                 ) {
                List<ListaCompraDetalle> compraDetalles= mViewModel.getAllByListasimple(compra.getId());


            }
        }
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

                if (informesfinal.size() >0) {
                    mBinding.lisinfeta.setVisibility(View.VISIBLE);

                    Log.d(TAG, "YA CARGÓxx " + informesfinal.size());

                    mEtaAdapter.setInformeCompraList(informesfinal);
                    //mListAdapter.setProductoList(informesfinal);
                    mEtaAdapter.notifyDataSetChanged();
                }


            }
        });

        //veo si ya puedo hacer empaque
         listacomp = mViewModel.cargarClientesSimplxet(Constantes.CIUDADTRABAJO, 4);
        InformeEtapa nvoinf=new InformeEtapa();
        List<InformeEtapa> listageneral=new ArrayList<>();
        if (listacomp != null && listacomp.size() > 0 && listacomp.get(0).getLis_reactivado()==2) {

            nvoinf.setIndice( listacomp.get(0).getIndice());
            // nvoinf.set = listacomp.get(0).getId();
            nvoinf.setEstatus(listacomp.get(0).getEstatus());
            nvoinf.setEtapa( 4);

            nvoinf.setCiudadNombre(listacomp.get(0).getCiudadNombre());
            nvoinf.setClienteNombre(listacomp.get(0).getClienteNombre());

            // nvoinf.mo
            listageneral.add(nvoinf);
        }


        nvoinf = null;

        if (listageneral.size() >0) {


            Log.d(TAG, "YA CARGÓxx " + listageneral.size());

            mEtaAdapter.setInformeCompraList(listageneral);

            mEtaAdapter.notifyDataSetChanged();
            mBinding.lisinfeta.setVisibility(View.VISIBLE);
        }


    }


    private void setupListAdapters() {
        mBinding.lismuestras.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.lismuestras.setHasFixedSize(true);
        mListAdapter = new CancelAdapter(this);
        mBinding.lismuestras.setAdapter(mListAdapter);
        mBinding.lisinfeta.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.lisinfeta.setHasFixedSize(true);
        mEtaAdapter = new NotifEtiqAdapter(this);

        mBinding.lisinfeta.setAdapter(mEtaAdapter);
        cargarLista();

    }



    @Override
    public void onClickAgregar(int idinforme) { //informe etiquetado
        /***no deberia entrar aqui***/
    }

    @Override
    public void onClickContinuar(int idinforme) { //informe etiquetado

        Log.d(TAG,"di click en continua inf");
        // NavHostFragment.findNavController(this).navigate(R.id.action_nottoact);
        Intent intento1 = new Intent(getActivity(), EditInfEtapaActivity.class);
        intento1.putExtra(EditInfEtapaActivity.INFORMESEL,idinforme );
        intento1.putExtra(EditInfEtapaActivity.ETAPA,3 );
        intento1.putExtra(EditInfEtapaActivity.REACTIVADO,2 );
        startActivity(intento1);
    }

    @Override
    public void onClickNvoEmp() {
        Intent intento1 = new Intent(getActivity(), NuevoInfEtapaActivity.class);
        intento1.putExtra(ContInfEtapaFragment.ETAPA,4 );
        startActivity(intento1);
    }


    @Override
    public void onClickVer() { //nvo informe compra
        Log.d(TAG,"di click en nvo inf");
        NavHostFragment.findNavController(this).navigate(R.id.action_cantoinf);
    }
}