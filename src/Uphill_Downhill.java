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

    /*
        思路:
        1. 做一个上行graph, 一个下行graph,  下行graph要反着做, 这样才能用 Dijkstra
        2. 用Dijkstra求两个graph的最短路径. 因为题目中给定了起点是回龙观. 所以我们的dijkstra的结果是回龙观到所有其他所有地点的上行距离和下行距离.
            注意, 虽然是一个上行graph, 下行graph, 但是下行的graph需要反方向做, 这样做才可以搜索.
            上行graph之中的所有距离是起点到各个点之中的上行距离. 而下行反着做, 是所有其他点到起点的最短距离. 之所以下行需要反向构建, 因为最终会回到原点, 所以, 在第三步要用原点的key进行搜索.
            这个实在是很难用语言表示清楚, 建议画个图, 看图比较直观,思考会快一些.  这题挺难的, 但想10分钟是能理解的.

        3. 把所有上行和下行的最短路径相加, 找出其中最小的和.
     */
    private Map<String, List<Edge>> adj;

    private static class Path{
        // 构建一个inner class path 用来存储两个地点. 并且处理hashcode
        String destination;
        String start;
        Path(String start, String destination){
            this.destination = destination;
            this.start = start;
        }

        // Hashmap 的get方法会同时对比key value和hashcode, 这里统一hashcode 100, 就让其只对比hash value即可.
        @Override
        public int hashCode()
        {
            return 100;
        }

        @Override
        public boolean equals(Object obj)
        {
            if(this.destination.equals(((Path)obj).destination) && this.start.equals(((Path)obj).start) )
                return true;
            else
                return false;
        }
    }

    public static void main(String[] args) {

        // ********************************************** 1. 构建和数据输入, 建立上行下行graph **********************************************
        // 题目中的数据, 构建elevation, 并把数据塞进去
        Map<String, Integer> elevationMap = new HashMap<>();
        elevationMap.put("Huilongguan",5);
        elevationMap.put("Chaoyang Park",25);
        elevationMap.put("National Stadium",15);
        elevationMap.put("Olympic Park",20);
        elevationMap.put("Tsinghua University",10);

        //题目中的给定数据, 构建path, 并把数据塞进去.
        Map<Path, Integer> pathsMap = new HashMap<>();
        pathsMap.put(new Path("Huilongguan", "Chaoyang Park"), 10);
        pathsMap.put(new Path("Huilongguan", "National Stadium"), 8);
        pathsMap.put(new Path("Huilongguan", "Olympic Park"),15);
        pathsMap.put(new Path("Chaoyang Park", "Olympic Park"),12);
        pathsMap.put(new Path("National Stadium", "Tsinghua University"), 10);
        pathsMap.put(new Path("Olympic Park", "Tsinghua University"), 5);
        pathsMap.put(new Path("Olympic Park", "Huilongguan"),17);
        pathsMap.put(new Path("Tsinghua University", "Huilongguan"),10);

        // init 两个graph, 一个上行, 一个下行. 下面例行公事加入vertex
        Uphill_Downhill upGraph = new Uphill_Downhill();
        Uphill_Downhill downGraph = new Uphill_Downhill();

        upGraph.addVertex("Huilongguan");
        upGraph.addVertex("Chaoyang Park");
        upGraph.addVertex("National Stadium");
        upGraph.addVertex("Olympic Park");
        upGraph.addVertex("Tsinghua University");

        downGraph.addVertex("Huilongguan");
        downGraph.addVertex("Chaoyang Park");
        downGraph.addVertex("National Stadium");
        downGraph.addVertex("Olympic Park");
        downGraph.addVertex("Tsinghua University");


        // 比较每一条路径的elevation, 如果起点elevation大于终点的, 就加进上行graph, 反之则加进下行graph.
        // 下行的终点elevation小于起点的, 但依然要把终点放在前面, 起点放在后面, 要反着做.
        // 原因是我们最后会回到终点, 而hashmap只允许检索key, 不能检索value.
        // 回程的时候, 我们要去搜索可以, 而无法搜索, value. 所以下行的graph要反方向构建.
        // 这个其实挺不好想象的, 也很难表达, 我言尽于此. 虽然难, 但花10分钟是能想明白的. 可以用笔画画图, 有帮助.
        for(Path p : pathsMap.keySet()) {
            if(elevationMap.get(p.destination) > elevationMap.get(p.start)){
                upGraph.addEdge(p.start, p.destination, pathsMap.get(p));
            }
            else{
                downGraph.addEdge(p.destination, p.start, pathsMap.get(p));
            }
        }

        System.out.println("Print uphill Graph.");
        upGraph.printGraph();
        System.out.println("Print downhill Graph.");
        downGraph.printGraph();


        // ********************************************** 2. 用Distra求给定回龙观到所有点的上行下行最短距离(半程) **********************************************
        // 这里用的distra是我们自己写的, 直接套进去, 除了最后移除原点的0距离,其他都没有改.
        Map<String,Integer> upHillDistance = upGraph.dijkstra("Huilongguan");
        Map<String,Integer> downHillDistance = downGraph.dijkstra("Huilongguan");


        // ********************************************** 3.用上一部的最短距离, 把上行下行到固定点的距离相加, 从他们的和之中求最短全程**********************************************
        int shortestDistance = Integer.MAX_VALUE;
        String halfWayDestination = "";
        for(String s : upHillDistance.keySet()) {
            if(upHillDistance.get(s)<Integer.MAX_VALUE && downHillDistance.get(s)<Integer.MAX_VALUE) {
                //shortestDistance = Math.min(shortestDistance, upHillDistance.get(s) + downHillDistance.get(s));
                if(upHillDistance.get(s) + downHillDistance.get(s) < shortestDistance){
                    shortestDistance = upHillDistance.get(s) + downHillDistance.get(s);
                    halfWayDestination = s;
                }
            }
        }

        System.out.println("Shortest Distance = " + shortestDistance);
        System.out.println("We will run back from: " + halfWayDestination);
    }



    // ********************************************* 以下都是graph的常用code, 没有根据这道题做任何改动, 粘贴进来的******************************************

    private static class Edge {
        /*
            ConnectedTo 就是连接到的那个Vertex. 可以是String也可以是Int
            As always we assume all Vertex holds DISTINCTIVE labels.
         */
        private String connectedTo;
        private int weight;

        public Edge(String connectedTo, int weight) {
            this.connectedTo = connectedTo;
            this.weight = weight;
        }

        // Helper method for printing
        @Override
        public String toString() {
            return "Edge{" +
                    "connectedTo='" + connectedTo + '\'' +
                    ", weight=" + weight +
                    '}';
        }
    }



    public Uphill_Downhill() {
        this.adj = new HashMap<>();
    }

    void addVertex(String label) {
        adj.putIfAbsent(label, new ArrayList<>());
    }

    void addEdge(String label1, String label2, int weight) {
        // directed Graph, 所以只加一遍.  如果undirected的话, 加两遍即可.
        adj.get(label1).add(new Uphill_Downhill.Edge(label2, weight));
    }

    List<Uphill_Downhill.Edge> getEdges(String label) {
        // return all the connected edges.
        return adj.get(label);
    }

    public void printGraph() {
        /*
            这段可以不背, 基本用不到.
         */
        StringBuffer sb = new StringBuffer();
        for(String s : adj.keySet()) {
            sb.append(s);
            sb.append(adj.get(s)+ "\n");
        }
        System.out.println(sb.toString());;
    }

    private static class minDistance {
        // 因为我们用的是String的label,所以做了这么一个 inner class.
        // 如果int type的label, 则用一个int[] 就可以, 不用单独建这么一个class.
        private int distance;
        private String label;

        public minDistance(String label, int distance)
        {
            this.distance = distance;
            this.label = label;
        }
    }

    public Map<String,Integer> dijkstra(String startVertex) {

        Map<String,Integer> distance = new HashMap<>(); // keep checking the distance from startVertex to each vertex.
        Set<String> visited = new LinkedHashSet<>();// track which vertex is visited.
        for(Map.Entry mapElement: adj.entrySet()) {
            // traverse entire graph and set default value for distance and processed.
            String vertexLabel = (String) mapElement.getKey();
            distance.put(vertexLabel, Integer.MAX_VALUE);
        }

        // For start edge, put distance = 0.
        distance.put(startVertex,0);

        PriorityQueue<minDistance> minPq = new PriorityQueue<>((dis1, dis2) -> {
            /*
                Here we use lambda to do comparison. This is same as using a comparator.
                It basically compare the distance and make sure the queue goes from vertex with smallest distance to largest distance.
             */
            int mindis1 = dis1.distance;
            int mindis2 = dis2.distance;
            return mindis1-mindis2;
        });

        // Baisc case, add startEdge into the graph.
        minPq.add(new minDistance(startVertex,0));

        while(!minPq.isEmpty()) {
            minDistance curr = minPq.poll(); // return and remove the top element in Pq, which is the vertex with smallest distance.
            int currNodeDistance = curr.distance;
            String currNodeLabel = curr.label;

            if (visited.contains(currNodeLabel)) {
                continue;
            }else{
                visited.add(currNodeLabel);
            }

            for(Edge edge : adj.get(currNodeLabel)) {
                // traverse for each connected edge.
                String neighborNodeLabel = edge.connectedTo;
                int weight = edge.weight;
                int newDistance = currNodeDistance + weight;

                if( newDistance < distance.get(neighborNodeLabel)){
                    // If the newDistance is smaller than the previous stored one. update the distance.
                    distance.put(neighborNodeLabel, newDistance);
                    minPq.add(new minDistance(neighborNodeLabel, newDistance));
                }
            }
        }
        //最后把起点的0距离移除.  这个可以存在或者可以不存在, 根据题来定.
        distance.remove(startVertex,0);

        return distance;
    }
}
