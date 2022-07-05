package com.example.comprasmu.ui.gallery;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeWithDetalle;
import com.example.comprasmu.data.modelos.Visita;
import com.example.comprasmu.data.remote.InformeEnvio;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.ProductoExhibidoRepositoryImpl;
import com.example.comprasmu.data.repositories.VisitaRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PruebaFotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PruebaFotosFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImagenDetRepositoryImpl imagenDetRepository;
    private  InformeCompraRepositoryImpl repository;


    private VisitaRepositoryImpl visitaRepository;
    public PruebaFotosFragment() {
        // Required empty public constructor
         }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PruebaFotosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PruebaFotosFragment newInstance(String param1, String param2) {
        PruebaFotosFragment fragment = new PruebaFotosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        this.imagenDetRepository=new ImagenDetRepositoryImpl(getContext());
        this.repository = new InformeCompraRepositoryImpl(getContext());

        this.visitaRepository=new VisitaRepositoryImpl(getContext());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_prueba_fotos, container, false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,
                false);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.rv_imagenes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        List<ImagenDetalle> lista=this.buscarImagenes(1,2,null);

        ImageGalleryAdapter adapter = new ImageGalleryAdapter(getContext(),lista);
        recyclerView.setAdapter(adapter);
        return root;
          }

    public List<ImagenDetalle> buscarImagenes(int idvisita, int idinforme, List<InformeCompraDetalle> detalles) {
        //todas las fotos aqui
        InformeWithDetalle informe=repository.getInformeWithDetalleByIdsimple(idinforme);
          Visita visita=visitaRepository.findsimple(informe.informe.getVisitasId());
        List<ImagenDetalle> fotosinfo = new ArrayList<>();
        //la visita

        ImagenDetalle imagenDetalle = getFoto(visita.getFotoFachada());
        if (imagenDetalle != null)
            fotosinfo.add(imagenDetalle);
        //las del informe
        List<Integer> arrFotos=new ArrayList<>();
        arrFotos.add(informe.informe.getCondiciones_traslado());
        arrFotos.add(informe.informe.getTicket_compra());
        List<ImagenDetalle> imagenDetalles=imagenDetRepository.findListsencillo(arrFotos);
        if(imagenDetalles!=null)
            fotosinfo.addAll(imagenDetalles);
        return fotosinfo;


    }
    public ImagenDetalle getFoto(int idfoto){


        return imagenDetRepository.findsimple(idfoto);

    }
}