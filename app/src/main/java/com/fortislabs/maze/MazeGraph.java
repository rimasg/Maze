package com.fortislabs.maze;

import android.util.Log;

import com.fortislabs.maze.sp.Node;
import com.fortislabs.maze.utils.Constants;

/**
 * Created by Okis on 2015.05.08.
 */
public class MazeGraph {

    private int cols, rows;
    private int recursiveCount;
    private Node[][] nodesGrid;
    private Node currentNode;
    private Node targetNode;

    public MazeGraph(int screenWidth, int screeHeight, int tileSize) {
        recursiveCount = ++recursiveCount;
        cols = (int) Math.floor(screenWidth / tileSize);
        rows = (int) Math.floor(screeHeight / tileSize);

        nodesGrid = new Node[cols][rows];

        initNodesGrid();
        setObstacles(1, 1);
        resetVisitedNodesToObstacles();
    }

    public void resetVisitedNodesToObstacles() {
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                nodesGrid[x][y].visited = nodesGrid[x][y].obstacle;
            }
        }
    }

    public void resetVisitedNodesToFalse() {
        for (int x = 1; x < cols - 1; x++) {
            for (int y = 1; y < rows - 1; y++) {
                nodesGrid[x][y].visited = false;
            }
        }
    }

    public void resetNodesValues() {
        for (int x = 1; x < cols - 1; x++) {
            for (int y = 1; y < rows - 1; y++) {
                nodesGrid[x][y].gCost = 0;
                nodesGrid[x][y].hCost = 0;
            }
        }
    }

    private void initNodesGrid() {
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                nodesGrid[x][y] = new Node(x, y);
            }
        }

        // init border cells as already visited
        for (int x = 0; x < cols; x++) {
            nodesGrid[x][0].visited = nodesGrid[x][rows - 1].visited = true;
            nodesGrid[x][0].obstacle = nodesGrid[x][rows - 1].obstacle = true;
            nodesGrid[x][0].gCost = nodesGrid[x][rows - 1].gCost = x;
        }

        for (int y = 0; y < rows; y++) {
            nodesGrid[0][y].visited = nodesGrid[cols - 1][y].visited = true;
            nodesGrid[0][y].obstacle = nodesGrid[cols - 1][y].obstacle = true;
            nodesGrid[0][y].gCost = nodesGrid[cols - 1][y].gCost = y;
        }
    }

    public void setObstacles() {
        for (int x = 1; x < cols - 1; x++) {
            for (int y = 1; y < rows - 1; y++) {
                nodesGrid[x][y].obstacle = true;
            }
        }
    }

    public void setObstacles(int x, int y) {
        nodesGrid[x][y].visited = true;

        if (recursiveCount >= cols * rows) {
            Log.d(Constants.TAG, "Max no. of setObstacles reached");
            recursiveCount = 0;
            return;
        }

        boolean north = !nodesGrid[x][y + 1].visited;
        boolean east = !nodesGrid[x + 1][y].visited;
        boolean south = !nodesGrid[x][y - 1].visited;
        boolean west = !nodesGrid[x - 1][y].visited;

        // while (north || east || south || west) {
         while (!nodesGrid[x][y + 1].visited ||
                 !nodesGrid[x + 1][y].visited ||
                 !nodesGrid[x][y - 1].visited ||
                 !nodesGrid[x - 1][y].visited) {
            while (true) {
                double r = Math.random();
                double nextNode = Math.random();
                if (r < 0.25 && !nodesGrid[x][y + 1].visited) {
                    nodesGrid[x][y + 1].obstacle = false;
                    if (nextNode < 0.33) {
                        nodesGrid[x + 1][y + 1].visited = true;
                    } else if (nextNode < 0.66) {
                        nodesGrid[x - 1][y + 1].visited = true;
                    } else if (nextNode < 1) {
                        nodesGrid[x][y + 2].visited = true;
                    }
                    setObstacles(x, y + 1);
                    break;
                } else if (r >= 0.25 && r < 0.5 && !nodesGrid[x + 1][y].visited) {
                    nodesGrid[x + 1][y].obstacle = false;
                    if (nextNode < 0.33) {
                        nodesGrid[x + 1][y + 1].visited = true;
                    } else if (nextNode < 0.66) {
                        nodesGrid[x + 1][y - 1].visited = true;
                    } else if (nextNode < 1) {
                        nodesGrid[x + 2][y].visited = true;
                    }
                    setObstacles(x + 1, y);
                    break;
                } else if (r >= 0.5 && r < 0.75 && !nodesGrid[x][y - 1].visited) {
                    nodesGrid[x][y - 1].obstacle = false;
                    if (nextNode < 0.33) {
                        nodesGrid[x + 1][y - 1].visited = true;
                    } else if (nextNode < 0.66) {
                        nodesGrid[x - 1][y - 1].visited = true;
                    } else if (nextNode < 1) {
                        nodesGrid[x][y - 2].visited = true;
                    }
                    setObstacles(x, y - 1);
                    break;
                } else if (r >= 0.75 && r < 1.0 && !nodesGrid[x - 1][y].visited) {
                    nodesGrid[x - 1][y].obstacle = false;
                    if (nextNode < 0.33) {
                        nodesGrid[x - 1][y + 1].visited = true;
                    } else if (nextNode < 0.66) {
                        nodesGrid[x - 1][y - 1].visited = true;
                    } else if (nextNode < 1) {
                        nodesGrid[x - 2][y].visited = true;
                    }
                    setObstacles(x - 1, y);
                    break;
                }
            }
        }
    }

    public Node[][] getNodesGrid() {
        return nodesGrid;
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }
}
