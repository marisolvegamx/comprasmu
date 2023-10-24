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
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;

import com.example.comprasmu.SubirInformeEtaTask;

import com.example.comprasmu.data.modelos.DescripcionGenerica;

import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.modelos.ListaCompra;

import com.example.comprasmu.data.remote.InformeEtapaEnv;
import com.example.comprasmu.services.SubirFotoService;
import com.example.comprasmu.ui.RevisarFotoActivity;
import com.example.comprasmu.ui.infetapa.NuevoInfEtapaActivity;

import com.example.comprasmu.ui.infetapa.NuevoInfEtapaViewModel;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.ui.preparacion.NvaPreparacionViewModel;

import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;

import com.example.comprasmu.utils.Preguntasino;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import com.example.comprasmu.utils.micamara.MiCamaraActivity;
import static android.app.Activity.RESULT_OK;


//las cajas se numeran x ciudad cliente
//ciudad x cajas 1,2,3 no importa el cliente
//ciudad y 1,2,3 no importa el cliente
public class NvoEtiquetadoFragment extends Fragment {

    private InformeEtapaDet detalleEdit;
    LinearLayout sv1, sv6, sv3, sv4, sv5, svotra, svpregcaja;
    private static final String TAG = "NvoEtiquetadoFragment";
    Button aceptar1, aceptar2, aceptar3, aceptar4, aceptar5, aceptar6, nvacaja, btnneacfotocaj;
    //eliminarCaja
    Button selplanta, guardar;
    private long lastClickTime = 0;
    private boolean yaestoyProcesando = false;
    EditText txtrutaim, txtcomentarios, txtqr, txtrutacaja, txtcajaact, txtdescidfoto;
    TextView txtnumuestra, txttotmues, txtdescfotocaj, txtcajafoto;
    ImageView fotomos, fotomoscaj;
    private ImageButton btnrotar, btntomarf, btnqr, btnrotar2, btntomarcaja;
    public int REQUEST_CODE_TAKE_PHOTO = 1;
    NuevoInfEtapaViewModel niviewModel;
    String rutafotoo;
    Preguntasino potra, pcoincide;
    private View root;
    private NvaPreparacionViewModel mViewModel;
    private int informeSel;
    private int clienteSel;

    private String clienteNombreSel;

    private boolean isCont;
    private InformeEtapa infomeEdit;
    int preguntaAct;
    private ListaDetalleViewModel lcViewModel;
    List<ListaCompra> listacomp;
    private static final int REQUEST_CODEQR = 345;
    int cajaini; //se usa para las cajas de la muestra
    int contmuestra, contmuint, contcajaf;
    int totmuestras, totcajas; //cajaact
    private ArrayList<DescripcionGenerica> listaClientes;
    private boolean isEdicion;
    Spinner spcliente, spcaja;
    RecyclerView listaqr;
    ComprasLog milog;
    List<String> spinnerValues;
    ArrayAdapter<String> adaptercaja;
    private TextView txttotcaj;
    public final static String ARG_PREGACT = "comprasmu.ne_pregactp";
    public final static String ARG_ESEDI = "comprasmu.ne_esedip";
    public final static String ARG_INFORMEDET = "comprasmu.ne_infdet";
    public final static String ARG_INFORMESEL = "comprasmu.neinfsel";
    public String ciudadInf;
    public String[] descripfoto = {"FOTO CALIDAD DE CAJA CARA A", "FOTO CALIDAD DE CAJA CARA B", "FOTO ACOMODO DE MUESTRAS DENTRO DE CAJA"};
    public int[] descripcionid = {12, 13, 14};
    private int cajainif;
    private Button btnreubicar;

    public NvoEtiquetadoFragment() {

    }

    public static NvoEtiquetadoFragment newInstance() {
        return new NvoEtiquetadoFragment();
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_nvoetiq, container, false);
        sv1 = root.findViewById(R.id.llnepreg1);
        sv6 = root.findViewById(R.id.llnepre6);
        sv3 = root.findViewById(R.id.llnepreg3);
        sv4 = root.findViewById(R.id.llrepre4);
        sv5 = root.findViewById(R.id.llnepre5);
        svpregcaja = root.findViewById(R.id.llnepregcaja);
        txttotcaj = root.findViewById(R.id.txtnetotcaj);
        txtcajafoto = root.findViewById(R.id.txtnecajafoto);
        svotra = root.findViewById(R.id.llnebotones);
        //  svcoin = root.findViewById(R.id.llnecoincide);
        aceptar1 = root.findViewById(R.id.btnneac1);
        //  aceptar2 = root.findViewById(R.id.btnneac2);
        aceptar3 = root.findViewById(R.id.btnneac3);
        aceptar4 = root.findViewById(R.id.btnneac4);
        aceptar5 = root.findViewById(R.id.btnneac5);
        aceptar6 = root.findViewById(R.id.btnneacep6);
        btnreubicar= root.findViewById(R.id.btnnereubicar);
        btnneacfotocaj = root.findViewById(R.id.btnneacfotocaj);
        nvacaja = root.findViewById(R.id.btnnecajamas);
        // eliminarCaja = root.findViewById(R.id.btnneelimcaj);
        //    selplanta = root.findViewById(R.id.btnneselplanta);
        guardar = root.findViewById(R.id.btnneguardar);
        btnrotar = root.findViewById(R.id.btnnerotar1);
        btnrotar2 = root.findViewById(R.id.btnnerotar2);
        btntomarf = root.findViewById(R.id.btnnefoto);
        btntomarcaja = root.findViewById(R.id.btnnefotocaj);
        // listaqr = root.findViewById(R.id.rvnelisqr);
        btnqr = root.findViewById(R.id.btnneobtqr);
        niviewModel = new ViewModelProvider(requireActivity()).get(NuevoInfEtapaViewModel.class);

