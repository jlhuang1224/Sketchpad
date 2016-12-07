package homework.cisc.mlgdev.com.sketchpad;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DrawingView drawingView;

    Button buttonClear, buttonColor, buttonBrushSize;

    ToggleButton toggleErase;

    // create a ColorPicker and set the initial color in RGB
    ColorPicker colorPicker;
    // selected paint color
    int selectedColorRGB;

    // brush size
    int brushSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        drawingView = (DrawingView) findViewById(R.id.drawingView);

        buttonClear = (Button) findViewById(R.id.buttonClear);
        buttonClear.setOnClickListener(this);

        buttonColor = (Button) findViewById(R.id.buttonColor);
        buttonColor.setBackgroundColor(Color.BLACK);
        buttonColor.setOnClickListener(this);

        buttonBrushSize = (Button) findViewById(R.id.buttonBrushSize);
        buttonBrushSize.setOnClickListener(this);

        toggleErase = (ToggleButton) findViewById(R.id.toggleErase);
        toggleErase.setOnClickListener(this);

        colorPicker = new ColorPicker(this, 0, 0, 0);

        // initialize brushSize to 50
        brushSize = 50;
        drawingView.setBrushSize(50);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.buttonClear:
                ShowConfirmClearDialog();
                break;
            case R.id.buttonColor:
                colorPicker.show();

                Button okColor = (Button) colorPicker.findViewById(R.id.okColorButton);
                okColor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get the Android RGB color
                        selectedColorRGB = colorPicker.getColor();
                        Log.i("INFO", "The selected color is: " + selectedColorRGB);
                        drawingView.setPaintColor(selectedColorRGB);
                        buttonColor.setBackgroundColor(selectedColorRGB);
                        if(toggleErase.isChecked()) {
                            toggleErase.setChecked(false);
                        }

                        colorPicker.dismiss();
                    }
                });
                break;
            case R.id.toggleErase:
                if(toggleErase.isChecked()) {
                    Log.i("INFO", "We are erasing now.");
                    drawingView.setPaintColor(Color.WHITE);
                } else {
                    Log.i("INFO", "We have stopped erasing.");
                    drawingView.setPaintColor(selectedColorRGB);
                }
                break;
            case R.id.buttonBrushSize:
                ShowSizeDialog();
        }
    }

    public void ShowConfirmClearDialog() {
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
        //popDialog.setIcon(android.R.drawable.btn_star_big_on);
        popDialog.setTitle("Confirm");
        popDialog.setMessage("Are you sure you want to clear the screen?");

        // Button OK
        popDialog.setPositiveButton("Okay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // clear the canvas
                        drawingView.clearCanvas();
                        // invalidate to force it to redraw
                        drawingView.invalidate();
                        if(toggleErase.isChecked()) {
                            drawingView.setPaintColor(selectedColorRGB);
                            toggleErase.setChecked(false);
                        }
                    }

                });
        // Button Not OK
        popDialog.setNegativeButton("Not Okay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // dismiss the dialog
                        dialog.dismiss();
                    }
                });


        popDialog.create();
        popDialog.show();
    }

    public void ShowSizeDialog()
    {
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
        final SeekBar seek = new SeekBar(this);
        seek.setMax(200);
        seek.setProgress(brushSize);

        //popDialog.setIcon(android.R.drawable.btn_star_big_on);
        popDialog.setTitle("Brush Size");
        popDialog.setView(seek);

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                //Do something here with new value
                //Log.i("INFO", "Progress is " + progress);
                brushSize = progress;
            }
            public void onStartTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });


        // Button OK
        popDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        drawingView.setBrushSize(brushSize);
                        dialog.dismiss();
                    }

                });


        popDialog.create();
        popDialog.show();

    }
}
