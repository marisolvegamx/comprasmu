package com.example.comprasmu.ui.listadetalle;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.databinding.ListaDetalleItem2Binding;
import java.util.List;



public class ListaDetalleBuAdapter extends ArrayAdapter<InformeCompraDetalle> {

    AppCompatActivity appCompatActivity;
    List<InformeCompraDetalle> items;
    ListaDetalleBuAdapter(AppCompatActivity context, List<InformeCompraDetalle> lsvm) {
        super(context,0,lsvm);
        // super(context,0, lsvm);
        appCompatActivity = context;
        items=lsvm;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        if (inflater == null) {
            inflater = ((Activity) parent.getContext()).getLayoutInflater();
        }

        // Perform the binding
        ListaDetalleItem2Binding binding = DataBindingUtil.getBinding(convertView);

        if (binding == null) {
            binding = DataBindingUtil.inflate( inflater, R.layout.lista_detalle_item2, parent, false);
        }

        binding.setDetallebu( items.get(position));
        if(binding.getDetallebu()!=null)
            binding.cajatexto.setVisibility(View.VISIBLE);
        binding.executePendingBindings();

        // Return the bound view
        return binding.getRoot();


    }
}