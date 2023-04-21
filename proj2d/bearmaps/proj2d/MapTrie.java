package bearmaps.proj2d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MapTrie {
    private ArrayList<String> names;
    private class Node {
        private boolean key;
        private int mult;
        private HashMap<String, Node> child;
        private Node(boolean isKey) {
            key = isKey;
            child = new HashMap<>();
            if (isKey == true) {
                mult += 1;
            } else {
                mult = 0;
            }
        }
    }
    private Node root;

    int size = 0;
    public int size() {
        return size;
    }
    public MapTrie(List<String> listOfName) {
        names = new ArrayList<>();
        root = new Node(false);
        for (String names: listOfName) {
            size += 1;
            insert(names);
        }
    }

    private void insert(String placeName) {
        Node ptr = root;
        int lenName = placeName.length();

        for (int i = 0; i < lenName; i++) {
            String elem = Character.toString(placeName.charAt(i));
            if (ptr.child.containsKey(elem)) {
                if (i + 1 == lenName) {
                    if (ptr.child.get(elem).key) {
                        ptr.child.get(elem).mult += 1;
                    } else {
                        ptr.child.get(elem).key = true;
                        ptr.child.get(elem).mult = 1;
                    }
                }
                ptr = ptr.child.get(elem);
            } else {
                if (i + 1 == lenName) {
                    ptr.child.put(elem, new Node(true));
                } else {
                    ptr.child.put(elem, new Node(false));
                }
                ptr = ptr.child.get(elem);
            }
        }
    }

    public List<String> searchByPrefix(String placeName) {
        names = new ArrayList<>();
        Node ptr = root;
        int lenName = placeName.length();
        String totalName = "";
        for (int i = 0; i < lenName; i++) {
            String elem = Character.toString(placeName.charAt(i));
            totalName += elem;
            if (ptr.child.containsKey(elem)) {
                if (i + 1 == lenName && ptr.child.get(elem).key) {
                    for (int m = 0; m < ptr.child.get(elem).mult; m++) {
                        names.add(totalName);
                    }
                }
                if (i + 1 == lenName) {
                    returnAllNames(totalName, ptr.child.get(elem));
                    break;
                }
                ptr = ptr.child.get(elem);
            }
        }
        return names;
    }

    private void returnAllNames(String cur, Node curPtr) {

        for (String c: curPtr.child.keySet()) {
            if (curPtr.child.get(c).key) {
                for (int i = 0; i < curPtr.child.get(c).mult; i++) {
                    names.add(cur + c);
                }
            }
            returnAllNames(cur + c, curPtr.child.get(c));
        }

    }

    public static void main(String[] ages) {
        List<String> ss = new ArrayList<>();
        ss.add("dap");
        ss.add("darden");
        ss.add("dlean");
        ss.add("dchool");
        ss.add("rdee");
        MapTrie mm = new MapTrie(ss);
        List<String> fd= mm.searchByPrefix("d");
        for (String s: fd) {
            System.out.print(s + " ");
        }
        return;
    }


}
