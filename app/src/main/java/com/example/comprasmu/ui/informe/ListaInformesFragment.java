package com.example.comprasmu.ui.informe;

import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;

import com.example.comprasmu.SubirInformeTask;
import com.example.comprasmu.data.dao.InformeCompraDao;

import com.example.comprasmu.data.remote.InformeEnvio;

import com.example.comprasmu.databinding.ListaInformesFragmentBinding;
import com.example.comprasmu.ui.BackActivity;

import com.example.comprasmu.ui.listadetalle.ListaCompraFragment;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.ui.FiltrarListaActivity;
import com.google.android.material.snackbar.Snackbar;
import java.util.List;

public class    ListaInformesFragment extends Fragment implements InformeCompraAdapter.AdapterCallback {
    private ListaInformesViewModel mViewModel;

    private NuevoinformeViewModel niViewModel;
    public static final String TAG = "ListaInformesFragment";
    private ListaInformesFragmentBinding mBinding;
    private InformeCompraAdapter mListAdapter;

    private int clienteid;
    private String ciudad;
    private int plantaid;
    private String tienda;
    private String indice;
    String clientesel;
    int plantasel;
    CoordinatorLayout coordinator;

    public ListaInformesFragment() {
    }

  /*  public  ListaInformesFragment(int planta, String onombrePlanta, String nomcliente) {
        //  ListaCompraFragment fragment = new ListaCompraFragment();
        plantaid=planta;
        plantasel=onombrePlanta;
        this.clientesel=nomcliente;

    }*/

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (getArguments() != null) {
            //    clienteid = getArguments().getInt(BuscarInformeFragment.ARG_CLIENTE);
            plantasel = getArguments().getInt(ListaCompraFragment.ARG_PLANTASEL);
            //  ciudad=getArguments().getString(BuscarInformeFragment.CIUDAD);
            tienda = getArguments().getString(BuscarInformeFragment.NOMBRETIENDA);
            indice = getArguments().getString(BuscarInformeFragment.INDICE);

        } else {
            indice = Constantes.INDICEACTUAL;

        }

        //   Log.d(Constantes.TAG,"cliente y planta sel"+clienteid+"--"+plantaid);

        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.lista_informes_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(com.example.comprasmu.ui.informe.ListaInformesViewModel.class);
        niViewModel = new ViewModelProvider(this).get(NuevoinformeViewModel.class);

        setHasOptionsMenu(true);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        mBinding.setLcviewModel(mViewModel);
        mBinding.setLifecycleOwner(this);
        coordinator = view.findViewById(R.id.coordinator3);

        setupListAdapter();
        setupSnackbar();
        cargarLista();
    }

    public void cargarLista() {
        mViewModel.setClienteSel(clientesel);
        mViewModel.setPlantaSel(plantasel);
        mViewModel.setNombreTienda(tienda);
        mViewModel.setCiudadSel(ciudad);
        mViewModel.setIndiceSel(indice);
        mViewModel.cargarDetalles();


        mViewModel.getListas().observe(getViewLifecycleOwner(), new Observer<List<InformeCompraDao.InformeCompravisita>>() {
            @Override
            public void onChanged(List<InformeCompraDao.InformeCompravisita> informeCompras) {
                if (informeCompras.size() < 1) {
                    mBinding.emptyStateText.setVisibility(View.VISIBLE);
                } else {
                    Log.d(TAG, "YA CARGÓ " + informeCompras.size());
                    //primero reviso el estatussync
                    for (InformeCompraDao.InformeCompravisita informe : informeCompras) {
                        if (informe.estatusSync != 1) //no se está enviando
                            informe.estatusSync = niViewModel.getEstatusSyncInforme(informe.idinforme);
                    }
                }
                mListAdapter.setInformeCompraList(informeCompras);
                mListAdapter.notifyDataSetChanged();

            }
        });
    }

    private void setupListAdapter() {
        mListAdapter = new InformeCompraAdapter(mViewModel, this);
        mBinding.detalleList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.detalleList.setHasFixedSize(true);
        mBinding.detalleList.setAdapter(mListAdapter);

    }

    private void setupSnackbar() {
        // Mostrar snackbar en resultados positivos de operaciones (crear, editar y eliminar)
        mViewModel.getSnackbarText().observe(getActivity(), integerEvent -> {
            Integer stringId = integerEvent.getContentIfNotHandled();
            if (stringId != null) {
                Snackbar.make(coordinator,
                        stringId, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    /*   @Override
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
                intento1.putExtra("filtrarlista.fragment", "buscarinforme");
                startActivityForResult(intento1, FiltrarListaActivity.REQUEST_CODE_INFORME);

                return true;

            default:
                break;
        }

        return false;
    }

    @Override
    public void onClickVer(int informe) {
        Intent intento1 = new Intent(getActivity(), BackActivity.class);
        intento1.putExtra(NuevoinformeFragment.INFORMESEL, informe);
        intento1.putExtra(BackActivity.ARG_FRAGMENT, BackActivity.OP_INFORME);
        startActivity(intento1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FiltrarListaActivity.REQUEST_CODE_INFORME && resultCode == 1) {
            ciudad = data.getStringExtra(BuscarInformeFragment.CIUDAD);
            tienda = data.getStringExtra(BuscarInformeFragment.NOMBRETIENDA);
            indice = data.getStringExtra(BuscarInformeFragment.INDICE);
            cargarLista();
        }
    }


    @Override
    public void onClickCancelar(int idinforme) {
        try {
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
            dialogo1.setTitle(R.string.importante);
            dialogo1.setMessage(R.string.pregunta_eliminar_mensaje);
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    //busco la imagen a eliminar

                    mViewModel.cancelarInforme(idinforme);
                    mListAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "Se eliminó el registro", Toast.LENGTH_SHORT).show();

                }
            });
            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    dialogo1.cancel();
                }
            });
            dialogo1.show();

        } catch (Exception ex) {
            Log.e(TAG, "Error" + ex.getMessage());
            Toast.makeText(getActivity(), "Hubo un error al eliminar", Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public void onClickEditar(int id) {
   /*     Bundle bundle = new Bundle();
        bundle.putInt(NuevoinformeFragment.INFORMESEL,id );
         bundle.putInt(NuevoinformeFragment.ISEDIT, 1);
           NavHostFragment.findNavController(this).navigate(R.id.nav_continuar,bundle);*/
        // Intent intent=new Intent(this,BackActivity.class);

    }

    @Override
    public void onClickSubir(int informe) {
        if (NavigationDrawerActivity.isOnlineNet(getContext())) {
            NuevoinformeViewModel niViewModel = new ViewModelProvider(this).get(NuevoinformeViewModel.class);

            InformeEnvio informeenv = niViewModel.preparaInforme(informe);
            //todo cambio estatus a enviando
            //mViewModel.actualizarEstatusSyncInf(informe,Constantes.ENVIANDO);
            Constantes.SINCRONIZANDO=1;
            mListAdapter.notifyDataSetChanged();
            SubirInformeTask miTareaAsincrona = new SubirInformeTask(true, informeenv, getActivity(), niViewModel);
            miTareaAsincrona.execute();
            Log.d(TAG, "preparando informe**********");
            // NuevoinformeFragment.subirFotosFila(getActivity(), informeenv);
            NuevoinformeFragment.subirFotos(getActivity(), informeenv);
        } else
            Toast.makeText(getActivity(), getString(R.string.sin_conexion), Toast.LENGTH_LONG).show();

    }



}