package com.example.comprasmu.ui.gallery;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.comprasmu.R;
import com.example.comprasmu.data.dao.ProductoExhibidoDao;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.modelos.InformeWithDetalle;
import com.example.comprasmu.data.modelos.Visita;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.ui.RevisarFotoActivity;
import com.example.comprasmu.ui.empaque.VerEmpaqueFragment;
import com.example.comprasmu.ui.informe.NuevoinformeFragment;
import com.example.comprasmu.ui.informe.NuevoinformeViewModel;
import com.example.comprasmu.ui.informe.VerInformeFragment;
import com.example.comprasmu.ui.informe.VerInformeViewModel;
import com.example.comprasmu.ui.informedetalle.NuevoDetalleViewModel;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.ui.InformesGenViewModel;
import com.example.comprasmu.utils.ui.VerInformeGenFragment;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

public class GalFotosFragment extends Fragment {

    public LiveData<InformeCompraDetalle> informeSel;
    public LiveData<InformeEtapaDet> informeEtaSel;
    private NuevoDetalleViewModel dViewModel;
    private NuevoinformeViewModel niViewModel;
    private InformesGenViewModel igViewModel;
    List<ImagenDetalle> fotos;
    MutableLiveData<Integer> cont;
    View root;
    private VerInformeViewModel mViewModel;
    private static final String TAG="GalFotosFragment";
    public String tipoinf;
    LiveData<List<InformeEtapaDet>> listafotos;
  ComprasLog compraslog;

    public static GalFotosFragment newInstance() {
        return new GalFotosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(VerInformeViewModel.class);

        dViewModel=new ViewModelProvider(this).get(NuevoDetalleViewModel.class);
        niViewModel=new ViewModelProvider(this).get(NuevoinformeViewModel.class);
        igViewModel=new ViewModelProvider(this).get(InformesGenViewModel.class);
        root= inflater.inflate(R.layout.gal_fotos_fragment, container, false);
        cont=new MutableLiveData<>();
       compraslog=ComprasLog.getSingleton();
        return  root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        Bundle params = getArguments();
        Log.d(TAG,"--"+Constantes.ETAPAACTUAL);
        if(params!=null) {
              tipoinf= params.getString(VerInformeGenFragment.ARG_TIPOINF);
            int idmuestra = params.getInt(VerInformeFragment.ARG_IDMUESTRA);
            if(tipoinf!=null&&tipoinf.equals("action_selclitocor2")) {
            }
            else
              if(tipoinf!=null&&tipoinf.equals("e")){
                  //es etapa
                  if(Constantes.ETAPAACTUAL==1){
                      listafotos=igViewModel.getfotosPrep(idmuestra);
                    startuiEta(idmuestra);
                  }
                  if(Constantes.ETAPAACTUAL==3){//etiquetado
                      listafotos=igViewModel.getfotosxetapa(idmuestra,3);

                      startuiEta(idmuestra);
                  }
                  if(Constantes.ETAPAACTUAL==4){
                      int infsel = params.getInt(VerEmpaqueFragment.ARG_INFSEL);
                      Log.d(TAG,infsel+"--"+Constantes.ETAPAACTUAL+"--"+idmuestra);
                      listafotos=igViewModel.getfotosxetapaxcaj(infsel,Constantes.ETAPAACTUAL,idmuestra);

                      startuiEta(idmuestra);
                  }
              }else {

                  if (idmuestra > 0) //es compra detalle
                      startuiDet(idmuestra);
                  else {
                      int idinforme = params.getInt(NuevoinformeFragment.INFORMESEL);
                      startuiInf(idinforme);
                  }
              }
        }

    }


