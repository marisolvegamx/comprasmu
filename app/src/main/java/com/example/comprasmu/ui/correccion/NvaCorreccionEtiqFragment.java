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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.modelos.SolicitudCor;
import com.example.comprasmu.data.remote.CorreccionEnvio;
import com.example.comprasmu.data.remote.InformeEtapaEnv;
import com.example.comprasmu.services.SubirFotoService;
import com.example.comprasmu.ui.RevisarFotoActivity;
import com.example.comprasmu.ui.infetapa.NuevoInfEtapaActivity;
import com.example.comprasmu.ui.preparacion.NvaPreparacionViewModel;
import com.example.comprasmu.ui.solcorreccion.ListaSolsViewModel;
import com.example.comprasmu.utils.BotonRotar;
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

public class NvaCorreccionEtiqFragment extends Fragment {


    CreadorFormulario cf;
    List<CampoForm> camposForm;
    LinearLayout sv,sv2,sv3;
    private static final String TAG = "NvaCorreccionEtiqFragment";
    Button aceptar;
    ArrayList<String> spinnerValues;
    private long lastClickTime = 0;
    private final boolean yaestoyProcesando=false;

    int solicitudSel;
    EditText textoint,txtrutaim2,txtrutaim3;
    Spinner spdato1;

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
    private NvaPreparacionViewModel preViewModel;
    SolicitudCor solicitud;
    TextView txtmotivo,txtsoldato1,txtnumcaja;
    int numfoto;
    ArrayAdapter<String> adapterCajas;
    int ultimacaja;
    InformeEtapaDet detEdit; //detalle que se editará solo en num de caja la foto será en la supervision de la correccion
    InformeEtapa infcor;


