package mynimbus;


import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDHDecrypter;
import com.nimbusds.jose.crypto.ECDHEncrypter;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JWUtil {

   // public enum SignType { Complete , Compressed }


    public static void createKeys(Map<String,JWK> publicJsonWebKeyMap , Map<String,JWK> privateJsonWebKeyMap,String prepend)
    {
        for (int i=0;i<10;i++) {
            try {
                KeyPairGenerator gen = KeyPairGenerator.getInstance("EC");
                gen.initialize(256);
                KeyPair keyPair = gen.generateKeyPair();

                String name = prepend + "_" + UUID.randomUUID().toString();

                publicJsonWebKeyMap.put(name, createECJWK(keyPair, name, false));
                privateJsonWebKeyMap.put(name, createECJWK(keyPair, name, true));


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


    public static String encrypt(List<Header> headers , JWK encrypterKey, String payload) throws Exception
    {

        var jweHeader = new JWEHeader.Builder(JWEAlgorithm.ECDH_ES,
                EncryptionMethod.A256GCM).keyID(encrypterKey.getKeyID())
                .build();

        var jweObject = new JWEObject(jweHeader, new Payload(payload));

        System.out.println("JWE header: " + jweHeader.toJSONObject());
        jweObject.encrypt(new ECDHEncrypter(encrypterKey.toECKey()));


        return jweObject.serialize();

//TODO - U / V info to be set from headers


 //           headers.stream().forEach(header-> jwe.setHeader(header.getName(),header.getValue()));



    }


    public static String decrypt(Map<String,JWK> keyMap,  String payload) throws Exception
    {


        var jweObject = JWEObject.parse(payload);

        String keyId = jweObject.getHeader().getKeyID();

        JWK decrypterKey = keyMap.get(keyId);

        //TODO - set and process expiry time


        jweObject
                .decrypt(new ECDHDecrypter((ECPrivateKey) decrypterKey.toECKey().toPrivateKey()));
        var decryptedJWE = jweObject.getPayload().toString();

        return decryptedJWE;



    }


/*
  private String sign(String msg) throws Exception {
      System.out.println("JWS payload message: " + msg);
    }
 */




    public static String sign(JWK jwk , String payload) throws JOSEException {


        return sign(jwk.toECKey().toPrivateKey(),payload, jwk.getKeyID());
    }



    public static String sign(PrivateKey privateKey , String msg,String keyId) throws JOSEException {

        var payload = new Payload(msg);
        var header = new JWSHeader.Builder(JWSAlgorithm.ES256).contentType("text/plain").keyID(keyId).build();


        var jwsObject = new JWSObject(header, payload);

        var signer = new ECDSASigner((ECPrivateKey) privateKey);
        jwsObject.sign(signer);
        return jwsObject.serialize();



    }



    public static Optional<String> verify(Map<String, JWK> keys , String compactSerialization) throws Exception
    {

        var jwsObject = JWSObject.parse(compactSerialization);

        String keyId = jwsObject.getHeader().getKeyID();

        JWK publicKey  = keys.get(keyId);

        var verifier = new ECDSAVerifier(publicKey.toECKey());

        if (jwsObject.verify(verifier)) {
            System.out.println("Receiver successfully verified signature");
            return Optional.of(jwsObject.getPayload().toString());
        }

        return Optional.empty();



    }


    public static JWK createECJWK(KeyPair keyPair, String keyId, boolean includePrivate) throws Exception
    {

        ECPublicKey ecPublicKey = (ECPublicKey)keyPair.getPublic();


        // Convert to JWK format
        ECKey.Builder builder =  new ECKey.Builder(Curve.P_256,ecPublicKey).keyID(keyId);
        if (includePrivate)
            builder.privateKey(keyPair.getPrivate());

        JWK jwk = builder.build();


        return jwk;


    }







}
