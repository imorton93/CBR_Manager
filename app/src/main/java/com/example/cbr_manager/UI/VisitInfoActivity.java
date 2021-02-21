package com.example.cbr_manager.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.visitInfoFragment.VisitGoalFragment;
import com.example.cbr_manager.UI.visitInfoFragment.VisitInfoFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class VisitInfoActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager;
    private String[] titles = new String[]{"Information", "Goals"};

    public static final String R_VISIT_ID_PASSED_IN = "r_visit_id_passed_in";

    private long visit_id;

    public static Intent makeIntent(Context context, long id) {
        Intent intent =  new Intent(context, VisitInfoActivity.class);
        intent.putExtra(R_VISIT_ID_PASSED_IN, id);
        return intent;
    }

    private void extractIntent(){
        Intent intent = getIntent();
        visit_id = intent.getLongExtra(R_VISIT_ID_PASSED_IN, 0);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_info);

        viewPager = findViewById(R.id.viewPager2);
        tabLayout = findViewById(R.id.tabLayout2);

        viewPager.setAdapter(createCardAdapter());
        new TabLayoutMediator(tabLayout, viewPager,(tab, position) -> tab.setText(titles[position])).attach();

        extractIntent();
        ToolbarButtons();
    }

    public long getVisit_id() {
        return visit_id;
    }

    public class ViewPagerAdapter extends FragmentStateAdapter {
        private static final int CARD_ITEM_SIZE = 2;
        public ViewPagerAdapter(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @Override public Fragment createFragment(int position) {
            switch (position) {
                case 0: {
                    return VisitInfoFragment.newInstance();
                }
                case 1: {
                    return VisitGoalFragment.newInstance();
                }
                default:
                    return VisitInfoFragment.newInstance();
            }
        }
        @Override public int getItemCount() {
            return CARD_ITEM_SIZE;
        }
    }

    private void ToolbarButtons(){
        ImageButton homeBtn = (ImageButton) findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TaskViewActivity.makeIntent(VisitInfoActivity.this);
                startActivity(intent);
            }
        });

        ImageButton profileBtn = (ImageButton) findViewById(R.id.profileButton);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProfileActivity.makeIntent(VisitInfoActivity.this);
                startActivity(intent);
            }
        });
    }

    private ViewPagerAdapter createCardAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        return adapter;
    }
}