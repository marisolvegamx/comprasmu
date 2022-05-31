package com.example.comprasmu.data;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.comprasmu.R;
import com.example.comprasmu.data.dao.AtributoDao;
import com.example.comprasmu.data.dao.BaseDao;
import com.example.comprasmu.data.dao.CatalogoDetalleDao;
import com.example.comprasmu.data.dao.GeocercaDao;
import com.example.comprasmu.data.dao.ImagenDetalleDao;
import com.example.comprasmu.data.dao.InformeCompraDao;
import com.example.comprasmu.data.dao.InformeCompraDetDao;
import com.example.comprasmu.data.dao.InformeTempDao;
import com.example.comprasmu.data.dao.ListaCompraDao;
import com.example.comprasmu.data.dao.ListaCompraDetalleDao;
import com.example.comprasmu.data.dao.ProductoExhibidoDao;
import com.example.comprasmu.data.dao.ReactivoDao;
import com.example.comprasmu.data.dao.SustitucionDao;
import com.example.comprasmu.data.dao.TablaVersionesDao;
import com.example.comprasmu.data.dao.VisitaDao;
import com.example.comprasmu.data.modelos.Atributo;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.Contrato;
import com.example.comprasmu.data.modelos.Geocerca;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeTemp;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.ListaWithDetalle;
import com.example.comprasmu.data.modelos.ProductoExhibido;
import com.example.comprasmu.data.modelos.Reactivo;
import com.example.comprasmu.data.modelos.Sustitucion;
import com.example.comprasmu.data.modelos.TablaVersiones;
import com.example.comprasmu.data.modelos.Visita;

