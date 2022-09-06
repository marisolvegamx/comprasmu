package com.example.comprasmu.ui.etiquetado;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.SubirCorreccionTask;
import com.example.comprasmu.SubirInformeEtaTask;
import com.example.comprasmu.data.modelos.Correccion;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.SolicitudCor;
import com.example.comprasmu.data.remote.CorreccionEnvio;
import com.example.comprasmu.data.remote.InformeEtapaEnv;
import com.example.comprasmu.services.SubirFotoService;
import com.example.comprasmu.ui.RevisarFotoActivity;
import com.example.comprasmu.ui.infetapa.NuevoInfEtapaActivity;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.ui.preparacion.NvaPreparacionViewModel;

import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class NvoEtiquetadoFragment extends Fragment {

    LinearLayout sv1,sv2,sv3,sv4,sv5;
    private static final String TAG = "NvoEtiquetadoFragment";
    Button aceptar1,aceptar2,aceptar3,aceptar4,guardar;
    private long lastClickTime = 0;
    private boolean yaestoyProcesando=false;
    EditText txtnumcajas,txtrutaim,txtcomentarios, txtqr;
    ImageView fotomos;
    private ImageButton btnrotar;
    public static  int REQUEST_CODE_TAKE_PHOTO=1;
    String rutafotoo;
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
    int contcaja, contmuestra;
    int totcajas=0,totmuestras;
    private  ArrayList<DescripcionGenerica> listaPlantas;
    private boolean isEdicion;

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
        aceptar1 = root.findViewById(R.id.btnneac1);
        aceptar2 = root.findViewById(R.id.btnneac2);
        aceptar3 = root.findViewById(R.id.btnneac3);
        aceptar4 = root.findViewById(R.id.btnneac4);
        guardar = root.findViewById(R.id.btnneguardar);

        mViewModel = new ViewModelProvider(requireActivity()).get(NvaPreparacionViewModel.class);
        lcViewModel = new ViewModelProvider(this).get(ListaDetalleViewModel.class);

        sv1.setVisibility(View.VISIBLE);
        sv2.setVisibility(View.GONE);
        sv3.setVisibility(View.GONE);
        sv4.setVisibility(View.GONE);
        sv5.setVisibility(View.GONE);

        aceptar1.setEnabled(false);
        //buscar la solicitud
        Bundle datosRecuperados = getArguments();

        if(datosRecuperados!=null) {
            informeSel = datosRecuperados.getInt(NuevoInfEtapaActivity.INFORMESEL);

        }
        //busco si tengo varias plantas
        listacomp= lcViewModel.cargarPestañasSimp(Constantes.CIUDADTRABAJO);
        preguntaAct=1;
        if(mViewModel.getIdNuevo()==0)
            if(listacomp.size()>1) {
                //tengo varias plantas
                preguntaAct=2;

                convertirLista(listacomp);
                mViewModel.variasPlantas=true;
            }else if(listacomp.size()>0) {
                mViewModel.variasPlantas = false;
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

        if(isCont) { //busco el informe
            mViewModel.getInformeEdit(informeSel).observe(getViewLifecycleOwner(), new Observer<InformeEtapa>() {
                @Override
                public void onChanged(InformeEtapa informeEtapa) {
                    infomeEdit = informeEtapa;
                    ((NuevoInfEtapaActivity) getActivity()).actualizarBarra(informeEtapa);
                    cargarDatos();


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
        aceptar3.setOnClickListener(new View.OnClickListener() {
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


            }
        });



        return root;
    }


    public void cargarDatos(){


    }
    public void avanzar(){
        switch (preguntaAct){
            case 1:
                sv1.setVisibility(View.GONE);
                sv2.setVisibility(View.VISIBLE);
                break;
            case 2:
                 sv2.setVisibility(View.GONE);
                 sv3.setVisibility(View.VISIBLE);
                 guardarInf();
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
                actualizarComent();
                finalizarInf();
                break;
        }
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
            int numcajas = 0;
            txtnumcajas=root.findViewById(R.id.txtnetotalcajas);
            numcajas =Integer.parseInt(txtnumcajas.getText().toString());

            if (preguntaAct == 1 && !isEdicion&&mViewModel.getNvoinforme()==null) {
                Log.d(TAG, "creando nvo inf");
                //creo el informe
                 mViewModel.setIdNuevo(mViewModel.insertarEtiq(Constantes.INDICEACTUAL,nombrePlantaSel,plantaSel,clienteNombre,clienteId,numcajas,totmuestras));
            }
        }catch (Exception ex){
            ex.getStackTrace();
            Log.e(TAG,"Algo salió mal al enviar"+ex.getMessage());
            Toast.makeText(getContext(),"Hubo un error al guardar intente de nuevo",Toast.LENGTH_SHORT).show();

        }
            aceptar1.setEnabled(true);
        }
        public void guardarDet(){
            try{
                String rutafoto = null;
                String qr = null;
                txtrutaim=root.findViewById(R.id.txtneruta);
                txtqr=root.findViewById(R.id.txtneqr);
                rutafoto=txtrutaim.getText().toString();
                qr=txtqr.getText().toString();
                if(mViewModel.getIdNuevo()>0)
                    //guardo el detalle
                     mViewModel.insertarEtiqDet(mViewModel.getIdNuevo(),1,"foto_etiquta",rutafoto,0,contcaja,qr,contmuestra);
               //limpio campos
                //muevo contadores
                //actualizo vista

            }catch (Exception ex){
                ex.getStackTrace();
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
            ex.getStackTrace();
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
        txtcomentarios=root.findViewById(R.id.txtnecomentarios);
        if(txtcomentarios.getText()!=null)
            comentarios=txtcomentarios.getText().toString().toUpperCase();

        try{
            mViewModel.actualizarComentarios(mViewModel.getIdNuevo(),comentarios);
        }catch(Exception ex){
            ex.getStackTrace();
            Log.e(TAG,"Algo salió mal al enviar"+ex.getMessage());
            Toast.makeText(getContext(),"Algo salio mal al enviar",Toast.LENGTH_SHORT).show();
        }
    }
    public void rotar(int idcampo,int numimagen){
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


        }
        else
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

        envio.setInformeEtapa(mViewModel.getNvoinforme());
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

