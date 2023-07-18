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
import java.util.Date;
import java.util.List;


public class ReubicEtiqFragment extends Fragment {

    private  InformeEtapaDet detalleEdit;

    private static final String TAG = "ReubicEtiqFragment";
    Button buscar,nvacaja,guardar;
    private long lastClickTime = 0;

    EditText  txtqr;
    Spinner spcaja;
    private ImageButton btnqr;

    private View root;
    private NvaPreparacionViewModel mViewModel;
    List<String> spinnerValues;
    private ListaDetalleViewModel lcViewModel;
    private LinearLayout p4, p5, lbotones;
    private static final int REQUEST_CODEQR = 345;

    ComprasLog milog;
    int totcajas;
    public ReubicEtiqFragment() {

    }
    public static ReubicEtiqFragment newInstance() {
        return new ReubicEtiqFragment();
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        root= inflater.inflate(R.layout.fragment_reubetiq, container, false);

      //  svcoin = root.findViewById(R.id.llnecoincide);
        guardar = root.findViewById(R.id.btnreguardar);

        btnqr = root.findViewById(R.id.btnreobtqr);
        mViewModel = new ViewModelProvider(requireActivity()).get(NvaPreparacionViewModel.class);
        lcViewModel = new ViewModelProvider(this).get(ListaDetalleViewModel.class);
        milog=ComprasLog.getSingleton();

        spcaja=root.findViewById(R.id.sprecaja);
        nvacaja = root.findViewById(R.id.btnrecajamas);
        txtqr=root.findViewById(R.id.txtreqr);
        buscar=root.findViewById(R.id.btnrebuscar);
        p4=root.findViewById(R.id.llrepre4);
        p5=root.findViewById(R.id.llrepre5);
        lbotones=root.findViewById(R.id.llrebotones);
       // potra.setmLabel("¿INCLUIRAS OTRA MUESTRA EN ESTA CAJA?");

//        ((NuevoInfEtapaActivity)getActivity()).cambiarTitulo("REUBICAR MUESTRA");
       // mostrarp4();
        txtqr.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        txtqr.addTextChangedListener(new BotonTextWatcher(guardar));

     //   txtnumcajas.addTextChangedListener(new BotonTextWatcher(aceptar2));
        spinnerValues = new ArrayList<>();
        //busco el total de cajas
        totcajas=mViewModel.getTotCajasEtiq(Constantes.CIUDADTRABAJO);
        for(int i=1;i<=totcajas;i++) {
            spinnerValues.add(i+"");
        }
        //todo buscar 1ro qr y luego la caja
       /* if(totcajas==0) {
            totcajas = 1;
            spinnerValues.add(1+"");
        }
        else { //ya tengo cajas  busco la ultima de esa cd
            List<InformeEtapaDet> listacajas=mViewModel.listaCajasEtiqxCd(Constantes.CIUDADTRABAJO,1);
            if(listacajas!=null) {
                totcajas = listacajas.size();
                if (totcajas > 0) { //las numeracion que ya tengo
                    for (InformeEtapaDet det : listacajas
                    ) {
                        spinnerValues.add(det.getNum_caja() + "");
                    }
                }else { //cambié de cd

                    totcajas = totcajas + 1; //para saber la sig caja
                    spinnerValues.add(totcajas + "");
                }
            }else { //cambié de cd

                totcajas = totcajas + 1; //para saber la sig caja
                spinnerValues.add(totcajas + "");
            }

        }*/

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerValues);
        spcaja.setAdapter(adapter);

        btnqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarLecQR();
            }

        });

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //buscarQr();
            }

        });


        milog.grabarError(TAG+" o x aca");
        //buscar la solicitud
        Bundle datosRecuperados = getArguments();


        nvacaja.setOnClickListener(new View.OnClickListener() { //coincide
            @Override
            public void onClick(View view) {
                nvacaja();

            }
        });
        guardar.setEnabled(false);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardar.setEnabled(false);
                long currentClickTime= SystemClock.elapsedRealtime();
                // preventing double, using threshold of 1000 ms
                if (currentClickTime - lastClickTime < 5500){
                    //  Log.d(TAG,"doble click :("+lastClickTime);
                    return;
                }

                lastClickTime = currentClickTime;

                 cambiarMues();



            }
        });


        return root;
    }

public void nvacaja(){
        totcajas++;
    spinnerValues.add(totcajas+"");

    }
    public void mostrarp4(){
        p4.setVisibility(View.VISIBLE);
        p5.setVisibility(View.GONE);
        lbotones.setVisibility(View.GONE);
    }
    public void mostrarp5(){
        p4.setVisibility(View.GONE);
        p5.setVisibility(View.VISIBLE);
        lbotones.setVisibility(View.VISIBLE);
    }
    public void cambiarMues(){
        if(txtqr.getText().toString().equals("")) {

            return;

        }
        InformeEtapaDet muestra=mViewModel.buscarDetxQr(txtqr.getText().toString());
        String opcionsel = (String) spcaja.getSelectedItem();
        if(opcionsel==null)
            return;
        int numcaja = Integer.parseInt(opcionsel);
        if(muestra!=null){
            muestra.setNum_caja(numcaja);
            mViewModel.actualizarInfEtaDet(muestra);
            //reenvio al serv
            InformeEtapaEnv envio= preparaInforme(muestra.getInformeEtapaId(),muestra);
            SubirInformeEtaTask miTareaAsincrona = new SubirInformeEtaTask(envio,getActivity());
            miTareaAsincrona.execute();
            //subirFotos(getActivity(),envio);
            Toast.makeText(getActivity(), getString(R.string.muestra_reubicada), Toast.LENGTH_SHORT).show();
            salir();
        }else{
            Toast.makeText(getActivity(), getString(R.string.verifique_codigo), Toast.LENGTH_LONG).show();

        }

    }


    public InformeEtapaEnv preparaInforme(int idnvo,InformeEtapaDet muestra){
        InformeEtapaEnv envio=new InformeEtapaEnv();

        envio.setInformeEtapa(mViewModel.getInformexId(idnvo));

        envio.setClaveUsuario(Constantes.CLAVEUSUARIO);
        envio.setIndice(Constantes.INDICEACTUAL);
        List<InformeEtapaDet> lista=new ArrayList<>();
        lista.add(muestra);
        envio.setInformeEtapaDet(lista);

        return envio;
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
    public void iniciarLecQR(){
        IntentIntegrator integrator  =new  IntentIntegrator ( getActivity() ).forSupportFragment(ReubicEtiqFragment.this);
        integrator.setRequestCode(REQUEST_CODEQR);
        //  integrator.setOrientationLocked(false);
        Log.d(TAG, "inciando scanner");
        integrator.initiateScan();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODEQR) {
            IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);
              if (result != null) {

                if (result.getContents() == null) {
                    Toast.makeText(getActivity(), "Scan cancelled", Toast.LENGTH_LONG).show();
                } else {   /* Update the textview with the scanned URL result */
                    txtqr.setText(result.getContents());
                  //  aceptar4.setEnabled(true);
                    //  Toast.makeText(getActivity(), "Content: ${result.getContents()}",Toast.LENGTH_LONG ).show();
                }

            } else {
                super.onActivityResult(requestCode, resultCode, data);
                Toast.makeText(getActivity(), "hubo un error", Toast.LENGTH_LONG).show();

            }
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel = null;
        root=null;

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


}

