package com.example.cbr_manager.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.cbr_manager.Database.AdminMessageManager;
import com.example.cbr_manager.Database.ClientManager;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.BaselineSurvey.HealthSurveyActivity;
import com.example.cbr_manager.UI.clientInfoFragment.InfoFragment;
import com.example.cbr_manager.UI.clientInfoFragment.RiskFragment;
import com.example.cbr_manager.UI.clientInfoFragment.ListsFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ClientInfoActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager;

    private long id;
    private int position;

    private String[] titles = new String[]{"Information", "Records", "Risk Level"};
    public static final String R_CLIENT_ID_PASSED_IN = "r_client_id_passed_in";
    public static final String R_CLIENT_POS_PASSED_IN = "r_client_POS_passed_in";

    public static Intent makeIntent(Context context, int position, long id) {
        Intent intent =  new Intent(context, ClientInfoActivity.class);
        intent.putExtra(R_CLIENT_ID_PASSED_IN, id);
        intent.putExtra(R_CLIENT_POS_PASSED_IN, position);
        return intent;
    }

    public int getPosition() {
        return position;
    }

    public long getId() {
        return id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_info);
        extractIntent();

        viewPager = findViewById(R.id.viewPager3);
        tabLayout = findViewById(R.id.tabLayout);

        viewPager.setAdapter(createCardAdapter());
        new TabLayoutMediator(tabLayout, viewPager,(tab, position) -> tab.setText(titles[position])).attach();

        navbar();
        ToolbarButtons();

        AdminMessageManager adminMessageManager = AdminMessageManager.getInstance(ClientInfoActivity.this);
        adminMessageManager.clear();
        adminMessageManager.updateList();

        TextView badgeOnToolBar = findViewById(R.id.cart_badge2);
        badgeNotification(adminMessageManager, badgeOnToolBar);
    }

    private void extractIntent(){
        Intent intent = getIntent();
        this.position = intent.getIntExtra(R_CLIENT_POS_PASSED_IN, 0);
        this.id = intent.getLongExtra(R_CLIENT_ID_PASSED_IN, 0);
    }

    private void navbar(){
        newClientButton();
        newVisitButton();
        newReferralButton();
        editButton();
        newBaselineButton();
    }

    private void newBaselineButton() {
        ImageButton newBaseline = findViewById(R.id.CI_newBaseline);
        newBaseline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = HealthSurveyActivity.makeIntent(ClientInfoActivity.this, position, id);
                startActivity(intent);
            }
        });
    }

    private void newClientButton() {
        ImageButton newClient = findViewById(R.id.CI_newClient);
        newClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = NewClientActivity.makeIntent(ClientInfoActivity.this);
                startActivity(intent);
            }
        });
    }

    private void newVisitButton() {
        ImageButton newVisit = findViewById(R.id.CI_newVisit);
        newVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = NewVisitActivity.makeIntent(ClientInfoActivity.this, position, id);
                startActivity(intent);
            }
        });
    }

    private void newReferralButton() {
        ImageButton newReferral = findViewById(R.id.CI_newReferral);
        newReferral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = NewReferralActivity.makeIntent(ClientInfoActivity.this, position, id);
                startActivity(intent);
            }
        });
    }

    private void editButton() {
        Button edit = findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = EditClientActivity.makeIntent(ClientInfoActivity.this, position, id);
                startActivity(intent);
            }
        });
    }


    public class ViewPagerAdapter extends FragmentStateAdapter {
        private static final int CARD_ITEM_SIZE = 3;
        public ViewPagerAdapter(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @Override public Fragment createFragment(int pos) {
            switch (pos) {
                case 0: {
                    return InfoFragment.newInstance();
                }
                case 1: {
                    ListsFragment listsFragment = ListsFragment.newInstance(ClientInfoActivity.this.getId());
                    Bundle bundle = new Bundle();
                    bundle.putLong("client_id", ClientInfoActivity.this.getId());
                    listsFragment.setArguments(bundle);
                    return listsFragment;
                }
                case 2: {
                    return RiskFragment.newInstance();
                }
                default:
                return InfoFragment.newInstance();
            }
        }
        @Override public int getItemCount() {
            return CARD_ITEM_SIZE;
        }
    }

    private ViewPagerAdapter createCardAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        return adapter;
    }

    private void ToolbarButtons(){
        ImageButton homeBtn = (ImageButton) findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TaskViewActivity.makeIntent(ClientInfoActivity.this);
                startActivity(intent);
            }
        });

        ImageButton notificationBtn = findViewById(R.id.notificationButton);
        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = DashboardActivity.makeIntent(ClientInfoActivity.this);
                startActivity(intent);
            }
        });

        ImageButton profileBtn = (ImageButton) findViewById(R.id.profileButton);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProfileActivity.makeIntent(ClientInfoActivity.this);
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
}



