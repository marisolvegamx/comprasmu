package com.example.comprasmu.ui.visita;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.comprasmu.R;

import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.ProductoExhibido;
import com.example.comprasmu.data.modelos.Visita;
import com.example.comprasmu.databinding.ListaInformesFragmentBinding;
import com.example.comprasmu.ui.informe.NuevoinformeViewModel;
import com.example.comprasmu.ui.informedetalle.ContinuarInformeActivity;
import com.example.comprasmu.ui.informedetalle.ValidadorDatos;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.ui.FiltrarListaActivity;

import java.util.Date;
import java.util.List;

public class ListaVisitasFragment extends Fragment implements VisitaAdapter.AdapterCallback {
    private ListaVisitasViewModel mViewModel;


    public static final String TAG = "ListaVisitaFragment";
   private ListaInformesFragmentBinding mBinding;
    private VisitaAdapter mListAdapter;

    static String ciudadid;

    static String tienda;
    static String indice;
    CoordinatorLayout coordinator;

    public ListaVisitasFragment() {
    }

    public static ListaVisitasFragment newInstance()
    {
     //   ListaVisitaFragment fragment = new ListaVisitaFragment();
      //  Bundle bundle = new Bundle();
      //  bundle.putInt(ARG_CLIENTEID, cliente);

       // fragment.setArguments(bundle);
      //  clienteid=cliente;
        return new ListaVisitasFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (getArguments() != null) {
        //    clienteid = getArguments().getInt(BuscarInformeFragment.ARG_CLIENTE);
        //    plantaid=getArguments().getInt(ARG_PLANTAID);
         //   ciudadid=getArguments().getInt(BuscarInformeFragment.CIUDAD);
         //   tienda=getArguments().getString(BuscarInformeFragment.NOMBRETIENDA);
         //   indice=getArguments().getString(BuscarInformeFragment.INDICE);

        }else {
            indice = Constantes.INDICEACTUAL;

        }

     //   Log.d(Constantes.TAG,"cliente y planta sel"+clienteid+"--"+plantaid);

        mBinding= DataBindingUtil.inflate(inflater,
                R.layout.lista_informes_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(ListaVisitasViewModel.class);
        setHasOptionsMenu(true);
        return    mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mViewModel.setCiudadSel(ciudadid);
        mViewModel.setIndiceSel(indice);
        mViewModel.setNombreTienda(tienda);
        mViewModel.cargarDetalles();
       // mBinding.setLcviewModel(mViewModel);
        mBinding.setLifecycleOwner(this);

        coordinator=view.findViewById(R.id.coordinator3);

        setupListAdapter();
       // setupSnackbar();
        LiveData<List<Visita>> lista=mViewModel.getListas();
        lista.observe(getViewLifecycleOwner(), new Observer<List<Visita>>() {

            @Override
            public void onChanged(List<Visita> visitas) {
                Log.d(TAG,"YA CARGÓ "+visitas.size());
                //primero reviso si ya tiene informe para no mostrar el finalizar
                for(Visita visit:visitas){
                    visit=mViewModel.tieneInforme(visit);
                    Log.d(TAG,"qqqqqqqqqq"+visit.getCiudad());
                }
                mListAdapter.setVisitaList(visitas);
                mListAdapter.notifyDataSetChanged();
                if(visitas!=null&&visitas.size()>0)
                    mBinding.emptyStateText.setVisibility(View.INVISIBLE);
                else
                    mBinding.emptyStateText.setVisibility(View.VISIBLE);
            }
        });

    }

    private void setupListAdapter() {
        mListAdapter = new VisitaAdapter(this);
        mBinding.detalleList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.detalleList.setHasFixedSize(true);
        mBinding.detalleList.setAdapter(mListAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_searchinforme:

                Intent intento1 = new Intent(getActivity(), FiltrarListaActivity.class);
                intento1.putExtra("filtrarlista.fragment","buscarinforme");
                startActivity(intento1);

                return true;

            default:
                break;
        }

        return false;
    }

    @Override
    public void onClickAgregar(int idvisita) {
        //primero valido que ya tenga fotos de prodex
        List<ProductoExhibido> productoExhibidos=mViewModel.buscarProdExhiPend(idvisita);
        Log.d(TAG,"buscando prodexh"+productoExhibidos.size());
        //por lo menos 1
        if(productoExhibidos==null||productoExhibidos.size()==0) {
            Toast.makeText(getActivity(), "Falta foto de producto exhibido", Toast.LENGTH_LONG).show();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt(ContinuarInformeActivity.INFORMESEL,idvisita);

        //NavHostFragment.findNavController(this).navigate(R.id.action_visitatonuevo,bundle);
        Intent intento1=new Intent(getActivity(), ContinuarInformeActivity.class);
        intento1.putExtras(bundle);

        startActivity(intento1);

    }

    @Override
    public void onClickEliminar(int idVisita, Visita visitaCont) {
        //reviso si elimina x fecha
        ValidadorDatos valdat=new ValidadorDatos();
        //si se creo antes de hoy
        try{

            if(valdat.compararFecha(visitaCont.getCreatedAt(),new Date())){
                //elimino
               eliminar(idVisita, 1);
               return;
           }
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
            dialogo1.setTitle(R.string.importante);
            dialogo1.setMessage(R.string.pregunta_eliminar_mensaje);
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    eliminar(idVisita,0);


                }
            });
            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    dialogo1.cancel();
                }
            });
            dialogo1.show();

        }
        catch (Exception ex){
            Log.e(TAG,"Error"+ ex.getMessage());
            Toast.makeText(getActivity(), "Hubo un error al eliminar", Toast.LENGTH_LONG).show();

        }

    }
    public void eliminar(int idVisita, int banAccion){ //0 eliminado x no finalizado
                                                        //1 eliminado x fecha
        mViewModel.eliminarVisita(idVisita, banAccion);

        mViewModel.getmSnackbarText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();

            }
        });
        //actualizo la lista
        mListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClickEditar(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(AbririnformeFragment.EXTRAPREINFORME_ID,id );
        /* bundle.putString("plantaNombre", listaSeleccionable.get(i).getNombre());*/
           NavHostFragment.findNavController(this).navigate(R.id.nav_nuevoinforme,bundle);

    }

    @Override
    public void onClickFinalizar(int idvisita, Visita visitaCont) {
        ValidadorDatos valdat=new ValidadorDatos();
        //si se creo antes de hoy
       if(valdat.compararFecha(visitaCont.getCreatedAt(),new Date())){
           if(visitaCont.getEstatusSync()==0) {
               Toast.makeText(getActivity(), "El informe no se puede finalizar por ser de una fecha posterior, favor de eliminar", Toast.LENGTH_SHORT).show();

               return;
           }
        }
        //reviso si ya se enviaron los informes
        //todo como va a enviarlo sin finalizar
        List<InformeCompra> informes=mViewModel.tieneInformePend(idvisita);
        if(informes!=null&&informes.size()>0) //no puede finalizar
        {
            Toast.makeText(getActivity(), getString(R.string.no_finalizar),Toast.LENGTH_SHORT).show();
            return;
        }
        //puede que no esté guardado reviso si hay algo en la tabla temporal
        if(mViewModel.hayInfDetalleTemp()){
            Toast.makeText(getActivity(), getString(R.string.no_finalizar),Toast.LENGTH_SHORT).show();
            return;
        }
        //pregunto si habrá más clientes
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
        dialogo1.setTitle(R.string.importante);
        dialogo1.setMessage(R.string.conf_finalizar);
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                //Es hora de cerrar el preinforme
                NuevoinformeViewModel niViewModel =
                        new ViewModelProvider(ListaVisitasFragment.this).get(NuevoinformeViewModel.class);
                niViewModel.finalizarVisita(idvisita);

                Toast.makeText(getActivity(), getString(R.string.informe_finalizado),Toast.LENGTH_SHORT).show();
                //paso al home
                NavHostFragment.findNavController(ListaVisitasFragment.this).navigate(R.id.action_visitatohome);

            }
        });
        dialogo1.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                //no hago nada
                dialogo1.cancel();

            }
        });
        dialogo1.show();
    }


}