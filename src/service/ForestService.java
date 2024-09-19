package service;

import model.Forest;
import repository.ForestRepository;

import java.util.List;

public class ForestService {

    private ForestRepository forestRepository;
    private TreeService treeService;

    public ForestService() {
        this.forestRepository = new ForestRepository();
        this.treeService = new TreeService();
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
                forest -> forest.setTrees(treeService.findTreesByForestId(forest))
        );
        return forests;
    }
}
