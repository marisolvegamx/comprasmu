package com.example.comprasmu.ui.gasto;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.SubirInformeEnvTask;
import com.example.comprasmu.SubirInformeGastoTask;
import com.example.comprasmu.data.modelos.Atributo;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeEnvioDet;
import com.example.comprasmu.data.modelos.InformeEnvioPaq;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.modelos.InformeGastoDet;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.remote.InformeEnvPaqEnv;
import com.example.comprasmu.data.remote.InformeGastoEnv;
import com.example.comprasmu.databinding.FragmentNvogastoBinding;
import com.example.comprasmu.databinding.VerEmpaqueFragmentBinding;
import com.example.comprasmu.services.SubirFotoService;
import com.example.comprasmu.ui.RevisarFotoActivity;
import com.example.comprasmu.ui.infetapa.NuevoInfEtapaActivity;
import com.example.comprasmu.ui.infetapa.NuevoInfEtapaViewModel;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.ui.preparacion.NvaPreparacionViewModel;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;
import com.example.comprasmu.utils.CurrencyTextWatcher;
import com.example.comprasmu.utils.micamara.MiCamaraActivity;
import com.example.comprasmu.utils.ui.DatePickerFragment;
import com.google.common.collect.Table;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class NvoGastoFragment extends Fragment {

    private int preguntaAct;
    LinearLayout llresumen,llpreg1,llconce, lldescripcion,llcosto,llcompr,llfoto,lltotal, llcomentarios;
    private static final String TAG = "NvoGastoFragment";
    private long lastClickTime = 0;
    private boolean yaestoyProcesando=false;
    ImageView fotomos;

    Button aceptar1,aceptar2,aceptar3,aceptar4,aceptar5, aceptar6, aceptar7, aceptar8,guardar;
    private ImageButton btnrotar;
    public static  int REQUEST_CODE_TAKE_PHOTO=1;
    private View root;
    private NvaPreparacionViewModel mViewModel;
    private int  informeSel;
    private String ciudadInf;
    ComprasLog compraslog;


    List<ListaCompra> listacomp;
    private  ArrayList<DescripcionGenerica> listaClientes; //otra vez lista clientes
    public final static String ARG_PREGACT="comprasmu.nga_pregact";
    public final static String ARG_ESEDI="comprasmu.nga_esedi";
    public final static String ARG_INFORMESEL = "comprasmu.ngainfsel";

    private FragmentNvogastoBinding mBinding;
    private boolean isEdicion;
    List<CatalogoDetalle> conceptos;
    InformeEtapa informeEdit;
    InformeGastoDet detalleEdit;
    int totalgastos;
    int totalotros; //solo puede capturar 5
    int etapa=6;
    float totalval;
    private ImageButton btntomarf;
    NvoGastoViewModel niviewModel;
    ComprasLog milog;
    private ArrayAdapter<CatalogoDetalle> catAdapter;

    public NvoGastoFragment() {

    }
    public static NvoGastoFragment newInstance() {
        return new NvoGastoFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_nvogasto, container, false);

        //   mBinding.setMviewModel(mViewModel);
        mBinding.setLifecycleOwner(this);
        try {
//son 8 preguntas
            root=mBinding.getRoot();
            mViewModel = new ViewModelProvider(requireActivity()).get(NvaPreparacionViewModel.class);
            niviewModel = new ViewModelProvider(requireActivity()).get(NvoGastoViewModel.class);

            //reviso si es edicion y ya tengo info en temp

            llresumen = root.findViewById(R.id.llgasresumen);
            llpreg1 = root.findViewById(R.id.llgaspreg1);
            llconce = root.findViewById(R.id.llgasselconce);
            lldescripcion = root.findViewById(R.id.llgaspregdesc);
            llcosto = root.findViewById(R.id.llgaspregcos);
            llcompr = root.findViewById(R.id.llgaspregcomp);
            llfoto = root.findViewById(R.id.llgaspregfoto);

            llcomentarios = root.findViewById(R.id.llgasprecomen);

            aceptar1 = root.findViewById(R.id.btngasacep1);

            aceptar2 = root.findViewById(R.id.btngaspreg1);
            aceptar3 = root.findViewById(R.id.btgasacepcon);
            aceptar4 = root.findViewById(R.id.btngasacepdes);
            aceptar5 = root.findViewById(R.id.btngasacepcos);
            aceptar6 = root.findViewById(R.id.btngasacepcomp);
            aceptar7 = root.findViewById(R.id.btngasacepfoto);
            guardar = root.findViewById(R.id.btnnvguardar);
            compraslog=ComprasLog.getSingleton();
            btnrotar = root.findViewById(R.id.btngasrotar);

            btntomarf = root.findViewById(R.id.btngasfoto);
            fotomos=root.findViewById(R.id.ivgasfoto);

             milog = ComprasLog.getSingleton();

            llresumen.setVisibility(View.GONE);
            llpreg1.setVisibility(View.GONE);
            llconce.setVisibility(View.GONE);
            lldescripcion.setVisibility(View.GONE);
            llcosto.setVisibility(View.GONE);
            llcompr.setVisibility(View.GONE);
            llfoto.setVisibility(View.GONE);

            llcomentarios.setVisibility(View.GONE);
            getConceptos();
            ciudadInf=Constantes.CIUDADTRABAJO;
            if (getArguments() != null) {
                // Log.d(TAG,"aqui");
                this.preguntaAct = getArguments().getInt(ARG_PREGACT);
                this.informeSel = getArguments().getInt(ARG_INFORMESEL);
                // mViewModel.setIdNuevo(this.informeSel);
                //BUSCo si viene de continuar
                InformeEtapa informeEtapa =mViewModel.getInformexId(informeSel);
                if (informeEtapa != null)
                    this.informeEdit = informeEtapa;


                this.isEdicion = getArguments().getBoolean(ARG_ESEDI);
            }
            this.buscarTotalesMuestra();
            totalgastos=totalotros=0;
            totalval=0;

            //busco los clientes x ciudad
           if(!niviewModel.validarEtapa(ciudadInf)){
               Toast.makeText(getContext(),"No puede hacer gastos",Toast.LENGTH_SHORT).show();
                return root;
           }
            //deshabilito botones de aceptar
            aceptar1.setEnabled(true); //resumen

            aceptar2.setEnabled(false);//pregunta gasto
            aceptar3.setEnabled(true); //concepto
            aceptar4.setEnabled(true);//descripcion
            aceptar5.setEnabled(false); //costo
            aceptar6.setEnabled(false); //comprobante
            aceptar7.setEnabled(false); //foto
            guardar.setEnabled(true);
            mBinding.singasto.setmLabel(getString(R.string.realizo_otro));
            mBinding.singasto.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    aceptar2.setEnabled(true);
                }
            });
            mBinding.sincomprobante.setmLabel(getString(R.string.tiene_comp));
            mBinding.sincomprobante.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    aceptar6.setEnabled(true);
                }
            });
            mBinding.txtgascomentarios.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            mBinding.txtgasdescrip.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        //    mBinding.txtgasdescrip.addTextChangedListener(new BotonTextWatcher(aceptar4));
            mBinding.txtgascosto.addTextChangedListener(new BotonTextWatcher(aceptar5));
            mBinding.txtgascosto.setRawInputType(Configuration.KEYBOARD_12KEY);
            mBinding.txtgascosto.addTextChangedListener(new CurrencyTextWatcher());

            mBinding.spgasconcep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    aceptar3.setEnabled(true);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });

            if (preguntaAct == 0)
                preguntaAct = 1;
            mViewModel.preguntaAct = preguntaAct;
            if (!isEdicion && preguntaAct < 2 && mViewModel.getIdNuevo() == 0) {
                //es nuevo
                //reviso si ya tengo uno abierto
                InformeEtapa informeEtapa = mViewModel.getInformePend(Constantes.INDICEACTUAL, etapa);

                if (informeEtapa != null) {

                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
                    dialogo1.setTitle(R.string.atencion);
                    dialogo1.setMessage(R.string.informe_abierto);
                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            //lo mando a continuar
                            getActivity().finish();

                        }
                    });

                    dialogo1.show();
                }
                llresumen.setVisibility(View.VISIBLE);
            }
            milog.grabarError(TAG + " iniciando nvo informe gastos");

            ((NuevoInfEtapaActivity) getActivity()).actualizarBarraGas(ciudadInf);

            if (isEdicion) { //busco el informe

                //busco el informe y el detalle
                mViewModel.setIdNuevo(informeSel);

                editarInforme();
            }

            aceptar1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //todo guaradr unforme etapa
                    guardarInf();
                    avanzar();

                }
            });
      aceptar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                avanzar();
            }
        });
            aceptar3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    avanzar();

                }
            });
            aceptar4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    avanzar();

                }
            });
            aceptar5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    avanzar();

                }
            });
            aceptar6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mBinding.sincomprobante.getRespuesta()){
                        avanzar();
                    }else
                       guardarDet();

                }
            });
            aceptar7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    guardarDet();

                }
            });
            guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  //  guardarDet();
                    finalizarInf();
                }
            });

            btnrotar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rotar(mBinding.txtgasrutafoto.getId());
                }
            });

            btntomarf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tomarFoto(1);
                }
            });

            mViewModel.preguntaAct = preguntaAct;

        }catch(Exception ex){
            ex.printStackTrace();
            milog.grabarError(TAG,"oncreateview",ex.getMessage());
        }
        return root;
    }

    public void buscarTotalesMuestra(){
        //voy al servidor por ellos
        niviewModel.getTotalMuestras(ciudadInf,new ListenerM());
    }
    public void llenarTabla(List<TotalMuestra> totales){
        TableRow tableRow;
        TextView cliente;
        TextView numuestra;
        TextView costo;
        float sumacosto=0;
        int sumamuestras=0;
        for (TotalMuestra detalle:totales
             ) {
             tableRow=new TableRow(getContext());
            cliente=new TextView(getContext());
           numuestra=new TextView(getContext());
           costo=new TextView(getContext());
         //   tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            cliente.setText(detalle.getCliente());
            cliente.setBackgroundResource(R.drawable.valuecellborder);
           numuestra.setText(detalle.getNum_muestras()+"");
            numuestra.setBackgroundResource(R.drawable.valuecellborder);
           costo.setText(Constantes.SIMBOLOMON+""+new DecimalFormat("#.00").format(detalle.getCosto()));
            costo.setBackgroundResource(R.drawable.valuecellborder);
            tableRow.addView(cliente);
            tableRow.addView(numuestra);
            tableRow.addView(costo);
            mBinding.tblnimuestras.addView(tableRow);
            sumacosto+=detalle.getCosto();
            sumamuestras+=detalle.getNum_muestras();
        }
         tableRow=null;
         cliente=null;
         numuestra=null;
         costo=null;
        TextView txtgatotnum=new TextView(getContext());
        TextView txttotal=new TextView(getContext());
        txtgatotnum.setText(sumamuestras+"");
        TextView txtgastotmue=new TextView(getContext());
        txtgastotmue.setText(Constantes.SIMBOLOMON+sumacosto);
        txttotal.setBackgroundResource(R.drawable.valuecellborder);
        txtgastotmue.setBackgroundResource(R.drawable.valuecellborder);
        txtgatotnum.setBackgroundResource(R.drawable.valuecellborder);
        tableRow=new TableRow(getContext());
        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        txttotal.setText("TOTAL");
        tableRow.addView(txttotal);
        tableRow.addView(txtgatotnum);
        tableRow.addView(txtgastotmue);
        mBinding.tblnimuestras.addView(tableRow);
        aceptar1.setEnabled(true); //resumen
    }

        public void avanzar() {
            Log.d(TAG, "++" + preguntaAct);

            switch (preguntaAct) {
                case 1: //pregunta

                    // txtcajaact.setVisibility(View.VISIBLE)
                    llresumen.setVisibility(View.GONE);
                    // txttotmues.setVisibility(View.VISIBLE);

                    llpreg1.setVisibility(View.VISIBLE);
                    preguntaAct = preguntaAct + 1;
                        //veo si ya tengo un informe
                        // InformeEtapa primero=mViewModel.tieneInforme(this.etapa,Constantes.CIUDADTRABAJO,clienteId);
                        // if(primero!=null){

                        //busco la ultima muestra
                        //     InformeEtapaDet ultima = mViewModel.getUltimaMuestraEtiq(primero.getId());

                        // }

                    break;
                case 2: //concepto
                    llpreg1.setVisibility(View.GONE);
                    if(mBinding.singasto.getRespuesta()) {
                        //reviso los conceptos que ya están utilizados

                        List<InformeGastoDet> detalles=niviewModel.getGastoDetalles(informeSel);
                        for (int i=0;i<conceptos.size();i++
                             ){
                            for (InformeGastoDet det:
                                 detalles) {

                                if (conceptos.get(i).getCad_idopcion() == det.getConceptoId()) {
                                    conceptos.remove(i);
                                    catAdapter.notifyDataSetChanged();
                                }

                            }
                        }
                            llconce.setVisibility(View.VISIBLE);
                            preguntaAct = preguntaAct + 1;
                    }else
                    {
                        //nos vamos a comentarios
                        preguntaAct=8;
                        mBinding.txtgaconceptosel.setText("");
                        // calcular total
                        llenarTablaConcep();
                        llcomentarios.setVisibility(View.VISIBLE);
                    }
                    break;
                case 3: //descripcion
                    llconce.setVisibility(View.GONE);
                    lldescripcion.setVisibility(View.VISIBLE);
                    //busco el concepto seleccionado
                    CatalogoDetalle sel=(CatalogoDetalle) mBinding.spgasconcep.getSelectedItem();

                    mBinding.txtgaconceptosel.setText(sel.getCad_descripcionesp());
                    preguntaAct = preguntaAct + 1;

                    break;
                case 4: //costo
                    lldescripcion.setVisibility(View.GONE);
                    llcosto.setVisibility(View.VISIBLE);

                    try {
                        String conceptosel = ((CatalogoDetalle) mBinding.spgasconcep.getSelectedItem()).getCad_descripcionesp();
                        mBinding.txtgascosto.setText("COSTO " +conceptosel);
                    }catch(Exception ex){

                    }
                    preguntaAct = preguntaAct + 1;

                    break;
                case 5: //preg comprob
                    llcosto.setVisibility(View.GONE);
                    llcompr.setVisibility(View.VISIBLE);

                    preguntaAct = preguntaAct + 1;

                    break;
                case 6: //foto

                    llcompr.setVisibility(View.GONE);
                    if(mBinding.sincomprobante.getRespuesta()) {
                        llfoto.setVisibility(View.VISIBLE);
                        preguntaAct = preguntaAct + 1;
                    }
                    else {
                        llpreg1.setVisibility(View.VISIBLE);
                        //todo limpio variables
                        limpiarForm();
                        preguntaAct = 2;
                    }



                    break;
                case 7:
                    llfoto.setVisibility(View.GONE);
                    //otra vez pregnta 1
                    preguntaAct=2;
                    llpreg1.setVisibility(View.VISIBLE);
                    //todo limpio variables
                    limpiarForm();
                    break;


            }

            mViewModel.preguntaAct = preguntaAct;
        }



        public void limpiarForm() {
            mBinding.singasto.clearCheck();

            mBinding.sincomprobante.clearCheck();
            aceptar2.setEnabled(false);//pregunta gasto
            aceptar3.setEnabled(true); //concepto
          //  aceptar4.setEnabled(false);//descripcion
            aceptar5.setEnabled(false); //costo
            aceptar6.setEnabled(false); //comprobante
            aceptar7.setEnabled(false); //foto
            guardar.setEnabled(true);
            mBinding.txtgascomentarios.setText("");
            mBinding.txtgasdescrip.setText("");

            mBinding.txtgascosto.setText("");



            mBinding.spgasconcep.setSelection(-1);
            mBinding.txtgasrutafoto.setText("");
          fotomos.setImageBitmap(null);
            btnrotar.setVisibility(View.GONE);
          nombre_foto=null;
          archivofoto=null;
            // fotomos.setLayoutParams(new LinearLayout.LayoutParams(350,150));
            fotomos.setVisibility(View.GONE);
        }
    public void getConceptos(){
        //  Log.d(TAG,"buscando atributos"+dViewModel.productoSel.empaque+"--"+dViewModel.productoSel.idempaque+"--"+dViewModel.productoSel.clienteSel);
        conceptos=niviewModel.cargarConceptos();
        catAdapter = new ArrayAdapter<CatalogoDetalle>(getContext(), android.R.layout.simple_spinner_dropdown_item, conceptos) {


            // And the "magic" goes here
            // This is for the "passive" state of the spinner
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
                TextView label = (TextView) super.getView(position, convertView, parent);
                label.setTextColor(Color.BLACK);
                // Then you can get the current item using the values array (Users array) and the current position
                // You can NOW reference each method you has created in your bean object (User class)
                CatalogoDetalle item = getItem(position);
                label.setText(item.getCad_descripcionesp());
                //TODO elegir idioma

                // And finally return your dynamic (or custom) view for each spinner item
                return label;
            }

            // And here is when the "chooser" is popped up
            // Normally is the same view, but you can customize it if you want
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                TextView label = (TextView) super.getDropDownView(position, convertView, parent);
                label.setTextColor(Color.BLACK);
                CatalogoDetalle item = getItem(position);
                label.setText(item.getCad_descripcionesp());

                return label;
            }
        };


        mBinding.spgasconcep.setAdapter(catAdapter);
    }
    public void llenarTablaConcep(){
        TableRow tableRow;

        TextView concepto;
        TextView costo;
        float sumacosto=0;
        TableRow.LayoutParams lp1;
        lp1 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, .7f);
        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, .3f);

        //busco lo capturado
        List<InformeGastoDet> detalles=niviewModel.getGastoDetalles(mViewModel.getIdNuevo());
        for (InformeGastoDet detalle:detalles
        ) {
            tableRow=new TableRow(getContext());

            concepto=new TextView(getContext());
            costo=new TextView(getContext());
          //  tableRow.setGravity(Gravity.CENTER_HORIZONTAL);

         //   tableRow.setLayoutParams(lp);


            concepto.setText(detalle.getConcepto()+"");
            concepto.setBackgroundResource(R.drawable.valuecellborder);
            costo.setText(Constantes.SIMBOLOMON+""+new DecimalFormat("0.00").format(detalle.getImporte()));
            costo.setBackgroundResource(R.drawable.valuecellborder);
            concepto.setLayoutParams(lp1);
            costo.setLayoutParams(lp2);

            tableRow.addView(concepto);
            tableRow.addView(costo);
            mBinding.tblgaresconcep.addView(tableRow);
            try {
                sumacosto =sumacosto+ detalle.getImporte();

            }catch(NumberFormatException ex){
                compraslog.grabarError(TAG+" "+ex.getMessage());
            }


        }

        tableRow=new TableRow(getContext());

        concepto=new TextView(getContext());
        costo=new TextView(getContext());

        tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
     //   tableRow.setLayoutParams(lp);
        costo.setBackgroundResource(R.drawable.valuecellborder);
        concepto.setBackgroundResource(R.drawable.valuecellborder);
        concepto.setText("TOTAL A VALIDAR");
       costo.setText(Constantes.SIMBOLOMON+new DecimalFormat("0.00").format(sumacosto));
        concepto.setLayoutParams(lp1);
        costo.setLayoutParams(lp2);
        tableRow.addView(concepto);
        tableRow.addView(costo);
        mBinding.tblgaresconcep.addView(tableRow);

      //  tableRow=null;
      //  concepto=null;

      //  costo=null;
    }
    public void editarInforme() {


            ImagenDetalle foto;
            //para saber si ya tego detalle
            List<InformeGastoDet> detalles=niviewModel.getGastoDetalles(informeSel);

            if(detalles!=null&&detalles.size()>0) //ya tengo algo
            {
                totalgastos= detalles.size();
                //muestro el ultimo
                detalleEdit=null;
                totalval=niviewModel.calcularTotal(informeSel);
                preguntaAct=2;
                llpreg1.setVisibility(View.VISIBLE);
            }else
                //solo hice informe
            {
                preguntaAct = 1;
                llresumen.setVisibility(View.VISIBLE);
            }
            mViewModel.preguntaAct = preguntaAct;
            if (detalleEdit!=null) {
                //busco en la bd la imagen
                if(detalleEdit.getFotocomprob()>0) {
                    foto = mViewModel.getFoto(detalleEdit.getFotocomprob());
                    mBinding.txtgasrutafoto.setText(foto.getRuta());
                    mBinding.sincomprobante.setSi(true);
                    Bitmap bitmap1 = ComprasUtils.decodeSampledBitmapFromResource(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + foto.getRuta(), 80, 80);
                    fotomos.setImageBitmap(bitmap1);
                    fotomos.setVisibility(View.VISIBLE);
                    btnrotar.setVisibility(View.VISIBLE);
                    aceptar7.setEnabled(true);
                }else
                    mBinding.sincomprobante.setNo(true);
                //lleno campos
                mBinding.spgasconcep.setSelection(detalleEdit.getConceptoId());
                aceptar3.setEnabled(true);

                mBinding.txtgasdescrip.setText(detalleEdit.getDescripcion());
                aceptar4.setEnabled(true);
                mBinding.txtgascosto.setText(detalleEdit.getImporte()+"");
                aceptar5.setEnabled(true);
                aceptar6.setEnabled(true);

            }

        }



        public void atras(){
            Log.d(TAG, "--" + preguntaAct);
            isEdicion=true; //siempre es edicion
            switch (preguntaAct){

                case 2:

                    if(totalgastos>0) //ya no vuelvo
                    {

                      /*  if (mBinding.sincomprobante.getRespuesta()) {
                            llfoto.setVisibility(View.VISIBLE);
                            preguntaAct=7;
                        }else
                        {
                            llcompr.setVisibility(View.VISIBLE);
                            preguntaAct=6;
                        }*/
                        break;
                    }else {
                        llpreg1.setVisibility(View.GONE);
                        llresumen.setVisibility(View.VISIBLE);
                        preguntaAct = preguntaAct - 1;
                    }



                        mViewModel.preguntaAct = preguntaAct;

                    break;
                case 3:
                    llpreg1.setVisibility(View.VISIBLE);
                    llconce.setVisibility(View.GONE);

                    preguntaAct=preguntaAct-1;
                    mViewModel.preguntaAct=preguntaAct;
                    break;


                case 4:
                    llconce.setVisibility(View.VISIBLE);
                    lldescripcion.setVisibility(View.GONE);

                    preguntaAct=preguntaAct-1;
                    mViewModel.preguntaAct=preguntaAct;
                    break;
                case 5:
                    lldescripcion.setVisibility(View.VISIBLE);
                    llcosto.setVisibility(View.GONE);

                    preguntaAct=preguntaAct-1;
                    mViewModel.preguntaAct=preguntaAct;
                    break;
                case 6:
                    llcosto.setVisibility(View.VISIBLE);
                    llcompr.setVisibility(View.GONE);

                    preguntaAct=preguntaAct-1;
                    mViewModel.preguntaAct=preguntaAct;
                    break;
                case 7:
                    llcompr.setVisibility(View.VISIBLE);
                    llfoto.setVisibility(View.GONE);

                    preguntaAct=preguntaAct-1;
                    mViewModel.preguntaAct=preguntaAct;
                    break;
                case 8:
                    llcomentarios.setVisibility(View.GONE);
                    if(mBinding.singasto.getRespuesta())
                        if(mBinding.sincomprobante.getRespuesta()) {
                            llfoto.setVisibility(View.VISIBLE);
                            preguntaAct = preguntaAct - 1;
                        }

                    else {
                            llcompr.setVisibility(View.VISIBLE);
                            preguntaAct=6;
                        }
                   else {
                        llpreg1.setVisibility(View.VISIBLE);
                        preguntaAct=2;
                    }


                    mViewModel.preguntaAct=preguntaAct;
                    break;

            }
            Log.d(TAG,"**"+preguntaAct);

        }


        public void guardarInf(){
            try {
                lastClickTime = 0;

                //valido fecha

                if (preguntaAct == 1 && !isEdicion&&mViewModel.getNvoinforme()==null) {
                    Log.d(TAG, "creando nvo inf");
                    //creo el informe
                    mViewModel.setIdNuevo(mViewModel.insertarGasto(Constantes.INDICEACTUAL, 0,ciudadInf));
                   informeSel=mViewModel.getIdNuevo();
                    informeEdit = mViewModel.getInformexId(informeSel);
                }


            }catch (Exception ex){
                ex.printStackTrace();
                Log.e(TAG,"Algo salió mal al guardarInf"+ex.getMessage());
                Toast.makeText(getContext(),"Hubo un error al guardar intente de nuevo",Toast.LENGTH_SHORT).show();

            }

            aceptar1.setEnabled(true);

        }
        public void guardarDet(){
            try{
                String rutafoto = null;
               CatalogoDetalle consel=(CatalogoDetalle) mBinding.spgasconcep.getSelectedItem();
                int conceptoid= consel.getCad_idopcion();
                String concepto=consel.getCad_descripcionesp();
                String descripcion=mBinding.txtgasdescrip.getText().toString();
                String costo=mBinding.txtgascosto.getText().toString();
                boolean tienecom=false;
                if(mBinding.sincomprobante.getRespuesta()) {
                    tienecom = true;
                    rutafoto = mBinding.txtgasrutafoto.getText().toString();
                }

                Log.d(TAG,"preg act "+preguntaAct);
                //es un nuevo registro


                if(detalleEdit==null) { //es 1a vez

                        InformeGastoDet nvoDet = new InformeGastoDet();
                        nvoDet.setInformeEtapaId(mViewModel.getIdNuevo());
                        nvoDet.setConcepto(concepto);
                        nvoDet.setConceptoId(conceptoid);
                        nvoDet.setDescripcion(descripcion);
                        //cambio el importe
                        if(!costo.equals("")) {
                            costo=costo.substring(1);
                            try {
                                float importe=Float.valueOf(costo);
                                nvoDet.setImporte(importe);
                            }catch (NumberFormatException ex) {
                                Toast.makeText(getActivity(),"El costo es incorrecto verifique",Toast.LENGTH_SHORT);
                                return;
                            }

                        }

                        nvoDet.setComprobante(tienecom);
                        if(rutafoto!=null&&!rutafoto.equals("")){

                                int numfoto=mViewModel.insertarImagen("foto_comprobante",rutafoto, Constantes.INDICEACTUAL);
                                if(numfoto>0){
                                    nvoDet.setFotocomprob(numfoto);

                                }
                        }
                            nvoDet.setEstatus(1);
                            niviewModel.insertarGastoDet(nvoDet);
                        totalgastos++;
                        }
                else {

                        //busco si ya tiene detalle


                        detalleEdit.setConcepto(concepto);
                        detalleEdit.setConceptoId(conceptoid);
                        detalleEdit.setDescripcion(descripcion);
                        //cambio el importe
                        if (!costo.equals("")) {
                            costo = costo.substring(1);
                            try {
                                float importe = Float.valueOf(costo);
                                detalleEdit.setImporte(importe);
                            } catch (NumberFormatException ex) {
                                Toast.makeText(getActivity(), "El costo es incorrecto verifique", Toast.LENGTH_SHORT);
                                return;
                            }

                        }

                        detalleEdit.setComprobante(tienecom);
                        if (!rutafoto.equals("")) {


                            niviewModel.actualizarImagen(detalleEdit, rutafoto);
                        }


                        //es actualizacion
                        niviewModel.actualizarDet(detalleEdit);
                }
                mBinding.txtgaconceptosel.setText("");
                avanzar();
            }catch (Exception ex){
                ex.printStackTrace();
                milog.grabarError(TAG,"guardarDet",ex.getMessage());
                Toast.makeText(getContext(),"Hubo un error al guardar intente de nuevo",Toast.LENGTH_SHORT).show();

            }

        }

        public void rotar(int idcampo){
            EditText txtruta = root.findViewById(idcampo);
            String foto=txtruta.getText().toString();
            if(ComprasUtils.getAvailableMemory(getActivity()).lowMemory)
            {
                Toast.makeText(getActivity(), "No hay memoria suficiente para esta accion", Toast.LENGTH_SHORT).show();

                return;
            }else
            {

                RevisarFotoActivity.rotarImagen(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" +foto,fotomos);

            }
        }

        String nombre_foto;
        File archivofoto;
        public void tomarFoto(int REQUEST_CODE){
            REQUEST_CODE_TAKE_PHOTO=REQUEST_CODE;
            if(ComprasUtils.getAvailableMemory(getActivity()).lowMemory)
            {
                Toast.makeText(getActivity(), "No hay memoria suficiente para esta accion", Toast.LENGTH_SHORT).show();

                return;
            }else {
                Activity activity = this.getActivity();
                Intent intento1 = new Intent(getContext(), MiCamaraActivity.class);
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");

                String dateString = format.format(new Date());
                String state = Environment.getExternalStorageState();

                File baseDirFile;
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    baseDirFile = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    if (baseDirFile == null) {
                        Toast.makeText(activity, "No se encontró almacenamiento externo", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(activity, "No se encontró almacenamiento externo", Toast.LENGTH_SHORT).show();
                    return;
                }
                //  baseDir = baseDirFile.getAbsolutePath();

                try {
                    nombre_foto = "img_" + Constantes.CLAVEUSUARIO + "_" + dateString + ".jpg";
                    archivofoto = new File(baseDirFile, nombre_foto);
                    Log.e(TAG, "****" + archivofoto.getAbsolutePath());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(activity, "No se encontró almacenamiento externo", Toast.LENGTH_SHORT).show();
                    return;

                }

                intento1.putExtra(MediaStore.EXTRA_OUTPUT, archivofoto.getAbsolutePath()); //se pasa a la otra activity la referencia al archivo

                if (fotomos != null) {

                    startActivityForResult(intento1, REQUEST_CODE);

                }
            }

        }
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            Log.d(TAG,"vars"+requestCode +"--"+ nombre_foto);
            if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {
                //   super.onActivityResult(requestCode, resultCode, data);

                if (archivofoto!=null&&archivofoto.exists()) {
                    if(requestCode == REQUEST_CODE_TAKE_PHOTO) {

                        mostrarFoto(mBinding.txtgasrutafoto,fotomos,btnrotar);
                        mBinding.btngasacepfoto.setEnabled(true);
                    }


                }
                else{
                    Log.e(TAG,"Algo salió mal???");
                }


            }else

            {
                Log.e(TAG,"Algo salió muy mal**");
            }

        }

        public void mostrarFoto( EditText textorut,ImageView xfotomos, ImageButton xbtnrotar){


            textorut.setText(nombre_foto);
            if(ComprasUtils.getAvailableMemory(getActivity()).lowMemory)
            {
                Toast.makeText(getActivity(), "No hay memoria suficiente para esta accion", Toast.LENGTH_SHORT).show();

                return;
            }else {
                // Bitmap bitmap1 = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null) + "/" + nombre_foto);
                ComprasUtils cu = new ComprasUtils();
                cu.comprimirImagen(archivofoto.getAbsolutePath());
                Bitmap bitmap1 = ComprasUtils.decodeSampledBitmapFromResource(archivofoto.getAbsolutePath(), 100, 100);
                xfotomos.setImageBitmap(bitmap1);
                // fotomos.setLayoutParams(new LinearLayout.LayoutParams(350,150));
                xfotomos.setVisibility(View.VISIBLE);

                xbtnrotar.setVisibility(View.VISIBLE);
                xbtnrotar.setFocusableInTouchMode(true);
                xbtnrotar.requestFocus();
                nombre_foto=null;
                archivofoto=null;
            }


        }
        public void salir(){
            //mViewModel.eliminarTblTemp();
            //me voy a la lista de informes
            getActivity().finish();
            Intent intento1 = new Intent(getActivity(), NavigationDrawerActivity.class);
            intento1.putExtra(NavigationDrawerActivity.NAVINICIAL,"listainformeeta");
            startActivity(intento1);
            // NavHostFragment.(this).navigate(R.id.action_selclientetolistacompras,bundle);


        }

        //ya se puede varios informes de etiquetado para la reactivacion

        class BotonTextWatcher implements TextWatcher {

            boolean mEditing;
            Button aceptar;
            public BotonTextWatcher() {
                mEditing = false;
            }
            public BotonTextWatcher(Button botonac) {
                mEditing = false;
                aceptar=botonac;
            }
            public synchronized void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //count es cantidad de caracteres que tiene
                aceptar.setEnabled(charSequence.length() > 0);

            }


        }

    public void finalizarInf() {
        try {
            Log.d(TAG,"total gastos="+totalgastos);
            String comentarios=mBinding.txtgascomentarios.getText().toString();
            if(!comentarios.equals(""))
                mViewModel.actualizarComentarios(mViewModel.getIdNuevo(),comentarios);
            mViewModel.finalizarInf();
            InformeGastoEnv envio=niviewModel.prepararInformeEnv(mViewModel.getIdNuevo());

            SubirInformeGastoTask miTareaAsincrona = new SubirInformeGastoTask(envio,getActivity());
            miTareaAsincrona.execute();
            subirFotos(getActivity(),envio);
            Toast.makeText(getContext(),"El informe se envió correctamente",Toast.LENGTH_SHORT).show();

            salir();

        }catch(Exception ex){
            ex.printStackTrace();
            milog.grabarError(TAG,"finalizarInf","Algo salió mal al finalizar"+ex.getMessage());
            Toast.makeText(getContext(),"Algo salio mal al enviar intente de nuevo desde el resumen",Toast.LENGTH_SHORT).show();
        }
        // limpio variables de sesion

        mViewModel.setIdNuevo(0);
        mViewModel.setIddetalle(0);
        mViewModel.setNvoinforme(null);

    }
    public static void subirFotos(Activity activity, InformeGastoEnv informe){
        //las imagenes
        for(ImagenDetalle imagen:informe.getImagenDetalles()){
            //
            //subo cada una
            Intent msgIntent = new Intent(activity, SubirFotoService.class);
            msgIntent.putExtra(SubirFotoService.EXTRA_IMAGE_ID, imagen.getId());
            msgIntent.putExtra(SubirFotoService.EXTRA_IMG_PATH,imagen.getRuta());
            msgIntent.putExtra(SubirFotoService.EXTRA_INDICE,informe.getIndice());
            // Constantes.INDICEACTUAL
            Log.d(TAG,"subiendo fotos"+activity.getLocalClassName());

            msgIntent.setAction(SubirFotoService.ACTION_UPLOAD_ETA);

            //cambio su estatus a subiendo
            imagen.setEstatusSync(1);
            activity.startService(msgIntent);

        }

    }
    public class ListenerM{
        //todo
        public void guardarRes(List<TotalMuestra> respuesta){
            //acomodo en la tabla
            if(respuesta!=null) {
                llenarTabla(respuesta);
                mBinding.txtgaalgunerror.setText("");
            }
            else
                mBinding.txtgaalgunerror.setText("No se pudo obtener la información del servidor");
        }


    }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            mViewModel = null;

            root=null;

            fotomos=null;
          //  sv1= sv6 =sv3=sv4=null;
            btnrotar=null;
            aceptar1=null;
         //   nombre_foto=null;
         //   archivofoto=null;
        }




}

