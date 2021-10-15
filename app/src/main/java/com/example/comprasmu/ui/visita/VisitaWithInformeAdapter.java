package com.example.comprasmu.ui.visita;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprasmu.R;

import com.example.comprasmu.data.modelos.VisitaWithInformes;
import com.example.comprasmu.utils.Constantes;

import java.util.List;

public class VisitaWithInformeAdapter
        //extends RecyclerView.Adapter<VisitaWithInformeAdapter.VisitaViewHolder>
 {

 /*   private List<VisitaWithInformes> mVisitaWithInformesList;

    //private final ListaRecyclerViewModel mViewModel;
    private AdapterCallback callback;

    public VisitaWithInformesWithInformeAdapter(AdapterCallback callback) {

     //   mViewModel = viewModel;
        this.callback=callback;
    }

    public void setVisitaWithInformesList(List<VisitaWithInformes> informesList) {
        mVisitaWithInformesList = informesList;
       // notifyDataSetChanged();
    }
    @NonNull
    @Override
    public VisitaWithInformesWithInformeAdapter.VisitaWithInformesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListaVisitaWithInformessItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.lista_VisitaWithInformess_item, parent, false);
        return new VisitaWithInformesViewHolder(binding,callback);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitaWithInformesWithInformeAdapter.VisitaWithInformesViewHolder holder, int position) {
     //   holder.binding.setViewModel(mViewModel);
        holder.binding.setDetalle(mVisitaWithInformesList.get(position));
        holder.binding.setSdf(Constantes.vistasdf);
      //  holder.binding.executePendingBindings();

    }


    @Override
    public int getItemCount() {
        return mVisitaWithInformesList == null ? 0 : mVisitaWithInformesList.size();
    }

    static class VisitaWithInformesViewHolder extends RecyclerView.ViewHolder {
        final ListaVisitaWithInformessItemBinding binding;

        public VisitaWithInformesViewHolder(ListaVisitaWithInformessItemBinding binding,AdapterCallback callback) {
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
                    Log.d("hola hola","hice clic"+binding.liTxtid.getText().toString());
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
        void onClickAgregar(int VisitaWithInformesid);
        void onClickEliminar(int id);
        void onClickEditar(int id);

    }*/

}
