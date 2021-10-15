package com.example.comprasmu.ui.informe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.Contrato;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.utils.CampoForm;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/***clase para mostrar los campos que faltan de capturar de la muestra***/

public class DetalleProductoFragment extends Fragment {

    private NuevoinformeViewModel mViewModel;
    CreadorFormulario cf;
    List<CampoForm> camposForm;
    private HashMap<Integer,String> tomadoDe;
    private HashMap<Integer,String> atributos;
    MenuItem fav;
    private static final int SELECT_FILE = 1;
    SimpleDateFormat sdf;
    View root;
    private int numMuestra;
    boolean isSegunda;
    boolean isTercera;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
         sdf = new SimpleDateFormat("dd-mm-yyyy");
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel =
                new ViewModelProvider(this).get(NuevoinformeViewModel.class);

         root = inflater.inflate(R.layout.fragment_generic, container, false);
        /**llegan los datos del producto el cliente y la planta seleccionada
         * desde la lista de compra
         */
        try {

        //    mViewModel.icdNuevo.setPlantaNombre("planta1");
         //   mViewModel.icdNuevo.setPlantasId(1);
//            mViewModel.icdNuevo.setProductoId(1);
//            mViewModel.icdNuevo.setProducto("pepsi");
//            mViewModel.icdNuevo.setEmpaque("PET");
//            mViewModel.icdNuevo.setCaducidad(sdf.parse("23-5-2021"));
//            mViewModel.icdNuevo.setCodigo("12345");
//            mViewModel.icdNuevo.setPresentacion("200ml");
//            mViewModel.icdNuevo.setBackup(false);
            LinearLayout sv = root.findViewById(R.id.content_generic);
         //   mViewModel.cargarCatsContinuar();
            tomadoDe = new HashMap();
            atributos = new HashMap();
            crearFormulario();
            numMuestra=1;

            sv.addView(cf.crearFormulario());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return root;
    }

    public void crearFormulario(){
         camposForm= new ArrayList<CampoForm>();
        CampoForm campo=new CampoForm();
        campo=new CampoForm();
        campo.label="Cliente";

        campo.type="label";
       // campo.value=mViewModel.icdNuevo.getClienteNombre();

        camposForm.add(campo);

        campo=new CampoForm();
        campo.label="Planta";
        campo.type="label";
      //  campo.value=mViewModel.icdNuevo.getPlantaNombre();

        camposForm.add(campo);

        campo=new CampoForm();
        campo.label="Producto";
        campo.type="label";
//        campo.value=mViewModel.icdNuevo.getProducto();
//
//        camposForm.add(campo);
//        campo=new CampoForm();
//        campo.label="Presentación";
//        campo.type="label";
//        campo.value=mViewModel.icdNuevo.getPresentacion()+" "+mViewModel.icdNuevo.getEmpaque();
//
//        camposForm.add(campo);
//        campo=new CampoForm();
//        campo.label="Código producción";
//        campo.type="label";
//        campo.value=mViewModel.icdNuevo.getCodigo();

        camposForm.add(campo);
        campo=new CampoForm();
        campo.label="Fecha de caducidad";
        campo.type="label";
//        campo.value=sdf.format(mViewModel.icdNuevo.getCaducidad());
//
//        camposForm.add(campo);
//        campo=new CampoForm();
//        campo.label="Backup";
//        campo.type="label";
//        campo.value=mViewModel.icdNuevo.isBackup()+"";

        camposForm.add(campo);


        campo=new CampoForm();
        campo.label="Origen";
        campo.nombre_campo= Contrato.TablaInformeDet.ORIGEN;
        campo.type="inputtext";
        campo.value=null;
        campo.id=R.string.form_detalle_producto_origen;
        campo.required="required";
        camposForm.add(campo);
        campo=new CampoForm();
        campo.label="Costo";
        campo.nombre_campo=Contrato.TablaInformeDet.COSTO;
        campo.type="inputtext";
        campo.value=null;
        campo.required="required";
        campo.id=R.string.form_detalle_producto_costo;
        camposForm.add(campo);
        campo=new CampoForm();
        campo.label="Foto Código producción";
        campo.nombre_campo=Contrato.TablaInformeDet.FOTOCODIGOPRODUCCION;
        campo.type="agregarimagen";
        campo.value=null;
        campo.required="required";
        campo.id=R.string.form_detalle_producto_foto_cod;

        camposForm.add(campo);
        campo=new CampoForm();
        campo.label="Energía";
        campo.nombre_campo=Contrato.TablaInformeDet.ENERGIA;
        campo.type="agregarimagen";
        campo.value=null;
        campo.required="required";
        campo.id=R.string.form_detalle_producto_energia;
        camposForm.add(campo);

        campo=new CampoForm();
        campo.label="Foto num. tienda";
        campo.nombre_campo= Contrato.TablaInformeDet.FOTONUMTIENDA;
        campo.type="agregarimagen";
        campo.value=null;
        campo.required="required";
        campo.id=R.string.form_detalle_producto_foto_num;
        camposForm.add(campo);
        campo=new CampoForm();
        campo.label="Marca traslape";
        campo.nombre_campo=Contrato.TablaInformeDet.MARCA_TRASLAPE;
        campo.type="agregarimagen";
        campo.value=null;
        campo.required="required";
        campo.id=R.string.form_detalle_producto_marca_tras;
        camposForm.add(campo);
        campo=new CampoForm();
        campo.label="Atributo A";
        campo.nombre_campo=Contrato.TablaInformeDet.ATRIBUTOA;
        campo.type="inputtext";
        campo.value=null;
        campo.required="required";
        campo.id=R.string.form_detalle_producto_atributo_a;
        camposForm.add(campo);
        campo=new CampoForm();
        campo.label="Foto atributo A";
        campo.nombre_campo=Contrato.TablaInformeDet.FOTO_ATRIBUTOA;
        campo.type="agregarimagen";
        campo.value=null;
        campo.required="required";
        campo.id=R.string.form_detalle_producto_foto_atributoa;
        camposForm.add(campo);

        campo=new CampoForm();
        campo.label="Atributo B";
        campo.nombre_campo=Contrato.TablaInformeDet.ATRIBUTOB;
        campo.type="inputtext";
        campo.value=null;
        campo.required="required";
        campo.id=R.string.form_detalle_producto_atributob;
        camposForm.add(campo);
        campo=new CampoForm();
        campo.label="Foto atributo B";
        campo.nombre_campo=Contrato.TablaInformeDet.FOTO_ATRIBUTOB;
        campo.type="agregarimagen";
        campo.value=null;
        campo.required="required";
        campo.id=R.string.form_detalle_producto_foto_atributob;
        camposForm.add(campo);
        campo=new CampoForm();
        campo.label="Atributo C";
        campo.nombre_campo=Contrato.TablaInformeDet.ATRIBUTOC;
        campo.type="inputtext";
        campo.value=null;
        campo.required="required";
        campo.id=R.string.form_detalle_producto_atributoc;
        camposForm.add(campo);
        campo=new CampoForm();
        campo.label="Foto atributo C";
        campo.nombre_campo=Contrato.TablaInformeDet.FOTO_ATRIBUTOC;
        campo.type="agregarimagen";
        campo.value=null;
        campo.required="required";
        campo.id=R.string.form_detalle_producto_foto_atributoc;
        camposForm.add(campo);
        campo=new CampoForm();
        campo.label="Etiqueta evaluación";
        campo.nombre_campo=Contrato.TablaInformeDet.ETIQUETA_EVALUACION;
        campo.type="agregarimagen";
        campo.value=null;
        campo.required="required";
        campo.id=R.string.form_detalle_producto_etiqueta;
        camposForm.add(campo);

        cf=new CreadorFormulario(camposForm,getContext());





    }

