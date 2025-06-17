package com.example.iot;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class RulesFragment extends Fragment {

    private RuleAdapter ruleAdapter;
    private SharedViewModel viewModel;

    public RulesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rules, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerRules);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ruleAdapter = new RuleAdapter(new ArrayList<>(), new RuleAdapter.RuleInteractionListener() {
            @Override
            public void onToggleChanged(Rule rule, boolean isActive) {
                rule.active = isActive;
            }

            @Override
            public void onDeleteClicked(Rule rule) {
                try {
                    for (Rule r : viewModel.getRuleList().getValue()) {
                        if (r.id.equals(rule.id)) {
                            if (!r.active) {
                                viewModel.deleteRule(rule);
                                viewModel.deleteRuleFirebase(rule);
                                break;
                            } else {
                                Toast.makeText(requireContext(), "A szabály aktív, nem törölhető!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }catch(NullPointerException e){
                    Toast.makeText(requireContext(), "Hiba történt a szabály törlésénél", Toast.LENGTH_SHORT).show();
                }


            }
            @Override
            public void onRuleClicked(Rule rule) {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, new EditFragment(rule, viewModel));
                transaction.addToBackStack(null);
                transaction.commit();
            }


        });

        recyclerView.setAdapter(ruleAdapter);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        if (Objects.requireNonNull(viewModel.getRuleList().getValue()).isEmpty()) {
            viewModel.initTestRulesIfEmpty();
        }

        viewModel.getRuleList().observe(getViewLifecycleOwner(), rules -> ruleAdapter.updateList(rules));
        Button szinkronizal = view.findViewById(R.id.btnSync);
        szinkronizal.setOnClickListener(v -> {
            viewModel.syncAllRulesToFirebase();
            viewModel.fetchRulesOnce();
        });
        }
}

