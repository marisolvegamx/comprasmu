package com.example.comprasmu.data.modelos;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class VisitaWithInformes {
    @Embedded
    public Visita visita;
    @Relation(
            parentColumn = "id",
            entityColumn = "visitasId"
    )
    public List<InformeCompra> informes;

}