    public void regresar(){

    }

    public void agregarMuestra() {
        //TODO validaciones

        EditText costo = root.findViewById(R.string.form_detalle_producto_costo);
        EditText energia = root.findViewById(R.string.form_detalle_producto_energia);
        EditText etiqueta = root.findViewById(R.string.form_detalle_producto_etiqueta);

        //agrega los campos nuevos
//        mViewModel.icdNuevo.setOrigen( root.findViewById(R.string.form_detalle_producto_origen).toString());
//        mViewModel.icdNuevo.setCosto(root.findViewById(R.string.form_detalle_producto_costo).toString());
//
//        mViewModel.icdNuevo.setAtributoa(root.findViewById(R.string.form_detalle_producto_atributo_a).toString());
//        mViewModel.icdNuevo.setAtributob(root.findViewById(R.string.form_detalle_producto_atributob).toString());
//        mViewModel.icdNuevo.setAtributoc(root.findViewById(R.string.form_detalle_producto_atributoc).toString());
//        mViewModel.icdNuevo.setComentarios(root.findViewById(R.string.form_detalle_producto_comentarios).toString());
//        mViewModel.icdNuevo.setNumMuestra(numMuestra);
//        //las fotos se agregan al model

//        mViewModel.energia = crearImagen(root.findViewById(R.string.form_detalle_producto_energia).toString(), Contrato.TablaInformeDet.ENERGIA);
//        mViewModel.foto_codigo_produccion = crearImagen(root.findViewById(R.string.form_detalle_producto_foto_cod).toString(), Contrato.TablaInformeDet.FOTOCODIGOPRODUCCION);
//        mViewModel.foto_num_tienda = crearImagen(root.findViewById(R.string.form_detalle_producto_foto_num).toString(), Contrato.TablaInformeDet.FOTONUMTIENDA);
//        mViewModel.marca_traslape = crearImagen(root.findViewById(R.string.form_detalle_producto_marca_tras).toString(), Contrato.TablaInformeDet.MARCA_TRASLAPE);
//        mViewModel.foto_atributoa = crearImagen(root.findViewById(R.string.form_detalle_producto_foto_atributoa).toString(), Contrato.TablaInformeDet.FOTO_ATRIBUTOA);
//        mViewModel.foto_atributob = crearImagen(root.findViewById(R.string.form_detalle_producto_foto_atributob).toString(), Contrato.TablaInformeDet.FOTO_ATRIBUTOB);
//        mViewModel.foto_atributoc = crearImagen(root.findViewById(R.string.form_detalle_producto_foto_atributoc).toString(), Contrato.TablaInformeDet.FOTO_ATRIBUTOC);
//        mViewModel.etiqueta_evaluacion = crearImagen(etiqueta.toString(), Contrato.TablaInformeDet.ETIQUETA_EVALUACION);
//        mViewModel.saveDetalle2();

    }

    public ImagenDetalle crearImagen(String ruta, String descripcion){
        ImagenDetalle     foto=new ImagenDetalle();
        foto.setRuta(ruta);
        foto.setDescripcion(descripcion);
        foto.setEstatus(1);

        foto.setEstatusSync(0);
        foto.setCreatedAt(new Date());
        return foto;
    }


}