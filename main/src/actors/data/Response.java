package actors.data;

public class Response implements Msg {

    public String requestId;

    public String responseId;

    public Response(String requestId, String responseId) {
        this.requestId = requestId;
        this.responseId = responseId;
    }

    @Override
    public String toString() {
        return "Response{" +
                "requestId='" + requestId + '\'' +
                ", responseId='" + responseId + '\'' +
                '}';
    }
}
