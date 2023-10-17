package com.example.comprasmu.utils.ui;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.comprasmu.R;
import com.example.comprasmu.utils.Constantes;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DescargarDocsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DescargarDocsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button btndesc;
    public DescargarDocsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DescargarDocsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DescargarDocsFragment newInstance(String param1, String param2) {
        DescargarDocsFragment fragment = new DescargarDocsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root= inflater.inflate(R.layout.lista_generic_fragment, container, false);
     //   btndesc = (Button) root.findViewById(R.id.btndddescargar);
        btndesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descargarPDF2();
            }
        });
        return root;
    }


    public void descargarPDF(){
       // http://localhost/comprasv1/
        String MY_URL = "http://192.168.1.84/comprasv1/comprasv1/imprimirReporte.php?admin=impetiq&idmes=8.2023&idrec=42&idplan=29&numcaja=1";

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MY_URL)));
    }

    public void descargarPDF2(){
        long archact;
        String MY_URL = "http://192.168.1.84/comprasv1/imprimirReporte.php?admin=impetiq&idmes="+ Constantes.INDICEACTUAL+"&idrec="+Constantes.CLAVEUSUARIO+"&idplan=29&numcaja=1";

        Uri uri = Uri.parse(MY_URL); // Path where you want to download file.
        // registrer receiver in order to verify when download is complete
        //  registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

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