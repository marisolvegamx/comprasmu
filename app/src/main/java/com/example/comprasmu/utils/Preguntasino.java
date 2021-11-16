package com.example.comprasmu.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.comprasmu.R;

public class Preguntasino extends LinearLayout {

    TextView label;
    RadioButton si;
    RadioButton no;

    public Preguntasino(Context context) {
        super(context);
        initializeViews(context);
    }

    public Preguntasino(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.preguntasino, this);
        label = (TextView) this.findViewById(R.id.cvtxtlabel);
        si = (RadioButton) this.findViewById(R.id.cvrbsi);
        no = (RadioButton) this.findViewById(R.id.cvrbno);
        this.setOrientation(VERTICAL);
    }

    public void onclicksi(OnClickListener clicksi) {
        si.setOnClickListener(clicksi);
    }

    public void onclickno(OnClickListener clickno) {
        no.setOnClickListener(clickno);
    }

    public void setmLabel(String pregunta) {
        label.setText(pregunta);
    }


    public void setEnabled(boolean valor) {
        this.no.setEnabled(valor);
        this.si.setEnabled(valor);
    }
    public void setVisible(int valor){
        this.label.setVisibility(valor);
        this.no.setVisibility(valor);
        this.si.setVisibility(valor);
    }

    /*** si=true
     * no=false
     * @return
     */
    public boolean getRespuesta(){
        return si.isChecked();

    }

}
