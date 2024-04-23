package com.example.comprasmu.ui.correccion;

import static android.app.Activity.RESULT_OK;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.SubirCorreccionTask;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeEnvioPaq;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.modelos.SolicitudCor;
import com.example.comprasmu.data.remote.CorreccionEnvio;
import com.example.comprasmu.data.remote.InformeEnvio;
import com.example.comprasmu.services.SubirFotoService;
import com.example.comprasmu.ui.RevisarFotoActivity;
import com.example.comprasmu.ui.infetapa.NuevoInfEtapaActivity;
import com.example.comprasmu.ui.solcorreccion.ListaSolsViewModel;
import com.example.comprasmu.utils.CampoForm;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;
import com.example.comprasmu.utils.micamara.MiCamaraActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/****correccion envio***/
public class NvaCorreccionEnvFragment extends Fragment {


    CreadorFormulario cf;
    List<CampoForm> camposForm;
    LinearLayout sv,sv2,sv3;
    private static final String TAG = "NvaCorreccionEnvFragment";
    Button aceptar;

    private long lastClickTime = 0;
    private final boolean yaestoyProcesando=false;

    int solicitudSel;
    EditText textoint,txtrutaim2,txtrutaim3;
    ImageView fotomos,fotomos2,fotomos3, fotoori1,fotoori2,fotoori3;
    private ImageButton btnrotar,btnrotar2,btnrotar3;
    public static  int REQUEST_CODE_TAKE_PHOTO=1;
    public static int REQUEST_CODE2 = 2;
    public static int REQUEST_CODE3 = 3;
    String rutafotoo;
    String rutafotoo3;
    String rutafotoo2;
    private View root;
    private NvaCorreViewModel mViewModel;
    ListaSolsViewModel solViewModel;
    SolicitudCor solicitud;
    TextView txtmotivo;
    int numfoto;

