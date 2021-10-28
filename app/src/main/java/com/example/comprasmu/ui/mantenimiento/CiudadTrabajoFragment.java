package com.example.comprasmu.ui.mantenimiento;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.ui.SelPlantaFragment;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;

import java.util.ArrayList;
import java.util.List;

public class CiudadTrabajoFragment extends Fragment {

    private CiudadTrabajoViewModel mViewModel;
    private static ArrayList<DescripcionGenerica> listaCiudadesEnv;

    Spinner spciudades;
  boolean isEdit=false;


    public static CiudadTrabajoFragment newInstance() {
        return new CiudadTrabajoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ciudad_trabajo_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(CiudadTrabajoViewModel.class);

        spciudades=view.findViewById(R.id.spciudadtrabajo);
        Button guardar=view.findViewById(R.id.btnctguardar);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarCiudad();
            }
        });

        final Observer<List<ListaCompra>> nameObserver = new Observer< List<ListaCompra>>() {
            @Override
            public void onChanged(@Nullable List<ListaCompra> lista) {
                Log.d(Constantes.TAG, "YA cargo la lista " + lista.size());
                if(lista.size()>1) {

                    convertirLista(lista);
                    CreadorFormulario.cargarSpinnerDescr(getContext(),spciudades,listaCiudadesEnv);
                    String ciudadsel = cargarCiudad();
                    if(isEdit) //preselecciono
                    {


                        spciudades.setSelection(getIndex(spciudades,ciudadsel ));
                    }
                }
                else{
                    Toast.makeText(getContext(),"No hay información para trabajar, verfique que descargó las listas de compra del indice "+Constantes.INDICEACTUAL,Toast.LENGTH_LONG);
                }
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        mViewModel.getCiudades(Constantes.INDICEACTUAL).observe(getViewLifecycleOwner(), nameObserver);

    }
    private int getIndex(Spinner spinner, String myString){
        int pos=0;
        for (int i=0;i<spinner.getCount();i++){
            DescripcionGenerica cliente=(DescripcionGenerica)spinner.getItemAtPosition(i);
            if (cliente.getNombre().equalsIgnoreCase(myString)){
                pos=i;
                break;

            }
        }

        return pos;
    }
    private static void convertirLista(List<ListaCompra>lista){
        listaCiudadesEnv=new ArrayList<DescripcionGenerica>();
        for (ListaCompra listaCompra: lista ) {
          /*String tupla=Integer.toString(listaCompra.getCiudadesId())+";"+
          listaCompra.getPlantaNombre();*/

            listaCiudadesEnv.add(new DescripcionGenerica(listaCompra.getCiudadesId(), listaCompra.getCiudadNombre()));

        }

    }
    public void guardarCiudad(){
        SharedPreferences prefe=getActivity().getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=prefe.edit();
        DescripcionGenerica ciudadSel=(DescripcionGenerica)spciudades.getSelectedItem();
        editor.putString("ciudadtrabajo",ciudadSel.getNombre() );
        editor.commit();
        Constantes.CIUDADTRABAJO=ciudadSel.getNombre();
       // Constantes.PAISTRABAJO=     prefe.getString("paistrabajo","");
        Constantes.IDCIUDADTRABAJO=ciudadSel.getId();
     //  Constantes.IDPAISTRABAJO=     prefe.getInt("idpaistrabajo",0);
       //voy al home
           NavHostFragment.findNavController(this).navigate(R.id.action_ciudadtohome);

    }
    public void recuperar(View v) {

        SharedPreferences prefe=getActivity().getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        String d=prefe.getString("ciudadtrabajo", "");
        if (d.length()==0) {
            Toast.makeText(getActivity(),"No existe dicho nombre en la agenda", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getActivity(),d, Toast.LENGTH_LONG).show();

        }

    }

    public String cargarCiudad(){
        SharedPreferences prefe=getActivity().getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        String ciudadSel= prefe.getString("ciudadtrabajo" ,"");
       if(!ciudadSel.equals("")) {
           isEdit=true; //ya tengo ciudad
           Constantes.CIUDADTRABAJO = ciudadSel;
           // Constantes.PAISTRABAJO=     prefe.getString("paistrabajo","");
           Constantes.IDCIUDADTRABAJO = prefe.getInt("idciudadtrabajo", 0);
       }
       return ciudadSel;

    }

}