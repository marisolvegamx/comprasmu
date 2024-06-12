package com.example.comprasmu.ui.infetapa;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.InformeEtapa;

import com.example.comprasmu.databinding.InformeetaCancelItemBinding;
import com.example.comprasmu.utils.Constantes;

import java.util.List;
/***tiende a desaparecer se sustituyo por notifetaadapter****/
public class CancelEtaAdapter extends RecyclerView.Adapter<CancelEtaAdapter.InformeEtapaViewHolder> {

    private List<InformeEtapa> mInformes;

    public CancelEtaAdapter() {



    }

    public void setInformeCompraList(List<InformeEtapa> informesList) {
        mInformes = informesList;
       // notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CancelEtaAdapter.InformeEtapaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        InformeetaCancelItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.informeeta_cancel_item, parent, false);
        return new InformeEtapaViewHolder(binding);
    }



    @Override
    public void onBindViewHolder(@NonNull InformeEtapaViewHolder holder, int position) {
       // holder.binding.setViewModel(mViewModel);
        if(mInformes !=null)
            holder.binding.setDetalle(mInformes.get(position));

        holder.binding.setSdf(Constantes.vistasdf);
      //  holder.binding.setVisita();
      //  holder.binding.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return mInformes == null ? 0 : mInformes.size();
    }

    static class InformeEtapaViewHolder extends RecyclerView.ViewHolder {
        final InformeetaCancelItemBinding binding;

        public InformeEtapaViewHolder(InformeetaCancelItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }


    }



}
