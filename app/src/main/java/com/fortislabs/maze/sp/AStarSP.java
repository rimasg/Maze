package com.fortislabs.maze.sp;

import android.util.Log;

import com.fortislabs.maze.MazeGraph;
import com.fortislabs.maze.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Okis on 2015.05.08.
 */
public class AStarSP {
    private MazeGraph grid;
    private Node startNode;
    private Node targetNode;

    public AStarSP(MazeGraph graph, Node startNode, Node targetNode) {
        this.grid = graph;
        this.startNode = startNode;
        this.targetNode = targetNode;
    }

    public void getSP() {
        getSP(startNode, targetNode);
    }

    public void getSP(Node startNode, Node targetNode) {
        this.startNode.gCost = 0;
        this.startNode.hCost = getHeuristic(startNode, targetNode);
        this.startNode.obstacle = false;
        this.startNode.visited = false;
        this.targetNode.obstacle = false;
        this.targetNode.visited = false;

        NodeComparator nodeComparator = new NodeComparator();

        List<Node> openNodeList = new ArrayList<Node>();
        List<Node> closedNodeList = new ArrayList<Node>();

        Node currentNode, nextNode;
        openNodeList.add(startNode);
        while (!openNodeList.isEmpty()) {
            Collections.sort(openNodeList, nodeComparator);
            currentNode = openNodeList.get(0);
            if (currentNode.equals(targetNode)) {
                Log.d(Constants.TAG, "Path found");
                grid.resetVisitedNodesToObstacles();
                while (!currentNode.equals(startNode)) {
                    grid.getNodesGrid()[currentNode.x][currentNode.y].visited = true;
                    currentNode = currentNode.parentNode;
                }
                break;
            }
            openNodeList.remove(currentNode);
            closedNodeList.add(currentNode);

            // Check neighbours
            nextNode = grid.getNodesGrid()[currentNode.x][currentNode.y + 1];
            if (!nextNode.visited && !nextNode.obstacle) {
                checkNeighbours(currentNode, nextNode, targetNode, openNodeList);
            }
            nextNode = grid.getNodesGrid()[currentNode.x + 1][currentNode.y];
            if (!nextNode.visited && !nextNode.obstacle) {
                checkNeighbours(currentNode, nextNode, targetNode, openNodeList);
            }
            nextNode = grid.getNodesGrid()[currentNode.x][currentNode.y - 1];
            if (!nextNode.visited && !nextNode.obstacle) {
                checkNeighbours(currentNode, nextNode, targetNode, openNodeList);
            }
            nextNode = grid.getNodesGrid()[currentNode.x - 1][currentNode.y];
            if (!nextNode.visited && !nextNode.obstacle) {
                checkNeighbours(currentNode, nextNode, targetNode, openNodeList);
            }
            //
        }
    }

    private void checkNeighbours(Node currentNode, Node nextNode, Node targetNode, List<Node> openNodeList) {
        nextNode.gCost = currentNode.gCost + 1;
        nextNode.hCost = getHeuristic(nextNode, targetNode);
        nextNode.parentNode = currentNode;
        nextNode.visited = true;
        openNodeList.add(nextNode);
    }

    private int getHeuristic(Node startNode, Node targetNode) {
        return Math.abs(targetNode.x - startNode.x) + Math.abs(targetNode.y - startNode.y);
    }
}
