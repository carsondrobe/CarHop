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

    ViewPager2 mViewPager;

    ViewPagerAdapter mViewPagerAdapter;

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
        initializeTabs();
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
                        intent = new Intent(UsageSummaryActivity.this, DriverBookTripActivity.class);
                    startActivity(intent);
                    break;
                case R.id.finances:
                    break;
            }
            return true;
        });
    }

//    public class CollectionDemoFragment extends Fragment {
//        // When requested, this adapter returns a DemoObjectFragment,
//        // representing an object in the collection.
//        DemoCollectionAdapter demoCollectionAdapter;
//        ViewPager2 viewPager;
//
//        @Nullable
//        @Override
//        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                                 @Nullable Bundle savedInstanceState) {
//            return inflater.inflate(R.layout.activity_usage_summary, container, false);
//        }
//
//        @Override
//        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//            TabLayout tabLayout = view.findViewById(R.id.usage_summary_tabLayout);
//            new TabLayoutMediator(tabLayout, viewPager,
//                    (tab, position) -> tab.setText("OBJECT " + (position + 1))
//            ).attach();
//        }
//    }
//
//    public class DemoCollectionAdapter extends FragmentStateAdapter {
//        public DemoCollectionAdapter(Fragment fragment) {
//            super(fragment);
//        }
//
//        @NonNull
//        @Override
//        public Fragment createFragment(int position) {
//            // Return a NEW fragment instance in createFragment(int).
//            Fragment fragment = new DemoObjectFragment();
//            Bundle args = new Bundle();
//            // The object is just an integer.
//            args.putInt(DemoObjectFragment.ARG_OBJECT, position + 1);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public int getItemCount() {
//            return 100;
//        }
//    }
//
//    // Instances of this class are fragments representing a single
//// object in the collection.
//    public class DemoObjectFragment extends Fragment {
//        public static final String ARG_OBJECT = "object";
//
//        @Nullable
//        @Override
//        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                                 @Nullable Bundle savedInstanceState) {
//            return inflater.inflate(R.layout.activity_usage_summary, container, false);
//        }
//
//        @Override
//        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//            Bundle args = getArguments();
//            ((TextView) view.findViewById(android.R.id.text1))
//                    .setText(Integer.toString(args.getInt(ARG_OBJECT)));
//        }
//    }

    private void initializeTabs(){
//        mViewPager = findViewById(R.id.usage_summary_viewPager);
//        mViewPagerAdapter = new ViewPagerAdapter(this, images);
//        mViewPager.setAdapter(mViewPagerAdapter);
    }

    public void clickedWeekly(View view) {
        Log.d("ViewPagerAdapter", "clickedWeekly");
        graphImage.setImageResource(R.drawable.monthly_expenses);
    }
}