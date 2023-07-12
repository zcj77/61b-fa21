import java.awt.color.ICC_ProfileRGB;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 * @author
 */
public class BSTStringSet implements StringSet, Iterable<String> {
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    private Node get(String s) {
        if (_root == null) {
            return null;
        }
        Node temp = _root;
        while (true) {
            int a = s.compareTo(temp.s);
            Node nxt;
            if (a < 0) {
                nxt = temp.left;
            } else if (a > 0) {
                nxt = temp.right;
            } else {
                return temp;
            }
            if (nxt == null) {
                return temp;
            } else {
                temp = nxt;
            }
        }

    }

    @Override
    public void put(String s) {
        Node temp = get(s);
        if (_root == null) {
            _root = new Node(s);
        } else {
            int a = s.compareTo(temp.s);
            if (a < 0) {
                temp.left = new Node(s);
            } else if (a > 0) {
                temp.right = new Node(s);
            }
        }
    }

    @Override
    public boolean contains(String s) {
        Node temp = get(s);
        return temp != null && temp.s.equals(s);
    }

    @Override
    public List<String> asList() {
        ArrayList<String> res = new ArrayList<>();
        for (String s: this) {
            res.add(s);
        }
        return res;
    }


    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
    }

    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

    // FIXME: UNCOMMENT THE NEXT LINE FOR PART B
    // @Override
    public Iterator<String> iterator(String low, String high) {
        return null;  // FIXME: PART B (OPTIONAL)
    }


    /** Root node of the tree. */
    private Node _root;
}
