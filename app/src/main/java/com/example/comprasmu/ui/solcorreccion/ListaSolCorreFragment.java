package com.example.comprasmu.ui.solcorreccion;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.SolicitudCor;
import com.example.comprasmu.databinding.ListaInformesFragmentBinding;
import com.example.comprasmu.ui.infetapa.ContInfEtapaFragment;
import com.example.comprasmu.ui.infetapa.NuevoInfEtapaActivity;
import com.example.comprasmu.ui.listadetalle.ListaCompraFragment;
import com.example.comprasmu.ui.tiendas.LoadingAlert;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.ui.InformesGenViewModel;

import java.util.List;

/*************Muestra la lista de solicitudes de correccion para todas las etapas*******/
public class ListaSolCorreFragment extends Fragment implements SolCorreAdapter.AdapterCallback {

    private ListaSolsViewModel mViewModel;
    public static final String TAG = "ListaSolCorreFragment";
    private ListaInformesFragmentBinding mBinding;
    private SolCorreAdapter mListAdapter;
    private String indice;
    int plantasel;
    private boolean isGeneral; //para saber si es correccion de tienda
    CoordinatorLayout coordinator;
    public static final String ARG_ESGEN="comprasmu.solcorreccion.esgeneral";//indica si se agregará muestra
    LoadingAlert alert;
    private InformesGenViewModel informesGViewModel;

    public ListaSolCorreFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (getArguments() != null) {
                //porque lo habia comentado????

            plantasel=getArguments().getInt(ListaCompraFragment.ARG_PLANTASEL);
            //para generales puede ser ciudad
            isGeneral=getArguments().getBoolean(ARG_ESGEN);

        }
        indice = Constantes.INDICEACTUAL;

        mBinding= DataBindingUtil.inflate(inflater,
                R.layout.lista_informes_fragment, container, false);

        mViewModel = new ViewModelProvider(this).get(ListaSolsViewModel.class);
        informesGViewModel = new ViewModelProvider(this).get(InformesGenViewModel.class);
        setHasOptionsMenu(true);
        return    mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        coordinator=view.findViewById(R.id.coordinator3);
        setupListAdapter();
        actualizarImagenes();
    }

    public void cargarListaAll(){
        Log.d(TAG,"etapa y planta sel"+Constantes.ETAPAACTUAL+"--"+plantasel);

        mViewModel.cargarDetallesAll(indice).observe(getViewLifecycleOwner(), new Observer<List<SolicitudCor>>() {
                @Override
                public void onChanged(List<SolicitudCor> solicitudCors) {

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
    public void onClickVer(int sol, int numfoto, int etapa) {
        Intent intento1=new Intent(getActivity(), NuevoInfEtapaActivity.class);
        intento1.putExtra(NuevoInfEtapaActivity.INFORMESEL, sol);
        intento1.putExtra(NuevoInfEtapaActivity.NUMFOTO, numfoto);
        intento1.putExtra(ContInfEtapaFragment.ETAPA, etapa);
        intento1.putExtra(NuevoInfEtapaActivity.CORRECCION,true);
        startActivity(intento1);
    }
    //para buscar los cambios en las imagenes
    private void actualizarImagenes() {
        informesGViewModel.setUsuario(Constantes.CLAVEUSUARIO);
        informesGViewModel.setIndice(Constantes.INDICEACTUAL);
        alert=new LoadingAlert(getActivity());
        alert.startAlert();
        MutableLiveData<Boolean> observable=new MutableLiveData<>();
        observable.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                alert.closeAlertDialog();
                cargarListaAll();
            }
        });
        String dirLog=getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getPath();
        informesGViewModel.actualizarImagen(dirLog,observable);
    }



}