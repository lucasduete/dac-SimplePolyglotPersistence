package io.github.lucasduete.simplePolyglotPersistence.dao.interfaces;

import io.github.lucasduete.simplePolyglotPersistence.entities.User;

import java.util.List;

public interface UserDaoInterface {

    public void save(User user);
    public void update(User user, String lastEmail);
    public void remove(User user);
    public List<User> listAll();
}
