package com.example.comprasmu.ui.informe;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.Visita;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.ui.visita.AbririnformeFragment;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.CreadorFormulario;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

public class NuevaFotoExhibFragment extends Fragment {

    private static final String CLIENTESEL = "comprasmu.fe.clientesel";
    private NuevaFotoExhibViewModel mViewModel;
    private ImageView imagen1;
    private EditText ruta1;
    private static final int REQUEST_CODE_TAKE_PHOTO = 1;

    private static final String IMG_PATH1 = "comprasmu.img_path1";
      private static final String TAG = NuevaFotoExhibFragment.class.getName();
     private static final int INTERVALO = 3000; //2 segundos para salir
    private long tiempoPrimerClick;
    private ListaDetalleViewModel lViewModel;
    String nombre_foto;
    Bitmap rotatedBitmap;
    Spinner spinn;
    int visitasId;
    public static final String ARG_VISITASID ="comprasmu.fevisitasid" ;
    public static NuevaFotoExhibFragment newInstance() {
        return new NuevaFotoExhibFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {





        return inflater.inflate(R.layout.nueva_foto_exhib_fragment, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ruta1 = (EditText) view.findViewById(R.id.txtferutafoto);

        imagen1=(ImageView) view.findViewById(R.id.ivfefotoproducto);
        Bundle extras = getActivity().getIntent().getExtras();
        nombre_foto=extras.getString(NuevoinformeFragment.ARG_FOTOPRODUCTO);
        visitasId=extras.getInt(NuevaFotoExhibFragment.ARG_VISITASID);
        lViewModel=new ViewModelProvider(this).get(ListaDetalleViewModel.class);
        File file = new File(getActivity().getExternalFilesDir(null)+"/"+nombre_foto);
        if (file.exists()) {
            //   Bitmap imageBitmap = (Bitmap) extras.get("data");
            if(AbririnformeFragment.getAvailableMemory(getActivity()).lowMemory)
            {
                Toast.makeText(getContext(), getString(R.string.error_memoria), Toast.LENGTH_SHORT).show();
                return;
            }
            Bitmap bitmap1= BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null)+"/"+nombre_foto);
          // comprimo imagen
            ComprasUtils cu=new ComprasUtils();
            bitmap1=cu.comprimirImagen(getActivity().getExternalFilesDir(null) + "/" + nombre_foto);


            imagen1.setImageBitmap(bitmap1);
            ruta1.setText( nombre_foto);
        }

        ImageButton btnrotar=view.findViewById(R.id.btnnfrotar);
        btnrotar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotarImagen(view);
            }
        });
        Button btnguardar=view.findViewById(R.id.btnfeguardar);
        btnguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarFotoExhibido();
            }
        });

        if (savedInstanceState != null) {
            // Restore value of counters from saved state
            ruta1.setText( savedInstanceState.getString(IMG_PATH1));

            Bitmap bitmap1= BitmapFactory.decodeFile(ruta1.getText().toString());

            imagen1.setImageBitmap(bitmap1);
            Log.d(TAG, "Restoring Data in onCreate savedInstanceState != null");
        }
        //llenar lista de clientes
         spinn=view.findViewById(R.id.spteclientes);

        //cargo la lista de clientes
        cargarClientes();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NuevaFotoExhibViewModel.class);

    }
    public void cargarClientes() {
        Log.d(TAG, "regresó de la consulta " + Constantes.CIUDADTRABAJO);
     //   if (Constantes.clientesAsignados == null||Constantes.clientesAsignados.size()<1)
        lViewModel.cargarClientes(Constantes.CIUDADTRABAJO).observe(getViewLifecycleOwner(), data -> {
            Log.d(TAG, "regresó de la consulta " + data.size());
            Constantes.clientesAsignados = ComprasUtils.convertirListaaClientes(data);
            CreadorFormulario.cargarSpinnerDescr(getContext(),spinn,Constantes.clientesAsignados);

         });
      //  else
       //     CreadorFormulario.cargarSpinnerDescr(getContext(),spinn,Constantes.clientesAsignados);

    }

   public void guardarFotoExhibido(){
        if(ruta1.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Falta tomar la foto", Toast.LENGTH_LONG).show();
            return;

        }
       if(spinn.getSelectedItem()==null){
           Toast.makeText(getActivity(), "Falta seleccionar cliente", Toast.LENGTH_LONG).show();
            return;

       }
       DescripcionGenerica cliente=(DescripcionGenerica)spinn.getSelectedItem();
       NuevoinformeViewModel ninfViewModel =
               new ViewModelProvider(this).get(NuevoinformeViewModel.class);

       mViewModel.guardarFoto(ruta1.getText().toString(),cliente.getId(),cliente.getNombre(),visitasId,getActivity(),ninfViewModel);
       Toast.makeText(getActivity(), "Se agregó la foto", Toast.LENGTH_LONG).show();
       getActivity().finish();

   }



    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putString(IMG_PATH1, ruta1.getText().toString());
        savedInstanceState.putString(CLIENTESEL, spinn.getSelectedItem().toString());

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
  /*  @Override
    public void onBackPressed(){
        if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else {
            Toast.makeText(this, "Sí sale sin guardar la información esta se perderá. Vuelve a presionar para salir", Toast.LENGTH_LONG).show();
        }
        tiempoPrimerClick = System.currentTimeMillis();
    }
   */

    public void rotarImagen(View v){
        if(AbririnformeFragment.getAvailableMemory(getActivity()).lowMemory)
        {
            Toast.makeText(getContext(), getString(R.string.error_memoria), Toast.LENGTH_SHORT).show();
            return;
        }
        Bitmap bitmapOrg=BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null)+"/"+nombre_foto);

        int width=bitmapOrg.getWidth();
        int height=bitmapOrg.getHeight();

        Matrix matrix = new Matrix();

        matrix.postRotate(90);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmapOrg, width, height, true);

        rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        //comprimir imagen
       /* File file = new File(getActivity().getExternalFilesDir(null)+"/"+nombre_foto);
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            Log.d("Compras",e.getMessage());
            Toast.makeText(getActivity(),"Hubo un error fatal", Toast.LENGTH_SHORT).show();

        }
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);*/

        imagen1.setImageBitmap(rotatedBitmap);


    }

    // Get a MemoryInfo object for the device's current memory status.
    private ActivityManager.MemoryInfo getAvailableMemory() {
        ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }
    public void cancelar(View v) {
        if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()){
          /*  super.onBackPressed();
            return;*/
           getActivity().finish();
        }else {
            Toast.makeText(getActivity(), "Sí sale sin guardar la información esta se perderá. Vuelve a presionar para salir", Toast.LENGTH_LONG).show();
        }
        tiempoPrimerClick = System.currentTimeMillis();

    }





}