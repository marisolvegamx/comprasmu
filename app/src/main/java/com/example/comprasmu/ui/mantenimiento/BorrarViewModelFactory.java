package com.example.comprasmu.ui.mantenimiento;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.comprasmu.data.dao.ConfiguracionRepositoryImpl;
import com.example.comprasmu.data.repositories.AcuseReciboRepositoryImpl;
import com.example.comprasmu.data.repositories.CorEtiqCajaDetRepoImpl;
import com.example.comprasmu.data.repositories.CorEtiqCajaRepoImpl;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.TiendaEstatusClienteRepositoryImpl;
import com.example.comprasmu.data.repositories.TiendaRepositoryImpl;

public class BorrarViewModelFactory  implements ViewModelProvider.Factory {
    private final AcuseReciboRepositoryImpl acuseRepo;
    private final ListaCompraDetRepositoryImpl lcdrepo;
    private final Application context;
    private final ConfiguracionRepositoryImpl configuracionRepo;
    private final CorEtiqCajaRepoImpl correccionEtiqRepo;
    private final CorEtiqCajaDetRepoImpl corEtiqCajaDetRepo;
    private final TiendaRepositoryImpl tiendaRepository;
    private final TiendaEstatusClienteRepositoryImpl tiendaEstatusclienteRepository;
    public BorrarViewModelFactory(AcuseReciboRepositoryImpl acuseRepo, Application context, ListaCompraDetRepositoryImpl lcdrepo,
                                  ConfiguracionRepositoryImpl configuracionRepo, CorEtiqCajaRepoImpl correccionEtiqRepo,
                                  CorEtiqCajaDetRepoImpl corEtiqCajaDetRepo, TiendaRepositoryImpl tiendaRepository,
                                  TiendaEstatusClienteRepositoryImpl tiendaEstatusclienteRepository) {
        this.acuseRepo = acuseRepo;
        this.lcdrepo = lcdrepo;
        this.context =context ;
        this.configuracionRepo = configuracionRepo;
        this.corEtiqCajaDetRepo = corEtiqCajaDetRepo;
        this.correccionEtiqRepo=correccionEtiqRepo;
        this.tiendaRepository = tiendaRepository;
        this.tiendaEstatusclienteRepository = tiendaEstatusclienteRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new BorrarDatosViewModel(context, lcdrepo,acuseRepo,configuracionRepo, correccionEtiqRepo, corEtiqCajaDetRepo, tiendaRepository,tiendaEstatusclienteRepository);
    }
}
