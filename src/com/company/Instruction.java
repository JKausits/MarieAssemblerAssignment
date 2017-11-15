package com.company;

public class Instruction {

    private String opCode;
    private String operand;
    private String label;

    public Instruction( String opCode, String operand) {
        this.opCode = opCode;
        this.operand = operand;
        label = "";
    }

    public String getOpCode() {
        return opCode;
    }

    public void setOpCode(String opCode) {
        this.opCode = opCode;
    }

    public String getOperand() {
        return operand;
    }

    public void setOperand(String operand) {
        this.operand = operand;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
