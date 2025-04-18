package com.example.comprasmu.ui.mantenimiento;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.comprasmu.data.dao.ConfiguracionRepositoryImpl;
import com.example.comprasmu.data.repositories.AcuseReciboRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;

public class BorrarViewModelFactory  implements ViewModelProvider.Factory {
    private final AcuseReciboRepositoryImpl acuseRepo;
    private final ListaCompraDetRepositoryImpl lcdrepo;
    private final Application context;
    private final ConfiguracionRepositoryImpl configuracionRepo;

    public BorrarViewModelFactory(AcuseReciboRepositoryImpl acuseRepo, Application context, ListaCompraDetRepositoryImpl lcdrepo,ConfiguracionRepositoryImpl configuracionRepo) {
        this.acuseRepo = acuseRepo;
        this.lcdrepo = lcdrepo;
        this.context =context ;
        this.configuracionRepo = configuracionRepo;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new BorrarDatosViewModel(context, lcdrepo,acuseRepo,configuracionRepo);
    }
}
