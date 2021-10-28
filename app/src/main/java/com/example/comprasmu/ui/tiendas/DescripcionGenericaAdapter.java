package com.example.comprasmu.ui.tiendas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.databinding.GenericItemBinding;

import java.util.List;

public class DescripcionGenericaAdapter extends RecyclerView.Adapter<DescripcionGenericaAdapter.DescripcionGenericaViewHolder> {

    private List<DescripcionGenerica> mDescripcionGenericaList;


    private DescripcionGenericaAdapter.AdapterCallback callback;

    public DescripcionGenericaAdapter( DescripcionGenericaAdapter.AdapterCallback callback) {


        this.callback = callback;
    }

    public List<DescripcionGenerica> getmDescripcionGenericaList() {
        return mDescripcionGenericaList;
    }

    public void setmDescripcionGenericaList(List<DescripcionGenerica> mDescripcionGenericaList) {
        this.mDescripcionGenericaList = mDescripcionGenericaList;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public DescripcionGenericaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       GenericItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.generic_item, parent, false);
        return new DescripcionGenericaViewHolder(binding, callback);
    }

    @Override
    public void onBindViewHolder(@NonNull DescripcionGenericaAdapter.DescripcionGenericaViewHolder holder, int position) {
       // holder.binding.setViewModel(mViewModel);
        holder.binding.setDetalle(mDescripcionGenericaList.get(position));

      //  holder.binding.executePendingBindings();

    }



    @Override
    public int getItemCount() {
        return mDescripcionGenericaList == null ? 0 : mDescripcionGenericaList.size();
    }

    static class DescripcionGenericaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final GenericItemBinding binding;
        Button btnsel;

        private DescripcionGenericaAdapter.AdapterCallback callback;


        public DescripcionGenericaViewHolder(GenericItemBinding binding, DescripcionGenericaAdapter.AdapterCallback callback) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setOnClickListeners() {
            btnsel.setOnClickListener(this);

        }

       @Override
        public void onClick(View view) {
            callback.onClickCallback("algo");
        }
    }

    public interface AdapterCallback {
        void onClickCallback(String codigos);
    }
}