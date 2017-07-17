package mobile.beweb.fondespierre.apprenantstest.Adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mobile.beweb.fondespierre.apprenantstest.MainActivity;
import mobile.beweb.fondespierre.apprenantstest.R;

import static android.support.constraint.R.id.parent;

public class ListApprenantAdapter extends RecyclerView.Adapter<ListApprenantAdapter.ListApprenantAdapterViewHolder> {

    private JSONArray apprenantsData;
    String nom, prenom, skill;
    private Context context;

    private final ListApprenantAdapterOnClickHandler mClickHandler;

    public ListApprenantAdapter(ListApprenantAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

    public interface ListApprenantAdapterOnClickHandler {
        void onClick(JSONObject apprenantDetail);
    }

    public class ListApprenantAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mApprenantNomTextView;
        public final TextView mApprenantPrenomTextView;
        public final TextView mApprenantSkillTextView;

        public ListApprenantAdapterViewHolder(View view) {
            super(view);
            mApprenantNomTextView = (TextView) view.findViewById(R.id.laItem_textview_nom);
            mApprenantPrenomTextView = (TextView) view.findViewById(R.id.laItem_textview_prenom);
            mApprenantSkillTextView = (TextView) view.findViewById(R.id.laItem_textview_skill);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("click", "toto");
            int adapterPosition = getAdapterPosition();
            try {
                JSONObject apprenantDetails = apprenantsData.getJSONObject(adapterPosition);
                mClickHandler.onClick(apprenantDetails);
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ListApprenantAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.listeapprenantitem;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ListApprenantAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListApprenantAdapterViewHolder listApprenantAdapterViewHolder, int position) {
        try {
            JSONObject apprenant;
            apprenant = apprenantsData.getJSONObject(position);

            nom = apprenant.getString("nom");
            prenom = apprenant.getString("prenom");
            skill = apprenant.getString("skill");

        } catch (final JSONException e) {

        }

        listApprenantAdapterViewHolder.mApprenantNomTextView.setText(nom);
        listApprenantAdapterViewHolder.mApprenantPrenomTextView.setText(prenom);
        listApprenantAdapterViewHolder.mApprenantSkillTextView.setText(skill);
    }

    @Override
    public int getItemCount() {
        if (null == apprenantsData) return 0;
        return apprenantsData.length();
    }

    public void setWeatherData(JSONArray apprenantsData) {
        this.apprenantsData = apprenantsData;
        notifyDataSetChanged();
    }

}