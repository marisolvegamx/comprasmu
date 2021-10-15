package com.example.comprasmu.ui.informe;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.comprasmu.R;
import com.example.comprasmu.data.dao.ProductoExhibidoDao;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeWithDetalle;
import com.example.comprasmu.data.modelos.Visita;
import com.example.comprasmu.databinding.VerInformeFragmentBinding;
import com.example.comprasmu.ui.BackActivity;
import com.example.comprasmu.ui.informedetalle.DetalleProductoFragment1;
import com.example.comprasmu.ui.informedetalle.InformeDetalleAdapter;
import com.example.comprasmu.ui.informedetalle.VerInformeDetFragment;
import com.example.comprasmu.utils.CampoForm;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;

import java.util.ArrayList;
import java.util.List;

public class VerInformeFragment extends Fragment implements InformeDetalleAdapter.AdapterCallback {

    private VerInformeViewModel mViewModel;
    private int informeSel;
    InformeCompra informeCompra;
    private CreadorFormulario cf1;
    private CreadorFormulario cf2;
    CampoForm campo2;
    private VerInformeFragmentBinding mBinding;
    private InformeDetalleAdapter mListAdapter;
    private static String TAG="VerInformeFragment";

    public static VerInformeFragment newInstance() {
        return new VerInformeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding= DataBindingUtil.inflate(inflater,R.layout.ver_informe_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(VerInformeViewModel.class);
        mBinding.setMviewModel(mViewModel);
        mBinding.setLifecycleOwner(this);
        Bundle datosRecuperados = getActivity().getIntent().getExtras();
        mBinding.setDirectorio(getActivity().getExternalFilesDir(null)+"/");
        if (datosRecuperados != null) {
            // No hay datos, manejar excepción
            //no debería estar aqui
            informeSel = datosRecuperados.getInt(NuevoinformeFragment.INFORMESEL);
            //busco el informe
            mViewModel.buscarInforme(informeSel);



       /*   mViewModel.informevisita.observe(getViewLifecycleOwner(), new Observer<InformeCompraDao.InformeCompravisita>() {
            @Override
            public void onChanged(InformeCompraDao.InformeCompravisita informeCompra) {
                //lleno el formulario
               // crearFormulario();
                mBinding.setInforme(informeCompra);

            }
        });*/

            setupListAdapter();
            llenarDetalle();

        }
        return mBinding.getRoot();
    }

    @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {




    }
    public void llenarDetalle(){
        Log.d(TAG,"llenndo detalles");
        mViewModel.informeCompraSel.observe(getViewLifecycleOwner(), new Observer<InformeWithDetalle>() {
            @Override
            public void onChanged(InformeWithDetalle informe) {
                if(mViewModel.informeCompraSel.getValue()!=null){
                mListAdapter.setInformeCompraDetalleList(mViewModel.informeCompraSel.getValue().informeDetalle,true);
                mListAdapter.notifyDataSetChanged();
                mBinding.setInforme(mViewModel.informeCompraSel.getValue().informe);
                mViewModel.visita.observe(getViewLifecycleOwner(), new Observer<Visita>() {
                    @Override
                    public void onChanged(Visita visita) {
                        crearFormulario();
                        mBinding.vidatosgen.addView(cf1.crearFormulario());
                    }
                });

                }
            }
        });

    }
    private void setupListAdapter() {
        mListAdapter = new InformeDetalleAdapter(this);
        mBinding.vicontentmuestras.rvnimuestras.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.vicontentmuestras.rvnimuestras.setHasFixedSize(true);
        mBinding.vicontentmuestras.rvnimuestras.setAdapter(mListAdapter);

    }

    public void crearFormulario(){

        informeCompra=mViewModel.getInformeCompraSel().getValue().informe;

        List<CampoForm> camposTienda = new ArrayList<CampoForm>();
        CampoForm campo = new CampoForm();


        campo.type = "label";
        campo.value = "<b>Consecutivo:</b>"+informeCompra.getConsecutivo()+"";
        camposTienda.add(campo);
         campo = new CampoForm();


        campo.type = "label";
        campo.value =  "Indice:"+mViewModel.getVisita().getValue().getIndice();

        camposTienda.add(campo);
         campo = new CampoForm();


        campo.type = "label";
        campo.value =  "Fecha:"+Constantes.vistasdf.format( mViewModel.getVisita().getValue().getCreatedAt());

        camposTienda.add(campo);
        campo = new CampoForm();
        campo.type = "label";
        campo.value = "Ciudad:"+mViewModel.getVisita().getValue().getCiudad();

        camposTienda.add(campo);
        campo = new CampoForm();
        campo.type = "label";
        campo.value = "Cliente:"+informeCompra.getClienteNombre();


        camposTienda.add(campo);
        campo = new CampoForm();

        campo.type = "label";
        campo.value =  "Planta:"+informeCompra.getPlantaNombre();



        camposTienda.add(campo);
        campo = new CampoForm();

        campo.nombre_campo = "tiendaNombre";
        campo.type = "label";
        campo.value ="Nombre tienda:"+ mViewModel.getVisita().getValue().getTiendaNombre();

        camposTienda.add(campo);
        campo = new CampoForm();
        campo.type = "label";
        campo.value ="Dirección:"+mViewModel.getVisita().getValue().getDireccion();
        camposTienda.add(campo);
        campo = new CampoForm();

        campo.nombre_campo = "complementodireccion";
        campo.type = "label";
        campo.value ="Complemento dirección:"+mViewModel.getVisita().getValue().getComplementodireccion();


        camposTienda.add(campo);


        campo = new CampoForm();

        campo.type = "label";
        campo.value = Html.fromHtml( "<b>Comentarios:</b>"+informeCompra.getComentarios())+"";

        camposTienda.add(campo);
        campo = new CampoForm();


        campo.type = "label";
        campo.value ="Estatus informe:"+ Constantes.ESTATUSINFORME[informeCompra.getEstatus()];

        camposTienda.add(campo);
        campo = new CampoForm();

        campo.nombre_campo = "tiendaNombre";
        campo.type = "label";
        campo.value = "Estatus envio:"+Constantes.ESTATUSSYNC[informeCompra.getEstatusSync()];
        camposTienda.add(campo);
        mViewModel.getProductoExhib().observe(getViewLifecycleOwner(), new Observer<List<ProductoExhibidoDao.ProductoExhibidoFoto>>() {
            @Override
            public void onChanged(List<ProductoExhibidoDao.ProductoExhibidoFoto> productoExhibidoFotos) {
                for(ProductoExhibidoDao.ProductoExhibidoFoto imagen:productoExhibidoFotos){
                    CampoForm campo = new CampoForm();

                    campo.nombre_campo = "tiendaNombre";
                    campo.type = "imageView";

                    campo.value =getActivity().getExternalFilesDir(null)+ "/" +  imagen.ruta;
                    camposTienda.add(campo);
                }
            }
        });


        cf1=new CreadorFormulario(camposTienda,getActivity());

    }

    public  void editar(){
        //voy a editar

    }

    @Override
    public void onClickVer(int idmuestra) {
        //ver la muestra
         Bundle bundle = new Bundle();

        bundle.putInt(NuevoinformeFragment.INFORMESEL,informeSel);
       bundle.putInt(DetalleProductoFragment1.ARG_IDMUESTRA,idmuestra);
        Fragment fragment = new VerInformeDetFragment();
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
    public void onClickCancelar(InformeCompraDetalle detalle) {

    }

    @Override
    public void onClickEditar(int id) {

    }
}