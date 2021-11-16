package com.example.comprasmu.ui.listadetalle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import androidx.recyclerview.widget.RecyclerView;

//import com.example.comprasmu.BR;
import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;

import com.example.comprasmu.databinding.ListaDetalleItemBinding;

import com.example.comprasmu.utils.Constantes;

import java.util.List;

public class ListaCompraDetalleAdapter extends RecyclerView.Adapter<ListaCompraDetalleAdapter.ListaCompraDetalleViewHolder> {

    private List<ListaCompraDetalle> mListaCompraDetalleList;

    private final ListaDetalleViewModel mViewModel;
    private AdapterCallback callback;
    private final static String TAG=ListaCompraDetalleAdapter.class.getName();
    int numtienda;
    boolean isbu, ismuestra;//para saber si ya estoy en lista de bu o agregando muestra

    public ListaCompraDetalleAdapter(ListaDetalleViewModel viewModel, AdapterCallback callback) {

        mViewModel = viewModel;
        this.callback=callback;

    }

    public void setListaCompraDetalleList(List<ListaCompraDetalle> categoriesList,int numtienda, boolean isbu,boolean ismuestra) {
        mListaCompraDetalleList = categoriesList;
        this.numtienda=numtienda;
        this.isbu=isbu;
        this.ismuestra=ismuestra;
      //  notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ListaCompraDetalleAdapter.ListaCompraDetalleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListaDetalleItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.lista_detalle_item, parent, false);

        return new ListaCompraDetalleViewHolder(binding,mViewModel.isNuevaMuestra(),callback);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaCompraDetalleAdapter.ListaCompraDetalleViewHolder holder, int position) {
    //    holder.binding.setViewModel(mViewModel);
       // holder.binding.setVariable(BR.detalle,mListaCompraDetalleList.get(position));
       holder.binding.setDetalle(mListaCompraDetalleList.get(position));
       holder.binding.setNumTienda(numtienda);
       holder.binding.setIsBu(isbu);
       holder.binding.setMostrarAgregar(ismuestra);
     //  Log.d(TAG,"mostar agregar "+mListaCompraDetalleList.get(position).getComprados()+"--"+mListaCompraDetalleList.get(position).getCantidad());
      /*  if(mListaCompraDetalleList.get(position).getComprados()==mListaCompraDetalleList.get(position).getCantidad()){
            holder.binding.setMostrarAgregar(false);
        }
        else
            holder.binding.setMostrarAgregar(true);*/
      //  holder.binding.executePendingBindings();

    }
    public String getItemCodigos(int index) {
        return  mListaCompraDetalleList.get(index).getCodigosNoPermitidos();
    }

    @Override
    public int getItemCount() {
        return mListaCompraDetalleList == null ? 0 : mListaCompraDetalleList.size();
    }

    static class ListaCompraDetalleViewHolder extends RecyclerView.ViewHolder  {
        final ListaDetalleItemBinding binding;
        boolean isNueva;
       // final PruebarecyclerBinding binding;

        static ViewGroup.LayoutParams   altoini;

       public ListaCompraDetalleViewHolder(ListaDetalleItemBinding binding,boolean isNueva,AdapterCallback callback) {

      //  public ListaCompraDetalleViewHolder(PruebarecyclerBinding binding,AdapterCallback callback) {
            super(binding.getRoot());
            this.binding = binding;
            binding.btnldagregar.setVisibility(View.GONE);
           if(isNueva)
               binding.btnldagregar.setVisibility(View.VISIBLE);
             //  binding.setMostrarAgregar(true);
            // if(!binding.txtdicomprados.getText().toString().equals("")&&Integer.parseInt(binding.txtdicomprados.getText().toString())<Integer.parseInt(binding.txtdicantidad.getText().toString()))

            //
           altoini=binding.ldcardview.getLayoutParams();

           binding.txtcodigos.setVisibility(View.GONE);
            binding.btncodigos.setOnClickListener(new View.OnClickListener() {

               @Override
               public void onClick(View view) {
                   switch (view.getId()) {
                       case R.id.btncodigos:
                             Log.d("Se seleccionó a ", binding.txtfecha.getText().toString());
                           //   Toast.makeText(context, "Se seleccionó a " + txtid.getText().toString(), Toast.LENGTH_SHORT).show();
                           //amplio el card view
                           if(binding.txtcodigos.getVisibility()==View.GONE) {

                               binding.txtcodigos.setVisibility(View.VISIBLE);
                              // int nvoalto= binding.txtcodigos.getHeight();

                               //binding.ldcardview.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 900));
                               //LinearLayout rLGreen = ((LinearLayout) binding.cajatexto);
                               //ViewGroup.LayoutParams params = rLGreen.getLayoutParams();
// Changes the height and width to the specified *pixels*
                               //params.height = 900;
                               //params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                              //  rLGreen.setLayoutParams(params);
                           //    rLGreen.setLayoutParams(new LinearLayout.LayoutParams(, 900));
                           //    LinearLayout rLGreen2 = ((LinearLayout) rLGreen.getParent());
                               //rLGreen.setLayoutParams(new LinearLayout.LayoutParams(, 900));

                           }else
                           {
                               Log.d(TAG,"haciendo chiquito");
                               binding.txtcodigos.setVisibility(View.GONE);
                           //    binding.ldcardview.setLayoutParams(altoini);
                            //   LinearLayout rLGreen = ((LinearLayout) binding.btncodigos.getParent());
                              // ViewGroup.LayoutParams params=rLGreen.getLayoutParams();
                               //params.width=LinearLayout.LayoutParams.MATCH_PARENT;
                               //params.height=ViewGroup.LayoutParams.WRAP_CONTENT;
                               //rLGreen.setLayoutParams(new ViewGroup.LayoutParams(, ));
                              // LinearLayout rLGreen2 = ((LinearLayout) rLGreen.getParent());
                               //rLGreen.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                           }

                           break;
                       default:
                           break;
                   }
               }
          });
            binding.btnldagregar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.getId()==R.id.btnldagregar) {
                        callback.agregarMuestra(view,binding.getDetalle());
                    }
                }
            });
           binding.btnldbackup.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if (view.getId()==R.id.btnldbackup) {
                       callback.verBackup(binding.getDetalle());
                   }
               }
           });
        }
       /*  public void setOnClickListeners() {
             btncodigos.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {

            callback.onClickCallback(view);
        }*/
    }
    public interface AdapterCallback {
        void onClickCallback(View view);
        void agregarMuestra(View view,ListaCompraDetalle productoSel);
        void verBackup(ListaCompraDetalle productoSel);
    }

}
