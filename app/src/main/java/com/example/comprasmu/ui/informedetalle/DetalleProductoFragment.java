package com.example.comprasmu.ui.informedetalle;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import com.example.comprasmu.data.modelos.InformeTemp;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ProductoExhibido;
import com.example.comprasmu.data.modelos.Reactivo;
import com.example.comprasmu.data.remote.InformeEnvio;
import com.example.comprasmu.services.SubirFotoService;
import com.example.comprasmu.ui.BackActivity;
import com.example.comprasmu.ui.RevisarFotoActivity;
import com.example.comprasmu.ui.informe.NuevoinformeViewModel;
import com.example.comprasmu.ui.listacompras.SelClienteFragment;
import com.example.comprasmu.ui.listadetalle.ListaCompraFragment;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.ui.visita.AbririnformeFragment;
import com.example.comprasmu.utils.CampoForm;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;
import com.example.comprasmu.utils.DetalleInfView;
import com.example.comprasmu.utils.Preguntasino;
import com.example.comprasmu.utils.RPResultListener;
import com.example.comprasmu.utils.RuntimePermissionUtil;
import com.example.comprasmu.utils.ui.LoadingDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import com.example.comprasmu.utils.micamara.MiCamaraActivity;
import static android.app.Activity.RESULT_OK;
import static com.example.comprasmu.ui.listacompras.TabsFragment.ARG_CLIENTESEL;

/***clase para mostrar los campos que faltan de capturar de la muestra uno por pantalla***/
/******* informe de pepsi ya que los otros tienen otra logica de negocio***/
// en guardar cliente se envia a los otros informes
public class DetalleProductoFragment extends Fragment {

    private NuevoinformeViewModel mViewModel;
    private NuevoDetalleViewModel dViewModel;

    private List<CatalogoDetalle> tomadoDe;
    private List<CatalogoDetalle> atributos;
    private List<CatalogoDetalle> causas;
    public List<CatalogoDetalle> causassust;
    private static final String TAG="DETALLEPRODUCTOFRAG";
    private static final int REQUEST_CODEQR = 341;
    SimpleDateFormat sdf;
    SimpleDateFormat sdfcodigo;
    View root;

    private Reactivo preguntaAct;

    LinearLayout svprin;
    boolean isEdicion;
    LoadingDialog loadingDialog ;
    List<DescripcionGenerica> clientesAsignados;

    ComprasLog compraslog;
    NuevoDetalleViewModel.ProductoSel prodSel;
    public static  int REQUEST_CODE_TAKE_PHOTO=1;
    SpeechRecognizer sspeechRecognizer;
    private int tipoTienda;
    public static final int NUEVO_RESULT_OK =102 ;
    // public static final Integer RecordAudioRequestCode = 1;
    //  private static final String cameraPerm = Manifest.permission.CAMERA;
    public final static String ARG_NUEVOINFORME="comprasmu.ni_idinforme";
    public static String NUMMUESTRA="comprasmu.ni.nummuestra";
    public final static String ARG_PREGACT="comprasmu.ni_pregact";
    public final static String ARG_ESEDI="comprasmu.ni_esedi";
    InformeTemp  ultimares;

    ListaDetalleViewModel lcviewModel;
    private int plantaSel;
    private String NOMBREPLANTASEL;
    private long lastClickTime = 0;
    private boolean yaestoyProcesando=false;
    public int estatusPepsi, estatusPen,estatusElec,estatusJum;
    DetalleInfView preguntaview;

    public DetalleProductoFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // setHasOptionsMenu(true);
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdfcodigo = new SimpleDateFormat("dd-MM-yy");
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel =
                new ViewModelProvider(requireActivity()).get(NuevoinformeViewModel.class);
        lcviewModel = new ViewModelProvider(this).get(ListaDetalleViewModel.class);

        dViewModel=new ViewModelProvider(requireActivity()).get(NuevoDetalleViewModel.class);
        root = inflater.inflate(R.layout.fragment_generic2, container, false);
        /**llegan los datos del producto el cliente y la planta seleccionada
         * desde la lista de compra
         */
        compraslog=ComprasLog.getSingleton();

        try{

            svprin = root.findViewById(R.id.fgllcontentmain);

            //   mViewModel.cargarCatsContinuar();

            estatusPepsi=mViewModel.visita.getEstatusPepsi(); //para saber si puede comprar pepsi
            estatusPen=mViewModel.visita.getEstatusPen();
            estatusElec=mViewModel.visita.getEstatusElec();
            estatusJum=mViewModel.visita.getEstatusJum();
            Log.d(TAG,"estatus "+estatusPepsi+"--"+estatusPen+"--"+estatusElec+"--"+estatusJum);
            crearPregunta();



        } catch (Exception e) {
            e.printStackTrace();
        }

