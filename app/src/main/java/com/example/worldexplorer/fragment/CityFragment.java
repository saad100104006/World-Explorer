package com.example.worldexplorer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.worldexplorer.MyApp;
import com.example.worldexplorer.R;
import com.example.worldexplorer.adapter.CityAdapter;
import com.example.worldexplorer.model.city;
import com.example.worldexplorer.model.country;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.worldexplorer.utils.Constants.BASE_URL;
import static com.example.worldexplorer.activity.MainActivity.countryList;
import static com.example.worldexplorer.activity.MainActivity.countryNameList;


public class CityFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    public static Spinner country;
    EditText searchEdt;
    Button searchBtn;
    String countryName;
    public static List<city> mCityList = new ArrayList<city>();
    public static CityAdapter mAdpter;
    public static ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.city_fragment, container, false);
        country = (Spinner) view.findViewById(R.id.country);
        searchEdt = (EditText) view.findViewById(R.id.searchEdt);
        searchBtn = (Button) view.findViewById(R.id.searchBtn);
        listView = (ListView) view.findViewById(R.id.list);
        country.setOnItemSelectedListener(this);
        getAllCountryInformation();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCityList.clear();
                if (mAdpter != null) {
                    mAdpter.notifyDataSetChanged();
                }
                if (countryName != null) {
                    String countryCode = getCountrtyCodeFromCountryName(countryName);
                    String cityName = searchEdt.getText().toString().trim();
                    getCityInformation(countryCode, cityName);
                }
            }
        });
        return view;


    }

    public String getCountrtyCodeFromCountryName(String countryName) {
        String countryCode = "";

        for (int i = 0; i < countryList.size(); i++) {
            if (countryList.get(i).getCountryName().equals(countryName)) {
                countryCode = countryList.get(i).getCountryCode();
                break;
            }
        }
        return countryCode;

    }

    public static void getCityInformation(String countryCode, String city) {
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
                                String cityId = countryResonse.getString("id");
                                String cityName = countryResonse.getString("city_name");
                                String regionCode = countryResonse.getString("region_code");
                                String regionName = countryResonse.getString("region_name");
                                String countryCode = countryResonse.getString("country_code");
                                String countryName = countryResonse.getString("country_name");
                                double latitude = countryResonse.getDouble("lat");
                                double longitude = countryResonse.getDouble("lng");
                                String diallingCode = countryResonse.getString("timezone");
                                boolean isCapital = countryResonse.getBoolean("capital");

                                city city = new city();
                                city.setCityId(cityId);
                                city.setCityName(cityName);
                                city.setRegionCode(regionCode);
                                city.setRegionName(regionName);
                                city.setCountryCode(countryCode);
                                city.setCountryName(countryName);
                                city.setLatitude(latitude);
                                city.setLongitude(longitude);
                                city.setTimezone(diallingCode);
                                city.setCapitall(isCapital);

                                mCityList.add(city);
                                mAdpter = new CityAdapter(MyApp.getAppContext(), mCityList);
                                listView.setAdapter(mAdpter);

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        countryName = parent.getItemAtPosition(position).toString();
        country.setSelection(((ArrayAdapter<String>) country.getAdapter()).getPosition(countryName.toString()));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static void setAdapter() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (MyApp.getAppContext(), android.R.layout.simple_spinner_item,
                        countryNameList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        if (country != null)
            country.setAdapter(spinnerArrayAdapter);
    }

    public static void getAllCountryInformation() {
        RequestQueue requestQueue = Volley.newRequestQueue(MyApp.getAppContext());
        final String url = BASE_URL + "geoinfo/countries";

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        countryList.clear();
                        countryNameList.clear();
                        // Process the JSON
                        try {
                            // Loop through the array elements
                            for (int i = 0; i < response.length(); i++) {
                                // Get current json object
                                JSONObject countryResonse = response.getJSONObject(i);

                                // Get the current student (json object) data
                                String countryCode = countryResonse.getString("country_code");
                                String countryName = countryResonse.getString("country_name");
                                String continentCode = countryResonse.getString("continent_code");
                                String continentName = countryResonse.getString("continent_name");
                                String currencyCode = countryResonse.getString("currency_code");
                                String diallingCode = countryResonse.getString("dialling_code");

                                com.example.worldexplorer.model.country country = new country();
                                country.setCountryCode(countryCode);
                                country.setCountryName(countryName);
                                country.setContinentCode(continentCode);
                                country.setContinentName(continentName);
                                country.setCurrencyCode(currencyCode);
                                country.setDialingCode(diallingCode);

                                countryList.add(country);
                                countryNameList.add(countryName);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        setAdapter();
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
