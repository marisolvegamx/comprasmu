package com.example.comprasmu.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;


import com.example.comprasmu.DescargaRespAsyncTask;
import com.example.comprasmu.DescargasIniAsyncTask;
import com.example.comprasmu.R;
import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.ListaCompraDao;

import com.example.comprasmu.data.repositories.AtributoRepositoryImpl;
import com.example.comprasmu.data.repositories.CatalogoDetalleRepositoryImpl;
import com.example.comprasmu.data.repositories.GeocercaRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.SustitucionRepositoryImpl;
import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;

public class DescargarLisFragment extends Fragment implements DescargaRespAsyncTask.ProgresoRespListener {
     TextView textView;

    AlertDialog alert;
    ProgressBar pb;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_descargarlc, container, false);
        textView = root.findViewById(R.id.text_gallery);
         pb=root.findViewById(R.id.progressBar2);
        descargar();
        return root;
    }

    public void descargar(){
        //pueda descargar
        //Inicio un servicio que se encargue de descargar

        TablaVersionesRepImpl tvRepo=new TablaVersionesRepImpl(getContext());
        CatalogoDetalleRepositoryImpl cdrepo=new CatalogoDetalleRepositoryImpl(getContext());

        AtributoRepositoryImpl atRepo=new AtributoRepositoryImpl(getContext());


        ListaCompraDao dao= ComprasDataBase.getInstance(getContext()).getListaCompraDao();
        ListaCompraDetRepositoryImpl lcdrepo=new ListaCompraDetRepositoryImpl(getContext());
        ListaCompraRepositoryImpl lcrepo=ListaCompraRepositoryImpl.getInstance(dao);
        SustitucionRepositoryImpl sustRepo=new SustitucionRepositoryImpl(getContext());
        GeocercaRepositoryImpl georep=new GeocercaRepositoryImpl(getContext());

        DescargasIniAsyncTask task = new DescargasIniAsyncTask(getActivity(),cdrepo,tvRepo,atRepo,lcdrepo,lcrepo,this,  sustRepo,georep);
        textView.setText("Por favor mantengase en la aplicación hasta que termine la descarga");
       pb.setVisibility(View.VISIBLE);

        task.execute("","act"); //para saber que estoy actualizando

        /*AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setIcon(android.R.drawable.stat_sys_download);
        builder.setTitle("Descargando");
        builder.setMessage("Por favor mantengase en la aplicación hasta que termine la descarga");
        builder.setInverseBackgroundForced(true);

        alert=builder.create();
        alert.show();*/

      /*  Dialog builder = new Dialog(act);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.setCancelable(false);
*/

    }


    @Override
    public void cerrarAlerta(boolean res) {

       /* if(alert!=null)
        alert.cancel();*/

        if(!res) {
            textView.setText(getString(R.string.listas_actualiz));
            textView.setVisibility(View.VISIBLE);
        }
        pb.setVisibility(View.GONE);

    }

    @Override
    public void todoBien() {
        if(textView!=null) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(getString(R.string.listas_actualiz));
        }
    }
}