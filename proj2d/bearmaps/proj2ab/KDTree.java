/** KDTree implementation.
 * @author SeongHyun Park
 */
package bearmaps.proj2ab;
import java.util.Iterator;
import java.util.List;

public class KDTree implements PointSet {
    private Node root;
    private int size;
    private class Node {
        private Node right;
        private Node left;
        private Point p;
        private boolean dir;
        private Node(Point p, boolean dir) {
            this.p = p;
            right = null;
            left = null;
            this.dir = dir;
        }

    }

    /** Constructor that puts Point based on the list of Point. This constructor
     * uses iterator and private put function to put Points in KDTree.
     * @param points -> contains list of Points that you are putting to KDTree.
     */
    public KDTree(List<Point> points) {

        Iterator<Point> pIter = points.listIterator();
        root = new Node(pIter.next(), true);
        size++;
        int i = 1;
        while (pIter.hasNext()) {
            if (i % 2 == 0) {
                put(root, pIter.next(), true);
            } else {
                put(root, pIter.next(), false);
            }
            i++;
            size++;
        }
    }


    /** put function that puts a point to the KDTree.
     * @param cur -> Node that you are currently in.
     * @param pt -> Point that you want to put in KDTree.
     * @param putDir -> see if the Point is y-based or x-based.
     *               if true, x based else, y based.
     */
    private void put(Node cur, Point pt, boolean putDir) {
        root = putHelper(root, pt, putDir);
    }

    /** putHelper that puts a Point based on value of the
     * Point that is being passed.
     * @param cur -> current Node.
     * @param pt -> Point you want to put in KDTree.
     * @param putDir -> direction variable determines where
     *               it's x or y based.
     * @return cur Node so that it will be equal to root Node
     *              at the end of recursive call.
     */
    private Node putHelper(Node cur, Point pt, boolean putDir) {
        if (cur == null) {
            return new Node(pt, putDir);
        }
        if (cur.dir) {
            if (Double.compare(pt.getX(), cur.p.getX()) < 0) {
                cur.left = putHelper(cur.left, pt, putDir);
            } else {
                cur.right = putHelper(cur.right, pt, putDir);
            }
        } else {
            if (Double.compare(pt.getY(), cur.p.getY()) < 0) {
                cur.left = putHelper(cur.left, pt, putDir);
            } else {
                cur.right = putHelper(cur.right, pt, putDir);
            }
        }
        return cur;
    }

    /** Nearest function that calls nearestHelper to find
     * nearest point with 'x' and 'y'.
     * @param x -> the x-coordinate of the point that you use for distance.
     * @param y -> the y-coordinate of the point that you use for distance.
     * @return nearestHelper that finds the Node that is closest to 'x' and 'y'.
     */
    @Override
    public Point nearest(double x, double y) {
        int i = 1;
        Point findP = new Point(x, y);
        return nearestHelper(root, findP, null).p;
    }

    /** helps nearest to find the closest Point with refP.
     * It keeps track of which side has closest distance with refP.
     * if there is a possible closest distance badSide,
     * checks if there is a desired Point.
     * @source I used the idea of goodSide and badSide from Professor
     *  Hug's slide.
     * @param cur -> current Node in KDTree
     * @param refP -> point that is used to calculate closest distance.
     * @param best -> best Node that have closest distance with refP.
     * @return Node that has the closest distance with refP
     */
    /*
    private Node nearestHelper(Node cur, Point refP, Node best) {
        if (cur == null) {
            return best;
        }

        if (Point.distance(refP, cur.p) < Point.distance(refP, best.p)) {
            best = cur;
        }
        Node goodSide;
        Node badSide;
        if (cur.dir) {
            if (Double.compare(refP.getX(), cur.p.getX()) < 0) {
                goodSide = cur.left;
                badSide = cur.right;
            } else {
                goodSide = cur.right;
                badSide = cur.left;
            }
        } else {
            if (Double.compare(refP.getY(), cur.p.getY()) < 0) {
                goodSide = cur.left;
                badSide = cur.right;
            } else {
                goodSide = cur.right;
                badSide = cur.left;
            }
        }
        best = nearestHelper(goodSide, refP, best);
        if (badSide != null) {
            if (cur.dir) {
                Point temp = new Point(cur.p.getX(), refP.getY());
                if (Point.distance(refP, temp) < Point.distance(refP, best.p)) {
                    best = nearestHelper(badSide, refP, best);
                }
            } else {
                Point temp = new Point(refP.getX(), cur.p.getY());
                if (Point.distance(refP, temp) < Point.distance(refP, best.p)) {
                    best = nearestHelper(badSide, refP, best);
                }
            }

        }
        return  best;

    }

     */

    private Node nearestHelper(Node cur, Point refP, Node best) {
        if (cur == null) {
            return best;
        }
        if (best == null) {
            best = cur;
        } else if (Point.distance(refP, cur.p) < Point.distance(refP, best.p)) {
            best = cur;
        }
        Node goodSide;
        Node badSide;
        if (cur.dir) {
            if (Double.compare(refP.getX(), cur.p.getX()) < 0) {
                goodSide = cur.left;
                badSide = cur.right;
            } else {
                goodSide = cur.right;
                badSide = cur.left;
            }
        } else {
            if (Double.compare(refP.getY(), cur.p.getY()) < 0) {
                goodSide = cur.left;
                badSide = cur.right;
            } else {
                goodSide = cur.right;
                badSide = cur.left;
            }
        }
        best = nearestHelper(goodSide, refP, best);
        if (badSide != null) {
            if (cur.dir) {
                Point temp = new Point(cur.p.getX(), refP.getY());
                if (Point.distance(refP, temp) < Point.distance(refP, best.p)) {
                    best = nearestHelper(badSide, refP, best);
                }
            } else {
                Point temp = new Point(refP.getX(), cur.p.getY());
                if (Point.distance(refP, temp) < Point.distance(refP, best.p)) {
                    best = nearestHelper(badSide, refP, best);
                }
            }
        }
        return  best;

    }
}
