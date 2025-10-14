package com.example.comprasmu.ui.ayuda;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;


import com.example.comprasmu.data.modelos.MenuVideo;

import com.example.comprasmu.databinding.MenuItemBinding;


import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ListaViewsViewHolder> {

    private AdapterCallback callback;
    private List<MenuVideo> cardViewList;
    private Context context;
    public VideosAdapter(Context context) {

        this.context=context;

    }

    public void setList(List<MenuVideo> cardViewList,   AdapterCallback callback) {
        this.cardViewList = cardViewList;
        this.callback=callback;
    }

    @NonNull
    @Override
    public ListaViewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MenuItemBinding binding = MenuItemBinding
                .inflate(LayoutInflater.from(parent.getContext()),
                        parent, false);
        return new ListaViewsViewHolder(binding, callback);
    }

    @Override
    public void onBindViewHolder(@NonNull VideosAdapter.ListaViewsViewHolder holder, int position) {
      if(cardViewList!=null&&cardViewList.size()>0)
        holder.setCardView(cardViewList.get(position), this.context);
    }


    @Override
    public int getItemCount() {
        return cardViewList == null ? 0 : cardViewList.size();
    }

    static class ListaViewsViewHolder extends RecyclerView.ViewHolder {
        final MenuItemBinding binding;


        public ListaViewsViewHolder(MenuItemBinding binding, AdapterCallback callback) {
            super(binding.getRoot());
            this.binding = binding;
            binding.txtmenopcion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // view.setEnabled(false);
                    callback.onClickVer(binding.txtmennombrearchivo.getText().toString());
                }
            });
        }




        public void setCardView( MenuVideo cardView, Context context) {
            if(cardView!=null)
                this.binding.txtmenopcion.setText(cardView.getVid_nombreopcion());
            this.binding.txtmennombrearchivo.setText(cardView.getVid_liga());
            TextView txtsubopcion=new TextView(context);
            if(cardView.getSubmenu()!=null) {

                for (MenuVideo subopcion: cardView.getSubmenu()
                     ) {
                    txtsubopcion.setText(subopcion.getVid_nombreopcion());

                    //agrego el submenu
                    this.binding.llmenusubmenu.addView(txtsubopcion);
                }
            }

        }


    }


    public interface AdapterCallback {

        void onClickVer(String liga);

    }
}
