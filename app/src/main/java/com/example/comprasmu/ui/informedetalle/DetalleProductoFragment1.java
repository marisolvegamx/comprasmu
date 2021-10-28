package com.example.comprasmu.ui.informedetalle;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.Contrato;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;

import com.example.comprasmu.databinding.FragmentDetalleProducto1Binding;
import com.example.comprasmu.ui.RevisarFotoActivity;
import com.example.comprasmu.ui.informe.NuevoinformeFragment;
import com.example.comprasmu.ui.informe.NuevoinformeViewModel;

import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.utils.CampoForm;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;

import java.io.File;
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


public class DetalleProductoFragment1 extends Fragment {
    public static final int EDIT_RESULT_OK = 101;
    public static final int NUEVO_RESULT_OK =102 ;
    private NuevoinformeViewModel mViewModel;
    private NuevoDetalleViewModel dViewModel;
    CreadorFormulario cf;
    List<CampoForm> camposForm;
    private List<CatalogoDetalle> tomadoDe;
    private List<CatalogoDetalle>atributos;
    MenuItem fav;
    private static final int SELECT_FILE = 1;
    SimpleDateFormat sdf;
    View root;
    private int numMuestra,tipoTienda;

    private static final String TAG="DETALLEPRODUCTOFRAG1";
    public static final String ARG_IDMUESTRA="comprasmu.argidmuestra";
    public static final String ARG_IDINFORME="comprasmu.argidinforme";
    public static final Integer RecordAudioRequestCode = 1;

    private EditText foto1;
    EditText fecha;
    EditText codigo;
    private ImageView imageView;
    public static  int REQUEST_CODE_TAKE_PHOTO=1;
    public static  int REQUEST_CODE_2=2;
    InformeCompraDetalle informeEdit;
    public ImagenDetalle efoto_codigo_produccion;
    public ImagenDetalle eenergia;
    public ImagenDetalle efoto_num_tienda;
    public ImagenDetalle efoto_atributoa;
    public ImagenDetalle efoto_atributob;
    public ImagenDetalle efoto_atributoc;
    public ImagenDetalle emarca_traslape;
    public ImagenDetalle eetiqueta_evaluacion;
    SpeechRecognizer sspeechRecognizer,cspeechRecognizer,cpspeechRecognizer,csspeechRecognizer;
    LinearLayout sv;
    EditText siglas;
    private int[] arrCampos={R.id.btnchecksiglas,R.id.btncheckcaducidad,R.id.btncheckcodigo,R.id.btnmiccad,R.id.btnmiccod,R.id.txtdpfecha_caducidad,R.id.txtdpcodigo_producto,R.string.form_detalle_producto_origen,R.string.form_detalle_producto_costo,R.string.form_detalle_producto_foto_cod,
            R.string.form_detalle_producto_energia,R.string.form_detalle_producto_foto_num,R.string.form_detalle_producto_marca_tras,
            R.string.form_detalle_producto_atributo_a,R.string.form_detalle_producto_atributob,R.string.form_detalle_producto_atributoc,
            R.string.form_detalle_producto_etiqueta
    };

    private FragmentDetalleProducto1Binding mBinding;
    private boolean isEdit; //para saber si es nuevo o edición
    private int idInformeNuevo=0; //por si ya existe

