package com.example.iot;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class AddRuleFragment extends Fragment {

    private final CheckBox[] inputCheckboxes = new CheckBox[4];
    private final EditText[] inputText = new EditText[2];
    private final CheckBox[] outputCheckboxes = new CheckBox[4];

    private SharedViewModel viewModel;

    public AddRuleFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_rule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        inputCheckboxes[0] = view.findViewById(R.id.checkbox_input1);
        inputCheckboxes[1] = view.findViewById(R.id.checkbox_input2);
        inputCheckboxes[2] = view.findViewById(R.id.checkbox_input3);
        inputCheckboxes[3] = view.findViewById(R.id.checkbox_input4);
        inputText[0] = view.findViewById(R.id.edit_input5);
        inputText[1] = view.findViewById(R.id.edit_input6);

        outputCheckboxes[0] = view.findViewById(R.id.checkbox_output1);
        outputCheckboxes[1] = view.findViewById(R.id.checkbox_output2);
        outputCheckboxes[2] = view.findViewById(R.id.checkbox_output3);
        outputCheckboxes[3] = view.findViewById(R.id.checkbox_output4);

        Button saveButton = view.findViewById(R.id.btn_save_rule);

        // ViewModel példány beszerzése
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        saveButton.setOnClickListener(v -> {
            int[] inputs = new int[6];
            int[] outputs = new int[4];

            try {
                // 1–4: checkbox bemenetek
                for (int i = 0; i < 4; i++) {
                    inputs[i] = inputCheckboxes[i].isChecked() ? 1 : 0;
                }

                // 5–6: edittext bemenetek (számként)
                for (int i = 0; i < 2; i++) {
                    String text = inputText[i].getText().toString().trim();

                    if (text.isEmpty()) {
                        inputText[i].setError("Kötelező mező");
                        inputText[i].requestFocus();
                        return;
                    }
                    if (Integer.parseInt(text) <=0 || Integer.parseInt(text) >=33){
                        inputText[i].setError("Érvénytelen szám, 0 és 33 között kell legyen");
                        inputText[i].requestFocus();
                        return;
                    }

                    try {
                        inputs[i + 4] = Integer.parseInt(text);
                    } catch (NumberFormatException e) {
                        inputText[i].setError("Érvénytelen szám");
                        inputText[i].requestFocus();
                        return;
                    }
                }

                // Kimenetek
                for (int i = 0; i < 4; i++) {
                    outputs[i] = outputCheckboxes[i].isChecked() ? 1 : 0;
                }

                // Új szabály létrehozása
                Rule newRule = new Rule(inputs[0], inputs[1], inputs[2], inputs[3], inputs[4], inputs[5], outputs[0], outputs[1], outputs[2], outputs[3], true);

                if (!viewModel.duplicateCheck(newRule)) {
                    Toast.makeText(getContext(), "Ez a szabály már létezik", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    try {
                        viewModel.addRule(newRule);
                    }catch(Exception e){
                        Toast.makeText(getContext(), "Hiba történt a szabály mentése során", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        viewModel.addRuleFirebase(newRule);
                    }catch(Exception e){
                        Toast.makeText(getContext(), "Hiba történt a szabály mentése során", Toast.LENGTH_SHORT).show();
                    }
                }

//



                Toast.makeText(getContext(), "Szabály sikeresen elmentve", Toast.LENGTH_SHORT).show();

                for (CheckBox cb : inputCheckboxes) {
                    cb.setChecked(false);
                }

                for (EditText et : inputText) {
                    et.setText("");
                }

                for (CheckBox cb : outputCheckboxes) {
                    cb.setChecked(false);
                }

            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Hibás számbevitel Input5 vagy Input6 mezőben", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getContext(), "Hiba történt a szabály mentése során", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


