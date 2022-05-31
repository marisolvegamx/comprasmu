package com.example.comprasmu.ui.mantenimiento;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comprasmu.R;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.EliminadorIndice;

import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BorrarDatosFragment extends Fragment {

    private BorrarDatosViewModel mViewModel;

    public static BorrarDatosFragment newInstance() {
        return new BorrarDatosFragment();
    }
    Spinner spindice;
    EditText txtipo;
    TextView aviso;
    public static final String TIPODATA="comprasmu.tipodata";
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.borrar_datos_fragment, container, false);
         spindice=root.findViewById(R.id.eispindice);
         txtipo=root.findViewById(R.id.eitxttipo);
        //llenar los indices

        String []indices= Constantes.listaindices;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, indices);
        spindice.setAdapter(adapter);
        Button btnborrar=root.findViewById(R.id.btnrfrotar);
        aviso=root.findViewById(R.id.txtbdlisto);
        btnborrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //borrarxindice();
                borrarautomatico();
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BorrarDatosViewModel.class);
     //  mViewModel.setCarpeta( getActivity().getExternalFilesDir(null));
        Bundle datosRecuperados = getArguments();

        if (datosRecuperados != null) {

            txtipo.setText(datosRecuperados.getString(TIPODATA));
        }



    }

    public void borrarxindice(){
      String indice=  spindice.getSelectedItem().toString();
      String tipo=txtipo.getText().toString();
      if(!indice.equals("")){
          //muestro confirmacion

          AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
          dialogo1.setTitle(R.string.importante);
          dialogo1.setMessage(R.string.pregunta_eliminar_informes);
          dialogo1.setCancelable(false);
          dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialogo1, int id) {
                 //depende del tipo
                  if(tipo.equals("informe")) {

                    //  mViewModel.borrarInformes(indice);
                      Toast.makeText(getActivity(), "Se eliminaron los informes",Toast.LENGTH_SHORT).show();

                  }
                  if(tipo.equals("compra")) {

                  //    mViewModel.borrarListasCompra(indice);
                      Toast.makeText(getActivity(), "Se eliminaron las listas",Toast.LENGTH_SHORT).show();

                  }


              }
          });
          dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialogo1, int id) {
                  dialogo1.cancel();
              }
          });
          dialogo1.show();
      }

    }


    public void borrarautomatico(){
        //buscar indice anterior
        //TODO como obtener el indice
        SimpleDateFormat sdfparaindice=new SimpleDateFormat("MM");
        SimpleDateFormat sdfanio=new SimpleDateFormat("yyyy");
        String mes=sdfparaindice.format(new Date());
        String anio=sdfanio.format(new Date());
        int mes_anterior=Integer.parseInt(mes)-1;
        int anio_anterior=Integer.parseInt(anio);
        if(mes_anterior<1){
            mes_anterior=1;
            anio_anterior=Integer.parseInt(anio)-1;
        }
        String indice_anterior= ComprasUtils.indiceLetra(mes_anterior+"-"+anio_anterior);
        indice_anterior="3.2022";
        EliminadorIndice ei=new EliminadorIndice(getActivity(),indice_anterior);
        ei.eliminarVisitas();
        aviso.setVisibility(View.VISIBLE);
       //  mViewModel.borrarInformes(indice_anterior);
         mViewModel.borrarListasCompra(indice_anterior);
       // Log.d("Comprasmu.BorrarDatosFragment","Se eliminaron las listas");




    }


}