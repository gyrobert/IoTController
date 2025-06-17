package com.example.iot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RuleAdapter extends RecyclerView.Adapter<RuleAdapter.RuleViewHolder> {

    public interface RuleInteractionListener {
        void onToggleChanged(Rule rule, boolean isActive);
        void onDeleteClicked(Rule rule);
        void onRuleClicked(Rule rule);
    }
    private static List<Rule> ruleList;
    private final RuleInteractionListener listener;
    public RuleAdapter(List<Rule> ruleList, RuleInteractionListener listener) {
        this.ruleList = ruleList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rule, parent, false); // legyen egy item_rule.xml layoutod
        return new RuleViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RuleViewHolder holder, int position) {
        Rule rule = ruleList.get(position);
        holder.bind(rule);
    }

    @Override
    public int getItemCount() {
        return ruleList.size();
    }

    public void updateList(List<Rule> rules) {
        ruleList.clear();
        ruleList.addAll(rules);
        notifyDataSetChanged();
    }


        public class RuleViewHolder extends RecyclerView.ViewHolder {
            TextView txtInputs, txtOutputs;
            Switch switchAktiv;
            Button btnTorles;




            public RuleViewHolder(@NonNull View itemView) {
                super(itemView);
                txtInputs = itemView.findViewById(R.id.txtInputs);
                txtOutputs = itemView.findViewById(R.id.txtOutputs);
                switchAktiv = itemView.findViewById(R.id.switchEnabled);
                btnTorles = itemView.findViewById(R.id.btnDelete);
            }

            public void bind(Rule rule) {
                txtInputs.setText("Bemenet: " + rule.input1 + " " + rule.input2 + " " + rule.input3 + " " + rule.input4 + " " + rule.input5 + " " + rule.input6);
                txtOutputs.setText("Kimenet: " + rule.output1 + " " + rule.output2 + " " + rule.output3 + " " + rule.output4);
                switchAktiv.setChecked(rule.active);
                // Reagál a felhasználó kapcsolására
                switchAktiv.setChecked(rule.active);
                switchAktiv.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    listener.onToggleChanged(rule, isChecked);
                });

                btnTorles.setOnClickListener(v -> {
                    listener.onDeleteClicked(rule);
                });

                itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onRuleClicked(rule);
                    }
                });
            }
    }
}
