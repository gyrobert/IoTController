package com.example.iot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class MainFragment extends Fragment {

    private final TextView[] inputViews = new TextView[6];
    private final Button[] outputButtons = new Button[4];

    private SharedViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        ViewCompat.setOnApplyWindowInsetsListener(root.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        inputViews[0] = root.findViewById(R.id.input1);
        inputViews[1] = root.findViewById(R.id.input2);
        inputViews[2] = root.findViewById(R.id.input3);
        inputViews[3] = root.findViewById(R.id.input4);
        inputViews[4] = root.findViewById(R.id.input5);
        inputViews[5] = root.findViewById(R.id.input6);

        outputButtons[0] = root.findViewById(R.id.output1);
        outputButtons[1] = root.findViewById(R.id.output2);
        outputButtons[2] = root.findViewById(R.id.output3);
        outputButtons[3] = root.findViewById(R.id.output4);


        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        viewModel.activate();

        viewModel.getIotData().observe(getViewLifecycleOwner(), iot -> {
            if (iot != null) {
                inputViews[0].setText(String.valueOf(iot.Input1));
                inputViews[1].setText(String.valueOf(iot.Input2));
                inputViews[2].setText(String.valueOf(iot.Input3));
                inputViews[3].setText(String.valueOf(iot.Input4));
                inputViews[4].setText(String.valueOf(iot.Input5));
                inputViews[5].setText(String.valueOf(iot.Input6));

                updateButtonLabel(outputButtons[0], iot.Output1, "Output1");
                updateButtonLabel(outputButtons[1], iot.Output2, "Output2");
                updateButtonLabel(outputButtons[2], iot.Output3, "Output3");
                updateButtonLabel(outputButtons[3], iot.Output4, "Output4");
            }
        });

        for (int i = 0; i < outputButtons.length; i++) {
            int index = i;
            outputButtons[i].setOnClickListener(view -> {
                String label = outputButtons[index].getText().toString();
                boolean isCurrentlyOn = label.contains("Off");
                viewModel.toggleOutput(index, !isCurrentlyOn);
            });
        }

        return root;
    }

    private void updateButtonLabel(Button button, boolean state, String baseName) {
        button.setText(state ? baseName + " (Off)" : baseName + " (On)");
    }

    @Override
    public void onDestroy() {
        viewModel.deactivate();
        super.onDestroy();
    }
}
