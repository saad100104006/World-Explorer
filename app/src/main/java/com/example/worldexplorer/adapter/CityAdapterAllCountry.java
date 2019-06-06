package com.example.worldexplorer.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.worldexplorer.MyApp;
import com.example.worldexplorer.R;
import com.example.worldexplorer.activity.MapsActivity;
import com.example.worldexplorer.model.city;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.example.worldexplorer.utils.Constants.BASE_URL;


public class CityAdapterAllCountry extends ArrayAdapter<city> {
    private final Context context;
    List<city> values;

    public CityAdapterAllCountry(Context context, List<city> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_city, parent, false);
        TextView countryName = (TextView) rowView.findViewById(R.id.CountryName);
        TextView cityName = (TextView) rowView.findViewById(R.id.cityName);
        TextView regionName = (TextView) rowView.findViewById(R.id.regionName);

        LinearLayout items = (LinearLayout) rowView.findViewById(R.id.item);

        countryName.setText("Country: "+values.get(position).getCountryName());
        cityName.setText("City: "+values.get(position).getCityName());
        regionName.setText("Region: "+values.get(position).getRegionName());

        items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCityInformation_new(values.get(position).getCountryCode(),values.get(position).getCityName());
            }
        });


        return rowView;
    }


    public  void getCityInformation_new(String countryCode, String city) {
        RequestQueue requestQueue = Volley.newRequestQueue(MyApp.getAppContext());
        final String url = BASE_URL + "geoinfo/search-name-country?city_name=" + city + "&country_code=" + countryCode;

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Process the JSON
                        try {
                            // Loop through the array elements
                            for (int i = 0; i < response.length(); i++) {
                                // Get current json object
                                JSONObject countryResonse = response.getJSONObject(i);

                                // Get the current student (json object) data

                                double latitude = countryResonse.getDouble("lat");
                                double longitude = countryResonse.getDouble("lng");


                                Intent intent= new Intent(context, MapsActivity.class);
                                intent.putExtra("latitude",latitude);
                                intent.putExtra("longitude",longitude);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                        Log.e("error", String.valueOf(error));
                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);

    }
}

