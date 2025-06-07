package com.example.comprasmu.ui.notificaciones;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.comprasmu.R;

import com.example.comprasmu.databinding.ListaGenericFragmentBinding;

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
        listaNotificacionesGen=mViewModel.pedirNotificacionesGenerales(Constantes.INDICEACTUAL,Constantes.CIUDADTRABAJO);
        listaNotificacionesGen.observe(getViewLifecycleOwner(), new Observer<List<NotificacionGen>>() {
            @Override
            public void onChanged(List<NotificacionGen> notificacionesGen) {

                for (NotificacionGen notificacion : notificacionesGen
                ) {
                    if (notificacion.getTipo() == opcion&notificacion.getTotal()>0) {

                        listaTarjetas.add(crearTarjeta(notificacion));
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

                    tarjeta.setTitulo1(notificacion.getIndice());
                  //  textos.add(notificacion.getCliente());
                    textos.add(notificacion.getCiudad());
                    textos.add("CAPTURAR EN EL MODULO CORRESPONDIENTE");
                    tarjeta.setTextos(textos);
                    break;
                case 4: //revisar recibo
                case 5: //agregar recibo

                    tarjeta.setTitulo1(notificacion.getIndice());
                    textos.add(notificacion.getCiudad());
                    textos.add("CAPTURAR EN EL MODULO CORRESPONDIENTE");
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

}