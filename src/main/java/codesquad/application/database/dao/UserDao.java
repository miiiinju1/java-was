package codesquad.application.database.dao;

import codesquad.application.database.Database;
import codesquad.application.database.DatabaseConfig;
import codesquad.application.repository.vo.UserVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao implements Database<UserVO> {

    private final DatabaseConfig databaseConfig;

    public UserDao(
            DatabaseConfig databaseConfig
    ) {
        this.databaseConfig = databaseConfig;
    }

    @Override
    public long save(
            final UserVO user
    ) {
        validateUserVo(user);
        String sql = "INSERT INTO users (username, password, name, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.username());
            pstmt.setString(2, user.password());
            pstmt.setString(3, user.name());
            pstmt.setString(4, user.email());
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    public Optional<UserVO> findByUsername(
            final String username
    ) {
        validateUsername(username);
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new UserVO(
                            rs.getLong("user_id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserVO> findById(
            final long id
    ) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new UserVO(
                            rs.getLong("user_id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<UserVO> findAll() {
        String sql = "SELECT * FROM users";
        List<UserVO> users = new ArrayList<>();
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                users.add(new UserVO(
                        rs.getLong("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public void update(
            final long userId,
            final UserVO user
    ) {
        validateUserVo(user);
        String sql = "UPDATE users SET username = ?, password = ?, email = ?, name = ? WHERE user_id = ?";
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.username());
            pstmt.setString(2, user.password());
            pstmt.setString(3, user.email());
            pstmt.setString(4, user.name());
            pstmt.setLong(5, userId);
            pstmt.executeUpdate();
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("존재하지 않는 User입니다.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(
            final long userId
    ) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("존재하지 않는 User입니다.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void validateUserVo(
            final UserVO userVO
    ) {
        if(userVO == null) {
            throw new IllegalArgumentException("UserVo는 null일 수 없습니다.");
        }
    }

    private void validateUsername(
            final String username
    ) {
        if(username == null || username.isEmpty()) {
            throw new IllegalArgumentException("username은 null이거나 빈 문자열일 수 없습니다.");
        }
    }
}