        return root;
    }

    public void crearPregunta(){
        int num_pregact=0;
        Log.d(TAG,"creando preg"+mViewModel.numMuestra);
        if(preguntaAct==null)

        if (getArguments() != null) {

            num_pregact = getArguments().getInt(ARG_PREGACT);
            this.isEdicion = getArguments().getBoolean(ARG_ESEDI);
            Log.d("que onda",num_pregact+"--");
            preguntaAct= dViewModel.buscarReactivoSimpl(num_pregact);
        }

        try {

            //busco preguntaAct



            if(preguntaAct==null){
                return ;
            }

        Log.d(TAG,"creando fragment "+preguntaAct.getNombreCampo());
        dViewModel.reactivoAct=preguntaAct.getId();

        //si es la misma
        //reviso si es edicion o es nueva
        if(preguntaAct.getTabla().equals("I"))
            mViewModel.numMuestra=0;
        ((ContinuarInformeActivity)getActivity()).noSalir(false);
        if(this.preguntaAct!=null)
            ultimares=dViewModel.buscarxNombreCam(this.preguntaAct.getNombreCampo(),mViewModel.numMuestra);
        //   Log.d(TAG,"------"+Constantes.NM_TOTALISTA+"---"+mViewModel.consecutivo);
        Log.d(TAG,"mmmmmmmmmmm"+preguntaAct.getId());
        Log.e(TAG, "1-------------cons" + Constantes.DP_CONSECUTIVO);
        if(ultimares!=null) {    //es edicion
            isEdicion = true;
            if(preguntaAct.getId()==47||preguntaAct.getId()==48){
                //no puedo modificar  avanzo a la siguiente

                preguntaAct=dViewModel.buscarReactivoxId(preguntaAct.getSigId());
                InformeTemp inft=dViewModel.buscarxNombreCam("informeid");
                if(inft!=null) {
                    mViewModel.setIdInformeNuevo(Integer.parseInt(inft.getValor()));
                    mViewModel.consecutivo = inft.getConsecutivo();
                    Constantes.DP_CONSECUTIVO=mViewModel.consecutivo;
                }
                //busco el informe

                ultimares=null;
                isEdicion=false;

            }
        }
        else
            //if(this.preguntaAct.getId()==2||this.preguntaAct.getId()==3||this.preguntaAct.getId()==5)
            isEdicion=false;

        Log.d(TAG,"mmmmmmmmmmm"+isEdicion);

        if(isEdicion) {
            //aceptar.setEnabled(true);

            mViewModel.consecutivo=ultimares.getConsecutivo();
            Constantes.DP_CONSECUTIVO=mViewModel.consecutivo;
            //  ya lo busco en la actividad
            //   InformeTemp inf= dViewModel.buscarxNombreCam("numMuestra");
            //     mViewModel.numMuestra=inf==null?0:Integer.parseInt(inf.getValor());
            //busco el cliente
            InformeTemp inf= dViewModel.buscarxNombreCam("clientesId");
            if(inf!=null) {
                int clienteSel = Integer.parseInt(inf.getValor());
                mViewModel.clienteSel=clienteSel;


            }
            inf= dViewModel.buscarxNombreCam("clienteNombre");
            if(inf!=null) {
                Constantes.ni_clientesel=inf.getValor();

            }
            inf= dViewModel.buscarxNombreCam("plantasId");
            if(inf!=null) {
                Constantes.ni_plantasel=Integer.parseInt(inf.getValor());

            }
            inf= dViewModel.buscarxNombreCam("plantaNombre");
            if(inf!=null) {
                Constantes.ni_plantanombre=inf.getValor();

            }

            if(preguntaAct.getId()==5)//ticket de compra
            {
                ((ContinuarInformeActivity)getActivity()).noSalir(true);
            }
            if(preguntaAct.getId()>=23&&preguntaAct.getId() !=126) //ya tengo producto voy en siglas
            {
                //  Constantes.ni_clientesel=opcionsel.getNombre();
                //int consecutivo=mViewModel.getConsecutivo(valor);
                // Log.d(TAG,"genere cons="+consecutivo);
                mViewModel.informe=new InformeCompra();
                // nviewModel.informe.setClienteNombre(opcionsel.getNombre());
                //  nviewModel.informe.setClientesId(ultimares.getValor());
                mViewModel.informe.setConsecutivo(ultimares.getConsecutivo());
                mViewModel.consecutivo=ultimares.getConsecutivo();
                Constantes.DP_CONSECUTIVO=mViewModel.consecutivo;
                //  mViewModel.consecutivo=ultimares.getConsecutivo();
                dViewModel.fromTemp(); //guardo datos del producto selec
                ((ContinuarInformeActivity)getActivity()).actualizarCliente(mViewModel.informe);

                ((ContinuarInformeActivity)getActivity()).actualizarProdSel(dViewModel.productoSel);
            }
            if (preguntaAct.getId() >= 25&&preguntaAct.getId() !=47&&preguntaAct.getId() !=126) {//si compro prod
                InformeTemp resp=dViewModel.buscarxNombreCam("codigo",mViewModel.numMuestra);
                ((ContinuarInformeActivity)getActivity()).actualizarCodProd(resp.getValor());

            }
            if(dViewModel.productoSel!=null)
            { getAtributos();
                getTomadoDe();}
            if (preguntaAct.getId() >= 26&&preguntaAct.getId()!=47&&preguntaAct.getId() !=126) { //si hay prod


                InformeTemp resp=dViewModel.buscarxNombreCam("origen",mViewModel.numMuestra);
                String valor="";
                int opcion=Integer.parseInt(resp.getValor());
                //busco en el cat
                for(CatalogoDetalle cat:tomadoDe){
                    if(cat.getCad_idopcion()==opcion)
                        valor=cat.getCad_descripcionesp();
                }
                Constantes.VarDetalleProd.tomadode =valor;

                ((ContinuarInformeActivity)getActivity()).actualizarAtributo1();

            }
            if (preguntaAct.getId() >= 33&&preguntaAct.getId()!=47&&preguntaAct.getId() !=126) {
                InformeTemp resp=dViewModel.buscarxNombreCam("atributoa",mViewModel.numMuestra);
                String valor="";
                if(resp!=null) {
                    int opcion = Integer.parseInt(resp.getValor());
                    //busco en el cat
                    for (CatalogoDetalle cat : atributos) {
                        if (cat.getCad_idopcion() == opcion)
                            valor = cat.getCad_descripcionesp();
                    }
                    Constantes.VarDetalleProd.nvoatra = valor;
                    ((ContinuarInformeActivity) getActivity()).actualizarAtributo1();
                }

            }
            if (preguntaAct.getId() >= 36&&preguntaAct.getId()!=47&&preguntaAct.getId() !=126) {
                InformeTemp resp=dViewModel.buscarxNombreCam("atributob",mViewModel.numMuestra);
                String valor="";
                if(resp!=null) {
                    int opcion = Integer.parseInt(resp.getValor());
                    //busco en el cat
                    for (CatalogoDetalle cat : atributos) {
                        if (cat.getCad_idopcion() == opcion)
                            valor = cat.getCad_descripcionesp();
                    }
                    Constantes.VarDetalleProd.nvoatrb = resp == null ? "" : valor;
                    ((ContinuarInformeActivity) getActivity()).actualizarAtributo2();
                }

            }
            if (preguntaAct.getId() >= 39&&preguntaAct.getId()!=47&&preguntaAct.getId() !=126) {
                InformeTemp resp=dViewModel.buscarxNombreCam("atributoc",mViewModel.numMuestra);
                String valor="";
                if(resp!=null) {
                    int opcion = Integer.parseInt(resp.getValor());
                    //busco en el cat
                    for (CatalogoDetalle cat : atributos) {
                        if (cat.getCad_idopcion() == opcion)
                            valor = cat.getCad_descripcionesp();
                    }
                    Constantes.VarDetalleProd.nvoatrc = resp == null ? "" : valor;
                    ((ContinuarInformeActivity) getActivity()).actualizarAtributo2();
                }

            }
            if (preguntaAct.getId() >= 90&&preguntaAct.getId()!=47&&preguntaAct.getId() !=126) {
                InformeTemp resp=dViewModel.buscarxNombreCam("atributod",mViewModel.numMuestra);
                String valor="";
                if(resp!=null) {
                    int opcion = Integer.parseInt(resp.getValor());
                    //busco en el cat
                    for (CatalogoDetalle cat : atributos) {
                        if (cat.getCad_idopcion() == opcion)
                            valor = cat.getCad_descripcionesp();
                    }
                    Constantes.VarDetalleProd.nvoatrd = resp == null ? "" : valor;
                    ((ContinuarInformeActivity) getActivity()).actualizarAtributo3();
                }

            }
            //busco el total de prods en la lista
            if(Constantes.NM_TOTALISTA==0) {
                InformeTemp resp = dViewModel.buscarxNombreCam("totalLista");
                String valor = "";
                if (resp != null) {
                    Constantes.NM_TOTALISTA = Integer.parseInt(resp.getValor());

                }
            }

        }


        if (preguntaAct.getId() == 2) {

            mViewModel.consecutivo=Constantes.DP_CONSECUTIVO;
            mViewModel.numMuestra = 1;
            Log.e(TAG, "-------------cons" + mViewModel.consecutivo);

        }

        if (preguntaAct.getId() == 3) {
            mViewModel.consecutivo=Constantes.DP_CONSECUTIVO;
            mViewModel.numMuestra = 2;
        }
        if (preguntaAct.getId() == 4) {
            mViewModel.consecutivo=Constantes.DP_CONSECUTIVO;
            mViewModel.numMuestra = 3;
        }
        //}
        // if(preguntaAct.getId()==2||preguntaAct.getId() == 3||preguntaAct.getId() == 4) //estot en siglas y es una nueva muestra
        //     mViewModel.numMuestra=mViewModel.numMuestra+1;
        //reviso que no haya muesmtras guardadas de ese informe :O como se que es uno nuevo
        //  Log.e(TAG,"-------------nummuestras:"+mViewModel.numMuestra);
        // }
        //para saber si el detalle ya existe y el informe
           /* if( mViewModel.getIdInformeNuevo()==0)
                InformeTemp inf= dViewModel.buscarxNombreCam("clienteNombre");
            mViewModel.getIdInformeNuevo(), dViewModel.getIddetalleNuevo()*/

        crearFormulario();

        if(isEdicion)
            preguntaview.aceptarSetEnabled(true);
        else
            preguntaview.aceptarSetEnabled(false);
        if(preguntaAct.getNombreCampo().equals("ticket_compra")) {
            PregBorraFoto preg=new PregBorraFoto();
            preguntaview.setNopermiso(preg);
        }
        if(preguntaAct.getType().equals(CreadorFormulario.FECHAMASK)){
            preguntaview.miaddTextChangedListener();
        }else
        if(preguntaview.hayTextoInt()&&preguntaAct.getId()!=7){ //los comentarios no son obligatorios
            preguntaview.miBotonTextWatcher();

        }
        if(preguntaAct.getId()==7){ //los comentarios no son obligatorios
            //  textoint.addTextChangedListener(new MayusTextWatcher());
          //veo si ya tengo informe
            mViewModel.informe=mViewModel.getInformeCompra(mViewModel.getIdInformeNuevo());
        if( mViewModel.informe!=null) {
            mViewModel.consecutivo = mViewModel.informe.getConsecutivo();
            Constantes.DP_CONSECUTIVO = mViewModel.consecutivo;

            ((ContinuarInformeActivity) getActivity()).actualizarCliente(mViewModel.informe);

            //   ((ContinuarInformeActivity)getActivity()).actualizarProdSel(dViewModel.productoSel);
        }
            preguntaview.setMaxLengthFilters(300);
            preguntaview.aceptarSetEnabled(true);
        }

        if(preguntaAct.getId()==7&&mViewModel.informe.isSinproducto()) {
            preguntaview.aceptarSetEnabled(false);
            preguntaview.miBotonTextWatcher();
        }
        if(preguntaAct.getId()==48)
            preguntaview.aceptarSetEnabled(true);


        if(preguntaview.haySPClientes()){

            preguntaview.setSpClientesListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // Log.d(TAG,"está aqui************************");
                    preguntaview.aceptarSetEnabled(true);
                    if(preguntaAct.getId()==33||preguntaAct.getId()==36||preguntaAct.getId()==39) {

                        CatalogoDetalle opcion = (CatalogoDetalle) parentView.getSelectedItem();
                        //  Log.d(TAG,"está aqui************************"+opcion.getCad_idopcion());
                        if (opcion.getCad_idopcion()==0)
                            preguntaview.aceptarSetEnabled(false);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });
        }


        if(preguntaview.hayPreguntaSino()){
            preguntaview.setPregSINoOnChange(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    preguntaview.aceptarSetEnabled(true);
                }
            });
        }
        if(preguntaview.hayRespGen()){

            preguntaview.setRadioGrupListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    preguntaview.aceptarSetEnabled(true);
                }
            });
        }

        preguntaview.onclickAceptar(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preguntaview.aceptarSetEnabled(false);
                long currentClickTime=SystemClock.elapsedRealtime();
                // preventing double, using threshold of 1000 ms
                if (currentClickTime - lastClickTime < 5500){
                    //  Log.d(TAG,"doble click :("+lastClickTime);
                    return;
                }

                lastClickTime = currentClickTime;

                if(preguntaAct.getNombreCampo().equals("clientesId")){
                    guardarCliente();
                }
                else
                    siguiente();

            }
        });


        prodSel=dViewModel.productoSel;
        if(preguntaAct.isBotonMicro()) {

            // micbtn=root.findViewById(R.id.btnmicsiglas);
           /*     sspeechRecognizer = grabarVoz();
                micbtn.setVisibility(View.VISIBLE);*/
        }
        if(preguntaAct.getId()==7){
            //cambio el boton a finalizar y muestro alerta
            preguntaview.setEnviarButton(getString(R.string.enviar));
        }
        Log.d(TAG,"tipo tienda -----------*"+Constantes.DP_TIPOTIENDA);
        tipoTienda=Constantes.DP_TIPOTIENDA;
        } catch (Exception e) {
            e.printStackTrace();
            compraslog.grabarError(TAG,"crearPregunta",e.getMessage());
        }
    }
    //btnaifotoexhibido
    public void crearFormulario(){
        preguntaview=new DetalleInfView(getContext());
        preguntaview.setPreguntaAct(preguntaAct);

        if(isEdicion) {
            preguntaview.setUltimares(ultimares);
            preguntaview.setEdicion(isEdicion);
        }

        if(preguntaAct.getType().equals(CreadorFormulario.SELECTCAT)||preguntaAct.getType().equals(CreadorFormulario.SELECTDES)){
            switch (preguntaAct.getNombreCampo()){
                case Contrato.TablaInformeDet.ATRIBUTOA:case Contrato.TablaInformeDet.ATRIBUTOB: case Contrato.TablaInformeDet.ATRIBUTOC:
                case Contrato.TablaInformeDet.ATRIBUTOD:
                    getAtributos();

                    preguntaview.setAtributos(atributos);
                    break;
                case Contrato.TablaInformeDet.ORIGEN:
                    getTomadoDe();
                    preguntaview.setAtributos(tomadoDe);
                    break;

                case "clientesId":
                    cargarClientes();
                    preguntaview.setClientesAsignados( clientesAsignados);
                    break;

                case Contrato.TablaInformeDet.CAUSA_SUSTITUCIONID:
                    getCausasSust();
                    preguntaview.setAtributos(causassust); //el campo es del tipo selectcatalogo
                    break;
            }

        }
        if( preguntaAct.getType().equals("agregarImagen")) {

            preguntaview.setTomarFoto( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tomarFoto();
                }
            });

            preguntaview.onclickRotar(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rotar();
                }
            });

            if(isEdicion){
                // Bitmap bitmap1 = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null) + "/" + nombre_foto);
                //ComprasUtils cu=new ComprasUtils();
                // bitmap1=cu.comprimirImagen(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + ultimares.getValor());
                if(ultimares.getValor().equals("0")){
                    //lo manejo en el view
                }else {
                    Bitmap bitmap1 = ComprasUtils.decodeSampledBitmapFromResource(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + ultimares.getValor(), 100, 100);

                    preguntaview.setImageView(bitmap1);
                }

            }
            // btnrotar.setVisibility(View.VISIBLE);


        }
        if(preguntaAct.getType().equals("botonqr")) {
            preguntaview.onclickBotonQr(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iniciarLecQR();
                }

            });
        }
        if(Contrato.TablaInformeDet.causa_nocompra.equals(preguntaAct.getNombreCampo())) {
            //busco en el catalogo
            this.getCausas();

            HashMap<Integer, String> registro=new HashMap<>();
            for(CatalogoDetalle valores:causas) {
                registro.put(valores.getCad_idopcion(),valores.getCad_descripcionesp());
            }
            // registro.put(2,"NO HAY CODIGO");

            preguntaview.setCausas(registro);
        }
       /* if(preguntaAct.getType().equals(CreadorFormulario.PREGUNTASINO)) {
            campo.value

        }*/

        preguntaview.crearFormulario();

        svprin.addView(preguntaview);

    }
    public void cargarClientes() {
        Integer[] clientesprev=dViewModel.tieneInforme(mViewModel.visita);
        //ahora son plantas
        //if (Constantes.clientesAsignados == null||Constantes.clientesAsignados.size()<1){
        //  List<ListaCompra> data=lcviewModel.cargarClientesSimpl(Constantes.CIUDADTRABAJO);
        List<ListaCompra> listacomp= lcviewModel.cargarPestanasxEtaSimp(Constantes.CIUDADTRABAJO);
        clientesAsignados = convertirListaaPlantas(listacomp, clientesprev);
        Log.d(TAG, "*regresó de la consulta de clientes " + clientesAsignados.size());


        // if(clientesprev!=null)
        //      Log.d(TAG, "regresó de la consulta de clientes " + clientesprev.length + "--" + data.size());



        //  }
        //  else
        //    campo.selectdes= Constantes.clientesAsignados;
    }



   /* public  List<DescripcionGenerica> convertirListaaClientesE(List<ListaCompra> lista, Integer[] clientesprev){
        int i=0;
        List<DescripcionGenerica> mapa=new ArrayList<>();
        List<Integer> coninf;
        if( clientesprev!=null) {
            Log.d(TAG, "*estoy aqui" + clientesprev.length);
          coninf=Arrays.asList(clientesprev);
        }

        if(lista!=null)
            for (ListaCompra listaCompra: lista ) {
                if(estatusPepsi==0&&listaCompra.getClientesId()==4)
                continue;
                if(estatusPen==0&&listaCompra.getClientesId()==5)
                    continue;
                if(estatusElec==0&&listaCompra.getClientesId()==6)
                    continue;
                DescripcionGenerica item=new DescripcionGenerica();
                Log.d(TAG,"-estoy aqui"+listaCompra.getClientesId());
                if( clientesprev!=null)
                    if(Arrays.asList(clientesprev).contains(listaCompra.getClientesId()))
                    {     //&&IntStream.of(clientesprev).anyMatch(n -> n == listaCompra.getClientesId()))
                        Log.d(TAG,"estoy aqui"+Arrays.asList(clientesprev));
                        continue;}

                item.setId(listaCompra.getClientesId());
                item.setNombre(listaCompra.getClienteNombre());
                mapa.add(item);

            }
        return mapa;
    }*/

    public  List<DescripcionGenerica> convertirListaaPlantas(List<ListaCompra> lista, Integer[] clientesprev){
        int i=0;
        List<DescripcionGenerica> mapa=new ArrayList<>();
        List<Integer> coninf;
        if( clientesprev!=null) {
            Log.d(TAG, "*estoy aqui" + clientesprev.length);
            coninf=Arrays.asList(clientesprev);
        }

        if(lista!=null)
            for (ListaCompra listaCompra: lista ) {
                if(estatusPepsi==0&&listaCompra.getClientesId()==4)
                    continue;
                if(estatusPen==0&&listaCompra.getClientesId()==5)
                    continue;
                if(estatusElec==0&&listaCompra.getClientesId()==6)
                    continue;
                if(estatusJum==0&&listaCompra.getClientesId()==7)
                    continue;
                DescripcionGenerica item=new DescripcionGenerica();
                Log.d(TAG,"-estoy aqui"+listaCompra.getClientesId());
                if( clientesprev!=null)
                    if(Arrays.asList(clientesprev).contains(listaCompra.getPlantasId()))
                    {     //&&IntStream.of(clientesprev).anyMatch(n -> n == listaCompra.getClientesId()))
                        Log.d(TAG,"estoy aqui"+Arrays.asList(clientesprev));
                        continue;}

                item.setId(listaCompra.getPlantasId());
                item.setNombre(listaCompra.getClienteNombre()+" "+listaCompra.getPlantaNombre());
                //pongo solo el nombre de la planta
                item.setDescripcion(listaCompra.getPlantaNombre());
                //pareja idcliente;nombreclientes
                item.setDescripcion2(listaCompra.getClientesId()+";"+listaCompra.getClienteNombre());
                mapa.add(item);

            }
        return mapa;
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
    public void getAtributos(){
        //  Log.d(TAG,"buscando atributos"+dViewModel.productoSel.empaque+"--"+dViewModel.productoSel.idempaque+"--"+dViewModel.productoSel.clienteSel);
        dViewModel.cargarCatalogos(dViewModel.productoSel.empaque,dViewModel.productoSel.idempaque,dViewModel.productoSel.clienteSel);
        List<Atributo> atrs=dViewModel.satributos;
        atributos=new ArrayList<>();
        CatalogoDetalle selop=new CatalogoDetalle();
        selop.setCad_idopcion(0);
        selop.setCad_descripcionesp(getString(R.string.seleccione_opcion));
        atributos.add(selop);
        atributos.addAll(atributoaCat(atrs));
    }

    public void getCausas(){
        dViewModel.buscarCausas();
        causas = dViewModel.getCausas();

    }
    public void getTomadoDe(){
        dViewModel.cargarCatalogos(dViewModel.productoSel.empaque,dViewModel.productoSel.idempaque,dViewModel.productoSel.clienteSel);

        List<CatalogoDetalle> catalogoDetalles=dViewModel.tomadoDe;
        tomadoDe = catalogoDetalles;
        Log.d(TAG,"ya tengo los catalogos"+catalogoDetalles.size());



    }
    public void getCausasSust(){
         causassust =   dViewModel.buscarCatalogoGen(Contrato.TablaInformeDet.CAUSA_SUSTITUCIONID);


    }
    public void regresar(){

    }

    public void guardarCliente(){ //ahora tambien es planta
        lastClickTime=0;
        DescripcionGenerica opcionsel = (DescripcionGenerica) preguntaview.getSpclientes().getSelectedItem();
       if(opcionsel==null){
           Toast.makeText(getActivity(),"Hubo un error, intente de nuevo",Toast.LENGTH_LONG).show();
        return;
       }
        int valor = opcionsel.getId(); //esta es la planta
        String aux=opcionsel.getDescripcion2();
        String arraux[]=aux.split(";");
        int clienteid=0;
        String nombreCliente="";

        if(aux.length()>1){
            clienteid=Integer.parseInt(arraux[0]);
            nombreCliente=arraux[1];
        }

        if(clienteid==4&&estatusPepsi==0)//no puedo comprar pepsi
        {
            Toast.makeText(getActivity(),"No puede comprar producto de pepsi en esta tienda",Toast.LENGTH_LONG).show();
            preguntaview.aceptarSetEnabled(true);
            return;
        }
        if(clienteid==5&&estatusPen==0)//no puedo comprar peñafiel
        {
            Toast.makeText(getActivity(),"No puede comprar producto de peñafiel en esta tienda",Toast.LENGTH_LONG).show();
            preguntaview.aceptarSetEnabled(true);
            return;
        }
        if(clienteid==6&&estatusElec==0)//no puedo comprar electro
        {
            Toast.makeText(getActivity(),"No puede comprar producto de electropura en esta tienda",Toast.LENGTH_LONG).show();
            preguntaview.aceptarSetEnabled(true);
            return;
        }
        if(clienteid==7&&estatusJum==0)//no puedo comprar electro
        {
            Toast.makeText(getActivity(),"No puede comprar producto de jumex en esta tienda",Toast.LENGTH_LONG).show();
            preguntaview.aceptarSetEnabled(true);
            return;
        }
        mViewModel.clienteSel=clienteid;
        Constantes.ni_clientesel=nombreCliente;
        mViewModel.informe=new InformeCompra();
        mViewModel.informe.setClienteNombre(nombreCliente);
        mViewModel.informe.setClientesId(clienteid);
        //ya tengo planta
        mViewModel.informe.setPlantaNombre(opcionsel.getDescripcion());
        mViewModel.informe.setPlantasId(valor);
        plantaSel =valor;
        NOMBREPLANTASEL = opcionsel.getDescripcion();

        //actualizo barra
        ((ContinuarInformeActivity)getActivity()).actualizarCliente(mViewModel.informe);
        //  guardarResp();
        mViewModel.guardarResp(mViewModel.getIdInformeNuevo(), dViewModel.getIddetalleNuevo(),plantaSel+"","plantasId","I",mViewModel.consecutivo,false);
        mViewModel.guardarResp(mViewModel.getIdInformeNuevo(), dViewModel.getIddetalleNuevo(),NOMBREPLANTASEL+"","plantaNombre","I",mViewModel.consecutivo,false);
        mViewModel.guardarResp(mViewModel.getIdInformeNuevo(), dViewModel.getIddetalleNuevo(),nombreCliente+"","clienteNombre","I",mViewModel.consecutivo,false);
        mViewModel.guardarResp(mViewModel.getIdInformeNuevo(), dViewModel.getIddetalleNuevo(),clienteid+"","clientesId","I",mViewModel.consecutivo,true);
        Log.d(TAG,preguntaAct.getNombreCampo()+"guardando cliente "+preguntaAct.getSigId());
        //dependiendo el cliente avanzo
        if(mViewModel.clienteSel==4)
            avanzarPregunta(preguntaAct.getSigId());
        if(mViewModel.clienteSel==5)
            irInfoPen();
        if(mViewModel.clienteSel==6)
            irInfoElect();
        if(mViewModel.clienteSel==7)
            irInfoJum();

    }
    public void siguiente(){
        boolean resp=false;
        lastClickTime=0;
        preguntaview.aceptarSetEnabled(false);
       /* if (textoint != null) {
           String valor = preguntaview.getTextoint().toString();
           if(valor.length()<=0){
               return;
           }

        }*/

        switch (preguntaAct.getNombreCampo()){
            case Contrato.TablaInformeDet.COSTO:
                String  valor = preguntaview.getTextoint().toString();
                // float val=Float.parseFloat(valor);
                if(valor.equals("$0.00")){
                    Toast.makeText(getActivity(),"Costo inválido, verifique",Toast.LENGTH_LONG).show();


                }
                else resp=true;
                break;
            case Contrato.TablaInformeDet.SIGLAS:
                resp=validarSiglas();
                break;
               /* case Contrato.TablaInformeDet.CODIGO:
                   resp=validarCodigoprod();
                    break;*/
            case Contrato.TablaInformeDet.CADUCIDAD:
                resp=validarFecha();
                if(resp)
                    resp=validarCodigoprod();
                break;
            //  case "clientesId": //reviso la opcion


            //break;
            default: resp=true; break;
        }

        if(resp)
        {

            if(preguntaAct.getId()==25 ) {
                String  valor = preguntaview.getTextoint().toString();

                //guardo el atributo para mostrarlo despues
                ((ContinuarInformeActivity)getActivity()).actualizarCodProd(valor);

            }else
            if(preguntaAct.getId()==26 ) {
                CatalogoDetalle opcionsel = (CatalogoDetalle) preguntaview.getSelectedItem();
                String  valor = opcionsel.getCad_descripcionesp()+ "";

                //guardo el atributo para mostrarlo despues
                Constantes.VarDetalleProd.tomadode = valor;
                ((ContinuarInformeActivity)getActivity()).actualizarAtributo1();

            }
            else
            if(preguntaAct.getId()==33 ) {
                CatalogoDetalle opcionsel = (CatalogoDetalle) preguntaview.getSelectedItem();
                String  valor = opcionsel.getCad_descripcionesp()+ "";

                //guardo el atributo para mostrarlo despues
                Constantes.VarDetalleProd.nvoatra = valor;
                ((ContinuarInformeActivity)getActivity()).actualizarAtributo1();

            }
            else  if( preguntaAct.getId()==36) {
                CatalogoDetalle opcionsel = (CatalogoDetalle) preguntaview.getSelectedItem();
                String valor = opcionsel.getCad_descripcionesp() + "";


                //guardo el atributo para mostrarlo despues
                Constantes.VarDetalleProd.nvoatrb = valor;
                ((ContinuarInformeActivity)getActivity()).actualizarAtributo2();

            } else  if(preguntaAct.getId()==39){
                CatalogoDetalle opcionsel = (CatalogoDetalle) preguntaview.getSelectedItem();
                String valor = opcionsel.getCad_descripcionesp() + "";

                //guardo el atributo para mostrarlo despues
                Constantes.VarDetalleProd.nvoatrc=valor;
                ((ContinuarInformeActivity)getActivity()).actualizarAtributo2();

            }else  if(preguntaAct.getId()==106){
                CatalogoDetalle opcionsel = (CatalogoDetalle) preguntaview.getSelectedItem();
                String valor = opcionsel.getCad_descripcionesp() + "";

                //guardo el atributo para mostrarlo despues
                Constantes.VarDetalleProd.nvoatrd=valor;
                ((ContinuarInformeActivity)getActivity()).actualizarAtributo3();

            }

            if(preguntaAct.getType().equals(CreadorFormulario.PREGUNTASINO)){
                //reviso la opcion seleccionada
                if(!preguntaview.getPregSiNoResp()&&preguntaAct.getId()!=43) //se selecciono no
                {
                    //voy al altsig
                    guardarResp();
                    //guarda informe

                    avanzarPregunta(preguntaAct.getSigAlt());
                    return;

                }
            }

            if(preguntaAct.getSigId()==10000) //voy a lista de compra
            {
                guardarResp();
                compraProd(preguntaview.getPreguntasino(),preguntaAct.getId() - 1);

            }else
            if(preguntaAct.getSigId()==43) //termine inf comentarios
            {
                if(yaestoyProcesando)
                    return;
                yaestoyProcesando=true;
                loadingDialog = new LoadingDialog(getActivity());
                //   loadingDialog.startLoadingDialog();
                guardarResp();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    //guarda informe
                    this.actualizarInforme();
                    this.finalizar();

                    //reviso si hay más clientes, si no fin
                    cargarClientes();
                    if (clientesAsignados != null && clientesAsignados.size() > 0) {

                        yaestoyProcesando = false;
                        avanzarPregunta(preguntaAct.getSigId());
                    } else {
                        //no hay mas clientes finalizo preinforme e informe
                        //    mViewModel.finalizarInforme();
                        //la muestra la guarde en la 42

                        Log.d(TAG, "NO MAS CLIENTES");
                        //es la 43 //finalizo preinforme
                        mViewModel.finalizarVisita(mViewModel.visita.getId());
                        //  mViewModel.eliminarTblTemp();
                        //   loadingDialog.dismisDialog();
                        Toast.makeText(getActivity(), getString(R.string.informe_finalizado), Toast.LENGTH_SHORT).show();
                        yaestoyProcesando = false;
                        salir();
                        //  preguntaview.aceptarSetEnabled(true);
                        return;

                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                    yaestoyProcesando=false;
                    compraslog.grabarError(TAG+" Error al guardar informe " +ex.getMessage());
                    Toast.makeText(getActivity(), "El informe no pudo guardarse", Toast.LENGTH_LONG).show();

                }

            }else
            if(preguntaAct.getSigId()==0)//terminé con preguntas de muestra
            {
                if(yaestoyProcesando)
                    return;
                yaestoyProcesando=true;
                preguntaview.aceptarSetEnabled(false);
                int sig=mViewModel.numMuestra+2;
                int nummuestra=mViewModel.numMuestra;


                //quito la info de la barra gris
                ((ContinuarInformeActivity)getActivity()).reiniciarBarra();
                Log.d(TAG,"antes de guardar num muestra"+nummuestra);
                guardarMuestra(sig);



            }else
            if(preguntaAct.getId()==47)//no hubo producto
            {
                if(yaestoyProcesando)
                    return;
                loadingDialog = new LoadingDialog(getActivity());
                loadingDialog.startLoadingDialog();
                guardarResp();
                //para otras si genero consecutivo
                // RadioGroup rg = (RadioGroup) respgen;
                String valor=null;
                int selectedRadioButtonId = preguntaview.getRadioGroup().getCheckedRadioButtonId();
                if (selectedRadioButtonId != -1) {
                  /*  RadioButton selectedRadioButton = root.findViewById(selectedRadioButtonId);
                    valor = selectedRadioButton.getText().toString();*/
                    //  valor=valor.toUpperCase();
                    valor = selectedRadioButtonId + "";
                }



                plantaSel=mViewModel.informe.getPlantasId();
                NOMBREPLANTASEL=mViewModel.informe.getPlantaNombre();
                if(valor!=null)
                    if(valor.equals("7")) //es otras
                    {
                        //generar consecutivo tienda
                        int consecutivo=mViewModel.getConsecutivo(plantaSel,getActivity(), this);

                        Log.d(TAG,"*genere cons="+consecutivo);

                        Log.d(TAG,"genere cons="+consecutivo);

                        mViewModel.informe.setConsecutivo(consecutivo);
                        Constantes.DP_CONSECUTIVO = consecutivo;
                        mViewModel.consecutivo=consecutivo;
                        mViewModel.guardarResp(0,0,plantaSel+"","plantasId","I",mViewModel.consecutivo,false);
                        mViewModel.guardarResp(0,0,NOMBREPLANTASEL+"","plantaNombre","I",mViewModel.consecutivo,false);
                        mViewModel.guardarResp(0,0,mViewModel.informe.getClienteNombre(),"clienteNombre","I",mViewModel.consecutivo,false);
                        guardarMuestra(preguntaAct.getSigId());
                        loadingDialog.dismisDialog();
                        //  consecutivo.removeObservers(DetalleProductoFragment.this);


                    }else {
                        mViewModel.guardarResp(0,0,plantaSel+"","plantasId","I",0,false);
                        mViewModel.guardarResp(0,0,NOMBREPLANTASEL+"","plantaNombre","I",0,false);
                        mViewModel.guardarResp(0,0,mViewModel.informe.getClienteNombre(),"clienteNombre","I",0,false);

                        guardarMuestra(preguntaAct.getSigId());
                        loadingDialog.dismisDialog();
                    }



                // avanzarPregunta(preguntaAct.getSigId());


            }else
            if(preguntaAct.getId()==23){ //son la siglas y ya seleccioné producto
                //creo informe e informe detalle con datos del producto
                guardarResp();
                guardarProductoTemp();
                avanzarPregunta(preguntaAct.getSigId());
            }else
            if(preguntaAct.getId()==43){ //hay ootro cliente
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

                // mViewModel.finalizarInforme();
                //la muestra la guarde en la 42
                if(!preguntaview.getPregSiNoResp()) //se selecciono no
                {
                    Log.d(TAG,"dice que no");
                    //es la 43 //finalizo preinforme
                    finalizarPreinforme();
                    return;
                }
                avanzarPregunta(1);


            }
            else{
                if(preguntaAct.getId()>0)
                    guardarResp();
                avanzarPregunta(preguntaAct.getSigId());
            }
        }
     //   preguntaview.aceptarSetEnabled(true);
    }
    public void finalizar() {

        //validar que si hay producto realmente tenga un producto capturado

        mViewModel.eliminarTblTemp();
        mViewModel.finalizarInforme();

        try {
            InformeEnvio informe=this.preparaInforme();
            SubirInformeTask miTareaAsincrona = new SubirInformeTask(true,informe,getActivity(),mViewModel);
            miTareaAsincrona.execute();


            subirFotos(getActivity(),informe);
        }catch(Exception ex){
            ex.getStackTrace();
            compraslog.grabarError(TAG+"Algo salió mal al enviar"+ex.getMessage());
            Toast.makeText(getContext(),"Algo salio mal al enviar",Toast.LENGTH_LONG).show();
        }
        //limpio variables de sesion
        Constantes.productoSel=null;
        Constantes.VarDetalleProd.tomadode=null;
        Constantes.VarDetalleProd.nvoatra=null;
        Constantes.VarDetalleProd.nvoatrb=null;
        Constantes.VarDetalleProd.nvoatrc=null;
        Constantes.DP_TIPOTIENDA=0;
        Constantes.NM_TOTALISTA=0;
        Constantes.ni_clientesel=null;
        mViewModel.limpiarVarInforme();
        mViewModel.setIdInformeNuevo(0);
        plantaSel=0;
        NOMBREPLANTASEL=null;
        Constantes.DP_CONSECUTIVO=0;

        nombre_foto=null;
        archivofoto=null;
    }



    public void finalizarPreinforme(){
        //Es hora de cerrar el preinforme
        //pregunto si habrá más clientes
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
        dialogo1.setTitle(R.string.importante);
        dialogo1.setMessage(R.string.conf_finalizar);
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                //Es hora de cerrar el preinforme
                mViewModel.finalizarVisita(mViewModel.visita.getId());
                // mViewModel.eliminarTblTemp();

                Toast.makeText(getActivity(), getString(R.string.informe_finalizado),Toast.LENGTH_SHORT).show();
                salir();
            }
        });
        dialogo1.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                //no hago nada
                preguntaview.aceptarSetEnabled(true);
                dialogo1.cancel();


            }
        });
        dialogo1.show();


    }
    public void salir(){
        mViewModel.eliminarTblTemp();
        //me voy a la lista de informes
        getActivity().finish();
        Intent intento1 = new Intent(getActivity(), NavigationDrawerActivity.class);
        intento1.putExtra(NavigationDrawerActivity.NAVINICIAL,"listainforme");
        startActivity(intento1);
        // NavHostFragment.(this).navigate(R.id.action_selclientetolistacompras,bundle);


    }
    public void guardarMuestra(int sig){
        try {

            //Creo el informe en nuevo informe y lo busco aqui
            //necestio saber si ya habia guardado informe
            //veo si ya existe el informe o hay que crearlo
            //   Log.d(TAG, "guardando informe"+mViewModel.numMuestra+"--"+mViewModel.getIdInformeNuevo());
            //   exit(0);
            if (mViewModel.numMuestra == 1 || mViewModel.getIdInformeNuevo() <= 0) {
                Log.d(TAG, "guardando informe");
                //busco el consecutivo
                MutableLiveData<Integer> idInformeNuevo = guardarInforme();
                //   Log.d(TAG, "guardando informe"+mViewModel.numMuestra+"--"+mViewModel.getIdInformeNuevo());
                //
                idInformeNuevo.observe(getViewLifecycleOwner(), new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer idnvo) {
                        Log.d(TAG, "se creo el informe" + idnvo);
                        mViewModel.informe.setId(idnvo);
                        mViewModel.setIdInformeNuevo(idnvo);
                        int numues=mViewModel.numMuestra;
                        // mViewModel.informe.setSinproducto();
                        if(!mViewModel.informe.isSinproducto()) {
                            //si tengo detalle
                            Log.d(TAG,"guardando  muestras "+mViewModel.numMuestra+"--"+Constantes.NM_TOTALISTA);

                            //    List<Integer> muestras= dViewModel.muestrasTotales();
                            //  for(int x:muestras) {
                            int nuevoid = dViewModel.insertarMuestra(mViewModel.getIdInformeNuevo(), mViewModel.numMuestra);
                            //guardo la muestra
                            if (nuevoid > 0&&dViewModel.icdNuevo!=null) {
                                dViewModel.setIddetalleNuevo(nuevoid);
                                //si ya se guardó lo agrego en la lista de compra
                                ListaDetalleViewModel lcviewModel = new ViewModelProvider(DetalleProductoFragment.this).get(ListaDetalleViewModel.class);
                                Log.d(TAG,"qqqqqqqqqqqqqqq"+dViewModel.icdNuevo+"--"+dViewModel.icdNuevo.getCaducidad());
                                int res=lcviewModel.comprarMuestraPepsi(dViewModel.icdNuevo.getComprasId(), dViewModel.icdNuevo.getComprasDetId(), sdfcodigo.format(dViewModel.icdNuevo.getCaducidad()), dViewModel.icdNuevo.getTipoMuestra(),dViewModel.icdNuevo.getComprasIdbu(),dViewModel.icdNuevo.getComprasDetIdbu(),4);
                                //limpiar tabla temp
                                //   limpiarTablTempMenCli();
                                //reviso si es x una cancelacion para actualizar el estatus
                                InformeCompraDetalle detallecan=mViewModel.getCancelada(dViewModel.icdNuevo.getComprasId(), dViewModel.icdNuevo.getComprasDetId(),2);
                                if(detallecan!=null)
                                    mViewModel.actualizarCancelada(detallecan.getId(),4);
                                mViewModel.eliminarMuestra(mViewModel.numMuestra);
                                dViewModel.setIddetalleNuevo(0);
                                dViewModel.limpiarVarsMuestra();
                            }
                            else{
                                //algo salio mal
                                Toast.makeText(getActivity(), "No se pudo guardar la muestra", Toast.LENGTH_LONG).show();
                                yaestoyProcesando=false;
                                return;
                            }
                            yaestoyProcesando=false;
                            if(Constantes.NM_TOTALISTA>=16&&numues==3||Constantes.NM_TOTALISTA<16&&numues==2) //ya terminé
                            {
                                Log.d(TAG,"terminé debo guardar y salir"+Constantes.NM_TOTALISTA+"--"+numues);
                                //   limpiarTablTemp();

                                avanzarPregunta(5);
                                //preguntar si hay otro cliente, para agregar otro o cerrar
                            }else
                            {

                                mViewModel.numMuestra++;
                                Log.d(TAG, "sume muestra"+mViewModel.numMuestra);

                                avanzarPregunta(sig);
                            }
                            //  }
                        }else {
                            dViewModel.setIddetalleNuevo(0);
                            dViewModel.icdNuevo = null;
                            //guardo el numinforme para cuando se creen los coment
                            mViewModel.guardarResp( mViewModel.getIdInformeNuevo() ,0, mViewModel.getIdInformeNuevo()+"" ,"informeid","I",mViewModel.consecutivo,false);
                            yaestoyProcesando=false;
                            mViewModel.numMuestra++;
                            Log.d(TAG, "sume muestra"+mViewModel.numMuestra);

                            avanzarPregunta(sig);
                        }

                         //  idInformeNuevo.removeObservers(DetalleProductoFragment.this);

                    }

                });

            }else
            if(!mViewModel.informe.isSinproducto()) {
                //si tengo detalle
                Log.d(TAG,"guardando  muestras");

                //    List<Integer> muestras= dViewModel.muestrasTotales();
                //  for(int x:muestras) {
                int numues=mViewModel.numMuestra;
                int nuevoid = dViewModel.insertarMuestra(mViewModel.getIdInformeNuevo(), mViewModel.numMuestra);
                //guardo la muestra
                if (nuevoid > 0) {
                    dViewModel.setIddetalleNuevo(nuevoid);
                    //si ya se guardó lo agrego en la lista de compra
                    ListaDetalleViewModel lcviewModel = new ViewModelProvider(this).get(ListaDetalleViewModel.class);
                    Log.d(TAG,"qqqqqqqqqqqqqqq"+dViewModel.icdNuevo.getCaducidad());
                    int res=lcviewModel.comprarMuestraPepsi(dViewModel.icdNuevo.getComprasId(), dViewModel.icdNuevo.getComprasDetId(), sdfcodigo.format(dViewModel.icdNuevo.getCaducidad()), dViewModel.icdNuevo.getTipoMuestra(),dViewModel.icdNuevo.getComprasIdbu(),dViewModel.icdNuevo.getComprasDetIdbu(),4);
                    //limpiar tabla temp
                    //   limpiarTablTempMenCli();
                    mViewModel.eliminarMuestra(mViewModel.numMuestra);
                    dViewModel.setIddetalleNuevo(0);
                    dViewModel.limpiarVarsMuestra();
                    yaestoyProcesando=false;
                    if(numues==3||Constantes.NM_TOTALISTA<17&&numues==2) //ya terminé
                    {
                        Log.d(TAG,"terminé debo guardar y salir"+Constantes.NM_TOTALISTA+"--"+numues);
                        //   limpiarTablTemp();
                        avanzarPregunta(5);

                    }else
                    {
                        mViewModel.numMuestra++;
                        Log.d(TAG, "sume muestra"+mViewModel.numMuestra);

                        avanzarPregunta(sig);
                    }
                }
               // mViewModel.numMuestra++;
                //  }
            }else {
                dViewModel.setIddetalleNuevo(0);
                dViewModel.icdNuevo = null;
                //guardo el numinforme para cuando se creen los coment
                mViewModel.guardarResp( mViewModel.getIdInformeNuevo() ,0, mViewModel.getIdInformeNuevo()+"" ,"informeid","I",mViewModel.consecutivo,false);
                yaestoyProcesando=false;
                mViewModel.numMuestra++;
                avanzarPregunta(sig);

            }

            //else
            // limpiarTablTempMenCli();
            //reinicio variables

            //    mViewModel.numMuestra=0;




        }catch (Exception ex){
            ex.printStackTrace();
            Log.e(TAG,ex.getMessage());
            compraslog.grabarError(TAG,"guardarMuestra",ex.getMessage());

            Toast.makeText(getActivity(), "No se pudo guardar la muestra", Toast.LENGTH_LONG).show();

        }

    }
    public InformeEnvio preparaInforme(){
        InformeEnvio envio=new InformeEnvio();
        Log.d(TAG,"estatus informe"+mViewModel.visita.getEstatus());
        if(mViewModel.visita.getEstatusSync()==0)
            envio.setVisita(mViewModel.visita);
        envio.setInformeCompra(mViewModel.informe);
        mViewModel.cargarMuestras(mViewModel.informe.getId());
        //busco el prod exhibido

        List<ProductoExhibido> productoExhibidos=mViewModel.buscarProdExhiPend();
        Log.d(TAG,"buscando prodexh"+productoExhibidos.size());
        envio.setClaveUsuario(Constantes.CLAVEUSUARIO);
        envio.setIndice(mViewModel.visita.getIndice());
        envio.setProductosEx(productoExhibidos);
        envio.setInformeCompraDetalles(mViewModel.muestrasCap);
        envio.setImagenDetalles(mViewModel.buscarImagenes(mViewModel.visita,mViewModel.informe,mViewModel.muestrasCap));
        return envio;
    }

    public static void subirFotos(Activity activity, InformeEnvio informe){
        //las imagenes
        for(ImagenDetalle imagen:informe.getImagenDetalles()){
            //subo cada una
            Intent msgIntent = new Intent(activity, SubirFotoService.class);
            msgIntent.putExtra(SubirFotoService.EXTRA_IMAGE_ID, imagen.getId());
            msgIntent.putExtra(SubirFotoService.EXTRA_IMG_PATH,imagen.getRuta());
            msgIntent.putExtra(SubirFotoService.EXTRA_INDICE,informe.getIndice());
            // Constantes.INDICEACTUAL
            Log.d(TAG,"subiendo fotos"+activity.getLocalClassName());

            msgIntent.setAction(SubirFotoService.ACTION_UPLOAD_IMG);

            //cambio su estatus a subiendo
            imagen.setEstatusSync(1);
            activity.startService(msgIntent);
            //cambio su estatus a subiendo



        }

    }
    public boolean validarFecha(){
        ValidadorDatos valdat=new ValidadorDatos();
        Log.d(TAG, ">>>> "+  dViewModel.productoSel.clienteNombre);

        if (dViewModel.productoSel.clienteNombre.toUpperCase().trim().equals("PEPSI")) {
            valdat.validarFechaPep(preguntaview.getTextoint().toString(),tipoTienda);
            if(valdat.mensaje>0)
                Toast.makeText(getActivity(), getString(valdat.mensaje), Toast.LENGTH_LONG).show();

            return valdat.resp;
        }
        try {
            sdfcodigo.setLenient(false);
            fechacad=sdfcodigo.parse(preguntaview.getTextoint().toString());
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    public boolean validarCodigoprod(){
        Log.d(TAG,"En validar cod consecutivo "+Constantes.DP_CONSECUTIVO);
        try {
            fechacad=sdfcodigo.parse(preguntaview.getTextoint().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(dViewModel.productoSel.clienteNombre.toUpperCase().equals("PEPSI")) {
            ValidadorDatos valdat = new ValidadorDatos();
            // if(Constantes.DP_CONSECUTIVO<11)
            if (!valdat.validarCodigoprodPep(preguntaview.getTextoint().toString(), dViewModel.productoSel.codigosnop)) { //son solo los que vienen del servidor en la lista no tengo los permanentes
                if(valdat.mensaje>0)
                    Toast.makeText(getActivity(), getString(valdat.mensaje), Toast.LENGTH_LONG).show();
                return false;
            } else {
                //Constantes.
                //busco si hay otra muestra == y si tiene el mismo codigo
                if(dViewModel.productoSel.tipoMuestra!=3)
                { res = buscarMuestraCodigo(dViewModel.productoSel, preguntaview.getTextoint().toString(), fechacad,dViewModel.productoSel.codigosperm);

                    if (res) {
                        Toast.makeText(getActivity(), getString(R.string.error_codigo_per), Toast.LENGTH_LONG).show();

                        return false;

                    }
                }

            }

        }
        return true;
    }
    public MutableLiveData<Integer> guardarInforme(){

        return mViewModel.insertarInfdeTemp(getActivity(), getViewLifecycleOwner());
    }
    public void actualizarInforme() throws Exception {
        mViewModel.actualizarInforme();
    }
    public void limpiarTablTemp(){
        Log.d(TAG,"limpiando tabla");

        mViewModel.eliminarTblTemp();
    }
    public void limpiarTablTempMenCli(){
        Log.d(TAG,"limpiando tabla");

        // mViewModel.eliminarTblTempMenosCli();
    }

    public void avanzarPregunta(int sig){
        if(sig==0)
            guardarResp();//vuelvo a guardar
        if(sig==1){
            //empiezo de 0
            Bundle bundle = new Bundle();
            bundle.putInt(ContinuarInformeActivity.INFORMESEL,mViewModel.visita.getId());
            Log.d(TAG,"donde entras doble");
            //NavHostFragment.findNavController(this).navigate(R.id.action_visitatonuevo,bundle);
            Intent intento1=new Intent(getActivity(), ContinuarInformeActivity.class);
            intento1.putExtras(bundle);
            requireActivity().finish();
            startActivity(intento1);
            return;
        }
        //busco el siguiente
        Reactivo nvoReac = dViewModel.buscarReactivoSimpl(sig);
        Log.e(TAG, "4-------------cons" + Constantes.DP_CONSECUTIVO+"--"+mViewModel.numMuestra);
        preguntaview=null;
        //creo uno nuevo
        preguntaview=new DetalleInfView(getContext());
        this.ultimares=null;
        this.preguntaAct=nvoReac;
        this.isEdicion=false;
        svprin.removeAllViewsInLayout();

        crearPregunta();

    }


    public void irInfoPen(){
        //busco el siguiente
        LiveData<Reactivo> nvoReac = dViewModel.buscarReactivo(52); //1er pregunta de pañafiel
        nvoReac.observe(getViewLifecycleOwner(), new Observer<Reactivo>() {
            @Override
            public void onChanged(Reactivo reactivo) {
                Bundle args = new Bundle();
                args.putInt(DetalleProductoPenFragment.ARG_PREGACTP,reactivo.getId() );
                args.putBoolean(DetalleProductoPenFragment.ARG_ESEDIP,false);
                DetalleProductoPenFragment nvofrag = new DetalleProductoPenFragment();
                nvofrag.setArguments(args);
                // DetalleProductoPenFragment nvofrag = new DetalleProductoPenFragment(reactivo,false);
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

    public void irInfoElect(){
        //busco el siguiente
        LiveData<Reactivo> nvoReac = dViewModel.buscarReactivo(72);
        nvoReac.observe(getViewLifecycleOwner(), new Observer<Reactivo>() {
            @Override
            public void onChanged(Reactivo reactivo) {
                Bundle args = new Bundle();
                args.putInt(DetalleProductoElecFragment.ARG_PREGACTE,reactivo.getId() );
                args.putBoolean(DetalleProductoElecFragment.ARG_ESEDIE,false);
                DetalleProductoElecFragment nvofrag = new DetalleProductoElecFragment();
                nvofrag.setArguments(args);
                // DetalleProductoElecFragment nvofrag = new DetalleProductoElecFragment(reactivo,false);
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

    public void irInfoJum(){
        //busco el siguiente
        LiveData<Reactivo> nvoReac = dViewModel.buscarReactivo(52);
        nvoReac.observe(getViewLifecycleOwner(), new Observer<Reactivo>() {
            @Override
            public void onChanged(Reactivo reactivo) {
                Bundle args = new Bundle();
                args.putInt(DetalleProductoJumFragment.ARG_PREGACTJ,reactivo.getId() );
                args.putBoolean(DetalleProductoJumFragment.ARG_ESEDIJ,false);
                DetalleProductoJumFragment nvofrag = new DetalleProductoJumFragment();
                nvofrag.setArguments(args);
                //DetalleProductoJumFragment nvofrag = new DetalleProductoJumFragment(reactivo,false);
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
                //  textoint.setText("");
                //  textoint.setHint("Escuchando...");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                //   textoint.setHint("");
            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                // micbtn.setImageResource(R.drawable.ic_baseline_mic_none_24);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                // textoint.setText(data.get(0));
                //  textoint.setHint("");

            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

          /*  micbtn.setOnTouchListener(new View.OnTouchListener() {


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
            });*/
        return  speechRecognizer;
    }

    //        int consecutivo =1;
    //guardo en tabla temp
    public void guardarResp() {
        Log.d(TAG, "guardando en temp*****" + preguntaAct.getId());


        String valor = null;
        if (preguntaview.hayTextoInt()) {
            valor = preguntaview.getTextoint().toString();
            valor = valor.toUpperCase();
        } else {
            if (preguntaAct.getType().equals(CreadorFormulario.SELECTCAT)) {
                CatalogoDetalle opcionsel = (CatalogoDetalle) preguntaview.getSelectedItem();
                valor = opcionsel.getCad_idopcion() + "";
            }
            if (preguntaAct.getType().equals(CreadorFormulario.SELECTDES)) {
                DescripcionGenerica opcionsel = (DescripcionGenerica) preguntaview.getSelectedItem();
                valor = opcionsel.getId() + "";
            }
            if (preguntaAct.getType().equals(CreadorFormulario.PSELECT)) {
                valor = preguntaview.getSpclientes().getSelectedItemId() + " ";

            }
            if (preguntaAct.getType().equals(CreadorFormulario.RADIOBUTTON)) {
                //    RadioGroup rg = (RadioGroup) respgen;

                int selectedRadioButtonId = preguntaview.getRadioGroup().getCheckedRadioButtonId();
                if (selectedRadioButtonId != -1) {
                  /*  RadioButton selectedRadioButton = root.findViewById(selectedRadioButtonId);
                    valor = selectedRadioButton.getText().toString();*/
                    //  valor=valor.toUpperCase();
                    valor = selectedRadioButtonId + "";
                }

            }
            if (preguntaAct.getType().equals(CreadorFormulario.PREGUNTASINO)) {
                valor = preguntaview.getPregSiNoResp() + "";

            }
        }
        Log.d(TAG, "guardando en temp" + preguntaAct.getId() + "val" + mViewModel.consecutivo);
        if(preguntaAct.getId()==5&& preguntaview.getNopermiso().isChecked())//es ticket
        {
            mViewModel.guardarResp(mViewModel.getIdInformeNuevo(), dViewModel.getIddetalleNuevo(), "0", preguntaAct.getNombreCampo(), preguntaAct.getTabla(), mViewModel.consecutivo, true);
            mViewModel.guardarResp(mViewModel.getIdInformeNuevo(), dViewModel.getIddetalleNuevo(), "1", "ticket_noemiten", preguntaAct.getTabla(), mViewModel.consecutivo, true);

        }else
            //  if(!preguntaAct.getType().equals(CreadorFormulario.AGREGARIMAGEN))
            //paso a mayusculas

            if (preguntaAct.getId() > 0 && valor != null && valor.length() > 0) {
                //actualizo la visita
                if(mViewModel.visita.getEstatus()!=3)
                    mViewModel.actualizarVisita(mViewModel.visita.getId(),3);
                mViewModel.guardarResp(mViewModel.getIdInformeNuevo(), dViewModel.getIddetalleNuevo(), valor, preguntaAct.getNombreCampo(), preguntaAct.getTabla(), mViewModel.consecutivo, true);
            }

        //si es la 2 4 o 3 guardo la

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
            //intento1.putExtra("origen", origen);
            if (preguntaview.getFotomos() != null) {

                startActivityForResult(intento1, REQUEST_CODE_TAKE_PHOTO);

            }
        }

    }
    public void rotar(){
       // String foto=preguntaview.getTextoint().toString().toLowerCase();
        String foto=preguntaview.getTextoint().toString().toLowerCase();
        if(ComprasUtils.getAvailableMemory(getActivity()).lowMemory)
        {
            Toast.makeText(getActivity(), "No hay memoria suficiente para esta accion", Toast.LENGTH_SHORT).show();

            return;
        }else
            RevisarFotoActivity.rotarImagen(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" +foto,preguntaview.getFotomos());
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //    super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"vars"+requestCode +"--"+ nombre_foto);
        if ((requestCode == REQUEST_CODE_TAKE_PHOTO) && resultCode == RESULT_OK) {
            //   super.onActivityResult(requestCode, resultCode, data);

            if (archivofoto!=null&&archivofoto.exists()) {
                if(requestCode == REQUEST_CODE_TAKE_PHOTO) {
                    //envio a la actividad dos para ver la foto
                    //    Intent intento1 = new Intent(getActivity(), RevisarFotoActivity.class);
                    //  intento1.putExtra("ei.archivo", nombre_foto);

                    preguntaview.setTextoint(nombre_foto);

                    if(ComprasUtils.getAvailableMemory(getActivity()).lowMemory)
                    {
                        Toast.makeText(getActivity(), "No hay memoria suficiente para esta accion", Toast.LENGTH_SHORT).show();

                        return;
                    }else {
                        // Bitmap bitmap1 = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null) + "/" + nombre_foto);
                        ComprasUtils cu = new ComprasUtils();
                        cu.comprimirImagen(archivofoto.getAbsolutePath());
                        Bitmap bitmap1 = ComprasUtils.decodeSampledBitmapFromResource(archivofoto.getAbsolutePath(), 100, 100);
                        preguntaview.setImageBitmap(bitmap1);
                        // fotomos.setLayoutParams(new LinearLayout.LayoutParams(350,150));
                        preguntaview.verBtnRotar();

                        nombre_foto=null;
                        archivofoto=null;
                        if(preguntaview.getNopermiso()!=null) {
                            preguntaview.getNopermiso().setChecked(false);
                        }
                        preguntaview.aceptarSetEnabled(true);
                    }

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

                // totalLista=data.getIntExtra(ARG_TOTALLISTA, 0);
                //muestro el de 3a muestra
                // if(Constantes.NM_TOTALISTA>=16)
                //  tercera.setVisible(View.VISIBLE);
                if(Constantes.productoSel!=null)
                {
                    dViewModel.productoSel = Constantes.productoSel;
                    //guardo el total de la lista
                    //generar consecutivo tienda
                    Log.d(TAG, "antes de generar cons "+  dViewModel.productoSel.clienteNombre+"--"+dViewModel.productoSel.plantaSel);

                    if(mViewModel.consecutivo==0) {
                        int consecutivo = mViewModel.getConsecutivo(dViewModel.productoSel.plantaSel, getActivity(), this);
                        //  Log.d(TAG, "*genere cons=" + consecutivo);
                        Log.d(TAG, "genere cons=" + consecutivo);
                        Log.d(TAG, "numuest list" + mViewModel.numMuestra);
                        mViewModel.informe.setConsecutivo(consecutivo);

                        mViewModel.consecutivo = consecutivo;
                        Constantes.DP_CONSECUTIVO=consecutivo;
                        //actualizo barra
                        Log.d(TAG,"tengo el tipo muestra "+dViewModel.productoSel);
                        ((ContinuarInformeActivity) getActivity()).actualizarProdSel(dViewModel.productoSel);

                        mViewModel.guardarResp(0, 0, Constantes.NM_TOTALISTA + "", "totalLista", "", mViewModel.consecutivo, false);
                        ((ContinuarInformeActivity)getActivity()).actualizarCliente(mViewModel.informe);
                        ((ContinuarInformeActivity) getActivity()).actualizarProdSel(dViewModel.productoSel);
                        //veo si fue sustitucion
                       // if(Constantes.productoSel.tipoMuestra==3)
                       //     avanzarPregunta(126); //pregunto motivo
                      //  else
                            avanzarPregunta(23);

                    }else {


                        mViewModel.informe.setConsecutivo(mViewModel.consecutivo);
                        ((ContinuarInformeActivity)getActivity()).actualizarCliente(mViewModel.informe);
                        //actualizo barra
                        ((ContinuarInformeActivity) getActivity()).actualizarProdSel(dViewModel.productoSel);

                        mViewModel.guardarResp(0, 0, Constantes.NM_TOTALISTA + "", "totalLista", "", mViewModel.consecutivo, false);
                        //veo si fue sustitucion
                      //  if(Constantes.productoSel.tipoMuestra==3)
                      //      avanzarPregunta(126); //pregunto motivo
                      //  else
                            avanzarPregunta(23);


                    }

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
                    preguntaview.setTextoint(result.getContents());
                    //Toast.makeText(getActivity(), "Content: ${result.getContents()}",Toast.LENGTH_LONG ).show();
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
    private void guardarProductoTemp(){
        mViewModel.guardarResp(mViewModel.getIdInformeNuevo(),0,dViewModel.productoSel.productoid+"","productoId","ID",mViewModel.consecutivo,false);

        mViewModel.guardarResp(mViewModel.getIdInformeNuevo(),0,dViewModel.productoSel.producto+"","producto","ID",mViewModel.consecutivo,false);
        mViewModel.guardarResp(mViewModel.getIdInformeNuevo(),0,dViewModel.productoSel.presentacion+"","presentacion","ID",mViewModel.consecutivo,false);
        mViewModel.guardarResp(mViewModel.getIdInformeNuevo(),0,dViewModel.productoSel.tamanioId+"","tamanioId","ID",mViewModel.consecutivo,false);

        mViewModel.guardarResp(mViewModel.getIdInformeNuevo(),0,dViewModel.productoSel.empaque+"","empaque","ID",mViewModel.consecutivo,false);
        mViewModel.guardarResp(mViewModel.getIdInformeNuevo(),0,dViewModel.productoSel.idempaque+"","empaquesId","ID",mViewModel.consecutivo,false);

        mViewModel.guardarResp( mViewModel.getIdInformeNuevo(),0,mViewModel.numMuestra+"","numMuestra","ID",mViewModel.consecutivo,false);

        mViewModel.guardarResp(mViewModel.getIdInformeNuevo(),0,dViewModel.productoSel.tipoAnalisis+"","tipoAnalisis","ID",mViewModel.consecutivo,false);
        mViewModel.guardarResp(mViewModel.getIdInformeNuevo(),0,dViewModel.productoSel.analisis+"","nombreAnalisis","ID",mViewModel.consecutivo,false);
        mViewModel.guardarResp(mViewModel.getIdInformeNuevo(),0,dViewModel.productoSel.compraSel+"","comprasId","ID",mViewModel.consecutivo,false);
        mViewModel.guardarResp(mViewModel.getIdInformeNuevo(),0,dViewModel.productoSel.compradetalleSel+"","comprasDetId","ID",mViewModel.consecutivo,false);
        mViewModel.guardarResp(mViewModel.getIdInformeNuevo(),0,dViewModel.productoSel.siglas+"","siglaspla","",mViewModel.consecutivo,false);
        mViewModel.guardarResp(mViewModel.getIdInformeNuevo(),0,dViewModel.productoSel.tipoMuestra+"","tipoMuestra","ID",mViewModel.consecutivo,false);
        mViewModel.guardarResp(mViewModel.getIdInformeNuevo(),0,dViewModel.productoSel.nombreTipoMuestra+"","nombreTipoMuestra","ID",mViewModel.consecutivo,false);



        mViewModel.guardarResp(mViewModel.getIdInformeNuevo(),0,dViewModel.productoSel.plantaSel+"","plantasId","I",mViewModel.consecutivo,false);
        mViewModel.guardarResp(mViewModel.getIdInformeNuevo(),0,dViewModel.productoSel.plantaNombre+"","plantaNombre","I",mViewModel.consecutivo,false);
        mViewModel.guardarResp(mViewModel.getIdInformeNuevo(),0,dViewModel.productoSel.clienteNombre+"","clienteNombre","I",mViewModel.consecutivo,false);
        mViewModel.guardarResp(mViewModel.getIdInformeNuevo(),0,dViewModel.productoSel.clienteSel+"","clientesId","I",mViewModel.consecutivo,false);
        mViewModel.guardarResp(mViewModel.getIdInformeNuevo(),0,dViewModel.productoSel.codigosnop+"","codigosnop","",mViewModel.consecutivo,false);
        mViewModel.guardarResp(mViewModel.getIdInformeNuevo(),0,dViewModel.productoSel.codigosperm+"","codigosperm","",mViewModel.consecutivo,false);

        mViewModel.guardarResp(mViewModel.getIdInformeNuevo(),0,dViewModel.productoSel.comprasDetIdbu+"","comprasDetIdbu","ID",mViewModel.consecutivo,false);
        mViewModel.guardarResp(mViewModel.getIdInformeNuevo(),0,dViewModel.productoSel.comprasIdbu+"","comprasIdbu","ID",mViewModel.consecutivo,false);

    }
    //validar siglas
    public boolean validarSiglas(){
        if(dViewModel.productoSel.clienteSel==4)
            if(!preguntaview.getTextoint().toString().equals("")){
                String siglaslis=dViewModel.productoSel.siglas;
                if(dViewModel.productoSel.siglas!=null&&!siglaslis.toUpperCase().equals(preguntaview.getTextoint().toString().toUpperCase())){
                    Toast.makeText(getActivity(), getString(R.string.error_siglas), Toast.LENGTH_LONG).show();
                    preguntaview.aceptarSetEnabled(true);
                    return false;
                }
            }

        return true;
    }
    Date fechacad = null;
    boolean res;
    //validar siglas

    public boolean buscarMuestraCodigo(NuevoDetalleViewModel.ProductoSel productosel,String codigonvo,Date caducidadnva, String codigosperm){
        //busco en el mismo informe
        return dViewModel.buscarMuestraCodigo(Constantes.INDICEACTUAL,dViewModel.productoSel.plantaSel,productosel,codigonvo,caducidadnva,getViewLifecycleOwner(),codigosperm);

    }
    //si true ya existe un codigo igual
    public boolean buscarMuestraCodigoPeñafiel(NuevoDetalleViewModel.ProductoSel productosel,Date caducidadnva, String codigosperm){
        //busco en el mismo informe
        return dViewModel.buscarMuestraCodigo(Constantes.INDICEACTUAL,dViewModel.productoSel.plantaSel,productosel,"",caducidadnva,getViewLifecycleOwner(),codigosperm);

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
          /*  if (requestCode == RecordAudioRequestCode && grantResults.length > 0 ){
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(getActivity(),"Permission Granted",Toast.LENGTH_SHORT).show();
            }*/
        if (requestCode == 100) {
            RuntimePermissionUtil.onRequestPermissionsResult(grantResults, new RPResultListener() {
                @Override
                public void onPermissionGranted() {
                    if ( RuntimePermissionUtil.checkPermissonGranted(getActivity(), Manifest.permission.CAMERA)) {
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
        String opcion = "";
        Intent intento1 = new Intent(getActivity(), BackActivity.class);


        //  if(nummuestra<2) {
          /*  List<ListaCompra> listapl = lcviewModel.cargarPlantas(mViewModel.visita.getCiudad(), mViewModel.clienteSel);
            // Log.d(TAG,"todavia no se que hacer"+listapl.size());

            if (listapl.size() > 1) {

                //Log.d(TAG,"todavia no se que hacer");
                opcion = BackActivity.OP_SELPLANTA;
                intento1.putExtra(SelClienteFragment.ARG_TIPOCONS, "action_selclitolista");
            } else if (listapl.size() > 0) {
                //voy directo a la lista
                plantaSel = listapl.get(0).getPlantasId();
                NOMBREPLANTASEL = listapl.get(0).getPlantaNombre();*/
        opcion = BackActivity.OP_LISTACOMPRA;

        // }
        //  }else{
        //ya tengo la planta
        InformeTemp inf= dViewModel.buscarxNombreCam("plantasId");
        if(inf!=null) {
            plantaSel=Integer.parseInt(inf.getValor());

        }
        inf= dViewModel.buscarxNombreCam("plantaNombre");
        if(inf!=null) {
            NOMBREPLANTASEL=inf.getValor();

        }

        opcion = BackActivity.OP_LISTACOMPRA;
        //  }

        //ya existe el informe
        intento1.putExtra(DetalleProductoFragment.ARG_NUEVOINFORME, mViewModel.getIdInformeNuevo());
        intento1.putExtra(BackActivity.ARG_FRAGMENT,opcion);
        intento1.putExtra("ciudadSel", mViewModel.visita.getCiudadId());
        intento1.putExtra("ciudadNombre", mViewModel.visita.getCiudad());
        intento1.putExtra(ListaCompraFragment.ARG_PLANTASEL, plantaSel);
        intento1.putExtra(ListaCompraFragment.ARG_NOMBREPLANTASEL, NOMBREPLANTASEL);
        intento1.putExtra(ListaCompraFragment.ARG_MUESTRA, "true");


        // intento1.putExtra(ListaCompraFragment.ARG_MUESTRA,"true");
        // spclientes = root.findViewById(1001);
        Log.d(TAG, " antes de ir a listacom nummuestra" + nummuestra);
        intento1.putExtra(ARG_CLIENTESEL, mViewModel.clienteSel);
        //intento1.putExtra(ARG_CLIENTENOMBRE,);
        intento1.putExtra(DetalleProductoFragment.NUMMUESTRA, nummuestra);

        startActivityForResult(intento1, BackActivity.REQUEST_CODE);

        //  startActivity(intento1);

    }



    @Override
    public void onPause() {
        super.onPause();


    }
    @Override
    public void onResume() {
        super.onResume();
          /*  if(textoint!=null) {
               // Log.d(TAG, "genere cons=" + preguntaview.getTextoint().toString());

                if (!preguntaview.getTextoint().toString().equals("") && preguntaAct.getType().equals(CreadorFormulario.AGREGARIMAGEN)) {
                    if(AbririnformeFragment.getAvailableMemory(getActivity()).lowMemory)
                    {
                        Toast.makeText(getActivity(), "No hay memoria suficiente para esta accion", Toast.LENGTH_SHORT).show();

                        return;
                    }else { //cargo la foto
                       // Bitmap bitmap1 = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null) + "/" + nombre_foto);
                      //  ComprasUtils cu = new ComprasUtils();
                        //      bitmap1 = cu.comprimirImagen(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + preguntaview.getTextoint().toString());
                        Bitmap bitmap1 = ComprasUtils.decodeSampledBitmapFromResource(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + preguntaview.getTextoint().toString(), 100, 100);

                        fotomos.setImageBitmap(bitmap1);
                        // fotomos.setLayoutParams(new LinearLayout.LayoutParams(350,150));
                        fotomos.setVisibility(View.VISIBLE);

                        btnrotar.setVisibility(View.VISIBLE);
                    }
                }
            }*/
    }
    public int getNumPregunta(){
        return preguntaAct.getId();
    }

    public Reactivo getPreguntaAct() {
        return preguntaAct;
    }

    public boolean isEdicion() {
        return isEdicion;
    }

    public InformeTemp getUltimares() {
        return ultimares;
    }

    class MayusTextWatcher implements TextWatcher {

        boolean mEditing;

        public MayusTextWatcher() {
            mEditing = false;
        }

        public synchronized void afterTextChanged(Editable s) {
            if(!mEditing) {
                mEditing = true;


                try{
                    if(s.length()>0)
                        s.replace(0, s.length(), s.toString().toUpperCase());
                } catch (NumberFormatException nfe) {
                    Log.d(TAG,"wwwwwwwwwww es un numero");
                    s.clear();
                }

                mEditing = false;
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //count es cantidad de caracteres que tiene
            preguntaview.aceptarSetEnabled(charSequence.length() > 0);

        }


    }




    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("picUri", nombre_foto);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState!=null)
            nombre_foto= savedInstanceState.getString("picUri");
    }

    public void atras(){
        InformeTemp resact=null;
        int idreact=0;
        if (isEdicion) {
            resact = getUltimares();
            idreact = resact.getId();
        }

        //busco el ant
        Reactivo reactivo = dViewModel.buscarReactivoAnterior(idreact,isEdicion);
        if(reactivo!=null) {
            preguntaview=null;
            //creo uno nuevo
            preguntaview=new DetalleInfView(getContext());
            this.ultimares=null;
            this.preguntaAct=reactivo;
           // this.isEdicion=false;
            svprin.removeAllViewsInLayout();

            crearPregunta();

           // args.putInt(DetalleProductoFragment.ARG_PREGACT,reactivo.getId() );
          //  args.putBoolean(DetalleProductoFragment.ARG_ESEDI,false);

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel = null;
        lcviewModel = null;
        dViewModel=null;
        preguntaview=null;

        tomadoDe=null;
        atributos=null;
        causas=null;
        root=null;

        preguntaAct=null;

        svprin=null;

        loadingDialog=null ;


        lcviewModel=null;
        nombre_foto=null;
        archivofoto=null;
    }

    public class PregBorraFoto{
        public void preguntarBorrarFoto(View cb,EditText txtruta,ImageView foto,ImageButton btnrotar,LinearLayout group) {
            if(((CheckBox)cb).isChecked()) {
                //veo si ya hay foto
                if(txtruta.getText()!=null&&!txtruta.getText().toString().equals("")) {
                    //pregunto si habrá más clientes
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
                    // dialogo1.setTitle(R.string.);
                    dialogo1.setMessage(R.string.eliminar_foto);
                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //todo eliminar foto
                            txtruta.setText("");
                            foto.setImageBitmap(null);

                            foto.setVisibility(View.GONE);
                            btnrotar.setVisibility(View.GONE);
                            preguntaview.aceptarSetEnabled(true);
                        }
                    });
                    dialogo1.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            ((CheckBox) cb).setChecked(false);
                            dialogo1.cancel();

                            //envio a la lista
                            //  NavHostFragment.findNavController(AbririnformeFragment.this).navigate(R.id.action_nuevotolista);

                        }
                    });
                    dialogo1.show();
                }
            }
        }
    }


}