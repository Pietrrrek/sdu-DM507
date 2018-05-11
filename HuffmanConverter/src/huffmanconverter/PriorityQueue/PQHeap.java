package huffmanconverter.PriorityQueue;

/**
 * An implementation of PQ using a heap data structure
 * @author Johan Fagerberg
 */
public class PQHeap implements PQ {
    private final Element[] heap;
    private int numElms = 0;
    public PQHeap(int maxElms) {
        this.heap = new Element[maxElms];
    }
    
    /** @return The index of a nodes parent, given the index of the node */
    private int getParent(int i) { return (int) Math.floor((i - 1) / 2); }
    /** @return The index of a nodes left child, given the index of the node */
    private int getLeftChild(int i) { return 2*i + 1; }
    /** @return The index of a nodes right child, given the index of the node */
    private int getRightChild(int i) { return 2*i + 2; }
    
    /**
     * Restores heap order in the subtree of a specific node
     * @param i The index of the node to restore heap order under
     */
    private void heapify(int i) {
        int leftIndex = getLeftChild(i);
        int rightIndex = getRightChild(i);
        // figure out which of the three nodes have the smallest key
        int minIndex = i;
        if (leftIndex < this.numElms
                && this.heap[leftIndex].key < this.heap[minIndex].key) {
            minIndex = leftIndex;
        }
        if (rightIndex < this.numElms
                && this.heap[rightIndex].key < this.heap[minIndex].key) {
            minIndex = rightIndex;
        }
        // if it's not the node currently on top, move the smallest-keyed elm 
        //   to top to create heap order. This is done by swapping the current
        //   top elm with the elm with smallest key
        if (minIndex != i) {
            Element elm = this.heap[i];
            Element minElm = this.heap[minIndex];
            this.heap[i] = minElm;
            this.heap[minIndex] = elm;
            // finally restore heap order in the child node we moved
            // we know the other child is in heap order since it's >= new parent
            //   and was in heap order when we started
            heapify(minIndex);
        }
    }

    /**
     * Gets the element with the smallest key
     * Also removes said element from the queue
     * @return The element in the queue with the smallest key
     */
    @Override
    public Element extractMin() {
        if (this.numElms == 0) { // check not required by project description
            throw new ArrayIndexOutOfBoundsException("Priority queue is empty");
        }
        
        // grab the smallest-keyed element
        Element min = this.heap[0];
        // then make sure our heap doesn't have holes
        this.heap[0] = this.heap[this.numElms - 1];
        this.heap[this.numElms - 1] = null;
        --this.numElms;
        // then restore heap order
        this.heapify(0);
        return min;
    }
    
    /**
     * Inserts an element into our heap
     * @param elm The element to insert
     */
    @Override
    public void insert(Element elm) {
        // insert the element at the end of our heap
        int i = this.numElms;
        this.heap[i] = elm;
        // then run from our new elm and up through its parents, restoring order
        while (i > 0) {
            int parentI = getParent(i);
            Element parent = this.heap[parentI];
            // if we found a parent < elm, we know all further parents < elm
            if (parent.key < elm.key) { break; }
            // if parent > elm, swap parent and elm
            // we know this creates heap order since our elm subtree had
            //   heap order, and the parent node subtree had heap order except
            //   for the new elm
            this.heap[i] = parent;
            this.heap[parentI] = elm;
            i = parentI;
        }
        ++this.numElms;
    }
    
    public int size() {
        return this.numElms;
    }
    
    /** @return A simple tree-like representation of the heap */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        int layer = 0;
        int numLayers = (int) (Math.log(this.numElms) / Math.log(2));
        while (layer <= numLayers) {
            int end = i + (int) Math.pow(2, layer);
            while (i < end) {
                sb.append(this.heap[i]).append(" ");
                ++i;
            }
            sb.append("\n");
            ++layer;
        }
        return sb.toString();
    }
}
