package com.example.comprasmu.utils.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.SubirCorreccionTask;
import com.example.comprasmu.SubirInformeEtaTask;
import com.example.comprasmu.data.modelos.Correccion;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.modelos.SolicitudWithCor;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.remote.CorreccionEnvio;
import com.example.comprasmu.data.remote.InformeEtapaEnv;
import com.example.comprasmu.databinding.ListaInformesFragmentBinding;
import com.example.comprasmu.services.SubirFotoService;
import com.example.comprasmu.ui.BackActivity;
import com.example.comprasmu.ui.correccion.NvaCorreViewModel;
import com.example.comprasmu.ui.infetapa.ContInfEtapaFragment;
import com.example.comprasmu.ui.infetapa.SelClienteGenFragment;
import com.example.comprasmu.ui.listadetalle.ListaCompraFragment;
import com.example.comprasmu.ui.preparacion.NvaPreparacionViewModel;
import com.example.comprasmu.utils.Constantes;

import java.util.ArrayList;
import java.util.List;

/*******para informes x etapa y correcciones******/
public class ListaInformesEtaFragment extends Fragment implements InformeGenAdapter.AdapterCallback {
    public static final String INFORMESEL ="comprasmu.lie.informesel" ;
    public static final String ARG_TIPOCONS ="comprasmu.lie.tipocons" ;
    private InformesGenViewModel mViewModel;
    public static final String TAG = "ListaInfEtaFragment";
    private ListaInformesFragmentBinding mBinding;
    private InformeGenAdapter mListAdapter;
    private LiveData<List<InformeEtapa>> listainfs;
    private int clienteid;
    private String ciudad;
    private String indice;
    String clientesel;
    int plantasel;
    CoordinatorLayout coordinator;
    private int etapa;
    String tipocons; //e para etapa, action_selclitocor2 para correccion
    private NvaCorreViewModel corViewModel;
    NvaPreparacionViewModel npViewModel;
    public ListaInformesEtaFragment() {

    }

  /*  public  ListaInformesFragment(int planta, String onombrePlanta, String nomcliente) {
        //  ListaCompraFragment fragment = new ListaCompraFragment();
        plantaid=planta;
        plantasel=onombrePlanta;
        this.clientesel=nomcliente;

    }*/

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (getArguments() != null) {
            Log.e(TAG,"si hay"+getArguments().toString());
            tipocons = getArguments().getString(SelClienteGenFragment.ARG_TIPOCONS);
            plantasel=getArguments().getInt(ListaCompraFragment.ARG_PLANTASEL);
            etapa=getArguments().getInt(ContInfEtapaFragment.ETAPA);

          //  indice=getArguments().getString(Constantes.INDICEACTUAL);

        }
            indice = Constantes.INDICEACTUAL;


     //   Log.d(Constantes.TAG,"cliente y planta sel"+clienteid+"--"+plantaid);

        mBinding= DataBindingUtil.inflate(inflater,
                R.layout.lista_informes_fragment, container, false);

        mViewModel = new ViewModelProvider(this).get(InformesGenViewModel.class);
        corViewModel=new ViewModelProvider(this).get(NvaCorreViewModel.class);
         npViewModel = new ViewModelProvider(this).get(NvaPreparacionViewModel.class);

        setHasOptionsMenu(true);
        return    mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

       // mViewModel = new ViewModelProvider(this).get(ListaInformesGenViewModel.class);

      //  mBinding.setLcviewModel(mViewModel);
       // mBinding.setLifecycleOwner(this);
        coordinator=view.findViewById(R.id.coordinator3);

        setupListAdapter();

