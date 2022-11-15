package com.example.comprasmu.ui.mantenimiento;


import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.services.SubirLogService;


public class LeerLogActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leer_log);

        mTextView = findViewById(R.id.txtlllog);
        Log.d("hola","hola");
        // Enables Always-on
      /*  ComprasLog flog=ComprasLog.getSingleton();

            try {
                String todo = flog.leerArch();
                mTextView.setText(todo);
            }catch (Exception e) {
                e.printStackTrace();
            }*/

        //envio el log al servidor que lo guardará en la carpeta del usuario

        Intent msgIntent = new Intent(this, SubirLogService.class);
        msgIntent.putExtra(SubirLogService.EXTRA_LOG_PATH, "compraslog.txt");
        msgIntent.setAction(SubirLogService.ACTION_UPLOAD_LOG);
        startService(msgIntent);
        finish();
    }
}