import java.util.*;

public class BasicGraph {
    /*
        最基本的Graph, 我没有使用Vertexclass, 而是直接使用String.
        重要: 这个应该牢记, 最好背下来. 每一行代码我都简化优化到了最大程度. 如果不能也要迅速默写出来, 可以极大提升做题效率.
     */
    private Map<String, List<String>> adjVertices;

    BasicGraph() {
        this.adjVertices = new HashMap<String, List<String>>();
    }

    void addVertex(String label) {
        adjVertices.putIfAbsent(label, new ArrayList<String>());
    }


    void addEdge(String label1, String label2) {
        // 这里是undirected, 所以把两个都连起来了. 如果是directed的话, 第二行删掉即可.
        adjVertices.get(label1).add(label2);
        adjVertices.get(label2).add(label1);
    }


    List<String> getAdjVertices(String label) {
        return adjVertices.get(label);
    }

    String printGraph() {
        StringBuffer sb = new StringBuffer();
        for(String s : adjVertices.keySet()) {
            sb.append(s);
            sb.append(adjVertices.get(s)+ "\n");
        }
        return sb.toString();
    }

    public void breadthFirstTraversal(String root) {
        Set<String> visited = new LinkedHashSet<String>();
        Queue<String> queue = new LinkedList<String>();
        queue.add(root);
        visited.add(root);

        while (!queue.isEmpty()) {
            String s = queue.poll();
            System.out.print(s+" ");
            for (String eachVertex : this.getAdjVertices(s)) {
                if (!visited.contains(eachVertex)) {
                    visited.add(eachVertex);
                    queue.add(eachVertex);
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
            for (String eachVertex : this.getAdjVertices(s)) {
                if (!visited.contains(eachVertex)) {
                    visited.add(eachVertex);
                    stack.push(eachVertex);
                }
            }
        }
    }

//    public int socialConnectionLevel(String root, String destination) {
//        Set<String> visited = new LinkedHashSet<String>();
//        Set<String> lastLevel = new LinkedHashSet<String>();
//        Queue<String> queue = new LinkedList<String>();
//        queue.add(root);
//        visited.add(root);
//        lastLevel.add(root);
//        int scl=0;
//
//        while (!queue.isEmpty()) {
//            String s = queue.poll();
//
//            if(s.equals(destination))
//            {
//                return scl;
//            }
//            for (String eachVertex : this.getAdjVertices(s)) {
//                if (!visited.contains(eachVertex)) {
//                    visited.add(eachVertex);
//                    queue.add(eachVertex);
//                }
//            }
//        }
//
//        return 0;
//    }

    public int socialConnectionLevel(String root, String destination) {
        Set<String> visited = new LinkedHashSet<String>();
        Set<String> lastVisited = new LinkedHashSet<String>();
        visited.add(root);
        lastVisited.add(root);
        int scl =0;

        /*
            单纯的BFS其实解不了这道题. 因为BFS算的是过了多少个Node, 而这道题需要算过了多少层Node.
            用lastVisted 去track上一层的Node, 然后scl计算经过的层数. 之后这一层traverse之后, 再用下一层(newVisted)把lastVisted覆盖.
            这个traverse方法对应在tree里面被称为 level order traverse, 因为是严格分层进行traverse.

            因为每一个Node和Edge其实还是只会过一次, 所以算力还是 O(V+E).   但是两个for loop不是好事, 我在想应该有更快捷简单的方法.
            注: 这道题只是用了一个很简单的graph去测试了一下. 如果可能的话, 需要更多验证.
         */

        while (!lastVisited.isEmpty()) {
            Set<String> newVisited = new LinkedHashSet<String>();

            for (String s : lastVisited)
            {
                if(s.equals(destination))
                {
                    return scl;
                }
                for (String eachVertex : this.getAdjVertices(s)) {
                    if (!visited.contains(eachVertex)) {
                        visited.add(eachVertex);
                        newVisited.add(eachVertex);
                    }
                }
            }

            lastVisited = newVisited;
            scl ++;

        }
        return 0;
    }


    /*
        这一版是上面DFS的原版作者写法. 我把它改成了跟BFS算法一样的写法.
        暂时还不是100%确定这个改动是否能在所有case顺利运行. 所以先把这个放这里. 测试之后再移除.
     */
//    public void depthFirstTraversal(String root) {
//        Set<String> visited = new LinkedHashSet<String>();
//        Stack<String> stack = new Stack<String>();
//        stack.push(root);
//
//        while (!stack.isEmpty()) {
//            String s = stack.pop();
//            if (!visited.contains(s)) {
//                visited.add(s);
//                System.out.print(s+" ");
//                for (String eachVertex : this.getAdjVertices(s)) {
//                    stack.push(eachVertex);
//                }
//            }
//        }
//    }


}