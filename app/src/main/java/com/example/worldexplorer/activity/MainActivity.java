package com.example.worldexplorer.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.worldexplorer.MyApp;
import com.example.worldexplorer.R;
import com.example.worldexplorer.fragment.AllCityInCountry;
import com.example.worldexplorer.fragment.AnyCityFragment;
import com.example.worldexplorer.fragment.CityFragment;
import com.example.worldexplorer.model.country;

import java.util.ArrayList;
import java.util.List;

import static com.example.worldexplorer.fragment.CityFragment.setAdapter;
import static com.example.worldexplorer.utils.Constants.BASE_URL;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationDrawer;
    public static List<country> countryList = new ArrayList<>();
    public static List<String> countryNameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_main);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNavigationDrawer = (NavigationView) findViewById(R.id.nv);
        mNavigationDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                FragmentManager fragmentManager = getSupportFragmentManager();
                switch (id) {
                    case R.id.country_city:

                        Fragment fragment = new CityFragment();

                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();

                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.country:
                        Fragment fragment2 = new AnyCityFragment();

                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragment2)
                                .commit();
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.city:
                        Fragment fragment3 = new AllCityInCountry();

                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragment3)
                                .commit();
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.exit:
                        finish();
                    default:
                        return true;
                }
                return true;
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new CityFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }



}
