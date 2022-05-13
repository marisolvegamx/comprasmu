package com.example.comprasmu.ui.sustitucion;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprasmu.R;

import com.example.comprasmu.data.modelos.Sustitucion;

import com.example.comprasmu.databinding.SustitucionItemBinding;

import java.util.List;



public class SustitucionAdapter extends RecyclerView.Adapter<SustitucionAdapter.SustitucionViewHolder> {

    private List<Sustitucion> mSustitucionList;
    private AdapterCallback callback;
    private final static String TAG=SustitucionAdapter.class.getName();
    int numtienda;
    boolean isbu, ismuestra;//para saber si ya estoy en lista de bu o agregando muestra
    String cliente;
    public SustitucionAdapter(String ismuestra, AdapterCallback callback) {
      if(ismuestra!=null&&ismuestra.equals("true"))
          this.ismuestra=true;
        this.callback=callback;

    }

    public void setSustitucionList(List<Sustitucion> categoriesList) {
        mSustitucionList = categoriesList;

    }
    @NonNull
    @Override
    public SustitucionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SustitucionItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.sustitucion_item, parent, false);

        return new SustitucionViewHolder(binding,callback);
    }

    @Override
    public void onBindViewHolder(@NonNull SustitucionViewHolder holder, int position) {
       holder.binding.setDetalle(mSustitucionList.get(position));
        holder.binding.setMostrarAgregar(ismuestra);


    }


    @Override
    public int getItemCount() {
        return mSustitucionList == null ? 0 : mSustitucionList.size();
    }

    static class SustitucionViewHolder extends RecyclerView.ViewHolder {
        final SustitucionItemBinding binding;
        boolean isNueva;
        // final PruebarecyclerBinding binding;

        static ViewGroup.LayoutParams altoini;

        public SustitucionViewHolder(SustitucionItemBinding binding, AdapterCallback callback) {

            super(binding.getRoot());
            this.binding = binding;


            binding.btnldagregar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.getId() == R.id.btnldagregar) {
                        callback.agregarMuestra(view, binding.getDetalle());
                    }
                }
            });

        }
    }
        public interface AdapterCallback {

            void agregarMuestra(View view, Sustitucion productoSel);

        }


}