    public void startuiDet(int idmuestra){

        informeSel= dViewModel.getMuestra(idmuestra);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,
                false);
        RecyclerView recyclerView = root.findViewById(R.id.rv_viimagenes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        informeSel.observe(getViewLifecycleOwner(), new Observer<InformeCompraDetalle>() {
          @Override
           public void onChanged(InformeCompraDetalle informeCompraDetalle) {
              ponerDatosDet(informeCompraDetalle);
              ImageGalleryAdapter adapter = new ImageGalleryAdapter(getContext(),fotos);
              recyclerView.setAdapter(adapter);

            }
        });

     //   List<ImagenDetalle> lista=this.buscarImagenes(1,2,null);



    }
    public void startuiInf(int infid){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,
                false);
        RecyclerView recyclerView = root.findViewById(R.id.rv_viimagenes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        mViewModel.buscarInforme(infid);
        mViewModel.informeCompraSel.observe(getViewLifecycleOwner(), new Observer<InformeWithDetalle>() {
            @Override
            public void onChanged(InformeWithDetalle informeWithDetalle) {
                if (mViewModel.informeCompraSel.getValue() != null) {
                    mViewModel.getVisita(informeWithDetalle.informe.getVisitasId()).observe(getViewLifecycleOwner(), new Observer<Visita>() {
                        @Override
                        public void onChanged(Visita visita) {
                            llenarFotosInf(informeWithDetalle.informe,visita);
                            ImageGalleryAdapter adapter = new ImageGalleryAdapter(getContext(),fotos);
                            recyclerView.setAdapter(adapter);

                        }
                    });
                }
            }
        });

        //   List<ImagenDetalle> lista=this.buscarImagenes(1,2,null);



    }

    public void startuiEta(int idmuestra){


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,
                false);
        RecyclerView recyclerView = root.findViewById(R.id.rv_viimagenes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        ImageGalleryAdapter adapter = new ImageGalleryAdapter(getContext());

        recyclerView.setAdapter(adapter);
        //paso los detalle a imagenes

        listafotos.observe(getViewLifecycleOwner(), new Observer<List<InformeEtapaDet>>() {
                    @Override
                    public void onChanged(List<InformeEtapaDet> informeEtapaDets) {
                        Log.d(TAG,informeEtapaDets.size()+"--");
                    //paso todo a imagendet
                        fotos=new ArrayList<>();
                        if(Constantes.ETAPAACTUAL==1)
                        for(InformeEtapaDet inf:informeEtapaDets){
                            //busco la imagen detalle
                            try {
                                ImagenDetalle id= igViewModel.getfotoxid(inf.getRuta_foto());
                                fotos.add(id);
                            }catch (NumberFormatException ex){
                                compraslog.grabarError(TAG+ " "+ex.getMessage());
                        }


                        }else
                        if(Constantes.ETAPAACTUAL==4)
                            for(InformeEtapaDet inf:informeEtapaDets){
                                //busco la imagen detalle
                                try {
                                    ImagenDetalle id= igViewModel.getfotoxid(inf.getRuta_foto());
                                    //id.setDescripcion(inf.getDescripcion());

                                    fotos.add(id);
                                }catch (NumberFormatException ex){
                                    compraslog.grabarError(TAG+ " "+ex.getMessage());
                                }


                            }else
                        if(Constantes.ETAPAACTUAL==3)
                            for(InformeEtapaDet inf:informeEtapaDets){
                                ImagenDetalle id= igViewModel.getfotoxid(inf.getRuta_foto());

                                //  id.setDescripcion(inf.getDescripcion());
                                if(inf.getDescripcionId()==11) //son de etiqueta
                                     id.setDescripcion(inf.getQr()+"\r\nCAJA:"+inf.getNum_caja());
                               else
                                   //son de muestra
                                    id.setDescripcion(inf.getDescripcion()+"\r\nCAJA:"+inf.getNum_caja());

                                fotos.add(id);
                            }
                      //  ImageGalleryAdapter adapter = new ImageGalleryAdapter(getContext());
                        adapter.setmSpacePhotos(fotos);
                        adapter.notifyDataSetChanged();
                      //  recyclerView.setAdapter(adapter);




            }
        });

        //   List<ImagenDetalle> lista=this.buscarImagenes(1,2,null);



    }

