package com.example.comprasmu.ui.informe;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprasmu.R;

import com.example.comprasmu.data.dao.ProductoExhibidoDao;
import com.example.comprasmu.data.modelos.ProductoExhibido;
import com.example.comprasmu.databinding.FotoexhibicionItemBinding;


import java.util.List;

/***adaptador para las fotos de producto exhibido que se muestran en el nuevo preinforme****/
public class FotoExhibicionAdapter extends RecyclerView.Adapter<FotoExhibicionAdapter.FotoExhibicionViewHolder> {

    private List<ProductoExhibidoDao.ProductoExhibidoFoto> mProductoExhibidoList;

 //   private final ListaInformesViewModel mViewModel;
    private AdapterCallback callback;
    private String directorio;
    public FotoExhibicionAdapter( AdapterCallback callback) {

     //   mViewModel = viewModel;
        this.callback=callback;
    }

    public void setProductoExhibidoList(List<ProductoExhibidoDao.ProductoExhibidoFoto> informesList, String directorio) {
        mProductoExhibidoList = informesList;
        this.directorio=directorio;
       // notifyDataSetChanged();
    }
    @NonNull
    @Override
    public FotoExhibicionAdapter.FotoExhibicionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FotoexhibicionItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.fotoexhibicion_item, parent, false);
        return new FotoExhibicionViewHolder(binding,callback);
    }

    @Override
    public void onBindViewHolder(@NonNull FotoExhibicionAdapter.FotoExhibicionViewHolder holder, int position) {
     //   holder.binding.setViewModel(mViewModel);
        holder.binding.setDetalle(mProductoExhibidoList.get(position));
        holder.binding.setDirectorio(directorio);

    }


    @Override
    public int getItemCount() {
        return mProductoExhibidoList == null ? 0 : mProductoExhibidoList.size();
    }

    static class FotoExhibicionViewHolder extends RecyclerView.ViewHolder {
        final FotoexhibicionItemBinding binding;

        public FotoExhibicionViewHolder(FotoexhibicionItemBinding binding,AdapterCallback callback) {
            super(binding.getRoot());
            this.binding = binding;

            binding.btnfeeliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("FotoExhibAdapter","hice clic"+binding.txtfeid.getText().toString());
                    int idfoto=Integer.parseInt(binding.txtfeid.getText().toString());

                    //lo elimino de la lista
                    if(view.getId()==R.id.btnfeeliminar)
                        callback.onClickEliminar(binding.getDetalle());

                }
            });


        }

    }
    public interface AdapterCallback {

        void onClickEliminar(ProductoExhibidoDao.ProductoExhibidoFoto foto);

    }

}
