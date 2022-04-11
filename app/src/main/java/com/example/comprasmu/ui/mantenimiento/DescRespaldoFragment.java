package com.example.comprasmu.ui.mantenimiento;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.comprasmu.DescargaRespAsyncTask;
import com.example.comprasmu.DescargasIniAsyncTask;
import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.ProductoExhibidoRepositoryImpl;
import com.example.comprasmu.data.repositories.VisitaRepositoryImpl;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DescRespaldoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DescRespaldoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DescRespaldoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DescRespaldoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DescRespaldoFragment newInstance(String param1, String param2) {
        DescRespaldoFragment fragment = new DescRespaldoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_desc_respaldo, container, false);
       Button boton1=root.findViewById(R.id.btndesc);
       boton1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
descargar();
           }
       });
        return  root;
    }

    public void descargar(){
        if(NavigationDrawerActivity.isOnlineNet()) {
            InformeComDetRepositoryImpl ifodrepo=new InformeComDetRepositoryImpl(getContext());
            InformeCompraRepositoryImpl infoRepo=new InformeCompraRepositoryImpl(getContext());
            ImagenDetRepositoryImpl imrepo=new ImagenDetRepositoryImpl(getContext());
            ProductoExhibidoRepositoryImpl prodrepo=new ProductoExhibidoRepositoryImpl(getContext());
            VisitaRepositoryImpl visrepo=new VisitaRepositoryImpl(getContext());

            DescargaRespAsyncTask task = new DescargaRespAsyncTask(infoRepo,ifodrepo,imrepo,visrepo,prodrepo,getActivity());

            //  textView.setText("Por favor mantengase en la aplicación hasta que termine la descarga");
            //pb.setVisibility(View.VISIBLE);

            task.execute("", "act"); //para saber que estoy actualizando
        }else
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
            builder.setCancelable(true);
            builder.setIcon(android.R.drawable.stat_sys_download);
            builder.setTitle("Descargando");
            builder.setMessage("No hay conexion a internet");
            builder.setInverseBackgroundForced(true);

            // builder=builder.create();
            builder.show();
        }

    }
}