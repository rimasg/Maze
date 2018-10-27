package com.fortislabs.maze;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.fortislabs.maze.sp.AStarSP;
import com.fortislabs.maze.sp.Node;


public class MainActivity extends Activity {

    Button generatePathBtn, generateMazeBtn;
    private LinearLayout frame;
    private MazeView mazeView;
    private MazeGraph mazeGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frame = findViewById(R.id.frame);
        mazeView = frame.findViewById(R.id.mazeView);
        // To keep the screen turned on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Init Maze in MazeView
        generatePathBtn = findViewById(R.id.btnGeneratePath);
        generatePathBtn.setOnClickListener(v -> generatePath());
        generateMazeBtn = findViewById(R.id.btnGenerateMaze);
        generateMazeBtn.setOnClickListener(v -> generateMaze(1, 1));
    }

    private void generateMaze(int x, int y) {
        mazeGraph = mazeView.getMazeGraph();
        mazeGraph.setObstacles();
        mazeGraph.resetNodesValues();
        mazeGraph.setObstacles(x, y);
        mazeGraph.resetVisitedNodesToFalse();
        mazeView.drawGrid();
        mazeView.invalidate();
    }

    private void generatePath() {
        mazeGraph = mazeView.getMazeGraph();
        Node startNode = new Node(1, 1);
        Node targetNode = new Node(mazeGraph.getCols() - 2, mazeGraph.getRows() - 2);
        AStarSP starSP = new AStarSP(mazeGraph, startNode, targetNode);
        mazeGraph.resetVisitedNodesToObstacles();
        starSP.getSP();
        mazeView.drawGrid();
        mazeView.invalidate();
        mazeGraph.resetVisitedNodesToObstacles();
    }
}
