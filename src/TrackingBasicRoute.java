import java.util.*;

public class TrackingBasicRoute {
    /*
        写这个class的原因是因为一些题目不仅仅需要得到最短路径的值, 同时也需要知道路径是什么.  BasicGraph.java里面只有求值, 没有路径.
        还是老规矩, 用String, 不用Int, Int不通用.

        没有什么难点,在还是加了一个paths的量去track route而已, key是String是每个vertex的label, value是路径.
        因为BFS和DFS都是所有的点走一次, 不存在Dijkstra这种最短路径问题需要不断和过去矫正的问题.  所以没什么复杂的, 看一下很容易理解.  DFS和BFS里面都标注了, 加上root那一行总共也就4行代码.
     */

    private Map<String, List<String>> adj;
    private Map<String, List<String>> paths;

    TrackingBasicRoute() {
        this.adj = new HashMap<>();
        this.paths = new HashMap<>();
    }

    void addVertex(String label) {
        adj.putIfAbsent(label, new ArrayList<>());
        paths.putIfAbsent(label, new ArrayList<>());
    }


    void addEdge(String label1, String label2) {
        // 这里是undirected, 所以把两个都连起来了. 如果是directed的话, 第二行删掉即可.
        adj.get(label1).add(label2);
        adj.get(label2).add(label1);
    }


    List<String> getAdjVertices(String label) {
        return adj.get(label);
    }

    String printGraph() {
        StringBuffer sb = new StringBuffer();
        for(String s : adj.keySet()) {
            sb.append(s);
            sb.append(adj.get(s)+ "\n");
        }
        return sb.toString();
    }

    public void breadthFirstTraversal(String root) {
        Set<String> visited = new LinkedHashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(root);
        visited.add(root);
        // put root into paths with empty array.
        paths.put(root, new ArrayList<>());

        while (!queue.isEmpty()) {
            String s = queue.poll();
            System.out.print(s+" ");
            for (String eachVertex : this.getAdjVertices(s)) {
                if (!visited.contains(eachVertex)) {
                    visited.add(eachVertex);
                    queue.add(eachVertex);

                    // Tack the paths.
                    List<String> parentsList = new ArrayList<>(paths.get(s)); // put paths.get(S) inside the bracket to avoid copy the reference.
                    parentsList.add(s);
                    paths.put(eachVertex, parentsList);
                }
            }
        }
    }

    String printPaths() {
        StringBuffer sb = new StringBuffer();
        for(String s : paths.keySet()) {
            sb.append(s);
            sb.append(paths.get(s)+ "\n");
        }
        return sb.toString();
    }

    public void depthFirstTraversal(String root) {
        Set<String> visited = new LinkedHashSet<>();
        Stack<String> stack = new Stack<>();
        stack.push(root);
        visited.add(root);
        // put root into paths with empty array.
        paths.put(root, new ArrayList<>());

        while (!stack.isEmpty()) {
            String s = stack.pop();
            System.out.print(s+" ");
            for (String eachVertex : this.getAdjVertices(s)) {
                if (!visited.contains(eachVertex)) {
                    visited.add(eachVertex);
                    stack.push(eachVertex);

                    // Tack the paths.
                    List<String> parentsList = new ArrayList<>(paths.get(s)); // put paths.get(S) inside the bracket to avoid copy the reference.
                    parentsList.add(s);
                    paths.put(eachVertex, parentsList);
                }
            }
        }
    }

    public void cleanPaths()
    {
        this.paths.clear();
    }
}
