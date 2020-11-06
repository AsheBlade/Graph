import java.util.*;

public class Uphill_Downhill {

    /*
    原题:
    You have a dictionary of places in Beijing.
    It's in the form of {location: elevation}. And an array of distances you find on Baidu Map connecting each places.
    Please find the length of the shortest route on which you can run completely uphill then completely downhill. Assume you live in "Huilongguan".
    elevations = {"Huilongguan": 5, "Chaoyang Park": 25, "National Stadium": 15, "Olympic Park": 20, "Tsinghua University": 10}
    paths = {
        ("Huilongguan", "Chaoyang Park"): 10,
        ("Huilongguan", "National Stadium"): 8,
        ("Huilongguan", "Olympic Park"): 15,
        ("Chaoyang Park", "Olympic Park"): 12,
        ("National Stadium", "Tsinghua University"): 10,
        ("Olympic Park", "Tsinghua University"): 5,
        ("Olympic Park", "Huilongguan"): 17,
        ("Tsinghua University", "Huilongguan"): 10
    }
    For this set of data, the shortest valid path would be "Huilongguan" -> "National Stadium" -> "Tsinghua University" -> "Huilongguan", with a distance of 28.
     */

    //思路:
    //1. 做一个上行graph, 一个下行graph,  下行graph要反着做, 这样才能用 Dijkstra
    //2. 用Dijkstra求两个graph的最短路径.
    //3. 把所有上行和下行的最短路径相加, 找出其中最小的和.

    public static void main(String[] args) {
        int[] elevations = {5,25,15,20,10};
        int[][] paths = {{0, 1, 10}, {0, 2, 8}, {0, 3, 15}, {1, 3, 12}, {2, 4, 10}, {3, 4, 5}, {3, 0, 17}, {4, 0, 10}};
        System.out.println(upDownShortestPath(elevations, paths));
    }

    public static int upDownShortestPath(int[] elevations, int[][] paths) {

        int[] upHillDistance = new int[elevations.length];
        int[] downHillDistance = new int[elevations.length];

        // Have to fill the array with max value first to make the  Dijkstra's algorithm work.
        Arrays.fill(upHillDistance, Integer.MAX_VALUE);
        Arrays.fill(downHillDistance, Integer.MAX_VALUE);

        Map<Integer, List<int[]>> uopG = new HashMap<>();
        Map<Integer, List<int[]>> downG = new HashMap<>();

        for(int i = 0; i < elevations.length; i++) {
            uopG.put(i, new ArrayList<>());
            downG.put(i, new ArrayList<>());
        }

        //构建纯上行和下行的两个graph
        for(int i = 0; i < paths.length; i++) {
            // 找出每个path对应的elevation, 比较elevation的大小, 从而判定路径是从下至上, 还是从上至下.
            if(elevations[paths[i][1]] > elevations[paths[i][0]]) {
                uopG.get(paths[i][0]).add(new int[]{paths[i][1], paths[i][2]});
            }
            else {
                downG.get(paths[i][1]).add(new int[]{paths[i][0], paths[i][2]});
            }
        }

        dijkstra(uopG, upHillDistance, 0);
        dijkstra(downG, downHillDistance, 0);


        // Iterate over every path to find the min solution.
        int answer = Integer.MAX_VALUE;
        for(int i = 1; i < elevations.length; i++) {
            if(upHillDistance[i] < Integer.MAX_VALUE && downHillDistance[i] < Integer.MAX_VALUE) {
                // Have to use this if to avoid overflow.
                answer = Math.min(answer, upHillDistance[i] + downHillDistance[i]);
            }
        }
        return answer;
    }

    // Use  Dijkstra's algorithm to find the shortest path on each graph. R
    private static void dijkstra(Map<Integer, List<int[]>> g, int[] distance, int startNode) {
        boolean[] processed = new boolean[distance.length];
        distance[startNode] = 0;

        PriorityQueue<int[]> minPq = new PriorityQueue<>((a1, a2) -> {return a1[0] - a2[0];});
        minPq.add(new int[]{0, startNode});
        while(!minPq.isEmpty()) {
            int[] curr = minPq.poll();
            int currNodeDistance = curr[0];
            int currNodeLabel = curr[1];
            if(processed[currNodeLabel]) {
                continue;
            }
            processed[currNodeLabel] = true;
            for(int[] edge : g.get(currNodeLabel)) {
                int neighborNodeLabel = edge[0];
                int weight = edge[1];
                if(currNodeDistance + weight < distance[neighborNodeLabel]) {
                    distance[neighborNodeLabel] = currNodeDistance + weight;
                    minPq.add(new int[]{distance[neighborNodeLabel], neighborNodeLabel});
                }
            }
        }
    }



}

