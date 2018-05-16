package huffmanconverter;

import huffmanconverter.PriorityQueue.Element;
import huffmanconverter.PriorityQueue.PQHeap;

/**
 * 
 * @author me
 */
public class Huffman {
    /**
     * A node in a tree (possibly the root)
     * If the children are null, it is a leaf and has a byte (byt)
     * If the children are not null, it is an internal node and does not have a byte (byt)
     */
    public static class Node {
        public Node left;
        public Node right;
        private int byt;
        
        /** Initializes an internal node (not a leaf) */
        public Node(Node left, Node right) {
            this.left = left;
            this.right = right;
            this.byt = -1;
        }
        
        /** Initializes a leaf node that contains data */
        public Node(int byt) {
            this.byt = byt;
        }
        
        /** Checks whether the node is a leaf node */
        public boolean isLeaf() {
            return this.left == null && this.right == null;
        }
        
        /** Gets the byte this Node represents (if it is a leaf node) */
        public int getByte() {
            return this.byt;
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
        
        // handle the case where we were given an empty file
        if (queue.size() == 0) {
            return null;
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
        if (node == null) { return; } // handle the case where we were given an empty file
        if (node.isLeaf()) {
            table[node.getByte()] = curKeyword;
        } else {
            _toKeywordsHelper(node.left, table, curKeyword+"0");
            _toKeywordsHelper(node.right, table, curKeyword+"1");
        }
    }
}
