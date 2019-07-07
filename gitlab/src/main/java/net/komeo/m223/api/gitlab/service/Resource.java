package net.komeo.m223.api.gitlab.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import net.komeo.m223.api.gitlab.boundary.Engine;
import net.komeo.m223.api.gitlab.entity.KomeoUser;
import org.gitlab4j.api.models.User;

@Path("/")
public class Resource {

    @EJB
    Engine engine;

    private final Type gitLabUserType = new TypeToken<List<User>>() {
    }.getType();

    private final Type komeoDBUserType = new TypeToken<List<KomeoUser>>() {
    }.getType();

    private final Gson gson = new Gson();

    @GET
    @Path("user/gitlab")
    @Produces(MediaType.APPLICATION_JSON)
    public String allUserFromGitLab() {
        List<User> users = engine.getAllUsersFromGitLab();
        return gson.toJson(users, gitLabUserType);
    }

    @GET
    @Path("user/db")
    @Produces(MediaType.APPLICATION_JSON)
    public String allUserFromDB() {
        List<KomeoUser> users = engine.getAllUsersFromDB();
        return gson.toJson(users, komeoDBUserType);
    }

    @GET
    @Path("merge")
    @Produces(MediaType.TEXT_HTML)
    public String mergeToDB() {
        return engine.merge();
    }

    @GET
    @Path("elapse")
    @Produces(MediaType.TEXT_HTML)
    public String elapseTime() {
        long git = engine.elapeTimeToCallGitLab();
        long db = engine.elapeTimeToCallDB();
        String elapse = "<style>\n"
                + "p table {\n"
                + "    font-family: arial, sans-serif;\n"
                + "    border-collapse: collapse;\n"
                + "    width: 100%;\n"
                + "}\n"
                + "\n"
                + "td, th {\n"
                + "    border: 1px solid #dddddd;\n"
                + "    text-align: left;\n"
                + "    padding: 8px;\n"
                + "}\n"
                + "\n"
                + "tr:nth-child(even) {\n"
                + "    background-color: #dddddd;\n"
                + "}\n"
                + "</style>\n"
                + "</head>\n"
                + "<body>\n"
                + "\n"
                + "<p>Difference Of Elapse Time Is Factor : " + git / db + "</p>\n"
                + "\n"
                + "<table>\n"
                + "  <tr>\n"
                + "    <th>Call To</th>\n"
                + "    <th>Git Lab</th>\n"
                + "    <th>DB</th>\n"
                + "  </tr>\n"
                + "  <tr>\n"
                + "    <td>seconds</td>\n"
                + "    <td>" + NANOSECONDS.toSeconds(git) + "</td>\n"
                + "    <td>" + NANOSECONDS.toSeconds(db) + "</td>\n"
                + "  </tr>\n"
                + "  <tr>\n"
                + "    <td>milli seconds</td>\n"
                + "    <td>" + NANOSECONDS.toMillis(git) + "</td>\n"
                + "    <td>" + NANOSECONDS.toMillis(db) + "</td>\n"
                + "  </tr>\n"
                + "  <tr>\n"
                + "    <td>micro seconds</td>\n"
                + "    <td>" + NANOSECONDS.toMicros(git) + "</td>\n"
                + "    <td>" + NANOSECONDS.toMicros(db) + "</td>\n"
                + "  </tr>\n"
                + "  <tr>\n"
                + "    <td>nanno seconds</td>\n"
                + "    <td>" + NANOSECONDS.toNanos(git) + "</td>\n"
                + "    <td>" + NANOSECONDS.toNanos(db) + "</td>\n"
                + "  </tr>\n"
                + "</table>\n";
        
        return elapse;
    }

    @GET
    @Path("hits")
    public String hitsFromHitCounter() {
        return engine.tellHits();
    }

    @GET
    @Path("echo/{message}")
    public String echo(@PathParam("message") String echoMessage) {
        return engine.echo(echoMessage);
    }
}
