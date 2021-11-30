package com.example.comprasmu.ui.informedetalle;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.SubirInformeTask;
import com.example.comprasmu.data.modelos.Atributo;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.Contrato;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.Reactivo;
import com.example.comprasmu.data.modelos.Visita;
import com.example.comprasmu.data.remote.InformeEnvio;
import com.example.comprasmu.services.SubirFotoService;
import com.example.comprasmu.ui.BackActivity;
import com.example.comprasmu.ui.RevisarFotoActivity;
import com.example.comprasmu.ui.informe.ListaInformesFragment;
import com.example.comprasmu.ui.informe.NuevoinformeFragment;
import com.example.comprasmu.ui.informe.NuevoinformeViewModel;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.ui.visita.AbririnformeFragment;
import com.example.comprasmu.utils.CampoForm;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;
import com.example.comprasmu.utils.Preguntasino;
import com.example.comprasmu.utils.RPResultListener;
import com.example.comprasmu.utils.RuntimePermissionUtil;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static com.example.comprasmu.ui.listacompras.TabsFragment.ARG_CLIENTESEL;

/***clase para mostrar los campos que faltan de capturar de la muestra uno por pantalla***/

public class DetalleProductoFragment extends Fragment {

    private NuevoinformeViewModel mViewModel;
    private NuevoDetalleViewModel dViewModel;
    CreadorFormulario cf;
    List<CampoForm> camposForm;
    private List<CatalogoDetalle> tomadoDe;
    private List<CatalogoDetalle>atributos;
    private static final String TAG="DETALLEPRODUCTOFRAG";

    private static final int SELECT_FILE = 1;
    private static final int REQUEST_CODEQR = 341;
    SimpleDateFormat sdf;
    SimpleDateFormat sdfcodigo;
    View root;

    boolean isSegunda;
    boolean isTercera;
    EditText textoint;
    Preguntasino pregunta;
   Spinner spclientes ;
    private Reactivo preguntaAct;
    ImageButton micbtn;
    ImageView fotomos;
    LinearLayout sv;

    NuevoDetalleViewModel.ProductoSel prodSel;
    public static  int REQUEST_CODE_TAKE_PHOTO=1;
    SpeechRecognizer sspeechRecognizer;
    private int tipoTienda;
    public static final int NUEVO_RESULT_OK =102 ;
    public static final Integer RecordAudioRequestCode = 1;
    private static final String cameraPerm = Manifest.permission.CAMERA;
    public final static String ARG_NUEVOINFORME="comprasmu.ni_idinforme";
    public static String NUMMUESTRA="comprasmu.ni.nummuestra";
    private ImageButton btnrotar;

