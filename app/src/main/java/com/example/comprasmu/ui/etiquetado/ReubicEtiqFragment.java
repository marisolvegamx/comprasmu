package com.example.comprasmu.ui.etiquetado;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.SubirInformeEtaTask;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.remote.InformeEtapaEnv;
import com.example.comprasmu.services.SubirFotoService;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.ui.preparacion.NvaPreparacionViewModel;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
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
    private InformeEtapaDet muestraEdit;
    private int ultimacaja;
    ArrayAdapter<String> adaptercaja;
    InformeEtapa informeEtapaAct;
    private TextView txtciudad, txtcliente, txtindice, txttotmuestras;

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
        txtciudad=root.findViewById(R.id.txtnieciudad);
        txtcliente=root.findViewById(R.id.txtniecliente2);
        txtindice=root.findViewById(R.id.txtnieindice);
        txttotmuestras=root.findViewById(R.id.txtnietotmues);
        buscar=root.findViewById(R.id.btnrebuscar);
        p4=root.findViewById(R.id.llrepre4);
        p5=root.findViewById(R.id.llrepre5);
        lbotones=root.findViewById(R.id.llrebotones);
        //reviso si ya tengo uno abierto


        //puedo reubicar

       // potra.setmLabel("¿INCLUIRAS OTRA MUESTRA EN ESTA CAJA?");

//        ((NuevoInfEtapaActivity)getActivity()).cambiarTitulo("REUBICAR MUESTRA");
        mostrarp4();
        txtqr.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        txtqr.addTextChangedListener(new BotonTextWatcher(guardar));

     //   txtnumcajas.addTextChangedListener(new BotonTextWatcher(aceptar2));
        spinnerValues = new ArrayList<>();

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
     //  spcaja.setEnabled(false);

        btnqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarLecQR();
            }

        });

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarQr();
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


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //solo se reubica antes de enviar y el informe que está activo
        informeEtapaAct = mViewModel.getInformePend(Constantes.INDICEACTUAL, 3);

        if (informeEtapaAct == null) {
            Fragment me=this;
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
            dialogo1.setTitle(R.string.atencion);
            dialogo1.setMessage(R.string.informe_reubicar);
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    //lo mando a continuar
                    NavHostFragment.findNavController(me).navigate(R.id.nav_home);

                }
            });

            dialogo1.show();

        } else {
            txtciudad.setText(Constantes.CIUDADTRABAJO);
            txtcliente.setText(informeEtapaAct.getClienteNombre());
            txtindice.setText(ComprasUtils.indiceLetra(Constantes.INDICEACTUAL));
        }
    }

    public void nvacaja(){
        ultimacaja++;
        spinnerValues.add(ultimacaja+"");
        adaptercaja.notifyDataSetChanged();
    }
    public void mostrarp4(){
        p4.setVisibility(View.VISIBLE);
        p5.setVisibility(View.GONE);
        buscar.setEnabled(true);
        lbotones.setVisibility(View.GONE);
    }
    public void mostrarp5(){
        p4.setVisibility(View.GONE);
        p5.setVisibility(View.VISIBLE);

        lbotones.setVisibility(View.VISIBLE);
    }
    public void buscarQr(){
        if(txtqr.getText().toString().equals("")) {

            return;

        }
        muestraEdit =mViewModel.buscarDetxQr(txtqr.getText().toString());
        if(muestraEdit ==null) { //busco el total de cajas

            Toast.makeText(getActivity(), getString(R.string.verifique_codigo), Toast.LENGTH_LONG).show();

            return;
        }

         /*  InformeEtapa informeSel=mViewModel.getInformexId(muestraEdit.getInformeEtapaId());
           if(informeSel==null) {

               Toast.makeText(getActivity(), getString(R.string.verifique_codigo), Toast.LENGTH_LONG).show();

               return;
           }*/

               int clienteSel = informeEtapaAct.getClientesId();
               List<InformeEtapaDet> listacaj=mViewModel.listaCajasEtiqxCdCli(Constantes.CIUDADTRABAJO, clienteSel);
               if(listacaj!=null) {
                   for (int i = 0; i < listacaj.size(); i++) {
                       spinnerValues.add(listacaj.get(i).getNum_caja() + "");
                       ultimacaja=listacaj.get(i).getNum_caja();
                   }
                   totcajas = listacaj.size();
               }
               //todo buscar 1ro qr y luego la caja
              adaptercaja = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerValues);
               spcaja.setAdapter(adaptercaja);
               mostrarp5();


    }
    public void cambiarMues(){

        String opcionsel = (String) spcaja.getSelectedItem();
        if(opcionsel==null)
            return;
        int numcaja = Integer.parseInt(opcionsel);

            muestraEdit.setNum_caja(numcaja);
            mViewModel.actualizarInfEtaDet(muestraEdit);
            //reenvio al serv
            InformeEtapaEnv envio= preparaInforme(muestraEdit.getInformeEtapaId(), muestraEdit);
            SubirInformeEtaTask miTareaAsincrona = new SubirInformeEtaTask(envio,getActivity());
            miTareaAsincrona.execute();
            //subirFotos(getActivity(),envio);
            Toast.makeText(getActivity(), getString(R.string.muestra_reubicada), Toast.LENGTH_SHORT).show();
            salir();


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

