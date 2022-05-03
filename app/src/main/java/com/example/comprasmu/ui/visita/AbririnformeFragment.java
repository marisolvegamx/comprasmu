package com.example.comprasmu.ui.visita;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;


import com.example.comprasmu.R;

import com.example.comprasmu.data.dao.ProductoExhibidoDao;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.Geocerca;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;

import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.Tienda;
import com.example.comprasmu.data.modelos.Visita;

import com.example.comprasmu.data.repositories.GeocercaRepositoryImpl;
import com.example.comprasmu.ui.RevisarFotoActivity;
import com.example.comprasmu.ui.informe.FotoExhibicionAdapter;
import com.example.comprasmu.ui.informe.NuevaFotoExhibViewModel;
import com.example.comprasmu.ui.informe.NuevoinformeFragment;
import com.example.comprasmu.ui.informe.NuevoinformeViewModel;

import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.utils.CampoForm;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;

import com.example.comprasmu.utils.Preguntasino;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.maps.android.PolyUtil;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.ACTIVITY_SERVICE;

public class AbririnformeFragment extends Fragment implements Validator.ValidationListener, FotoExhibicionAdapter.AdapterCallback {

    private static final int REQUEST_CHECK_SETTINGS =0 ;
    public static final String ARG_FOTONUEVA ="comprasmu.nuevafoto" ;
    protected Validator validator;
    protected boolean guardado;

    private NuevoinformeViewModel mViewModel;
    private NuevaFotoExhibViewModel feviewModel;

    CreadorFormulario cf2;
    List<CampoForm> camposForm;
    List<CampoForm> camposTienda;
    private FusedLocationProviderClient fusedLocationClient; //cliente servicio de ubicación
    LocationRequest locationRequest;
    private String country;
    Button guardar;
    ImageButton rotar;
    View root;
    boolean nuevaTienda;
    Tienda tienda;
    int nuevoId;
    private boolean yaTengoFoto;


    EditText txtubicacion;
    public static String EXTRAPREINFORME_ID="comprasmu.preinformeid";
    private CreadorFormulario cf1;
    private static final int SELECT_FILE = 1;
    public static  int REQUEST_CODE_TAKE_PHOTO=1;
    public static  int REQUEST_CODE_PROD1=2;
    public static  int REQUEST_CODE_PROD2=3;
    public static  int REQUEST_CODE_PROD3=4;
    private static final String TAG="AbrirInformeFragment";

    private CreadorFormulario cf3;

    private int totClientes=0;
    private RecyclerView recycler;
    private Visita visitaEdi;
    private List<InformeCompra> fotosExhibido;//para ir guardando las fotos de producto exhibido
    private ImagenDetalle efotoFachada;
    private FotoExhibicionAdapter mListAdapter;
    ImageView fotofac;
    ImageView fotoex1;
    ImageView fotoex2;
    ImageView fotoex3;
    private TextView mensajedir;
    LocationManager mlocManager;
    Localizacion Local;
    File rutaArchivo;
    EditText txtcomplemento; // para complemento direccion
    private Spinner spinn;
    private Spinner spinn2;
    private Spinner spinn3;
    private ListaDetalleViewModel lViewModel;
    private EditText txtfotofachada;
    private EditText txtfotoex1;
    private EditText txtfotoex2;
    private EditText txtfotoex3;
    ImageButton btnrotar1;
    ImageButton btnrotar2;
    ImageButton btnrotar3;
    Preguntasino snmascli1;
    Preguntasino snmascli2;
    public  List<DescripcionGenerica> clientesAsignados;
    public  List<DescripcionGenerica> clientesAsignados2;
    public  List<DescripcionGenerica> clientesAsignados3;
    List<ProductoExhibidoDao.ProductoExhibidoFoto> fotosExh;
    private boolean isEdicion;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel =
                new ViewModelProvider(this).get(NuevoinformeViewModel.class);
        feviewModel =
                new ViewModelProvider(this).get(NuevaFotoExhibViewModel.class);
        lViewModel=new ViewModelProvider(this).get(ListaDetalleViewModel.class);

        /*inicio databinding
        mBinding = DataBindingUtil.setContentView(getActivity(), R.layout.fragment_abririnforme);
        mBinding.setInfviewModel(mViewModel);
        mBinding.setLifecycleOwner(this);*/

        root = inflater.inflate(R.layout.fragment_abririnforme, container, false);

        TextView indice = root.findViewById(R.id.txtaiindice);
        indice.setText(ComprasUtils.indiceLetra(Constantes.INDICEACTUAL));





