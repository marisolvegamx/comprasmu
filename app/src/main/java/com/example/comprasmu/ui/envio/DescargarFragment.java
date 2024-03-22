package com.example.comprasmu.ui.envio;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.comprasmu.R;
import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.modelos.Correccion;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.remote.RespInfEtapaResponse;
import com.example.comprasmu.data.remote.RespInformesResponse;
import com.example.comprasmu.databinding.DescargarEnvFragmentBinding;
import com.example.comprasmu.databinding.ListaSelecFragmentBinding;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.ui.preparacion.NvaPreparacionViewModel;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.ui.ListaSelecFragment;
import com.example.comprasmu.utils.ui.ListaSelecViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Primero selecciona el cliente y luego
 * el doc
 */
public class DescargarFragment extends Fragment {

    private ListaDetalleViewModel lcViewModel;
    private NvaPreparacionViewModel mViewModel;
    private ListaSelecViewModel lsViewModel;
    private  ArrayList<DescripcionGenerica> listaClientesEnv;
    private static final String TAG = "DescargarFragment";
    private int etapa=5;
    private DescargarEnvFragmentBinding mBinding;
    protected ListaSelecFragment.AdaptadorListas adaptadorLista;
    private ListView objetosLV;
    private TextView indicacion,textView;
    int clientesel;
    ProgressBar progressBar;
    protected ArrayList<DescripcionGenerica> listaSeleccionable;

