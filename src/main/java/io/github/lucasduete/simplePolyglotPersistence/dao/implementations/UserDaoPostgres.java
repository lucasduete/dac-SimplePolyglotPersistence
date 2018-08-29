package io.github.lucasduete.simplePolyglotPersistence.dao.implementations;

import io.github.lucasduete.simplePolyglotPersistence.dao.interfaces.UserDaoInterface;
import io.github.lucasduete.simplePolyglotPersistence.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserDaoPostgres implements UserDaoInterface {

    private final Connection connection;

    public UserDaoPostgres() throws SQLException, ClassNotFoundException {
        this.connection = Conexao.getConnection();
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO User(Email, Password, FullName) VALUES (?,?,?);";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFullName());

            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(User user, String lastEmail) {
        String sql = "UPDATE User SET Email = ?, Password = ?, FullName = ? WHERE Email = ?;";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFullName());
            stmt.setString(4, lastEmail);

            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void remove(User user) {
        String sql = "DELETE FROM User WHERE Email = ?;";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, user.getEmail());

            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<User> listAll() {
        String sql = "SELECT * FROM User;";
        List<User> users = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next())
                users.add(
                        new User(
                                resultSet.getString("Email"),
                                resultSet.getString("Password"),
                                resultSet.getString("FullName")
                        )
                );

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        if (users.isEmpty()) return Collections.emptyList();
        else return Collections.unmodifiableList(users);
    }
}