    public DetalleProductoFragment1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //revisar permisos para voz
        if(getContext().checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }

    }

    //getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding= DataBindingUtil.inflate(inflater,
                R.layout.fragment_detalle_producto1, container, false);

        mViewModel = new ViewModelProvider(requireActivity()).get(NuevoinformeViewModel.class);
        dViewModel=new ViewModelProvider(requireActivity()).get(NuevoDetalleViewModel.class);
        dViewModel.cargarCatalogos();
        root=mBinding.getRoot();
        sv = root.findViewById(R.id.dpcontentform);
        startui();
        //creo los micros
        ImageButton micsig=root.findViewById(R.id.btnmicsiglas);
        ImageButton miccad=root.findViewById(R.id.btnmiccad);
        ImageButton micprod=root.findViewById(R.id.btnmiccod);
        // ImageButton miccosto=root.findViewById(R.id.btnmic);

        sspeechRecognizer=grabarVoz(siglas,micsig);
        cspeechRecognizer=grabarVoz(codigo,micprod);
        cpspeechRecognizer=grabarVoz(fecha,miccad);

         csspeechRecognizer=grabarVoz(mBinding.txtdpcosto,mBinding.btnmiccosto);


        Log.d("DetalleProductoFrag1","creando fragment");

        sdf=new SimpleDateFormat("dd-MM-yyyy");
        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        informeEdit=new InformeCompraDetalle();

        //el numero de muestra está en los extras
        if(!isEdit) {
            Bundle params = getActivity().getIntent().getExtras();
            if (params != null) {
                numMuestra = params.getInt(NuevoinformeFragment.NUMMUESTRA);
                //veo si ya tengo informe
                idInformeNuevo= params.getInt(NuevoinformeFragment.ARG_NUEVOINFORME);
            }
        }
        mBinding.btnchecksiglas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarSiglas()) {
                    mBinding.btnmiccad.setEnabled(true);

                    mBinding.txtdpfechaCaducidad.setEnabled(true);
                }
            }
        });
        mBinding.btncheckcaducidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarFecha()) {
                    mBinding.btnmiccod.setEnabled(true);
                    mBinding.txtdpcodigoProducto.setEnabled(true);
                }
            }
        });
        mBinding.btncheckcodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarCodigoprod().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if(aBoolean)
                            habilitarCampos();
                    }
                });

            }
        });


        //    mViewModel.icdNuevo.setPlantaNombre("planta1");
        //   mViewModel.icdNuevo.setPlantasId(1);

        }

    String nombrePlanta;
    int idPlanta;
    public void startui(){
       siglas= root.findViewById(R.id.txtdpsiglas);
        fecha = root.findViewById(R.id.txtdpfecha_caducidad);
        //busco los datos del vmodel
        if(dViewModel.productoSel!=null&&dViewModel.productoSel.compradetalleSel>0) {
            isEdit=false;
            dViewModel.atributos.observe(getViewLifecycleOwner(), new Observer<List<CatalogoDetalle>>() {
                @Override
                public void onChanged(List<CatalogoDetalle> catalogoDetalles) {

                    atributos = catalogoDetalles;
                    dViewModel.tomadoDe.observe(getViewLifecycleOwner(), new Observer<List<CatalogoDetalle>>() {
                        @Override
                        public void onChanged(List<CatalogoDetalle> catalogoDetalles) {
                            tomadoDe = catalogoDetalles;
                            Log.d(TAG,"ya tengo los catalogos"+catalogoDetalles.size());
                            crearFormulario();
                            sv.addView(cf.crearFormulario());
                            deshabilitarCampos();
                        }
                    });
                }
            });
            Log.d(TAG,"compra seleccionada"+dViewModel.productoSel.compraSel+"--"+dViewModel.productoSel.compradetalleSel);
            mBinding.setProductoSel(dViewModel.productoSel);
            dViewModel.icdNuevo=new InformeCompraDetalle();
            dViewModel.icdNuevo.setProductoId(dViewModel.productoSel.productoid);
            dViewModel.icdNuevo.setProducto(dViewModel.productoSel.producto);
            dViewModel.icdNuevo.setEmpaque(dViewModel.productoSel.empaque);
            dViewModel.icdNuevo.setEmpaquesId(dViewModel.productoSel.idempaque);
          //  siglas.setText(dViewModel.productoSel.siglas);
            //TODO COMO le haremos par el backup
            dViewModel.icdNuevo.setTipoMuestra(dViewModel.productoSel.tipoMuestra);
            dViewModel.icdNuevo.setNombreTipoMuestra(dViewModel.productoSel.nombreTipoMuestra);
            dViewModel.icdNuevo.setTipoAnalisis(dViewModel.productoSel.tipoAnalisis);
            dViewModel.icdNuevo.setNombreAnalisis(dViewModel.productoSel.analisis);


            //

            dViewModel.icdNuevo.setPresentacion(dViewModel.productoSel.presentacion);

            mBinding.btndpvalidar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    guardarMuestra();
                }
            });


        }else
        //vengo a editar
        {
            Bundle bundle=getActivity().getIntent().getExtras();
            if(bundle!=null){
                isEdit=true;

                int idmuestra=bundle.getInt(ARG_IDMUESTRA);
                int idinforme=bundle.getInt(NuevoinformeFragment.INFORMESEL);

                //busco el informe
                mViewModel.buscarInforme(idinforme).observe(getViewLifecycleOwner(), new Observer<InformeCompra>() {
                    @Override
                    public void onChanged(InformeCompra informeCompra) {
                        nombrePlanta=informeCompra.getPlantaNombre();
                        idPlanta=informeCompra.getPlantasId();

                dViewModel.getMuestra(idmuestra).observe(getViewLifecycleOwner(), new Observer<InformeCompraDetalle>() {
                    @Override
                    public void onChanged(InformeCompraDetalle informeCompraDetalle) {
                        dViewModel.icdNuevo=informeCompraDetalle;
                        informeEdit=informeCompraDetalle;
                        Log.d(TAG,"buscando a"+idmuestra);

                        dViewModel.setProductoSel(informeCompraDetalle,nombrePlanta,idPlanta);
                        Log.d(TAG,"compra seleccionada"+dViewModel.productoSel.compraSel+"--"+dViewModel.productoSel.compradetalleSel);

                        mBinding.setProductoSel(dViewModel.productoSel);
                        // mBinding.setNuevaMuestra();
                        dViewModel.atributos.observe(getViewLifecycleOwner(), new Observer<List<CatalogoDetalle>>() {
                            @Override
                            public void onChanged(List<CatalogoDetalle> catalogoDetalles) {

                                atributos = catalogoDetalles;
                                dViewModel.tomadoDe.observe(getViewLifecycleOwner(), new Observer<List<CatalogoDetalle>>() {
                                    @Override
                                    public void onChanged(List<CatalogoDetalle> catalogoDetalles) {
                                        tomadoDe = catalogoDetalles;
                                        Log.d(TAG,"ya tengo los catalogos");
                                        crearFormulario();
                                        sv.addView(cf.crearFormulario());
                                        ponerDatos();
                                        deshabilitarCampos();
                                    }
                                });
                            }
                        });

                    }
                });
                    }
                });

            }else
            {

                Log.e(TAG,"No llego la info");

            }
            mBinding.btndpvalidar.setText("ACTUALIZAR");
            mBinding.btndpvalidar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actualizarMuestra();
                }
            });
        }



        codigo = root.findViewById(R.id.txtdpcodigo_producto);
      /*  siglas.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    if(siglas.getText().toString().length()>0){
                        //valido y habilito

                        fecha.setEnabled(true);

                    }
                }
            }
        });*/
        siglas.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(siglas.getText().toString().length()>0){
                    //valido y habilito

                    mBinding.btnchecksiglas.setEnabled(true);

                }
            }
        });
        fecha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(fecha.getText().toString().length()>0){
                    //valido y habilito

                    mBinding.btncheckcaducidad.setEnabled(true);

                }
            }
        });
        codigo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(codigo.getText().toString().length()>0){
                    //valido y habilito

                    mBinding.btncheckcodigo.setEnabled(true);

                }
            }
        });
      /*  fecha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    if(fecha.getText().toString().length()>5){
                        //valido y habilito

                        codigo.setEnabled(true);

                    }
                }
            }
        });*/
      /*  codigo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    if(codigo.getText().toString().length()>0){
                        //valido y habilito

                        habilitarCampos();

                    }
                }
            }
        });*/


    }

    public SpeechRecognizer grabarVoz(EditText destino,ImageButton micButton){
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
                destino.setText("");
                destino.setHint("Escuchando...");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                destino.setHint("");
            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
              //  micButton.setImageResource(R.drawable.ic_baseline_mic_none_24);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                destino.setText(data.get(0));
                destino.setHint("");

            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
        micButton.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    micButton.setImageResource(R.drawable.ic_baseline_mic_24);
                    speechRecognizer.stopListening();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    micButton.setImageResource(R.drawable.ic_baseline_mic_none_24);
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
                return false;
            }
        });
        return  speechRecognizer;
    }
    public void deshabilitarCampos() {
        for (int i = 0; i < 12; i++) {
            View v = root.findViewById(arrCampos[i]);
            if(v!=null)
            v.setEnabled(false);
        }
        mBinding.btndpvalidar.setEnabled(false);
        mBinding.spdporigen.setEnabled(false);
        mBinding.txtdpcosto.setEnabled(false);
        mBinding.btnmiccosto.setEnabled(false);
        LinearLayout layout = (LinearLayout) root.findViewById(R.id.dpcontentform);
        loopViews(layout,false);
    }
    public void loopViews( LinearLayout layout ,boolean opcion){

        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);


            if (child instanceof EditText) {
                // Do something
                child.setEnabled(opcion);
            }  if (child instanceof Button) {
                // Do something
                child.setEnabled(opcion);
            }  if (child instanceof ImageButton) {
                // Do something
                child.setEnabled(opcion);
            }  if (child instanceof Spinner) {
                // Do something
                child.setEnabled(opcion);
            } else  if (child instanceof LinearLayout) {

                this.loopViews((LinearLayout) child,opcion);
            }
            /*if (child instanceof ViewGroup) {

                this.loopViews((ViewGroup) child);
            }*/


        }
    }
    public void habilitarCampos(){

        LinearLayout layout = (LinearLayout) root.findViewById(R.id.dpcontentform);
        loopViews(layout,true);
        mBinding.btndpvalidar.setEnabled(true);
        mBinding.spdporigen.setEnabled(true);
        mBinding.txtdpcosto.setEnabled(true);
        mBinding.btnmiccosto.setEnabled(true);
    }
    int consecutivo =1;
    public void guardarMuestra(){
        try {
            //primero valido
            // InformeCompraDetalle detalle=new InformeCompraDetalle();
            //detalle=mBinding.getNuevaMuestra();
            //TODO validaciones

            //Creo el informe en nuevo informe y lo busco aqui


            //veo si ya existe el informe o hay que crearlo
            if (numMuestra == 1 || idInformeNuevo <= 0) {
                //busco el consecutivo

               int consecutivo=mViewModel.getConsecutivo(dViewModel.productoSel.clienteSel);


               mViewModel.informe.setConsecutivo(consecutivo);
               Log.d(TAG,"el consecutivo es "+consecutivo+"--"+dViewModel.productoSel.clienteSel);


                mViewModel.informe.setPlantasId(dViewModel.productoSel.plantaSel);
                mViewModel.informe.setPlantaNombre(dViewModel.productoSel.plantaNombre);
                mViewModel.informe.setClienteNombre(dViewModel.productoSel.clienteNombre);
                mViewModel.informe.setClientesId(dViewModel.productoSel.clienteSel);
                mViewModel.informe.setEstatusSync(0);
                mViewModel.informe.setEstatus(1);

                idInformeNuevo = (int) mViewModel.guardarInforme();
                Log.d(TAG, "se creo el informe" + idInformeNuevo);

            }

            mViewModel.informe.setId(idInformeNuevo);
            if (idInformeNuevo > 0) {
                EditText codigo = root.findViewById(R.id.txtdpcodigo_producto);

                @SuppressLint("ResourceType") EditText etiqueta = root.findViewById(R.string.form_detalle_producto_etiqueta);
                dViewModel.icdNuevo.setInformesId(idInformeNuevo);
                //guardo la lista origen
                dViewModel.icdNuevo.setComprasId(dViewModel.productoSel.compraSel);
                dViewModel.icdNuevo.setComprasDetId(dViewModel.productoSel.compradetalleSel);
                dViewModel.icdNuevo.setCodigo(codigo.getText().toString());
                try {
                    dViewModel.icdNuevo.setCaducidad(sdf.parse(fecha.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                EditText fotocodigo = root.findViewById(R.string.form_detalle_producto_foto_cod);
                //agrega los campos nuevos
               // Spinner origen = root.findViewById(R.string.form_detalle_producto_origen);
                CatalogoDetalle catselect = (CatalogoDetalle) mBinding.spdporigen.getSelectedItem();
                dViewModel.icdNuevo.setOrigen(catselect.getCad_idopcion() + "");
                EditText costo = root.findViewById(R.id.txtdpcosto);
                dViewModel.icdNuevo.setCosto(costo.getText().toString());
                Spinner atributoa = (Spinner) root.findViewById(R.string.form_detalle_producto_atributo_a);
                Spinner atributob = (Spinner) root.findViewById(R.string.form_detalle_producto_atributob);
                Spinner atributoc = (Spinner) root.findViewById(R.string.form_detalle_producto_atributoc);

                catselect = (CatalogoDetalle) atributoa.getSelectedItem();
                dViewModel.icdNuevo.setAtributoa(catselect.getCad_idopcion() + "");
                catselect = (CatalogoDetalle) atributob.getSelectedItem();
                dViewModel.icdNuevo.setAtributob(catselect.getCad_idopcion() + "");
                catselect = (CatalogoDetalle) atributoc.getSelectedItem();
                dViewModel.icdNuevo.setAtributoc(catselect.getCad_idopcion() + "");
                //       dViewModel.icdNuevo.setComentarios(root.findViewById(R.string.form_detalle_producto_comentarios).toString());
                dViewModel.icdNuevo.setNumMuestra(numMuestra);

                //las fotos se agregan al model
                dViewModel.icdNuevo.setEstatusSync(0);
                dViewModel.icdNuevo.setEstatus(1);
                dViewModel.icdNuevo.setCreatedAt(new Date());
                EditText energia = root.findViewById(R.string.form_detalle_producto_energia);
                dViewModel.energia = crearImagen(energia.getText().toString(), Contrato.TablaInformeDet.ENERGIA);

                dViewModel.foto_codigo_produccion = crearImagen(fotocodigo.getText().toString(), Contrato.TablaInformeDet.FOTOCODIGOPRODUCCION);
                EditText numtienda = root.findViewById(R.string.form_detalle_producto_foto_num);
                dViewModel.foto_num_tienda = crearImagen(numtienda.getText().toString(), Contrato.TablaInformeDet.FOTONUMTIENDA);
                EditText traslape = root.findViewById(R.string.form_detalle_producto_marca_tras);
                dViewModel.marca_traslape = crearImagen(traslape.getText().toString(), Contrato.TablaInformeDet.MARCA_TRASLAPE);
                EditText fotoatributoa = root.findViewById(R.string.form_detalle_producto_foto_atributoa);
                EditText fotoatributob = root.findViewById(R.string.form_detalle_producto_foto_atributob);

                EditText fotoatributoc = root.findViewById(R.string.form_detalle_producto_foto_atributoc);
                EditText azucares = root.findViewById(R.string.form_detalle_producto_azucares);
                EditText qr = root.findViewById(R.string.form_detalle_producto_qr);

                dViewModel.foto_atributoa = crearImagen(fotoatributoa.getText().toString(), Contrato.TablaInformeDet.FOTO_ATRIBUTOA);
                dViewModel.foto_atributob = crearImagen(fotoatributob.getText().toString(), Contrato.TablaInformeDet.FOTO_ATRIBUTOB);
                dViewModel.foto_atributoc = crearImagen(fotoatributoc.getText().toString(), Contrato.TablaInformeDet.FOTO_ATRIBUTOC);
                dViewModel.etiqueta_evaluacion = crearImagen(etiqueta.getText().toString(), Contrato.TablaInformeDet.ETIQUETA_EVALUACION);
                dViewModel.fotoazucares = crearImagen(azucares.getText().toString(), Contrato.TablaInformeDet.AZUCARES);
                dViewModel.fotoqr = crearImagen(qr.getText().toString(), Contrato.TablaInformeDet.QR);

                int nuevoid = dViewModel.saveDetalle2();
                if (nuevoid > 0) {
                    //si ya se guardó lo agrego en la lista de compra
                    ListaDetalleViewModel lcviewModel = new ViewModelProvider(this).get(ListaDetalleViewModel.class);
                    lcviewModel.comprarMuestra(dViewModel.productoSel.compradetalleSel,fecha.getText().toString());
                    Log.d(TAG, "guardando>>" + mViewModel.informe.getId());
                    Intent resultIntent = new Intent();

                    resultIntent.putExtra(NuevoinformeFragment.ARG_NUEVOINFORME, mViewModel.informe.getId());
                    getActivity().setResult(NUEVO_RESULT_OK, resultIntent);

                    //regreso al informe
                    getActivity().finish();
                }
            }

     /*   if(validarMuestra()){
            //guardo el informe que está en el viewmodel
            mViewModel.guardarInforme();
            if(mViewModel.getIdInformeNuevo()>0){ //seguardó correctamente
                //guardo datos de la muestra
                //creo nuevo detalle
                detalle.setInformesId(mViewModel.getIdInformeNuevo());
                //dViewModel.setIcdNuevo(detalle);
                dViewModel.saveDetalle1();
            }
            // mViewModel.icdNuevo.setCodigo(mBinding);

        }*/
        }catch (Exception ex){
            ex.getStackTrace();
            Log.e(TAG,ex.getMessage());
            Toast.makeText(getActivity(), "No se pudo guardar la muestra", Toast.LENGTH_SHORT).show();

        }

    }



    public void actualizarMuestra(){
        //primero valido
        // InformeCompraDetalle detalle=new InformeCompraDetalle();
        //detalle=mBinding.getNuevaMuestra();
        //TODO validaciones

        EditText codigo = root.findViewById(R.id.txtdpcodigo_producto);
        EditText etiqueta = root.findViewById(R.string.form_detalle_producto_etiqueta);


        dViewModel.icdNuevo.setCodigo(codigo.getText().toString());
        try {
            dViewModel.icdNuevo.setCaducidad(sdf.parse(fecha.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        EditText fotocodigo = root.findViewById(R.string.form_detalle_producto_foto_cod);
        //agrega los campos nuevos
        Spinner origen=root.findViewById(R.string.form_detalle_producto_origen);
        CatalogoDetalle catselect= (CatalogoDetalle) origen.getSelectedItem();
        dViewModel.icdNuevo.setOrigen(catselect.getCad_idopcion()+"");
        EditText costo=root.findViewById(R.string.form_detalle_producto_costo);
        dViewModel.icdNuevo.setCosto(costo.getText().toString());
        Spinner atributoa=(Spinner)root.findViewById(R.string.form_detalle_producto_atributo_a);
        Spinner atributob=(Spinner)root.findViewById(R.string.form_detalle_producto_atributob);
        Spinner atributoc=(Spinner)root.findViewById(R.string.form_detalle_producto_atributoc);

        catselect= (CatalogoDetalle) atributoa.getSelectedItem();
        dViewModel.icdNuevo.setAtributoa(catselect.getCad_idopcion()+"");
        catselect= (CatalogoDetalle) atributob.getSelectedItem();
        dViewModel.icdNuevo.setAtributob(catselect.getCad_idopcion()+"");
        catselect= (CatalogoDetalle) atributoc.getSelectedItem();
        dViewModel.icdNuevo.setAtributoc(catselect.getCad_idopcion()+"");
        //       dViewModel.icdNuevo.setComentarios(root.findViewById(R.string.form_detalle_producto_comentarios).toString());

        //las fotos se agregan al model
        dViewModel.icdNuevo.setEstatusSync(0);
        dViewModel.icdNuevo.setEstatus(1);
        dViewModel.icdNuevo.setUpdatedAt(new Date());
        EditText energia=root.findViewById(R.string.form_detalle_producto_energia);
        dViewModel.energia = crearImagen(energia.getText().toString(), Contrato.TablaInformeDet.ENERGIA);

        dViewModel.foto_codigo_produccion = crearImagen(fotocodigo.getText().toString(), Contrato.TablaInformeDet.FOTOCODIGOPRODUCCION);
        EditText numtienda=root.findViewById(R.string.form_detalle_producto_foto_num);
        dViewModel.foto_num_tienda = crearImagen(numtienda.getText().toString(), Contrato.TablaInformeDet.FOTONUMTIENDA);
        EditText traslape=root.findViewById(R.string.form_detalle_producto_marca_tras);
        dViewModel.marca_traslape = crearImagen(traslape.getText().toString(), Contrato.TablaInformeDet.MARCA_TRASLAPE);
        EditText fotoatributoa=root.findViewById(R.string.form_detalle_producto_foto_atributoa);
        EditText fotoatributob=root.findViewById(R.string.form_detalle_producto_foto_atributob);

        EditText fotoatributoc=root.findViewById(R.string.form_detalle_producto_foto_atributoc);

        dViewModel.foto_atributoa = crearImagen(fotoatributoa.getText().toString(), Contrato.TablaInformeDet.FOTO_ATRIBUTOA);
        dViewModel.foto_atributob = crearImagen(fotoatributob.getText().toString(), Contrato.TablaInformeDet.FOTO_ATRIBUTOB);
        dViewModel.foto_atributoc = crearImagen(fotoatributoc.getText().toString(), Contrato.TablaInformeDet.FOTO_ATRIBUTOC);
        dViewModel.etiqueta_evaluacion = crearImagen(etiqueta.getText().toString(), Contrato.TablaInformeDet.ETIQUETA_EVALUACION);


        if(dViewModel.energia!=null) {
            dViewModel.energia.setEstatusSync(0);
            dViewModel.energia.setUpdatedAt(new Date());
        }
        if(dViewModel.foto_codigo_produccion!=null) {
            dViewModel.foto_codigo_produccion.setEstatusSync(0);
            dViewModel.foto_codigo_produccion.setUpdatedAt(new Date());
        }
        if(dViewModel.foto_num_tienda!=null) {
            dViewModel.foto_num_tienda.setEstatusSync(0);
            dViewModel.foto_num_tienda.setUpdatedAt(new Date());
        }
        if(dViewModel.marca_traslape!=null) {
            dViewModel.marca_traslape.setEstatusSync(0);
            dViewModel.marca_traslape.setUpdatedAt(new Date());
        }
        if(dViewModel.foto_atributoa!=null) {
            dViewModel.foto_atributoa.setEstatusSync(0);
            dViewModel.foto_atributoa.setUpdatedAt(new Date());
        }
        if(dViewModel.foto_atributob!=null) {
            dViewModel.foto_atributob.setEstatusSync(0);
            dViewModel.foto_atributob.setUpdatedAt(new Date());
        }
        if(dViewModel.foto_atributoc!=null) {
            dViewModel.foto_atributoc.setEstatusSync(0);
            dViewModel.foto_atributoc.setUpdatedAt(new Date());
        }
        if(dViewModel.etiqueta_evaluacion!=null) {
            dViewModel.etiqueta_evaluacion.setEstatusSync(0);
            dViewModel.etiqueta_evaluacion.setUpdatedAt(new Date());
        }
             dViewModel.saveDetalle2();
        getActivity().setResult( EDIT_RESULT_OK);
        //regreso al informe
        getActivity().finish();
     /*   if(validarMuestra()){
            //guardo el informe que está en el viewmodel
            mViewModel.guardarInforme();
            if(mViewModel.getIdInformeNuevo()>0){ //seguardó correctamente
                //guardo datos de la muestra
                //creo nuevo detalle
                detalle.setInformesId(mViewModel.getIdInformeNuevo());
                //dViewModel.setIcdNuevo(detalle);
                dViewModel.saveDetalle1();
            }
            // mViewModel.icdNuevo.setCodigo(mBinding);

        }*/

    }

    //nombre del txt dela ruta
    //id de imagendetalle
    //id imageview
    //objeto imagendetalle
    public void ponerFoto( int fotoruta,int idfoto,int foto,String detalle){



        EditText txtfoto= root.findViewById(fotoruta);

        if(txtfoto!=null)
        mViewModel.getFotoLD(idfoto).observe(this, new Observer<ImagenDetalle>() {
            @Override
            public void onChanged(ImagenDetalle s) {
                if(s!=null) {
                    Log.d(TAG, "--" + fotoruta + "--" + s.getRuta());
                    txtfoto.setText(s.getRuta());

                    Bitmap bitmap1 = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null) + "/" + s.getRuta());
                    ImageView ivfoto = root.findViewById(foto);
                    switch (detalle) {
                        case "efoto_codigo_produccion":
                            efoto_codigo_produccion = s;
                            break;
                        case "eenergia":
                            eenergia = s;
                            break;
                        case "efoto_num_tienda":
                            efoto_num_tienda = s;
                            break;
                        case "efoto_atributoa":
                            efoto_atributoa = s;
                            break;
                        case "efoto_atributob":
                            efoto_atributob = s;
                            break;
                        case "efoto_atributoc":
                            efoto_atributoc = s;
                            break;
                        case "emarca_traslape":
                            emarca_traslape = s;
                            break;
                        case "eetiqueta_evaluacion":
                            eetiqueta_evaluacion = s;
                            break;
                    }
                    ivfoto.setImageBitmap(bitmap1);

                }
            }
        });



    }
    public void ponerDatos() {



        //TODO buscar las siglas
        //siglas.setText(informeEdit.;
        try {
            fecha.setText(sdf.format(informeEdit.getCaducidad()));
        }catch(Exception ex)
        {
            //nada
        }
        codigo.setText(informeEdit.getCodigo());
        Log.d(TAG,"poniendo datos");
        ponerFoto(R.string.form_detalle_producto_foto_cod,informeEdit.getFoto_codigo_produccion(),1041,"efoto_codigo_produccion");

        ponerFoto(R.string.form_detalle_producto_energia,informeEdit.getEnergia(),1042,"eenergia");
        ponerFoto(R.string.form_detalle_producto_foto_num,informeEdit.getFoto_num_tienda(),1043,"efoto_num_tienda");
        ponerFoto(R.string.form_detalle_producto_marca_tras,informeEdit.getMarca_traslape(),1044,"emarca_traslape");
        ponerFoto(R.string.form_detalle_producto_foto_atributoa,informeEdit.getFoto_atributoa(),1045,"efoto_atributoa");
        ponerFoto(R.string.form_detalle_producto_foto_atributob,informeEdit.getFoto_atributob(),1046,"efoto_atributob");
        ponerFoto(R.string.form_detalle_producto_etiqueta,informeEdit.getEtiqueta_evaluacion(),1048,"eetiqueta_evaluacion");

        ponerFoto(R.string.form_detalle_producto_foto_atributoc,informeEdit.getFoto_atributoc(),1047,"efoto_atributoc");

    }
    public void crearFormulario(){
        Log.d(TAG,"haciendo formulario");
        camposForm= new ArrayList<CampoForm>();
        CampoForm campo=new CampoForm();
      /*  campo.label=getString(R.string.origen);
        campo.nombre_campo= Contrato.TablaInformeDet.ORIGEN;
        campo.type="selectCat";
        campo.selectcat=tomadoDe;
        campo.value=informeEdit.getOrigen();
        campo.id=R.string.form_detalle_producto_origen;
        campo.required="required";
        camposForm.add(campo);
        campo=new CampoForm();
        campo.label=getString(R.string.costo);
        campo.nombre_campo=Contrato.TablaInformeDet.COSTO;
        campo.type="inputtext";
        campo.value=informeEdit.getCosto();
        campo.required="required";
        campo.id=R.string.form_detalle_producto_costo;
        camposForm.add(campo);

        campo=new CampoForm();


        campo.label="";
        campo.nombre_campo=Contrato.TablaInformeDet.COSTO;
        campo.type="botonMicro";
        campo.required="required";
        campo.id=1051;
        camposForm.add(campo);*/
        CreadorFormulario.cargarSpinnerCat(getContext(), mBinding.spdporigen,tomadoDe);
        campo=new CampoForm();
        campo.label=getString(R.string.foto_codigo_produccion);
        campo.nombre_campo=Contrato.TablaInformeDet.FOTOCODIGOPRODUCCION;
        campo.type="agregarImagen";
        campo.value="";
        campo.required="required";
        campo.id=R.string.form_detalle_producto_foto_cod;
        CampoForm finalCampo=campo;
        campo.funcionOnClick=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarFoto(finalCampo.id,1041);
            }
        };
        campo.tomarFoto=true;

        camposForm.add(campo);
        CampoForm campo2=new CampoForm();
        campo2.nombre_campo="foto_codigo";
        campo2.type="imagenView";
        campo2.value=null;
        campo2.id=1041;


        camposForm.add(campo2);
        campo=new CampoForm();
        campo.label=getString(R.string.azucareS);
        campo.nombre_campo=Contrato.TablaInformeDet.AZUCARES;
        campo.type="agregarImagen";
        campo.value=null;
        campo.required="required";
        CampoForm finalCampo2=campo;
        campo.funcionOnClick=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarFoto(finalCampo2.id,1049);
            }
        };
        campo.id=R.string.form_detalle_producto_azucares;
        camposForm.add(campo);
        campo2=new CampoForm();
        campo2.type="imagenView";

        campo2.id=1049;

        camposForm.add(campo2);
        campo=new CampoForm();

        campo=new CampoForm();
        campo.label=getString(R.string.energia);
        campo.nombre_campo=Contrato.TablaInformeDet.ENERGIA;
        campo.type="agregarImagen";
        campo.value=null;
        campo.required="required";
        CampoForm finalCampo01=campo;
        campo.funcionOnClick=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarFoto(finalCampo01.id,1042);
            }
        };
        campo.id=R.string.form_detalle_producto_energia;
        camposForm.add(campo);
        campo2=new CampoForm();
        campo2.type="imagenView";

        campo2.id=1042;


        camposForm.add(campo2);
        campo=new CampoForm();
        campo.label=getString(R.string.foto_num_tienda);
        campo.nombre_campo= Contrato.TablaInformeDet.FOTONUMTIENDA;
        campo.type="agregarImagen";
        campo.value=null;
        campo.required="required";
        CampoForm finalCampo3=campo;
        campo.funcionOnClick=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarFoto(finalCampo3.id,1043);
            }
        };
        campo.id=R.string.form_detalle_producto_foto_num;
        camposForm.add(campo);
        campo2=new CampoForm();
        campo2.type="imagenView";

        campo2.id=1043;


        camposForm.add(campo2);
        campo=new CampoForm();
        campo.label=getString(R.string.marca_traslape);
        campo.nombre_campo=Contrato.TablaInformeDet.MARCA_TRASLAPE;
        campo.type="agregarImagen";
        campo.value=null;
        CampoForm finalCampo4=campo;
        campo.funcionOnClick=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarFoto(finalCampo4.id,1044);
            }
        };
        campo.required="required";
        campo.id=R.string.form_detalle_producto_marca_tras;
        camposForm.add(campo);
        campo2=new CampoForm();
        campo2.type="imagenView";

        campo2.id=1044;


        camposForm.add(campo2);
        campo=new CampoForm();
        campo.label=getString(R.string.atributoa);
        campo.nombre_campo=Contrato.TablaInformeDet.ATRIBUTOA;
        campo.type="selectCat";
        campo.value=informeEdit.getAtributoa();
        campo.required="required";
        campo.id=R.string.form_detalle_producto_atributo_a;
        campo.selectcat=atributos;
        camposForm.add(campo);
        campo=new CampoForm();
        campo.label=getString(R.string.foto_atributoa);
        campo.nombre_campo=Contrato.TablaInformeDet.FOTO_ATRIBUTOA;
        campo.type="agregarImagen";
        CampoForm finalCampo5=campo;
        campo.funcionOnClick=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarFoto(finalCampo5.id,1045);
            }
        };
        campo.value=null;
        campo.required="required";
        campo.id=R.string.form_detalle_producto_foto_atributoa;
        camposForm.add(campo);
        campo2=new CampoForm();
        campo2.type="imagenView";

        campo2.id=1045;


        camposForm.add(campo2);
        campo=new CampoForm();
        campo.label=getString(R.string.atributob);
        campo.nombre_campo=Contrato.TablaInformeDet.ATRIBUTOB;
        campo.type="selectCat";
        campo.selectcat=atributos;
        campo.value=informeEdit.getAtributob();
        campo.required="required";
        campo.id=R.string.form_detalle_producto_atributob;
        camposForm.add(campo);
        campo=new CampoForm();
        campo.label=getString(R.string.foto_atributob);
        campo.nombre_campo=Contrato.TablaInformeDet.FOTO_ATRIBUTOB;
        campo.type="agregarImagen";
        campo.value=null;
        CampoForm finalCampo6=campo;
        campo.funcionOnClick=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarFoto(finalCampo6.id,1046);
            }
        };
        campo.required="required";
        campo.id=R.string.form_detalle_producto_foto_atributob;
        camposForm.add(campo);
        campo2=new CampoForm();
        campo2.type="imagenView";

        campo2.id=1046;


        camposForm.add(campo2);
        campo=new CampoForm();
        campo.label=getString(R.string.atributoc);
        campo.nombre_campo=Contrato.TablaInformeDet.ATRIBUTOC;
        campo.type="selectCat";
        campo.selectcat=atributos;
        campo.value=null;
        campo.required="required";
        campo.id=R.string.form_detalle_producto_atributoc;
        camposForm.add(campo);
        campo=new CampoForm();
        campo.label=getString(R.string.foto_atributoc);
        campo.nombre_campo=Contrato.TablaInformeDet.FOTO_ATRIBUTOC;
        campo.type="agregarImagen";
        campo.value="";
        CampoForm finalCampo7=campo;
        campo.funcionOnClick=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarFoto(finalCampo7.id,1047);
            }
        };
        campo.required="required";
        campo.id=R.string.form_detalle_producto_foto_atributoc;
        camposForm.add(campo);
        campo2=new CampoForm();
        campo2.type="imagenView";

        campo2.id=1047;


        camposForm.add(campo2);
        campo=new CampoForm();
        campo.label="QR";
        campo.nombre_campo=Contrato.TablaInformeDet.QR;
        campo.type="agregarImagen";
        campo.value=null;
        CampoForm finalCampo9=campo;
        campo.funcionOnClick=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarFoto(finalCampo9.id,1050);
            }
        };
        campo.required="required";
        campo.id=R.string.form_detalle_producto_qr;
        camposForm.add(campo);
        campo2=new CampoForm();
        campo2.type="imagenView";

        campo2.id=1050;


        camposForm.add(campo2);
        campo=new CampoForm();
        campo.label=getString(R.string.etiqueta_evaluacion);
        campo.nombre_campo=Contrato.TablaInformeDet.ETIQUETA_EVALUACION;
        campo.type="agregarImagen";
        campo.value=null;
        CampoForm finalCampo8=campo;
        campo.funcionOnClick=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarFoto(finalCampo8.id,1048);
            }
        };
        campo.required="required";
        campo.id=R.string.form_detalle_producto_etiqueta;
        camposForm.add(campo);
        campo2=new CampoForm();
        campo2.type="imagenView";

        campo2.id=1048;


        camposForm.add(campo2);

        cf=new CreadorFormulario(camposForm,getContext());

    }

    public void regresar(){

    }



    public ImagenDetalle crearImagen(String ruta, String descripcion){
        if(ruta.equals("")){
            return null;
        }
        ImagenDetalle     foto=new ImagenDetalle();
        foto.setRuta(ruta);
        foto.setDescripcion(descripcion);
        foto.setEstatus(1);

        foto.setEstatusSync(0);
        foto.setCreatedAt(new Date());
        return foto;
    }


    public boolean validarMuestra(){
        return true;
    }
    String nombre_foto;
    public   void tomarFoto(int origen, int destino) {
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
        foto1=root.findViewById(origen);
        if(destino!=0) {
            imageView = root.findViewById(destino);
            if(imageView==null) Log.e(TAG,"no encontré a "+destino);
            startActivityForResult(intento1, REQUEST_CODE_TAKE_PHOTO);
        }
        else{
            startActivityForResult(intento1, REQUEST_CODE_2);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //   Log.d(TAG,"vars"+requestCode +"=="+ REQUEST_CODE_TAKE_PHOTO+"--"+ resultCode+" =="+ RESULT_OK);
        if ((requestCode == REQUEST_CODE_TAKE_PHOTO||requestCode==REQUEST_CODE_2) && resultCode == RESULT_OK) {

            File file = new File(getActivity().getExternalFilesDir(null), nombre_foto);
            if (file.exists()) {
                if(requestCode == REQUEST_CODE_TAKE_PHOTO) {
                    //envio a la actividad dos para ver la foto
                    Intent intento1 = new Intent(getActivity(), RevisarFotoActivity.class);
                    intento1.putExtra("ei.archivo", nombre_foto);

                    foto1.setText(nombre_foto);
                    Bitmap bitmap1 = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null) + "/" + nombre_foto);

                    imageView.setImageBitmap(bitmap1);
                }
                if(requestCode == REQUEST_CODE_2) {
                    Intent intento1 = new Intent(getActivity(), RevisarFotoActivity.class);
                    intento1.putExtra("ei.archivo", nombre_foto);
                    intento1.putExtra("ei.opcionfoto", "exhibicion");

                    startActivity(intento1);
                }

            }
            else{
                Log.d(TAG,"Algo salió mal???");
            }


        }else{
            Log.d(TAG,"Algo salió muy mal");
        }
    }

    //validar siglas
    public boolean validarSiglas(){
        if(dViewModel.productoSel.clienteNombre.toUpperCase().equals("PEPSI"))
        if(!siglas.getText().toString().equals("")){
           if(dViewModel.productoSel.siglas!=null&&!dViewModel.productoSel.siglas.equals(siglas.getText().toString())){
               Toast.makeText(getActivity(), getString(R.string.error_siglas), Toast.LENGTH_LONG).show();
                return false;
           }
        }
        return true;
    }
    Date fechacad = null;
    MutableLiveData<Boolean> res;
    //validar siglas
    public boolean validarFecha(){
        Date hoy=new Date();

        if (!fecha.getText().toString().equals("")) {

            try {
                fechacad = sdf.parse(fecha.getText().toString());
            } catch (ParseException e) {

                Toast.makeText(getActivity(), getString(R.string.error_fecha_formato), Toast.LENGTH_LONG).show();
               return false;
            }

            if (dViewModel.productoSel.clienteNombre.toUpperCase().equals("PEPSI")) {

                Calendar cal = Calendar.getInstance(); // Obtenga un calendario utilizando la zona horaria y la configuración regional predeterminadas
                cal.setTime(hoy);
                cal.add(Calendar.DAY_OF_MONTH, +30);
               if (fechacad.getTime()<=hoy.getTime()) { //ya caducó fechacad>=hoy

                    //TODO necesito el tipo de tienda
                    if (tipoTienda != 2 && tipoTienda != 3) {
                        Toast.makeText(getActivity(), getString(R.string.error_fecha_caduca), Toast.LENGTH_LONG).show();
                        return false;
                    }
                 else return true;

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

                }
            }

            return true;
        }
        return false;
    }
    public MutableLiveData<Boolean> validarCodigoprod(){
        MutableLiveData<Boolean> resp=new MutableLiveData<>();
        if(!codigo.getText().toString().equals("")){
            if(dViewModel.productoSel.clienteNombre.toUpperCase().equals("PEPSI")){
                String arrecodigos[]=dViewModel.productoSel.codigosnop.split(";");
               if(arrecodigos!=null&&arrecodigos.length>0) {
                   List<String> lista = Arrays.asList(arrecodigos);
                   if (lista.contains(codigo.getText().toString())) {
                       Toast.makeText(getActivity(), getString(R.string.error_codigo_repetido)+"*", Toast.LENGTH_LONG).show();

                       resp.setValue(false);
                       return resp;
                   }else{

                       //busco si hay otra muestra == y si tiene el mismo codigo
                       buscarMuestraCodigo(dViewModel.productoSel,codigo.getText().toString(),fechacad);
                       res.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                           @Override
                           public void onChanged(Boolean aBoolean) {
                               if(aBoolean){
                                   Toast.makeText(getActivity(), getString(R.string.error_codigo_repetido), Toast.LENGTH_LONG).show();


                               }
                               resp.setValue(aBoolean);

                           }
                       });
                   }
               }

            }
            resp.setValue(true);
            return resp;
        }
        resp.setValue(false);
        return resp;

    }

    public void buscarMuestraCodigo(NuevoDetalleViewModel.ProductoSel productosel,String codigonvo,Date caducidadnva){
        //busco en el mismo informe
        res=dViewModel.buscarMuestraCodigo(Constantes.INDICEACTUAL,dViewModel.productoSel.plantaSel,productosel,codigonvo,caducidadnva,getViewLifecycleOwner());

    }
    public void buscarMuestraCodigoPeñafiel(NuevoDetalleViewModel.ProductoSel productosel,Date caducidadnva){
        //busco en el mismo informe
        res=dViewModel.buscarMuestraCodigo(Constantes.INDICEACTUAL,dViewModel.productoSel.plantaSel,productosel,"",caducidadnva,getViewLifecycleOwner());

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(cspeechRecognizer!=null)
        cspeechRecognizer.destroy();
        if(cpspeechRecognizer!=null)
            cpspeechRecognizer.destroy();
        if(csspeechRecognizer!=null)
            csspeechRecognizer.destroy();
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
    }
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }
    }


    //para asegurarme de que guarda
//    @Override
//    public void onBackPressed() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Are you sure you want to exit?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        MyActivity.this.finish();
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//        AlertDialog alert = builder.create();
//        alert.show();
//
//    }
}