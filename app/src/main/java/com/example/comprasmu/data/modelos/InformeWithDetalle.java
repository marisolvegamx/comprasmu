package com.example.comprasmu.data.modelos;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;


public class InformeWithDetalle {

    @Embedded
    public InformeCompra informe;
    @Relation(
            parentColumn = "id",
            entityColumn = "informesId"
    )

    public List<InformeCompraDetalle> informeDetalle;

}
