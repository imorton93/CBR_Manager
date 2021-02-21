package com.example.cbr_manager.UI.clientInfoFragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.ClientInfoActivity;
import com.example.cbr_manager.UI.ClientListActivity;
import com.example.cbr_manager.UI.VisitInfoActivity;

public class VisitsFragment extends Fragment {

    public VisitsFragment() {
    }

    public static VisitsFragment newInstance(long id) {
        VisitsFragment fragment = new VisitsFragment();
        Bundle args = new Bundle();
        args.putLong("id", id);
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
        Bundle args = getArguments();
        long id = args.getLong("id", 0);
        View V =  inflater.inflate(R.layout.fragment_visits, container, false);
        populateListView(V, id);
        clickVisit(V);
        return V;
    }

    private void populateListView(View V, long id) {
        DatabaseHelper handler = new DatabaseHelper(this.getActivity());
        Cursor todoCursor = handler.getVisits(id);
        ListView lv = V.findViewById(R.id.visitList);
        TodoCursorAdapter2 todoAdapter = new TodoCursorAdapter2(this.getActivity(), todoCursor);
        lv.setAdapter(todoAdapter);
    }

    private void clickVisit(View V) {
        ListView list = V.findViewById(R.id.visitList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = VisitInfoActivity.makeIntent(getActivity(), id);
                startActivity(intent);
            }
        });
    }

    public class TodoCursorAdapter2 extends CursorAdapter {
        public TodoCursorAdapter2(Context context, Cursor cursor) {
            super(context, cursor, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.visit_list, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView purpose = view.findViewById(R.id.purpose_vlist);
            TextView date = view.findViewById(R.id.dateOfVisit);

            String purposeOfVisit = cursor.getString(cursor.getColumnIndexOrThrow("PURPOSE_OF_VISIT"));
            String dateOfVisit = cursor.getString(cursor.getColumnIndexOrThrow("VISIT_DATE"));

            purpose.setText(purposeOfVisit);
            date.setText(dateOfVisit);
        }
    }
}