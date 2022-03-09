package com.example.comprasmu.ui.listadetalle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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

import android.widget.TextView;

import com.example.comprasmu.R;

import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.ListaWithDetalle;
import com.example.comprasmu.databinding.ListaCompraFragmentBinding;

import com.example.comprasmu.ui.informe.NuevoinformeViewModel;
import com.example.comprasmu.ui.informedetalle.DetalleProductoFragment;

import com.example.comprasmu.ui.informedetalle.DetalleProductoPenFragment;
import com.example.comprasmu.ui.informedetalle.NuevoDetalleViewModel;
import com.example.comprasmu.ui.listacompras.SelClienteFragment;
import com.example.comprasmu.ui.sustitucion.SustitucionFragment;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
    public  int opcionbu;
    String tipoconsulta;
    NuevoDetalleViewModel nuevoInf;
    private NuevoinformeViewModel niViewModel;

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
         nuevoInf=new ViewModelProvider(requireActivity()).get(NuevoDetalleViewModel.class);
        niViewModel=new ViewModelProvider(requireActivity()).get(NuevoinformeViewModel.class);

        Bundle bundle = getArguments();

        if (bundle != null) {  //pra tomar los que vienen del navhost selcleitefragment
            plantaSel = bundle.getInt(ARG_PLANTASEL) ;
            nombrePlanta = bundle.getString(ARG_NOMBREPLANTASEL);
            tipoconsulta=bundle.getString(SelClienteFragment.ARG_TIPOCONS);
//            this.nombreCliente = bundle.getString(ARG_CLIENTENOMBRE);

            mViewModel.setClienteSel(bundle.getInt(ARG_CLIENTESEL));
            clienteSel=bundle.getInt(ARG_CLIENTESEL);//ya llega como la clave
            Log.d(TAG," cliente"+clienteSel);
            MutableLiveData<Integer> consecutivo = niViewModel.getConsecutivo(plantaSel, getActivity(), this);
            //  Log.d(TAG, "*genere cons=" + consecutivo);
            consecutivo.observe(getViewLifecycleOwner(), new Observer<Integer>() {
                @Override
                public void onChanged(Integer cons) {
                    Log.d(TAG, "genere cons=" + cons);

                    consecutivoTienda = cons;


                }
            });
            if(bundle.getString(ARG_MUESTRA)!=null&&bundle.getString(ARG_MUESTRA).equals("true")) {
                //vengo de agregar muestra
                // mViewModel.setClienteSel(Integer.parseInt(clienteSel));
                mViewModel.setNuevaMuestra(true);
                //busco el consecutivo
               /* MutableLiveData<Integer> consecutivo = niViewModel.getConsecutivo(plantaSel, getActivity(), this);
                //  Log.d(TAG, "*genere cons=" + consecutivo);
                consecutivo.observe(getViewLifecycleOwner(), new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer cons) {
                        Log.d(TAG, "genere cons=" + cons);

                        consecutivoTienda = cons;


                    }
                });*/
            }
            Bundle bundle2 =getActivity().getIntent().getExtras(); //vengo del infrome

            if( bundle2!=null&&bundle2.getString(ARG_MUESTRA)!=null) {
             //   Log.d(TAG,"entre aqui??");
                mViewModel.setNuevaMuestra(true);
                //busco el consecutivo

            }
        }
        return    mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

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
       // TextView spopciones = mBinding.opc;
       // spopciones.setVisibility(View.GONE);
        mBinding.btnlcsigbu.setVisibility(View.GONE);

        //dependiendo de si es backup

        if(args!=null&&args.getBoolean(ISBACKUP)){
            isbu=args.getBoolean(ISBACKUP);
            if(isbu) {

                opcionbu=2;
                mBinding.txtlcopcionbu.setText(getString(R.string.criterio)+" "+(opcionbu-1));
                mBinding.txtlcopcionbu.setVisibility(View.VISIBLE);
                //reviso que traiga el detalle original
                if(mViewModel.getDetallebuSel()!=null) {
                  //  Log.d(TAG,"rrrrrrrrrrrrrrr"+isbu);
                    etsiglas.setText(mViewModel.listaSelec.getSiglas());
                    mBinding.txtlcplanta.setText(nombreCliente+" "+nombrePlanta+" ("+mViewModel.listaSelec.getSiglas()+")");
                    mBinding.btnlcsigbu.setText(getString(R.string.sig_criterio)+" "+opcionbu);

                    // mBinding.txtlcopcionbu.setVisibility(View.VISIBLE);
                    mBinding.btnlcsigbu.setVisibility(View.VISIBLE);
                    int idana = mViewModel.getDetallebuSel().getAnalisisId();
                    //busco los criterios
                    List<DescripcionGenerica> opcionesbu=mViewModel.cargarOpcionesAnalisis(idana);

                   /* CreadorFormulario.cargarSpinnerDescr(getContext(), spopciones, );

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

                    });*/
                    mBinding.btnlcsigbu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           // opcionbu=Integer.parseInt(mBinding.txtlcopcionbu.getText().toString());
                            nuevaConsultaBu(opcionbu,mViewModel.getDetallebuSel().getId());
                            mBinding.txtlcopcionbu.setText(getString(R.string.criterio)+" "+(opcionbu));
                            opcionbu++;
                            mBinding.btnlcsigbu.setText(getString(R.string.sig_criterio)+" "+opcionbu);


                            if(opcionbu>opcionesbu.size()){

                                mBinding.btnlcsigbu.setVisibility(View.GONE);
                            }
                        }
                    });
                    nuevaConsultaBu(1,mViewModel.getDetallebuSel().getId());

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
                    //si hay nota
                        mBinding.lonota.setVisibility(View.VISIBLE);
                        mBinding.txtlcnota2.setText(mViewModel.listaSelec.getLis_nota());
                    mBinding.txtlcnota.setText(getString(R.string.nota) + ": ");

                }
                else
                    mBinding.lonota.setVisibility(View.GONE);
                mBinding.txtlctotal.setVisibility(View.GONE);
            }
        }else { //no es bu
            mViewModel.cargarDetalles();

            mViewModel.getListas().observe(getViewLifecycleOwner(), myProducts -> {

                //   mBinding.paradebug1.setText(myProducts.size()+"");


                if (myProducts != null && myProducts.size() > 0) {
                    lista = myProducts.get(0);
                    Log.d(Constantes.TAG, "en la consulta id lista=> " + myProducts.size());
                    // mBinding.setIsLoading(false);

                    lista.listaDetalle=buscarBU(lista.listaDetalle);
                    calcularTotales(lista.listaDetalle);
                    etsiglas.setText(lista.user.getSiglas());
                   //ordeno la lista
                    Collections.sort( lista.listaDetalle, new Comparator<ListaCompraDetalle>() {
                        @Override
                        public int compare(ListaCompraDetalle lhs, ListaCompraDetalle rhs) {
                            return Integer.compare( lhs.getLid_orden(),rhs.getLid_orden());
                        }
                    });
                    //pongo el nombre
                    nombreCliente=lista.user.getClienteNombre();
                    if(ismuestra)
                    mBinding.txtlcplanta.setText(nombreCliente+" "+nombrePlanta+" ("+lista.user.getSiglas()+")");
                    else
                        mBinding.txtlcplanta.setText(nombrePlanta+" ("+lista.user.getSiglas()+")");

                    Log.d(TAG,"qqqqqqqqqqqqq"+ismuestra);
                    //cambio las cantidades si es bu
                  /*  if(listacomprasbu!=null) {
                        for(int position=0;position<lista.listaDetalle.size();position++) {
                            InformeCompraDetalle icd = isBU(lista.listaDetalle.get(position));
                            if (icd != null) {



                                int cantorig = lista.listaDetalle.get(position).getCantidad();
                                lista.listaDetalle.get(position).setCantidad(cantorig - 1);
                            }
                        }
                    }*/
                    mViewModel.setListacomprasbu(listacomprasbu);
                    mListAdapter.setListaCompraDetalleList(lista.listaDetalle, consecutivoTienda,isbu,ismuestra,lista.user.getClientesId(), listacomprasbu);
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

                    }else
                        mBinding.lonota.setVisibility(View.GONE);
                      } else {
                    //  mBinding.setIsLoading(true);
                }
                // espresso does not know how to wait for data binding's loop so we execute changes
                // sync.
                //  mBinding.executePendingBindings();
            });
        }

    }

    private void nuevaConsultaBu(int opcionsel, int detId){ //para cuando cambia el combolist
        int idlista=mViewModel.getIdListaSel();
        /*int opcionsel=1;
        if(opcionSel==null)
          opcionsel=1;//inicio con la 1
        else
            opcionsel=opcionSel.id;*/
        String categoria=mViewModel.getDetallebuSel().getCategoria();
        String productoNombre=mViewModel.getDetallebuSel().getProductoNombre();
        String empaque=mViewModel.getDetallebuSel().getEmpaque();
        int tamanio=mViewModel.getDetallebuSel().getTamanioId();
        int analisisid=mViewModel.getDetallebuSel().getAnalisisId();
        String analisis=mViewModel.getDetallebuSel().getTipoAnalisis();
        mViewModel.consultasBackup(idlista,opcionsel,categoria,productoNombre,empaque,tamanio ,analisisid,analisis,detId);
        mViewModel.getDetallebu().observe(getViewLifecycleOwner(), myProducts -> {
            if (myProducts != null && myProducts.size() > 0) {
                //ordeno la lista
                Collections.sort( myProducts, new Comparator<ListaCompraDetalle>() {
                    @Override
                    public int compare(ListaCompraDetalle lhs, ListaCompraDetalle rhs) {
                        return Integer.compare( lhs.getLid_orden(),rhs.getLid_orden());
                    }
                });
                //  Log.d(Constantes.TAG, "en la consulta id lista=> " + myProducts.getId());
                // mBinding.setIsLoading(false);
               // calcularTotales(myProducts);

                //   etsiglas.setText(lista.user.getSiglas());
                //   mViewModel.listaSelec = lista.user;
               // mBinding.txtlcplanta.setText(nombrePlanta+"("+lista.user.getSiglas()+")");
                mBinding.txtsdatosbu.setVisibility(View.GONE);
                mListAdapter.setListaCompraDetalleList(myProducts, consecutivoTienda,isbu,ismuestra,clienteSel,null);
                mListAdapter.notifyDataSetChanged();
            } else {
                mBinding.txtsdatosbu.setText(getString(R.string.sin_datosbu)+" "+opcionsel);
                mBinding.txtsdatosbu.setVisibility(View.VISIBLE);
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
        //   Log.d(TAG,"WWWWWWWWWW estoy en los totales"+listacomprasbu.size());
        //sumo los bu
        if(listacomprasbu!=null) {
            totalcomprados = totalcomprados + listacomprasbu.size();
            totalPedidos=totalPedidos+ listacomprasbu.size();
        }
        //pongo en el textview
        mBinding.setTotal(totalcomprados+"/"+totalPedidos);
        Constantes.NM_TOTALISTA=totalPedidos;

    }
    private InformeCompraDetalle isBU(ListaCompraDetalle det){

        for(InformeCompraDetalle icd: listacomprasbu) {
            //   Log.d(TAG, "--------------Se seleccionó a " + det.getListaId() + "--" + det.getId() + "--" + icd.getComprasIdbu() + "--" + icd.getComprasDetIdbu());


            if (icd.getComprasIdbu() == det.getListaId() && icd.getComprasDetIdbu() ==det.getId() )
            {
                //Log.d(TAG, "--------------Se seleccionó a " +icd.getComprasIdbu()+ "--" + det.getId()+"--"+ det.getListaId() +"--"+icd.getComprasDetIdbu());

                return icd;
            }
        }
        return null;

    }

    //aqui guardo los backups
    public List<ListaCompraDetalle> buscarBU(List<ListaCompraDetalle> listalcd){
        listacomprasbu=new ArrayList<InformeCompraDetalle>();
        for (ListaCompraDetalle lcd:listalcd
             ) {
         //  Log.d(TAG, "---- viendo si tiene bu " + lcd.getListaId() + "--" + lcd.getId());
            List<InformeCompraDetalle> comprabu = getBackup(lcd);
            if (comprabu.size() > 0) {
             //   Log.d(TAG, "es bu " + comprabu.size());

                listacomprasbu.addAll(comprabu);
                //resto al producto de la lista
                lcd.setCantidad(lcd.getCantidad()-comprabu.size());
                lcd.setComprados(lcd.getComprados()-comprabu.size());

            }
        }
        return listalcd;
    }
    @Override
    public void onDestroyView() {
        mBinding = null;
        mListAdapter = null;
        super.onDestroyView();
    }
    private void setupListAdapter() {

        //consecutivoTienda=Constantes.DP_CONSECUTIVO;
      //  consecutivoTienda=11;
        mListAdapter = new ListaCompraDetalleAdapter(mViewModel,this);
        //mBinding.detalleList.setAdapter(mListAdapter);
        mBinding.detalleList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.detalleList.setHasFixedSize(true);
        mBinding.detalleList.setAdapter(mListAdapter);

    }

   /* public void mostrarCodigos(View v){



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


  /*  }*/




    @Override
    public void onClickCallback(View v) {


       // mostrarCodigos(v);
    }

    @Override
    public void agregarMuestra(View view, ListaCompraDetalle productoSel) {
       // clienteSel= Integer.parseInt(getArguments().getInt(ARG_CLIENTESEL)+"");

        //cambio al fragmento de captura del detalle
        if (view.getId() == R.id.btnldagregar) {
           Log.d(TAG, "agregar muestra"+mViewModel.nombrePlantaSel+"--"+mViewModel.getPlantaSel());

                 // String clienteNombre=Constantes.ni_clientesel;//lo pongo hasta que se guarda el informe
            //para los bu

            if(isbu){
                //cambio el tipo de muestra y el producto
                // en detallebusel esta lainfo original

                mViewModel.getDetallebuSel().setTipoMuestra(2);
                mViewModel.getDetallebuSel().setNombreTipoMuestra("BACKUP");
                mViewModel.getDetallebuSel().setProductosId(productoSel.getProductosId());
                mViewModel.getDetallebuSel().setProductoNombre(productoSel.getProductoNombre());
                mViewModel.getDetallebuSel().setTamanioId(productoSel.getTamanioId());
                mViewModel.getDetallebuSel().setTamanio(productoSel.getTamanio());
                mViewModel.getDetallebuSel().setEmpaque(productoSel.getEmpaque());
                mViewModel.getDetallebuSel().setEmpaquesId(productoSel.getEmpaquesId());

                //  productoSel.setAnalisisId(mViewModel.getDetallebuSel().getAnalisisId());
                //productoSel.setTipoAnalisis(mViewModel.getDetallebuSel().getTipoAnalisis());
                //productoSel.setCantidad();

              //  productoSel.setProductosId(mViewModel.getDetallebuSel());


                nuevoInf.setProductoSel(mViewModel.getDetallebuSel(),mViewModel.nombrePlantaSel,mViewModel.getPlantaSel(), mViewModel.getClienteSel(),nombreCliente,etsiglas.getText().toString(),productoSel);

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
            Log.d(TAG, ">>>> "+ mViewModel.getClienteSel());

            Intent resultIntent = new Intent();

           // resultIntent.putExtra(DetalleProductoFragment.ARG_NUEVOINFORME, mViewModel.informe.getId());
           if( mViewModel.getClienteSel()==4)
            getActivity().setResult(DetalleProductoFragment.NUEVO_RESULT_OK, resultIntent);
            if( mViewModel.getClienteSel()==5)
                getActivity().setResult(DetalleProductoPenFragment.NUEVO_RESULT_OK, resultIntent);
            if( mViewModel.getClienteSel()==6)
                getActivity().setResult(DetalleProductoPenFragment.NUEVO_RESULT_OK, resultIntent);

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
        //es el detalle original
        mViewModel.setDetallebuSel(productoSel);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        // Definir una transacción
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();

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
        if(clienteSel==5||clienteSel==6) {

            Fragment fragment =  SustitucionFragment.newInstance();
            // Obtener el administrador de fragmentos a través de la actividad
            bundle.putBoolean(ISBACKUP, true);
            bundle.putString(SustitucionFragment.ARG_CATEGORIA,productoSel.getCategoria());
            bundle.putInt(SustitucionFragment.ARG_PLANTA, plantaSel);
            bundle.putString(SustitucionFragment.ARG_NOMBREPLANTA, nombrePlanta);
            bundle.putString(SustitucionFragment.ARG_SIGLAS, etsiglas.getText().toString());
            fragment.setArguments(bundle);
            Constantes.ni_clientesel=nombreCliente;
            Log.d(TAG,"di clic en bu "+nombreCliente);
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