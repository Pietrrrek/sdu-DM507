package huffmanconverter;

import huffmanconverter.PriorityQueue.Element;
import huffmanconverter.PriorityQueue.PQHeap;

/**
 * 
 * @author me
 */
public class Huffman {
    public static class Node {
        public Node left;
        public Node right;
        public boolean isLeaf;
        public int leafData;
        /** Initializes an internal node (not a leaf) */
        public Node(Node left, Node right) {
            this.left = left;
            this.right = right;
            this.isLeaf = false;
        }
        /** Initializes a leaf node that contains data */
        public Node(int leafData) {
            this.isLeaf = true;
            this.leafData = leafData;
        }
    }
    
    /**
     * Generates keywords from a frequency table
     * @param byteTable An int[] with each value being the # of occurrences of the byte
     * @return A String[] of the bit-sequences that represent each byte
     */
    public static String[] generate(int[] byteTable) {
        return toKeywords(generateTree(byteTable));
    }
    
    /**
     * Turns a byte table into a Huffman tree
     * @param byteTable An int[] with each value being the # of occurrences of the byte
     * @return A TreeNode which represents the whole Huffman tree
     */
    public static Node generateTree(int[] byteTable) {
        PQHeap queue = new PQHeap(byteTable.length);
        // add each byte we've seen at least once to the queue
        for (int byt = 0; byt < byteTable.length; ++byt) {
            if (byteTable[byt] != 0) {
                // the byte is ordered according to how often it was seen
                queue.insert(new Element(byteTable[byt], new Node(byt)));
            }
        }
        
        // turn the queue into a single tree
        // we do this by repeatedly grabbing the two smallest subtrees
        //   and joining them into one, then re-inserting that into queue
        while (queue.size() > 1) {
            Element left = queue.extractMin();
            Element right = queue.extractMin();
            Node node = new Node((Node) left.data, (Node) right.data);
            queue.insert(new Element(left.key + right.key, node));
        }
        
        // we know the lone elm is a Node as both leafs and nodes are represented as such
        return (Node) queue.extractMin().data;
    }
    
    /**
     * Converts a Huffman tree to a String[256] of keywords
     * Each node's left child represents adding a 0 to the keyword
     * Each node's right child represents adding a 1 to the keyword
     * @param tree The Huffman tree
     * @return A String[256], with each value being the bit sequence used to refer to the byte
     */
    public static String[] toKeywords(Node tree) {
        String[] keywords = new String[256];
        _toKeywordsHelper(tree, keywords, "");
        return keywords;
    }
    
    /** Calculates keyword in the subtree and inserts them into the String[] */
    private static void _toKeywordsHelper(
            Node node, String[] table, String curKeyword) {
        if (node.isLeaf) {
            table[node.leafData] = curKeyword;
        } else {
            _toKeywordsHelper(node.left, table, curKeyword+"0");
            _toKeywordsHelper(node.right, table, curKeyword+"1");
        }
    }
}
