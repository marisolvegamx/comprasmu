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
import com.example.comprasmu.utils.EliminadorIndice;

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

        String []indices= {"AGOSTO 2023,SEPTIEMBRE 2023,OCTUBRE 2023,NOVIEMBRE 2023,DICIEMBRE 2023"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, indices);
        spindice.setAdapter(adapter);
        Button btnborrar=root.findViewById(R.id.btndbborrar);
        aviso=root.findViewById(R.id.txtbdlisto);
        btnborrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //borrarxindice();
               preguntar();
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

    public void preguntar(){
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
        dialogo1.setTitle(R.string.importante);
        dialogo1.setMessage(R.string.pregunta_eliminar_informes);
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
              //  borrarautomatico("8.2023");
            borrarxindice();

            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dialogo1.cancel();
            }
        });
        dialogo1.show();

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
                    borrarxindice(indice);
                    //  mViewModel.borrarInformes(indice);

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


    public void borrarautomatico(String indice_anterior){


        EliminadorIndice ei=new EliminadorIndice(getActivity(),indice_anterior);
        ei.eliminarVisitas();
        aviso.setVisibility(View.VISIBLE);
        //todo borrar informes etapa
        // mViewModel.borrarInformes(indice_anterior);
         mViewModel.borrarListasCompra(indice_anterior);
       // Log.d("Comprasmu.BorrarDatosFragment","Se eliminaron las listas");
        mViewModel.borrarInformesetapa(indice_anterior);
        ei.eliminarCorrecciones();
        ei.eliminarSolicitudes();
        ei.borrarImagenes();
        ei.eliminarTablaVers();


    }
    public void borrarxindice(String indice_anterior){


        EliminadorIndice ei=new EliminadorIndice(getActivity(),indice_anterior);
        ei.eliminarVisitas();
        aviso.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(), "Se eliminaron los informes",Toast.LENGTH_SHORT).show();

    }

}