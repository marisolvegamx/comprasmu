package com.example.comprasmu.ui.informe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprasmu.R;
import com.example.comprasmu.data.dao.InformeCompraDao;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.databinding.InformeCancelItemBinding;
import com.example.comprasmu.databinding.ListaInformeItemBinding;
import com.example.comprasmu.utils.Constantes;

import java.util.List;

public class CancelAdapter extends RecyclerView.Adapter<CancelAdapter.InformeCompraViewHolder> {

  //  private List<InformeCompra> mInformeCompraList;
    private List<InformeCompraDao.InformeCompravisita> mInformeCompraList;
    private List<InformeCompraDetalle> mdetalle;

    private AdapterCallback callback;

    public CancelAdapter(AdapterCallback callback) {


        this.callback=callback;
    }

    public void setInformeCompraList(List<InformeCompraDao.InformeCompravisita> informesList) {
        mInformeCompraList = informesList;
       // notifyDataSetChanged();
    }

    public void setProductoList(List<InformeCompraDetalle> informesList) {
        mdetalle = informesList;
        // notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CancelAdapter.InformeCompraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        InformeCancelItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.informe_cancel_item, parent, false);
        return new InformeCompraViewHolder(binding,callback);
    }

    @Override
    public void onBindViewHolder(@NonNull CancelAdapter.InformeCompraViewHolder holder, int position) {
       // holder.binding.setViewModel(mViewModel);
        if(mInformeCompraList!=null)
        holder.binding.setDetalle(mInformeCompraList.get(position));
        if(mdetalle!=null)
        holder.binding.setProducto(mdetalle.get(position));
        holder.binding.setSdf(Constantes.vistasdf);
      //  holder.binding.setVisita();
      //  holder.binding.executePendingBindings();

    }


    @Override
    public int getItemCount() {
        return mInformeCompraList == null ? 0 : mInformeCompraList.size();
    }

    static class InformeCompraViewHolder extends RecyclerView.ViewHolder {
        final InformeCancelItemBinding binding;





        public InformeCompraViewHolder(InformeCancelItemBinding binding,AdapterCallback callback) {
            super(binding.getRoot());
            this.binding = binding;
            binding.btnicnuevo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  //  if(view.getId()==R.id.li_btnedit)
                    //    callback.onClickEditar(Integer.parseInt(binding.liTxtid.getText().toString()));
                    //    callback.onClickVer(Integer.parseInt(binding.liTxtid.getText().toString()));

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


        }



    }
    public interface AdapterCallback {
        void onClickVer(int idinforme);

    }

}
