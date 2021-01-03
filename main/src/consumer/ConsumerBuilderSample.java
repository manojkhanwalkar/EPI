package consumer;

import java.util.function.Consumer;

public class ConsumerBuilderSample {

    /*

    sesClient.sendEmail(
    email -> email.destination(d -> d.toAddresses("to-email@domain.com")
                                     .bccAddresses("bcc-email@domain.com"))
                  .replyToAddresses("reply-to@domain.com")
                  .message(m -> m.subject(s -> s.charset("UTF-8")
                                                .data("Subject Line"))
                                 .body(b -> b.text(t -> t.data("The body of the email")
                                                         .charset("UTF-8")))));
     */

    public void sendEmail(Consumer<SendEmailRequest.Builder> emailRequestBuilder)
    {

        SendEmailRequest.Builder builder = new SendEmailRequest.Builder();
        emailRequestBuilder.accept(builder);

        SendEmailRequest sendEmailRequest = builder.build();

        System.out.println("Sending  " + sendEmailRequest);

    }


    public static void main(String[] args) {

        ConsumerBuilderSample sample = new ConsumerBuilderSample();

        sample.sendEmail(email->email.replyToAddresses("Hello@test.com").bccAddress("bcc@domain.com"));


    }


}
