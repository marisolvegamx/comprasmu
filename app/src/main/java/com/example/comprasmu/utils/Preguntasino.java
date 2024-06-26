package com.example.comprasmu.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    public Preguntasino(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }
    public Preguntasino(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.preguntasino, this);
        label = this.findViewById(R.id.cvtxtlabel);
        si = this.findViewById(R.id.cvrbsi);
        no = this.findViewById(R.id.cvrbno);
        this.setOrientation(VERTICAL);

    }

    public void onclicksi(OnClickListener clicksi) {
        si.setOnClickListener(clicksi);
    }

    public void onclickno(OnClickListener clickno) {
        no.setOnClickListener(clickno);
    }
    public void setOnCheckedChangeListener( RadioGroup.OnCheckedChangeListener ccl){
        RadioGroup rg=this.findViewById(R.id.cvradiogpo);
        rg.setOnCheckedChangeListener(ccl);
    }

    public void setmLabel(String pregunta) {
        label.setText(pregunta);
    }
    public void setStyleLabel(int style){
        label.setTextAppearance(style);
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
    public void setSi(boolean val){
        if(val)
            si.setChecked(val);

    }
    public void setNo(boolean val){
        if(val)
            no.setChecked(val);

    }

}
