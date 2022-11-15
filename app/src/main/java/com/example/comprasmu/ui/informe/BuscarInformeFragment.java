package com.example.comprasmu.ui.informe;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.ui.FiltrarListaActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BuscarInformeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuscarInformeFragment extends Fragment {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String CIUDAD = "buscarinforme.ciudad";
    public static final String NOMBRETIENDA = "buscarinforme.nombre";
    public static final String INDICE = "buscainforme.indice";
    public static final String ARG_CLIENTE = "clienteSel";

    public static final String ARG_PLANTA= "plantaSel";

    public BuscarInformeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BuscarInformeFragment.
     */

    public static BuscarInformeFragment newInstance(String param1, String param2) {
        BuscarInformeFragment fragment = new BuscarInformeFragment();
       /* Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      /*  if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_buscar_informe, container, false);
        Button btnfiltrar=root.findViewById(R.id.btnbibuscar);
        Spinner spindices=root.findViewById(R.id.spbiindice);
        //String []indices=new String[Constantes.listaindices.length+1];
        List<String>indices= new ArrayList<>();
        indices.add("Seleccione una opción");
      //  String []indices= Constantes.listaindices;
        indices.addAll(Arrays.asList(Constantes.listaindices));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, indices){
            @Override
            public boolean isEnabled(int position){
                // Disable the first item from Spinner
                // First item will be use for hint
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spindices.setAdapter(adapter);
        spindices.setPrompt("Seleccione una opción");
        spindices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
                    Toast.makeText
                            (getContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnfiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtrar(container);
            }
        });
        // Inflate the layout for this fragment
        return root;
    }
    public void filtrar(View v){
        EditText ciudad=v.findViewById(R.id.txtbiciudad);
        EditText nombreTienda=v.findViewById(R.id.txtbinombre_tienda);
        Spinner indice=v.findViewById(R.id.spbiindice);

        Bundle bundle = new Bundle();
        bundle.putString(BuscarInformeFragment.CIUDAD,ciudad.getText().toString());
        bundle.putString(BuscarInformeFragment.NOMBRETIENDA,nombreTienda.getText().toString());
        if(indice.getSelectedItem()!=null)
        bundle.putString(BuscarInformeFragment.INDICE,indice.getSelectedItem().toString());
        bundle.putString("comprasmu.inicio","listainforme");
        /* bundle.putString("plantaNombre", listaSeleccionable.get(i).getNombre());*/
        /*   NavHostFragment.findNavController(this).navigate(R.id.action_selclientetolistacompras,bundle);
         */

        Intent resultIntent = new Intent();

        resultIntent.putExtras(bundle);
        getActivity().setResult(1, resultIntent);

        //regreso a la lista
        getActivity().finish();
      //  NavHostFragment.findNavController(this).navigate(R.id.nav_listar);

    }
}