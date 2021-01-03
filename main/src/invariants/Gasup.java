package invariants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Gasup {


    static class City{

        int gas;
        int city;

        @Override
        public String toString() {
            return "City{" +
                    "gas=" + gas +
                    ", city=" + city +
                    '}';
        }
    }


    public static void main(String[] args) {



        Integer[] arr1 = {50,20,5,30,25,10,10};
        List<Integer> gallons = Arrays.asList(arr1);

        Integer[] arr2 = { 900,600,200,400,600,200,100};


        List<Integer> miles = Arrays.asList(arr2);

        final int MPG = 20;

        int remainingGas = 0;

        City min = new City();

        for (int i=1;i<gallons.size();i++)
        {
            remainingGas = remainingGas+gallons.get(i-1)-(miles.get(i-1)/MPG);
            if (remainingGas<min.gas)
            {
                min.gas = remainingGas;
                min.city=i;
            }

        }

        System.out.println(min);

    }





}