    public DetalleProductoFragment(Reactivo preguntaAct) {
        this.preguntaAct = preguntaAct;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
       // setHasOptionsMenu(true);
         sdf = new SimpleDateFormat("dd-mm-yyyy");
        sdfcodigo = new SimpleDateFormat("dd-mmm-yy");
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel =
                new ViewModelProvider(requireActivity()).get(NuevoinformeViewModel.class);

        dViewModel=new ViewModelProvider(requireActivity()).get(NuevoDetalleViewModel.class);
        root = inflater.inflate(R.layout.fragment_generic, container, false);
        /**llegan los datos del producto el cliente y la planta seleccionada
         * desde la lista de compra
         */
        try {
            Log.d(TAG,"creando fragment");

            Log.d(TAG,"genere cons="+mViewModel.consecutivo+"----"+mViewModel.clienteSel);
            sv = root.findViewById(R.id.content_generic);
         //   mViewModel.cargarCatsContinuar();

            crearFormulario();
            if(preguntaAct.getType().equals(CreadorFormulario.SELECTCAT)||preguntaAct.getType().equals(CreadorFormulario.SELECTDES)||preguntaAct.getType().equals(CreadorFormulario.PSELECT)) {
                spclientes = root.findViewById(1001);
            }
             else
            if(preguntaAct.getType().equals(CreadorFormulario.PREGUNTASINO))
            {  pregunta=root.findViewById(1001);
            }else
            {
                textoint = root.findViewById(1001);
            }

            Button aceptar = root.findViewById(R.id.btngaceptar);
            aceptar.setEnabled(false);
            if(textoint!=null){
                textoint.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (i2>0){ //count es cantidad de caracteres que tiene
                            aceptar.setEnabled(true);
                        }else{
                            aceptar.setEnabled(false);
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
            if(spclientes!=null){
                spclientes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
            if(pregunta!=null){
                pregunta.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        aceptar.setEnabled(true);
                    }
                });
            }
            aceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 //   guardarResp();
                    siguiente();

                }
            });
            prodSel=dViewModel.productoSel;
            if(preguntaAct.isBotonMicro()) {

                micbtn=root.findViewById(R.id.btnmicsiglas);
                sspeechRecognizer = grabarVoz();
                micbtn.setVisibility(View.VISIBLE);
            }
            if(preguntaAct.getId()==42||preguntaAct.getId()==7){
                //cambio el boton a finalizar y mestro alerta
                aceptar.setText(getString(R.string.finalizar));
                aceptar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.botonvalido));
            }
            Log.d(TAG,"tipo tienda -----------*"+Constantes.DP_TIPOTIENDA);
            tipoTienda=Constantes.DP_TIPOTIENDA;



        } catch (Exception e) {
            e.printStackTrace();
        }

        return root;
    }

    public void crearFormulario(){
        camposForm=new ArrayList<>();
        CampoForm campo=new CampoForm();
        campo.label=preguntaAct.getLabel();
        campo.nombre_campo=preguntaAct.getNombreCampo();
        campo.type=preguntaAct.getType();
        campo.style=R.style.formlabel2;

        campo.id=1001;
        //para los catalogos
        if(preguntaAct.getType().equals(CreadorFormulario.SELECTCAT)||preguntaAct.getType().equals(CreadorFormulario.SELECTDES)){
            switch (preguntaAct.getNombreCampo()){
                case Contrato.TablaInformeDet.ATRIBUTOA:case Contrato.TablaInformeDet.ATRIBUTOB: case Contrato.TablaInformeDet.ATRIBUTOC:
                    getAtributos(campo);

                    break;
                case Contrato.TablaInformeDet.ORIGEN:
                    getTomadoDe(campo);

                    break;
              case "clientesId":
                    Log.d(TAG,"----"+Constantes.clientesAsignados.size());
                    campo.selectdes= Constantes.clientesAsignados;
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
                    rotar();
                }
            });


           // btnrotar.setVisibility(View.VISIBLE);


        }
        if(campo.type.equals("botonqr")) {
            campo.funcionOnClick=new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iniciarLecQR();
                }

        };
        }

        camposForm.add(campo);

        cf=new CreadorFormulario(camposForm,getContext());
        sv.addView(cf.crearFormulario());





    }
    public void compraProd(View view,int nummuestra) {
        // Is the button now checked?
        boolean checked = ((Preguntasino) view).getRespuesta();
        Log.d(TAG,"CLICK EN RADIOBUTTON ID="+view.getId());

        // Check which radio button was clicked
        switch(preguntaAct.getId()) {
            case 2: case 3: case 4:
                if (checked)
                    // fue si mostrar lista de compra
                    verListaCompra(nummuestra);
                else
                    avanzarPregunta(preguntaAct.getSigAlt());
                break;

            default: break;
        }
    }

    private  List<CatalogoDetalle> atributoaCat(List<Atributo> latributos){
        List<CatalogoDetalle> nuevocat=new ArrayList<CatalogoDetalle>();
        for(Atributo atri:latributos){
            CatalogoDetalle cat=new CatalogoDetalle();
            cat.setCad_idopcion(atri.getId_atributo());
            cat.setCad_descripcionesp(atri.getAt_nombre());
            //cat.setCad_descripcioning(atri.get);
            nuevocat.add(cat);
        }
        return  nuevocat;
    }
    public void getAtributos(CampoForm campo){
        dViewModel.cargarCatalogos(dViewModel.productoSel.empaque,dViewModel.productoSel.idempaque,dViewModel.productoSel.clienteSel);



        List<Atributo> atrs=dViewModel.satributos;

        atributos = atributoaCat(atrs);
        campo.selectcat=atributos;

    }
    public void getTomadoDe(CampoForm campo){
        dViewModel.cargarCatalogos(dViewModel.productoSel.empaque,dViewModel.productoSel.idempaque,dViewModel.productoSel.clienteSel);

        List<CatalogoDetalle> catalogoDetalles=dViewModel.tomadoDe;
                tomadoDe = catalogoDetalles;
                Log.d(TAG,"ya tengo los catalogos"+catalogoDetalles.size());
                campo.selectcat=tomadoDe;


    }
    public void regresar(){

    }
    public void siguiente(){
        boolean resp=false;
        switch (preguntaAct.getNombreCampo()){
            case Contrato.TablaInformeDet.SIGLAS:
                resp=validarSiglas();
                break;
                case Contrato.TablaInformeDet.CODIGO:
                   resp=validarCodigoprod();
                    break;
           case Contrato.TablaInformeDet.CADUCIDAD:
                    resp=validarFecha();

                break;
            case "clientesId":
                DescripcionGenerica opcionsel = (DescripcionGenerica) spclientes.getSelectedItem();
                int valor = opcionsel.getId();
                mViewModel.clienteSel=valor;
                Constantes.ni_clientesel=opcionsel.getNombre();
                int consecutivo=mViewModel.getConsecutivo(valor);
                Log.d(TAG,"genere cons="+consecutivo);
                mViewModel.informe=new InformeCompra();
                mViewModel.informe.setClienteNombre(opcionsel.getNombre());
                mViewModel.informe.setClientesId(valor);
                mViewModel.informe.setConsecutivo(consecutivo);
                mViewModel.consecutivo=consecutivo;
                //actualizo barra
                ((ContinuarInformeActivity)getActivity()).actualizarCliente(mViewModel.informe);
                resp=true;
            break;
             default: resp=true; break;
        }
        if(resp)
        {
            if(preguntaAct.getType().equals(CreadorFormulario.PREGUNTASINO)){
                //reviso la opcion seleccionada
                if(!pregunta.getRespuesta()) //se selecciono no
                {
                    if(preguntaAct.getId()==43){
                        //es la 43 //finalizo preinforme
                        finalizarPreinforme();
                    }
                    //voy al altsig
                    guardarResp();
                    //guarda informe

                    avanzarPregunta(preguntaAct.getSigAlt());
                    return;
                }
            }
            if(preguntaAct.getSigId()==10000) //voy a lista de compra
            {
                compraProd(pregunta,preguntaAct.getId() - 1);

            }else
            if(preguntaAct.getSigId()==43) //termine inf
            {
                guardarResp();

                //guarda informe
                this.actualizarInforme();
                this.finalizar();
                //limpiar tabla
                limpiarTablTemp();
                avanzarPregunta(43);
            }else
            if(preguntaAct.getSigId()==0)//terminé con preguntas de muestra
            {
                guardarResp();
                guardarMuestra();

                //limpiar tabla temp
                limpiarTablTemp();
                if(Constantes.NM_TOTALISTA>=16&&mViewModel.numMuestra==3||Constantes.NM_TOTALISTA<16&&mViewModel.numMuestra==2) //ya terminé
                {
                    Log.d(TAG,"terminé debo guradar y salir");
                    avanzarPregunta(5);
                            //preguntar si hay otro cliente, para agregar otro o cerrar
                }else
                {
                    avanzarPregunta(mViewModel.numMuestra+2);
                }

            }else
            if(preguntaAct.getId()==43){
                //avisar
            /*    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
                dialogo1.setTitle(R.string.importante);
                dialogo1.setMessage(R.string.informe_abierto);
                dialogo1.setCancelable(false);

                dialogo1.setNegativeButton(R.string.cerrar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        //  dialogo1.cancel();
                        //envio a la lista
                        NavHostFragment.findNavController(AbririnformeFragment.this).navigate(R.id.action_nuevotolista);

                    }
                });
                dialogo1.show();*/
                //es la 43 //finalizo informe
                mViewModel.finalizarInforme();
                //la muestra la guarde en la 42

                avanzarPregunta(1);
            }else {
                guardarResp();
                avanzarPregunta(preguntaAct.getSigId());
            }
        }

    }
    public void finalizar() {

        //validar que si hay producto realmente tenga un producto capturado


        mViewModel.finalizarInforme();
        try {
            InformeEnvio informe=preparaInforme();
            SubirInformeTask miTareaAsincrona = new SubirInformeTask(true,informe,getActivity());
            miTareaAsincrona.execute();


            subirFotos(getActivity(),informe);
        }catch(Exception ex){
            ex.getStackTrace();
            Log.e(TAG,"Algo salió mal al enviar"+ex.getMessage());
            Toast.makeText(getContext(),"Algo salio mal al enviar",Toast.LENGTH_SHORT).show();
        }


    }
    public InformeEnvio preparaInforme(){
        InformeEnvio envio=new InformeEnvio();
        envio.setVisita(mViewModel.visita);
        envio.setInformeCompra(mViewModel.informe);
        mViewModel.cargarMuestras(mViewModel.informe.getId());

        envio.setInformeCompraDetalles(mViewModel.muestrasCap);
        envio.setImagenDetalles(mViewModel.buscarImagenes(mViewModel.visita,mViewModel.informe,mViewModel.muestrasCap));
        return envio;
    }

    public void finalizarPreinforme(){
        //Es hora de cerrar el preinforme

        mViewModel.finalizarVisita(mViewModel.visita.getId());

        Toast.makeText(getActivity(), "Se finalizó el preinforme", Toast.LENGTH_SHORT).show();
        salir();
    }
    public void salir(){
        //me voy a la lista de informes
    getActivity().finish();
        Intent intento1 = new Intent(getActivity(), NavigationDrawerActivity.class);
        startActivity(intento1);
       // NavHostFragment.findNavController(this).navigate(R.id.action_selclientetolistacompras,bundle);


    }
    public void guardarMuestra(){
        try {
            //primero valido
            // InformeCompraDetalle detalle=new InformeCompraDetalle();
            //detalle=mBinding.getNuevaMuestra();

            //Creo el informe en nuevo informe y lo busco aqui

            int idInformeNuevo=0;
            //veo si ya existe el informe o hay que crearlo
            if (mViewModel.numMuestra == 1 || mViewModel.getIdInformeNuevo() <= 0) {
                //busco el consecutivo

                // int consecutivo=mViewModel.consecutivo;


               // mViewModel.informe.setConsecutivo(consecutivo);
               // Log.d(TAG,"el consecutivo es "+consecutivo+"--"+dViewModel.productoSel.clienteSel);

                mViewModel.informe.setVisitasId(mViewModel.visita.getId());
                mViewModel.informe.setPlantasId(dViewModel.productoSel.plantaSel);
                mViewModel.informe.setPlantaNombre(dViewModel.productoSel.plantaNombre);
                mViewModel.informe.setClienteNombre(dViewModel.productoSel.clienteNombre);
                mViewModel.informe.setClientesId(dViewModel.productoSel.clienteSel);
                mViewModel.informe.setEstatusSync(0);
                mViewModel.informe.setEstatus(1);

                 idInformeNuevo = (int) mViewModel.guardarInforme();
                Log.d(TAG, "se creo el informe" + idInformeNuevo);
                mViewModel.informe.setId(idInformeNuevo);
                mViewModel.setIdInformeNuevo(idInformeNuevo);
            }


            int nuevoid=dViewModel.insertarMuestra( mViewModel.getIdInformeNuevo(),mViewModel.numMuestra);
            //guardo la muestra
            if (nuevoid > 0) {
                //si ya se guardó lo agrego en la lista de compra
                ListaDetalleViewModel lcviewModel = new ViewModelProvider(this).get(ListaDetalleViewModel.class);
              //  Log.d(TAG,"qqqqqqqqqqqqqqq"+idlista+"--"+idDetalle);
                lcviewModel.comprarMuestraPepsi(dViewModel.productoSel.compraSel,dViewModel.productoSel.compradetalleSel,sdfcodigo.format(dViewModel.icdNuevo.getCaducidad()),dViewModel.productoSel.nombreTipoMuestra);

            }



        }catch (Exception ex){
            ex.printStackTrace();
            Log.e(TAG,ex.getMessage());
            Toast.makeText(getActivity(), "No se pudo guardar la muestra", Toast.LENGTH_LONG).show();

        }

    }
    public void guardarInforme(){
        mViewModel.insertarInfdeTemp();
    }
    public void actualizarInforme(){
        mViewModel.actualizarInforme();
    }
    public void limpiarTablTemp(){
        mViewModel.eliminarTblTemp();
    }
    public void avanzarPregunta(int sig){
        //busco el siguiente
        LiveData<Reactivo> nvoReac = dViewModel.buscarReactivo(sig);
        nvoReac.observe(getViewLifecycleOwner(), new Observer<Reactivo>() {
            @Override
            public void onChanged(Reactivo reactivo) {
                DetalleProductoFragment nvofrag = new DetalleProductoFragment(reactivo);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
// Definir una transacción
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
// Remplazar el contenido principal por el fragmento
                fragmentTransaction.replace(R.id.continf_fragment, nvofrag);
                fragmentTransaction.addToBackStack(null);
// Cambiar
                fragmentTransaction.commit();
            }
        });
    }
        public SpeechRecognizer grabarVoz(){

            SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getActivity());
            final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            speechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle bundle) {

                }

                @Override
                public void onBeginningOfSpeech() {
                    textoint.setText("");
                    textoint.setHint("Escuchando...");
                }

                @Override
                public void onRmsChanged(float v) {

                }

                @Override
                public void onBufferReceived(byte[] bytes) {

                }

                @Override
                public void onEndOfSpeech() {
                    textoint.setHint("");
                }

                @Override
                public void onError(int i) {

                }

                @Override
                public void onResults(Bundle bundle) {
                    micbtn.setImageResource(R.drawable.ic_baseline_mic_none_24);
                    ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    textoint.setText(data.get(0));
                    textoint.setHint("");

                }

                @Override
                public void onPartialResults(Bundle bundle) {

                }

                @Override
                public void onEvent(int i, Bundle bundle) {

                }
            });

            micbtn.setOnTouchListener(new View.OnTouchListener() {


                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                        micbtn.setImageResource(R.drawable.ic_baseline_mic_24);
                        speechRecognizer.stopListening();
                    }
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                        micbtn.setImageResource(R.drawable.ic_baseline_mic_none_24);
                        speechRecognizer.startListening(speechRecognizerIntent);
                    }
                    return false;
                }
            });
            return  speechRecognizer;
        }

