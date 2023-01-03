package com.example.comprasmu.ui.empaque;


import android.app.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.comprasmu.R;
import com.example.comprasmu.SubirInformeEtaTask;

import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.DetalleCaja;

import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;

import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.Reactivo;
import com.example.comprasmu.data.remote.InformeEtapaEnv;
import com.example.comprasmu.services.SubirFotoService;
import com.example.comprasmu.ui.RevisarFotoActivity;
import com.example.comprasmu.ui.infetapa.ContInfEtapaFragment;
import com.example.comprasmu.ui.infetapa.NuevoInfEtapaActivity;

import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.ui.preparacion.NvaPreparacionViewModel;

import com.example.comprasmu.utils.CampoForm;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;

import static android.app.Activity.RESULT_OK;


public class NvoEmpaqueFragment extends Fragment {
    CreadorFormulario cf;
    List<CampoForm> camposForm;

    private Reactivo preguntaAct;
    LinearLayout sv1,sv2,sv3,sv4,sv5, svotra, svcoin;
    private static final String TAG = "NvoEmpaqueFragment";

    private long lastClickTime = 0;
    private boolean yaestoyProcesando=false;

    ImageView fotomos;
    EditText textoint;
    LinearLayout sv;

    Button aceptar;
    private ImageButton btnrotar;
    public static  int REQUEST_CODE_TAKE_PHOTO=1;
    private View root;
    private NvaPreparacionViewModel mViewModel;
    private int  informeSel;
    private int plantaSel;
    private String nombrePlantaSel;
    private String clienteNombre;
    private int clienteId;
    private ListaDetalleViewModel lcViewModel;

    List<InformeEtapa> listainfetiq;
    private  ArrayList<DescripcionGenerica> listaPlantas;
    private boolean isEdicion;
    Spinner spplanta;
    InformeEtapaDet ultimares;
    DetalleCaja ultimarescaja;
    ComprasLog compraslog;

    public NvoEmpaqueFragment(Reactivo preguntaAct,boolean edicion) {
        this.preguntaAct = preguntaAct;
        this.isEdicion=edicion;

    }
    public NvoEmpaqueFragment() {

    }
    public static NvoEmpaqueFragment newInstance() {
        return new NvoEmpaqueFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_generic, container, false);

