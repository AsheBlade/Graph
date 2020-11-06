import java.util.*;

public class WeightedIntGraph {

    private Map<Integer, List<Edge>> adj;

    private static class Edge {
        /*
            ConnectedTo 就是连接到的那个Vertex. 可以是String也可以是Int
            As always we assume all Vertex holds DISTINCTIVE labels.
         */
        private int connectedTo;
        private int weight;

        public Edge(int connectedTo, int weight) {
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

    private static class minDistance {
        // 因为我们用的是String的label,所以做了这么一个 inner class.
        // 如果int type的label, 则用一个int[] 就可以, 不用单独建这么一个class.
        // 另: 虽然是这样的. 但是其实用了int[]之后minpq还是要写comparator, 代码量并没有明显降低. 为了背着方便, 这一部分目前还是保留. 
        private int distance;
        private int label;

        public minDistance(int label, int distance)
        {
            this.distance = distance;
            this.label = label;
        }
    }

    public WeightedIntGraph() {
        this.adj = new HashMap<Integer, List<Edge>>();
    }

    void addVertex(int label) {
        adj.putIfAbsent(label, new ArrayList<Edge>());
    }

    void addEdge(int label1, int label2, int weight) {
        // directed Graph, 所以只加一遍.  如果undirected的话, 加两遍即可.
        adj.get(label1).add(new Edge(label2, weight));
    }

    List<Edge> getEdges(int label) {
        // return all the connected edges.
        return adj.get(label);
    }

    public void printGraph() {
        StringBuffer sb = new StringBuffer();
        for(int s : adj.keySet()) {
            sb.append(s);
            sb.append(adj.get(s)+ "\n");
        }
        System.out.println(sb.toString());;
    }

    public void breadthFirstTraversal(int root) {
        Set<Integer> visited = new LinkedHashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.add(root);
        visited.add(root);

        while (!queue.isEmpty()) {
            int s = queue.poll();
            System.out.print(s+" ");
            for (Edge eachEdge : this.getEdges(s)) {
                if (!visited.contains(eachEdge.connectedTo)) {
                    visited.add(eachEdge.connectedTo);
                    queue.add(eachEdge.connectedTo);
                }
            }
        }
    }

    public void depthFirstTraversal(int root) {
        Set<Integer> visited = new LinkedHashSet<>();
        Stack<Integer> stack = new Stack<>();
        stack.push(root);
        visited.add(root);

        while (!stack.isEmpty()) {
            int s = stack.pop();
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
    public Map<Integer,Integer> dijkstra(int startVertex) {
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

            另: 这个代码是自己写的, 没有经过大量验证. 用的是小规模的graph严整, 所以最好能有大graph验证一下.
         */
        Map<Integer,Integer> distance = new HashMap<>(); // keep checking the distance from startVertex to each vertex.
        Set<Integer> visited = new LinkedHashSet<>();// track which vertex is visited.
        for(Map.Entry mapElement: adj.entrySet()) {
            // traverse entire graph and set default value for distance and processed.
            int vertexLabel = (Integer) mapElement.getKey();
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
            int currNodeLabel = curr.label;

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
                int neighborNodeLabel = edge.connectedTo;
                int weight = edge.weight;
                int newDistance = currNodeDistance + weight;

                if( newDistance < distance.get(neighborNodeLabel)){
                    // If the newDistance is smaller than the previous stored one. update the distance.
                    distance.put(neighborNodeLabel, newDistance);
                    minPq.add(new minDistance(neighborNodeLabel, newDistance));
                }
            }
        }

        return distance;
    }





}