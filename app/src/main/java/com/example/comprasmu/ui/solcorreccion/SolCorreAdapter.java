package com.example.comprasmu.ui.solcorreccion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.SolicitudCor;
import com.example.comprasmu.databinding.SolCorreccionItemBinding;
import com.example.comprasmu.ui.informe.ListaInformesViewModel;
import com.example.comprasmu.utils.Constantes;

import java.util.List;

public class SolCorreAdapter extends RecyclerView.Adapter<SolCorreAdapter.SolicitudCorViewHolder> {


    private List<SolicitudCor> mSolicitudCorList;
    private final AdapterCallback callback;

    public SolCorreAdapter( AdapterCallback callback) {

        this.callback=callback;
    }

    public void setSolicitudCorList(List<SolicitudCor> informesList) {
        mSolicitudCorList = informesList;
       // notifyDataSetChanged();
    }
    @NonNull
    @Override
    public SolCorreAdapter.SolicitudCorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SolCorreccionItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.sol_correccion_item, parent, false);
        return new SolicitudCorViewHolder(binding,callback);
    }

    @Override
    public void onBindViewHolder(@NonNull SolCorreAdapter.SolicitudCorViewHolder holder, int position) {
       // holder.binding.setViewModel(mViewModel);
        holder.binding.setDetalle(mSolicitudCorList.get(position));
        holder.binding.setSdf(Constantes.vistasdf);
      //  holder.binding.set


    }


    @Override
    public int getItemCount() {
        return mSolicitudCorList == null ? 0 : mSolicitudCorList.size();
    }

    static class SolicitudCorViewHolder extends RecyclerView.ViewHolder {
        final SolCorreccionItemBinding binding;





        public SolicitudCorViewHolder(SolCorreccionItemBinding binding,AdapterCallback callback) {
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
         /*   binding.liBtnborrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("hola hola","hice clic");
                    if(view.getId()==R.id.li_btnborrar)
                        callback.onClickCancelar(Integer.parseInt(binding.liTxtid.getText().toString()));
                }
            });*/
            binding.button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // view.setEnabled(false);
                        callback.onClickVer(Integer.parseInt(binding.liTxtid.getText().toString()));
                }
            });

        }



    }
    public interface AdapterCallback {
        void onClickVer(int idinforme);

    }

}
