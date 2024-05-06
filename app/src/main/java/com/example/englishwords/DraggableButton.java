package com.example.englishwords;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("AppCompatCustomView")
public class DraggableButton extends androidx.appcompat.widget.AppCompatButton implements View.OnTouchListener {

    private float dX, dY;
    private String correspondingText;
    private boolean isCorrect = false;

    public DraggableButton(Context context) {
        super(context);
        init();
    }

    public DraggableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DraggableButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setCorrespondingText(String text) {
        correspondingText = text;
    }

    public String getCorrespondingText() {
        return correspondingText;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
        if (correct) {
            setBackgroundColor(Color.GREEN);
        } else {
            setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private void init() {
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Get initial touch coordinates
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                // Update position as the button is being dragged
                view.animate()
                        .x(event.getRawX() + dX)
                        .y(event.getRawY() + dY)
                        .setDuration(0)
                        .start();
                break;

            default:
                return false;
        }
        return true;
    }
}
