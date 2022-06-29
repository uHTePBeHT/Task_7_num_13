package ru.vsu.cs.course1.graph;

import java.util.ArrayList;
import java.util.List;

public class Task {

    private int countEdge(Graph graph, int v) {
        boolean[][] arr = graph.getAdjMatrix();
        int res = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[v][i]) {
                res++;
            }
        }
        return res;
    }
    private List<List<int[]>> permutation(List<int[]> list, int limit) {
        List<List<int[]>> roadDel = new ArrayList<>();
        return permutIter(list, 0, limit, roadDel);
    }
    private List<List<int[]>> permutIter(List<int[]> arr, int index, int limit, List<List<int[]>> roadDel) {
        if (index >= limit) {
            List<int[]> line = new ArrayList<>();
            for (int i = 0; i < limit; i++) {
                line.add(arr.get(i));
            }
            roadDel.add(line);
            return roadDel;
        }
        for (int i = index; i < arr.size(); i++){
            int[] temp = arr.get(index);
            arr.set(index, arr.get(i));
            arr.set(i, temp);

            permutIter(arr, index + 1, limit, roadDel);

            temp = arr.get(index);
            arr.set(index, arr.get(i));
            arr.set(i, temp);
        }
        return roadDel;
    }

    private List<int[]> roadFind(Graph graph) {
        List<int[]> road = new ArrayList<>();
        for (int i = 0; i < graph.vertexCount(); i++) {
            for (int j = 0; j < graph.vertexCount(); j++) {
                if (graph.isAdj(i, j) && !road.contains((new int[]{i, j}))) {
                    road.add(new int[]{i, j});
                }
            }
        }
        return road;
    }

    private Graph graphClone(Graph graph) {
        Graph copy = new AdjMatrixGraph();
        copy.setAdjMatrix(deepCopy(graph.getAdjMatrix()));
        copy.setECount(graph.getECount());
        copy.setVCount(graph.getVCount());

        return copy;
    }

    boolean[][] deepCopy(boolean[][] matrix) {
        return java.util.Arrays.stream(matrix).map(el -> el.clone()).toArray($ -> matrix.clone());
    }

    private void delRoad(List<int[]> delR, Graph graphCopy) {
        for (int i = 0; i < delR.size(); i++) {
            graphCopy.removeAdge(delR.get(i)[0], delR.get(i)[1]);
        }
    }

    private StringBuilder answer(List<int[]> road) {
        StringBuilder ans = new StringBuilder("Нет, так как нельзя при удалении ");
        for (int[] r : road) {
            ans.append(r[0] + "-" + r[1] + " ");
        }
        return ans;
    }

    public String solution (Graph graph, int pointA, int pointB, int roadsK) {
        if (Math.max(countEdge(graph, pointA), countEdge(graph, pointB)) <= roadsK) {
            return "Можно закрыть, так как количество удаляемых дорог больше, количество дорог, исходящих из одного из пунктов.";
        }

        final boolean[] flag = new boolean[1];


        List<List<int[]>> varDel = permutation(roadFind(graph), roadsK);

        for (List<int[]> varRoad : varDel) {
            Graph graphCopy = graphClone(graph);
            delRoad(varRoad, graphCopy);

            GraphAlgorithms.dfsRecursion(graphCopy, pointA, v -> {
                if (v == pointB) {
                    flag[0] = true;
                }
            });
            if (!flag[0]) {
                return answer(varRoad).toString();
            }
            flag[0] = false;
        }
        return "Нельзя добиться, закрыв " + roadsK + " дорог.";
    }
}
