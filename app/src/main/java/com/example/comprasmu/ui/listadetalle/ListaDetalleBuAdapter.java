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
    int cliente;
    ListaDetalleBuAdapter(AppCompatActivity context, List<InformeCompraDetalle> lsvm, int clientesel) {
        super(context,0,lsvm);
        // super(context,0, lsvm);
        appCompatActivity = context;
        items=lsvm;
        this.cliente=clientesel;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = appCompatActivity.getLayoutInflater();

        if (inflater == null) {
            inflater = ((Activity) parent.getContext()).getLayoutInflater();
        }

        // Perform the binding
        ListaDetalleItem2Binding binding = DataBindingUtil.getBinding(convertView);

        if (binding == null) {
            binding = DataBindingUtil.inflate( inflater, R.layout.lista_detalle_item2, parent, false);
        }

        binding.setDetallebu( items.get(position));
        binding.setClientesel(cliente);
        if(binding.getDetallebu()!=null)
            binding.cajatexto.setVisibility(View.VISIBLE);
        binding.executePendingBindings();

        // Return the bound view
        return binding.getRoot();


    }
}