    public DescargarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment DescargarEtiqFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DescargarFragment newInstance() {
        DescargarFragment fragment = new DescargarFragment();

        return fragment;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding= DataBindingUtil.inflate(inflater,
                R.layout.descargar_env_fragment, container, false);
        lsViewModel = new ViewModelProvider(this).get(ListaSelecViewModel.class);


        listaSeleccionable=new ArrayList<DescripcionGenerica>();
        // mViewModel.setLista( this.listaSeleccionable);
        //  mBinding.setViewModel(mViewModel);
        mBinding.setLifecycleOwner(this);
        objetosLV=mBinding.getRoot().findViewById(R.id.listaobjetos);
        objetosLV.setVisibility(View.GONE);

        indicacion=mBinding.textView9;
        return mBinding.getRoot();

    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(NvaPreparacionViewModel.class);
        lcViewModel = new ViewModelProvider(this).get(ListaDetalleViewModel.class);
        if(!isOnlineNet()) {
            notificarSinConexion();
            //  miproglis.todoBien(maininfoetaResp,maininfoResp,mainRespcor);

            return ;
        }
        if(Constantes.CIUDADTRABAJO==null||Constantes.CIUDADTRABAJO.equals("")){

            // Constantes.CIUDADTRABAJO="CIUDAD DE MEXICO";
            irAcdSel();
            return;
        }

        //hago la peticion
        PeticionesServidor ps=new PeticionesServidor(Constantes.CLAVEUSUARIO);
        DocsEnvioListener listener=new DocsEnvioListener();
        ps.getDocumentosEnvio(Constantes.INDICEACTUAL,Constantes.CIUDADTRABAJO,listener);


        getObjetosLV().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    clientesel=listaSeleccionable.get(i).getId();
                mBinding.lldeselcliente.setVisibility(View.GONE);
                   //mostrar ligas

                mBinding.lldeseldoc.setVisibility(View.VISIBLE);

            }
        });

        mBinding.btndeguia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                descargarPDF("g");
            }
        });
        mBinding.btndefda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                descargarPDF("fda");
            }
        });
        mBinding.btndefactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                descargarPDF("fac");
            }
        });
        mBinding.btndeanexo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                descargarPDF("anx1");
            }
        });
        mBinding.btndeanexo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                descargarPDF("anx2");
            }
        });
        mBinding.btndeanexo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                descargarPDF("anx3");
            }
        });

    }
    public void setLista(ArrayList<DescripcionGenerica> lista){
        for(DescripcionGenerica des:lista) {
            Log.d("ListaSelectFragment",des.getNombre()+"--"+ des.getDescripcion2());
        }
        this.listaSeleccionable=lista;
        lsViewModel.setLista( this.listaSeleccionable);

    }
    public void setIndicacion(String indicacion) {
        this.indicacion.setText(indicacion);
    }
    public void setupListAdapter() {
        adaptadorLista = new ListaSelecFragment.AdaptadorListas((AppCompatActivity) getActivity(),lsViewModel);

        objetosLV.setAdapter(adaptadorLista);

    }
    public ListView getObjetosLV() {
        return objetosLV;
    }

    private  void convertirLista(List<ListaCompra>lista){
        listaClientesEnv =new ArrayList<DescripcionGenerica>();
        for (ListaCompra listaCompra: lista ) {
            Log.d(TAG,listaCompra.getPlantaNombre());

            listaClientesEnv.add(new DescripcionGenerica(listaCompra.getClientesId(), listaCompra.getClienteNombre()));

        }

    }

    public void descargarPDF(String opcion){
        long archact;
       // String MY_URL = "http://192.168.1.84/comprasv1/imprimirReporte.php?admin=impetiq&indicelis="+ Constantes.INDICEACTUAL+"&rec="+Constantes.CLAVEUSUARIO+"&cli="+cliente+"&ciu="+Constantes.CIUDADTRABAJO;
        String MY_URL = Constantes.URLSERV+"descargarenv.php?doc="+opcion+"&indice="+ Constantes.INDICEACTUAL+"&cli="+clientesel+"&ciu="+Constantes.CIUDADTRABAJO+"&rec="+Constantes.CLAVEUSUARIO;
        Uri uri = Uri.parse(MY_URL); // Path where you want to download file.
        // registrer receiver in order to verify when download is complete
        //  registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
     //   Toast.makeText(getContext(),cliente+"", Toast.LENGTH_LONG).show();

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);  // Tell on which network you want to download file.
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("DESCARGA DOCUMENTOS"); // Title for notification.
        // request.setVisibleInDownloadsUi(true);
        // request.setTitle("DESCARGA ETIQUETAS");
      //  Log.d(TAG,"hola"+MY_URL);

        request.setDestinationInExternalFilesDir(getActivity(), Environment.DIRECTORY_PICTURES, "etiquetas.pdf");  // Storage directory path
        archact=((DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request); // This will start downloading
        // return 0;
    }
    public void irAcdSel(){
        NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        if(navController!=null)
            navController.navigate(R.id.action_descetitocdtrab);

    }
    public void notificarSinConexion(){
        mBinding.txtdemensaje.setText("Esta acción requiere conexión a Internet, verifique");
        mBinding.txtdemensaje.setVisibility(View.VISIBLE);
    }
    public static Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }
    public class DocsEnvioListener {
        //  void cerrarAlerta(boolean res);
        public void mostrarBotones(DocumentosEnvio docsenvio){
            mBinding.deprogressBar3.setVisibility(View.GONE);
            mBinding.btndefda.setVisibility(View.GONE);
            mBinding.btndefactura.setVisibility(View.GONE);
            mBinding.btndeanexo1.setVisibility(View.GONE);
            mBinding.btndeanexo2.setVisibility(View.GONE);
            mBinding.btndeanexo3.setVisibility(View.GONE);
            if(docsenvio==null){
                mBinding.txtdemensaje.setText(R.string.empty_state_text);
                mBinding.txtdemensaje.setVisibility(View.VISIBLE);
                return;

            }
            mBinding.lldeselcliente.setVisibility(View.GONE);
            mBinding.lldeseldoc.setVisibility(View.GONE);
            setIndicacion(getString(R.string.seleccione_cliente));
            //busco si tengo varios clientes x ciudad

            List<ListaCompra> listainfetiq;
            listainfetiq = lcViewModel.cargarClientesSimplxet(Constantes.CIUDADTRABAJO, etapa);
            Log.d(TAG, "id nuevo" + mViewModel.getIdNuevo() + "--" + listainfetiq.size());

            if(listainfetiq.size()>0) {

                convertirLista(listainfetiq);

                setLista(listaClientesEnv);
                setupListAdapter();

            }
            else
                Log.d(TAG,"algo salió mal con la consulta de listas");
            //ponemos botones
            if(docsenvio.getFda()==1)
                mBinding.btndefda.setVisibility(View.VISIBLE);
            if(docsenvio.getRecoleccion()==1)
                mBinding.btndefactura.setVisibility(View.VISIBLE);
            if(docsenvio.getAnexo1()==1)
                mBinding.btndeanexo1.setVisibility(View.VISIBLE);
            if(docsenvio.getAnexo2()==1)
                mBinding.btndeanexo2.setVisibility(View.VISIBLE);
            if(docsenvio.getAnexo3()==1)
                mBinding.btndeanexo3.setVisibility(View.VISIBLE);
        }
        //  void estatusInf(int es);
        //  void estatusLis(int es);
        // void imagenesEtapa(RespInfEtapaResponse infoResp);


    }
}