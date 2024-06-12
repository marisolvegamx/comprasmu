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
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.databinding.ListaCancelFragmentBinding;
import com.example.comprasmu.ui.infetapa.CancelEtaAdapter;
import com.example.comprasmu.ui.infetapa.ContInfEtaViewModel;
import com.example.comprasmu.ui.infetapa.ContInfEtapaFragment;
import com.example.comprasmu.ui.infetapa.EditInfEtapaActivity;
import com.example.comprasmu.ui.infetapa.NuevoInfEtapaActivity;
import com.example.comprasmu.ui.notificaciones.ListaNotifEtiqViewModel;
import com.example.comprasmu.ui.notificaciones.NotifEtiqAdapter;
import com.example.comprasmu.ui.preparacion.NvaPreparacionViewModel;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.ui.FiltrarListaActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

/****muestro resumen de muestras canceladas o informes cancelados de la demas etapas**/
public class ListaCancelFragment extends Fragment implements CancelAdapter.AdapterCallback, NotifEtiqAdapter.AdapterCallback {
    private ListaInformesViewModel mViewModel;


    public static final String TAG = "ListaCancelFragment";
   private ListaCancelFragmentBinding mBinding;
    private CancelAdapter mListAdapter;
    private NotifEtiqAdapter mEtaAdapter;
    private int clienteid;
    private String ciudad;
    private int plantaid;
    private String tienda;
    private String indice;
    String clientesel;
    int plantasel;
    CoordinatorLayout coordinator;
    ListaNotifEtiqViewModel notViewModel;
    public ListaCancelFragment() {
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        indice = Constantes.INDICEACTUAL;



     //   Log.d(Constantes.TAG,"cliente y planta sel"+clienteid+"--"+plantaid);

        mBinding= DataBindingUtil.inflate(inflater,
                R.layout.lista_cancel_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(ListaInformesViewModel.class);
        notViewModel = new ViewModelProvider(this).get(ListaNotifEtiqViewModel.class);
        //
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



        setupSnackbar();
    }

    public void cargarLista(){
        //muestras canceladas en compras
        mViewModel.cargarCancelados(indice).observe(getViewLifecycleOwner(), new Observer<List<InformeCompraDetalle>>() {
            @Override
            public void onChanged(List<InformeCompraDetalle> informeCompraDetalles) {

                Log.d(TAG, "compra YA CARGÓ " + informeCompraDetalles.size());

                mViewModel.cargarCancelados2(indice).observe(getViewLifecycleOwner(), new Observer<List<InformeCompraDao.InformeCompravisita>>() {
                    @Override
                    public void onChanged(List<InformeCompraDao.InformeCompravisita> informeCompravisitas) {

                        if (informeCompravisitas.size() < 1&&informeCompraDetalles.size()<1) {

                            //busco las demas etapas
                            mBinding.lismuestras.setVisibility(View.GONE);
                        }else
                        {
                            mBinding.lismuestras.setVisibility(View.VISIBLE);
                        }
                        Log.d(TAG, "YA CARGÓxx " + informeCompravisitas.size());

                        mListAdapter.setInformeCompraList(informeCompravisitas);
                        mListAdapter.setProductoList(informeCompraDetalles);
                        mListAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        setEtiquetado(3,6);
        //veo si ya puedo hacer empaque
        List<ListaCompra> listacomp = notViewModel.cargarClientesSimplxet(Constantes.CIUDADTRABAJO, 4);
        InformeEtapa nvoinf=new InformeEtapa();
        List<InformeEtapa> listageneral=new ArrayList<>();
        if (listacomp != null && listacomp.size() > 0 && listacomp.get(0).getLis_reactivado()==1) {
            //veo que no haya hecho informe para no esperar a la supervisión
            ContInfEtaViewModel conViewModel = new ViewModelProvider(this).get(ContInfEtaViewModel.class);
            InformeEtapa informesEtapa = conViewModel.getInformeNoCancel(Constantes.INDICEACTUAL,4);
            if(informesEtapa!=null) {
              nvoinf.setIndice(listacomp.get(0).getIndice());
              // nvoinf.set = listacomp.get(0).getId();
              nvoinf.setEstatus(listacomp.get(0).getEstatus());
              nvoinf.setEtapa(4);

              nvoinf.setCiudadNombre(listacomp.get(0).getCiudadNombre());
              nvoinf.setClienteNombre(listacomp.get(0).getClienteNombre());

              // nvoinf.mo
              listageneral.add(nvoinf);
            }
        }


        nvoinf = null;

                if (listageneral.size() >0) {


        Log.d(TAG, "emp YA CARGÓ" + listageneral.size());

        mEtaAdapter.setInformeCompraList(listageneral);

        mEtaAdapter.notifyDataSetChanged();
        mBinding.lisinfeta.setVisibility(View.VISIBLE);
    }


    }


    private void setupListAdapters() {
        /**hice 2 reciclerview 1 para las muestras canceladas y otro para cuando sea reactivacion
         * se muestre que falta etiquetado y empaque
         */
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

    private void setEtiquetado(int etapa, int estatus) {
        List<InformeEtapa> listageneral=new ArrayList<>();
        //para ver si sigue etiquetado y empaque
        mViewModel.getInfEtapaxEstatus(indice,etapa,estatus).observe(getViewLifecycleOwner(), new Observer<List<InformeEtapa>>() {
            @Override
            public void onChanged(List<InformeEtapa> informes) {


                //paso de informe etapa ainforme compra
                DetalleCancelado nvoinf = new DetalleCancelado();
                for (InformeEtapa infeta : informes
                ) {

                    //reviso si ya estoy en etapa 3
                    List<ListaCompra> listacomp = notViewModel.cargarClientesSimplxet(Constantes.CIUDADTRABAJO, 3);
                    if (listacomp != null && listacomp.size() > 0 && listacomp.get(0).getClientesId() == infeta.getClientesId()) {

                     /*   nvoinf.indice = infeta.getIndice();
                        nvoinf.idinforme = infeta.getId();
                        nvoinf.estatus = infeta.getEstatus();
                        nvoinf.nombreEtapa = Constantes.ETAPAS[infeta.getEtapa()];
                        if (infeta.getClienteNombre().equals("")) {
                            nvoinf.plancdNombre = infeta.getCiudadNombre();
                            nvoinf.clienteNombre = infeta.getClienteNombre();
                        }*/
                            // nvoinf.mo
                            listageneral.add(infeta);


                    }
                    nvoinf = null;
                }
                if (listageneral.size() < 1) {

                } else {
                    Log.d(TAG, "etiq YA CARGÓ " + listageneral.size());

                    mEtaAdapter.setInformeCompraList(listageneral);

                    mEtaAdapter.notifyDataSetChanged();
                    mBinding.lisinfeta.setVisibility(View.VISIBLE);
                }


            }
        });
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


  /*  @Override
    public void onClickAgregar(int idinforme) {
        //se cancelo
        Intent intento1 = new Intent(getActivity(), EditInfEtapaActivity.class);
        intento1.putExtra(EditInfEtapaActivity.INFORMESEL,idinforme );
        intento1.putExtra(EditInfEtapaActivity.ETAPA,3 );
        intento1.putExtra(EditInfEtapaActivity.REACTIVADO,1);
        startActivity(intento1);

    }*/

    @Override
    public void onClickContinuar(int idinforme) {
        //no deberia entrar aqui
    }

    @Override
    public void onClickNvoEmp() { //paso 3

        Intent intento1 = new Intent(getActivity(), NuevoInfEtapaActivity.class);
        intento1.putExtra(ContInfEtapaFragment.ETAPA,4 );
        startActivity(intento1);
    }
}