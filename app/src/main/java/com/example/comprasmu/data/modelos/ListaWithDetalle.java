package com.example.comprasmu.data.modelos;

import androidx.room.Embedded;

import androidx.room.Relation;

import java.util.List;


public class ListaWithDetalle {

    @Embedded
    public ListaCompra user;
    @Relation(
            parentColumn = "id",
            entityColumn = "listaId"
    )
    public List<ListaCompraDetalle> listaDetalle;

}
