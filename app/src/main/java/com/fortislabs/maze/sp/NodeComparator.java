package com.fortislabs.maze.sp;

import java.util.Comparator;

/**
 * Created by Okis on 2015.05.09.
 */
public class NodeComparator implements Comparator<Node> {
    @Override
    public int compare(Node lhs, Node rhs) {
        if (lhs.getFCost() < rhs.getFCost()) {
            return -1;
        }
        if (lhs.getFCost() > rhs.getFCost()) {
            return +1;
        }
        return 0;
    }
}
