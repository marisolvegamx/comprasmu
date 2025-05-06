package com.example.comprasmu.ui.preparacion;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.comprasmu.data.dao.ConfiguracionRepositoryImpl;
import com.example.comprasmu.data.repositories.AcuseReciboRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.ui.mantenimiento.BorrarDatosViewModel;

public class NvaPreparacionViewModelFactory    implements androidx.lifecycle.ViewModelProvider.Factory {
    ListaCompraRepositoryImpl listaCompraRepo;
    Application application;
    public NvaPreparacionViewModelFactory(Application application,ListaCompraRepositoryImpl listaCompraRepo, Application context, ListaCompraDetRepositoryImpl lcdrepo,ConfiguracionRepositoryImpl configuracionRepo) {
        this.application=application;
        this.listaCompraRepo = listaCompraRepo;

    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //return (T)new NvaPreparacionViewModel(application, listaCompraRepo);
        return (T)new NvaPreparacionViewModel(application);
         }
}