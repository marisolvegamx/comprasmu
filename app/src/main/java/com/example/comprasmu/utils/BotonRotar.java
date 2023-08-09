package com.example.comprasmu.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.comprasmu.R;

/**
 *  document your custom view class.
 */
public class BotonRotar extends LinearLayout {
    ImageView boton;

    public BotonRotar(Context context) {
        super(context);
        init(context);
    }

    public BotonRotar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BotonRotar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.boton_rotar, this);
        boton=this.findViewById(R.id.imageButton);

    }
    public void setOnClickListener(OnClickListener clickno) {

        boton.setOnClickListener(clickno);
    }

}