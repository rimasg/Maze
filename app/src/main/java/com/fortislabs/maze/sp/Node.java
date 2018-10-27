package com.fortislabs.maze.sp;

/**
 * Created by Okis on 2015.05.08.
 */
public class Node {
    public Node parentNode;
    public NodeType type;
    public int x, y;
    public int gCost;
    public int hCost;
    public boolean obstacle = true;
    public boolean visited = false;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Node) {
            Node node = (Node) o;
            if ((this.x == node.x) && (this.y == node.y)) {
                return true;
            }
        }
        return false;
    }

    public int getFCost() {
        return gCost + hCost;
    }

}
