package com.example.comprasmu.data.remote;

import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.TablaVersiones;

import java.util.ArrayList;
import java.util.List;

public class ListaCompraResponse {

    private List<TablaVersiones> versiones;
    private String status;
    private String data;
    private Componente inserts;
    private Componente updates;


   // public List<TablaVersiones> getVersiones() {
      //  return versiones;
    //}

    //public void setVersiones(ArrayList<TablaVersiones> versiones) {
      //  this.versiones = versiones;
    //}


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Componente getInserts() {
        return inserts;
    }

    public void setInserts(Componente inserts) {
        this.inserts = inserts;
    }

    public Componente getUpdates() {
        return updates;
    }

    public void setUpdates(Componente updates) {
        this.updates = updates;
    }

    public class Componente{
        private List<ListaCompra> ListaCompra;
        private List<ListaCompraDetalle> ListaCompraDetalle;

        public List<com.example.comprasmu.data.modelos.ListaCompra> getListaCompra() {
            return ListaCompra;
        }

        public void setListaCompra(List<com.example.comprasmu.data.modelos.ListaCompra> listaCompra) {
            ListaCompra = listaCompra;
        }

        public List<com.example.comprasmu.data.modelos.ListaCompraDetalle> getListaCompraDetalle() {
            return ListaCompraDetalle;
        }

        public void setListaCompraDetalle(List<com.example.comprasmu.data.modelos.ListaCompraDetalle> listaCompraDetalle) {
            ListaCompraDetalle = listaCompraDetalle;
        }
    }
}
