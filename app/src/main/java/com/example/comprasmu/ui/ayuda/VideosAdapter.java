package com.example.comprasmu.ui.ayuda;

import android.content.Context;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;


import com.example.comprasmu.R;
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
                .inflate(LayoutInflater.from(parent.getContext())
                );
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
        AdapterCallback callback;
        public ListaViewsViewHolder(MenuItemBinding binding, AdapterCallback callback) {
            super(binding.getRoot());
            this.binding = binding;
            this.callback=callback;

        }


        public void setCardView( MenuVideo cardView, Context context) {
            if(cardView!=null)
                this.binding.txtmenopcion.setText(cardView.getVid_nombreopcion());

            TextView txtsubopcion=new TextView(context);
            TextView txtmennombrearchivo=new TextView(context);
            ImageView botonReproducir;
            botonReproducir=new ImageView(context);
            // botonReproducir.getLayoutParams().width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            //botonReproducir.getLayoutParams().height=35;
            botonReproducir.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
            // Create a new View to serve as the divider

            View dividerView;
            if(cardView.getSubmenu()!=null) {

                for (MenuVideo subopcion: cardView.getSubmenu()
                ) {
                    txtsubopcion=new TextView(context);
                    txtsubopcion.setText(subopcion.getVid_nombreopcion());
                    //txtsubopcion.setBackgroundResource(R.drawable.valuecellborder);
                    txtsubopcion.setPadding(0,30,0,30);

                    txtsubopcion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // view.setEnabled(false);
                            callback.onClickVer(subopcion.getVid_liga());
                            Log.d("videos adapter","video"+subopcion.getVid_liga());
                        }
                    });
                    dividerView = new View(context);

                    // Define layout parameters for the divider (e.g., 1dp height, match_parent width)
                    int dividerHeightPx = (int) (context.getResources().getDisplayMetrics().density * 1); // 1dp in px
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, // Width
                            dividerHeightPx // Height in pixels
                    );
                    dividerView.setLayoutParams(layoutParams);

                    // Set the background color
                    dividerView.setBackgroundColor(Color.GRAY);
                    //agrego el submenu

                    this.binding.llmenusubmenu.addView(txtsubopcion);
                    this.binding.llmenusubmenu.addView(dividerView);
                }
            }

        }


    }


    public interface AdapterCallback {

        void onClickVer(String liga);

    }
}
