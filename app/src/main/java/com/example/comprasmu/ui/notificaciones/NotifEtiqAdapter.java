package com.example.comprasmu.ui.notificaciones;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.databinding.InformeetaCancelItemBinding;
import com.example.comprasmu.databinding.NotificacionEtiqItemBinding;
import com.example.comprasmu.utils.Constantes;

import java.util.List;

/***solo será informativo****/
public class NotifEtiqAdapter extends RecyclerView.Adapter<NotifEtiqAdapter.InformeEtapaViewHolder> {

    private List<InformeEtapa> mInformes;
    private final AdapterCallback callback;


    public NotifEtiqAdapter(AdapterCallback callback) {


        this.callback = callback;
    }

    public void setInformeCompraList(List<InformeEtapa> informesList) {
        mInformes = informesList;
       // notifyDataSetChanged();
    }


    @NonNull
    @Override
    public NotifEtiqAdapter.InformeEtapaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NotificacionEtiqItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.notificacion_etiq_item, parent, false);
        return new InformeEtapaViewHolder(binding,callback);
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
        final NotificacionEtiqItemBinding binding;

        public InformeEtapaViewHolder(NotificacionEtiqItemBinding binding, AdapterCallback callback) {
            super(binding.getRoot());
            this.binding = binding;
          /*  binding.btnecagregar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {  //para cuando se cancela
                        callback.onClickAgregar(Integer.parseInt(binding.icTxtiecid.getText().toString()));
                }
            });
            binding.btneccontinuar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { //para cuando se agrega
                    callback.onClickContinuar(Integer.parseInt(binding.icTxtiecid.getText().toString()));
                }
            });
            binding.btnecnvoemp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onClickNvoEmp();
                }
            });*/
        }


    }
    public interface AdapterCallback {
       // void onClickAgregar(int idinforme);


        void onClickContinuar(int idinforme);

        void onClickNvoEmp();
    }


}
