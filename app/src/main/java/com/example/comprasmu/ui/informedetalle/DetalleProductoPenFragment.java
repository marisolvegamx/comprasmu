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
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.example.comprasmu.data.modelos.InformeTemp;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ProductoExhibido;
import com.example.comprasmu.data.modelos.Reactivo;
import com.example.comprasmu.data.remote.InformeEnvio;
import com.example.comprasmu.data.remote.UltimoInfResponse;
import com.example.comprasmu.data.remote.UltimosIdsResponse;
import com.example.comprasmu.services.SubirFotoService;
import com.example.comprasmu.ui.BackActivity;
import com.example.comprasmu.ui.RevisarFotoActivity;
import com.example.comprasmu.ui.informe.NuevoinformeViewModel;
import com.example.comprasmu.ui.listacompras.SelClienteFragment;
import com.example.comprasmu.ui.listadetalle.ListaCompraFragment;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.ui.visita.AbririnformeFragment;
import com.example.comprasmu.utils.CampoForm;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;
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

import static android.app.Activity.RESULT_OK;
import static com.example.comprasmu.ui.listacompras.TabsFragment.ARG_CLIENTESEL;

/***clase para mostrar los campos que faltan de capturar de la muestra uno por pantalla***/

public class DetalleProductoPenFragment extends Fragment {

    protected NuevoinformeViewModel mViewModel;
    protected NuevoDetalleViewModel dViewModel;
    CreadorFormulario cf;
    List<CampoForm> camposForm;
    protected List<CatalogoDetalle> tomadoDe;
    protected List<CatalogoDetalle>atributos;
    protected List<CatalogoDetalle>causas;
    protected static final String TAG="DETALLEPRODUCTOPEÑFRAG";
    SimpleDateFormat sdf;
    SimpleDateFormat sdfcodigo;
    View root;
    EditText textoint;
    Preguntasino pregunta;
    View respgen;
    Spinner spclientes ;
    protected Reactivo preguntaAct;
    ImageButton micbtn;
    ImageView fotomos;
    LinearLayout sv;
    boolean isEdicion;
    LoadingDialog loadingDialog ;
    public int estatusPepsi, estatusPen,estatusElec;
    NuevoDetalleViewModel.ProductoSel prodSel;
    public static  int REQUEST_CODE_TAKE_PHOTO=5;

    protected int tipoTienda;
    public static final int NUEVO_RESULT_OK =103 ;
    public static final Integer RecordAudioRequestCode = 1;
    //protected static final String cameraPerm = Manifest.permission.CAMERA;
    public final static String ARG_NUEVOINFORME="comprasmu.ni_idinforme";
    public static final String NUMMUESTRA="comprasmu.ni.nummuestra";
    protected ImageButton btnrotar;
    InformeTemp  ultimares;
    Button aceptar;
    Button validar;
    ListaDetalleViewModel lcviewModel;
    protected int plantaSel;
    protected String NOMBREPLANTASEL;
    protected long lastClickTime = 0;
   protected boolean yaestoyProcesando=false;
    List<DescripcionGenerica> clientesAsig;
    CheckBox nopermiso;

