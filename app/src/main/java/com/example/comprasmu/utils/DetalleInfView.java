package com.example.comprasmu.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.Contrato;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.InformeTemp;
import com.example.comprasmu.data.modelos.Reactivo;
import com.example.comprasmu.ui.informedetalle.DetalleProductoFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DetalleInfView extends LinearLayout {

    CreadorFormulario cf;
    List<CampoForm> camposForm;
    EditText textoint;
    Preguntasino preguntasino;
    View respgen;
    Spinner spclientes;
    ImageButton micbtn;
    ImageView fotomos;
    LinearLayout sv;
    CheckBox nopermiso;
    ComprasLog compraslog;
    ImageButton btnrotar;
    InformeTemp ultimares;
    Button aceptar;
    Reactivo preguntaAct;
    boolean isEdicion;
    List<CatalogoDetalle> atributos;
    List<DescripcionGenerica> clientesAsignados;
    OnClickListener tomarFoto;
    OnClickListener frotar;
    Bitmap bitmap1;
    Button validar;
    int idCampo;

    private HashMap<Integer, String> causas;
    private OnClickListener finiciarLecQR;

    public DetalleInfView(Context context) {
        super(context);
        initializeViews(context);
    }

    public DetalleInfView(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);
        initializeViews(context);
    }

    public DetalleInfView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    public DetalleInfView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.detalle_inf_view, this);
        sv = this.findViewById(R.id.vcontent_generic);
        aceptar = this.findViewById(R.id.vbtngaceptar);
        nopermiso = this.findViewById(R.id.vckgnoperm);
        validar = this.findViewById(R.id.vbtngvalidar);
        this.setOrientation(VERTICAL);

    }

    public void crearFormulario() {
        camposForm = new ArrayList<>();
        CampoForm campo = new CampoForm();
        campo.label = preguntaAct.getLabel();
        campo.nombre_campo = preguntaAct.getNombreCampo();
        campo.type = preguntaAct.getType();
        campo.style = R.style.formlabel2;
        Log.d("en la vista","que puse "+isEdicion);
        if (isEdicion)
            campo.value = ultimares.getValor();
        campo.id = 1001;
        if(this.idCampo>0){
            campo.id=this.idCampo;
        }
        //para los catalogos
        if (preguntaAct.getType().equals(CreadorFormulario.SELECTCAT)){
            campo.selectcat = atributos;
        }
        if(preguntaAct.getType().equals(CreadorFormulario.SELECTDES)) {

            if(campo!=null)
                campo.selectdes= clientesAsignados;

        }
        if(preguntaAct.getType().equals(CreadorFormulario.PSELECT)) {

            if(campo!=null)
                campo.select=causas ;

        }


        if (campo.type.equals("agregarImagen")) {

            campo.funcionOnClick = tomarFoto;

            campo.tomarFoto = true;

            fotomos = this.findViewById(R.id.vivgfoto);
            fotomos.setVisibility(View.VISIBLE);
            btnrotar = this.findViewById(R.id.vbtngrotar);
              btnrotar.setOnClickListener(frotar);

            if (isEdicion) {
                // Bitmap bitmap1 = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null) + "/" + nombre_foto);
                //ComprasUtils cu=new ComprasUtils();
                // bitmap1=cu.comprimirImagen(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + ultimares.getValor());
                if (ultimares.getValor().equals("0")) {
                    //veo si tengo el chbox

                    if (nopermiso != null) {
                        nopermiso.setChecked(true);
                        btnrotar.setVisibility(View.GONE);
                    }
                } else {
                    fotomos.setImageBitmap(bitmap1);
                    // fotomos.setLayoutParams(new LinearLayout.LayoutParams(350,150));
                    fotomos.setVisibility(View.VISIBLE);

                    btnrotar.setVisibility(View.VISIBLE);
                    btnrotar.setFocusableInTouchMode(true);
                    btnrotar.requestFocus();
                }

            }
            // btnrotar.setVisibility(View.VISIBLE);


        }
        if (campo.type.equals("botonqr")) {
            campo.funcionOnClick = finiciarLecQR;

        }
        if (Contrato.TablaInformeDet.causa_nocompra.equals(campo.nombre_campo)) {


            campo.select = causas;
        }
       /* if(preguntaAct.getType().equals(CreadorFormulario.PREGUNTASINO)) {
            campo.value

        }*/
        camposForm.add(campo);

        cf = new CreadorFormulario(camposForm, getContext());
        //aqui está el error
        sv.addView(cf.crearFormulario());
        if (campo.type.equals(CreadorFormulario.PSELECT)) {

            spclientes = this.findViewById( campo.id);

        }else
        if (preguntaAct.getType().equals(CreadorFormulario.SELECTCAT) || preguntaAct.getType().equals(CreadorFormulario.SELECTDES) || preguntaAct.getType().equals(CreadorFormulario.PSELECT)) {

            spclientes = this.findViewById( campo.id);
        } else if (preguntaAct.getType().equals(CreadorFormulario.PREGUNTASINO)) {
            preguntasino = this.findViewById( campo.id);
        } else if (preguntaAct.getType().equals(CreadorFormulario.RADIOBUTTON)) {
            respgen = this.findViewById( campo.id);
        } else {
            textoint = this.findViewById( campo.id);
        }

    }

    public void onclickAceptar(OnClickListener clicksi) {
        aceptar.setOnClickListener(clicksi);
    }

    public void onclickRotar(OnClickListener clickno) {
        this.frotar=clickno;
    }

    public void onclickValidar(OnClickListener clicksi) {
        validar.setOnClickListener(clicksi);
    }

    public void onclickBotonQr(OnClickListener clicksi) {
        this.finiciarLecQR = clicksi;
    }

    public void setImageView(Bitmap bitmap) {
        this.bitmap1 = bitmap;
    }

    public Reactivo getPreguntaAct() {
        return preguntaAct;
    }

    public void setPreguntaAct(Reactivo preguntaAct) {
        this.preguntaAct = preguntaAct;
    }

    public boolean isEdicion() {
        return isEdicion;
    }

    public void setEdicion(boolean edicion) {
        isEdicion = edicion;
    }

    public List<CatalogoDetalle> getAtributos() {
        return atributos;
    }

    public void setAtributos(List<CatalogoDetalle> atributos) {
        this.atributos = atributos;
    }

    public OnClickListener getTomarFoto() {
        return tomarFoto;
    }

    public void setTomarFoto(OnClickListener tomarFoto) {
        this.tomarFoto = tomarFoto;
    }

    public InformeTemp getUltimares() {
        return ultimares;
    }

    public void setUltimares(InformeTemp ultimares) {
        this.ultimares = ultimares;
    }

    public HashMap<Integer, String> getCausas() {
        return causas;
    }

    public void setCausas(HashMap<Integer, String> causas) {
        this.causas = causas;
    }

    public List<DescripcionGenerica> getClientesAsignados() {
        return clientesAsignados;
    }

    public void setClientesAsignados(List<DescripcionGenerica> clientesAsignados) {
        this.clientesAsignados = clientesAsignados;
    }

    public Object getSelectedItem(){
        return spclientes.getSelectedItem();
    }

    public Editable getTextoint() {
        return textoint.getText();
    }

    public void setTextoint(String texto) {
        this.textoint.setText(texto);
    }

    public RadioGroup getRadioGroup() {
        return (RadioGroup) respgen;
    }

    public void setRespgen(View respgen) {
        this.respgen = respgen;
    }

    public boolean getPregSiNoResp(){
        return preguntasino.getRespuesta();
    }

    public Spinner getSpclientes() {
        return spclientes;
    }
    public void setImageBitmap(Bitmap imagen){
        fotomos.setImageBitmap(imagen);
        // fotomos.setLayoutParams(new LinearLayout.LayoutParams(350,150));
        fotomos.setVisibility(View.VISIBLE);
    }
    public void verBtnRotar(){
        btnrotar.setVisibility(View.VISIBLE);
        btnrotar.setFocusableInTouchMode(true);

    }

    public void setSpclientes(Spinner spclientes) {
        this.spclientes = spclientes;
    }

    /*   public void eliminarFoto()
               {
                   txtruta.setText("");
                   foto.setImageBitmap(null);

                   foto.setVisibility(View.GONE);
                   btnrotar.setVisibility(View.GONE);
                   aceptar.setEnabled(true);
               }
               public void noeliminarFoto(){
                   ((CheckBox) cb).setChecked(false);
               }*/
    public void setNopermiso(DetalleProductoFragment.PregBorraFoto preg) {

        nopermiso.setVisibility(View.VISIBLE);

        nopermiso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textoint.getText() != null)
                    aceptar.setEnabled(((CheckBox) view).isChecked());
                else
                    aceptar.setEnabled(true);

                preg.preguntarBorrarFoto(view, textoint, fotomos, btnrotar, null);
            }
        });
    }

    public CheckBox getNopermiso() {
        return nopermiso;
    }

    public void setNopermiso(CheckBox nopermiso) {
        this.nopermiso = nopermiso;
    }

    public void aceptarSetEnabled(boolean value) {
        aceptar.setEnabled(value);
    }

    public void miaddTextChangedListener() {

      textoint.addTextChangedListener(new

    TextWatcher() {
        @Override
        public void beforeTextChanged (CharSequence charSequence,int i, int i1, int i2){

        }
        boolean mEditing = false;


        public synchronized void afterTextChanged (Editable s){
            if (!mEditing) {
                mEditing = true;


            }
        }

        @Override
        public void onTextChanged (CharSequence charSequence,int i, int i1, int i2){
            //count es cantidad de caracteres que tiene
            aceptar.setEnabled(charSequence.length() > 0);

        }


    });

}
    public void miBotonTextWatcher() {
        textoint.addTextChangedListener(new BotonTextWatcher());
    }

    public boolean hayTextoInt(){
        return textoint!=null;
    }
    public boolean haySPClientes(){
        return spclientes!=null;
    }

    public boolean hayPreguntaSino(){
        return preguntasino !=null;
    }
    public boolean hayRespGen(){
        return respgen!=null;
    }

    public void setRadioGrupListener(RadioGroup.OnCheckedChangeListener listener){
        RadioGroup botones=(RadioGroup)respgen;
        botones.setOnCheckedChangeListener(listener);
    }

    public Preguntasino getPreguntasino() {
        return preguntasino;
    }

    public ImageView getFotomos() {
        return fotomos;
    }

    public void setTextFilters(){
        textoint.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

    }

    public void setEnviarButton(String enviar){
        aceptar.setText(enviar);
        aceptar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.botonvalido));

    }

    public void setSpClientesListener(AdapterView.OnItemSelectedListener listener){
        spclientes.setOnItemSelectedListener(listener);
    }
    public void setPregSINoOnChange(RadioGroup.OnCheckedChangeListener listener){
        preguntasino.setOnCheckedChangeListener(listener);
    }

    public int getIdCampo() {
        return idCampo;
    }

    public void setIdCampo(int idCampo) {
        this.idCampo = idCampo;
    }

    class BotonTextWatcher implements TextWatcher {

        boolean mEditing;

        public BotonTextWatcher() {
            mEditing = false;
        }

        public synchronized void afterTextChanged(Editable s) {

        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //count es cantidad de caracteres que tiene
            aceptar.setEnabled(charSequence.length() > 0);

        }


    }

}
