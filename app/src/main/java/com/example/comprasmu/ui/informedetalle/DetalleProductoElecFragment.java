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
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;
import com.example.comprasmu.utils.Preguntasino;
import com.example.comprasmu.utils.ui.LoadingDialog;

import java.io.File;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class DetalleProductoElecFragment extends DetalleProductoPenFragment{
    protected static final String TAG="DETALLEPRODUCTOELECFRAG";
    public DetalleProductoElecFragment() {

    }
    public DetalleProductoElecFragment(Reactivo preguntaAct, boolean edicion) {
        this.preguntaAct = preguntaAct;
        this.isEdicion=edicion;
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
            Log.d(TAG,"creando fragment "+preguntaAct.getId());
            dViewModel.reactivoAct=preguntaAct.getId();
            sv = root.findViewById(R.id.content_generic);
            aceptar = root.findViewById(R.id.btngaceptar);
            //   mViewModel.cargarCatsContinuar();
            //si es la misma
            //reviso si es edicion o es nueva
            if(preguntaAct.getTabla().equals("I"))
                mViewModel.numMuestra=0;
            if(this.preguntaAct!=null)
                ultimares=dViewModel.buscarxNombreCam(this.preguntaAct.getNombreCampo(),mViewModel.numMuestra);
            Log.d(TAG,"------"+ Constantes.NM_TOTALISTA+"---"+mViewModel.consecutivo);

            if(ultimares!=null) {    //es edicion
                isEdicion = true;
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
                    ultimares=null;
                    isEdicion=false;

                }
            }
            else
                //if(this.preguntaAct.getId()==2||this.preguntaAct.getId()==3||this.preguntaAct.getId()==5)
                isEdicion=false;


            if(isEdicion) {
                aceptar.setEnabled(true);

                mViewModel.consecutivo=ultimares.getConsecutivo();
                Constantes.DP_CONSECUTIVO=mViewModel.consecutivo;
                //  ya lo busco en la actividad
                //   InformeTemp inf= dViewModel.buscarxNombreCam("numMuestra");
                //     mViewModel.numMuestra=inf==null?0:Integer.parseInt(inf.getValor());
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
                        if (charSequence.length()>0){ //count es cantidad de caracteres que tiene
                            aceptar.setEnabled(true);
                        }else{
                            aceptar.setEnabled(false);
                        }

                    }


                });
            }else
            if(textoint!=null&&preguntaAct.getId()!=77){ //los comentarios no son obligatorios
                textoint.addTextChangedListener(new BotonTextWatcher());

            }
            if(preguntaAct.getId()==77){ //los comentarios no son obligatorios
                //  textoint.addTextChangedListener(new MayusTextWatcher());
                textoint.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
            }

            if(preguntaAct.getId()==77||preguntaAct.getId()==87)
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
                    long currentClickTime= SystemClock.elapsedRealtime();
                    // preventing double, using threshold of 1000 ms
                    if (currentClickTime - lastClickTime < 5500){
                        return;
                    }

                    lastClickTime = currentClickTime;
                    Log.d(TAG,"di click :("+lastClickTime);
                    if(preguntaAct.getNombreCampo().equals("clientesId")){
                        guardarCliente();
                    }

                    siguiente();

                }
            });
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


        } catch (Exception e) {
            e.printStackTrace();
        }

        return root;
    }

    public void iniciarNumMuestra(){
        //es un nuevo informe o una nueva pregunta
        //if(mViewModel.numMuestra==0) {
        // &&preguntaAct.getId()!=5) {
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
        if(preguntaAct.getId()>=78) //ya tengo producto voy en siglas
        {
            //  Constantes.ni_clientesel=opcionsel.getNombre();
            //int consecutivo=mViewModel.getConsecutivo(valor);
            // Log.d(TAG,"genere cons="+consecutivo);
            mViewModel.informe=new InformeCompra();
            // nviewModel.informe.setClienteNombre(opcionsel.getNombre());
            //  nviewModel.informe.setClientesId(ultimares.getValor());
            mViewModel.informe.setConsecutivo(ultimares.getConsecutivo());
            mViewModel.consecutivo=ultimares.getConsecutivo();
            Constantes.DP_CONSECUTIVO=mViewModel.consecutivo;
            //  mViewModel.consecutivo=ultimares.getConsecutivo();
            ((ContinuarInformeActivity)getActivity()).actualizarCliente(mViewModel.informe);
            dViewModel.fromTemp(); //guardo datos del producto selec
            ((ContinuarInformeActivity)getActivity()).actualizarProdSel(dViewModel.productoSel);
        }
        if (preguntaAct.getId() >= 80&&preguntaAct.getId() !=89) {//si compro prod
            InformeTemp resp=dViewModel.buscarxNombreCam("codigo",mViewModel.numMuestra);
            ((ContinuarInformeActivity)getActivity()).actualizarCodProd(resp.getValor());

        }
        if(dViewModel.productoSel!=null)
        {
            getTomadoDe();}
        if (preguntaAct.getId() >= 81&&preguntaAct.getId()!=89) { //si hay prod


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
     /*   if (textoint != null) {
            String valor = textoint.getText().toString();
            if(valor.length()<=0){
                return;
            }

        }*/

        switch (preguntaAct.getNombreCampo()){
            case Contrato.TablaInformeDet.SIGLAS:

                resp=validarSiglas();
                break;
               /* case Contrato.TablaInformeDet.CODIGO:
                   resp=validarCodigoprod();
                    break;*/
            case Contrato.TablaInformeDet.CADUCIDAD:
                resp=validarFecha();
                // if(resp)
                //    resp=validarCodigoprod();
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
                if(!pregunta.getRespuesta()&&preguntaAct.getId()!=88) //se selecciono no
                {
                    //voy al altsig
                    guardarResp();
                    //guarda informe

                    avanzarPregunta(preguntaAct.getSigAlt());
                    return;

                }
            }

            if(preguntaAct.getSigId()==3000) //voy a lista de compra
            {
                guardarResp();
                compraProd(pregunta,preguntaAct.getId() - 71);
                //no funcionará para la 4a muestra
            }else
            if(preguntaAct.getSigId()==88) //termine inf compro para otros clientes
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
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //guarda informe
                this.actualizarInforme();
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
                  //  mViewModel.finalizarInforme();
                    //la muestra la guarde en la 42

                    Log.d(TAG,"dice que no");
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

            }else
            if(preguntaAct.getSigId()==0)//terminé con preguntas de muestra
            {
                if(yaestoyProcesando)
                    return;
                yaestoyProcesando=true;
                aceptar.setEnabled(false);
                int sig=mViewModel.numMuestra+72;

               // int nummuestra=mViewModel.numMuestra;
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
                  /*  RadioButton selectedRadioButton = root.findViewById(selectedRadioButtonId);
                    valor = selectedRadioButton.getText().toString();*/
                    //  valor=valor.toUpperCase();
                    valor = selectedRadioButtonId + "";
                }


                //busco planta
                List<ListaCompra> listapl=lcviewModel.cargarPlantas(mViewModel.visita.getCiudad(),mViewModel.clienteSel);

                if(listapl.size()>0)
                {
                    Log.d(TAG,"poniendo el cliente"+listapl.get(0).getClienteNombre());
                    //voy directo a la lista
                    plantaSel=listapl.get(0).getPlantasId() ;
                    NOMBREPLANTASEL=listapl.get(0).getPlantaNombre();
                    if(valor!=null&&valor.equals("7")) //es otras
                    {
                        //generar consecutivo tienda
                        MutableLiveData<Integer> consecutivo=mViewModel.getConsecutivo(plantaSel,getActivity(), this);
                        Log.d(TAG,"*genere cons="+consecutivo);
                        consecutivo.observe(this, new Observer<Integer>() {
                            @Override
                            public void onChanged(Integer cons) {
                                Log.d(TAG,"genere cons="+cons);

                                mViewModel.informe.setConsecutivo(cons);

                                mViewModel.consecutivo=cons;
                                consecutivo.removeObservers(DetalleProductoElecFragment.this);
                            }
                        });
                    }
                    mViewModel.guardarResp(0,0,plantaSel+"","plantasId","I",mViewModel.consecutivo,false);
                    mViewModel.guardarResp(0,0,NOMBREPLANTASEL+"","plantaNombre","I",mViewModel.consecutivo,false);
                    mViewModel.guardarResp(0,0,listapl.get(0).getClienteNombre(),"clienteNombre","I",mViewModel.consecutivo,false);

                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                guardarMuestra(preguntaAct.getSigId());
                loadingDialog.dismisDialog();

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

        //busco el siguiente

        nvoReac.observe(getViewLifecycleOwner(), new Observer<Reactivo>() {
            @Override
            public void onChanged(Reactivo reactivo) {
                if(sig==1) //pregunta de cliente o confirmacion vuelvo al detalleproducto1
                {

                    DetalleProductoFragment nvofrag = new DetalleProductoFragment(reactivo,false);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
// Definir una transacción
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
// Remplazar el contenido principal por el fragmento
                    fragmentTransaction.replace(R.id.continf_fragment, nvofrag);
               //     fragmentTransaction.addToBackStack(null);
// Cambiar
                    fragmentTransaction.commit();
                }else {
                    DetalleProductoElecFragment nvofrag = new DetalleProductoElecFragment(reactivo, false);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
// Definir una transacción
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
// Remplazar el contenido principal por el fragmento
                    fragmentTransaction.replace(R.id.continf_fragment, nvofrag);
                  //  fragmentTransaction.addToBackStack(null);
// Cambiar
                    fragmentTransaction.commit();
                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //    super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"vars"+requestCode +"--"+ nombre_foto);
        if ((requestCode == REQUEST_CODE_TAKE_PHOTO) && resultCode == RESULT_OK) {
           // super.onActivityResult(requestCode, resultCode, data);
            String state = Environment.getExternalStorageState();
            String baseDir;
            if(Environment.MEDIA_MOUNTED.equals(state)) {
                File baseDirFile = getActivity().getExternalFilesDir(null);
                if(baseDirFile == null) {
                    baseDir = getActivity().getFilesDir().getAbsolutePath();
                } else {
                    baseDir = baseDirFile.getAbsolutePath();
                }
            } else {
                baseDir = getActivity().getFilesDir().getAbsolutePath();
            }
            File file = new File(baseDir, nombre_foto);
            if (file.exists()) {
                if(requestCode == REQUEST_CODE_TAKE_PHOTO) {
                    //envio a la actividad dos para ver la foto
                    //    Intent intento1 = new Intent(getActivity(), RevisarFotoActivity.class);
                    //  intento1.putExtra("ei.archivo", nombre_foto);

                    textoint.setText(nombre_foto);

                    if(AbririnformeFragment.getAvailableMemory(getActivity()).lowMemory)
                    {
                        Toast.makeText(getActivity(), "No hay memoria suficiente para esta accion", Toast.LENGTH_SHORT).show();

                        return;
                    }else {
                        Bitmap bitmap1 = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null) + "/" + nombre_foto);
                        ComprasUtils cu = new ComprasUtils();
                        bitmap1 = cu.comprimirImagen(getActivity().getExternalFilesDir(null) + "/" + nombre_foto);
                        bitmap1 = ComprasUtils.decodeSampledBitmapFromResource(getActivity().getExternalFilesDir(null) + "/" + nombre_foto, 100, 100);
                        fotomos.setImageBitmap(bitmap1);
                        // fotomos.setLayoutParams(new LinearLayout.LayoutParams(350,150));
                        fotomos.setVisibility(View.VISIBLE);

                        btnrotar.setVisibility(View.VISIBLE);
                        btnrotar.setFocusableInTouchMode(true);
                        btnrotar.requestFocus();
                        nombre_foto=null;
                    }

                }


            }
            else{
                Log.e(TAG,"Algo salió mal???");
            }


        } else if(requestCode== BackActivity.REQUEST_CODE)
        {
          //  super.onActivityResult(requestCode, resultCode, data);
            //capturé muestra
            if(resultCode==NUEVO_RESULT_OK) {

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
                        MutableLiveData<Integer> consecutivo = mViewModel.getConsecutivo(dViewModel.productoSel.plantaSel, getActivity(), this);
                        //  Log.d(TAG, "*genere cons=" + consecutivo);
                        consecutivo.observe(this, new Observer<Integer>() {
                            @Override
                            public void onChanged(Integer cons) {
                                Log.d(TAG, "genere cons=" + cons);

                                mViewModel.informe.setConsecutivo(cons);

                                mViewModel.consecutivo = cons;
                                Constantes.DP_CONSECUTIVO=cons;
                                //actualizo barra
                                ((ContinuarInformeActivity) getActivity()).actualizarProdSel(dViewModel.productoSel);

                                mViewModel.guardarResp(0, 0, Constantes.NM_TOTALISTA + "", "totalLista", "", mViewModel.consecutivo, false);
                                ((ContinuarInformeActivity)getActivity()).actualizarCliente(mViewModel.informe);

                                avanzarPregunta(78);
                            }
                        });
                    }else
                    {
                        //actualizo barra
                        ((ContinuarInformeActivity) getActivity()).actualizarProdSel(dViewModel.productoSel);

                        mViewModel.guardarResp(0, 0, Constantes.NM_TOTALISTA + "", "totalLista", "", mViewModel.consecutivo, false);

                        avanzarPregunta(78);
                    }
                }else
                    Log.e(TAG,"Algo salió muy mal al elegir el producto");

                //lo busco y cargo


            }

        }   else
        {
            Log.e(TAG,"Algo salió muy mal**");
        }
    }


}