    public DetalleProductoPenFragment() {

    }
    public DetalleProductoPenFragment(Reactivo preguntaAct, boolean edicion) {
        this.preguntaAct = preguntaAct;
        this.isEdicion=edicion;
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
        root = inflater.inflate(R.layout.fragment_generic, container, false);
        /**llegan los datos del producto el cliente y la planta seleccionada
         * desde la lista de compra
         */
        try {
            Log.d(TAG,"creando fragment "+preguntaAct.getId());

            sv = root.findViewById(R.id.content_generic);
            aceptar = root.findViewById(R.id.btngaceptar);
            validar=root.findViewById(R.id.btngvalidar);
         //   mViewModel.cargarCatsContinuar();
            //si es la misma
            //reviso si es edicion o es nueva
            if(preguntaAct.getTabla().equals("I"))
                mViewModel.numMuestra=0;
            if(this.preguntaAct!=null)
                 ultimares=dViewModel.buscarxNombreCam(this.preguntaAct.getNombreCampo(),mViewModel.numMuestra);
            Log.d(TAG,"------"+Constantes.NM_TOTALISTA+"---"+mViewModel.consecutivo);
            dViewModel.reactivoAct=preguntaAct.getId();
            if(ultimares!=null) {    //es edicion
               isEdicion = true;
            //    if(preguntaAct.getId()==52||preguntaAct.getId()==53||preguntaAct.getId()==54){
               //     {
                        //reviso si ya tengo muestra
                Log.d(TAG,"mmmmmmmmmmm"+preguntaAct.getId());

           }
           else
           //if(this.preguntaAct.getId()==2||this.preguntaAct.getId()==3||this.preguntaAct.getId()==5)
               isEdicion=false;

            if(isEdicion) {
                aceptar.setEnabled(true);
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
                reiniciarDatos();
                //busco el total de prods en la lista
                if(Constantes.NM_TOTALISTA==0) {
                    InformeTemp resp = dViewModel.buscarxNombreCam("totalLista");
                    String valor = "";
                    if (resp != null) {
                        Constantes.NM_TOTALISTA = Integer.parseInt(resp.getValor());

                    }
                }
                if(preguntaAct.getId()==69||preguntaAct.getId()==70){
                    //no puedo modificar  avanzo a la siguiente

                    preguntaAct=dViewModel.buscarReactivoxId(preguntaAct.getSigId());
                    InformeTemp inft=dViewModel.buscarxNombreCam("informeid");
                    Log.d(TAG,"inf"+inft.getValor());
                    if(inft!=null) {
                        mViewModel.setIdInformeNuevo(Integer.parseInt(inft.getValor()));
                        mViewModel.consecutivo = inft.getConsecutivo();
                        Constantes.DP_CONSECUTIVO=mViewModel.consecutivo;
                    }
                    inft=dViewModel.buscarxNombreCam(Contrato.TablaInformeDet.causa_nocompra);

                    if(inft!=null) {
                        mViewModel.informe.setSinproducto(true);
                        mViewModel.informe.setCausa_nocompra(inft.getValor());
                    }
                    ultimares=null;
                    isEdicion=false;
                    aceptar.setEnabled(false);

                }
            }
            else {
                aceptar.setEnabled(false);
            }
            iniciarNumMuestra();
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

            if(preguntaAct.getType().equals(CreadorFormulario.SELECTCAT)||preguntaAct.getType().equals(CreadorFormulario.SELECTDES)||preguntaAct.getType().equals(CreadorFormulario.PSELECT)) {
                spclientes = root.findViewById(1001); //es el unico id que se genera en crear formlario
            }
             else
            if(preguntaAct.getType().equals(CreadorFormulario.PREGUNTASINO))
            {  pregunta=root.findViewById(1001);
            }else if(preguntaAct.getType().equals(CreadorFormulario.RADIOBUTTON))
            {  respgen=root.findViewById(1001);
            }else
            {
                textoint = root.findViewById(1001);
            }
            if(preguntaAct.getNombreCampo().equals("ticket_compra")) {
                nopermiso=root.findViewById(R.id.ckgnoperm);
                nopermiso.setVisibility(View.VISIBLE);

                nopermiso.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(textoint.getText()!=null)
                            aceptar.setEnabled(((CheckBox)view).isChecked());
                        else
                            aceptar.setEnabled(true);

                        preguntarBorrarFoto(view,textoint,fotomos,btnrotar,null);
                    }
                });
            }
            if(preguntaAct.getType().equals(CreadorFormulario.FECHAMASK)){
                textoint.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }
                    boolean mEditing=false;

                    public synchronized void afterTextChanged(Editable s) {
                        if(!mEditing) {
                            mEditing = true;

                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        //count es cantidad de caracteres que tiene
                        aceptar.setEnabled(charSequence.length() > 0);

                    }

                });
            }else
            if(textoint!=null&&preguntaAct.getId()!=57){ //los comentarios no son obligatorios
                textoint.addTextChangedListener(new BotonTextWatcher());

            }
            if(preguntaAct.getId()==57){ //los comentarios no son obligatorios
              //  textoint.addTextChangedListener(new MayusTextWatcher());
                textoint.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
                aceptar.setEnabled(true);
            }
            if(preguntaAct.getId()==57&&mViewModel.informe.isSinproducto()) {
                aceptar.setEnabled(false);
                textoint.addTextChangedListener(new BotonTextWatcher());
            }
            if(preguntaAct.getId()==58){ //para las siglas agrego el boton de validar
                validar.setVisibility(View.VISIBLE);
                aceptar.setVisibility(View.GONE);
                validar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        validar.setEnabled(false);
                       buscarPlanta();

                    }
                });

            }
            if(preguntaAct.getId()==70)
                aceptar.setEnabled(true);
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
            if(respgen!=null){
                RadioGroup botones=(RadioGroup)respgen;
                botones.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        aceptar.setEnabled(true);
                    }
                });
            }
            aceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    aceptar.setEnabled(false);
                    long currentClickTime=SystemClock.elapsedRealtime();
                    // preventing double, using threshold of 1000 ms
                    if (currentClickTime - lastClickTime < 5500){
                        return;
                    }

                    lastClickTime = currentClickTime;
                    Log.d(TAG,"di click :("+lastClickTime);
                    if(preguntaAct.getNombreCampo().equals("clientesId")){
                        guardarCliente();
                    }else

                    siguiente();

                }
            });
            prodSel=dViewModel.productoSel;
            if(preguntaAct.isBotonMicro()) {

                micbtn=root.findViewById(R.id.btnmicsiglas);
           /*     sspeechRecognizer = grabarVoz();
                micbtn.setVisibility(View.VISIBLE);*/
            }
            if(preguntaAct.getId()==57){ //comentarios
                //cambio el boton a finalizar y muestro alerta
                aceptar.setText(getString(R.string.enviar));
                aceptar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.botonvalido));
            }
            tipoTienda=Constantes.DP_TIPOTIENDA;
            estatusPepsi=mViewModel.visita.getEstatusPepsi(); //para saber si puede comprar pepsi
            estatusPen=mViewModel.visita.getEstatusPen();
            estatusElec=mViewModel.visita.getEstatusElec();
            Log.d(TAG,"tipo tienda -----------*"+estatusPepsi);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return root;
    }
    //btnaifotoexhibido
    public void crearFormulario(){

        camposForm=new ArrayList<>();
        CampoForm campo=new CampoForm();
        campo.label=preguntaAct.getLabel();
        campo.nombre_campo=preguntaAct.getNombreCampo();
        campo.type=preguntaAct.getType();
        campo.style=R.style.formlabel2;
        if(isEdicion)
            campo.value=ultimares.getValor();
        campo.id=1001;
        //para los catalogos
        if(preguntaAct.getType().equals(CreadorFormulario.SELECTCAT)||preguntaAct.getType().equals(CreadorFormulario.SELECTDES)){
            switch (preguntaAct.getNombreCampo()){
               /* case Contrato.TablaInformeDet.ATRIBUTOA:case Contrato.TablaInformeDet.ATRIBUTOB: case Contrato.TablaInformeDet.ATRIBUTOC:
                   // getAtributos();

                     campo.selectcat=atributos;
                    break;*/
                case Contrato.TablaInformeDet.ORIGEN:
                    getTomadoDe();
                 campo.selectcat=tomadoDe;
                    break;
              case "clientesId":
                    cargarClientes(campo);

                    break;

            }

        }

        if(campo.type.equals("agregarImagen")) {
            Log.e(TAG,"creando form"+preguntaAct.getLabel());
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
            btnrotar.setVisibility(View.GONE);
            btnrotar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rotar();
                }
            });

            if(isEdicion){
                 if(ultimares.getValor().equals("0")){
                    //veo si tengo el chbox
                   nopermiso=root.findViewById(R.id.ckgnoperm);
                   if(nopermiso!=null){
                       nopermiso.setChecked(true);
                       btnrotar.setVisibility(View.GONE);
                   }
                }else {
                     //  Bitmap bitmap1 = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null) + "/" + nombre_foto);
                     ComprasUtils cu = new ComprasUtils();
                     Bitmap bitmap1 = ComprasUtils.decodeSampledBitmapFromResource(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + ultimares.getValor(), 80, 80);

                     fotomos.setImageBitmap(bitmap1);
                     // fotomos.setLayoutParams(new LinearLayout.LayoutParams(350,150));
                     fotomos.setVisibility(View.VISIBLE);

                     btnrotar.setVisibility(View.VISIBLE);
                     btnrotar.setFocusableInTouchMode(true);
                     btnrotar.requestFocus();
                 }
            }
           // btnrotar.setVisibility(View.VISIBLE);

        }

        if(Contrato.TablaInformeDet.causa_nocompra.equals(campo.nombre_campo)) {
            //busco en el catalogo
            this.getCausas();

            HashMap<Integer, String> registro=new HashMap<>();
            for(CatalogoDetalle valores:causas) {
                registro.put(valores.getCad_idopcion(),valores.getCad_descripcionesp());
            }
           // registro.put(2,"NO HAY CODIGO");

            campo.select= registro;
        }
       /* if(preguntaAct.getType().equals(CreadorFormulario.PREGUNTASINO)) {
            campo.value

        }*/
            camposForm.add(campo);

        cf=new CreadorFormulario(camposForm,getContext());
        sv.addView(cf.crearFormulario());

    }
    public void reiniciarDatos(){
        if(preguntaAct.getId()==55)//ticket de compra
        {
            ((ContinuarInformeActivity)getActivity()).noSalir(true);
        }
        if(preguntaAct.getId()>=57&&preguntaAct.getId()!=67&&preguntaAct.getId()!=69) //ya tengo producto voy en siglas
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

            Log.d(TAG,"tipo muestras>>>>"+dViewModel.productoSel.nombreTipoMuestra);
            ((ContinuarInformeActivity)getActivity()).actualizarProdSel(dViewModel.productoSel);
        }
        if (preguntaAct.getId() >= 60&&preguntaAct.getId() !=69&&preguntaAct.getId() !=67) {//si compro prod
            InformeTemp resp=dViewModel.buscarxNombreCam("codigo",mViewModel.numMuestra);
            ((ContinuarInformeActivity)getActivity()).actualizarCodProd(resp.getValor());

        }
        if(dViewModel.productoSel!=null)
        {
            getTomadoDe();}
        if (preguntaAct.getId() >= 61&&preguntaAct.getId()!=69&&preguntaAct.getId() !=67) { //si hay prod

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


    }
    public void iniciarNumMuestra(){
        //es un nuevo informe o una nueva pregunta
        //if(mViewModel.numMuestra==0) {
        // &&preguntaAct.getId()!=5) {
        if (preguntaAct.getId() == 52)
            mViewModel.numMuestra = 1;

        if (preguntaAct.getId() == 53) {
            mViewModel.numMuestra = 2;
        }
        if (preguntaAct.getId() == 54) {
            mViewModel.numMuestra = 3;
        }
        if (preguntaAct.getId() == 67) {
            mViewModel.numMuestra = 4;
        }
        Log.e(TAG, "--nuevo nummuestras:" + mViewModel.numMuestra);

    }
    public void cargarClientes(CampoForm campo) {
       buscarClientes();
       campo.selectdes= clientesAsig;

          //  }
      //  else
        //    campo.selectdes= clientesAsig;
    }

    public void buscarClientes(){
        Integer[] clientesprev=dViewModel.tieneInforme(mViewModel.visita);
        //if (clientesAsig == null||clientesAsig.size()<1){
        List<ListaCompra> data=lcviewModel.cargarClientesSimpl(Constantes.CIUDADTRABAJO);
      /*  if(estatusPepsi==0){
            data=lcviewModel.cargarClientesSimplsp(Constantes.CIUDADTRABAJO,4);
        }else
        if(estatusPen==0){
            data=lcviewModel.cargarClientesSimplsp(Constantes.CIUDADTRABAJO,5);
        }else
        if(estatusElec==0){
            data=lcviewModel.cargarClientesSimplsp(Constantes.CIUDADTRABAJO,6);
        }*/
       clientesAsig = convertirListaaClientesE(data,clientesprev);
        Log.d(TAG, "*regresó de la consulta de clientes " +  clientesAsig.size()+"");
    }

    public  List<DescripcionGenerica> convertirListaaClientesE(List<ListaCompra> lista, Integer[] clientesprev){
        int i=0;
        List<DescripcionGenerica> mapa=new ArrayList<>();
        List<Integer> coninf;


        if(lista!=null)
            for (ListaCompra listaCompra: lista ) {
                DescripcionGenerica item=new DescripcionGenerica();
                if(estatusPepsi==0&&listaCompra.getClientesId()==4)
                    continue;
                if(estatusPen==0&&listaCompra.getClientesId()==5)
                    continue;
                if(estatusElec==0&&listaCompra.getClientesId()==6)
                    continue;
                Log.d(TAG,"estoy aqui"+listaCompra.getClientesId());
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
    }
    public void compraProd(View view,int nummuestra) {
        // Is the button now checked?
        boolean checked = ((Preguntasino) view).getRespuesta();
      //  Log.d(TAG,"CLICK EN RADIOBUTTON ID="+view.getId());
        if (checked)
        // Check which radio button was clicked
        switch(preguntaAct.getId()) {
            case 52: case 53: case 54:

                    // fue si mostrar lista de compra
                    verListaCompra(nummuestra);

                break;
            case 67:

                    // fue si mostrar lista de compra
                    verListaCompra(4);

                break;
            default: break;
        }
        else
            avanzarPregunta(preguntaAct.getSigAlt());
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

    public void guardarCliente(){
        DescripcionGenerica opcionsel = (DescripcionGenerica) spclientes.getSelectedItem();
        int valor = opcionsel.getId();
        lastClickTime=0;
        mViewModel.clienteSel=valor;
        Constantes.ni_clientesel=opcionsel.getNombre();
        mViewModel.informe=new InformeCompra();
        mViewModel.informe.setClienteNombre(opcionsel.getNombre());
        mViewModel.informe.setClientesId(valor);

        //actualizo barra
        ((ContinuarInformeActivity)getActivity()).actualizarCliente(mViewModel.informe);
        guardarResp();
        avanzarPregunta(preguntaAct.getSigId());

    }
    public void siguiente(){
        boolean resp=false;
        aceptar.setEnabled(false);

      /*  if (textoint != null) {
            String valor = textoint.getText().toString();
            if(valor.length()<=0){
                return;
            }

        }*/

        switch (preguntaAct.getNombreCampo()){

            case Contrato.TablaInformeDet.COSTO:
                String  valor = textoint.getText().toString();
                // float val=Float.parseFloat(valor);
                if(valor.equals("$0.00")){
                    Toast.makeText(getActivity(),"Costo inválido, verifique",Toast.LENGTH_LONG).show();


                }
                else resp=true;
                break;
               /* case Contrato.TablaInformeDet.CODIGO:
                   resp=validarCodigoprod();
                    break;*/
           case Contrato.TablaInformeDet.CADUCIDAD:
                    resp=validarFecha();
                    if(resp)
                        resp=validarCodigoprod();
                break;
            case "clientesId":

            break;
             default: resp=true; break;
        }

        if(resp)
        {

            if(preguntaAct.getId()==60 ) {
                 String  valor = textoint.getText().toString();

            //guardo el atributo para mostrarlo despues
            ((ContinuarInformeActivity)getActivity()).actualizarCodProd(valor);

            }else


            if(preguntaAct.getType().equals(CreadorFormulario.PREGUNTASINO)){
                //reviso la opcion seleccionada de compro prod para otros clientes
                if(!pregunta.getRespuesta()&&preguntaAct.getId()!=68) //se selecciono no
                {
                    //voy al altsig
                    guardarResp();
                    //guarda informe

                    avanzarPregunta(preguntaAct.getSigAlt());
                    return;

                }
            }

            if(preguntaAct.getSigId()==20000) //voy a lista de compra
            {
                guardarResp();
                compraProd(pregunta,preguntaAct.getId() - 51);
                //no funcionará para la 4a muestra
            }else
            if(preguntaAct.getSigId()==68) // comentarios termine inf
            {
                if(yaestoyProcesando)
                    return;
                yaestoyProcesando=true;
                loadingDialog = new LoadingDialog(getActivity());
                loadingDialog.startLoadingDialog();
                guardarResp();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

              //  guardarMuestra();
                try {
                    //guarda informe
                    this.actualizarInforme();
                    this.finalizar();
                    //limpiar tabla
                    limpiarTablTemp();
                    //reviso si hay más clientes, si no fin
                    buscarClientes();
                    if (clientesAsig != null && clientesAsig.size() > 0) {
                        loadingDialog.dismisDialog();
                         yaestoyProcesando=false;
                        avanzarPregunta(preguntaAct.getSigId());

                    } else {
                        //no hay mas clientes finalizo preinforme e informe
                        //    mViewModel.finalizarInforme();
                        //la muestra la guarde en la 42

                        Log.d(TAG, "no hay mas clientes");
                        //es la 43 //finalizo preinforme
                        mViewModel.finalizarVisita(mViewModel.visita.getId());
                        //   mViewModel.eliminarTblTemp();
                        loadingDialog.dismisDialog();
                        Toast.makeText(getActivity(), getString(R.string.informe_finalizado), Toast.LENGTH_SHORT).show();
                          yaestoyProcesando=false;
                        salir();
                        //  aceptar.setEnabled(true);
                        return;

                    }
                    }
                catch(Exception ex){
                    ex.printStackTrace();
                    loadingDialog.dismisDialog();
                    yaestoyProcesando=false;
                    Toast.makeText(getActivity(), "algo salió mal", Toast.LENGTH_SHORT).show();


                }

            }else
            if(preguntaAct.getSigId()==0)//terminé con preguntas de muestra
            {
                if(yaestoyProcesando)
                    return;
                yaestoyProcesando=true;
                aceptar.setEnabled(false);
                int sig=mViewModel.numMuestra+52;
                if(mViewModel.numMuestra==3)
                    sig=67;
                int nummuestra=mViewModel.numMuestra;
                guardarResp();

                //quito la info de la barra gris
                ((ContinuarInformeActivity)getActivity()).reiniciarBarra();

                guardarMuestra(sig);



            }else
            if(preguntaAct.getId()==69)//no hubo producto, causano compra
            {
                if(yaestoyProcesando)
                    return;
                loadingDialog = new LoadingDialog(getActivity());
                loadingDialog.startLoadingDialog();
                guardarResp();
                //para otras si genero consecutivo
                RadioGroup rg = (RadioGroup) respgen;
                String valor=null;
                int selectedRadioButtonId = rg.getCheckedRadioButtonId();
                if (selectedRadioButtonId != -1) {
                  /*  RadioButton selectedRadioButton = root.findViewById(selectedRadioButtonId);
                    valor = selectedRadioButton.getText().toString();*/
                    //  valor=valor.toUpperCase();
                    valor = selectedRadioButtonId + "";
                }


                //busco planta
                List<ListaCompra> listapl=lcviewModel.cargarPlantas(mViewModel.visita.getCiudad(),mViewModel.clienteSel);

                if(listapl.size()>0)
                {
                    Log.d(TAG,"poniendo el cliente"+listapl.get(0).getClienteNombre());
                    //voy directo a la lista
                    plantaSel=listapl.get(0).getPlantasId() ;
                    NOMBREPLANTASEL=listapl.get(0).getPlantaNombre();
                    if(valor!=null)
                        if(valor.equals("7")) //es otras
                        {
                            //generar consecutivo tienda
                            int consecutivo=mViewModel.getConsecutivo(plantaSel,getActivity(), this);
                            Log.d(TAG,"*genere cons="+consecutivo);
                            mViewModel.informe.setConsecutivo(consecutivo);
                            mViewModel.consecutivo=consecutivo;
                            mViewModel.guardarResp(0,0,plantaSel+"","plantasId","I",mViewModel.consecutivo,false);
                            mViewModel.guardarResp(0,0,NOMBREPLANTASEL+"","plantaNombre","I",mViewModel.consecutivo,false);
                            mViewModel.guardarResp(0,0,listapl.get(0).getClienteNombre(),"clienteNombre","I",mViewModel.consecutivo,false);
                            guardarMuestra(preguntaAct.getSigId());
                            loadingDialog.dismisDialog();
                            //  consecutivo.removeObservers(DetalleProductoFragment.this);

                        }else {
                            mViewModel.guardarResp(0,0,plantaSel+"","plantasId","I",0,false);
                            mViewModel.guardarResp(0,0,NOMBREPLANTASEL+"","plantaNombre","I",0,false);
                            mViewModel.guardarResp(0,0,listapl.get(0).getClienteNombre(),"clienteNombre","I",0,false);
                            guardarMuestra(preguntaAct.getSigId());
                            loadingDialog.dismisDialog();
                        }

                }else {
                    guardarMuestra(preguntaAct.getSigId());
                    loadingDialog.dismisDialog();
                }

                // avanzarPregunta(preguntaAct.getSigId());

            }else
            if(preguntaAct.getId()==58){ //son la siglas y ya seleccioné producto
                //creo informe e informe detalle con datos del producto
                guardarResp();
                guardarProductoTemp();
                avanzarPregunta(preguntaAct.getSigId());
            }else
            if(preguntaAct.getId()==68){ //hay ootro cliente
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
                //es la 68 //finalizo informe

              //  mViewModel.finalizarInforme();
                //la muestra la guarde en la 42
                if(!pregunta.getRespuesta()) //se selecciono no
                {
                    Log.d(TAG,"dice que no");
                    //es la 68 //finalizo preinforme
                    finalizarPreinforme();
                    return;
                }

                //avanzarPregunta(1);
                mViewModel.eliminarTblTemp();
                //me voy a la lista de informes
             //   getActivity().finish();
                Bundle bundle = new Bundle();
                bundle.putInt(ContinuarInformeActivity.INFORMESEL,mViewModel.visita.getId());

                //NavHostFragment.findNavController(this).navigate(R.id.action_visitatonuevo,bundle);
                Intent intento1=new Intent(getActivity(), ContinuarInformeActivity.class);
                intento1.putExtras(bundle);
                requireActivity().finish();
                startActivity(intento1);
            }
            else{
                if(preguntaAct.getId()>50)
                      guardarResp();
                avanzarPregunta(preguntaAct.getSigId());
            }
        }
        aceptar.setEnabled(true);
        lastClickTime=0;
    }
    public void reiniciarVars(){
        cf=null;
        camposForm=null;
        tomadoDe=null;
        atributos=null;
        causas=null;
        textoint=null;
        pregunta=null;
        spclientes =null;
        isEdicion=false;
        loadingDialog=null ;
      prodSel=null;
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
            Log.e(TAG,"Algo salió mal al enviar"+ex.getMessage());
            Toast.makeText(getContext(),"Algo salio mal al enviar",Toast.LENGTH_SHORT).show();
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
        mViewModel.informe=null;
        mViewModel.setIdInformeNuevo(0);
        mViewModel.numMuestra=0;
        mViewModel.consecutivo=0;

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
                mViewModel.eliminarTblTemp();

                Toast.makeText(getActivity(), getString(R.string.informe_finalizado),Toast.LENGTH_SHORT).show();
                salir();
            }
        });
        dialogo1.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                //no hago nada
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
    String sigmuestra="segundaMuestra";
    public void guardarMuestra(int sigui){
        try {

            if(mViewModel.numMuestra ==4)
                sigui=55; //ticket de compra
            final int sig=sigui;
            //Creo el informe en nuevo informe y lo busco aqui
            //necestio saber si ya habia guardado informe
            //veo si ya existe el informe o hay que crearlo
            Log.d(TAG, "primero guardando informe"+mViewModel.numMuestra+"--"+mViewModel.getIdInformeNuevo());
        //   exit(0);

            if(mViewModel.numMuestra==2)
                 sigmuestra="terceraMuestra";
            if(mViewModel.numMuestra==3)
                sigmuestra="cuartaMuestra";
            if (mViewModel.numMuestra == 1 || mViewModel.getIdInformeNuevo() <= 0) {

                //busco el consecutivo
               MutableLiveData<Integer> idInformeNuevo = guardarInforme();
                Log.d(TAG, "guardando informe"+mViewModel.numMuestra+"--"+mViewModel.getIdInformeNuevo());
                //
               idInformeNuevo.observe(getViewLifecycleOwner(), new Observer<Integer>() {
                   @Override
                   public void onChanged(Integer idnvo) {
                       Log.d(TAG, "se creo el informe" + idnvo);
                       mViewModel.informe.setId(idnvo);
                       mViewModel.setIdInformeNuevo(idnvo);
                       if(!mViewModel.informe.isSinproducto()) {
                           //si tengo detalle
                          // Log.d(TAG,"guardando  muestras "+mViewModel.numMuestra);

                           //    List<Integer> muestras= dViewModel.muestrasTotales();
                           //  for(int x:muestras) {
                           int nuevoid = dViewModel.insertarMuestra(mViewModel.getIdInformeNuevo(), mViewModel.numMuestra);
                           //guardo la muestra
                           if (nuevoid > 0) {
                               dViewModel.setIddetalleNuevo(nuevoid);
                               //si ya se guardó lo agrego en la lista de compra
                               ListaDetalleViewModel lcviewModel = new ViewModelProvider(DetalleProductoPenFragment.this).get(ListaDetalleViewModel.class);
                               Log.d(TAG,"voy a descontar"+dViewModel.icdNuevo.getCaducidad());
                               int res=lcviewModel.comprarMuestraPen(dViewModel.icdNuevo.getComprasId(), dViewModel.icdNuevo.getComprasDetId(), sdfcodigo.format(dViewModel.icdNuevo.getCaducidad()), dViewModel.icdNuevo.getTipoMuestra(),dViewModel.icdNuevo,dViewModel.productoSel.plantaSel,Constantes.INDICEACTUAL);
                               //limpiar tabla temp
                               //   limpiarTablTempMenCli();
                               mViewModel.eliminarMuestra(mViewModel.numMuestra);

                               dViewModel.setIddetalleNuevo(0);
                               dViewModel.icdNuevo=null;

                               mViewModel.guardarResp(mViewModel.getIdInformeNuevo(), dViewModel.getIddetalleNuevo(), "true", sigmuestra, "I", mViewModel.consecutivo, true);

                           }
                           yaestoyProcesando=false;
                           avanzarPregunta(sig);

                           //  }
                       }else {
                           dViewModel.setIddetalleNuevo(0);
                           dViewModel.icdNuevo = null;
                           //guardo el numinforme para cuando se creen los coment
                           mViewModel.guardarResp( mViewModel.getIdInformeNuevo() ,0, mViewModel.getIdInformeNuevo()+"" ,"informeid","I",mViewModel.consecutivo,false);
                           yaestoyProcesando=false;
                           avanzarPregunta(sig);
                       }
                       mViewModel.numMuestra++;
                      // idInformeNuevo.removeObservers(DetalleProductoPenFragment.this);

                   }

               });

            }else
            if(!mViewModel.informe.isSinproducto()) {
                //si tengo detalle
                Log.d(TAG,"guardando  muestras");

           //    List<Integer> muestras= dViewModel.muestrasTotales();
             //  for(int x:muestras) {
                   int nuevoid = dViewModel.insertarMuestra(mViewModel.getIdInformeNuevo(), mViewModel.numMuestra);
                   //guardo la muestra
                   if (nuevoid > 0) {
                       dViewModel.setIddetalleNuevo(nuevoid);
                       //si ya se guardó lo agrego en la lista de compra
                       ListaDetalleViewModel lcviewModel = new ViewModelProvider(this).get(ListaDetalleViewModel.class);
                         Log.d(TAG,"qqqqqqqqqqqqqqq"+dViewModel.icdNuevo.getCaducidad());
                       int res=lcviewModel.comprarMuestraPen(dViewModel.icdNuevo.getComprasId(), dViewModel.icdNuevo.getComprasDetId(), sdfcodigo.format(dViewModel.icdNuevo.getCaducidad()), dViewModel.icdNuevo.getTipoMuestra(),dViewModel.icdNuevo,dViewModel.productoSel.plantaSel,Constantes.INDICEACTUAL);
                       //limpiar tabla temp
                       //   limpiarTablTempMenCli();
                       mViewModel.eliminarMuestra(mViewModel.numMuestra);
                       dViewModel.setIddetalleNuevo(0);
                       dViewModel.icdNuevo=null;
                       yaestoyProcesando=false;
                       mViewModel.guardarResp(mViewModel.getIdInformeNuevo(), dViewModel.getIddetalleNuevo(), "true", sigmuestra, "I", mViewModel.consecutivo, true);
                       avanzarPregunta(sig);

                   }
                mViewModel.numMuestra++;
             //  }
            }else {
                dViewModel.setIddetalleNuevo(0);
                dViewModel.icdNuevo = null;
                //guardo el numinforme para cuando se creen los coment
                mViewModel.guardarResp( mViewModel.getIdInformeNuevo() ,0, mViewModel.getIdInformeNuevo()+"" ,"informeid","I",mViewModel.consecutivo,false);
                yaestoyProcesando=false;
                avanzarPregunta(sig);
                mViewModel.numMuestra++;
            }

        }catch (Exception ex){
            ex.printStackTrace();
            Log.e(TAG,ex.getMessage());
            Toast.makeText(getActivity(), "No se pudo guardar la muestra", Toast.LENGTH_LONG).show();

        }

    }
    public InformeEnvio preparaInforme(){
        InformeEnvio envio=new InformeEnvio();
        Log.d(TAG,"wwwwwwwww"+mViewModel.visita.getEstatus());

        if(mViewModel.visita.getEstatusSync()==0) {
            envio.setVisita(mViewModel.visita);

        }

        envio.setInformeCompra(mViewModel.informe);
        mViewModel.cargarMuestras(mViewModel.informe.getId());
        //busco el prod exhibido
        envio.setIndice(mViewModel.visita.getIndice());
        envio.setClaveUsuario(Constantes.CLAVEUSUARIO);
        List<ProductoExhibido> productoExhibidos=mViewModel.buscarProdExhiPend();
        Log.d(TAG,"buscando prodexh"+productoExhibidos.size());

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
            msgIntent.putExtra(SubirFotoService.EXTRA_INDICE,informe.getVisita().getIndice());
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
        //
    //
        ValidadorDatos valdat=new ValidadorDatos();
        try {
            fechacad=sdfcodigo.parse(textoint.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), getString( R.string.error_fecha_formato), Toast.LENGTH_LONG).show();


            return false;
        }
       // if (dViewModel.productoSel.clienteNombre.trim().equals("PEÑAFIEL")) {
            Date hoy=new Date();

            if (fechacad.getTime()<=hoy.getTime()) { //ya caducó fechacad>=hoy
                Toast.makeText(getActivity(), getString(R.string.error_fecha_caduca), Toast.LENGTH_LONG).show();

                return false;
            }else
            //busco que no haya otra muestra con la misma fecha en el muestreo
            //ni en el mismo informe
                if(dViewModel.productoSel.tipoMuestra!=3||mViewModel.numMuestra>1) //solo si no es bu
            if(this.buscarMuestraCodigoPeniafiel(fechacad)) {
                Toast.makeText(getActivity(), getString(R.string.error_codigo_per), Toast.LENGTH_LONG).show();

                return false;
            }
            return true;

        //}else{
       //     Log.d(TAG,"como que no es peñafiel");
      //  }
      //  return false;
    }

    public boolean validarCodigoprod(){
         try {
            fechacad=sdfcodigo.parse(textoint.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(dViewModel.productoSel.clienteSel==5||dViewModel.productoSel.clienteSel==6) {
            ValidadorDatos valdat = new ValidadorDatos();
            if (!valdat.validarCodigoprodPen(textoint.getText().toString(), dViewModel.productoSel.codigosnop)) {
                if(valdat.mensaje>0)
                    Toast.makeText(getActivity(), getString(valdat.mensaje), Toast.LENGTH_LONG).show();
                return false;
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

    public void avanzarPregunta(int sig){
        LiveData<Reactivo> nvoReac = dViewModel.buscarReactivo(sig);

        //busco el siguiente
        nvoReac.observe(getViewLifecycleOwner(), new Observer<Reactivo>() {
            @Override
            public void onChanged(Reactivo reactivo) {
                 if(sig==1) //pregunta de cliente o confirmacion vuelvo al detalleproducto1
        {

            DetalleProductoFragment nvofrag = new DetalleProductoFragment(reactivo,false);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
// Definir una transacción
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
// Remplazar el contenido principal por el fragmento
            fragmentTransaction.replace(R.id.continf_fragment, nvofrag);
         //   fragmentTransaction.addToBackStack(null);
// Cambiar
            fragmentTransaction.commit();
        }else {
                     DetalleProductoPenFragment nvofrag = new DetalleProductoPenFragment(reactivo, false);
                     FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
// Definir una transacción
                     FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
// Remplazar el contenido principal por el fragmento
                     fragmentTransaction.replace(R.id.continf_fragment, nvofrag);
                  //   fragmentTransaction.addToBackStack(null);
// Cambiar
                     fragmentTransaction.commit();
                 }
            }
        });
    }


    //guardo en tabla temp
    public void guardarResp() {
        Log.d(TAG, "guardando en temp*****" + preguntaAct.getId());


        String valor = null;
        if (textoint != null) {
            valor = textoint.getText().toString();
            valor = valor.toUpperCase();
        } else {
            if (preguntaAct.getType().equals(CreadorFormulario.SELECTCAT)) {
                CatalogoDetalle opcionsel = (CatalogoDetalle) spclientes.getSelectedItem();
                valor = opcionsel.getCad_idopcion() + "";
            }
            if (preguntaAct.getType().equals(CreadorFormulario.SELECTDES)) {
                DescripcionGenerica opcionsel = (DescripcionGenerica) spclientes.getSelectedItem();
                valor = opcionsel.getId() + "";
            }
            if (preguntaAct.getType().equals(CreadorFormulario.PSELECT)) {
                valor = spclientes.getSelectedItemId() + " ";

            }
            if (preguntaAct.getType().equals(CreadorFormulario.RADIOBUTTON)) {
                RadioGroup rg = (RadioGroup) respgen;

                int selectedRadioButtonId = rg.getCheckedRadioButtonId();
                if (selectedRadioButtonId != -1) {
                  /*  RadioButton selectedRadioButton = root.findViewById(selectedRadioButtonId);
                    valor = selectedRadioButton.getText().toString();*/
                    //  valor=valor.toUpperCase();
                    valor = selectedRadioButtonId + "";
                }

            }
            if (preguntaAct.getType().equals(CreadorFormulario.PREGUNTASINO)) {
                valor = pregunta.getRespuesta() + "";
            }
        }
            //  if(!preguntaAct.getType().equals(CreadorFormulario.AGREGARIMAGEN))
            //paso a mayusculas

            Log.d(TAG, "guardando en temp" + preguntaAct.getId() + "val" + valor);
        if((preguntaAct.getId()==55||preguntaAct.getId()==75)&& nopermiso.isChecked())//es ticket
        {
            mViewModel.guardarResp(mViewModel.getIdInformeNuevo(), dViewModel.getIddetalleNuevo(), "0", preguntaAct.getNombreCampo(), preguntaAct.getTabla(), mViewModel.consecutivo, true);
            mViewModel.guardarResp(mViewModel.getIdInformeNuevo(), dViewModel.getIddetalleNuevo(), "1", "ticket_noemiten", preguntaAct.getTabla(), mViewModel.consecutivo, true);

        }else
            if (preguntaAct.getId() > 0 && valor != null && valor.length() > 0) {
              //actualizo la visita
                if(mViewModel.visita.getEstatus()!=3)
                    mViewModel.actualizarVisita(mViewModel.visita.getId(),3);
                mViewModel.guardarResp(mViewModel.getIdInformeNuevo(), dViewModel.getIddetalleNuevo(), valor, preguntaAct.getNombreCampo(), preguntaAct.getTabla(), mViewModel.consecutivo, true);
            }

    }
        String nombre_foto;
    File archivofoto;

    public void tomarFoto(){
        Activity activity=this.getActivity();
        Intent intento1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");

        String dateString = format.format(new Date());
        String state = Environment.getExternalStorageState();

        File baseDirFile;
         if(ComprasUtils.getAvailableMemory(getActivity()).lowMemory)
        {
            Toast.makeText(getActivity(), "No hay memoria suficiente para esta accion", Toast.LENGTH_SHORT).show();

            return;
        }else {
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
                 // Log.e(TAG, "****"+foto.getAbsolutePath());
                 archivofoto = new File(baseDirFile, nombre_foto);
             } catch (Exception ex) {
                 ex.printStackTrace();
                 Toast.makeText(activity, "No se encontró almacenamiento externo", Toast.LENGTH_SHORT).show();
                 return;

             }
             Uri photoURI = FileProvider.getUriForFile(activity,
                     "com.example.comprasmu.fileprovider",
                     archivofoto);
             intento1.putExtra(MediaStore.EXTRA_OUTPUT, photoURI); //se pasa a la otra activity la referencia al archivo
             //intento1.putExtra("origen", origen);
             if (fotomos != null) {

                 startActivityForResult(intento1, REQUEST_CODE_TAKE_PHOTO);

             }
         }

        }
    public void rotar(){
        String foto=textoint.getText().toString();
            if(ComprasUtils.getAvailableMemory(getActivity()).lowMemory)
            {
                Toast.makeText(getActivity(), "No hay memoria suficiente para esta accion", Toast.LENGTH_SHORT).show();

                return;
            }else
                RevisarFotoActivity.rotarImagen(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" +foto,fotomos);
        }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //    super.onActivityResult(requestCode, resultCode, data);
               Log.d(TAG,"vars"+requestCode +"--"+ nombre_foto);
        if ((requestCode == REQUEST_CODE_TAKE_PHOTO) && resultCode == RESULT_OK) {
            try {
                //   super.onActivityResult(requestCode, resultCode, data);

                if (archivofoto != null && archivofoto.exists()) {

                        //envio a la actividad dos para ver la foto
                        //    Intent intento1 = new Intent(getActivity(), RevisarFotoActivity.class);
                        //  intento1.putExtra("ei.archivo", nombre_foto);

                        textoint.setText(nombre_foto);

                        if (ComprasUtils.getAvailableMemory(getActivity()).lowMemory) {
                            Toast.makeText(getActivity(), "No hay memoria suficiente para esta accion", Toast.LENGTH_SHORT).show();

                            return;
                        } else {
                            // Bitmap bitmap1 = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null) + "/" + nombre_foto);
                            ComprasUtils cu = new ComprasUtils();
                            cu.comprimirImagen(archivofoto.getAbsolutePath());
                            Bitmap bitmap1 = ComprasUtils.decodeSampledBitmapFromResource(archivofoto.getAbsolutePath(), 100, 100);
                            fotomos.setImageBitmap(bitmap1);
                            // fotomos.setLayoutParams(new LinearLayout.LayoutParams(350,150));
                            fotomos.setVisibility(View.VISIBLE);

                            btnrotar.setVisibility(View.VISIBLE);
                            btnrotar.setFocusableInTouchMode(true);
                            btnrotar.requestFocus();
                            nombre_foto = null;
                            archivofoto=null;
                            if(nopermiso!=null) {
                                nopermiso.setChecked(false);
                            }
                        }




                } else {
                    Toast.makeText(getActivity(), "Algo salió mal intente de nuevo", Toast.LENGTH_SHORT).show();


                    Log.e(TAG, "Algo salió mal???");
                }
            }catch (Exception ex){
                Toast.makeText(getActivity(), "No hay memoria suficiente para esta accion, intente de nuevo", Toast.LENGTH_SHORT).show();

                ex.printStackTrace();

                //Log.e(TAG, );
            }



        } else if(requestCode==BackActivity.REQUEST_CODE)
    {
        super.onActivityResult(requestCode, resultCode, data);
        //capturé muestra
        if(resultCode==NUEVO_RESULT_OK) {
        enviaraSiglas();



        }

        }   else
            {
                Log.e(TAG,"Algo salió muy mal**");
            }
    }

    protected void enviaraSiglas(){
        // totalLista=data.getIntExtra(ARG_TOTALLISTA, 0);
        //muestro el de 3a muestra
        // if(Constantes.NM_TOTALISTA>=16)
        //  tercera.setVisible(View.VISIBLE);
        if(Constantes.productoSel!=null)
        {
            dViewModel.productoSel = Constantes.productoSel;
            //guardo el total de la lista
            //generar consecutivo tienda
            Log.d(TAG, ">>>> "+  dViewModel.productoSel.clienteNombre);
            if(mViewModel.consecutivo==0) {
                int consecutivo = mViewModel.getConsecutivo(dViewModel.productoSel.plantaSel, getActivity(), this);
                //  Log.d(TAG, "*genere cons=" + consecutivo);
                        Log.d(TAG, "genere cons=" + consecutivo);

                        mViewModel.informe.setConsecutivo(consecutivo);

                        mViewModel.consecutivo = consecutivo;
                        Constantes.DP_CONSECUTIVO=consecutivo;
                        //actualizo barra
                        ((ContinuarInformeActivity) getActivity()).actualizarProdSel(dViewModel.productoSel);

                        mViewModel.guardarResp(0, 0, Constantes.NM_TOTALISTA + "", "totalLista", "", mViewModel.consecutivo, false);
                        ((ContinuarInformeActivity)getActivity()).actualizarCliente(mViewModel.informe);

                        avanzarPregunta(58);

            }else
            {
                //actualizo barra
                ((ContinuarInformeActivity) getActivity()).actualizarProdSel(dViewModel.productoSel);

                mViewModel.guardarResp(0, 0, Constantes.NM_TOTALISTA + "", "totalLista", "", mViewModel.consecutivo, false);

                avanzarPregunta(58);
            }
        }else
            Log.e(TAG,"Algo salió muy mal al elegir el producto");

        //lo busco y cargo
    }
    protected void guardarProductoTemp(){
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
        //TODO validar siglas
   public void  buscarPlanta() {
       String siglas = textoint.getText().toString();
       textoint.setEnabled(false);
       if (!siglas.equals("")) {
           EnvioListener listener = new EnvioListener();
           //bloqueo la pnatalla y hago la peticion
           dViewModel.buscarPlantaPen(siglas,listener );
           //actualizo siglas en prod sel
           dViewModel.productoSel.siglas=siglas;
       }else {
           textoint.setEnabled(true);
           validar.setEnabled(true);
       }
   }
        Date fechacad = null;
       boolean res;
        //validar siglas
   //true= ya existe un codigo igual

    public boolean buscarMuestraCodigoPeniafiel(Date caducidadnva){
        //busco en el mismo informe
        return dViewModel.buscarMuestraCodigoPen(Constantes.INDICEACTUAL,dViewModel.productoSel.plantaSel,dViewModel.productoSel,"",caducidadnva,getViewLifecycleOwner(),dViewModel.productoSel.codigosperm);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel = null;
        lcviewModel = null;
        dViewModel=null;
        cf=null;
        camposForm=null;
        tomadoDe=null;
        atributos=null;
        causas=null;
        root=null;
        textoint=null;
        pregunta=null;
        respgen=null;
        spclientes=null ;
        preguntaAct=null;
        micbtn=null;
        fotomos=null;
        sv=null;
        btnrotar=null;
        loadingDialog=null ;
        aceptar=null;
        validar=null;
        lcviewModel=null;
        nombre_foto=null;
        archivofoto=null;
    }

    @Override
   public void onDestroy() {

                super.onDestroy();

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


    public void verListaCompra(int nummuestra){
        /* b2undle.putString("plantaNombre", listaSeleccionable.get(i).getNombre());*/
        /*   NavHostFragment.findNavController(this).navigate(R.id.action_selclientetolistacompras,bundle);
         */
        //NavHostFragment.findNavController(this).navigate(R.id.action_lista compra);
        String opcion = "";
        Intent intento1 = new Intent(getActivity(), BackActivity.class);

        //busco la planta
        if(nummuestra<2) {
            List<ListaCompra> listapl = lcviewModel.cargarPlantas(mViewModel.visita.getCiudad(), mViewModel.clienteSel);
             Log.d(TAG,"todavia no se que hacer"+listapl.size()+"viewmodel cliente"+mViewModel.clienteSel);

            if (listapl.size() > 1) {

                //Log.d(TAG,"todavia no se que hacer");
                opcion = BackActivity.OP_SELPLANTA;
                intento1.putExtra(SelClienteFragment.ARG_TIPOCONS, "action_selclitolista");
                intento1.putExtra(ListaCompraFragment.ARG_CLIENTESEL, mViewModel.clienteSel);
            } else if (listapl.size() > 0) {
                //voy directo a la lista
                plantaSel = listapl.get(0).getPlantasId();
                NOMBREPLANTASEL = listapl.get(0).getPlantaNombre();
                opcion = BackActivity.OP_LISTACOMPRA;

            }
        }else{
            //ya tengo la planta
            InformeTemp inf= dViewModel.buscarxNombreCam("plantasId");
            if(inf!=null) {
                plantaSel=Integer.parseInt(inf.getValor());

            }
            inf= dViewModel.buscarxNombreCam("plantaNombre");
            InformeTemp inf2= dViewModel.buscarxNombreCam("clienteNombre");
            if(inf2!=null) {
                NOMBREPLANTASEL=inf.getValor();

            }

            opcion = BackActivity.OP_LISTACOMPRA;
        }

        //ya existe el informe
        intento1.putExtra(DetalleProductoPenFragment.ARG_NUEVOINFORME, mViewModel.getIdInformeNuevo());
        intento1.putExtra(BackActivity.ARG_FRAGMENT,opcion);
        intento1.putExtra("ciudadSel", mViewModel.visita.getCiudadId());
        intento1.putExtra("ciudadNombre", mViewModel.visita.getCiudad());
        intento1.putExtra(ListaCompraFragment.ARG_PLANTASEL, plantaSel);
        intento1.putExtra(ListaCompraFragment.ARG_NOMBREPLANTASEL, NOMBREPLANTASEL);
        intento1.putExtra(ListaCompraFragment.ARG_MUESTRA, "true");

        Constantes.DP_CONSECUTIVO = mViewModel.consecutivo;
        // intento1.putExtra(ListaCompraFragment.ARG_MUESTRA,"true");
        // spclientes = root.findViewById(1001);
        Log.d(TAG, " antes de ir a listacom planta" + plantaSel + "--" + NOMBREPLANTASEL);
        intento1.putExtra(ARG_CLIENTESEL, mViewModel.clienteSel);
        //intento1.putExtra(ARG_CLIENTENOMBRE,);
        intento1.putExtra(DetalleProductoPenFragment.NUMMUESTRA, nummuestra);

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
            if(textoint!=null) {
               // Log.d(TAG, "genere cons=" + textoint.getText().toString());

                if (!textoint.getText().toString().equals("") && preguntaAct.getType().equals(CreadorFormulario.AGREGARIMAGEN)) {
                    if(
                            ComprasUtils.getAvailableMemory(getActivity()).lowMemory)
                    {
                        Toast.makeText(getActivity(), "No hay memoria suficiente para esta accion", Toast.LENGTH_SHORT).show();

                        return;
                    }else { //cargo la foto
                       // Bitmap bitmap1 = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null) + "/" + nombre_foto);
                      //  ComprasUtils cu = new ComprasUtils();
                        Bitmap bitmap1 = ComprasUtils.decodeSampledBitmapFromResource(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + textoint.getText().toString(), 100, 100);

                        fotomos.setImageBitmap(bitmap1);
                        // fotomos.setLayoutParams(new LinearLayout.LayoutParams(350,150));
                        fotomos.setVisibility(View.VISIBLE);

                      //  btnrotar.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
     public int getNumPregunta(){
            return preguntaAct.getId();
     }


    public InformeTemp getUltimares() {
        return ultimares;
    }
    protected void preguntarBorrarFoto(View cb,EditText txtruta,ImageView foto,ImageButton btnrotar,LinearLayout group) {
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
                        //eliminar foto
                        txtruta.setText("");
                        foto.setImageBitmap(null);

                        foto.setVisibility(View.GONE);
                        btnrotar.setVisibility(View.GONE);
                        aceptar.setEnabled(true);
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

       public class EnvioListener {

           public EnvioListener() {
           }


           public void guardarRespuestaInf(CatalogoDetalle planta) {
               if (planta != null) {
                   //muestro la planta y muestro el boton de seguir y desbloqueo
                   TextView txtplanta=root.findViewById(R.id.txtfgplanta);
                   txtplanta.setText(planta.getCad_descripcionesp());
                   txtplanta.setVisibility(View.VISIBLE);
                   aceptar.setVisibility(View.VISIBLE);
                   validar.setVisibility(View.GONE);
                  // dViewModel.productoSel.plantaSel=planta.getCad_idopcion();
                 //  dViewModel.productoSel.plantaNombre=planta.getCad_descripcionesp();
                //  siguiente();
                  // Toast.makeText(getContext(), "Las siglas no corresponden a lguna planta", Toast.LENGTH_LONG).show();
                   //actualizo barra
                   ((ContinuarInformeActivity) getActivity()).actualizarProdSel(dViewModel.productoSel);



               } else {
                   Toast.makeText(getContext(), "Las siglas no corresponden a una planta", Toast.LENGTH_LONG).show();
                    validar.setEnabled(true);
                    textoint.setEnabled(true);
               }

           }

       }


}