       cargarLista();
    }

    public void cargarLista(){
        //aqui hago la consulta de los informes x etapa
        //y de los informes correccion
        if(tipocons.equals("e")){
            // getActivity().getActionBar().setTitle("RESUMEN INFORMES");

             ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("RESUMEN INFORMES");
            Log.e(TAG,etapa+"--"+indice+"--"+plantasel);
            if(etapa==3)
            listainfs=mViewModel.cargarEtapaAll(etapa,indice);
            else
                listainfs=mViewModel.cargarEtapaAll(etapa,indice);
            listainfs.observe(getViewLifecycleOwner(), new Observer<List<InformeEtapa>>() {
                @Override
                public void onChanged(List<InformeEtapa> informeEtapas) {
                    if(informeEtapas.size()<1){
                        mBinding.emptyStateText.setVisibility(View.VISIBLE);
                    }
                    mListAdapter.setInformeCompraList(informeEtapas);
                    mListAdapter.notifyDataSetChanged();
                }
            });
        }
        if(tipocons.equals("rescor")||tipocons.equals("action_selclitocor2")){ //CORRECCIONES
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.correcciones);

            etapa=Constantes.ETAPAACTUAL;
            Log.e(TAG,etapa+"--"+indice+"--"+plantasel);
            LiveData<List<SolicitudWithCor>> listacor=null;
            if(etapa==2)
                 listacor = corViewModel.getCorreccionesxEta(etapa, indice, plantasel);

            else
                listacor = corViewModel.getCorreccionesxEtaPlan(etapa, indice, plantasel);
            listacor.observe(getViewLifecycleOwner(), new Observer<List<SolicitudWithCor>>() {
                @Override
                public void onChanged(List<SolicitudWithCor> correccions) {
                    if(correccions.size()<1){
                        mBinding.emptyStateText.setVisibility(View.VISIBLE);
                    }
                    //paso la correccion a informeetapa

                    mListAdapter.setInformeCompraList(correccionAInforme(correccions));
                    mListAdapter.notifyDataSetChanged();
                }
            });
        }



    }
    private void setupListAdapter() {
        mListAdapter = new InformeGenAdapter(this,tipocons);
        mBinding.detalleList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.detalleList.setHasFixedSize(true);
        mBinding.detalleList.setAdapter(mListAdapter);

    }


 /*   @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        menu.clear();
        inflater.inflate(R.menu.menu_listainforme, menu);
        //  super.onCreateOptionsMenu(menu, inflater);


    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_searchinforme:

                Intent intento1 = new Intent(getActivity(), FiltrarListaActivity.class);
                intento1.putExtra("filtrarlista.fragment","buscarinforme");
                startActivityForResult(intento1, FiltrarListaActivity.REQUEST_CODE_INFORME);

                return true;

            default:
                break;
        }

        return false;
    }
    private List<InformeEtapa> correccionAInforme(List<SolicitudWithCor> correccs){
        List<InformeEtapa> informes=new ArrayList<>();
        InformeEtapa temp;
        for(SolicitudWithCor cor:correccs){
            temp=new InformeEtapa();
            temp.setId(cor.correccion.getId());
            temp.setEtapa(cor.solicitud.getEtapa());
            temp.setIndice(cor.solicitud.getIndice());
            temp.setPlantasId(cor.solicitud.getPlantasId());
            temp.setPlantaNombre(cor.solicitud.getPlantaNombre());
            temp.setClienteNombre(cor.solicitud.getClienteNombre());
            temp.setClientesId(cor.solicitud.getClientesId());
            temp.setEstatus(cor.correccion.getEstatus());
            temp.setEstatusSync(cor.correccion.getEstatusSync());
            temp.setCreatedAt(cor.correccion.getCreatedAt());
            temp.setComentarios(cor.solicitud.getNombreTienda());
            temp.setConsecutivo(cor.solicitud.getNumFoto());
            informes.add(temp);

        }
        return informes;

    }
    @Override
    public void onClickVer(int informe) {
        Intent intento1=new Intent(getActivity(), BackActivity.class);
        intento1.putExtra(INFORMESEL, informe);
      //  intento1.putExtra(NuevoInfEtapaActivity.NUMFOTO, numFoto);
        intento1.putExtra(ARG_TIPOCONS, tipocons);
        Constantes.ETAPAACTUAL=etapa;
        if(etapa==4)
            intento1.putExtra(BackActivity.ARG_FRAGMENT,BackActivity.OP_VEREMPQ);
        else
            intento1.putExtra(BackActivity.ARG_FRAGMENT,BackActivity.OP_INFORMECOR);
        startActivity(intento1);

    }




    @Override
    public void onClickSubir(int informe, String tipo) {
        if(NavigationDrawerActivity.isOnlineNet()) {
            if(tipo.equals("e")) {

                InformeEtapaEnv informeEta=this.preparaInforme(informe);
                SubirInformeEtaTask miTareaAsincrona = new SubirInformeEtaTask(informeEta,getActivity());
                miTareaAsincrona.execute();
                subirFotos(getActivity(),informeEta);
                Log.d(TAG, "preparando informe**********");
             //   NuevoinformeFragment.subirFotos(getActivity(), informeenv);
            }else
            if(tipo.equals("action_selclitocor2")){//correccion
                //busco la correccion x el id
                Correccion corrsel=corViewModel.getCorreccionesxid(informe,Constantes.INDICEACTUAL,Constantes.ETAPAACTUAL);
                CorreccionEnvio envio=corViewModel.prepararEnvio(corrsel);
                SubirCorreccionTask miTareaAsincrona = new SubirCorreccionTask(envio,getActivity());
                miTareaAsincrona.execute();

                subirFotosCor(getActivity(),envio.getCorreccion().getId(),envio.getCorreccion().getRuta_foto1());


                if(envio.getCorreccion().getRuta_foto2()!=null&&envio.getCorreccion().getRuta_foto2().length()>1)
                    subirFotosCor(getActivity(),envio.getCorreccion().getId(),envio.getCorreccion().getRuta_foto2());
                if(envio.getCorreccion().getRuta_foto3()!=null&&envio.getCorreccion().getRuta_foto3().length()>1)
                    subirFotosCor(getActivity(),envio.getCorreccion().getId(),envio.getCorreccion().getRuta_foto3());


            }
        }else
            Toast.makeText(getActivity(), getString(R.string.sin_conexion), Toast.LENGTH_LONG).show();

    }
    public static void subirFotosCor(Activity activity, int id, String ruta){

        //subo cada una
        Intent msgIntent = new Intent(activity, SubirFotoService.class);
        msgIntent.putExtra(SubirFotoService.EXTRA_IMAGE_ID,id);
        msgIntent.putExtra(SubirFotoService.EXTRA_IMG_PATH,ruta);

        msgIntent.putExtra(SubirFotoService.EXTRA_INDICE,Constantes.INDICEACTUAL);
        // Constantes.INDICEACTUAL
        Log.d(TAG,"subiendo fotos"+activity.getLocalClassName());

        msgIntent.setAction(SubirFotoService.ACTION_UPLOAD_COR);


        activity.startService(msgIntent);






    }

    public InformeEtapaEnv preparaInforme(int informeid){

        InformeEtapaEnv envio=new InformeEtapaEnv();
        //busco el informe

        envio.setInformeEtapa(npViewModel.getInformexId(informeid));
        envio.setClaveUsuario(Constantes.CLAVEUSUARIO);
        envio.setIndice(Constantes.INDICEACTUAL);
        envio.setInformeEtapaDet(npViewModel.getInformeDet(informeid));
        //busco las imagenes

            List<ImagenDetalle> imagenes=npViewModel.buscarImagenes(envio.getInformeEtapaDet());

            envio.setImagenDetalles(imagenes);


        return envio;
    }

    public static void subirFotos(Activity activity, InformeEtapaEnv informe){
        //las imagenes
        if(informe.getInformeEtapa().getEtapa()==1||informe.getInformeEtapa().getEtapa()==3){
            //busco la imagenes
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
                //cambio su estatus a subiendo



            }


        }else
            for(InformeEtapaDet imagen:informe.getInformeEtapaDet()){
                //subo cada una
                Intent msgIntent = new Intent(activity, SubirFotoService.class);
                msgIntent.putExtra(SubirFotoService.EXTRA_IMAGE_ID, imagen.getId());
                msgIntent.putExtra(SubirFotoService.EXTRA_IMG_PATH,imagen.getRuta_foto());
                msgIntent.putExtra(SubirFotoService.EXTRA_INDICE,informe.getIndice());
                // Constantes.INDICEACTUAL
                Log.d(TAG,"subiendo fotos"+activity.getLocalClassName());

                msgIntent.setAction(SubirFotoService.ACTION_UPLOAD_ETA);

                //cambio su estatus a subiendo
                imagen.setEstatusSync(1);
                activity.startService(msgIntent);
                //cambio su estatus a subiendo



            }

    }

}