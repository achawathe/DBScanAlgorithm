/**
 * KDTree with nested KDNode class
 *
 * CSI2110 Data Structures and Algorithms
 * www.uottawa.ca
 *
 * @author Akhil Chawathe
 *
 * 300237603
 *
 * 2022
 *
 */

public class KDtree {
    private static final int DIM = 3;
    public int currAxis = 0;

    public class KDnode{
        public Point3D point;
        public int axis;
        public double value;
        public KDnode left;
        public KDnode right;

        /**
         * Constructor for KDNode Class
         *
         * @param pt
         * @param axis
         */
        public KDnode(Point3D pt, int axis) {
            this.point= pt;
            this.axis= axis;
            this.value= pt.get(axis);
            left = null;
            right = null;
        }
    }
    private KDnode root;
    // construct empty tree
    public KDtree() {
        root= null;
    }

    /**
     * Getter for root of tree.
     *
     * @return root
     */
    public KDnode getRoot() {
        return root;
    }

    /**
     * Creates initial insertions for the KDtree.
     *
     * @param p
     */
    public void add(Point3D p){
        insert(p, root, 0);
    }

    /**
     * Recursive insertion algorithm for KDTree
     *
     * @param P
     * @param node
     * @param axis
     * @return node
     */
    public KDnode insert (Point3D P, KDnode node, int axis){
        if (root==null){
            root = new KDnode(P, 0); //If Tree is Empty, create root
        } else if (node==null) {
            node = new KDnode(P, axis); //If There is no node, but there is a point, create a new node.
        } else if (P.get(axis) <= node.value) {
            node.left = insert(P, node.left, (axis+1) % DIM); //If Node is <= other node, insert left.
        }else{
            node.right = insert(P,node.right,(axis+1) % DIM); //If Node is > other node, insert right.
        }
        return node; // return node
    }
}
