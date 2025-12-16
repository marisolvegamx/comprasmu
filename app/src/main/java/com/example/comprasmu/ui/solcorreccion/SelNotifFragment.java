package com.example.comprasmu.ui.solcorreccion;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.remote.NotificacionResponse;
import com.example.comprasmu.data.remote.PostResponse;
import com.example.comprasmu.ui.gasto.IListenerRevRec;
import com.example.comprasmu.ui.infetapa.ContInfEtaViewModel;
import com.example.comprasmu.ui.notificaciones.NotificacionGen;
import com.example.comprasmu.ui.notificaciones.ResumenNotificacionesGenFragment;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.ui.ListaSelecFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*** juntar correcciones, notificaciones y canceladas***/
public class SelNotifFragment extends ListaSelecFragment{

    private  ArrayList<DescripcionGenerica> listaClientesEnv;
    private static final String TAG="SelNotifFragment";
    public static String ARG_TIPOCONS="comprasmu.correselcli.tipocons";
    int totCorrecciones;
    int totCancel;
    int itotCanceleta;
    int totMuestraAdic;

    ListaSolsViewModel scViewModel;
    private List<InformeEtapa> totCanceleta;
    private ComprasLog comprasLog;
    private MutableLiveData<List<NotificacionGen>> listaNotificacionesGenerales;

    public SelNotifFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //busco los datos y los convierto al tipo String[]
        super.onCreate(savedInstanceState);
        comprasLog = ComprasLog.getSingleton();


    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        /*aqui me dice que dato es*/

