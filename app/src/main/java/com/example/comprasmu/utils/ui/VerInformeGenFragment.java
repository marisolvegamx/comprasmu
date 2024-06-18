package com.example.comprasmu.utils.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.Correccion;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeEnvioDet;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeGastoDet;
import com.example.comprasmu.data.modelos.SolicitudCor;

import com.example.comprasmu.databinding.VerInformegenFragmentBinding;
import com.example.comprasmu.ui.RevisarFotoActivity;
import com.example.comprasmu.ui.correccion.CorreccionWithSol;
import com.example.comprasmu.ui.correccion.NvaCorreViewModel;
import com.example.comprasmu.ui.gallery.GalFotosFragment;
import com.example.comprasmu.ui.gasto.NvoGastoViewModel;
import com.example.comprasmu.ui.infetapa.NuevoInfEtapaActivity;
import com.example.comprasmu.ui.informe.NuevoinformeFragment;
import com.example.comprasmu.ui.informe.VerInformeFragment;
import com.example.comprasmu.utils.CampoForm;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/***se usa para informes correccion e informes etapas********/
public class VerInformeGenFragment extends Fragment {

    public static final String ARG_IDDET = "comprasmu.vie.iddet";
    public static final String ARG_TIPOINF = "comprasmu.vie.tipoinf";
    private InformesGenViewModel mViewModel;
    private int informeSel;
    InformeEtapa informeEtapa;
    CorreccionWithSol correccion;
    private CreadorFormulario cf1;
    private CreadorFormulario cf2;
    CampoForm campo2;
    private VerInformegenFragmentBinding mBinding;
    NvaCorreViewModel corViewModel;
    private static final String TAG = "VerInformeGenFragment";
    String directorio;
    String tipo;
    String textoboton;
    int numfoto;
    ComprasLog milog;

    public static VerInformeGenFragment newInstance() {
        return new VerInformeGenFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.ver_informegen_fragment, container, false);

