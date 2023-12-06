package com.example.carpoolapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class UsageSummaryActivity extends AppCompatActivity {

    boolean isPassenger;

    int[] images = {R.drawable.weekly_expenses, R.drawable.monthly_expenses, R.drawable.monthly_expenses};

    ImageView graphImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_summary);
        Intent intent = getIntent();
        isPassenger = intent.getStringExtra("source").equals("passenger");
        graphImage = findViewById(R.id.usage_summary_graph_image);

        initializeNavBar();
    }
    private void initializeNavBar(){
        NavigationBarView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()){
                case R.id.home:
                    if (isPassenger)
                        intent = new Intent(UsageSummaryActivity.this, PassengerMainActivity.class);
                    else
                        intent = new Intent(UsageSummaryActivity.this, DriverMainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.book:
                    if (isPassenger)
                        intent = new Intent(UsageSummaryActivity.this, PassengerBookRideActivity.class);
                    else
                        intent = new Intent(UsageSummaryActivity.this, DriverMapActivity.class);
                    startActivity(intent);
                    break;
                case R.id.finances:
                    break;
            }
            return true;
        });
    }

    public void clickedWeekly(View view) {
        Log.d("ViewPagerAdapter", "clickedWeekly");
        graphImage.setImageResource(R.drawable.weekly_expenses);
    }

    public void clickedMonthly(View view) {
        Log.d("ViewPagerAdapter", "clickedWeekly");
        graphImage.setImageResource(R.drawable.monthly_expenses);
    }

    public void clickedYearly(View view) {
        Log.d("ViewPagerAdapter", "clickedWeekly");
        graphImage.setImageResource(R.drawable.yearly_expenses);
    }
}