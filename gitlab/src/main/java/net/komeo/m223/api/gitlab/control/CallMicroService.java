package net.komeo.m223.api.gitlab.control;

import java.util.List;
import javax.ws.rs.core.MediaType;
import com.sun.jersey.api.client.*;
import org.gitlab4j.api.models.User;

public class CallMicroService {
    
    // DB_USER is same as PROJECT_NAME
    private final String PROJECT_NAME = System.getenv("DB_USER");
    final String REST_URI = "https://" + PROJECT_NAME + ".komeo.net";

    public List<User> getAllUser() {
        Client client = Client.create();
        WebResource resource = client.resource(REST_URI);
        List<User> users = resource.path("user/all").accept(MediaType.APPLICATION_JSON).get(new GenericType<List<User>>() {
        });
        System.out.println(this.getClass().getName() + ".getAllUsers " + users.size() + " found");
        return users;
    }

    public long elapseTime() {
        long start = System.nanoTime();
        List<User> users = this.getAllUser();
        long stop = System.nanoTime();
        return stop - start;
    }
}
