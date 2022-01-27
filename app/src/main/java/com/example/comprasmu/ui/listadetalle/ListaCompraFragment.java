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
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.ListaWithDetalle;
import com.example.comprasmu.databinding.ListaCompraFragmentBinding;

import com.example.comprasmu.ui.informedetalle.DetalleProductoFragment;

import com.example.comprasmu.ui.informedetalle.NuevoDetalleViewModel;
import com.example.comprasmu.ui.listacompras.SelClienteFragment;
import com.example.comprasmu.ui.listacompras.TabsFragment;
import com.example.comprasmu.ui.sustitucion.SustitucionFragment;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static com.example.comprasmu.ui.listacompras.TabsFragment.ARG_CLIENTESEL;

public class ListaCompraFragment extends Fragment implements ListaCompraDetalleAdapter.AdapterCallback {


    private static final String ISBACKUP = "comprasmu.isbackup";
    private  int plantaSel;

    private ListaDetalleViewModel mViewModel;
    private ListaCompraFragmentBinding mBinding;
    private ListaCompraDetalleAdapter mListAdapter;
    TextView paraDebug;
     String nombrePlanta;
     String nombreCliente;
    ListaWithDetalle lista;
        int clienteSel;
   // private String siglas;
    TextView etsiglas;
    int consecutivoTienda;
    List<InformeCompraDetalle> listacomprasbu=null;
    private boolean isbu, ismuestra; //para saber si es reemplazo

    private static final String TAG="LISTACOMPRAFRAGMENT";
    public static final String ARG_PLANTASEL="comprasmu.lista.plantaSel";
    public static final String ARG_NOMBREPLANTASEL="comprasmu.lista.plantaNombre";
    public static final String ARG_CLIENTESEL="comprasmu.lista.clienteSel";
    public static final String ARG_CLIENTENOMBRE="comprasmu.lista.clientenombre";
    public static final String ARG_MUESTRA="comprasmu.lista.agregarmuestra";//indica si se agregará muestra

    String tipoconsulta;
    public  ListaCompraFragment() {

    }

