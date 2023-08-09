package com.example.comprasmu.ui.mantenimiento;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.Configuracion;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;

import java.util.ArrayList;
import java.util.List;

public class ConfiguracionCamFragment extends Fragment {
    private static final String TAG = "ConfiguracionCamFragment";
    Configuracion configcam;
    private CiudadTrabajoViewModel mViewModel;

    CheckBox checkrotar;
    boolean isEdit=false;


    public static ConfiguracionCamFragment newInstance() {
        return new ConfiguracionCamFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.configuracion_cam_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(CiudadTrabajoViewModel.class);

        checkrotar=view.findViewById(R.id.ckcnfrotar);
        Button guardar=view.findViewById(R.id.btncnfguardar);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarConfigCam();
            }
        });
         configcam=mViewModel.getConfig();
        checkrotar.setChecked(false);
        if(configcam!=null) //preselecciono
            {
                if(configcam.getValor().equals("1"))
                    checkrotar.setChecked(true);

        }


    }

    public void guardarConfigCam() {
        configcam=mViewModel.getConfig();

        // Veo si ya existe sino la reemplazo
        if (configcam != null && configcam.getClave().equals(Constantes.CONFROTAR)) {
            Log.d(TAG,"id conf"+configcam.getId());
        }
        else {

            configcam = new Configuracion();
            configcam.setClave(Constantes.CONFROTAR);
        }
        String rotar="0";
        if(checkrotar.isChecked()){
            rotar="1";

        }
        configcam.setValor(rotar);
        mViewModel.guardarConf(configcam);
            Toast.makeText(getActivity(),"Se guardó la configuración correctamente", Toast.LENGTH_LONG).show();

        //  Constantes.IDPAISTRABAJO=     prefe.getInt("idpaistrabajo",0);
       //voy al home
       //    NavHostFragment.findNavController(this).navigate(R.id.action_ciudadtohome);

    }

}