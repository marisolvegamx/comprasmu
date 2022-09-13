package com.example.comprasmu.ui.gallery;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.ui.RevisarFotoActivity;
import com.example.comprasmu.utils.Constantes;

import java.util.List;

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder>  {

    private List<ImagenDetalle> mSpacePhotos;
    private Context mContext;
    String directorio;
   // private AdapterCallback callback;
    public ImageGalleryAdapter(Context context, List<ImagenDetalle> spacePhotos) {
        mContext = context;
        mSpacePhotos = spacePhotos;
        directorio=context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/";


    }
    public ImageGalleryAdapter(Context context) {
        mContext = context;

        directorio=context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/";


    }




    public void setmSpacePhotos(List<ImagenDetalle> mSpacePhotos) {
        this.mSpacePhotos = mSpacePhotos;
    }

    @Override
    public ImageGalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View photoView = inflater.inflate(R.layout.foto_item, parent, false);
        ImageGalleryAdapter.MyViewHolder viewHolder = new ImageGalleryAdapter.MyViewHolder(photoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ImageGalleryAdapter.MyViewHolder holder, int position) {

        ImagenDetalle spacePhoto = mSpacePhotos.get(position);
        ImageView imageView = holder.mPhotoImageView;
        Glide.with(mContext)
                .load(directorio+spacePhoto.getRuta())
              //  .placeholder(R.drawable.ic_cloud_off_red)
                .into(imageView);
        holder.txtdesc.setText(spacePhoto.getDescripcion());
        holder.txtruta.setText(spacePhoto.getRuta());
        if(Constantes.ETAPAACTUAL==3) {
            holder.txtdesc.setText("MUESTRA "+(position+1));
            holder.atr1.setText(spacePhoto.getDescripcion()); //qr
            holder.atr1.setVisibility(View.VISIBLE);
          //  holder.atr2.setText("CAJA: "+spacePhoto.getRuta());//caja
        }
    }


    @Override
    public int getItemCount() {
        if(mSpacePhotos!=null)
        return (mSpacePhotos.size());
        return 0;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mPhotoImageView;
        public TextView txtdesc;
        public TextView txtruta;
        public TextView atr1;
        public TextView atr2;


        public MyViewHolder(View itemView) {

            super(itemView);
            mPhotoImageView = (ImageView) itemView.findViewById(R.id.iv_photo);
            txtdesc=itemView.findViewById(R.id.txtfidesc);
            txtruta=itemView.findViewById(R.id.txtfiruta);
            atr1=itemView.findViewById(R.id.txtfiatr1);
            atr2=itemView.findViewById(R.id.txtfiatr2);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION) {
                ImagenDetalle spacePhoto = mSpacePhotos.get(position);
                Intent intent = new Intent(mContext, RevisarFotoActivity.class);
                intent.putExtra(RevisarFotoActivity.IMG_PATH1,txtruta.getText().toString());
                mContext.startActivity(intent);
            }
        }
    }


}
