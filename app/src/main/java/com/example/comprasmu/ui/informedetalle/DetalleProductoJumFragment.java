package com.example.comprasmu.ui.informedetalle;

import static android.app.Activity.RESULT_OK;

import static com.example.comprasmu.ui.listacompras.TabsFragment.ARG_CLIENTESEL;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.SubirInformeTask;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.Contrato;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeTemp;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ProductoExhibido;
import com.example.comprasmu.data.modelos.Reactivo;
import com.example.comprasmu.data.remote.InformeEnvio;
import com.example.comprasmu.services.SubirFotoService;
import com.example.comprasmu.ui.BackActivity;
import com.example.comprasmu.ui.RevisarFotoActivity;
import com.example.comprasmu.ui.informe.NuevoinformeViewModel;
import com.example.comprasmu.ui.listacompras.SelClienteFragment;
import com.example.comprasmu.ui.listadetalle.ListaCompraFragment;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.utils.CampoForm;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;
import com.example.comprasmu.utils.Preguntasino;
import com.example.comprasmu.utils.RPResultListener;
import com.example.comprasmu.utils.RuntimePermissionUtil;
import com.example.comprasmu.utils.ui.LoadingDialog;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DetalleProductoJumFragment extends DetalleProductoPenFragment{
    protected static final String TAG="DETALLEPRODUCTOJUMFRAG";
    public final static String ARG_PREGACTJ="comprasmu.ni_pregactj";
    public final static String ARG_ESEDIJ="comprasmu.ni_esedij";
    public DetalleProductoJumFragment() {

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



            sv = root.findViewById(R.id.content_generic);
            aceptar = root.findViewById(R.id.btngaceptar);
            validar=root.findViewById(R.id.btngvalidar);
            int num_pregact=0;
            if (getArguments() != null) {
                num_pregact = getArguments().getInt(ARG_PREGACTJ);
                this.isEdicion = getArguments().getBoolean(ARG_ESEDIJ);
            }

            try {

                //busco preguntaAct
                preguntaAct= dViewModel.buscarReactivoSimpl(num_pregact);
                //   mViewModel.cargarCatsContinuar();
                //si es la misma
                //reviso si es edicion o es nueva
            if(preguntaAct.getTabla().equals("I"))
                    mViewModel.numMuestra=0;
            if(this.preguntaAct!=null)
                    ultimares=dViewModel.buscarxNombreCam(this.preguntaAct.getNombreCampo(),mViewModel.numMuestra);
            Log.d(TAG,"------"+Constantes.NM_TOTALISTA+"---"+mViewModel.consecutivo);
            dViewModel.reactivoAct=preguntaAct.getId();
            if(ultimares!=null) {    //es edicion
                isEdicion = true;
                    //    if(preguntaAct.getId()==52||preguntaAct.getId()==53||preguntaAct.getId()==54){
                    //     {
                    //reviso si ya tengo muestra
                Log.d(TAG,"mmmmmmmmmmm"+preguntaAct.getId());

            }
            else
                    //if(this.preguntaAct.getId()==2||this.preguntaAct.getId()==3||this.preguntaAct.getId()==5)
                isEdicion=false;

            if(isEdicion) {
                aceptar.setEnabled(true);
                mViewModel.consecutivo=ultimares.getConsecutivo();
                Constantes.DP_CONSECUTIVO=mViewModel.consecutivo;
                //  ya lo busco en la actividad
               //busco el cliente
                InformeTemp inf= dViewModel.buscarxNombreCam("clientesId");
                if(inf!=null) {
                    int clienteSel = Integer.parseInt(inf.getValor());
                    mViewModel.clienteSel=clienteSel;

                }
                inf= dViewModel.buscarxNombreCam("clienteNombre");
                if(inf!=null) {
                    Constantes.ni_clientesel=inf.getValor();

                }
                inf= dViewModel.buscarxNombreCam("plantasId");
                if(inf!=null) {
                    Constantes.ni_plantasel=Integer.parseInt(inf.getValor());

                }
                inf= dViewModel.buscarxNombreCam("plantaNombre");
                if(inf!=null) {
                    Constantes.ni_plantanombre=inf.getValor();

                }
                reiniciarDatos();
                    //busco el total de prods en la lista
                if(Constantes.NM_TOTALISTA==0) {
                    InformeTemp resp = dViewModel.buscarxNombreCam("totalLista");
                    String valor = "";
                    if (resp != null) {
                        Constantes.NM_TOTALISTA = Integer.parseInt(resp.getValor());

                    }
                }
                if(preguntaAct.getId()==69||preguntaAct.getId()==70){
                        //no puedo modificar  avanzo a la siguiente

                    preguntaAct=dViewModel.buscarReactivoxId(preguntaAct.getSigId());
                    InformeTemp inft=dViewModel.buscarxNombreCam("informeid");
                    Log.d(TAG,"inf"+inft.getValor());
                    if(inft!=null) {
                        mViewModel.setIdInformeNuevo(Integer.parseInt(inft.getValor()));
                        mViewModel.consecutivo = inft.getConsecutivo();
                        Constantes.DP_CONSECUTIVO=mViewModel.consecutivo;
                    }
                    inft=dViewModel.buscarxNombreCam(Contrato.TablaInformeDet.causa_nocompra);

                    if(inft!=null) {
                        mViewModel.informe.setSinproducto(true);
                        mViewModel.informe.setCausa_nocompra(inft.getValor());
                    }
                    ultimares=null;
                    isEdicion=false;
                    aceptar.setEnabled(false);

                }
            }
            else {
                aceptar.setEnabled(false);
            }
            iniciarNumMuestra();
                //}
                // if(preguntaAct.getId()==2||preguntaAct.getId() == 3||preguntaAct.getId() == 4) //estot en siglas y es una nueva muestra
                //     mViewModel.numMuestra=mViewModel.numMuestra+1;
                //reviso que no haya muesmtras guardadas de ese informe :O como se que es uno nuevo
                //  Log.e(TAG,"-------------nummuestras:"+mViewModel.numMuestra);
                // }
                //para saber si el detalle ya existe y el informe
           /* if( mViewModel.getIdInformeNuevo()==0)
                InformeTemp inf= dViewModel.buscarxNombreCam("clienteNombre");
            mViewModel.getIdInformeNuevo(), dViewModel.getIddetalleNuevo()*/
            crearFormulario();

            if(preguntaAct.getType().equals(CreadorFormulario.SELECTCAT)||preguntaAct.getType().equals(CreadorFormulario.SELECTDES)||preguntaAct.getType().equals(CreadorFormulario.PSELECT)) {
                spclientes = root.findViewById(1001); //es el unico id que se genera en crear formlario
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

                            preguntarBorrarFoto(view,textoint,fotomos,btnrotar,null);
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
            }else if(textoint!=null&&preguntaAct.getId()!=57){ //los comentarios no son obligatorios
                    textoint.addTextChangedListener(new BotonTextWatcher());

                }
            if(preguntaAct.getId()==57){ //los comentarios no son obligatorios
                    //  textoint.addTextChangedListener(new MayusTextWatcher());
                    textoint.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
                    aceptar.setEnabled(true);
            }
            if(preguntaAct.getId()==57&&mViewModel.informe.isSinproducto()) {
                    aceptar.setEnabled(false);
                    textoint.addTextChangedListener(new BotonTextWatcher());
            }
            if(preguntaAct.getId()==58){ //para las siglas agrego el boton de validar
                validar.setVisibility(View.VISIBLE);
                aceptar.setVisibility(View.GONE);
                validar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        validar.setEnabled(false);
                        buscarPlanta();

                    }
                });

            }
            if(preguntaAct.getId()==70)
                    aceptar.setEnabled(true);
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
                        long currentClickTime=SystemClock.elapsedRealtime();
                        // preventing double, using threshold of 1000 ms
                        if (currentClickTime - lastClickTime < 5500){
                            return;
                        }

                        lastClickTime = currentClickTime;
                        Log.d(TAG,"di click :("+lastClickTime);
                        if(preguntaAct.getNombreCampo().equals("clientesId")){
                            guardarCliente();
                        }else

                            siguiente();

                    }
            });
            prodSel=dViewModel.productoSel;
            if(preguntaAct.isBotonMicro()) {

                    micbtn=root.findViewById(R.id.btnmicsiglas);
           /*     sspeechRecognizer = grabarVoz();
                micbtn.setVisibility(View.VISIBLE);*/
                }
            if(preguntaAct.getId()==57){ //comentarios
                    //cambio el boton a finalizar y muestro alerta
                    aceptar.setText(getString(R.string.enviar));
                    aceptar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.botonvalido));
            }
            tipoTienda=Constantes.DP_TIPOTIENDA;
            estatusPepsi=mViewModel.visita.getEstatusPepsi(); //para saber si puede comprar pepsi
            estatusPen=mViewModel.visita.getEstatusPen();
            estatusElec=mViewModel.visita.getEstatusElec();
            //todo
            //estatusJumex=mViewModel.visita.getEstatusJumex();
            Log.d(TAG,"tipo tienda -----------*"+estatusPepsi);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return root;
        }



    public void iniciarNumMuestra(){
        //es un nuevo informe o una nueva pregunta
        //if(mViewModel.numMuestra==0) {
        // &&preguntaAct.getId()!=5) {
        if (preguntaAct.getId() == 52)
            mViewModel.numMuestra = 1;

        if (preguntaAct.getId() == 53) {
            mViewModel.numMuestra = 2;
        }

        Log.e(TAG, "--nuevo nummuestras:" + mViewModel.numMuestra);

    }


    public void compraProd(View view,int nummuestra) {
        // Is the button now checked?
        boolean checked = ((Preguntasino) view).getRespuesta();
        //  Log.d(TAG,"CLICK EN RADIOBUTTON ID="+view.getId());
        if (checked)
            // Check which radio button was clicked
            switch(preguntaAct.getId()) {
                case 52: case 53:

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

      /*  if (textoint != null) {
            String valor = textoint.getText().toString();
            if(valor.length()<=0){
                return;
            }

        }*/

        switch (preguntaAct.getNombreCampo()){

            case Contrato.TablaInformeDet.COSTO:
                String  valor = textoint.getText().toString();
                // float val=Float.parseFloat(valor);
                if(valor.equals("$0.00")){
                    Toast.makeText(getActivity(),"Costo inválido, verifique",Toast.LENGTH_LONG).show();


                }
                else resp=true;
                break;
               /* case Contrato.TablaInformeDet.CODIGO:
                   resp=validarCodigoprod();
                    break;*/
            case Contrato.TablaInformeDet.CADUCIDAD:
                resp=validarFecha();

                break;
            case "clientesId":

                break;
            default: resp=true; break;
        }

        if(resp)
        {

            if(preguntaAct.getId()==60 ) {
                String  valor = textoint.getText().toString();

                //guardo el atributo para mostrarlo despues
                ((ContinuarInformeActivity)getActivity()).actualizarCodProd(valor);

            }else


            if(preguntaAct.getType().equals(CreadorFormulario.PREGUNTASINO)){
                //reviso la opcion seleccionada de compro prod para otros clientes
                if(!pregunta.getRespuesta()&&preguntaAct.getId()!=68) //se selecciono no
                {
                    //voy al altsig
                    guardarResp();
                    //guarda informe

                    avanzarPregunta(preguntaAct.getSigAlt());
                    return;

                }
            }

            if(preguntaAct.getSigId()==20000) //voy a lista de compra
            {
                guardarResp();
                compraProd(pregunta,preguntaAct.getId() - 51);
                //no funcionará para la 4a muestra
            }else
            if(preguntaAct.getSigId()==68) // comentarios termine inf
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

                //  guardarMuestra();
                try {
                    //guarda informe
                    this.actualizarInforme();
                    this.finalizar();
                    //limpiar tabla
                    limpiarTablTemp();
                    //reviso si hay más clientes, si no fin
                    buscarClientes();
                    compraslog.grabarError(TAG+" finalizando buscando clientes ");
                    if (clientesAsig != null && clientesAsig.size() > 0) {
                        loadingDialog.dismisDialog();
                        yaestoyProcesando=false;
                        avanzarPregunta(preguntaAct.getSigId());

                    } else {
                        //no hay mas clientes finalizo preinforme e informe
                        //    mViewModel.finalizarInforme();
                        //la muestra la guarde en la 42

                        Log.d(TAG, "no hay mas clientes");
                        //es la 43 //finalizo preinforme
                        mViewModel.finalizarVisita(mViewModel.visita.getId());
                        //   mViewModel.eliminarTblTemp();
                        loadingDialog.dismisDialog();
                        Toast.makeText(getActivity(), getString(R.string.informe_finalizado), Toast.LENGTH_SHORT).show();
                        yaestoyProcesando=false;
                        salir();
                        //  aceptar.setEnabled(true);
                        return;

                    }
                }
                catch(Exception ex){
                    ex.printStackTrace();
                    loadingDialog.dismisDialog();
                    yaestoyProcesando=false;
                    compraslog.grabarError(TAG+" HUBO UN ERROR AL FINALIZAR EL INFORME "+ex.getMessage());
                    Toast.makeText(getActivity(), "HUBO UN ERROR AL FINALIZAR EL INFORME", Toast.LENGTH_LONG).show();


                }

            }else
            if(preguntaAct.getSigId()==0)//terminé con preguntas de muestra
            {
                if(yaestoyProcesando)
                    return;
                yaestoyProcesando=true;
                aceptar.setEnabled(false);
                int sig=mViewModel.numMuestra+52;
                if(mViewModel.numMuestra==2)
                    sig=67;
                int nummuestra=mViewModel.numMuestra;
                guardarResp();

                //quito la info de la barra gris
                ((ContinuarInformeActivity)getActivity()).reiniciarBarra();

                guardarMuestra(sig);



            }else
            if(preguntaAct.getId()==69)//no hubo producto, causano compra
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
                  /*  RadioButton selectedRadioButton = root.findViewById(selectedRadioButtonId);
                    valor = selectedRadioButton.getText().toString();*/
                    //  valor=valor.toUpperCase();
                    valor = selectedRadioButtonId + "";
                }


                //busco planta
                plantaSel=mViewModel.informe.getPlantasId();
                NOMBREPLANTASEL=mViewModel.informe.getPlantaNombre();
                if(valor!=null)
                    if(valor.equals("7")) //es otras
                    {
                        //generar consecutivo tienda
                        int consecutivo=mViewModel.getConsecutivo(plantaSel,getActivity(), this);
                        Log.d(TAG,"*genere cons="+consecutivo);
                        mViewModel.informe.setConsecutivo(consecutivo);
                        mViewModel.consecutivo=consecutivo;
                        Constantes.DP_CONSECUTIVO = consecutivo;
                        mViewModel.guardarResp(0,0,plantaSel+"","plantasId","I",mViewModel.consecutivo,false);
                        mViewModel.guardarResp(0,0,NOMBREPLANTASEL+"","plantaNombre","I",mViewModel.consecutivo,false);
                        mViewModel.guardarResp(0,0,mViewModel.informe.getClienteNombre(),"clienteNombre","I",mViewModel.consecutivo,false);
                        guardarMuestra(preguntaAct.getSigId());
                        loadingDialog.dismisDialog();
                        //  consecutivo.removeObservers(DetalleProductoFragment.this);

                    }else {
                        mViewModel.guardarResp(0,0,plantaSel+"","plantasId","I",0,false);
                        mViewModel.guardarResp(0,0,NOMBREPLANTASEL+"","plantaNombre","I",0,false);
                        mViewModel.guardarResp(0,0,mViewModel.informe.getClienteNombre(),"clienteNombre","I",0,false);
                        guardarMuestra(preguntaAct.getSigId());
                        loadingDialog.dismisDialog();
                    }



                // avanzarPregunta(preguntaAct.getSigId());

            }else
            if(preguntaAct.getId()==58){ // ya seleccioné producto
                //creo informe e informe detalle con datos del producto
                guardarResp();
                guardarProductoTemp();
                avanzarPregunta(preguntaAct.getSigId());
            }else
            if(preguntaAct.getId()==68){ //hay ootro cliente

                if(!pregunta.getRespuesta()) //se selecciono no
                {
                    Log.d(TAG,"dice que no");
                    //es la 68 //finalizo preinforme
                    finalizarPreinforme();
                    return;
                }

                //avanzarPregunta(1);
                mViewModel.eliminarTblTemp();
                //me voy a la lista de informes
                //   getActivity().finish();
                Bundle bundle = new Bundle();
                bundle.putInt(ContinuarInformeActivity.INFORMESEL,mViewModel.visita.getId());

                //NavHostFragment.findNavController(this).navigate(R.id.action_visitatonuevo,bundle);
                Intent intento1=new Intent(getActivity(), ContinuarInformeActivity.class);
                intento1.putExtras(bundle);
                requireActivity().finish();
                startActivity(intento1);
            }
            else{
                if(preguntaAct.getId()>50)
                    guardarResp();
                avanzarPregunta(preguntaAct.getSigId());
            }
        }
        aceptar.setEnabled(true);
        lastClickTime=0;
    }

    String sigmuestra="segundaMuestra";
    public void guardarMuestra(int sigui){
        try {

            if(mViewModel.numMuestra ==2)
                sigui=55; //ticket de compra
            final int sig=sigui;
            //Creo el informe en nuevo informe y lo busco aqui
            //necestio saber si ya habia guardado informe
            //veo si ya existe el informe o hay que crearlo
            Log.d(TAG, "primero guardando informe"+mViewModel.numMuestra+"--"+mViewModel.getIdInformeNuevo());
            //   exit(0);



            if (mViewModel.numMuestra == 1 || mViewModel.getIdInformeNuevo() <= 0) {

                //busco el consecutivo
                MutableLiveData<Integer> idInformeNuevo = guardarInforme();
                Log.d(TAG, "guardando informe"+mViewModel.numMuestra+"--"+mViewModel.getIdInformeNuevo());
                //
                idInformeNuevo.observe(getViewLifecycleOwner(), new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer idnvo) {
                        Log.d(TAG, "se creo el informe" + idnvo);
                        mViewModel.informe.setId(idnvo);
                        mViewModel.setIdInformeNuevo(idnvo);
                        if(!mViewModel.informe.isSinproducto()) {
                            //si tengo detalle
                            // Log.d(TAG,"guardando  muestras "+mViewModel.numMuestra);

                            //    List<Integer> muestras= dViewModel.muestrasTotales();
                            //  for(int x:muestras) {
                            int nuevoid = dViewModel.insertarMuestra(mViewModel.getIdInformeNuevo(), mViewModel.numMuestra);
                            //guardo la muestra
                            if (nuevoid > 0) {
                                dViewModel.setIddetalleNuevo(nuevoid);
                                //si ya se guardó lo agrego en la lista de compra
                                ListaDetalleViewModel lcviewModel = new ViewModelProvider(DetalleProductoJumFragment.this).get(ListaDetalleViewModel.class);
                                Log.d(TAG,"voy a descontar"+dViewModel.icdNuevo.getCaducidad());
                                int res=lcviewModel.comprarMuestraPen(dViewModel.icdNuevo.getComprasId(), dViewModel.icdNuevo.getComprasDetId(), sdfcodigo.format(dViewModel.icdNuevo.getCaducidad()), dViewModel.icdNuevo.getTipoMuestra(),dViewModel.icdNuevo,dViewModel.productoSel.plantaSel,Constantes.INDICEACTUAL);
                                //limpiar tabla temp
                                //   limpiarTablTempMenCli();
                                mViewModel.eliminarMuestra(mViewModel.numMuestra);

                                dViewModel.setIddetalleNuevo(0);
                                dViewModel.icdNuevo=null;

                                mViewModel.guardarResp(mViewModel.getIdInformeNuevo(), dViewModel.getIddetalleNuevo(), "true", sigmuestra, "I", mViewModel.consecutivo, true);

                            }
                            yaestoyProcesando=false;
                            avanzarPregunta(sig);

                            //  }
                        }else {
                            dViewModel.setIddetalleNuevo(0);
                            dViewModel.icdNuevo = null;
                            //guardo el numinforme para cuando se creen los coment
                            mViewModel.guardarResp( mViewModel.getIdInformeNuevo() ,0, mViewModel.getIdInformeNuevo()+"" ,"informeid","I",mViewModel.consecutivo,false);
                            yaestoyProcesando=false;
                            avanzarPregunta(sig);
                        }
                        mViewModel.numMuestra++;
                        // idInformeNuevo.removeObservers(DetalleProductoPenFragment.this);

                    }

                });

            }else
            if(!mViewModel.informe.isSinproducto()) {
                //si tengo detalle
                Log.d(TAG,"guardando  muestras");

                //    List<Integer> muestras= dViewModel.muestrasTotales();
                //  for(int x:muestras) {
                int nuevoid = dViewModel.insertarMuestra(mViewModel.getIdInformeNuevo(), mViewModel.numMuestra);
                //guardo la muestra
                if (nuevoid > 0) {
                    dViewModel.setIddetalleNuevo(nuevoid);
                    //si ya se guardó lo agrego en la lista de compra
                    ListaDetalleViewModel lcviewModel = new ViewModelProvider(this).get(ListaDetalleViewModel.class);
                    Log.d(TAG,"qqqqqqqqqqqqqqq"+dViewModel.icdNuevo.getCaducidad());
                    int res=lcviewModel.comprarMuestraPen(dViewModel.icdNuevo.getComprasId(), dViewModel.icdNuevo.getComprasDetId(), sdfcodigo.format(dViewModel.icdNuevo.getCaducidad()), dViewModel.icdNuevo.getTipoMuestra(),dViewModel.icdNuevo,dViewModel.productoSel.plantaSel,Constantes.INDICEACTUAL);
                    //limpiar tabla temp
                    //   limpiarTablTempMenCli();
                    mViewModel.eliminarMuestra(mViewModel.numMuestra);
                    dViewModel.setIddetalleNuevo(0);
                    dViewModel.icdNuevo=null;
                    yaestoyProcesando=false;
                    mViewModel.guardarResp(mViewModel.getIdInformeNuevo(), dViewModel.getIddetalleNuevo(), "true", sigmuestra, "I", mViewModel.consecutivo, true);
                    avanzarPregunta(sig);

                }
                mViewModel.numMuestra++;
                //  }
            }else {
                dViewModel.setIddetalleNuevo(0);
                dViewModel.icdNuevo = null;
                //guardo el numinforme para cuando se creen los coment
                mViewModel.guardarResp( mViewModel.getIdInformeNuevo() ,0, mViewModel.getIdInformeNuevo()+"" ,"informeid","I",mViewModel.consecutivo,false);
                yaestoyProcesando=false;
                avanzarPregunta(sig);
                mViewModel.numMuestra++;
            }

        }catch (Exception ex){
            ex.printStackTrace();
            Log.e(TAG,ex.getMessage());
            Toast.makeText(getActivity(), "No se pudo guardar la muestra", Toast.LENGTH_LONG).show();

        }

    }

    public boolean validarFecha(){

        //solo valido formato de fecha
        ValidadorDatos valdat=new ValidadorDatos();
        try {
            sdfcodigo.setLenient(false);
            fechacad=sdfcodigo.parse(textoint.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), getString( R.string.error_fecha_formato), Toast.LENGTH_LONG).show();


            return false;
        }


        return true;


    }



    public void verListaCompra(int nummuestra){
        /* b2undle.putString("plantaNombre", listaSeleccionable.get(i).getNombre());*/
        /*   NavHostFragment.findNavController(this).navigate(R.id.action_selclientetolistacompras,bundle);
         */
        //NavHostFragment.findNavController(this).navigate(R.id.action_lista compra);
        String opcion = "";
        Intent intento1 = new Intent(getActivity(), BackActivity.class);


            //ya tengo la planta
            InformeTemp inf= dViewModel.buscarxNombreCam("plantasId");
            if(inf!=null) {
                plantaSel=Integer.parseInt(inf.getValor());

            }
            inf= dViewModel.buscarxNombreCam("plantaNombre");
            InformeTemp inf2= dViewModel.buscarxNombreCam("clienteNombre");
            if(inf2!=null) {
                NOMBREPLANTASEL=inf.getValor();

            }

            opcion = BackActivity.OP_LISTACOMPRA;


        //ya existe el informe
        intento1.putExtra(DetalleProductoPenFragment.ARG_NUEVOINFORME, mViewModel.getIdInformeNuevo());
        intento1.putExtra(BackActivity.ARG_FRAGMENT,opcion);
        intento1.putExtra("ciudadSel", mViewModel.visita.getCiudadId());
        intento1.putExtra("ciudadNombre", mViewModel.visita.getCiudad());
        intento1.putExtra(ListaCompraFragment.ARG_PLANTASEL, plantaSel);
        intento1.putExtra(ListaCompraFragment.ARG_NOMBREPLANTASEL, NOMBREPLANTASEL);
        intento1.putExtra(ListaCompraFragment.ARG_MUESTRA, "true");

        // intento1.putExtra(ListaCompraFragment.ARG_MUESTRA,"true");
        // spclientes = root.findViewById(1001);
        Log.d(TAG, " antes de ir a listacom planta" + plantaSel + "--" + NOMBREPLANTASEL);
        intento1.putExtra(ARG_CLIENTESEL, mViewModel.clienteSel);
        //intento1.putExtra(ARG_CLIENTENOMBRE,);
        intento1.putExtra(DetalleProductoPenFragment.NUMMUESTRA, nummuestra);

        startActivityForResult(intento1, BackActivity.REQUEST_CODE);

        //  startActivity(intento1);

    }

    protected void enviaraSiglas(){
        // totalLista=data.getIntExtra(ARG_TOTALLISTA, 0);
        //muestro el de 3a muestra
        // if(Constantes.NM_TOTALISTA>=16)
        //  tercera.setVisible(View.VISIBLE);
        if(Constantes.productoSel!=null)
        {
            dViewModel.productoSel = Constantes.productoSel;
            //guardo el total de la lista
            //generar consecutivo tienda
            Log.d(TAG, ">>>> "+  dViewModel.productoSel.clienteNombre);
            if(mViewModel.consecutivo==0) {
                int consecutivo = mViewModel.getConsecutivo(dViewModel.productoSel.plantaSel, getActivity(), this);
                //  Log.d(TAG, "*genere cons=" + consecutivo);
                Log.d(TAG, "genere cons=" + consecutivo);

                mViewModel.informe.setConsecutivo(consecutivo);

                mViewModel.consecutivo = consecutivo;
                Constantes.DP_CONSECUTIVO = consecutivo;
            }
            mViewModel.informe.setConsecutivo(mViewModel.consecutivo);

                Log.d(TAG, "tengo el tipo muestra " + dViewModel.productoSel);
                ((ContinuarInformeActivity)getActivity()).actualizarCliente(mViewModel.informe);

               //actualizo barra
            ((ContinuarInformeActivity) getActivity()).actualizarProdSel(dViewModel.productoSel);

            mViewModel.guardarResp(0, 0, Constantes.NM_TOTALISTA + "", "totalLista", "", mViewModel.consecutivo, false);

            avanzarPregunta(58);


        }else
            Log.e(TAG,"Algo salió muy mal al elegir el producto");

        //lo busco y cargo
    }
    public void avanzarPregunta(int sig){
        LiveData<Reactivo> nvoReac = dViewModel.buscarReactivo(sig);

        //busco el siguiente
        nvoReac.observe(getViewLifecycleOwner(), new Observer<Reactivo>() {
            @Override
            public void onChanged(Reactivo reactivo) {
                if(sig==1) //pregunta de cliente o confirmacion vuelvo al detalleproducto1
                {
                    Bundle args = new Bundle();
                    args.putInt(DetalleProductoFragment.ARG_PREGACT,reactivo.getId() );
                    args.putBoolean(DetalleProductoFragment.ARG_ESEDI,false);
                    DetalleProductoFragment nvofrag = new DetalleProductoFragment();
                    nvofrag.setArguments(args);
                    //DetalleProductoFragment nvofrag = new DetalleProductoFragment(reactivo,false);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    // Definir una transacción
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    // Remplazar el contenido principal por el fragmento
                    fragmentTransaction.replace(R.id.continf_fragment, nvofrag);
                    //   fragmentTransaction.addToBackStack(null);
                    // Cambiar
                    fragmentTransaction.commit();
                }else {
                    Bundle args = new Bundle();
                    args.putInt(ARG_PREGACTJ,reactivo.getId() );
                    args.putBoolean(ARG_ESEDIJ,false);
                    DetalleProductoJumFragment nvofrag = new DetalleProductoJumFragment();
                    nvofrag.setArguments(args);
                    //DetalleProductoJumFragment nvofrag = new DetalleProductoJumFragment(reactivo, false);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
// Definir una transacción
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
// Remplazar el contenido principal por el fragmento
                    fragmentTransaction.replace(R.id.continf_fragment, nvofrag);
                    //   fragmentTransaction.addToBackStack(null);
// Cambiar
                    fragmentTransaction.commit();
                }
            }
        });
    }
}
