package repository;

import model.Forest;
import util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ForestRepository {
    public Optional<Forest> save(Forest forest) {
        String query = "INSERT INTO forest (id, name) VALUES (?, ?)";
        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, forest.getId());
            stmt.setString(2, forest.getName());
            if(stmt.executeUpdate() > 0) {
                return Optional.of(forest);
            }
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Forest> findById(String id) {
        String query = "SELECT * FROM forest WHERE id = ?";
        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Forest forest = new Forest();
                forest.setId(rs.getString("id"));
                forest.setName(rs.getString("name"));
                return Optional.of(forest);
            }
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Forest> findAll() {
        List<Forest> forests = new ArrayList<>();

        String query = "SELECT * FROM forest";
        try {
            Connection connection = DatabaseUtil.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Forest forest = new Forest();
                forest.setId(rs.getString("id"));
                forest.setName(rs.getString("name"));
//                forest.setTrees(new TreeRepository().findByForestId(forest.getId()));
                forests.add(forest);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return forests;
    }
}