        mViewModel = new ViewModelProvider(requireActivity()).get(NvaPreparacionViewModel.class);
        lcViewModel = new ViewModelProvider(this).get(ListaDetalleViewModel.class);
        milog = ComprasLog.getSingleton();
        contcajaf=1;
        sv1.setVisibility(View.GONE);
        sv6.setVisibility(View.GONE);
        sv3.setVisibility(View.GONE);
        sv4.setVisibility(View.GONE);
        sv5.setVisibility(View.GONE);
        svotra.setVisibility(View.GONE);
        svpregcaja.setVisibility(View.GONE);
        fotomos = root.findViewById(R.id.ivnefotomue);
        fotomoscaj = root.findViewById(R.id.ivnefotocaj);
        txtrutaim = root.findViewById(R.id.txtneruta);
        txtnumuestra = root.findViewById(R.id.txtnemuestra);
        txtdescidfoto = root.findViewById(R.id.txtnedescfotoid);
        txtdescfotocaj = root.findViewById(R.id.txtnedescfoto);
        // pcoincide=root.findViewById(R.id.sinonecoincide);
        // potra=root.findViewById(R.id.sinoneotramu);
        spcliente = root.findViewById(R.id.spneplanta);
        spcaja = root.findViewById(R.id.spnecaja);
        txtcomentarios = root.findViewById(R.id.txtnecomentarios);
        txtcajaact = root.findViewById(R.id.txtnecajaact);
        txtrutacaja = root.findViewById(R.id.txtnefotocaj);
        if (getArguments() != null) {
            // Log.d(TAG,"aqui");
            this.preguntaAct = getArguments().getInt(ARG_PREGACT);
            this.informeSel = getArguments().getInt(ARG_INFORMESEL);
            // mViewModel.setIdNuevo(this.informeSel);
            //BUSCAR DETALLE EDIT SI ES DIFERENTE DE NULL
            int detid = getArguments().getInt(ARG_INFORMEDET);
            InformeEtapaDet det = mViewModel.getDetalleEta(detid);
            if (det != null)
                this.detalleEdit = det;


            this.isEdicion = getArguments().getBoolean(ARG_ESEDI);
        }
        contmuestra = 1;
        contmuint = 1;
        cajaini = 1;
        totcajas=0;
        //  txtcajaact=root.findViewById(R.id.txtnenumcaja);

        txtqr = root.findViewById(R.id.txtneqr);
        // potra.setmLabel("¿INCLUIRAS OTRA MUESTRA EN ESTA CAJA?");
        // txttotmues.setText("TOTAL MUESTRAS: 2");
        //  txttotmues.setVisibility(View.GONE);
        // txtcajaact.setVisibility(View.GONE);
        //deshabilito botones de aceptar
        aceptar1.setEnabled(false);
        // aceptar2.setEnabled(false);
        aceptar3.setEnabled(false);
        aceptar4.setEnabled(false);
        aceptar5.setEnabled(false);
        btnneacfotocaj.setEnabled(false);
        txtqr.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        txtcomentarios.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        txtqr.addTextChangedListener(new BotonTextWatcher(aceptar4));

        //   txtnumcajas.addTextChangedListener(new BotonTextWatcher(aceptar2));

