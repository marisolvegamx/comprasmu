package com.example.comprasmu.ui.tiendas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
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
import com.example.comprasmu.ui.envio.NvoEnvioFragment;
import com.example.comprasmu.ui.informedetalle.NuevoDetalleViewModel;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.ui.visita.AbririnformeFragment;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;
import com.google.android.gms.location.LocationRequest;
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

/*de acuerdo ala ubicacion del recolector se mostraràn las tiendas cerca*/
public class MapaSugFragment extends Fragment implements OnMapReadyCallback ,GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener  {
    public static final String EXTRA_LATITUD = "extra_latitud";
    public static final String EXTRA_LONGITUD ="extra_longitud" ;
    private MapaSugFragment mFirstMapFragment;
    private static final int LOCATION_REQUEST_CODE = 1;
    private NuevoDetalleViewModel dViewModel;
    private GoogleMap mMap;
    String[] coloreszon={"#1E90FF","#FF1493", "#32CD32", "#FF8C00", "#4B0082"};

    private  final String TAG="MapaSugFragment";

    MutableLiveData<List<Tienda>> listatiendas;

    List<Marker> martiendas;

    boolean doubleBackToExitPressedOnce = false;
    ListaDetalleViewModel lcviewModel;

    Marker markerSel;
    Location ultimaLoc;
    private final long lastClickTime = 0;


    List<DescripcionGenerica>clientesAsignados;

    private int cliente;

    LocationManager mlocManager;
    Localizacion Local;
    LocationRequest locationRequest;
    String provedorgps;
    float colorTienda;
    FragmentContainerView mapa;
    ProgressBar progresbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.activity_mapa_sug, parent, false);
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapcd_container);
            lcviewModel = new ViewModelProvider(this).get(ListaDetalleViewModel.class);

            mapFragment.getMapAsync(this);
             mapa=view.findViewById(R.id.mapcd_container);
            mapa.setVisibility(View.GONE);
             progresbar=view.findViewById(R.id.progressBarmap);
            progresbar.setVisibility(View.VISIBLE);
        colorTienda=BitmapDescriptorFactory.HUE_GREEN;
        locationStart();
          //  coloresTienda=new HashMap<>();
        //coloresTienda.put("3",BitmapDescriptorFactory.HUE_GREEN);//verde
       // coloresTienda.put("2",BitmapDescriptorFactory.HUE_YELLOW);//amarillo
      //  coloresTienda.put("1",BitmapDescriptorFactory.HUE_RED);
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
       // btncancel.setVisibility(View.VISIBLE);
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
        @Override
        public void onInfoWindowClick(final Marker marker) {
            Tienda tienda=(Tienda)marker.getTag();
            if(tienda.getColor()=="2"&&clientesAsignados.size()==0){
                return ;
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
            NavHostFragment.findNavController(MapaSugFragment.this).navigate(R.id.action_mapatonuevo,bundle);

        }




    private void locationStart() {

        mlocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Local = new Localizacion();


        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
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
        if (mlocManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)) {
            mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, Local);
            provedorgps = LocationManager.NETWORK_PROVIDER;
            Log.d(TAG, "3");

        } else  if (mlocManager.getAllProviders().contains(LocationManager.GPS_PROVIDER)) {
            //  if (Local == null) { //Validación que evita NullPointerException
            //Requiere actualización
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, Local);
            provedorgps = LocationManager.GPS_PROVIDER;
            // }
            Log.d(TAG, "4");
        } else
            Toast.makeText(getActivity(), "No hay gps?", Toast.LENGTH_SHORT).show();
        //todo quitar era para pruebas
          mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, Local);
          provedorgps = LocationManager.GPS_PROVIDER;
        Log.d(TAG,"quedo esta "+ provedorgps);
        // }

    }


        public void buscarTiendas( String ciudad,String indicefin){
            //peticion al servidor
            //cambio el inice
            PeticionMapaCd petmap=new PeticionMapaCd(Constantes.CLAVEUSUARIO);
            //usaria la ciudad de trabajo

            markerSel=null;
            String ffin= "";
            ffin= ComprasUtils.indiceaFecha2(indicefin);
            String fini="";
            //calculo el fin
            String[] aux1=indicefin.replace(".","-").split("-");
            String anio=aux1[1];
            int anioant=Integer.parseInt(anio)-1;
            String indiceini=aux1[0]+"."+anioant;
            fini=ComprasUtils.indiceaFecha2(indiceini);
            //petmap.getTiendasRadio("0",ciudad+"",fini,ffin+""); //se agregarian filtros despues
            petmap.getTiendas("0",ciudad+"",25,cliente,fini,ffin,0+"",""); //se agregarian filtros despues
            //
            this.listatiendas=petmap.getListatiendas();

            //observo
            this.listatiendas.observe(this, new Observer<List<Tienda>>() {
                @Override
                public void onChanged(List<Tienda> tiendas) {

                        dibujarTiendas(tiendas);

                }
            });

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
    public void mostrarPosicion(Location location){
        String latitude = String.valueOf(location.getLatitude());
        String longitude = String.valueOf(location.getLongitude());
       // txtubicacion.setText(latitude + "," + longitude);
        ultimaLoc=location;
        //  mViewModel.visita.setGeolocalizacion(latitude + "," + longitude);
        Log.d(TAG,"****Ya tengo la ubicacion" + latitude + "," + longitude);
        String indiceini=Constantes.INDICEACTUAL;
        //calculo indice fin
        String ciudad=Constantes.CIUDADTRABAJO;
        buscarTiendas(ciudad, indiceini);
        mapa.setVisibility(View.VISIBLE);
      //  progresbar=view.findViewById(R.id.progressBarmap);
        progresbar.setVisibility(View.GONE);
    }
        public void dibujarTiendas(List<Tienda> listiendas){
            martiendas=new ArrayList<>();
            LatLng japon2 = null;
            String color="1";
            mMap.clear();
            if(listiendas!=null)
            for(Tienda tienda: listiendas){
              //   Log.d(TAG,tienda.getUne_id()+"--"+tienda.getEstpep()+tienda.getUne_descripcion()+".."+tienda.getEstele()+".."+tienda.getEstpen());

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
                        //todo es aqui meter un catch
                        try {
                            japon2 = new LatLng(Double.parseDouble(aux[0]), Double.parseDouble(aux[1]));
                            MarkerOptions moptions = new MarkerOptions();
                            moptions.position(japon2)
                                    .title(tienda.getUne_descripcion())
                                    .icon(BitmapDescriptorFactory.defaultMarker(colorTienda));
                            if (estatusClientes.length() > 0) {
                                moptions.snippet(estatusClientes);
                            }
                            Marker marker = mMap.addMarker(moptions
                            );
                            marker.setTag(tienda);

                            martiendas.add(marker);
                        }catch(NumberFormatException ex){
                            Log.e(TAG,"error de formato "+ex.getMessage()+"  "+tienda.getUne_descripcion());
                        }

                }

                }
            if(japon2!=null)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(japon2,10));
        }


    public static MapaSugFragment newInstance() {
        return new MapaSugFragment();
    }


   /* @Override
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
        NavHostFragment.findNavController(MapaSugFragment.this).navigate(R.id.action_buscartonuevo,bundle);
        //return false;
    }*/

  /*  @Override
    public void onInfoWindowClose(@NonNull Marker marker) {
        Log.d(TAG,"eto cuando es???????");
    }*/

    @Override
    public void onMapClick(@NonNull LatLng latLng) {

        markerSel=null;
    }
}