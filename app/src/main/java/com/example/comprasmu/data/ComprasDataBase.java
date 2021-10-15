package com.example.comprasmu.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.comprasmu.data.dao.BaseDao;
import com.example.comprasmu.data.dao.CatalogoDetalleDao;
import com.example.comprasmu.data.dao.ImagenDetalleDao;
import com.example.comprasmu.data.dao.InformeCompraDao;
import com.example.comprasmu.data.dao.InformeCompraDetDao;
import com.example.comprasmu.data.dao.ListaCompraDao;
import com.example.comprasmu.data.dao.ListaCompraDetalleDao;
import com.example.comprasmu.data.dao.ProductoExhibidoDao;
import com.example.comprasmu.data.dao.ReactivoDao;
import com.example.comprasmu.data.dao.TablaVersionesDao;
import com.example.comprasmu.data.dao.VisitaDao;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.ListaWithDetalle;
import com.example.comprasmu.data.modelos.ProductoExhibido;
import com.example.comprasmu.data.modelos.Reactivo;
import com.example.comprasmu.data.modelos.TablaVersiones;
import com.example.comprasmu.data.modelos.Visita;

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
        Visita.class,
        ProductoExhibido.class,
        CatalogoDetalle.class},views = {InformeCompraDao.InformeCompravisita.class, ProductoExhibidoDao.ProductoExhibidoFoto.class}, version=1, exportSchema = false)
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

    public abstract InformeCompraDao getInformeCompraDao();
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

    private void cargandodatos(){
        runInTransaction(new Runnable() {
            @Override
            public void run() {
                ListaCompraDao dao = getListaCompraDao();
                dao.findAll().observeForever( myProducts -> {
                    if (myProducts == null||myProducts.size()==0) {
                        //no tengo datos


                        prepopulatelc();
                        prepopulatedetc();
                        prepopulateder();
                        catalogos();

                    }
                });
            }
        });
    }

    private  void prepopulatelc(  ) {
        ListaCompra lc=new ListaCompra();
        ListaCompraDao dao=getListaCompraDao();
        lc.setClientesId(1);
        lc.setPlantasId(1);
        lc.setCiudadesId(1);
        lc.setCiudadNombre("Cd Juarez");
        lc.setClienteNombre("pepsi");
        lc.setPlantaNombre("juarez");
        lc.setIndice("julio 2021");
        lc.setEstatus(1);
        dao.insert(lc);

        lc=new ListaCompra();
        lc.setClientesId(1);
        lc.setPlantasId(1);
        lc.setClienteNombre("peñafiel");
        lc.setPlantaNombre("acapulco");
        lc.setIndice("junio 2021");
        lc.setEstatus(1);
        lc.setCiudadesId(2);
        lc.setCiudadNombre("Cd Acapulco");
        dao.insert(lc);
        lc=new ListaCompra();
        lc.setClientesId(1);
        lc.setPlantasId(1);
        lc.setClienteNombre("peñafiel");
        lc.setPlantaNombre("juarez");
        lc.setIndice("junio 2021");
        lc.setCiudadesId(1);
        lc.setCiudadNombre("Cd Juarez");
        lc.setEstatus(1);
        dao.insert(lc);
        lc=new ListaCompra();
        lc.setClientesId(1);
        lc.setPlantasId(1);
        lc.setClienteNombre("pepsi");
        lc.setPlantaNombre("monterrey");
        lc.setIndice("junio 2021");
        lc.setCiudadesId(3);
        lc.setCiudadNombre("Nuevo leon");
        lc.setEstatus(1);
        dao.insert(lc);
        lc=new ListaCompra();
        lc.setClientesId(1);
        lc.setPlantasId(1);
        lc.setClienteNombre("peñafiel");
        lc.setPlantaNombre("monterrey");
        lc.setIndice("junio 2021");
        lc.setCiudadesId(3);
        lc.setCiudadNombre("Nuevo leon");
        lc.setEstatus(1);
        dao.insert(lc);
        lc=new ListaCompra();
        lc.setClientesId(1);
        lc.setPlantasId(1);
        lc.setClienteNombre("pepsi");
        lc.setPlantaNombre("acapulco");
        lc.setCiudadesId(2);
        lc.setCiudadNombre(" cd. acapulco");
        lc.setEstatus(1);
        lc.setIndice("julio 2021");
        dao.insert(lc);
    }

    private  void prepopulatedetc( ) {
        ListaCompraDetalle lcd=new ListaCompraDetalle();
        lcd.setListaId(1);
        lcd.setProductosId(1);
        lcd.setProductoNombre("pepsi");
        lcd.setEmpaquesId(1);
        lcd.setEmpaque("pet");
        lcd.setTamanio("200ml");
        lcd.setTipoAnalisis("fisico");
        lcd.setAnalisisId(1);
        lcd.setTipoMuestra(1);
        lcd.setNombreTipoMuestra("Normal");
        lcd.setCantidad(2);
        lcd.setEstatus(1);
        lcd.setCodigosNoPermitidos("20-may-2021;21-may-2021;20-may-2021;21-jun-2021;20-jun-2021;21-ene-2021;20-ene-2021;21-feb-2021;20-feb-2021;18-may-2021;19-may-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(1);
        lcd.setProductosId(1);
        lcd.setProductoNombre("pepsi");
        lcd.setEmpaquesId(1);
        lcd.setEmpaque("pet");
        lcd.setTamanio("2l");
        lcd.setTipoAnalisis("fisico");
        lcd.setAnalisisId(1);
        lcd.setTipoMuestra(1);
        lcd.setNombreTipoMuestra("Normal");
        lcd.setCantidad(2);
        lcd.setEstatus(1);
        lcd.setCodigosNoPermitidos("20-may-2021;21-may-2021;20-may-2021;21-jun-2021;20-jun-2021;21-ene-2021;20-ene-2021;21-feb-2021;20-feb-2021;18-may-2021;19-may-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(1);
        lcd.setProductosId(2);
        lcd.setProductoNombre("7UP");
        lcd.setEmpaquesId(1);
        lcd.setEmpaque("pet");
        lcd.setTamanio("200ml");
        lcd.setTipoAnalisis("fisico");
        lcd.setAnalisisId(2);
        lcd.setTipoMuestra(1);
        lcd.setNombreTipoMuestra("Normal");
        lcd.setCantidad(2);
        lcd.setEstatus(1);
        lcd.setCodigosNoPermitidos("20-may-2021;21-may-2021;20-may-2021;21-jun-2021;20-jun-2021;21-ene-2021;20-ene-2021;21-feb-2021;20-feb-2021;18-may-2021;19-may-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(1);
        lcd.setProductosId(3);
        lcd.setProductoNombre("MANZANA");
        lcd.setEmpaquesId(3);
        lcd.setEmpaque("lata");
        lcd.setTamanio("200ml");
        lcd.setTipoAnalisis("torque");
        lcd.setAnalisisId(2);
        lcd.setTipoMuestra(1);
        lcd.setNombreTipoMuestra("Normal");
        lcd.setCantidad(1);
        lcd.setEstatus(1);
        lcd.setCodigosNoPermitidos("20-may-2021;21-may-2021;20-may-2021;21-jun-2021;20-jun-2021;21-ene-2021;20-ene-2021;21-feb-2021;20-feb-2021;18-may-2021;19-may-2021;21-mar-2021;");
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
        lcd.setCodigosNoPermitidos("20-may-2021;21-may-2021;20-may-2021;21-jun-2021;20-jun-2021;21-ene-2021;20-ene-2021;21-feb-2021;20-feb-2021;18-may-2021;19-may-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(1);
        lcd.setProductosId(2);
        lcd.setProductoNombre("NARANJA");
        lcd.setEmpaquesId(1);
        lcd.setEmpaque("pet");
        lcd.setTamanio("2l");
        lcd.setTipoAnalisis("torque");
        lcd.setAnalisisId(1);
        lcd.setCantidad(1);
        lcd.setTipoMuestra(1);
        lcd.setNombreTipoMuestra("Normal");
        lcd.setEstatus(1);
        lcd.setCodigosNoPermitidos("20-may-2021;21-may-2021;20-may-2021;21-jun-2021;20-jun-2021;21-ene-2021;20-ene-2021;21-feb-2021;20-feb-2021;18-may-2021;19-may-2021;21-mar-2021;");
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
        lcd.setCodigosNoPermitidos("20-may-2021;21-may-2021;20-may-2021;21-jun-2021;20-jun-2021;21-ene-2021;20-ene-2021;21-feb-2021;20-feb-2021;18-may-2021;19-may-2021;21-mar-2021;");
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
        lcd.setCodigosNoPermitidos("20-may-2021;21-may-2021;20-may-2021;21-jun-2021;20-jun-2021;21-ene-2021;20-ene-2021;21-feb-2021;20-feb-2021;18-may-2021;19-may-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(2);
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
        lcd.setCodigosNoPermitidos("20-may-2021;21-may-2021;20-may-2021;21-jun-2021;20-jun-2021;21-ene-2021;20-ene-2021;21-feb-2021;20-feb-2021;18-may-2021;19-may-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(2);
        lcd.setProductosId(3);
        lcd.setProductoNombre("LIMON");
        lcd.setEmpaquesId(3);
        lcd.setEmpaque("lata");
        lcd.setTamanio("200ml");
        lcd.setTipoAnalisis("torque");
        lcd.setAnalisisId(2);
        lcd.setCantidad(1);
        lcd.setTipoMuestra(1);
        lcd.setNombreTipoMuestra("Normal");
        lcd.setEstatus(1);
        lcd.setCodigosNoPermitidos("20-may-2021;21-may-2021;20-may-2021;21-jun-2021;20-jun-2021;21-ene-2021;20-ene-2021;21-feb-2021;20-feb-2021;18-may-2021;19-may-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(2);
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
        lcd.setCodigosNoPermitidos("20-may-2021;21-may-2021;20-may-2021;21-jun-2021;20-jun-2021;21-ene-2021;20-ene-2021;21-feb-2021;20-feb-2021;18-may-2021;19-may-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(2);
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
        lcd.setCodigosNoPermitidos("20-may-2021;21-may-2021;20-may-2021;21-jun-2021;20-jun-2021;21-ene-2021;20-ene-2021;21-feb-2021;20-feb-2021;18-may-2021;19-may-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);

        lcd=new ListaCompraDetalle();
        lcd.setListaId(3);
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
        lcd.setCodigosNoPermitidos("20-may-2021;21-may-2021;20-may-2021;21-jun-2021;20-jun-2021;21-ene-2021;20-ene-2021;21-feb-2021;20-feb-2021;18-may-2021;19-may-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(3);
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
        lcd.setCodigosNoPermitidos("20-may-2021;21-may-2021;20-may-2021;21-jun-2021;20-jun-2021;21-ene-2021;20-ene-2021;21-feb-2021;20-feb-2021;18-may-2021;19-may-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(3);
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
        lcd.setCodigosNoPermitidos("20-may-2021;21-may-2021;20-may-2021;21-jun-2021;20-jun-2021;21-ene-2021;20-ene-2021;21-feb-2021;20-feb-2021;18-may-2021;19-may-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(3);
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
        lcd.setCodigosNoPermitidos("20-may-2021;21-may-2021;20-may-2021;21-jun-2021;20-jun-2021;21-ene-2021;20-ene-2021;21-feb-2021;20-feb-2021;18-may-2021;19-may-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(3);
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
        lcd.setCodigosNoPermitidos("20-may-2021;21-may-2021;20-may-2021;21-jun-2021;20-jun-2021;21-ene-2021;20-ene-2021;21-feb-2021;20-feb-2021;18-may-2021;19-may-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);
        lcd=new ListaCompraDetalle();
        lcd.setListaId(3);
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
        lcd.setCodigosNoPermitidos("20-may-2021;21-may-2021;20-may-2021;21-jun-2021;20-jun-2021;21-ene-2021;20-ene-2021;21-feb-2021;20-feb-2021;18-may-2021;19-may-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);

        lcd=new ListaCompraDetalle();
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
        lcd.setCodigosNoPermitidos("20-may-2021;21-may-2021;20-may-2021;21-jun-2021;20-jun-2021;21-ene-2021;20-ene-2021;21-feb-2021;20-feb-2021;18-may-2021;19-may-2021;21-mar-2021;");
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
        lcd.setCodigosNoPermitidos("20-may-2021;21-may-2021;20-may-2021;21-jun-2021;20-jun-2021;21-ene-2021;20-ene-2021;21-feb-2021;20-feb-2021;18-may-2021;19-may-2021;21-mar-2021;");
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
        lcd.setCodigosNoPermitidos("20-may-2021;21-may-2021;20-may-2021;21-jun-2021;20-jun-2021;21-ene-2021;20-ene-2021;21-feb-2021;20-feb-2021;18-may-2021;19-may-2021;21-mar-2021;");
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
        lcd.setCodigosNoPermitidos("20-may-2021;21-may-2021;20-may-2021;21-jun-2021;20-jun-2021;21-ene-2021;20-ene-2021;21-feb-2021;20-feb-2021;18-may-2021;19-may-2021;21-mar-2021;");
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
        lcd.setCodigosNoPermitidos("20-may-2021;21-may-2021;20-may-2021;21-jun-2021;20-jun-2021;21-ene-2021;20-ene-2021;21-feb-2021;20-feb-2021;18-may-2021;19-may-2021;21-mar-2021;");
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
        lcd.setCodigosNoPermitidos("20-may-2021;21-may-2021;20-may-2021;21-jun-2021;20-jun-2021;21-ene-2021;20-ene-2021;21-feb-2021;20-feb-2021;18-may-2021;19-may-2021;21-mar-2021;");
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
        lcd.setCodigosNoPermitidos("20-may-2021;21-may-2021;20-may-2021;21-jun-2021;20-jun-2021;21-ene-2021;20-ene-2021;21-feb-2021;20-feb-2021;18-may-2021;19-may-2021;21-mar-2021;");
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
        lcd.setCodigosNoPermitidos("20-may-2021;21-may-2021;20-may-2021;21-jun-2021;20-jun-2021;21-ene-2021;20-ene-2021;21-feb-2021;20-feb-2021;18-may-2021;19-may-2021;21-mar-2021;");
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
        lcd.setCodigosNoPermitidos("20-may-2021;21-may-2021;20-may-2021;21-jun-2021;20-jun-2021;21-ene-2021;20-ene-2021;21-feb-2021;20-feb-2021;18-may-2021;19-may-2021;21-mar-2021;");
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
        lcd.setCodigosNoPermitidos("20-may-2021;21-may-2021;20-may-2021;21-jun-2021;20-jun-2021;21-ene-2021;20-ene-2021;21-feb-2021;20-feb-2021;18-may-2021;19-may-2021;21-mar-2021;");
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
        lcd.setCodigosNoPermitidos("20-may-2021;21-may-2021;20-may-2021;21-jun-2021;20-jun-2021;21-ene-2021;20-ene-2021;21-feb-2021;20-feb-2021;18-may-2021;19-may-2021;21-mar-2021;");
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
        lcd.setCodigosNoPermitidos("20-may-2021;21-may-2021;20-may-2021;21-jun-2021;20-jun-2021;21-ene-2021;20-ene-2021;21-feb-2021;20-feb-2021;18-may-2021;19-may-2021;21-mar-2021;");
        getListaCompraDetalleDao().insert(lcd);


    }

    private void prepopulateder( ) {

        Reactivo r=new Reactivo();
        r.setDescripcion("tomado de");
        r.setEstatus(1);
        r.setTipoReactivo("L");
        HashMap map=new HashMap<String,String>();
        map.put("1","Anaquel");
        map.put("2","Refrigerador");
        map.put("3","Estante");
        r.setLista(map);
        getReactivoDao().insert(r);

        r=new Reactivo();
        r.setDescripcion("Costo");
        r.setEstatus(1);
        r.setTipoReactivo("A");

        getReactivoDao().insert(r);
        r=new Reactivo();
        r.setDescripcion("Codigo producción");
        r.setEstatus(1);
        r.setTipoReactivo("I");

        r.setLista(map);
        getReactivoDao().insert(r);
        r=new Reactivo();
        r.setDescripcion("Muestra con numero");
        r.setEstatus(1);
        r.setTipoReactivo("I");

        getReactivoDao().insert(r);

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
        lista=new ArrayList<>();
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
        getCatalogoDao().insertAll(lista);
    }
  /*  static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("alter table imagenes add column created_at timestamp");
        }
    };
*/

}


