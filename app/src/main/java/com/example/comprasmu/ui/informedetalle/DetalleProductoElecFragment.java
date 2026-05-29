package com.example.comprasmu.ui.informedetalle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.Contrato;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeTemp;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.Reactivo;
import com.example.comprasmu.ui.BackActivity;
import com.example.comprasmu.ui.informe.NuevoinformeViewModel;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.ui.visita.AbririnformeFragment;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;
import com.example.comprasmu.utils.Preguntasino;
import com.example.comprasmu.utils.ui.LoadingDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class DetalleProductoElecFragment extends DetalleProductoPenFragment{
    protected static final String TAG="DETALLEPRODUCTOELECFRAG";
    public final static String ARG_PREGACTE="comprasmu.ni_pregacte";
    public final static String ARG_ESEDIE="comprasmu.ni_esedie";
    public DetalleProductoElecFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel =
                new ViewModelProvider(requireActivity()).get(NuevoinformeViewModel.class);
        lcviewModel = new ViewModelProvider(this).get(ListaDetalleViewModel.class);

        dViewModel=new ViewModelProvider(requireActivity()).get(NuevoDetalleViewModel.class);
        root = inflater.inflate(R.layout.fragment_generic, container, false);
        /**llegan los datos del producto el cliente y la planta seleccionada
         * desde la lista de compra
         */
        try {

            sv = root.findViewById(R.id.content_generic);
            aceptar = root.findViewById(R.id.btngaceptar);
            linearLayoutCausas=root.findViewById(R.id.fgslllistacausas);
            int num_pregact=0;
            if (getArguments() != null) {
                num_pregact = getArguments().getInt(ARG_PREGACTE);
                this.isEdicion = getArguments().getBoolean(ARG_ESEDIE);
            }
            preguntaAct= dViewModel.buscarReactivoSimpl(num_pregact);
            Log.i(TAG,"creando fragment "+preguntaAct.getId());
            dViewModel.reactivoAct=preguntaAct.getId();
            //   mViewModel.cargarCatsContinuar();
            //si es la misma
            //reviso si es edicion o es nueva
            if(preguntaAct.getTabla().equals("I"))
                mViewModel.numMuestra=0;
            if(this.preguntaAct!=null)
                ultimares=dViewModel.buscarxNombreCam(this.preguntaAct.getNombreCampo(),mViewModel.numMuestra);
            Log.d(TAG,"------"+ Constantes.NM_TOTALISTA+"---"+mViewModel.consecutivo);
            isEdicion = ultimares != null;

            buscarDatosGenerales();
            if(isEdicion) {
                aceptar.setEnabled(true);

                mViewModel.consecutivo=ultimares.getConsecutivo();
                Constantes.DP_CONSECUTIVO=mViewModel.consecutivo;
                //  ya lo busco en la actividad
                //   InformeTemp inf= dViewModel.buscarxNombreCam("numMuestra");
                //     mViewModel.numMuestra=inf==null?0:Integer.parseInt(inf.getValor());

                reiniciarDatos();


                //busco el total de prods en la lista
                if(Constantes.NM_TOTALISTA==0) {
                    InformeTemp resp = dViewModel.buscarxNombreCam("totalLista");
                    String valor = "";
                    if (resp != null) {
                        Constantes.NM_TOTALISTA = Integer.parseInt(resp.getValor());

                    }
                }
                if(preguntaAct.getId()==89||preguntaAct.getId()==87){
                    //no puedo modificar  avanzo a la siguiente

                    preguntaAct=dViewModel.buscarReactivoxId(preguntaAct.getSigId());
                    Log.d(TAG,"mmmmmmmmmmm"+preguntaAct.getId());
                    InformeTemp inft=dViewModel.buscarxNombreCam("informeid");
                    if(inft!=null) {
                        mViewModel.setIdInformeNuevo(Integer.parseInt(inft.getValor()));
                        mViewModel.consecutivo = inft.getConsecutivo();
                        Constantes.DP_CONSECUTIVO=mViewModel.consecutivo;
                    }
                    inft=dViewModel.buscarxNombreCam(Contrato.TablaInformeDet.causa_nocompra);
                    //    Log.d(TAG,"causa no compra="+inft.getNombre_campo());
                    if(inft!=null) {
                        mViewModel.informe.setSinproducto(true);
                        mViewModel.informe.setCausa_nocompra(inft.getValor());
                    }
                    ultimares=null;
                    isEdicion=false;

                }

            }
            else {
                aceptar.setEnabled(false);
            }
            iniciarNumMuestra();

            crearFormulario();

            if(preguntaAct.getType().equals(CreadorFormulario.SELECTCAT)||preguntaAct.getType().equals(CreadorFormulario.SELECTDES)||preguntaAct.getType().equals(CreadorFormulario.PSELECT)) {
                spclientes = root.findViewById(1001);
            }
            else
            if(preguntaAct.getType().equals(CreadorFormulario.PREGUNTASINO))
            {  pregunta=root.findViewById(1001);
            }else if(preguntaAct.getType().equals(CreadorFormulario.RADIOBUTTON))
            {  respgen=root.findViewById(1001);
            }else
            {
                textoint = root.findViewById(1001);
            }
            if(preguntaAct.getNombreCampo().equals("ticket_compra")) {
                nopermiso=root.findViewById(R.id.ckgnoperm);
                nopermiso.setVisibility(View.VISIBLE);

                nopermiso.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(textoint.getText()!=null)
                            aceptar.setEnabled(((CheckBox)view).isChecked());
                        else
                            aceptar.setEnabled(true);

                        preguntarBorrarFoto(view,textoint,fotomos,btnrotar);
                    }
                });
            }
            if(preguntaAct.getType().equals(CreadorFormulario.FECHAMASK)){
                textoint.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }
                    boolean mEditing=false;



                    public synchronized void afterTextChanged(Editable s) {
                        if(!mEditing) {
                            mEditing = true;


                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        //count es cantidad de caracteres que tiene
                        aceptar.setEnabled(charSequence.length() > 0);

                    }


                });
            }else
            if(textoint!=null&&preguntaAct.getId()!=77){ //los comentarios no son obligatorios
                textoint.addTextChangedListener(new BotonTextWatcher());

            }
            if(preguntaAct.getId()==77){ //los comentarios no son obligatorios
                //  textoint.addTextChangedListener(new MayusTextWatcher());

                //veo si ya tengo informe
                    mViewModel.informe=mViewModel.getInformeCompra(mViewModel.getIdInformeNuevo());
                    if( mViewModel.informe!=null) {
                        mViewModel.consecutivo = mViewModel.informe.getConsecutivo();
                        Constantes.DP_CONSECUTIVO = mViewModel.consecutivo;

                        ((ContinuarInformeActivity) getActivity()).actualizarCliente(mViewModel.informe);

                        //   ((ContinuarInformeActivity)getActivity()).actualizarProdSel(dViewModel.productoSel);
                    }
                    aceptar.setEnabled(true);
                textoint.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(300)});

            }


            if(preguntaAct.getId()==87)
                aceptar.setEnabled(true);
            if(preguntaAct.getId()==77&&mViewModel.informe.isSinproducto()) {
                aceptar.setEnabled(false);
                textoint.addTextChangedListener(new DetalleProductoPenFragment.BotonTextWatcher());
            }
            if(spclientes!=null){
                spclientes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        aceptar.setEnabled(true);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }

                });
            }
            if(pregunta!=null){
                pregunta.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        aceptar.setEnabled(true);
                    }
                });
            }
            if(respgen!=null){
                RadioGroup botones=(RadioGroup)respgen;
                botones.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        aceptar.setEnabled(true);
                    }
                });
            }

            aceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    aceptar.setEnabled(false);
                    long currentClickTime= SystemClock.elapsedRealtime();
                    Log.d(TAG,"di click :("+currentClickTime+"--"+lastClickTime);
                    // preventing double, using threshold of 1000 ms
                 //   if(preguntaAct.getSigId()==88&&currentClickTime - lastClickTime < 7000)
                //        return;
                    if (currentClickTime - lastClickTime < 5500){
                        return;
                    }

                    lastClickTime = currentClickTime;

                    if(preguntaAct.getNombreCampo().equals("clientesId")){
                        guardarCliente();
                    }

                    siguiente();

                }
            });
            if(Contrato.TablaInformeDet.causa_nocompra.equals(preguntaAct.getNombreCampo())) {
                Log.d(TAG, "llenando causas");

                llenarCausasNoCompraOtras();
                respgen = root.findViewById(1001);
                if (respgen != null) {
                    RadioGroup botones = (RadioGroup) respgen;
                    Log.d(TAG, "ENCONTRE RADIOBUTON");

                    botones.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int i) {

                            int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                            Log.d(TAG, "click en causas " + selectedRadioButtonId);
                            if (selectedRadioButtonId == 7) //seleccionó otras y pongo las demas opciones
                            {
                                aceptar.setEnabled(false);
                                aceptar.setVisibility(View.GONE);
                                radioGroupCausas = root.findViewById(R.id.fgsrgpopcionesnocompra);
                                radioGroupCausas.setVisibility(View.VISIBLE);

                                aceptarCausas = root.findViewById(R.id.fgsbtnaceptarcausas);
                                aceptarCausas.setVisibility(View.VISIBLE);
                                aceptarCausas.setEnabled(false);
                                linearLayoutCausas.setVisibility(View.VISIBLE);
                                aceptarCausas.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        aceptarCausas.setEnabled(false);
                                        long currentClickTime = SystemClock.elapsedRealtime();
                                        // preventing double, using threshold of 1000 ms
                                        if (currentClickTime - lastClickTime < 5500) {
                                            //  Log.d(TAG,"doble click :("+lastClickTime);
                                            return;
                                        }


                                        linearLayoutCausas.setVisibility(View.GONE);
                                        lastClickTime = currentClickTime;
                                        siguiente();
                                    }
                                });

                                radioGroupCausas.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                        aceptarCausas.setEnabled(true);
                                    }
                                });
                            } else {
                                aceptar.setEnabled(true);
                                aceptar.setVisibility(View.VISIBLE);
                                linearLayoutCausas.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }

                prodSel=dViewModel.productoSel;
            if(preguntaAct.isBotonMicro()) {

                micbtn=root.findViewById(R.id.btnmicsiglas);
           /*     sspeechRecognizer = grabarVoz();
                micbtn.setVisibility(View.VISIBLE);*/
            }
            if(preguntaAct.getId()==77){ //comentarios
                //cambio el boton a finalizar y muestro alerta
                aceptar.setText(getString(R.string.enviar));
                aceptar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.botonvalido));
            }
            Log.d(TAG,"tipo tienda -----------*"+Constantes.DP_TIPOTIENDA);
            tipoTienda=Constantes.DP_TIPOTIENDA;
            estatusPepsi=mViewModel.visita.getEstatusPepsi(); //para saber si puede comprar pepsi
            estatusPen=mViewModel.visita.getEstatusPen();
            estatusElec=mViewModel.visita.getEstatusElec();

        } catch (Exception e) {
            e.printStackTrace();
            compraslog.grabarError(TAG, "onCreateView", e.getMessage());
            Toast.makeText(getActivity(),"Hubo un error inesperado",Toast.LENGTH_LONG).show();

        }

        return root;
    }

    public void iniciarNumMuestra(){
        //es un nuevo informe o una nueva pregunta

        if (preguntaAct.getId() == 72)
            mViewModel.numMuestra = 1;

        if (preguntaAct.getId() == 73) {
            mViewModel.numMuestra = 2;
        }
        if (preguntaAct.getId() == 74) {
            mViewModel.numMuestra = 3;
        }

        Log.e(TAG, "--nuevo nummuestras:" + mViewModel.numMuestra);

    }
    public void reiniciarDatos(){
        if(preguntaAct.getId()==75)//ticket de compra
        {
            ((ContinuarInformeActivity)getActivity()).noSalir(true);
        }
        if(preguntaAct.getId()>=78&&preguntaAct.getId()!=89&&preguntaAct.getId() !=128) //ya tengo producto voy en siglas
        {

            mViewModel.informe=new InformeCompra();

            mViewModel.informe.setConsecutivo(ultimares.getConsecutivo());
            mViewModel.consecutivo=ultimares.getConsecutivo();
            Constantes.DP_CONSECUTIVO=mViewModel.consecutivo;
            ((ContinuarInformeActivity)getActivity()).actualizarCliente(mViewModel.informe);
            dViewModel.fromTemp(); //guardo datos del producto selec
            if(dViewModel.productoSel!=null)
                ((ContinuarInformeActivity)getActivity()).actualizarProdSel(dViewModel.productoSel);
        }
        if (preguntaAct.getId() >= 80&&preguntaAct.getId() !=89&&preguntaAct.getId()!=77&&preguntaAct.getId()!=115&&preguntaAct.getId() !=128) {//si compro prod
            InformeTemp resp=dViewModel.buscarxNombreCam("codigo",mViewModel.numMuestra);
            ((ContinuarInformeActivity)getActivity()).actualizarCodProd(resp.getValor());

        }
        if(dViewModel.productoSel!=null)
        {
            getTomadoDe();}
        if (preguntaAct.getId() >= 81&&preguntaAct.getId()!=89&&mViewModel.numMuestra>0&&preguntaAct.getId() !=128) { //si hay prod


            InformeTemp resp=dViewModel.buscarxNombreCam("origen",mViewModel.numMuestra);
            String valor="";
            int opcion=Integer.parseInt(resp.getValor());
            //busco en el cat
            for(CatalogoDetalle cat:tomadoDe){
                if(cat.getCad_idopcion()==opcion)
                    valor=cat.getCad_descripcionesp();
            }
            Constantes.VarDetalleProd.tomadode =valor;

            ((ContinuarInformeActivity)getActivity()).actualizarAtributo1();

        }


    }
    public void compraProd(View view,int nummuestra) {
        // Is the button now checked?
        boolean checked = ((Preguntasino) view).getRespuesta();
        //  Log.d(TAG,"CLICK EN RADIOBUTTON ID="+view.getId());
        if (checked)
            // Check which radio button was clicked
            switch(preguntaAct.getId()) {
                case 72: case 73: case 74:

                    // fue si mostrar lista de compra
                    verListaCompra(nummuestra);

                    break;

                default: break;
            }
        else
            avanzarPregunta(preguntaAct.getSigAlt());
    }
    public void siguiente(){
        boolean resp=false;
        aceptar.setEnabled(false);
        switch (preguntaAct.getNombreCampo()){

            case Contrato.TablaInformeDet.SIGLAS:

                resp=validarSiglas();
                break;

            case Contrato.TablaInformeDet.CADUCIDAD:

                int respf=validarCodigoProd();
                if(respf==1)
                    resp=false; //camino que tenia
                if(respf==2){
                    //nuevo mensaje de pregunta
                    guardarResp();
                    avanzarPregunta(preguntaAct.getSigAlt());
                    return;
                }
                if(respf==3) { //todo bien
                    guardarResp();
                    avanzarPregunta(preguntaAct.getSigId());
                    return;

                }
                break;
            case Contrato.TablaInformeDet.COSTO:
                String  valor = textoint.getText().toString();
                // float val=Float.parseFloat(valor);
                if(valor.equals("$0.00")){
                    Toast.makeText(getActivity(),"Costo inválido, verifique",Toast.LENGTH_LONG).show();


                }
                else {
                    //le quito la ,
                    valor=valor.replace(",","");
                    resp=true;}
                break;
            case  Contrato.TablaInformeDet.QR: //valido el qr
                String  valor2 = textoint.getText().toString();
                resp=mViewModel.validarQr(valor2, compraslog);
                if(resp){
                    Toast.makeText(getActivity(),"EL QR YA SE CAPTURO, VERIFIQUE",Toast.LENGTH_LONG).show();
                    resp=false;
                }
                else resp=true;
                break;
            default: resp=true; break;
        }

        if(resp)
        {

            if(preguntaAct.getId()==80 ) {
                String  valor = textoint.getText().toString();


                ((ContinuarInformeActivity)getActivity()).actualizarCodProd(valor);

            }else


            if(preguntaAct.getType().equals(CreadorFormulario.PREGUNTASINO)){
                //reviso la opcion seleccionada de compro prod para otros clientes
                if(!pregunta.getRespuesta()&&preguntaAct.getId()!=88&&preguntaAct.getId()!=116) //se selecciono no
                {
                    //voy al altsig
                    guardarResp();
                    //guarda informe

                    avanzarPregunta(preguntaAct.getSigAlt());
                    return;

                }
            }
            if(preguntaAct.getType().equals(CreadorFormulario.PREGUNTASINO)){
                //reviso la opcion seleccionada de compro prod para otros clientes
                if(preguntaAct.getId()==116) //validacion de fecha
                {
                    if(pregunta.getRespuesta()) {


                        avanzarPregunta(preguntaAct.getSigId());
                        return;
                    }else { //es no
                        //borro xq ya guardé
                        //talve no necesite borrar xq sobreescribo
                        avanzarPregunta(preguntaAct.getSigAlt()); //vuevo a pregunta fecha
                        return;
                    }

                }
            }
            if(preguntaAct.getSigId()==3000) //voy a lista de compra
            {
                guardarResp();
                compraProd(pregunta,preguntaAct.getId() - 71);
                //no funcionará para la 4a muestra
            }else
            if(preguntaAct.getSigId()==88) //comentarios termine inf compro para otros clientes
            {
                if(yaestoyProcesando)
                    return;
                yaestoyProcesando=true;
                loadingDialog = new LoadingDialog(getActivity());
                loadingDialog.startLoadingDialog();
                guardarResp();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try{

                    //actualiza informe con comentarios ticket
                    this.actualizarInforme();
                    //busco el informe

                    this.finalizar();
                    //limpiar tabla
                    limpiarTablTemp();
                    //reviso si hay más clientes, si no fin
                    buscarClientes();
                    if(clientesAsig!=null&&clientesAsig.size()>0) {
                        loadingDialog.dismisDialog();
                        yaestoyProcesando=false;
                        avanzarPregunta(preguntaAct.getSigId());
                    }
                    else{
                        //no hay mas clientes finalizo preinforme e informe

                        //la muestra la guarde en la 42

                        Log.d(TAG,"dice que no");
                        compraslog.info(TAG,"siguiente","no hay mas cliente finaliza visita "+mViewModel.visita.getId());

                        //es la 43 //finalizo preinforme
                        mViewModel.finalizarVisita(mViewModel.visita.getId());
                        mViewModel.eliminarTblTemp();
                        loadingDialog.dismisDialog();
                        Toast.makeText(getActivity(), getString(R.string.informe_finalizado),Toast.LENGTH_SHORT).show();
                        yaestoyProcesando=false;
                        salir();
                        //  aceptar.setEnabled(true);
                        return;

                    }
                }
                catch(Exception ex){
                    ex.printStackTrace();
                    ComprasLog flog=ComprasLog.getSingleton();
                    flog.grabarError(TAG+" ERROR AL GUARDAR EL INFORME "+ex.getMessage());
                    yaestoyProcesando=false;
                    loadingDialog.dismisDialog();
                    Toast.makeText(getActivity(), "ERROR AL GUARDAR EL INFORME", Toast.LENGTH_LONG).show();

                }

            }else
            if(preguntaAct.getSigId()==0)//terminé con preguntas de muestra
            {
                if(yaestoyProcesando)
                    return;
                yaestoyProcesando=true;
                aceptar.setEnabled(false);
                int sig=mViewModel.numMuestra+72;
                guardarResp();

                //quito la info de la barra gris
                ((ContinuarInformeActivity)getActivity()).reiniciarBarra();
                guardarMuestra(sig);

            }else
            if(preguntaAct.getId()==89)//no hubo producto, causano compra
            {
                if(yaestoyProcesando)
                    return;
                loadingDialog = new LoadingDialog(getActivity());
                loadingDialog.startLoadingDialog();
                guardarResp();
                //para otras si genero consecutivo
                RadioGroup rg = (RadioGroup) respgen;
                String valor=null;
                int selectedRadioButtonId = rg.getCheckedRadioButtonId();
                if (selectedRadioButtonId != -1) {

                    valor = selectedRadioButtonId + "";
                }

                    if(valor!=null)
                        if(valor.equals("7")) //es otras
                        {
                            //busco la causa
                            radioGroupCausas =root.findViewById(R.id.fgsrgpopcionesnocompra);

                            int checkedId=radioGroupCausas.getCheckedRadioButtonId();
                            //guardo causa no compra
                            mViewModel.guardarResp(mViewModel.getIdInformeNuevo(), dViewModel.getIddetalleNuevo(), "7", preguntaAct.getNombreCampo(), preguntaAct.getTabla(), mViewModel.consecutivo, true);
                            //guardo en otras
                            mViewModel.guardarResp(mViewModel.getIdInformeNuevo(), dViewModel.getIddetalleNuevo(), checkedId+"","causa_nocompraotras" , preguntaAct.getTabla(), mViewModel.consecutivo, false);

                            //generar consecutivo tienda
                            int consecutivo=mViewModel.getConsecutivo(Constantes.ni_plantasel,getActivity(), this);
                            Log.d(TAG,"*genere cons="+consecutivo);

                            mViewModel.informe.setConsecutivo(consecutivo);
                            Constantes.DP_CONSECUTIVO = consecutivo;
                            mViewModel.consecutivo=consecutivo;
                            mViewModel.guardarResp(0,0,Constantes.ni_plantasel+"","plantasId","I",mViewModel.consecutivo,false);
                            mViewModel.guardarResp(0,0,Constantes.ni_plantanombre+"","plantaNombre","I",mViewModel.consecutivo,false);
                            mViewModel.guardarResp(0,0,Constantes.ni_clientesel,"clienteNombre","I",mViewModel.consecutivo,false);
                            guardarMuestra(preguntaAct.getSigId());
                            loadingDialog.dismisDialog();
                            //  consecutivo.removeObservers(DetalleProductoFragment.this);


                        }else { //el consecutivo es 0
                            mViewModel.guardarResp(0,0,Constantes.ni_plantasel+"","plantasId","I",0,false);
                            mViewModel.guardarResp(0,0,Constantes.ni_plantanombre+"","plantaNombre","I",0,false);
                            mViewModel.guardarResp(0,0,Constantes.ni_clientesel,"clienteNombre","I",0,false);

                            guardarMuestra(preguntaAct.getSigId());
                            loadingDialog.dismisDialog();
                        }



                // avanzarPregunta(preguntaAct.getSigId());
            }else
            if(preguntaAct.getId()==78){ //son la siglas y ya seleccioné producto
                //creo informe e informe detalle con datos del producto
                guardarResp();
                guardarProductoTemp();
                avanzarPregunta(preguntaAct.getSigId());
            }else
            if(preguntaAct.getId()==88){ //hay ootro cliente
           //finalizo informe

             //   mViewModel.finalizarInforme();
                //la muestra la guarde en la 42
                if(!pregunta.getRespuesta()) //se selecciono no
                {
                    Log.d(TAG,"dice que no");
                    //es la 68 //finalizo preinforme
                    finalizarPreinforme();
                    return;
                }
                avanzarPregunta(1);
            }
            else{
                if(preguntaAct.getId()>70)
                    guardarResp();
                avanzarPregunta(preguntaAct.getSigId());
            }
        }
        aceptar.setEnabled(true);
        lastClickTime=0;
    }
    //validar siglas
    public boolean validarSiglas(){
        if(dViewModel.productoSel.clienteSel==6)
            if(!textoint.getText().toString().equals("")){
                String siglaslis=dViewModel.productoSel.siglas;
                if(dViewModel.productoSel.siglas!=null&&!siglaslis.toUpperCase().equals(textoint.getText().toString().toUpperCase())){
                    Toast.makeText(getActivity(), getString(R.string.error_siglas), Toast.LENGTH_LONG).show();
                    return false;
                }
            }

        return true;
    }

    public void avanzarPregunta(int sig){
        LiveData<Reactivo> nvoReac = dViewModel.buscarReactivo(sig);
        Log.d(TAG,"xxxxx "+sig);
        //busco el siguiente
        if(sig==1){
            //empiezo de 0
            Bundle bundle = new Bundle();
            bundle.putInt(ContinuarInformeActivity.INFORMESEL,mViewModel.visita.getId());
            Intent intento1=new Intent(getActivity(), ContinuarInformeActivity.class);
            intento1.putExtras(bundle);
            requireActivity().finish();
            startActivity(intento1);
            return;
        }
        nvoReac.observe(getViewLifecycleOwner(), new Observer<Reactivo>() {
            @Override
            public void onChanged(Reactivo reactivo) {

                if(reactivo!=null)
                {if(sig==1) //pregunta de cliente o confirmacion vuelvo al detalleproducto1
                {
                    Bundle args = new Bundle();
                    args.putInt(DetalleProductoFragment.ARG_PREGACT,reactivo.getId() );
                    args.putBoolean(DetalleProductoFragment.ARG_ESEDI,false);
                    DetalleProductoFragment nvofrag = new DetalleProductoFragment();
                    nvofrag.setArguments(args);
                    //   DetalleProductoFragment nvofrag = new DetalleProductoFragment(reactivo,false);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    // Definir una transacción
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    // Remplazar el contenido principal por el fragmento
                    fragmentTransaction.replace(R.id.continf_fragment, nvofrag);
                    //     fragmentTransaction.addToBackStack(null);
                    // Cambiar
                    fragmentTransaction.commit();
                }else {
                    Bundle args = new Bundle();
                    args.putInt(ARG_PREGACTE,reactivo.getId() );
                    args.putBoolean(ARG_ESEDIE,false);
                    DetalleProductoElecFragment nvofrag = new DetalleProductoElecFragment();
                    nvofrag.setArguments(args);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    // Definir una transacción
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    // Remplazar el contenido principal por el fragmento
                    fragmentTransaction.replace(R.id.continf_fragment, nvofrag);
                    // fragmentTransaction.addToBackStack(null);
                    // Cambiar
                    fragmentTransaction.commit();
                }}
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"vars"+requestCode +"--"+ nombre_foto);
        if ((requestCode == REQUEST_CODE_TAKE_PHOTO) && resultCode == RESULT_OK) {
            try {
                if (archivofoto != null && archivofoto.exists()) {

                    textoint.setText(nombre_foto);

                    if (ComprasUtils.getAvailableMemory(getActivity()).lowMemory) {
                        Toast.makeText(getActivity(), "No hay memoria suficiente para esta accion", Toast.LENGTH_SHORT).show();
                        compraslog.grabarError(TAG, "onActivityResult", "No hay memoria suficiente para esta accion");
                        aceptar.setEnabled(false);
                        return;
                    } else {
                         ComprasUtils cu = new ComprasUtils();
                        cu.comprimirImagen(archivofoto.getAbsolutePath());
                        Bitmap bitmap1 = ComprasUtils.decodeSampledBitmapFromResource(archivofoto.getAbsolutePath(), 100, 100);
                        fotomos.setImageBitmap(bitmap1);
                        fotomos.setVisibility(View.VISIBLE);
                        btnrotar.setVisibility(View.VISIBLE);
                        btnrotar.setFocusableInTouchMode(true);
                        btnrotar.requestFocus();
                        nombre_foto = null;
                        archivofoto = null;
                        if (nopermiso != null) {
                            nopermiso.setChecked(false);
                        }
                    }

                } else {
                    Toast.makeText(getActivity(), "No hay memoria suficiente para esta accion", Toast.LENGTH_SHORT).show();
                    compraslog.grabarError(TAG,"onActivityResult", "No hay memoria suficiente para esta accion");
                    aceptar.setEnabled(false);


                }
            }catch (Exception ex){
                compraslog.grabarError(TAG,"onActivityResult", "No hay memoria suficiente para esta accion");
                aceptar.setEnabled(false);
                Toast.makeText(getActivity(), "Hubo un error al guardar la foto, intente de nuevo", Toast.LENGTH_LONG).show();

            }


        } else if(requestCode== BackActivity.REQUEST_CODE)
        {
            //capturé muestra
            if(resultCode==NUEVO_RESULT_OK) {
                if(Constantes.productoSel!=null)
                {
                    dViewModel.productoSel = Constantes.productoSel;
                    //guardo el total de la lista
                    //generar consecutivo tienda
                    Log.i(TAG, ">>>> "+  dViewModel.productoSel.clienteNombre);
                    if(mViewModel.consecutivo==0) {
                        int consecutivo = mViewModel.getConsecutivo(dViewModel.productoSel.plantaSel, getActivity(), this);
                        //  Log.d(TAG, "*genere cons=" + consecutivo);
                        Log.i(TAG, "genere cons=" + consecutivo);

                        mViewModel.consecutivo = consecutivo;
                        Constantes.DP_CONSECUTIVO = consecutivo;

                    }
                    mViewModel.informe.setConsecutivo(mViewModel.consecutivo);
                    Log.i(TAG, "tengo el tipo muestra " + dViewModel.productoSel);
                    ((ContinuarInformeActivity)getActivity()).actualizarCliente(mViewModel.informe);
                    //actualizo barra
                    ((ContinuarInformeActivity) getActivity()).actualizarProdSel(dViewModel.productoSel);
                    mViewModel.guardarResp(0, 0, Constantes.NM_TOTALISTA + "", "totalLista", "", mViewModel.consecutivo, false);
                    avanzarPregunta(78);

                }else
                    Log.e(TAG,"Algo salió muy mal al elegir el producto");


            }

        }  else if(requestCode == REQUEST_CODEQR) {
            IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);
            if(result.getContents() == null) {
                    Toast.makeText(getActivity(), "Scan cancelled", Toast.LENGTH_LONG).show();
            }
            else
            {   /* Update the textview with the scanned URL result */
                textoint.setText(result.getContents());

            }

        }
            else {
            super.onActivityResult(requestCode, resultCode, data);
            Toast.makeText(getActivity(), "hubo un error", Toast.LENGTH_LONG).show();

            }

    }
    public boolean buscarMuestraCodigoElec(Date caducidadnva){
        //busco en el mismo informe
        return dViewModel.buscarMuestraCodigo(Constantes.INDICEACTUAL,dViewModel.productoSel.plantaSel,dViewModel.productoSel,"",caducidadnva,getViewLifecycleOwner(),dViewModel.productoSel.codigosperm);

    }

    // devuelve 1 cuando muestro solo un toat y sigo en la misma pantalla
    //devuelve 2 cuando falla los no permi o los repetidos
    public int validarCodigoProd(){

        Date fechacad;
        try {
            sdfcodigo.setLenient(false);
            fechacad = sdfcodigo.parse(textoint.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), getString(R.string.error_fecha_formato), Toast.LENGTH_LONG).show();
            return 1;
        }
        //valido fecha cad
        if(!validarFechaCad(fechacad)){  //compraro con   rangos
            return 1;}
        if(dViewModel.productoSel.clienteSel==5||dViewModel.productoSel.clienteSel==6) {
            ValidadorDatos valdat = new ValidadorDatos();
            String codigonoper = dViewModel.productoSel.codigosnop;
            if (!fechacad.equals("") && codigonoper.length() > 1) {
                if (!valdat.validarCodigoPepRango(textoint.getText().toString(), codigonoper)) {

                    Toast.makeText(getActivity(), getString(R.string.error_codigo_per), Toast.LENGTH_LONG).show();

                    return 1;
                }
            }

            if (!valdat.validarCodigonoPermPen(textoint.getText().toString(), codigonoper))
                return 2;

            if (dViewModel.productoSel.tipoMuestra != 3) //solo si no es bu
            {
                if (this.buscarMuestraCodigoElec(fechacad))
                    return 2;
            }else {
                //nueva validacion 21-10-25 cuando sea sustitucion puede comprar el mismo codigo para el mismo analisis en la misma tienda
                boolean res = dViewModel.buscarMuestraCodigoMismoInforme(Constantes.INDICEACTUAL, dViewModel.productoSel, fechacad, mViewModel.getIdInformeNuevo());

                if (res) {
                    Toast.makeText(getActivity(), getString(R.string.error_codigo_per), Toast.LENGTH_LONG).show();

                    return 1;
                }
            }
        }

        return 3; //todo bien
    }


}
