package com.example.comprasmu.ui.informe;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.comprasmu.R;
import com.example.comprasmu.ui.informedetalle.NuevoDetalleViewModel;
import com.example.comprasmu.utils.CampoForm;
import com.example.comprasmu.utils.CreadorFormulario;

import java.util.List;

/**********
 * prueba usando tabla reactivos
 */
public class NuevoDetalleFragment extends Fragment {

    private NuevoDetalleViewModel mViewModel;
    CreadorFormulario cf;
    List<CampoForm> camposForm;
    View root;
    public static NuevoDetalleFragment newInstance() {
        return new NuevoDetalleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       root=inflater.inflate(R.layout.nuevo_detalle_fragment, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NuevoDetalleViewModel.class);
     //   LinearLayout sv = root.findViewById(R.id.llnepreg2);

        crearFormulario();
    }

    public void crearFormulario(){
        /*el formulario se crea de acuerdo a los reactivos*/

      /*  mViewModel.getReactivos().observe(getViewLifecycleOwner(), new Observer<List<Reactivo>>() {
            @Override
            public void onChanged(@Nullable List<Reactivo> s) {
                for(Reactivo reactivo:s){
                    camposForm= new ArrayList<CampoForm>();
                    CampoForm campo=new CampoForm();
                    campo.label=reactivo.getDescripcion();
                    campo.nombre_campo="reactivo_"+reactivo.getId();

                    campo.value=null;
                    campo.required="required";
                    campo.id=1000+reactivo.getId();
                    switch(reactivo.getTipoReactivo()){
                        case "L":campo.select=reactivo.getLista();
                            campo.type="select";
                            break;
                        case "C":campo.select=reactivo.getLista();
                            campo.type="select";
                            break;
                        case "B":campo.select=reactivo.getLista();
                            campo.type="radiobutton";

                            break;
                        case "I":campo.type="agregarImagen";
                        case "A":
                        default:campo.type="inputtext";

                            break;

                    }

                    camposForm.add(campo);
                }


            }
        });
        cf=new CreadorFormulario(camposForm,getContext());
        cf.crearFormulario();*/
    }

}