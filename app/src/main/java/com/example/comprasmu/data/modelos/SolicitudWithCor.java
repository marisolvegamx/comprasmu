package com.example.comprasmu.data.modelos;

import androidx.room.Embedded;
import androidx.room.Relation;

public class SolicitudWithCor {

    @Embedded
    public Correccion correccion;
    @Relation(
            parentColumn = "solicitudId",
            entityColumn = "id"
    )

    public SolicitudCor solicitud;

}
