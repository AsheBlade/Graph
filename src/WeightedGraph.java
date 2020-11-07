import java.util.*;

public class WeightedGraph {
    /*
        这里用的graph是weighted & directed. 这里用一个class去写. 因为面试之中一个肯定要用一个class来写.
        关于要不要用inner class Edge的问题, 想了很久还是决定用. 因为不用的话, 其实反而会更麻烦有各种各样的问题.
        这里用的图可以参见Shadow Archive之中的guide

        另: 有的构建还会引入inner class Vertex, 我决定先不用Vertex. 因为Inner class这种东西使用是为了更加便捷. 但是能砍的话最好还是往下砍.
        用Edge会更便捷, 但Vertex却不一定, 可能只会更冗长. 当然了, 如果能写出万金油的Vertex当然最好. 不用Vertex的话, 内部代码就要根据Object Type进行更改.
     */
    private Map<String, List<Edge>> adj;

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



    public WeightedGraph() {
        this.adj = new HashMap<String, List<Edge>>();
    }

    void addVertex(String label) {
        adj.putIfAbsent(label, new ArrayList<Edge>());
    }

    void addEdge(String label1, String label2, int weight) {
        // directed Graph, 所以只加一遍.  如果undirected的话, 加两遍即可.
        adj.get(label1).add(new Edge(label2, weight));
    }

    List<Edge> getEdges(String label) {
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

    public void breadthFirstTraversal(String root) {
        Set<String> visited = new LinkedHashSet<>();
        Queue<String> queue = new LinkedList<String>();
        queue.add(root);
        visited.add(root);

        while (!queue.isEmpty()) {
            String s = queue.poll();
            System.out.print(s+" ");
            for (Edge eachEdge : this.getEdges(s)) {
                if (!visited.contains(eachEdge.connectedTo)) {
                    visited.add(eachEdge.connectedTo);
                    queue.add(eachEdge.connectedTo);
                }
            }
        }
    }

    public void depthFirstTraversal(String root) {
        Set<String> visited = new LinkedHashSet<String>();
        Stack<String> stack = new Stack<String>();
        stack.push(root);
        visited.add(root);

        while (!stack.isEmpty()) {
            String s = stack.pop();
            System.out.print(s+" ");
            for (Edge eachEdge : this.getEdges(s)) {
                if (!visited.contains(eachEdge.connectedTo)) {
                    visited.add(eachEdge.connectedTo);
                    stack.push(eachEdge.connectedTo);
                }
            }
        }
    }

    // Use  Dijkstra's algorithm to find the shortest path on each graph.
    // Dijkstra 从这里开始.

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
        /*
            Dijkstra 这一块搜了很多都没有好的, 最后东拼西凑了一下代码是自己写的, 自我感觉已经优化到极致了.
            表面上很复杂, 其实并不困难. 其实只有三个量, minPq, visited, distance.
            visited自不必说, 网上看看BFS DFS就会理解visited存在的意义.
            distance是返回的答案, 其实如果你不需要返回这个答案的话, distance也可以没有.
            主要就在于理解minPq, 可以去搜一下Java Priority Queue, PQ可以理解为自动rank的queue, 就是每次加入元素会自动rank
            这里用的是override compare方法, 即每次加入新的vertex都会按照其上面的distance, 这样就保证每次poll的时候都自动把有最小distance的那个poll出来.

            算法也不难理解. 就是永远用从最小的distance的那个vertex往下走. 具体算法搜一下吧. 其实挺容易理解的.
            值得一提的是, 这段代码是无差别搜索, 即给定一个起点搜索全图. 而不是有起点和终点的那种定向搜索.

            Return: distance 是一个hashmap<String, Integer>, key是vertex label, value是startVertex到各个key vertex 的最短距离.
            注:
            1. distance之中无法到达的点的distance 会是 Integer.MAX_VALUE, 可以在最后进行手工移除.
            2. 原点的距离输出是0, 这个根据不同的题目可以保留或者移除.
            3. 这个算法只能求得到固定点的最短距离, 无法求得打到固定点的最短距离路径. 如果要求路径, 还需要进行更改, 或者干脆使用其他算法.

            另: 这个代码是自己写的, 没有经过大量验证. 用的是小规模的graph严整, 所以最好能有大graph验证一下.
            更新: 2020-11-06 这个代码已经三个不同的graph验证了三次, 包括String Graph, Integer Graph 和 上行下行Graph. 应该比较稳定了.
         */
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
                // If visited, then we don't have to go through its subEdge.
                // 注意, 与DFS, BFS不同, 这个check visited必须放在这里.
                // 因为要经常update minDistance, 所以还会把旧的edge放回去. 所以不能放在下面那个for loop之中.
                // 这个想了一下还很费脑子, 背吧, 最好不要思考.
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

        //注, 这个算法的distance之中无法到达的点的distance 会是 Integer.MAX_VALUE, 可以在最后进行手工移除.

        return distance;
    }





}