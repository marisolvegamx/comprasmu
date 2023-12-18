package com.example.comprasmu.ui.empaque;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.ui.preparacion.NvaPreparacionViewModel;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.ui.ListaSelecFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DescargarEtiqFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DescargarEtiqFragment  extends ListaSelecFragment {


    private NvaPreparacionViewModel mViewModel;
    private  ArrayList<DescripcionGenerica> listaClientesEnv;
    private static final String TAG = "ListaSelecFragment";
    public DescargarEtiqFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment DescargarEtiqFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DescargarEtiqFragment newInstance() {
        DescargarEtiqFragment fragment = new DescargarEtiqFragment();

        return fragment;
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(NvaPreparacionViewModel.class);

        setIndicacion(getString(R.string.seleccione_cliente));
        //busco si tengo varios clientes x ciudad
        List<InformeEtapa> listainfetiq;
        listainfetiq = mViewModel.getClientesconInf(Constantes.INDICEACTUAL,Constantes.CIUDADTRABAJO);
        Log.d(TAG, "id nuevo" + mViewModel.getIdNuevo() + "--" + listainfetiq.size());

                if(listainfetiq.size()>0) {

                    convertirLista(listainfetiq);

                    setLista(listaClientesEnv);
                    setupListAdapter();




                }
                else
                    Log.d(TAG,"algo salió mal con la consulta de listas");


        getObjetosLV().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                   int cliente=listaSeleccionable.get(i).getId();
                descargarPDF2(cliente);

            }
        });

    }
    private  void convertirLista(List<InformeEtapa>lista){
        listaClientesEnv =new ArrayList<DescripcionGenerica>();
        for (InformeEtapa listaCompra: lista ) {

            listaClientesEnv.add(new DescripcionGenerica(listaCompra.getClientesId(), listaCompra.getClienteNombre()));

        }

    }
    public void descargarPDF2(int cliente){
        long archact;
        String MY_URL = "http://192.168.1.84/comprasv1/imprimirReporte.php?admin=impetiq&indicelis="+ Constantes.INDICEACTUAL+"&rec="+Constantes.CLAVEUSUARIO+"&cli="+cliente+"&ciu="+Constantes.CIUDADTRABAJO;

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

        request.setDestinationInExternalFilesDir(getActivity(), Environment.DIRECTORY_PICTURES, "etiquetas.pdf");  // Storage directory path
        archact=((DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request); // This will start downloading
        // return 0;
    }
}