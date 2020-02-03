package com.mike;

import java.util.List;
import java.util.Map;

interface Operationable {
    int craft(int a, int b);
}

enum Numeral {
    ET, // ExtraTerrestrial
    ROMAN,
    ARABIC
}

public class Guru {
    Guru() {
        // maybe applause
    }
    Guru(String inscription) {
        this();
        lookAt(inscription);
    }

    public void lookAt(String inscription) throws IllegalArgumentException {
        String[] glyphs = inscription.split(" ");
        if (glyphs.length != 3) throw new IllegalArgumentException("illegal task!");
        leftOperand = recognizeScrabble(glyphs[0]);
        rightOperand = recognizeScrabble(glyphs[2]);
        effect = recognizeOperation(glyphs[1]);
    }

    private int leftOperand;
    private int rightOperand;
    private Operationable effect;

    private String say(Integer n) {
        switch (civilization) {
            case ARABIC:
                return n.toString();
            case ROMAN:
                return arabic2roman(n);
            default:
                return "שלום שבת";
        }
    }

    public String craftAndSay() {
        if (civilization != Numeral.ET)
            return say(effect.craft(leftOperand, rightOperand));
        else throw new IllegalArgumentException("שלום");
    }


    private Numeral civilization = Numeral.ET;

    public Operationable sum = (x, y) -> x + y;
    public Operationable difference = (x, y) -> {
        if (civilization == Numeral.ARABIC || (civilization == Numeral.ROMAN && x > y)) return x - y;
        else throw new ArithmeticException(" cannot be calculated!");
    };
    public Operationable product = (x, y) -> x * y;
    public Operationable division = (x, y) -> x / y;


    public static String arabic2roman(int n) {
        // отдельно список ступеней, потому что getKeys порядок не сохраняет
        final List<Integer> gradList = List.of(100, 90, 50, 40, 10, 9, 5, 4, 1);
        final Map<Integer, String> arabic2roman = Map.of(
                100, "C",   // больше 100 не будет
                90, "XC",   // а Map.of() всё равно не умеет больше 10 пар
                50, "L",    // поэтому - и так сойдёт
                40, "XL",
                10, "X",
                9, "IX",
                5, "V",
                4, "IV",
                1, "I"
        );
        int z = n;
        StringBuilder out = new StringBuilder();
        for (Integer grad : gradList)
            if (z >= grad) {
                for (int q = 0; q < z / grad; q++) out.append(arabic2roman.get(grad));
                z %= grad;
            }
        return out.toString();
    }

    public static int parseRomanNumeral(String glyph) {
        final Map<String, Integer> roman2arabic = Map.of(
                "I", 1, // для простоты verbatim
                "II", 2,
                "III", 3,
                "IV", 4,
                "V", 5,
                "VI", 6,
                "VII", 7,
                "VIII", 8,
                "IX", 9,
                "X", 10
        );
        Integer sign = roman2arabic.get(glyph.toUpperCase().trim());
        if (sign != null)
            return sign;
        else
            throw new NumberFormatException("illegal roman numeral \"" + glyph + "\"");
    }

    public Operationable recognizeOperation(String op) {
        final Map<String, Operationable> operationable = Map.of(
                "+", sum,
                "-", difference,
                "*", product,
                "/", division
        );
        if (operationable.get(op) != null) return operationable.get(op);
        else throw new ArithmeticException("illegal operation, + - * / needed");
    }

    public int recognizeScrabble(String scrabble) throws NumberFormatException {
        String myS = scrabble.trim().toUpperCase(); // почистили
        int result = 0;
        if (Character.isDigit(myS.charAt(0))) { // если цифры арабские
            if (civilization != Numeral.ROMAN) { // trying to parse
                civilization = Numeral.ARABIC;
                try {
                    result = Integer.parseInt(myS);
                } catch (NumberFormatException nfe) {
                    System.out.println(nfe.getMessage());
                }
                //return r;
            } else throw new NumberFormatException("illegal operand");
        } else { // roman or undefined
            if (civilization != Numeral.ARABIC) {  // maybe Roman
                civilization = Numeral.ROMAN;
                try {
                    result = parseRomanNumeral(myS);
                } catch (NumberFormatException nfe) {
                    System.out.println(nfe.getMessage());
                }
                //return r;
            } else throw new NumberFormatException("illegal operand");
        }
        if (result > 0) return result;
        else throw new NumberFormatException("illegal operand");
    }
}
