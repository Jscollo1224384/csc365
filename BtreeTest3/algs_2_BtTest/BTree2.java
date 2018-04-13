public class BTree2
    {//begin bTree class
        /** The minimum degree, i.e., the minimum number of keys in any
         * node other than the root. */
        final private int t;

        /** The maximum number of keys in any node, =
         * 2<code>t</code>-1. */
        final private int maxKeys;

        /** The root of the B-tree. */
        private Node root;

        /** Private inner class for a B-tree node. */
        private class Node
        {//begin node class
            /** The number of keys stored in the node. */
            public int n;

            /** Array of the keys stored in the node. */
            public DynamicSetElement[] key;

            /** Pointers to the children, if this node is not a leaf.  If
             * this node is a leaf, then <code>null</code>. */
            public Node[] c;

            /** <code>true</code> if this node is a leaf,
             * <code>false</code> if this node is an interior node. */
            public boolean leaf;

            /**
             * Initializes the instance variables, including allocating
             * the <code>c</code> array if this node is not a leaf.
             *
             * @param n How many of keys are to be stored in this node.
             * @param leaf <code>true</code> if this node is a leaf,
             * <code>false</code> if this node is an interior node.
             */
            public Node(int n, boolean leaf)
            {
                this.n = n;
                this.leaf = leaf;
                key = new DynamicSetElement[maxKeys];
                if (leaf)
                    c = null;
                else
                    c = new Node[maxKeys+1];
            }

            /** Reads a disk block.  Does nothing in this implementation.
             * You should customize it for your system. */
            private void diskRead()
            {
            }

            /** Writes a disk block.  Does nothing in this implementation.
             * You should customize it for your system. */
            public void diskWrite()
            {
            }

            /** Frees this node.  Does nothing in this implementation.
             * You should customize it for your system. */
            private void free()
            {
            }

            /**
             * Searches for a dynamic set element with a given key,
             * starting at this node.
             *
             * @param k The key.
             * @return A handle to the object found, or <code>null</code>
             * if there is no match.
             */
            public BTreeHandle BTreeSearch(Comparable k)
            {
                int i = 0;

                while (i < n && key[i].compareTo(k) < 0)
                    i++;

                if (i < n && key[i].compareTo(k) == 0)
                    return new BTreeHandle(this, i); // found it

                if (leaf)
                    return null;	// no child to search
                else {		// search child i
                    c[i].diskRead();
                    return c[i].BTreeSearch(k);
                }
            }

            /**
             * Splits this node, which is a full child of its parent,
             * which is in turn a nonfull internal node.  We assume that
             * both this node and its parent are already in main memory.
             * This method splits this node in two and adjusts the parent
             * so that it has an additional child.
             *
             * @param x This node's parent.
             * @param i This node is child <code>c[i]</code> of
             * <code>x</code>.
             */
            public void BTreeSplitChild(Node x, int i)
            {
                Node z = new Node(t-1, leaf);

                // Copy the t-1 keys in positions t to 2t-2 into z.
                for (int j = 0; j < t-1; j++) {
                    z.key[j] = key[j+t];
                    key[j+t] = null; // remove the reference
                }

                // If this node is not a leaf, copy the t children in
                // positions t to 2t-1, too.
                if (!leaf)
                    for (int j = 0; j < t; j++) {
                        z.c[j] = c[j+t];
                        c[j+t] = null; // remove the reference
                    }

                n = t-1;

                // Move the children in x that are to the right of y by
                // one position to the right.
                for (int j = x.n; j >= i+1; j--)
                    x.c[j+1] = x.c[j];

                // Drop z into x's child i+1.
                x.c[i+1] = z;

                // Move the keys in x that are to the right of y by one
                // position to the right.
                for (int j = x.n-1; j >= i; j--)
                    x.key[j+1] = x.key[j];

                // Move this node's median key into x, and remove the
                // reference to the key in this node.
                x.key[i] = key[t-1];
                key[t-1] = null;

                x.n++;		// one more key/child in x

                // All done.  Write out the nodes.
                diskWrite();
                z.diskWrite();
                x.diskWrite();
            }

            /**
             * Inserts a new element in this node, which is assumed to be
             * nonfull.
             *
             * @param k The new element to be inserted.
             */
            public BTreeHandle BTreeInsertNonfull(DynamicSetElement k)
            {
                int i = n-1;
                Comparable kKey = k.getKey();

                if (leaf) {
                    // Move all keys greater than k's by one position to
                    // the right.
                    while (i >= 0 && key[i].compareTo(kKey) > 0) {
                        key[i+1] = key[i];
                        i--;
                    }

                    // Either i is -1 or key[i] is the rightmost key <=
                    // k's key.  In either case, drop k into position i+1.
                    key[i+1] = k;
                    n++;
                    diskWrite();

                    // Return the handle saying where we dropped k.
                    return new BTreeHandle(this, i+1);
                }
                else {
                    // Find which child we descend into.
                    while (i >= 0 && key[i].compareTo(kKey) > 0)
                        i--;

                    // Either i is -1 or key[i] is the rightmost key <=
                    // k's key.  In either case, descend into position
                    // i+1.
                    i++;
                    c[i].diskRead();
                    if (c[i].n == maxKeys) {
                        // That child is full, so split it, and possibly
                        // update i to descend into the new child.
                        c[i].BTreeSplitChild(this, i);
                        if (key[i].compareTo(kKey) < 0)
                            i++;
                    }

                    return c[i].BTreeInsertNonfull(k);
                }
            }


        }

        /** Class to define a handle returned by searches.  This class is
         * opaque, in that outside of the <code>BTree</code> class,
         * <code>BTreeHandle</code> objects cannot be examined. */
        private static class BTreeHandle
        {
            /** A node in the B-tree. */
            Node node;

            /** Index of the key in the node. */
            int i;

            /**
             * Saves the node and index in the instance variables.
             *
             * @param node The node.
             * @param i The index of the key in the node.
             */
            public BTreeHandle(Node node, int i)
            {
                this.node = node;
                this.i = i;
            }
        }

        /**
         * Creates an empty B-tree.  Emulates B-Tree-Create on page 442.
         *
         * @param t The minimum degree of this B-tree.
         */
        public BTree2(int t)
        {
            this.t = t;
            maxKeys = 2 * t - 1;
            root = new Node(0, true); // root is a leaf
            root.diskWrite();	  // write the root to disk
        }

        /**
         * Searches for an element with a given key.
         *
         * @param k The key being searched for.
         * @return A handle to the object found, or <code>null</code> if
         * there is no match.
         */
        public Object search(Comparable k)
        {
            return root.BTreeSearch(k);
        }

        /**
         * Inserts an element.
         *
         * @param o The element to insert.
         * @return A handle to the new element.
         * @throws ClassCastException if <code>o</code> does not reference
         * a {@link DynamicSetElement}.
         */
        public Object insert(Comparable o)
        {
            Node r = root;
            DynamicSetElement e = DynamicSetElement.Helper.cast(o);

            if (r.n == maxKeys)
            {
                // Split the root.
                Node s = new Node(0, false);
                root = s;
                s.c[0] = r;
                r.BTreeSplitChild(s, 0);
                return s.BTreeInsertNonfull(e);
            }
            else
                return r.BTreeInsertNonfull(e);
        }

    }

// $Id: BTree.java,v 1.1 2003/10/14 16:56:20 thc Exp $
// $Log: BTree.java,v $
// Revision 1.1  2003/10/14 16:56:20  thc
// Initial revision.
//



