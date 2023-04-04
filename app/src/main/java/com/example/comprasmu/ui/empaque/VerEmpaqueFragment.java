package com.example.comprasmu.ui.empaque;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.DetalleCaja;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.databinding.VerEmpaqueFragmentBinding;
import com.example.comprasmu.ui.RevisarFotoActivity;
import com.example.comprasmu.ui.gallery.GalFotosFragment;
import com.example.comprasmu.ui.informe.NuevoinformeFragment;
import com.example.comprasmu.ui.informe.VerInformeFragment;
import com.example.comprasmu.ui.informedetalle.InformeDetalleAdapter;
import com.example.comprasmu.ui.informedetalle.VerInformeDetFragment;
import com.example.comprasmu.utils.CampoForm;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;
import com.example.comprasmu.utils.ui.InformesGenViewModel;
import com.example.comprasmu.utils.ui.ListaInformesEtaFragment;
import com.example.comprasmu.utils.ui.VerInformeGenFragment;

import java.util.ArrayList;
import java.util.List;


public class VerEmpaqueFragment extends Fragment implements DetalleCajaAdapter.AdapterCallback {
    public static final String ARG_INFSEL = "comprasmu.vem.infsel";

    private InformesGenViewModel mViewModel;
    private int informeSel;
    LiveData<InformeEtapa> informeetapa;
    private CreadorFormulario cf1;


    private VerEmpaqueFragmentBinding mBinding;
    private DetalleCajaAdapter mListAdapter;
    private static final String TAG = "VerEmpaqueFragment";

    private int cliente;

    String directorio;

    public static VerEmpaqueFragment newInstance() {
        return new VerEmpaqueFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.ver_empaque_fragment, container, false);

        //   mBinding.setMviewModel(mViewModel);
        mBinding.setLifecycleOwner(this);
        mViewModel = new ViewModelProvider(this).get(InformesGenViewModel.class);
        mBinding.setDirectorio(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/");
        directorio = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/";
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle datosRecuperados = getActivity().getIntent().getExtras();

        if (datosRecuperados != null) {
            // No hay datos, manejar excepción
            //no debería estar aqui
            informeSel = datosRecuperados.getInt(ListaInformesEtaFragment.INFORMESEL);
            Log.d(TAG,"informesel"+informeSel);
            //busco el informe
            informeetapa=mViewModel.getInforme (informeSel,Constantes.INDICEACTUAL);
          informeetapa.observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<InformeEtapa>() {
              @Override
              public void onChanged(InformeEtapa informeEtapa) {
                  if(informeEtapa!=null)
                    crearFormulario(informeEtapa);
              }
          });
            setupListAdapter();
            llenarDetalle();

        }


    }

    public void llenarDetalle() {
        //busco las cajas
        mViewModel.getDetalleCajaEmp(informeSel).observe(getViewLifecycleOwner(), new Observer<List<DetalleCaja>>() {
            @Override
            public void onChanged(List<DetalleCaja> detalles) {
                Log.d(TAG,"ya tengo las cajas"+detalles.size());
                mListAdapter.setList(detalles);
                mListAdapter.notifyDataSetChanged();
            }

        });


    }

    private void setupListAdapter() {
        mListAdapter = new DetalleCajaAdapter(getActivity(),this);
        mBinding.edcontentmuestras.rvnimuestras.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.edcontentmuestras.rvnimuestras.setHasFixedSize(true);
        mBinding.edcontentmuestras.rvnimuestras.setAdapter(mListAdapter);

    }

    public void crearFormulario(InformeEtapa informe) {


        List<CampoForm> camposTienda = new ArrayList<CampoForm>();
        CampoForm campo = new CampoForm();


        campo.label = getString(R.string.indice);
        campo.type = "label";
        campo.value = ComprasUtils.indiceLetra(informe.getIndice());

        camposTienda.add(campo);
        campo = new CampoForm();


        campo.label = getString(R.string.fecha);
        campo.type = "label";
        campo.value = Constantes.vistasdf.format(informe.getCreatedAt());

        camposTienda.add(campo);
        campo = new CampoForm();
        campo.label = getString(R.string.cliente);

        campo.type = "label";
        campo.value = informe.getClienteNombre();


        camposTienda.add(campo);

        campo = new CampoForm();
        campo.label = getString(R.string.comentarios);
        campo.type = "label";

        campo.value = informe.getComentarios();


        camposTienda.add(campo);

        campo = new CampoForm();

        campo.label=getString(R.string.estatus);
        campo.type = "label";
        campo.value = Constantes.ESTATUSINFORME[informe.getEstatus()];

        camposTienda.add(campo);

        cf1 = new CreadorFormulario(camposTienda, getActivity());
        mBinding.eddatosgen.addView(cf1.crearFormulario());

    }



    public void verFotos(int numcaja) {
        Bundle bundle = new Bundle();
        bundle.putInt(VerInformeFragment.ARG_IDMUESTRA,numcaja);
        bundle.putInt(ARG_INFSEL,informeSel);
        bundle.putString(VerInformeGenFragment.ARG_TIPOINF, "e");
        Fragment fragment = new GalFotosFragment();
        fragment.setArguments(bundle);
        // Obtener el administrador de fragmentos a través de la actividad
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        // Definir una transacción
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Remplazar el contenido principal por el fragmento
        fragmentTransaction.replace(R.id.back_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        // Cambiar
        fragmentTransaction.commit();

}


    @Override
    public void onClickVer(int idmuestra) {
        //ver la muestra
      verFotos(idmuestra);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mViewModel=null;
        informeetapa=null;
        cf1=null;

         mBinding=null;
         mListAdapter=null;

         directorio=null;

    }

}