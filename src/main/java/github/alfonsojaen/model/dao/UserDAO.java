package github.alfonsojaen.model.dao;

import github.alfonsojaen.model.connection.ConnectionMariaDB;
import github.alfonsojaen.model.entity.User;
import github.alfonsojaen.model.interfaces.InterfaceUserDAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDAO implements InterfaceUserDAO<User> {

    private final static String INSERT = "INSERT INTO user (username,password,email,name) VALUES (?,?,?,?)";
    private final static String UPDATE = "UPDATE user SET  password=?, email=?, name=? WHERE username=?";
    private final static String DELETE = "DELETE FROM user WHERE username=?";
    private final static String FINDBYUSERNAME = "SELECT a.username,a.password,a.email,a.name FROM user AS a WHERE a.username=?";
    private final static String QUERY = "SELECT username FROM user WHERE email=? AND password=?";

    private Connection conn;

    /**
     * Constructor that initializes the connection to the database.
     */
    public UserDAO() {
        conn = ConnectionMariaDB.getConnection();
    }

    /**
     * Saves a user in the database.
     * @param user The user to be saved.
     * @return The saved user, or null if an error occurred.
     * @throws SQLException If an error occurs while executing the operation in the database.
     */
    @Override
    public User save(User user) throws SQLException {
        User result = new User();
        User u = findByUserName(user.getUsername());
        if (u == null) {
            //INSERT
            try (PreparedStatement ps = ConnectionMariaDB.getConnection().prepareStatement(INSERT)) {
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPassword());
                ps.setString(3, user.getEmail());
                ps.setString(4, user.getName());
                ps.executeUpdate();


            } catch (SQLException e) {
                e.printStackTrace();
            }
    } else {
        try (PreparedStatement ps = ConnectionMariaDB.getConnection().prepareStatement(UPDATE)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getName());
            ps.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
        }

        return user;

}

    /**
     * Deletes a user from the database.
     * @param entity The user to be deleted.
     * @return The deleted user, or null if an error occurred or the user does not exist.
     * @throws SQLException If an error occurs while executing the operation in the database.
     */
    @Override
    public User delete(User entity) throws SQLException {
        if (entity == null || entity.getUsername() == null) return entity;
        try (PreparedStatement pst = ConnectionMariaDB.getConnection().prepareStatement(DELETE)) {
            pst.setString(1, entity.getUsername());
            pst.executeUpdate();
        }
        return entity;
    }

    @Override
    public User findById(int key) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    /**
     * Finds a user by their username in the database.
     * @param username The username of the user to find.
     * @return The found user, or null if not found.
     * @throws SQLException If an error occurs while executing the operation in the database.
     */
    @Override
    public User findByUserName(String username) throws SQLException {
            User result = null;
            if (username != null) {
                try (PreparedStatement pst = ConnectionMariaDB.getConnection().prepareStatement(FINDBYUSERNAME)) {
                    pst.setString(1, username);
                    ResultSet res = pst.executeQuery();
                    if (res.next()) {
                        result = new User();
                        result.setUsername(res.getString("username"));
                        result.setName(res.getString("name"));
                    }
                    res.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return result;
        }


    /**
     * Verifies the login credentials of a user.
     * @param email The user's email.
     * @param password The user's password.
     * @return The username if the credentials are valid, or null if not.
     * @throws SQLException If an error occurs while executing the operation in the database.
     */
    @Override
    public String checkLogin(String email, String password) throws SQLException {
    try (PreparedStatement pst = ConnectionMariaDB.getConnection().prepareStatement(QUERY)) {
        pst.setString(1, email);
        pst.setString(2, password);
        ResultSet res = pst.executeQuery();
        if (res.next()) {
            return res.getString("userName");
        }
    }
    return null;
    }

    /**
     * Gets all users stored in the database.
     * @return A list of all users stored in the database.
     */
    @Override
    public void close() throws IOException {

    }
}
