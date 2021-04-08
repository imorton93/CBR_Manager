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
import com.example.cbr_manager.UI.visitInfoFragment.VisitGoalFragment;
import com.example.cbr_manager.UI.visitInfoFragment.VisitInfoFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class VisitInfoActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager;
    private String[] titles = new String[]{"Information", "Goals"};
    public static final String R_VISIT_ID_PASSED_IN = "r_visit_id_passed_in";
    public static final String R_VISIT_POSITION_PASSED_IN = "r_visit_position_passed_in";
    private long visit_id;
    private int position;

    public static Intent makeIntent(Context context, long id, int position) {
        Intent intent =  new Intent(context, VisitInfoActivity.class);
        intent.putExtra(R_VISIT_ID_PASSED_IN, id);
        intent.putExtra(R_VISIT_POSITION_PASSED_IN, position);
        return intent;
    }

    private void extractIntent(){
        Intent intent = getIntent();
        visit_id = intent.getLongExtra(R_VISIT_ID_PASSED_IN, 0);
        position = intent.getIntExtra(R_VISIT_POSITION_PASSED_IN, 0);
    }

    public int getPosition() {
        return position;
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

        AdminMessageManager adminMessageManager = AdminMessageManager.getInstance(VisitInfoActivity.this);
        adminMessageManager.clear();
        adminMessageManager.updateList();

        TextView badgeOnToolBar = findViewById(R.id.cart_badge2);
        badgeNotification(adminMessageManager, badgeOnToolBar);
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
        ImageButton homeBtn = findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TaskViewActivity.makeIntent(VisitInfoActivity.this);
                startActivity(intent);
            }
        });

        ImageButton notificationBtn = findViewById(R.id.notificationButton);
        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = DashboardActivity.makeIntent(VisitInfoActivity.this);
                startActivity(intent);
            }
        });

        ImageButton profileBtn = findViewById(R.id.profileButton);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProfileActivity.makeIntent(VisitInfoActivity.this);
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

    private ViewPagerAdapter createCardAdapter() {
        return new ViewPagerAdapter(this);
    }
}