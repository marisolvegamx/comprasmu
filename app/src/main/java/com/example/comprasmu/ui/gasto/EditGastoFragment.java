package com.example.comprasmu.ui.gasto;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.SubirInformeGastoTask;
import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeGastoDet;
import com.example.comprasmu.data.remote.InformeGastoEnv;
import com.example.comprasmu.databinding.FragmentNvogastoBinding;
import com.example.comprasmu.services.SubirFotoService;
import com.example.comprasmu.ui.RevisarFotoActivity;

import com.example.comprasmu.ui.preparacion.NvaPreparacionViewModel;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CurrencyTextWatcher;
import com.example.comprasmu.utils.micamara.MiCamaraActivity;
import com.google.gson.Gson;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/****para agregar nuevos conceptos al inf. de gastos****/
public class EditGastoFragment extends Fragment {

    private int preguntaAct;
    LinearLayout llresumenedi, llresumen,llpreg1,llconce, lldescripcion,llcosto,llcompr,llfoto,lltotal, llcomentarios;
    private static final String TAG = "EditGastoFragment";
    private long lastClickTime = 0;
    private boolean yaestoyProcesando=false;
    ImageView fotomos;

    Button aceptar1,aceptar2,aceptar3,aceptar4,aceptar5, aceptar6, aceptar7, aceptar8,guardar,acepresedi;
    private ImageButton btnrotar;
    public static  int REQUEST_CODE_TAKE_PHOTO=1;
    private View root;
    private NvaPreparacionViewModel mViewModel;
    private int  informeSel;
    private String ciudadInf;
    ComprasLog compraslog;
    public final static String ARG_PREGACT="comprasmu.ega_pregact";
    public final static String ARG_INFORMESEL = "comprasmu.egainfsel";

    private FragmentNvogastoBinding mBinding;

    List<CatalogoDetalle> conceptos;
    InformeEtapa informeEdit;
    int totalgastos;
    int totalotros; //solo puede capturar 5
    float totalval;
    private ImageButton btntomarf;
    NvoGastoViewModel niviewModel;

    private ArrayAdapter<CatalogoDetalle> catAdapter;
    private Button aceptarresedi;

