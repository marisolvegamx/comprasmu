package com.example.comprasmu.utils.ui;

import android.content.Intent;
import android.os.Bundle;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.Correccion;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.SolicitudWithCor;
import com.example.comprasmu.databinding.VerInformegenFragmentBinding;
import com.example.comprasmu.ui.RevisarFotoActivity;
import com.example.comprasmu.ui.correccion.NvaCorreViewModel;
import com.example.comprasmu.ui.gallery.GalFotosFragment;
import com.example.comprasmu.ui.informe.NuevoinformeFragment;
import com.example.comprasmu.ui.informe.VerInformeFragment;
import com.example.comprasmu.utils.CampoForm;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;

import java.util.ArrayList;
import java.util.List;

/***se usa para informes correccion e informes etapas********/
public class VerInformeGenFragment extends Fragment {

    public static final String ARG_IDDET = "comprasmu.vie.iddet";
    public static final String ARG_TIPOINF = "comprasmu.vie.tipoinf";
    private InformesGenViewModel mViewModel;
    private int informeSel;
    InformeEtapa informeEtapa;
    SolicitudWithCor correccion;
    private CreadorFormulario cf1;
    private CreadorFormulario cf2;
    CampoForm campo2;
    private VerInformegenFragmentBinding mBinding;
    NvaCorreViewModel corViewModel;
    private static String TAG = "VerInformeGenFragment";
    String directorio;
    String tipo;


    public static VerInformeGenFragment newInstance() {
        return new VerInformeGenFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.ver_informegen_fragment, container, false);

