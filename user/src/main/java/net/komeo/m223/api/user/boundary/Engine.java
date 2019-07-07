package net.komeo.m223.api.user.boundary;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import net.komeo.m223.api.user.control.GitLabDAO;
import org.gitlab4j.api.models.User;

@Stateless
public class Engine {

    @Inject
    GitLabDAO gitLabDAO;

    public List<User> getAllUsers() {
        return gitLabDAO.getAllUsers();
    }

    public User getUserById(Integer id) {
        return gitLabDAO.getUserById(id);
    }

    public User getUserByName(String name) {
        return gitLabDAO.getUserByName(name);
    }

    public String echo(String echoMessage) {
        return "WildFly echoes : " + echoMessage;
    }
}