    public void ponerDatosDet(InformeCompraDetalle informe) {

        fotos= new ArrayList<ImagenDetalle>();

        ponerFoto(getString(R.string.foto_codigo_produccion),informe.getFoto_codigo_produccion());

       // ponerFoto(informe.getEnergia());
      //  ponerFoto(getString(R.string.foto_num_tienda),informe.getFoto_num_tienda());
       // ponerFoto(getString(R.string.foto_codigo_produccion)informe.getMarca_traslape());
        ponerFoto(getString(R.string.foto_posicion1),informe.getFoto_atributoa());
        ponerFoto(getString(R.string.foto_posicion2),informe.getFoto_atributob());
        ponerFoto(getString(R.string.foto_posicion3),informe.getFoto_atributoc());
        ponerFoto(getString(R.string.etiqueta_evaluacion),informe.getEtiqueta_evaluacion());
       // ponerFoto(informe.getQr());
      //  ponerFoto(informe.getAzucares());


    }
    //nombre del txt dela ruta
    //id de imagendetalle
    //id imageview
    //objeto imagendetalle

    public void ponerFoto(String key, int idfoto){
        ImagenDetalle foto=niViewModel.getFoto(idfoto);

        if(foto!=null) {
         foto.setDescripcion(key);
         fotos.add(foto);
        }

    }
    public void llenarFotosInf(InformeCompra informe, Visita visita) {

        //reviso la memoria
        if (getAvailableMemory().lowMemory) {
            Toast.makeText(getActivity(), "No hay memoria suficiente para esta accion", Toast.LENGTH_SHORT).show();

            return;
        } else {
            fotos = new ArrayList<ImagenDetalle>();
            ponerFoto(getString(R.string.foto_fachada), visita.getFotoFachada());
            List<ProductoExhibidoDao.ProductoExhibidoFoto> productoExhibidoFotos= mViewModel.getproductoExhibsimp(visita.getId(), informe.getClientesId());

            if (productoExhibidoFotos != null && productoExhibidoFotos.size() > 0) {
                        //      mBinding.setProdex(productoExhibidoFotos.get(0));
                        //    mBinding.ivuiprodex.setImageBitmap(ComprasUtils.decodeSampledBitmapFromResource(directorio+productoExhibidoFotos.get(0).ruta
                        //         , 100, 100));
                        //convierto el prod exhibido a imagen detalle
                for (ProductoExhibidoDao.ProductoExhibidoFoto pf : productoExhibidoFotos) {
                            ImagenDetalle id = new ImagenDetalle();
                            Log.d(TAG,"foto ex"+pf.ruta);
                            id.setRuta(pf.ruta);
                            id.setDescripcion("EXHIBIDOR " + pf.nombreCliente);
                            id.setId(pf.imagenId);
                            fotos.add(id);
                        }
               /*  mBinding.ivuiprodex.setOnClickListener(new View.OnClickListener() {
                     @Override
                      public void onClick(View view) {
                          verImagen(productoExhibidoFotos.get(0).ruta);
                      }
                  });*/
            }

            ponerFoto(getString(R.string.ticket_compra), informe.getTicket_compra());
            ponerFoto(getString(R.string.condiciones), informe.getCondiciones_traslado());

        }

    }
    // Get a MemoryInfo object for the device's current memory status.
    private ActivityManager.MemoryInfo getAvailableMemory() {
        ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }

    public void verImagen(String nombrearch){
        //  ImageView imagen=(ImageView)v;
        // imagen.get
        Log.d(TAG,nombrearch);
        Intent iverim=new Intent(getActivity(), RevisarFotoActivity.class);
        iverim.putExtra(RevisarFotoActivity.IMG_PATH1,nombrearch);
        startActivity(iverim);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        informeSel=null;
        dViewModel=null;
        niViewModel=null;
        fotos=null;
        cont=null;
       root=null;

    }

}