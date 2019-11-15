package com.example.drawingdemo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements ToggleButton.OnClickListener, Spinner.OnItemSelectedListener {

    private DrawingView drawingView;
    private Spinner drawingPicker;
    private ToggleButton toggleColor;
    private Button undoButton;

    @Override
    public void onClick(View view) {
        if (view.getId() == this.toggleColor.getId()) {
            if (this.drawingView.getColor() == Color.GREEN)
                this.drawingView.setColor(Color.WHITE);
            else
                this.drawingView.setColor(Color.GREEN);
            this.toggleColor.setBackgroundColor(this.drawingView.getColor());
            this.toggleColor.setTag(this.drawingView.getColor());
            this.drawingView.setToggleButtonColor(this.drawingView.getColor());
            System.out.println("Color Changed");
        }
        if (view.getId() == this.undoButton.getId()) {
            this.drawingView.undo();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            this.drawingView.setDrawingType((String) this.drawingPicker.getItemAtPosition(i));
            System.out.println("Current Shape: " + this.drawingPicker.getItemAtPosition(i));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.drawingView = findViewById(R.id.drawingView);
        this.drawingPicker = findViewById(R.id.spinner);
        this.toggleColor = findViewById(R.id.toggleButton);
        this.toggleColor.setOnClickListener(this);
        this.undoButton = findViewById(R.id.button);
        this.undoButton.setOnClickListener(this);


        this.drawingPicker.setOnItemSelectedListener(this);
        this.toggleColor.setBackgroundColor(Color.GREEN);
        this.toggleColor.setTag(Color.GREEN);
        this.drawingView.setToggleButtonColor(Color.GREEN);

    }
}