        adaptercaja = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerValues);

        spcliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                aceptar1.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    /*    potra.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                aceptar5.setEnabled(true);
            }
        });*/
      /*  pcoincide.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                aceptar6.setEnabled(true);
            }
        });*/
        btnqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarLecQR();
            }

        });
        btnreubicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iraReubicar();
            }
        });
        aceptar1.setEnabled(false);
        if (preguntaAct == 0)
            preguntaAct = 1;
        mViewModel.preguntaAct = preguntaAct;
        if (!isEdicion && preguntaAct < 2 && mViewModel.getIdNuevo() == 0) {
            //es nuevo
            //reviso si ya tengo uno abierto
            InformeEtapa informeEtapa = mViewModel.getInformePend(Constantes.INDICEACTUAL, 3);

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
        milog.grabarError(TAG + " o x aca");
        //busco si tengo varias plantas
        ciudadInf = Constantes.CIUDADTRABAJO;
        listacomp = lcViewModel.cargarClientesSimplxet(Constantes.CIUDADTRABAJO);
        Log.d(TAG, "PLANTA" + ciudadInf + "ss" + mViewModel.getIdNuevo() + "--" + listacomp.size());

        //veo si ya tengo informes
        Integer[] clientesprev = mViewModel.tieneInforme(3);

        if (mViewModel.getIdNuevo() == 0) {
            //reviso si ya tengo uno abierto
            if (!isEdicion && preguntaAct < 1) {
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
                //tengo varios clientes
                preguntaAct = 1;

                convertirLista(listacomp, clientesprev);
                cargarPlantas(listaClientes, "");

                mViewModel.variasClientes = true;
                sv1.setVisibility(View.VISIBLE);
                aceptar1.setEnabled(true);
            } else if (listacomp.size() > 0) {
                preguntaAct = 2;

                sv3.setVisibility(View.VISIBLE);
                mViewModel.variasClientes = false;
                clienteSel = listacomp.get(0).getClientesId();
                clienteNombreSel = listacomp.get(0).getClienteNombre();
                totmuestras = mViewModel.getTotalMuestrasxCliXcd(clienteSel, Constantes.CIUDADTRABAJO);
                InformeEtapa informetemp = new InformeEtapa();
                informetemp.setClienteNombre( clienteNombreSel);
                informetemp.setClientesId(clienteSel);
                informetemp.setCiudadNombre(Constantes.CIUDADTRABAJO);
                informetemp.setIndice(Constantes.INDICEACTUAL);
                ((NuevoInfEtapaActivity) getActivity()).actualizarBarraEtiq(informetemp);
            }
        }
        if (isEdicion) { //busco el informe

            //busco el informe y el detalle

            infomeEdit = mViewModel.getInformexId(informeSel);
            preguntaAct = 3;
            mViewModel.preguntaAct = 3;
            if (listaClientes != null && listaClientes.size() > 0)
                cargarPlantas(listaClientes, infomeEdit.getClientesId() + "");
            ((NuevoInfEtapaActivity) getActivity()).actualizarBarraEtiq(infomeEdit);
            mViewModel.setIdNuevo(informeSel);
            totmuestras = infomeEdit.getTotal_muestras();
            clienteSel = infomeEdit.getClientesId();
            //  totcajas=mViewModel.getu
            //veo si es de muestra o de cja
            if (detalleEdit != null && detalleEdit.getDescripcionId() > 11) {
                editarCapCaja();
            } else
                mostrarCapMuestra();


        }
        if (preguntaAct > 1) //ya tengo planta y cliente
        {
            cargarListaCajas();
        }
        if(preguntaAct>11)//busco el total de cajas
        {
         //   totcajas =mViewModel.getTotCajasEtiqxCd(Constantes.CIUDADTRABAJO);
        }
        if (detalleEdit != null) {

            spcaja.setSelection(adaptercaja.getPosition(detalleEdit.getNum_caja() + ""));

        }
        aceptar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DescripcionGenerica opcionsel = (DescripcionGenerica) spcliente.getSelectedItem();
                if (opcionsel != null) {
                    //busco par de id, cliente
                    //String[] aux = opcionsel.getDescripcion().split(",");
                    clienteSel = opcionsel.getId();
                    clienteNombreSel = opcionsel.getNombre();
                    //busco el total de muestras
                    totmuestras = mViewModel.getTotalMuestrasxCliXcd(clienteSel, Constantes.CIUDADTRABAJO);
                    InformeEtapa temp = new InformeEtapa();
                    temp.setClienteNombre(clienteNombreSel);
                    temp.setClientesId(clienteSel);
                    temp.setCiudadNombre(Constantes.CIUDADTRABAJO);
                    temp.setIndice(Constantes.INDICEACTUAL);
                    temp.setTotal_muestras(totmuestras);
                    ((NuevoInfEtapaActivity) getActivity()).actualizarBarraEtiq(temp);
                    avanzar();
                }
            }
        });
     /*  aceptar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });*/
        aceptar3.setOnClickListener(new View.OnClickListener() { //foto
            @Override
            public void onClick(View view) {
                guardarInf();

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
        nvacaja.setOnClickListener(new View.OnClickListener() { //coincide
            @Override
            public void onClick(View view) {
                nvacaja();

            }
        });
       /* eliminarCaja.setOnClickListener(new View.OnClickListener() { //coincide
            @Override
            public void onClick(View view) {
                eliminarCaja();

            }
        });*/
      /*  selplanta.setOnClickListener(new View.OnClickListener() { //coincide
            @Override
            public void onClick(View view) {


                //iraReubicar();

            }
        });*/
        aceptar6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aceptar1.setEnabled(false);
             /*   long currentClickTime= SystemClock.elapsedRealtime();
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
                salir();*/
                avanzar();


            }
        });

        btnneacfotocaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnneacfotocaj.setEnabled(false);

                avanzar();


            }
        });
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aceptar1.setEnabled(false);
                long currentClickTime = SystemClock.elapsedRealtime();
                // preventing double, using threshold of 1000 ms
                if (currentClickTime - lastClickTime < 5500) {
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
        btnrotar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotar(R.id.txtnefotocaj);
            }
        });
        btntomarf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarFoto(1);
            }
        });
        btntomarcaja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarFoto(2);
            }
        });
        //  txtcajaact.setText("CAJA "+contcaja);
        txtnumuestra.setText("MUESTRA " + contmuestra);
        //    Log.d(TAG,"ando aqi");
        mViewModel.preguntaAct = preguntaAct;
        return root;
    }


