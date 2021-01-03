package com.idealista.fpe.algorithm.ff1;

import com.idealista.fpe.FormatPreservingEncryption;
import com.idealista.fpe.algorithm.ff1.FF1Algorithm;
import com.idealista.fpe.builder.FormatPreservingEncryptionBuilder;
import com.idealista.fpe.component.functions.prf.DefaultPseudoRandomFunction;

import java.security.SecureRandom;
import java.security.SecureRandom;
import java.util.Arrays;

import com.idealista.fpe.component.functions.prf.DefaultPseudoRandomFunction;



public class FPETest {

    public static void main(String[] args) {




    Integer radix = 10;

    byte[] key = new byte[32];


    byte[] tweak = new byte[10];
        byte[] tweak1 = new byte[10];

       SecureRandom secureRandom = new SecureRandom();

        //secureRandom.nextBytes(key);


       secureRandom.nextBytes(tweak);
        secureRandom.nextBytes(tweak1);

        int[] input = { 1,2,3,4,5,6,7,8,9,1,2,3,4,5,6,7};

        int[] cipherText = FF1Algorithm.encrypt(input, radix, tweak, new DefaultPseudoRandomFunction(key));

        int[] cipherText1 = FF1Algorithm.encrypt(input, radix, tweak1, new DefaultPseudoRandomFunction(key));

        for (int i=0;i<input.length;i++)
            System.out.print(input[i] + " ");

        System.out.println();
        for (int i=0;i<input.length;i++)
            System.out.print(cipherText[i] + " ");

        System.out.println();

        for (int i=0;i<input.length;i++)
            System.out.print(cipherText1[i] + " ");

        System.out.println();

        int[] decText = FF1Algorithm.decrypt(cipherText, radix, tweak, new DefaultPseudoRandomFunction(key));


        for (int i=0;i<input.length;i++)
            System.out.print(decText[i] + " ");




    }



}

