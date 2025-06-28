package com.example.iot;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditFragment extends Fragment {

    private EditText editInput5, editInput6;
    private TextView editIdText;
    private CheckBox checkboxOutput1, checkboxOutput2, checkboxOutput3, checkboxOutput4;
    private Button btnSave;
    private Rule rule;
    private SharedViewModel viewModel;

    public EditFragment(Rule rule, SharedViewModel viewModel) {
        this.rule = rule;
        this.viewModel = viewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        editInput5 = view.findViewById(R.id.editInput5);
        editInput6 = view.findViewById(R.id.editInput6);

        editIdText = view.findViewById(R.id.editIdText);

        checkboxOutput1 = view.findViewById(R.id.checkboxOutput1);
        checkboxOutput2 = view.findViewById(R.id.checkboxOutput2);
        checkboxOutput3 = view.findViewById(R.id.checkboxOutput3);
        checkboxOutput4 = view.findViewById(R.id.checkboxOutput4);

        btnSave = view.findViewById(R.id.btnSave);

        editInput5.setText(String.valueOf(rule.input5));
        editInput6.setText(String.valueOf(rule.input6));

        String summary = "Digitális inputok: " + rule.input1 + ", " + rule.input2 + ", " + rule.input3 + ", " + rule.input4;
        editIdText.setText(summary);

        checkboxOutput1.setChecked(rule.output1 == 1);
        checkboxOutput2.setChecked(rule.output2 == 1);
        checkboxOutput3.setChecked(rule.output3 == 1);
        checkboxOutput4.setChecked(rule.output4 == 1);

        btnSave.setOnClickListener(v -> {
            try {
                int input5Val= Integer.parseInt(editInput5.getText().toString());
                int input6Val= Integer.parseInt(editInput6.getText().toString());

                if (input5Val < 0 || input5Val > 33 || input6Val < 0 || input6Val > 33) {
                    Toast.makeText(getContext(), "Az értékek csak 0 és 33 között lehetnek", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    rule.input5 = input5Val;
                    rule.input6 = input6Val;
                }

                rule.output1 = checkboxOutput1.isChecked() ? 1 : 0;
                rule.output2 = checkboxOutput2.isChecked() ? 1 : 0;
                rule.output3 = checkboxOutput3.isChecked() ? 1 : 0;
                rule.output4 = checkboxOutput4.isChecked() ? 1 : 0;


                viewModel.updateData(rule);
                requireActivity().getSupportFragmentManager().popBackStack(); // visszalépés

            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Érvénytelen számformátum", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
