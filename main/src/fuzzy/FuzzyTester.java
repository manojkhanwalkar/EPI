package fuzzy;

public class FuzzyTester {


    public static void main(String[] args) {


        FuzzyNameMatch f = new FuzzyNameMatch();

        f.add("Manoj" , "Khanwalkar");
        f.add("Manish" , "Khanolkar");
        f.add("Manjo" , "Khanwalkar");


        System.out.println(f.getMatches("Manoj" , "Khanwalkar"));


    }
}