        //   mBinding.setMviewModel(mViewModel);
        Log.d(TAG,"en ver informe de correcccion");
        mBinding.setLifecycleOwner(this);
        mViewModel = new ViewModelProvider(this).get(InformesGenViewModel.class);
        corViewModel=new ViewModelProvider(this).get(NvaCorreViewModel.class);
        milog = ComprasLog.getSingleton();
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle=getArguments();
        if(bundle!=null){
            informeSel=bundle.getInt(ListaInformesEtaFragment.INFORMESEL);
            tipo=bundle.getString(ListaInformesEtaFragment.ARG_TIPOCONS);
            numfoto= bundle.getInt(NuevoInfEtapaActivity.NUMFOTO);
            llenarDetalle();

        }



    }

    public void llenarDetalle() {
        if(Constantes.ETAPAACTUAL==1||Constantes.ETAPAACTUAL==4||Constantes.ETAPAACTUAL==6){
           textoboton= getString(R.string.ver_fotos);
        }
        if(Constantes.ETAPAACTUAL==3){
            textoboton= getString(R.string.ver_muestras);
        }
        Log.d(TAG, "llenando detalles"+Constantes.ETAPAACTUAL+tipo+"--"+informeSel);
        if(tipo.equals("rescor")) { //resumen correccion
            corViewModel.getCorreccion(informeSel).observe(getViewLifecycleOwner(), new Observer<Correccion>() {
                @Override
                public void onChanged(Correccion vcorreccion) {
                  //  Log.d(TAG,"---"+vcorreccion.correccion.getSolicitudId()+"--"+vcorreccion.solicitud.getId()+"--"+vcorreccion.correccion.getNumfoto()+"--"+vcorreccion.solicitud.getNumFoto());
                   //busco la solicitud
                   SolicitudCor solicitudCor=corViewModel.getSolicitud(vcorreccion.getSolicitudId(),vcorreccion.getNumfoto());
                    correccion=new CorreccionWithSol();
                    correccion.correccion=vcorreccion;
                    correccion.solicitud=solicitudCor;
                    if(correccion.solicitud.getEtapa()==1) {
                        crearFormCorVarFotos();
                        mBinding.btnverdet.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                verFotos();
                            }
                        });
                    }else {
                        crearFormularioCor();
                        mBinding.igdatosgen.addView(cf1.crearTabla());
                        mBinding.btnverdet.setVisibility(View.GONE);
                    }



                }
            });

        }
        else if(tipo.equals("action_selclitocor2")) {
            if (Constantes.ETAPAACTUAL == 1) {
                corViewModel.getCorreccion(informeSel).observe(getViewLifecycleOwner(), new Observer<Correccion>() {
                    @Override
                    public void onChanged(Correccion vcorreccion) {
                        //  Log.d(TAG,"---"+vcorreccion.correccion.getSolicitudId()+"--"+vcorreccion.solicitud.getId()+"--"+vcorreccion.correccion.getNumfoto()+"--"+vcorreccion.solicitud.getNumFoto());
                        //busco la solicitud
                        SolicitudCor solicitudCor = corViewModel.getSolicitud(vcorreccion.getSolicitudId(), vcorreccion.getNumfoto());
                        correccion = new CorreccionWithSol();
                        correccion.correccion = vcorreccion;
                        correccion.solicitud = solicitudCor;
                        crearFormCorVarFotos();
                      //  mBinding.igdatosgen.addView(cf1.crearTabla());

                        // mBinding.btnverdet.setText(textoboton);
                        mBinding.btnverdet.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                verFotos();
                            }
                        });
                    }
                });

            }else  if (Constantes.ETAPAACTUAL == 3) {
                corViewModel.getCorreccion(informeSel).observe(getViewLifecycleOwner(), new Observer<Correccion>() {
                    @Override
                    public void onChanged(Correccion vcorreccion) {
                         //busco la solicitud
                        SolicitudCor solicitudCor = corViewModel.getSolicitud(vcorreccion.getSolicitudId(), vcorreccion.getNumfoto());
                        correccion = new CorreccionWithSol();
                        correccion.correccion = vcorreccion;
                        correccion.solicitud = solicitudCor;
                        Log.d(TAG,"---"+correccion.correccion.getSolicitudId()+"--"+correccion.solicitud.getId()+"--"+correccion.correccion.getNumfoto()+"--"+correccion.solicitud.getNumFoto());

                        crearFormularioCor();
                        mBinding.igdatosgen.addView(cf1.crearTabla());
                         mBinding.btnverdet.setVisibility(View.GONE);

                    }
                });

            }

        }
        else    //para ver informes etapa
            mViewModel.getInforme(informeSel,Constantes.INDICEACTUAL).observe(getViewLifecycleOwner(), new Observer<InformeEtapa>() {
                @Override
                public void onChanged(InformeEtapa informeEtapax) {
                    informeEtapa=informeEtapax;
                    crearFormularioEta();
                    mBinding.igdatosgen.addView(cf1.crearTabla());
                    mBinding.btnverdet.setText(textoboton);
                    mBinding.btnverdet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            verFotos();
                        }
                    });
                }
            });

    }


    public void crearFormularioCor() {
        String directorio=getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" ;


        List<CampoForm> camposTienda = new ArrayList<CampoForm>();
        CampoForm campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.label = getString(R.string.indice);
        campo.type = "label";
        campo.value = correccion.solicitud.getIndice();

      /*  camposTienda.add(campo);
        campo = new CampoForm();
        campo.label = getString(R.string.consecutivo);
        campo.style = R.style.verinforme2;
        campo.type = "label";
        campo.value = correccion.solicitud.;
        camposTienda.add(campo);*/

        campo = new CampoForm();
        campo.label = getString(R.string.cliente);
        campo.style = R.style.verinforme2;
        campo.type = "label";
        campo.value = correccion.solicitud.getClienteNombre();

        camposTienda.add(campo);
        campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.label = getString(R.string.planta);
        campo.type = "label";
        campo.value = correccion.solicitud.getPlantaNombre();

        camposTienda.add(campo);
        campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.label = getString(R.string.fecha);
        campo.type = "label";
        campo.value = Constantes.vistasdf.format(correccion.correccion.getCreatedAt());
        camposTienda.add(campo);
        if(correccion.solicitud.getEtapa()==2) {
            campo = new CampoForm();
            campo.style = R.style.verinforme2;
            campo.nombre_campo = "tiendaNombre";
            campo.label = getString(R.string.nombre_tienda);
            campo.type = "label";
            campo.value = correccion.solicitud.getNombreTienda();
            camposTienda.add(campo);
        }

        campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.nombre_campo = "descripcion";
        campo.label = getString(R.string.descripcion);
        campo.type = "label";
        campo.value =correccion.solicitud.getDescMostrar();
        camposTienda.add(campo);

        campo = new CampoForm();
        campo.nombre_campo = "foto";
        campo.type = "imagenView";
        campo.value =directorio+correccion.correccion.getRuta_foto1();
        campo.funcionOnClick=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verImagen(correccion.correccion.getRuta_foto1());
            }
        };
        camposTienda.add(campo);

        if(correccion.correccion.getRuta_foto2()!=null&&!correccion.correccion.getRuta_foto2().equals(""))
        {
            campo = new CampoForm();
            campo.nombre_campo = "foto2";
            campo.type = "imagenView";
            campo.value =directorio+correccion.correccion.getRuta_foto2();
            campo.funcionOnClick=new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    verImagen(correccion.correccion.getRuta_foto2());
                }
            };
            camposTienda.add(campo);
        }
        if(correccion.correccion.getRuta_foto3()!=null&&!correccion.correccion.getRuta_foto3().equals(""))
        {
            campo = new CampoForm();
            campo.nombre_campo = "foto3";
            campo.type = "imagenView";
            campo.value =directorio+correccion.correccion.getRuta_foto3();
            campo.funcionOnClick=new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    verImagen(correccion.correccion.getRuta_foto3());
                }
            };
            camposTienda.add(campo);
        }
        if(correccion.correccion.getRuta_foto4()!=null&&!correccion.correccion.getRuta_foto4().equals(""))
        {
            campo = new CampoForm();
            campo.nombre_campo = "foto4";
            campo.type = "imagenView";
            campo.value =directorio+correccion.correccion.getRuta_foto4();
            campo.funcionOnClick=new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    verImagen(correccion.correccion.getRuta_foto4());
                }
            };
            camposTienda.add(campo);
        }
        campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.nombre_campo = "tiendaNombre";
        campo.label = getString(R.string.estatus_envio);
        campo.type = "label";
        campo.value = Constantes.ESTATUSSYNC[correccion.correccion.getEstatusSync()];
        camposTienda.add(campo);



        cf1 = new CreadorFormulario(camposTienda, getActivity());

    }
    //para las correcciones de varias fotos busco por el idsol cuando me llega uno
    //de los idcorrecciones
    public void crearFormCorVarFotos() {
        String directorio=getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" ;

        corViewModel.getCorreccionesxsol(correccion.solicitud.getId(),correccion.solicitud.getIndice())
                .observe(getViewLifecycleOwner(), new Observer<List<Correccion>>() {
            @Override
            public void onChanged(List<Correccion> correccions) {


        List<CampoForm> camposTienda = new ArrayList<CampoForm>();
        CampoForm campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.label = getString(R.string.indice);
        campo.type = "label";
        campo.value = correccion.solicitud.getIndice();


        campo = new CampoForm();
        campo.label = getString(R.string.cliente);
        campo.style = R.style.verinforme2;
        campo.type = "label";
        campo.value = correccion.solicitud.getClienteNombre();

        camposTienda.add(campo);
        campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.label = getString(R.string.planta);
        campo.type = "label";
        campo.value = correccion.solicitud.getPlantaNombre();

        camposTienda.add(campo);
        campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.label = getString(R.string.fecha);
        campo.type = "label";
        campo.value = Constantes.vistasdf.format(correccion.correccion.getCreatedAt());
        camposTienda.add(campo);

        campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.nombre_campo = "descripcion";
        campo.label = getString(R.string.descripcion);
        campo.type = "label";
        campo.value =correccion.solicitud.getDescMostrar();
        camposTienda.add(campo);
        //ciclo para las fotos
        for (Correccion incorr:correccions) {
                    campo = new CampoForm();
                    campo.nombre_campo = "foto";
                    campo.type = "imagenView";
                    campo.value = directorio + incorr.getRuta_foto1();
                    campo.funcionOnClick = new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            verImagen(incorr.getRuta_foto1());
                        }
                    };
                    camposTienda.add(campo);
        }

        campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.nombre_campo = "tiendaNombre";
        campo.label = getString(R.string.estatus_envio);
        campo.type = "label";
        campo.value = Constantes.ESTATUSSYNC[correccion.correccion.getEstatusSync()];
        camposTienda.add(campo);



        cf1 = new CreadorFormulario(camposTienda, getActivity());
                mBinding.igdatosgen.addView(cf1.crearTabla());
                mBinding.btnverdet.setVisibility(View.GONE);
            }
                });
    }

    public void crearFormularioEta() {

        List<CampoForm> camposTienda = new ArrayList<CampoForm>();
        CampoForm campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.label = getString(R.string.indice);
        campo.type = "label";
        campo.value = ComprasUtils.indiceLetra(informeEtapa.getIndice());

      /*  camposTienda.add(campo);
        campo = new CampoForm();
        campo.label = getString(R.string.consecutivor);
        campo.style = R.style.verinforme2;
        campo.type = "label";
        campo.value = informeEtapa.getConsecutivo()+"";
        camposTienda.add(campo);*/
        if(Constantes.ETAPAACTUAL<6) {
            campo = new CampoForm();
            campo.label = getString(R.string.cliente);
            campo.style = R.style.verinforme2;
            campo.type = "label";
            campo.value = informeEtapa.getClienteNombre();

            camposTienda.add(campo);
        }
        campo = new CampoForm();
        campo.style = R.style.verinforme2;
        if(Constantes.ETAPAACTUAL>2){
            campo.label = getString(R.string.ciudad);
            campo.type = "label";
            campo.value = informeEtapa.getCiudadNombre();
        }else {
            campo.label = getString(R.string.planta);
            campo.type = "label";
            campo.value = informeEtapa.getPlantaNombre();
        }

        camposTienda.add(campo);
        campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.label = getString(R.string.fecha);
        campo.type = "label";
        campo.value = Constantes.vistasdf.format(informeEtapa.getCreatedAt());

        camposTienda.add(campo);
        if (Constantes.ETAPAACTUAL==5) {
            //busco detalle
            InformeEnvioDet infenvio= mViewModel.getInformeEnvioDet(informeEtapa.getId());

            campo = new CampoForm();
            campo.style = R.style.verinforme2;
            campo.label = "TOTAL CAJAS";
            campo.type = "label";
            campo.value = informeEtapa.getTotal_cajas() + "";
            camposTienda.add(campo);
            if(infenvio!=null) {
                campo = new CampoForm();
                campo.style = R.style.verinforme2;
                campo.label = "FECHA ENTREGA";
                campo.type = "label";
                campo.value = Constantes.sdfsolofecha.format(infenvio.getFechaEnvio());
                camposTienda.add(campo);
                campo = new CampoForm();
                campo.style = R.style.verinforme2;
                campo.label = "NOMBRE DE QUIEN RECIBE";
                campo.type = "label";
                campo.value = infenvio.getNombreRecibe();
                camposTienda.add(campo);
                campo = new CampoForm();
                campo.style = R.style.verinforme2;
                campo.label = getString(R.string.foto_sello);
                campo.type = "imagenView";
                //busco la foto
                ImagenDetalle foto= mViewModel.getFoto(infenvio.getFotoSello());
               if(foto!=null) {
                   campo.funcionOnClick = new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           verImagen(foto.getRuta());
                       }
                   };
                   campo.value = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" +foto.getRuta();
               }
                camposTienda.add(campo);
                mBinding.btnverdet.setVisibility(View.GONE);
            }


        }


        campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.nombre_campo = "comentarios";
        campo.label = getString(R.string.comentarios);
        switch (Constantes.ETAPAACTUAL) {
            case 1:
                campo.value = informeEtapa.getComentarios();
                break;
            case 3:
                campo.value = informeEtapa.getComentarios();
                break;
            case 4:case 5:case 6:
                campo.value=informeEtapa.getComentarios();
                break;

        }
        campo.type = "label";


        camposTienda.add(campo);

        campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.nombre_campo = "tiendaNombre";
        campo.label = getString(R.string.estatus_envio);
        campo.type = "label";
        campo.value = Constantes.ESTATUSSYNC[informeEtapa.getEstatusSync()];
        camposTienda.add(campo);


        Log.d(TAG,"CAMPOS TOT"+camposTienda);
        cf1 = new CreadorFormulario(camposTienda, getActivity());
        if(Constantes.ETAPAACTUAL==6){
            llenarTablaConcep(informeEtapa.getId());
            mBinding.tblvigastos.setVisibility(View.VISIBLE);
        }
    }

    public void verImagen(String nombrearch){
      //  ImageView imagen=(ImageView)v;
       // imagen.get
        Intent iverim=new Intent(getActivity(), RevisarFotoActivity.class);
        iverim.putExtra(RevisarFotoActivity.IMG_PATH1,nombrearch);
        startActivity(iverim);
    }



    public void onClickVerDet() {
        //ver la muestra
        Bundle bundle = new Bundle();

        bundle.putInt(NuevoinformeFragment.INFORMESEL,informeSel);

       bundle.putInt(ARG_IDDET,informeSel);
        Fragment fragment = new VerInfDetGenFragment();
        fragment.setArguments(bundle);
    // Obtener el administrador de fragmentos a través de la actividad
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    // Definir una transacción
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    // Remplazar el contenido principal por el fragmento
        fragmentTransaction.replace(R.id.back_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
    // Cambiar
        fragmentTransaction.commit();

    }
    public void verFotos(){

        Bundle bundle = new Bundle();
        bundle.putInt(VerInformeFragment.ARG_IDMUESTRA,informeSel);
        bundle.putString(ARG_TIPOINF, tipo);
        Fragment fragment = new GalFotosFragment();
        fragment.setArguments(bundle);
        // Obtener el administrador de fragmentos a través de la actividad
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        // Definir una transacción
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Remplazar el contenido principal por el fragmento
        fragmentTransaction.replace(R.id.back_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        // Cambiar
        fragmentTransaction.commit();
    }

    public void llenarTablaConcep(int infsel){
        TableRow tableRow;
        NvoGastoViewModel niviewModel = new ViewModelProvider(requireActivity()).get(NvoGastoViewModel.class);

        TextView concepto;
        TextView costo;
        float sumacosto=0;
        TableRow.LayoutParams lp1;


        lp1 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, .65f);
        TableRow.LayoutParams  lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, .35f);

        //busco lo capturado
        List<InformeGastoDet> detalles=niviewModel.getGastoDetalles(infsel);
        for (InformeGastoDet detalle:detalles
        ) {
            tableRow=new TableRow(getContext());


            TextView tv1 = new TextView(getActivity());

            concepto=new TextView(getContext());
            costo=new TextView(getContext());
            //  tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            concepto.setLayoutParams(lp1);
            costo.setLayoutParams(lp2);

            concepto.setText(detalle.getConcepto()+"");
            concepto.setBackgroundResource(R.drawable.valuecellborder);
            costo.setText(Constantes.SIMBOLOMON+""+new DecimalFormat("#.##").format(detalle.getImporte()));
            costo.setBackgroundResource(R.drawable.valuecellborder);
            //  concepto.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1));
            //   costo.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1));

            tableRow.addView(concepto);
            tableRow.addView(costo);
            mBinding.tblvigastos.addView(tableRow);
            try {
                sumacosto =sumacosto+ detalle.getImporte();

            }catch(NumberFormatException ex){
                milog.grabarError(TAG+" "+ex.getMessage());
            }


        }

        tableRow=new TableRow(getContext());

        concepto=new TextView(getContext());
        costo=new TextView(getContext());

        tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        costo.setBackgroundResource(R.drawable.valuecellborder);
        concepto.setBackgroundResource(R.drawable.valuecellborder);
        concepto.setText("TOTAL A VALIDAR");
        costo.setText(Constantes.SIMBOLOMON+new DecimalFormat("#.##").format(sumacosto));
        //  concepto.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
        //   costo.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
        concepto.setLayoutParams(lp1);
        costo.setLayoutParams(lp2);
        tableRow.addView(concepto);
        tableRow.addView(costo);
        mBinding.tblvigastos.addView(tableRow);

        //  tableRow=null;
        //  concepto=null;

        //  costo=null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mViewModel=null;

        cf2=null;

         informeEtapa=null;
        cf1=null;

         campo2=null;
         mBinding=null;


         directorio=null;

    }

}