package com.example.comprasmu.ui.correccion;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.SubirCorreccionTask;
import com.example.comprasmu.SubirInformeEtaTask;
import com.example.comprasmu.data.modelos.Correccion;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.modelos.InformeTemp;
import com.example.comprasmu.data.modelos.Reactivo;
import com.example.comprasmu.data.modelos.SolicitudCor;
import com.example.comprasmu.data.remote.CorreccionEnvio;
import com.example.comprasmu.data.remote.InformeEtapaEnv;
import com.example.comprasmu.services.SubirFotoService;
import com.example.comprasmu.ui.RevisarFotoActivity;
import com.example.comprasmu.ui.infetapa.NuevoInfEtapaActivity;
import com.example.comprasmu.ui.informedetalle.NuevoDetalleViewModel;
import com.example.comprasmu.ui.preparacion.NvaPreparacionViewModel;
import com.example.comprasmu.ui.solcorreccion.ListaSolsViewModel;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;
import com.example.comprasmu.utils.DetalleInfView;
import com.example.comprasmu.utils.micamara.MiCamaraActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NvaCorrecCalCajaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NvaCorrecCalCajaFragment extends Fragment {


    CreadorFormulario cf;
    LinearLayout  sv2;
    private static final String TAG = "NvaCorrecCalCajaFragment";
    private long lastClickTime = 0;
    int solicitudSel;

    private static final int REQUEST_CODEQR = 341;
    HashMap<Integer,String> cajasValues;

    public static int REQUEST_CODE_TAKE_PHOTO = 1;

    LiveData<List<Correccion>> nuevasCor;
    private View root;
    private NvaCorreViewModel mViewModel;
    ListaSolsViewModel solViewModel;
    private NvaPreparacionViewModel preViewModel;
    SolicitudCor solicitud;
    int numfoto;
    InformeEtapa infEtiquetado;
    private Reactivo preguntaAct;
    Correccion correccion; //por si es edicion
    LinearLayout svprin;
    private NuevoDetalleViewModel dViewModel;
    private ComprasLog compraslog;
    private DetalleInfView preguntaview;
    private DetalleInfView preguntaview2;
    private boolean isEdicion;
    private int contCajas;
    private int cajaAct;
    private int totcajas;
    List<InformeEtapaDet> detallesInf;

    private InformeEtapaDet muestraEdit;
    private int ultimacaja;
    private boolean reubicoMuestra;
    private TextView txtcajaact;

    public NvaCorrecCalCajaFragment() {
        // Required empty public constructor
    }

    /**
     * Informe de correccion de calidad caja para etiquetado
     *Pasa pregunta por pregunta
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NvaCorrecCalCajaFragment.
     */
    public static NvaCorrecCalCajaFragment newInstance(String param1, String param2) {
        NvaCorrecCalCajaFragment fragment = new NvaCorrecCalCajaFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle datosRecuperados = getArguments();
        if (datosRecuperados != null) {
            solicitudSel = datosRecuperados.getInt(NuevoInfEtapaActivity.INFORMESEL);
            numfoto = datosRecuperados.getInt(NuevoInfEtapaActivity.NUMFOTO);

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_generic2, container, false);
        svprin = root.findViewById(R.id.fgllcontentmain);
        sv2 = root.findViewById(R.id.fgll2); //para las cajas
        txtcajaact = root.findViewById(R.id.fgtxttitulo1);
        mViewModel = new ViewModelProvider(requireActivity()).get(NvaCorreViewModel.class);
        solViewModel = new ViewModelProvider(requireActivity()).get(ListaSolsViewModel.class);
        preViewModel = new ViewModelProvider(requireActivity()).get(NvaPreparacionViewModel.class);
        dViewModel = new ViewModelProvider(requireActivity()).get(NuevoDetalleViewModel.class);

        compraslog = ComprasLog.getSingleton();
        try {
            //    aceptar.setEnabled(false);
            solViewModel.getSolicitud(solicitudSel, numfoto).observe(getViewLifecycleOwner(), new Observer<SolicitudCor>() {
                @Override
                public void onChanged(SolicitudCor solicitudCor) {
                    solicitud = solicitudCor;
                    Log.e(TAG, "estatus " + solicitud.getInformesId() + numfoto + "--" + solicitud.getEtapa());
                    //busco el consecutivo de la tienda
                    int constienda = 0;
                    // txtmotivo.setText(solicitud.getMotivo());
                    infEtiquetado = preViewModel.getInformexId(solicitudCor.getInformesId());
                    ((NuevoInfEtapaActivity)getActivity()).actualizarBarraCorEta(solicitud,0);


                    //busco el total de cajas del informe
                    detallesInf = preViewModel.getInfDetCalCaja(infEtiquetado.getId()); //solo de calidad de caja
                    if (detallesInf != null && detallesInf.size() > 0) {
                        totcajas = preViewModel.getTotCajasxInf(infEtiquetado.getId());
                        contCajas = 1;
                        cajaAct = detallesInf.get(0).getNum_caja();
                        crearPregunta();
                    }

                }
            });
        }catch(Exception ex){
            ex.printStackTrace();
            compraslog.grabarError(ex.getMessage());
        }
        return root;


    }

    public void crearPregunta() {
        int num_pregact = 120;
        Log.d(TAG, "creando preg");


        if(preguntaAct==null) //para la 1a vez
        {

            //busco preguntaAct
            preguntaAct = dViewModel.buscarReactivoSimpl(num_pregact);
        }

        try {
            if (preguntaAct == null) {
                return;
            }
            if(preguntaAct.getId()>122) {
                //todo saber en que pregunta voy primero veo si ya tiene una correccion iniciada es edicion
                int descripcionId=12;
                if(preguntaAct.getId()==124){
                    descripcionId=13;
                }
                if(preguntaAct.getId()==125){
                    descripcionId=14;
                }
                //busco el numfoto
                InformeEtapaDet lddetalle = preViewModel.getByDescripCajaSim(infEtiquetado.getId(), descripcionId, cajaAct);

                if (lddetalle != null) {
                    int num_foto=0;
                    try {
                        num_foto = Integer.parseInt(lddetalle.getRuta_foto());
                    } catch (NumberFormatException ex) {
                        compraslog.grabarError(TAG, "guardarCorr", ex.getMessage());
                    }
                    correccion = mViewModel.getUltCorrecionxSolSimple(solicitudSel, num_foto, Constantes.INDICEACTUAL);
                }
            }
            if(correccion!=null){
                isEdicion=true;
                //y ya iba en las fotos
                num_pregact=123;
            }


            cajasValues=new HashMap<>();
          Log.d(TAG, "mmmmmmmmmmm" + preguntaAct.getId());
            crearFormulario();

            if (isEdicion)
                preguntaview.aceptarSetEnabled(true);
            else
                preguntaview.aceptarSetEnabled(false);

            preguntaview.onclickAceptar(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                preguntaview.aceptarSetEnabled(false);
                                                long currentClickTime = SystemClock.elapsedRealtime();
                                                // preventing double, using threshold of 1000 ms
                                                if (currentClickTime - lastClickTime < 5500) {
                                                    //  Log.d(TAG,"doble click :("+lastClickTime);
                                                    return;
                                                }

                                                lastClickTime = currentClickTime;


                                               siguiente();
                                            }
            });
            if(preguntaAct.getId()==121){
                preguntaview.onclickAceptar(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //validar qr
                        buscarQr();


                    }

                });
            }
            if(preguntaview.hayTextoInt()){ //los comentarios no son obligatorios
                preguntaview.miBotonTextWatcher();

            }

            if (preguntaAct.getId() == 7) {
                //cambio el boton a finalizar y muestro alerta
                preguntaview.setEnviarButton(getString(R.string.enviar));
            }
            if(preguntaview.hayPreguntaSino()){
                preguntaview.setPregSINoOnChange(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        preguntaview.aceptarSetEnabled(true);
                    }
                });
            }
            if(preguntaAct.getId()>122){
                txtcajaact.setText("CAJA "+cajaAct);
            }
        } catch(Exception ex)

    {
        ex.printStackTrace();
    }

}
    public void crearFormulario(){
        preguntaview=new DetalleInfView(getContext());
        preguntaview.setPreguntaAct(preguntaAct);
        InformeTemp inftemp=null;
        if(isEdicion) {
             inftemp=new InformeTemp();// en detalleinfview se usa informetemporal para obtener el valor que se edita
            //por eso creo un objeto para guardar el valor
            inftemp.setValor(correccion.getRuta_foto1());
            preguntaview.setUltimares(inftemp);
            preguntaview.setEdicion(isEdicion);
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
                if(inftemp.getValor().equals("0")){
                    //lo manejo en el view
                }else {
                    Bitmap bitmap1 = ComprasUtils.decodeSampledBitmapFromResource(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + inftemp.getValor(), 100, 100);

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
        if(preguntaAct.getId()==121) //qr y caja
       {
           //todavia no se como ponerlo
          //       cargarListaCajas();
                 //preguntaview.setCausas(cajasValues);

       }
        preguntaview.crearFormulario();

        svprin.addView(preguntaview);
        if(preguntaAct.getType().equals("botonqr")) {
            preguntaview.setEnviarButton("BUSCAR");


        }


    }
  public void mostrarPreg2(int opcion){
        sv2.setVisibility(opcion);
  }
    public void guardarCorr(){
        try {
            lastClickTime = 0;
            String valor = null;

            int nucaja=0;
            if (preguntaview.hayTextoInt()) {
                valor = preguntaview.getTextoint().toString();
                valor = valor.toUpperCase();

            }
            int descripcionId=12;
            if(preguntaAct.getId()==124){
                descripcionId=13;
            }
            if(preguntaAct.getId()==125){
                descripcionId=14;
            }
            //todavia no se donde guardar que hubo reubicacion
            //buscar el num de foto para las que son cara a y para las sig. cajas
            InformeEtapaDet lddetalle= preViewModel.getByDescripCajaSim(infEtiquetado.getId(),descripcionId,cajaAct);

            if (lddetalle != null) {
                int num_foto=0;
                try {
                             num_foto = Integer.parseInt(lddetalle.getRuta_foto());
                }catch(NumberFormatException ex){
                            compraslog.grabarError(TAG,"guardarCorr",ex.getMessage());
                }
                mViewModel.setIdNuevo(mViewModel.insertarCorreccionEtiq(solicitud.getId(), Constantes.INDICEACTUAL, num_foto, valor, "", "", ""));
                        preguntaview.aceptarSetEnabled(true);
            }



        }catch (Exception ex){
            ex.getStackTrace();
            Log.e(TAG,"Algo salió mal al guardar"+ex.getMessage());
            Toast.makeText(getContext(),"Hubo un error al guardar intente de nuevo",Toast.LENGTH_SHORT).show();

        }

    }
    public void buscarQr(){
        mostrarPreg2(View.GONE);
        String qr = null;
        int nucaja=0;
        if (preguntaview.hayTextoInt()) {
            qr = preguntaview.getTextoint().toString();

        }

        if(qr.equals("")) {
            return;
        }
        muestraEdit =preViewModel.buscarDetxQr(qr);
        if(muestraEdit ==null) { //busco el total de cajas

            Toast.makeText(getActivity(), getString(R.string.verifique_codigo), Toast.LENGTH_LONG).show();

            return;
        }

        InformeEtapa informeSel=preViewModel.getInformexId(muestraEdit.getInformeEtapaId());
        if(informeSel==null) {

            Toast.makeText(getActivity(), getString(R.string.verifique_codigo), Toast.LENGTH_LONG).show();

            return;
        }
        // mostrar lista cajas
        int clienteSel = informeSel.getClientesId();
        List<InformeEtapaDet> listacaj=preViewModel.listaCajasEtiqxCdCli(Constantes.CIUDADTRABAJO, clienteSel);
        cajasValues=new HashMap<>();
        if(listacaj!=null) {

            for (int i = 0; i < listacaj.size(); i++) {
                cajasValues.put(listacaj.get(i).getNum_caja() ,listacaj.get(i).getNum_caja() + "");
                ultimacaja=listacaj.get(i).getNum_caja();
            }
            totcajas = listacaj.size();
            Log.d(TAG,"totcajas"+totcajas+"--"+ultimacaja);
        }
        //creo para poner la caja
        preguntaview2=new DetalleInfView(getContext());
        Reactivo pregunta2=new Reactivo();
        pregunta2.setId(0);
        pregunta2.setType("select");
        pregunta2.setLabel("LA MUESTRA FINALMENTE QUEDO EN LA CAJA");
        pregunta2.setNombreCampo("caja");

        preguntaview2.setPreguntaAct(pregunta2);
        preguntaview2.aceptarSetEnabled(false);
        preguntaview2.setCausas(cajasValues);
        preguntaview2.setIdCampo(1002);
        preguntaview2.crearFormulario();
        preguntaview2.aceptarSetEnabled(true);
        sv2.addView(preguntaview2);
        sv2.setVisibility(View.GONE);

        preguntaview2.onclickAceptar(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preguntaview2.aceptarSetEnabled(false);
                long currentClickTime = SystemClock.elapsedRealtime();
                // preventing double, using threshold of 1000 ms
                if (currentClickTime - lastClickTime < 5500) {
                    //  Log.d(TAG,"doble click :("+lastClickTime);
                    return;
                }

                lastClickTime = currentClickTime;


                siguiente();
            }
        });
        // buscar 1ro qr y luego la caja
        mostrarPreg2(View.VISIBLE);

    }
    public void actualizarMuestra(){
        try {
            lastClickTime = 0;
           int numcaja=0;
            String valor2="";
            if (preguntaAct.getId()==121) {
                Log.d(TAG,"<<"+preguntaview2.getSpclientes().getSelectedItem());

                //busco la caja
                if(preguntaview2.haySPClientes())
                valor2 = preguntaview2.getSpclientes().getSelectedItem() + "";



                Log.d(TAG,"actmuestra "+preguntaview2.getSelectedItem()+"--"+valor2);

            }
            if(!valor2.equals(""))
                    try {
                        numcaja = Integer.parseInt(valor2);
                    }catch (NumberFormatException ex){
                        Log.e(TAG,ex.getMessage());
                        ex.printStackTrace();
                    }

            // validar que no queden cajas vacias
            if(validarUltMuesCaja()){
                preguntaview.aceptarSetEnabled(true);
                muestraEdit=null;
                return;
            }
            //actualizar el detalle el numero de caja buscarlo primero
            if(numcaja>0)
                muestraEdit.setNum_caja(numcaja);
            preViewModel.actualizarInfEtaDet(muestraEdit);

            InformeEtapaEnv envio=preViewModel.preparaInformeEtiqCor(solicitud.getInformesId(),muestraEdit);
            SubirInformeEtaTask miTareaAsincrona = new SubirInformeEtaTask(envio,getActivity());
            miTareaAsincrona.execute();
            Toast.makeText(getContext(),"Muestra reubicada correctamente",Toast.LENGTH_SHORT).show();


        }catch (Exception ex){
            ex.getStackTrace();
            Log.e(TAG,"Algo salió mal al enviar"+ex.getMessage());
            Toast.makeText(getContext(),"Hubo un error al guardar intente de nuevo",Toast.LENGTH_SHORT).show();

        }
        preguntaview.aceptarSetEnabled(true);
        muestraEdit=null;
    }


    //cambiar estatus sol y enviar todas las fotos y correcciones juntas
    public void actualizarSolicitud() {
        try {


            solViewModel.actualizarEstSolicitud(solicitudSel,numfoto,4);
           nuevasCor=mViewModel.getCorreccionesxsol(solicitudSel,Constantes.INDICEACTUAL);
           nuevasCor.observe(getViewLifecycleOwner(), new Observer<List<Correccion>>() {
               @Override
               public void onChanged(List<Correccion> correccions) {
                   CorreccionEnvio envio=mViewModel.prepararEnvioVar(correccions);
                   SubirCorreccionTask miTareaAsincrona = new SubirCorreccionTask(envio,getActivity());
                   miTareaAsincrona.execute();
                   for(Correccion cor:correccions) {
                       subirFotos(getActivity(), cor.getId(), cor.getRuta_foto1());
                   }
                   //todo limpio variables de sesion
                   mViewModel.setIdNuevo(0);

                   mViewModel.setNvocorreccion(null);
                   salir();
               }
           });


        }catch(Exception ex){
            ex.getStackTrace();
            Log.e(TAG,"Algo salió mal al enviar"+ex.getMessage());
            Toast.makeText(getContext(),"Algo salio mal al enviar",Toast.LENGTH_SHORT).show();
        }


    }

    //devuelve verdadero si es la ultima muestra
    public boolean validarUltMuesCaja(){
        int cajaorig= muestraEdit.getNum_caja();
        preViewModel.buscatTMuesxCaj(cajaorig, infEtiquetado.getId());
        if(preViewModel.numMuestras<2&&cajaorig<ultimacaja){
            Toast.makeText(getActivity(), "No es posible quitar la caja número "+cajaorig, Toast.LENGTH_LONG).show();

            return true;
        }
        return false;
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
        String foto=preguntaview.getTextoint().toString();
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


        } else  if(requestCode == REQUEST_CODEQR) {


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
    public void salir(){
        //mViewModel.eliminarTblTemp();
        //me voy a la lista de informes
        // getActivity().finish();
        Intent intento1 = new Intent(getActivity(), NavigationDrawerActivity.class);
        intento1.putExtra(NavigationDrawerActivity.NAVINICIAL,"listainformeeta");
        startActivity(intento1);
        getActivity().finish();
        // NavHostFragment.(this).navigate(R.id.action_selclientetolistacompras,bundle);


    }




    public static void subirFotos(Activity activity, int id, String ruta){

        //subo cada una
        Intent msgIntent = new Intent(activity, SubirFotoService.class);
        msgIntent.putExtra(SubirFotoService.EXTRA_IMAGE_ID,id);
        msgIntent.putExtra(SubirFotoService.EXTRA_IMG_PATH,ruta);

        msgIntent.putExtra(SubirFotoService.EXTRA_INDICE,Constantes.INDICEACTUAL);
        // Constantes.INDICEACTUAL
        Log.d(TAG,"subiendo fotos"+activity.getLocalClassName());

        msgIntent.setAction(SubirFotoService.ACTION_UPLOAD_COR);


        activity.startService(msgIntent);






    }

    public void siguiente(){

        if(preguntaAct.getId()==120){ //reubicaste muestra
                //reviso la opcion seleccionada
            if(!preguntaview.getPregSiNoResp()) //se selecciono no
            {
                reubicoMuestra = false; //todo guardar en bd talvez en la solicitud
                avanzarPregunta(preguntaAct.getSigAlt()); //voy a fotos caja
                return;


            }
            reubicoMuestra = false;
        } else if(preguntaAct.getId()==121) //es qr
        {
            actualizarMuestra(); //avanzo a la sig
        }else
           if(preguntaAct.getId()==122) //reubico otra
        {
            if(!preguntaview.getPregSiNoResp()) //se selecciono no
            {
                avanzarPregunta(preguntaAct.getSigAlt()); //voy a fotos caja
                return;


            }
        }
        else if(preguntaAct.getId()==123||preguntaAct.getId()==124) //fotos caja
        {
            guardarCorr();
        }else

            if(preguntaAct.getId()==125) //ultima foto
            {
                guardarCorr();
              //reviso si hay más cajas
                if(contCajas<totcajas){
                    contCajas++;
                    cajaAct=cajaAct+1;


                }
              else //es la ultima caja
                {


                    this.actualizarSolicitud(); //aqui me salgo

                        return;

                    }
            }

            avanzarPregunta(preguntaAct.getSigId());


        //   preguntaview.aceptarSetEnabled(true);
    }
    public void avanzarPregunta(int sig){
        Log.d(TAG,"avanzando "+sig);
        //busco el siguiente
        Reactivo nvoReac = dViewModel.buscarReactivoSimpl(sig);
      //limpio variables
        this.correccion=null;
        this.muestraEdit=null;
        preguntaview=null;
        preguntaview2=null;
        //creo uno nuevo
        preguntaview=new DetalleInfView(getContext());

        this.preguntaAct=nvoReac;
        this.isEdicion=false;
        svprin.removeAllViewsInLayout();
        sv2.removeAllViewsInLayout();
        crearPregunta();

    }

    public void iniciarLecQR(){
        IntentIntegrator integrator  =new  IntentIntegrator ( getActivity() ).forSupportFragment(NvaCorrecCalCajaFragment.this);
        integrator.setRequestCode(REQUEST_CODEQR);
        //  integrator.setOrientationLocked(false);
        Log.d(TAG, "inciando scanner");
        integrator.initiateScan();
    }

    public void atras(){
        if(preguntaAct.getId()==120){
            //no puedo regresar
            return;
        }
        if(preguntaAct.getId()==122){
            //no puedo regresar
            return;
        }
        this.correccion=null;
        this.muestraEdit=null;
        preguntaview=null;
        //creo uno nuevo
        preguntaview=new DetalleInfView(getContext());


        this.isEdicion=false;
        svprin.removeAllViewsInLayout();
        sv2.removeAllViewsInLayout();
        int idreact=preguntaAct.getId()-1;
        if (preguntaAct.getId()==123) { //son fotos de caja y tengo que buscar la foto
            //todo revisar si hay cajas anteriores
            if(contCajas>1)//es caja de enmedio
            {
                idreact = 125;
                contCajas--;
                cajaAct++;
            }
        }
        if (preguntaAct.getId()>122) { //son fotos de caja y tengo que buscar la foto
            //busco la ultima correccion
          //  correccion=mViewModel.getUltCorrecionxSolSimple
        }

        //busco el ant
        Reactivo reactivo = dViewModel.buscarReactivoSimpl(idreact);
        if(reactivo!=null) {


            crearPregunta();


        }

    }
    //el total de cajas si es x ciudad
    public void cargarListaCajas() {
       for (int i=cajaAct;i<totcajas;i++)
       {
           cajasValues.put(i,i + "");
                        //   cajaini = det.getNum_caja();
       }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel = null;
        solViewModel=null;
        cf=null;

        root=null;
    preguntaview=null;
    preguntaAct=null;

        //fotomos=null;
        svprin=null;
        nombre_foto=null;
        archivofoto=null;
    }


}