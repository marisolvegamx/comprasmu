package com.example.comprasmu.utils.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comprasmu.R;

import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.databinding.ListaSelecFragmentBinding;
import com.example.comprasmu.utils.Constantes;

import java.util.ArrayList;


public class ListaSelecFragment extends Fragment {

    private ListaSelecViewModel mViewModel;
    private ListaSelecFragmentBinding mBinding;
    private AdaptadorListas adaptadorLista;
    private ListView objetosLV;
    protected ArrayList<DescripcionGenerica> listaSeleccionable;

    public static ListaSelecFragment newInstance() {
        return new ListaSelecFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ListaSelecViewModel.class);


    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
         mBinding=DataBindingUtil.inflate(inflater,
               R.layout.lista_selec_fragment, container, false);

              listaSeleccionable=new ArrayList<DescripcionGenerica>();
       // mViewModel.setLista( this.listaSeleccionable);
      //  mBinding.setViewModel(mViewModel);
        mBinding.setLifecycleOwner(this);
        objetosLV=mBinding.getRoot().findViewById(R.id.listaobjetos);


        return mBinding.getRoot();

    }

    public void setLista(ArrayList<DescripcionGenerica> lista){
        this.listaSeleccionable=lista;
        mViewModel.setLista( this.listaSeleccionable);

    }




    public void setupListAdapter() {
        adaptadorLista = new AdaptadorListas((AppCompatActivity) getActivity(),mViewModel);

        objetosLV.setAdapter(adaptadorLista);

    }

    class AdaptadorListas extends ArrayAdapter<DescripcionGenerica> {

        AppCompatActivity appCompatActivity;

        AdaptadorListas(AppCompatActivity context, ListaSelecViewModel lsvm) {
            super(context,0, lsvm.getLista());
            appCompatActivity = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = appCompatActivity.getLayoutInflater();
            View item = inflater.inflate(R.layout.list_selec_item, null);
           DescripcionGenerica obj=getItem(position);
            TextView textView1 = item.findViewById(R.id.key);
            TextView textView2 = item.findViewById(R.id.nombre);
            textView1.setText(obj.getId()+"");
            textView2.setText(obj.getNombre());

            return(item);
        }
    }

    public ListView getObjetosLV() {
        return objetosLV;
    }

}