public void iraReubicar(){
    Intent intento1 = new Intent(getActivity(), NavigationDrawerActivity.class);
    intento1.putExtra(NavigationDrawerActivity.NAVINICIAL,"nav_reubicetiq");
    startActivity(intento1);
    getActivity().finish();
}

    //el total de cajas si es x ciudad cliente
    public void cargarListaCajas() {
        int totxciu = mViewModel.getTotCajasEtiqxCli(ciudadInf,clienteSel);
        spinnerValues = new ArrayList<>();

        if (totxciu == 0) {

            cajaini =1;
            spinnerValues.add(1 + "");
        } else { //ya tengo cajas pero no es dicion busco de ese cliente
            List<InformeEtapaDet> listacajas = mViewModel.listaCajasEtiqxCdCli(ciudadInf,clienteSel);
            if (listacajas != null) {


                Log.d(TAG, clienteSel + "numcajas ll" + listacajas.size());
                if (listacajas.size() > 0) { //las numeracion que ya tengo
                    totcajas=listacajas.size();
                    cajaini =listacajas.get(0).getNum_caja() ;
                    for (InformeEtapaDet det : listacajas
                    ) {
                        spinnerValues.add(det.getNum_caja() + "");
                     //   cajaini = det.getNum_caja();
                    }
                } else { //cambié de cliente

                    cajaini = totxciu + 1; //para saber la sig caja
                    spinnerValues.add(cajaini + "");
                    totcajas=0;
                }
            } else { //cambié de cliente

                cajaini = totxciu + 1; //para saber la sig caja
                spinnerValues.add(cajaini + "");
                totcajas=0;
            }

        }

        Log.d(TAG, "caja ini " + cajaini);
      /*  for(int i=1;i<=contcaja;i++) {
            spinnerValues.add(i+"");
        }*/


        adaptercaja = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerValues);
        spcaja.setAdapter(adaptercaja);
    }

    public void nvacaja() {
        cajaini++;
        spinnerValues.add(cajaini + "");
        adaptercaja.notifyDataSetChanged();
        spcaja.performClick();
        //  preguntaAct=2;
        //  txtcajaact.setText("CAJA "+contcaja);
        //avanzar();
    }

    public void eliminarCaja() {
        Log.d(TAG, "cajas" + cajaini);
        if (cajaini > 1) {
            //reviso que no haya muestras, primero qu reubique
            List<InformeEtapaDet> det = mViewModel.getDetEtaxCaja(mViewModel.getIdNuevo(), 3, cajaini);
            if (det != null && det.size() > 0) {
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
                dialogo1.setTitle(R.string.atencion);
                dialogo1.setMessage(R.string.esta_caja);
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        //lo mando a continuar
                        dialogo1.cancel();
                        return;

                    }
                });

                dialogo1.show();

            } else {
                cajaini--;
                spinnerValues.remove(spinnerValues.size() - 1);
                adaptercaja.notifyDataSetChanged();
            }
        }
        //  preguntaAct=2;
        //  txtcajaact.setText("CAJA "+contcaja);
        //avanzar();
    }

    public void avanzar() {
        Log.d(TAG, "--" + preguntaAct);
        Log.d(TAG, "ananza"+ contcajaf+" <="+ totcajas);


        switch (preguntaAct) {
            case 1: //sel cliente


                // txtcajaact.setVisibility(View.VISIBLE);
                txtnumuestra.setVisibility(View.VISIBLE);
                sv1.setVisibility(View.GONE);
                // txttotmues.setVisibility(View.VISIBLE);
                sv3.setVisibility(View.VISIBLE);
                preguntaAct = preguntaAct + 1;
                cargarListaCajas();
                break;
            case 2: //foto
                sv3.setVisibility(View.GONE);

                sv4.setVisibility(View.VISIBLE);
                preguntaAct = preguntaAct + 1;
                break;
            case 3: //qr
                String qr=txtqr.getText().toString();
                if(validarQr(qr)) {
                    //sv3.setVisibility(View.GONE);
                    sv4.setVisibility(View.GONE);


                    preguntaAct = 4;
                    //pido caja
                    sv6.setVisibility(View.VISIBLE);
                }
              /*  }else {

                    guardarDet();
                    isEdicion = false;
                    if(contmuestra<=totmuestras) {

                        capturarMuestra();

                    }
                    else

                    {

                        preguntaAct=5;
                        //me voy a comentarios
                        sv5.setVisibility(View.VISIBLE);
                        break;
                    }
                }*/
                break;
            case 4: //numcaja

                sv6.setVisibility(View.GONE);
                guardarDet();
                Log.d(TAG, contmuestra + "--" + totmuestras);
                isEdicion = false;
                if (contmuestra <= totmuestras) {
                    preguntaAct = 2;
                    capturarMuestra();

                } else {
                    preguntaAct = 5;
                    //todo aqui validacion consecutivos
                    if(!validasSecuenciaCaj()){
                        svotra.setVisibility(View.VISIBLE);
                        return;
                    }
                    if(validarCajasVacias()){
                        Toast.makeText(getActivity(), "Las cajas vacías se eliminarán", Toast.LENGTH_SHORT).show();

                    }
                    //lleno el total de cajas
                    txttotcaj.setText("TOTAL CAJAS:" + totcajas);
                    //me voy a fotos de caja
                    svpregcaja.setVisibility(View.VISIBLE);
                    contcajaf = 1; //para el for de cuantas veces pedir fotos
                    //busco la caja en la que empiezo
                     cajainif = mViewModel.getMinCajaxCli(ciudadInf, clienteSel);


                    //busco total de cajas final
                    totcajas = mViewModel.getTotCajasEtiqxCli(ciudadInf,clienteSel);
                    capturarFotoCaja();
                    break;
                }

                break;

            case 5:
            case 6://foto caja
                guardarDetCaja();
                svpregcaja.setVisibility(View.VISIBLE);
                preguntaAct = preguntaAct + 1;
                capturarFotoCaja();
                break;
            case 7: //foto caja

                guardarDetCaja();
                svpregcaja.setVisibility(View.VISIBLE);


                //si es la ultima foto de la caja y hay más cajas avanzo
                if (contcajaf < totcajas) {
                    preguntaAct = 5;
                    contcajaf++;
                    cajainif++;

                    capturarFotoCaja();
                } else //no hay más cajas me voy a comentarios
                {
                    svpregcaja.setVisibility(View.GONE);
                    txttotcaj.setText("TOTAL CAJAS:" + totcajas);
                    sv5.setVisibility(View.VISIBLE);
                    preguntaAct = preguntaAct + 1;
                }
                break;

            case 8: //comentarios

                preguntaAct = preguntaAct + 1;

                //  pantallaPlantas();
                salir();
                break;
        }

        mViewModel.preguntaAct = preguntaAct;
    }

    public void capturarMuestra() {
        sv3.setVisibility(View.VISIBLE);
        preguntaAct = 2;
        mViewModel.preguntaAct = preguntaAct;

    }

    public void capturarFotoCaja() {
        svotra.setVisibility(View.GONE);
        // ver si ya existe esta foto
        Log.d(TAG,"capturarFoto"+(preguntaAct - 5));
        mostrarCapturaCajaxDesc(descripfoto[preguntaAct - 5]);
      //  mViewModel.preguntaAct = preguntaAct;
        //busco descripcion
        txtdescfotocaj.setText(descripfoto[preguntaAct - 5]);
        txtdescidfoto.setText(descripcionid[preguntaAct - 5] + "");
        txtcajafoto.setText("CAJA " + cajainif);//la caja actual
        txtcajaact.setText(cajainif + "");
        txtrutacaja.setText("");
        fotomoscaj.setImageBitmap(null);


    }
   /* public void buscarQrCap(int numCaja){
       // listaqr.setAdapter(null);
        List<InformeCompraDetalle> prods=mViewModel.buscarProdsxQr(mViewModel.getIdNuevo(),3,numCaja);

        DescripcionGenericaAdapter mListAdapter = new DescripcionGenericaAdapter();

        listaqr.setLayoutManager(new LinearLayoutManager(getActivity()));
        listaqr.setHasFixedSize(true);
        mListAdapter.setmDescripcionGenericaList(deCompraADesc(prods));
        listaqr.setAdapter(mListAdapter);
        listaqr.setVisibility(View.VISIBLE);

    }*/

    //devuelve verdadero si no existe el qr
    public boolean validarQr(String qr) {
        // listaqr.setAdapter(null);
        InformeEtapaDet prods = mViewModel.buscarDetxQr(qr);
        if(prods!=null&&detalleEdit!=null&&prods.getId()==detalleEdit.getId()&&isEdicion){
            //es el mismo
        }else
        if (prods != null) {
            Toast.makeText(getActivity(), getString(R.string.ya_existe_qr), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean validasSecuenciaCaj(){
        List<InformeEtapaDet> cajas=mViewModel.listaCajasEtiqxCdCli(ciudadInf,clienteSel);
        int i=1;
        for (InformeEtapaDet caja:cajas) {
            if(caja.getNum_caja()==i)
                i++;
            else
                return false;

        }
        return true;
    }
    //si hhay cajas vacias devuelve true
    public boolean validarCajasVacias(){

         int i=1;
        for (String caja:spinnerValues) {
            int numcaja=0;
            try {
                 numcaja = Integer.parseInt(caja);
            }catch(NumberFormatException ex){
                milog.grabarError(TAG+".validarCajasVacias "+ex.getMessage());
            }
            //busco si hay muestras
            List<InformeEtapaDet> cajas=mViewModel.getDetEtaxCaja(mViewModel.getIdNuevo(),3,numcaja);

            if(cajas!=null&&cajas.size()>0)
              return true;

        }
        return false;
    }
    public List<DescripcionGenerica> deCompraADesc(List<InformeCompraDetalle> prods) {
        DescripcionGenerica desc;
        List<DescripcionGenerica> prodsdes = new ArrayList<>();
        for (InformeCompraDetalle det : prods) {
            if (det != null) {
                desc = new DescripcionGenerica();
                desc.setId(det.getId());
                desc.setNombre(det.getProducto() + " " + det.getPresentacion() + " " + det.getEmpaque());
                desc.setDescripcion(det.getQr());
                prodsdes.add(desc);
            }

        }
        return prodsdes;
    }

    public void mostrarCapMuestra() {
        sv1.setVisibility(View.GONE);
        sv6.setVisibility(View.GONE);
        sv3.setVisibility(View.VISIBLE);
        //  txtcajaact.setVisibility(View.VISIBLE);
        preguntaAct = 2;
        ImagenDetalle foto;
        mViewModel.preguntaAct = preguntaAct;
        if (detalleEdit != null) {
            //busco en la bd para regresar a la primer muestra
            foto = mViewModel.getFoto(Integer.parseInt(detalleEdit.getRuta_foto()));
            txtrutaim.setText(foto.getRuta());
            Bitmap bitmap1 = ComprasUtils.decodeSampledBitmapFromResource(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + foto.getRuta(), 80, 80);
            fotomos.setImageBitmap(bitmap1);
            fotomos.setVisibility(View.VISIBLE);
            btnrotar.setVisibility(View.VISIBLE);
            aceptar3.setEnabled(true);
            txtqr.setText(detalleEdit.getQr());
            contmuestra = detalleEdit.getNum_muestra();
            contmuint = 1;
            //alta saber el numero total de cajas
            //    contcaja=detalleEdit.getNum_caja();

            //  txtcajaact.setText("CAJA "+contcaja);


            // totcajas=infomeEdit.getTotal_cajas();
        } else {
            //busco en la bd para regresar a la primer muestra
            List<InformeEtapaDet> informeEtapaDet = mViewModel.getDetEtaxCaja(mViewModel.getIdNuevo(), 3, cajaini);
            Log.e(TAG, "total" + informeEtapaDet.size());
            if (informeEtapaDet != null && informeEtapaDet.size() > 0) {
                //   detalleEdit = informeEtapaDet.get(0);
                // txtrutaim.setText(detalleEdit.getRuta_foto());
                //  Bitmap bitmap1 = ComprasUtils.decodeSampledBitmapFromResource(detalleEdit.getRuta_foto(),80,80);
                //  fotomos.setImageBitmap(bitmap1);
                // txtqr.setText(detalleEdit.getQr());
                contmuestra = informeEtapaDet.get(0).getNum_muestra();
                contmuint = contmuestra;
            }
            spinnerValues = new ArrayList<>();

        }
        Log.e(TAG, "--" + contmuestra);
        //actualizo vista
        txtnumuestra.setText("MUESTRA " + contmuestra);
    }

    public void editarCapCaja() {
        sv1.setVisibility(View.GONE);
        sv6.setVisibility(View.GONE);
        sv3.setVisibility(View.GONE);

        svpregcaja.setVisibility(View.VISIBLE);
        int primerCaja = mViewModel.getMinCajaxCli(ciudadInf, clienteSel);
        Log.d(TAG,"primer caja"+primerCaja);
        //veo en que pregunta voy de acuerdo a la descripcion


        ImagenDetalle foto;
        mViewModel.preguntaAct = preguntaAct;

            //busco en la bd para regresar a la primer muestra
            foto = mViewModel.getFoto(Integer.parseInt(detalleEdit.getRuta_foto()));
            txtrutacaja.setText(foto.getRuta());
            Bitmap bitmap1 = ComprasUtils.decodeSampledBitmapFromResource(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + foto.getRuta(), 80, 80);
            fotomoscaj.setImageBitmap(bitmap1);
            fotomoscaj.setVisibility(View.VISIBLE);
            btnrotar2.setVisibility(View.VISIBLE);
            btnneacfotocaj.setEnabled(true);
            // contmuestra = detalleEdit.getNum_muestra();
            //veo en que pregunta voy de acuerdo a la descripcion

            preguntaAct = detalleEdit.getDescripcionId()-7;
            cajainif = detalleEdit.getNum_caja();
            txtdescfotocaj.setText(descripfoto[preguntaAct - 5]);
            txtdescidfoto.setText(descripcionid[preguntaAct - 5] + "");
            txtcajafoto.setText("CAJA " + cajainif);//la caja actual
            txtcajaact.setText(cajainif + "");
            //  txtcajaact.setText("CAJA "+contcaja);
            //para saber el contador
        contcajaf=cajainif-primerCaja+1;

            // totcajas=infomeEdit.getTotal_cajas();

        Log.e(TAG, "--" + contcajaf);
        //actualizo vista
        txtnumuestra.setText("MUESTRA " + contmuestra);
    }

    public void mostrarCapturaCajaxDesc(String numdescr) {
        Log.d(TAG,"-----"+numdescr);
        //busco la foto anterior
        LiveData<InformeEtapaDet> lddetalle= mViewModel.getDetallexDescCaja(informeSel,numdescr,cajainif);
        lddetalle.observe(getViewLifecycleOwner(), new Observer<InformeEtapaDet>() {
             @Override
             public void onChanged(InformeEtapaDet informeEtapaDet) {
                 if(informeEtapaDet!=null) {
                     detalleEdit=informeEtapaDet;
                     isEdicion=true;

                     txtdescfotocaj.setText(detalleEdit.getDescripcion());
                     txtdescidfoto.setText(detalleEdit.getDescripcionId() + "");
                     //busco la foto
                     ImagenDetalle foto = mViewModel.getFoto(Integer.parseInt(detalleEdit.getRuta_foto()));
                     txtrutacaja.setText(foto.getRuta());
                     Bitmap bitmap1 = ComprasUtils.decodeSampledBitmapFromResource(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + foto.getRuta(), 80, 80);
                     fotomoscaj.setImageBitmap(bitmap1);
                     fotomoscaj.setVisibility(View.VISIBLE);
                     btnrotar2.setVisibility(View.VISIBLE);
                     btnneacfotocaj.setEnabled(true);
                 }
                 txtcajafoto.setText("CAJA " + cajainif);//la caja actual
                 txtcajaact.setText(cajainif + "");
                 lddetalle.removeObservers(getViewLifecycleOwner());
             }
         });


    }

    public void editarMuestra() {

        ImagenDetalle foto;
        detalleEdit=mViewModel.getUltimaMuestra(informeSel);
        if (detalleEdit != null) {
            //busco en la bd para regresar a la primer muestra
            foto = mViewModel.getFoto(Integer.parseInt(detalleEdit.getRuta_foto()));
            txtrutaim.setText(foto.getRuta());
            Bitmap bitmap1 = ComprasUtils.decodeSampledBitmapFromResource(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + foto.getRuta(), 80, 80);
            fotomos.setImageBitmap(bitmap1);
            fotomos.setVisibility(View.VISIBLE);
            btnrotar.setVisibility(View.VISIBLE);

            txtqr.setText(detalleEdit.getQr());
            contmuestra = detalleEdit.getNum_muestra();

            txtnumuestra.setText("MUESTRA " + contmuestra);
            //alta saber el numero total de cajas
            //    contcaja=detalleEdit.getNum_caja();

            //  txtcajaact.setText("CAJA "+contcaja);


            // totcajas=infomeEdit.getTotal_cajas();
            aceptar3.setEnabled(true);
            aceptar4.setEnabled(true);
            aceptar5.setEnabled(true);
            aceptar6.setEnabled(true);
        }

    }
    public void atras(){
        Log.d(TAG,"**contf"+contcajaf);
        switch (preguntaAct){
            case 3: //qr
                sv3.setVisibility(View.VISIBLE);
                svotra.setVisibility(View.GONE);
                sv4.setVisibility(View.GONE);
                preguntaAct=preguntaAct-1;
                mViewModel.preguntaAct=preguntaAct;
                break;
            //  sv6.setVisibility(View.VISIBLE);
            //  sv3.setVisibility(View.GONE);
            //   txtcajaact.setVisibility(View.GONE);

            case 4://caja

                sv4.setVisibility(View.VISIBLE);
                sv6.setVisibility(View.GONE);
                svotra.setVisibility(View.GONE);
                preguntaAct=preguntaAct-1;
                mViewModel.preguntaAct=preguntaAct;
                break;
            case 5://foto cara a
                contmuestra--;
                contmuint--;
                svotra.setVisibility(View.GONE);
                //puede ser que tenga que volver a otra caja
                if(contcajaf>1){
                    preguntaAct=7;
                    mViewModel.preguntaAct=preguntaAct;
                    contcajaf--;
                    cajainif--;
                    mostrarCapturaCajaxDesc(descripfoto[2]);
                }else {
                    svpregcaja.setVisibility(View.GONE);
                    sv6.setVisibility(View.VISIBLE);
                    preguntaAct = preguntaAct - 1;
                    mViewModel.preguntaAct = preguntaAct;
                    editarMuestra();
                }
                break;

            case 6://foto cara b
                preguntaAct=preguntaAct-1;
                mViewModel.preguntaAct=preguntaAct;
                mostrarCapturaCajaxDesc(descripfoto[0]);

                break;
            case 7://acomodo

                preguntaAct=preguntaAct-1;
                mViewModel.preguntaAct=preguntaAct;
                mostrarCapturaCajaxDesc(descripfoto[1]);
                break;
            case 8://comentarios
                svpregcaja.setVisibility(View.VISIBLE);
                sv5.setVisibility(View.GONE);
                preguntaAct=preguntaAct-1;
                mViewModel.preguntaAct=preguntaAct;
                mostrarCapturaCajaxDesc(descripfoto[2]);
                break;
        }
        Log.d(TAG,"**"+preguntaAct);

    }

    public void mostrarlayout(){
        switch (preguntaAct){
            case 1:
                sv1.setVisibility(View.GONE);
                sv6.setVisibility(View.VISIBLE);
                break;
            case 2:
                sv6.setVisibility(View.GONE);
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
            //  totcajas = 0;

            //totcajas =Integer.parseInt(txtnumcajas.getText().toString());

            if (preguntaAct == 2 && !isEdicion&&mViewModel.getNvoinforme()==null&&contmuestra==1) {
                Log.d(TAG, "creando nvo inf");
                //creo el informe
                mViewModel.setIdNuevo(mViewModel.insertarEtiq(Constantes.INDICEACTUAL, clienteNombreSel,clienteSel,0,totmuestras,ciudadInf));
              //  ((NuevoInfEtapaActivity)getActivity()).actualizarBarraEtiq(mViewModel.getNvoinforme());

            }
        }catch (Exception ex){
            ex.printStackTrace();
            Log.e(TAG,"Algo salió mal al guardarInf"+ex.getMessage());
            Toast.makeText(getContext(),"Hubo un error al guardar intente de nuevo",Toast.LENGTH_SHORT).show();

        }

        aceptar1.setEnabled(true);
        avanzar();
    }
    public void guardarDet(){
        try{
            String rutafoto = null;
            String qr = null;


            rutafoto=txtrutaim.getText().toString();
            qr=txtqr.getText().toString();

            String opcionsel = (String) spcaja.getSelectedItem();
            int numcaja = Integer.parseInt(opcionsel);
            if(isEdicion&&detalleEdit!=null){
                mViewModel.actualizarEtiqDet(mViewModel.getIdNuevo(),11,"foto_etiqueta",rutafoto,detalleEdit.getId(),numcaja,qr,contmuestra,detalleEdit.getRuta_foto(),Constantes.INDICEACTUAL);
                isEdicion=false;

            }else
            if(mViewModel.getIdNuevo()>0)
                //guardo el detalle
                mViewModel.insertarEtiqDet(mViewModel.getIdNuevo(),11,"foto_etiqueta",rutafoto,0,numcaja,qr,contmuestra,Constantes.INDICEACTUAL);
            //limpio campos
            txtrutaim.setText("");
            fotomos.setImageBitmap(null);
            fotomos.setVisibility(View.GONE);
            btnrotar.setVisibility(View.GONE);
            aceptar3.setEnabled(false);
            aceptar4.setEnabled(false);
            txtqr.setText("");
            detalleEdit=null;
            //muevo contadores
            contmuestra++;
            contmuint++;
            //actualizo vista
            txtnumuestra.setText("MUESTRA "+contmuestra);
        }catch (Exception ex){
            ex.printStackTrace();
            Log.e(TAG,"Algo salió mal al guardarDet"+ex.getMessage());
            Toast.makeText(getContext(),"Hubo un error al guardar intente de nuevo",Toast.LENGTH_SHORT).show();

        }

    }

    public void guardarDetCaja(){
        try{
            String rutafoto = null;
            rutafoto=txtrutacaja.getText().toString();
            int descripfotoid=0;
            String descripfoto=txtdescfotocaj.getText().toString();
            int numcaja=0;
            try {
                 numcaja = Integer.parseInt(txtcajaact.getText().toString());
                 descripfotoid=Integer.parseInt(txtdescidfoto.getText().toString());
            }catch (NumberFormatException ex){
                milog.grabarError(TAG+" guardarDetCaja"+ex.getMessage());
            }

            if(isEdicion&&detalleEdit!=null){
                mViewModel.actualizarEtiqDet(mViewModel.getIdNuevo(),descripfotoid,descripfoto,rutafoto,detalleEdit.getId(),numcaja,null,0,detalleEdit.getRuta_foto(),Constantes.INDICEACTUAL);
                isEdicion=false;

            }else
            if(mViewModel.getIdNuevo()>0)
                //guardo el detalle
                mViewModel.insertarEtiqDet(mViewModel.getIdNuevo(),descripfotoid,descripfoto,rutafoto,0,numcaja,null,0,Constantes.INDICEACTUAL);
            //limpio campos
            txtrutacaja.setText("");
            txtcajaact.setText("");
            txtdescidfoto.setText("");
            fotomoscaj.setImageBitmap(null);
            fotomoscaj.setVisibility(View.GONE);
            btnrotar2.setVisibility(View.GONE);
            detalleEdit=null;
            btnneacfotocaj.setEnabled(false);

        }catch (Exception ex){
            ex.printStackTrace();
            Log.e(TAG,"Algo salió mal al guardarDet"+ex.getMessage());
            Toast.makeText(getContext(),"Hubo un error al guardar intente de nuevo",Toast.LENGTH_SHORT).show();

        }

    }
    //cambiar estatus sol
    public void finalizarInf() {
        try {
            mViewModel.finalizarInf();
            InformeEtapaEnv envio=mViewModel.preparaInformeEtiq(mViewModel.getIdNuevo());
            SubirInformeEtaTask miTareaAsincrona = new SubirInformeEtaTask(envio,getActivity());
            miTareaAsincrona.execute();
            subirFotos(getActivity(),envio);
        }catch(Exception ex){
            ex.printStackTrace();
            Log.e(TAG,"Algo salió mal al finalizar"+ex.getMessage());
            Toast.makeText(getContext(),"Algo salio mal al enviar",Toast.LENGTH_SHORT).show();
        }
        // limpio variables de sesion

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
            //  mViewModel.actualizarMuestras(mViewModel.getIdNuevo(), contmuestra-1);
        }catch(Exception ex){
            ex.printStackTrace();
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
            Uri photoURI = FileProvider.getUriForFile(activity,
                    "com.example.comprasmu.fileprovider",
                    archivofoto);
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
                    if(requestCode==1)
                        mostrarFoto(txtrutaim,fotomos,btnrotar);
                    if(requestCode==2) //foto de caja
                        mostrarFoto(txtrutacaja,fotomoscaj,btnrotar2);
                }
                if(requestCode==1)
                    aceptar3.setEnabled(true);
                if(requestCode==2) {
                    Log.e(TAG,"que raro :(");
                    btnneacfotocaj.setEnabled(true);
                }

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




    public static void subirFotos(Activity activity, InformeEtapaEnv informe){
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

    private  void convertirLista(List<ListaCompra>lista, Integer[] clientesprev){
        listaClientes =new ArrayList<DescripcionGenerica>();
        for (ListaCompra listaCompra: lista ) {
            Log.d(TAG,listaCompra.getPlantaNombre());
            if( clientesprev!=null)
                if(Arrays.asList(clientesprev).contains(listaCompra.getClientesId()))
                {     //&&IntStream.of(clientesprev).anyMatch(n -> n == listaCompra.getClientesId()))
                    Log.d(TAG,"estoy aqui"+Arrays.asList(clientesprev));
                    continue;}
            listaClientes.add(new DescripcionGenerica(listaCompra.getClientesId(), listaCompra.getClienteNombre()));

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
    private void cargarPlantas(List<DescripcionGenerica> selectdes,String value){
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


        spcliente.setAdapter(catAdapter);
        spcliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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
                    spcliente.setSelection(catAdapter.getPosition(cat),true);
                    break;
                }
                if(value.equals(cat.getNombre())){
                    Log.d("CreadorForm",catAdapter.getPosition(cat)+"");

                    spcliente.setSelection(catAdapter.getPosition(cat),true);
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
        sv1= sv6 =sv3=sv4=null;
        btnrotar=null;
        aceptar1=null;
        nombre_foto=null;
        archivofoto=null;
    }




}

