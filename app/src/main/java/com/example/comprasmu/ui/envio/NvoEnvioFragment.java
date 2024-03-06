package com.example.comprasmu.ui.envio;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;

import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Layout;
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


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.comprasmu.EtiquetadoxCliente;
import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.SubirInformeEtaTask;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.DetalleCaja;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeEnvioDet;
import com.example.comprasmu.data.modelos.InformeEnvioPaq;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.Reactivo;
import com.example.comprasmu.data.remote.InformeEnvPaqEnv;
import com.example.comprasmu.data.remote.InformeEtapaEnv;
import com.example.comprasmu.services.SubirFotoService;
import com.example.comprasmu.ui.RevisarFotoActivity;
import com.example.comprasmu.ui.etiquetado.NvoEtiqCajaFragment;
import com.example.comprasmu.ui.etiquetado.NvoEtiquetadoFragment;
import com.example.comprasmu.ui.infetapa.NuevoInfEtapaActivity;
import com.example.comprasmu.ui.infetapa.NuevoInfEtapaViewModel;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.ui.preparacion.NvaPreparacionViewModel;
import com.example.comprasmu.utils.CampoForm;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;
import com.example.comprasmu.utils.micamara.MiCamaraActivity;
import com.example.comprasmu.utils.ui.DatePickerFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class NvoEnvioFragment extends Fragment {

    private int preguntaAct;
    LinearLayout svcli,svfecha,svrecibe,svsello,svcoment;
    private static final String TAG = "NvoEnvioFragment";
    private long lastClickTime = 0;
    private boolean yaestoyProcesando=false;
    ImageView fotomos;

    Button aceptar1,aceptar2,aceptar3,aceptar4,guardar;
    private ImageButton btnrotar;
    public static  int REQUEST_CODE_TAKE_PHOTO=1;
    private View root;
    private NvaPreparacionViewModel mViewModel;
    private int  informeSel;

    private String clienteNombre;
    private int clienteId;
    private ListaDetalleViewModel lcViewModel;

    List<ListaCompra> listacomp;
    private  ArrayList<DescripcionGenerica> listaClientes; //otra vez lista clientes
    public final static String ARG_PREGACT="comprasmu.nen_pregact";
    public final static String ARG_ESEDI="comprasmu.nen_esedi";
    public final static String ARG_INFORMESEL = "comprasmu.neninfsel";
    public String ciudadInf;

    private boolean isEdicion;
    Spinner spcliente;
    InformeEnvioPaq informeEdit;
    DetalleCaja ultimarescaja;
    ComprasLog compraslog;
    int totalcajas;
    int etapa=5;
    private Button btntomarf;
    private NuevoInfEtapaViewModel niviewModel;
    ComprasLog milog;
    private EditText txtfechaent,txtnombrerec,txtcoment,txtfotosello;

    public NvoEnvioFragment() {

    }
    public static NvoEnvioFragment newInstance() {
        return new NvoEnvioFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_nvoenvio, container, false);

        try {

            mViewModel = new ViewModelProvider(requireActivity()).get(NvaPreparacionViewModel.class);
            lcViewModel = new ViewModelProvider(this).get(ListaDetalleViewModel.class);
            //reviso si es edicion y ya tengo info en temp


            svcli = root.findViewById(R.id.llnvpregcli);
            svfecha = root.findViewById(R.id.llnvpregfecha);
            svrecibe = root.findViewById(R.id.llnvpregrecibe);
            svsello = root.findViewById(R.id.llnvpresello);

            svcoment = root.findViewById(R.id.llnvprecomen);

            aceptar1 = root.findViewById(R.id.btnnvacecli);

            aceptar2 = root.findViewById(R.id.btnnvacfecha);
            aceptar3 = root.findViewById(R.id.btnnvacrec);
            aceptar4 = root.findViewById(R.id.btnnvacepsello);
            guardar = root.findViewById(R.id.btnnvguardar);

            btnrotar = root.findViewById(R.id.btnnvrotar);

            btntomarf = root.findViewById(R.id.btnnvfoto);

            niviewModel = new ViewModelProvider(requireActivity()).get(NuevoInfEtapaViewModel.class);

            mViewModel = new ViewModelProvider(requireActivity()).get(NvaPreparacionViewModel.class);
            lcViewModel = new ViewModelProvider(this).get(ListaDetalleViewModel.class);
            milog = ComprasLog.getSingleton();

            svcli.setVisibility(View.GONE);
            svfecha.setVisibility(View.GONE);
            svrecibe.setVisibility(View.GONE);
            svsello.setVisibility(View.GONE);

            svcoment.setVisibility(View.GONE);


            txtfotosello = root.findViewById(R.id.txtnvfotosello);
            txtfechaent = root.findViewById(R.id.txtnvfechaentr);
            txtnombrerec=root.findViewById(R.id.txtnvnombrerec);
            txtcoment=root.findViewById(R.id.txtnvcomentarios);
            // pcoincide=root.findViewById(R.id.sinonecoincide);
            // potra=root.findViewById(R.id.sinoneotramu);
            spcliente = root.findViewById(R.id.spnvclientesel);

            if (getArguments() != null) {
                // Log.d(TAG,"aqui");
                this.preguntaAct = getArguments().getInt(ARG_PREGACT);
                this.informeSel = getArguments().getInt(ARG_INFORMESEL);
                // mViewModel.setIdNuevo(this.informeSel);
                //BUSCo si viene de continuar
                InformeEnvioPaq det = mViewModel.getInformeEnvio(informeSel);
                if (det != null)
                    this.informeEdit = det;


                this.isEdicion = getArguments().getBoolean(ARG_ESEDI);
            }


            //deshabilito botones de aceptar
            aceptar1.setEnabled(false);
            aceptar2.setEnabled(false);
            aceptar3.setEnabled(false);
            aceptar4.setEnabled(false);
            guardar.setEnabled(false);

            txtcoment.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            txtnombrerec.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

         //   txtnombrerec.addTextChangedListener(new NvoEnvioFragment().BotonTextWatcher(aceptar4));

            spcliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    aceptar1.setEnabled(true);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });

            if (preguntaAct == 0)
                preguntaAct = 1;
            mViewModel.preguntaAct = preguntaAct;
            if (!isEdicion && preguntaAct < 2 && mViewModel.getIdNuevo() == 0) {
                //es nuevo
                //reviso si ya tengo uno abierto
                InformeEtapa informeEtapa = mViewModel.getInformePend(Constantes.INDICEACTUAL, etapa);

                if (informeEtapa != null) {

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
            milog.grabarError(TAG + " o x aca");
            //busco si tengo varias plantas
            ciudadInf = Constantes.CIUDADTRABAJO;
            //busco los clientes x ciudad
            listacomp = lcViewModel.cargarClientesSimplxet(Constantes.CIUDADTRABAJO, this.etapa);
            Log.d(TAG, "cliente" + ciudadInf + "ss" + mViewModel.getIdNuevo() + "--" + listacomp.size());

            //ya puedo tener varios informes
         //   Integer[] clientesprev = mViewModel.tieneInforme(3);

               convertirLista(listacomp);
            if (listaClientes.size() > 1) {
                    //tengo varios clientes
                    preguntaAct = 1;


                    cargarPlantas(listaClientes, "");

                    mViewModel.variasClientes = true;
                    svcli.setVisibility(View.VISIBLE);
                    aceptar1.setEnabled(true);
           } else if (listaClientes.size() > 0) {
                    preguntaAct = 2;

                    svfecha.setVisibility(View.VISIBLE);
                    mViewModel.variasClientes = false;
                    clienteId = listacomp.get(0).getClientesId();
                    clienteNombre = listacomp.get(0).getClienteNombre();
                    totalcajas = mViewModel.getTotalCajasxCliXcd(clienteId, Constantes.CIUDADTRABAJO);
                    //veo si ya tengo un informe
                    InformeEtapa primero = mViewModel.tieneInforme(3, Constantes.CIUDADTRABAJO, clienteId);

                    InformeEtapa informetemp = new InformeEtapa();
                    informetemp.setClienteNombre(clienteNombre);
                    informetemp.setClientesId(clienteId);
                    informetemp.setCiudadNombre(Constantes.CIUDADTRABAJO);
                    informetemp.setIndice(Constantes.INDICEACTUAL);
                    ((NuevoInfEtapaActivity) getActivity()).actualizarBarraEtiq(informetemp);
            }

            if (isEdicion) { //busco el informe

                //busco el informe y el detalle

             //   infomeEdit = mViewModel.getInformexId(informeSel);
                preguntaAct = 2;
                mViewModel.preguntaAct = 2;
                if (listaClientes != null && listaClientes.size() > 0)
                    cargarPlantas(listaClientes, this.informeEdit.informeEtapa.getClientesId() + "");
                ((NuevoInfEtapaActivity) getActivity()).actualizarBarraEtiq(this.informeEdit.informeEtapa);
                mViewModel.setIdNuevo(informeSel);
                totalcajas = this.informeEdit.informeEtapa.getTotal_cajas();
                clienteId = this.informeEdit.informeEtapa.getClientesId();

            }

            aceptar1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DescripcionGenerica opcionsel = (DescripcionGenerica) spcliente.getSelectedItem();
                    if (opcionsel != null) {
                        //busco par de id, cliente
                        //String[] aux = opcionsel.getDescripcion().split(",");
                        clienteId = opcionsel.getId();
                        clienteNombre = opcionsel.getNombre();
                        //busco el total de muestras
                        totalcajas = mViewModel.getTotalCajasxCliXcd(clienteId, Constantes.CIUDADTRABAJO);
                        InformeEtapa temp = new InformeEtapa();
                        temp.setClienteNombre(clienteNombre);
                        temp.setClientesId(clienteId);
                        temp.setCiudadNombre(Constantes.CIUDADTRABAJO);
                        temp.setIndice(Constantes.INDICEACTUAL);
                        temp.setTotal_muestras(totalcajas);
                        ((NuevoInfEtapaActivity) getActivity()).actualizarBarraEtiq(temp);
                        avanzar();
                    }
                }
            });
      aceptar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                guardarInf();
            }
        });
            aceptar3.setOnClickListener(new View.OnClickListener() { //foto
                @Override
                public void onClick(View view) {
                 guardarDet();

                }
            });
            aceptar4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    guardarDet();

                }
            });
            guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    guardarDet();

                }
            });



            btnrotar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rotar(R.id.txtneruta);
                }
            });

            btntomarf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tomarFoto(1);
                }
            });

            mViewModel.preguntaAct = preguntaAct;

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return root;
    }




        public void avanzar() {
            Log.d(TAG, "--" + preguntaAct);


            switch (preguntaAct) {
                case 1: //sel cliente


                    // txtcajaact.setVisibility(View.VISIBLE);
                    svcli.setVisibility(View.GONE);
                    // txttotmues.setVisibility(View.VISIBLE);
                    svfecha.setVisibility(View.VISIBLE);
                    preguntaAct = preguntaAct + 1;
                    //veo si ya tengo un informe
                    InformeEtapa primero=mViewModel.tieneInforme(this.etapa,Constantes.CIUDADTRABAJO,clienteId);
                    if(primero!=null){

                        //busco la ultima muestra
                        InformeEtapaDet ultima = mViewModel.getUltimaMuestraEtiq(primero.getId());

                    }

                    break;
                case 2: //fecha
                    svfecha.setVisibility(View.GONE);

                    svrecibe.setVisibility(View.VISIBLE);
                    preguntaAct = preguntaAct + 1;
                    break;
                case 3: //nombre recibe
                    svrecibe.setVisibility(View.GONE);
                    svsello.setVisibility(View.VISIBLE);
                    preguntaAct = preguntaAct + 1;

                    break;
                case 4: //foto
                    svsello.setVisibility(View.GONE);
                    svcoment.setVisibility(View.VISIBLE);

                    preguntaAct = preguntaAct + 1;

                    break;
                case 5: //comentarios
                    svcoment.setVisibility(View.GONE);
                   finalizarInf();
                    isEdicion = false;

                    break;


            }

            mViewModel.preguntaAct = preguntaAct;
        }





        public void editarInforme() {
            svcli.setVisibility(View.GONE);
            svrecibe.setVisibility(View.GONE);
            svsello.setVisibility(View.GONE);
            svcoment.setVisibility(View.GONE);
            svfecha.setVisibility(View.VISIBLE);
            //  txtcajaact.setVisibility(View.VISIBLE);
            preguntaAct = 2;
            ImagenDetalle foto;
            mViewModel.preguntaAct = preguntaAct;
            if (informeEdit != null) {
                //busco en la bd la imagen
                foto = mViewModel.getFoto(informeEdit.infEnvioDet.getFotoSello());
                txtfotosello.setText(foto.getRuta());
                Bitmap bitmap1 = ComprasUtils.decodeSampledBitmapFromResource(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + foto.getRuta(), 80, 80);
                fotomos.setImageBitmap(bitmap1);
                fotomos.setVisibility(View.VISIBLE);
                btnrotar.setVisibility(View.VISIBLE);
                aceptar3.setEnabled(true);
                txtnombrerec.setText(informeEdit.infEnvioDet.getNombreRecibe());
               // contmuestra = detalleEdit.getNum_muestra();

            }

        }



        public void atras(){

            isEdicion=true; //siempre es edicion
            switch (preguntaAct){

                case 2:
                    svcli.setVisibility(View.VISIBLE);
                    svfecha.setVisibility(View.GONE);

                    preguntaAct=preguntaAct-1;
                    mViewModel.preguntaAct=preguntaAct;
                    break;
                case 3:
                    svfecha.setVisibility(View.VISIBLE);
                    svrecibe.setVisibility(View.GONE);

                    preguntaAct=preguntaAct-1;
                    mViewModel.preguntaAct=preguntaAct;
                    break;
                //  sv6.setVisibility(View.VISIBLE);
                //  sv3.setVisibility(View.GONE);
                //   txtcajaact.setVisibility(View.GONE);

                case 4:
                    svrecibe.setVisibility(View.VISIBLE);
                    svsello.setVisibility(View.GONE);

                    preguntaAct=preguntaAct-1;
                    mViewModel.preguntaAct=preguntaAct;
                    break;
                case 5:
                    svsello.setVisibility(View.VISIBLE);
                    svcoment.setVisibility(View.GONE);

                    preguntaAct=preguntaAct-1;
                    mViewModel.preguntaAct=preguntaAct;
                    break;

            }
            Log.d(TAG,"**"+preguntaAct);

        }


        public void guardarInf(){
            try {
                lastClickTime = 0;
                //  totcajas = 0;

                //totcajas =Integer.parseInt(txtnumcajas.getText().toString());

                if (preguntaAct == 2 && !isEdicion&&mViewModel.getNvoinforme()==null) {
                    Log.d(TAG, "creando nvo inf");
                    //creo el informe
                    mViewModel.setIdNuevo(mViewModel.insertarEnvio(Constantes.INDICEACTUAL, clienteNombre,clienteId,totalcajas,0,ciudadInf));
                    //  ((NuevoInfEtapaActivity)getActivity()).actualizarBarraEtiq(mViewModel.getNvoinforme());

                }
                //ya existe
            }catch (Exception ex){
                ex.printStackTrace();
                Log.e(TAG,"Algo salió mal al guardarInf"+ex.getMessage());
                Toast.makeText(getContext(),"Hubo un error al guardar intente de nuevo",Toast.LENGTH_SHORT).show();

            }

            aceptar1.setEnabled(true);
           guardarDet();
        }
        public void guardarDet(){
            try{
                String rutafoto = null;
                //busco si ya tiene detalle
                if(informeEdit.infEnvioDet!=null) {
                   switch(preguntaAct){
                       case 2:
                           Date fechaenv;
                           SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
                           String fecha=txtfechaent.getText().toString();
                           if (!fecha.equals("")) {

                               try {
                                   fechaenv = sdf.parse(fecha);
                               } catch (ParseException e) {

                                   Toast.makeText(getContext(), R.string.error_fecha_formato, Toast.LENGTH_SHORT).show();

                                   return;
                               }


                               informeEdit.infEnvioDet.setFechaEnvio(fechaenv);
                           }
                           break;
                       case 3:
                           String recibe=txtnombrerec.getText().toString();
                           if(!recibe.equals("")){
                               informeEdit.infEnvioDet.setNombreRecibe(recibe);
                           }
                           else {
                               Toast.makeText(getContext(), "Capture el nombre de quien recibe", Toast.LENGTH_SHORT).show();

                               return;
                           }
                           break;
                       case 4:
                           //la foto se guarda primero en imagenes
                           String nomfoto=txtfotosello.getText().toString();
                           if(!nomfoto.equals("")){
                           mViewModel.actualizarImagenEnvio(informeEdit.infEnvioDet,nomfoto);
                             }
                           else {
                               Toast.makeText(getContext(), "Capture la foto de sello de entregado", Toast.LENGTH_SHORT).show();


                           }
                           return;

                       case 5:
                           String coment=txtcoment.getText().toString();

                           informeEdit.infEnvioDet.setNombreRecibe(coment);

                           break;


                   }

                    //es actualizacion
                    mViewModel.actualizarEnvioDet(informeEdit.infEnvioDet);
                }else{
                    //es un nuevo registro
                    if(preguntaAct==2&&mViewModel.getIdNuevo()>0){
                        Date fechaenv;
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
                        String fecha=txtfechaent.getText().toString();
                        if (!fecha.equals("")) {

                            try {
                                fechaenv = sdf.parse(fecha);
                            } catch (ParseException e) {

                                Toast.makeText(getContext(), R.string.error_fecha_formato, Toast.LENGTH_SHORT).show();

                                return;
                            }
                            InformeEnvioDet nvoDet=new InformeEnvioDet();
                            nvoDet.setInformeEtapaId(mViewModel.getIdNuevo());


                            nvoDet.setFechaEnvio(fechaenv);
                            mViewModel.insertarEnvioDet(nvoDet);
                        }
                    }


               }

                //limpio campos
                avanzar();
            }catch (Exception ex){
                ex.printStackTrace();
                milog.grabarError(TAG,"guardarDet",ex.getMessage());
                Toast.makeText(getContext(),"Hubo un error al guardar intente de nuevo",Toast.LENGTH_SHORT).show();

            }

        }

        public void rotar(int idcampo){
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
            REQUEST_CODE_TAKE_PHOTO=REQUEST_CODE;
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
            if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {
                //   super.onActivityResult(requestCode, resultCode, data);

                if (archivofoto!=null&&archivofoto.exists()) {
                    if(requestCode == REQUEST_CODE_TAKE_PHOTO) {

                        mostrarFoto(txtfotosello,fotomos,btnrotar);

                    }
                    if(requestCode==1)
                        aceptar4.setEnabled(true);


                }
                else{
                    Log.e(TAG,"Algo salió mal???");
                }


            }else

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

        //ya se puede varios informes de etiquetado para la reactivacion

        private  void convertirLista(List<ListaCompra>lista){
            listaClientes =new ArrayList<DescripcionGenerica>();
            for (ListaCompra listaCompra: lista ) {
                Log.d(TAG,listaCompra.getPlantaNombre());

                listaClientes.add(new DescripcionGenerica(listaCompra.getClientesId(), listaCompra.getClienteNombre()));

            }

        }


        class BotonTextWatcher implements TextWatcher {

            boolean mEditing;
            Button aceptar;
            public BotonTextWatcher() {
                mEditing = false;
            }
            public BotonTextWatcher(Button botonac) {
                mEditing = false;
                aceptar=botonac;
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
        private void cargarPlantas(List<DescripcionGenerica> selectdes,String value){
            ArrayAdapter catAdapter = new ArrayAdapter<DescripcionGenerica>(getContext(), android.R.layout.simple_spinner_dropdown_item, selectdes) {


                // And the "magic" goes here
                // This is for the "passive" state of the spinner
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
                    TextView label = (TextView) super.getView(position, convertView, parent);
                    label.setTextColor(Color.BLACK);
                    // Then you can get the current item using the values array (Users array) and the current position
                    // You can NOW reference each method you has created in your bean object (User class)
                    DescripcionGenerica item = getItem(position);
                    label.setText(item.getNombre());
                    //TODO elegir idioma

                    // And finally return your dynamic (or custom) view for each spinner item
                    return label;
                }

                // And here is when the "chooser" is popped up
                // Normally is the same view, but you can customize it if you want
                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    TextView label = (TextView) super.getDropDownView(position, convertView, parent);
                    label.setTextColor(Color.BLACK);
                    DescripcionGenerica item = getItem(position);
                    label.setText(item.getNombre());

                    return label;
                }
            };


            spcliente.setAdapter(catAdapter);
            spcliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // Get the value selected by the user
                    // e.g. to store it as a field or immediately call a method
                    DescripcionGenerica opcion = (DescripcionGenerica) parent.getSelectedItem();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            if(value!=null&&value.length()>0){
                //busco el valor en la lista
                for(DescripcionGenerica cat:selectdes){
                    Log.d("CreadorForm","val"+value+" cat"+cat.getId());
                    if(value.equals(cat.getId()+"")){
                        // Log.d("CreadorForm","val"+infocampo.value+" cat"+cat.getId());
                        spcliente.setSelection(catAdapter.getPosition(cat),true);
                        break;
                    }
                    if(value.equals(cat.getNombre())){
                        Log.d("CreadorForm",catAdapter.getPosition(cat)+"");

                        spcliente.setSelection(catAdapter.getPosition(cat),true);
                        break;
                    }
                }
            }
        }
    public void finalizarInf() {
        try {
            mViewModel.finalizarInf();
            InformeEnvPaqEnv envio=mViewModel.prepararInformeEnvPaq(mViewModel.getIdNuevo());
          //  SubirInformeEtaTask miTareaAsincrona = new SubirInformeEtaTask(envio,getActivity());
          //  miTareaAsincrona.execute();
            subirFotos(getActivity(),envio);
        }catch(Exception ex){
            ex.printStackTrace();
            Log.e(TAG,"Algo salió mal al finalizar"+ex.getMessage());
            Toast.makeText(getContext(),"Algo salio mal al enviar",Toast.LENGTH_SHORT).show();
        }
        // limpio variables de sesion

        mViewModel.setIdNuevo(0);
        mViewModel.setIddetalle(0);
        mViewModel.setNvoinforme(null);

    }
    public static void subirFotos(Activity activity, InformeEnvPaqEnv informe){
        //las imagenes
        for(ImagenDetalle imagen:informe.getImagenDetalles()){
            //
            //subo cada una
            Intent msgIntent = new Intent(activity, SubirFotoService.class);
            msgIntent.putExtra(SubirFotoService.EXTRA_IMAGE_ID, imagen.getId());
            msgIntent.putExtra(SubirFotoService.EXTRA_IMG_PATH,imagen.getRuta());
            msgIntent.putExtra(SubirFotoService.EXTRA_INDICE,informe.getIndice());
            // Constantes.INDICEACTUAL
            Log.d(TAG,"subiendo fotos"+activity.getLocalClassName());

            msgIntent.setAction(SubirFotoService.ACTION_UPLOAD_ETA);

            //cambio su estatus a subiendo
            imagen.setEstatusSync(1);
            activity.startService(msgIntent);

        }

    }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            mViewModel = null;

            root=null;
            txtnombrerec=null;

            fotomos=null;
          //  sv1= sv6 =sv3=sv4=null;
            btnrotar=null;
            aceptar1=null;
         //   nombre_foto=null;
         //   archivofoto=null;
        }

        private void showDatePickerDialog() {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }



}

