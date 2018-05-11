package huffmanconverter.PriorityQueue;

/**
 * Class provided in project description
 * Modified to include .toString()
 * @author Rolf Fagerberg
 */
public class Element {
    public int key;
    public Object data;
    public Element(int i, Object o) {
        this.key = i;
        this.data = o;
    }
    
    public String toString() {
        return "<"+this.key+":"+this.data+">";
    }
}
