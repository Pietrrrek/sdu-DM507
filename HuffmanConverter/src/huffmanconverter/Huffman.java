package huffmanconverter;

import huffmanconverter.PriorityQueue.Element;
import huffmanconverter.PriorityQueue.PQHeap;

/**
 * 
 * @author me
 */
public class Huffman {
    public static class TreeNode {
        public Object left;
        public Object right;
        public TreeNode(Object left, Object right) {
            this.left = left;
            this.right = right;
        }
    }
    
    /**
     * Turns a byte table into a Huffman tree
     * @param byteTable An int[] with each value being the # of occurrences of the byte
     * @return A TreeNode which represents the whole Huffman tree
     */
    public static TreeNode generateTree(int[] byteTable) {
        PQHeap queue = new PQHeap(byteTable.length);
        // add each byte we've seen at least once to the queue
        for (int byt = 0; byt < byteTable.length; ++byt) {
            if (byteTable[byt] != 0) {
                // the byte is ordered according to how often it was seen
                queue.insert(new Element(byteTable[byt], byt));
            }
        }
        
        // abort early if we only have a single/no byte
        if (queue.size() < 2) {
            return new TreeNode(queue.extractMin(), null);
        }
        
        // otherwise turn the queue into a single tree
        // we do this by repeatedly grabbing the two smallest subtrees
        //   and joining them into one, then re-inserting that into queue
        while (queue.size() > 1) {
            Element left = queue.extractMin();
            Element right = queue.extractMin();
            TreeNode node = new TreeNode(left.data, right.data);
            queue.insert(new Element(left.key + right.key, node));
        }
        
        // we know the lone elm is a TreeNode
        // the only exception is if queue.size() was never > 1
        // which was handled at our early abort
        return (TreeNode) queue.extractMin().data;
    }
    
    /**
     * Converts a Huffman tree to a String[256] of keywords
     * @param tree The Huffman tree
     * @return A String[256], with each value being the bit sequence used to refer to the byte
     */
    public static String[] toKeywords(TreeNode tree) {
        String[] keywords = new String[256];
        _toKeywordsHelper(tree, keywords, "");
        return keywords;
    }
    
    /** Calculates keyword in the subtree and inserts them into the String[] */
    private static void _toKeywordsHelper(
            Object node, String[] table, String curKeyword) {
        if (node instanceof TreeNode) {
            TreeNode subtree = (TreeNode) node;
            _toKeywordsHelper(subtree.left, table, curKeyword+"0");
            _toKeywordsHelper(subtree.right, table, curKeyword+"1");
        } else if (node instanceof Integer) {
            table[(Integer) node] = curKeyword;
        } else {
            System.err.println("[Err] Unknown node when converting Huffman -> keywords: "+node);
        }
    }
}
