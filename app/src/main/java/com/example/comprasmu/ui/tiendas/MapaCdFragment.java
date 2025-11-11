package com.example.comprasmu.ui.tiendas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;

import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import com.example.comprasmu.DescargarListaAsyncTask;
import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.dao.ListaCompraDao;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.Contrato;
import com.example.comprasmu.data.modelos.Correccion;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.Geocerca;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.Tienda;
import com.example.comprasmu.data.modelos.TiendaEstatusCliente;
import com.example.comprasmu.data.remote.RespInfEtapaResponse;
import com.example.comprasmu.data.remote.RespInformesResponse;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;
import com.example.comprasmu.services.DescAutomaticasServiceManager;
import com.example.comprasmu.ui.informedetalle.NuevoDetalleViewModel;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapaCdFragment extends Fragment implements OnMapReadyCallback ,
        GoogleMap.OnInfoWindowCloseListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener,
        DescargarListaAsyncTask.ProgresoDLListener{
    public static final String EXTRA_LATITUD = "extra_latitud";
    public static final String EXTRA_LONGITUD ="extra_longitud" ;

    private static final int LOCATION_REQUEST_CODE = 1;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private NuevoDetalleViewModel dViewModel;
    private GoogleMap mMap;
    String[] coloreszon={"#1E90FF","#FF1493", "#32CD32", "#FF8C00", "#4B0082"};
    Map<String,Float> coloresTienda;
    private  final String TAG="MapaCdFragment";
    private ArrayList<DescripcionGenerica> listaPlantasEnv;
    LiveData<List<Tienda>> listatiendas;
    List<Tienda> nollistatiendas;
    Button btnverfil;
    List<Geocerca> listageocercas;
    List<Polygon> regionPolygon;
    List<Marker> martiendas;
    int cdId;
    boolean doubleBackToExitPressedOnce = false;
    ListaDetalleViewModel lcviewModel;
    Button btncancel;
    Marker markerSel;
    LinearLayout llfiltros;
    //llcancel,
    LinearLayout lltipotienda,llcadena;
    private final long lastClickTime = 0;
    private static final int DEFAULT_ZOOM = 4;
    Spinner spplantas;
    List<DescripcionGenerica>clientesAsignados;

    private boolean locationPermissionGranted;
    private final LatLng defaultLocation = new LatLng(19.36884,  -99.16410);

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;
    private boolean requestingLocationUpdates;
    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    View view;
    private int cliente;
    private int plantaId;
    private Spinner sptipoti, spcadena, spfecha,spseleccion;

    private boolean verfiltros;
    LoadingAlert alert;
    private LocationCallback locationCallback;
    private LocationManager fusedLocationClient;
    private miLocationListener locallis;
    ComprasLog compraslog;
    LinearLayout mensajetienda;
    TextView txtcerrarmensaje;
    Circle circleNuevaTienda;
    Button btnvatienda;
    public MapaCdFragment() {
    }

    public static MapaCdFragment newInstance() {
        return new MapaCdFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mapa_cd, parent, false);
        SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapcd_container);
        lcviewModel = new ViewModelProvider(this).get(ListaDetalleViewModel.class);
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            //   cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        mapFragment.getMapAsync(this);
        btnvatienda=view.findViewById(R.id.btnmcdnvati);
        mensajetienda=view.findViewById(R.id.llmapamensajetienda);
        txtcerrarmensaje=view.findViewById(R.id.txtmapcerrarmensaje);
        verfiltros=false;
        compraslog=ComprasLog.getSingleton();
        spplantas=view.findViewById(R.id.spmcdplanta);
        spcadena=view.findViewById(R.id.spmccadenati);
        sptipoti=view.findViewById(R.id.spmctipoti);
       // spfecha=view.findViewById(R.id.spmctiempo);
        spseleccion=view.findViewById(R.id.spmcdseltienda);
        llfiltros=view.findViewById(R.id.llmfiltros);
       // llcancel=view.findViewById(R.id.llmcancel);
        lltipotienda=view.findViewById(R.id.llmcdtipotienda);
        llcadena=view.findViewById(R.id.llmcdcadena);
        llfiltros.setVisibility(View.GONE);
       // llcancel.setVisibility(View.GONE);
        mensajetienda.setVisibility(View.GONE);
        btnverfil=view.findViewById(R.id.btnmfiltros);
        btnvatienda.setEnabled(false);
        btnverfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verFiltros();

            }
        });
     /*   btncancel=view.findViewById(R.id.btnmccancel);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(markerSel!=null) {
                    Tienda tiendaSel=(Tienda)markerSel.getTag();
                    if(tiendaSel!=null) {
                        new AlertDialog.Builder(getActivity())
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle(R.string.importante)
                                .setMessage(getString(R.string.cancelar_tienda) + " " + tiendaSel.getUne_descripcion() + "?")
                                .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //hago la peticion en el servidor y recargo
                                        //getActivity().finish();
                                        PeticionMapaCd petmap = new PeticionMapaCd(Constantes.CLAVEUSUARIO);
                                        petmap.cancelTienda(tiendaSel.getUne_id());
                                        //  martiendas.remove(markerSel);
                                        markerSel.remove();

                                    }
                                })
                                .setNegativeButton(R.string.no, null)
                                .show();
                    }
                }
            }
        });
*/
        if (ContextCompat.checkSelfPermission( getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            getDeviceLocation();
        } else {
           compraslog.grabarError(TAG, "create","no hay permiso para gps "+LOCATION_REQUEST_CODE);
            // Solicitar permiso
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE);

        }
        //actualizo tiendas

        //actualizo la lista de compra
        actualizarListaCompra();

        cargarCatalogos();

        //  cargarIndices();
        Button btnbuscar=view.findViewById(R.id.btnmcdbuscar);
        btnbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DescripcionGenerica plantasel=(DescripcionGenerica)spplantas.getSelectedItem();
                if(plantasel!=null) {
                     plantaId = plantasel.id;
                    //calculo indice fin
                    buscarTiendas(plantaId);
                }
                else
                    irAcdSel();

            }
        });

        btnvatienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mensajetienda.getVisibility()==View.GONE)
                    nuevaTienda();
            }
        });

        //inicio colores tienda
        coloresTienda=new HashMap<>();
        coloresTienda.put("3",BitmapDescriptorFactory.HUE_GREEN);//verde
        coloresTienda.put("2",BitmapDescriptorFactory.HUE_YELLOW);//amarillo
        coloresTienda.put("1",BitmapDescriptorFactory.HUE_RED);
       //ArrayList<DescripcionGenerica> listaFecha;
        //listaFecha=new ArrayList<DescripcionGenerica>();
        //listaFecha.add(new DescripcionGenerica(1,"1 "+getString(R.string.anio)));
        //listaFecha.add(new DescripcionGenerica(2,"2 "+getString(R.string.anio)+"S"));
        //listaFecha.add(new DescripcionGenerica(3,"3 "+getString(R.string.anio)+"S"));
       // CreadorFormulario.cargarSpinnerDescr(getContext(),spfecha,listaFecha);
        ArrayList<DescripcionGenerica> listaop;
        listaop=new ArrayList<DescripcionGenerica>();
        listaop.add(new DescripcionGenerica(0,"SELECCIONAR OPCION"));
        listaop.add(new DescripcionGenerica(1,"POR TIPO TIENDA"));
        listaop.add(new DescripcionGenerica(2,"POR CADENA"));
        CreadorFormulario.cargarSpinnerDescr(getContext(),spseleccion,listaop);
        spseleccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the value selected by the user
                // e.g. to store it as a field or immediately call a method
                DescripcionGenerica opcion = (DescripcionGenerica) parent.getSelectedItem();
                if(opcion.getId()==1){
                    //muestro lista de tipo tiendas
                    lltipotienda.setVisibility(View.VISIBLE);
                }
                else
                {
                    lltipotienda.setVisibility(View.GONE);
                    sptipoti.setSelection(0);
                }
                if(opcion.getId()==2){
                    //muestro lista de tipo tiendas
                    llcadena.setVisibility(View.VISIBLE);
                }
                else
                {
                    llcadena.setVisibility(View.GONE);
                    spcadena.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        txtcerrarmensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mensajetienda.setVisibility(View.GONE);
                circleNuevaTienda.remove();


            }
        });
        return  view;

    }

    private void actualizarListaCompra() {
        //desactivo descargas automaticas
        DescAutomaticasServiceManager.getInstancia().pausarServicio();
        if (NavigationDrawerActivity.isOnlineNet(getActivity())) {
            alert = new LoadingAlert(getActivity());
            alert.startAlert();
            TablaVersionesRepImpl tvRepo = new TablaVersionesRepImpl(getContext());
            ListaCompraDao dao = ComprasDataBase.getInstance(getContext()).getListaCompraDao();
            ListaCompraDetRepositoryImpl lcdrepo = new ListaCompraDetRepositoryImpl(getContext());
            ListaCompraRepositoryImpl lcrepo = ListaCompraRepositoryImpl.getInstance(dao);
            PeticionesServidor ps = new PeticionesServidor(Constantes.CLAVEUSUARIO);
            DescargarListaAsyncTask task = new DescargarListaAsyncTask(getActivity(), tvRepo, lcdrepo, lcrepo, this, ps, Constantes.CIUDADTRABAJO);
            task.execute("");
        }
        else{
            Toast.makeText(getActivity(),"Esta acción requiere conexión a internet, verifique",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.d(TAG, "mapa listo ");
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission( getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            mMap.setMyLocationEnabled(true);
            getDeviceLocation();

        } else {
            Log.d(TAG, "no tengo  "+LOCATION_REQUEST_CODE);
            // Solicitar permiso
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE);

        }

        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        // Get the current location of the device and set the position of the map.

    }



    @SuppressLint("MissingPermission")
    public void onRequestPermissionsRes(int requestCode, @NonNull String[] permissions,
                                        @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //  if (requestCode == LOCATION_REQUEST_CODE) {
        // ¿Permisos asignados?
        if (permissions.length > 0 &&
                permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            getDeviceLocation();
        } else {
            //    Toast.makeText(getContext(), "Error de permisos", Toast.LENGTH_LONG).show();
            cerrar();
        }

        // }
    }



    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        Log.d(TAG, "buscando ");
        try {
            if (locationPermissionGranted) {

                fusedLocationClient=(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                locallis=new miLocationListener();
               // this.lastKnownLocation=fusedLocationClient.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                final boolean networkEnabled = fusedLocationClient.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                final boolean gpsEnabled = fusedLocationClient.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (!gpsEnabled&&!networkEnabled) {
                    Log.d(TAG, "1");
                    Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(settingsIntent);
                    return;
                }
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
                    Log.d(TAG, "2");
                    return;
                }
                if (fusedLocationClient.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)) {
                    fusedLocationClient.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, locallis);

                    Log.d(TAG, "3");

                } else  if (fusedLocationClient.getAllProviders().contains(LocationManager.GPS_PROVIDER)) {
                    //  if (Local == null) { //Validación que evita NullPointerException
                    //Requiere actualización
                    fusedLocationClient.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locallis);

                    // }
                    Log.d(TAG, "4");
                } else
                    Toast.makeText(getActivity(), "No hay gps?", Toast.LENGTH_SHORT).show();


            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);

        }
    }

    public void guardarPosicion(Location loc)
    {
        lastKnownLocation=loc;
        if (lastKnownLocation != null&&mMap!=null) {
            Log.d(TAG, "Current location is "+lastKnownLocation.getLatitude()+","+
                    lastKnownLocation.getLongitude());
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
      //  btncancel.setVisibility(View.VISIBLE);
       // llcancel.setVisibility(View.VISIBLE);
        markerSel=marker;
        return false;
    }

    public void nuevaTienda(){
        BuscadorTiendas bt=new BuscadorTiendas();
        if(lastKnownLocation!=null) {
            compraslog.info(TAG,".nuevatienda lastKnownLocation:",lastKnownLocation.getLatitude()+"--"+lastKnownLocation.getLongitude());
            if(nollistatiendas==null){
                DescripcionGenerica plantasel=(DescripcionGenerica)spplantas.getSelectedItem();
                if(plantasel!=null)
                    plantaId=plantasel.getId();
                //nollistatiendas=lcviewModel.getTiendasByPlantaSimp(Constantes.CIUDADTRABAJO,plantaId);
                nollistatiendas=lcviewModel.getTiendasSimp(Constantes.CIUDADTRABAJO,0,0,0,0);

            }
            if(nollistatiendas!=null) {
               Log.d(TAG, ".nuevatienda tot tiendas:"+nollistatiendas.size());

                compraslog.info(TAG, ".nuevatienda ", "tot tiendas:"+nollistatiendas.size());
                if (bt.hayTiendas(nollistatiendas, lastKnownLocation.getLatitude(),
                        lastKnownLocation.getLongitude(), compraslog)) {
                    compraslog.info(TAG, ".nuevatienda ", "ya existe");
                    //solo informativo te recomendamos visitar una tienda existente
                    mensajetienda.setVisibility(View.VISIBLE);
                    circleNuevaTienda = mMap.addCircle(new CircleOptions()
                            .center(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()))
                            .radius(200)
                            .strokeColor(Color.RED));


                } else {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("nuevatienda", true);
                    NavController nav = NavHostFragment.findNavController(MapaCdFragment.this);
                    Log.d(TAG, nav.getCurrentDestination().getId() + "--" + R.id.nav_tiendas);
                    if (nav.getCurrentDestination().getId() == R.id.nav_tiendas) {

                        nav.navigate(R.id.action_buscartonuevo, bundle);
                        //  NavHostFragment.findNavController(this).navigate(R.id.action_ciudadtohome);
                    }

                }
            }else{
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("nuevatienda", true);
                    NavController nav = NavHostFragment.findNavController(MapaCdFragment.this);
                    Log.d(TAG, nav.getCurrentDestination().getId() + "--" + R.id.nav_tiendas);
                    if (nav.getCurrentDestination().getId() == R.id.nav_tiendas) {

                        nav.navigate(R.id.action_buscartonuevo, bundle);
                        //  NavHostFragment.findNavController(this).navigate(R.id.action_ciudadtohome);
                    }


            }

        }else{
            Toast.makeText(getActivity(),"Espere para registrar su ubicación",Toast.LENGTH_LONG).show();

        }
    }
    public void cerrar(){
        Toast.makeText(getActivity(),"Es necesario dar permiso para utilizar esta opción",Toast.LENGTH_LONG).show();
        NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        if(navController!=null)
            navController.popBackStack();
    }
    public void verFiltros(){
        if(!verfiltros){
            btnverfil.setText("OCULTAR FILTROS");
            llfiltros.setVisibility(View.VISIBLE);
            verfiltros=true;
        }else {
            btnverfil.setText("VER FILTROS");
            llfiltros.setVisibility(View.GONE);
            verfiltros=false;
        }
    }


    public void dibujarZonas(List<Geocerca> zonas){
        regionPolygon=new ArrayList<Polygon>();

        for(Geocerca geo:zonas){
            String[] aux =geo.getGeo_p1().split(",");
            LatLng p1 = new LatLng(Double.parseDouble(aux[0]), Double.parseDouble(aux[1]));
            String[] aux2 =geo.getGeo_p2().split(",");
            LatLng p2 =new LatLng(Double.parseDouble(aux2[0]), Double.parseDouble(aux2[1]));
            String[] aux3 =geo.getGeo_p3().split(",");
            LatLng p3 =new LatLng(Double.parseDouble(aux3[0]), Double.parseDouble(aux3[1]));
            String[] aux4 =geo.getGeo_p4().split(",");
            LatLng p4 =new LatLng(Double.parseDouble(aux4[0]), Double.parseDouble(aux4[1]));

            regionPolygon.add( mMap.addPolygon(new PolygonOptions()
                    .add(p1,p2,p3,p4)
                    .strokeColor(Color.parseColor(coloreszon[geo.getGeo_region()-1]))
                    .strokeWidth(5)
            ));
            //busco el centro para poner la camara
            if(geo.getGeo_region()==5){
               // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(p4, 12));


            }
        }

    }
    public void buscarTiendas( int planta){

        //usaria la ciudad de trabajo

        markerSel=null;

       // llcancel.setVisibility(View.GONE);
        //calculo el fin
        //cambio 29/09/25 siempre es 1
        int anios=1;

        //busco el pais y cd de la planta
        int[] aux =lcviewModel.buscarClienCdxPlan(planta, Constantes.INDICEACTUAL);
        cliente=aux[0];
        String ciudad=Constantes.CIUDADTRABAJO;
       //   Log.d(TAG,"--"+0+"--"+ciudad+"..."+planta+".."+cliente+"--"+fini);
        int tipo=((CatalogoDetalle)sptipoti.getSelectedItem()).getCad_idopcion();
        int cadena=((CatalogoDetalle)spcadena.getSelectedItem()).getCad_idopcion();

      //  Log.d(TAG,"pidiendo tiendas"+(new Date()));
        mMap.clear();
        this.listatiendas=lcviewModel.getTiendas(ciudad,anios,tipo,cadena,cliente);
        this.listageocercas= lcviewModel.getGeocercas(ciudad);
        if(listageocercas!=null&&listageocercas.size()>0) {

            dibujarZonas(listageocercas);
        }
        //observo
        this.listatiendas.observe(getViewLifecycleOwner(), new Observer<List<Tienda>>() {
            @Override
            public void onChanged(List<Tienda> tiendas) {

              //  Log.d(TAG," antes de dibujar"+(new Date()));
                dibujarTiendas(tiendas, planta);

             //   alert.closeAlertDialog();
                listatiendas.removeObservers(getViewLifecycleOwner());
            }
        });




    }

    public void dibujarTiendas(List<Tienda> listiendas, int plantaId){
        martiendas=new ArrayList<>();
        LatLng japon2 = null;



        if(listiendas!=null) {
           // Log.d(TAG,"--tiendas"+listiendas.size());
            List<TiendaEstatusCliente> estatusTienda;
            StringBuilder estatusClientes = new StringBuilder();
            String color="3";
            ArrayList<DescripcionGenerica> plantasDisponibles;
            HashMap<Integer,Integer> totalPlantas=lcviewModel.getTotalPlantasxCliente(Constantes.CIUDADTRABAJO);
            for (Tienda tienda : listiendas) {
                Log.d(TAG,tienda.getUne_id()+"--"+tienda.getUne_descripcion()+"--"+tienda.getEstpep()+"--"+tienda.getEstpen());
                //busco los estatus por cliente

                tienda.setEstpep(lcviewModel.getEstatusCliente(tienda.getEstpep(),totalPlantas,4));
                tienda.setEstpen(lcviewModel.getEstatusCliente(tienda.getEstpen(),totalPlantas,5));
                tienda.setEstele(lcviewModel.getEstatusCliente(tienda.getEstele(),totalPlantas,6));
                tienda.setEstjum(lcviewModel.getEstatusCliente(tienda.getEstjum(),totalPlantas,7));
                Log.d(TAG,"despues"+tienda.getUne_id()+"--"+tienda.getUne_descripcion()+"--"+tienda.getEstpep()+"--"+tienda.getEstpen()+"--"+tienda.getEstele()+"--"+tienda.getEstjum());

                //busco los estatus por planta
                estatusTienda= lcviewModel.buscarEstatusTienda(tienda.getUne_id(),ComprasDataBase.getInstance(getActivity()).getTiendaEstatusClienteDao());
                estatusClientes = new StringBuilder();
                color="3";
                //armo lista de plantas de la ciudad
                plantasDisponibles=new ArrayList<>();
                plantasDisponibles.addAll(listaPlantasEnv);
              //  Log.i(TAG,"size antes>>"+estatusTienda.size());
                if(estatusTienda!=null)

                    for (TiendaEstatusCliente estatus:estatusTienda
                         ) {
                       if(estatus.getPlantasId()==plantaId) {
                            color = validarColorTienda(estatus.getEstatus());

                        }
                        //para poner en que tiendas puedo comprar
                        if(plantasDisponibles!=null)
                            plantasDisponibles=quitarPlanta(plantasDisponibles,estatus.getPlantasId());


                    }
              //  Log.i(TAG,"size>>"+plantasDisponibles.size());
                //el estatus es 1-rojo, 2 amarillo, 3.verde solo en verde puedo comprar o con 0
                int i=0;
                if (!plantasDisponibles.isEmpty()) {
                    for (DescripcionGenerica descripcion:plantasDisponibles
                         ) {
                        estatusClientes.append(", ");
                        estatusClientes.append(descripcion.getNombre());
                        i++;
                    }

                    estatusClientes = new StringBuilder(estatusClientes.substring(2, estatusClientes.length()));
                }
                tienda.setColor(color);

                //latitud es x longitud es y
               //  Log.d(TAG,"--"+tienda.getUne_descripcion()+tienda.getCiudad()+".."+tienda.getUne_descripcion());
                if (tienda.getUne_coordenadasxy() != null && tienda.getUne_coordenadasxy().length() > 0) {
                    String[] aux = tienda.getUne_coordenadasxy().split(",");

                    try {
                        japon2 = new LatLng(Double.parseDouble(aux[0]), Double.parseDouble(aux[1]));
                        MarkerOptions moptions = new MarkerOptions();
                        moptions.position(japon2)
                                .title(tienda.getUne_descripcion())
                                .icon(BitmapDescriptorFactory.defaultMarker(coloresTienda.get(color)));
                        if (estatusClientes.length() > 0) {
                            moptions.snippet(estatusClientes.toString());
                        }
                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                            @Override
                            public View getInfoWindow(Marker arg0) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {

                                Context context = getActivity(); //or getActivity(), YourActivity.this, etc.

                                LinearLayout info = new LinearLayout(context);
                                info.setOrientation(LinearLayout.VERTICAL);

                                TextView title = new TextView(context);
                                title.setTextColor(Color.BLACK);
                                title.setGravity(Gravity.CENTER);
                                title.setTypeface(null, Typeface.BOLD);
                                title.setText(marker.getTitle());

                                TextView snippet = new TextView(context);
                                snippet.setTextColor(Color.GRAY);
                                snippet.setTextSize(10);
                                snippet.setText(marker.getSnippet());

                                info.addView(title);
                                info.addView(snippet);

                                return info;
                            }
                        });
                        Marker marker = mMap.addMarker(moptions
                        );
                        marker.setTag(tienda);

                        martiendas.add(marker);
                    } catch (NumberFormatException ex) {
                        Log.e(TAG, "error de formato " + ex.getMessage() + "  " + tienda.getUne_descripcion());
                    }

                }

            }
        }
        if(japon2!=null)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(japon2,10));
        else
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 4));
       btnvatienda.setEnabled(true);
    }

    public String validarColorTienda(int estatus){
        if( estatus > 0) {

            return estatus+"";
        }
        else
            return "3";
    }
    public void buscarClientes(){
        //   Log.d(TAG,"cd "+Constantes.CIUDADTRABAJO);
        if(Constantes.CIUDADTRABAJO==null||Constantes.CIUDADTRABAJO.equals("")){

            if(alert!=null)
                alert.closeAlertDialog();
            irAcdSel();

            return;
        }
        List<ListaCompra> data=lcviewModel.cargarClientesSimplxet(Constantes.CIUDADTRABAJO, 2);

        Log.d(TAG, "regresó de la consulta de clientes " + data.size()+"--"+Constantes.CIUDADTRABAJO);
        clientesAsignados = convertirListaaClientes(data);
        // CreadorFormulario.cargarSpinnerDescr(getContext(),spclientes,clientesAsignados);
    }

    public  List<DescripcionGenerica> convertirListaaClientes(List<ListaCompra> lista){
        int i=0;
        List<DescripcionGenerica> mapa=new ArrayList<>();

        if(lista!=null)
            for (ListaCompra listaCompra: lista ) {
                DescripcionGenerica item=new DescripcionGenerica();
                //  Log.d(TAG,"-estoy aqui"+listaCompra.getClientesId());
                item.setId(listaCompra.getClientesId());
                item.setNombre(listaCompra.getClienteNombre());
                mapa.add(item);

            }
        return mapa;
    }
    public void buscarPlantas(String ciudadNombre){
        //para buscar las plantas


        LiveData<List<ListaCompra>> listacomp = lcviewModel.cargarPestañasEta(ciudadNombre);

        // Create the observer which updates the UI.
        final Observer< List<ListaCompra>> nameObserver = new Observer< List<ListaCompra>>() {
            @Override
            public void onChanged(@Nullable List<ListaCompra> lista) {

                convertirLista(lista);
                // setLista(listaClientesEnv);
                // siguiente(0);
                Log.d(TAG,"------- "+lista.size());
                if(lista.size()>0) {
                    //cargo el spinner
                    CreadorFormulario.cargarSpinnerDescr(getActivity(),spplantas,listaPlantasEnv);
                    DescripcionGenerica plantasel=(DescripcionGenerica)spplantas.getSelectedItem();
                    if(plantasel==null) {
                        //busco el 1o de la lista
                        DescripcionGenerica primero=listaPlantasEnv.get(0);
                        buscarTiendas(primero.id);
                    }else {

                        int planta = plantasel.id;
                        buscarTiendas(planta);
                    }


                }
                else
                    Log.d(TAG,"algo salió mal con la consulta de listas");
                listacomp.removeObservers(getViewLifecycleOwner());
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        //   lcrepo.getClientesByIndiceCiudad(Constantes.INDICEACTUAL,ciudadNombre).observe(getViewLifecycleOwner(), nameObserver);
        listacomp.observe(getViewLifecycleOwner(),nameObserver);


    }

    public void cargarCatalogos(){
        CatalogoDetalle ins=new CatalogoDetalle();
        ins.setCad_idopcion(0);
        ins.setCad_idcatalogo(1);
        ins.setCad_descripcionesp("TODAS");
        List<CatalogoDetalle> listacadena=new ArrayList<>();
        listacadena.add(ins);

        listacadena.addAll(lcviewModel.buscarCadenaComer());

        if(listacadena.size()>0) {
            //cargo el spinner

            CreadorFormulario.cargarSpinnerCat(getActivity(),spcadena,listacadena);
        }
        List<CatalogoDetalle> listatipo=new ArrayList<>();
        ins.setCad_idcatalogo(Contrato.CatalogosId.CADENACOMER);
        listatipo.add(ins);
        listatipo.addAll(lcviewModel.buscarTipoTienda());
        if(listatipo.size()>0) {
            //cargo el spinner

            CreadorFormulario.cargarSpinnerCat(getActivity(),sptipoti,listatipo);
        }
    }

    /* public void setupListAdapter() {
         adaptadorLista = new ListaSelecFragment.AdaptadorListas((AppCompatActivity) getActivity(),mViewModel);

         objetosLV.setAdapter(adaptadorLista);

     }*/
    public void irAcdSel(){
        NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        if(navController!=null)
            navController.navigate(R.id.action_buscartocdtrab);

    }
    private  void convertirLista(List<ListaCompra>lista){
        listaPlantasEnv=new ArrayList<DescripcionGenerica>();
        for (ListaCompra listaCompra: lista ) {
          /*String tupla=Integer.toString(listaCompra.getClienteId())+";"+
          listaCompra.getPlantaNombre();*/

            listaPlantasEnv.add(new DescripcionGenerica(listaCompra.getPlantasId(), listaCompra.getClienteNombre()+" "+listaCompra.getPlantaNombre(),listaCompra.getClienteNombre()));

        }

    }
    public boolean existeCliente(int id){
        for (DescripcionGenerica des: clientesAsignados
             ) {
            if(des.getId()==id)
                return true;
        }
        return false;
    }

    public ArrayList<DescripcionGenerica> quitarPlanta(ArrayList<DescripcionGenerica> listaPlantas,int planta){
        DescripcionGenerica elementoBorrar=null;
        for (DescripcionGenerica descipcion: listaPlantas
        ) {
            if(descipcion.getId()==planta) {
                elementoBorrar=descipcion;
                Log.i(TAG,"quitando:"+planta);
                break;
            }
        }
        listaPlantas.remove(elementoBorrar);
        return listaPlantas;
    }
    public void cargarIndices(){
        String[] indiceslist={"SEPTIEMBRE 2021","OCTUBRE 2021","NOVIEMBRE 2021","DICIEMBRE 2021","ENERO 2022","FEBRERO 2022","MARZO 2022","ABRIL 2022","MAYO 2022","JUNIO 2022","JULIO 2022","AGOSTO 2022"};
        ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,indiceslist);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter aa2 = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,indiceslist);
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }


    @Override
    public void onResume() {
        super.onResume();
        if(lastKnownLocation==null&&this.locationPermissionGranted){
            getDeviceLocation();
        }
    }


    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        //reviso si hay mas clientes si no no tiene caso
        Tienda tienda=(Tienda)marker.getTag();
        if(tienda.getColor()=="2"&&clientesAsignados.size()==0){
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putBoolean("nuevatienda",false);
        bundle.putInt("idtienda", tienda.getUne_id());
        bundle.putString("nombretienda", tienda.getUne_descripcion());
        bundle.putInt("tipotienda", tienda.getUne_tipotienda());
        bundle.putString("direccion", tienda.getUne_direccion());
        bundle.putString("color", tienda.getColor());
        bundle.putInt("estpep", tienda.getEstpep());
        bundle.putInt("estpen", tienda.getEstpen());
        bundle.putInt("estele", tienda.getEstele());
        bundle.putInt("estjum", tienda.getEstjum());
        this.doubleBackToExitPressedOnce = false;
        NavHostFragment.findNavController(MapaCdFragment.this).navigate(R.id.action_buscartonuevo,bundle);
        //return false;
    }

    @Override
    public void onInfoWindowClose(@NonNull Marker marker) {
        Log.d(TAG,"eto cuando es???????");
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
     //   Log.d(TAG,"ocultando++"+btncancel.getVisibility());
      //  llcancel.setVisibility(View.GONE);
        markerSel=null;
    }


    @Override
    public void onDestroy() {
        if(alert!=null) {
            alert.closeAlertDialog();

        }
        if (this.locallis != null)
            locallis.desactivar();
        super.onDestroy();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.locallis != null)
            locallis.desactivar();

    }

    @Override
    public void todoBien(RespInfEtapaResponse maininfoetaResp, RespInformesResponse maininfoResp, List<Correccion> mainRespcor) {
       if(getView()!=null) {


           String ffin= "";
           ffin= ComprasUtils.indiceaFecha2(Constantes.INDICEACTUAL);
           descargarTiendas(Constantes.CIUDADTRABAJO,ffin).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
               @Override
               public void onChanged(Boolean aBoolean) {

                   buscarPlantas(Constantes.CIUDADTRABAJO);
                   buscarClientes();

                   alert.closeAlertDialog();
               }
           });
       }
    }

    @Override
    public void notificarSinConexion() {
        //puede seguir trabajando
        alert.closeAlertDialog();
    }
    //descarga las tiendas del servidor, ffin en el indice actual
    public MutableLiveData<Boolean> descargarTiendas(String ciudad, String ffin){
        return lcviewModel.descargarTiendas(ciudad,ffin,getViewLifecycleOwner());
    }

    public class miLocationListener implements LocationListener {
        public void desactivar() {
            if ( fusedLocationClient!=null) {
                Log.d(TAG,"desactivando");
                fusedLocationClient.removeUpdates(locallis);
            }
            fusedLocationClient=null;
        }
        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            guardarPosicion(loc);


        }
        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            //  Toast.makeText(getActivity(), "Falta foto de producto exhibido", Toast.LENGTH_SHORT).show();

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
}