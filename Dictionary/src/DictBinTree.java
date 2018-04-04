/**
 *
 * @author me
 */
public class DictBinTree implements Dict {
    private Node root;
    private int size = 0;
    private int i;

    /**
     * Inserts a value into the tree
     * @param k The value to insert
     */
    @Override
    public void insert(int k) {
        ++this.size;
        Node newNode = new Node(k); // the node we want to insert
        Node parentNode = null; // the leaf we should insert under
        Node loopNode = this.root;
        // keep looping until we've found the end of the tree
        while (loopNode != null) {
            parentNode = loopNode;
            if (newNode.getKey() < loopNode.getKey()) {
                loopNode = loopNode.getLeft();
            } else {
                loopNode = loopNode.getRight();
            }
        }
        // if the while loop was never ran, tree is empty
        if (parentNode == null) {
            this.root = newNode;
        // place our new node either to the left or the right of the leaf
        } else if (newNode.getKey() < parentNode.getKey()) {
            parentNode.setLeft(newNode);
        } else {
            parentNode.setRight(newNode);
        }
    }
    
    /**
     * Returns whether a value exists in the tree
     * @param k The value to search for
     * @return Whether the value exists in the tree
     */
    @Override
    public boolean search(int k) {
        return _search(k, this.root);
    }
    
    /**
     * Internal method for recursively looking for a target value
     * @param target The value to look for
     * @param x The node whose subtree we should look in
     * @return Whether the value was found
     */
    private boolean _search(int target, Node x) {
        if (x == null) { return false; } // no point searching if we don't have a tree
        int key = x.getKey();
        if (key == target) { return true; }
        // if we're looking for a lower value, we know it's to the left of x
        if (key > target) {
            return _search(target, x.getLeft());
        }
        // otherwise we know it's to the right of x (as it is != x.key)
        return _search(target, x.getRight());
    }
    
    /**
     * Returns an array of the values in the tree, in ascending order
     * @return An int[] of values in the tree
     */
    @Override
    public int[] orderedTraversal() {
        int[] output = new int[this.size];
        this.i = 0; // we use an internal counter for inserting into the array
        _orderedTraversal(this.root, output);
        return output;
    }
    
    /**
     * Internal method for creating an array of values in ascending order
     * Assumes that this.i has been set to the index at which the values should
     *   start being inserted into a
     * Also assumes that a has a size of at least this.i + #nodes in x's subtree
     * Mutates a.
     * @param x The Node whose subtree we want to insert into a
     * @param a The int[] we should insert values into
     */
    private void _orderedTraversal(Node x, int[] a) {
        if (x == null) { return; }
        // every value to the left of x should come before x
        _orderedTraversal(x.getLeft(), a);
        a[this.i++] = x.getKey();
        // every value to the right of x should come after x
        _orderedTraversal(x.getRight(), a);
    }
}
