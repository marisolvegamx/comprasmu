package com.example.comprasmu.ui.solcorreccion;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.InformeCompraDao;
import com.example.comprasmu.data.dao.ListaCompraDao;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.ui.infetapa.ContInfEtaViewModel;
import com.example.comprasmu.ui.informe.DetalleCancelado;
import com.example.comprasmu.ui.informe.ListaCancelFragment;
import com.example.comprasmu.ui.informedetalle.DetalleProductoFragment;
import com.example.comprasmu.ui.listadetalle.ListaCompraFragment;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.ui.ListaSelScrollFragment;
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
    int totMuestraAdic;

    ListaSolsViewModel scViewModel;
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
      convertirListaCor();


    }


    private void contarCanceladas(){

        List<InformeCompraDetalle> informesCancel=scViewModel.getTotalCancel(Constantes.INDICEACTUAL);

                if(informesCancel!=null)

                totCancel=informesCancel.size();

        setEtiquetadoCancel(3,6);
        //veo si ya puedo hacer empaque
        List<ListaCompra> listacomp = scViewModel.cargarClientesSimplxet(Constantes.CIUDADTRABAJO, 4);
        InformeEtapa nvoinf=new InformeEtapa();
        List<InformeEtapa> listageneral=new ArrayList<>();
        if (listacomp != null && listacomp.size() > 0 && listacomp.get(0).getLis_reactivado()==1) {
            //veo que no haya hecho informe para no esperar a la supervisión
            ContInfEtaViewModel conViewModel = new ViewModelProvider(this).get(ContInfEtaViewModel.class);
            InformeEtapa informesEtapa = conViewModel.getInformeNoCancel(Constantes.INDICEACTUAL,4);
            if(informesEtapa!=null) {
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

        totCancel=listageneral.size();

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

        }

        //busco etiquetado
        List<InformeEtapa> informes= scViewModel.getEtiquetadoAdicional(Constantes.INDICEACTUAL);
        int informesfinal=0;//contador para saber cuantos informes hay
        Log.d(TAG, "YA CARGÓ " + informes.size());

        for (InformeEtapa infeta:informes
        ) {
            //reviso que ya pueda hacer esa etapa
            //busco los clientes x ciudad
            listacomp = scViewModel.cargarClientesSimplxet(Constantes.CIUDADTRABAJO, 3);
            if(listacomp!=null&&listacomp.size()>0&&listacomp.get(0).getClientesId()==infeta.getClientesId()) {

                informesfinal++;
            }
        }

        totMuestraAdic=informesfinal;

        //veo si ya puedo hacer empaque
        listacomp =scViewModel.cargarClientesSimplxet(Constantes.CIUDADTRABAJO, 4);

        int listageneral=0; //para contar los informes
        if (listacomp != null && listacomp.size() > 0 && listacomp.get(0).getLis_reactivado()==2) {
            ContInfEtaViewModel conViewModel = new ViewModelProvider(this).get(ContInfEtaViewModel.class);
            InformeEtapa informesEtapa = conViewModel.getInformeNoCancel(Constantes.INDICEACTUAL,4);
            if(informesEtapa==null) {

                listageneral++;
            }
        }

        totMuestraAdic=listageneral;

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

        }

    }


    private  void convertirListaCor() {
        listaClientesEnv = new ArrayList<DescripcionGenerica>();
        //primero las generales

         Log.d(TAG,"si llega aqui");
         DescripcionGenerica gen = new DescripcionGenerica(1,"CORRECCIONES","gen",String.valueOf(totCorrecciones));
         listaClientesEnv.add(gen);

         listaClientesEnv.add(new DescripcionGenerica(2, "CANCELADAS", "0",totCancel+""));

         listaClientesEnv.add(new DescripcionGenerica(3, "MUESTRA ADICIONAL", "0",totMuestraAdic+""));
        // totCorrecciones.removeObservers(getViewLifecycleOwner());
         //totMuestraAdic.removeObservers(getViewLifecycleOwner());
         //totCancel.removeObservers(getViewLifecycleOwner());
         setLista(listaClientesEnv);
         setupListAdapter();
         adaptadorLista.setDesc2(true);

    }

/*
    @Override
    public void onClickVer(int position) {
        siguiente(position);
    }*/
}