package com.example.comprasmu.utils.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprasmu.R;
import com.example.comprasmu.databinding.ListatextviewsItemBinding;

import java.util.List;
import java.util.Map;

public class ListaTextViewsAdapter extends RecyclerView.Adapter<ListaTextViewsAdapter.ListaTextViewsViewHolder> {


    private List<CardViewGenerico> cardViewList;
    private Context context;
    public ListaTextViewsAdapter( Context context) {

        this.context=context;

    }

    public void setCardViewList(List<CardViewGenerico> cardViewList) {
        this.cardViewList = cardViewList;
    }

    @NonNull
    @Override
    public ListaTextViewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListatextviewsItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.listatextviews_item, parent, false);
        return new ListaTextViewsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaTextViewsAdapter.ListaTextViewsViewHolder holder, int position) {
      if(cardViewList!=null&&cardViewList.size()>0)
        holder.setCardView(cardViewList.get(position), this.context);
    }


    @Override
    public int getItemCount() {
        return cardViewList == null ? 0 : cardViewList.size();
    }

    static class ListaTextViewsViewHolder extends RecyclerView.ViewHolder {
        final ListatextviewsItemBinding binding;


        public ListaTextViewsViewHolder(ListatextviewsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
        public void setCardView( CardViewGenerico cardView, Context context) {
            if(cardView!=null)
             Log.e("adapter",cardView.getTitulo1()+"--"+cardView.getTextos().toString());
            this.binding.txtlttitulo1.setText(cardView.getTitulo1());
            for (String entry :cardView.getTextos()){
                TextView textView=new TextView(context);
                textView.setText(entry);
                this.binding.llltmainlinearlayout.addView(textView);

            }
            if(cardView.getBotonIr()!=null){
                this.binding.llltmainlinearlayout.addView(cardView.getBotonIr());
            }

        }


    }


}
