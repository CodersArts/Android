package com.example.rre.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.rre.R;

public class DashboardFragment extends Fragment {
    DatePicker datePicker;
        Button button;
        TextView textView1;


    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.tv);
        datePicker=root.findViewById(R.id.datePick);
        button=root.findViewById(R.id.button1);
        textView1=root.findViewById(R.id.txt);
        textView1.setText("Trip Date: "+getCurrentDate());


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView1.setText("Trip Date: "+getCurrentDate());

            }

        });
        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);

            }
        });
        return root;


    }

    private String getCurrentDate() {
        StringBuilder builder=new StringBuilder();
        builder.append((datePicker.getMonth() + 1)+"/");
        builder.append(datePicker.getDayOfMonth()+"/");
        builder.append(datePicker.getYear());
        return builder.toString();
    }
}
