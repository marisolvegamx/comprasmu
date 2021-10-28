package com.example.comprasmu.ui.listadetalle;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.comprasmu.R;

import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.ListaWithDetalle;
import com.example.comprasmu.databinding.ListaCompraFragmentBinding;
import com.example.comprasmu.ui.BackActivity;
import com.example.comprasmu.ui.informe.NuevoinformeFragment;
import com.example.comprasmu.ui.informe.NuevoinformeViewModel;
import com.example.comprasmu.ui.informedetalle.DetalleProductoFragment1;
import com.example.comprasmu.ui.informedetalle.NuevoDetalleViewModel;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;

import java.util.List;

import static com.example.comprasmu.ui.listacompras.TabsFragment.ARG_CLIENTESEL;

public class ListaCompraFragment extends Fragment implements ListaCompraDetalleAdapter.AdapterCallback {


    private static final String ISBACKUP = "comprasmu.isbackup";
    private static int plantaSel;

    private ListaDetalleViewModel mViewModel;
    private ListaCompraFragmentBinding mBinding;
    private ListaCompraDetalleAdapter mListAdapter;
    TextView paraDebug;
    static String nombrePlanta;
    ListaWithDetalle lista;

    private String siglas;
    TextView etsiglas;
    int consecutivoTienda;

    private boolean isbu, ismuestra; //para saber si es reemplazo

    private static final String TAG="LISTACOMPRAFRAGMENT";



