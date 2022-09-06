package com.example.comprasmu.ui.informe;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.comprasmu.R;
import com.example.comprasmu.data.dao.ProductoExhibidoDao;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeWithDetalle;
import com.example.comprasmu.data.modelos.Visita;
import com.example.comprasmu.databinding.VerInformeFragmentBinding;
import com.example.comprasmu.ui.BackActivity;
import com.example.comprasmu.ui.RevisarFotoActivity;

import com.example.comprasmu.ui.gallery.GalFotosFragment;
import com.example.comprasmu.ui.informedetalle.InformeDetalleAdapter;
import com.example.comprasmu.ui.informedetalle.VerInformeDetFragment;
import com.example.comprasmu.ui.visita.AbririnformeFragment;
import com.example.comprasmu.utils.CampoForm;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

public class VerInformeFragment extends Fragment implements InformeDetalleAdapter.AdapterCallback {

    public static final String ARG_IDMUESTRA = "comprasmu.ni_idmuestra";
    private VerInformeViewModel mViewModel;
    private int informeSel;
    InformeCompra informeCompra;
    private CreadorFormulario cf1;
    private CreadorFormulario cf2;
    ImagenDetalle fotocondiciones;
    ImagenDetalle fotoFachada;
    CampoForm campo2;
    private VerInformeFragmentBinding mBinding;
    private InformeDetalleAdapter mListAdapter;
    private static String TAG = "VerInformeFragment";

    private int cliente;

    String directorio;

    public static VerInformeFragment newInstance() {
        return new VerInformeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.ver_informe_fragment, container, false);

