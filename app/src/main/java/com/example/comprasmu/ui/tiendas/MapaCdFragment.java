package com.example.comprasmu.ui.tiendas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.Contrato;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.Geocerca;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.Tienda;
import com.example.comprasmu.data.repositories.GeocercaRepositoryImpl;
import com.example.comprasmu.ui.informe.NuevoinformeViewModel;
import com.example.comprasmu.ui.informedetalle.NuevoDetalleViewModel;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.ui.visita.AbririnformeFragment;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapaCdFragment extends Fragment implements OnMapReadyCallback ,GoogleMap.OnInfoWindowCloseListener,GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {
    public static final String EXTRA_LATITUD = "extra_latitud";
    public static final String EXTRA_LONGITUD ="extra_longitud" ;
    private MapaCdFragment mFirstMapFragment;
    private static final int LOCATION_REQUEST_CODE = 1;
    private NuevoDetalleViewModel dViewModel;
    private GoogleMap mMap;
    String[] coloreszon={"#1E90FF","#FF1493", "#32CD32", "#FF8C00", "#4B0082"};
    Map<String,Float> coloresTienda;
    private  final String TAG="MapaCdFragment";
    private ArrayList<DescripcionGenerica> listaPlantasEnv;
    MutableLiveData<List<Tienda>> listatiendas;
    //  MutableLiveData<List<Tienda>> listatiendasRojas;
    //  MutableLiveData<List<Tienda>> listatiendasAmarillas;
    MutableLiveData<List<Geocerca>> listageocercas;
    List<Polygon> regionPolygon;
    List<Marker> martiendas;
    int cdId;
    boolean doubleBackToExitPressedOnce = false;
    ListaDetalleViewModel lcviewModel;
    Button btncancel;
    Marker markerSel;

    private final long lastClickTime = 0;

        Spinner spplantas;
    List<DescripcionGenerica>clientesAsignados;

      //  Spinner spindiceini;
      //  Spinner spindicefin;
    View view;
    private int cliente;
    private Spinner sptipoti, spcadena;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.activity_mapa_cd, parent, false);
            SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();

    /*    getChildFragmentManager()
                .beginTransaction()
                .add(R.id.mapContainer, supportMapFragment)
                .commit();

        supportMapFragment.getMapAsync(this::onMapReady);*/

            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapcd_container);
            lcviewModel = new ViewModelProvider(this).get(ListaDetalleViewModel.class);

            mapFragment.getMapAsync(this);
           // spclientes=view.findViewById(R.id.spmcdcliente);
            spplantas=view.findViewById(R.id.spmcdplanta);
            spcadena=view.findViewById(R.id.spmccadenati);
            sptipoti=view.findViewById(R.id.spmctipoti);
        //    spindicefin=view.findViewById(R.id.spmcdindicefin);
        //    spindiceini=view.findViewById(R.id.spmcdindiceini);
             btncancel=view.findViewById(R.id.btnmccancel);
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
                                       /*  DescripcionGenerica plantasel=(DescripcionGenerica)spplantas.getSelectedItem();
                                         if(plantasel!=null) {
                                             int planta = plantasel.id;
                                             //  String indiceini = (String) spindiceini.getSelectedItem();
                                             //  String indicefin = (String) spindicefin.getSelectedItem();
                                             String indiceini = Constantes.INDICEACTUAL;
                                             buscarTiendas(planta, indiceini);
                                         }*/
                                         }
                                     })
                                     .setNegativeButton(R.string.no, null)
                                     .show();
                         }
                     }
                 }
             });
            buscarClientes();
            buscarPlantas(Constantes.CIUDADTRABAJO,0);
            cargarCatalogos();
          //  cargarIndices();
            Button btnbuscar=view.findViewById(R.id.btnmcdbuscar);
            btnbuscar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DescripcionGenerica plantasel=(DescripcionGenerica)spplantas.getSelectedItem();
                    if(plantasel!=null) {
                        int planta = plantasel.id;
                      //  String indiceini = (String) spindiceini.getSelectedItem();
                      //  String indicefin = (String) spindicefin.getSelectedItem();
                        String indiceini=Constantes.INDICEACTUAL;
                        //calculo indice fin
                        buscarTiendas(planta, indiceini);
                    }
                    else
                        irAcdSel();

                }
            });
            Button btnvatienda=view.findViewById(R.id.btnmcdnvati);
            btnvatienda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   /* FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                    AbririnformeFragment fragconfig=new AbririnformeFragment();
                    ft.add(R.id.nav_host_fragment, fragconfig);

                    ft.commit();*/
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("nuevatienda",true);
                  //  bundle.putString("ciudadNombre", listaSeleccionable.get(i).getNombre());
                    NavController nav= NavHostFragment.findNavController(MapaCdFragment.this);
                    Log.d(TAG,nav.getCurrentDestination().getId() +"--"+R.id.nav_tiendas);
                    if (nav.getCurrentDestination().getId() == R.id.nav_tiendas) {

                        nav.navigate(R.id.action_buscartonuevo, bundle);
                        //  NavHostFragment.findNavController(this).navigate(R.id.action_ciudadtohome);
                    }
                }
            });
            //inicio colores tienda
            coloresTienda=new HashMap<>();
        coloresTienda.put("3",BitmapDescriptorFactory.HUE_GREEN);//verde
        coloresTienda.put("2",BitmapDescriptorFactory.HUE_YELLOW);//amarillo
        coloresTienda.put("1",BitmapDescriptorFactory.HUE_RED);
            return  view;

        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng mex = new LatLng(19.36884,  -99.16410);

            mMap = googleMap;
            mMap.setOnMarkerClickListener(this);
            mMap.setOnInfoWindowClickListener(this);
            if (ContextCompat.checkSelfPermission( getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // Mostrar diálogo explicativo
                } else {
                    // Solicitar permiso
                    ActivityCompat.requestPermissions(
                            getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_REQUEST_CODE);
                }
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mex, 4));

            mMap.getUiSettings().setZoomControlsEnabled(true);

        }


        @SuppressLint("MissingPermission")
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == LOCATION_REQUEST_CODE) {
                // ¿Permisos asignados?
                if (permissions.length > 0 &&
                        permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                } else {
                    Toast.makeText(getContext(), "Error de permisos", Toast.LENGTH_LONG).show();
                }

            }
        }

        @Override
        public boolean onMarkerClick(final Marker marker) {
       // if (marker.equals(markerPais)) {
           /* FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            AbririnformeFragment fragconfig=new AbririnformeFragment();
            ft.add(R.id.nav_host_fragment, fragconfig);

            ft.commit();*/

       // }
            btncancel.setVisibility(View.VISIBLE);
            markerSel=marker;
          /*  long currentClickTime= SystemClock.elapsedRealtime();
            // preventing double, using threshold of 1000 ms
            if (currentClickTime - lastClickTime < 3000){
             return false;
            }

            lastClickTime = currentClickTime;
            Log.d(TAG,"di click :("+lastClickTime);*/



            return false;
        }

   /* @Override
    public boolean onMarkerClick(final Marker marker) {
        //if (marker.equals(markerPais)) {
           /* FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            AbririnformeFragment fragconfig=new AbririnformeFragment();
            ft.add(R.id.nav_host_fragment, fragconfig);

            ft.commit();*/

       /* if (doubleBackToExitPressedOnce) {
            Tienda tienda=(Tienda)marker.getTag();

            Bundle bundle = new Bundle();
            bundle.putBoolean("nuevatienda",false);
            bundle.putInt("idtienda", tienda.getUne_id());
            bundle.putString("nombretienda", tienda.getUne_descripcion());
            bundle.putInt("tipotienda", tienda.getUne_tipotienda());
            bundle.putString("direccion", tienda.getUne_direccion());
            bundle.putString("color", tienda.getColor());
            this.doubleBackToExitPressedOnce = false;
            NavHostFragment.findNavController(MapaCdFragment.this).navigate(R.id.action_buscartonuevo,bundle);
        } else {
            this.doubleBackToExitPressedOnce = true;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }*/

       /*     mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()), new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    Intent intent = new Intent(MapaCdActivity.this, MarkerDetailActivity.class);
                    intent.putExtra(EXTRA_LATITUD, marker.getPosition().latitude);
                    intent.putExtra(EXTRA_LONGITUD, marker.getPosition().longitude);
                    startActivity(intent);
                }

                @Override
                public void onCancel() {

                }
            });

            return true;
        }*/
       /* return false;
    }*/


    public void dibujarZonas(List<Geocerca> zonas){
            regionPolygon=new ArrayList<Polygon>();



            //guardar zonas
            GeocercaRepositoryImpl georep=new GeocercaRepositoryImpl(getActivity());

            for(Geocerca geo:zonas){
                //georep.insert(geo);
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
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(p4, 12));


                }
            }

        }
        public void buscarTiendas( int planta,String indicefin){
            //peticion al servidor
            //cambio el inice
            PeticionMapaCd petmap=new PeticionMapaCd(Constantes.CLAVEUSUARIO);
            //usaria la ciudad de trabajo
            //25guadalajara
            markerSel=null;
          //  Log.d(TAG,"ocultando"+btncancel.getVisibility());
            btncancel.setVisibility(View.GONE);
            String ffin= "";
            ffin= ComprasUtils.indiceaFecha2(indicefin);
            String fini="";
            //calculo el fin
            String[] aux1=indicefin.replace(".","-").split("-");
            String anio=aux1[1];
            int anioant=Integer.parseInt(anio)-1;
            String indiceini=aux1[0]+"."+anioant;
            fini=ComprasUtils.indiceaFecha2(indiceini);
            //busco el pais y cd de la planta
            int[] aux =lcviewModel.buscarClienCdxPlan(planta);
             cliente=aux[0];
            int ciudad=aux[1];
          //  Log.d(TAG,"--"+0+"--"+ciudad+"..."+planta+".."+cliente+"--"+fini+","+ffin);
            int tipo=((CatalogoDetalle)sptipoti.getSelectedItem()).getCad_idopcion();
            int cadena=((CatalogoDetalle)spcadena.getSelectedItem()).getCad_idopcion();
            petmap.getTiendas("0",ciudad+"",planta,cliente,fini,ffin,tipo+"",cadena+""); //se agregarian filtros despues
            // Log.d(TAG,"--"+pais+"--"+ciudad+"..."+planta+".."+cliente);
            //petmap.getTiendas("1","1",25,4,"2022-01-01","2022-04-01","",""); //se agregarian filtros despues
            this.listatiendas=petmap.getListatiendas();
            this.listageocercas=petmap.getListageocercas();
            //observo
            this.listatiendas.observe(this, new Observer<List<Tienda>>() {
                @Override
                public void onChanged(List<Tienda> tiendas) {
                   // if(tiendas!=null&&tiendas.size()>0) {


                        dibujarTiendas(tiendas);
                    //}
                    //else
                    //pongo anuncio sin tiendas
                    //{

                    //}
                }
            });
            this.listageocercas.observe(this, new Observer<List<Geocerca>>() {
                @Override
                public void onChanged(List<Geocerca> zonas) {
                    if(zonas!=null&&zonas.size()>0) {

                        dibujarZonas(zonas);
                    }
                }
            });

        }

        public void dibujarTiendas(List<Tienda> listiendas){
            martiendas=new ArrayList<>();
            LatLng japon2 = null;
            String color="1";
            mMap.clear();
            if(listiendas!=null)
            for(Tienda tienda: listiendas){
                 Log.d(TAG,tienda.getUne_id()+"--"+tienda.getEstpep()+tienda.getUne_descripcion()+".."+tienda.getEstele()+".."+tienda.getEstpen());

                if(cliente==4&&tienda.getEstpep()>0) {
                    color = tienda.getEstpep() + "";
                    tienda.setColor(color);
                }else
                    if(cliente==5&&tienda.getEstpen()>0) {

                        color = tienda.getEstpen() + "";
                        tienda.setColor(color);
                    }
                    else
                    if(cliente==6&&tienda.getEstele()>0) {
                        color = tienda.getEstele()+"";
                        tienda.setColor(color);
                        // Log.d(TAG,"--"+tienda.getUne_descripcion()+tienda.getCiudad()+".."+tienda.getUne_descripcion());

                    } else if(cliente==7&&tienda.getEstjum()>0) {

                        color = tienda.getEstjum() + "";
                        tienda.setColor(color);
                    }
                    //para poner en que tiendas puedo comprar
                String estatusClientes="";
                    //el estatus es 1-rojo, 2 amarillo, 3.verde solo en verde puedo comprar
                if(tienda.getEstpep()==3) {
                    estatusClientes=estatusClientes+"PEPSI, ";
                }
                if(tienda.getEstpen()==3) {

                    estatusClientes=estatusClientes+"PEÑAFIEL, ";
                }

                if(tienda.getEstele()==3) {
                    estatusClientes=estatusClientes+"ELECTROPURA, ";
                }  if(tienda.getEstjum()==3) {

                    estatusClientes=estatusClientes+"JUMEX, ";
                }
                if(estatusClientes.length()>0){
                    estatusClientes=estatusClientes.substring(0,estatusClientes.length()-2);
                }
                    // Log.d(TAG,"--"+tienda.getUne_descripcion()+tienda.getCiudad()+".."+tienda.getUne_descripcion());
                    if (tienda.getUne_coordenadasxy() != null && tienda.getUne_coordenadasxy().length() > 0) {
                        String[] aux = tienda.getUne_coordenadasxy().split(",");

                        japon2 = new LatLng(Double.parseDouble(aux[0]), Double.parseDouble(aux[1]));
                        MarkerOptions moptions=new MarkerOptions();
                        moptions.position(japon2)
                                .title(tienda.getUne_descripcion())
                                .icon(BitmapDescriptorFactory.defaultMarker(coloresTienda.get(color)));
                        if(estatusClientes.length()>0) {
                            moptions.snippet(estatusClientes);
                        }
                        Marker marker=mMap.addMarker(moptions
                                );
                        marker.setTag(tienda);

                        martiendas.add(marker);

                }

                }
            if(japon2!=null)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(japon2,10));
        }
        public void buscarClientes(){
         //   Log.d(TAG,"cd "+Constantes.CIUDADTRABAJO);
           // Constantes.CIUDADTRABAJO=null;
            if(Constantes.CIUDADTRABAJO==null||Constantes.CIUDADTRABAJO.equals("")){

               // Constantes.CIUDADTRABAJO="CIUDAD DE MEXICO";
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
        public void buscarPlantas(String ciudadNombre,int clienteSel){
            //para buscar las plantas

            LiveData<List<ListaCompra>> listacomp = lcviewModel.cargarPestañasEta(ciudadNombre, clienteSel);

            // Create the observer which updates the UI.
            final Observer< List<ListaCompra>> nameObserver = new Observer< List<ListaCompra>>() {
                @Override
                public void onChanged(@Nullable List<ListaCompra> lista) {

                    convertirLista(lista);
                    // setLista(listaClientesEnv);
                    // siguiente(0);
                    //  Log.d(TAG,"------- "+lista.size());
                    if(lista.size()>0) {
                        //cargo el spinner
                        CreadorFormulario.cargarSpinnerDescr(getActivity(),spplantas,listaPlantasEnv);
                    }
                    else
                        Log.d(TAG,"algo salió mal con la consulta de listas");
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

        public void cargarIndices(){
            String[] indiceslist={"SEPTIEMBRE 2021","OCTUBRE 2021","NOVIEMBRE 2021","DICIEMBRE 2021","ENERO 2022","FEBRERO 2022","MARZO 2022","ABRIL 2022","MAYO 2022","JUNIO 2022","JULIO 2022","AGOSTO 2022"};
            ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,indiceslist);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ArrayAdapter aa2 = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,indiceslist);
            aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
          //  spindiceini.setAdapter(aa);
           // spindicefin.setAdapter(aa);
            //spindiceini.setSelection(0);
            //spindicefin.setSelection(7);
        }



    public MapaCdFragment() {
    }

    public static MapaCdFragment newInstance() {
        return new MapaCdFragment();
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
            Log.d(TAG,"ocultando++"+btncancel.getVisibility());
        btncancel.setVisibility(View.GONE);
        markerSel=null;
    }
}