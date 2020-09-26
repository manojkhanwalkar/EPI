package mynimbus;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.ECDHDecrypter;
import com.nimbusds.jose.crypto.ECDHEncrypter;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;
import java.security.interfaces.ECPrivateKey;
import java.util.HashMap;
import java.util.Map;

public class Nimbus {

  private static Map<String, ECKey> keyIdMap = new HashMap<>();

  public static void main(final String[] args) throws Exception {

    var senderJWKSigning = new ECKeyGenerator(Curve.P_256).keyID("S-123")
        .keyUse(KeyUse.SIGNATURE).generate();
    var receiverJWKEncryption = new ECKeyGenerator(Curve.P_256).keyID("E-789")
        .keyUse(KeyUse.ENCRYPTION).generate();
    var senderSigningPublicJWK = senderJWKSigning.toPublicJWK();
    var receiverKAPublicJWK = receiverJWKEncryption.toPublicJWK();

    keyIdMap.put(senderSigningPublicJWK.getKeyID(), senderSigningPublicJWK.toPublicJWK());

    var sender = new Sender(senderJWKSigning, receiverKAPublicJWK);
    var receiver = new Receiver(senderJWKSigning.getKeyID(),
        receiverJWKEncryption);

    // Actual exchange of msg from sender to receiver
    var msg = "hello";
    var senderMsg = sender.send(msg);
    System.out.println("Received msg: " + receiver.receive(senderMsg));
  }

  public static class Sender {

    private ECKey signing;
    private ECKey receiverPublicEncryptionKey;

    public Sender(ECKey signing, ECKey encryption) {
      this.signing = signing;
      this.receiverPublicEncryptionKey = encryption;
    }

    private String sign(String msg) throws Exception {
      System.out.println("JWS payload message: " + msg);
      var payload = new Payload(msg);
      var header = new JWSHeader.Builder(JWSAlgorithm.ES256).contentType("text/plain").build();

      System.out.println("JWS header: " + header.toJSONObject());
      var jwsObject = new JWSObject(header, payload);

      var signer = new ECDSASigner((ECPrivateKey) signing.toPrivateKey());
      jwsObject.sign(signer);
      return jwsObject.serialize();
    }

    private String encrypt(String jwsPayload) throws Exception {
      var jweHeader = new JWEHeader.Builder(JWEAlgorithm.ECDH_ES,
          EncryptionMethod.A256GCM)
          .build();

      var jweObject = new JWEObject(jweHeader, new Payload(jwsPayload));

      System.out.println("JWE header: " + jweHeader.toJSONObject());
      jweObject.encrypt(new ECDHEncrypter(receiverPublicEncryptionKey.toECPublicKey()));

      return jweObject.serialize();
    }

    public String send(String msg) throws Exception {
      var encryptedData = encrypt(sign(msg));
      System.out.println("JWE encrypted payload: " + encryptedData);
      return encryptedData;
    }

  }

  public static class Receiver {

    private ECKey signing;
    private ECKey encryption;

    public Receiver(String signingKeyId, ECKey encryptionKey) {
      this.signing = keyIdMap.get(signingKeyId);
      this.encryption = encryptionKey;
    }

    private String verify(String msg) throws Exception {
      var jwsObject = JWSObject.parse(msg);
      var verifier = new ECDSAVerifier(signing.toECPublicKey());
     if (jwsObject.verify(verifier)) {
        System.out.println("Receiver successfully verified signature");
        return jwsObject.getPayload().toString();
      }
      return null;
    }

    private String decrypt(String msg) throws Exception {
      var jweObject = JWEObject.parse(msg);


      jweObject
          .decrypt(new ECDHDecrypter((ECPrivateKey) encryption.toPrivateKey()));
      var decryptedJWE = jweObject.getPayload().toString();
      System.out.println("JWE decrypted payload: " + jweObject.getPayload());

      return decryptedJWE;
    }

    public String receive(String jwt) throws Exception {
      return verify(decrypt(jwt));
    }
  }
}
