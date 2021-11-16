package com.example.comprasmu.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.comprasmu.DescargasIniAsyncTask;
import com.example.comprasmu.R;
import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.ListaCompraDao;
import com.example.comprasmu.data.repositories.AtributoRepositoryImpl;
import com.example.comprasmu.data.repositories.CatalogoDetalleRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;

public class DescargarLisFragment extends Fragment {
     TextView textView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_descargarlc, container, false);
        textView = root.findViewById(R.id.text_gallery);
        descargar();
        return root;
    }

    public void descargar(){
        //todo hacerlo en un servicio que esté constantemente verficando la conexion hasta que
        //pueda descargar
        //Inicio un servicio que se encargue de descargar

        TablaVersionesRepImpl tvRepo=new TablaVersionesRepImpl(getContext());


        ListaCompraDao dao= ComprasDataBase.getInstance(getContext()).getListaCompraDao();
        ListaCompraDetRepositoryImpl lcdrepo=new ListaCompraDetRepositoryImpl(getContext());
        ListaCompraRepositoryImpl lcrepo=ListaCompraRepositoryImpl.getInstance(dao);

        DescargasIniAsyncTask task = new DescargasIniAsyncTask(getActivity(),null,tvRepo,null,lcdrepo,lcrepo);
        textView.setText("descargando");
        task.execute("");
      /*  AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setIcon(android.R.drawable.stat_sys_download);
        builder.setTitle("Descargando");
        builder.setMessage("Por favor mantengase en la aplicación hasta que termine la descarga");
        builder.setInverseBackgroundForced(true);

        AlertDialog alert=builder.create();
        alert.show();*/

      /*  Dialog builder = new Dialog(act);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.setCancelable(false);
*/

    }
}