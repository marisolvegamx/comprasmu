package com.example.comprasmu.data.modelos;

public  class Contrato {

    public static  class Informe {
       public static int ESTATUS_ABIERTO = 1;
        public static int ESTATUS_CANCELADO = 0;
        public static int ESTATUS_FINALIZADO = 2;
    }

        public static int ESTATUSSYNC_PENDIENTE = 0;
        public static int ESTATUSSYNC_SUBIENDO = 1;
        public static int ESTATUSSYNC_ACTUALIZADO = 2;

    interface  CatalogosId{
        int ATRIBUTOS=1;
        int TOMADOSDE=2;
    }
    interface ColumnasInformeDetalle{

        String ID = "id"; // Pk
        String PLANTASID = "plantasId";
        String PLANTANOMBRE = "plantaNombre";
        String CLIENTESID = "clientesId";
        String CLIENTENOMBRE = "clienteNombre";
        String INFORMESID = "informesId";
        String TAMANIO = "tamanio";
        String EMPAQUE = "empaque";
        String CODIGO = "codigo";
        String CADUCIDAD = "caducidad";
        String BACKUP = "backup";
        String ORIGEN = "origen";
        String COSTO = "costo";
        String FOTOCODIGOPRODUCCION = "foto_codigo_produccion";
        String ENERGIA = "energia";
        String PRODUCTOEXHIBIDO = "producto_exhibido";
        String FOTONUMTIENDA = "foto_num_tienda";
        String MARCA_TRASLAPE = "marca_traslape";
        String ATRIBUTOA = "atributoa";
        String FOTO_ATRIBUTOA = "foto_atributoa";
        String ATRIBUTOB = "atributob";

        String FOTO_ATRIBUTOB = "foto_atributob";
        String ATRIBUTOC = "atributoc";
        String FOTO_ATRIBUTOC = "foto_atributoc";
        String ETIQUETA_EVALUACION = "etiqueta_evaluacion";
        String SEGUNDAMUESTRA = "segundaMuestra";
        String TICKET_COMPRA = "ticket_compra";
        String CONDICIONES_TRASLADO = "condiciones_traslado";
        String COMENTARIOS = "comentarios";

    }
    public static class TablaInformeDet
            implements  ColumnasInformeDetalle {

    }
}
