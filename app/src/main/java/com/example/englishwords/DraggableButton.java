package com.example.englishwords;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Objects;

@SuppressLint("AppCompatCustomView")
public class DraggableButton extends androidx.appcompat.widget.AppCompatButton implements View.OnTouchListener {

    private float dX, dY;
    private String correspondingText;
    private boolean isCorrect = false;

    public DraggableButton(Context context) {
        super(context);
        init();
    }
    public boolean isCorrect() {
        return isCorrect;
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
        setBackgroundColor(Color.rgb(99,5,220));
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DraggableButton that = (DraggableButton) o;
        return Objects.equals(correspondingText, that.correspondingText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(correspondingText);
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

                // Start a drag-and-drop operation
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDragAndDrop(null, shadowBuilder, view, 0);
                return true;

            case MotionEvent.ACTION_MOVE:
                // Update position as the button is being dragged
                view.animate()
                        .x(event.getRawX() + dX)
                        .y(event.getRawY() + dY)
                        .setDuration(0)
                        .start();
                return true;

            default:
                return false;
        }
    }
}
