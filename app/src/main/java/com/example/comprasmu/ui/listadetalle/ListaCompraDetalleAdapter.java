package com.example.comprasmu.ui.listadetalle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import androidx.recyclerview.widget.RecyclerView;

//import com.example.comprasmu.BR;
import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;

import com.example.comprasmu.data.modelos.ListaDetalleBu;
import com.example.comprasmu.databinding.ListaDetalleItemBinding;

import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.ui.ListaSelecFragment;

import java.util.ArrayList;
import java.util.List;

public class ListaCompraDetalleAdapter extends RecyclerView.Adapter<ListaCompraDetalleAdapter.ListaCompraDetalleViewHolder> {

    private List<ListaDetalleBu> mListaCompraDetalleList;

    private final ListaDetalleViewModel mViewModel;
    private final AdapterCallback callback;
    ListaDetalleBuAdapter childAdapter;
    private final static String TAG=ListaCompraDetalleAdapter.class.getName();
    int numtienda;
    boolean isbu, ismuestra;//para saber si ya estoy en lista de bu o agregando muestra
    int cliente;
    ViewGroup parent;
    int criterio;
    int plantasel;
    public ListaCompraDetalleAdapter(ListaDetalleViewModel viewModel, AdapterCallback callback) {

        mViewModel = viewModel;
        this.callback=callback;

    }

    public void setListaCompraDetalleList(List<ListaDetalleBu> categoriesList, int numtienda, boolean isbu, boolean ismuestra, int cliente, int criteriobu, int plantasel) {
        mListaCompraDetalleList = categoriesList;
        this.numtienda=numtienda;
        this.isbu=isbu;
        this.ismuestra=ismuestra;
        this.cliente=cliente;
        this.criterio=criteriobu;
        this.plantasel=plantasel;
     //   this.listacomprasbu=listacomprasbu;

       // Log.d(TAG,"consecutivo "+numtienda);
      //  notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ListaCompraDetalleAdapter.ListaCompraDetalleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListaDetalleItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.lista_detalle_item, parent, false);
        this.parent=parent;

        return new ListaCompraDetalleViewHolder(binding,this.ismuestra,callback,this.numtienda);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaCompraDetalleAdapter.ListaCompraDetalleViewHolder holder, int position) {

       // holder.binding.setVariable(BR.detalle,mListaCompraDetalleList.get(position));
       holder.binding.setDetalle(mListaCompraDetalleList.get(position));
       holder.binding.setNumTienda(numtienda);
       holder.binding.setIsBu(isbu);
       holder.binding.setMostrarAgregar(ismuestra);
       holder.binding.setCliente(cliente);
       holder.binding.setPlantasel(plantasel);
    //   this.mViewModel.setListacomprasbu(listacomprasbu);
       holder.binding.setMViewModel(this.mViewModel);
        holder.binding.setCriteriobu(this.criterio);
//        Log.d(TAG, "va    rs " +listacomprasbu.size());
      // if(cliente==4&&numtienda>10&&!isbu) {
           if(!isbu) {
           holder.binding.setMostrarbcu(true);
       }
        if(cliente!=4&&!isbu) {
            holder.binding.setMostrarbcu(true);
        }

        List<InformeCompraDetalle> icd =mListaCompraDetalleList.get(position).getInfcd();
           if (icd != null) {
               Log.d(TAG, "es bu " + icd.size());
               ListaDetalleBuAdapter adaptadorLista = new ListaDetalleBuAdapter((AppCompatActivity) parent.getContext(),icd, cliente);
               final int adapterCount = adaptadorLista.getCount();
               holder.binding.informebudata.removeAllViews();
               for (int i = 0; i < adapterCount; i++) {
                   View item = adaptadorLista.getView(i, null, null);

                   holder.binding.informebudata.addView(item);
               }


           }


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
        int numtienda;
        boolean isNueva;
       // final PruebarecyclerBinding binding;

        static ViewGroup.LayoutParams   altoini;

       public ListaCompraDetalleViewHolder(ListaDetalleItemBinding binding,boolean isNueva,AdapterCallback callback, int numtienda) {

      //  public ListaCompraDetalleViewHolder(PruebarecyclerBinding binding,AdapterCallback callback) {
            super(binding.getRoot());
            this.binding = binding;
            this.numtienda=numtienda;
            binding.btnldagregar.setVisibility(View.GONE);
           Log.d(TAG,"qqqqqqqqqqqqq"+isNueva);
           if(isNueva)
               binding.btnldagregar.setVisibility(View.VISIBLE);
             //  binding.setMostrarAgregar(true);
            // if(!binding.txtdicomprados.getText().toString().equals("")&&Integer.parseInt(binding.txtdicomprados.getText().toString())<Integer.parseInt(binding.txtdicantidad.getText().toString()))

            //
         //  Log.d(TAG,"xxxxxxx "+binding.informebudata.txtldpres.getText()+"--"+binding.informebudata.txtldcomprabu.getText());
           altoini=binding.ldcardview.getLayoutParams();

           binding.txtcodigos.setVisibility(View.GONE);

           binding.btncodigos.setOnClickListener(new View.OnClickListener() {

               @Override
               public void onClick(View view) {
                   switch (view.getId()) {
                       case R.id.btncodigos:
                            // Log.d("Se seleccionó a ", binding.txtfecha.getText().toString());
                           //   Toast.makeText(context, "Se seleccionó a " + txtid.getText().toString(), Toast.LENGTH_SHORT).show();
                           //amplio el card view
                           if(binding.txtcodigos.getVisibility()==View.GONE) {

                               binding.txtcodigos.setVisibility(View.VISIBLE);

                           }else
                           {
                              // Log.d(TAG,"haciendo chiquito");
                               binding.txtcodigos.setVisibility(View.GONE);

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
           binding.btnldbackuppen.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if (view.getId()==R.id.btnldbackuppen) {

                       callback.verBackupPen(binding.getDetalle(),numtienda);

                   }
               }
           });
        }

    }
    public interface AdapterCallback {
        void onClickCallback(View view);
        void agregarMuestra(View view,ListaCompraDetalle productoSel);
        void verBackup(ListaCompraDetalle productoSel);
        List<InformeCompraDetalle> getBackup(ListaCompraDetalle productoSel);

        void verBackupPen(ListaDetalleBu detalle, int numtienda);
    }

}
