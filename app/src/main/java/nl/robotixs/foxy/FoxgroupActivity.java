package nl.robotixs.foxy;


import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import nl.robotixs.foxy.app.AppConfig;
import nl.robotixs.foxy.app.AppController;
import nl.robotixs.foxy.app.SessionManager;

public class FoxgroupActivity extends AppCompatActivity implements View.OnClickListener {

    //TextView tvLogin;
    TextInputLayout foxgroupName;
    TextInputLayout foxgroupInfo;
    TextInputLayout foxgroupAvatar;
    TextInputLayout foxgroupProv;
    TextInputLayout foxgroupCountry;
    EditText etfoxgroupName;
    EditText etfoxgroupInfo;
    EditText etfoxgroupAvatar;
    EditText etfoxgroupProv;
    EditText etfoxgroupCountry;

    Button fgregisterButton;

    SessionManager session;

    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foxgroup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //initializing Views
        fgregisterButton = (Button) findViewById(R.id.fg_register_button);
        foxgroupName = (TextInputLayout) findViewById(R.id.fg_name_registerlayout);
        foxgroupInfo = (TextInputLayout) findViewById(R.id.fg_info_registerlayout);
        foxgroupAvatar = (TextInputLayout) findViewById(R.id.fg_avatar_registerlayout);
        foxgroupProv = (TextInputLayout) findViewById(R.id.fg_prov_registerlayout);
        foxgroupCountry = (TextInputLayout) findViewById(R.id.fg_country_registerlayout);
        etfoxgroupName = (EditText) findViewById(R.id.fg_name_register);
        etfoxgroupInfo = (EditText) findViewById(R.id.fg_info_register);
        etfoxgroupAvatar = (EditText) findViewById(R.id.fg_avatar_register);
        etfoxgroupProv = (EditText) findViewById(R.id.fg_prov_register);
        etfoxgroupCountry = (EditText) findViewById(R.id.fg_country_register);
        //tvLogin = (TextView) findViewById(R.id.tv_signin);
        // Set up action bar.
        //final ActionBar actionBar = getActionBar();

        // Specify that the Home button should show an "Up" caret, indicating that touching the
        // button will take the user one step up in the application's hierarchy.
        //actionBar.setDisplayHomeAsUpEnabled(true);

        //tvLogin.setOnClickListener(this);
        fgregisterButton.setOnClickListener(this);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());


        // Check if user is already logged in
        /**if (session.isLoggedIn()) {
         // User is already logged in. Move to main activity
         Intent intent = new Intent(FoxgroupActivity.this,
         FoxgroupActivity.class);
         startActivity(intent);
         finish();
         }*/

    }

    /*
    function to register user details in mysql database
     */
    private void registerFoxgroup(final String fg_name, final String fg_info,
                                  final String fg_avatar, final String fg_prov, final String fg_country) {
        // Tag used to cancel the request
        String tag_string_req = "req_fgregister";

        pDialog.setMessage("Foxgroup Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject foxgroup = jObj.getJSONObject("foxgroup");
                        /**
                         String fg_name = foxgroup.getString("fg_name");
                         String fg_info = foxgroup.getString("fg_info");
                         String fg_avatar = foxgroup.getString("fg_avatar");
                         String fg_prov = foxgroup.getString("fg_prov");
                         String fg_country = foxgroup.getString("fg_country");
                         String fg_created_at = foxgroup.getString("fg_created_at");

                         AppController.setString(FoxgroupActivity.this, "uid", uid);
                         AppController.setString(FoxgroupActivity.this, "fg_name", fg_name);
                         AppController.setString(FoxgroupActivity.this, "fg_info", fg_info);
                         AppController.setString(FoxgroupActivity.this, "fg_avatar", fg_avatar);
                         AppController.setString(FoxgroupActivity.this, "fg_prov", fg_prov);
                         AppController.setString(FoxgroupActivity.this, "fg_country", fg_country);
                         AppController.setString(FoxgroupActivity.this, "fg_created_at", fg_created_at);
                         */

                        // Launch login activity
                        Intent intent = new Intent(
                                FoxgroupActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "fgregister");
                params.put("fg_name", fg_name);
                params.put("fg_info", fg_info);
                params.put("fg_avatar", fg_avatar);
                params.put("fg_prov", fg_prov);
                params.put("fg_country", fg_country);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_signin:
                Intent intent = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(intent);
                finish();
            case R.id.fg_register_button:
                String fg_name = etfoxgroupName.getText().toString();
                String fg_info = etfoxgroupInfo.getText().toString();
                String fg_avatar = etfoxgroupAvatar.getText().toString();
                String fg_prov = etfoxgroupProv.getText().toString();
                String fg_country = etfoxgroupCountry.getText().toString();

                if (!fg_name.isEmpty() && !fg_info.isEmpty() && !fg_avatar.isEmpty() && !fg_prov.isEmpty() && !fg_country.isEmpty()) {
                    registerFoxgroup(fg_name, fg_info, fg_avatar, fg_prov, fg_country);
                } else {
                    Snackbar.make(v, "Please enter the credentials!", Snackbar.LENGTH_LONG)
                            .show();
                }
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_foxgroup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This is called when the Home (Up) button is pressed in the action bar.
                // Create a simple intent that starts the hierarchical parent activity and
                // use NavUtils in the Support Package to ensure proper handling of Up.
                Intent upIntent = new Intent(this, MainActivity.class);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is not part of the application's task, so create a new task
                    // with a synthesized back stack.
                    TaskStackBuilder.from(this)
                            // If there are ancestor activities, they should be added here.
                            .addNextIntent(upIntent)
                            .startActivities();
                    finish();
                } else {
                    // This activity is part of the application's task, so simply
                    // navigate up to the hierarchical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings_foxgroup) {
            /**Intent settings = new Intent(getApplicationContext(), SettingsActivity.class);
            settings.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(settings);
            // Closing dashboard screen
            finish();*/
            return true;
        }

        if (id == R.id.logout_foxgroup) {
            session.setLogin(false);
            Intent intent = new Intent(FoxgroupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }
}
