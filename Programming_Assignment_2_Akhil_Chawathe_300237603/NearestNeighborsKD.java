import java.util.ArrayList;
import java.util.List;
/**
 * Completed Experiment 2
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
public class NearestNeighborsKD {

    public KDtree kdtree;
    List<Point3D> neighbors;

    /**
     * NearestNeighborsKD Constructor
     *
     * @param list
     */
    NearestNeighborsKD(List<Point3D> list) {
       kdtree = new KDtree();
       neighbors = new ArrayList<Point3D>();
        for(Point3D p: list){
            kdtree.add(p);
        }
    }

    /**
     * Overloaded Method for rangeQuery
     *
     * @param p
     * @param eps
     * @return neighbors
     */
    public List<Point3D> rangeQuery(Point3D p, double eps){
        neighbors = new ArrayList<Point3D>();
        rangeQuery(p,eps, neighbors, kdtree.getRoot());
        return neighbors;
    }

    /**
     * Recursive use of rangeQuery using KDtree
     *
     * @param p
     * @param eps
     * @param neighbors
     * @param node
     */
    private void rangeQuery(Point3D p, double eps, List<Point3D> neighbors, KDtree.KDnode node){
        if(node == null){ // Check if null
            return;
        }
        if(p.distance(node.point) < eps) // Check if distance < eps, add to point.
            neighbors.add(node.point);

        if(p.get(node.axis) - eps <= node.value) // check if axis value - eps <= node value, check left.
            rangeQuery(p,eps,neighbors,node.left);

        if(p.get(node.axis) + eps > node.value) // check if axis value + eps > node value, check right.
            rangeQuery(p,eps,neighbors,node.right);

        return;
    }
}
