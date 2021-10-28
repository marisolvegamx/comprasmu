package com.example.comprasmu.ui.informedetalle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprasmu.R;

import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.databinding.MuestraItemBinding;
import com.example.comprasmu.utils.Constantes;
import com.google.android.material.tabs.TabLayout;


import java.util.List;

public class InformeDetalleAdapter extends  RecyclerView.Adapter<InformeDetalleAdapter.InformeCompraDetalleViewHolder>
{

    private List<InformeCompraDetalle> mInformeCompraDetalleList;

    private AdapterCallback callback;
    boolean consulta=false;

    public InformeDetalleAdapter( AdapterCallback callback) {

       // mViewModel = viewModel;
        this.callback=callback;
    }

    public void setInformeCompraDetalleList(List<InformeCompraDetalle> informesList,boolean consulta) {
        mInformeCompraDetalleList = informesList;
        this.consulta=consulta;
       // notifyDataSetChanged();
    }

    public List<InformeCompraDetalle> getmInformeCompraDetalleList() {
        return mInformeCompraDetalleList;
    }

    @NonNull
    @Override
    public InformeDetalleAdapter.InformeCompraDetalleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MuestraItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.muestra_item, parent, false);
        return new InformeCompraDetalleViewHolder(binding,callback);
    }

    @Override
    public void onBindViewHolder(@NonNull InformeDetalleAdapter.InformeCompraDetalleViewHolder holder, int position) {
      //  holder.binding.setViewModel(mViewModel);
        holder.binding.setDetalle(mInformeCompraDetalleList.get(position));
        holder.binding.setSdf(Constantes.vistasdf);
        holder.binding.setConsulta(consulta);
      //  holder.binding.executePendingBindings();

    }


    @Override
    public int getItemCount() {
        return mInformeCompraDetalleList == null ? 0 : mInformeCompraDetalleList.size();
    }

    static class InformeCompraDetalleViewHolder extends RecyclerView.ViewHolder {
        final MuestraItemBinding binding;

        public InformeCompraDetalleViewHolder(MuestraItemBinding binding,AdapterCallback callback) {
            super(binding.getRoot());
            this.binding = binding;
            Log.d("InformeDetalleAdapter","estatussync"+binding.txtmestatussync.getText().toString());
         //  if( binding.txtmestatussync.getText().toString().equals("0")) //no puedo eliminar
             //  binding.btnmeliminar.setVisibility(View.VISIBLE);
           //else
             //  binding.btnmeliminar.setVisibility(View.INVISIBLE);

          //      binding.btnmeditar.setVisibility(View.VISIBLE);

              /*  binding.btnmeditar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (view.getId() == R.id.btnmeditar)
                            callback.onClickEditar(Integer.parseInt(binding.txtmid.getText().toString()));

                    }
                });*/
            binding.btnmver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.getId() == R.id.btnmver)
                        callback.onClickVer(Integer.parseInt(binding.txtmid.getText().toString()));

                }
            });

          //  if( binding.txtmestatussync.getText().toString().equals("0"))
           // binding.btnmeliminar.setOnClickListener(new View.OnClickListener() {
            //    @Override
            //    public void onClick(View view) {
             //       Log.d("hola hola","hice clic");
//                    if(view.getId()==R.id.btnmeliminar)
//                        callback.onClickCancelar(Integer.parseInt(binding.txtmid.getText().toString()));
//                }
//            });


        }



    }
    public interface AdapterCallback {
        void onClickVer(int codigos);
        void onClickCancelar(InformeCompraDetalle detalle);
        void onClickEditar(int id);

    }

}