    public static NvaCorreccionEtiqFragment newInstance() {
        return new NvaCorreccionEtiqFragment();
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        root= inflater.inflate(R.layout.fragment_correccionpre, container, false);
        sv = root.findViewById(R.id.cpcontent_generic);
        sv2 = root.findViewById(R.id.cpcontent_generic2);
        aceptar = root.findViewById(R.id.btncpaceptar);

        txtmotivo=root.findViewById(R.id.txtcpmotivo);
        txtsoldato1=root.findViewById(R.id.txtcpsoldato1);
      //  txtnumcaja=(TextView) root.findViewById(R.id.txtcpcajita);

        spdato1 =root.findViewById(R.id.spcpdato1);
        LinearLayout lcajas=root.findViewById(R.id.llcpcajasetiq);
        lcajas.setVisibility(View.VISIBLE);

     //   fotoori1=root.findViewById(R.id.ivcporiginal);
        mViewModel = new ViewModelProvider(requireActivity()).get(NvaCorreViewModel.class);
        solViewModel = new ViewModelProvider(requireActivity()).get(ListaSolsViewModel.class);
        preViewModel = new ViewModelProvider(requireActivity()).get(NvaPreparacionViewModel.class);

        aceptar.setEnabled(false);
        //buscar la solicitud
        Bundle datosRecuperados = getArguments();

        if(datosRecuperados!=null) {
            solicitudSel = datosRecuperados.getInt(NuevoInfEtapaActivity.INFORMESEL);
            numfoto = datosRecuperados.getInt(NuevoInfEtapaActivity.NUMFOTO);

        }

        solViewModel.getSolicitud(solicitudSel,numfoto).observe(getViewLifecycleOwner(), new Observer<SolicitudCor>() {
            @Override
            public void onChanged(SolicitudCor solicitudCor) {
                solicitud=solicitudCor;
                Log.e(TAG,"estatus "+solicitud.getInformesId()+numfoto+"--"+solicitud.getEtapa());
                //busco el consecutivo de la tienda
                int constienda=0;
                 txtmotivo.setText(solicitud.getMotivo());
                //BUSCO LA FOTO ORIGINAL
                //en donde la busco
                switch (solicitud.getEtapa()){

                        case 3:
                           infcor= preViewModel.getInformexId(solicitudCor.getInformesId());
                          //  Log.d(TAG,"ciud"+infcor.getCiudadNombre());
                            solViewModel.buscarFotoEta(solicitud.getNumFoto(),solicitudCor.getInformesId(),3).observe(getViewLifecycleOwner(), new Observer<InformeEtapaDet>() {
                                @Override
                                public void onChanged(InformeEtapaDet informeEtapaDet) {

                                    detEdit=informeEtapaDet;
                                    if(informeEtapaDet!=null) {
                                        Log.d(TAG,"buscando"+informeEtapaDet.getId());

                                      //  txtnumcaja.setText("hola");
                                      //  txtnumcaja.setText(informeEtapaDet.getNum_caja()+""); //caja en la que esta la muestra
                                        ((NuevoInfEtapaActivity)getActivity()).actualizarBarraCorEta(solicitud,informeEtapaDet.getNum_caja());

                                        solViewModel.buscarImagenCom(Integer.parseInt(informeEtapaDet.getRuta_foto())).observe(getViewLifecycleOwner(), new Observer<ImagenDetalle>() {
                                            @Override
                                            public void onChanged(ImagenDetalle imagenDetalle) {
                                                rutafotoo = imagenDetalle.getRuta();

                                               // Bitmap bitmap1 = ComprasUtils.decodeSampledBitmapFromResource(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + rutafotoo, 80, 80);

                                               // fotoori1.setImageBitmap(bitmap1);
                                                crearFormulario(imagenDetalle);

                                            }
                                        });


                                    }
                                    // fotomos.setLayoutParams(new LinearLayout.LayoutParams(350,150));
                                    //fotoori1.setVisibility(View.VISIBLE);

                                }
                            });
                            break;
                            case 4:case 5:case 6:
                        ((NuevoInfEtapaActivity)getActivity()).actualizarBarraCorEta(solicitud,0);

                        solViewModel.buscarEtapaDet(solicitud.getNumFoto()).observe(getViewLifecycleOwner(), new Observer<InformeEtapaDet>() {
                            @Override
                            public void onChanged(InformeEtapaDet informeEtapaDet) {
                                if(informeEtapaDet!=null) {
                                    rutafotoo = informeEtapaDet.getRuta_foto();

                                    //Bitmap bitmap1 = ComprasUtils.decodeSampledBitmapFromResource(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + rutafotoo, 80, 80);

                                    //fotoori1.setImageBitmap(bitmap1);
                                }
                                // fotomos.setLayoutParams(new LinearLayout.LayoutParams(350,150));
                                //fotoori1.setVisibility(View.VISIBLE);

                            }
                        });
                        break;

                }

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


    public void crearFormulario(ImagenDetalle imagenOrig){
        txtsoldato1.setText("LA MUESTRA QUEDO FINALMENTE EN LA CAJA NUM.");

        camposForm=new ArrayList<>();
        CampoForm   campo=new CampoForm();
        //fotos originales


        campo=new CampoForm();
        campo.label="";
        campo.nombre_campo="1001";
        campo.type="imagenView";

        campo.value=getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" +imagenOrig.getRuta();
        campo.funcionOnClick= new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    verImagen(imagenOrig.getRuta());
                }
            };
            camposForm.add(campo);


        cf=new CreadorFormulario(camposForm,getContext());

        sv.addView(cf.crearFormulario());
        List<CampoForm> camposForm2 = new ArrayList<>();
        campo=new CampoForm();
        campo.label="NUEVA";
        campo.nombre_campo="label";
        campo.type="label";
        camposForm2.add(campo);
        //boton para agregar imagen

            //los botones para foto son 200
            //campos de ruta son 300
            //imagenes 400
            //boton rotar 500
            campo=new CampoForm();
            campo.label="";
            campo.id=6001;
            campo.type="hidden";
            campo.value=imagenOrig.getId()+"";
            camposForm2.add(campo);
            campo = new CampoForm();
            campo.label = solicitud.getDescMostrar();
            campo.nombre_campo = "foto";
            campo.type = "agregarImagen";
            campo.style = R.style.formlabel2;
            campo.id = 3001;


            campo.funcionOnClick = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tomarFoto(REQUEST_CODE_TAKE_PHOTO);
                }
            };
            campo.tomarFoto = true;

            camposForm2.add(campo);
            //nombre de la nueva foto
            campo=new CampoForm();
            campo.label="";
            campo.id=7001;
            campo.type="hidden";
            camposForm2.add(campo);
            //donde voy a ver la foto
            campo=new CampoForm();
            campo.label="";
            campo.id=4001;
            campo.type="imagenViewr";


            campo.funcionOnClick=new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rotar(3001, 4001);
                }
            };

            camposForm2.add(campo);


        cf=new CreadorFormulario(camposForm2,getContext());
        sv2.addView(cf.crearFormulario());
      /*  fotoori1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verImagen(rutafotoo);
            }
        });*/

        spinnerValues = new ArrayList<>();
        //busco el total de cajas
        int totcajas=0;
        if(infcor!=null) {
         //   Log.d(TAG,infcor.getCiudadNombre());
            totcajas = preViewModel.getTotCajasEtiqxCd(infcor.getCiudadNombre());

        }
       // spinnerValues.add(0);
        spinnerValues.add(getString(R.string.seleccione_opcion));
        for(int i=1;i<=totcajas;i++) {
            spinnerValues.add(i+"");
            ultimacaja=i;
        }

        adapterCajas = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerValues);
        spdato1.setAdapter(adapterCajas);
    }
    public void guardar(){
        try {
            lastClickTime = 0;
            String valor = null;
            String valor2 = null;
            String valor3 = null;
            int nucaja=0;
            if (textoint != null) {
                valor = textoint.getText().toString();
                valor = valor.toUpperCase();

            }


            if (spdato1 != null) {
                valor2 =  (String) spdato1.getSelectedItem();
                if(!valor2.equals("0"))
                try {
                    nucaja = Integer.parseInt(valor2);
                }catch (NumberFormatException ex){
                    Log.e(TAG,ex.getMessage());
                    ex.printStackTrace();
                }
            }
            //paso a
            //creo el informe
            mViewModel.setIdNuevo(mViewModel.insertarCorreccionEtiq(solicitud.getId(), Constantes.INDICEACTUAL,solicitud.getNumFoto(),valor, "", "",valor2));

            actualizarSolicitud();
            //actualizar el detalle el numero de caja buscarlo promero
            if(nucaja>0)
                preViewModel.corregirEtiquetado(detEdit,nucaja);
            //reenvio inf con su detalle

            InformeEtapaEnv envio=preViewModel.preparaInformeEtiqCor(solicitud.getInformesId(),detEdit);
            SubirInformeEtaTask miTareaAsincrona = new SubirInformeEtaTask(envio,getActivity());
            miTareaAsincrona.execute();
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
            //grupo=vgrupo;


                startActivityForResult(intento1, REQUEST_CODE);


        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"vars"+requestCode +"--"+ nombre_foto);


        if ((requestCode == REQUEST_CODE_TAKE_PHOTO) && resultCode == RESULT_OK) {
            //   super.onActivityResult(requestCode, resultCode, data);

           // Log.d(TAG,"vars"+requestCode +"--"+ rutasnfotos.size());
            if (archivofoto!=null&&archivofoto.exists()) {

                mostrarFoto(1);

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
        //    EditText textorut=root.findViewById(300+grupo);
         textoint=root.findViewById(3000+grupo);
        ImageView xfotomos=root.findViewById(4000+grupo);
        BotonRotar xbtnrotar=root.findViewById(500+4000+grupo);

        textoint.setText(nombre_foto);
      //  textoint.setVisibility(View.VISIBLE);
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

  /*  public void nvacaja(){
        ultimacaja++;
        spinnerValues.add(ultimacaja+"");
        adaptercaja.notifyDataSetChanged();
    }*/
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel = null;
        solViewModel=null;
        cf=null;
        camposForm=null;
        root=null;
        textoint=null;

        //fotomos=null;
        sv=null;
        btnrotar=null;
        aceptar=null;
        nombre_foto=null;
        archivofoto=null;
    }




}

