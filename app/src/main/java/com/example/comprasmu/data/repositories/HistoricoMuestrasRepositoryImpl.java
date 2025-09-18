package com.example.comprasmu.data.repositories;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;
import com.example.comprasmu.data.dao.HistoricoMuestrasDao;
import com.example.comprasmu.data.modelos.HistoricoMuestras;
import java.util.ArrayList;
import java.util.List;

public class HistoricoMuestrasRepositoryImpl extends BaseRepository<HistoricoMuestras> {

    private static HistoricoMuestrasDao dao;
    private static HistoricoMuestrasRepositoryImpl INSTANCE;
    public HistoricoMuestrasRepositoryImpl() {

    }

    public static HistoricoMuestrasRepositoryImpl getInstance(HistoricoMuestrasDao HistoricoMuestrasDao) {
        if (INSTANCE == null) {
            HistoricoMuestrasRepositoryImpl.dao=HistoricoMuestrasDao;
            synchronized (HistoricoMuestrasDao.class) {
                if (INSTANCE == null) {
                    INSTANCE=new HistoricoMuestrasRepositoryImpl();
                }
            }
        }
        return INSTANCE;

    }
    @Override
    public LiveData<List<HistoricoMuestras>> getAll() {
      return dao.findAll();
    }

    @Override
    public List<HistoricoMuestras> getAllsimple() {
        return dao.findAllsimple();
    }

  //no está implementado
    @Override
    public LiveData<HistoricoMuestras> find(int id) {
        return null;
    }
    //no está implementado
    @Override
    public HistoricoMuestras findsimple(int id) {
        return null;
    }


    public void deleteAll() {

        dao.deleteAll();
    }


    public List<HistoricoMuestras> getByIndice(String indice) {

        return dao.getByIndice(indice);
    }
    public List<HistoricoMuestras> getByPlanta(int plantaId) {

        return dao.getByPlanta(plantaId);
    }


    @Override
    public void insertAll(List<HistoricoMuestras> objects) {
        dao.insertAll(objects);
    }


    @Override
    public long insert(HistoricoMuestras object) {
        return dao.insert(object);
    }

    //no implementado
    @Override
    public void delete(HistoricoMuestras object) {

    }

    public void getByProducto(String indice1, String indice2, int planta, int producto, int analisis, int empaque, String tamanio) {

        dao.getByProducto( indice1,indice2,  planta,  producto,  analisis,  empaque,  tamanio);
    }

    public List<HistoricoMuestras> getDetalleByFiltros(int plantaId, int analisis, String productoNombre, String empaque, int tamanio, String indice1, String indice2 ) {

        String query="select  hisId," +
                     "  inf_indice," +
                     "  plantaId," +
                     "  clienteId," +
                     "  ind_informes_id," +
                     "  ind_id," +
                     "  productoId," +
                     "  producto ," +
                     "  tamanioId," +
                     "  presentacion," +
                     "  empaquesId," +
                     "  empaque,tipoAnalisis," +
                     "  nombreAnalisis," +
                     "  categoriaId," +
                     "  categoriaNombre," +
                     "  caducidad" +
                     "  from historico_muestras where   producto=? and empaque=? and tamanioId=? and tipoAnalisis=?" +
                     "  and inf_indice in (?, ?) and plantaId=?" +
                     "  group by caducidad order by caducidad desc";
        ArrayList<String> filtros=new ArrayList<String>();
        filtros.add(productoNombre);
        filtros.add(empaque);
        filtros.add(tamanio+"");
        filtros.add(analisis+"");
        filtros.add(indice1);
        filtros.add(indice2);
        filtros.add(plantaId+"");
        Object[] params=filtros.toArray();

        for(int i=0;i<params.length;i++)
            Log.d("HistoricoMuestrasRepositoryImpl","***"+params[i]);
        Log.d("HistoricoMuestrasRepositoryImpl","****"+query);
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,filtros.toArray()
        );

        return dao.getDetallesByFiltros(sqlquery);
    }
}
