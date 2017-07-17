package mobile.beweb.fondespierre.apprenantstest;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mobile.beweb.fondespierre.apprenantstest.Adapter.ListApprenantAdapter;

public class MainActivity extends AppCompatActivity implements ListApprenantAdapter.ListApprenantAdapterOnClickHandler {
    private static final String TAG = "MainActivity";
    private JSONArray apprenantsData = null;
    private RecyclerView mRecyclerView;
    private ListApprenantAdapter listeApprenantAdapter;

    private String promoFilter = "Toutes";
    private String sessionFilter = "Toutes";
    private String skillsFilter = "Tous";

    /**
     * Filter apprenantsData and updates the list
     */
    private void filterApprenantsData() {
        JSONArray apprenantsDataFilter = new JSONArray();

        try {
            for(int i = 0; i < apprenantsData.length(); i++) {
                JSONObject json_data = apprenantsData.getJSONObject(i);
                if ((promoFilter.equals("Toutes") || json_data.getString("ville").equals(promoFilter)) &&
                    (sessionFilter.equals("Toutes") || json_data.getString("session").equals(sessionFilter)) &&
                    (skillsFilter.equals("Tous") || json_data.getString("skill").equals(skillsFilter))) {
                    apprenantsDataFilter.put(json_data);
                }
            }

            listeApprenantAdapter.setWeatherData(apprenantsDataFilter);
        }
        catch(Exception ex) {
            Log.d("Erreur : ", ex.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_liste_apprenant);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        listeApprenantAdapter = new ListApprenantAdapter(this);
        mRecyclerView.setAdapter(listeApprenantAdapter);

        // Promo spinner event
        Spinner promo_spinner = (Spinner) findViewById(R.id.la_spinner_promo);
        promo_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                promoFilter = adapterView.getItemAtPosition(i).toString();
                filterApprenantsData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // Session spinner event
        Spinner session_spinner = (Spinner) findViewById(R.id.la_spinner_session);
        session_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sessionFilter = adapterView.getItemAtPosition(i).toString();
                filterApprenantsData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // Skills spinner event
        Spinner skills_spinner = (Spinner) findViewById(R.id.la_spinner_skills);
        skills_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                skillsFilter = adapterView.getItemAtPosition(i).toString();
                filterApprenantsData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // Gets apprenants list as json array and update the listview
        RequestQueue queue = VolleySingleton.getInstance(MainActivity.this).getRequestQueue();
        JsonArrayRequest jsonreq = new JsonArrayRequest("http://900grammes.fr/api",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Data : ", String.valueOf(response.length()));
                        apprenantsData = response;
                        listeApprenantAdapter.setWeatherData(apprenantsData);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonreq);
    }

    @Override
    public void onClick(JSONObject apprenantDetails) {
        Intent intent = new Intent(MainActivity.this, DetailapprenantActivity.class);
        intent.putExtra("apprenant",apprenantDetails.toString());
        MainActivity.this.getApplicationContext().startActivity(intent);
    }

}