        try {


            mViewModel = new ViewModelProvider(requireActivity()).get(NvaPreparacionViewModel.class);
            lcViewModel = new ViewModelProvider(this).get(ListaDetalleViewModel.class);
            //reviso si es edicion y ya tengo info en temp

            sv = root.findViewById(R.id.content_generic);
            aceptar = root.findViewById(R.id.btngaceptar);
            Log.d(TAG, "iniciando"+isEdicion);
            compraslog=ComprasLog.getSingleton();
            if(preguntaAct!=null)
            {
                Log.d(TAG, "pregact"+preguntaAct.getLabel()+"--"+mViewModel.getIdNuevo());
                mViewModel.preguntaAct=preguntaAct.getId();
                //llegué por siguiente
                if(mViewModel.getIdNuevo()>0) {
                    plantaSel=mViewModel.getNvoinforme().getPlantasId();
                    if (preguntaAct.getTabla().equals("ED")) { //veo si ya está
                        mViewModel.getDetallexDescCaja(mViewModel.getIdNuevo(), preguntaAct.getNombreCampo(),mViewModel.cajaAct).observe(getViewLifecycleOwner(), new Observer<InformeEtapaDet>() {
                            @Override
                            public void onChanged(InformeEtapaDet informeEtapaDet) {
                                ultimares = informeEtapaDet;
                                if(ultimares!=null)
                                { mViewModel.cajaAct = ultimares.getNum_caja();

                                isEdicion=true;}
                                crearFormulario();


                            }


                        });
                    }
                     if (preguntaAct.getTabla().equals("DC")) {//veo si ya está
                         Log.d(TAG,"num caja"+mViewModel.cajaAct);
                        ultimarescaja = mViewModel.getDetalleCajaxCaja(mViewModel.getIdNuevo(), mViewModel.cajaAct);
                        if(ultimarescaja!=null) {
                            isEdicion = true;

                            if(ultimarescaja.getLargo()!=null)
                            mViewModel.largoCaja = Float.parseFloat(ultimarescaja.getLargo());
                            if(ultimarescaja.getAncho()!=null)
                             mViewModel.anchoCaja = Float.parseFloat(ultimarescaja.getAncho());
                            if(ultimarescaja.getAlto()!=null)
                                mViewModel.altoCaja = Float.parseFloat(ultimarescaja.getAlto());
                            if(ultimarescaja.getPeso()!=null)
                                mViewModel.pesoCaja = Float.parseFloat(ultimarescaja.getPeso());

                        }
                         crearFormulario();

                    }
                    if (preguntaAct.getTabla().equals("IE")) {
                        //son comentarios
                        crearFormulario();
                    }
                }else {
                   //no he guardado el informe
                    crearFormulario();

                }

            }else
            {

                Bundle datosRecuperados = getArguments();

                if (datosRecuperados != null) {
                    informeSel = datosRecuperados.getInt(NuevoInfEtapaActivity.INFORMESEL);


                if (informeSel > 0) //vengo de continuar busco el informe o de nueva caja
                {
                    if (isEdicion) {

                        mViewModel.getInformeEdit(informeSel).observe(getViewLifecycleOwner(), new Observer<InformeEtapa>() {
                            @Override
                            public void onChanged(InformeEtapa informeEtapa) {

                                mViewModel.setNvoinforme(informeEtapa);
                                plantaSel=informeEtapa.getPlantasId();
                                mViewModel.setIdNuevo(informeSel);
                                int pregact = 0;
                                mViewModel.buscarInformeEtiq(Constantes.INDICEACTUAL, informeEtapa.getPlantasId());
                                ((NuevoInfEtapaActivity) getActivity()).actualizarBarra(informeEtapa);
                                //busco si tengo detalle
                                ultimares = mViewModel.getUltimoInformeDet(informeSel, 4);
                                if (ultimares == null) {      //voy en la 91
                                    //no deberia estar aqui
                                    Log.e(TAG, "no deberia entrar aqui");
                                } else {
                                    mViewModel.cajaAct = ultimares.getNum_caja();
                                    //ya tengo dimensiones?
                                    ultimarescaja = mViewModel.getUltimoxCaja(informeSel,mViewModel.cajaAct );
                                    Log.d(TAG, "lol"+ultimares.getNum_caja());

                                    if (ultimarescaja != null)//voy en dimensiones
                                    {
                                        Log.d(TAG, ultimarescaja.getInformeEtapaId() + "<--" + ultimarescaja.getLargo() + "--" + ultimarescaja.getAncho() + "--" + ultimarescaja.getAlto() + "--" + ultimarescaja.getPeso());
                                        if (ultimarescaja.getLargo() != null)
                                            mViewModel.largoCaja = Float.parseFloat(ultimarescaja.getLargo());
                                        if (ultimarescaja.getAncho() != null)
                                            mViewModel.anchoCaja = Float.parseFloat(ultimarescaja.getAncho());
                                        if (ultimarescaja.getAlto() != null)
                                            mViewModel.altoCaja = Float.parseFloat(ultimarescaja.getAlto());
                                        if (ultimarescaja.getPeso() != null)
                                            mViewModel.pesoCaja = Float.parseFloat(ultimarescaja.getPeso());

                                        if (mViewModel.largoCaja > 0)
                                            pregact = 96;
                                        if (mViewModel.anchoCaja > 0)
                                            pregact = 98;
                                        if (mViewModel.altoCaja > 0)
                                            pregact = 100;
                                        if (mViewModel.pesoCaja > 0)
                                            pregact = 102;

                                        mViewModel.cajaAct = ultimarescaja.getNum_caja();

                                        if (pregact > 0) {
                                            mViewModel.buscarReactivo(pregact).observe(getViewLifecycleOwner(), new Observer<Reactivo>() {
                                                @Override
                                                public void onChanged(Reactivo reactivo) {
                                                    preguntaAct = reactivo;
                                                    mViewModel.preguntaAct = preguntaAct.getId();
                                                    isEdicion = true;
                                                    crearFormulario();
                                                }
                                            });
                                        } else {
                                            //solo es foto
                                            preguntaAct = mViewModel.buscarReactivoxDesc(ultimares.getDescripcion(), 4);
                                            mViewModel.preguntaAct = preguntaAct.getId();
                                            isEdicion = true;
                                            crearFormulario();

                                        }
                                    } else {

                                        //busco por la pregunta
                                        preguntaAct = mViewModel.buscarReactivoxDesc(ultimares.getDescripcion(), 4);
                                        mViewModel.preguntaAct = preguntaAct.getId();
                                        isEdicion = true;
                                        crearFormulario();
                                    }

                                }
                            }
                        });
                    }else
                    {
                         mViewModel.variasPlantas = false;

                           /* nombrePlantaSel = listainfetiq.get(0).getPlantaNombre();
                            plantaSel = listainfetiq.get(0).getPlantasId();
                            clienteId = listainfetiq.get(0).getClientesId();
                            clienteNombre = listainfetiq.get(0).getClienteNombre();
                            InformeEtapa informetemp = new InformeEtapa();
                            informetemp.setClienteNombre(clienteNombre);
                            informetemp.setClientesId(clienteId);
                            informetemp.setPlantasId(plantaSel);
                            informetemp.setPlantaNombre(nombrePlantaSel);
                            informetemp.setIndice(Constantes.INDICEACTUAL);
                            mViewModel.setNvoinforme(informetemp);*/
                            //((NuevoInfEtapaActivity) getActivity()).actualizarBarra(informetemp);
                            //me voy a la sig
                            mViewModel.buscarInformeEtiq(Constantes.INDICEACTUAL,plantaSel);
                            /*if(mViewModel.getInformeEtiq()==null){
                                //no tengo inf etiqueta de esta planta
                                Toast.makeText(getContext(),"NO TIENE INFORME DE ETIQUETADO DE "+nombrePlantaSel,Toast.LENGTH_SHORT).show();

                                return root;
                            }*/
                           // mViewModel.cajaAct=1;
                            mViewModel.buscarReactivo(92).observe(getViewLifecycleOwner(), new Observer<Reactivo>() {
                                @Override
                                public void onChanged(Reactivo reactivo) {
                                    preguntaAct = reactivo;
                                    crearFormulario();
                                }
                            });
                    }
                }
                } else { //es nuevo pregunta 91
  //reviso si ya tengo uno abierto
            InformeEtapa informeEtapa = mViewModel.getInformePend(Constantes.INDICEACTUAL,4);

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


                    //busco si tengo varias plantas
                    listainfetiq = mViewModel.getPlantasconInf(Constantes.INDICEACTUAL);
                    Log.d(TAG, "id nuevo" + mViewModel.getIdNuevo() + "--" + listainfetiq.size());

                    if (mViewModel.getIdNuevo() == 0)
                        if (listainfetiq.size() > 1) {
                            //tengo varias plantas
                            // preguntaAct=1;
                            convertirLista(listainfetiq);

                            mViewModel.variasPlantas = true;
                            buscarPreguntas();
                            preguntaAct = mViewModel.getListaPreguntas().get(0);
                            crearFormulario();

                        } else if (listainfetiq.size() > 0) {

                            mViewModel.variasPlantas = false;
                            nombrePlantaSel = listainfetiq.get(0).getPlantaNombre();
                            plantaSel = listainfetiq.get(0).getPlantasId();
                            clienteId = listainfetiq.get(0).getClientesId();
                            clienteNombre = listainfetiq.get(0).getClienteNombre();
                            InformeEtapa informetemp = new InformeEtapa();
                            informetemp.setClienteNombre(clienteNombre);
                            informetemp.setClientesId(clienteId);
                            informetemp.setPlantasId(plantaSel);
                            informetemp.setPlantaNombre(nombrePlantaSel);
                            informetemp.setIndice(Constantes.INDICEACTUAL);
                            mViewModel.setNvoinforme(informetemp);
                            ((NuevoInfEtapaActivity) getActivity()).actualizarBarra(informetemp);
                            //me voy a la sig
                            mViewModel.buscarInformeEtiq(Constantes.INDICEACTUAL,plantaSel);
                            if(mViewModel.getInformeEtiq()==null){
                                //no tengo inf etiqueta de esta planta
                                Toast.makeText(getContext(),"NO TIENE INFORME DE ETIQUETADO DE "+nombrePlantaSel,Toast.LENGTH_SHORT).show();

                                return root;
                            }
                            mViewModel.cajaAct=1;
                            mViewModel.buscarReactivo(92).observe(getViewLifecycleOwner(), new Observer<Reactivo>() {
                                @Override
                                public void onChanged(Reactivo reactivo) {
                                    preguntaAct = reactivo;
                                    crearFormulario();
                                }
                            });


                        }else{
                            Toast.makeText(getContext(),"NO TIENE INFORMES DE ETIQUETADO ",Toast.LENGTH_LONG).show();

                           getActivity().finish();
                        }
                }
            }


                aceptar.setEnabled(false);
            if(isEdicion||preguntaAct!=null&&preguntaAct.getId()==104){
                aceptar.setEnabled(true);
            }




                aceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG,"DICLICK");
                        aceptar.setEnabled(false);
                        long currentClickTime = SystemClock.elapsedRealtime();
                        // preventing double, using threshold of 1000 ms
                        if (currentClickTime - lastClickTime < 5500) {
                            //  Log.d(TAG,"doble click :("+lastClickTime);
                            return;
                        }

