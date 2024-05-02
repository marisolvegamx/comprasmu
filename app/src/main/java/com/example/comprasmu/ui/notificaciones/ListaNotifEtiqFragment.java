package com.example.comprasmu.ui.notificaciones;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.databinding.ListaGenericFragmentBinding;
import com.example.comprasmu.ui.infetapa.ContInfEtapaFragment;
import com.example.comprasmu.ui.infetapa.EditInfEtapaActivity;
import com.example.comprasmu.ui.infetapa.NuevoInfEtapaActivity;
import com.example.comprasmu.utils.Constantes;

import java.util.List;

public class ListaNotifEtiqFragment extends Fragment implements NotifEtiqAdapter.AdapterCallback {
    private ListaNotifEtiqViewModel mViewModel;
    public static final String TAG = "ListaNotifEtiqFragment";
    private ListaGenericFragmentBinding mBinding;

    private NotifEtiqAdapter mEtaAdapter;

    private String indice;

    CoordinatorLayout coordinator;

    public ListaNotifEtiqFragment() {
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

        mBinding.setLifecycleOwner(this);
        coordinator=view.findViewById(R.id.coordinator3);
        setupListAdapter();

    }

    public void cargarLista(){

        //mViewModel.cargarDetalles();
        List<InformeEtapa> informeDetalles= mViewModel.cargarCanceladosEtiq();


      //  mViewModel.getInfcancelados().observe(getViewLifecycleOwner(), new Observer<List<InformeEtapa>>() {
        //    @Override
        //    public void onChanged() {

               // Log.d(TAG, "YA CARGÓ " + informeDetalles.size());
             //   List<InformeEtapa> informeDetalles=new ArrayList<>();

                //o informes por completar con estatus 4
                List<InformeEtapa> etiqpend=mViewModel.cargarInfxCompletar();
                if(etiqpend!=null&&etiqpend.size()>0) {
                    informeDetalles.addAll(etiqpend);
                    Log.d(TAG, "COMP ETIQ " + etiqpend.size());
                }

                //tambien necesito saber si eliminé empaque
                List<InformeEtapa> empaque= mViewModel.cargarCanceladosEmp();
                if(empaque!=null&&empaque.size()>0) {
                    informeDetalles.addAll(empaque);
                    Log.d(TAG, "ELIMI EMP " + empaque.size());
                }
        mEtaAdapter.setInformeCompraList(informeDetalles);
        mEtaAdapter.notifyDataSetChanged();
        if (informeDetalles.size() < 1&&etiqpend.size()<1 &&empaque.size()<1) {
            mBinding.emptyStateText.setVisibility(View.VISIBLE);
        }
         //   }
         //   });
    }


    private void setupListAdapter() {
        mBinding.detalleList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.detalleList.setHasFixedSize(true);

        mEtaAdapter = new NotifEtiqAdapter(this);
        mBinding.detalleList.setAdapter(mEtaAdapter);

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