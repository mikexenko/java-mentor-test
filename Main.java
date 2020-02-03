package com.mike;

import java.util.*;


public class Main {

    public static void main(String[] args) {

        System.out.print("Type scrabbles: ");
        String input = new Scanner(System.in).nextLine();

        System.out.println("Guru said");
        Guru myGuru = new Guru(input);
        //myGuru.lookAt(input);
        System.out.println(myGuru.craftAndSay());
    }
}
