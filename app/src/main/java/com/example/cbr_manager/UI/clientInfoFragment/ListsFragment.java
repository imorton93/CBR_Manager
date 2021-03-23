package com.example.cbr_manager.UI.clientInfoFragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.cbr_manager.Database.Visit;
import com.example.cbr_manager.Database.VisitManager;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.ClientInfoActivity;
import com.example.cbr_manager.UI.VisitInfoActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class ListsFragment extends Fragment {

    private FragmentActivity infoActivity;
    private long client_id;
    private VisitManager visitManager;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private String[] titles = new String[]{"Visits", "Referrals", "Baseline Survey"};

    public ListsFragment() {
    }

    public static ListsFragment newInstance(long client_id) {
        ListsFragment fragment = new ListsFragment();
        Bundle args = new Bundle();
        args.putLong("client_id", client_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.infoActivity = getActivity();
        Bundle args = getArguments();
        this.client_id = args.getLong("client_id", 0);
        this.visitManager = VisitManager.getInstance(infoActivity);

        View V = inflater.inflate(R.layout.fragment_visits, container, false);
        ViewPager2 mViewPager = (ViewPager2) V.findViewById(R.id.listsViewPager);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), getLifecycle());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        return V;
    }

    private class SectionsPagerAdapter extends FragmentStateAdapter {

        public SectionsPagerAdapter(FragmentManager fm, Lifecycle lifecycle) {
            super(fm, lifecycle);
        }

        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return VisitListFragment.newInstance(client_id);
                default:
                    return ReferralListFragment.newInstance(client_id);
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}