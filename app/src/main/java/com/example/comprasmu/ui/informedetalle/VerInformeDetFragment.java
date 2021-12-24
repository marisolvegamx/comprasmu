package com.example.comprasmu.ui.informedetalle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.Atributo;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.Contrato;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.ui.informe.NuevoinformeFragment;
import com.example.comprasmu.ui.informe.NuevoinformeViewModel;
import com.example.comprasmu.utils.CampoForm;
import com.example.comprasmu.utils.CreadorFormulario;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class VerInformeDetFragment extends Fragment {
    public LiveData<InformeCompraDetalle> informeSel;
    private VerInformeDetViewModel mViewModel;
    private NuevoDetalleViewModel dViewModel;
    private NuevoinformeViewModel niViewModel;
   int idInforme;
    CreadorFormulario cf;
    CreadorFormulario cf2;
    List<CampoForm> camposForm;
    private List<CatalogoDetalle> tomadoDe;
    private List<Atributo>atributos;
    ImagenDetalle[] fotos;
    MutableLiveData<Integer> cont;

    SimpleDateFormat sdf;
    View root;
    int i;
    private static final String TAG="VerInformeDetFRAGMENT";


    LinearLayout sv2;
    LinearLayout sv1;
    private int[] arrCampos={R.id.txtdpfecha_caducidad,R.id.txtdpcodigo_producto,R.string.form_detalle_producto_origen,R.string.form_detalle_producto_costo,R.string.form_detalle_producto_foto_cod,
            R.string.form_detalle_producto_energia,R.string.form_detalle_producto_foto_num,R.string.form_detalle_producto_marca_tras,
            R.string.form_detalle_producto_atributo_a,R.string.form_detalle_producto_atributob,R.string.form_detalle_producto_atributoc,
            R.string.form_detalle_producto_etiqueta
    };


    public static VerInformeDetFragment newInstance() {
        return new VerInformeDetFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
         root= inflater.inflate(R.layout.ver_informe_det_fragment, container, false);
        cont=new MutableLiveData<>();
         return  root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(VerInformeDetViewModel.class);
        dViewModel=new ViewModelProvider(this).get(NuevoDetalleViewModel.class);


         niViewModel=new ViewModelProvider(this).get(NuevoinformeViewModel.class);

        Log.d("VerInformeDFragment1","creando fragment");

        sdf=new SimpleDateFormat("dd-MMM-yy");
        // Inflate the layout for this fragment
        startui();

    }


    public void startui(){
        sv1=root.findViewById(R.id.vpcontentform);
        sv2=root.findViewById(R.id.vpcontentform2);
        Bundle params = getArguments();

        idInforme= params.getInt(NuevoinformeFragment.ARG_NUEVOINFORME);
       int idmuestra=params.getInt(DetalleProductoFragment1.ARG_IDMUESTRA);


        informeSel= dViewModel.getMuestra(idmuestra);

        informeSel.observe(getViewLifecycleOwner(), new Observer<InformeCompraDetalle>() {
          @Override
           public void onChanged(InformeCompraDetalle informeCompraDetalle) {
              ponerDatos(informeCompraDetalle);


            }
        });





    }

    public void ponerDatos(InformeCompraDetalle informe) {

        fotos= new ImagenDetalle[10];
        i=0;


        ponerFoto(informe.getFoto_codigo_produccion());

       // ponerFoto(informe.getEnergia());
        ponerFoto(informe.getFoto_num_tienda());
        ponerFoto(informe.getMarca_traslape());
        ponerFoto(informe.getFoto_atributoa());
        ponerFoto(informe.getFoto_atributob());
        ponerFoto(informe.getFoto_atributoc());
        ponerFoto(informe.getEtiqueta_evaluacion());
       // ponerFoto(informe.getQr());
        ponerFoto(informe.getAzucares());
        tomadoDe=dViewModel.cargarCatalogos();
        dViewModel.atributos=dViewModel.getAtributos();
        dViewModel.atributos.observe(getViewLifecycleOwner(), new Observer<List<Atributo>>() {

            @Override
            public void onChanged(List<Atributo> catalogoDetalles) {
                Log.d(TAG,"ya tengo atributos");
                atributos = catalogoDetalles;

            cont.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d(TAG,"cont="+integer);
                if(integer==8) {
                    Log.d(TAG,"dibujando form");
                    crearFormulario(informe);
                    sv1.addView(cf.crearTabla());
                    sv2.addView(cf2.crearTabla());
                }
            }
        });
            }
        });


    }
    //nombre del txt dela ruta
    //id de imagendetalle
    //id imageview
    //objeto imagendetalle

    public void ponerFoto( int idfoto){
        niViewModel.getFotoLD(idfoto).observe(this, new Observer<ImagenDetalle>() {
                @Override
                public void onChanged(ImagenDetalle s) {
                    if(s!=null) {
                        Log.d(TAG,   "--" + s.getRuta());
                     fotos[i]=s;


                    }
                    i++;
                    cont.setValue(i);
                }
            });



    }
    public void crearFormulario(InformeCompraDetalle detalle){
        String directorio=getActivity().getExternalFilesDir(null) + "/" ;
        Log.d(TAG,"haciendo formulario");

        camposForm= new ArrayList<CampoForm>();
        this.crearCampo("NUM. MUESTRA",detalle.getNumMuestra()+"");
        this.crearCampo("PRODUCTO",detalle.getProducto()+" "+detalle.getEmpaque()+" "+detalle.getPresentacion());
        this.crearCampo("ANALISIS",detalle.getNombreAnalisis());
        this.crearCampo("TIPO MUESTRA",detalle.getNombreTipoMuestra());

        CampoForm campo=new CampoForm();
        campo.label=getString(R.string.fecha_caducidad);
        campo.nombre_campo=Contrato.TablaInformeDet.COSTO;
        campo.type="label";
        campo.value=sdf.format(detalle.getCaducidad());

        camposForm.add(campo);

       this.crearCampo(getString(R.string.codigo_producto),detalle.getCodigo());
        cf=new CreadorFormulario(camposForm,getContext());
        camposForm= new ArrayList<CampoForm>();
         campo=new CampoForm();
        campo.label=getString(R.string.origen);
        campo.nombre_campo= Contrato.TablaInformeDet.ORIGEN;
         campo.type="label";

        campo.value=buscarCatValor(detalle.getOrigen(),tomadoDe);

        camposForm.add(campo);
        campo=new CampoForm();
        campo.label=getString(R.string.costo);

         campo.type="label";
        campo.value= detalle.getCosto();

        camposForm.add(campo);
    /*
        campo=new CampoForm();
        campo.label=getString(R.string.azucareS);

        campo.type="imagenView";
        campo.value=fotos[7]!=null?directorio+fotos[7].getRuta():"";

        camposForm.add(campo);
     /*   campo=new CampoForm();
        campo.label=getString(R.string.energia);
        campo.nombre_campo=Contrato.TablaInformeDet.ENERGIA;
        campo.type="imagenView";
        campo.value=fotos[1]!=null?directorio+fotos[1].getRuta():"";

        camposForm.add(campo);
*/
   /*     campo=new CampoForm();
        campo.label=getString(R.string.foto_num_tienda);

        campo.type="imagenView";
        campo.value=fotos[2]!=null?directorio+fotos[2].getRuta():"";


        camposForm.add(campo);

        campo=new CampoForm();
        campo.label=getString(R.string.marca_traslape);
        campo.nombre_campo=Contrato.TablaInformeDet.MARCA_TRASLAPE;
        campo.type="imagenView";
        campo.value=fotos[3]!=null?directorio+fotos[3].getRuta():"";
        camposForm.add(campo);*/
        if(detalle.getAtributoa()!=null&&!detalle.getAtributoa().equals("")) {
            campo = new CampoForm();
            campo.label = getString(R.string.atributoa);

            campo.type = "label";

            campo.value = buscarAtr(detalle.getAtributoa(), atributos);

            camposForm.add(campo);
         /*   campo = new CampoForm();
            campo.label = getString(R.string.foto_atributoa);
            campo.nombre_campo = Contrato.TablaInformeDet.FOTO_ATRIBUTOA;
            campo.type = "imagenView";

            campo.value = fotos[4] != null ? directorio + fotos[4].getRuta() : "";


            camposForm.add(campo);*/
        }
        if(detalle.getAtributob()!=null&&!detalle.getAtributob().equals("")) {
            campo = new CampoForm();
            campo.label = getString(R.string.atributob);
            campo.nombre_campo = Contrato.TablaInformeDet.ATRIBUTOB;
            campo.type = "label";

            campo.value = buscarAtr(detalle.getAtributob(), atributos);

            camposForm.add(campo);
        /*    campo = new CampoForm();
            campo.label = getString(R.string.foto_atributob);
            campo.nombre_campo = Contrato.TablaInformeDet.FOTO_ATRIBUTOB;
            campo.type = "imagenView";
            campo.value = fotos[5] != null ? directorio + fotos[5].getRuta() : "";

            camposForm.add(campo);*/
        }
        if(detalle.getAtributoc()!=null&&!detalle.getAtributoc().equals("")) {
            campo = new CampoForm();
            campo.label = getString(R.string.atributoc);
            campo.nombre_campo = Contrato.TablaInformeDet.ATRIBUTOC;
            campo.type = "label";

            campo.value = buscarAtr(detalle.getAtributoc(), atributos);

         /*   camposForm.add(campo);
            campo = new CampoForm();
            campo.label = getString(R.string.foto_atributoc);
            campo.nombre_campo = Contrato.TablaInformeDet.FOTO_ATRIBUTOC;
            campo.type = "imagenView";
            campo.value = fotos[6] != null ? directorio + fotos[6].getRuta() : "";

            camposForm.add(campo);*/

        }
        campo=new CampoForm();
        campo.label="QR";

        campo.type="label";
        campo.value=detalle.getQr();

        camposForm.add(campo);
        campo=new CampoForm();
        campo.label=getString(R.string.foto_codigo_produccion);
        campo.type="imagenView";
        campo.value=fotos[0]!=null?directorio+fotos[0].getRuta():"";
        campo.required="required";
        camposForm.add(campo);
        campo = new CampoForm();
        campo.label = getString(R.string.foto_posicion1);
        campo.nombre_campo = Contrato.TablaInformeDet.FOTO_ATRIBUTOA;
        campo.type = "imagenView";

        campo.value = fotos[4] != null ? directorio + fotos[4].getRuta() : "";
        camposForm.add(campo);
        campo = new CampoForm();
        campo.label = getString(R.string.foto_posicion2);
        campo.nombre_campo = Contrato.TablaInformeDet.FOTO_ATRIBUTOB;
        campo.type = "imagenView";
        campo.value = fotos[5] != null ? directorio + fotos[5].getRuta() : "";
        camposForm.add(campo);

        campo = new CampoForm();
        campo.label = getString(R.string.foto_posicion3);
        campo.nombre_campo = Contrato.TablaInformeDet.FOTO_ATRIBUTOC;
        campo.type = "imagenView";
        campo.value = fotos[6] != null ? directorio + fotos[6].getRuta() : "";

        camposForm.add(campo);

        campo=new CampoForm();
        campo.label=getString(R.string.etiqueta_evaluacion);

        campo.type="imagenView";
        campo.value=fotos[7]!=null?directorio+fotos[7].getRuta():"";

        camposForm.add(campo);


        cf2=new CreadorFormulario(camposForm,getContext());

    }

    private void crearCampo(String label, String value){
        CampoForm campo=new CampoForm();
        campo.label=label;

        campo.type="label";
        campo.value=value;

        camposForm.add(campo);
    }

    public String buscarCatValor(String opcion,List<CatalogoDetalle> cat){
        int op= Integer.parseInt(opcion);
        for(CatalogoDetalle valores:cat){
            if(op==valores.getCad_idopcion()){
                return valores.getCad_descripcionesp();
            }
        }
        return "";

    }
    public String buscarAtr(String opcion,List<Atributo> cat){

        int op= Integer.parseInt(opcion);
        if(cat!=null)
        for(Atributo valores:cat){
            if(op==valores.getId_atributo()){
                return valores.getAt_nombre();
            }
        }
        return "";

    }

}