        scViewModel = new ViewModelProvider(this).get(ListaSolsViewModel.class);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.menu_notificaciones);
        totCancel=0;
        itotCanceleta=0;
        totCorrecciones=0;
        totMuestraAdic=0;

        initializeCountDrawer();
        getObjetosLV().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //  Toast.makeText(getContext(),listaSeleccionable.get(i).getId()+"", Toast.LENGTH_LONG).show();
                siguiente(i);

            }
        });

    }

    private void contarCorrecc(){
        totCorrecciones=scViewModel.getTotalSolsGen(Constantes.INDICEACTUAL,1);

    }
    private void initializeCountDrawer(){
        contarCorrecc();
        scViewModel.contarCanceladas();
        scViewModel.getTotCancel().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer totcan) {
                totCancel=totcan;
                scViewModel.contarMuestraAdic().observe(getViewLifecycleOwner(), new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer total) {
                        totMuestraAdic=total;
                        notificacionesGenerales();
                        convertirListaCor();
                    }
                });

            }
        });

    }

    private void notificacionesGenerales() {
        listaNotificacionesGenerales=scViewModel.pedirNotificacionesGenerales(Constantes.INDICEACTUAL);

    }


   /* private void contarCanceladas(){
        ContInfEtaViewModel conViewModel = new ViewModelProvider(this).get(ContInfEtaViewModel.class);

        //busco cancelados de preparacion

        totCanceleta=scViewModel.getTotalCancelEtaSim(Constantes.INDICEACTUAL,1);

        for (InformeEtapa informe:totCanceleta
        ) { //busco si no se ha vuelto a elaborar
         /*   InformeEtapa inf=scViewModel.getInformexPlantaEtaEst(informe.getPlantasId(),informe.getEtapa(),Constantes.INDICEACTUAL,0);
            if(inf!=null){
                //corregido
                continue;
            }
            else*/
       /*         itotCanceleta++;

        }

        //busco cancelados compra
        List<InformeCompraDetalle> informesCancel=scViewModel.getTotalCancel(Constantes.INDICEACTUAL);

        if(informesCancel!=null&&informesCancel.size()>0)

            totCancel=informesCancel.size();
        totCancel=totCancel+itotCanceleta;
        comprasLog.info(TAG,"contarCanceladas","totcancel:"+totCancel);

        if(totCancel==0){
            //busco etiquetado por reactivacion
            comprasLog.info(TAG,"contarCanceladas","buscando etiquetado x reactivacion");
            List<ListaCompra> listacomp = scViewModel.cargarClientesSimplxetReacsc( 3,1);
            if(listacomp!=null&&listacomp.size()>0)
                setEtiquetadoCancel(3, 6);
            else {
                    //veo si ya puedo hacer empaque
                    listacomp = scViewModel.cargarClientesSimplxetReacsc(4, 1);
                    InformeEtapa nvoinf = new InformeEtapa();
                    List<InformeEtapa> listageneral = new ArrayList<>();
                    comprasLog.info(TAG, "contarCanceladas", "puedo hacer empaque?:" + listacomp);
                    for (ListaCompra listaCompra : listacomp) {
                        if (listaCompra.getLis_reactivado() != null && listaCompra.getLis_reactivado() == 1) {

                            //veo que no haya hecho informe para no esperar a la supervisión
                            InformeEtapa informesEtapa = conViewModel.getInformeNoCancel(Constantes.INDICEACTUAL, 4, listaCompra.getCiudadNombre(), listaCompra.getClientesId());
                            comprasLog.info(TAG, "contarCanceladas", "tengo informe?:" + informesEtapa);

                            if (informesEtapa == null) {
                                nvoinf.setIndice(listaCompra.getIndice());
                                nvoinf.setEstatus(listaCompra.getEstatus());
                                nvoinf.setEtapa(4);

                                nvoinf.setCiudadNombre(listaCompra.getCiudadNombre());
                                nvoinf.setClienteNombre(listaCompra.getClienteNombre());

                                // nvoinf.mo
                                listageneral.add(nvoinf);
                            }
                        }
                    }

                    totCancel = listageneral.size();


            }
        }
        comprasLog.info(TAG,"contarCanceladas","totcancel:"+totCancel);

    }*/

    /*private void contarMuestraAdic(){

        // lista de compra pendiente
        List<ListaCompra> listacomp = scViewModel.cargarClientesSimplxetReacsc(2,2);
        comprasLog.info(TAG,"contarMuestraAdic","hay reactivacion muestra adic compra? " + listacomp.size());
        if(listacomp.size()>0){

            int informesdetList=0;
            //busco el detalle
            for (ListaCompra compra:listacomp
            ) {

                List<ListaCompraDetalle> compraDetalles = scViewModel.getProductosPend(compra.getId());
                if (compraDetalles != null && compraDetalles.size() > 0) {
                    for (ListaCompraDetalle detalle : compraDetalles
                    ) {

                        comprasLog.info(TAG,"contarMuestraAdic", "contarMuestraAdic falta comprar? "+detalle.getId());
                        informesdetList++;
                    }
                }
            }
            totMuestraAdic=informesdetList;

        }else {
                listacomp = scViewModel.cargarClientesSimplxetReacsc( 3,2);
                if (listacomp != null && listacomp.size() > 0) {
                    //busco etiquetado
                    List<InformeEtapa> informes = scViewModel.getEtiquetadoAdicional(Constantes.INDICEACTUAL);
                    int informesfinal = 0;//contador para saber cuantos informes hay
                    comprasLog.info(TAG,"contarMuestraAdic", "contarMuestraAdic falta etiquetado" + informes.size());

                    for (InformeEtapa infeta : informes
                    ) {
                        //reviso que ya pueda hacer esa etapa
                        //busco los clientes x ciudad
                        listacomp = scViewModel.cargarClientesSimplxet(infeta.getCiudadNombre(), 3);

                        if (listacomp != null && listacomp.size() > 0 && listacomp.get(0).getClientesId() == infeta.getClientesId()) {

                            informesfinal++;
                        }
                    }

                    totMuestraAdic = informesfinal;
                } else //veo si ya puedo hacer empaque
                {
                    listacomp = scViewModel.cargarClientesSimplxetReacsc( 4,2);

                    int listageneral = 0; //para contar los informes
                    for(ListaCompra listaCompra:listacomp) {
                        if (listaCompra.getLis_reactivado() != null &&listaCompra.getLis_reactivado() == 2) {
                            ContInfEtaViewModel conViewModel = new ViewModelProvider(this).get(ContInfEtaViewModel.class);
                            InformeEtapa informesEtapa = conViewModel.getInformeNoCancel(Constantes.INDICEACTUAL, 4, listaCompra.getCiudadNombre(), listaCompra.getClientesId());
                            if (informesEtapa == null) {

                                listageneral++;
                            }
                        }
                    }

                    totMuestraAdic = listageneral;
                }


            }
        comprasLog.info(TAG,"contarMuestraAdic","totMuestraAdic:"+totMuestraAdic);
    }*/
   /* private void setEtiquetadoCancel(int etapa, int estatus) {
        List<InformeEtapa> listageneral=new ArrayList<>();
        //para ver si sigue etiquetado y empaque
        List<InformeEtapa> informes=scViewModel.getInfEtapaxEstatusSim(Constantes.INDICEACTUAL,etapa,estatus);
        if(informes!=null) {
            comprasLog.info(TAG, "setEtiquetadoCancel", "tengo informe etiquetado?" + informes.size());
            for (InformeEtapa infeta : informes
            ) {
                //reviso si ya estoy en etapa 3
                List<ListaCompra> listacomp = scViewModel.cargarClientesSimplxet(infeta.getCiudadNombre(), 3);
                if (listacomp != null && listacomp.size() > 0 && listacomp.get(0).getClientesId() == infeta.getClientesId()) {
                    listageneral.add(infeta);
                }

            }
        }
            //totCancel=new MutableLiveData<>();
        totCancel=listageneral.size();

    }*/
    public void siguiente(int i){
       // Log.d(TAG,"una planta "+tipoconsulta+"--"+listaSeleccionable.get(i).getId());
        int opcion=listaSeleccionable.get(i).getId();
        Bundle bundle = new Bundle();
        switch(opcion) {

            case 1:
               // bundle.putInt(ListaCompraFragment.ARG_PLANTASEL, listaSeleccionable.get(i).getId());
               // bundle.putString(ListaCompraFragment.ARG_NOMBREPLANTASEL, listaSeleccionable.get(i).getDescripcion2());
                if (listaSeleccionable.get(i).getDescripcion().equals("gen")) {
                    bundle.putBoolean(ListaSolCorreFragment.ARG_ESGEN, true);
                }

                bundle.putString(ARG_TIPOCONS, "action_selclitosolcor2");
                NavHostFragment.findNavController(this).navigate(R.id.action_notiftosolcor, bundle);
                break;
            case 2: //canceladas
              //  FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
               // ListaCancelFragment nvofrag = new ListaCancelFragment();
               // nvofrag.setArguments(args);

                //ft.add(R.id.continf_fragment, nvofrag);
                NavHostFragment.findNavController(this).navigate(R.id.action_notiftocan, bundle);
                break;

            case 3:
                NavHostFragment.findNavController(this).navigate(R.id.action_notiftomu, bundle);
                break;
            case 4: case 5: case 6: //revisar recibo
                Bundle args = new Bundle();
                args.putInt(ResumenNotificacionesGenFragment.ARG_OPCION_NOTIF, opcion);
                ResumenNotificacionesGenFragment nvofrag = new ResumenNotificacionesGenFragment();
                nvofrag.setArguments(args);

                NavHostFragment navHostFragment =
                        (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                NavController navController = navHostFragment.getNavController();
                if(navController!=null)
                    navController.navigate(R.id.action_notiftores, args);
              //  FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                // Definir una transacción
                //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // Remplazar el contenido principal por el fragmento
               // fragmentTransaction.replace(R.id.nav_host_fragment, nvofrag);

                // Cambiar
               // fragmentTransaction.commit();
                break;


        }

    }


    private  void convertirListaCor() {
        try {
            listaClientesEnv = new ArrayList<DescripcionGenerica>();
            //primero las generales

             Log.i(TAG,"si llega aqui");
             DescripcionGenerica gen = new DescripcionGenerica(1,"CORRECCIONES","gen",String.valueOf(totCorrecciones));
             listaClientesEnv.add(gen);

             listaClientesEnv.add(new DescripcionGenerica(2, "CANCELADAS", "0",totCancel+""));

             listaClientesEnv.add(new DescripcionGenerica(3, "MUESTRA ADICIONAL", "0",totMuestraAdic+""));
            listaNotificacionesGenerales.observe(getViewLifecycleOwner(), new Observer<List<NotificacionGen>>() {
                 @Override
                 public void onChanged(List<NotificacionGen> notificacionGen) {
                     HashMap<Integer,String[]> arregloTemporal=arreglarNotificacionesGenerales(notificacionGen);
                     for (Map.Entry<Integer, String[]> entry:arregloTemporal.entrySet()
                          )
                         {
                             if(entry.getValue()!=null)
                               listaClientesEnv.add(new DescripcionGenerica(entry.getKey(), entry.getValue()[0], "0", entry.getValue()[1]+""));

                          }




                     setLista(listaClientesEnv);
                     setupListAdapter();
                     adaptadorLista.setDesc2(true);
                 }
             });




        }catch(Exception ex){
            ex.printStackTrace();
            Log.e(TAG,ex.getMessage());
            comprasLog.grabarError(TAG,"convertirListaCor", "Hubo un error al desplegar la lista "+ex.getMessage());
        }
    }


   private HashMap<Integer,String[]> arreglarNotificacionesGenerales(List<NotificacionGen> notificaciones){
       HashMap<Integer,String[]> arregloTemporal=new HashMap<>();
       if(notificaciones!=null)
       for (NotificacionGen notificacionGen:
               notificaciones) {
           String[] arregloNoti= arregloTemporal.get(notificacionGen.getTipo());
           if(arregloNoti!=null&&arregloNoti.length>0){
               int total=0;
               try {
                   total= Integer.parseInt(arregloNoti[1]);
               }
               catch (NumberFormatException ex){
                   total=0;
               }
                   arregloNoti[1] = (total+ notificacionGen.getTotal()) + "";

           }
           else{
               arregloNoti=new String[2];
               arregloNoti[0]=notificacionGen.getDescripcion1();
               arregloNoti[1]=notificacionGen.getTotal()+"";
           }
           arregloTemporal.put(notificacionGen.getTipo(),arregloNoti);

       }
       return arregloTemporal;
   }

    public class ListenerNotRevRec implements IListenerRevRec{

        @Override
        public void guardarEstatus(PostResponse response) {

        }

        @Override
        public void guardarRes(PostResponse respuesta) {

        }

        /***para estatus envio****/
        @Override
        public void guardarResNotif(NotificacionResponse response) {
            //podría ya no tener la actividad
            if(!isAdded() || getActivity()==null)
                return;
            if (response != null && response.getData() != null) {



            }

        }


    }
/*
    @Override
    public void onClickVer(int position) {
        siguiente(position);
    }*/
}