    public  ListaCompraFragment(int planta,String onombrePlanta, String nomcliente) {
      //  ListaCompraFragment fragment = new ListaCompraFragment();
        plantaSel=planta;
        nombrePlanta=onombrePlanta;
        this.nombreCliente=nomcliente;

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding= DataBindingUtil.inflate(inflater,
                R.layout.lista_compra_fragment, container, false);

       // mViewModel = new ViewModelProvider(this).get(com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel.class);
        mViewModel=new ViewModelProvider(requireActivity()).get(ListaDetalleViewModel.class);
        Bundle bundle = getArguments();

        if (bundle != null) {  //pra tomar los que vienen del navhost selcleitefragment
            plantaSel = bundle.getInt(ARG_PLANTASEL) ;
            nombrePlanta = bundle.getString(ARG_NOMBREPLANTASEL);
            tipoconsulta=bundle.getString(SelClienteFragment.ARG_TIPOCONS);
//            this.nombreCliente = bundle.getString(ARG_CLIENTENOMBRE);

            mViewModel.setClienteSel(bundle.getInt(ARG_CLIENTESEL));
            clienteSel=bundle.getInt(ARG_CLIENTESEL);//ya llega como la clave
            Log.d(TAG," cliente"+clienteSel);

            if(bundle.getString(ARG_MUESTRA)!=null&&bundle.getString(ARG_MUESTRA).equals("true")) {
                //vengo de agregar muestra
                // mViewModel.setClienteSel(Integer.parseInt(clienteSel));
                mViewModel.setNuevaMuestra(true);

            }
        }
        return    mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle2 =getActivity().getIntent().getExtras(); //vengo del infrome

        if( bundle2!=null&&bundle2.getString(ARG_MUESTRA)!=null) {
            Log.d(TAG,"entre aqui??");
            mViewModel.setNuevaMuestra(true);
        }
        Log.d(TAG," plantas"+plantaSel+"--"+nombrePlanta);

        mViewModel.setPlantaSel(plantaSel);
        mViewModel.nombrePlantaSel=nombrePlanta;
        Bundle args=getArguments();
       // if(args!=null)

        ismuestra=mViewModel.isNuevaMuestra();
        Log.d(TAG,"cliente"+ mViewModel.getClienteSel());
        TextView etindice=mBinding.getRoot().findViewById(R.id.txtlcindice);
        etsiglas=mBinding.getRoot().findViewById(R.id.txtlcsiglas);
        etindice.setText(ComprasUtils.indiceLetra(Constantes.INDICEACTUAL));
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

        if(args!=null&&args.getBoolean(ISBACKUP)){
            isbu=args.getBoolean(ISBACKUP);
            if(isbu) {
                //reviso que traiga el detalle
                if(mViewModel.getDetallebuSel()!=null) {
                  //  Log.d(TAG,"rrrrrrrrrrrrrrr"+isbu);
                    etsiglas.setText(mViewModel.listaSelec.getSiglas());
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
                if( mViewModel.listaSelec.getLis_nota().length()>2) {
                    //acorto la nota
                  /*  if( mViewModel.listaSelec.getLis_nota().length()>20){
                        mBinding.txtlcnota2.setText(mViewModel.listaSelec.getLis_nota().substring(0,20)+"...ver más");
                        mBinding.txtlcnota2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mBinding.txtlcnota2.setText(mViewModel.listaSelec.getLis_nota());

                            }
                        });

                    }
                    else*/
                        mBinding.txtlcnota2.setText(mViewModel.listaSelec.getLis_nota());
                    mBinding.txtlcnota.setText(getString(R.string.nota) + ": ");

                }
            }
        }else { //no es bu
            mViewModel.cargarDetalles();

            mViewModel.getListas().observe(getViewLifecycleOwner(), myProducts -> {

                //   mBinding.paradebug1.setText(myProducts.size()+"");


                if (myProducts != null && myProducts.size() > 0) {
                    lista = myProducts.get(0);
                    Log.d(Constantes.TAG, "en la consulta id lista=> " + myProducts.size());
                    // mBinding.setIsLoading(false);

                    buscarBU(lista.listaDetalle);
                    calcularTotales(lista.listaDetalle);
                    etsiglas.setText(lista.user.getSiglas());
                    //pongo el nombre
                    Collections.sort( lista.listaDetalle, new Comparator<ListaCompraDetalle>() {
                        @Override
                        public int compare(ListaCompraDetalle lhs, ListaCompraDetalle rhs) {
                            return Integer.compare( rhs.getId(),lhs.getId());
                        }
                    });
                    nombreCliente=lista.user.getClienteNombre();
                    mBinding.txtlcplanta.setText(nombrePlanta+"("+lista.user.getSiglas()+")");
                    mListAdapter.setListaCompraDetalleList(lista.listaDetalle, consecutivoTienda,isbu,ismuestra,lista.user.getClienteNombre(), listacomprasbu);
                    mListAdapter.notifyDataSetChanged();
                    if(lista.user.getLis_nota()!=null&&lista.user.getLis_nota().length()>2) {
                       /* if(lista.user.getLis_nota().length()>20) {

                                mBinding.txtlcnota2.setText(lista.user.getLis_nota().substring(0,20)+"...ver más");
                            mBinding.txtlcnota2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mBinding.txtlcnota2.setText(lista.user.getLis_nota());

                                }
                            });
                            }
                        else*/
                            mBinding.txtlcnota2.setText(lista.user.getLis_nota());
                        mBinding.txtlcnota.setText(getString(R.string.nota) + ": ");

                    }
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
               // mBinding.txtlcplanta.setText(nombrePlanta+"("+lista.user.getSiglas()+")");

                mListAdapter.setListaCompraDetalleList(myProducts, consecutivoTienda,isbu,ismuestra,nombreCliente,null);
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
        //sumo los bu
        if(listacomprasbu!=null)
            totalcomprados=totalcomprados+listacomprasbu.size();
        //pongo en el textview
        mBinding.setTotal(totalcomprados+"/"+totalPedidos);
        Constantes.NM_TOTALISTA=totalPedidos;

    }


    //aqui guardo los backups
    public void buscarBU(List<ListaCompraDetalle> listalcd){
        listacomprasbu=new ArrayList<InformeCompraDetalle>();
        for (ListaCompraDetalle lcd:listalcd
             ) {


            Log.d(TAG, "--------------Se seleccionó a " + lcd.getListaId() + "--" + lcd.getId());
            List<InformeCompraDetalle> comprabu = getBackup(lcd);
            if (comprabu.size() > 0) {
                Log.d(TAG, "es bu " + comprabu.size());

                listacomprasbu.add(comprabu.get(0));

            }
        }

    }
    @Override
    public void onDestroyView() {
        mBinding = null;
        mListAdapter = null;
        super.onDestroyView();
    }
    private void setupListAdapter() {

        consecutivoTienda=Constantes.DP_CONSECUTIVO;
      //  consecutivoTienda=11;
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
            Log.d(TAG, "cliente sel "+ mViewModel.getClienteSel()+"--"+nombreCliente);

            NuevoDetalleViewModel nuevoInf=new ViewModelProvider(requireActivity()).get(NuevoDetalleViewModel.class);
               // String clienteNombre=Constantes.ni_clientesel;//lo pongo hasta que se guarda el informe
            //para los bu

            if(isbu){
                //cambio el tipo de muestra
                productoSel.setTipoMuestra(2);
                productoSel.setNombreTipoMuestra("BACKUP");
                nuevoInf.setProductoSel(productoSel,mViewModel.nombrePlantaSel,mViewModel.getPlantaSel(), mViewModel.getClienteSel(),nombreCliente,etsiglas.getText().toString(),mViewModel.getDetallebuSel());

            }
            else
            nuevoInf.setProductoSel(productoSel,mViewModel.nombrePlantaSel,mViewModel.getPlantaSel(), mViewModel.getClienteSel(),nombreCliente,etsiglas.getText().toString(),null);
            Constantes.productoSel=nuevoInf.productoSel;

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

           // resultIntent.putExtra(DetalleProductoFragment.ARG_NUEVOINFORME, mViewModel.informe.getId());
            getActivity().setResult(DetalleProductoFragment.NUEVO_RESULT_OK, resultIntent);

            //regreso al informe
            getActivity().finish();
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
        mViewModel.listaSelec=lista.user;
        mViewModel.setDetallebuSel(productoSel);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        // Definir una transacción
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        Log.d(TAG,"di clic en bu "+clienteSel);
        if(clienteSel==4) {
            Fragment fragment = new ListaCompraFragment(plantaSel, nombrePlanta, this.nombreCliente);
// Obtener el administrador de fragmentos a través de la actividad
            bundle.putInt(ListaCompraFragment.ARG_PLANTASEL,plantaSel );
            bundle.putString(ListaCompraFragment.ARG_NOMBREPLANTASEL, nombrePlanta);
            bundle.putString(SelClienteFragment.ARG_TIPOCONS, tipoconsulta);
         //   bundle.putInt(DetalleProductoFragment.NUMMUESTRA,);
            bundle.putString(ListaCompraFragment.ARG_MUESTRA, "true");
            bundle.putInt(ListaCompraFragment.ARG_CLIENTESEL,clienteSel );
            bundle.putBoolean(ISBACKUP, true);
            fragment.setArguments(bundle);

            fragmentTransaction.replace(R.id.back_fragment, fragment);
            fragmentTransaction.addToBackStack(null);
// Cambiar
            fragmentTransaction.commit();
        }else
        if(clienteSel==5) {
            Fragment fragment =  SustitucionFragment.newInstance(plantaSel, nombrePlanta);
            // Obtener el administrador de fragmentos a través de la actividad
            bundle.putBoolean(ISBACKUP, true);
            bundle.putString(SustitucionFragment.ARG_CATEGORIA,productoSel.getCategoria());
            fragment.setArguments(bundle);

            fragmentTransaction.replace(R.id.back_fragment, fragment);
            fragmentTransaction.addToBackStack(null);
// Cambiar
            fragmentTransaction.commit();
        }

    }

    @Override
    public List<InformeCompraDetalle> getBackup(ListaCompraDetalle productoSel) {
        //busco si tiene un backup
        List<InformeCompraDetalle> compra=mViewModel.tieneBackup(productoSel.getListaId(),productoSel.getId());

        return compra;
    }
}