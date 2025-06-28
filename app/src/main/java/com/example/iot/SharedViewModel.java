package com.example.iot;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<IoT> iotLiveData = new MutableLiveData<>();
    private final DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference("IoT");

    private final DatabaseReference rulesRef = FirebaseDatabase.getInstance().getReference("Rules");

    private final MutableLiveData<List<Rule>> ruleList = new MutableLiveData<>(new ArrayList<>());

    public SharedViewModel() {
        //kontroller portok
        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                IoT iot = snapshot.getValue(IoT.class);
                if (iot != null) {
                    iotLiveData.setValue(iot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("SharedViewModel", "Firebase hiba: " + error.getMessage());
            }
        });

        //szabalyok
        fetchRulesOnce();
    }

    public void updateData(Rule rule) {
        for (Rule r : ruleList.getValue()) {
            if (r.id.equals(rule.id)) {
                r.input5 = rule.input5;
                r.input6 = rule.input6;
                break;
            }
    }
    }
    public LiveData<IoT> getIotData() {
        return iotLiveData;
    }

    public LiveData<List<Rule>> getRuleList() {
        return ruleList;
    }

    public void addRule(Rule rule) {
        List<Rule> current = ruleList.getValue();
        assert current != null;
        current.add(rule);
        ruleList.setValue(current);
    }
    public void deleteRule(Rule rule) {
        List<Rule> current = ruleList.getValue();
        assert current != null;
        current.remove(rule);
        ruleList.setValue(current);
    }
    public boolean duplicateCheck(Rule rule) {
        List<Rule> current = ruleList.getValue();
        assert current != null;
        for (Rule r : current) {
            if (r.input1 == rule.input1 && r.input2 == rule.input2 && r.input3 == rule.input3 && r.input4 == rule.input4){
                return false;
            }
        }
        return true;
    }
    public void initTestRulesIfEmpty() {
        if (ruleList.getValue().isEmpty()) {
            addRule(new Rule(0, 0, 0, 0, 0, 0,0, 0, 0, 0, true));
        }
    }

    public void addRuleFirebase(Rule rule) {
        String firebaseId = rulesRef.push().getKey();
        if (firebaseId != null) {
            rule.id = firebaseId;
            rulesRef.child(firebaseId).setValue(rule)
                    .addOnSuccessListener(unused -> fetchRulesOnce()); // újratöltés csak akkor, ha sikeres
        }
    }

    public void deleteRuleFirebase(Rule rule) {
        if (rule == null || rule.id == null || rule.id.isEmpty()) {
            Log.w("SharedViewModel", "Nem lehet törölni a szabályt: rule vagy rule.id null/üres");
            return;
        }

        try {
            rulesRef.child(rule.id).removeValue()
                    .addOnSuccessListener(aVoid -> Log.d("SharedViewModel", "Szabály sikeresen törölve: " + rule.id))
                    .addOnFailureListener(e -> Log.e("SharedViewModel", "Hiba a törlés során: " + e.getMessage(), e));
        } catch (Exception e) {
            Log.e("SharedViewModel", "Kivétel történt a törlés közben: " + e.getMessage(), e);
        }
    }

    public void fetchRulesOnce() {
        rulesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<Rule> rulesFromFirebase = new ArrayList<>();
                for (DataSnapshot ruleSnapshot : task.getResult().getChildren()) {
                    Rule rule = ruleSnapshot.getValue(Rule.class);
                    if (rule != null) {
                        rulesFromFirebase.add(rule);
                    }
                }
                ruleList.setValue(rulesFromFirebase);
                
                Log.d("SharedViewModel", "Rules frissítve (" + rulesFromFirebase.size() + " db)");
            } else {
                Log.e("SharedViewModel", "Rules lekérés hiba: ", task.getException());
            }
        });
    }
    public void syncAllRulesToFirebase() {
        firebaseRef.child("Sync").setValue(true)
                .addOnSuccessListener(aVoid -> Log.d("SharedViewModel", "/IoT/Sync beállítva true-ra"))
                .addOnFailureListener(e -> Log.e("SharedViewModel", "/IoT/Sync beállítás hiba: ", e));


        List<Rule> currentRules = ruleList.getValue();
        if (currentRules == null) return;

        for (Rule rule : currentRules) {
            if (rule.id != null && !rule.id.isEmpty()) {
                rulesRef.child(rule.id).setValue(rule)
                        .addOnSuccessListener(aVoid -> Log.d("SharedViewModel", "Szabály szinkronizálva: " + rule.id))
                        .addOnFailureListener(e -> Log.e("SharedViewModel", "Szinkronizálási hiba: ", e));
            }
        }
    }
    public void activate(){
        firebaseRef.child("Activity").setValue(true);
    }
    public void deactivate(){
        firebaseRef.child("Activity").setValue(false);
    }

    public void setAutomationMode(boolean automationMode) {
        firebaseRef.child("Mode").setValue(automationMode);
    }
    public void toggleOutput(int index, boolean newState) {
        firebaseRef.child("Output" + (index + 1)).setValue(newState);
    }
}