        //   mBinding.setMviewModel(mViewModel);
        mBinding.setLifecycleOwner(this);
        mViewModel = new ViewModelProvider(this).get(VerInformeViewModel.class);
        mBinding.setDirectorio(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/");
        directorio = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/";

        mBinding.btnvifotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verFotos();
            }
        });
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle datosRecuperados = getActivity().getIntent().getExtras();

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


    }

    public void llenarDetalle() {
        Log.d(TAG, "llenndo detalles");
        mViewModel.informeCompraSel.observe(getViewLifecycleOwner(), new Observer<InformeWithDetalle>() {
            @Override
            public void onChanged(InformeWithDetalle informeWithDetalle) {
                if (mViewModel.informeCompraSel.getValue() != null) {
                    mListAdapter.setInformeCompraDetalleList(mViewModel.informeCompraSel.getValue().informeDetalle, true);
                    mListAdapter.notifyDataSetChanged();
                    Log.d(TAG, "tengo el informe" + informeWithDetalle.informe.getId());
                    mBinding.setInforme(informeWithDetalle.informe);
                    cliente = informeWithDetalle.informe.getClientesId();
                    mViewModel.getVisita(informeWithDetalle.informe.getVisitasId()).observe(getViewLifecycleOwner(), new Observer<Visita>() {
                        @Override
                        public void onChanged(Visita visita) {
                            //llenarFotos(informeWithDetalle.informe, visita);
                            crearFormulario();
                            mBinding.vidatosgen.addView(cf1.crearTabla());
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

    public void crearFormulario() {

        informeCompra = mViewModel.getInformeCompraSel().getValue().informe;

        List<CampoForm> camposTienda = new ArrayList<CampoForm>();
        CampoForm campo = new CampoForm();

        campo.label = getString(R.string.consecutivo);
        campo.style = R.style.verinforme2;
        campo.type = "label";
        campo.value = informeCompra.getConsecutivo() + "";
        camposTienda.add(campo);
        campo = new CampoForm();

        campo.style = R.style.verinforme2;
        campo.label = getString(R.string.indice);
        campo.type = "label";
        campo.value = ComprasUtils.indiceLetra(mViewModel.getVisita().getValue().getIndice());

        camposTienda.add(campo);
        campo = new CampoForm();

        campo.style = R.style.verinforme2;
        campo.label = getString(R.string.fecha);
        campo.type = "label";
        campo.value = Constantes.vistasdf.format(mViewModel.getVisita().getValue().getCreatedAt());

        camposTienda.add(campo);
        campo = new CampoForm();
        campo.label = getString(R.string.ciudad);
        campo.style = R.style.verinforme2;
        campo.type = "label";
        campo.value = mViewModel.getVisita().getValue().getCiudad();

        camposTienda.add(campo);
        campo = new CampoForm();
        campo.label = getString(R.string.cliente);
        campo.style = R.style.verinforme2;
        campo.type = "label";
        campo.value = informeCompra.getClienteNombre();


        camposTienda.add(campo);
        campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.label = getString(R.string.planta);
        campo.type = "label";
        campo.value = informeCompra.getPlantaNombre();


        camposTienda.add(campo);

        campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.nombre_campo = "tiendaNombre";
        campo.label = getString(R.string.nombre_tienda);
        campo.type = "label";
        campo.value = mViewModel.getVisita().getValue().getTiendaNombre();

        camposTienda.add(campo);
        campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.nombre_campo = "tipoTienda";
        campo.label = getString(R.string.tipo_tienda);
        campo.type = "label";
        campo.value = mViewModel.getVisita().getValue().getTipoTienda();

        camposTienda.add(campo);
        campo = new CampoForm();
        campo.label = getString(R.string.direccion);
        campo.type = "label";
        campo.style = R.style.verinforme2;
        campo.value = mViewModel.getVisita().getValue().getDireccion();
        camposTienda.add(campo);
        campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.nombre_campo = "complementodireccion";
        campo.label = getString(R.string.complementodir);
        campo.type = "label";
        campo.value = mViewModel.getVisita().getValue().getComplementodireccion();
        camposTienda.add(campo);

        campo = new CampoForm();
        campo.label = getString(R.string.puntocardinal);
        campo.type = "label";
        campo.style = R.style.verinforme2;
        if (mViewModel.getVisita().getValue().getPuntoCardinal() != null && !mViewModel.getVisita().getValue().getPuntoCardinal().equals("") && !mViewModel.getVisita().getValue().getPuntoCardinal().equals("0")) {
            campo.value = Constantes.PUNTOCARDINAL[Integer.parseInt(mViewModel.getVisita().getValue().getPuntoCardinal()) - 1];
        }

        camposTienda.add(campo);

      /*  campo = new CampoForm();

        campo.label=getString(R.string.comentarios);
        campo.type = "label";
        campo.value =informeCompra.getComentarios()+"";

        camposTienda.add(campo);*/
      /*  campo = new CampoForm();


        campo.label=getString(R.string.estatus);
        campo.type = "label";
        campo.value = Constantes.ESTATUSINFORME[informeCompra.getEstatus()];

        camposTienda.add(campo);*/
        campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.nombre_campo = "tiendaNombre";
        campo.label = getString(R.string.estatus_envio);
        campo.type = "label";
        campo.value = Constantes.ESTATUSSYNC[informeCompra.getEstatusSync()];
        camposTienda.add(campo);
        /*        mViewModel.getProductoExhib().observe(getViewLifecycleOwner(), new Observer<List<ProductoExhibidoDao.ProductoExhibidoFoto>>() {
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
        });*/


        cf1 = new CreadorFormulario(camposTienda, getActivity());

    }

    public void editar() {
        //voy a editar

    }

    public void verFotos() {
        Bundle bundle = new Bundle();
        bundle.putInt(NuevoinformeFragment.INFORMESEL,informeSel);
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

    public void verImagen(String nombrearch){
      //  ImageView imagen=(ImageView)v;
       // imagen.get
        Intent iverim=new Intent(getActivity(), RevisarFotoActivity.class);
        iverim.putExtra(RevisarFotoActivity.IMG_PATH1,nombrearch);
        startActivity(iverim);
    }


    @Override
    public void onClickVer(int idmuestra) {
        //ver la muestra
        Bundle bundle = new Bundle();

        bundle.putInt(NuevoinformeFragment.INFORMESEL,informeSel);
        bundle.putInt(NuevoinformeFragment.ARG_CLIENTEINFORME,cliente);
       bundle.putInt(ARG_IDMUESTRA,idmuestra);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mViewModel=null;

        cf2=null;

         informeCompra=null;
        cf1=null;
         fotocondiciones=null;
         fotoFachada=null;
         campo2=null;
         mBinding=null;
         mListAdapter=null;

         directorio=null;

    }

}