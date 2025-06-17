package com.example.iot;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditFragment extends Fragment {

    private EditText editInput5, editInput6;
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
        btnSave = view.findViewById(R.id.btnSave);

        editInput5.setText(String.valueOf(rule.input5));
        editInput6.setText(String.valueOf(rule.input6));

        btnSave.setOnClickListener(v -> {
            try {
                rule.input5 = Integer.parseInt(editInput5.getText().toString());
                rule.input6 = Integer.parseInt(editInput6.getText().toString());

              viewModel.updateData(rule);

                requireActivity().getSupportFragmentManager().popBackStack(); // vissza
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Érvénytelen számformátum", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
