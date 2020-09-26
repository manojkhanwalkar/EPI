package mynimbus;



import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;

import java.util.*;
import java.util.stream.Collectors;

import static mynimbus.JWUtil.*;


public class Sender {



    Map<String, JWK> kaPubKSenderMap = new HashMap<>();
    Map<String,JWK> kaPrivKSenderMap = new HashMap<>();
    Map<String,JWK> signPubKSenderMap = new HashMap<>();
    Map<String,JWK> signPrivKSenderMap = new HashMap<>();





    public String getKAandSignPubKeys()
    {

        List<JWK> list = new ArrayList<>();
        list.addAll(kaPubKSenderMap.values()) ;
        list.addAll(signPubKSenderMap.values() ) ;
        JWKSet set = new JWKSet(list);
        return set.toJSONObject().toJSONString();



    }

    ClientHandler clientHandler;



    private void createKeys()
    {
        JWUtil.createKeys(signPubKSenderMap,signPrivKSenderMap,"signSender");

        JWUtil.createKeys(kaPubKSenderMap,kaPrivKSenderMap,"kaSender");


    }





    String clientId;

    public Sender(String clientId)
    {

        this.clientId = clientId;
        createKeys();

    }

    Map<String,JWK> signreceiverKeys = new HashMap<>();
    Map<String,JWK> kareceiverKeys = new HashMap<>();

    public void init(ClientHandler clientHandler) throws Exception
    {

        String jsonWebKeySet = clientHandler.getKAandSignPubKeys();
        JWKSet set = JWKSet.parse(jsonWebKeySet);

        set.getKeys().stream().filter(key->key.getKeyID().startsWith("sign")).forEach(key->{ signreceiverKeys.put(key.getKeyID(),key);});
        set.getKeys().stream().filter(key->key.getKeyID().startsWith("ka")).forEach(key->{ kareceiverKeys.put(key.getKeyID(),key);});

       // kaPubKReceiver = (PublicJsonWebKey)set.findJsonWebKey("kaPubKReceiver",null,null,null);
        //signPubKReceiver =(PublicJsonWebKey)set.findJsonWebKey("signPubKReceiver",null,null,null);

        this.clientHandler = clientHandler;


    }



    public void sendMessagetoReceiver(String payload) throws Exception
    {
        Random random = new Random();

        int max = random.nextInt(kareceiverKeys.size());

        JWK kaPubKReceiver = kareceiverKeys.values().stream().collect(Collectors.toList()).get(max);

        max = random.nextInt(kareceiverKeys.size());

        JWK signPrivKSender = signPrivKSenderMap.values().stream().collect(Collectors.toList()).get(max);

        System.out.println("Encrypt key " + kaPubKReceiver.getKeyID() + " " + "Sign Key " + signPrivKSender.getKeyID());



        //        jwe.setHeader(HeaderParameterNames.AGREEMENT_PARTY_V_INFO, UUID.randomUUID().toString());

      //  Header header = new Header(HeaderParameterNames.AGREEMENT_PARTY_U_INFO, UUID.randomUUID().toString());
        List<Header> headers = List.of();

        String str = encrypt(headers , kaPubKReceiver,sign(signPrivKSender,payload));

        clientHandler.recvMessageFromSender(str);
    }


    public void recvMessageFromReceover(String message) throws Exception
    {
        String decryptedMsg = decrypt(kaPrivKSenderMap,message);

       // System.out.println(decryptedMsg);

//        String payload = verify(signPubKReceiver.getPublicKey(),decryptedMsg).get();

        String payload = verify(signreceiverKeys,decryptedMsg).get();


        System.out.println(payload);

    }





}
