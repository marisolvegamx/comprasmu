package com.example.comprasmu.ui.solcorreccion;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.comprasmu.R;
import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.dao.InformeCompraDao;

import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;

import com.example.comprasmu.data.remote.NotificacionResponse;
import com.example.comprasmu.data.remote.PostResponse;
import com.example.comprasmu.ui.gasto.IListenerRevRec;
import com.example.comprasmu.ui.infetapa.ContInfEtaViewModel;
import com.example.comprasmu.ui.informe.DetalleCancelado;
import com.example.comprasmu.ui.notificaciones.NotificacionGen;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.ui.ListaSelecFragment;

import java.util.ArrayList;
import java.util.List;

/*** juntar correcciones, notificaciones y canceladas***/
public class SelNotifFragment extends ListaSelecFragment{

    private  ArrayList<DescripcionGenerica> listaClientesEnv;
    private static final String TAG="SelNotifFragment";

    public static String ARG_TIPOCONS="comprasmu.correselcli.tipocons";
    int totCorrecciones;
    int totCancel;
    int itotCanceleta;
    int totMuestraAdic;
    MutableLiveData<Integer> contNotif;

    ListaSolsViewModel scViewModel;
    private List<InformeEtapa> totCanceleta;

    public SelNotifFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //busco los datos y los convierto al tipo String[]
        super.onCreate(savedInstanceState);


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
        contarCanceladas();
        contarMuestraAdic();
        notificacionesGenerales();


    }

    private void notificacionesGenerales() {
        PeticionesServidor ps=new PeticionesServidor(Constantes.CLAVEUSUARIO);
        ListenerNotRevRec listener=new ListenerNotRevRec();
        ps.getNotificacionesGen(Constantes.INDICEACTUAL,Constantes.CIUDADTRABAJO,listener);

    }


    private void contarCanceladas(){
        ContInfEtaViewModel conViewModel = new ViewModelProvider(this).get(ContInfEtaViewModel.class);

        //busco cancelados de preparacion

        totCanceleta=scViewModel.getTotalCancelEtaSim(Constantes.INDICEACTUAL,1);
        for (InformeEtapa informe:totCanceleta
        ) { //busco si no se ha vuelto a elaborar
            InformeEtapa inf=scViewModel.getInformexPlantaEtaEst(informe.getPlantasId(),informe.getEtapa(),Constantes.INDICEACTUAL,0);
            if(inf!=null){
                //corregido
                continue;
            }
            else
                itotCanceleta++;

        }

        //busco cancelados compra
        List<InformeCompraDetalle> informesCancel=scViewModel.getTotalCancel(Constantes.INDICEACTUAL);

        if(informesCancel!=null&&informesCancel.size()>0)

            totCancel=informesCancel.size();
        totCancel=totCancel+itotCanceleta;
        if(totCancel==0){
            //busco etiquetado por reactivacion
            List<ListaCompra> listacomp = scViewModel.cargarClientesSimplxet(Constantes.CIUDADTRABAJO, 3);
            if(listacomp!=null&&listacomp.size()>0)
                         setEtiquetadoCancel(3, 6);
            else {
                        //veo si ya puedo hacer empaque
                listacomp = scViewModel.cargarClientesSimplxet(Constantes.CIUDADTRABAJO, 4);
                InformeEtapa nvoinf = new InformeEtapa();
                List<InformeEtapa> listageneral = new ArrayList<>();
                if (listacomp != null && listacomp.size() > 0 && listacomp.get(0).getLis_reactivado() != null && listacomp.get(0).getLis_reactivado() == 1) {
                    //veo que no haya hecho informe para no esperar a la supervisión
                    InformeEtapa informesEtapa = conViewModel.getInformeNoCancel(Constantes.INDICEACTUAL, 4);
                    if (informesEtapa != null) {
                        nvoinf.setIndice(listacomp.get(0).getIndice());
                        // nvoinf.set = listacomp.get(0).getId();
                        nvoinf.setEstatus(listacomp.get(0).getEstatus());
                        nvoinf.setEtapa(4);

                        nvoinf.setCiudadNombre(listacomp.get(0).getCiudadNombre());
                        nvoinf.setClienteNombre(listacomp.get(0).getClienteNombre());

                        // nvoinf.mo
                        listageneral.add(nvoinf);
                    }
                }

                totCancel = listageneral.size();
            }
        }

    }
    private void contarMuestraAdic(){

        // lista de compra pendiente
        List<ListaCompra> listacomp = scViewModel.cargarClientesSimplxetReac(Constantes.CIUDADTRABAJO, 2,2);
        if(listacomp.size()>0){

            int informesdetList=0;
            InformeCompraDao.InformeCompravisita informetemp=new InformeCompraDao.InformeCompravisita();
            InformeCompraDetalle detalleTemp=new InformeCompraDetalle();
            //busco el detalle
            for (ListaCompra compra:listacomp
            ) {

                List<ListaCompraDetalle> compraDetalles = scViewModel.getProductosPend(compra.getId());
                if (compraDetalles != null && compraDetalles.size() > 0) {
                    for (ListaCompraDetalle detalle : compraDetalles
                    ) {


                        informesdetList++;
                    }


                }
            }
            totMuestraAdic=informesdetList;

        }else {
                listacomp = scViewModel.cargarClientesSimplxet(Constantes.CIUDADTRABAJO, 3);
                if (listacomp != null && listacomp.size() > 0) {
                    //busco etiquetado
                    List<InformeEtapa> informes = scViewModel.getEtiquetadoAdicional(Constantes.INDICEACTUAL);
                    int informesfinal = 0;//contador para saber cuantos informes hay
                    Log.d(TAG, "YA CARGÓ " + informes.size());

                    for (InformeEtapa infeta : informes
                    ) {
                        //reviso que ya pueda hacer esa etapa
                        //busco los clientes x ciudad
                        listacomp = scViewModel.cargarClientesSimplxet(Constantes.CIUDADTRABAJO, 3);
                        if (listacomp != null && listacomp.size() > 0 && listacomp.get(0).getClientesId() == infeta.getClientesId()) {

                            informesfinal++;
                        }
                    }

                    totMuestraAdic = informesfinal;
                } else { //veo si ya puedo hacer empaque
                    listacomp = scViewModel.cargarClientesSimplxet(Constantes.CIUDADTRABAJO, 4);

                    int listageneral = 0; //para contar los informes
                    if (listacomp != null && listacomp.size() > 0 && listacomp.get(0).getLis_reactivado() != null && listacomp.get(0).getLis_reactivado() == 2) {
                        ContInfEtaViewModel conViewModel = new ViewModelProvider(this).get(ContInfEtaViewModel.class);
                        InformeEtapa informesEtapa = conViewModel.getInformeNoCancel(Constantes.INDICEACTUAL, 4);
                        if (informesEtapa == null) {

                            listageneral++;
                        }
                    }

                    totMuestraAdic = listageneral;
                }
            }

    }
    private void setEtiquetadoCancel(int etapa, int estatus) {
        List<InformeEtapa> listageneral=new ArrayList<>();
            //para ver si sigue etiquetado y empaque
        List<InformeEtapa> informes=scViewModel.getInfEtapaxEstatusSim(Constantes.INDICEACTUAL,etapa,estatus);

        //paso de informe etapa ainforme compra
        DetalleCancelado nvoinf = new DetalleCancelado();
        for (InformeEtapa infeta : informes
        ) {

            //reviso si ya estoy en etapa 3
            List<ListaCompra> listacomp = scViewModel.cargarClientesSimplxet(Constantes.CIUDADTRABAJO, 3);
            if (listacomp != null && listacomp.size() > 0 && listacomp.get(0).getClientesId() == infeta.getClientesId()) {
                listageneral.add(infeta);
            }

        }
            //totCancel=new MutableLiveData<>();
        totCancel=listageneral.size();

    }
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
            case 4: //revisar recibo
                if(Constantes.ETAPAACTUAL==6) {
                    NavHostFragment.findNavController(this).navigate(R.id.action_notiftorev, bundle);
                }
                else
                    Toast.makeText(getContext(),"Capturar en el módulo de Gastos", Toast.LENGTH_LONG).show();

                    break;
            case 5: //ajustar recibo
                if(Constantes.ETAPAACTUAL==6) {
                    NavHostFragment.findNavController(this).navigate(R.id.nav_continuargas, bundle);

                } else
                    Toast.makeText(getContext(),"Capturar en el módulo de Gastos", Toast.LENGTH_LONG).show();

                break;
            case 6: //estatus envio
               // if(listaClientesEnv.get)
                if(Constantes.ETAPAACTUAL==5) {
                    NavHostFragment.findNavController(this).navigate(R.id.nav_envdescargas, bundle);
                } else
                    Toast.makeText(getContext(),"Capturar en el módulo de Envio", Toast.LENGTH_LONG).show();

                break;

        }

    }


    private  void convertirListaCor(List<NotificacionGen> lista) {
        listaClientesEnv = new ArrayList<DescripcionGenerica>();
        //primero las generales

         Log.d(TAG,"si llega aqui");
         DescripcionGenerica gen = new DescripcionGenerica(1,"CORRECCIONES","gen",String.valueOf(totCorrecciones));
         listaClientesEnv.add(gen);

         listaClientesEnv.add(new DescripcionGenerica(2, "CANCELADAS", "0",totCancel+""));

         listaClientesEnv.add(new DescripcionGenerica(3, "MUESTRA ADICIONAL", "0",totMuestraAdic+""));
         for (NotificacionGen noti:
             lista) {
            listaClientesEnv.add(new DescripcionGenerica(noti.getTipo(), noti.getDescripcion1(), "0",noti.getTotal()+""));
             //modifico el estatus del informe
             if(noti.getTipo()==5&&noti.getTotal()>0){    //4-revisar recibo
                 //5-ajustar recibo
                 //6-estatus envio
                 //busco el informe
                 List<InformeEtapa> listaInf=scViewModel.getInfGastoxCiudad(Constantes.INDICEACTUAL, Constantes.CIUDADTRABAJO);
                 if(listaInf!=null&&listaInf.size()>0){
                     scViewModel.actualizarEstatusGas(listaInf.get(0).getId());
                     Log.d(TAG,"convertirListaNotif "+"actualizando informe gastos ajuste"+listaInf.get(0).getId());

                     // flog.grabarError(TAG,"convertirListaNotif","actualizando informe gastos ajuste"+listaInf.get(0).getId());
                 }
             }

         }

         setLista(listaClientesEnv);
         setupListAdapter();
         adaptadorLista.setDesc2(true);

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

            if(response!=null&&response.getData()!=null) {
                convertirListaCor(response.getData());


            }

        }


    }
/*
    @Override
    public void onClickVer(int position) {
        siguiente(position);
    }*/
}