package sms;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class PhoneSMSTester {


    public static void main(String[] args) {
        AmazonSNS snsClient = AmazonSNSClientBuilder.standard()
                .withRegion(Regions.US_EAST_1).build();
               // .withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();

        Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();
        //smsAttributes.put("AWS.SNS.SMS.SenderID",new MessageAttributeValue().withStringValue("SENDER-ID").withDataType("String"));
        smsAttributes.put("AWS.SNS.SMS.SMSType",new MessageAttributeValue().withStringValue("Transactional").withDataType("String"));

        PublishRequest request = new PublishRequest();
        request.withMessage("Your 6 digit OTP code is " + OTPString())
                .withPhoneNumber("7015168317")
                .withMessageAttributes(smsAttributes);
        PublishResult result=snsClient.publish(request);

        System.out.println(result);
    }

    static ThreadLocalRandom random = ThreadLocalRandom.current();



    public static String OTPString()
    {

        StringBuilder builder = new StringBuilder(6);
        for (int i=0;i<6;i++)
        {
            builder.append(random.nextInt(10));
        }

        return builder.toString();
    }


}
