package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.comprasmu.data.modelos.ListaCompra;

import java.lang.reflect.ParameterizedType;
import java.util.List;


public abstract class BaseDao<T> {
    T ob;

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insert(T object);


   /* @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<T> object);*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<T> objects);

    @Delete
    public abstract void delete(T object);

   /* @RawQuery(observedEntities = )
    protected abstract LiveData<List<T>> doFindAllValid(SupportSQLiteQuery query);

    @RawQuery(observedEntities = ob.getClass())
  /*  protected abstract LiveData<T> doFind(SupportSQLiteQuery query);

    public LiveData<List<T>> findAll() {

        SimpleSQLiteQuery query = new SimpleSQLiteQuery(
                "select * from " + getTableName() + " where estatus = 0 order by sortKey"
        );
        return doFindAllValid(query);
    }


    public LiveData<T> find(long id) {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(
                "select * from " + getTableName() + " where estatus = 0 and id = ?",
                new Object[]{id}
        );
        return doFind(query);
    }


    public String getTableName() {

        Class clazz = (Class)
                ((ParameterizedType) getClass().getSuperclass().getGenericSuperclass())
                        .getActualTypeArguments()[0];
        // tableName = StringUtil.toSnakeCase(clazz.getSimpleName());
        String tableName = clazz.getSimpleName();
        return tableName;
    }*/


}
