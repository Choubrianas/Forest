package repository;

import model.Forest;
import model.Tree;
import util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TreeRepository {

    public Optional<Tree> save(Tree tree) {
        String query = "INSERT INTO Tree (id, type, forest_id) VALUES (?, ?, ?)";
        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, tree.getId());
            stmt.setString(2, tree.getType());
            stmt.setString(3, tree.getForest().getId());
            if(stmt.executeUpdate() > 0) {
                return Optional.of(tree);
            }
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Tree> findById(String id) {
        String query = "SELECT * FROM Tree WHERE id = ?";
        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Tree tree = new Tree();
                tree.setId(rs.getString("id"));
                tree.setType(rs.getString("type"));
                Optional<Forest> forestOptional = new ForestRepository().findById(rs.getString("forest_id"));
                forestOptional.ifPresentOrElse(
                        tree::setForest,
                        () ->
                            System.out.println("Forest not found")
                );

                return Optional.of(tree);
            }
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Tree> findByForestId(Forest forest) {
        List<Tree> trees = new ArrayList<>();
        String query = "SELECT * FROM Tree WHERE forest_id = ?";
        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, forest.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Tree tree = new Tree();
                tree.setId(rs.getString("id"));
                tree.setType(rs.getString("type"));
                tree.setForest(forest);
                trees.add(tree);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trees;
    }
}
