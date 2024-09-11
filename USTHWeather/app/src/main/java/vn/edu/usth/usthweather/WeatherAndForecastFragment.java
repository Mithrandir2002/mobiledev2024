package vn.edu.usth.usthweather;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
public class WeatherAndForecastFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_and_forecast, container, false);
        WeatherFragment weatherFragment1 = (WeatherFragment) getChildFragmentManager().findFragmentById(R.id.fragment_weather);
        ForecastFragment forecastFragment = (ForecastFragment) getChildFragmentManager().findFragmentById(R.id.fragment_forecast);
        return view;
    }
}