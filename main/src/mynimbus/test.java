package mynimbus;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jose.jwk.gen.*;



public class test {


 /*   public static void main(String[] args) throws Exception {

        // Create an HMAC-protected JWS object with some payload
        JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256),
                new Payload("Hello, world!"));

// We need a 256-bit key for HS256 which must be pre-shared
        byte[] sharedKey = new byte[32];
        new SecureRandom().nextBytes(sharedKey);

// Apply the HMAC to the JWS object
        jwsObject.sign(new MACSigner(sharedKey));

// Output in URL-safe format
        System.out.println(jwsObject.serialize());



    }*/

    public static void main(String[] args) throws Exception {



// Generate an EC key pair
        ECKey ecJWK = new ECKeyGenerator(Curve.P_256)
                .keyID("123")
                .generate();
        ECKey ecPublicJWK = ecJWK.toPublicJWK();

// Create the EC signer
        JWSSigner signer = new ECDSASigner(ecJWK);

// Creates the JWS object with payload
        JWSObject jwsObject = new JWSObject(
                new JWSHeader.Builder(JWSAlgorithm.ES256).keyID(ecJWK.getKeyID()).build(),
                new Payload("Elliptic cure"));

// Compute the EC signature
        jwsObject.sign(signer);

// Serialize the JWS to compact form
        String s = jwsObject.serialize();


// The recipient creates a verifier with the public EC key
        JWSVerifier verifier = new ECDSAVerifier(ecPublicJWK);

        // Verify the EC signature
        System.out.println(jwsObject.verify(verifier));
        System.out.println(jwsObject.getPayload().toString());

    }
}
