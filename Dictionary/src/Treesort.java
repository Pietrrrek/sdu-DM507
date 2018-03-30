import java.util.Scanner;

/**
 *
 * @author me
 */
public class Treesort {
    public static void main(String[] args) {
        DictBinTree tree = new DictBinTree();
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextInt()) {
            int i = sc.nextInt();
            tree.insert(i);
        }
        
        for (int i : tree.orderedTraversal()) {
            System.out.println(i);
        }
    }
}
