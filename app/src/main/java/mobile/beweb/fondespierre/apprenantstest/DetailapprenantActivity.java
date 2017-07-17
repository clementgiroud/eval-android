package mobile.beweb.fondespierre.apprenantstest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailapprenantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_apprenant);
        JSONObject aprennantJson=null;
        TextView nomT = (TextView) findViewById(R.id.da_textview_nom);
        TextView prenomT = (TextView) findViewById(R.id.da_textview_prenom);
        TextView villeT = (TextView) findViewById(R.id.da_textview_ville);
        TextView descriptionT = (TextView) findViewById(R.id.da_textview_description);
        RatingBar ratingbarT = (RatingBar) findViewById(R.id.da_ratingbar);

        String apprenant  = getIntent().getExtras().getString("apprenant");

        try {

            aprennantJson = new JSONObject(apprenant);
            nomT.setText(aprennantJson.getString("nom"));
            prenomT.setText(aprennantJson.getString("prenom"));
            villeT.setText(aprennantJson.getString("ville"));
            descriptionT.setText(aprennantJson.getString("description"));
            ratingbarT.setRating(aprennantJson.getInt("skill"));

        } catch (JSONException e) {
            Toast.makeText(this, "Erreur : "+e,
                    Toast.LENGTH_LONG)
                    .show();
        }

        String url = "https://randomuser.me/api/";
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("data", response.toString());


                        try {
                            String picture = response.getJSONArray("results")
                                    .getJSONObject(0)
                                    .getJSONObject("picture")
                                    .getString("thumbnail");
                            ImageView pictureView = (ImageView) findViewById(R.id.da_imageView);
                            new ImageLoadTask(picture, pictureView).execute();
                        } catch (final JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjReq);

        Button retour = (Button) findViewById(R.id.da_button_retour);
        retour.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                getApplicationContext().startActivity(intent);
            }
        });
    }
}
