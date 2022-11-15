package com.example.comprasmu.ui.infetapa;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.databinding.ContinfgenItemBinding;
import com.example.comprasmu.databinding.SolCorreccionItemBinding;
import com.example.comprasmu.ui.informe.ListaInformesViewModel;
import com.example.comprasmu.utils.Constantes;

import java.util.List;

public class ContInfEtaAdapter extends RecyclerView.Adapter<ContInfEtaAdapter.ContInfEtaViewHolder> {

    private List<InformeEtapa> mInformeEtaList;
    //private List<String> mInformeCompraList;
    private final AdapterCallback callback;

    public ContInfEtaAdapter( AdapterCallback callback) {

        this.callback=callback;
    }

    public void setInformeCompraList(List<InformeEtapa> informesList) {
        mInformeEtaList = informesList;
       // notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContInfEtaAdapter.ContInfEtaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ContinfgenItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.continfgen_item, parent, false);
        return new ContInfEtaViewHolder(binding,callback);
    }

    @Override
    public void onBindViewHolder(@NonNull ContInfEtaAdapter.ContInfEtaViewHolder holder, int position) {
        holder.binding.setDetalle(mInformeEtaList.get(position));
        holder.binding.setSdf(Constantes.vistasdf);
      //  holder.binding.setVisita();
      //  holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mInformeEtaList == null ? 0 : mInformeEtaList.size();
    }

    static class ContInfEtaViewHolder extends RecyclerView.ViewHolder {
        final ContinfgenItemBinding binding;
        public ContInfEtaViewHolder(ContinfgenItemBinding binding,AdapterCallback callback) {
            super(binding.getRoot());
            this.binding = binding;
           /* binding.liBtnedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(view.getId()==R.id.li_btnedit)
                    //    callback.onClickEditar(Integer.parseInt(binding.liTxtid.getText().toString()));
                        callback.onClickVer(Integer.parseInt(binding.liTxtid.getText().toString()));

                }
            });*/
            binding.btncigborrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(view.getId()==R.id.btncigborrar)
                        callback.onClickEliminar(Integer.parseInt(binding.txtcigid.getText().toString()));
                }
            });
            binding.btncigagregar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // view.setEnabled(false);
                        callback.onClickContinuar(Integer.parseInt(binding.txtcigid.getText().toString()));
                }
            });
        }

    }
    public interface AdapterCallback {
        void onClickContinuar(int idinforme);
        void onClickEliminar(int id);

    }

}
