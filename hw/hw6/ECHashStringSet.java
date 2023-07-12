import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** A set of String values.
 *  @author
 */
class ECHashStringSet implements StringSet {
    public LinkedList<String>[] buckets;
    private int size;

    public ECHashStringSet(){
        size = 0;
        buckets = (LinkedList<String>[]) new LinkedList[5];
        for (int i = 0; i < 5; i++) {
            buckets[i] = new LinkedList<String>();
        }
    }

    @Override
    public void put(String s) {
        size++;
        int ndx = which(s);
        if (s != null) {
            if ((double) size /(double) buckets.length >  5.0) {
                resize();
            }
            if (buckets[ndx] == null) {
                buckets[ndx] = new LinkedList<>();
            }
        }
        buckets[ndx].add(s);
    }

    @Override
    public boolean contains(String s) {
        return buckets[which(s)].contains(s) && s != null;
    }

    @Override
    public List<String> asList() {
        ArrayList<String> res = new ArrayList<>();
        for (LinkedList<String> l: buckets) {
            if (l != null){
                for (String s: l) {
                    res.add(s);
                }
            }
        }
        return res;
    }

    private int which(String s) {
        return (s.hashCode() & 0x7fffffff) % buckets.length;
    }

    private void resize() {
        LinkedList<String>[] previous = buckets;
        buckets = (LinkedList<String>[]) new LinkedList[previous.length * 2];
        size = 0;

        for (LinkedList<String> l: previous) {
            if (l != null) {
                for (String s: l) {
                    this.put(s);
                }
            }
        }
    }
}