package com.example.comprasmu.ui.notificaciones;

import static com.example.comprasmu.ui.envio.DescargarFragment.ARG_DESCCIUDADSEL;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.comprasmu.R;

import com.example.comprasmu.databinding.ListaGenericFragmentBinding;

import com.example.comprasmu.ui.envio.DescargarFragment;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.ui.CardViewGenerico;
import com.example.comprasmu.utils.ui.ListaTextViewsAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/****muestro resumen de notificaciones como revisar recibo, agregar recibo y documentos envio que
 * son de todas la ciudades asignadas**/
public class ResumenNotificacionesGenFragment extends Fragment  {
    public static final String ARG_OPCION_NOTIF = "compras.resumennotif.opcion";
    private ListaNotifEtiqViewModel mViewModel;
    public static final String TAG = "ResumenNotificacionesGenFragment";
    private ListaGenericFragmentBinding mBinding;
    private ListaTextViewsAdapter listaTextViewsAdapter;
    private String indice;
    private int opcion; //6-docs envio 4-revisar recibo 5-agregar recibo
    MutableLiveData<List<NotificacionGen>> listaNotificacionesGen;
    public ResumenNotificacionesGenFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        indice = Constantes.INDICEACTUAL;

        mBinding= DataBindingUtil.inflate(inflater,
                R.layout.lista_generic_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(ListaNotifEtiqViewModel.class);
        if (getArguments() != null) {
            //porque lo habia comentado????

            opcion=getArguments().getInt(ARG_OPCION_NOTIF);

        }
        Log.d(TAG,"---->"+opcion);
        return    mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

       // mBinding.setLcviewModel(mViewModel);
        mBinding.setLifecycleOwner(this);

        mBinding.detalleList.setVisibility(View.GONE);
        mBinding.emptyStateText.setVisibility(View.GONE);
        setupListAdapters();

    }

    public void cargarLista(){
        List<CardViewGenerico> listaTarjetas=new ArrayList<>();
        listaNotificacionesGen=mViewModel.pedirNotificacionesGenerales(Constantes.INDICEACTUAL);
        listaNotificacionesGen.observe(getViewLifecycleOwner(), new Observer<List<NotificacionGen>>() {
            @Override
            public void onChanged(List<NotificacionGen> notificacionesGen) {
                CardViewGenerico cardViewGenerico=new CardViewGenerico();
                for (NotificacionGen notificacion : notificacionesGen
                ) {
                    if (notificacion.getTipo() == opcion&notificacion.getTotal()>0) {
                        cardViewGenerico=crearTarjeta(notificacion);

                        listaTarjetas.add(cardViewGenerico);
                        Log.d(TAG,listaTarjetas.get(0).getTextos().toString());
                    }

                }
                if (listaTarjetas.size() > 0) {
                    listaTextViewsAdapter.setCardViewList(listaTarjetas);

                    listaTextViewsAdapter.notifyDataSetChanged();
                    mBinding.detalleList.setVisibility(View.VISIBLE);
                } else {
                    mBinding.emptyStateText.setVisibility(View.VISIBLE);
                }
            }
            });



        }
        public CardViewGenerico crearTarjeta(NotificacionGen notificacion) {
            CardViewGenerico tarjeta=new CardViewGenerico();
            List<String> textos=new ArrayList<>();

            switch (opcion) {
                case 6: //docs envio

                    tarjeta.setTitulo1(ComprasUtils.indiceLetra(notificacion.getIndice()));
                  //  textos.add(notificacion.getCliente());
                    textos.add(notificacion.getCiudad());
                 //   textos.add("CAPTURAR EN EL MODULO CORRESPONDIENTE");
                    tarjeta.setTextos(textos);
                    ContextThemeWrapper newContext = new ContextThemeWrapper(getContext(),R.style.btnverde);

                    Button boton=new Button(newContext);
                    boton.setTextColor(Color.WHITE);
                    boton.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.blue_principal,null));
                    boton.setText(getString(R.string.descargar));
                    boton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            iraDescargarDocsEnvio(notificacion.getCiudad());
                        }
                    });
                    tarjeta.setBotonIr(boton);
                    break;
                case 4: //revisar recibo
                    tarjeta.setTitulo1(ComprasUtils.indiceLetra(notificacion.getIndice()));
                    textos.add(notificacion.getCiudad());
                    textos.add("IR A DESCARGAR RECIBO EN EL MODULO DE GASTOS");
                    tarjeta.setTextos(textos);
                    break;
                case 5: //agregar recibo

                    tarjeta.setTitulo1(ComprasUtils.indiceLetra(notificacion.getIndice()));
                    textos.add(notificacion.getCiudad());
                    textos.add("IR A CONTINUAR INFORME EN EL MODULO DE GASTOS");
                    tarjeta.setTextos(textos);
                    break;
            }
            Log.d(TAG,"**"+tarjeta);
            return tarjeta;
        }


    private void setupListAdapters() {
        mBinding.detalleList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.detalleList.setHasFixedSize(true);
        listaTextViewsAdapter = new ListaTextViewsAdapter(getContext());
        mBinding.detalleList.setAdapter(listaTextViewsAdapter);
        cargarLista();

    }
    private void iraDescargarDocsEnvio(String ciudad)
    {
        if(Constantes.ETAPAACTUAL==5) {
            Bundle bundle = new Bundle();
            bundle.putString(ARG_DESCCIUDADSEL, ciudad);
            NavHostFragment.findNavController(this).navigate(R.id.nav_envdescargas, bundle);
        } else
            Toast.makeText(getContext(),"Capturar en el módulo de Envio", Toast.LENGTH_LONG).show();


    /*    DescargarFragment nvofrag = new DescargarFragment();
        nvofrag.setArguments(args);

        NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();*/

    }
}