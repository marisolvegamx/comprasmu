package com.example.comprasmu.data.modelos;

import androidx.room.Embedded;
import androidx.room.Relation;


public class InformeEnvioPaq {

    @Embedded
    public InformeEtapa informeEtapa;
    @Relation(
            parentColumn = "id",
            entityColumn = "informeEtapaId"
    )

    public InformeEnvioDet infEnvioDet;

}