import com.example.comprasmu.data.repositories.ReactivoRepositoryImpl;
import com.example.comprasmu.utils.CreadorFormulario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Database(entities={ImagenDetalle.class,
        InformeCompra.class,
        InformeCompraDetalle.class,
        ListaCompra.class,
        ListaCompraDetalle.class,
        Reactivo.class,
        TablaVersiones.class,
        Visita.class,  InformeTemp.class,
        ProductoExhibido.class, Sustitucion.class,
        CatalogoDetalle.class, Atributo.class, Geocerca.class},

        views = {InformeCompraDao.InformeCompravisita.class, ProductoExhibidoDao.ProductoExhibidoFoto.class}, version=9, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class ComprasDataBase extends RoomDatabase {
    private static ComprasDataBase INSTANCE;
    static Context ctx;
    public abstract ListaCompraDetalleDao getListaCompraDetalleDao();
    public abstract ListaCompraDao getListaCompraDao();
    public abstract TablaVersionesDao getTableVersionesDao();
    public abstract ImagenDetalleDao getImagenDetalleDao();
    public abstract VisitaDao getVisitaDao();
    public abstract InformeCompraDetDao getInformeCompraDetDao();
    public abstract ReactivoDao getReactivoDao();
    public abstract CatalogoDetalleDao getCatalogoDao();
    public abstract ProductoExhibidoDao getProductoExhibidoDao();
    public abstract AtributoDao getAtributoDao();
    public abstract InformeCompraDao getInformeCompraDao();
    public abstract InformeTempDao getInformeTempDao();
    public abstract SustitucionDao getSustitucionDao();
    public abstract GeocercaDao getGeocercaDao();
    public static ComprasDataBase getInstance(final Context context) {
        if (INSTANCE == null) {
            ctx=context;
            synchronized (ComprasDataBase.class) {
                if (INSTANCE == null) {
                 /*   INSTANCE =  Room.databaseBuilder(context,
                            ReportesDatabase.class, "muestreo").allowMainThreadQueries()
                            .addMigrations(MIGRATION_1_2)
                            .build();*/
                    INSTANCE =  Room.databaseBuilder(context,
                            ComprasDataBase.class, "compras_data").allowMainThreadQueries()
                            .addMigrations(MIGRATION_1_2,MIGRATION_2_3,MIGRATION_3_4,MIGRATION_4_5, MIGRATION_5_6,MIGRATION_6_7,MIGRATION_7_8,MIGRATION_8_9)
                            .build();
                    INSTANCE.cargandodatos();
                }
            }
        }
        return INSTANCE;
    }
   /* static RoomDatabase.Callback rdc = new RoomDatabase.Callback() {
        public void onCreate (SupportSQLiteDatabase db) {
            // do something after database has been created
            prepopulatelc(db);
            prepopulatedetc(INSTANCE);

        }
        public void onOpen (SupportSQLiteDatabase db) {
            // do something every time database is open
        }
    };*/

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `sustitucion` (`id_sustitucion` INTEGER NOT NULL, su_tipoempaque INTEGER NOT NULL, "
                    + "`nomempaque` TEXT, su_producto INTEGER NOT NULL, su_tamanio INTEGER NOT NULL, nomproducto TEXT, nomtamanio TEXT," +
                    "categoriasId INTEGER NOT NULL,nomcategoria TEXT,PRIMARY KEY(`id_sustitucion`));");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2,3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE informe_detalle ADD COLUMN comprasIdbu INTEGER; " );
            database.execSQL( "ALTER TABLE informe_detalle ADD COLUMN comprasDetIdbu INTEGER;");

        }
    };
    static final Migration MIGRATION_3_4 = new Migration(3,4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE tabla_versiones ADD COLUMN indice TEXT; " );

        }
    };

    static final Migration MIGRATION_4_5 = new Migration(4,5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE sustitucion ADD COLUMN clientesId INTEGER DEFAULT 0 NOT NULL; " );

            database.execSQL(
                    "ALTER TABLE lista_compras_detalle ADD COLUMN lid_orden INTEGER DEFAULT 0 NOT NULL; " );


        }
    };

    static final Migration MIGRATION_5_6 = new Migration(5,6) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "DELETE FROM informe_temp; " );

            database.execSQL(
                    "DELETE FROM lista_compras_detalle " );
            database.execSQL(
                    "DELETE FROM lista_compras " );


            database.execSQL(
                    "ALTER TABLE informe_temp ADD COLUMN clienteSel INTEGER DEFAULT 0 NOT NULL; " );
            database.execSQL(
                    "ALTER TABLE reactivos  ADD COLUMN clienteSel INTEGER DEFAULT 0 NOT NULL; " );


            database.execSQL(
                    "ALTER TABLE lista_compras_detalle ADD COLUMN lid_backup INTEGER DEFAULT -1 NOT NULL; " );


        }
    };
    static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `geocerca` (`geo_id` INTEGER NOT NULL," +
                    " geo_n4id INTEGER NOT NULL, "
                    + "`geo_region` INTEGER NOT NULL ,  geo_p1 TEXT,geo_p2 TEXT, geo_p3 TEXT, geo_p4 TEXT, ciudad TEXT," +
                    " PRIMARY KEY(`geo_id`));");

             }
    };

    static final Migration MIGRATION_7_8 = new Migration(7,8) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
              database.execSQL("ALTER TABLE lista_compras_detalle ADD COLUMN ordtam INTEGER DEFAULT 0 NOT NULL  ");
            database.execSQL("ALTER TABLE lista_compras_detalle ADD COLUMN ordemp INTEGER DEFAULT 0  NOT NULL");

            database.execSQL("ALTER TABLE lista_compras_detalle ADD COLUMN ordtipa INTEGER DEFAULT 0 NOT NULL ");
            database.execSQL("ALTER TABLE lista_compras_detalle ADD COLUMN ordtipm INTEGER DEFAULT 0 NOT NULL ");
            database.execSQL("ALTER TABLE visita ADD COLUMN estatusPepsi INTEGER DEFAULT 0 NOT NULL ");

        }
    };

    static final Migration MIGRATION_8_9 = new Migration(8,9) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE informe_detalle ADD COLUMN siglas TEXT  ");

        }
    };



    private void cargandodatos(){

        runInTransaction(new Runnable() {
            @Override
            public void run() {
                ReactivoDao dao = getReactivoDao();
                List<Reactivo> myProducts=dao.findAllsimple();
                    if (myProducts == null||myProducts.size()==0) {
                        //no tengo datos


                        prepopulatelc();
                        prepopulatedetc();
                        prepopulateder();

                       // catalogos();

                    }
                List<Reactivo> myProductsP=   dao.findByCliente(5);
                if (myProductsP == null||myProductsP.size()==0) {
                    //no tengo datos

                    prepopulatederpeni();
                    prepopulatederele();
                }

            }
        });
    }

    private  void prepopulatelc(  ) {
        ListaCompra lc1=new ListaCompra();
        ListaCompraDao dao=getListaCompraDao();

      /*  String clientenom="PEPSI";
       // clientes.put(2,"PEñAFIEL");
        String planta="ACAPULCO";
        String ciudad2="ZACATECAS";
        String planta2="CALERA";
        int idplanta=4, idciudad=12, i;
        String ciudad="ACAPULCO";
        String siglas="ACA";
        String siglas2="ZAC";
        String indice="noviembre 2021";
        lc1.setClientesId(1);
        lc1.setPlantasId(idplanta);
        lc1.setCiudadesId(idciudad);
        lc1.setCiudadNombre(ciudad);
        lc1.setClienteNombre(clientenom);
        lc1.setPlantaNombre(planta);
        lc1.setSiglas(siglas);
        lc1.setIndice(indice);
        lc1.setEstatus(1);
        dao.insert(lc1);

        ListaCompra lc2=new ListaCompra();
        lc2.setClientesId(1);
        lc2.setPlantasId(20);
        int idciudad2=22;
        lc2.setClienteNombre(clientenom);
        lc2.setPlantaNombre(planta2);
        lc2.setSiglas(siglas2);
        lc2.setIndice("noviembre 2021");
        lc2.setEstatus(1);
        lc2.setCiudadesId(22);
        lc2.setCiudadNombre(ciudad2);
        dao.insert(lc2);*/
   /*     lc=new ListaCompra();
        lc.setClientesId(2);
        lc.setPlantasId(3);
        lc.setClienteNombre("peñafiel");
        lc.setPlantaNombre("juarez");
        lc.setSiglas("pj");
        lc.setIndice("noviembre 2021");
        lc.setCiudadesId(1);
        lc.setCiudadNombre("Cd Juarez");
        lc.setEstatus(1);
        dao.insert(lc);
        lc=new ListaCompra();
        lc.setClientesId(1);
        lc.setPlantasId(4);
        lc.setClienteNombre("pepsi");
        lc.setPlantaNombre("monterrey");
        lc.setSiglas("pm");
        lc.setIndice("noviembre 2021");
        lc.setCiudadesId(3);
        lc.setCiudadNombre("Nuevo leon");
        lc.setEstatus(1);
        dao.insert(lc);*/
        ListaCompra lc=new ListaCompra();
        lc.setClientesId(2);
        lc.setPlantasId(5);
        lc.setClienteNombre("PEñAFIEL");
        lc.setPlantaNombre("monterrey");
        lc.setSiglas("ppm");
        lc.setIndice("12.2021");
        lc.setCiudadesId(3);
        lc.setCiudadNombre("Nuevo leon");
        lc.setEstatus(1);
        dao.insert(lc);
      /*  lc=new ListaCompra();
        lc.setClientesId(1);
        lc.setPlantasId(6);
        lc.setClienteNombre("pepsi");
        lc.setPlantaNombre("acapulco");
        lc.setSiglas("pa");
        lc.setCiudadesId(2);
        lc.setCiudadNombre(" cd. acapulco");
        lc.setEstatus(1);
        lc.setIndice("julio 2021");
        dao.insert(lc);*/
    }

    private  void prepopulatedetc( ) {
      /*  ListaCompraDetalle lcd=new ListaCompraDetalle();
        lcd.setListaId(1);
        lcd.setProductosId(1);
        lcd.setProductoNombre("pepsi");
        lcd.setEmpaquesId(1);
        lcd.setEmpaque("pet");
        lcd.setTamanio("200ml");
        lcd.setTipoAnalisis("sensorial");
        lcd.setAnalisisId(2);
        lcd.setTipoMuestra(1);
        lcd.setNombreTipoMuestra("Normal");
        lcd.setCantidad(2);
        lcd.setEstatus(1);
        lcd.setCodigosNoPermitidos("20-sep-2021;21-sep-2021;20-sep-2021;21-jun-2021;20-jun-2021;21-oct-2021;20-oct-2021;21-ago-2021;20-ago-2021;18-sep-2021;19-sep-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(1);
        lcd.setProductosId(1);
        lcd.setProductoNombre("pepsi");
        lcd.setEmpaquesId(1);
        lcd.setEmpaque("pet");
        lcd.setTamanio("2l");
        lcd.setTipoAnalisis("micro");
        lcd.setAnalisisId(4);
        lcd.setTipoMuestra(1);
        lcd.setNombreTipoMuestra("Normal");
        lcd.setCantidad(2);
        lcd.setEstatus(1);
        lcd.setCodigosNoPermitidos("20-sep-2021;21-sep-2021;20-sep-2021;21-jun-2021;20-jun-2021;21-oct-2021;20-oct-2021;21-ago-2021;20-ago-2021;18-sep-2021;19-sep-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(1);
        lcd.setProductosId(2);
        lcd.setProductoNombre("7UP");
        lcd.setEmpaquesId(1);
        lcd.setEmpaque("pet");
        lcd.setTamanio("200ml");
        lcd.setTipoAnalisis("fisico");
        lcd.setAnalisisId(1);
        lcd.setTipoMuestra(1);
        lcd.setNombreTipoMuestra("Normal");
        lcd.setCantidad(2);
        lcd.setEstatus(1);
        lcd.setCodigosNoPermitidos("20-sep-2021;21-sep-2021;20-sep-2021;21-jun-2021;20-jun-2021;21-oct-2021;20-oct-2021;21-ago-2021;20-ago-2021;18-sep-2021;19-sep-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(1);
        lcd.setProductosId(3);
        lcd.setProductoNombre("MANZANA");
        lcd.setEmpaquesId(3);
        lcd.setEmpaque("lata");
        lcd.setTamanio("200ml");
        lcd.setTipoAnalisis("torque");
        lcd.setAnalisisId(3);
        lcd.setTipoMuestra(1);
        lcd.setNombreTipoMuestra("Normal");
        lcd.setCantidad(1);
        lcd.setEstatus(1);
        lcd.setCodigosNoPermitidos("20-sep-2021;21-sep-2021;20-sep-2021;21-jun-2021;20-jun-2021;21-oct-2021;20-oct-2021;21-ago-2021;20-ago-2021;18-sep-2021;19-sep-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(1);
        lcd.setProductosId(1);
        lcd.setProductoNombre("PEPSI");
        lcd.setEmpaquesId(2);
        lcd.setEmpaque("lata");
        lcd.setTamanio("200ml");
        lcd.setTipoAnalisis("fisico");
        lcd.setAnalisisId(1);
        lcd.setCantidad(2);
        lcd.setEstatus(1);
        lcd.setTipoMuestra(1);
        lcd.setNombreTipoMuestra("Normal");
        lcd.setCodigosNoPermitidos("20-sep-2021;21-sep-2021;20-sep-2021;21-jun-2021;20-jun-2021;21-oct-2021;20-oct-2021;21-ago-2021;20-ago-2021;18-sep-2021;19-sep-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(1);
        lcd.setProductosId(2);
        lcd.setProductoNombre("NARANJA");
        lcd.setEmpaquesId(1);
        lcd.setEmpaque("pet");
        lcd.setTamanio("2l");
        lcd.setTipoAnalisis("torque");
        lcd.setAnalisisId(3);
        lcd.setCantidad(1);
        lcd.setTipoMuestra(1);
        lcd.setNombreTipoMuestra("Normal");
        lcd.setEstatus(1);
        lcd.setCodigosNoPermitidos("20-sep-2021;21-sep-2021;20-sep-2021;21-jun-2021;20-jun-2021;21-oct-2021;20-oct-2021;21-ago-2021;20-ago-2021;18-sep-2021;19-sep-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(2);
        lcd.setProductosId(1);
        lcd.setProductoNombre("PEPSI");
        lcd.setEmpaquesId(1);
        lcd.setEmpaque("pet");
        lcd.setTamanio("200ml");
        lcd.setTipoAnalisis("fisico");
        lcd.setAnalisisId(1);
        lcd.setCantidad(2);
        lcd.setEstatus(1);
        lcd.setTipoMuestra(1);
        lcd.setNombreTipoMuestra("Normal");
        lcd.setCodigosNoPermitidos("20-sep-2021;21-sep-2021;20-sep-2021;21-jun-2021;20-jun-2021;21-oct-2021;20-oct-2021;21-ago-2021;20-ago-2021;18-sep-2021;19-sep-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(2);
        lcd.setProductosId(1);
        lcd.setProductoNombre("PEPSI");
        lcd.setEmpaquesId(1);
        lcd.setEmpaque("pet");
        lcd.setTamanio("2l");
        lcd.setTipoAnalisis("fisico");
        lcd.setAnalisisId(1);
        lcd.setCantidad(2);
        lcd.setEstatus(1);
        lcd.setTipoMuestra(1);
        lcd.setNombreTipoMuestra("Normal");
        lcd.setCodigosNoPermitidos("20-sep-2021;21-sep-2021;20-sep-2021;21-jun-2021;20-jun-2021;21-oct-2021;20-oct-2021;21-ago-2021;20-ago-2021;18-sep-2021;19-sep-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);*/
        ListaCompraDetalle lcd=new ListaCompraDetalle();
        lcd.setListaId(1);
        lcd.setProductosId(2);
        lcd.setProductoNombre("NARANJA");
        lcd.setEmpaquesId(1);
        lcd.setEmpaque("pet");
        lcd.setTamanio("200ml");
        lcd.setTipoAnalisis("sensorial");
        lcd.setAnalisisId(2);
        lcd.setCantidad(2);
        lcd.setEstatus(1);
        lcd.setTipoMuestra(1);
        lcd.setNombreTipoMuestra("Normal");
        lcd.setCodigosNoPermitidos("20-sep-2021;21-sep-2021;20-sep-2021;21-jun-2021;20-jun-2021;21-oct-2021;20-oct-2021;21-ago-2021;20-ago-2021;18-sep-2021;19-sep-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(1);
        lcd.setProductosId(3);
        lcd.setProductoNombre("LIMON");
        lcd.setEmpaquesId(3);
        lcd.setEmpaque("lata");
        lcd.setTamanio("200ml");
        lcd.setTipoAnalisis("sensorial");
        lcd.setAnalisisId(2);
        lcd.setCantidad(1);
        lcd.setTipoMuestra(1);
        lcd.setNombreTipoMuestra("Normal");
        lcd.setEstatus(1);
        lcd.setCodigosNoPermitidos("20-sep-2021;21-sep-2021;20-sep-2021;21-jun-2021;20-jun-2021;21-oct-2021;20-oct-2021;21-ago-2021;20-ago-2021;18-sep-2021;19-sep-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(1);
        lcd.setProductosId(1);
        lcd.setProductoNombre("PEPSI");
        lcd.setEmpaquesId(2);
        lcd.setEmpaque("lata");
        lcd.setTamanio("200ml");
        lcd.setTipoAnalisis("fisico");
        lcd.setAnalisisId(1);
        lcd.setCantidad(2);
        lcd.setEstatus(1);
        lcd.setTipoMuestra(1);
        lcd.setNombreTipoMuestra("Normal");
        lcd.setCodigosNoPermitidos("20-sep-2021;21-sep-2021;20-sep-2021;21-jun-2021;20-jun-2021;21-oct-2021;20-oct-2021;21-ago-2021;20-ago-2021;18-sep-2021;19-sep-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(1);
        lcd.setProductosId(2);
        lcd.setProductoNombre("NARANJA");
        lcd.setEmpaquesId(1);
        lcd.setEmpaque("pet");
        lcd.setTamanio("2l");
        lcd.setTipoAnalisis("torque");
        lcd.setAnalisisId(3);
        lcd.setCantidad(1);
        lcd.setEstatus(1);
        lcd.setTipoMuestra(1);
        lcd.setNombreTipoMuestra("Normal");
        lcd.setCodigosNoPermitidos("20-sep-2021;21-sep-2021;20-sep-2021;21-jun-2021;20-jun-2021;21-oct-2021;20-oct-2021;21-ago-2021;20-ago-2021;18-sep-2021;19-sep-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);

        lcd=new ListaCompraDetalle();
        lcd.setListaId(1);
        lcd.setProductosId(1);
        lcd.setProductoNombre("PEPSI");
        lcd.setEmpaquesId(1);
        lcd.setEmpaque("pet");
        lcd.setTamanio("200ml");
        lcd.setTipoAnalisis("fisico");
        lcd.setAnalisisId(1);
        lcd.setCantidad(2);
        lcd.setEstatus(1);
        lcd.setTipoMuestra(1);
        lcd.setNombreTipoMuestra("Normal");
        lcd.setCodigosNoPermitidos("20-sep-2021;21-sep-2021;20-sep-2021;21-jun-2021;20-jun-2021;21-oct-2021;20-oct-2021;21-ago-2021;20-ago-2021;18-sep-2021;19-sep-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(1);
        lcd.setProductosId(1);
        lcd.setProductoNombre("PEPSI");
        lcd.setEmpaquesId(1);
        lcd.setEmpaque("pet");
        lcd.setTamanio("2l");
        lcd.setTipoAnalisis("fisico");
        lcd.setAnalisisId(1);
        lcd.setCantidad(2);
        lcd.setEstatus(1);
        lcd.setTipoMuestra(1);
        lcd.setNombreTipoMuestra("Normal");
        lcd.setCodigosNoPermitidos("20-sep-2021;21-sep-2021;20-sep-2021;21-jun-2021;20-jun-2021;21-oct-2021;20-oct-2021;21-ago-2021;20-ago-2021;18-sep-2021;19-sep-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(1);
        lcd.setProductosId(2);
        lcd.setProductoNombre("NARANJA");
        lcd.setEmpaquesId(1);
        lcd.setEmpaque("pet");
        lcd.setTamanio("200ml");
        lcd.setTipoAnalisis("sensorial");
        lcd.setAnalisisId(2);
        lcd.setCantidad(2);
        lcd.setEstatus(1);
        lcd.setTipoMuestra(1);
        lcd.setNombreTipoMuestra("Normal");
        lcd.setCodigosNoPermitidos("20-sep-2021;21-sep-2021;20-sep-2021;21-jun-2021;20-jun-2021;21-oct-2021;20-oct-2021;21-ago-2021;20-ago-2021;18-sep-2021;19-sep-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
       /* lcd=new ListaCompraDetalle();
        lcd.setListaId(2);
        lcd.setProductosId(3);
        lcd.setProductoNombre("LIMON");
        lcd.setEmpaquesId(3);
        lcd.setEmpaque("lata");
        lcd.setTamanio("200ml");
        lcd.setTipoAnalisis("torque");
        lcd.setAnalisisId(3);
        lcd.setCantidad(1);
        lcd.setEstatus(1);
        lcd.setTipoMuestra(1);
        lcd.setNombreTipoMuestra("Normal");
        lcd.setCodigosNoPermitidos("20-sep-2021;21-sep-2021;20-sep-2021;21-jun-2021;20-jun-2021;21-oct-2021;20-oct-2021;21-ago-2021;20-ago-2021;18-sep-2021;19-sep-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(2);
        lcd.setProductosId(1);
        lcd.setProductoNombre("pepsi");
        lcd.setEmpaquesId(2);
        lcd.setEmpaque("lata");
        lcd.setTamanio("200ml");
        lcd.setTipoAnalisis("fisico");
        lcd.setAnalisisId(1);
        lcd.setCantidad(2);
        lcd.setEstatus(1);
        lcd.setTipoMuestra(1);
        lcd.setNombreTipoMuestra("Normal");
        lcd.setCodigosNoPermitidos("20-sep-2021;21-sep-2021;20-sep-2021;21-jun-2021;20-jun-2021;21-oct-2021;20-oct-2021;21-ago-2021;20-ago-2021;18-sep-2021;19-sep-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(2);
        lcd.setProductosId(2);
        lcd.setProductoNombre("NARANJA");
        lcd.setEmpaquesId(1);
        lcd.setEmpaque("pet");
        lcd.setTamanio("2l");
        lcd.setTipoAnalisis("micro");
        lcd.setAnalisisId(4);
        lcd.setCantidad(1);
        lcd.setEstatus(1);
        lcd.setTipoMuestra(1);
        lcd.setNombreTipoMuestra("Normal");
        lcd.setCodigosNoPermitidos("20-sep-2021;21-sep-2021;20-sep-2021;21-jun-2021;20-jun-2021;21-oct-2021;20-oct-2021;21-ago-2021;20-ago-2021;18-sep-2021;19-sep-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);*/

     /*   lcd=new ListaCompraDetalle();
        lcd.setListaId(4);
        lcd.setProductosId(1);
        lcd.setProductoNombre("pepsi");
        lcd.setEmpaquesId(1);
        lcd.setEmpaque("pet");
        lcd.setTamanio("200ml");
        lcd.setTipoAnalisis("fisico");
        lcd.setAnalisisId(1);
        lcd.setCantidad(2);
        lcd.setEstatus(1);
        lcd.setTipoMuestra(1);
        lcd.setNombreTipoMuestra("Normal");
        lcd.setCodigosNoPermitidos("20-sep-2021;21-sep-2021;20-sep-2021;21-jun-2021;20-jun-2021;21-oct-2021;20-oct-2021;21-ago-2021;20-ago-2021;18-sep-2021;19-sep-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(4);
        lcd.setProductosId(1);
        lcd.setProductoNombre("pepsi");
        lcd.setEmpaquesId(1);
        lcd.setEmpaque("pet");
        lcd.setTamanio("2l");
        lcd.setTipoAnalisis("fisico");
        lcd.setAnalisisId(1);
        lcd.setCantidad(2);
        lcd.setEstatus(1);
        lcd.setTipoMuestra(2);
        lcd.setNombreTipoMuestra("Catchup");
        lcd.setCodigosNoPermitidos("20-sep-2021;21-sep-2021;20-sep-2021;21-jun-2021;20-jun-2021;21-oct-2021;20-oct-2021;21-ago-2021;20-ago-2021;18-sep-2021;19-sep-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(4);
        lcd.setProductosId(2);
        lcd.setProductoNombre("NARANJA");
        lcd.setEmpaquesId(1);
        lcd.setEmpaque("pet");
        lcd.setTamanio("200ml");
        lcd.setTipoAnalisis("fisico");
        lcd.setAnalisisId(2);
        lcd.setCantidad(2);
        lcd.setEstatus(1);
        lcd.setTipoMuestra(2);
        lcd.setNombreTipoMuestra("Catchup");
        lcd.setCodigosNoPermitidos("20-sep-2021;21-sep-2021;20-sep-2021;21-jun-2021;20-jun-2021;21-oct-2021;20-oct-2021;21-ago-2021;20-ago-2021;18-sep-2021;19-sep-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(4);
        lcd.setProductosId(3);
        lcd.setProductoNombre("LIMON");
        lcd.setEmpaquesId(3);
        lcd.setEmpaque("lata");
        lcd.setTamanio("200ml");
        lcd.setTipoAnalisis("torque");
        lcd.setAnalisisId(2);
        lcd.setCantidad(1);
        lcd.setEstatus(1);
        lcd.setTipoMuestra(2);
        lcd.setNombreTipoMuestra("Catchup");
        lcd.setCodigosNoPermitidos("20-sep-2021;21-sep-2021;20-sep-2021;21-jun-2021;20-jun-2021;21-oct-2021;20-oct-2021;21-ago-2021;20-ago-2021;18-sep-2021;19-sep-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(4);
        lcd.setProductosId(1);
        lcd.setProductoNombre("pepsi");
        lcd.setEmpaquesId(2);
        lcd.setEmpaque("lata");
        lcd.setTamanio("200ml");
        lcd.setTipoAnalisis("fisico");
        lcd.setAnalisisId(1);
        lcd.setCantidad(2);
        lcd.setEstatus(1);
        lcd.setTipoMuestra(2);
        lcd.setNombreTipoMuestra("Catchup");
        lcd.setCodigosNoPermitidos("20-sep-2021;21-sep-2021;20-sep-2021;21-jun-2021;20-jun-2021;21-oct-2021;20-oct-2021;21-ago-2021;20-ago-2021;18-sep-2021;19-sep-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(4);
        lcd.setProductosId(2);
        lcd.setProductoNombre("NARANJA");
        lcd.setEmpaquesId(1);
        lcd.setEmpaque("pet");
        lcd.setTamanio("2l");
        lcd.setTipoAnalisis("torque");
        lcd.setAnalisisId(1);
        lcd.setCantidad(1);
        lcd.setEstatus(1);
        lcd.setTipoMuestra(1);
        lcd.setNombreTipoMuestra("Normal");
        lcd.setCodigosNoPermitidos("20-sep-2021;21-sep-2021;20-sep-2021;21-jun-2021;20-jun-2021;21-oct-2021;20-oct-2021;21-ago-2021;20-ago-2021;18-sep-2021;19-sep-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);

        lcd=new ListaCompraDetalle();
        lcd.setListaId(5);
        lcd.setProductosId(1);
        lcd.setProductoNombre("pepsi");
        lcd.setEmpaquesId(1);
        lcd.setEmpaque("pet");
        lcd.setTamanio("200ml");
        lcd.setTipoAnalisis("fisico");
        lcd.setAnalisisId(1);
        lcd.setCantidad(2);
        lcd.setEstatus(1);
        lcd.setTipoMuestra(1);
        lcd.setNombreTipoMuestra("Normal");
        lcd.setCodigosNoPermitidos("20-sep-2021;21-sep-2021;20-sep-2021;21-jun-2021;20-jun-2021;21-oct-2021;20-oct-2021;21-ago-2021;20-ago-2021;18-sep-2021;19-sep-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(5);
        lcd.setProductosId(1);
        lcd.setProductoNombre("pepsi");
        lcd.setEmpaquesId(1);
        lcd.setEmpaque("pet");
        lcd.setTamanio("2l");
        lcd.setTipoAnalisis("fisico");
        lcd.setAnalisisId(1);
        lcd.setCantidad(2);
        lcd.setEstatus(1);
        lcd.setTipoMuestra(1);
        lcd.setNombreTipoMuestra("Normal");
        lcd.setCodigosNoPermitidos("20-sep-2021;21-sep-2021;20-sep-2021;21-jun-2021;20-jun-2021;21-oct-2021;20-oct-2021;21-ago-2021;20-ago-2021;18-sep-2021;19-sep-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(5);
        lcd.setProductosId(2);
        lcd.setProductoNombre("NARANJA");
        lcd.setEmpaquesId(1);
        lcd.setEmpaque("pet");
        lcd.setTamanio("200ml");
        lcd.setTipoAnalisis("fisico");
        lcd.setAnalisisId(2);
        lcd.setCantidad(2);
        lcd.setEstatus(1);
        lcd.setTipoMuestra(1);
        lcd.setNombreTipoMuestra("Normal");
        lcd.setCodigosNoPermitidos("20-sep-2021;21-sep-2021;20-sep-2021;21-jun-2021;20-jun-2021;21-oct-2021;20-oct-2021;21-ago-2021;20-ago-2021;18-sep-2021;19-sep-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(5);
        lcd.setProductosId(3);
        lcd.setProductoNombre("LIMON");
        lcd.setEmpaquesId(3);
        lcd.setEmpaque("lata");
        lcd.setTamanio("200ml");
        lcd.setTipoAnalisis("torque");
        lcd.setAnalisisId(2);
        lcd.setCantidad(1);
        lcd.setEstatus(1);
        lcd.setTipoMuestra(1);
        lcd.setNombreTipoMuestra("Normal");
        lcd.setCodigosNoPermitidos("20-sep-2021;21-sep-2021;20-sep-2021;21-jun-2021;20-jun-2021;21-oct-2021;20-oct-2021;21-ago-2021;20-ago-2021;18-sep-2021;19-sep-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(5);
        lcd.setProductosId(1);
        lcd.setProductoNombre("pepsi");
        lcd.setEmpaquesId(2);
        lcd.setEmpaque("lata");
        lcd.setTamanio("200ml");
        lcd.setTipoAnalisis("fisico");
        lcd.setAnalisisId(1);
        lcd.setCantidad(2);
        lcd.setEstatus(1);
        lcd.setTipoMuestra(1);
        lcd.setNombreTipoMuestra("Normal");
        lcd.setCodigosNoPermitidos("20-sep-2021;21-sep-2021;20-sep-2021;21-jun-2021;20-jun-2021;21-oct-2021;20-oct-2021;21-ago-2021;20-ago-2021;18-sep-2021;19-sep-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(5);
        lcd.setProductosId(2);
        lcd.setProductoNombre("NARANJA");
        lcd.setEmpaquesId(1);
        lcd.setEmpaque("pet");
        lcd.setTamanio("2l");
        lcd.setTipoAnalisis("torque");
        lcd.setAnalisisId(1);
        lcd.setCantidad(1);
        lcd.setEstatus(1);
        lcd.setTipoMuestra(2);
        lcd.setNombreTipoMuestra("catchup");
        lcd.setCodigosNoPermitidos("20-sep-2021;21-sep-2021;20-sep-2021;21-jun-2021;20-jun-2021;21-oct-2021;20-oct-2021;21-ago-2021;20-ago-2021;18-sep-2021;19-sep-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
*/

    }

    private void prepopulateder( ) {
        String cliente="PEPSI";
        int cliid=4;
        Reactivo campo = new Reactivo();
        campo.setLabel(ctx.getString(R.string.cliente));
        campo.setNombreCampo( "clientesId");
        campo.setType("selectDes");
        campo.setCatalogo(true);
        campo.setSigId(2);//abrir listacompra
        campo.setId(1);
        campo.setTabla("I");
        campo.setCliente("TODOS");
        campo.setClienteSel(0);
        List<Reactivo> camposForm = new ArrayList<Reactivo>();
        camposForm.add(campo);
         campo = new Reactivo();

        campo.setLabel(ctx.getString(R.string.se_compro_producto));
        campo.setNombreCampo( "primeraMuestra");
        campo.setType( "preguntasino");
        campo.setSigAlt(47);
        campo.setSigId(10000);//abrir listacompra
        campo.setId(2);
        campo.setTabla("I");
        campo.setCliente(cliente);
        campo.setClienteSel(cliid);
        camposForm.add(campo);
        campo = new Reactivo();
        campo.setLabel(ctx.getString(R.string.se_compro_segunda));
        campo.setNombreCampo( "segundaMuestra");
        campo.setId(3);
        campo.setType( "preguntasino");
        campo.setSigId(10000);//abrir listacompra
        campo.setSigAlt(5);
        campo.setTabla("I");
        campo.setCliente(cliente);
        campo.setClienteSel(cliid);
        camposForm.add(campo);

        campo = new Reactivo();
        campo.setLabel(ctx.getString(R.string.se_compro_terc));
        campo.setNombreCampo("terceraMuestra");
        campo.setTabla("I");
        campo.setType("preguntasino");
        campo.setSigId(10000);//abrir listacompra
        campo.setSigAlt(5);
        campo.setId( 4);
        campo.setCliente(cliente);
        campo.setClienteSel(cliid);
        camposForm.add(campo);


        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.comentarios));
        campo.setNombreCampo("comentarios");
        campo.setType("textarea");
        campo.setTabla("I");
        campo.setId(7);
        campo.setSigId(43);
        campo.setCliente(cliente);
        campo.setClienteSel(cliid);
        camposForm.add(campo);
        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.ticket_compra));
        campo.setNombreCampo("ticket_compra");
        campo.setType("agregarImagen");
        campo.setTabla("I");
        campo.setId(5);
        campo.setSigId(6);
        campo.setCliente(cliente);
        campo.setClienteSel(cliid);
        camposForm.add(campo);
        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.condiciones));
        campo.setNombreCampo("condiciones_traslado");
        campo.setType("agregarImagen");
        campo.setTabla("I");
        campo.setId(6);
        campo.setSigId(7);
        campo.setCliente(cliente);
        campo.setClienteSel(cliid);
        camposForm.add(campo);
        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.siglas_planta));
        campo.setNombreCampo(Contrato.TablaInformeDet.SIGLAS);
        campo.setType("inputtext");
        campo.setTabla("ID");
        campo.setId(23);
        campo.setSigId(24);
        campo.setBotonMicro(true);
        campo.setCliente(cliente);
        campo.setClienteSel(cliid);
        camposForm.add(campo);

        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.fecha_caducidad));
        campo.setNombreCampo("caducidad");
        campo.setType(CreadorFormulario.FECHAMASK);
        campo.setTabla("ID");
        campo.setId(24);
        campo.setSigId(25);
        campo.setBotonMicro(true);
        campo.setCliente(cliente);
        campo.setClienteSel(cliid);
        camposForm.add(campo);
        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.codigo_producto));
        campo.setNombreCampo("codigo");
        campo.setType("inputtext");
        campo.setTabla("ID");
        campo.setId(25);
        campo.setSigId(26);
        campo.setBotonMicro(true);
        campo.setCliente(cliente);
        campo.setClienteSel(cliid);
        camposForm.add(campo);

        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.origen));
        campo.setTabla("ID");
        campo.setNombreCampo( Contrato.TablaInformeDet.ORIGEN);
        campo.setType("selectCat");
        campo.setCatalogo(true);
        campo.setId(26);
        campo.setSigId(27);
        campo.setCliente(cliente);
        campo.setClienteSel(cliid);
        camposForm.add(campo);
        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.costo));
        campo.setNombreCampo(Contrato.TablaInformeDet.COSTO);
        campo.setType("decimalMask");
        campo.setTabla("ID");
        campo.setId(27);
       // campo.setSigId(28);
        campo.setSigId(32);
        campo.setBotonMicro(true);
        campo.setCliente(cliente);
        campo.setClienteSel(cliid);
        camposForm.add(campo);

        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.foto_codigo_produccion));
        campo.setNombreCampo( Contrato.TablaInformeDet.FOTOCODIGOPRODUCCION);
        campo.setType("agregarImagen");
        campo.setTabla("ID");
        campo.setId(28);
        campo.setSigId(44);
        campo.setCliente(cliente);
        campo.setClienteSel(cliid);
        camposForm.add(campo);

       /* campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.azucareS));
        campo.setNombreCampo(Contrato.TablaInformeDet.AZUCARES);
        campo.setType("agregarImagen");
        campo.setTabla("ID");


        campo.setId(29);
        campo.setSigId(30);
        camposForm.add(campo);



        /*campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.energia));
        campo.setNombreCampo(Contrato.TablaInformeDet.ENERGIA);
        campo.setType("agregarImagen");
        campo.setId(30);
        camposForm.add(campo);
*/
     /*   campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.foto_num_tienda));
        campo.setNombreCampo( Contrato.TablaInformeDet.FOTONUMTIENDA);
        campo.setType("agregarImagen");
        campo.setTabla("ID");

        campo.setId(30);
        campo.setSigId(31);
        camposForm.add(campo);




        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.marca_traslape));
        campo.setNombreCampo(Contrato.TablaInformeDet.MARCA_TRASLAPE);
        campo.setType("agregarImagen");
        campo.setTabla("ID");

        campo.setId(31);
        campo.setSigId(32);
        camposForm.add(campo);*/


        campo = new Reactivo();
        campo.setLabel( ctx.getString(R.string.danio1));
        campo.setNombreCampo("danio1");
        campo.setType( "preguntasino");
        campo.setTabla("ID");
        campo.setCliente(cliente);

        campo.setId(32);
        campo.setSigId(33);
        campo.setSigAlt(41);
        campo.setClienteSel(cliid);
        camposForm.add(campo);

        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.atributoa));
        campo.setNombreCampo(Contrato.TablaInformeDet.ATRIBUTOA);
        campo.setType("selectCat");
        campo.setSigId(35);
        campo.setCatalogo(true);
        campo.setTabla("ID");
        campo.setId(33);
        //paso los atributos a catalogogen
        campo.setCliente(cliente);
        campo.setClienteSel(cliid);
        camposForm.add(campo);


      /*  campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.foto_atributoa));
        campo.setNombreCampo(Contrato.TablaInformeDet.FOTO_ATRIBUTOA);
        campo.setType("agregarImagen");
        campo.setTabla("ID");

        campo.setId(34);
        campo.setSigId(35);
        camposForm.add(campo);*/



        campo = new Reactivo();
        campo.setLabel( ctx.getString(R.string.danio2));
        campo.setId(35);
        campo.setSigId(36);
        campo.setTabla("ID");
        campo.setNombreCampo("danio2");
        campo.setType( "preguntasino");
        campo.setSigAlt(41);
        campo.setCliente(cliente);
        campo.setClienteSel(cliid);

        camposForm.add(campo);

        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.atributob));
        campo.setNombreCampo(Contrato.TablaInformeDet.ATRIBUTOB);
        campo.setType(CreadorFormulario.SELECTCAT);
        //campo.setSigId(37);
        campo.setSigId(38);
        campo.setId(36);
        campo.setCatalogo(true);
        campo.setTabla("ID");
        campo.setCliente(cliente);
        campo.setClienteSel(cliid);
        camposForm.add(campo);
       /* campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.foto_atributob));
        campo.setNombreCampo(Contrato.TablaInformeDet.FOTO_ATRIBUTOB);
        campo.setType("agregarImagen");
        campo.setTabla("ID");

        campo.setId(37);
        campo.setSigId(38);
        camposForm.add(campo);*/

        campo = new Reactivo();
        campo.setLabel( ctx.getString(R.string.danio3));
        campo.setId(38);
        campo.setSigId(39);
        campo.setNombreCampo("danio3");
        campo.setType( "preguntasino");
        campo.setSigAlt(41);
        campo.setTabla("ID");
        campo.setCliente(cliente);
        campo.setClienteSel(cliid);
        camposForm.add(campo);

        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.atributoc));
        campo.setNombreCampo(Contrato.TablaInformeDet.ATRIBUTOC);
        campo.setType(CreadorFormulario.SELECTCAT);
        campo.setTabla("ID");
        campo.setId(39);
      //  campo.setSigId(40);
        campo.setSigId(41);
        campo.setCatalogo(true);
        campo.setCliente(cliente);
        campo.setClienteSel(cliid);
        camposForm.add(campo);
      /*  campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.foto_atributoc));
        campo.setNombreCampo(Contrato.TablaInformeDet.FOTO_ATRIBUTOC);
        campo.setType("agregarImagen");
        campo.setTabla("ID");

        campo.setId(40);
        campo.setSigId(41);
        camposForm.add(campo);*/




        campo=new Reactivo();
        campo.setLabel("QR");
        campo.setTabla("ID");
        campo.setNombreCampo(Contrato.TablaInformeDet.QR);
        campo.setType("botonqr");
        campo.setSigId(28);
       // campo.setSigId(42);
        campo.setId(41);
        campo.setClienteSel(cliid);
        campo.setCliente(cliente);
        camposForm.add(campo);

        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.foto_posicion1));
        campo.setNombreCampo(Contrato.TablaInformeDet.FOTO_ATRIBUTOA);
        campo.setType("agregarImagen");
        campo.setTabla("ID");
        campo.setId(44);
        campo.setSigId(45);
        campo.setCliente(cliente);
        campo.setClienteSel(cliid);
        camposForm.add(campo);


        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.foto_posicion2));
        campo.setNombreCampo(Contrato.TablaInformeDet.FOTO_ATRIBUTOB);
        campo.setType("agregarImagen");
        campo.setTabla("ID");
        campo.setId(45);
        campo.setSigId(46);
        campo.setCliente(cliente);
        campo.setClienteSel(cliid);
        camposForm.add(campo);

        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.foto_posicion3));
        campo.setNombreCampo(Contrato.TablaInformeDet.FOTO_ATRIBUTOC);
        campo.setType("agregarImagen");
        campo.setTabla("ID");
         campo.setId(46);
        campo.setSigId(42);
        campo.setCliente(cliente);
        campo.setClienteSel(cliid);
        camposForm.add(campo);


        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.etiqueta_evaluacion));
        campo.setNombreCampo(Contrato.TablaInformeDet.ETIQUETA_EVALUACION);
        campo.setType("agregarImagen");
        campo.setTabla("ID");
        campo.setId(42);
        campo.setSigId(48);
        campo.setCliente(cliente);
        campo.setClienteSel(cliid);
        camposForm.add(campo);

        //insert into reactivos values(43,1,'¿CAPTURAR INFORME DE OTRO CLIENTE MISMA TIENDA?',"preguntasino","",0,0,0,null)
        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.pregunta_mas_clientes));
        campo.setNombreCampo("");
        campo.setType("preguntasino");
        campo.setTabla("");
        campo.setClienteSel(cliid);
        campo.setCliente(cliente);

        campo.setId(43);
        campo.setSigId(1);
        campo.setSigAlt(500);
        camposForm.add(campo);
        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.selecciones_causa));
        campo.setNombreCampo(Contrato.TablaInformeDet.causa_nocompra);
        campo.setType("radiobutton");
        campo.setTabla("I");
        campo.setId(47);
        campo.setSigId(7);
        campo.setClienteSel(cliid);
        campo.setCatalogo(true);

        campo.setCliente(cliente);
        camposForm.add(campo);

        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.seguro_muestra));
        campo.setNombreCampo("confirmaMuestra");
        campo.setType("hidden");
        campo.setTabla("");
        campo.setId(48);
        campo.setClienteSel(cliid);
        campo.setCliente(cliente);
        camposForm.add(campo);

        getReactivoDao().insertAll(camposForm);


    }

    private void prepopulatederpeni( ) {
        Reactivo campo = new Reactivo();
        String cliente="PEÑAFIEL";
        int cliid=5;

      /*  campo.setLabel(ctx.getString(R.string.cliente));
        campo.setNombreCampo( "clientesId");
        campo.setType("selectDes");
        campo.setCatalogo(true);
        campo.setSigId(52);//abrir listacompra
        campo.setId(51);
        campo.setTabla("I");
        campo.setCliente("PEÑAFIEL");*/
        List<Reactivo> camposForm = new ArrayList<Reactivo>();
       // camposForm.add(campo);
        campo = new Reactivo();

        campo.setLabel(ctx.getString(R.string.se_compro_producto));
        campo.setNombreCampo( "primeraMuestra");
        campo.setType( "preguntasino");
        campo.setSigAlt(69);
        campo.setSigId(20000);//abrir listacompra
        campo.setId(52);
        campo.setTabla("I");
        campo.setCliente(cliente);
        campo.setClienteSel(cliid);
        camposForm.add(campo);
        campo = new Reactivo();
        campo.setLabel(ctx.getString(R.string.se_compro_segunda));
        campo.setNombreCampo( "segundaMuestra");
        campo.setId(53);
        campo.setType( "preguntasino");
        campo.setSigId(20000);//abrir listacompra
        campo.setSigAlt(55);
        campo.setTabla("I");
        campo.setCliente(cliente);    campo.setClienteSel(cliid);
        camposForm.add(campo);

        campo = new Reactivo();
        campo.setLabel(ctx.getString(R.string.se_compro_terc));
        campo.setNombreCampo("terceraMuestra");
        campo.setTabla("I");
        campo.setType("preguntasino");
        campo.setSigId(20000);//abrir listacompra
        campo.setSigAlt(55);
        campo.setId(54);
        campo.setCliente(cliente);    campo.setClienteSel(cliid);
        camposForm.add(campo);

        campo = new Reactivo();
        campo.setLabel(ctx.getString(R.string.se_compro_cuar));
        campo.setNombreCampo("cuartaMuestra");
        campo.setTabla("I");
        campo.setType("preguntasino");
        campo.setSigId(20000);//abrir listacompra
        campo.setSigAlt(55);
        campo.setId(67);
        campo.setCliente(cliente);    campo.setClienteSel(cliid);
        camposForm.add(campo);

        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.comentarios));
        campo.setNombreCampo("comentarios");
        campo.setType("textarea");
        campo.setTabla("I");
        campo.setId(57);
        campo.setSigId(68);
        campo.setCliente(cliente);    campo.setClienteSel(cliid);
        camposForm.add(campo);
        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.ticket_compra));
        campo.setNombreCampo("ticket_compra");
        campo.setType("agregarImagen");
        campo.setTabla("I");
        campo.setId(55);
        campo.setSigId(56);
        campo.setCliente(cliente);    campo.setClienteSel(cliid);
        camposForm.add(campo);
        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.condiciones));
        campo.setNombreCampo("condiciones_traslado");
        campo.setType("agregarImagen");
        campo.setTabla("I");
        campo.setId(56);
        campo.setSigId(57);
        campo.setCliente(cliente);    campo.setClienteSel(cliid);
        camposForm.add(campo);
        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.siglas_planta));
        campo.setNombreCampo(Contrato.TablaInformeDet.SIGLAS);
        campo.setType("inputtext");
        campo.setTabla("ID");
        campo.setId(58);
        campo.setSigId(59);
        campo.setBotonMicro(true);
        campo.setCliente(cliente);    campo.setClienteSel(cliid);

        camposForm.add(campo);

        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.fecha_caducidad));
        campo.setNombreCampo("caducidad");
        campo.setType(CreadorFormulario.FECHAMASK);
        campo.setTabla("ID");
        campo.setId(59);
        campo.setSigId(60);
        campo.setBotonMicro(true);
        campo.setCliente(cliente);    campo.setClienteSel(cliid);

        camposForm.add(campo);
        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.codigo_producto));
        campo.setNombreCampo("codigo");
        campo.setType("inputtext");
        campo.setTabla("ID");
        campo.setId(60);
        campo.setSigId(61);
        campo.setBotonMicro(true);
        campo.setCliente(cliente);    campo.setClienteSel(cliid);

        camposForm.add(campo);

        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.origen));
        campo.setTabla("ID");
        campo.setNombreCampo( Contrato.TablaInformeDet.ORIGEN);
        campo.setType("selectCat");
        campo.setCatalogo(true);
        campo.setId(61);
        campo.setSigId(62);
        campo.setCliente(cliente);    campo.setClienteSel(cliid);
        camposForm.add(campo);
        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.costo));
        campo.setNombreCampo(Contrato.TablaInformeDet.COSTO);
        campo.setType("decimalMask");
        campo.setTabla("ID");
        campo.setId(62);
        // campo.setSigId(28);
        campo.setSigId(63);
        campo.setBotonMicro(true);
        campo.setCliente(cliente);    campo.setClienteSel(cliid);

        camposForm.add(campo);

        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.foto_codigo_produccion));
        campo.setNombreCampo( Contrato.TablaInformeDet.FOTOCODIGOPRODUCCION);
        campo.setType("agregarImagen");
        campo.setTabla("ID");
        campo.setId(63);
        campo.setSigId(64);
        campo.setCliente(cliente);    campo.setClienteSel(cliid);
        camposForm.add(campo);

        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.foto_posicion1));
        campo.setNombreCampo(Contrato.TablaInformeDet.FOTO_ATRIBUTOA);
        campo.setType("agregarImagen");
        campo.setTabla("ID");
        campo.setId(64);
        campo.setSigId(65);
        campo.setCliente(cliente);    campo.setClienteSel(cliid);
        camposForm.add(campo);


        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.foto_posicion2));
        campo.setNombreCampo(Contrato.TablaInformeDet.FOTO_ATRIBUTOB);
        campo.setType("agregarImagen");
        campo.setTabla("ID");
        campo.setId(65);
        campo.setSigId(66);
        campo.setCliente(cliente);    campo.setClienteSel(cliid);
        camposForm.add(campo);

        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.foto_posicion3));
        campo.setNombreCampo(Contrato.TablaInformeDet.FOTO_ATRIBUTOC);
        campo.setType("agregarImagen");
        campo.setTabla("ID");
        campo.setId(66);
        campo.setSigId(70);
        campo.setCliente(cliente);    campo.setClienteSel(cliid);
        camposForm.add(campo);
        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.seguro_muestra));
        campo.setNombreCampo("confirmaMuestra");
        campo.setType("hidden");
        campo.setTabla("");
        campo.setId(70);

        campo.setCliente(cliente);
        campo.setClienteSel(cliid);
        camposForm.add(campo);

     /*   campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.etiqueta_evaluacion));
        campo.setNombreCampo(Contrato.TablaInformeDet.ETIQUETA_EVALUACION);
        campo.setType("agregarImagen");
        campo.setTabla("ID");
        campo.setId(42);
        campo.setSigId(48);
        campo.setCliente(cliente);    campo.setClienteSel(cliid);
        camposForm.add(campo);*/

        //insert into reactivos values(43,1,'¿CAPTURAR INFORME DE OTRO CLIENTE MISMA TIENDA?',"preguntasino","",0,0,0,null)
        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.pregunta_mas_clientes));
        campo.setNombreCampo("");
        campo.setType("preguntasino");
        campo.setTabla("");
        campo.setCliente(cliente);    campo.setClienteSel(cliid);

        campo.setId(68);
        campo.setSigId(51);
        campo.setSigAlt(600);
        camposForm.add(campo);
        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.selecciones_causa));
        campo.setNombreCampo(Contrato.TablaInformeDet.causa_nocompra);
        campo.setType("radiobutton");
        campo.setTabla("I");
        campo.setId(69);
        campo.setSigId(57);
        campo.setCatalogo(true);

        campo.setCliente(cliente);
        campo.setClienteSel(cliid);
        camposForm.add(campo);
        getReactivoDao().insertAll(camposForm);


    }

    private  void prepopulatederele( ) {
        Reactivo campo = new Reactivo();
        String cliente="ELECTROPURA";
        int cliid=6;

      /*  campo.setLabel(ctx.getString(R.string.cliente));
        campo.setNombreCampo( "clientesId");
        campo.setType("selectDes");
        campo.setCatalogo(true);
        campo.setSigId(72);//abrir listacompra
        campo.setId(71);
        campo.setTabla("I");
        campo.setCliente(cliente);    campo.setClienteSel(cliid);*/
        List<Reactivo> camposForm = new ArrayList<Reactivo>();
       // camposForm.add(campo);
        //campo = new Reactivo();

        campo.setLabel(ctx.getString(R.string.se_compro_producto));
        campo.setNombreCampo( "primeraMuestra");
        campo.setType( "preguntasino");
        campo.setSigAlt(89);
        campo.setSigId(3000);//abrir listacompra
        campo.setId(72);
        campo.setTabla("I");
        campo.setCliente(cliente);    campo.setClienteSel(cliid);

        camposForm.add(campo);
        campo = new Reactivo();
        campo.setLabel(ctx.getString(R.string.se_compro_segunda));
        campo.setNombreCampo( "segundaMuestra");
        campo.setId(73);
        campo.setType( "preguntasino");
        campo.setSigId(3000);//abrir listacompra
        campo.setSigAlt(75);
        campo.setTabla("I");
        campo.setCliente(cliente);    campo.setClienteSel(cliid);
        camposForm.add(campo);

        campo = new Reactivo();
        campo.setLabel(ctx.getString(R.string.se_compro_terc));
        campo.setNombreCampo("terceraMuestra");
        campo.setTabla("I");
        campo.setType("preguntasino");
        campo.setSigId(3000);//abrir listacompra
        campo.setSigAlt(75);
        campo.setId( 74);
        campo.setCliente(cliente);    campo.setClienteSel(cliid);
        camposForm.add(campo);

        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.comentarios));
        campo.setNombreCampo("comentarios");
        campo.setType("textarea");
        campo.setTabla("I");
        campo.setId(77);
        campo.setSigId(88);
        campo.setCliente(cliente);    campo.setClienteSel(cliid);
        camposForm.add(campo);
        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.ticket_compra));
        campo.setNombreCampo("ticket_compra");
        campo.setType("agregarImagen");
        campo.setTabla("I");
        campo.setId(75);
        campo.setSigId(76);
        campo.setCliente(cliente);    campo.setClienteSel(cliid);
        camposForm.add(campo);
        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.condiciones));
        campo.setNombreCampo("condiciones_traslado");
        campo.setType("agregarImagen");
        campo.setTabla("I");
        campo.setId(76);
        campo.setSigId(77);
        campo.setCliente(cliente);    campo.setClienteSel(cliid);
        camposForm.add(campo);
        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.siglas_planta));
        campo.setNombreCampo(Contrato.TablaInformeDet.SIGLAS);
        campo.setType("inputtext");
        campo.setTabla("ID");
        campo.setId(78);
        campo.setSigId(79);
        campo.setBotonMicro(true);
        campo.setCliente(cliente);    campo.setClienteSel(cliid);

        camposForm.add(campo);

        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.fecha_caducidad));
        campo.setNombreCampo("caducidad");
        campo.setType(CreadorFormulario.FECHAMASK);
        campo.setTabla("ID");
        campo.setId(79);
        campo.setSigId(80);
        campo.setBotonMicro(true);
        campo.setCliente(cliente);    campo.setClienteSel(cliid);

        camposForm.add(campo);
        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.codigo_producto));
        campo.setNombreCampo("codigo");
        campo.setType("inputtext");
        campo.setTabla("ID");
        campo.setId(80);
        campo.setSigId(81);
        campo.setBotonMicro(true);
        campo.setCliente(cliente);    campo.setClienteSel(cliid);

        camposForm.add(campo);

        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.origen));
        campo.setTabla("ID");
        campo.setNombreCampo( Contrato.TablaInformeDet.ORIGEN);
        campo.setType("selectCat");
        campo.setCatalogo(true);
        campo.setId(81);
        campo.setSigId(82);
        campo.setCliente(cliente);    campo.setClienteSel(cliid);
        camposForm.add(campo);
        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.costo));
        campo.setNombreCampo(Contrato.TablaInformeDet.COSTO);
        campo.setType("decimalMask");
        campo.setTabla("ID");
        campo.setId(82);
        // campo.setSigId(28);
        campo.setSigId(83);
        campo.setBotonMicro(true);
        campo.setCliente(cliente);    campo.setClienteSel(cliid);

        camposForm.add(campo);

        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.foto_codigo_produccion));
        campo.setNombreCampo( Contrato.TablaInformeDet.FOTOCODIGOPRODUCCION);
        campo.setType("agregarImagen");
        campo.setTabla("ID");
        campo.setId(83);
        campo.setSigId(84);
        campo.setCliente(cliente);    campo.setClienteSel(cliid);
        camposForm.add(campo);

        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.foto_posicion1));
        campo.setNombreCampo(Contrato.TablaInformeDet.FOTO_ATRIBUTOA);
        campo.setType("agregarImagen");
        campo.setTabla("ID");
        campo.setId(84);
        campo.setSigId(85);
        campo.setCliente(cliente);    campo.setClienteSel(cliid);
        camposForm.add(campo);


        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.foto_posicion2));
        campo.setNombreCampo(Contrato.TablaInformeDet.FOTO_ATRIBUTOB);
        campo.setType("agregarImagen");
        campo.setTabla("ID");
        campo.setId(85);
        campo.setSigId(86);
        campo.setCliente(cliente);    campo.setClienteSel(cliid);
        camposForm.add(campo);

        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.foto_posicion3));
        campo.setNombreCampo(Contrato.TablaInformeDet.FOTO_ATRIBUTOC);
        campo.setType("agregarImagen");
        campo.setTabla("ID");
        campo.setId(86);
        campo.setSigId(87);
        campo.setCliente(cliente);    campo.setClienteSel(cliid);
        camposForm.add(campo);


        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.seguro_muestra));
        campo.setNombreCampo("confirmaMuestra");
        campo.setType("hidden");
        campo.setTabla("");
        campo.setId(87);

        campo.setCliente(cliente);
        campo.setClienteSel(cliid);
        camposForm.add(campo);

        //insert into reactivos values(43,1,'¿CAPTURAR INFORME DE OTRO CLIENTE MISMA TIENDA?',"preguntasino","",0,0,0,null)
        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.pregunta_mas_clientes));
        campo.setNombreCampo("");
        campo.setType("preguntasino");
        campo.setTabla("");
        campo.setCliente(cliente);    campo.setClienteSel(cliid);

        campo.setId(88);
        campo.setSigId(1);
        campo.setSigAlt(800);
        camposForm.add(campo);
        campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.selecciones_causa));
        campo.setNombreCampo(Contrato.TablaInformeDet.causa_nocompra);
        campo.setType("radiobutton");
        campo.setTabla("I");
        campo.setId(89);
        campo.setSigId(77);
        campo.setCatalogo(true);

        campo.setCliente(cliente);    campo.setClienteSel(cliid);
        camposForm.add(campo);

      /*  campo=new Reactivo();
        campo.setLabel(ctx.getString(R.string.seguro_muestra));
        campo.setNombreCampo("confirmaMuestra");
        campo.setType("hidden");
        campo.setTabla("");
        campo.setId(48);

        campo.setCliente("TODOS");
        camposForm.add(campo);*/

        getReactivoDao().insertAll(camposForm);


    }
    public void catalogos(){
        CatalogoDetalle cat=new CatalogoDetalle();
        List<CatalogoDetalle> lista=new ArrayList<>();
        cat.setCad_idcatalogo(1);
        cat.setCad_idopcion(1);
        cat.setCad_descripcionesp("atributo 1");
        cat.setCad_nombreCatalogo("atributos");
        lista.add(cat);
        getCatalogoDao().insertAll(lista);
         cat=new CatalogoDetalle();

        cat.setCad_idcatalogo(1);
        cat.setCad_idopcion(2);
        cat.setCad_descripcionesp("atributo 2");
        cat.setCad_nombreCatalogo("atributos");
        lista.add(cat);
        getCatalogoDao().insertAll(lista);
        cat=new CatalogoDetalle();

        cat.setCad_idcatalogo(1);
        cat.setCad_idopcion(3);
        cat.setCad_descripcionesp("atributo 3");
        cat.setCad_nombreCatalogo("atributos");
        lista.add(cat);
        getCatalogoDao().insertAll(lista);
        cat=new CatalogoDetalle();

        cat.setCad_idcatalogo(1);
        cat.setCad_idopcion(4);
        cat.setCad_descripcionesp("atributo 4");
        cat.setCad_nombreCatalogo("atributos");
        lista.add(cat);
        getCatalogoDao().insertAll(lista);
        cat=new CatalogoDetalle();

        cat.setCad_idcatalogo(1);
        cat.setCad_idopcion(5);
        cat.setCad_descripcionesp("atributo 5");
        cat.setCad_nombreCatalogo("atributos");
        lista.add(cat);
        getCatalogoDao().insertAll(lista);
        getCatalogoDao().insertAll(lista);
       /* lista=new ArrayList<>();
        cat=new CatalogoDetalle();
        cat.setCad_idcatalogo(2);
        cat.setCad_idopcion(1);
        cat.setCad_descripcionesp("anaquel");
        cat.setCad_nombreCatalogo("ubicacion_muestra");
        lista.add(cat);
        getCatalogoDao().insertAll(lista);
        cat=new CatalogoDetalle();
        cat.setCad_idcatalogo(2);
        cat.setCad_idopcion(2);
        cat.setCad_descripcionesp("refri");
        cat.setCad_nombreCatalogo("ubicacion_muestra");
        lista.add(cat);
        getCatalogoDao().insertAll(lista);*/
    }
  /*  static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("alter table imagocts add column created_at timestamp");
        }
    };
*/

}


