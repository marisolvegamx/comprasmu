package com.example.comprasmu.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.comprasmu.data.dao.InformeCompraDao;
import com.example.comprasmu.data.modelos.InformeCompra;


import java.util.List;

public class InformeCompraRepositoryImplresp {

   // private static InformeCompraRepository sInstance;
    private InformeCompraDao icDao;
    private LiveData<List<InformeCompra>> allInformeCompra;

   /* public InformeCompraReInformeCompraRepositoryImplp(Context context) {
        WordDatabase wordDatabase = WordDatabase.getWordDatabase(context.getApplicationContext());
        icDao = wordDatabase.getInformeCompraDao();

    }

    public static InformeCompraRepositoryImplresp getsInstance() {
        if (sInstance == null) {
            sInstance = new InformeCompraRepositoryImplresp();
        }
        return sInstance;
    }
    private void asyncFinished(LiveData<List<InformeCompra>> results) {
        allInformeCompra=results;
    }



    @Override
    public LiveData<List<InformeCompra>> getAllInformeCompra() {
        QueryAsyncTask task = new QueryAsyncTask(icDao);
        task.delegate = this;
        task.execute(name);
    }

    @Override
    public MutableLiveData<List<InformeCompra>> getSearchResults() {
        return null;
    }

    @Override
    public void insertInformeCompra(InformeCompra newInformeCompra) {
        new InsertAsyncTask(icDao).execute(newInformeCompra);
    }

    @Override
    public void deleteInformeCompra(int id) {
        new DeleteAsyncTask(icDao).execute(informeCompra);
    }

    @Override
    public void findInformeCompra(int id) {

    }

    static class InsertAsyncTask extends AsyncTask<InformeCompra, Void, Void> {

        private InformeCompraDao icDao;

        public InsertAsyncTask(InformeCompraDao icDao) {
            this.icDao = icDao;
        }

        @Override
        protected Void doInBackground(InformeCompra... informeCompra) {

            icDao.addInforme(informeCompra[0]);
            return null;
        }
    }


    static class DeleteAllAsyncTask extends AsyncTask<String, Void, Void> {

        private InformeCompraDao icDao;

        public DeleteAllAsyncTask(InformeCompraDao icDao) {
            this.icDao = icDao;
        }

        @Override
        protected Void doInBackground(String... indice) {
            icDao.deleteInformesByIndice(indice[0]);
            return null;
        }


    }

    static class DeleteAsyncTask extends AsyncTask<int, Void, Void> {

        private InformeCompraDao icDao;

        public DeleteAsyncTask(InformeCompraDao icDao) {
            this.icDao = icDao;
        }

        @Override
        protected Void doInBackground(int... informeCompra) {
            icDao.deleteInforme(informeCompra[0]);
            return null;
        }
    }
    private static class QueryAsyncTask extends
            AsyncTask<String, Void, List<InformeCompra>> {

        private InformeCompraDao asyncTaskDao;
        private InformeCompraRepositoryImplresp delegate = null;

        QueryAsyncTask(InformeCompraDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected List<InformeCompra> doInBackground(final String... params) {
            return asyncTaskDao.(params[0]);
        }

        @Override
        protected void onPostExecute(List<InformeCompra> result) {
            delegate.asyncFinished(result);
        }
    }
}
*/

}
