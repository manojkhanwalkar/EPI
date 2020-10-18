package consumer;

public class SendEmailRequest {


    private SendEmailRequest()
    {

    }


    String address ;

    String bccAddress ;

    @Override
    public String toString() {
        return "SendEmailRequest{" +
                "address='" + address + '\'' +
                ", bccAddress='" + bccAddress + '\'' +
                '}';
    }


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

    static class Builder
    {
        public Builder()
        {

        }

        String address;
        String bccAddress;

        public Builder replyToAddresses(String address)
        {
            this.address=address;

            return this;
        }

        public Builder bccAddress(String address)
        {
            this.bccAddress=address;

            return this;
        }


        public SendEmailRequest build()
        {


            SendEmailRequest sendEmailRequest = new SendEmailRequest();

            sendEmailRequest.address = address;

            sendEmailRequest.bccAddress = bccAddress;

            return sendEmailRequest;
        }

    }


}
