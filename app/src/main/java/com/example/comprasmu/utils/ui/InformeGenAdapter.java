package com.example.comprasmu.utils.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.databinding.ListaInformegenItemBinding;
import com.example.comprasmu.utils.Constantes;
import java.util.List;

//para todos los informes usando elinformeCompravisita
public class InformeGenAdapter extends RecyclerView.Adapter<InformeGenAdapter.InformeGenViewHolder> {

    private List<InformeEtapa> mInformesList;

    private final AdapterCallback callback;
    public String tipo;

    public InformeGenAdapter( AdapterCallback callback, String tipo) {

        this.callback=callback;
        this.tipo=tipo;
    }

    public void setInformeCompraList(List<InformeEtapa> informesList) {
        mInformesList = informesList;

    }
    @NonNull
    @Override
    public InformeGenAdapter.InformeGenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListaInformegenItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.lista_informegen_item, parent, false);
        return new InformeGenViewHolder(binding,callback,this.tipo);
    }

    @Override
    public void onBindViewHolder(@NonNull InformeGenAdapter.InformeGenViewHolder holder, int position) {
       // holder.binding.setViewModel(mViewModel);

        holder.binding.setDetalle(mInformesList.get(position));
        holder.binding.setSdf(Constantes.vistasdf);
      //  holder.binding.setVisita();
      //  holder.binding.executePendingBindings();

    }


    @Override
    public int getItemCount() {
        return mInformesList == null ? 0 : mInformesList.size();
    }

    static class InformeGenViewHolder extends RecyclerView.ViewHolder {
        final ListaInformegenItemBinding binding;
        public String tipo;




        public InformeGenViewHolder(ListaInformegenItemBinding binding,AdapterCallback callback,String tipo) {
            super(binding.getRoot());
            this.binding = binding;
            this.tipo=tipo;

            binding.liBtnedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // view.setEnabled(false);
                    if(binding.liTxtconsecutivo.getText().toString().equals("-1")){
                        callback.onClickVerCC(Integer.parseInt(binding.liTxtid.getText().toString()));

                    }
                        callback.onClickVer(Integer.parseInt(binding.liTxtid.getText().toString()),Integer.parseInt(binding.liTxtetapainf.getText().toString()));
                }
            });
            binding.liBtnsubir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setEnabled(false);
                    if(binding.liTxtconsecutivo.getText().toString().equals("-1")){
                        callback.onClickSubirCC(Integer.parseInt(binding.liTxtid.getText().toString()));

                    }
                    callback.onClickSubir(Integer.parseInt(binding.liTxtid.getText().toString()),tipo);
                }
            });

        }



    }
    public interface AdapterCallback {
        void onClickVer(int idinforme,int etapainf);
        void onClickVerCC(int idinforme);
        void onClickSubir(int id, String tipo);
        void onClickSubirCC(int id);
    }

}
