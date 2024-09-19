import model.Forest;
import model.Tree;
import service.ForestService;
import service.TreeService;
import util.FactoryUtil;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        List<String> forestNames = new ArrayList<>(
                Arrays.asList(
                        "Amazon Rainforest",
                        "Black Forest",
                        "Daintree Rainforest",
                        "Tongass National",
                        "Monteverde Cloud"));

        List<String> TreeTypes = new ArrayList<>(
                Arrays.asList(
                        "Oak",
                        "Redwood",
                        "Cypress",
                        "Pine",
                        "Elm",
                        "Sequoia",
                        "Fir",
                        "Banyan",
                        "Cedar"));

        List<Forest> forests = FactoryUtil.GenerateObjects(Forest.class, 2);
        forests.forEach(forest -> forest.setName(forestNames.get(ThreadLocalRandom.current().nextInt(forestNames.size()))));
        forests.forEach(System.out::println);

        System.out.println("------------------------------------------");

        List<Tree> trees = FactoryUtil.GenerateObjects(Tree.class, 20);

        trees.forEach(tree -> tree.setType(TreeTypes.get(ThreadLocalRandom.current().nextInt(TreeTypes.size()))));
        trees.forEach(System.out::println);


        // Call services here !
        ForestService forestService = new ForestService();
        TreeService treeService = new TreeService();

        // save forests and trees to DB

        Scanner scanner = new Scanner(System.in);
        System.out.print("\nDo you want to save the forests and trees to the database? (y/n) : ");
        String conf = scanner.nextLine();
        if (conf.equals("y")) {
            if (forestService.saveAll(forests)) {
                System.out.println("Forests saved successfully");
                /* Set Tree forest with random forest from forest list */
                trees.forEach(tree -> {
                    tree.setForest(forests.get(ThreadLocalRandom.current().nextInt(forests.size())));
                });
                if (treeService.saveAll(trees)) {
                    System.out.println("Trees saved successfully");
                } else {
                    System.err.println("Failed to save trees");
                }
            } else {
                System.err.println("Failed to save forests");
            }
        }

        // get List of forests From DB each with associated trees
        List<Forest> forestsFromDB = forestService.findAll();
        System.out.println("\n\nForests from DB : ");
        forestsFromDB.forEach(forest -> {
            System.out.println(forest);
            forest.getTrees().forEach(System.out::println);
        });

        // Filter forests by number of trees
        System.out.print("\nEnter the number of trees to filter by: ");
        Integer treeCount = scanner.nextInt();
        List<Forest> filteredForests = forestsFromDB
                .stream()
                .filter(forest -> forest.getTrees().size() > treeCount)
                .collect(Collectors.toList());

        System.out.println("--> Forests with more than " + treeCount + " trees :");
        filteredForests.forEach(System.out::println);

        // For each forest extract the name of each tree and how many samples exists in the forest
        System.out.println("\n\nSamples of each tree in each forest");
        System.out.println("\nForest Name -> Tree Name -> Number of Samples");
        Map<String, Map<String, Integer>> forestTreeSamples = new HashMap<>();
        forestsFromDB.forEach(forest -> {
            Map<String, Integer> treeSamples = new HashMap<>();
            forest.getTrees().forEach(tree -> {
                Integer freq = Collections.frequency(forest.getTrees(), tree);
                if (treeSamples.containsKey(tree.getType())) {
                    treeSamples.put(tree.getType(), treeSamples.getOrDefault(tree.getType(), 0) + freq);
                } else {
                    treeSamples.put(tree.getType(), freq);
                }
            });
            forestTreeSamples.put(forest.getName(), treeSamples);
        });

        forestTreeSamples.forEach((forestName, treeSamples) -> {
            System.out.println("Forest : " + forestName);
            treeSamples.forEach((treeName, samples) -> {
                System.out.println("\t" + treeName + " -> " + samples);
            });
        });

        // Print the most dominant Tree (All forests combined)
        System.out.println("\n\nMost Dominant Tree : ");
        Map<String, Integer> treeFreq = new HashMap<>();
        // for each in forestTreeSamples get each tree freq sum
        forestTreeSamples.forEach((forestName, treeSamples) -> {
            treeSamples.forEach((treeName, samples) -> {
                if (treeFreq.containsKey(treeName)) {
                    treeFreq.put(treeName, treeFreq.getOrDefault(treeName, 0) + samples);
                } else {
                    treeFreq.put(treeName, samples);
                }
            });
        });

        String mostDominantTree = Collections.max(treeFreq.entrySet(), Map.Entry.comparingByValue()).getKey();
        System.out.println(mostDominantTree + " is the most dominant tree with " + treeFreq.get(mostDominantTree) + " samples");
    }
}