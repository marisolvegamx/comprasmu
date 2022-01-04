package com.example.comprasmu.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.ui.mantenimiento.CiudadTrabajoViewModel;
import com.example.comprasmu.utils.Constantes;
/****guardar clave usuario******/
public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    TextView textusuario;
    Button guardar;
    private boolean isEdit;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        textusuario= root.findViewById(R.id.txthmusuario);
        guardar= root.findViewById(R.id.btnhmguardar);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardar();
            }
        });
        Button boton = (Button) root.findViewById(R.id.button);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirPrueba();
            }
        });
        //textusuario.on
        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
       String clave=cargarUsuario();
       if(!clave.equals("")){
           textusuario.setText(clave);
       }


    }
    public void abrirPrueba(){
        Intent intento=new Intent(getContext(), PruebasActivity.class);
        startActivity(intento);
    }

    public void guardar(){
       if(textusuario.getText().toString().length()>0){
           guardarUsuario(textusuario.getText().toString());
       }
//       getActivity().finish();
       //voy a descargar listas
        NavHostFragment.findNavController(this).navigate(R.id.action_conftohome);

    }
    public void guardarUsuario(String clave){
        SharedPreferences prefe=getActivity().getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=prefe.edit();

        editor.putString("claveusuario",clave);
        editor.commit();
        Constantes.CLAVEUSUARIO=clave;


    }


    public String cargarUsuario(){
        SharedPreferences prefe=getActivity().getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        String clave= prefe.getString("claveusuario" ,"");
        if(!clave.equals("")) {
            isEdit=true; //ya tengo ciudad
            Constantes.CLAVEUSUARIO = clave;

        }
        return clave;

    }
}