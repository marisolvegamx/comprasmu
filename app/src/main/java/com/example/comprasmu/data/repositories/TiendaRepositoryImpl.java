package com.example.comprasmu.data.repositories;


import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;
import com.example.comprasmu.data.dao.TiendaDao;
import com.example.comprasmu.data.modelos.Tienda;
import java.util.ArrayList;
import java.util.List;

public class TiendaRepositoryImpl extends BaseRepository<Tienda> {

    private static TiendaDao dao;
    private static TiendaRepositoryImpl INSTANCE;



    public static TiendaRepositoryImpl getInstance(TiendaDao comprasdao) {
        if (INSTANCE == null) {
            dao=comprasdao;
            synchronized (TiendaRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE=new TiendaRepositoryImpl();
                }
            }
        }
        return INSTANCE;
    }



    public LiveData<List<Tienda>> gettiendasByFiltros( String ciudad, String fechaini,String fechafin, int tipo, int cadena) {
        List<String> params= new ArrayList<>();

        String query="Select * from tienda where ciudad=? and " +
                " and str_to_date(concat('01.',indiceUltimaVisita ),'%d.%m.%Y') < DATE_ADD('"+fechafin+"', interval 1 month)" +
                " and str_to_date(concat('01.',indiceUltimaVisita ),'%d.%m.%Y') >= DATE_ADD('"+fechaini+"', interval 1 month) ";
        params.add(ciudad+"");

        if(tipo>0) {
            query = query + " and une_tipotienda=?";
            params.add(tipo+"");
        }
        if(cadena>0) {
            query = query + " and une_cadenacomercial=?";
            params.add(cadena+"");
        }

        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,params.toArray()
        );
        //Log.d("TiendaRepositoryImpl","clientes "+query);
        return dao.getTiendasByFiltros( sqlquery);
    }


    @Override
    public LiveData<List<Tienda>> getAll() {
      return dao.findAll();
    }

    //no está implementada
    @Override
    public List<Tienda> getAllsimple() {
        return null;
    }


    @Override
    public LiveData<Tienda> find(int id) {
        return dao.find(id);
    }

    @Override
    public Tienda findsimple(int id) {
        return null;
    }

    @Override
    public void delete(Tienda object) {
        dao.delete(object);
    }

    @Override
    public void insertAll(List<Tienda> objects) {
        dao.insertAll(objects);
    }

    @Override
    public long insert(Tienda object) {
        return dao.insert(object);
    }

}
