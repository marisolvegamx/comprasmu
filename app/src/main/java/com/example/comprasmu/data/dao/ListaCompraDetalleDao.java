package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.sqlite.db.SupportSQLiteQuery;
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

    @Query("SELECT * FROM lista_compras_detalle")
    public abstract List<ListaCompraDetalle> findAllSimpl();

    @Query("SELECT * FROM lista_compras_detalle where id=:id and listaId=:listaid")
    public abstract LiveData<ListaCompraDetalle> find( int listaid, int id);
    @Query("SELECT * FROM lista_compras_detalle where id=:id and listaId=:listaid")
    public abstract ListaCompraDetalle findsimple( int listaid, int id );

    @Query("delete FROM lista_compras_detalle WHERE listaId = :id")
    public abstract void deleteByLista(int id);
    @Query("Select lista_compras_detalle.* from lista_compras_detalle" +
            "  inner join lista_compras on listaId=lista_compras.id" +
            "   where productosId=:prodid and empaquesId=:empid and tamanioId=:tamid and analisisId=:anaid and indice=:indice and plantasId=:planta")
    public abstract ListaCompraDetalle getByProductoAna(int prodid,int empid,int tamid,int anaid, String indice, int planta);

   /* @Query("Select  lis.id, listaId ,lis.productosId ,lis.productoNombre," +
            "     lis.tamanio ,lis.tamanioId," +

            "     lis.empaque," +
            "     lis.empaquesId," +
            "    lis.tipoAnalisis," +
            "     lis.analisisId," +
            "     lis.cantidad," +
            "    lis.codigosNoPermitidos," +
            "    com.caducidad as nvoCodigo," +
            "     lis.estatus," +
            "     lis.comprados," +
            "     lis.tipoMuestra," +
            "    lis.nombreTipoMuestra," +
            "     lis.categoriaid," +
            "    lis.categoria," +
            "    lis.lid_fechapermitida," +
            "    lid_fecharestringida," +
            "     lid_orden," +

            "     ordtam," +

            "     ordemp," +

            "     ordtipa," +

            "     ordtipm," +


            "     lid_backup from lista_compras_detalle lis" +
            "   inner join informe_detalle com on lis.productosId=com.productoId and lis.analisisId=com.tipoAnalisis" +
            " and lis.tamanioId=com.tamanioId and lis.empaquesId=com.empaquesId" +
            "    where listaId=:listaid " +
            " order by lid_orden ASC, ordtam DESC, ordemp ASC, ordtipa ASC, ordtipm")
    public abstract LiveData<List<ListaCompraDetalle>> getListasDetalleOrdByLista(int listaid);
*/

    @Query("Select   lista_compras_detalle.* from lista_compras_detalle " +
            "   where listaId=:listaid " +
            " order by lid_orden ASC, ordtam DESC, ordemp ASC, ordtipa ASC, ordtipm")
    public abstract LiveData<List<ListaCompraDetalle>> getListasDetalleOrdByLista(int listaid);

    @Query("update lista_compras_detalle set comprados=:cantidad WHERE id = :id and listaId=:listaid")
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

  /*  @Transaction
    public void insertActCantidades(InformeCompraDetDao infcomdao,List<InformeCompraDetalle> detalles, Product oldProduct) {
        // Anything inside this method runs in a single transaction.


        infcomdao.insertAll(detalles);
        //sumo en la lista de compra
        List<InformeCompraDetalle> detalles=infdrepo.getAllsimple();
        //ajusto cantidades
        if(detalles!=null)
            for(InformeCompraDetalle det:detalles){
                ListaCompraDetalle compradet=lcrepo.findsimple(det.getComprasId(),det.getId());
                Log.d(TAG,"sss"+compradet.getProductoNombre()+"--"+compradet.getComprados());
                if(compradet!=null)
                { int nvacant=compradet.getComprados()+1;
                    lcrepo.actualizarComprados(det.getId(),det.getComprasId(),nvacant);
                    Log.d(TAG,"sss"+compradet.getProductoNombre()+"--"+nvacant);

                }

            }
    }*/
    @RawQuery(observedEntities = ListaCompraDetalle.class)
    public abstract LiveData<List<ListaCompraDetalle>> getDetallesByFiltros(SupportSQLiteQuery query);

    @Query("update lista_compras_detalle set nvoCodigo=:nvocodigo WHERE id = :id and listaId=:listaid")
    public abstract void actualizarNvosCodigos(int id, int listaid, String nvocodigo) ;
    @Query("SELECT * FROM lista_compras_detalle WHERE listaId =:uuid and comprados<cantidad")
    public abstract List<ListaCompraDetalle> getPendientes(int uuid);

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
