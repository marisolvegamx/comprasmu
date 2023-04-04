package com.example.comprasmu.ui.empaque;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.DetalleCaja;
import com.example.comprasmu.databinding.ContinfgenItemBinding;

import com.example.comprasmu.databinding.DetallecajaItemBinding;
import com.example.comprasmu.utils.Constantes;

import java.util.List;

public class DetalleCajaAdapter extends RecyclerView.Adapter<DetalleCajaAdapter.DetalleCajaViewHolder>  {

    private List<DetalleCaja> listaCajas;

    String directorio;

     private AdapterCallback callback;

    public DetalleCajaAdapter(Context context, AdapterCallback callback) {


        directorio=context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/";
        this.callback=callback;

    }

    public void setList(List<DetalleCaja> listaCajas) {
        this.listaCajas = listaCajas;
        // notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DetalleCajaAdapter.DetalleCajaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DetallecajaItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.detallecaja_item, parent, false);
        return new DetalleCajaAdapter.DetalleCajaViewHolder(binding,callback);
    }



    @Override
    public void onBindViewHolder(@NonNull DetalleCajaAdapter.DetalleCajaViewHolder holder, int position) {
        holder.binding.setDetalle(listaCajas.get(position));

    }

    @Override
    public int getItemCount() {
        return listaCajas == null ? 0 : listaCajas.size();
    }

    static class DetalleCajaViewHolder extends RecyclerView.ViewHolder {
        final DetallecajaItemBinding binding;
        public DetalleCajaViewHolder(DetallecajaItemBinding binding, DetalleCajaAdapter.AdapterCallback callback) {
            super(binding.getRoot());
            this.binding = binding;

            binding.btndcver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // view.setEnabled(false);
                    callback.onClickVer(Integer.parseInt(binding.txtdcid.getText().toString()));
                }
            });
        }

    }
    public interface AdapterCallback {

        void onClickVer(int id);

    }

}
