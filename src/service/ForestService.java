package service;

import model.Forest;
import repository.ForestRepository;
import repository.TreeRepository;

import java.util.List;

public class ForestService {

    private ForestRepository forestRepository;
    private TreeRepository treeRepository;

    public ForestService() {
        this.forestRepository = new ForestRepository();
        this.treeRepository = new TreeRepository();
    }
    public Boolean saveAll(List<Forest> forests) {
        for (Forest forest : forests) {
            if (forestRepository.save(forest).isEmpty())
                return false;
        }
        return true;
    }

    public List<Forest> findAll() {
        List<Forest> forests = forestRepository.findAll();
        forests.forEach(
                forest -> forest.setTrees(treeRepository.findByForestId(forest))
        );
        return forests;
    }
}