                        lastClickTime = currentClickTime;

                        // if(preguntaAct.getNombreCampo().equals("plantasId")){
                        //   guardarCliente();
                        //}
                        //else
                        siguiente();

                    }
                });



        } catch (Exception e) {
            e.printStackTrace();
        }

        return root;
    }
//mViewModel.getInformeEtiq().getTotal_cajas()
  //      mViewModel.getInformeEtiq().getTotal_muestras()
    public void crearFormulario(){
        camposForm=new ArrayList<>();
        CampoForm campo=new CampoForm();
        Log.d(TAG, "pregunta "+preguntaAct.getId());
        if(preguntaAct.getId()>91&&preguntaAct.getId()<104) {
            campo.label = "CAJA NUMERO " + mViewModel.cajaAct;
            campo.type = "label";
            camposForm.add(campo);

            campo = new CampoForm();
            campo.label = "MUESTRAS EN CAJA " + mViewModel.getInformeEtiq().getTotal_muestras();
            campo.type = "label";
            camposForm.add(campo);


            campo = new CampoForm();
        }
        campo.label=preguntaAct.getLabel();
        campo.nombre_campo=preguntaAct.getNombreCampo();
        campo.type=preguntaAct.getType();
        campo.style=R.style.formlabel2;
        if(isEdicion&&preguntaAct.getTabla().equals("ED"))
        {
            ImagenDetalle foto=mViewModel.getFoto(Integer.parseInt(ultimares.getRuta_foto()));

            campo.value=foto.getRuta();
        }

        if(isEdicion&&preguntaAct.getTabla().equals("DC"))
        {
            switch (preguntaAct.getId()) {
                case 96:
                    campo.value = mViewModel.largoCaja + "";
                    break;
                case 98:
                    campo.value = mViewModel.anchoCaja + "";
                    break;
                case 100:
                    campo.value = mViewModel.altoCaja + "";
                    break;
                case 102:
                    campo.value = mViewModel.pesoCaja + "";
                    break;

            }
        }
        campo.id=1001;
        //para las plantas
        if(preguntaAct.getType().equals(CreadorFormulario.SELECTDES)){
            switch (preguntaAct.getNombreCampo()){
                case "plantasId":


                    campo.selectdes=listaPlantas;
                    break;

            }

        }
        if(campo.type.equals("agregarImagen")) {

            campo.funcionOnClick = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tomarFoto();
                }
            };
            campo.tomarFoto = true;

            fotomos=root.findViewById(R.id.ivgfoto);
            fotomos.setVisibility(View.VISIBLE);
            btnrotar=root.findViewById(R.id.btngrotar);
            btnrotar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rotar(1001);
                }
            });

            if(isEdicion){
                // Bitmap bitmap1 = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null) + "/" + nombre_foto);
                //ComprasUtils cu=new ComprasUtils();
                // bitmap1=cu.comprimirImagen(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + ultimares.getValor());
                ImagenDetalle foto=mViewModel.getFoto(Integer.parseInt(ultimares.getRuta_foto()));

                Bitmap bitmap1= ComprasUtils.decodeSampledBitmapFromResource(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + foto.getRuta(), 100, 100);

                fotomos.setImageBitmap(bitmap1);
                // fotomos.setLayoutParams(new LinearLayout.LayoutParams(350,150));
                fotomos.setVisibility(View.VISIBLE);

                btnrotar.setVisibility(View.VISIBLE);
                btnrotar.setFocusableInTouchMode(true);
                btnrotar.requestFocus();

            }
            // btnrotar.setVisibility(View.VISIBLE);


        }

       /* if(preguntaAct.getType().equals(CreadorFormulario.PREGUNTASINO)) {
            campo.value

        }*/
        camposForm.add(campo);

        cf=new CreadorFormulario(camposForm,getContext());

        sv.addView(cf.crearFormulario());
        if(preguntaAct.getType().equals(CreadorFormulario.SELECTCAT)||preguntaAct.getType().equals(CreadorFormulario.SELECTDES)||preguntaAct.getType().equals(CreadorFormulario.PSELECT)) {
            spplanta = root.findViewById(1001);
        }
        else
        {
            textoint = root.findViewById(1001);
        }
        if(textoint!=null&&preguntaAct.getId()!=104){ //los comentarios no son obligatorios
            textoint.addTextChangedListener(new BotonTextWatcher());

        }

        if(isEdicion&&!textoint.getText().equals("")){
            aceptar.setEnabled(true);
        }
         if(preguntaAct.getId()==104){ //los comentarios no son obligatorios
            //  textoint.addTextChangedListener(new MayusTextWatcher());
            textoint.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
            aceptar.setEnabled(true);
        }
        if (preguntaAct!=null&&preguntaAct.getId() == 104) {
            //cambio el boton a finalizar y muestro alerta
            aceptar.setText(getString(R.string.enviar));
            aceptar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.botonvalido));
        }

        if(spplanta!=null){
            spplanta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    aceptar.setEnabled(true);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });
        }
    }
    //cambiar estatus sol
    public void finalizarInf() {
        try {
            mViewModel.finalizarInf();
            InformeEtapaEnv envio=this.preparaInforme();
            SubirInformeEtaTask miTareaAsincrona = new SubirInformeEtaTask(envio,getActivity());
            miTareaAsincrona.execute();
            subirFotos(getActivity(),envio);
        }catch(Exception ex){
            compraslog.grabarError("Algo salió mal al finalizar inf"+ex.getMessage());
            //Log.e(TAG,"Algo salió mal al finalizar inf"+ex.getMessage());
            Toast.makeText(getContext(),"Algo salio mal al enviar",Toast.LENGTH_SHORT).show();
        }
        //todo limpio variables de sesion

        mViewModel.setIdNuevo(0);
        mViewModel.setIddetalle(0);
        mViewModel.setNvoinforme(null);

    }
    public void siguiente(){
        boolean resp=false;
        lastClickTime=0;
        aceptar.setEnabled(false);
       /* if (textoint != null) {
           String valor = textoint.getText().toString();
           if(valor.length()<=0){
               return;
           }

        }*/

        switch (preguntaAct.getNombreCampo()){
            case "dimalto":case "dimlargo":case "dimancho":case "dimpeso":
                String  valor = textoint.getText().toString();
                try {
                    float val = Float.parseFloat(valor);
                }catch(NumberFormatException ex){
                    Toast.makeText(getActivity(),"Captura inválida, verifique",Toast.LENGTH_LONG).show();
                    return;

                }
                 resp=true;
                break;

            default: resp=true; break;
        }

        if(resp)
        {

            if(preguntaAct.getId()==91 ) { //planta
                DescripcionGenerica opcionsel = (DescripcionGenerica) spplanta.getSelectedItem();

                //busco par de id, cliente
                String[] aux = opcionsel.getDescripcion().split(",");
                clienteId = Integer.parseInt(aux[0]);
                clienteNombre = aux[1];
                plantaSel = opcionsel.getId();
                nombrePlantaSel = opcionsel.getNombre();
                InformeEtapa informetemp=new InformeEtapa();
                informetemp.setClienteNombre(clienteNombre);
                informetemp.setClientesId(clienteId);
                informetemp.setPlantasId(plantaSel);
                informetemp.setPlantaNombre(nombrePlantaSel);
                informetemp.setIndice(Constantes.INDICEACTUAL);
                mViewModel.setNvoinforme(informetemp);
                ((NuevoInfEtapaActivity)getActivity()).actualizarBarra(informetemp);
                mViewModel.cajaAct=1;

            }else
            if(preguntaAct.getId()==92 ) { //acomodo creo el informe y el detalle
                String  valor = textoint.getText().toString();
                this.guardarInf();
                this.guardarDet();
                //guardo el atributo para mostrarlo despues
                //    ((NuevoInfEtapaActivity)getActivity()).actualizarCodProd(valor);

            }else
            if(preguntaAct.getId()==104) //termine inf comentarios
            {
                if(yaestoyProcesando)
                    return;
                yaestoyProcesando=true;

                //guardarResp();

                try {
                    //guarda informe
                    this.actualizarComent();
                    this.finalizarInf();



                       Toast.makeText(getActivity(), getString(R.string.informe_finalizado), Toast.LENGTH_SHORT).show();
                        yaestoyProcesando = false;
                        salir();
                        //  aceptar.setEnabled(true);
                        return;


                }catch(Exception ex){
                    ex.printStackTrace();
                    yaestoyProcesando=false;
                    compraslog.grabarError("Algo salió mal"+ex.getMessage());
                    Toast.makeText(getActivity(), "algo salió mal", Toast.LENGTH_SHORT).show();

                }

            }else
            if(preguntaAct.getId()==103)//terminé con preguntas de caja
            {
                if(yaestoyProcesando)
                    return;
                yaestoyProcesando=true;
                aceptar.setEnabled(false);
                guardarDet();
                //reviso si hay más cajas, si no fin
                int sig=mViewModel.cajaAct+1;
                mViewModel.cajaAct=sig;
                Log.d(TAG,"cajas"+sig+"--"+mViewModel.getInformeEtiq().getTotal_cajas());
                if(sig<=mViewModel.getInformeEtiq().getTotal_cajas()){
                    //empiezo en las preguntas con nueva caja
                    //empiezo de 0
                  /*  Bundle bundle = new Bundle();
                    bundle.putInt(NuevoInfEtapaActivity.INFORMESEL,mViewModel.getIdNuevo());
                    bundle.putInt(NuevoInfEtapaActivity.PLANTASEL,plantaSel);
                    Log.d(TAG,"a otra caja "+plantaSel);
                    bundle.putInt(ContInfEtapaFragment.ETAPA,4);

                    //NavHostFragment.findNavController(this).navigate(R.id.action_visitatonuevo,bundle);
                    Intent intento1=new Intent(getActivity(), NuevoInfEtapaActivity.class);
                    intento1.putExtras(bundle);
                    startActivity(intento1);
                    requireActivity().finish();*/
                    avanzarPregunta(92);
                    return;

                }
                else
                {
                    avanzarPregunta(preguntaAct.getSigAlt());
                return;
                }
               // int nummuestra=mViewModel.numMuestra;

                //quito la info de la barra gris
              //  ((NuevoInfEtapaActivity)getActivity()).reiniciarBarraCaja();
               // Log.d(TAG,"antes de guardar num muestra"+nummuestra);
                //avanzarPregunta(preguntaAct.getSigId());
            }else {
                if (preguntaAct.getId() > 91)
                    if (!guardarDet())
                        return;

            }
            Log.d(TAG,"AVANZANDO"+preguntaAct.getId());
            avanzarPregunta(preguntaAct.getSigId());

        }
        aceptar.setEnabled(true);
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
    public void actualizarComent(){

        String comentarios = null;

        if(textoint.getText()!=null)
            comentarios=textoint.getText().toString().toUpperCase();

        try{
            mViewModel.actualizarComentEmp(mViewModel.getIdNuevo(),comentarios);
        }catch(Exception ex){

            compraslog.grabarError(TAG+" Algo salio mal al enviar"+ex.getMessage());

            Toast.makeText(getContext(),"Algo salio mal al enviar",Toast.LENGTH_SHORT).show();
        }
    }

    String nombre_foto;
    File archivofoto;
    public void tomarFoto(){
        if(ComprasUtils.getAvailableMemory(getActivity()).lowMemory)
        {
            Toast.makeText(getActivity(), "No hay memoria suficiente para esta accion", Toast.LENGTH_SHORT).show();

            return;
        }else {
            Activity activity = this.getActivity();
            Intent intento1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
            Uri photoURI = FileProvider.getUriForFile(activity,
                    "com.example.comprasmu.fileprovider",
                    archivofoto);
            intento1.putExtra(MediaStore.EXTRA_OUTPUT, photoURI); //se pasa a la otra activity la referencia al archivo

            if (fotomos != null) {

                startActivityForResult(intento1, REQUEST_CODE_TAKE_PHOTO);

            }
        }

    }
    public void guardarInf(){
        try {
            lastClickTime = 0;

            /*if( mViewModel.variasPlantas) {
                DescripcionGenerica opcionsel = (DescripcionGenerica) spplanta.getSelectedItem();

                //busco par de id, cliente
                String aux[] = opcionsel.getDescripcion().split(",");
                clienteId = Integer.parseInt(aux[0]);
                clienteNombre = aux[1];
                plantaSel = opcionsel.getId();
                nombrePlantaSel = opcionsel.getNombre();
            }*/
            //totcajas =Integer.parseInt(txtnumcajas.getText().toString());
            Log.d(TAG,"edicion"+isEdicion+"--"+mViewModel.getIdNuevo());
            if (preguntaAct.getId() == 92 && !isEdicion&&mViewModel.getNvoinforme()!=null) {
                Log.d(TAG, "creando nvo inf");
                //creo el informe
                if(mViewModel.getIdNuevo()==0)
                mViewModel.setIdNuevo(mViewModel.insertarInformeEtapae(Constantes.INDICEACTUAL,mViewModel.getNvoinforme().getPlantaNombre(),mViewModel.getNvoinforme().getPlantasId(),mViewModel.getNvoinforme().getClienteNombre(),mViewModel.getNvoinforme().getClientesId(),4));
            }
        }catch (Exception ex){
            ex.getStackTrace();
            compraslog.grabarError(TAG+"Hubo un error al guardar intente de nuevo"+ex.getMessage());

            Toast.makeText(getContext(),"Hubo un error al guardar intente de nuevo",Toast.LENGTH_SHORT).show();

        }
        ((NuevoInfEtapaActivity)getActivity()).actualizarBarra(mViewModel.getNvoinforme());

        aceptar.setEnabled(true);
      //  avanzarPregunta(preguntaAct);
    }
    public boolean guardarDet(){
        try{
            String rutafoto = null;
            String qr = null;

            Log.d(TAG,"guardando det"+mViewModel.getIdNuevo());
            rutafoto=textoint.getText().toString();
           // qr=txtqr.getText().toString();
            //es foto o dimension?
            if(preguntaAct.getTabla().equals("ED")) {
                if (isEdicion) {
                    //actualizo imagen detalle
                    mViewModel.actualizarImagenDet(Integer.parseInt(ultimares.getRuta_foto()),"foto_empaque" + preguntaAct,textoint.getText().toString(),Constantes.INDICEACTUAL);

                  //  mViewModel.insertarEmpDet(mViewModel.getIdNuevo(), 1, preguntaAct.getNombreCampo(), rutafoto, ultimares.getId(), mViewModel.cajaAct);
                    isEdicion = false;
                } else if (mViewModel.getIdNuevo() > 0)
                    //guardo el detalle
                    mViewModel.insertarEmpDet(mViewModel.getIdNuevo(), 1, preguntaAct.getNombreCampo(), rutafoto, 0, mViewModel.cajaAct,Constantes.INDICEACTUAL);
                    fotomos.setImageBitmap(null);
                    fotomos.setVisibility(View.GONE);
                    btnrotar.setVisibility(View.GONE);
            }else
            if(preguntaAct.getTabla().equals("DC")) {

                  if (mViewModel.getIdNuevo() > 0&&ultimarescaja==null)
                        //guardo el detalle
                    {
                        Log.d(TAG,"guardando caja"+mViewModel.cajaAct);
                        mViewModel.setIddetalle( mViewModel.insertarDetCaja(mViewModel.getIdNuevo(), 0, mViewModel.cajaAct));

                    }
                try {
                    if(ultimarescaja!=null&&ultimarescaja.getId()>0) {
                        mViewModel.setIddetalle(ultimarescaja.getId());

                        switch (preguntaAct.getId()) {
                            case 96:
                                mViewModel.largoCaja = Float.parseFloat(rutafoto);
                                mViewModel.actDetCaja(mViewModel.getIdNuevo(), mViewModel.getIddetalle(), mViewModel.cajaAct, ultimarescaja.getAncho(), ultimarescaja.getAlto(), ultimarescaja.getPeso(), mViewModel.largoCaja + "");

                                break;
                            case 98:
                                mViewModel.anchoCaja = Float.parseFloat(rutafoto);
                                mViewModel.actDetCaja(mViewModel.getIdNuevo(), mViewModel.getIddetalle(), mViewModel.cajaAct, mViewModel.anchoCaja + "", ultimarescaja.getAlto(), ultimarescaja.getPeso(), ultimarescaja.getLargo());

                                break;
                            case 100:
                                mViewModel.altoCaja = Float.parseFloat(rutafoto);
                                mViewModel.actDetCaja(mViewModel.getIdNuevo(), mViewModel.getIddetalle(), mViewModel.cajaAct, ultimarescaja.getAncho(), mViewModel.altoCaja + "", ultimarescaja.getPeso(), ultimarescaja.getLargo());

                                break;
                            case 102:
                                mViewModel.pesoCaja = Float.parseFloat(rutafoto);
                                mViewModel.actDetCaja(mViewModel.getIdNuevo(), mViewModel.getIddetalle(), mViewModel.cajaAct, ultimarescaja.getAncho(), ultimarescaja.getAlto(), mViewModel.pesoCaja + "", ultimarescaja.getLargo());

                                break;
                        }
                    }else{
                        mViewModel.largoCaja = Float.parseFloat(rutafoto);
                        mViewModel.actDetCaja(mViewModel.getIdNuevo(), mViewModel.getIddetalle(), mViewModel.cajaAct, "", "", "", mViewModel.largoCaja + "");

                    }
                }catch(NumberFormatException ex){
                    Toast.makeText(getContext(),"Número incorrecto, verifique",Toast.LENGTH_SHORT).show();
                }
                if(isEdicion)
                    isEdicion=false;




            }

            //limpio campos
            textoint.setText("");
//

            return true;
        }catch (Exception ex){
            compraslog.grabarError(TAG+" Hubo un error al guardar intente de nuevo"+ex.getMessage());

            Toast.makeText(getContext(),"Hubo un error al guardar intente de nuevo",Toast.LENGTH_SHORT).show();
            return  false;
        }

    }

    //cambi
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"vars"+requestCode +"--"+ nombre_foto);
        if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {
            //   super.onActivityResult(requestCode, resultCode, data);

            if (archivofoto!=null&&archivofoto.exists()) {
                if(requestCode == REQUEST_CODE_TAKE_PHOTO) {
                    mostrarFoto(textoint,fotomos,btnrotar);
                }

                aceptar.setEnabled(true);

            }
            else{
                compraslog.grabarError(TAG+"Algo salió mal???");


            }


        }   else
        {
            compraslog.grabarError(TAG+"Algo salió muy mal");
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

        //me voy a la lista de informes
        getActivity().finish();
      //  Intent intento1 = new Intent(getActivity(), NavigationDrawerActivity.class);
     //   intento1.putExtra(NavigationDrawerActivity.NAVINICIAL,"listainforme");
      //  startActivity(intento1);
        // NavHostFragment.(this).navigate(R.id.action_selclientetolistacompras,bundle);


    }


    public InformeEtapaEnv preparaInforme(){
        InformeEtapaEnv envio=new InformeEtapaEnv();

        envio.setInformeEtapa(mViewModel.getNvoinforme());
        envio.setClaveUsuario(Constantes.CLAVEUSUARIO);
        envio.setIndice(Constantes.INDICEACTUAL);
        envio.setInformeEtapaDet(mViewModel.cargarInformeDet(mViewModel.getIdNuevo()));
        envio.setDetalleCaja(mViewModel.cargarDetCajas(mViewModel.getIdNuevo()));
        Log.d(TAG, " det cajas "+envio.getDetalleCaja().get(0).getNum_caja());
        return envio;
    }

    public static void subirFotos(Activity activity, InformeEtapaEnv informe){
        //las imagenes
        for(InformeEtapaDet imagen:informe.getInformeEtapaDet()){
            //subo cada una
            Intent msgIntent = new Intent(activity, SubirFotoService.class);
            msgIntent.putExtra(SubirFotoService.EXTRA_IMAGE_ID, imagen.getId());
            msgIntent.putExtra(SubirFotoService.EXTRA_IMG_PATH,imagen.getRuta_foto());
            msgIntent.putExtra(SubirFotoService.EXTRA_INDICE,informe.getIndice());
            // Constantes.INDICEACTUAL
            Log.d(TAG,"subiendo fotos"+activity.getLocalClassName());

            msgIntent.setAction(SubirFotoService.ACTION_UPLOAD_ETA);

            //cambio su estatus a subiendo
            imagen.setEstatusSync(1);
            activity.startService(msgIntent);

        }

    }
    public void buscarPreguntas() {
        mViewModel.buscarReactivos();

    }
    private  void convertirLista(List<InformeEtapa>lista){
        listaPlantas=new ArrayList<DescripcionGenerica>();
        for (InformeEtapa listaCompra: lista ) {

            listaPlantas.add(new DescripcionGenerica(listaCompra.getPlantasId(), listaCompra.getClienteNombre()+" "+listaCompra.getPlantaNombre(),listaCompra.getClientesId()+","+listaCompra.getClienteNombre()));

        }

    }
    public void avanzarPregunta(int sig){
        Log.d(TAG,"sig "+sig);
        if(sig==92){
            Log.e(TAG,plantaSel+"..");
            //ya tengo la planta busco el informe etiquetado
            mViewModel.buscarInformeEtiq(Constantes.INDICEACTUAL,plantaSel);

            if(mViewModel.getInformeEtiq()==null){
                //no tengo inf etiqueta de esta planta
                Toast.makeText(getContext(),"NO TIENE INFORME DE ETIQUETADO DE "+nombrePlantaSel,Toast.LENGTH_SHORT).show();

                return ;
            }

        }
        if(sig==0){
            //empiezo de 0
            Bundle bundle = new Bundle();
            bundle.putInt(NuevoInfEtapaActivity.INFORMESEL,mViewModel.getIdNuevo());

            //NavHostFragment.findNavController(this).navigate(R.id.action_visitatonuevo,bundle);
            Intent intento1=new Intent(getActivity(), NuevoInfEtapaActivity.class);
            intento1.putExtras(bundle);
            requireActivity().finish();
            startActivity(intento1);
        }
      /*  if(sig==91) //reviso si hay otra caja
        {
            if(mViewModel.cajaAct+1<=mViewModel.getInformeEtiq().getTotal_cajas()){

            }else
                sig=preguntaAct.getSigAlt();
        }*/
        //busco el siguiente reactivo
        LiveData<Reactivo> nvoReac = mViewModel.buscarReactivo(sig);
        nvoReac.observe(getViewLifecycleOwner(), new Observer<Reactivo>() {
            @Override
            public void onChanged(Reactivo reactivo) {
                Log.d(TAG,"sigu "+reactivo.getId()+"--"+reactivo.getNombreCampo());
                NvoEmpaqueFragment nvofrag = new NvoEmpaqueFragment(reactivo,false);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
// Definir una transacción
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
// Remplazar el contenido principal por el fragmento
                fragmentTransaction.replace(R.id.continfeta_fragment, nvofrag);
                //     fragmentTransaction.addToBackStack(null);
// Cambiar
                fragmentTransaction.commit();
            }
        });
    }
    class BotonTextWatcher implements TextWatcher {

        boolean mEditing;

        public BotonTextWatcher() {
            mEditing = false;
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

    public int getPreguntaAct() {
        return preguntaAct.getId();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel = null;

        root=null;
        textoint=null;

        fotomos=null;
        sv1=sv2=sv3=sv4=null;
        btnrotar=null;
        aceptar=null;
        nombre_foto=null;
        archivofoto=null;
    }




}

