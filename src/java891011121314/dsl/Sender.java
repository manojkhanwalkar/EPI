package java891011121314.dsl;

import java891011121314.JSONUtil;
import java891011121314.phaserframework.*;

import java.util.Base64;

public class Sender {

    public static void main(String[] args) {

      //  String str = "Hello World";

        PII pii = new PII();
        pii.email="manoj@test.com";
        pii.name="Manoj";
        pii.tel="(732)888-0000";
        pii.ssn = "909-99-8888";

        Response response = Sender.validate(pii).convert().encrypt().sign("Secret Key").send();

        System.out.println(response);

    }

    public static MessageBuilder validate(PII pii)
    {
        return new MessageBuilder(pii).validate();
    }


    public static MessageParser verify(Request request)
    {
        return new MessageParser(request).verify("Secret Key");
    }

    static class PIIValidator
    {

        PII pii;

        boolean status = true;

        public PIIValidator(PII pii)
        {
            this.pii=pii;
        }

        Validator nameValidator = new NameValidator();
        Validator emailValidator = new EmailValidator();
        Validator ssnValidator = new SSNValidator();
        Validator telValidator = new TelephoneValidator();


        public boolean isValid()
        {
            return status;
        }
        public PIIValidator name()
        {
            boolean result = nameValidator.validate(pii);
            if (!result) status = result;
            return this;
        }

        public PIIValidator email()
        {
            boolean result =emailValidator.validate(pii);
            if (!result) status = result;
            return this;
        }

        public PIIValidator ssn()
        {
            boolean result =ssnValidator.validate(pii);
            if (!result) status = result;
            return this;
        }

        public PIIValidator tel()
        {
            boolean result =telValidator.validate(pii);
            if (!result) status = result;
            return this;
        }



    }


    static class MessageParser
    {

        Request request;

        public MessageParser(Request request) {
            this.request = request;
        }

        public MessageParser verify(String secret)
        {

                byte[] bytes = HMACUtil.calcHmacSha256(secret.getBytes(),request.payload.getBytes());

                String sign = Base64.getEncoder().encodeToString(bytes);
                if (!sign.equals(request.sign)) {

                    throw new RuntimeException("Signatures do not match");
                }

                return this;



        }

        String str;

        PII pii;

        public MessageParser decrypt()
        {
            str  = new String(Base64.getDecoder().decode(request.payload));

            System.out.println("Decoded str " + str);
            return this;
        }

        public MessageParser convert()
        {
            pii = (PII)JSONUtil.fromJSON(str,PII.class);

            return this;
        }

        public MessageParser validate()
        {

            boolean result = new PIIValidator(pii).tel().ssn().email().name().isValid();

            if (result)
              return this;
            else
                throw new RuntimeException("Invalid attributes in pii");
        }

        public Response response()
        {
           Response response = new Response(Response.Status.Success,pii.toString());

            return response;
        }


    }


    static class Connection
    {

        Request request;


        public Connection(Request request) {
            this.request = request;
        }

        public Response send()
        {
            System.out.println(request);

            Response response = verify(request).decrypt().convert().validate().response();

            return response;


            //return new Response("","");
        }

    }



    static class Response
    {

        public enum Status { Success, Fail};

        Status status ;
        String payload;


        public Response(Status status, String payload) {
            this.status = status;
            this.payload = payload;
        }

        @Override
        public String toString() {
            return "Response{" +
                    "status=" + status +
                    ", payload='" + payload + '\'' +
                    '}';
        }
    }


    static class Request
    {
        String payload;
        String sign;

        public Request(String payload, String sign) {
            this.payload = payload;
            this.sign = sign;
        }

        @Override
        public String toString() {
            return "Request{" +
                    "payload='" + payload + '\'' +
                    ", sign='" + sign + '\'' +
                    '}';
        }
    }

    static class MessageBuilder
    {

        String str;

        PII pii;

        String sign;

        public MessageBuilder(PII pii)
        {
            this.pii = pii;
        }

        public MessageBuilder validate()
        {
            if (pii==null)
                throw new RuntimeException("Invalid Message");
            else
                return this;
        }

        public MessageBuilder convert()
        {
            str = JSONUtil.toJSON(pii);
            // convert string to json string .

            System.out.println("JSON " + str);
            return this;
        }

        public MessageBuilder encrypt()
        {
            str = Base64.getEncoder().encodeToString(str.getBytes());
            return this;
        }

        public Connection sign(String secret)
        {

            byte[] bytes = HMACUtil.calcHmacSha256(secret.getBytes(),str.getBytes());

            sign = Base64.getEncoder().encodeToString(bytes);
            return new Connection(new Request(str,sign));
        }


    }

}
