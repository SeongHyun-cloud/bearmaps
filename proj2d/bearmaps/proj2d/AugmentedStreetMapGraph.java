package bearmaps.proj2d;

import bearmaps.proj2ab.KDTree;
import bearmaps.proj2ab.Point;
import bearmaps.proj2c.streetmap.StreetMapGraph;
import bearmaps.proj2c.streetmap.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {
    private final int size = 27320;
    private HashMap<Point, Node> pointMap;
    private KDTree kdtree;
    private List<Point> points;
    private HashMap<String, List<Node>> nameMap;
    private MapTrie trie;
    private List<String> names;
    private HashMap<String, List<Node>> locMap;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        // You might find it helpful to uncomment the line below:
         List<Node> nodes = this.getNodes();
        names = new LinkedList<>();
        // ---------------------

        pointMap = new HashMap<>(size);
        points = new ArrayList<>(size);
        nameMap = new HashMap<>(size);
        locMap = new HashMap<>(size);
        for (Node node: nodes) {
            String name = node.name();
            double lat = node.lat();
            double lon = node.lon();
            long id = node.id();
            Point cur = new Point(lon, lat);
            pointMap.put(cur, node);
            if (neighbors(id).size() > 0) {
                points.add(cur);
            }
            if (name != null) {
                String cleanName = cleanString(name);
                if (locMap.containsKey(cleanName)) {
                    List<Node> n = locMap.get(cleanName);
                    n.add(node);
                } else {
                    List<Node> n = new LinkedList<>();
                    n.add(node);
                    locMap.put(cleanName, n);
                }
                if (nameMap.containsKey(cleanName)) {
                    List<Node> n = nameMap.get(cleanName);
                    n.add(node);
                } else {
                    List<Node> n = new LinkedList<>();
                    n.add(node);
                    nameMap.put(cleanName, n);
                }
                names.add(cleanName);
            }

        }
        kdtree = new KDTree(points);
        trie = new MapTrie(names);
        return;
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        return pointMap.get(kdtree.nearest(lon, lat)).id();
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        List<String> result = new LinkedList<>();
        List<String> trieResult = trie.searchByPrefix(cleanString(prefix));
        for (String name: trieResult) {
            if (nameMap.containsKey(name)) {
                for (int i = 0; i < nameMap.get(name).size(); i++) {
                    result.add(nameMap.get(name).get(i).name());
                }
            }
        }
        return result;
    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        List<Map<String, Object>> lst = new LinkedList<>();
        List<Node> lstOfNode = locMap.get(cleanString(locationName));
        for (int i = 0; i < lstOfNode.size(); i++) {
            long id = lstOfNode.get(i).id();
            double lat = lstOfNode.get(i).lat();
            double lon = lstOfNode.get(i).lon();
            String name = lstOfNode.get(i).name();
            Map<String, Object> result = new HashMap<>();
            result.put("lat", lat);
            result.put("lon", lon);
            result.put("name", name);
            result.put("id", id);
            lst.add(result);
        }
        return lst;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }






}
