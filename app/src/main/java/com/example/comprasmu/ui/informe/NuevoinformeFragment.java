package com.example.comprasmu.ui.informe;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.SubirInformeTask;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.Visita;
import com.example.comprasmu.data.remote.InformeEnvio;
import com.example.comprasmu.databinding.FragmentNuevoinformeBinding;
import com.example.comprasmu.services.SubirFotoService;
import com.example.comprasmu.ui.BackActivity;
import com.example.comprasmu.ui.RevisarFotoActivity;
import com.example.comprasmu.ui.informedetalle.DetalleProductoFragment;
import com.example.comprasmu.ui.informedetalle.DetalleProductoFragment1;
import com.example.comprasmu.ui.informedetalle.InformeDetalleAdapter;
import com.example.comprasmu.ui.informedetalle.NuevoDetalleViewModel;
import com.example.comprasmu.ui.visita.AbririnformeFragment;
import com.example.comprasmu.utils.CampoForm;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;
import com.example.comprasmu.utils.Preguntasino;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.example.comprasmu.ui.listacompras.TabsFragment.ARG_CLIENTESEL;

/*****
 * solo necesito que llegue la visita seleccionada
 */
public class NuevoinformeFragment extends Fragment implements InformeDetalleAdapter.AdapterCallback {

    public static final String ARG_FOTOPRODUCTO ="comprasmu.ni.fotoprod" ;

    public static String NUMMUESTRA="comprasmu.ni.nummuestra";
    private NuevoinformeViewModel mViewModel;
    private NuevoDetalleViewModel dViewModel;

    CreadorFormulario cf;
    List<CampoForm> camposForm;
    InformeCompra informeEdicion;
    Visita visitaCont;

    MenuItem fav;
    Preguntasino primera ;
    Preguntasino segunda ;
    Preguntasino tercera ;
    ImageButton rotar2;
    ImageButton rotar1;

    int clienteSel;
    private int[] arrCampos={2001,2002,2003,R.id.tblnifotos};

    File ruta;
    private int totalLista=0;
    public final static String INFORMESEL="comprasmu.ni_informesel";
    public final static String ISEDIT="comprasmu.ni_isedit";
    public final static String ISCOMPLETE="comprasmu.ni_complete";
    public final static String ARG_NUEVOINFORME="comprasmu.ni_idinforme";
    private View root;
    private static final String TAG="NuevoInformeFragment";
    private FragmentNuevoinformeBinding mBinding;
    private InformeDetalleAdapter mListAdapter;
    CoordinatorLayout coordinator;
    private EditText foto1;
    boolean isEdit=false; //para saber si estoy editando

    private ImageView imageView;
    public static  int REQUEST_CODE_TAKE_PHOTO=1;
    public static  int REQUEST_CODE_2=2;

