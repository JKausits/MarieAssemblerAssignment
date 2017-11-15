package com.company;

public enum OpCodes {
    JNS("0"), LOAD("1"), STORE("2"), ADD("3"), SUBT("4"), INPUT("5")
    , OUTPUT("6"), HALT("7"), SKIPCOND("8"), JUMP("9"), CLEAR("A"), ADDI("B"), JUMPI("C"), LOADI("D"), STOREI("E");
    String value;
    private OpCodes(String value){
        this.value = value;
    }
}
