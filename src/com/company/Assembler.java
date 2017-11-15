package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class Assembler {
    private static int address;
    private static Map<String, Integer> symbols = new HashMap<>();
    private static Map<Integer, Instruction> instructions = new HashMap<>();
    private static boolean passed;
    public static void process(String fileName){
        buildLists(fileName);
        if(passed) {
            printSymbols();
            printInstructions();
        }
    }


    //BUILDS A MAP OF SYMBOLS WITH THE SYMBOL BEING THE KEY, THE ADDRESS BEING THE VALUE;
    //BUILDS A MAP OF INSTRUCTIONS WITH THE ADDRESS BEING THE KEY, AND THE INSTRUCTION BEING THE VALUE
    private static void buildLists(String fileName){
        instructions.clear();
        symbols.clear();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while((line = reader.readLine()) != null){
                String[] instruction = line.split("\\s+");
                if(line.contains("ORG")){
                    instructionORG(instruction);
                }else if(!line.contains(OpCodes.HALT.toString()) && !line.contains("CLEAR") && !line.contains("OUTPUT") && !line.contains("INPUT")){
                    instructionDouble(instruction);
                }else{
                    instructionSingle(instruction);
                }
            }
            reader.close();
            passed = true;
        }catch (Exception e){
            passed = false;
            System.out.println("Failed");
            e.printStackTrace();
        }
    }

    private static void instructionORG(String[] instruction){
        instructions.put(address, new Instruction(instruction[1].trim(), instruction[2].trim()));
        address = Integer.parseInt(instruction[2], 16);
    }

    private static void instructionSingle(String[] instruction){
        instructions.put(address++, new Instruction(instruction[1].trim(), ""));
    }

    private static void instructionDouble(String[] instruction){
        Instruction newInstruction = new Instruction(instruction[1].trim(), instruction[2].trim());
        if(instruction[0].contains(",")){
            symbols.put(instruction[0].split(",")[0], address);
            newInstruction.setLabel(instruction[0]);
        }
        instructions.put(address++, newInstruction);
    }
    private static void printSymbols(){
        System.out.println("Symbol  Value");
        for(String key : symbols.keySet()){
            String hexString = formatHexString(symbols.get(key), 4);
            System.out.println(key + "       " + hexString);
        }
    }

    private static void printInstructions(){
        System.out.println("\nlocation    code                Source Code");
        for(Integer key : instructions.keySet()){
            Instruction instruction = instructions.get(key);
            String code = generateCode(instruction);
            String resultString = String.format("%s        %s      %s      %s      %s",
                    formatHexString(key, 4), code, pad(instruction.getLabel(), 3)
                    , pad(instruction.getOpCode(), 8),
                    instruction.getOperand());
            System.out.println(resultString);
        }
    }


    //GENERATES THE CODE FROM THE INSTRUCTION
    private static String generateCode(Instruction instruction){
        Integer address = symbols.get(instruction.getOperand());
        String opCode = getOpcodeValue(instruction.getOpCode());
        int operand = 0;
        if(address != null){
            if(!opCode.equals("B") && !opCode.equals("C") && !opCode.equals("D") && !opCode.equals("E"))
                operand = address;
            else{
                Instruction temp = instructions.get(address);
                String value = temp.getOperand();
                if(temp.getOperand().equals("HEX"))
                    operand = Integer.parseInt(value, 16);
                else
                    operand = Integer.parseInt(value);
            }
            opCode += formatHexString(operand, 3);
        }else if(opCode.equals("7") || opCode.equals("5") || opCode.equals("6") || opCode.equals("A")){
            opCode += "000";
        }else if(opCode.equals("8")){
            opCode += instruction.getOperand();
        }else if(!opCode.equals("0000")){
            if(instruction.getOpCode().equals("DEC"))
                opCode = formatHexString(Integer.parseInt(instruction.getOperand()), 4);
            else
                opCode = formatHexString(Integer.parseInt(instruction.getOperand(), 16), 4);
        }
        return opCode.toUpperCase();
    }

    //RETURNS THE OPCODE HEXADECIMAL VALUE
    private static String getOpcodeValue(String opCode){
        for(OpCodes code : OpCodes.values()){
            if(code.toString().equals(opCode))
                return code.value;
        }

        if(opCode.equals("ORG"))
            return "0000";

        return "";
    }

    //FORMATS THE HEXADECIMAL STRING, MAKING IT A 4 DIGIT VALUE
    private static String formatHexString(int dec, int length){
        StringBuilder hexString = new StringBuilder(Integer.toHexString(dec).toUpperCase());
        while(hexString.length() < length){
            hexString.insert(0, "0");
        }

        if(hexString.length() > 4){
            hexString.delete(0, hexString.length() - 4);
        }
        return hexString.toString();
    }

    // RETURNS A STRING THAT'S PADDED TO THE PROVIDED LENGTH
    private static String pad(String string, int maxLength){
        StringBuilder builder = new StringBuilder(string);
        while(builder.length() < maxLength){
            builder.append(" ");
        }
        return builder.toString();
    }


}
