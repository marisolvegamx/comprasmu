package com.example.comprasmu.data.modelos;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;


public class InfEtapaWithDetalle {

    @Embedded
    public InformeEtapa informeEtapa;
    @Relation(
            parentColumn = "id",
            entityColumn = "informeEtapaId"
    )

    public List<InformeEtapaDet> infEtapaDet;

}
