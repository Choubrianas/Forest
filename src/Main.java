import model.Forest;
import model.Tree;
import util.FactoryUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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

        List<Forest> forests = FactoryUtil.GenerateObjects(Forest.class,2);
        forests.forEach(forest -> forest.setName(forestNames.get(ThreadLocalRandom.current().nextInt(forestNames.size()))));
        forests.forEach(System.out::println);

        System.out.println("------------------------------------------");

        List<Tree> trees = FactoryUtil.GenerateObjects(Tree.class,20);

        trees.forEach(tree -> tree.setType(TreeTypes.get(ThreadLocalRandom.current().nextInt(TreeTypes.size()))));
        trees.forEach(System.out::println);



        // Call services here !

        // save forests and trees to DB

        // get List of forests From DB each with associated trees

        // Filter forests by number of trees

        // For each forest extract the name of each tree and how many samples exists in the forest

        // Print the most dominant Tree (All forests combined)




    }
}