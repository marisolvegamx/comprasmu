package com.example.comprasmu.ui.tiendas;


import androidx.fragment.app.Fragment;

public class BuscarTiendaFragment extends Fragment {
   /* private BuscarTiendaViewModel mViewModel;
    private BuscarTiendaFragmentBinding mBinding;
    private DescripcionGenericaAdapter mListAdapter;
    private RecyclerView mRecyclerView;
    private Button btnbuscar;
    private View root;


    public static BuscarTiendaFragment newInstance() {
        return new BuscarTiendaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding= DataBindingUtil.inflate(inflater,
                R.layout.buscar_tienda_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(BuscarTiendaViewModel.class);
        mBinding.setViewModel(mViewModel);
        mBinding.setLifecycleOwner(this);

        mRecyclerView=mBinding.btrvtiendas;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        root=mBinding.getRoot();
        initUI();

        return  root ;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BuscarTiendaViewModel.class);
        //reviso que haya internet sino no avanzo
        if(!NavigationDrawerActivity.isOnlineNet()){
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
            dialogo1.setTitle(R.string.importante);
            dialogo1.setMessage(R.string.no_hay_conexion);
            dialogo1.setCancelable(false);

            dialogo1.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    dialogo1.cancel();
                }
            });
            dialogo1.show();
        }
    }

    public void initUI(){
        btnbuscar=root.findViewById(R.id.btbtnbuscar);
        btnbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               getTiendas();
            }
        });
        setupListAdapter();

    }

    private void getTiendas(){
        CatalogoDetalle catsel=(CatalogoDetalle)mBinding.btsptipo.getSelectedItem();
        String tipo=catsel!=null?catsel.getCad_descripcionesp():"";
        mViewModel.getTiendas(mBinding.btspciudad.getText().toString(),mBinding.btetnombre.getText().toString(),tipo,getViewLifecycleOwner());
        mViewModel.getListadescripcion().observe(getViewLifecycleOwner(), new Observer<List<DescripcionGenerica>>() {
            @Override
            public void onChanged(List<DescripcionGenerica> descripcionGenericas) {
                if(descripcionGenericas.size()>0){
                    Log.d("BuscarTiendaFragment","llegué aquí"+ descripcionGenericas.get(0).getNombre());

                    mBinding.txtbtsindatos.setText("");
                    mBinding.txtbtsindatos.setVisibility(View.GONE);
                }
                else{
                    mBinding.txtbtsindatos.setText("No se encontraron datos con esa busqueda");
                    mBinding.txtbtsindatos.setVisibility(View.VISIBLE);
                }

                mListAdapter.setmDescripcionGenericaList(descripcionGenericas);
                mListAdapter.notifyDataSetChanged();
            }
        });


    }
    private void setupListAdapter() {


        mListAdapter = new DescripcionGenericaAdapter(this);

        mBinding.btrvtiendas.setAdapter(mListAdapter);

    }

    @Override
    public void onClickCallback(String codigos) {

    }*/
}