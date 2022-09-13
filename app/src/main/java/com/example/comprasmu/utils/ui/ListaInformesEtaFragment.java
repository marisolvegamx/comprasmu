package com.example.comprasmu.utils.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.SubirInformeTask;
import com.example.comprasmu.data.modelos.SolicitudWithCor;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.remote.InformeEnvio;
import com.example.comprasmu.databinding.ListaInformesFragmentBinding;
import com.example.comprasmu.ui.BackActivity;
import com.example.comprasmu.ui.correccion.NvaCorreViewModel;
import com.example.comprasmu.ui.infetapa.ContInfEtapaFragment;
import com.example.comprasmu.ui.infetapa.SelClienteGenFragment;
import com.example.comprasmu.ui.informe.NuevoinformeFragment;
import com.example.comprasmu.ui.informe.NuevoinformeViewModel;
import com.example.comprasmu.ui.listadetalle.ListaCompraFragment;
import com.example.comprasmu.utils.Constantes;

import java.util.ArrayList;
import java.util.List;

/*******para informes x etapa y correcciones******/
public class ListaInformesEtaFragment extends Fragment implements InformeGenAdapter.AdapterCallback {
    public static final String INFORMESEL ="comprasmu.lie.informesel" ;
    public static final String ARG_TIPOCONS ="comprasmu.lie.tipocons" ;
    private InformesGenViewModel mViewModel;
    public static final String TAG = "ListaInfEtaFragment";
    private ListaInformesFragmentBinding mBinding;
    private InformeGenAdapter mListAdapter;
    private LiveData<List<InformeEtapa>> listainfs;
    private int clienteid;
    private String ciudad;
    private String indice;
    String clientesel;
    int plantasel;
    CoordinatorLayout coordinator;
    private int etapa;
    String tipocons; //e para etapa, action_selclitocor2 para correccion
    private NvaCorreViewModel corViewModel;

    public ListaInformesEtaFragment() {

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
            Log.e(TAG,"si hay"+getArguments().toString());
            tipocons = getArguments().getString(SelClienteGenFragment.ARG_TIPOCONS);
            plantasel=getArguments().getInt(ListaCompraFragment.ARG_PLANTASEL);
            etapa=getArguments().getInt(ContInfEtapaFragment.ETAPA);

          //  indice=getArguments().getString(Constantes.INDICEACTUAL);

        }
            indice = Constantes.INDICEACTUAL;


     //   Log.d(Constantes.TAG,"cliente y planta sel"+clienteid+"--"+plantaid);

        mBinding= DataBindingUtil.inflate(inflater,
                R.layout.lista_informes_fragment, container, false);

        mViewModel = new ViewModelProvider(this).get(InformesGenViewModel.class);
        corViewModel=new ViewModelProvider(this).get(NvaCorreViewModel.class);
        setHasOptionsMenu(true);
        return    mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

       // mViewModel = new ViewModelProvider(this).get(ListaInformesGenViewModel.class);

      //  mBinding.setLcviewModel(mViewModel);
       // mBinding.setLifecycleOwner(this);
        coordinator=view.findViewById(R.id.coordinator3);

        setupListAdapter();

       cargarLista();
    }

    public void cargarLista(){
        //aqui hago la consulta de los informes x etapa
        //y de los informes correccion
        if(tipocons.equals("e")){
            Log.e(TAG,etapa+"--"+indice+"--"+plantasel);
            listainfs=mViewModel.cargarEtapa(etapa,indice,plantasel);
            listainfs.observe(getViewLifecycleOwner(), new Observer<List<InformeEtapa>>() {
                @Override
                public void onChanged(List<InformeEtapa> informeEtapas) {
                    if(informeEtapas.size()<1){
                        mBinding.emptyStateText.setVisibility(View.VISIBLE);
                    }
                    mListAdapter.setInformeCompraList(informeEtapas);
                    mListAdapter.notifyDataSetChanged();
                }
            });
        }
        if(tipocons.equals("action_selclitocor2")){
            etapa=Constantes.ETAPAACTUAL;
            Log.e(TAG,etapa+"--"+indice+"--"+plantasel);
            LiveData<List<SolicitudWithCor>> listacor = corViewModel.getCorreccionesxEta(etapa, indice, plantasel);
            listacor.observe(getViewLifecycleOwner(), new Observer<List<SolicitudWithCor>>() {
                @Override
                public void onChanged(List<SolicitudWithCor> correccions) {
                    if(correccions.size()<1){
                        mBinding.emptyStateText.setVisibility(View.VISIBLE);
                    }
                    //paso la correccion a informeetapa

                    mListAdapter.setInformeCompraList(correccionAInforme(correccions));
                    mListAdapter.notifyDataSetChanged();
                }
            });
        }



    }
    private void setupListAdapter() {
        mListAdapter = new InformeGenAdapter(this);
        mBinding.detalleList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.detalleList.setHasFixedSize(true);
        mBinding.detalleList.setAdapter(mListAdapter);

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
    private List<InformeEtapa> correccionAInforme(List<SolicitudWithCor> correccs){
        List<InformeEtapa> informes=new ArrayList<>();
        InformeEtapa temp;
        for(SolicitudWithCor cor:correccs){
            temp=new InformeEtapa();
            temp.setId(cor.correccion.getId());
            temp.setEtapa(cor.solicitud.getEtapa());
            temp.setIndice(cor.solicitud.getIndice());
            temp.setPlantasId(cor.solicitud.getPlantasId());
            temp.setPlantaNombre(cor.solicitud.getPlantaNombre());
            temp.setClienteNombre(cor.solicitud.getClienteNombre());
            temp.setClientesId(cor.solicitud.getClientesId());
            temp.setEstatus(cor.correccion.getEstatus());
            temp.setEstatusSync(cor.correccion.getEstatusSync());
            temp.setCreatedAt(cor.correccion.getCreatedAt());
            informes.add(temp);

        }
        return informes;

    }
    @Override
    public void onClickVer(int informe) {
        Intent intento1=new Intent(getActivity(), BackActivity.class);
        intento1.putExtra(INFORMESEL, informe);
        intento1.putExtra(ARG_TIPOCONS, tipocons);
        Constantes.ETAPAACTUAL=etapa;
        intento1.putExtra(BackActivity.ARG_FRAGMENT,BackActivity.OP_INFORMECOR);
        startActivity(intento1);

    }




    @Override
    public void onClickSubir(int informe) {
        if(NavigationDrawerActivity.isOnlineNet()) {
            NuevoinformeViewModel niViewModel = new ViewModelProvider(this).get(NuevoinformeViewModel.class);

            InformeEnvio informeenv = niViewModel.preparaInforme(informe);
            SubirInformeTask miTareaAsincrona = new SubirInformeTask(true, informeenv, getActivity(), niViewModel);
            miTareaAsincrona.execute();
            Log.d(TAG, "preparando informe**********");
            NuevoinformeFragment.subirFotos(getActivity(), informeenv);
        }else
            Toast.makeText(getActivity(), getString(R.string.sin_conexion), Toast.LENGTH_LONG).show();

    }




}