package com.example.iot;

public class Rule {
    public String id;
    public int input1;

    public int input2;
    public int input3;
    public int input4;
    public int input5;
    public int input6;
    public int output1;
    public int output2;
    public int output3;
    public int output4;
    public boolean active;

    public Rule() {
        input1 = 0;
        input2 = 0;
        input3 = 0;
        input4 = 0;
        input5 = 0;
        input6 = 0;
        output1 = 0;
        output2 = 0;
        output3 = 0;
        output4 = 0;
        active = true;
    }

    public Rule(int input1, int input2, int input3, int input4, int input5, int input6, int output1, int output2, int output3, int output4, boolean active) {
        this.input1=input1;
       this.input2=input2;
       this.input3=input3;
       this.input4=input4;
       this.input5=input5;
       this.input6=input6;
       this.output1=output1;
       this.output2=output2;
       this.output3=output3;
       this.output4=output4;
        this.active = active;
    }
}
