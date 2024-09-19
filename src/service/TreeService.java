package service;

import model.Tree;
import repository.TreeRepository;

import java.util.List;

public class TreeService {
    private TreeRepository treeRepository;

    public TreeService() {
        this.treeRepository = new TreeRepository();
    }
    public Boolean save(Tree tree) {
        return treeRepository.save(tree).isPresent();
    }

    public Boolean saveAll(List<Tree> trees) {
        for (Tree tree : trees) {
            if (treeRepository.save(tree).isEmpty())
                return false;
        }
        return true;
    }
}
