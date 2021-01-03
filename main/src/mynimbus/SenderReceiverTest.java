package mynimbus;



public class SenderReceiverTest {


    public static void main(String[] args) throws Exception  {


        Sender sender = new Sender("client1");
        Sender sender2 = new Sender("client2");


        {
            ClientHandler clientHandler = Receiver.connect("client1", sender);
            sender.init(clientHandler);
        }

        {
            ClientHandler clientHandler = Receiver.connect("client2", sender2);
            sender2.init(clientHandler);
        }

        for (int i=0;  i<1;i++)
        {

            sender.sendMessagetoReceiver("From sender1 to the clientHandler in the Nimbus world ");
            sender2.sendMessagetoReceiver("From senderrrrrrrrrrrrrr 2 to the clientHandler in the Nimbus World ");

        }



    }
}
