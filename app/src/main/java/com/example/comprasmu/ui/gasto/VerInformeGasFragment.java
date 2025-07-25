package com.example.comprasmu.ui.gasto;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.comprasmu.R;
import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeGastoDet;
import com.example.comprasmu.databinding.VerInformegenFragmentBinding;
import com.example.comprasmu.ui.correccion.NvaCorreViewModel;
import com.example.comprasmu.ui.gallery.GalFotosFragment;
import com.example.comprasmu.ui.infetapa.NuevoInfEtapaActivity;
import com.example.comprasmu.ui.informe.VerInformeFragment;
import com.example.comprasmu.utils.CampoForm;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;
import com.example.comprasmu.utils.ui.InformesGenViewModel;
import com.example.comprasmu.ui.infetapa.ListaInformesEtaFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/***se usa para ver detalle informe gasto ya que se procesó********/
public class VerInformeGasFragment extends Fragment {


    public static final String ARG_TIPOINF = "comprasmu.vie.tipoinf";
    private InformesGenViewModel mViewModel;
    private int informeSel;
    InformeEtapa informeEtapa;
    private CreadorFormulario cf1;
    private CreadorFormulario cf2;
    CampoForm campo2;
    private VerInformegenFragmentBinding mBinding;
    NvaCorreViewModel corViewModel;
    private static final String TAG = "VerInformeGasFragment";
    String directorio;
    String tipo;
    String textoboton;
    int numfoto;
    ComprasLog milog;

    public static VerInformeGasFragment newInstance() {
        return new VerInformeGasFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.ver_informegen_fragment, container, false);

        //   mBinding.setMviewModel(mViewModel);
        Log.d(TAG,"en ver informe de gastos");
        mBinding.setLifecycleOwner(this);
        mViewModel = new ViewModelProvider(this).get(InformesGenViewModel.class);
        corViewModel=new ViewModelProvider(this).get(NvaCorreViewModel.class);
        milog = ComprasLog.getSingleton();
        textoboton= getString(R.string.ver_fotos);
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

                mViewModel.getInforme(informeSel, Constantes.INDICEACTUAL).observe(getViewLifecycleOwner(), new Observer<InformeEtapa>() {
                @Override
                public void onChanged(InformeEtapa informeEtapax) {
                    informeEtapa = informeEtapax;
                    if(informeEtapa!=null){
                    PeticionesServidor ps=new PeticionesServidor(Constantes.CLAVEUSUARIO);
                    ps.getCambiosGastos(Constantes.INDICEACTUAL,informeEtapa.getCiudadNombre(),new ListenerResumen());
                }}
            });


    }



    public void crearFormularioEta(List<InformeGastoDet> detalles) {

        List<CampoForm> camposTienda = new ArrayList<CampoForm>();
        CampoForm campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.label = getString(R.string.indice);
        campo.type = "label";
        campo.value = ComprasUtils.indiceLetra(informeEtapa.getIndice());

        campo = new CampoForm();
        campo.style = R.style.verinforme2;

        campo.label = getString(R.string.ciudad);
        campo.type = "label";
        campo.value = informeEtapa.getCiudadNombre();


        camposTienda.add(campo);
        campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.label = getString(R.string.fecha);
        campo.type = "label";
        campo.value = Constantes.vistasdf.format(informeEtapa.getCreatedAt());

        camposTienda.add(campo);
        campo = new CampoForm();
        campo.style = R.style.verinforme2;
        campo.nombre_campo = "comentarios";
        campo.label = getString(R.string.comentarios);

        campo.value=informeEtapa.getComentarios();

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

        llenarTablaConcep(null, detalles);
            mBinding.tblvigastos.setVisibility(View.VISIBLE);

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

    public void llenarTablaConcep(List<TotalMuestra> totales,  List<InformeGastoDet> detalles){
        TableRow tableRow;
        NvoGastoViewModel niviewModel = new ViewModelProvider(requireActivity()).get(NvoGastoViewModel.class);

        TextView concepto;
        TextView costo;
        float sumacosto=0;
        TableRow.LayoutParams lp1;


        lp1 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, .65f);
        TableRow.LayoutParams  lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, .35f);
        //pongo suma de muestras
        tableRow=new TableRow(getContext());

        concepto=new TextView(getContext());
        costo=new TextView(getContext());
        TextView cliente;
        TextView numuestra;
        int sumamuestras=0;

        tableRow=null;
        cliente=null;
        numuestra=null;
        costo=null;

        Log.d(TAG, detalles.size()+"--"+detalles.toString());
        for (InformeGastoDet detalle:detalles
        ) {
            tableRow=new TableRow(getContext());

            Log.d(TAG,detalle.getConcepto()+"--"+detalle.getImporte());
            TextView tv1 = new TextView(getActivity());

            concepto=new TextView(getContext());
            costo=new TextView(getContext());
            //  tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            concepto.setLayoutParams(lp1);
            costo.setLayoutParams(lp2);

            concepto.setText(detalle.getConcepto());
            concepto.setBackgroundResource(R.drawable.valuecellborder);
            costo.setText(Constantes.SIMBOLOMON+""+new DecimalFormat("#.00").format(detalle.getImporte()));
            costo.setBackgroundResource(R.drawable.valuecellborder);
            concepto.setPadding(30,10,30,10);
            costo.setPadding(30,10,30,10);
            tableRow.addView(concepto);
            tableRow.addView(costo);
            mBinding.tblvigastos.addView(tableRow);
            try {
                sumacosto =sumacosto+ detalle.getImporte();

            }catch(NumberFormatException ex){
                milog.grabarError(TAG+" "+ex.getMessage());
                ex.printStackTrace();
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
        costo.setText(Constantes.SIMBOLOMON+new DecimalFormat("#.00").format(sumacosto));
        concepto.setPadding(30,10,30,10);
        costo.setPadding(30,10,30,10);
        concepto.setLayoutParams(lp1);
        costo.setLayoutParams(lp2);
        tableRow.addView(concepto);
        tableRow.addView(costo);
        mBinding.tblvigastos.addView(tableRow);

        //  tableRow=null;
        //  concepto=null;

        //  costo=null;
    }
    public String buscarMuestras() {

        SharedPreferences prefe = getActivity().getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        String listamuestras = prefe.getString("totalmuestras", "");
        return listamuestras;
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
    public class ListenerResumen implements IListenerResumen{
        @Override
        public void guardarRes(List<InformeGastoDet> respuesta){
            //acomodo en la tabla
            if(respuesta!=null) {

                if(VerInformeGasFragment.this.isAdded()) {
                    crearFormularioEta(respuesta);
                    mBinding.igdatosgen.addView(cf1.crearTabla());
                    mBinding.btnverdet.setText(textoboton);
                    mBinding.btnverdet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            verFotos();
                        }
                    });
                }
            }
            else{
                TextView txtaviso=new TextView(getContext());
                txtaviso.setText("No se pudo obtener la información del servidor, revise su conexión");


                mBinding.igdatosgen.addView(txtaviso);
            }
        }



    }
}