    public EditGastoFragment() {

    }
    public static EditGastoFragment newInstance() {
        return new EditGastoFragment();
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
            if (savedInstanceState != null) {    // Restore value of members from saved state
                Intent intento1 = new Intent(getActivity(), NavigationDrawerActivity.class);
                startActivity(intento1);
                getActivity().finish();
                return root;
            }
            mViewModel = new ViewModelProvider(requireActivity()).get(NvaPreparacionViewModel.class);
            niviewModel = new ViewModelProvider(requireActivity()).get(NvoGastoViewModel.class);

            //reviso si es edicion y ya tengo info en temp
            llresumenedi = root.findViewById(R.id.llgasresumenedi);
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
            aceptarresedi = root.findViewById(R.id.btngasacepresedi);
            guardar = root.findViewById(R.id.btnnvguardar);
            compraslog=ComprasLog.getSingleton();
            btnrotar = root.findViewById(R.id.btngasrotar);

            btntomarf = root.findViewById(R.id.btngasfoto);
            fotomos=root.findViewById(R.id.ivgasfoto);

            llresumen.setVisibility(View.GONE);
            llpreg1.setVisibility(View.GONE);
            llconce.setVisibility(View.GONE);
            lldescripcion.setVisibility(View.GONE);
            llcosto.setVisibility(View.GONE);
            llcompr.setVisibility(View.GONE);
            llfoto.setVisibility(View.GONE);

            llcomentarios.setVisibility(View.GONE);

            ciudadInf=Constantes.CIUDADTRABAJO;
            if (getArguments() != null) {
                // Log.d(TAG,"aqui");
                this.preguntaAct = getArguments().getInt(ARG_PREGACT);
                this.informeSel = getArguments().getInt(ARG_INFORMESEL);
                // mViewModel.setIdNuevo(this.informeSel);
                //BUSCo si viene de continuar
                InformeEtapa informeEtapa =mViewModel.getInformexId(informeSel);

                this.informeEdit = informeEtapa;

            }
            //todo codigo de revisar recibo
            PeticionesServidor ps=new PeticionesServidor(Constantes.CLAVEUSUARIO);
            ps.getCambiosGastos(Constantes.INDICEACTUAL,informeEdit.getCiudadNombre(),new ListenerEdiGas());

            totalgastos=totalotros=0;
            totalval=0;
            getConceptos();
            //deshabilito botones de aceptar
            //  aceptar1.setEnabled(true); //resumen

            aceptar2.setEnabled(false);//pregunta gasto
            aceptar3.setEnabled(true); //concepto
            aceptar4.setEnabled(true);//descripcion
            aceptar5.setEnabled(false); //costo
            aceptar6.setEnabled(false); //comprobante
            aceptar7.setEnabled(false); //foto
            aceptarresedi.setEnabled(false);
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

            mBinding.txtgasdescrip.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(50)});
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


            preguntaAct = 1;
            mViewModel.preguntaAct = preguntaAct;

            compraslog.grabarError(TAG ,"create", " agregar informe gastos");

            ((EditGastoActivity) getActivity()).actualizarBarra(ciudadInf);

            //busco el informe y el detalle
            mViewModel.setIdNuevo(informeSel);
            editarInforme();


            aceptarresedi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    aceptarresedi.setEnabled(true);
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
                    String costo=mBinding.txtgascosto.getText().toString();

                    if(!costo.equals("")) {
                        if(costo.equals("$0.00")){
                            Toast.makeText(getActivity(),"Costo inválido, verifique",Toast.LENGTH_LONG).show();

                            return;
                        }
                        try {  //valido el csto

                            costo = costo.substring(1).replaceAll(",", "");
                            float importe = Float.valueOf(costo);

                        } catch (NumberFormatException ex) {
                            Toast.makeText(getActivity(), "El costo es incorrecto verifique", Toast.LENGTH_LONG).show();
                            compraslog.grabarError(TAG, "guardarDet", " costo incorrecto");


                            return;
                        }
                        avanzar();
                    }else
                    {
                        Toast.makeText(getActivity(),"Costo inválido, verifique",Toast.LENGTH_LONG).show();

                        return;
                    }


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

                    view.setEnabled(false);
                    long currentClickTime= SystemClock.elapsedRealtime();
                    // preventing double, using threshold of 1000 ms
                    if (currentClickTime - lastClickTime < 3000){
                        view.setEnabled(false);
                        return;
                    }

                    lastClickTime = currentClickTime;
                    finalizarInf();
                    view.setEnabled(true);
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
            compraslog.grabarError(TAG,"oncreateview",ex.getMessage());
        }
        return root;
    }

    public void avanzar() {
        Log.d(TAG, "++" + preguntaAct);
        compraslog.grabarError(TAG ,"avanzar","preguntaact="+preguntaAct);

        switch (preguntaAct) {
            case 1: //pregunta
                llresumenedi.setVisibility(View.GONE);
                llpreg1.setVisibility(View.VISIBLE);
                preguntaAct = preguntaAct + 1;
                break;
            case 2: //concepto
                llpreg1.setVisibility(View.GONE);
                if(mBinding.singasto.getRespuesta()) {
                    //reviso los conceptos que ya están utilizados

                    List<InformeGastoDet> detalles=niviewModel.getGastoDetalles(informeSel);
                    for (InformeGastoDet det:
                            detalles) {
                        for (int i=0;i<conceptos.size();i++
                        ){
                            if (conceptos.get(i).getCad_idopcion() == det.getConceptoId()) {
                                conceptos.remove(i);
                                break;
                            }

                        }
                    }
                    catAdapter.notifyDataSetChanged();
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
                preguntaAct = preguntaAct + 2; //la foto se hace obligatoria ya no pregunta si hay comprobante

                break;
            case 5: //preg comprob
                llcosto.setVisibility(View.GONE);
                // llcompr.setVisibility(View.VISIBLE);

                preguntaAct = preguntaAct + 1;

                break;
            case 6: //foto

                llcosto.setVisibility(View.GONE);
                llfoto.setVisibility(View.VISIBLE);
                preguntaAct = preguntaAct + 1;

                break;
            case 7:
                llfoto.setVisibility(View.GONE);
                //otra vez pregnta 1
                preguntaAct=2;
                llpreg1.setVisibility(View.VISIBLE);
                //todo limpio variables
                limpiarForm();
                break;
            default:
                compraslog.grabarError(TAG ,"avanzar","preguntaact="+preguntaAct);
                Toast.makeText(getActivity(), "Hubo un error intente de nuevo", Toast.LENGTH_SHORT).show();

                break;

        }

        mViewModel.preguntaAct = preguntaAct;
    }


    public void limpiarForm() {
        compraslog.grabarError(TAG ,"limpiarForm","limpiando formulario");

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
        List<CatalogoDetalle> conceptosCatalogo=niviewModel.cargarConceptos();
        conceptos=new ArrayList<>();
        //busco los que ya seleccionó
        List<InformeGastoDet> detalles=niviewModel.getGastoDetalles(informeSel);
        for (int i=0;i<conceptosCatalogo.size();i++
        ){
            int bandera=0;
            for (InformeGastoDet det:
                    detalles) {

                if (conceptosCatalogo.get(i).getCad_idopcion() == det.getConceptoId()) {
                    //conceptos.remove(i);
                    bandera=1;
                    break;
                }

            }
            if(bandera==0)
                conceptos.add(conceptosCatalogo.get(i)); //lo agrego
        }
        catAdapter = new ArrayAdapter<CatalogoDetalle>(getContext(), android.R.layout.simple_spinner_dropdown_item, conceptos) {

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
        compraslog.grabarError(TAG ,"llenarTablaConcep","llenando ultima tabla");

        TableRow tableRow=null;
        mBinding.tblgaresconcep.removeAllViews();
        TextView concepto;
        TextView costo;
        double sumacosto=0;
        TableRow.LayoutParams lp1;
        lp1 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, .7f);
        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, .3f);
        //pongo suma de muestras
        tableRow=new TableRow(getContext());
        concepto=new TextView(getContext());
        costo=new TextView(getContext());

        // tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
        //   tableRow.setLayoutParams(lp);
        costo.setBackgroundResource(R.drawable.valuecellborder);
        concepto.setBackgroundResource(R.drawable.valuecellborder);
        concepto.setText("CONCEPTO");

        costo.setText("COSTO");
        concepto.setLayoutParams(lp1);
        costo.setLayoutParams(lp2);

        concepto.setPadding(30,10,30,10);
        costo.setPadding(30,10,30,10);
        tableRow.addView(concepto);
        tableRow.addView(costo);
        mBinding.tblgaresconcep.addView(tableRow);
        tableRow=new TableRow(getContext());
        concepto=new TextView(getContext());
        costo=new TextView(getContext());

        // tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
        //   tableRow.setLayoutParams(lp);
        costo.setBackgroundResource(R.drawable.valuecellborder);
        concepto.setBackgroundResource(R.drawable.valuecellborder);
        concepto.setText("TOTAL MUESTRAS");
        //busco el total
        Double totalmu=0.0;
        String total=null;
        try {
            total=niviewModel.getTotalmu();
            if(total.equals("")) {
                Toast.makeText(getActivity(), "Hubo un error al guardar intente de nuevo", Toast.LENGTH_SHORT).show();
                return;
            }
            totalmu=Double.parseDouble(total);
            costo.setText(Constantes.SIMBOLOMON + new DecimalFormat("#.00").format(totalmu));
        }catch(Exception ex){

            ex.printStackTrace();
            compraslog.grabarError(TAG,"llenarTablaConcep","Hubo un error al dar formato a "+total);
            Toast.makeText(getActivity(), "Hubo un error al guardar intente de nuevo", Toast.LENGTH_SHORT).show();
            return;
        }
        concepto.setLayoutParams(lp1);
        costo.setLayoutParams(lp2);
        concepto.setPadding(30,10,30,10);
        costo.setPadding(30,10,30,10);
        tableRow.addView(concepto);
        tableRow.addView(costo);


        mBinding.tblgaresconcep.addView(tableRow);
        try {
            sumacosto = totalmu;
        }catch (NumberFormatException ex){
            compraslog.grabarError(TAG,"llenarTablaConcep","error al convertir total muestras a float");
            Toast.makeText(getActivity(), "Hubo un error al guardar intente de nuevo", Toast.LENGTH_SHORT).show();
            return;
        }
        //busco lo capturado
        List<InformeGastoDet> detalles=niviewModel.getGastoDetalles(mViewModel.getIdNuevo());
        for (InformeGastoDet detalle:detalles
        ) {
            tableRow=new TableRow(getContext());

            concepto=new TextView(getContext());
            costo=new TextView(getContext());

            concepto.setText(detalle.getConcepto()+"");
            concepto.setBackgroundResource(R.drawable.valuecellborder);
            costo.setText(Constantes.SIMBOLOMON+""+new DecimalFormat("#.00").format(detalle.getImporte()));
            costo.setBackgroundResource(R.drawable.valuecellborder);
            concepto.setLayoutParams(lp1);
            costo.setLayoutParams(lp2);
            concepto.setPadding(30,10,30,10);
            costo.setPadding(30,10,30,10);
            tableRow.addView(concepto);
            tableRow.addView(costo);
            mBinding.tblgaresconcep.addView(tableRow);
            try {
                sumacosto =sumacosto+ detalle.getImporte();

            }catch(NumberFormatException ex){
                compraslog.grabarError(TAG+" "+ex.getMessage());
                Toast.makeText(getActivity(), "Hubo un error al guardar intente de nuevo", Toast.LENGTH_SHORT).show();
                return;
            }


        }

        tableRow=new TableRow(getContext());

        concepto=new TextView(getContext());
        costo=new TextView(getContext());

        // tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
        //   tableRow.setLayoutParams(lp);
        costo.setBackgroundResource(R.drawable.valuecellborder);
        concepto.setBackgroundResource(R.drawable.valuecellborder);
        concepto.setText("TOTAL A VALIDAR");
        costo.setText(Constantes.SIMBOLOMON+new DecimalFormat("0.00").format(sumacosto));
        concepto.setLayoutParams(lp1);
        costo.setLayoutParams(lp2);
        concepto.setPadding(30,10,30,10);
        costo.setPadding(30,10,30,10);
        tableRow.addView(concepto);
        tableRow.addView(costo);
        mBinding.tblgaresconcep.addView(tableRow);

    }

    public void editarInforme() {

        compraslog.grabarError(TAG ,"editarInforme","editando informe"+informeSel);
        ImagenDetalle foto;
        //para saber si ya tego detalle
        List<InformeGastoDet> detalles=niviewModel.getGastoDetalles(informeSel);
        if(detalles!=null&&detalles.size()>0) //ya tengo algo
        {
            totalgastos= detalles.size();
            //muestro el ultimo
            totalval=niviewModel.calcularTotal(informeSel);
        }
    }


    public void atras(){
        Log.d(TAG,"atras**"+preguntaAct);
        compraslog.grabarError(TAG ,"atras","preguntaact="+preguntaAct);


        switch (preguntaAct){

            case 2:

                if(totalgastos>0) //ya no vuelvo
                {


                    break;
                }else {
                    llpreg1.setVisibility(View.GONE);
                    llresumenedi.setVisibility(View.VISIBLE);
                    preguntaAct = preguntaAct - 1;
                }



                mViewModel.preguntaAct = preguntaAct;

                break;
            case 3: //concepto
                llpreg1.setVisibility(View.VISIBLE);
                llconce.setVisibility(View.GONE);

                preguntaAct=preguntaAct-1;
                mViewModel.preguntaAct=preguntaAct;
                break;


            case 4://descripcion
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
                lldescripcion.setVisibility(View.VISIBLE);
                llcosto.setVisibility(View.GONE);

                preguntaAct=preguntaAct-2;
                mViewModel.preguntaAct=preguntaAct;

                break;
            case 7:
                llcosto.setVisibility(View.VISIBLE);
                llfoto.setVisibility(View.GONE);

                preguntaAct=preguntaAct-1;
                mViewModel.preguntaAct=preguntaAct;

                break;
            case 8:
                llcomentarios.setVisibility(View.GONE);
                if(mBinding.singasto.getRespuesta())
                {
                    llfoto.setVisibility(View.VISIBLE);
                    preguntaAct = preguntaAct - 1;}

                else {
                    llpreg1.setVisibility(View.VISIBLE);
                    preguntaAct=2;
                }


                mViewModel.preguntaAct=preguntaAct;
                break;

        }
        Log.d(TAG,"**"+preguntaAct);

    }


    public void guardarDet(){
        try{
            String rutafoto = null;
            CatalogoDetalle consel=(CatalogoDetalle) mBinding.spgasconcep.getSelectedItem();
            int conceptoid= consel.getCad_idopcion();
            String concepto=consel.getCad_descripcionesp();
            String descripcion=mBinding.txtgasdescrip.getText().toString();
            String costo=mBinding.txtgascosto.getText().toString();
            boolean tienecom= true;
            rutafoto = mBinding.txtgasrutafoto.getText().toString();

            compraslog.grabarError(TAG,"guardarDet","id nuevo inf "+mViewModel.getIdNuevo());

            Log.d(TAG,"preg act "+preguntaAct);
            //es un nuevo registro

            compraslog.grabarError(TAG,"guardarDet","no es edicion ");



            if(mViewModel.getIdNuevo()<1){
                throw new Exception("Se perdió el valor del idinforme");
            }
            InformeGastoDet nvoDet = new InformeGastoDet();
            nvoDet.setInformeEtapaId(mViewModel.getIdNuevo());
            nvoDet.setConcepto(concepto);
            nvoDet.setConceptoId(conceptoid);
            nvoDet.setDescripcion(descripcion);
            //cambio el importe
            if(!costo.equals("")) {
                costo=costo.substring(1).replaceAll(",","");
                // costo=costo.substring(1);
                try {
                    float importe=Float.valueOf(costo);
                    nvoDet.setImporte(importe);
                }catch (NumberFormatException ex) {
                    Toast.makeText(getActivity(),"El costo es incorrecto verifique",Toast.LENGTH_LONG).show();
                    compraslog.grabarError(TAG,"guardarDet"," costo incorrecto");


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

            compraslog.grabarError(TAG,"guardarDet","avanza");

            mBinding.txtgaconceptosel.setText("");
            avanzar();
        }catch (Exception ex){
            ex.printStackTrace();
            compraslog.grabarError(TAG,"guardarDet",ex.getMessage());
            Toast.makeText(getActivity(),"Hubo un error al guardar intente de nuevo",Toast.LENGTH_SHORT).show();

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
                compraslog.grabarError(TAG,"tomarFoto",archivofoto.getAbsolutePath());
            } catch (Exception ex) {
                ex.printStackTrace();
                compraslog.grabarError(TAG,"tomarFoto","No se encontró almacenamiento externo");

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
        compraslog.grabarError(TAG,"onActivityResult","vars"+requestCode +"--"+ nombre_foto);

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
                compraslog.grabarError(TAG,"tomarFoto","Algo salió mal???");

            }


        }else

        {
            compraslog.grabarError(TAG,"tomarFoto","Algo salió mal");

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
            compraslog.grabarError(TAG,"mostrarFoto","mostrado foto");

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
            compraslog.grabarError(TAG,"finalizarInf","total gastos="+totalgastos);

            String comentarios=mBinding.txtgascomentarios.getText().toString();

            if(mViewModel.getIdNuevo()<1){
                throw new Exception("Se perdió el valor del idinforme");
            }
            //busco los comentarios

            if(!comentarios.equals("")) {
                if(informeEdit.getComentarios()!=null&&!informeEdit.getComentarios().equals(""))
                    comentarios= informeEdit.getComentarios()+";"+comentarios;
                mViewModel.actualizarComentarios(mViewModel.getIdNuevo(),comentarios );

            }
            mViewModel.finalizarInfGasAjuste(mViewModel.getIdNuevo());
            compraslog.info(TAG,"finalizarInf","inf "+mViewModel.getIdNuevo());

            //espero un poco para enviarlo
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            InformeGastoEnv envio=niviewModel.prepararInformeEnv(mViewModel.getIdNuevo());

            SubirInformeGastoTask miTareaAsincrona = new SubirInformeGastoTask(envio,getActivity(),2);
            miTareaAsincrona.execute();
            subirFotos(getActivity(),envio);
            Toast.makeText(getActivity(),getString(R.string.informe_enviado),Toast.LENGTH_SHORT).show();

            salir();

        }catch(Exception ex){
            ex.printStackTrace();
            compraslog.grabarError(TAG,"finalizarInf","Algo salió mal al finalizar"+ex.getMessage());
            Toast.makeText(getActivity(),"Algo salio mal al enviar intente de nuevo desde el resumen",Toast.LENGTH_SHORT).show();
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

    public void llenarTablaConcepIni( List<InformeGastoDet> detalles){
        TableRow tableRow;
        NvoGastoViewModel niviewModel = new ViewModelProvider(requireActivity()).get(NvoGastoViewModel.class);

        TextView concepto;
        TextView costo;
        float sumacosto=0;
        float sumacostomuestra=0;
        int sumamuestras=0;
        TableRow.LayoutParams lp1;
        lp1 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, .7f);
        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, .3f);


        //pongo suma de muestras
        tableRow=new TableRow(getContext());

        concepto=new TextView(getContext());
        costo=new TextView(getContext());
        TextView cliente;
        TextView numuestra;

        concepto.setText("CONCEPTO");

        costo.setText("COSTO");
        costo.setBackgroundResource(R.drawable.valuecellborder);
        concepto.setBackgroundResource(R.drawable.valuecellborder);
        concepto.setLayoutParams(lp1);
        costo.setLayoutParams(lp2);
        concepto.setPadding(30,10,30,10);
        costo.setPadding(30,10,30,10);

        tableRow.addView(concepto);
        tableRow.addView(costo);
        mBinding.tblvigastos.addView(tableRow);
        Log.d(TAG, detalles.size()+"--"+detalles.toString());
        for (InformeGastoDet detalle:detalles
        ) {
            tableRow=new TableRow(getContext());

            Log.d(TAG,detalle.getId()+"--"+detalle.getConceptoId()+"--"+detalle.getConcepto()+"--"+detalle.getImporte());
            TextView tv1 = new TextView(getActivity());

            concepto=new TextView(getContext());
            costo=new TextView(getContext());
            //  tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            concepto.setLayoutParams(lp1);
            costo.setLayoutParams(lp2);

            concepto.setText(detalle.getConcepto());
            concepto.setBackgroundResource(R.drawable.valuecellborder);
            costo.setText(Constantes.SIMBOLOMON+""+new DecimalFormat("#.##").format(detalle.getImporte()));
            costo.setBackgroundResource(R.drawable.valuecellborder);
            //  concepto.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1));
            //   costo.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1));
            concepto.setLayoutParams(lp1);
            costo.setLayoutParams(lp2);
            concepto.setPadding(30,10,30,10);
            costo.setPadding(30,10,30,10);
            tableRow.addView(concepto);
            tableRow.addView(costo);
            mBinding.tblvigastos.addView(tableRow);
            try {
                sumacosto =sumacosto+ detalle.getImporte();
                if(detalle.getConceptoId()==0){
                    sumacostomuestra+=detalle.getImporte();
                }
            }catch(NumberFormatException ex){
                compraslog.grabarError(TAG+" "+ex.getMessage());
                ex.printStackTrace();
            }


        }

        tableRow=new TableRow(getContext());

        concepto=new TextView(getContext());
        costo=new TextView(getContext());

        tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        costo.setBackgroundResource(R.drawable.valuecellborder);
        concepto.setBackgroundResource(R.drawable.valuecellborder);
        concepto.setText("TOTAL A VALIDAR");
        costo.setText(Constantes.SIMBOLOMON+new DecimalFormat("#.##").format(sumacosto));
        //  concepto.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
        //   costo.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
        concepto.setLayoutParams(lp1);
        costo.setLayoutParams(lp2);
        concepto.setPadding(30,10,30,10);
        costo.setPadding(30,10,30,10);
        tableRow.addView(concepto);
        tableRow.addView(costo);
        mBinding.tblvigastos.addView(tableRow);
        niviewModel.guardarTotalmu(sumacostomuestra);
        //  tableRow=null;
        //  concepto=null;

        //  costo=null;
    }

    public class ListenerEdiGas implements  IListenerResumen{
        @Override
        public void guardarRes(List<InformeGastoDet> respuesta) {
            //acomodo en la tabla
            if (respuesta != null) {


                llenarTablaConcepIni(respuesta);
                mBinding.txtgaalgunerror.setText("");
                mBinding.llgasresumenedi.setVisibility(View.VISIBLE);
                aceptarresedi.setEnabled(true);
            } else {
                mBinding.txtgaalgunerror.setText("Hubo un error al hacer el informe de gastos");
                compraslog.grabarError(TAG, "ListenerM", "error al regresar de la peticion getTotalMuestras");
            }


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


    public void guardarMuestras(List<TotalMuestra> lista) {

        compraslog.grabarError(TAG,"guardarMuestras","guardar muestras");

        SharedPreferences prefe = getActivity().getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefe.edit();
        // editor.putString("claveusuario",cveusr);
        String json = new Gson().toJson(lista);
        editor.putString("totalmuestras", json);
        // editor.putString("password", Base64.encodeToString(passwordEditText.getText().toString().getBytes(), Base64.DEFAULT));
        editor.commit();



    }
    public String buscarMuestras() {

        SharedPreferences prefe = getActivity().getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        String listamuestras = prefe.getString("totalmuestras", "");
        return listamuestras;
    }
}