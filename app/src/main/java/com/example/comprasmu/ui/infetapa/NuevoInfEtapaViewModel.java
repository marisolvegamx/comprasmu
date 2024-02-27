package com.example.comprasmu.ui.infetapa;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.comprasmu.data.modelos.InformeEtapaDet;

import com.example.comprasmu.data.repositories.InfEtapaDetRepoImpl;


public class NuevoInfEtapaViewModel extends AndroidViewModel {

    public int cont;
    private final InfEtapaDetRepoImpl infDetRepo;

    public NuevoInfEtapaViewModel(@NonNull Application application) {
        super(application);
        infDetRepo=new InfEtapaDetRepoImpl(application);
    }
    //busco el ultimo detalle informe
    public InformeEtapaDet getDetalleEtEdit(int idinf, int etapa){

        return infDetRepo.getUltimonocan(idinf,etapa);
    }


}