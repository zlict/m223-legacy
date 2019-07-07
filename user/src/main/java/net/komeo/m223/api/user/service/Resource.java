package net.komeo.m223.api.user.service;

import java.lang.reflect.Type;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.gitlab4j.api.models.User;
import net.komeo.m223.api.user.boundary.Engine;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Path("/")
public class Resource {

    @EJB
    Engine engine;

    private final Type typeUserList = new TypeToken<List<User>>() {
    }.getType();

    private final Type typeUser = new TypeToken<User>() {
    }.getType();

    private final Gson gson = new Gson();

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public String all() {
        List<User> users = engine.getAllUsers();
        return gson.toJson(users, typeUserList);
    }

    @GET
    @Path("id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String byId(@PathParam("id") Integer id) {
        User user = engine.getUserById(id);
        return gson.toJson(user, typeUser);
    }

    @GET
    @Path("name/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public String byName(@PathParam("name") String name) {
        User user = engine.getUserByName(name);
        return gson.toJson(user, typeUser);
    }

    @GET
    @Path("echo/{message}")
    public String echo(@PathParam("message") String echoMessage) {
        return engine.echo(echoMessage);
    }
}
