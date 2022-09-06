package com.example.comprasmu.ui.mantenimiento;

import android.app.Activity;
import android.os.Bundle;

import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comprasmu.R;
import com.example.comprasmu.utils.ComprasLog;

import java.io.FileNotFoundException;
import java.io.IOException;

public class LeerLogActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leer_log);

        mTextView = (TextView) findViewById(R.id.txtlllog);

        // Enables Always-on
        ComprasLog flog=ComprasLog.getSingleton();

            try {
                String todo = flog.leerArch();
                mTextView.setText(todo);
            }catch (Exception e) {
                e.printStackTrace();
            }

    }
}