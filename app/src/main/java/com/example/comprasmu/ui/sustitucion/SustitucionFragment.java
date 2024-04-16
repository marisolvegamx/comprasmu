package com.example.comprasmu.ui.sustitucion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.comprasmu.R;

import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.Sustitucion;

import com.example.comprasmu.databinding.ListaGenericFragmentBinding;
import com.example.comprasmu.ui.informedetalle.DetalleProductoElecFragment;
import com.example.comprasmu.ui.informedetalle.DetalleProductoFragment;
import com.example.comprasmu.ui.informedetalle.DetalleProductoJumFragment;
import com.example.comprasmu.ui.informedetalle.DetalleProductoPenFragment;
import com.example.comprasmu.ui.informedetalle.NuevoDetalleViewModel;
import com.example.comprasmu.ui.listacompras.PlaceholderFragment;
import com.example.comprasmu.ui.listadetalle.ListaCompraFragment;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.ui.mantenimiento.CiudadTrabajoFragment;
import com.example.comprasmu.utils.Constantes;


public class SustitucionFragment extends Fragment implements SustitucionAdapter.AdapterCallback {


    public static final String ISBACKUP = "comprasmu.sustitucion.isbackup";
    public static final String ARG_CATEGORIA = "comprasmu.sustitucion.categoria";
    public static final String ARG_CONSTIENDA = "comprasmu.sustitucion.constienda";
    public static final String ARG_CATEGORIAID = "comprasmu.sustitucion.categoriaid";


    private SustitucionViewModel mViewModel;
    private ListaDetalleViewModel ldViewModel;
    private ListaGenericFragmentBinding mBinding;
    private SustitucionAdapter mListAdapter;
    String nombrePlanta;
    int plantaSel;

    private boolean isbu;
    private String ismuestra; //para saber si es reemplazo

    public static final String ARG_SIGLAS = "comprasmu.sustsiglas";

    private static final String TAG="SUSTITUCIONFRAGMENT";
    private int categoriaSel;
    private String siglas;
    private int clienteSel,tamanio,empaque, productoId;
    private int numTienda;

    public static SustitucionFragment newInstance() {
        //  ListaCompraFragment fragment = new ListaCompraFragment();
        //   Log.d(TAG,"planta sel"+planta);
        // Log.d(TAG,"nombre"+onombrePlanta);
        SustitucionFragment fragment = new SustitucionFragment();
        // Bundle bundle = new Bundle();
        //fragment.setArguments(bundle);
        return fragment;

    }