    public static NvaCorreccionEnvFragment newInstance() {
        return new NvaCorreccionEnvFragment();
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        root= inflater.inflate(R.layout.fragment_correccion, container, false);
        sv = root.findViewById(R.id.content_generic);
        sv2 = root.findViewById(R.id.content_generic2);
        sv3 = root.findViewById(R.id.content_generic3);
        aceptar = root.findViewById(R.id.btngaceptar);
        txtmotivo=root.findViewById(R.id.txtcmotivo);
        mViewModel = new ViewModelProvider(requireActivity()).get(NvaCorreViewModel.class);
        solViewModel = new ViewModelProvider(requireActivity()).get(ListaSolsViewModel.class);
        aceptar.setEnabled(false);
        //buscar la solicitud
        Bundle datosRecuperados = getArguments();

        if(datosRecuperados!=null) {
            solicitudSel = datosRecuperados.getInt(NuevoInfEtapaActivity.INFORMESEL);
            numfoto = datosRecuperados.getInt(NuevoInfEtapaActivity.NUMFOTO);

        }
        fotoori1=root.findViewById(R.id.ivcoriginal);

        solViewModel.getSolicitud(solicitudSel,numfoto).observe(getViewLifecycleOwner(), new Observer<SolicitudCor>() {
            @Override
            public void onChanged(SolicitudCor solicitudCor) {
                solicitud=solicitudCor;
                Log.e(TAG,"estatus "+solicitud.getInformesId()+numfoto);

                if(solicitudCor.getInformesId()>0) {
                    InformeEnvioPaq informe = solViewModel.getInformeEnvSol(solicitudCor.getInformesId());

                    ((NuevoInfEtapaActivity)getActivity()).actBarraCorreEnvio(solicitud);

                    if(informe!=null&&informe.infEnvioDet!=null){
                        crearFormulario();

                        //BUSCO LA FOTO ORIGINAL
                        //en donde la busco

                        LiveData<ImagenDetalle> imagen=solViewModel.buscarImagenCom(informe.infEnvioDet.getFotoSello());
                        imagen.observe(getViewLifecycleOwner(), new Observer<ImagenDetalle>() {
                            @Override
                            public void onChanged(ImagenDetalle imagenDetalle) {


                                rutafotoo=imagenDetalle.getRuta();

                                Bitmap bitmap1= ComprasUtils.decodeSampledBitmapFromResource(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + rutafotoo, 80, 80);

                                fotoori1.setImageBitmap(bitmap1);

                                // fotomos.setLayoutParams(new LinearLayout.LayoutParams(350,150));
                                //fotoori1.setVisibility(View.VISIBLE);

                            }
                        });
                    }

                }
                fotoori1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        verImagen(rutafotoo);
                    }
                });
            }
        });
        aceptar.setText(getString(R.string.enviar));
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


                    guardar();


            }
        });


        return root;
    }


    public void crearFormulario(){

        camposForm=new ArrayList<>();
        CampoForm campo=new CampoForm();
        campo.label=solicitud.getMotivo();
        campo.nombre_campo="label";
        campo.type="label";
        campo.style=R.style.formlabel2;
        camposForm.add(campo);
        campo=new CampoForm();
        campo.label="NUEVA";
        campo.nombre_campo="label";
        campo.type="label";
        camposForm.add(campo);
         campo=new CampoForm();
        campo.label=solicitud.getDescMostrar();
        campo.nombre_campo="foto";
        campo.type="agregarImagen";
        campo.style=R.style.formlabel2;
        campo.id=1001;
        campo.funcionOnClick = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tomarFoto(REQUEST_CODE_TAKE_PHOTO);
                }
        };
        campo.tomarFoto = true;
        fotomos=root.findViewById(R.id.ivgfoto);

       // fotomos.setVisibility(View.VISIBLE);
        btnrotar=root.findViewById(R.id.btngrotar);
        btnrotar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rotar(1001,1);
                }
            });


        camposForm.add(campo);
       // Log.d(TAG,"haciendo form");
        cf=new CreadorFormulario(camposForm,getContext());
        sv.addView(cf.crearFormulario());

        if(solicitud.getDescripcionFoto().equals("foto_atributoa")) { //es 360
            camposForm = new ArrayList<>();
            campo = new CampoForm();
            campo.label = getString(R.string.foto_posicion2);
            campo.nombre_campo = "foto2";
            campo.type = "agregarImagen";
            campo.style = R.style.formlabel2;
            campo.id = 1002;
            campo.funcionOnClick = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tomarFoto(REQUEST_CODE2);
                }
            };
            campo.tomarFoto = true;

            fotomos2 = root.findViewById(R.id.ivgfoto2);
        //    fotomos2.setVisibility(View.VISIBLE);
            btnrotar2 = root.findViewById(R.id.btngrotar2);
            btnrotar2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rotar(1002,2);
                }
            });

            camposForm.add(campo);

            cf = new CreadorFormulario(camposForm, getContext());
            sv2.addView(cf.crearFormulario());
            sv2.setVisibility(View.VISIBLE);
            txtrutaim2 = root.findViewById(1002);

            camposForm = new ArrayList<>();
            campo = new CampoForm();
            campo.label = getString(R.string.foto_posicion3);
            campo.nombre_campo = "foto3";
            campo.type = "agregarImagen";
            campo.style = R.style.formlabel2;
            campo.id = 1003;
            campo.funcionOnClick = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tomarFoto(REQUEST_CODE3);
                }
            };
            campo.tomarFoto = true;

            camposForm.add(campo);
            Log.d(TAG, "haciendo form");
            cf = new CreadorFormulario(camposForm, getContext());
            sv3.addView(cf.crearFormulario());
            sv3.setVisibility(View.VISIBLE);
            txtrutaim3 = root.findViewById(1003);
            fotomos3 = root.findViewById(R.id.ivgfoto3);
            //  fotomos3.setVisibility(View.VISIBLE);
            btnrotar3 = root.findViewById(R.id.btngrotar3);
            btnrotar3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rotar(1003,3);
                }
            });
        }
        textoint = root.findViewById(1001);
    }
    public void guardar(){
        try {
            lastClickTime = 0;
            String valor = null;
            String valor2 = null;
            String valor3 = null;
            if (textoint != null) {
                valor = textoint.getText().toString();
                valor = valor.toUpperCase();
            }
            if (txtrutaim2 != null) {
                valor2 = txtrutaim2.getText().toString();
                valor2 = valor2.toUpperCase();
            }
            if (txtrutaim3 != null) {
                valor3 = txtrutaim3.getText().toString();
                valor3 = valor3.toUpperCase();
            }
            //valido q haya 3 fotos si es de atributo
            if(solicitud.getDescripcionFoto().equals("foto_atributoa")) { //es 360
                if(valor2.equals("")||valor3.equals("")){
                    Toast.makeText(getContext(),"Favor de capturar las 3 fotos",Toast.LENGTH_LONG).show();
                    return;
                }
            }
                //paso a
            //creo el informe
            mViewModel.setIdNuevo(mViewModel.insertarCorreccion(solicitud.getId(), Constantes.INDICEACTUAL,solicitud.getNumFoto(),valor, valor2, valor3));
            actualizarSolicitud();
            Toast.makeText(getContext(),"Informe guardado correctamente",Toast.LENGTH_SHORT).show();
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            salir();

        }catch (Exception ex){
            ex.getStackTrace();
            Log.e(TAG,"Algo salió mal al enviar"+ex.getMessage());
            Toast.makeText(getContext(),"Hubo un error al guardar intente de nuevo",Toast.LENGTH_SHORT).show();

        }
        aceptar.setEnabled(true);
    }

    //cambiar estatus sol
    public void actualizarSolicitud() {
        try {
            solViewModel.actualizarEstSolicitud(solicitudSel,numfoto,4);
            CorreccionEnvio envio=mViewModel.prepararEnvio(mViewModel.getNvocorreccion());
            SubirCorreccionTask miTareaAsincrona = new SubirCorreccionTask(envio,getActivity());
            miTareaAsincrona.execute();

            subirFotos(getActivity(),envio.getCorreccion().getId(),envio.getCorreccion().getRuta_foto1());


            if(envio.getCorreccion().getRuta_foto2()!=null&&envio.getCorreccion().getRuta_foto2().length()>1)
                subirFotos(getActivity(),envio.getCorreccion().getId(),envio.getCorreccion().getRuta_foto2());
            if(envio.getCorreccion().getRuta_foto3()!=null&&envio.getCorreccion().getRuta_foto3().length()>1)
                subirFotos(getActivity(),envio.getCorreccion().getId(),envio.getCorreccion().getRuta_foto3());

        }catch(Exception ex){
            ex.getStackTrace();
            Log.e(TAG,"Algo salió mal al enviar"+ex.getMessage());
            Toast.makeText(getContext(),"Algo salio mal al enviar",Toast.LENGTH_SHORT).show();
        }
        //todo limpio variables de sesion
        mViewModel.setIdNuevo(0);

        mViewModel.setNvocorreccion(null);

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
            if(numimagen==1)
            RevisarFotoActivity.rotarImagen(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" +foto,fotomos);
            if(numimagen==2)
                RevisarFotoActivity.rotarImagen(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" +foto,fotomos2);

            if(numimagen==3)
                RevisarFotoActivity.rotarImagen(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" +foto,fotomos3);

        }
    }
    public void verImagen(String nombrearch){
        //  ImageView imagen=(ImageView)v;
        // imagen.get
        Log.e(TAG,"arch"+nombrearch);
        Intent iverim=new Intent(getActivity(), RevisarFotoActivity.class);
        iverim.putExtra(RevisarFotoActivity.IMG_PATH1,nombrearch);
        startActivity(iverim);
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
        if ((requestCode == REQUEST_CODE_TAKE_PHOTO||requestCode == REQUEST_CODE2||requestCode == REQUEST_CODE3) && resultCode == RESULT_OK) {
            //   super.onActivityResult(requestCode, resultCode, data);

            if (archivofoto!=null&&archivofoto.exists()) {
                if(requestCode == REQUEST_CODE_TAKE_PHOTO) {
                    mostrarFoto(textoint,fotomos,btnrotar);
                }
                if(requestCode == REQUEST_CODE2) {
                    View grupo=root.findViewById(R.id.gpofoto2);
                    grupo.setVisibility(View.VISIBLE);
                    mostrarFoto(txtrutaim2,fotomos2,btnrotar2);

                }
                if(requestCode == REQUEST_CODE3) {
                    View grupo=root.findViewById(R.id.gpofoto3);
                    grupo.setVisibility(View.VISIBLE);
                    mostrarFoto(txtrutaim3,fotomos3,btnrotar3);
                }
                aceptar.setEnabled(true);

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

    public void mostrarFoto(EditText textorut,ImageView xfotomos, ImageButton xbtnrotar){


            textorut.setText(nombre_foto);
            if(ComprasUtils.getAvailableMemory(getActivity()).lowMemory)
            {
                Toast.makeText(getActivity(), "No hay memoria suficiente para esta accion", Toast.LENGTH_SHORT).show();

                return;
            }else {
                // Bitmap bitmap1 = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null) + "/" + nombre_foto);
                ComprasUtils cu = new ComprasUtils();
                cu.comprimirImagen(archivofoto.getAbsolutePath());
                Bitmap bitmap1 = ComprasUtils.decodeSampledBitmapFromResource(archivofoto.getAbsolutePath(), 80, 80);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel = null;
        solViewModel=null;
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

