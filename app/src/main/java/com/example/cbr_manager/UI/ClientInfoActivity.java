package com.example.cbr_manager.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.clientInfoFragment.InfoFragment;
import com.example.cbr_manager.UI.clientInfoFragment.RiskFragment;
import com.example.cbr_manager.UI.clientInfoFragment.VisitsFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ClientInfoActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager;

    private long id;

    private String[] titles = new String[]{"Information", "Visits", "Risk Level"};
    public static final String R_CLIENT_ID_PASSED_IN = "r_client_id_passed_in";

    public static Intent makeIntent(Context context, long id) {
        Intent intent =  new Intent(context, ClientInfoActivity.class);
        intent.putExtra(R_CLIENT_ID_PASSED_IN, id);
        return intent;
    }

    public long getId() {
        return id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_info);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        viewPager.setAdapter(createCardAdapter());
        new TabLayoutMediator(tabLayout, viewPager,(tab, position) -> tab.setText(titles[position])).attach();

        editButton();
        newVisitButton();
        ToolbarButtons();
    }

    private void extractIntent(){
        Intent intent = getIntent();
        this.id = intent.getLongExtra(R_CLIENT_ID_PASSED_IN, 0);
    }

    private void newVisitButton() {
        Button newVisit = findViewById(R.id.visit);
        newVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = NewVisitActivity.makeIntent(ClientInfoActivity.this);
                intent.putExtra("ID", id);
                startActivity(intent);
            }
        });
    }

    private void editButton() {
        Button edit = findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // edit
            }
        });
    }

    public class ViewPagerAdapter extends FragmentStateAdapter {
        private static final int CARD_ITEM_SIZE = 3;
        public ViewPagerAdapter(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @Override public Fragment createFragment(int position) {
            switch (position) {
                case 0: {
                    return InfoFragment.newInstance();
                }
                case 1: {
                    return VisitsFragment.newInstance(id);
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

    private void ToolbarButtons(){
        ImageButton homeBtn = (ImageButton) findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TaskViewActivity.makeIntent(ClientInfoActivity.this);
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

    private ViewPagerAdapter createCardAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        return adapter;
    }

}



