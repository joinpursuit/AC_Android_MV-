package nyc.c4q.doggos.doggos_list;

import java.util.List;

public class ImagesByBreedResponse {

    private String status;
    private List<String> message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }
}