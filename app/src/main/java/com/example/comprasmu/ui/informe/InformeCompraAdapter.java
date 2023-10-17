package com.example.comprasmu.ui.informe;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprasmu.R;

import com.example.comprasmu.data.dao.InformeCompraDao;
import com.example.comprasmu.data.modelos.InformeCompra;

import com.example.comprasmu.databinding.ListaInformeItemBinding;

import com.example.comprasmu.utils.Constantes;

import java.util.List;

public class InformeCompraAdapter extends RecyclerView.Adapter<InformeCompraAdapter.InformeCompraViewHolder> {

  //  private List<InformeCompra> mInformeCompraList;
    private List<InformeCompraDao.InformeCompravisita> mInformeCompraList;

    private final ListaInformesViewModel mViewModel;
    private final AdapterCallback callback;
    private boolean subiendoInf; //para saber si se está subiendo un informe y bloquear los demas

    public InformeCompraAdapter(ListaInformesViewModel viewModel, AdapterCallback callback) {

        mViewModel = viewModel;
        this.callback=callback;
    }

    public void setInformeCompraList(List<InformeCompraDao.InformeCompravisita> informesList) {
        mInformeCompraList = informesList;
        subiendoInf=false;
        //reviso si alguno se está subiendo
       /* for( int i=0;i<mInformeCompraList.size();i++){

            if(mInformeCompraList.get(i)!=null&&mInformeCompraList.get(i).estatusSync==1){
              Log.d("InformeCompraAdapter","estoy subiendo algo");
                subiendoInf=true;
                break;
            }
        }*/



    }
    @NonNull
    @Override
    public InformeCompraAdapter.InformeCompraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListaInformeItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.lista_informe_item, parent, false);
        return new InformeCompraViewHolder(binding,callback,subiendoInf);
    }

    @Override
    public void onBindViewHolder(@NonNull InformeCompraAdapter.InformeCompraViewHolder holder, int position) {
       // holder.binding.setViewModel(mViewModel);
        holder.binding.setDetalle(mInformeCompraList.get(position));
        holder.binding.setSdf(Constantes.vistasdf);
      //  holder.binding.setVisita();
      //  holder.binding.executePendingBindings();
        if(Constantes.SINCRONIZANDO==1){
            subiendoInf=true;
            holder.binding.liBtnsubir.setEnabled(false);
        }

    }


    @Override
    public int getItemCount() {
        return mInformeCompraList == null ? 0 : mInformeCompraList.size();
    }

    static class InformeCompraViewHolder extends RecyclerView.ViewHolder {
        final ListaInformeItemBinding binding;





        public InformeCompraViewHolder(ListaInformeItemBinding binding,AdapterCallback callback,boolean subiendoInf) {
            super(binding.getRoot());
            this.binding = binding;
            binding.liBtnedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(view.getId()==R.id.li_btnedit)
                    //    callback.onClickEditar(Integer.parseInt(binding.liTxtid.getText().toString()));
                        callback.onClickVer(Integer.parseInt(binding.liTxtid.getText().toString()));

                }
            });

         /*   binding.liBtnborrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("hola hola","hice clic");
                    if(view.getId()==R.id.li_btnborrar)
                        callback.onClickCancelar(Integer.parseInt(binding.liTxtid.getText().toString()));
                }
            });*/
            binding.liBtnsubir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.liBtnsubir.setEnabled(false);

                        callback.onClickSubir(Integer.parseInt(binding.liTxtid.getText().toString()));
                    //notifyDataSetChanged();
                }
            });

        }



    }
    public interface AdapterCallback {
        void onClickVer(int idinforme);
        void onClickCancelar(int id);
        void onClickEditar(int id);
        void onClickSubir(int id);
    }

}
