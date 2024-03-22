package com.example.comprasmu.ui.etiquetado;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.ui.RevisarFotoActivity;
import com.example.comprasmu.ui.infetapa.EditInfEtapaActivity;
import com.example.comprasmu.ui.infetapa.NuevoInfEtapaActivity;
import com.example.comprasmu.ui.infetapa.NuevoInfEtapaViewModel;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.ui.preparacion.NvaPreparacionViewModel;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.micamara.MiCamaraActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

//cuando se cancela una muestra desde compra y se cancela en etiquetado se vuelve a pedir
public class EditEtiquetadoFragment extends Fragment {

    private InformeEtapaDet detalleEdit;
    LinearLayout  sv6, sv3, sv4, svotra;
    private static final String TAG = "EditEtiquetadoFragment";
    Button  aceptar3, aceptar4, aceptar5, aceptar6, nvacaja;
    //eliminarCaja

    private long lastClickTime = 0;
    private boolean yaestoyProcesando = false;
    EditText txtrutaim, txtqr, txtrutacaja, txtcajaact, txtdescidfoto;
    TextView txtnumuestra, txttotmues, txtdescfotocaj, txtcajafoto;
    ImageView fotomos, fotomoscaj;
    private ImageButton btnrotar, btntomarf, btnqr;
    public int REQUEST_CODE_TAKE_PHOTO = 1;
    NuevoInfEtapaViewModel niviewModel;

    private View root;
    private NvaPreparacionViewModel mViewModel;
    private int informeSel;
    private int clienteSel;
    int etapa=3;
    private String clienteNombreSel;

    private boolean isCont;
    private InformeEtapa infomeEdit;
    int preguntaAct;
    private ListaDetalleViewModel lcViewModel;
    List<ListaCompra> listacomp;
    private static final int REQUEST_CODEQR = 345;
    int cajaini; //se usa para las cajas de la muestra
    int contmuestra, contmuint, contcajaf; //contador para fotos caja
    int totmuestras, totcajas; //cajaact
    int totmuestrascan;
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

    public EditEtiquetadoFragment() {

    }

    public static EditEtiquetadoFragment newInstance() {
        return new EditEtiquetadoFragment();
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_edietiq, container, false);

        sv6 = root.findViewById(R.id.lleepre6);
        sv3 = root.findViewById(R.id.lleepreg3);
        sv4 = root.findViewById(R.id.llrepre4);
     //   txttotcaj = root.findViewById(R.id.txteetotcaj);
        svotra = root.findViewById(R.id.lleebotones);

        aceptar3 = root.findViewById(R.id.btneeac3);
        aceptar4 = root.findViewById(R.id.btneeac4);
        aceptar5 = root.findViewById(R.id.btneeac5);
        aceptar6 = root.findViewById(R.id.btneeacep6);
        btnreubicar= root.findViewById(R.id.btneereubicar);

        nvacaja = root.findViewById(R.id.btneecajamas);

        btnrotar = root.findViewById(R.id.btneerotar1);

        btntomarf = root.findViewById(R.id.btneefoto);

        // listaqr = root.findViewById(R.id.rvnelisqr);
        btnqr = root.findViewById(R.id.btneeobtqr);
        niviewModel = new ViewModelProvider(requireActivity()).get(NuevoInfEtapaViewModel.class);

        mViewModel = new ViewModelProvider(requireActivity()).get(NvaPreparacionViewModel.class);
        lcViewModel = new ViewModelProvider(this).get(ListaDetalleViewModel.class);
        milog = ComprasLog.getSingleton();


        sv6.setVisibility(View.GONE);
        sv3.setVisibility(View.GONE);
        sv4.setVisibility(View.GONE);

        svotra.setVisibility(View.GONE);

        fotomos = root.findViewById(R.id.iveefotomue);

        txtrutaim = root.findViewById(R.id.txteeruta);
        txtnumuestra = root.findViewById(R.id.txteemuestra);
        txtnumuestra = root.findViewById(R.id.txteenummuesorig);

        // pcoincide=root.findViewById(R.id.sinonecoincide);
        // potra=root.findViewById(R.id.sinoneotramu);
      //  spcliente = root.findViewById(R.id.speeplanta);
        spcaja = root.findViewById(R.id.speecaja);

