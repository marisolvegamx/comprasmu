package com.example.comprasmu.ui.preparacion;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.SubirInformeEtaTask;
import com.example.comprasmu.SubirInformeTask;
import com.example.comprasmu.data.modelos.Contrato;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.modelos.InformeTemp;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.Reactivo;
import com.example.comprasmu.data.remote.InformeEnvio;
import com.example.comprasmu.data.remote.InformeEtapaEnv;
import com.example.comprasmu.services.SubirFotoService;
import com.example.comprasmu.ui.RevisarFotoActivity;
import com.example.comprasmu.ui.infetapa.NuevoInfEtapaActivity;
import com.example.comprasmu.ui.infetapa.NuevoInfEtapaViewModel;
import com.example.comprasmu.ui.informedetalle.ContinuarInformeActivity;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.utils.CampoForm;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class NvaPreparacionFragment extends Fragment {

    private NvaPreparacionViewModel mViewModel;
    private View root;
    boolean isEdicion;
    private static final String TAG="NvaPrparacionFRAG";
    EditText textoint;
    ImageView fotomos;
    public static  int REQUEST_CODE_TAKE_PHOTO=1;
    private ImageButton btnrotar;
    String  ultimares;
    Button aceptar;
    private int plantaSel;
    private String nombrePlantaSel;
    private long lastClickTime = 0;
    private boolean yaestoyProcesando=false;
    public final static String ARG_NUEVOINFORME="comprasmu.ni_idinforme";
    private int preguntaAct;
    CreadorFormulario cf;
    List<CampoForm> camposForm;
    LinearLayout sv;
    private String clienteNombre;
    private int clienteId;
    private int preguntaSig;
    List<ListaCompra> listacomp;
    private ListaDetalleViewModel lcViewModel;
    private  ArrayList<DescripcionGenerica> listaPlantas;
    Spinner spclientes ;
    private InformeEtapa informeEdit;
    InformeEtapaDet detalleEdit;
    private NuevoInfEtapaViewModel infvm;
    int informesel;

    public static NvaPreparacionFragment newInstance() {
        return new NvaPreparacionFragment();
    }
    public NvaPreparacionFragment(int preguntaAct,boolean edicion, InformeEtapaDet informeEdit) {
        this.preguntaAct = preguntaAct;
        this.isEdicion=edicion;
        this.detalleEdit=informeEdit;
    }
    public NvaPreparacionFragment() {

    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root= inflater.inflate(R.layout.fragment_generic, container, false);
        sv = root.findViewById(R.id.content_generic);
        aceptar = root.findViewById(R.id.btngaceptar);
        mViewModel = new ViewModelProvider(requireActivity()).get(NvaPreparacionViewModel.class);
        lcViewModel=new ViewModelProvider(this).get(ListaDetalleViewModel.class);
        infvm =
                new ViewModelProvider(requireActivity()).get(NuevoInfEtapaViewModel.class);


        //busco si tengo varias plantas
        listacomp= lcViewModel.cargarPestañasSimp(Constantes.CIUDADTRABAJO);

        if(!isEdicion&&mViewModel.getIdNuevo()==0)
        if(listacomp.size()>1) {
            //tengo varias plantas
            preguntaAct=0;
            preguntaSig=1;
            convertirLista(listacomp);
            mViewModel.variasPlantas=true;
        }else if(listacomp.size()>0)
        {
            mViewModel.variasPlantas=false;
                  //tengo el nombre de la planta y cliente

            nombrePlantaSel=listacomp.get(0).getPlantaNombre();
            plantaSel=listacomp.get(0).getPlantasId();
            clienteId=listacomp.get(0).getClientesId();
            clienteNombre=listacomp.get(0).getClienteNombre();
            InformeEtapa informetemp=new InformeEtapa();
            informetemp.setClienteNombre(clienteNombre);
            informetemp.setClientesId(clienteId);
            informetemp.setPlantasId(plantaSel);
            informetemp.setPlantaNombre(nombrePlantaSel);
            informetemp.setIndice(Constantes.INDICEACTUAL);
            ((NuevoInfEtapaActivity)getActivity()).actualizarBarra(informetemp);
        }
        else
            Log.e(TAG,"algo salió mal con la consulta de listas");


        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        //   lcrepo.getClientesByIndiceCiudad(Constantes.INDICEACTUAL,ciudadNombre).observe(getViewLifecycleOwner(), nameObserver);
        if(!isEdicion&&preguntaAct<2&&mViewModel.getIdNuevo()==0) {
            //reviso si ya tengo uno abierto
            InformeEtapa informeEtapa = mViewModel.getInformePend(Constantes.INDICEACTUAL,Constantes.ETAPAACTUAL);
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
        if(isEdicion) {
            aceptar.setEnabled(true);
            Bundle datosRecuperados = getArguments();

            if(datosRecuperados!=null) {
                informesel= datosRecuperados.getInt(NuevoInfEtapaActivity.INFORMESEL);
            }
            if(preguntaAct==6){
                if(detalleEdit!=null){
                    //busco aqui el idinforme
                    mViewModel.setIdNuevo(detalleEdit.getInformeEtapaId());


                }
                //solo traigo el informe necesito el id
                mViewModel.getInformeEdit(mViewModel.getIdNuevo()).observe(getViewLifecycleOwner(), new Observer<InformeEtapa>() {
                    @Override
                    public void onChanged(InformeEtapa informeEtapa) {
                        ultimares=informeEtapa.getComentarios();
                        informeEdit=informeEtapa;
                        crearFormulario();
                    }
                });

            }if(preguntaAct==0) {

                convertirLista(listacomp);
                mViewModel.variasPlantas=true;
                if(informesel>0)
                    mViewModel.setIdNuevo( informesel);
                Log.e(TAG,"--"+mViewModel.getIdNuevo());
                //solo traigo el informe necesito el id
                mViewModel.getInformeEdit(mViewModel.getIdNuevo()).observe(getViewLifecycleOwner(), new Observer<InformeEtapa>() {
                    @Override
                    public void onChanged(InformeEtapa informeEtapa) {
                        ultimares=informeEtapa.getPlantasId()+"";
                        informeEdit=informeEtapa;
                        mViewModel.setNvoinforme(informeEdit);
                        crearFormulario();
                        spclientes = root.findViewById(1001);
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
                    }
                });


            }else
             {
                if(detalleEdit!=null){

                        //busco aqui el idinforme
                    ultimares=detalleEdit.getRuta_foto();
                    mViewModel.setIdNuevo(detalleEdit.getInformeEtapaId());
                    mViewModel.getInformeEdit(mViewModel.getIdNuevo()).observe(getViewLifecycleOwner(), new Observer<InformeEtapa>() {
  @Override
                    public void onChanged(InformeEtapa informeEtapa) {

                        informeEdit=informeEtapa;
                        crearFormulario();
                    }
                });

                }else
                mViewModel.getDetalleEtEdit(mViewModel.getIdNuevo(),preguntaAct).observe(getViewLifecycleOwner(), new Observer<InformeEtapaDet>() {
                    @Override
                    public void onChanged(InformeEtapaDet informeEtapaDet) {
                        ultimares=informeEtapaDet.getRuta_foto();
                        detalleEdit = informeEtapaDet;
                        crearFormulario();
                    }
                });
            }
        }
        else {
            if(preguntaAct<6)
            aceptar.setEnabled(false);
            crearFormulario();
        }

        Log.d(TAG,"p*************aso x aqui"+mViewModel.getIdNuevo());
        if(textoint!=null){ //los comentarios no son obligatorios
          if(preguntaAct<6)
            textoint.addTextChangedListener(new NvaPreparacionFragment.BotonTextWatcher());

            textoint.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        }

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aceptar.setEnabled(false);
                long currentClickTime= SystemClock.elapsedRealtime();
                // preventing double, using threshold of 1000 ms
                if (currentClickTime - lastClickTime < 5500){
                    //  Log.d(TAG,"doble click :("+lastClickTime);
                    return;
                }

                lastClickTime = currentClickTime;

                if(preguntaAct==0){
                    guardarCliente();
                }
                else
                    siguiente();

            }
        });

        if(preguntaAct==6){
            //cambio el boton a finalizar y muestro alerta
            aceptar.setText(getString(R.string.enviar));
            aceptar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.botonvalido));
        }
        infvm.cont =preguntaAct;
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
            if (charSequence.length()>0){ //count es cantidad de caracteres que tiene
                aceptar.setEnabled(true);
            }else{
                aceptar.setEnabled(false);
            }

        }


    }
    public void Reactivos(){

        List<Reactivo> camposForm = new ArrayList<Reactivo>();
        Reactivo foto1=new Reactivo();
        foto1.setId(1);
        foto1.setLabel("FOTO ");
        foto1.setSigId(2);
        foto1.setType("agregarImagen");
        foto1=new Reactivo();
        foto1.setLabel(getString(R.string.comentarios));
        foto1.setNombreCampo("comentarios");
        foto1.setType("textarea");
        foto1.setId(2);

        camposForm.add(foto1);

    }
    //btnaifotoexhibido
    public void crearFormulario(){
        camposForm=new ArrayList<>();
        CampoForm campo=new CampoForm();
        if(preguntaAct==0)
        {
            campo.label=getString(R.string.seleccione_planta);
            campo.nombre_campo="planta";
            campo.type="selectDes";
            campo.style=R.style.formlabel2;
            campo.id=1001;
            if(isEdicion)
                campo.value=ultimares;

            cargarPlantas(campo);


        }else
        if(preguntaAct<6){ //es para foto
        campo.label="FOTO "+preguntaAct;
        campo.nombre_campo="foto";
        campo.type="agregarImagen";
        campo.style=R.style.formlabel2;


        campo.id=1001;
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

        if(isEdicion){
                // Bitmap bitmap1 = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null) + "/" + nombre_foto);
                //ComprasUtils cu=new ComprasUtils();
                // bitmap1=cu.comprimirImagen(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + ultimares.getValor());
                Bitmap bitmap1= ComprasUtils.decodeSampledBitmapFromResource(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + ultimares, 100, 100);
                campo.value=ultimares;
                fotomos.setImageBitmap(bitmap1);
                // fotomos.setLayoutParams(new LinearLayout.LayoutParams(350,150));
                fotomos.setVisibility(View.VISIBLE);

                btnrotar.setVisibility(View.VISIBLE);
                btnrotar.setFocusableInTouchMode(true);
                btnrotar.requestFocus();

            }
            // btnrotar.setVisibility(View.VISIBLE);


        }else{ //son los comentarios
            campo.label=getString(R.string.comentarios);
            campo.nombre_campo="comentarios";
            campo.type="textarea";
            campo.style=R.style.formlabel2;
            if(isEdicion)
                campo.value=ultimares;
            campo.id=1001;


        }

        campo.id=1001;
        camposForm.add(campo);
        Log.d(TAG,"haciendo form");
        cf=new CreadorFormulario(camposForm,getContext());
        sv.addView(cf.crearFormulario());
        if(preguntaAct==0) {
            spclientes = root.findViewById(1001);
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
        }else
            textoint = root.findViewById(1001);
    }
    public void guardarCliente(){
        lastClickTime=0;
        DescripcionGenerica opcionsel = (DescripcionGenerica) spclientes.getSelectedItem();

        //busco par de id, cliente
        String aux[]=opcionsel.getDescripcion().split(",");
        clienteId=Integer.parseInt(aux[0]);
        clienteNombre=aux[1];
        plantaSel=opcionsel.getId();
        nombrePlantaSel=opcionsel.getNombre();


       //creo el informe
        if ( !isEdicion&&mViewModel.getNvoinforme()==null) {
            Log.d(TAG, "creando nvo inf");

            mViewModel.setIdNuevo( mViewModel.insertarInformeEtapa(Constantes.INDICEACTUAL, nombrePlantaSel, plantaSel, clienteNombre, clienteId));
        } //   Log.d(TAG, "guardando informe"+mViewModel.numMuestra+"--"+mViewModel.getIdInformeNuevo());
        else{ //si es edicion
            //actualizo el cliente y la planta
            Log.d(TAG, "creando nvo inf---"+mViewModel.getNvoinforme().toString());

            mViewModel.getNvoinforme().setClienteNombre(clienteNombre);
            mViewModel.getNvoinforme().setClientesId(clienteId);
            mViewModel.getNvoinforme().setPlantasId(plantaSel);
            mViewModel.getNvoinforme().setPlantaNombre(nombrePlantaSel);
            mViewModel.actualizarInfEtapa(mViewModel.getNvoinforme());


        }
        ((NuevoInfEtapaActivity)getActivity()).actualizarBarra(mViewModel.getNvoinforme());
        avanzarPregunta(1);



    }
    public void siguiente(){

        lastClickTime=0;
        aceptar.setEnabled(false);

        if(preguntaAct==6) //termine inf comentarios
        {
            if(yaestoyProcesando)
                return;
            yaestoyProcesando=true;
              //  loadingDialog = new LoadingDialog(getActivity());
                //   loadingDialog.startLoadingDialog();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                    //guarda informe
                this.actualizarInforme();
                this.finalizar();
      //  mViewModel.eliminarTblTemp();
                        //   loadingDialog.dismisDialog();
                        Toast.makeText(getActivity(), getString(R.string.informe_finalizado), Toast.LENGTH_SHORT).show();
                        yaestoyProcesando = false;
                        salir();
                        //  aceptar.setEnabled(true);
                        return;


                }catch(Exception ex){
                    ex.printStackTrace();
                    yaestoyProcesando=false;

                    Toast.makeText(getActivity(), "algo salió mal", Toast.LENGTH_SHORT).show();

                }

            }else
            {
                Log.d(TAG,"-----preguntaact"+preguntaAct);
                if(preguntaAct>0)
                    guardarResp(preguntaAct+1);
               // avanzarPregunta(preguntaAct++);

           }
        aceptar.setEnabled(true);
    }
    //finalizar informe
    public void finalizar() {
        try {
            mViewModel.finalizarInf();
            InformeEtapaEnv informe=this.preparaInforme();
            SubirInformeEtaTask miTareaAsincrona = new SubirInformeEtaTask(informe,getActivity());
            miTareaAsincrona.execute();
            subirFotos(getActivity(),informe);
        }catch(Exception ex){
            ex.getStackTrace();
            Log.e(TAG,"Algo salió mal al enviar"+ex.getMessage());
            Toast.makeText(getContext(),"Algo salio mal al enviar",Toast.LENGTH_SHORT).show();
        }
        //todo limpio variables de sesion
        infvm.cont=0;
        mViewModel.setIdNuevo(0);
        mViewModel.setIddetalle(0);
        mViewModel.setNvoinforme(null);

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
            //intento1.putExtra("origen", origen);
            if (fotomos != null) {

                startActivityForResult(intento1, REQUEST_CODE_TAKE_PHOTO);

            }
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"vars"+requestCode +"--"+ nombre_foto);
        if ((requestCode == REQUEST_CODE_TAKE_PHOTO) && resultCode == RESULT_OK) {
            //   super.onActivityResult(requestCode, resultCode, data);

            if (archivofoto!=null&&archivofoto.exists()) {
                if(requestCode == REQUEST_CODE_TAKE_PHOTO) {
                    textoint.setText(nombre_foto);
                    if(ComprasUtils.getAvailableMemory(getActivity()).lowMemory)
                    {
                        Toast.makeText(getActivity(), "No hay memoria suficiente para esta accion", Toast.LENGTH_SHORT).show();

                        return;
                    }else {
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
                        nombre_foto=null;
                        archivofoto=null;
                    }

                }


            }
            else{
                Log.e(TAG,"Algo salió mal???");
            }


       }
          else
        {
            Log.e(TAG,"Algo salió muy mal**");
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

    public void guardarFoto(int sig){
        try {

            //Creo el informe en nuevo informe y lo busco aqui
            //necestio saber si ya habia guardado informe
            //veo si ya existe el informe o hay que crearlo
            Log.d(TAG, "guardando foto");
            //   exit(0);
            if (preguntaAct == 1 && !isEdicion&&mViewModel.getNvoinforme()==null) {
                Log.d(TAG, "creando nvo inf");
                //busco el consecutivo
                mViewModel.setIdNuevo( mViewModel.insertarInformeEtapa(Constantes.INDICEACTUAL, nombrePlantaSel, plantaSel, clienteNombre, clienteId));

            } //   Log.d(TAG, "guardando informe"+mViewModel.numMuestra+"--"+mViewModel.getIdInformeNuevo());

                //
              //  idInformeNuevo.observe(getViewLifecycleOwner(), new Observer<Integer>() {
                /*    @Override
                    public void onChanged(Integer idnvo) {
                        Log.d(TAG, "se creo el informe" + idnvo);
                        mViewModel.informe.setId(idnvo);
                        mViewModel.setIdInformeNuevo(idnvo);
                        // mViewModel.informe.setSinproducto();

                            //si tengo detalle
                            Log.d(TAG,"guardando  foto "+preguntaAct);

                            //    List<Integer> muestras= dViewModel.muestrasTotales();
                            //  for(int x:muestras) {
                            int nuevoid = dViewModel.insertarDetalle(mViewModel.getIdInformeNuevo(), mViewModel.numMuestra);
                            //guardo la muestra
                            if (nuevoid > 0&&dViewModel.icdNuevo!=null) {
                                dViewModel.setIddetalleNuevo(nuevoid);

                            }
                            else{
                                //algo salio mal
                                Toast.makeText(getActivity(), "No se pudo guardar la muestra", Toast.LENGTH_LONG).show();
                                yaestoyProcesando=false;
                                return;
                            }
                            yaestoyProcesando=false;

                                avanzarPregunta(sig);
                                //preguntar si hay otro cliente, para agregar otro o cerrar


                        //  idInformeNuevo.removeObservers(DetalleProductoFragment.this);

                    }

                });

            }else //ya tengo informe y solo guardo el det
          {*/
              //  si tengo detalle
                Log.d(TAG,"guardando  detalle"+mViewModel.getIdNuevo());
            int nuevoid =0;
            if(isEdicion){
                nuevoid = mViewModel.insertarInfEtaDet(mViewModel.getIdNuevo(), 10, "foto_preparacion" + preguntaAct, textoint.getText().toString(),detalleEdit.getId());

            }else {   //guardo la muestra
                nuevoid = mViewModel.insertarInfEtaDet(mViewModel.getIdNuevo(), 10, "foto_preparacion" + preguntaAct, textoint.getText().toString(),0);
            }
                if (nuevoid > 0) {


                    yaestoyProcesando=false;
                    Log.d(TAG,"en guaradar"+sig);
                        avanzarPregunta(sig);
                    }

        }catch (Exception ex){
            ex.printStackTrace();
            Log.e(TAG,ex.getMessage());
            Toast.makeText(getActivity(), "No se pudo guardar la muestra", Toast.LENGTH_LONG).show();

        }

    }
    public InformeEtapaEnv preparaInforme(){
        InformeEtapaEnv envio=new InformeEtapaEnv();
        //busco el informe

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
            //cambio su estatus a subiendo



        }

    }

    public void avanzarPregunta(int sig){
        Log.d(TAG,"voy a la sig pregunta"+sig);
        //pregunto si otra foto
        //pregunto si habrá más clientes
        if(sig==1){
            preguntaSig = sig;

            NvaPreparacionFragment nvofrag = new NvaPreparacionFragment(preguntaSig, false,null);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
// Definir una transacción
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
// Remplazar el contenido principal por el fragmento
            fragmentTransaction.replace(R.id.continfeta_fragment, nvofrag);
            //     fragmentTransaction.addToBackStack(null);
// Cambiar
            fragmentTransaction.commit();}
       else if(sig<6) {
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
            //dialogo1.setTitle(R.);
            dialogo1.setMessage(R.string.req_foto);
            dialogo1.setCancelable(true);
            dialogo1.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    //siguiente queda igual
                    preguntaSig = sig;
                    dialogo1.cancel();
                    NvaPreparacionFragment nvofrag = new NvaPreparacionFragment(preguntaSig, false,null);
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
            dialogo1.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    //me voy a comentarios el 6
                    preguntaSig = 6;
                    dialogo1.cancel();
                    NvaPreparacionFragment nvofrag = new NvaPreparacionFragment(preguntaSig, false,null);
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
            dialogo1.show();
        }else { //son los comentarios
            NvaPreparacionFragment nvofrag = new NvaPreparacionFragment(sig, false,null);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
// Definir una transacción
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
// Remplazar el contenido principal por el fragmento
            fragmentTransaction.replace(R.id.continfeta_fragment, nvofrag);
            //     fragmentTransaction.addToBackStack(null);
// Cambiar
            fragmentTransaction.commit();

        }


    }

    //guardo en tabla temp
    public void guardarResp(int sig) {
      //  Log.d(TAG, "guardando en temp*****" + preguntaAct.getId());
        String valor = null;
        if (textoint != null) {
            valor = textoint.getText().toString();
            valor = valor.toUpperCase();
        }
        //paso a mayusculas
        Log.d(TAG, "guardando en temp" + preguntaAct + "val" + valor);
        if (preguntaAct > 0 && valor != null && valor.length() > 0) {
            guardarFoto( sig);
        }

    }


    public void actualizarInforme() throws Exception {
        String valor = null;
        if (textoint != null) {
            valor = textoint.getText().toString();
            valor = valor.toUpperCase();
        }
        mViewModel.actualizarComentarios(mViewModel.getIdNuevo(),valor);
    }
    public void cargarPlantas(CampoForm campo) {

        campo.selectdes= listaPlantas;

        //  }
        //  else
        //    campo.selectdes= Constantes.clientesAsignados;
    }

    private  void convertirLista(List<ListaCompra>lista){
        listaPlantas=new ArrayList<DescripcionGenerica>();
        for (ListaCompra listaCompra: lista ) {
          /*String tupla=Integer.toString(listaCompra.getClienteId())+";"+
          listaCompra.getPlantaNombre();*/

            listaPlantas.add(new DescripcionGenerica(listaCompra.getPlantasId(), listaCompra.getClienteNombre()+" "+listaCompra.getPlantaNombre(),listaCompra.getClientesId()+","+listaCompra.getClienteNombre()));

        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel = null;

        cf=null;
        camposForm=null;

        root=null;
        textoint=null;


        fotomos=null;
        sv=null;
        btnrotar=null;

        aceptar=null;


        nombre_foto=null;
        archivofoto=null;
    }


}