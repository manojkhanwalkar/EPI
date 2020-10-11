package aero;

public class AeroNodeTester {

    public static void main(String[] args) {

        Node node = new Node();

        node.put("Hello1" , "World1");

        String str = node.get("Hello");

        System.out.println(str);

        //node.delete("Hello");

        System.out.println(node.get("Hello1"));


        node.shutdown();
    }

}
