package net.komeo.m223.api.user.control;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.Pager;
import org.gitlab4j.api.models.User;

public class GitLabDAO {

    final String GIT_LAB_URI = "https://k289gitlab1.citrin.ch";

    // gitlab.api
    // ENTER TOKEN HERE
    final String TOKEN = "";

    GitLabApi gitLabApi = null;

    public GitLabDAO() {
        gitLabApi = new GitLabApi(GIT_LAB_URI, TOKEN);
    }

    public List<User> getAllUsers() {
        try {
            List<User> users = new ArrayList<>();
            // Get a Pager instance that will page through the users with 100 users per page
            Pager<User> userPager = gitLabApi.getUserApi().getUsers(100);

            // Iterate through the pages and add user to list
            while (userPager.hasNext()) {
                userPager.next().forEach((user) -> {
                    users.add(user);
                });
            }

            return users;
        } catch (GitLabApiException ex) {
            Logger.getLogger(GitLabDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public User getUserById(Integer id) {
        try {
            return gitLabApi.getUserApi().getUser(id);
        } catch (GitLabApiException ex) {
            Logger.getLogger(GitLabDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public User getUserByName(String name) {
        try {
            return gitLabApi.getUserApi().getUser(name);
        } catch (GitLabApiException ex) {
            Logger.getLogger(GitLabDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
