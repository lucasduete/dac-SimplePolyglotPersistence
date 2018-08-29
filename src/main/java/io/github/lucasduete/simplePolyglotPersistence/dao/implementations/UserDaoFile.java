package io.github.lucasduete.simplePolyglotPersistence.dao.implementations;

import io.github.lucasduete.simplePolyglotPersistence.dao.interfaces.UserDaoInterface;
import io.github.lucasduete.simplePolyglotPersistence.entities.User;

import java.io.*;
import java.util.Collections;
import java.util.List;

public class UserDaoFile implements UserDaoInterface {

    private final File FILE;

    public UserDaoFile() throws IOException {
        FILE = new File("user.bin");

        if (!FILE.exists()) FILE.createNewFile();
    }

    public void save(User user) {
        List<User> usersPersisted = listAllInternal();

        usersPersisted.add(user);

        if (usersPersisted.stream().filter(userPersisted -> userPersisted.getEmail().equals(user.getEmail())).findAny().isPresent())
            throw new RuntimeException("Email já registrado");

        persistList(usersPersisted);
    }

    public void update(User user, String lastEmail) {
        List<User> usersPersisted = listAllInternal();

        if (usersPersisted.stream().filter(userPersisted -> userPersisted.getEmail().equals(lastEmail)).count() != 1)
            throw new RuntimeException("Email inválido");

        for (int i = 0; i < usersPersisted.size(); i++) {
            if (usersPersisted.get(i).getEmail().equals(lastEmail))
                usersPersisted.set(i, user);
        }

        persistList(usersPersisted);
    }

    public void remove(User user) {
        List<User> usersPersisted = listAllInternal();

        if (!usersPersisted.isEmpty()) {

            usersPersisted.removeIf(userPersisted -> userPersisted.getEmail().equals(user));
            persistList(usersPersisted);
        }
    }

    public List<User> listAll() {
        if (FILE.length() == 0) return Collections.emptyList();
        else {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(FILE))) {
                return Collections.unmodifiableList( (List<User>) inputStream.readObject() );
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        return Collections.emptyList();
    }

    public List<User> listAllInternal() {
        if (FILE.length() == 0) return Collections.emptyList();
        else {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(FILE))) {
                return (List<User>) inputStream.readObject();
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        return Collections.emptyList();
    }

    private void persistList(List<User> users) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(FILE))) {
            outputStream.writeObject(users);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