        //   mBinding.setMviewModel(mViewModel);
        Log.d(TAG,"en ver informe de correcccion");
        mBinding.setLifecycleOwner(this);
        mViewModel = new ViewModelProvider(this).get(InformesGenViewModel.class);
        corViewModel=new ViewModelProvider(this).get(NvaCorreViewModel.class);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle=getArguments();
        if(bundle!=null){
            informeSel=bundle.getInt(ListaInformesEtaFragment.INFORMESEL);
            tipo=bundle.getString(ListaInformesEtaFragment.ARG_TIPOCONS);
            llenarDetalle();

        }



    }

    public void llenarDetalle() {
        Log.d(TAG, "llenando detalles"+Constantes.ETAPAACTUAL);
        if(tipo.equals("action_selclitocor2")) {
            corViewModel.getCorreccion(informeSel).observe(getViewLifecycleOwner(), new Observer<SolicitudWithCor>() {
                @Override
                public void onChanged(SolicitudWithCor vcorreccion) {
                    correccion=vcorreccion;
                    crearFormularioCor();
                }
            });

        }else
            mViewModel.getInforme(informeSel,Constantes.INDICEACTUAL).observe(getViewLifecycleOwner(), new Observer<InformeEtapa>() {
                @Override
                public void onChanged(InformeEtapa informeEtapax) {
                    informeEtapa=informeEtapax;
                    crearFormularioEta();
                    mBinding.vidatosgen.addView(cf1.crearTabla());
                    mBinding.btnverdet.setText(R.string.ver_fotos);
                    mBinding.btnverdet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            verFotos();
                        }
                    });
                }
            });

    }


    public void crearFormularioCor() {


        List<CampoForm> camposTienda = new ArrayList<CampoForm>();
        CampoForm campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.label = getString(R.string.indice);
        campo.type = "label";
        campo.value = correccion.solicitud.getIndice();

      /*  camposTienda.add(campo);
        campo = new CampoForm();
        campo.label = getString(R.string.consecutivo);
        campo.style = R.style.verinforme2;
        campo.type = "label";
        campo.value = correccion.solicitud.;
        camposTienda.add(campo);*/

        campo = new CampoForm();
        campo.label = getString(R.string.cliente);
        campo.style = R.style.verinforme2;
        campo.type = "label";
        campo.value = correccion.solicitud.getClienteNombre();

        camposTienda.add(campo);
        campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.label = getString(R.string.planta);
        campo.type = "label";
        campo.value = correccion.solicitud.getPlantaNombre();

        camposTienda.add(campo);
        campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.label = getString(R.string.fecha);
        campo.type = "label";
        campo.value = Constantes.vistasdf.format(correccion.correccion.getCreatedAt());
        camposTienda.add(campo);
        if(Constantes.ETAPAACTUAL==2) {
            campo = new CampoForm();
            campo.style = R.style.verinforme2;
            campo.nombre_campo = "tiendaNombre";
            campo.label = getString(R.string.nombre_tienda);
            campo.type = "label";
            campo.value = correccion.solicitud.getNombreTienda();
            camposTienda.add(campo);
        }

        campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.nombre_campo = "tiendaNombre";
        campo.label = getString(R.string.descripcion);
        campo.type = "label";
        campo.value =correccion.solicitud.getDescripcionFoto();
        camposTienda.add(campo);

        campo = new CampoForm();
        campo.nombre_campo = "foto";
        campo.type = "imagenViewd";
        campo.value =correccion.correccion.getRuta_foto1();
        camposTienda.add(campo);
        //faltan otras 2 fotos
        campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.nombre_campo = "tiendaNombre";
        campo.label = getString(R.string.estatus_envio);
        campo.type = "label";
        campo.value = Constantes.ESTATUSSYNC[correccion.correccion.getEstatusSync()];
        camposTienda.add(campo);



        cf1 = new CreadorFormulario(camposTienda, getActivity());

    }

    public void crearFormularioEta() {

        List<CampoForm> camposTienda = new ArrayList<CampoForm>();
        CampoForm campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.label = getString(R.string.indice);
        campo.type = "label";
        campo.value = ComprasUtils.indiceLetra(informeEtapa.getIndice());

      /*  camposTienda.add(campo);
        campo = new CampoForm();
        campo.label = getString(R.string.consecutivor);
        campo.style = R.style.verinforme2;
        campo.type = "label";
        campo.value = informeEtapa.getConsecutivo()+"";
        camposTienda.add(campo);*/

        campo = new CampoForm();
        campo.label = getString(R.string.cliente);
        campo.style = R.style.verinforme2;
        campo.type = "label";
        campo.value = informeEtapa.getClienteNombre();

        camposTienda.add(campo);
        campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.label = getString(R.string.planta);
        campo.type = "label";
        campo.value = informeEtapa.getPlantaNombre();


        camposTienda.add(campo);
        campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.label = getString(R.string.fecha);
        campo.type = "label";
        campo.value = Constantes.vistasdf.format(informeEtapa.getCreatedAt());

        camposTienda.add(campo);


        campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.nombre_campo = "comentarios";
        campo.label = getString(R.string.comentarios);
        switch (Constantes.ETAPAACTUAL) {
            case 1:
                campo.value = informeEtapa.getComentarios_prep();
                break;
            case 2:
                campo.value = informeEtapa.getComentarios_etiq();
                break;
            case 3:
                campo.value=informeEtapa.getComentarios_empaq();
                break;

        }
        campo.type = "label";


        camposTienda.add(campo);

        campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.nombre_campo = "tiendaNombre";
        campo.label = getString(R.string.estatus_envio);
        campo.type = "label";
        campo.value = Constantes.ESTATUSSYNC[informeEtapa.getEstatusSync()];
        camposTienda.add(campo);


        Log.d(TAG,"CAMPOS TOT"+camposTienda);
        cf1 = new CreadorFormulario(camposTienda, getActivity());

    }

    public void verImagen(String nombrearch){
      //  ImageView imagen=(ImageView)v;
       // imagen.get
        Intent iverim=new Intent(getActivity(), RevisarFotoActivity.class);
        iverim.putExtra(RevisarFotoActivity.IMG_PATH1,nombrearch);
        startActivity(iverim);
    }



    public void onClickVerDet() {
        //ver la muestra
        Bundle bundle = new Bundle();

        bundle.putInt(NuevoinformeFragment.INFORMESEL,informeSel);

       bundle.putInt(ARG_IDDET,informeSel);
        Fragment fragment = new VerInfDetGenFragment();
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
    public void verFotos(){

        Bundle bundle = new Bundle();
        bundle.putInt(VerInformeFragment.ARG_IDMUESTRA,informeSel);
        bundle.putString(ARG_TIPOINF, "e");
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
    public void onDestroyView() {
        super.onDestroyView();

        mViewModel=null;

        cf2=null;

         informeEtapa=null;
        cf1=null;

         campo2=null;
         mBinding=null;


         directorio=null;

    }

}