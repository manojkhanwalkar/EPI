package mynimbus;


import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.List.of;
import static mynimbus.JWUtil.*;


public class ClientHandler {





    Map<String, JWK> kaPubKReceiverMap = new HashMap<>();
    Map<String,JWK> kaPrivKReceiverMap = new HashMap<>();
    Map<String,JWK> signPubKReceiverMap = new HashMap<>();
    Map<String,JWK> signPrivKReceiverMap = new HashMap<>();


    public String getKAandSignPubKeys()
    {
        List<JWK> list = new ArrayList<>();
       list.addAll(kaPubKReceiverMap.values()) ;
        list.addAll(signPubKReceiverMap.values() ) ;
       JWKSet set = new JWKSet(list);
         return set.toJSONObject().toJSONString();
    }

    Sender sender;


//TODO - use static keys and then use JWKS to get the static keys and then do key rotation

    Map<String,JWK> signSenderKeys = new HashMap<>();
    Map<String,JWK> kaSenderKeys = new HashMap<>();


    public void init(Sender sender) throws Exception
    {

        String jsonWebKeySet = sender.getKAandSignPubKeys();
        JWKSet set = JWKSet.parse(jsonWebKeySet);


        set.getKeys().stream().filter(key->key.getKeyID().startsWith("sign")).forEach(key->{ signSenderKeys.put(key.getKeyID(),key);});
        set.getKeys().stream().filter(key->key.getKeyID().startsWith("ka")).forEach(key->{ kaSenderKeys.put(key.getKeyID(),key);});


        this.sender = sender;




    }

    private void createKeys()
    {
            JWUtil.createKeys(signPubKReceiverMap,signPrivKReceiverMap,"signReceiver");

            JWUtil.createKeys(kaPubKReceiverMap,kaPrivKReceiverMap,"kaReceiver");


    }



    public ClientHandler()
    {
        createKeys();
    }

    public void recvMessageFromSender(String message) throws Exception
    {

        System.out.println(verify(signSenderKeys,decrypt(kaPrivKReceiverMap,message)).orElseThrow());

        sendMessageToSender("Received message ");

       // alternate(message);

    }


    public void sendMessageToSender(String payload) throws Exception
    {

        JWK kaPubKSender = kaSenderKeys.values().stream().findAny().get();

        JWK signPrivKReceiver = signPrivKReceiverMap.values().stream().findAny().get();

        //   jwe.setHeader(HeaderParameterNames.AGREEMENT_PARTY_U_INFO, UUID.randomUUID().toString());
        //        jwe.setHeader(HeaderParameterNames.AGREEMENT_PARTY_V_INFO, UUID.randomUUID().toString());


       /* Header header = new Header(HeaderParameterNames.AGREEMENT_PARTY_V_INFO, UUID.randomUUID().toString());
        var headers = List.of(header);*/

       List<Header> headers = List.of();

        String str = encrypt(headers , kaPubKSender,sign(signPrivKReceiver,payload));

        sender.recvMessageFromReceover(str);

    }








}
