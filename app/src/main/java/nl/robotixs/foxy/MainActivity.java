package nl.robotixs.foxy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import nl.robotixs.foxy.app.AppController;
import nl.robotixs.foxy.app.SessionManager;

public class MainActivity extends AppCompatActivity {

    TextView txtName;
    TextView txtEmail;

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);

        session = new SessionManager(getApplicationContext());

        String name = AppController.getString(getApplicationContext(), "name");
        String email = AppController.getString(getApplicationContext(), "email");
        txtName.setText(name);
        txtEmail.setText(email);

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.addfoxgroupmainmenu) {
            Intent addfoxgroup = new Intent(getApplicationContext(), FoxgroupActivity.class);
            addfoxgroup.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(addfoxgroup);
            // Closing dashboard screen
            finish();
            //return true;
        }

        if (id == R.id.settingsmainmenu) {
            return true;
        }

        if (id == R.id.logoutmainmenu) {
            session.setLogin(false);
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }
}
