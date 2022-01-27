package com.example.comprasmu.ui.visita;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.Visita;
import com.example.comprasmu.databinding.ListaInformesFragmentBinding;
import com.example.comprasmu.ui.BackActivity;
import com.example.comprasmu.ui.informe.NuevoinformeFragment;
import com.example.comprasmu.ui.informe.NuevoinformeViewModel;
import com.example.comprasmu.ui.informedetalle.ContinuarInformeActivity;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.ui.FiltrarListaActivity;

import java.util.List;

import static com.example.comprasmu.ui.listacompras.TabsFragment.ARG_CLIENTESEL;

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

        coordinator=view.findViewById(R.id.coordinator);

        setupListAdapter();
       // setupSnackbar();
        LiveData<List<Visita>> lista=mViewModel.getListas();
        lista.observe(getViewLifecycleOwner(), new Observer<List<Visita>>() {

            @Override
            public void onChanged(List<Visita> visitas) {
                Log.d(TAG,"YA CARGÓ "+visitas.size());
                //primero reviso si ya tiene informe para no mostrar el finalizar
                for(Visita visit:visitas){
                    visit=mViewModel.tieneInforme(visit,getViewLifecycleOwner());
                    Log.d(TAG,"qqqqqqqqqq"+visit.getEstatus());
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
   /* private void setupSnackbar() {
        // Mostrar snackbar en resultados positivos de operaciones (crear, editar y eliminar)
        mViewModel.getSnackbarText().observe(getActivity(), integerEvent -> {
            Integer stringId = integerEvent.getContentIfNotHandled();
            if (stringId != null) {
                Snackbar.make(coordinator,
                        stringId, Snackbar.LENGTH_LONG).show();
            }
        });
    }*/

   /* solo saldrá un elemento no se requiere la busqueda
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        menu.clear();
        inflater.inflate(R.menu.menu_listainforme, menu);
        //  super.onCreateOptionsMenu(menu, inflater);


    }*/
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
        Bundle bundle = new Bundle();
        bundle.putInt(ContinuarInformeActivity.INFORMESEL,idvisita);

        //NavHostFragment.findNavController(this).navigate(R.id.action_visitatonuevo,bundle);
        Intent intento1=new Intent(getActivity(), ContinuarInformeActivity.class);
        intento1.putExtras(bundle);

        startActivity(intento1);

    }

    @Override
    public void onClickEliminar(int idVisita) {

        try{
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
            dialogo1.setTitle(R.string.importante);
            dialogo1.setMessage(R.string.pregunta_eliminar_mensaje);
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {


                    mViewModel.eliminarVisita(idVisita);

                    mViewModel.getmSnackbarText().observe(getViewLifecycleOwner(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();

                        }
                    });
                     //actualizo la lista
                    mListAdapter.notifyDataSetChanged();
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

    @Override
    public void onClickEditar(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(AbririnformeFragment.EXTRAPREINFORME_ID,id );
        /* bundle.putString("plantaNombre", listaSeleccionable.get(i).getNombre());*/
           NavHostFragment.findNavController(this).navigate(R.id.nav_nuevoinforme,bundle);

    }

    @Override
    public void onClickFinalizar(int idvisita) {
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

                Toast.makeText(getActivity(), "Se finalizó el preinforme",Toast.LENGTH_SHORT).show();
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