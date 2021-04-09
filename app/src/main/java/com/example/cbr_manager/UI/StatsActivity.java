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
import android.widget.TextView;

import com.example.cbr_manager.Database.AdminMessageManager;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.clientInfoFragment.InfoFragment;
import com.example.cbr_manager.UI.clientInfoFragment.ListsFragment;
import com.example.cbr_manager.UI.clientInfoFragment.RiskFragment;
import com.example.cbr_manager.UI.statsFragment.BaselineStatsFragment;
import com.example.cbr_manager.UI.statsFragment.ClientStatsFragment;
import com.example.cbr_manager.UI.statsFragment.GeneralStatsFragment;
import com.example.cbr_manager.UI.statsFragment.ReferralStatsFragment;
import com.example.cbr_manager.UI.statsFragment.VisitStatsFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class StatsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private String[] titles = new String[]{"General", "Clients", "Baseline Surveys", "Referrals", "Visits"};


    public static Intent makeIntent(Context context) {
        return new Intent(context, StatsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        ToolbarButtons();

        AdminMessageManager adminMessageManager = AdminMessageManager.getInstance(StatsActivity.this);
        adminMessageManager.clear();
        adminMessageManager.updateList();

        TextView badgeOnToolBar = findViewById(R.id.cart_badge2);
        badgeNotification(adminMessageManager, badgeOnToolBar);

        viewPager = findViewById(R.id.statsViewPager);
        tabLayout = findViewById(R.id.statsTabLayout);

        viewPager.setAdapter(createCardAdapter());
        new TabLayoutMediator(tabLayout, viewPager,(tab, position) -> tab.setText(titles[position])).attach();
    }

    private void ToolbarButtons(){
        ImageButton homeBtn = findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TaskViewActivity.makeIntent(StatsActivity.this);
                startActivity(intent);
            }
        });

        ImageButton notificationBtn = findViewById(R.id.notificationButton);
        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = DashboardActivity.makeIntent(StatsActivity.this);
                startActivity(intent);
            }
        });

        ImageButton profileBtn = findViewById(R.id.profileButton);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProfileActivity.makeIntent(StatsActivity.this);
                startActivity(intent);
            }
        });
    }

    private void badgeNotification(AdminMessageManager adminMessageManager, TextView badge) {
        int size = adminMessageManager.numUnread();

        if (badge != null) {
            if (size == 0) {
                if (badge.getVisibility() != View.GONE) {
                    badge.setVisibility(View.GONE);
                }
            } else {
                badge.setText(String.valueOf(Math.min(size, 99)));
                if (badge.getVisibility() != View.VISIBLE) {
                    badge.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public class ViewPagerAdapter extends FragmentStateAdapter {
        private static final int CARD_ITEM_SIZE = 5;
        public ViewPagerAdapter(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @Override
        public Fragment createFragment(int pos) {
            switch (pos) {
                case 0: {
                    return GeneralStatsFragment.newInstance();
                }
                case 1: {
                    return ClientStatsFragment.newInstance();
                }
                case 2: {
                    return BaselineStatsFragment.newInstance();
                }
                case 3: {
                    return ReferralStatsFragment.newInstance();
                }
                case 4: {
                    return VisitStatsFragment.newInstance();
                }
                default:
                    return GeneralStatsFragment.newInstance();
            }
        }
        @Override
        public int getItemCount() {
            return CARD_ITEM_SIZE;
        }
    }

    private StatsActivity.ViewPagerAdapter createCardAdapter() {
        StatsActivity.ViewPagerAdapter adapter = new StatsActivity.ViewPagerAdapter(this);
        return adapter;
    }
}