package com.example.comprasmu.ui.empaque;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.comprasmu.R;
import com.example.comprasmu.ui.preparacion.NvaPreparacionViewModel;
import com.example.comprasmu.utils.Constantes;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DescargarEtiq2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DescargarEtiq2Fragment extends Fragment {




    private static final String TAG = "DescargarEtiq2Fragment";
    public static String ARG_TIPO="comprasmu.desceti.tipo";
    private String tipo;
    public DescargarEtiq2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment DescargarEtiqFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DescargarEtiq2Fragment newInstance() {
        DescargarEtiq2Fragment fragment = new DescargarEtiq2Fragment();

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {






            Bundle datosRecuperados = getArguments();

            if (datosRecuperados != null) {
                tipo = datosRecuperados.getString(ARG_TIPO);


            }
          return  inflater.inflate(R.layout.fragment_descargar_etiq, container, false);


    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {


        if(!tipo.equals(""))
            descargarArchivo();



    }

    public void descargarArchivo(){
        long archact;
       // String MY_URL = "http://192.168.1.84/comprasv1/imprimirReporte.php?admin=impetiq&indicelis="+ Constantes.INDICEACTUAL+"&rec="+Constantes.CLAVEUSUARIO+"&cli="+cliente+"&ciu="+Constantes.CIUDADTRABAJO;
        String MY_URL = Constantes.URLSERV+"etiquetas/"+tipo+".pdf";
        Uri uri = Uri.parse(MY_URL); // Path where you want to download file.
        // registrer receiver in order to verify when download is complete
        //  registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
     //   Toast.makeText(getContext(),cliente+"", Toast.LENGTH_LONG).show();

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);  // Tell on which network you want to download file.
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("DESCARGA ETIQUETAS"); // Title for notification.
        // request.setVisibleInDownloadsUi(true);
        // request.setTitle("DESCARGA ETIQUETAS");

        request.setDestinationInExternalFilesDir(getActivity(), Environment.DIRECTORY_PICTURES, "etiqueta_"+tipo+".pdf");  // Storage directory path
        archact=((DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request); // This will start downloading
        // return 0;
    }

}