    public static ListaCompraFragment newInstance(int planta,String onombrePlanta) {
        ListaCompraFragment fragment = new ListaCompraFragment();
        plantaSel=planta;
        nombrePlanta=onombrePlanta;
        return fragment;


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding= DataBindingUtil.inflate(inflater,
                R.layout.lista_compra_fragment, container, false);

       // mViewModel = new ViewModelProvider(this).get(com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel.class);
        mViewModel=new ViewModelProvider(requireActivity()).get(ListaDetalleViewModel.class);

        return    mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("aquiiiiii","********"+mViewModel.isNuevaMuestra());
        mViewModel.setPlantaSel(plantaSel);
        mViewModel.nombrePlantaSel=nombrePlanta;
        Bundle bundle2 =getActivity().getIntent().getExtras();
        if(bundle2!=null)
        mViewModel.setClienteSel(bundle2.getInt(ARG_CLIENTESEL));
        ismuestra=mViewModel.isNuevaMuestra();
        Log.d(Constantes.TAG,"cliente y planta sel"+mViewModel.isNuevaMuestra());
        TextView etindice=mBinding.getRoot().findViewById(R.id.txtlcindice);
        etsiglas=mBinding.getRoot().findViewById(R.id.txtlcsiglas);
        etindice.setText(Constantes.INDICEACTUAL);
        TextView etciudad=mBinding.getRoot().findViewById(R.id.txtlcciudad);
        etciudad.setText(mViewModel.nombreCiudadSel);
        TextView ettotal=mBinding.getRoot().findViewById(R.id.txtlctotal);


        // mBinding.setLcviewModel(mViewModel);
        mBinding.setLifecycleOwner(this);
        setupListAdapter();
        Spinner spopciones = mBinding.splcopcionesbu;
        spopciones.setVisibility(View.GONE);
        mBinding.txtlcelegir.setVisibility(View.GONE);
        //dependiendo de si es backup
        Bundle args=getArguments();
        if(args!=null&&args.getBoolean(ISBACKUP)){
            isbu=args.getBoolean(ISBACKUP);
            if(isbu) {
                //reviso que traiga el detalle
                if(mViewModel.getDetallebuSel()!=null) {
                    Log.d(TAG,"rrrrrrrrrrrrrrr"+isbu);

                    spopciones.setVisibility(View.VISIBLE);
                    mBinding.txtlcelegir.setVisibility(View.VISIBLE);
                    int idana = mViewModel.getDetallebuSel().getAnalisisId();

                    CreadorFormulario.cargarSpinnerDescr(getContext(), spopciones,  mViewModel.cargarOpcionesAnalisis(idana));

                    spopciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            DescripcionGenerica opcion = (DescripcionGenerica) parentView.getSelectedItem();

                            nuevaConsultaBu(opcion);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // your code here
                        }

                    });

                    nuevaConsultaBu(null);

                }else
                {
                    Log.e(TAG,"algo salió mal");
                }
            }
        }else {
            mViewModel.cargarDetalles();

            mViewModel.getListas().observe(getViewLifecycleOwner(), myProducts -> {

                //   mBinding.paradebug1.setText(myProducts.size()+"");


                if (myProducts != null && myProducts.size() > 0) {
                    lista = myProducts.get(0);
                    Log.d(Constantes.TAG, "en la consulta id lista=> " + lista.user.getId());
                    // mBinding.setIsLoading(false);
                    calcularTotales(lista.listaDetalle);
                    etsiglas.setText(lista.user.getSiglas());
                    mViewModel.listaSelec = lista.user;
                    mListAdapter.setListaCompraDetalleList(lista.listaDetalle, consecutivoTienda,isbu,ismuestra);
                    mListAdapter.notifyDataSetChanged();
                } else {
                    //  mBinding.setIsLoading(true);
                }
                // espresso does not know how to wait for data binding's loop so we execute changes
                // sync.
                //  mBinding.executePendingBindings();
            });
        }
    }

    private void nuevaConsultaBu(DescripcionGenerica opcionSel){ //para cuando cambia el combolist
        int idlista=mViewModel.getIdListaSel();
        int opcionsel=1;
        if(opcionSel==null)
          opcionsel=1;//inicio con la 1
        else
            opcionsel=opcionSel.id;
        String categoria=mViewModel.getDetallebuSel().getCategoria();
        String productoNombre=mViewModel.getDetallebuSel().getProductoNombre();
        String empaque=mViewModel.getDetallebuSel().getEmpaque();
        String tamanio=mViewModel.getDetallebuSel().getTamanio();
        int analisisid=mViewModel.getDetallebuSel().getAnalisisId();
        String analisis=mViewModel.getDetallebuSel().getTipoAnalisis();
        mViewModel.consultasBackup(idlista,opcionsel,categoria,productoNombre,empaque,tamanio ,analisisid,analisis);
        mViewModel.getDetallebu().observe(getViewLifecycleOwner(), myProducts -> {
            if (myProducts != null && myProducts.size() > 0) {

                //  Log.d(Constantes.TAG, "en la consulta id lista=> " + myProducts.getId());
                // mBinding.setIsLoading(false);
                calcularTotales(myProducts);
                //   etsiglas.setText(lista.user.getSiglas());
                //   mViewModel.listaSelec = lista.user;
                mListAdapter.setListaCompraDetalleList(myProducts, consecutivoTienda,isbu,ismuestra);
                mListAdapter.notifyDataSetChanged();
            } else {
                //  mBinding.setIsLoading(true);
            }
            // espresso does not know how to wait for data binding's loop so we execute changes
            // sync.
            //  mBinding.executePendingBindings();
        });
    }

    public void calcularTotales(List<ListaCompraDetalle> detalles)
    {
        int totalPedidos=0;
        int totalcomprados=0;
        for(ListaCompraDetalle detalle:detalles){
            totalcomprados=totalcomprados+detalle.getComprados();
            totalPedidos=totalPedidos+detalle.getCantidad();
        }
        //pongo en el textview
        mBinding.setTotal(totalcomprados+"/"+totalPedidos);

    }
    @Override
    public void onDestroyView() {
        mBinding = null;
        mListAdapter = null;
        super.onDestroyView();
    }
    private void setupListAdapter() {
        //TODO falta traer el consecutivo
        consecutivoTienda=11;

        mListAdapter = new ListaCompraDetalleAdapter(mViewModel,this);
        //mBinding.detalleList.setAdapter(mListAdapter);
        mBinding.detalleList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.detalleList.setHasFixedSize(true);
        mBinding.detalleList.setAdapter(mListAdapter);

    }

    public void mostrarCodigos(View v){



          /*  AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
            dialogo1.setTitle(R.string.codigos_no_per);
            dialogo1.setMessage(codigos);
            dialogo1.setCancelable(true);

            dialogo1.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    dialogo1.cancel();
                }
            });
            dialogo1.show();*/


    }




    @Override
    public void onClickCallback(View v) {


        mostrarCodigos(v);
    }

    @Override
    public void agregarMuestra(View view, ListaCompraDetalle productoSel) {
       // clienteSel= Integer.parseInt(getArguments().getInt(ARG_CLIENTESEL)+"");

        //cambio al fragmento de captura del detalle
        if (view.getId() == R.id.btnldagregar) {
            Log.d(TAG, "agregar muestra"+mViewModel.nombrePlantaSel+"--"+mViewModel.getPlantaSel());

            NuevoDetalleViewModel nuevoInf=new ViewModelProvider(requireActivity()).get(NuevoDetalleViewModel.class);
                String clienteNombre=Constantes.ni_clientesel;//lo pongo hasta que se guarda el informe
            //para los bu

            if(isbu){
                //cambio el tipo de muestra
                productoSel.setTipoMuestra(2);
                productoSel.setNombreTipoMuestra("Backup");
            }
            nuevoInf.setProductoSel(productoSel,mViewModel.nombrePlantaSel,mViewModel.getPlantaSel(), mViewModel.getClienteSel(),clienteNombre,mViewModel.listaSelec.getSiglas());

            Fragment fragment = new DetalleProductoFragment1();
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

        }
    }

    @Override
    public void verBackup(ListaCompraDetalle productoSel) {
//        Intent intento1=new Intent(getActivity(), BackActivity.class);
//        intento1.putExtra(BackActivity.ARG_FRAGMENT,BackActivity.OP_LISTACOMPRA);
//        intento1.putExtra("ciudadSel",mViewModel.ciudadSel);
//        intento1.putExtra("ciudadNombre",mViewModel.nombreCiudadSel);
//        intento1.putExtra(ISBACKUP, true);
//        intento1.putExtra(ARG_CLIENTESEL, mViewModel.getClienteSel());
//     //   intento1.putExtra(NuevoinformeFragment.NUMMUESTRA,nummuestra );
//
//        startActivity(intento1);

       //paso los params que necesito
        mViewModel.setIdListaSel(lista.user.getId());
        mViewModel.setDetallebuSel(productoSel);
        Fragment fragment = new ListaCompraFragment();
// Obtener el administrador de fragmentos a través de la actividad
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
// Definir una transacción
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle=new Bundle();
        bundle.putBoolean(ISBACKUP, true);
        fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.back_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
// Cambiar
        fragmentTransaction.commit();

    }
}