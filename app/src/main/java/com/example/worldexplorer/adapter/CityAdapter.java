package com.example.worldexplorer.adapter;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.worldexplorer.activity.MapsActivity;
import com.example.worldexplorer.R;
import com.example.worldexplorer.model.city;

import java.util.List;

public class CityAdapter extends ArrayAdapter<city> {
    private final Context context;
    List<city> values;

    public CityAdapter(Context context, List<city> values) {
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

                Intent intent= new Intent(context, MapsActivity.class);
                intent.putExtra("latitude",values.get(position).getLatitude());
                intent.putExtra("longitude",values.get(position).getLongitude());
                context.startActivity(intent);

            }
        });


        return rowView;
    }
}