    Button guardar;
    Button finalizar;
    ImagenDetalle fototicket, fotocodiciones;
    LinearLayout contentmain;
    Spinner spclientes;
    PostInformeViewModel postviewModel;
    List<InformeCompraDetalle> muestras;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d("NuevoInformeFragmnet",">>creando fragment");

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel =
                new ViewModelProvider(this).get(NuevoinformeViewModel.class);
        dViewModel =
                new ViewModelProvider(this).get(NuevoDetalleViewModel.class);

        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_nuevoinforme, container, false);
        root=mBinding.getRoot();
        contentmain = root.findViewById(R.id.content_mainni);
        //recupero el id seleccionado

        Log.d("NuevoInformeFragmnet",">>creando fragment2");

        guardar=root.findViewById(R.id.btnniguardar);
        spclientes = root.findViewById(R.id.spnicliente);
        loadData();
      /*  Button cancelar=root.findViewById(R.id.btnnicancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // guardar();
            }
        });*/
         finalizar=root.findViewById(R.id.btnnifinalizar);
        finalizar.setVisibility(View.GONE);
        finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finalizar();
            }
        });
        ImageButton foto1=(ImageButton)root.findViewById(R.id.btnni1);
        foto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarFoto(R.id.txtuiticket_compra,R.id.ivuiticket_compra);
            }
        });
        ImageButton foto2=(ImageButton)root.findViewById(R.id.btnni2);
        foto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarFoto(R.id.txtuicondiciones,R.id.ivuicondiciones);
            }
        });
        rotar1=(ImageButton)root.findViewById(R.id.btnnirotar1);
        rotar1.setVisibility(View.GONE);

        rotar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotar(R.id.ivuiticket_compra);
            }
        });
        rotar2=(ImageButton)root.findViewById(R.id.btnnirotcond);
        rotar2.setVisibility(View.GONE);
        rotar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotar(R.id.ivuicondiciones);
            }
        });

        if(Constantes.clientesAsignados!=null) {

            //busco los clientes

            Log.d(TAG,Constantes.clientesAsignados.size()+"pppppp");

            CreadorFormulario.cargarSpinnerDescr(getContext(),spclientes,Constantes.clientesAsignados);
        }


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mBinding.setLifecycleOwner(this);

        setupListAdapter();
        setupSnackbar();

        Log.d("NuevoInformeFragmnet",">>creando fragment3");


    }
    private void setupListAdapter() {
        mListAdapter = new InformeDetalleAdapter(this);
        mBinding.nicontentmuestras.rvnimuestras.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.nicontentmuestras.rvnimuestras.setHasFixedSize(true);
        mBinding.nicontentmuestras.rvnimuestras.setAdapter(mListAdapter);

    }
    private void setupSnackbar() {
        // Mostrar snackbar en resultados positivos de operaciones (crear, editar y eliminar)
        mViewModel.getSnackbarText().observe(getActivity(), integerEvent -> {
            Integer stringId = integerEvent.getContentIfNotHandled();
            if (stringId != null) {
                Snackbar.make(root.findViewById(R.id.coordinator),
                        stringId, Snackbar.LENGTH_LONG).show();
            }
        });
    }
    int i=0,j=0,k=0;
    boolean habilitar=false;
    View sig3=null;
    View sig4=null;
    /***para la edicion***/
    private void loadData() {
        if (getArguments().getInt(ISEDIT)==1) {
            //es edicion
            isEdit= true;
            int idinformeSel=getArguments().getInt(INFORMESEL);
            mViewModel.startInforme(idinformeSel);
            spclientes.setEnabled(false);
            //lleno los campos
            mViewModel.informeEdicion.observe(getViewLifecycleOwner(), new Observer<InformeCompra>() {
                @Override
                public void onChanged(InformeCompra informe) {
                    informeEdicion=informe;
                    mViewModel.informe=informe;
                    //busco la visita
                    mViewModel.visitaEdicion=mViewModel.buscarVisita(informe.getVisitasId());
                  //  visitaCont=mViewModel.visitaEdicion;
                  //  Constantes.DP_TIPOTIENDA=  mViewModel.visitaEdicion.getTipoId();
                    ponerDatos();
                    mBinding.setInforme(informe);

                }
            });
            guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actualizar();
                }
            });

        }else {


            //es nuevo
            Bundle datosRecuperados = getArguments();
            int idinformeSel = datosRecuperados.getInt(INFORMESEL);
            Log.d(TAG, "informe creado=" + idinformeSel);
            //busco la visita
            mViewModel.buscarVisita(idinformeSel).observeForever(new Observer<Visita>() {
                @Override
                public void onChanged(Visita visita) {
                    visitaCont = visita;
                     Constantes.DP_TIPOTIENDA= visita.getTipoId();
                    Log.d(TAG, "VISITA " + visita.getCiudad());
                    Log.d(TAG,"tipo tienda -----------*"+Constantes.DP_TIPOTIENDA);

                    mBinding.setVisita(visita);

                }
            });


            crearFormulario();

            if (getActivity() != null) {
                cf = new CreadorFormulario(camposForm, getActivity());
                contentmain.addView(cf.crearFormulario());
            } else {
                Log.d(TAG, "porque no hay actividad");
            }
            guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    guardar();
                }
            });
            habilitarDes(false,true,true,true);

        }
        primera =root.findViewById(2001);

        segunda = root.findViewById(2002);
        tercera = root.findViewById(2003);
        tercera.setVisible(View.GONE);

    }
        public void habilitarDes(boolean valor, boolean preg2, boolean preg3, boolean fotos) {
            //deshabilito casi todos los campos
                if(preg2) {
                    Preguntasino v = root.findViewById(2002);
                    if (v != null)
                        v.setEnabled(valor);
                }
               if(preg3) {
                   Preguntasino v2 = root.findViewById(2003);
                   if (v2 != null)
                       v2.setEnabled(valor);
               }
            if(fotos) {
                LinearLayout layout = (LinearLayout) root.findViewById(R.id.tblnifotos);
                ComprasUtils.loopViews(layout, valor);
            }

        }

          /*   for(i=0;i<4;i=i+2) {
                 if(i%2==0){
                     j=i+2;
                     k=i+3;

                 }else
                 {
                     j=i+1;
                     k=i+2;

                 }
                 RadioButton v = root.findViewById(arrCampos[i]);
                 View sig = root.findViewById(arrCampos[j]);
                 View sig2 = root.findViewById(arrCampos[k]);
                 v.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                     @Override
                     public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                          if(b) {
                             //habilito el siguiente

                             if (sig != null) {
                                 sig.setEnabled(true);
                             }

                             if (sig2 != null) {
                                 sig2.setEnabled(true);
                             }
                         }
                     }
                 });
             }
             for(i=1;i<4;i=i+2) {

                     j=i+1;
                     k=i+2;

                 RadioButton v = root.findViewById(arrCampos[i]);
                 View sig = root.findViewById(arrCampos[j]);
                 View sig2 = root.findViewById(arrCampos[k]);

                 if(j<4) {
                      sig3 = root.findViewById(arrCampos[j + 2]);
                      sig4 = root.findViewById(arrCampos[k + 2]);
                 }
                 v.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                     @Override
                     public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                         if(b) {
                             //habilito el siguiente

                             if (sig != null) {
                                 sig.setEnabled(false);
                             }

                             if (sig2 != null) {
                                 sig2.setEnabled(false);
                             }
                             if (sig3 != null) {
                                 sig3.setEnabled(false);
                             }
                             if (sig4 != null) {
                                 sig4.setEnabled(false);
                             }
                         }
                     }
                 });
             }*/


    public void rotar(int idfoto){
        ImageView foto = root.findViewById(idfoto);
        if(AbririnformeFragment.getAvailableMemory(getActivity()).lowMemory)
        {
            Toast.makeText(getActivity(), "No hay memoria suficiente para esta accion", Toast.LENGTH_SHORT).show();

            return;
        }else
            RevisarFotoActivity.rotarImagen(getActivity().getExternalFilesDir(null) + "/" +nombre_foto,foto);
    }
    public void ponerDatos() {


        ponerFoto(informeEdicion.getTicket_compra(), R.id.txtuiticket_compra, R.id.ivuiticket_compra, "fotofachada");

        ponerFoto(informeEdicion.getCondiciones_traslado(), R.id.txtuicondiciones, R.id.ivuicondiciones, "fotocondiciones");
        mViewModel.visitaEdicion.observe(getViewLifecycleOwner(), new Observer<Visita>() {
            @Override
            public void onChanged(Visita visita) {
                visitaCont = visita;
                Log.d(TAG, "encontre visita" + visita.getId());
                mBinding.setVisita(visita);
            }
        });

        // mViewModel.cargarMuestras();
    /*  mViewModel.muestras.observe(getViewLifecycleOwner(), new Observer<List<InformeCompraDetalle>>() {
            @Override
            public void onChanged(List<InformeCompraDetalle> detalles) {
                if(detalles.size()>0) {

                    Log.d(TAG, "YA CARGÓxxx " + detalles.size());
                    mListAdapter.setInformeCompraDetalleList(detalles,false);
                    mListAdapter.notifyDataSetChanged();
                    //si ya tengo muestra no puedo cambiar el radiobutton
                    //ya no los pinta
                   // root.findViewById(2001).setEnabled(false);
                   // root.findViewById(2002).setEnabled(false);
                  /*  if(detalles.size()>1) {
                        root.findViewById(2003).setEnabled(false);
                        root.findViewById(2004).setEnabled(false);
                    }*/
                 /*   if(detalles.size()>2&&totalLista>=16) {
                        root.findViewById(2005).setEnabled(false);
                        root.findViewById(2006).setEnabled(false);
                    }*/
              /*  }

            }
        });*/


    }
    public void ponerFoto( int idfoto,int fotoinput,int imageview,String detalle) {

        ImagenDetalle s=mViewModel.getFoto(idfoto);
                if(s!=null) {

                    Bitmap bitmap1 = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null) + "/" + s.getRuta());
                    ImageView ivfoto = root.findViewById(imageview);
                    EditText input = root.findViewById(fotoinput);
                    switch (detalle) {
                        case "fototicket":
                            mBinding.setRutaTicket(s.getRuta());
                            fototicket = s;
                            break;
                        case "fotocodiciones":
                            mBinding.setRutaCondiciones(s.getRuta());
                            fotocodiciones = s;
                            break;

                    }
                    input.setText(s.getRuta());
                    ivfoto.setImageBitmap(bitmap1);
                }


    }

    public void crearFormulario() {
        CampoForm campo = new CampoForm();
        campo.label = getString(R.string.se_compro_producto);
        campo.nombre_campo = "3001";
        campo.type = "preguntasino";
        campo.value = null;
        campo.id=2001;
        campo.required = "required";
        campo.funcionOnClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                habilitarDes(true,true,false,false);
                onRadioButtonClicked(view,1);

            }
        };
        campo.funcionOnClick2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"click en no");
                habilitarDes(false,true,true,true);

                habilitarDes(true,false,false,true);

            }
        };
        camposForm = new ArrayList<CampoForm>();
        camposForm.add(campo);
        campo = new CampoForm();
        campo.label = getString(R.string.se_compro_segunda);
        campo.nombre_campo = "3002";
        campo.id = 2002;
        campo.type = "preguntasino";
        campo.value = null;

        campo.funcionOnClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                habilitarDes(true,true,true,totalLista<16);
                onRadioButtonClicked(view,2);

            }
        };
        campo.funcionOnClick2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                habilitarDes(false,false,true,true);
                habilitarDes(true,false,false,true);
            }
        };
        campo.required = "required";

        camposForm.add(campo);

            campo = new CampoForm();
            campo.label = getString(R.string.se_compro_terc);
            campo.nombre_campo = "3003";
            campo.type = "preguntasino";
            campo.value = null;
            campo.id = 2003;
            campo.funcionOnClick = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    habilitarDes(true,true,true,true);

                    onRadioButtonClicked(view,3);
                }
            };
            campo.funcionOnClick2 = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    habilitarDes(true,true,true,true);

                    onNoClicked(view);
                }
            };
            campo.required = "required";
            camposForm.add(campo);

    }

   /* public void crearFormulario(){
        CampoForm campo=new CampoForm();
        campo.label=getString(R.string.se_compro_producto);
        campo.nombre_campo="3001";
        campo.type="radiobutton";
        campo.value=null;

        campo.required="required";
        HashMap<Integer,String> mapa1=new HashMap<>();
        mapa1.put(2001,"Sí");
        mapa1.put(2002,"No");
        campo.select=mapa1;
        campo.funcionOnClick=new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onRadioButtonClicked(view);
            }
        };
        camposForm=new ArrayList<CampoForm>();
        camposForm.add(campo);
        campo=new CampoForm();
        campo.label=getString(R.string.se_compro_segunda);
        campo.nombre_campo="3002";
        campo.type="radiobutton";
        campo.value=null;
        HashMap<Integer,String> mapa2=new HashMap<>();
        mapa2.put(2003,"Sí");
        mapa2.put(2004,"No");
        campo.select=mapa2;
        campo.funcionOnClick=new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onRadioButtonClicked(view);
            }
        };

        campo.required="required";

        camposForm.add(campo);
        if(totalLista>=16) {
            campo = new CampoForm();
            campo.label = getString(R.string.se_compro_terc);
            campo.nombre_campo = "3003";
            campo.type = "radiobutton";
            campo.value = null;
            HashMap<Integer,String> mapa3=new HashMap<>();
            mapa3.put(2005,"Sí");
            mapa3.put(2006,"No");
            campo.select=mapa3;
            campo.funcionOnClick=new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    onRadioButtonClicked(view);
                }
            };
            campo.required = "required";
            camposForm.add(campo);
        }
     /*   campo=new CampoForm();
        campo.label="Comentarios";
        campo.nombre_campo="comentarios";
        campo.type="textarea";
        campo.value=null;
        campo.required="required";
        campo.id=R.string.form_informe_comentarios;
        camposForm.add(campo);
        campo=new CampoForm();
        campo.label="Foto ticket";
        campo.nombre_campo="ticket_compra";
        campo.type="agregarImagen";
        campo.value=null;
        campo.id=R.string.form_informe_ticketcompra;
        campo.required="required";
        camposForm.add(campo);
        campo=new CampoForm();
        campo.label="Condiciones de traslado";
        campo.nombre_campo="condiciones_traslado";
        campo.type="agregarImagen";
        campo.value=null;
        campo.id=R.string.form_informe_condiciones;
        campo.required="required";
        camposForm.add(campo);*/


    //}

    public void onRadioButtonClicked(View view,int nummuestra) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        Log.d(TAG,"CLICK EN RADIOBUTTON ID="+view.getId());

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.cvrbsi:
                if (checked)
                    // fue si mostrar lista de compra
                    verListaCompra(nummuestra);
                break;

            default: break;
        }
    }
    public void onNoClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        Log.d(TAG, "CLICK EN RADIOBUTTON ID=" + view.getId());

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        menu.clear();
        inflater.inflate(R.menu.menu_nuevoinforme, menu);
      //  super.onCreateOptionsMenu(menu, inflater);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_saveinforme:
                guardar();
                return true;

            default:
                break;
        }

        return false;
    }

    public void guardar(){
        EditText ticket=root.findViewById(R.id.txtuiticket_compra);
        EditText comentarios=root.findViewById(R.id.txtvicomentarios);
        EditText condiciones=root.findViewById(R.id.txtuicondiciones);

        //hago las validaciones
        if(ticket.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Falta capturar ticket de compra", Toast.LENGTH_SHORT).show();

            return;
        }
        if(comentarios.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Falta capturar los comentarios", Toast.LENGTH_SHORT).show();
            return;
        }
        if(condiciones.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Falta capturar condiciones de traslado", Toast.LENGTH_SHORT).show();
            return;
        }


        if(!primera.getRespuesta()){
            //pido el cliente y planta


        }else
        {
            //reviso que ya tenga detalles
            if(mListAdapter.getItemCount()<=0){

                Toast.makeText(getActivity(), "Falta capturar datos de la muestra", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(segunda.getRespuesta()) {
            //reviso que ya tenga detalles
            if(mListAdapter.getItemCount()<2){

                Toast.makeText(getActivity(), "Falta capturar datos de la segunda muestra", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(tercera!=null&&tercera.getRespuesta()) {
            //reviso que ya tenga detalles
            if(mListAdapter.getItemCount()<3){

                Toast.makeText(getActivity(), "Falta capturar datos de la tercera muestra", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        /*
        mViewModel.informe.setIndice(visitaCont.getIndice());
        nuevoInforme.setVisitasId(visitaCont.getId());*/

        mViewModel.informe.setComentarios(comentarios.getText().toString());
        mViewModel.informe.setPrimeraMuestra(primera.getRespuesta());
        mViewModel.informe.setSegundaMuestra(segunda.getRespuesta());
        mViewModel.informe.setTerceraMuestra(tercera.getRespuesta());
        mViewModel.informe.setVisitasId(visitaCont.getId());
        DescripcionGenerica clientesop=(DescripcionGenerica) spclientes.getSelectedItem();
        mViewModel.informe.setClienteNombre(clientesop.getNombre());

     //TODO falta el id   mViewModel.informe.setClientesId(Constantes.clientesAsignados.  get(spclientes.getSelectedItem() ));
       try {
           //guardo la foto
           //guardar imagen
           mViewModel.ticket_compra = new ImagenDetalle();

           mViewModel.ticket_compra.setRuta(ticket.getText().toString());
           mViewModel.ticket_compra.setDescripcion("ticket compra");
           mViewModel.ticket_compra.setEstatus(1);
           mViewModel.ticket_compra.setIndice(visitaCont.getIndice());
           mViewModel.ticket_compra.setEstatusSync(0);
           mViewModel.ticket_compra.setCreatedAt(new Date());

           mViewModel.condiciones_traslado = new ImagenDetalle();
           mViewModel.condiciones_traslado.setRuta(condiciones.getText().toString());
           mViewModel.condiciones_traslado.setDescripcion("condiciones traslado");
           mViewModel.condiciones_traslado.setEstatus(1);
           mViewModel.condiciones_traslado.setIndice(visitaCont.getIndice());
           mViewModel.condiciones_traslado.setEstatusSync(0);
           mViewModel.condiciones_traslado.setCreatedAt(new Date());
         //  mViewModel.informe = nuevoInforme;
           mViewModel.actualizarInforme();
           try {
               finalizar();
             }catch(Exception ex){
           ex.getStackTrace();
           Log.e(TAG,"Algo salió mal en finalizar"+ex.getMessage());
           Toast.makeText(getContext(),"Algo salio mal en finalizar",Toast.LENGTH_SHORT).show();
       }
           //pregunto si habrá más clientes
           AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
           dialogo1.setTitle(R.string.importante);
           dialogo1.setMessage(R.string.pregunta_mas_clientes);
           dialogo1.setCancelable(false);
           dialogo1.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialogo1, int id) {
                  //no hago nada
                   dialogo1.cancel();
               }
           });
           dialogo1.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialogo1, int id) {

                   //Es hora de cerrar el preinforme

                   mViewModel.finalizarVisita(visitaCont.getId());

                   Toast.makeText(getActivity(), "Se finalizó el preinforme",Toast.LENGTH_SHORT).show();

               }
           });
           dialogo1.show();
           salir();
       }catch(Exception ex){
           ex.getStackTrace();
           Log.e(TAG,"Algo salió mal+"+ex.getMessage());
           Toast.makeText(getContext(),"Algo salio mal+",Toast.LENGTH_SHORT).show();
       }
    }

    public void actualizar(){
        //solo edito comentarios, fotos y edito muestras
        //hago las validaciones
        EditText ticket=root.findViewById(R.id.txtuiticket_compra);
        EditText comentarios=root.findViewById(R.id.txtvicomentarios);
        EditText condiciones=root.findViewById(R.id.txtuicondiciones);

        if(ticket.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Falta capturar ticket de compra", Toast.LENGTH_SHORT).show();

            return;
        }
        if(comentarios.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Falta capturar los comentarios", Toast.LENGTH_SHORT).show();
            return;
        }
        if(condiciones.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Falta capturar condiciones de traslado", Toast.LENGTH_SHORT).show();
            return;
        }
        //si ya tiene muestras solo edita ticket de compra, condiciones y comentarios



        informeEdicion.setComentarios(comentarios.getText().toString());
       // informeEdicion.setPrimeraMuestra(primera.isChecked());
        //informeEdicion.setSegundaMuestra(segunda.isChecked());
        //informeEdicion.setTerceraMuestra(tercera.isChecked());
        try {
            //guardo la foto
            //guardar imagen
            mViewModel.ticket_compra = new ImagenDetalle();
            mViewModel.ticket_compra.setRuta(ticket.getText().toString());
            mViewModel.ticket_compra.setDescripcion("ticket compra");
            mViewModel.ticket_compra.setEstatus(1);

            mViewModel.ticket_compra.setEstatusSync(0);
            mViewModel.ticket_compra.setCreatedAt(new Date());

            mViewModel.condiciones_traslado = new ImagenDetalle();
            mViewModel.condiciones_traslado.setRuta(condiciones.getText().toString());
            mViewModel.condiciones_traslado.setDescripcion("condiciones traslado");
            mViewModel.condiciones_traslado.setEstatus(1);

            mViewModel.condiciones_traslado.setEstatusSync(0);
            mViewModel.condiciones_traslado.setCreatedAt(new Date());
          //  mViewModel.informe = nuevoInforme;
            mViewModel.actualizarInforme();
            finalizar();
            salir();
        }catch(Exception ex){
            ex.getStackTrace();
            Log.e(TAG,"Algo salió mal*"+ex.getMessage());
            Toast.makeText(getContext(),"Algo salio mal*",Toast.LENGTH_SHORT).show();
        }
    }

    public void finalizar() {

        //validar que si hay producto realmente tenga un producto capturado


        mViewModel.finalizarInforme();
        try {
            InformeEnvio informe=preparaInforme();
            SubirInformeTask miTareaAsincrona = new SubirInformeTask(true,informe,getActivity(),null);
            miTareaAsincrona.execute();


            subirFotos(getActivity(),informe);
        }catch(Exception ex){
            ex.getStackTrace();
            Log.e(TAG,"Algo salió mal al enviar"+ex.getMessage());
            Toast.makeText(getContext(),"Algo salio mal al enviar",Toast.LENGTH_SHORT).show();
        }


    }
    public static void subirFotos(Activity activity, InformeEnvio informe){
        //las imagenes
        for(ImagenDetalle imagen:informe.getImagenDetalles()){
            //subo cada una
            Intent msgIntent = new Intent(activity, SubirFotoService.class);
            msgIntent.putExtra(SubirFotoService.EXTRA_IMAGE_ID, imagen.getId());
            msgIntent.putExtra(SubirFotoService.EXTRA_IMG_PATH,imagen.getRuta());
            msgIntent.putExtra(SubirFotoService.EXTRA_INDICE,informe.getVisita().getIndice());
           // Constantes.INDICEACTUAL
            Log.d(TAG,"subiendo fotos"+activity.getLocalClassName());
            activity.startService(msgIntent);
            //cambio su estatus a subiendo



        }

    }

    public void salir(){
        //me voy a la lista de informes
        NavHostFragment.findNavController(this).navigate(R.id.action_informetolista);

    }

    public void verListaCompra(int nummuestra){
          /* b2undle.putString("plantaNombre", listaSeleccionable.get(i).getNombre());*/
        /*   NavHostFragment.findNavController(this).navigate(R.id.action_selclientetolistacompras,bundle);
         */
        //NavHostFragment.findNavController(this).navigate(R.id.action_lista compra);
        Intent intento1=new Intent(getActivity(), BackActivity.class);

        //ya existe el informe
        intento1.putExtra(NuevoinformeFragment.ARG_NUEVOINFORME,mViewModel.getIdInformeNuevo() );
        intento1.putExtra(BackActivity.ARG_FRAGMENT,BackActivity.OP_LISTACOMPRA);
        intento1.putExtra("ciudadSel",visitaCont.getCiudadId());
        intento1.putExtra("ciudadNombre",visitaCont.getCiudad());
        DescripcionGenerica opcionsel=(DescripcionGenerica)spclientes.getSelectedItem();
        clienteSel=opcionsel.getId();
        Constantes.ni_clientesel=opcionsel.getNombre();
        intento1.putExtra(ARG_CLIENTESEL,clienteSel);
        intento1.putExtra(NuevoinformeFragment.NUMMUESTRA,nummuestra );

        startActivityForResult(intento1, BackActivity.REQUEST_CODE);
      //  startActivity(intento1);

    }
    String nombre_foto;
    public   void tomarFoto(int origen, int destino) {
        Activity activity=this.getActivity();
        Intent intento1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");

        String dateString = format.format(new Date());
        File foto=null;
        try{
            nombre_foto = "img_"  +Constantes.CLAVEUSUARIO+"_"+ dateString + ".jpg";
            foto = new File(activity.getExternalFilesDir(null), nombre_foto);
            Log.e(TAG, "****"+foto.getAbsolutePath());
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(activity, "No se encontró almacenamiento externo", Toast.LENGTH_SHORT).show();


        }
        Uri photoURI = FileProvider.getUriForFile(activity,
                "com.example.comprasmu.fileprovider",
                foto);
        intento1.putExtra(MediaStore.EXTRA_OUTPUT, photoURI); //se pasa a la otra activity la referencia al archivo
        //intento1.putExtra("origen", origen);
        foto1=root.findViewById(origen);
        if(destino!=0) {
            imageView = root.findViewById(destino);
            startActivityForResult(intento1, REQUEST_CODE_TAKE_PHOTO);
        }
        else{
            startActivityForResult(intento1, REQUEST_CODE_2);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
           Log.d(TAG,"vars"+requestCode +"--"+ resultCode);
        if ((requestCode == REQUEST_CODE_TAKE_PHOTO||requestCode==REQUEST_CODE_2) && resultCode == RESULT_OK) {

            File file = new File(getActivity().getExternalFilesDir(null), nombre_foto);
            if (file.exists()) {
                if(requestCode == REQUEST_CODE_TAKE_PHOTO) {
                    //envio a la actividad dos para ver la foto
                    Intent intento1 = new Intent(getActivity(), RevisarFotoActivity.class);
                    intento1.putExtra("ei.archivo", nombre_foto);

                    foto1.setText(nombre_foto);
                    Bitmap bitmap1 = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null) + "/" + nombre_foto);
                    //comprimir
                    ComprasUtils cu=new ComprasUtils();
                    cu.comprimirImagen(getActivity().getExternalFilesDir(null) + "/" + nombre_foto);
                    imageView.setImageBitmap(bitmap1);
                    rotar1.setVisibility(View.VISIBLE);
                    rotar2.setVisibility(View.VISIBLE);
                }
                if(requestCode == REQUEST_CODE_2) {
                    Intent intento1 = new Intent(getActivity(), RevisarFotoActivity.class);
                    intento1.putExtra("ei.archivo", nombre_foto);
                    intento1.putExtra("ei.opcionfoto", "exhibicion");

                    startActivity(intento1);
                }

            }
            else{
                Log.d(TAG,"Algo salió mal???");
            }


        }else if(requestCode==BackActivity.REQUEST_CODE)
        {
            //capturé muestra
            if(resultCode==DetalleProductoFragment1.NUEVO_RESULT_OK) {
                Log.d(TAG,"ssssssssssssssssss"+data.getIntExtra(ARG_NUEVOINFORME, 0));
               // totalLista=data.getIntExtra(ARG_TOTALLISTA, 0);
                //muestro el de 3a muestra
                if(Constantes.NM_TOTALISTA>=16)
                    tercera.setVisible(View.VISIBLE);
                spclientes.setEnabled(false); //para que no cambie el cliente
               // if( mViewModel.informe.getId()==0) {
                 //   mViewModel.informe.setId(data.getIntExtra(ARG_NUEVOINFORME, 0));
                    //lo busco y cargo


                mViewModel.buscarInforme(data.getIntExtra(ARG_NUEVOINFORME, 0)).observe(getViewLifecycleOwner(), new Observer<InformeCompra>() {
                        @Override
                        public void onChanged(InformeCompra informeCompra) {
                            mViewModel.informe=informeCompra;
                            Log.d(TAG, "en resul " + mViewModel.informe.getId());



                //  switch(resultCode) {
                //    case BackActivity.PRIMERA_RESULT_OK:
                //}
                //vengo de lista de compra
                //leo el idinforme creado


                    //todo ok;
                    mViewModel.setIdInformeNuevo(mViewModel.informe.getId());
                //    mViewModel.cargarMuestras();

/*                    mViewModel.muestras.observe(getViewLifecycleOwner(), new Observer<List<InformeCompraDetalle>>() {
                        @Override
                        public void onChanged(List<InformeCompraDetalle> detalles) {
                            if (detalles.size() > 0) {
                                Log.d(TAG, "YA CARGÓxxx " + detalles.size());
                                mListAdapter.setInformeCompraDetalleList(detalles,false);
                                mListAdapter.notifyDataSetChanged();
                                //si ya tengo muestra no puedo cambiar el radiobutton
                                primera.setEnabled(false);
                              //  root.findViewById(2002).setEnabled(false);
                                spclientes.setEnabled(false);
                                if (detalles.size() > 1) {
                                    segunda.setEnabled(false);
                                   // root.findViewById(2004).setEnabled(false);
                                }
                                if (detalles.size() > 2 && totalLista >= 16) {
                                    tercera.setEnabled(false);
                                   // root.findViewById(2006).setEnabled(false);
                                }
                            }

                        }
                    });*/
                        }
                    });
                    mBinding.setInforme(mViewModel.informe);
             //   }


            }

        }  else
        {
            Log.d(TAG,"Algo salió muy mal");
        }
    }


    @Override
    public void onClickVer(int codigos) {


    }

    public InformeEnvio preparaInforme(){
        InformeEnvio envio=new InformeEnvio();
        envio.setVisita(visitaCont);
        envio.setInformeCompra(mViewModel.informe);
        envio.setInformeCompraDetalles(mListAdapter.getmInformeCompraDetalleList());
        envio.setImagenDetalles(mViewModel.buscarImagenes(visitaCont,mViewModel.informe,mListAdapter.getmInformeCompraDetalleList()));
        return envio;
    }






    @Override
    public void onClickCancelar(InformeCompraDetalle iddet) {
        try{
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
            dialogo1.setTitle(R.string.importante);
            dialogo1.setMessage(R.string.pregunta_eliminar_mensaje);
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    //busco la muestra a eliminar

                    dViewModel.eliminarMuestra(iddet);

                    Toast.makeText(getActivity(), "Se eliminó el registro",Toast.LENGTH_SHORT).show();

                }
            });
            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    dialogo1.cancel();
                }
            });
            dialogo1.show();

        }
        catch (Exception ex){
            Log.e(TAG,"Error"+ ex.getMessage());
            Toast.makeText(getActivity(), "Hubo un error al eliminar", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onClickEditar(int id) {
        //editar muestra
        Log.d(TAG, "editar muestra");

        Intent intento1=new Intent(getActivity(), BackActivity.class);
       // Bundle bundle = new Bundle();

        intento1.putExtra(NuevoinformeFragment.INFORMESEL,mViewModel.getIdInformeNuevo() );
        intento1.putExtra(BackActivity.ARG_FRAGMENT,BackActivity.OP_DETALLE_PRODUCTO);
        intento1.putExtra(DetalleProductoFragment1.ARG_IDMUESTRA,id);
      //  intento1.putExtra(DetalleProductoFragment1.ARG_IDINFORME,id);

        startActivityForResult(intento1, BackActivity.REQUEST_CODE);
        //  startActivity(intento1);
    }


}