//        int consecutivo =1;
    //guardo en tabla temp
    public void guardarResp(){
        String valor=null;
        if(textoint!=null)
            valor=textoint.getText().toString();
        else {
            if(preguntaAct.getType().equals(CreadorFormulario.SELECTCAT)){
                CatalogoDetalle opcionsel=(CatalogoDetalle)spclientes.getSelectedItem();
                valor = opcionsel.getCad_idopcion()+"";
            }
            if(preguntaAct.getType().equals(CreadorFormulario.SELECTDES)) {
                DescripcionGenerica opcionsel = (DescripcionGenerica) spclientes.getSelectedItem();
                valor = opcionsel.getId()+"";
            }  if(preguntaAct.getType().equals(CreadorFormulario.PSELECT)) {
                valor=spclientes.getSelectedItemId()+" ";

            }


        }
        mViewModel.guardarResp(valor,preguntaAct.getNombreCampo(),preguntaAct.getTabla(),mViewModel.consecutivo);

            //es un select

    }


        String nombre_foto;

        public   void tomarFoto() {
            Activity activity=this.getActivity();
            Intent intento1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");

            String dateString = format.format(new Date());
            File foto=null;
            try{
                nombre_foto = "img_" + dateString + ".jpg";
                foto = new File(activity.getExternalFilesDir(null), nombre_foto);
                Log.e(TAG, "****"+foto.getAbsolutePath());
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(activity, "No se encontró almacenamiento externo", Toast.LENGTH_SHORT).show();


            }
            Uri photoURI = FileProvider.getUriForFile(activity,
                    "com.example.comprasmu.fileprovider",
                    foto);
            intento1.putExtra(MediaStore.EXTRA_OUTPUT, photoURI); //se pasa a la otra activity la referencia al archivo
            //intento1.putExtra("origen", origen);




            if(fotomos!=null) {

                startActivityForResult(intento1, REQUEST_CODE_TAKE_PHOTO);

            }

        }

        public void rotar(){

            if(AbririnformeFragment.getAvailableMemory(getActivity()).lowMemory)
            {
                Toast.makeText(getActivity(), "No hay memoria suficiente para esta accion", Toast.LENGTH_SHORT).show();

                return;
            }else
                RevisarFotoActivity.rotarImagen(getActivity().getExternalFilesDir(null) + "/" +nombre_foto,fotomos);
        }
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //    super.onActivityResult(requestCode, resultCode, data);
               Log.d(TAG,"vars"+requestCode +"--"+ resultCode);
            if ((requestCode == REQUEST_CODE_TAKE_PHOTO) && resultCode == RESULT_OK) {
                super.onActivityResult(requestCode, resultCode, data);
                File file = new File(getActivity().getExternalFilesDir(null), nombre_foto);
                if (file.exists()) {
                    if(requestCode == REQUEST_CODE_TAKE_PHOTO) {
                        //envio a la actividad dos para ver la foto
                        Intent intento1 = new Intent(getActivity(), RevisarFotoActivity.class);
                        intento1.putExtra("ei.archivo", nombre_foto);

                        textoint.setText(nombre_foto);
                        Bitmap bitmap1 = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null) + "/" + nombre_foto);
                        ComprasUtils cu=new ComprasUtils();
                        bitmap1=cu.comprimirImagen(getActivity().getExternalFilesDir(null) + "/" + nombre_foto);

                        fotomos.setImageBitmap(bitmap1);
                       // fotomos.setLayoutParams(new LinearLayout.LayoutParams(350,150));
                        fotomos.setVisibility(View.VISIBLE);

                        btnrotar.setVisibility(View.VISIBLE);
                        btnrotar.setFocusableInTouchMode(true);
                        btnrotar.requestFocus();


                    }


                }
                else{
                    Log.e(TAG,"Algo salió mal???");
                }


            } else if(requestCode==BackActivity.REQUEST_CODE)
    {
        super.onActivityResult(requestCode, resultCode, data);
        //capturé muestra
        if(resultCode==NUEVO_RESULT_OK) {
            Log.d(TAG,"ssssssssssssssssss"+data.getIntExtra(ARG_NUEVOINFORME, 0));
            // totalLista=data.getIntExtra(ARG_TOTALLISTA, 0);
            //muestro el de 3a muestra
           // if(Constantes.NM_TOTALISTA>=16)
              //  tercera.setVisible(View.VISIBLE);
            if(Constantes.productoSel!=null)
                {
                    dViewModel.productoSel = Constantes.productoSel;
                    //actualizo barra
                    ((ContinuarInformeActivity)getActivity()).actualizarProdSel( dViewModel.productoSel);
                    mViewModel.numMuestra++;
                    avanzarPregunta(23);
                }else
                Log.e(TAG,"Algo salió muy mal al elegir el producto");

            //lo busco y cargo


        }

    }else if(requestCode == REQUEST_CODEQR) {


              //  IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);
                IntentResult result =IntentIntegrator.parseActivityResult(resultCode, data);

                Log.d(TAG,"res del qr "+result.getContents());
                if(result != null) {

                    if(result.getContents() == null) {
                        Toast.makeText(getActivity(), "Scan cancelled", Toast.LENGTH_LONG).show();
                    }
                    else
                    {   /* Update the textview with the scanned URL result */
                        textoint.setText(result.getContents());
                        Toast.makeText(getActivity(), "Content: ${result.getContents()}",Toast.LENGTH_LONG ).show();
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

        //validar siglas
        public boolean validarSiglas(){
            if(dViewModel.productoSel.clienteNombre.toUpperCase().equals("PEPSI"))
                if(!textoint.getText().toString().equals("")){
                    String siglaslis=dViewModel.productoSel.siglas;
                    if(dViewModel.productoSel.siglas!=null&&!siglaslis.toUpperCase().equals(textoint.getText().toString().toUpperCase())){
                        Toast.makeText(getActivity(), getString(R.string.error_siglas), Toast.LENGTH_LONG).show();
                        return false;
                    }
                }

            return true;
        }
        Date fechacad = null;
       boolean res;
        //validar siglas
        public boolean validarFecha(){
            Date hoy=new Date();

            if (!textoint.getText().toString().equals("")) {

                try {
                    fechacad = sdf.parse(textoint.getText().toString());
                } catch (ParseException e) {

                    Toast.makeText(getActivity(), getString(R.string.error_fecha_formato), Toast.LENGTH_LONG).show();
                    return false;
                }
                if (!sdf.format(fechacad).equals(textoint.getText().toString()))
                {
                    Toast.makeText(getActivity(), getString(R.string.error_fecha_formato), Toast.LENGTH_LONG).show();
                    return false;
                }
                if (dViewModel.productoSel.clienteNombre.toUpperCase().equals("PEPSI")) {

                    Calendar cal = Calendar.getInstance(); // Obtenga un calendario utilizando la zona horaria y la configuración regional predeterminadas
                    cal.setTime(hoy);
                    cal.add(Calendar.DAY_OF_MONTH, +30);
                    Log.d(TAG,"tipo tienda ------------"+tipoTienda);
                    if (fechacad.getTime()<=hoy.getTime()) { //ya caducó fechacad>=hoy


                        if (tipoTienda == 2||tipoTienda == 3) {
                            Toast.makeText(getActivity(), getString(R.string.error_fecha_caduca), Toast.LENGTH_LONG).show();
                            return false;
                        }
                        else {
                            //ImageButton bton=(ImageButton)view;
                            // bton.setSupportButtonTintList(ContextCompat.getColorStateList(getActivity(), R.color.botonvalido));
                            //bton.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.botonvalido));

                            return true;
                        }

                    }else if (fechacad.compareTo(cal.getTime())<0) { //hoy+30>fechacad
                        Toast.makeText(getActivity(), getString(R.string.error_fecha_caduca_prox), Toast.LENGTH_LONG).show();

                        return false;
                    } else

                        return true;


                } else if (fechacad.getTime()<=hoy.getTime()) { //ya caducó fechacad>=hoy

                    Toast.makeText(getActivity(), getString(R.string.error_fecha_caduca), Toast.LENGTH_LONG).show();
                    return false;
                }else{
                    if (dViewModel.productoSel.clienteNombre.toUpperCase().equals("PEñAFIEL")) {
                        //busco que no haya otra muestra con la misma fecha en el muestreo
                      if(this.buscarMuestraCodigoPeñafiel(dViewModel.productoSel,fechacad))
                          Toast.makeText(getActivity(), getString(R.string.error_codigo_repetido), Toast.LENGTH_LONG).show();

                        return false;

                    }
                }

                return true;
            }
            return false;
        }
        public boolean validarCodigoprod(){
            boolean resp;
            if(!textoint.getText().toString().equals("")){
                if(dViewModel.productoSel.clienteNombre.toUpperCase().equals("PEPSI")){
                    String arrecodigos[]=dViewModel.productoSel.codigosnop.split(";");
                    if(arrecodigos!=null&&arrecodigos.length>0) {
                        List<String> lista = Arrays.asList(arrecodigos);
                        if (lista.contains(textoint.getText().toString())) {
                            Toast.makeText(getActivity(), getString(R.string.error_codigo_repetido)+"*", Toast.LENGTH_LONG).show();

                            resp=false;
                            return resp;
                        }else{

                            //busco si hay otra muestra == y si tiene el mismo codigo
                            res=buscarMuestraCodigo(dViewModel.productoSel,textoint.getText().toString(),fechacad);

                                    if(res){
                                        Toast.makeText(getActivity(), getString(R.string.error_codigo_repetido), Toast.LENGTH_LONG).show();
                                        resp=false;
                                        return resp;

                                    }

                        }
                    }

                }
                resp=true;
                return resp;
            }
            resp=false;
            return resp;

        }

        public boolean buscarMuestraCodigo(NuevoDetalleViewModel.ProductoSel productosel,String codigonvo,Date caducidadnva){
            //busco en el mismo informe
            return dViewModel.buscarMuestraCodigo(Constantes.INDICEACTUAL,dViewModel.productoSel.plantaSel,productosel,codigonvo,caducidadnva,getViewLifecycleOwner());

        }
        //si true ya existe un codigo igual
        public boolean buscarMuestraCodigoPeñafiel(NuevoDetalleViewModel.ProductoSel productosel,Date caducidadnva){
            //busco en el mismo informe
            return dViewModel.buscarMuestraCodigo(Constantes.INDICEACTUAL,dViewModel.productoSel.plantaSel,productosel,"",caducidadnva,getViewLifecycleOwner());

        }
        @Override
        public void onDestroy() {

                super.onDestroy();


            if(sspeechRecognizer!=null)
                sspeechRecognizer.destroy();
        }
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == RecordAudioRequestCode && grantResults.length > 0 ){
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(getActivity(),"Permission Granted",Toast.LENGTH_SHORT).show();
            }
            if (requestCode == 100) {
                RuntimePermissionUtil.onRequestPermissionsResult(grantResults, new RPResultListener() {
                    @Override
                    public void onPermissionGranted() {
                        if ( RuntimePermissionUtil.checkPermissonGranted(getActivity(), cameraPerm)) {
                            //   restartActivity();
                        }
                    }

                    @Override
                    public void onPermissionDenied() {
                        // Do nothing
                    }
                });
            }
        }

        public void iniciarLecQR(){
            IntentIntegrator integrator  =new  IntentIntegrator ( getActivity() ).forSupportFragment(DetalleProductoFragment.this);
            integrator.setRequestCode(REQUEST_CODEQR);
          //  integrator.setOrientationLocked(false);
            Log.d(TAG, "inciando scanner");
            integrator.initiateScan();
        }
    public void verListaCompra(int nummuestra){
        /* b2undle.putString("plantaNombre", listaSeleccionable.get(i).getNombre());*/
        /*   NavHostFragment.findNavController(this).navigate(R.id.action_selclientetolistacompras,bundle);
         */
        //NavHostFragment.findNavController(this).navigate(R.id.action_lista compra);
        Intent intento1=new Intent(getActivity(), BackActivity.class);

        //ya existe el informe
        intento1.putExtra(DetalleProductoFragment.ARG_NUEVOINFORME,mViewModel.getIdInformeNuevo() );
        intento1.putExtra(BackActivity.ARG_FRAGMENT,BackActivity.OP_LISTACOMPRA);
        intento1.putExtra("ciudadSel",mViewModel.visita.getCiudadId());
        intento1.putExtra("ciudadNombre",mViewModel.visita.getCiudad());
       // spclientes = root.findViewById(1001);

        intento1.putExtra(ARG_CLIENTESEL,mViewModel.clienteSel);
        intento1.putExtra(DetalleProductoFragment.NUMMUESTRA,nummuestra );

        startActivityForResult(intento1, BackActivity.REQUEST_CODE);
        //  startActivity(intento1);

    }
    public static void subirFotos(Activity activity, InformeEnvio informe){
        //las imagenes
        for(ImagenDetalle imagen:informe.getImagenDetalles()){
            //subo cada una
            Intent msgIntent = new Intent(activity, SubirFotoService.class);
            msgIntent.putExtra(SubirFotoService.EXTRA_IMAGE_ID, imagen.getId());
            msgIntent.putExtra(SubirFotoService.EXTRA_IMG_PATH,imagen.getRuta());
            msgIntent.putExtra(SubirFotoService.EXTRA_INDICE,informe.getVisita().getIndice());
            // Constantes.INDICEACTUAL
            Log.d(TAG,"subiendo fotos"+activity.getLocalClassName());
            activity.startService(msgIntent);
            //cambio su estatus a subiendo



        }

    }

    private void checkPermission() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
            }
        }
        @Override
        public void onPause() {
            super.onPause();


        }
        @Override
        public void onResume() {
            super.onResume();


        }
     public int getNumPregunta(){
            return preguntaAct.getId();
     }


}