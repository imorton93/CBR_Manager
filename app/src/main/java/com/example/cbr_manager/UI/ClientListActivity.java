package com.example.cbr_manager.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.clientListFragment.MapsFragment;
import com.example.cbr_manager.UI.clientListFragment.listFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ClientListActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager;
    private String[] titles = new String[]{"Clients", "Map"};

    public static Intent makeIntent(Context context) {
        return new Intent(context, ClientListActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);

        ToolbarButtons();

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tab);

        viewPager.setAdapter(createCardAdapter());
        new TabLayoutMediator(tabLayout, viewPager,(tab, position) -> tab.setText(titles[position])).attach();
    }

    private void ToolbarButtons(){
        ImageButton homeBtn = findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TaskViewActivity.makeIntent(ClientListActivity.this);
                startActivity(intent);
            }
        });

        ImageButton profileBtn = findViewById(R.id.profileButton);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProfileActivity.makeIntent(ClientListActivity.this);
                startActivity(intent);
            }
        });
    }

    public class ViewPagerAdapter extends FragmentStateAdapter {
        private static final int CARD_ITEM_SIZE = 2;
        public ViewPagerAdapter(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @Override public Fragment createFragment(int pos) {
            switch (pos) {
                case 0: {
                    return listFragment.newInstance();
                }
                case 1: {
                    return MapsFragment.newInstance();
                }
                default:
                    return listFragment.newInstance();
            }
        }
        @Override public int getItemCount() {
            return CARD_ITEM_SIZE;
        }
    }

    private ClientListActivity.ViewPagerAdapter createCardAdapter() {
        ClientListActivity.ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        return adapter;
    }


}