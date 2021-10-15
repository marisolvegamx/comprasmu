package com.example.comprasmu.ui.mantenimiento;

import android.app.Application;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;

import java.util.List;

public class CiudadTrabajoViewModel extends AndroidViewModel {
    ListaCompraRepositoryImpl lcrepo;


    public CiudadTrabajoViewModel(@NonNull Application application) {
        super(application);
        lcrepo=new ListaCompraRepositoryImpl();

    }

    public LiveData<List<ListaCompra>> getCiudades(String indiceactual) {
        return lcrepo.getCiudades(indiceactual);
    }


}