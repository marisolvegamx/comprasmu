package com.example.comprasmu.data.modelos;

public  class Contrato {

  public static String TBLLISTACOMPRAS="lista_compras";
    public static String TBLLISTACOMPRASDET="lista_compras_detalle";
    public static String TBLSOLCORRECCIONES="solicitud_cor";
    public static String TBLINFORMESCOMP="informe_compras";
    public static String TBLINFORMESDET="informe_detalle";
    public static String TBLVISITAS="visitas";

    public static class  CatalogosId{
        public static final int CADENACOMER =1 ;
        public static final int CAUSAS =100 ;
        public static int TOMADOSDE=8;
        public static int TIPODEMUESTRA=12;
        public static int TIPODETIENDA=2;
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
        String ATRIBUTOD = "atributod";
        String ETIQUETA_EVALUACION = "etiqueta_evaluacion";
        String SEGUNDAMUESTRA = "segundaMuestra";
        String TICKET_COMPRA = "ticket_compra";
        String CONDICIONES_TRASLADO = "condiciones_traslado";
        String COMENTARIOS = "comentarios";
        String AZUCARES = "azucares";
        String SIGLAS = "siglas";
        String causa_nocompra = "causa_nocompra";


        String QR = "qr";
    }
    public static class TablaInformeDet
            implements  ColumnasInformeDetalle {


    }
}