        //camposFotosProd();
      //  LinearLayout sv2 = root.findViewById(R.id.content_main2);
      //  sv2.addView(cf3.crearFormulario());
        //   createLocationRequest();
      //  validator = new Validator(this);
      //  validator.setValidationListener(this);
        guardar=(Button)root.findViewById(R.id.aibtnguardar);
        rotar=(ImageButton)root.findViewById(R.id.btnairotar1);
        fotofac=(ImageView)root.findViewById(R.id.ivaifachada);
        fotoex1=(ImageView)root.findViewById(R.id.ivaifotoex1);
        fotoex2=(ImageView)root.findViewById(R.id.ivaifotoex2);
        fotoex3=(ImageView)root.findViewById(R.id.ivaifotoex3);
        rotar.setVisibility(View.GONE);
        txtfotofachada = root.findViewById(R.id.txtaifotofachada);
        txtfotoex1= root.findViewById(R.id.txtaifotoex1);
        txtfotoex2= root.findViewById(R.id.txtaifotoex2);
        txtfotoex3= root.findViewById(R.id.txtaifotoex3);
        rotar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotar(view, txtfotofachada,fotofac);
            }
        });
       // continuar=(Button)root.findViewById(R.id.aibtnguardarcont);
       ImageButton fotoexhibido=(ImageButton)root.findViewById(R.id.btnaifotoexhibido);
        guardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            //      validator.validate();
                if(mViewModel.mIsNew)
                    sologuardar();
                else
                    actualizar();
            }
        });
      /*  continuar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                guardarContinuar();
            }
        });*/
        fotoexhibido.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tomarFoto(txtfotoex1,fotoex1,REQUEST_CODE_PROD1);
            }
        });
      // Button ubicar=(Button)root.findViewById(R.id.btnaiubicar);
        ImageButton fotofachada=(ImageButton)root.findViewById(R.id.btnaifotofachada);
        probarUbicacion();
        fotofachada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                probarUbicacion();
                if (txtubicacion.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Falta activar la ubicación", Toast.LENGTH_SHORT).show();

                    return;
                }

                tomarFoto(txtfotofachada,fotofac,REQUEST_CODE_TAKE_PHOTO);

            }
        });
        ImageButton fotoexhibido2=(ImageButton)root.findViewById(R.id.btnaifotoexhibido2);

        fotoexhibido2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tomarFoto(txtfotoex2,fotoex2,REQUEST_CODE_PROD2);
            }
        });
        ImageButton fotoexhibido3=(ImageButton)root.findViewById(R.id.btnaifotoexhibido3);

        fotoexhibido3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tomarFoto(txtfotoex3,fotoex3,REQUEST_CODE_PROD3);
            }
        });

       /* ubicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                probarUbicacion();
            }
        });*/
        /*para obtener la onre*/
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        rutaArchivo=getActivity().getExternalFilesDir(null);
        setupSnackbar();
     //   initUi();
         txtubicacion = root.findViewById(R.id.txtaiubicacion);
        // This callback will only be called when MyFragment is at least Started.
     /*  OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default *//*) {
         /*   @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                onBackPressed();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);*/
        txtcomplemento = root.findViewById(R.id.txtaicomplementodir);
        txtcomplemento.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
         btnrotar1=root.findViewById(R.id.btnairotarfe1);
        btnrotar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotar(view,txtfotoex1,fotoex1);
            }
        });
         btnrotar2=root.findViewById(R.id.btnairotarfe2);
        btnrotar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotar(view,txtfotoex2,fotoex2);
            }
        });
         btnrotar3=root.findViewById(R.id.btnairotarfe3);
        btnrotar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotar(view,txtfotoex3,fotoex3);
            }
        });
        //llenar lista de clientes

         //input3.addTextChangedListener(new MayusTextWatcher());
        return root;
    }
    /*public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Handle the back button
        if(keyCode == KeyEvent.KEYCODE_BACK && getActivity().isTaskRoot()) {
            //Ask the user if they want to quit
            new AlertDialog.Builder(getActivity())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.importante)
                    .setMessage(R.string.cerrar_app)
                    .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Stop the activity
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();

            return true;
        }
        else {
            return getActivity().onKeyDown(keyCode, event);
        }
    }*/
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        if(Constantes.CIUDADTRABAJO==null||Constantes.CIUDADTRABAJO.equals("")){
            //falta definir
            Toast.makeText(getActivity(), "Falta definir ciudad de trabajo", Toast.LENGTH_SHORT).show();

            NavHostFragment.findNavController(this).navigate(R.id.action_nuevotociudad);

        }
        Bundle datosRecuperados = getArguments();
        if (datosRecuperados != null&&getArguments().getInt(EXTRAPREINFORME_ID)>0) {
            // No hay datos, manejar excepción

            Log.d(TAG,"******* es edicion");
            getActivity().setTitle(R.string.editar_informe);

        }

        // Y ahora puedes recuperar usando get en lugar de put
        nuevaTienda= datosRecuperados.getBoolean("nuevatienda");
        Log.d(TAG,"datosrec "+nuevaTienda);
        if(!nuevaTienda)// es una tienda existente
        {

            tienda=new Tienda();
            tienda.setUne_id(datosRecuperados.getInt("idtienda"));
            tienda.setUne_descripcion(datosRecuperados.getString("nombretienda"));
            tienda.setUne_tipotienda(datosRecuperados.getInt("tipotienda"));
            tienda.setUne_direccion(datosRecuperados.getString("direccion"));
        }

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            //  requestPermissionLauncher.launch(
            //        Manifest.permission.REQUESTED_PERMISSION);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);

        } else {
            // rastreoGPS();
          //  locationStart();
        }
        loadData();

     //   setupListAdapter();

            //   mBinding.paradebug1.setText(myProducts.size()+"");


         //   if (myProducts != null&&myProducts.size()>0) {


            //    mListAdapter.setProductoExhibidoList(myProducts,getActivity().getExternalFilesDir(null)+"/");
             //   mListAdapter.notifyDataSetChanged();
          //  } else {
                //  mBinding.setIsLoading(true);
         //   }
            // espresso does not know how to wait for data binding's loop so we execute changes
            // sync.
            //  mBinding.executePendingBindings();


    }

    private void alertaAbierto(){
        //pregunto si habrá más clientes
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
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
        dialogo1.show();

    }
    /***para la edicion***/
    private void loadData() {
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        spinn=root.findViewById(R.id.spaiclientes1);
        spinn2=root.findViewById(R.id.spaiclientes2);
        spinn3=root.findViewById(R.id.spaiclientes3);
        //cargo la lista de clientes
        cargarClientes();


        if (getArguments() != null) {

            //es edicion
            int categoryId = getArguments().getInt(EXTRAPREINFORME_ID);
            if (categoryId > 0) {
                //es edicion
                isEdicion=true;
                nuevoId = mViewModel.start(categoryId, getActivity());
                fotosExh = feviewModel.cargarfotosSimpl(nuevoId);
                Log.d(TAG, "----" + fotosExh.size());
                //lleno los campos
                mViewModel.visitaEdicion.observe(getViewLifecycleOwner(), new Observer<Visita>() {
                    @Override
                    public void onChanged(Visita visita) {
                        if(visita.getTiendaId()>0){
                            //no es tienda nueva
                            nuevaTienda=false;
                            tienda=new Tienda();
                            tienda.setUne_id(visita.getTiendaId());
                            tienda.setUne_descripcion(visita.getTiendaNombre());
                            try {
                                tienda.setUne_tipotienda(Integer.parseInt(visita.getTipoTienda()));
                            }catch(NumberFormatException ex){
                                Log.d(TAG,"Error al convertir");
                            }
                            tienda.setUne_direccion(visita.getDireccion());
                        }
                        crearFormulario(visita);
                        ponerDatos(visita);
                        LinearLayout sv = root.findViewById(R.id.content_main);
                        sv.addView(cf1.crearFormulario());
                        //   sv.addView(cf2.crearFormulario());
                        visitaEdi = visita;


                    }
                });
                return;
            } else {
                //es nuevo
                //reviso si no hay visitas abiertas
                MutableLiveData x = mViewModel.informesAbiertos(getViewLifecycleOwner());
                x.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if (aBoolean)
                            alertaAbierto();
                    }
                });
                nuevoId = mViewModel.start(0, getActivity());
                crearFormulario(new Visita());

                LinearLayout sv = root.findViewById(R.id.content_main);
                sv.addView(cf1.crearFormulario());
                //    sv.addView(cf2.crearFormulario());
                getActivity().setTitle(R.string.nuevo_informe);
                // toolbar.setTitle(R.string.nuevo_informe);
            }
        }else{
            MutableLiveData x = mViewModel.informesAbiertos(getViewLifecycleOwner());
            x.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if (aBoolean)
                        alertaAbierto();
                }
            });
            nuevoId = mViewModel.start(0, getActivity());
            crearFormulario(new Visita());

            LinearLayout sv = root.findViewById(R.id.content_main);
            sv.addView(cf1.crearFormulario());
            //    sv.addView(cf2.crearFormulario());
            getActivity().setTitle(R.string.nuevo_informe);
            // toolbar.setTitle(R.string.nuevo_informe);

        }


    }

    public void ponerDatos(Visita visita){
      /*  if(visita.getTiendaId()>0) { //es edicion
        }else
            nuevaTienda=true;*/

        if(isEdicion) {
            mViewModel.getFotoLD(visita.getFotoFachada()).observe(this, new Observer<ImagenDetalle>() {
                @Override
                public void onChanged(ImagenDetalle s) {
                    // if(s!=null)
                    txtfotofachada.setText(s.getRuta());

                    Bitmap bitmap1 = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null) + "/" + s.getRuta());
                    fotofac.setVisibility(View.VISIBLE);
                    rotar.setVisibility(View.VISIBLE);
                    fotofac.setImageBitmap(bitmap1);
                    efotoFachada = s;
                }
            });

            //las fotos exhib
            // for(ProductoExhibidoDao.ProductoExhibidoFoto fotoe:fotosExh) {
            if (fotosExh != null && fotosExh.size() > 0) {
                cargarFotos(fotosExh.get(0).ruta, txtfotoex1, btnrotar1, fotoex1);
                //  Log.d(TAG,);
                // Log.d(TAG,"a ver"+fotosExh.get(0).clienteId+"-"+Constantes.clientesAsignados.indexOf(fotosExh.get(0).clienteId));
                int pos = buscarEnClientes(fotosExh.get(0).clienteId, clientesAsignados);
                spinn.setSelection(pos, true);
                //quito la opción en los otros

                clientesAsignados2.remove(pos);


                clientesAsignados3.remove(pos);
                CreadorFormulario.cargarSpinnerDescr(getContext(), spinn2, clientesAsignados2);

                CreadorFormulario.cargarSpinnerDescr(getContext(), spinn3, clientesAsignados3);


            }
            if (fotosExh != null && fotosExh.size() > 1) {
//            mostrarOcultarlayout("true",ll);
                cargarFotos(fotosExh.get(1).ruta, txtfotoex2, btnrotar2, fotoex2);
                int pos = buscarEnClientes(fotosExh.get(1).clienteId, clientesAsignados2);
                if (pos > -1) {
                    spinn2.setSelection(pos, true);


                    clientesAsignados3.remove(pos);

                    CreadorFormulario.cargarSpinnerDescr(getContext(), spinn3, clientesAsignados3);
                }
            }
            if (fotosExh != null && fotosExh.size() > 2) {
                cargarFotos(fotosExh.get(2).ruta, txtfotoex3, btnrotar3, fotoex3);
                int pos = buscarEnClientes(fotosExh.get(2).clienteId, clientesAsignados3);
                if (pos > -1)
                    spinn3.setSelection(pos, true);

            }
            //}

            //el complemento
            txtcomplemento.setText(visita.getComplementodireccion());
            EditText ubicacion = root.findViewById(R.id.txtaiubicacion);
            ubicacion.setText(visita.getGeolocalizacion());
        }
    }

    public int buscarEnClientes(int seleccion, List<DescripcionGenerica> listaclientes){
        for(int i=0;i<listaclientes.size();i++){
            Log.d(TAG,"clientesas "+listaclientes.get(i).getId()+"--"+seleccion);
           if(listaclientes.get(i).getId()==seleccion)
               return i;
        }
        return -1;
    }
    public void cargarFotos(String s, EditText txtruta, ImageButton boton, ImageView iv){
        txtruta.setText(s);
      //  Log.d(TAG,"ruta "+s);
        Bitmap bitmap1 = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null) + "/" + s);
        iv.setVisibility(View.VISIBLE);
        boton.setVisibility(View.VISIBLE);
        iv.setImageBitmap(bitmap1);
    }
   /* private void setupListAdapter() {
        RecyclerView listaproductosex=root.findViewById(R.id.rvaiproductoex);
        mListAdapter = new FotoExhibicionAdapter(this);
        //mBinding.detalleList.setAdapter(mListAdapter);
        listaproductosex.setLayoutManager(new LinearLayoutManager(getActivity()));
        listaproductosex.setHasFixedSize(true);
        listaproductosex.setAdapter(mListAdapter);

    }*/
    private void locationStart() {
        Log.d("wwwwwwwwwww",mViewModel.mIsNew+"--");
        if(mViewModel.mIsNew) {
            mlocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
             Local = new Localizacion();


            final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!gpsEnabled) {
                Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(settingsIntent);
            }
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
                return;
            } else if (mlocManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)) {
                mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 20000, 10, (LocationListener) Local);
            }
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
                return;
            } else {
                if (mlocManager.getAllProviders().contains(LocationManager.GPS_PROVIDER)) {
                    if (Local == null) { //Validación que evita NullPointerException
                        //Requiere actualización
                        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000, 10, (LocationListener) Local, Looper.getMainLooper());
                    } else
                        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000, 10, (LocationListener) Local);
                    //  input11.setText("Localización aqui");

                } else
                    Toast.makeText(getActivity(), "No hay gps?", Toast.LENGTH_SHORT).show();

            }

        }

    }

    public void cargarClientes() {
       // Log.d(TAG, "regresó de la consulta " + Constantes.CIUDADTRABAJO);
        //   if (Constantes.clientesAsignados == null||Constantes.clientesAsignados.size()<1)
        List<ListaCompra> data=lViewModel.cargarClientesSimpl(Constantes.CIUDADTRABAJO);
        if(data.size()==0){
            Toast.makeText(getActivity(), "Falta actualizar la app", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).navigate(R.id.action_nuevotolista);

            return;
        }
            Log.d(TAG, "regresó de la consulta " + data.size());
           clientesAsignados= ComprasUtils.convertirListaaClientes(data);
           clientesAsignados2= ComprasUtils.convertirListaaClientes(data);
           clientesAsignados3= ComprasUtils.convertirListaaClientes(data);
            CreadorFormulario.cargarSpinnerDescr(getContext(),spinn,clientesAsignados);
            CreadorFormulario.cargarSpinnerDescr(getContext(),spinn2,clientesAsignados2);
            CreadorFormulario.cargarSpinnerDescr(getContext(),spinn3,clientesAsignados3);
           /* spinn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // Get the value selected by the user
                    // e.g. to store it as a field or immediately call a method
                    DescripcionGenerica opcion = (DescripcionGenerica) parent.getSelectedItem();
                    int pos=buscarEnClientes(opcion.id,clientesAsignados2);
                    if(pos>-1)
                        {
                            clientesAsignados2.remove(pos);

                        }
                    pos=buscarEnClientes(opcion.id,clientesAsignados3);
                    if(pos>-1)
                            {
                                clientesAsignados3.remove(pos);

                            }
                      //  Log.d(TAG, "CLIENTES ASIG QUITÉ UNO " + clientesAsignados2.size());
                        CreadorFormulario.cargarSpinnerDescr(getContext(), spinn2, clientesAsignados2);

                        CreadorFormulario.cargarSpinnerDescr(getContext(), spinn3, clientesAsignados3);

                |}



                  /*   @Override
                     public void onNothingSelected(AdapterView<?> parent) {
                     }
                 });*/
           /* spinn2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the value selected by the user
                // e.g. to store it as a field or immediately call a method
                DescripcionGenerica opcion = (DescripcionGenerica) parent.getSelectedItem();
                int pos=buscarEnClientes(opcion.id,clientesAsignados);
                if(pos>-1) {
                    {
                        clientesAsignados.remove(pos);

                    }
                    int pos=buscarEnClientes(opcion.id,clientesAsignados3);
                    if(pos>-1) {
                        {
                            clientesAsignados.remove(pos);
                            clientesAsignados3.remove(pos);
                        }
                    Log.d(TAG, "CLIENTES ASIG QUITÉ UNO " + clientesAsignados2.size());
                    CreadorFormulario.cargarSpinnerDescr(getContext(), spinn2, clientesAsignados2);

                    CreadorFormulario.cargarSpinnerDescr(getContext(), spinn3, clientesAsignados3);
                }
                |}



            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the value selected by the user
                // e.g. to store it as a field or immediately call a method
                DescripcionGenerica opcion = (DescripcionGenerica) parent.getSelectedItem();
                int pos=buscarEnClientes(opcion.id,clientesAsignados2);
                if(pos>-1) {
                    {
                        clientesAsignados2.remove(pos);
                        clientesAsignados3.remove(pos);
                    }
                    Log.d(TAG, "CLIENTES ASIG QUITÉ UNO " + clientesAsignados2.size());
                    CreadorFormulario.cargarSpinnerDescr(getContext(), spinn2, clientesAsignados2);

                    CreadorFormulario.cargarSpinnerDescr(getContext(), spinn3, clientesAsignados3);
                }
                |}



            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/

        //  else
        //     CreadorFormulario.cargarSpinnerDescr(getContext(),spinn,Constantes.clientesAsignados);

    }

    public void saliendoSinguardar() {

        //para avisar que sale sin guardar
        EditText fotofachada = root.findViewById(R.id.txtaifotofachada);
        EditText nombretienda = root.findViewById(1001);
        if (guardado) {
            super.onDestroyView();
        } else if (!fotofachada.getText().toString().equals("") && visitaEdi == null) {
            Log.d(TAG, "Estoy saliendoe" + fotofachada.getText().toString() + visitaEdi);

            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
            dialogo1.setTitle(R.string.importante);
            dialogo1.setMessage(R.string.salir_sin_guardar);
            dialogo1.setCancelable(true);
            dialogo1.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                    AbririnformeFragment.super.onDestroyView();

                }
            });
            dialogo1.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    dialogo1.cancel();
                    return;

                }
            });
            dialogo1.show();
            // return;
        }


      //else
    //    super.onDestroyView();
      //  super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(Local!=null)
            Local.desactivar();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(Local!=null)
        Local.desactivar();
    }
    private void setupSnackbar() {
        // Mostrar snackbar en resultados positivos de operaciones (crear, editar y eliminar)
        mViewModel.getSnackbarText().observe(getActivity(), integerEvent -> {
            Integer stringId = integerEvent.getContentIfNotHandled();
            if (stringId != null) {
                Snackbar.make(getActivity().findViewById(R.id.coordinator),
                        stringId, Snackbar.LENGTH_LONG).show();
            }
        });
    }
    public void crearFormulario(Visita visita){
        if(nuevaTienda) {
            camposTienda = new ArrayList<CampoForm>();
            CampoForm campo = new CampoForm();
            campo.label = getString(R.string.nombre_tienda);
            campo.nombre_campo = "tiendaNombre";
            campo.type = "inputtext";
            campo.value =visita.getTiendaNombre() ;
            campo.required = "required";
            campo.id = 1001;
            camposTienda.add(campo);

           /* campo = new CampoForm();
            campo.label = "Direccion";
            campo.nombre_campo = "direccion";
            campo.type = "inputtext";
            campo.value = visita.getDireccion();
            campo.required = "required";
            campo.id = 1002;
            camposTienda.add(campo);
         /*   campo = new CampoForm();
            campo.label = "Complmento dirección";
            campo.nombre_campo = "complementodireccion";
            campo.type = "inputtext";
            campo.value = null;
            campo.required = "required";
            campo.id = 1003;
            camposTienda.add(campo);*/
         /*   campo = new CampoForm();
            campo.label = "Punto cardinal";
            campo.nombre_campo = "puntoCardinal";
            campo.type = "inputtext";
            campo.value = visita.getPuntoCardinal();
            campo.required = "required";
            campo.id = 1004;
            camposTienda.add(campo);*/
            campo = new CampoForm();
            campo.label = "TIPO TIENDA";
            campo.nombre_campo = "tipoTienda";
            campo.type = "select";
            campo.value = visita.getTipoId()+"";
            Log.d(TAG,"tienda sel "+visita.getTipoId());
            campo.select=Constantes.TIPOTIENDA;
            campo.required = "required";
            campo.id = 1005;
            camposTienda.add(campo);
            /*
            campo = new CampoForm();
            campo.label = "Cadena comercial";
            campo.nombre_campo = "cadenaComercial";
            campo.type = "inputtext";
            campo.value = visita!=null?visita.getCadenaComercial():null;
            campo.required = "required";
            campo.id = 1006;
            camposTienda.add(campo);*/

        }else //ya existe
        {
            camposTienda = new ArrayList<CampoForm>();
            CampoForm campo = new CampoForm();
            campo.label = getString(R.string.nombre_tienda);
            campo.nombre_campo = "tiendaNombre";
            campo.type = "inputtext";
            campo.value = tienda.getUne_descripcion();
            campo.required = "required";
            campo.id = 1001;
            campo.readonly="readonly";
            camposTienda.add(campo);

            campo = new CampoForm();
            campo.label = getString(R.string.direccion);
            campo.nombre_campo = "direccion";
            campo.type = "inputtext";
            campo.value = tienda.getUne_direccion();
            campo.required = "required";
            campo.readonly="readonly";
            campo.id = 1002;
            camposTienda.add(campo);
            campo = new CampoForm();
            campo.label =getString(R.string.tipo_tienda) ;
            campo.nombre_campo = "tipoTienda";
            campo.type = "inputtext";
            campo.readonly="readonly";
            campo.value =tienda.getTipoTienda();
            campo.required = "required";
            campo.id = 1005;
            camposTienda.add(campo);


        }
     /***finaliza campos de tienda***/
        camposForm = new ArrayList<CampoForm>();
      //  CampoForm campo2 = new CampoForm();
      /*  campo2.label=getString(R.string.ciudad);
        campo2.nombre_campo = "ciudad";
        campo2.type = "inputtext";
        campo2.value = null;
        campo2.required = "required";
        campo2.id = 1007;
        camposForm.add(campo2);
        campo2 = new CampoForm();

        campo2.nombre_campo = "pais";
        campo2.type = "inputtext";
        campo2.value = null;
        campo2.required = "required";
        campo2.id = 1008;
        camposForm.add(campo2);*/
        /***hago preguntas si no**/
        CampoForm campo = new CampoForm();
        campo.label = "¿FOTO DE EXHIBIDOR DE OTRO CLIENTE?";
        campo.nombre_campo = "mascliente";

        LinearLayout ll2=root.findViewById(R.id.llfotoex2);
        LinearLayout ll3=root.findViewById(R.id.llfotoex3);
        if(fotosExh!=null&&fotosExh.size()>1) {
            campo.value = "true";
            mostrarOcultarlayout(View.VISIBLE,ll2);
        }
       campo.funcionOnClick=new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               //muestro sig boton

               mostrarOcultarlayout(View.VISIBLE,ll2);
               //quito el cliente seleecionado del spinner
               DescripcionGenerica cliente=(DescripcionGenerica)spinn.getSelectedItem();
             //  Log.d(TAG,spinn.getId()+"<--"+spinn.getSelectedItemId());
               int pos=buscarEnClientes(cliente.id,clientesAsignados2);
               //lo bloqueo
               spinn.setEnabled(false);
               if(pos>-1) {
                   {
                       clientesAsignados2.remove(pos);

                   }
                    pos=buscarEnClientes(cliente.id,clientesAsignados3);
                   if(pos>-1)
                       {

                           clientesAsignados3.remove(pos);
                       }
                   Log.d(TAG, "CLIENTES ASIG QUITÉ UNO " + clientesAsignados2.size());
                   CreadorFormulario.cargarSpinnerDescr(getContext(), spinn2, clientesAsignados2);

                   CreadorFormulario.cargarSpinnerDescr(getContext(), spinn3, clientesAsignados3);
               }
           }
       };
       campo.funcionOnClick2=new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               //oculto el layout
               spinn.setEnabled(true);
               loopViews(ll2);
               mostrarOcultarlayout(View.GONE,ll2);
                //borrar si hay algo

           }
       };
        campo.required = "required";
        campo.id = 1010;
        snmascli1 = root.findViewById(R.id.snaimascli1);
        if(clientesAsignados!=null&&clientesAsignados.size()==1){
            snmascli1.setVisible(View.GONE);
        }else {
            //camposForm.add(campo);


            snmascli1.setmLabel(campo.label);
            snmascli1.setStyleLabel(R.style.formlabel);
            if (campo.style > 0) {
                snmascli1.setStyleLabel(campo.style);
            }
            if (campo.value != null && campo.value.equals("true")) {
                snmascli1.setSi(true);

            }
            snmascli1.onclicksi(campo.funcionOnClick);
            snmascli1.onclickno(campo.funcionOnClick2);


            // }
            snmascli1.setId((campo.id));

            snmascli2 = root.findViewById(R.id.snmascli2);

            snmascli2.setmLabel(campo.label);
            snmascli2.setStyleLabel(R.style.formlabel);
            if (campo.style > 0) {
                snmascli2.setStyleLabel(campo.style);
            }
            if (fotosExh != null && fotosExh.size() > 2) {
                campo.value = "true";
                mostrarOcultarlayout(View.VISIBLE, ll3);
            } else
                campo.value = "false";
            if (campo.value != null && campo.value.equals("true")) {
                snmascli2.setSi(true);

            }
            snmascli2.onclicksi(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //muestro sig boton

                    mostrarOcultarlayout(View.VISIBLE, ll3);
                    DescripcionGenerica cliente = (DescripcionGenerica) spinn2.getSelectedItem();
                    int pos = buscarEnClientes(cliente.id, clientesAsignados3);
                    spinn2.setEnabled(false);
                    if (pos > -1) {
                        {

                            clientesAsignados3.remove(pos);
                        }


                        CreadorFormulario.cargarSpinnerDescr(getContext(), spinn3, clientesAsignados3);
                    }

                }
            });
            snmascli2.onclickno(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //muestro sig boton
                    spinn2.setEnabled(true);
                    loopViews(ll3);
                    mostrarOcultarlayout(View.GONE, ll3);

                }
            });


            // }
            snmascli2.setId(1011);

        }

        cf1=new CreadorFormulario(camposTienda,getActivity());
      //  cf2=new CreadorFormulario(camposForm,getActivity());



    }
    public void mostrarOcultarlayout(int val, LinearLayout ll){
        ll.setVisibility(val);

    }
    public void loopViews( LinearLayout layout ){

        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);


            if (child instanceof EditText) {
                // Do something
                ((EditText) child).setText("");
            }if (child instanceof ImageView) {
                // Do something
                ((ImageView) child).setImageBitmap(null);
            } /* else  /*if (child instanceof LinearLayout) {

                this.loopViews((LinearLayout) child);
            }*/
            /*if (child instanceof ViewGroup) {

                this.loopViews((ViewGroup) child);
            }*/


        }
    }

  /*  public void camposFotosProd(){
        List<CampoForm> camposForm=new ArrayList<>();
        //busco el num de clientes
        ListaCompraRepositoryImpl listaRepo=ListaCompraRepositoryImpl.getInstance(ComprasDataBase.getInstance(getActivity()).getListaCompraDao());

        listaRepo.getClientesByIndiceCiudad(Constantes.INDICEACTUAL,Constantes.IDCIUDADTRABAJO)
                .observeForever(new Observer<List<ListaCompra>>() {
                    @Override
                    public void onChanged(List<ListaCompra> listaCompras) {
                        int i=0;
                        totClientes=listaCompras.size();
                        for (ListaCompra compra: listaCompras) {

                            CampoForm campo2=new CampoForm();
                            campo2.label="Foto exhibición "+compra.getClienteNombre();
                            campo2.nombre_campo="producto_exhibido";
                            campo2.type="agregarImagen";
                            campo2.value=null;
                            campo2.id=1030+i;
                            CampoForm finalCampo = campo2;
                            int finali=i;
                            campo2.funcionOnClick=new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    tomarFoto(finalCampo.id,1040+finali);
                                }
                            };
                            campo2.tomarFoto=true;
                            campo2.required="required";

                            camposForm.add(campo2);
                            campo2=new CampoForm();
                            campo2.nombre_campo="producto_exhibido";
                            campo2.type="imagenView";

                            campo2.id=1040+i;


                            camposForm.add(campo2);
                            campo2=new CampoForm();
                            campo2.nombre_campo="cliente";
                            campo2.type="inputtext";
                            campo2.id=1050+i;

                            i++;

                            camposForm.add(campo2);
                        }


                    }
                });
        cf3=new CreadorFormulario(camposForm,getActivity());


    }*/
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
     /*   savedInstanceState.putString(IMG_PATH1, foto1.getText().toString());
        savedInstanceState.putString(DESCRIPCION, descripcion1.getText().toString());
        savedInstanceState.putString(TXTUBICACION, ubicacion.getText().toString());
       */ // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void probarUbicacion() {
        //
        //    createLocationRequest();
        // GPS_ACTIVE = 1;
        //  obtenerUbicacion();
        Log.d("AbrirInformeFragment","presione boton");
      /*  if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            //  requestPermissionLauncher.launch(
            //        Manifest.permission.REQUESTED_PERMISSION);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        } else {*/
            // rastreoGPS();
            locationStart();
        //}

    }
    public boolean guardar(){

        /*AwesomeValidation mAwesomeValidation = new AwesomeValidation(UNDERLABEL);
        mAwesomeValidation.setContext(this);*/
     //   AwesomeValidation mAwesomeValidation = new AwesomeValidation(COLORATION);
      //  mAwesomeValidation.addValidation(this, R.id., RegexTemplate.NOT_EMPTY, R.string.invalid_name);
        EditText input7 = root.findViewById(1007);
        EditText input8 = root.findViewById(1008);

        EditText fotofachada = root.findViewById(R.id.txtaifotofachada);

        Log.d(TAG,"foto fachada"+fotofachada.getText().toString());
       // EditText input13 = root.findViewById(1013);
         mViewModel.visita=new Visita();
        mViewModel.visita.setId(nuevoId);
        EditText input1;
        if(nuevaTienda){
            input1 = root.findViewById(1001);
            if(input1.getText().toString().equals("")) {
                Toast.makeText(getActivity(), getString(R.string.error_nombre_tienda), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (txtubicacion.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Falta activar la ubicación", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (fotofachada.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Falta foto de fachada", Toast.LENGTH_SHORT).show();
            return false;
        }
        Log.d(TAG,"xxxxxx"+txtfotoex1.getText().toString());
        if(txtfotoex1.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Falta foto de producto exhibido", Toast.LENGTH_SHORT).show();
            return false;
        }if(snmascli1.getRespuesta())//veo que tenga foto etc
            if(txtfotoex2.getText().toString().equals("")){
                Toast.makeText(getActivity(), "Falta foto de producto exhibido", Toast.LENGTH_SHORT).show();
                return false;
            }
        if(snmascli2!=null&&snmascli2.getRespuesta())//veo que tenga foto etc
            if(txtfotoex3.getText().toString().equals("")){
                Toast.makeText(getActivity(), "Falta foto de producto exhibido", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(nuevaTienda){
                input1 = root.findViewById(1001);
                EditText input2 = root.findViewById(1002);

                EditText input4 = root.findViewById(1004);
                Spinner input5 = root.findViewById(1005);
                String tipotiendasel=input5.getSelectedItem().toString();
                int idtipo=(int)input5.getSelectedItemId();
                EditText input6 = root.findViewById(1006);

                mViewModel.visita.setTiendaNombre(input1.getText().toString());
                mViewModel.visita.setTipoTienda(tipotiendasel);

                mViewModel.visita.setTipoId(idtipo+1);
                mViewModel.visita.setDireccion(mensajedir.getText().toString());

               // mViewModel.visita.setCadenaComercial(input6.getText().toString());
                //busco el punto cardinal

               buscarZona(txtubicacion.getText().toString());
                mViewModel.visita.setPuntoCardinal(input4.getText().toString());

            }else
            {//ya existe

                EditText input5 = root.findViewById(1005);
                 input1 = root.findViewById(1001);
                EditText input2 = root.findViewById(1002);

                mViewModel.visita.setTiendaId(tienda.getUne_id());
                mViewModel.visita.setTiendaNombre(tienda.getUne_descripcion());
                mViewModel.visita.setTipoTienda(tienda.getTipoTienda());
                mViewModel.visita.setDireccion(mensajedir.getText().toString());

                mViewModel.visita.setTipoId(tienda.getUne_tipotienda());
            }

            mViewModel.visita.setComplementodireccion(txtcomplemento.getText().toString());
            mViewModel.visita.setIndice(Constantes.INDICEACTUAL);
            //guardo la foto
            //guardar imagen

            mViewModel.fotoFachada=new ImagenDetalle();
            mViewModel.fotoFachada.setRuta(fotofachada.getText().toString());
            mViewModel.fotoFachada.setDescripcion("Foto fachada");
            mViewModel.fotoFachada.setEstatus(1);
            mViewModel.fotoFachada.setIndice(Constantes.INDICEACTUAL);
            mViewModel.fotoFachada.setEstatusSync(0);
            mViewModel.fotoFachada.setCreatedAt(new Date());
            //la ciudad y el pais en el que está se definen en las propiedades

            mViewModel.visita.setCiudadId(Constantes.IDCIUDADTRABAJO);
            mViewModel.visita.setCiudad(Constantes.CIUDADTRABAJO);
            mViewModel.visita.setPaisId(Constantes.IDPAISTRABAJO);
            mViewModel.visita.setPais(Constantes.PAISTRABAJO);

            mViewModel.visita.setClaveUsuario(Constantes.CLAVEUSUARIO);

         //   EditText input2 = root.findViewById(1001);
        //    mViewModel.informe.setNombreTemporal(nombreTemporal.getText().toString());
            mViewModel.visita.setEstatus(1);
            mViewModel.visita.setEstatusSync(0);
            mViewModel.visita.setCreatedAt(new Date());
            mViewModel.visita.setGeolocalizacion(txtubicacion.getText().toString());


            MutableLiveData<Integer> resv=mViewModel.guardarVisita(getActivity(),this);
           resv.observe(this, new Observer<Integer>() {
               @Override
               public void onChanged(Integer integer) {
                   mViewModel.eliminarTblTemp();
                   guardado=true;
                   guardarFotoExhibido(txtfotoex1.getText().toString(),nuevoId,spinn);
                   if(snmascli1!=null&&snmascli1.getRespuesta()) {
                       Log.d(TAG,"entre aqui"+spinn.getId()+"--"+spinn2.getId());
                       guardarFotoExhibido(txtfotoex2.getText().toString(), nuevoId, spinn2);
                   }
                   if(snmascli2!=null&&snmascli2.getRespuesta()) {
                       guardarFotoExhibido(txtfotoex3.getText().toString(), nuevoId, spinn3);
                   }

               }
           });

        return true;
    }
    public void guardarFotoExhibido(String ruta1,int visitasId,Spinner spinnx){

        DescripcionGenerica cliente=(DescripcionGenerica)spinnx.getSelectedItem();
        Log.d(TAG,"guardarfoto cliente"+cliente.getId());
        NuevoinformeViewModel ninfViewModel =
                new ViewModelProvider(this).get(NuevoinformeViewModel.class);

        feviewModel.guardarFoto(ruta1,cliente.getId(),cliente.getNombre(),visitasId,getActivity(),ninfViewModel);
        Toast.makeText(getActivity(), "Se agregó la foto", Toast.LENGTH_LONG).show();
      //  getActivity().finish();

    }
    public void sologuardar(){
        if(guardar()) {


            NavHostFragment.findNavController(this).navigate(R.id.action_nuevotolista);
        }
    }
    public void guardarContinuar(){
        if(guardar()) {

            Bundle bundle = new Bundle();
            bundle.putInt(NuevoinformeFragment.INFORMESEL, mViewModel.idVisita);
            /* bundle.putString("plantaNombre", listaSeleccionable.get(i).getNombre());*/
            /*   NavHostFragment.findNavController(this).navigate(R.id.action_selclientetolistacompras,bundle);
             */
            NavHostFragment.findNavController(this).navigate(R.id.action_continuar, bundle);

        }
    }
    //con la geolocalizacion de la tienda se busca la zona en la que esta
    //devuelve el id de la zona de acuerdo al catalogo
    //se puede consultar el catalogo en MapdaCDFragment
    public int buscarZona(String puntotxt){
        if(puntotxt.equals("")){
            return 0;
        }
        String[] auxp=puntotxt.split(",");
        LatLng punto=new LatLng(Double.parseDouble(auxp[0]),Double.parseDouble(auxp[1]));
        //busco las zonas de la ciudad de trabajo
        GeocercaRepositoryImpl georep=new GeocercaRepositoryImpl(getActivity());
        List<Geocerca> zonas=georep.findsimplexCd(Constantes.CIUDADTRABAJO);
        LatLng p1;
        LatLng p2;
        LatLng p3;
        LatLng p4;
        if(zonas!=null)
        for(Geocerca geo:zonas){
            String aux[]=geo.getGeo_p1().split(",");
             p1 = new LatLng(Double.parseDouble(aux[0]), Double.parseDouble(aux[1]));
            String aux2[]=geo.getGeo_p2().split(",");
             p2 =new LatLng(Double.parseDouble(aux2[0]), Double.parseDouble(aux2[1]));
            String aux3[]=geo.getGeo_p3().split(",");
             p3 =new LatLng(Double.parseDouble(aux3[0]), Double.parseDouble(aux3[1]));
            String aux4[]=geo.getGeo_p4().split(",");
             p4 =new LatLng(Double.parseDouble(aux4[0]), Double.parseDouble(aux4[1]));
            List<LatLng> poly=new ArrayList<>();
            poly.add(p1);
            poly.add(p2);
            poly.add(p3);
            poly.add(p4);
            //mMap.addPolygon(new PolygonOptions()
           if(PolyUtil.containsLocation(punto,poly,false))
               return geo.getGeo_region();
        }
        return 0; //no estuvo lol
    }
    public void actualizar(){
        EditText fotofachada = root.findViewById(R.id.txtaifotofachada);
        EditText input1;
        if(nuevaTienda){
             input1 = root.findViewById(1001);
             if(input1.getText().toString().equals("")) {
                 Toast.makeText(getActivity(), getString(R.string.error_nombre_tienda), Toast.LENGTH_SHORT).show();
                 return;
             }
        }
        if (txtubicacion.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Falta activar la ubicación", Toast.LENGTH_SHORT).show();
            return;
        }
        if (fotofachada.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Falta foto de fachada", Toast.LENGTH_SHORT).show();
            return ;
        }
        if(txtfotoex1.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Falta foto de producto exhibido", Toast.LENGTH_SHORT).show();
            return ;
        }if(snmascli1.getRespuesta())//veo que tenga foto etc
            if(txtfotoex2.getText().toString().equals("")){
                Toast.makeText(getActivity(), "Falta foto de producto exhibido", Toast.LENGTH_SHORT).show();
                return ;
            }
        if(snmascli2!=null&&snmascli2.getRespuesta())//veo que tenga foto etc
            if(txtfotoex3.getText().toString().equals("")){
                Toast.makeText(getActivity(), "Falta foto de producto exhibido", Toast.LENGTH_SHORT).show();
                return ;
            }
        //actualizo campos
        if(nuevaTienda){
             input1 = root.findViewById(1001); //nombre de la tienda
            EditText input2 = root.findViewById(1002);

            EditText input4 = root.findViewById(1004);
            Spinner input5 = root.findViewById(1005);
            EditText input6 = root.findViewById(1006);



            visitaEdi.setTiendaNombre(input1.getText().toString());
            visitaEdi.setTipoTienda((input5.getSelectedItem()).toString());

            visitaEdi.setTipoId((int)input5.getSelectedItemId()+1);
           // visitaEdi.setDireccion(input2.getText().toString());

            //visitaEdi.setCadenaComercial(input6.getText().toString());
            //visitaEdi.setPuntoCardinal( input4.getText().toString());


        }
        EditText input3 = root.findViewById(R.id.txtaicomplementodir);
        visitaEdi.setComplementodireccion(input3.getText().toString());
        mViewModel.fotoFachada=efotoFachada;
        mViewModel.fotoFachada.setRuta(fotofachada.getText().toString());
        mViewModel.fotoFachada.setEstatus(1);
        mViewModel.fotoFachada.setEstatusSync(0);
        mViewModel.fotoFachada.setUpdatedAt(new Date());
        //la ciudad y el pais en el que está se definen en las propiedades


        //   EditText input2 = root.findViewById(1001);
        //    mViewModel.informe.setNombreTemporal(nombreTemporal.getText().toString());
        visitaEdi.setEstatus(1);
        visitaEdi.setEstatusSync(0);
        visitaEdi.setUpdatedAt(new Date());
        visitaEdi.setGeolocalizacion(txtubicacion.getText().toString());
        mViewModel.visita=visitaEdi;
        mViewModel.actualizarVisita();
        //borro las fotos y vuelvo a insertar
        for(ProductoExhibidoDao.ProductoExhibidoFoto  foto :fotosExh){
            feviewModel.eliminarFoto(foto);
            //
        }
        //Log.d(TAG,"**CLIENTES ASIG "+Constantes.clientesAsignados.size());

        guardarFotoExhibido(txtfotoex1.getText().toString(),nuevoId,spinn);
        if(snmascli1.getRespuesta())
            guardarFotoExhibido(txtfotoex2.getText().toString(),nuevoId,spinn2);
        if(snmascli2!=null&&snmascli2.getRespuesta())
            guardarFotoExhibido(txtfotoex3.getText().toString(),nuevoId,spinn3);

        guardado=true;
        NavHostFragment.findNavController(this).navigate(R.id.action_nuevotolista);

    }

    @Override
    public void onClickEliminar(ProductoExhibidoDao.ProductoExhibidoFoto foto) {
        //para eliminar las fotos exhibicion
        //Pregunto si está seguro
        Log.d(TAG,"eliminando"+foto.idprodex);
        feviewModel.eliminarFoto(foto);
        //actualizo la lista
        mListAdapter.notifyDataSetChanged();
    }


    public class Localizacion implements LocationListener {
      /*  public void activar() {
            if ( mlocManager!=null) activarProveedores();
        }*/
        public void desactivar() {
            if ( mlocManager!=null) {
                Log.d(TAG,"desactivando");
                mlocManager.removeUpdates(Local);
            }
            mlocManager=null;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion
            if(getActivity()!=null) {

                //Local.desactivar();
                mostrarPosicion(loc);

            }
            //desactivar();
             //this.mainActivity.setLocation(loc);
        }
        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado

        }
        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            Log.d(TAG, "---------------gps activado");
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }


    }
    public void mostrarPosicion(Location location){
        String latitude = String.valueOf(location.getLatitude());
        String longitude = String.valueOf(location.getLongitude());
       Log.d(TAG,"****Ya tengo la ubicacion" + latitude + "," + longitude);
        if(!yaTengoFoto) {
            //buscar direccion
            try {

                mensajedir = root.findViewById(R.id.txtaimensajeubicacion);
                mensajedir.setText("Ubicación registrada");
                if (ComprasUtils.isOnlineNet()) {
                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    List<Address> list = new ArrayList<>();
                    if (geocoder != null)
                        list = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1);


                    if (!list.isEmpty()) {
                        Address DirCalle = list.get(0);
                        String state = DirCalle.getAdminArea();
                        country = DirCalle.getCountryName();

                        mensajedir.setText(DirCalle.getAddressLine(0));
                        //   ubicacion.setText(state+","+country);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            EditText input1 = root.findViewById(R.id.txtaiubicacion);
            input1.setText(latitude + "," + longitude);
        }
      //  mViewModel.visita.setGeolocalizacion(latitude + "," + longitude);

    }

    @Override
    public void onValidationSucceeded() {
       guardar();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getActivity());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == 1000) {
            if (grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }

   /* public void irAProductoEx(){
        Intent intento1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");

        String dateString = format.format(new Date());
        File foto=null;
        try{
            nombre_foto = "img_" +Constantes.CLAVEUSUARIO+"_"+ dateString + ".jpg";
            foto = new File(getActivity().getExternalFilesDir(null), nombre_foto);
            Log.e(TAG, "****"+foto.getAbsolutePath());
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(getActivity(), "No se encontró almacenamiento externo", Toast.LENGTH_SHORT).show();


        }
        Uri photoURI = FileProvider.getUriForFile(getActivity(),
                "com.example.comprasmu.fileprovider",
                foto);
        intento1.putExtra(MediaStore.EXTRA_OUTPUT, photoURI); //se pasa a la otra activity la referencia al archivo
        startActivityForResult(intento1, REQUEST_CODE_PROD);
    }*/
    String nombre_foto;
    public   void tomarFoto(EditText origen, ImageView destino, int REQUEST_CODE) {
        if(getAvailableMemory(getActivity()).lowMemory)
        {
            Toast.makeText(getActivity(), "No hay memoria suficiente para esta accion", Toast.LENGTH_SHORT).show();

            return;
        }

        File foto=null;
        Activity activity=this.getActivity();
        Intent intento1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try{
        try{

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");

        String dateString = format.format(new Date());


            nombre_foto = "img_" +Constantes.CLAVEUSUARIO+"_"+ dateString + ".jpg";
            foto = new File(activity.getExternalFilesDir(null), nombre_foto);
            Log.d(TAG, "****"+foto.getAbsolutePath());
        } catch (Exception  ex) {
            ex.printStackTrace();
            Toast.makeText(activity, "No se encontró almacenamiento externo", Toast.LENGTH_SHORT).show();


        }
        Uri photoURI = FileProvider.getUriForFile(activity,
                "com.example.comprasmu.fileprovider",
                foto);
        intento1.putExtra(MediaStore.EXTRA_OUTPUT, photoURI); //se pasa a la otra activity la referencia al archivo
        //intento1.putExtra("origen", origen);

         if(destino!=null) {
          //   imageView = root.findViewById(destino);
             destino.setVisibility(View.VISIBLE);
            // rotar = root.findViewById(R.id.btnairotar1);
           //  rotar.setVisibility(View.VISIBLE);
             startActivityForResult(intento1, REQUEST_CODE);
         }

    } catch (OutOfMemoryError  ex) {
        ex.printStackTrace();
        Toast.makeText(activity, "No hay memoria suficiente para esta accion", Toast.LENGTH_SHORT).show();


    }

    }
    /****vuelve el foco***/
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"Estoy aqui ptra vez");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
         if ((requestCode == REQUEST_CODE_TAKE_PHOTO||requestCode==REQUEST_CODE_PROD1||requestCode == REQUEST_CODE_PROD2||requestCode==REQUEST_CODE_PROD3) && resultCode == RESULT_OK) {
          //  super.onActivityResult(requestCode, resultCode, data);
            Log.d(TAG,"vars*"+requestCode +"=="+ resultCode+" =="+ RESULT_OK);
            Log.d(TAG,"nombre archivo result "+nombre_foto);
             String state = Environment.getExternalStorageState();
             String baseDir;
             if(Environment.MEDIA_MOUNTED.equals(state)) {
                 File baseDirFile = getActivity().getExternalFilesDir(null);
                 if(baseDirFile == null) {
                     baseDir = getActivity().getFilesDir().getAbsolutePath();
                 } else {
                     baseDir = baseDirFile.getAbsolutePath();
                 }
             } else {
                 baseDir = getActivity().getFilesDir().getAbsolutePath();
             }
            File file = new File(baseDir, nombre_foto);
            if (file.exists()) {
                if(requestCode == REQUEST_CODE_TAKE_PHOTO) {
                    //es la de fachada
                    //envio a la actividad dos para ver la foto
                   /* Intent intento1 = new Intent(getActivity(), RevisarFotoActivity.class);
                    intento1.putExtra("ei.archivo", nombre_foto);
                    intento1.putExtra("ei.opcionfoto", "nose");

                  //  startActivity(intento1);*/
                    yaTengoFoto=false;
                    probarUbicacion();
                    txtfotofachada.setText(nombre_foto);
                    Bitmap bitmap1 = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null) + "/" + nombre_foto);
                    ComprasUtils cu=new ComprasUtils();
                    bitmap1=cu.comprimirImagen(getActivity().getExternalFilesDir(null) + "/" + nombre_foto);

                    fotofac.setImageBitmap(bitmap1);
                    rotar.setVisibility(View.VISIBLE);
                  //  agregarImagen();
                    Local.desactivar();
                   // mostrarPosicion(Local);
                    yaTengoFoto=true;
                   // nombre_foto=null;
                   //
                }
                if(requestCode == REQUEST_CODE_PROD1) {
                    txtfotoex1.setText(nombre_foto);
                   // Bitmap bitmap1 = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null) + "/" + nombre_foto);
                    ComprasUtils cu=new ComprasUtils();
                    Bitmap bitmap1=cu.comprimirImagen(getActivity().getExternalFilesDir(null) + "/" + nombre_foto);

                    fotoex1.setImageBitmap(bitmap1);
                    btnrotar1.setVisibility(View.VISIBLE);
                  //  agregarImagen();
                }
                if(requestCode == REQUEST_CODE_PROD2) {
                    txtfotoex2.setText(nombre_foto);
                  //  Bitmap bitmap2 = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null) + "/" + nombre_foto);
                    ComprasUtils cu=new ComprasUtils();
                    Bitmap bitmap2=cu.comprimirImagen(getActivity().getExternalFilesDir(null) + "/" + nombre_foto);

                    fotoex2.setImageBitmap(bitmap2);
                    btnrotar2.setVisibility(View.VISIBLE);
                  //  agregarImagen();
                }

                if(requestCode == REQUEST_CODE_PROD3) {
                    txtfotoex3.setText(nombre_foto);
                   // Bitmap bitmap1 = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null) + "/" + nombre_foto);
                    ComprasUtils cu=new ComprasUtils();
                    Bitmap bitmap1=cu.comprimirImagen(getActivity().getExternalFilesDir(null) + "/" + nombre_foto);

                    fotoex3.setImageBitmap(bitmap1);
                    btnrotar3.setVisibility(View.VISIBLE);
                 //   agregarImagen();
                }


            }
            else{
                Log.d(TAG,"Algo salió mal???");
            }


        }else{
            super.onActivityResult(requestCode, resultCode, data);
            Log.d(TAG,"Algo salió muy mal");
        }
    }

  /*  public void agregarImagen() {

        //reviso si hay datos
        try {


           ActivityManager.MemoryInfo memoryInfo = getAvailableMemory(getActivity());

            if (!memoryInfo.lowMemory) {
                Bitmap bitmapOrg = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null) + "/" + nombre_foto);

                //comprimir imagen
                File file = new File(getActivity().getExternalFilesDir(null) + "/" + nombre_foto);
                OutputStream os = null;
                try {
                    os = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    Log.d("RevisarFotoActivity", e.getMessage());
                    Toast.makeText(getContext(),"No se encontró el archivo" , Toast.LENGTH_SHORT).show();

                }
                bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 45, os);




            }
            else
            {
                Toast.makeText(getContext(), "Memoria insuficiente, no se guardó el reporte", Toast.LENGTH_SHORT).show();

            }

        } catch (Exception ex) {
            Log.d(TAG,ex.getMessage());
            Toast.makeText(getActivity(), "Algo salió mal", Toast.LENGTH_SHORT).show();
        }


    }*/
    public void rotar(View view,EditText nombrefoto, ImageView foto) {
        String nombreArch="";
        if (AbririnformeFragment.getAvailableMemory(getActivity()).lowMemory) {
            Toast.makeText(getActivity(), "No hay memoria suficiente para esta accion", Toast.LENGTH_SHORT).show();

            return;
        } else {
            if (nombrefoto != null) {
                nombreArch = nombrefoto.getText().toString();
            }
            RevisarFotoActivity.rotarImagen(getActivity().getExternalFilesDir(null) + "/" + nombreArch, foto);
        }
    }
    public static ActivityManager.MemoryInfo getAvailableMemory(Activity act) {
        ActivityManager activityManager = (ActivityManager) act.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        return memoryInfo;
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
                    if(s.length()>0) {
                        String s2=s.toString();
                        String nueva="";
                        if(!s2.equals(s2.toUpperCase()))
                        {
                            nueva=s2.toUpperCase();

                        }
                        s.replace(0, nueva.length(), s.toString().toUpperCase());
                    }
                } catch (NumberFormatException nfe) {
                    s.clear();
                }

                mEditing = false;
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        public void onTextChanged(CharSequence s, int start, int before, int count) { }

    }


}