     //   txtcajaact = root.findViewById(R.id.txteecajaact);

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

        }

        txtqr = root.findViewById(R.id.txteeqr);
        //deshabilito botones de aceptar

        aceptar3.setEnabled(false);
        aceptar4.setEnabled(false);
        aceptar5.setEnabled(false);
        txtqr.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        txtqr.addTextChangedListener(new BotonTextWatcher(aceptar4));

        //   txtnumcajas.addTextChangedListener(new BotonTextWatcher(aceptar2));

        adaptercaja = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerValues);

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

        if (preguntaAct == 0)
            preguntaAct = 1;
        mViewModel.preguntaAct = preguntaAct;



        //busco el informe y el detalle

        infomeEdit = mViewModel.getInformexId(informeSel);
        preguntaAct = 3;
        mViewModel.preguntaAct = 3;
        ((EditInfEtapaActivity) getActivity()).actualizarBarraEtiq(infomeEdit);
        mViewModel.setIdNuevo(informeSel);
        clienteSel=infomeEdit.getClientesId();
        ciudadInf=infomeEdit.getCiudadNombre();
        //busco los clientes x ciudad
        listacomp = lcViewModel.cargarClientesSimplxet(Constantes.CIUDADTRABAJO,this.etapa);
        if (listacomp.size() <=0) {
            TextView txtmensaje = root.findViewById(R.id.txteefaltacom);
            txtmensaje.setText("FALTA REALIZAR EL INFORME DE COMPRA");
            txtmensaje.setVisibility(View.VISIBLE);
            return root; //todavía no puede hacer etiquetado
        }


        totmuestras = mViewModel.getTotalMuestrasxCliXcd(clienteSel, ciudadInf);

        //busco el ultimo num de muestra
        InformeEtapaDet ultima=mViewModel.getUltimaMuestraEtiq(informeSel);
        if(ultima!=null)
            contmuestra =ultima.getNum_muestra()+1;
        //veo si es de muestra o de cja
        if (detalleEdit != null && detalleEdit.getDescripcionId() > 11) {
            capturarFotoCaja();
        } else
            mostrarCapMuestra();


        if (preguntaAct > 1) //ya tengo planta y cliente
        {
            cargarListaCajas();
        }

        if (detalleEdit != null) {

            spcaja.setSelection(adaptercaja.getPosition(detalleEdit.getNum_caja() + ""));

        }


        aceptar3.setOnClickListener(new View.OnClickListener() { //foto
            @Override
            public void onClick(View view) {
                lastClickTime=0;
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
        nvacaja.setOnClickListener(new View.OnClickListener() { //coincide
            @Override
            public void onClick(View view) {
                nvacaja();

            }
        });

        aceptar6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                avanzar();


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
                tomarFoto(1);
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
                    cajaini =totxciu;
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
                    capturarFotoCaja();
                    break;
                }

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
        Bundle args = new Bundle();
        args.putInt(EditEtiquetadoFragment.ARG_PREGACT,5 );
        args.putBoolean(EditEtiquetadoFragment.ARG_ESEDI,this.isEdicion);
        args.putBoolean(NvoEtiqCajaFragment.ARG_ESACT,true);

        args.putInt(EditEtiquetadoFragment.ARG_INFORMESEL,this.informeSel);
        NvoEtiqCajaFragment nvofrag = new NvoEtiqCajaFragment();
        nvofrag.setArguments(args);

        //busco la pregunta actual en la decripcion

        // args.putInt(NvoEtiquetadoFragment.ARG_INFORMEDET,det.getId() );

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
// Definir una transacción
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
// Remplazar el contenido principal por el fragmento
        fragmentTransaction.replace(R.id.continfeta_fragment, nvofrag);
        //     fragmentTransaction.addToBackStack(null);
// Cambiar
        fragmentTransaction.commit();

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
        if(prods!=null&&detalleEdit!=null)
            Log.d(TAG,prods.getId()+"--"+detalleEdit.getId()+" "+isEdicion);
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

            if(cajas==null||cajas.size()<1)
              return true;

        }
        return false;
    }


    public void mostrarCapMuestra() {

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



    public void editarMuestra() {

        ImagenDetalle foto;
        detalleEdit=mViewModel.getUltimaMuestraEtiq(informeSel);
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
        isEdicion=true; //siempre es edicion
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

        }
        Log.d(TAG,"**"+preguntaAct);

    }

    public void mostrarlayout(){
        switch (preguntaAct){
            case 1:
              //  sv1.setVisibility(View.GONE);
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

                break;
            case 5:

                break;
        }
    }

    public void guardarDet(){
        try{
            String rutafoto = null;
            String qr = null;


            rutafoto=txtrutaim.getText().toString();
            qr=txtqr.getText().toString();

            String opcionsel = (String) spcaja.getSelectedItem();
            int numcaja = Integer.parseInt(opcionsel);
            int iddet=0;
            if(isEdicion&&detalleEdit!=null){
               iddet= mViewModel.actualizarEtiqDet(mViewModel.getIdNuevo(),11,"foto_etiqueta",rutafoto,detalleEdit.getId(),numcaja,qr,contmuestra,detalleEdit.getRuta_foto(),Constantes.INDICEACTUAL);
                isEdicion=false;

            }else
            if(mViewModel.getIdNuevo()>0)
                //guardo el detalle
               iddet= mViewModel.insertarEtiqDet(mViewModel.getIdNuevo(),11,"foto_etiqueta",rutafoto,0,numcaja,qr,contmuestra,Constantes.INDICEACTUAL);
           //guardo para enviar despues
            mViewModel.muestrasactEtiq.add(iddet);
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

                        mostrarFoto(txtrutaim,fotomos,btnrotar);

                }
                if(requestCode==1)
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
        IntentIntegrator integrator  =new  IntentIntegrator ( getActivity() ).forSupportFragment(EditEtiquetadoFragment.this);
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
        sv6 =sv3=sv4=null;
        btnrotar=null;
      //  aceptar1=null;
        nombre_foto=null;
        archivofoto=null;
    }

}
