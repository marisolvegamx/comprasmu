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
    private AdapterCallback callback;
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

        return new ListaCompraDetalleViewHolder(binding,this.ismuestra,callback);
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

     // if(listacomprasbu!=null) {

       //    List<InformeCompraDetalle> icd = buscarBU(mListaCompraDetalleList.get(position));
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

             //  holder.binding.informebudata.setAdapter(adaptadorLista);
               // holder.binding.setTotalbu(comprabu.size());
             //  int cantorig = mListaCompraDetalleList.get(position).getCantidad();
             //  mListaCompraDetalleList.get(position).setCantidad(cantorig - 1);


               // if(binding.informebudata.getDetallebu()!=null)
               //  holder.binding.informebudata.cajatexto.setVisibility(View.VISIBLE);
           }
      // }
     //  holder.binding.informebudata.setDetallebu(comprabu);
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
   /* private List<InformeCompraDetalle> buscarBU(ListaCompraDetalle det){
        List<InformeCompraDetalle> informes=new ArrayList<>();
        for(InformeCompraDetalle icd: listacomprasbu) {
            Log.d(TAG, "--------------Se seleccionó a " + det.getListaId() + "--" + det.getId() + "--" + icd.getComprasId() + "--" + icd.getComprasDetId());


            if (icd.getComprasId() == det.getListaId() && icd.getComprasDetId() ==det.getId() )
            {
                Log.d(TAG, "2--------------Se seleccionó a " +icd.getComprasId()+ "--" + det.getId()+"--"+ det.getListaId() +"--"+icd.getComprasDetId());

                 informes.add(icd);
             }
        }
        if(informes.size()>0) {

            return informes;
        }
        return  null;

    }*/

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
                              // Log.d(TAG,"haciendo chiquito");
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
         /* SE QUITAN LOS BOTONES QEUDARAN EN LAS NOTAS
          binding.btncodigosper.setOnClickListener(new View.OnClickListener() {

               @Override
               public void onClick(View view) {
                   switch (view.getId()) {
                       case R.id.btncodigosper:
                          // Log.d("Se seleccionó a ", binding.txtfecha.getText().toString());
                           //   Toast.makeText(context, "Se seleccionó a " + txtid.getText().toString(), Toast.LENGTH_SHORT).show();
                           //amplio el card view
                           if(binding.txtcodigosper.getVisibility()==View.GONE) {

                               binding.txtcodigosper.setVisibility(View.VISIBLE);

                           }else
                           {
                               Log.d(TAG,"haciendo chiquito");
                               binding.txtcodigosper.setVisibility(View.GONE);

                           }

                           break;
                       default:
                           break;
                   }
               }
           });
           binding.btncodigosex.setOnClickListener(new View.OnClickListener() {

               @Override
               public void onClick(View view) {
                   switch (view.getId()) {
                       case R.id.btncodigosex:
                          // Log.d("Se seleccionó a ", binding.txtfecha.getText().toString());
                           //   Toast.makeText(context, "Se seleccionó a " + txtid.getText().toString(), Toast.LENGTH_SHORT).show();
                           //amplio el card view
                           if(binding.txtcodigosex.getVisibility()==View.GONE) {

                               binding.txtcodigosex.setVisibility(View.VISIBLE);

                           }else
                           {
                               Log.d(TAG,"haciendo chiquito");
                               binding.txtcodigosex.setVisibility(View.GONE);

                           }

                           break;
                       default:
                           break;
                   }
               }
           });*/
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
//Log.d(TAG, "hice clikkkkkkkkk");
                       callback.verBackup(binding.getDetalle());

                   }
               }
           });
           binding.btnldbackuppen.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if (view.getId()==R.id.btnldbackuppen) {
//Log.d(TAG, "hice clikkkkkkkkk");
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
        List<InformeCompraDetalle> getBackup(ListaCompraDetalle productoSel);
    }

}
