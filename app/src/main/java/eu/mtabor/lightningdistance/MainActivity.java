package eu.mtabor.lightningdistance;

import android.app.Dialog;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import eu.mtabor.lightningdistance.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    boolean zacatek = true;
    androidx.navigation.NavDestination vychoziDestinace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        if (zacatek) {
            zacatek = false;
            vychoziDestinace = navController.getCurrentDestination();
        }


        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_help:
                if (vychoziDestinace == navController.getCurrentDestination())
                    navController.navigate(R.id.action_FirstFragment_to_SecondFragment);
                return true;
            case R.id.action_about:
                // Toast.makeText(getApplicationContext(), R.string.menu_main_about, Toast.LENGTH_LONG).show();
                dialogAbout();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void dialogAbout() {

        final Dialog dialog_About = new Dialog(this);
        dialog_About.setTitle(R.string.menu_main_about);
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