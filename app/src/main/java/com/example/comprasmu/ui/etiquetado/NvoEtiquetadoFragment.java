package com.example.comprasmu.ui.etiquetado;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.ArrayAdapter;
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
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;

import com.example.comprasmu.SubirInformeEtaTask;

import com.example.comprasmu.data.modelos.DescripcionGenerica;

import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.modelos.ListaCompra;

import com.example.comprasmu.data.remote.InformeEtapaEnv;
import com.example.comprasmu.services.SubirFotoService;
import com.example.comprasmu.ui.RevisarFotoActivity;
import com.example.comprasmu.ui.infetapa.NuevoInfEtapaActivity;

import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.ui.preparacion.NvaPreparacionViewModel;

import com.example.comprasmu.ui.tiendas.DescripcionGenericaAdapter;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;

import com.example.comprasmu.utils.Preguntasino;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import static android.app.Activity.RESULT_OK;


public class NvoEtiquetadoFragment extends Fragment {

    private  InformeEtapaDet detalleEdit;
    LinearLayout sv1,sv2,sv3,sv4,sv5, svotra, svcoin;
    private static final String TAG = "NvoEtiquetadoFragment";
    Button aceptar1,aceptar2,aceptar3,aceptar4,aceptar5,aceptar6,guardar;
    private long lastClickTime = 0;
    private boolean yaestoyProcesando=false;
    EditText txtnumcajas,txtrutaim,txtcomentarios, txtqr;
    TextView txtnumuestra, txtcajaact;
    ImageView fotomos;
    private ImageButton btnrotar, btntomarf,btnqr;
    public static  int REQUEST_CODE_TAKE_PHOTO=1;
    String rutafotoo;
    Preguntasino potra, pcoincide;
    private View root;
    private NvaPreparacionViewModel mViewModel;
    private int  informeSel;
    private int plantaSel;
    private String nombrePlantaSel;
    private String clienteNombre;
    private int clienteId;
    private boolean isCont;
    private InformeEtapa infomeEdit;
    int preguntaAct;
    private ListaDetalleViewModel lcViewModel;
    List<ListaCompra> listacomp;
    private static final int REQUEST_CODEQR = 345;
    int contcaja, contmuestra, contmuint;
    int totcajas=0,totmuestras;
    private  ArrayList<DescripcionGenerica> listaPlantas;
    private boolean isEdicion;
    Spinner spplanta;
    RecyclerView listaqr;
    ComprasLog milog;
    public NvoEtiquetadoFragment(int preguntaAct,boolean edicion, InformeEtapaDet informeEdit,int informeSel) {
        this.preguntaAct = preguntaAct;
        this.isEdicion=edicion;
        this.detalleEdit=informeEdit;
        this.informeSel=informeSel;
    }
    public NvoEtiquetadoFragment() {

    }
    public static NvoEtiquetadoFragment newInstance() {
        return new NvoEtiquetadoFragment();
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        root= inflater.inflate(R.layout.fragment_nvoetiq, container, false);
        sv1 = root.findViewById(R.id.llnepreg1);
        sv2 = root.findViewById(R.id.llnepreg2);
        sv3 = root.findViewById(R.id.llnepreg3);
        sv4 = root.findViewById(R.id.llnepre4);
        sv5 = root.findViewById(R.id.llnepre5);
        svotra = root.findViewById(R.id.llneotra);
        svcoin = root.findViewById(R.id.llnecoincide);
        aceptar1 = root.findViewById(R.id.btnneac1);
        aceptar2 = root.findViewById(R.id.btnneac2);
        aceptar3 = root.findViewById(R.id.btnneac3);
        aceptar4 = root.findViewById(R.id.btnneac4);
        aceptar5 = root.findViewById(R.id.btnneac5);
        aceptar6 = root.findViewById(R.id.btnneac6);
        guardar = root.findViewById(R.id.btnneguardar);
        btnrotar = root.findViewById(R.id.btnnerotar1);
        btntomarf = root.findViewById(R.id.btnnefoto);
        listaqr = root.findViewById(R.id.rvnelisqr);
        btnqr = root.findViewById(R.id.btnneobtqr);
        mViewModel = new ViewModelProvider(requireActivity()).get(NvaPreparacionViewModel.class);
        lcViewModel = new ViewModelProvider(this).get(ListaDetalleViewModel.class);
        milog=ComprasLog.getSingleton();
        milog.grabarError(TAG+" se muere aqui");
        sv1.setVisibility(View.GONE);
        sv2.setVisibility(View.GONE);
        sv3.setVisibility(View.GONE);
        sv4.setVisibility(View.GONE);
        sv5.setVisibility(View.GONE);
        svotra.setVisibility(View.GONE);
        svcoin.setVisibility(View.GONE);
        fotomos=root.findViewById(R.id.ivnefotomue);
        txtrutaim=root.findViewById(R.id.txtneruta);
        txtnumuestra=root.findViewById(R.id.txtnemuestra);
        pcoincide=root.findViewById(R.id.sinonecoincide);
        potra=root.findViewById(R.id.sinoneotramu);
        spplanta=root.findViewById(R.id.spneplanta);
        txtcomentarios=root.findViewById(R.id.txtnecomentarios);
        txtnumcajas=root.findViewById(R.id.txtnetotalcajas);
        contmuestra=1;
        contmuint=1;
        contcaja=1;

        txtcajaact=root.findViewById(R.id.txtnenumcaja);

        txtqr=root.findViewById(R.id.txtneqr);
        potra.setmLabel("¿INCLUIRAS OTRA MUESTRA EN ESTA CAJA?");

        txtcajaact.setVisibility(View.GONE);
        aceptar1.setEnabled(false);
        aceptar2.setEnabled(false);
        aceptar3.setEnabled(false);
        aceptar4.setEnabled(false);
        aceptar5.setEnabled(false);
        aceptar6.setEnabled(false);
        txtqr.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        txtcomentarios.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        txtqr.addTextChangedListener(new BotonTextWatcher(aceptar4));

        txtnumcajas.addTextChangedListener(new BotonTextWatcher(aceptar2));

        spplanta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                aceptar1.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        potra.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                aceptar5.setEnabled(true);
            }
        });
        pcoincide.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                aceptar6.setEnabled(true);
            }
        });
        btnqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarLecQR();
            }

        });
        aceptar1.setEnabled(false);
        mViewModel.preguntaAct=preguntaAct;
        if(!isEdicion&&preguntaAct<3&&mViewModel.getIdNuevo()==0) {
            //reviso si ya tengo uno abierto
            InformeEtapa informeEtapa = mViewModel.getInformePend(Constantes.INDICEACTUAL,3);

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
        }
        milog.grabarError(TAG+" o x aca");
        //buscar la solicitud
        Bundle datosRecuperados = getArguments();

        if(datosRecuperados!=null) {
            informeSel = datosRecuperados.getInt(NuevoInfEtapaActivity.INFORMESEL);

        }
        //busco si tengo varias plantas
        listacomp= lcViewModel.cargarPestañasSimp(Constantes.CIUDADTRABAJO);
        Log.d(TAG,"PLANTA"+listacomp.toString()+"ss"+mViewModel.getIdNuevo());
        preguntaAct=1;
        if(mViewModel.getIdNuevo()==0) {
            //reviso si ya tengo uno abierto
            if(!isEdicion&&preguntaAct<2) {
                InformeEtapa informeEtapa = mViewModel.getInformePend(Constantes.INDICEACTUAL, Constantes.ETAPAACTUAL);
                Log.d(TAG, "buscando pend");
                if (informeEtapa != null) {
                    Log.d(TAG, "encontré 1");
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

            }
            if (listacomp.size() > 1) {
                //tengo varias plantas
                preguntaAct = 1;

                convertirLista(listacomp);
                cargarPantas(listaPlantas, "");
                mViewModel.variasPlantas = true;
                sv1.setVisibility(View.VISIBLE);
                aceptar1.setEnabled(true);
            } else if (listacomp.size() > 0) {
                preguntaAct = 2;

                sv2.setVisibility(View.VISIBLE);
                mViewModel.variasPlantas = false;
                nombrePlantaSel = listacomp.get(0).getPlantaNombre();
                plantaSel = listacomp.get(0).getPlantasId();
                clienteId = listacomp.get(0).getClientesId();
                clienteNombre = listacomp.get(0).getClienteNombre();
                InformeEtapa informetemp = new InformeEtapa();
                informetemp.setClienteNombre(clienteNombre);
                informetemp.setClientesId(clienteId);
                informetemp.setPlantasId(plantaSel);
                informetemp.setPlantaNombre(nombrePlantaSel);
                informetemp.setIndice(Constantes.INDICEACTUAL);
                ((NuevoInfEtapaActivity) getActivity()).actualizarBarra(informetemp);
            }
        }
        if(isEdicion) { //busco el informe

            //busco el informe y el detalle

            mViewModel.getInformeEdit(informeSel).observe(getViewLifecycleOwner(), new Observer<InformeEtapa>() {
                @Override
                public void onChanged(InformeEtapa informeEtapa) {
                    infomeEdit = informeEtapa;
                    preguntaAct=3;
                    mViewModel.preguntaAct=3;
                    cargarPantas(listaPlantas,informeEtapa.getPlantasId()+"");
                    ((NuevoInfEtapaActivity) getActivity()).actualizarBarra(informeEtapa);
                    mViewModel.setIdNuevo(informeSel);
                    mostrarCapMuestra();

                }



            });

        }

        aceptar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avanzar();

            }
        });
        aceptar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarInf();

            }
        });
        aceptar3.setOnClickListener(new View.OnClickListener() { //foto
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
        aceptar6.setOnClickListener(new View.OnClickListener() { //coincide
            @Override
            public void onClick(View view) {
                avanzar();

            }
        });
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aceptar1.setEnabled(false);
                long currentClickTime= SystemClock.elapsedRealtime();
                // preventing double, using threshold of 1000 ms
                if (currentClickTime - lastClickTime < 5500){
                    //  Log.d(TAG,"doble click :("+lastClickTime);
                    return;
                }

                lastClickTime = currentClickTime;

                actualizarComent();
                finalizarInf();
                Toast.makeText(getActivity(), getString(R.string.informe_finalizado), Toast.LENGTH_SHORT).show();
                yaestoyProcesando = false;
                salir();


            }
        });
        btnrotar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotar(R.id.txtneruta);
            }
        });
        btntomarf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarFoto(REQUEST_CODE_TAKE_PHOTO);
            }
        });
        txtcajaact.setText("CAJA "+contcaja);
        txtnumuestra.setText("MUESTRA "+contmuestra);
        Log.d(TAG,"ando aqi");
        mViewModel.preguntaAct=preguntaAct;
        return root;
    }



    public void avanzar(){
        Log.d(TAG,"--"+preguntaAct);

        switch (preguntaAct){
            case 1: //sel planta
                sv1.setVisibility(View.GONE);
                sv2.setVisibility(View.VISIBLE);
                preguntaAct=preguntaAct+1;
                break;
            case 2: //num cajas
                 sv2.setVisibility(View.GONE);
                 sv3.setVisibility(View.VISIBLE);
                 txtcajaact.setVisibility(View.VISIBLE);
                isEdicion=false;
                preguntaAct=preguntaAct+1;
                  break;
            case 3: //foto
                sv3.setVisibility(View.GONE);
                sv4.setVisibility(View.VISIBLE);
                preguntaAct=preguntaAct+1;
                break;
            case 4://qr
                sv3.setVisibility(View.GONE);
                sv4.setVisibility(View.GONE);
                svotra.setVisibility(View.VISIBLE);
               // txtcajaact.setVisibility(View.GONE);

                guardarDet();
                isEdicion=false;
                preguntaAct=preguntaAct+1;
                break;
            case 5: //otra
                //depende de la respuesta

                capturarMuestra();

                break;
            case 6: //coincide

                if(pcoincide.getRespuesta()){
                    svcoin.setVisibility(View.GONE);
                    listaqr.setVisibility(View.GONE);

                    Log.d(TAG,"contcaja "+contcaja+"--"+totcajas);
                    if(contcaja<totcajas) {  //voy con la sig caja
                        contcaja++;
                        txtcajaact.setText("CAJA "+contcaja);
                        contmuint=1;
                            sv3.setVisibility(View.VISIBLE);
                            txtcajaact.setVisibility(View.VISIBLE);
                            preguntaAct = 3;
                            mViewModel.preguntaAct=preguntaAct;
                    }
                    else{
                        preguntaAct=preguntaAct+1;
                        //me voy a comentarios
                        sv5.setVisibility(View.VISIBLE);
                    }
                }else{
                    //muestro la lista de qr capturados y los busco
                    buscarQrCap(contcaja);

                   // mostrarCapMuestra();
                    //borro las muestras
                   // mViewModel.borrarDetEtaxCaja(mViewModel.getIdNuevo(),3,contcaja);

                }
                break;

        }

        mViewModel.preguntaAct=preguntaAct;
    }
    public void capturarMuestra(){
        svotra.setVisibility(View.GONE);
        txtcajaact.setText("CAJA "+contcaja);
        if(potra.getRespuesta()) {
            sv3.setVisibility(View.VISIBLE);
            txtcajaact.setVisibility(View.VISIBLE);
            preguntaAct = 3;
            mViewModel.preguntaAct=preguntaAct;
            //sv4.setVisibility(View.VISIBLE);
        }
        else{
            pcoincide.setmLabel("CANTIDAD DE MUESTRAS EN ESTA CAJA: "+(contmuint-1)+ " ¿COINCIDE CON LO QUE TIENES EN CAJA?");
            svcoin.setVisibility(View.VISIBLE);
            preguntaAct=preguntaAct+1;
        }

    }
    public void buscarQrCap(int numCaja){
        listaqr.setAdapter(null);
        List<InformeCompraDetalle> prods=mViewModel.buscarProdsxQr(mViewModel.getIdNuevo(),3,numCaja);

        DescripcionGenericaAdapter mListAdapter = new DescripcionGenericaAdapter();

        listaqr.setLayoutManager(new LinearLayoutManager(getActivity()));
        listaqr.setHasFixedSize(true);
        mListAdapter.setmDescripcionGenericaList(deCompraADesc(prods));
        listaqr.setAdapter(mListAdapter);
        listaqr.setVisibility(View.VISIBLE);

    }

    public List<DescripcionGenerica> deCompraADesc(List<InformeCompraDetalle> prods){
        DescripcionGenerica desc;
        List<DescripcionGenerica> prodsdes=new ArrayList<>();
        for(InformeCompraDetalle det:prods){
            if(det!=null) {
                desc = new DescripcionGenerica();
                desc.setId(det.getId());
                desc.setNombre(det.getProducto() + " " + det.getPresentacion() + " " + det.getEmpaque());
                desc.setDescripcion(det.getQr());
                prodsdes.add(desc);
            }

        }
        return prodsdes;
    }
    public void mostrarCapMuestra(){
        sv1.setVisibility(View.GONE);
        sv2.setVisibility(View.GONE);
        sv3.setVisibility(View.VISIBLE);
        txtcajaact.setVisibility(View.VISIBLE);
        preguntaAct = 3;
        mViewModel.preguntaAct=preguntaAct;
        if(isEdicion&&detalleEdit!=null){
            //busco en la bd para regresar a la primer muestra
            txtrutaim.setText(detalleEdit.getRuta_foto());
            Bitmap bitmap1 = ComprasUtils.decodeSampledBitmapFromResource(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + detalleEdit.getRuta_foto(),80,80);
            fotomos.setImageBitmap(bitmap1);
            fotomos.setVisibility(View.VISIBLE);
            btnrotar.setVisibility(View.VISIBLE);
            aceptar3.setEnabled(true);
            txtqr.setText(detalleEdit.getQr());
            contmuestra = detalleEdit.getNum_muestra();
            contmuint=1;
            contcaja=detalleEdit.getNum_caja();
            txtcajaact.setText("CAJA "+contcaja);
            totcajas=infomeEdit.getTotal_cajas();
        }
       else{
            //busco en la bd para regresar a la primer muestra
            List<InformeEtapaDet> informeEtapaDet= mViewModel.getDetEtaxCaja(mViewModel.getIdNuevo(),3,contcaja);
            Log.e(TAG,"total"+informeEtapaDet.size());
            if(informeEtapaDet!=null&&informeEtapaDet.size()>0) {
                //   detalleEdit = informeEtapaDet.get(0);
                // txtrutaim.setText(detalleEdit.getRuta_foto());
                //  Bitmap bitmap1 = ComprasUtils.decodeSampledBitmapFromResource(detalleEdit.getRuta_foto(),80,80);
                //  fotomos.setImageBitmap(bitmap1);
                // txtqr.setText(detalleEdit.getQr());
                contmuestra = informeEtapaDet.get(0).getNum_muestra();
                contmuint=contmuestra;
            }

        }
        Log.e(TAG,"--"+contmuestra);
        //actualizo vista
        txtnumuestra.setText("MUESTRA "+contmuestra);
    }

    public void atras(){
        switch (preguntaAct){

            case 2:
                if(mViewModel.variasPlantas)
                { sv1.setVisibility(View.VISIBLE);}

                sv2.setVisibility(View.GONE);
                preguntaAct=preguntaAct-1;
                mViewModel.preguntaAct=preguntaAct;
                break;
            case 3:
                break;
                //  sv2.setVisibility(View.VISIBLE);
              //  sv3.setVisibility(View.GONE);
             //   txtcajaact.setVisibility(View.GONE);

            case 4:
                sv3.setVisibility(View.VISIBLE);
                sv4.setVisibility(View.GONE);
                preguntaAct=preguntaAct-1;
                mViewModel.preguntaAct=preguntaAct;
                break;
            case 5:
               break;
             //   sv4.setVisibility(View.VISIBLE);
             //   svotra.setVisibility(View.GONE);
             //   break;
            case 6:
                svotra.setVisibility(View.VISIBLE);
                svcoin.setVisibility(View.GONE);
                preguntaAct=preguntaAct-1;
                mViewModel.preguntaAct=preguntaAct;
                break;
            case 7:
                svcoin.setVisibility(View.VISIBLE);
                sv5.setVisibility(View.GONE);
                preguntaAct=preguntaAct-1;
                mViewModel.preguntaAct=preguntaAct;
                break;

        }
        Log.d(TAG,"**"+preguntaAct);

    }

    public void mostrarlayout(){
        switch (preguntaAct){
            case 1:
                sv1.setVisibility(View.GONE);
                sv2.setVisibility(View.VISIBLE);
                break;
            case 2:
                sv2.setVisibility(View.GONE);
                sv3.setVisibility(View.VISIBLE);
                break;
            case 3:
                sv3.setVisibility(View.GONE);
                sv4.setVisibility(View.VISIBLE);
                break;
            case 4:
                sv4.setVisibility(View.GONE);
                sv5.setVisibility(View.VISIBLE);
                break;
            case 5:

                break;
        }
    }
    public void guardarInf(){
        try {
            lastClickTime = 0;
            totcajas = 0;
            if( mViewModel.variasPlantas) {
                DescripcionGenerica opcionsel = (DescripcionGenerica) spplanta.getSelectedItem();

                //busco par de id, cliente
                String[] aux = opcionsel.getDescripcion().split(",");
                clienteId = Integer.parseInt(aux[0]);
                clienteNombre = aux[1];
                plantaSel = opcionsel.getId();
                nombrePlantaSel = opcionsel.getNombre();
            }
            totcajas =Integer.parseInt(txtnumcajas.getText().toString());

            if (preguntaAct == 2 && !isEdicion&&mViewModel.getNvoinforme()==null) {
                Log.d(TAG, "creando nvo inf");
                //creo el informe
                 mViewModel.setIdNuevo(mViewModel.insertarEtiq(Constantes.INDICEACTUAL,nombrePlantaSel,plantaSel,clienteNombre,clienteId,totcajas,totmuestras));
            }
        }catch (Exception ex){
            ex.getStackTrace();
            Log.e(TAG,"Algo salió mal al enviar"+ex.getMessage());
            Toast.makeText(getContext(),"Hubo un error al guardar intente de nuevo",Toast.LENGTH_SHORT).show();

        }
        ((NuevoInfEtapaActivity)getActivity()).actualizarBarra(mViewModel.getNvoinforme());

        aceptar1.setEnabled(true);
            avanzar();
        }
        public void guardarDet(){
            try{
                String rutafoto = null;
                String qr = null;


                rutafoto=txtrutaim.getText().toString();
                qr=txtqr.getText().toString();
                if(isEdicion){
                    mViewModel.insertarEtiqDet(mViewModel.getIdNuevo(),11,"foto_etiqueta",rutafoto,detalleEdit.getId(),contcaja,qr,contmuestra);
                    isEdicion=false;
                }else
                if(mViewModel.getIdNuevo()>0)
                    //guardo el detalle
                     mViewModel.insertarEtiqDet(mViewModel.getIdNuevo(),11,"foto_etiqueta",rutafoto,0,contcaja,qr,contmuestra);
               //limpio campos
                txtrutaim.setText("");
                fotomos.setImageBitmap(null);
                fotomos.setVisibility(View.GONE);
                btnrotar.setVisibility(View.GONE);
                aceptar3.setEnabled(false);
                aceptar4.setEnabled(false);
                txtqr.setText("");

                //muevo contadores
                contmuestra++;
                contmuint++;
                //actualizo vista
                txtnumuestra.setText("MUESTRA "+contmuestra);
            }catch (Exception ex){
                ex.printStackTrace();
                Log.e(TAG,"Algo salió mal al enviar"+ex.getMessage());
                Toast.makeText(getContext(),"Hubo un error al guardar intente de nuevo",Toast.LENGTH_SHORT).show();

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
            ex.printStackTrace();
            Log.e(TAG,"Algo salió mal al enviar"+ex.getMessage());
            Toast.makeText(getContext(),"Algo salio mal al enviar",Toast.LENGTH_SHORT).show();
        }
        //todo limpio variables de sesion

        mViewModel.setIdNuevo(0);
        mViewModel.setIddetalle(0);
        mViewModel.setNvoinforme(null);

    }
    public void actualizarComent(){

        String comentarios = null;

        if(txtcomentarios.getText()!=null)
            comentarios=txtcomentarios.getText().toString().toUpperCase();

        try{
            mViewModel.actualizarComentEtiq(mViewModel.getIdNuevo(),comentarios);
            mViewModel.actualizarMuestras(mViewModel.getIdNuevo(), contmuestra-1);
        }catch(Exception ex){
            ex.getStackTrace();
            Log.e(TAG,"Algo salió mal al enviar"+ex.getMessage());
            Toast.makeText(getContext(),"Algo salio mal al enviar",Toast.LENGTH_SHORT).show();
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
                    mostrarFoto(txtrutaim,fotomos,btnrotar);
                }

                aceptar3.setEnabled(true);

            }
            else{
                Log.e(TAG,"Algo salió mal???");
            }


        }else if(requestCode == REQUEST_CODEQR) {


            //  IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);
            IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);

            Log.d(TAG,"res del qr "+result.getContents());
            if(result != null) {

                if(result.getContents() == null) {
                    Toast.makeText(getActivity(), "Scan cancelled", Toast.LENGTH_LONG).show();
                }
                else
                {   /* Update the textview with the scanned URL result */
                    txtqr.setText(result.getContents());
                    aceptar4.setEnabled(true);
                  //  Toast.makeText(getActivity(), "Content: ${result.getContents()}",Toast.LENGTH_LONG ).show();
                }

            }
            else
            {
                super.onActivityResult(requestCode, resultCode, data);
                Toast.makeText(getActivity(), "hubo un error",Toast.LENGTH_LONG ).show();

            }
        }    else
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


    public InformeEtapaEnv preparaInforme(){
        InformeEtapaEnv envio=new InformeEtapaEnv();

        envio.setInformeEtapa(mViewModel.getInformexId(mViewModel.getIdNuevo()));

        envio.setClaveUsuario(Constantes.CLAVEUSUARIO);
        envio.setIndice(Constantes.INDICEACTUAL);
        envio.setInformeEtapaDet(mViewModel.cargarInformeDet(mViewModel.getIdNuevo()));
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
    private  void convertirLista(List<ListaCompra>lista){
        listaPlantas=new ArrayList<DescripcionGenerica>();
        for (ListaCompra listaCompra: lista ) {

            listaPlantas.add(new DescripcionGenerica(listaCompra.getPlantasId(), listaCompra.getClienteNombre()+" "+listaCompra.getPlantaNombre(),listaCompra.getClientesId()+","+listaCompra.getClienteNombre()));

        }

    }
    public void iniciarLecQR(){
        IntentIntegrator integrator  =new  IntentIntegrator ( getActivity() ).forSupportFragment(NvoEtiquetadoFragment.this);
        integrator.setRequestCode(REQUEST_CODEQR);
        //  integrator.setOrientationLocked(false);
        Log.d(TAG, "inciando scanner");
        integrator.initiateScan();
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
    private void cargarPantas(List<DescripcionGenerica> selectdes,String value){
        ArrayAdapter catAdapter = new ArrayAdapter<DescripcionGenerica>(getContext(), android.R.layout.simple_spinner_dropdown_item, selectdes) {


            // And the "magic" goes here
            // This is for the "passive" state of the spinner
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
                TextView label = (TextView) super.getView(position, convertView, parent);
                label.setTextColor(Color.BLACK);
                // Then you can get the current item using the values array (Users array) and the current position
                // You can NOW reference each method you has created in your bean object (User class)
                DescripcionGenerica item = getItem(position);
                label.setText(item.getNombre());
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
                DescripcionGenerica item = getItem(position);
                label.setText(item.getNombre());

                return label;
            }
        };


        spplanta.setAdapter(catAdapter);
        spplanta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the value selected by the user
                // e.g. to store it as a field or immediately call a method
                DescripcionGenerica opcion = (DescripcionGenerica) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if(value!=null&&value.length()>0){
            //busco el valor en la lista
            for(DescripcionGenerica cat:selectdes){
                Log.d("CreadorForm","val"+value+" cat"+cat.getId());
                if(value.equals(cat.getId()+"")){
                    // Log.d("CreadorForm","val"+infocampo.value+" cat"+cat.getId());
                    spplanta.setSelection(catAdapter.getPosition(cat),true);
                    break;
                }
                if(value.equals(cat.getNombre())){
                    Log.d("CreadorForm",catAdapter.getPosition(cat)+"");

                    spplanta.setSelection(catAdapter.getPosition(cat),true);
                    break;
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel = null;

        root=null;
        txtrutaim=null;

        fotomos=null;
        sv1=sv2=sv3=sv4=null;
        btnrotar=null;
        aceptar1=null;
        nombre_foto=null;
        archivofoto=null;
    }




}

