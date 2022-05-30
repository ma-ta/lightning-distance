package eu.mtabor.lightningdistance;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import eu.mtabor.lightningdistance.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    // proměnné a konstanty

    double cas_0     = 0;
    double cas_1     = 0;
    double cas_delta = 0;
    int teplota      = 20;
    double rychlost  = 0;
    long vzdalenost  = 0;

    final int color_bila = Color.WHITE;
    final int color_accent = Color.parseColor("#C1D9d4");

    String jednotka_vzdalenost = "m";
    String jednotka_teplota    = "°C";


    // GUI

    private static TextView textView_Vzdalenost;
    private static Button button_Teplota;
    private static Button button_Zmer;
    private static Chronometer chronometer1;
    private FragmentFirstBinding binding;




    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

/*        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });*/

        textView_Vzdalenost = (TextView) view.findViewById(R.id.textView_Vzdalenost);
        button_Teplota = (Button) view.findViewById(R.id.button_Teplota);
        button_Zmer = (Button) view.findViewById(R.id.button_Zmer);
        chronometer1 = (Chronometer) view.findViewById(R.id.chronometer1);

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

        button_Teplota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogTeplota(view);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }




    // vlastní metody

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

        final Dialog dialog_Teplota = new Dialog(getActivity());
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

}