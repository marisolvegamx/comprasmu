package com.example.comprasmu.ui.visita;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprasmu.R;

import com.example.comprasmu.data.modelos.Visita;

import com.example.comprasmu.databinding.ListaVisitasItemBinding;
import com.example.comprasmu.utils.Constantes;

import java.util.List;

public class VisitaAdapter extends RecyclerView.Adapter<VisitaAdapter.VisitaViewHolder> {

    private List<Visita> mVisitaList;

    //private final ListaRecyclerViewModel mViewModel;
    private AdapterCallback callback;

    public VisitaAdapter(AdapterCallback callback) {

     //   mViewModel = viewModel;
        this.callback=callback;
    }

    public void setVisitaList(List<Visita> informesList) {
        mVisitaList = informesList;
       // notifyDataSetChanged();
    }
    @NonNull
    @Override
    public VisitaAdapter.VisitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListaVisitasItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.lista_visitas_item, parent, false);
        return new VisitaViewHolder(binding,callback);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitaAdapter.VisitaViewHolder holder, int position) {
     //   holder.binding.setViewModel(mViewModel);
        holder.binding.setDetalle(mVisitaList.get(position));
        holder.binding.setSdf(Constantes.vistasdf);
      //  holder.binding.executePendingBindings();

    }


    @Override
    public int getItemCount() {
        return mVisitaList == null ? 0 : mVisitaList.size();
    }

    static class VisitaViewHolder extends RecyclerView.ViewHolder {
        final ListaVisitasItemBinding binding;

        public VisitaViewHolder(ListaVisitasItemBinding binding,AdapterCallback callback) {
            super(binding.getRoot());
            this.binding = binding;
            binding.liBtnedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(view.getId()==R.id.li_btnedit)
                        callback.onClickEditar(Integer.parseInt(binding.liTxtid.getText().toString()));

                }
            });
            binding.liBtnborrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(view.getId()==R.id.li_btnborrar)
                        callback.onClickEliminar(Integer.parseInt(binding.liTxtid.getText().toString()));
                }
            });
            binding.liBtnagregar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //muestra el formulario de nuevo informe
                    if(view.getId()==R.id.li_btnagregar)
                        callback.onClickAgregar(Integer.parseInt(binding.liTxtid.getText().toString()));

                }
            });

        }



    }
    public interface AdapterCallback {
        void onClickAgregar(int visitaid);
        void onClickEliminar(int id);
        void onClickEditar(int id);

    }

}