    public SustitucionFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle2 =getArguments();
        if(bundle2!=null)
        {
            categoriaSel=bundle2.getInt(ARG_CATEGORIAID);
            //  Log.d(TAG,"ZZZ"+bundle2.getString(ARG_CATEGORIA));
            plantaSel = bundle2.getInt(ListaCompraFragment.ARG_PLANTASEL);
            if(Constantes.VarListCompra.detallebuSel!=null)
            {
                tamanio= Constantes.VarListCompra.detallebuSel.getTamanioId();
                empaque= Constantes.VarListCompra.detallebuSel.getEmpaquesId();
                productoId= Constantes.VarListCompra.detallebuSel.getProductosId();
            }
            clienteSel= bundle2.getInt(ListaCompraFragment.ARG_CLIENTESEL);

            nombrePlanta = bundle2.getString(ListaCompraFragment.ARG_NOMBREPLANTASEL);
            siglas = bundle2.getString(ARG_SIGLAS);
            ismuestra=bundle2.getString(ListaCompraFragment.ARG_MUESTRA);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding= DataBindingUtil.inflate(inflater,
                R.layout.lista_generic_fragment, container, false);

        // mViewModel = new ViewModelProvider(this).get(com.example.comprasmu.ui.Sustitucion.SustitucionViewModel.class);
        mViewModel=new ViewModelProvider(requireActivity()).get(SustitucionViewModel.class);
        ldViewModel=new ViewModelProvider(requireActivity()).get(ListaDetalleViewModel.class);

        return    mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"planta sel"+plantaSel);
        Log.d(TAG,"cat"+categoriaSel+"--cli"+clienteSel);
        mBinding.setLifecycleOwner(this);
        Constantes.VarListCompra.plantaSel=plantaSel;
        setupListAdapter();
        //Constantes.VarListCompra.detallebuSel.getAnalisisId()
        Bundle bundle2 =getArguments();
        if(bundle2!=null) {
            numTienda = bundle2.getInt(ARG_CONSTIENDA);
        }
        mViewModel.cargarListas(plantaSel,categoriaSel,clienteSel,empaque,tamanio,numTienda,productoId);
        mViewModel.getListas().observe(getViewLifecycleOwner(), myProducts -> {
            if (myProducts != null && myProducts.size() > 0) {
                Log.d(Constantes.TAG, "en la consulta de sust=> " + myProducts.get(0).getId_sustitucion());
                mListAdapter.setSustitucionList(myProducts,mViewModel);
                mListAdapter.notifyDataSetChanged();
            }

        });

    }

    @Override
    public void onDestroyView() {
        mBinding = null;
        mListAdapter = null;
        mViewModel = null;
        ldViewModel = null;
        nombrePlanta = null;
        ismuestra = null;
        categoriaSel = 0;
        siglas = null;

        super.onDestroyView();
    }
    private void setupListAdapter() {
        mListAdapter = new SustitucionAdapter(ismuestra,this);

        mBinding.detalleList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.detalleList.setHasFixedSize(true);
        mBinding.detalleList.setAdapter(mListAdapter);

    }

    @Override
    public void agregarMuestra(View view, Sustitucion productoSel) {

        //cambio al fragmento de captura del detalle
        if (view.getId() == R.id.btnldagregar) {
            Log.d(TAG, "agregar muestra"+productoSel.getNomproducto());
            if(productoSel.getClientesId()==7) { //valido que no exista en la compra
                if(!productoSel.getNomproducto().equals("")&&productoSel.getNomproducto().contains("FRUTZZO")){
                    //PUEDO COMPRAR
                }else
                if (mViewModel.validarProdJum(Constantes.INDICEACTUAL, plantaSel, productoSel)) {
                    Toast.makeText(getActivity(), getString(R.string.err_mismo_prod), Toast.LENGTH_LONG).show();
                    return;
                }
            }
            NuevoDetalleViewModel nuevoInf=new ViewModelProvider(requireActivity()).get(NuevoDetalleViewModel.class);
            String clienteNombre=Constantes.ni_clientesel;//lo pongo hasta que se guarda el informe
            //para los bu
            //productoSel.setTipoMuestra(2);
            //productoSel.setNombreTipoMuestra("BACKUP");
            //productoSel.setAnalisisId(mViewModel.getDetallebuSel().getAnalisisId());
            //productoSel.setTipoAnalisis(mViewModel.getDetallebuSel().getTipoAnalisis());
            //me faltan las siglas
            //cambio el tipo de muestra y el producto
            // en detallebusel esta lainfo original

            ldViewModel.getDetallebuSel().setTipoMuestra(3);
            ldViewModel.getDetallebuSel().setNombreTipoMuestra("BACKUP");
            ldViewModel.getDetallebuSel().setProductosId(productoSel.getSu_producto());
            ldViewModel.getDetallebuSel().setProductoNombre(productoSel.getNomproducto());
            ldViewModel.getDetallebuSel().setTamanioId(productoSel.getSu_tamanio());
            ldViewModel.getDetallebuSel().setTamanio(productoSel.getNomtamanio());
            ldViewModel.getDetallebuSel().setEmpaque(productoSel.getNomempaque());
            ldViewModel.getDetallebuSel().setEmpaquesId(productoSel.getSu_tipoempaque());
            nuevoInf.setProductoSelSust(ldViewModel.getDetallebuSel(),nombrePlanta,plantaSel, ldViewModel.getClienteSel(),clienteNombre,siglas,productoSel);
            Constantes.productoSel=nuevoInf.productoSel;
            //  Constantes.NM_TOTALISTA=mListAdapter.getItemCount();
    /*        Fragment fragment = new DetalleProductoFragment1();
// Obtener el administrador de fragmentos a través de la actividad
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
// Definir una transacción
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
// Remplazar el contenido principal por el fragmento
            fragmentTransaction.replace(R.id.back_fragment, fragment);
            fragmentTransaction.addToBackStack(null);
// Cambiar
            fragmentTransaction.commit();
          //  fragmentManager.beginTransaction().remove(this).commitAllowingStateLoss();
*/

            Intent resultIntent = new Intent();
            if(ldViewModel.getClienteSel()==5)
                // resultIntent.putExtra(DetalleProductoFragment.ARG_NUEVOINFORME, mViewModel.informe.getId());
                getActivity().setResult(DetalleProductoPenFragment.NUEVO_RESULT_OK, resultIntent);
            if(ldViewModel.getClienteSel()==6)
                // resultIntent.putExtra(DetalleProductoFragment.ARG_NUEVOINFORME, mViewModel.informe.getId());
                getActivity().setResult(DetalleProductoElecFragment.NUEVO_RESULT_OK, resultIntent);
            if(ldViewModel.getClienteSel()==7)
                // resultIntent.putExtra(DetalleProductoFragment.ARG_NUEVOINFORME, mViewModel.informe.getId());
                getActivity().setResult(DetalleProductoJumFragment.NUEVO_RESULT_OK, resultIntent);

            //regreso al informe
            getActivity().finish();
        }
    }


}