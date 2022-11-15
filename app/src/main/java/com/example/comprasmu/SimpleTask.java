package com.example.comprasmu;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.example.comprasmu.data.remote.InformeEnvio;
import com.example.comprasmu.ui.informe.PostInformeViewModel;

import java.util.ArrayList;

public class SimpleTask extends AsyncTask<Void, Integer, Void> {


    private final int[] numbers;
    public TextView progressLabel;



    public SimpleTask(int[] numbers) {

        this.numbers = numbers;
      //  this.progressLabel = progressLabel;
    }

    private final ArrayList<PruebaListener> observadores = new ArrayList<PruebaListener>();


    public void agregarObservador(PruebaListener o)
    {
        observadores.add(o);

    }
    public void notificarObservadores()
    {
        Log.d("Ejemplo "," Notificando");
        Log.d("Ejemplo","tamaño->"+observadores.size());
        // Enviar la notificación a cada observador a través de su propio método
        for (PruebaListener obj : observadores) {
            obj.onSuccess();
        }
    }

    /*
        Se hace visible el botón "Cancelar" y se desactiva
        el botón "Ordenar"
         */
    @Override
    protected void onPreExecute() {

    }



    /*
  Ejecución del ordenamiento y transmision de progreso
   */
    @Override
    protected Void doInBackground(Void... params) {
        int aux;

        for (int i = 0; i < numbers.length - 1; i++) {
            for (int j = 0; j < numbers.length -1; j++) {
                if (numbers[j] > numbers[j+1])
                {
                    aux          = numbers[j];
                    numbers[j]   = numbers[j+1];
                    numbers[j+1] = aux;
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // Notifica a onProgressUpdate() del progreso actual
            if(!isCancelled())
                publishProgress((int)(((i+1)/(float)(numbers.length-1))*100));
            else break;
            Log.d("Ejemplo ","Vuelta"+i);
        }
        Log.d("Ejemplo ","Ya terminé");
        notificarObservadores();
        return null;
    }

    /*
   Se informa en progressLabel que se canceló la tarea y
   se hace invisile el botón "Cancelar"
    */
    @Override
    protected void onCancelled() {
        super.onCancelled();
      //  progressLabel.setText("Tarea cancelada");

    }

    /*
 Impresión del progreso en tiempo real
   */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
  //      progressLabel.setText(values[0] + "%");
    }

    /*
    Se notifica que se completó el ordenamiento y se habilita
    de nuevo el botón "Ordenar"
     */
    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
     //   progressLabel.setText("Completado");

    }


}
