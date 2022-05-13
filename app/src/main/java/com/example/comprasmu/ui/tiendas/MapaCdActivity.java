package com.example.comprasmu.ui.tiendas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.Geocerca;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.Tienda;
import com.example.comprasmu.ui.informe.NuevoinformeViewModel;
import com.example.comprasmu.ui.informedetalle.NuevoDetalleViewModel;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapaCdActivity extends FragmentActivity implements OnMapReadyCallback , GoogleMap.OnMarkerClickListener {
    public static final String EXTRA_LATITUD = "extra_latitud";
    public static final String EXTRA_LONGITUD ="extra_longitud" ;
    private MapaCdFragment mFirstMapFragment;
    private static final int LOCATION_REQUEST_CODE = 1;
    private NuevoDetalleViewModel dViewModel;
    private GoogleMap mMap;
    String[] coloreszon={"#1E90FF","#FF1493", "#32CD32", "#FF8C00", "#4B0082"};
    Map<String,Float> coloresTienda;
    private  final String TAG="MapaCdActivity";
    private  ArrayList<DescripcionGenerica> listaPlantasEnv;
    MutableLiveData<List<Tienda>> listatiendas;
  //  MutableLiveData<List<Tienda>> listatiendasRojas;
  //  MutableLiveData<List<Tienda>> listatiendasAmarillas;
    MutableLiveData<List<Geocerca>> listageocercas;
    List<Polygon> regionPolygon;
    List<Marker> martiendas;
    int cdId;
    String nombreCd;
    ListaDetalleViewModel lcviewModel;
    private NuevoinformeViewModel mViewModel;
    Spinner spclientes;
    Spinner spplantas;
    Spinner spindiceini;
    Spinner spindicefin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_cd);
       /* mFirstMapFragment = MapaCdFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()

               .add(R.id.map_container,mFirstMapFragment)
                .commit();*/
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapcd_container);
        lcviewModel = new ViewModelProvider(this).get(ListaDetalleViewModel.class);

        mapFragment.getMapAsync(this);
      //  spclientes=findViewById(R.id.spmcdcliente);
        spplantas=findViewById(R.id.spmcdplanta);
        //spindicefin=findViewById(R.id.spmcdindicefin);
       // spindiceini=findViewById(R.id.spmcdindiceini);
        buscarClientes();
        buscarPlantas(Constantes.CIUDADTRABAJO,0);
        cargarIndices();
        Button btnbuscar=findViewById(R.id.btnmcdbuscar);
        btnbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DescripcionGenerica plantasel=(DescripcionGenerica)spplantas.getSelectedItem();
                int planta=plantasel.id;
                String indiceini=(String)spindiceini.getSelectedItem();
                String indicefin=(String)spindicefin.getSelectedItem();

                buscarTiendas(planta,0,indiceini,indicefin);
            }
        });
        Button btnvatienda=findViewById(R.id.btnmcdnvati);
        btnvatienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO envio al fragmento de nueva tienda
            }
        });
        //inicio colores tienda
        coloresTienda=new HashMap<>();
        coloresTienda.put("verde",BitmapDescriptorFactory.HUE_GREEN);
        coloresTienda.put("amarillo",BitmapDescriptorFactory.HUE_YELLOW);
        coloresTienda.put("rojo",BitmapDescriptorFactory.HUE_RED);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng cali = new LatLng(3.4383, -76.5161);

        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        if (ContextCompat.checkSelfPermission( this,Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                   Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Mostrar diálogo explicativo
            } else {
                // Solicitar permiso
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST_CODE);
            }
        }

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
                Toast.makeText(this, "Error de permisos", Toast.LENGTH_LONG).show();
            }

        }
    }
    @Override
    public boolean onMarkerClick(final Marker marker) {
       /* if (marker.equals(markerPais)) {
         /*   Intent intent = new Intent(this, MarkerDetailActivity.class);
            intent.putExtra(EXTRA_LATITUD, marker.getPosition().latitude);
            intent.putExtra(EXTRA_LONGITUD, marker.getPosition().longitude);

            startActivity(intent);*/
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
        return false;
    }

    public void dibujarZonas(List<Geocerca> zonas){
         regionPolygon=new ArrayList<Polygon>();

      //  LatLng p1; LatLng p2;    LatLng p3 ;  LatLng p4;
        // Ejemplo: Encerrar a Cuba con un polígono de bajo detalle
     /*   LatLng px1 = new LatLng(21.88661065803621, -85.01541511562505);
        LatLng px2 = new LatLng(22.927294359193038, -83.76297370937505);
        LatLng px3 = new LatLng(23.26620799401109, -82.35672370937505);
        LatLng px4 = new LatLng(23.387267854439315, -80.79666511562505);
*/
     /*   Geocerca geo=zonas.get(0);
        Log.e("MapCd",geo.getGeo_p1()+"--"+geo.getGeo_p2()+"--"+geo.getGeo_p3()+"--"+geo.getGeo_p4());
        String aux[]=geo.getGeo_p1().split(",");

        LatLng px1 = new LatLng(Double.parseDouble(aux[0]), Double.parseDouble(aux[1]));

        String aux2[]=geo.getGeo_p2().split(",");
        LatLng px2 =new LatLng(Double.parseDouble(aux2[0]), Double.parseDouble(aux2[1]));
        String aux3[]=geo.getGeo_p3().split(",");
        LatLng px3 =new LatLng(Double.parseDouble(aux3[0]), Double.parseDouble(aux3[1]));
        String aux4[]=geo.getGeo_p4().split(",");
        LatLng px4 =new LatLng(Double.parseDouble(aux4[0]), Double.parseDouble(aux4[1]));
        mMap.addMarker(new MarkerOptions()
                .position(px1)
                .title("p1")
                //TODO poner el color
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        mMap.addMarker(new MarkerOptions()
                .position(px2)
                .title("p2")
                //TODO poner el color
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        mMap.addMarker(new MarkerOptions()
                .position(px3)
                .title("p3")
                //TODO poner el color
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        mMap.addMarker(new MarkerOptions()
                .position(px4)
                .title("p4")
                //TODO poner el color
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        Polygon cubaPolygon = mMap.addPolygon(new PolygonOptions()
                .add(px1, px2, px3, px4)
                .strokeColor(Color.parseColor("#AB47BC"))
                .fillColor(Color.parseColor("#7B1FA2")));
*/
        for(Geocerca geo:zonas){
            String aux[]=geo.getGeo_p1().split(",");
            LatLng p1 = new LatLng(Double.parseDouble(aux[0]), Double.parseDouble(aux[1]));
            String aux2[]=geo.getGeo_p2().split(",");
            LatLng p2 =new LatLng(Double.parseDouble(aux2[0]), Double.parseDouble(aux2[1]));
            String aux3[]=geo.getGeo_p3().split(",");
            LatLng p3 =new LatLng(Double.parseDouble(aux3[0]), Double.parseDouble(aux3[1]));
            String aux4[]=geo.getGeo_p4().split(",");
            LatLng p4 =new LatLng(Double.parseDouble(aux4[0]), Double.parseDouble(aux4[1]));

            regionPolygon.add( mMap.addPolygon(new PolygonOptions()
                    .add(p1,p2,p3,p4)
                    .strokeColor(Color.parseColor(coloreszon[geo.getGeo_region()-1]))
                    ));
            //busco el centro para poner la camara
            if(geo.getGeo_region()==5){
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(p4, 12));


            }
        }

    }
    public void buscarTiendas( int planta, int cliente,String indiceini,String indicefin){
        //peticion al servidor

        PeticionMapaCd petmap=new PeticionMapaCd(Constantes.CLAVEUSUARIO);
        //usaria la ciudad de trabajo
        //25guadalajara
        String fini= "";
        fini=ComprasUtils.indiceaFecha(indiceini);
        String ffin="";
        ffin=ComprasUtils.indiceaFecha(indicefin);
        //busco el pais y cd de la planta
        int aux[]=lcviewModel.buscarClienCdxPlan(planta);
        int pais=aux[0];
        int ciudad=aux[1];
        Log.d(TAG,"--"+pais+"--"+ciudad+"..."+planta+".."+cliente);

       petmap.getTiendas(pais+"",ciudad+"",planta,0,fini,ffin,"",""); //se agregarian filtros despues
       // Log.d(TAG,"--"+pais+"--"+ciudad+"..."+planta+".."+cliente);
        //petmap.getTiendas("1","1",25,4,"2022-01-01","2022-04-01","",""); //se agregarian filtros despues
        this.listatiendas=petmap.getListatiendas();
        this.listageocercas=petmap.getListageocercas();
        //observo
        this.listatiendas.observe((LifecycleOwner) this, new Observer<List<Tienda>>() {
            @Override
            public void onChanged(List<Tienda> tiendas) {
                if(tiendas!=null&&tiendas.size()>0) {


                    dibujarTiendas(tiendas);
                }
                else
                //pongo anuncio sin tiendas
                {

                }
            }
        });
        this.listageocercas.observe((LifecycleOwner) this, new Observer<List<Geocerca>>() {
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
        for(Tienda tienda: listiendas){
            Log.d(TAG,"--"+tienda.getUne_descripcion()+tienda.getCiudad()+".."+tienda.getUne_descripcion());
            if(tienda.getUne_coordenadasxy()!=null&&tienda.getUne_coordenadasxy().length()>0) {
                String aux[] = tienda.getUne_coordenadasxy().split(",");

                 japon2 = new LatLng(Double.parseDouble(aux[0]), Double.parseDouble(aux[1]));
                martiendas.add(mMap.addMarker(new MarkerOptions()
                        .position(japon2)
                        .title(tienda.getUne_descripcion())
                        .icon(BitmapDescriptorFactory.defaultMarker(coloresTienda.get(tienda.getColor())))));

            }
        }
        if(japon2!=null)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(japon2,10));
    }
    public void buscarClientes(){
        Log.d(TAG,"cd "+Constantes.CIUDADTRABAJO);
        if(Constantes.CIUDADTRABAJO==null||Constantes.CIUDADTRABAJO.equals("")){
            //TODO enviar a elegir cd de trabajo
            Constantes.CIUDADTRABAJO="CIUDAD DE MEXICO";
        }
        List<ListaCompra> data=lcviewModel.cargarClientesSimpl(Constantes.CIUDADTRABAJO);

        Log.d(TAG, "regresó de la consulta de clientes " + data.size());
        List<DescripcionGenerica>clientesAsignados = convertirListaaClientes(data);
        CreadorFormulario.cargarSpinnerDescr(this,spclientes,clientesAsignados);
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

        LiveData<List<ListaCompra>> listacomp = lcviewModel.cargarPestañas(ciudadNombre, clienteSel);

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
                    CreadorFormulario.cargarSpinnerDescr(MapaCdActivity.this,spplantas,listaPlantasEnv);
                }
                else
                    Log.d(TAG,"algo salió mal con la consulta de listas");
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        //   lcrepo.getClientesByIndiceCiudad(Constantes.INDICEACTUAL,ciudadNombre).observe(getViewLifecycleOwner(), nameObserver);
        listacomp.observe(this,nameObserver);

    }

   /* public void setupListAdapter() {
        adaptadorLista = new ListaSelecFragment.AdaptadorListas((AppCompatActivity) getActivity(),mViewModel);

        objetosLV.setAdapter(adaptadorLista);

    }*/
    private  void convertirLista(List<ListaCompra>lista){
        listaPlantasEnv=new ArrayList<DescripcionGenerica>();
        for (ListaCompra listaCompra: lista ) {
          /*String tupla=Integer.toString(listaCompra.getClienteId())+";"+
          listaCompra.getPlantaNombre();*/

            listaPlantasEnv.add(new DescripcionGenerica(listaCompra.getPlantasId(), listaCompra.getClienteNombre()+" "+listaCompra.getPlantaNombre(),listaCompra.getClienteNombre()));

        }

    }

    public void cargarIndices(){
        String[] indiceslist={"","SEPTIEMBRE 2021","OCTUBRE 2021","NOVIEMBRE 2021","DICIEMBRE 2021","ENERO 2022","FEBRERO 2022","MARZO 2022","ABRIL 2022","MAYO 2022","JUNIO 2022","JULIO 2022","AGOSTO 2022"};
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,indiceslist);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter aa2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,indiceslist);
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spindiceini.setAdapter(aa);
        spindicefin.setAdapter(aa);
    }


}