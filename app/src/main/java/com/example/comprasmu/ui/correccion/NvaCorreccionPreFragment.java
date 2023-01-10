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
import com.example.comprasmu.data.modelos.Correccion;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.modelos.SolicitudCor;
import com.example.comprasmu.data.remote.CorreccionEnvio;
import com.example.comprasmu.services.SubirFotoService;
import com.example.comprasmu.ui.RevisarFotoActivity;
import com.example.comprasmu.ui.infetapa.NuevoInfEtapaActivity;
import com.example.comprasmu.ui.solcorreccion.ListaSolsViewModel;
import com.example.comprasmu.utils.BotonRotar;
import com.example.comprasmu.utils.CampoForm;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class NvaCorreccionPreFragment extends Fragment {

    CreadorFormulario cf;
    List<CampoForm> camposForm;
    LinearLayout sv,sv2,sv3;
    private static final String TAG = "NvaCorreccionFragment";
    Button aceptar;
    private long lastClickTime = 0;
    private final boolean yaestoyProcesando=false;
    int solicitudSel;
    public static  int REQUEST_CODE_TAKE_PHOTO=1;
    public static int REQUEST_CODE2 = 2;
    public static int REQUEST_CODE3 = 3;
    private View root;
    private NvaCorreViewModel mViewModel;
    ListaSolsViewModel solViewModel;
    SolicitudCor solicitud;
    TextView txtmotivo;
    int numfoto;
    List<ImagenDetalle> fotosorig;
    int totalfotos;
    List<String> rutasnfotos; //para guardar las rutas de las fotos nuevas
    public static NvaCorreccionPreFragment newInstance() {
        return new NvaCorreccionPreFragment();
    }
    List<Correccion> nuevasCor;

    ComprasLog milog;
    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        root= inflater.inflate(R.layout.fragment_correccionpre, container, false);
        sv = root.findViewById(R.id.cpcontent_generic);
        sv2 = root.findViewById(R.id.cpcontent_generic2);
       // sv3 = root.findViewById(R.id.content_generic3);
        aceptar = root.findViewById(R.id.btncpaceptar);
        txtmotivo=root.findViewById(R.id.txtcpmotivo);
        mViewModel = new ViewModelProvider(requireActivity()).get(NvaCorreViewModel.class);
        solViewModel = new ViewModelProvider(requireActivity()).get(ListaSolsViewModel.class);
        aceptar.setEnabled(false);
        //buscar la solicitud
        Bundle datosRecuperados = getArguments();

        if(datosRecuperados!=null) {
            solicitudSel = datosRecuperados.getInt(NuevoInfEtapaActivity.INFORMESEL);
            numfoto = datosRecuperados.getInt(NuevoInfEtapaActivity.NUMFOTO);

        }
        milog=ComprasLog.getSingleton();
        solViewModel.getSolicitud(solicitudSel,numfoto).observe(getViewLifecycleOwner(), new Observer<SolicitudCor>() {
            @Override
            public void onChanged(SolicitudCor solicitudCor) {
                solicitud=solicitudCor;
                Log.e(TAG,"estatus "+solicitud.getEstatus());
                ((NuevoInfEtapaActivity)getActivity()).actualizarBarraCor(solicitud);

                //BUSCO LAS FOTOS ORIGINAL

                txtmotivo.setText(solicitud.getMotivo());
                switch(Constantes.ETAPAACTUAL){
                    case 1:
                        solViewModel.buscarFotosEta(solicitud.getInformesId(),Constantes.ETAPAACTUAL).observe(getViewLifecycleOwner(), new Observer<List<ImagenDetalle>>() {
                            @Override
                            public void onChanged(List<ImagenDetalle> imagenDetalles) {
                               fotosorig=imagenDetalles;
                               totalfotos=fotosorig.size();
                                Log.d(TAG,fotosorig.size()+"--");
                                crearFormulario();
                            }
                        });
                        break;
                    case 3:case 4:case 5:case 6:
                        solViewModel.buscarEtapaDet(solicitud.getNumFoto()).observe(getViewLifecycleOwner(), new Observer<InformeEtapaDet>() {
                            @Override
                            public void onChanged(InformeEtapaDet informeEtapaDet) {
                              //  rutafotoo=informeEtapaDet.getRuta_foto();

                               // Bitmap bitmap1= ComprasUtils.decodeSampledBitmapFromResource(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + rutafotoo, 150, 150);

                               // fotoori1.setImageBitmap(bitmap1);

                                // fotomos.setLayoutParams(new LinearLayout.LayoutParams(350,150));
                                //fotoori1.setVisibility(View.VISIBLE);

                            }
                        });
                        break;

                }

            }
        });
        rutasnfotos=new ArrayList<>();
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
        CampoForm   campo=new CampoForm();
        //fotos originales
        int i=1;
        for (ImagenDetalle imagen:fotosorig) {
            campo=new CampoForm();
            campo.label="";
            campo.nombre_campo="100"+i;
            campo.type="imagenView";
            Log.d(TAG,"creando form"+imagen.getRuta());
            campo.value=getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" +imagen.getRuta();
            campo.funcionOnClick= new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    verImagen(imagen.getRuta());
                }
            };
            camposForm.add(campo);
            i++;
        }
        cf=new CreadorFormulario(camposForm,getContext());
        Log.d(TAG,"creando form"+camposForm.size()+".."+totalfotos);
        sv.addView(cf.crearFormulario());
        List<CampoForm> camposForm2 = new ArrayList<>();
        campo=new CampoForm();
        campo.label="NUEVA";
        campo.nombre_campo="label";
        campo.type="label";
        camposForm2.add(campo);
        //boton para agregar imagen
        for(i=0;i<totalfotos;i++) {
            //los botones para foto son 200
            //campos de ruta son 300
            //imagenes 400
            //boton rotar 500
            campo=new CampoForm();
            campo.label="";
            campo.id=600+i;
            campo.type="hidden";
            campo.value=fotosorig.get(i).getId()+"";
            camposForm2.add(campo);
            campo = new CampoForm();
            campo.label = solicitud.getDescMostrar();
            campo.nombre_campo = "foto";
            campo.type = "agregarImagen";
            campo.style = R.style.formlabel2;
            campo.id = 300+i;
            final int finalI1 = i;
            campo.funcionOnClick = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tomarFoto(REQUEST_CODE_TAKE_PHOTO, finalI1);
                }
            };
            campo.tomarFoto = true;
            camposForm2.add(campo);
            //donde voy a ver la foto
            campo=new CampoForm();
            campo.label="";
            campo.id=400+i;
            campo.type="imagenViewr";

            final int finalI = i;
            campo.funcionOnClick=new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rotar(300+ finalI, 400+finalI);
                }
            };

            camposForm2.add(campo);
        }

        cf=new CreadorFormulario(camposForm2,getContext());
        sv2.addView(cf.crearFormulario());

    }
    public void guardar(){
        try {
            lastClickTime = 0;
           if(rutasnfotos.size()!=totalfotos){
               Toast.makeText(getContext(),"Debe ingresar "+totalfotos+" fotos",Toast.LENGTH_LONG).show();
               return;

           }
           nuevasCor=new ArrayList<>();
           int numfoto=0;
            for (int i=0;i<totalfotos;i++) {
                String valor = rutasnfotos.get(i);
                valor = valor.toUpperCase();
                EditText txtnumfoto=root.findViewById(600+i);
                try {
                    numfoto = Integer.parseInt(txtnumfoto.getText().toString());
                }catch (NumberFormatException ex)//paso a
                {
                    ex.printStackTrace();
                    milog.grabarError(TAG+" "+ex.getMessage());
                }
                //creo el informe
               nuevasCor.add(mViewModel.insertarCorreccion2(solicitud.getId(), Constantes.INDICEACTUAL, numfoto, valor, "", ""));
                mViewModel.setIdNuevo(mViewModel.getNvocorreccion().getId());

            }
            solViewModel.actualizarEstSolicitud(solicitudSel,solicitud.getId(),4);
            enviarSolicitud();
            Toast.makeText(getContext(),"Informe guardado correctamente",Toast.LENGTH_SHORT).show();

            salir();

        }catch (Exception ex){
            ex.getStackTrace();
            Log.e(TAG,"Algo salió mal al enviar"+ex.getMessage());
            Toast.makeText(getContext(),"Hubo un error al guardar intente de nuevo",Toast.LENGTH_SHORT).show();

        }
        aceptar.setEnabled(true);
    }

    //cambiar estatus sol
    public void enviarSolicitud() {
        try {

            CorreccionEnvio envio=mViewModel.prepararEnvioVar(nuevasCor);
            SubirCorreccionTask miTareaAsincrona = new SubirCorreccionTask(envio,getActivity());
            miTareaAsincrona.execute();
            for(Correccion cor:nuevasCor) {
                subirFotos(getActivity(), cor.getId(), cor.getRuta_foto1());
            }

        }catch(Exception ex){
            ex.getStackTrace();
            Log.e(TAG,"Algo salió mal al enviar"+ex.getMessage());
            Toast.makeText(getContext(),"Algo salio mal al enviar",Toast.LENGTH_SHORT).show();
        }
        //todo limpio variables de sesion
        mViewModel.setIdNuevo(0);

        mViewModel.setNvocorreccion(null);

    }

    public void rotar(int idcampo,int idimageview){
        EditText txtruta = root.findViewById(idcampo);
        String foto=txtruta.getText().toString();
        if(ComprasUtils.getAvailableMemory(getActivity()).lowMemory)
        {
            Toast.makeText(getActivity(), "No hay memoria suficiente para esta accion", Toast.LENGTH_SHORT).show();

            return;
        }else
        {
            ImageView imagen=root.findViewById(idimageview);
            RevisarFotoActivity.rotarImagen(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" +foto,imagen);

        }
    }
    public void verImagen(String nombrearch){
        //  ImageView imagen=(ImageView)v;
        // imagen.get
        Log.e(TAG,nombrearch);
        Intent iverim=new Intent(getActivity(), RevisarFotoActivity.class);
        iverim.putExtra(RevisarFotoActivity.IMG_PATH1,nombrearch);
        startActivity(iverim);
    }
    String nombre_foto;
    File archivofoto;
    int grupo;
    public void tomarFoto(int REQUEST_CODE, int vgrupo){
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

            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(activity, "No se encontró almacenamiento externo", Toast.LENGTH_SHORT).show();
                return;

            }
            Uri photoURI = FileProvider.getUriForFile(activity,
                    "com.example.comprasmu.fileprovider",
                    archivofoto);
            grupo=vgrupo;
            Log.e(TAG, "****" + grupo);
            intento1.putExtra(MediaStore.EXTRA_OUTPUT, photoURI); //se pasa a la otra activity la referencia al archivo



            startActivityForResult(intento1, REQUEST_CODE);


        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"vars"+requestCode +"--"+ nombre_foto);


        if ((requestCode == REQUEST_CODE_TAKE_PHOTO) && resultCode == RESULT_OK) {
            //   super.onActivityResult(requestCode, resultCode, data);

            Log.d(TAG,"vars"+requestCode +"--"+ grupo);
            if (archivofoto!=null&&archivofoto.exists()) {
               rutasnfotos.add(nombre_foto);
               mostrarFoto(grupo);
               if(rutasnfotos.size()==totalfotos)
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

    public void mostrarFoto(int grupo){
        EditText textorut=root.findViewById(300+grupo);
        ImageView xfotomos=root.findViewById(400+grupo);
        BotonRotar xbtnrotar=root.findViewById(500+400+grupo);

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

        nuevasCor=null;
        sv=sv2=null;

        aceptar=null;
        nombre_foto=null;
        archivofoto=null;
    }




}

