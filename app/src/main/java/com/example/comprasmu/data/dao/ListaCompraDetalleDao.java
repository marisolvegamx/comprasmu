package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;

import java.util.List;

@Dao
public abstract class ListaCompraDetalleDao extends BaseDao<ListaCompraDetalle> {

    @Query("SELECT * FROM lista_compras_detalle WHERE listaId =:uuid")
     public abstract LiveData<List<ListaCompraDetalle>> getListaDetallesByLista(int uuid);

    @Query("SELECT * FROM lista_compras_detalle WHERE listaId =:uuid")
    public abstract List<ListaCompraDetalle> getListaDetallesByListasimple(int uuid);
    @Query("SELECT * FROM lista_compras_detalle")
    public abstract LiveData<List<ListaCompraDetalle>> findAll();

    @Query("SELECT * FROM lista_compras_detalle where id=:id and listaId=:listaid")
    public abstract LiveData<ListaCompraDetalle> find( int listaid, int id);
    @Query("SELECT * FROM lista_compras_detalle where id=:id and listaId=:listaid")
    public abstract ListaCompraDetalle findsimple( int listaid, int id );

    @Query("delete FROM lista_compras_detalle WHERE listaId = :id")
    public abstract void deleteByLista(int id);

    @Query("update lista_compras_detalle set comprados=comprados+:cantidad WHERE id = :id and listaId=:listaid")
    public abstract void actualizarCompra(int id,int listaid,int cantidad);

    @Query("UPDATE lista_compras_detalle SET productosId = :productosId, productoNombre = :productoNombre,tamanio=:tamanio, empaque=:empaque, " +
            "empaquesId=:empaquesId, tipoAnalisis=:tipoAnalisis, analisisId=:analisisId,cantidad=:cantidad,tipoMuestra=:tipoMuestra,nombreTipoMuestra=:nombreTipoMuestra," +
            "categoriaid=:categoriaid,categoria=:categoria, codigosNoPermitidos=:codigos WHERE id =:id and listaId=:listaId")
    abstract void actualizarLista(int productosId, String productoNombre, String tamanio, String empaque,
                                  int empaquesId, String tipoAnalisis,int analisisId, int cantidad, int tipoMuestra,
    String nombreTipoMuestra,int categoriaid, String categoria,String codigos, int id, int listaId);

    @Transaction
    public void updateAll(List<ListaCompraDetalle> detalles) {
        // Anything inside this method runs in a single transaction.
        for(ListaCompraDetalle det:detalles){
            actualizarLista(det.getProductosId(), det.getProductoNombre(), det.getTamanio(), det.getEmpaque(),
                    det.getEmpaquesId(), det.getTipoAnalisis(),det.getAnalisisId(), det.getCantidad(), det.getTipoMuestra(),
            det.getNombreTipoMuestra(),det.getCategoriaid(),det.getCategoria(),det.getCodigosNoPermitidos(), det.getId(), det.getListaId());
        }

    }
    @RawQuery(observedEntities = ListaCompraDetalle.class)
    public abstract LiveData<List<ListaCompraDetalle>> getDetallesByFiltros(SupportSQLiteQuery query);
  /*  public class MinimalLstaDet{
        public int productosId;
        public String productoNombre;
        public String tamanio;
        public String empaque;
        public int empaquesId;
        public int tipoAnalisis;
        public int analisisId;
        public int cantidad;
        public int tipoMuestra;
        public String nombreTipoMuestra;
        public int categoriaid;
        public String categoria;
        public String codigos;
        public int id;
        public int listaId;


    }*/
}
