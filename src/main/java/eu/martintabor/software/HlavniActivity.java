package eu.martintabor.software;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.nearby.messages.internal.ClientAppContext;

import org.w3c.dom.Text;

import java.security.Timestamp;
import java.text.DateFormat;


public class HlavniActivity extends AppCompatActivity {

    double cas_0 = 0;
    double cas_1 = 0;
    double cas_delta = 0;
    int teplota = 20;
    double rychlost = 0;
    long vzdalenost = 0;

    int color_bila = Color.WHITE;
    int color_accent = Color.parseColor("#c1d9d4");

    String jednotka_vzdalenost = "m";
    String jednotka_teplota = "°C";


    private static TextView textView_Vzdalenost;
    private static Button button_Teplota;
    private static Button button_Zmer;
    private static Chronometer chronometer1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hlavni);

        textView_Vzdalenost = (TextView) findViewById(R.id.textView_Vzdalenost);
        button_Teplota = (Button) findViewById(R.id.button_Teplota);
        button_Zmer = (Button) findViewById(R.id.button_Zmer);
        chronometer1 = (Chronometer) findViewById(R.id.chronometer1);

        nastavZobrazovace();

        button_Zmer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        cas_0 = SystemClock.uptimeMillis();
                        chronometer1.setBase(SystemClock.elapsedRealtime());
                        chronometer1.start();
                        chronometer1.setTextColor(color_accent);
                        textView_Vzdalenost.setTextColor(color_bila);

                        return true;
                    case MotionEvent.ACTION_UP:
                        cas_1 = SystemClock.uptimeMillis();
                        spocitejVzdalenost();
                        chronometer1.stop();
                        textView_Vzdalenost.setText(String.valueOf(vzdalenost) + " " + jednotka_vzdalenost);
                        textView_Vzdalenost.setTextColor(color_accent);
                        chronometer1.setTextColor(color_bila);

                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hlavni, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            dialogAbout();

            return true;
        }
        else if (id == R.id.action_help) {
            //Toast.makeText(HlavniActivity.this, R.string.menu_hlavni_help, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, NapovedaActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void nastavZobrazovace() {
        textView_Vzdalenost.setText(String.valueOf(vzdalenost) + " " + jednotka_vzdalenost);
        button_Teplota.setText(String.valueOf(teplota) + " " + jednotka_teplota);
    }

    public void spocitejVzdalenost() {
        cas_delta = (cas_1 - cas_0) / 1000;
        rychlost = 331.57 + (0.607 * teplota);
        vzdalenost = Math.round(rychlost * cas_delta);
    }

    public void dialogTeplota(View v) {

        final Dialog dialog_Teplota = new Dialog(this);
        dialog_Teplota.setTitle(R.string.set_air_temp);
        dialog_Teplota.setContentView(R.layout.dialog_teplota);
        Button button_Potvrdit = (Button) dialog_Teplota.findViewById(R.id.button_potvrdit);
        Button button_Zrusit = (Button) dialog_Teplota.findViewById(R.id.button_zrusit);
        final NumberPicker numberPicker_Teplota = (NumberPicker) dialog_Teplota.findViewById(R.id.numberPicker_teplota);
        numberPicker_Teplota.setMaxValue(70);
        numberPicker_Teplota.setMinValue(0);
        numberPicker_Teplota.setValue(teplota);
        numberPicker_Teplota.setWrapSelectorWheel(false);
        button_Potvrdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teplota = numberPicker_Teplota.getValue();
                button_Teplota.setText(String.valueOf(teplota) + " " + jednotka_teplota);
                dialog_Teplota.dismiss();
            }
        });
        button_Zrusit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_Teplota.dismiss();
            }
        });
        dialog_Teplota.show();

    }

    public void dialogAbout() {

        final Dialog dialog_About = new Dialog(this);
        dialog_About.setTitle(R.string.menu_hlavni_about);
        dialog_About.setContentView(R.layout.dialog_about);
        Button button_OK = (Button) dialog_About.findViewById(R.id.button_ok);
        button_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_About.dismiss();
            }
        });
        dialog_About.show();

    }

}
