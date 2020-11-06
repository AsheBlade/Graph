import java.util.HashMap;
import java.util.Map;

public class Test {

    /* Test class to handle all tests.
     */

    public static void main(String[] args) {

        buildBasicGraph();
        //buildWeightedGraph();
        //buildIntWeightedGraph();
    }

    private static void buildIntWeightedGraph()
    {
        WeightedIntGraph wig = new WeightedIntGraph();

        wig.addVertex(0);
        wig.addVertex(1);
        wig.addVertex(2);
        wig.addVertex(3);
        wig.addVertex(4);
        wig.addVertex(5);

        wig.addEdge(0,1,4);
        wig.addEdge(0,2,3);
        wig.addEdge(1,2,5);
        wig.addEdge(1,3,2);
        wig.addEdge(3,4,2);
        wig.addEdge(2,3,7);
        wig.addEdge(4,1,4);
        wig.addEdge(4,5,6);
        wig.addEdge(4,0,4);

        wig.printGraph();

        // BFS
        System.out.print("Print BFS Result : ");
        wig.breadthFirstTraversal(0);
        System.out.println();

        // DFS
        System.out.print("Print DFS Result : ");
        wig.depthFirstTraversal(0);
        System.out.println("\n");

        // Dijkstra
        System.out.println(wig.dijkstra(0));
    }

    private static void buildWeightedGraph()
    {
        /*
            构建一个Weighted Graph, 加入weight和directed.
         */
        WeightedGraph wg = new WeightedGraph();

        wg.addVertex("A");
        wg.addVertex("B");
        wg.addVertex("C");
        wg.addVertex("D");
        wg.addVertex("E");
        wg.addVertex("F");

        wg.addEdge("A", "B",4);
        wg.addEdge("A",  "C",2);
        wg.addEdge("B", "C",5);
        wg.addEdge("B", "D",10);
        wg.addEdge("C", "E",3);
        wg.addEdge("E", "D",4);
        wg.addEdge("D", "F",11);

        wg.printGraph();

        // BFS
        System.out.print("Print BFS Result : ");
        wg.breadthFirstTraversal("A");
        System.out.println();

        // DFS
        System.out.print("Print DFS Result : ");
        wg.depthFirstTraversal("A");
        System.out.println("\n");

        // Dijkstra
        System.out.println(wg.dijkstra("A"));
    }





    private static void buildBasicGraph()
    {
        /*
            构建一个最基本的Graph, 加入Vertex和Edge, 然后打印.
         */
        BasicGraph bg = new BasicGraph();

        bg.addVertex("Bob");
        bg.addVertex("Mark");
        bg.addVertex("Rob");
        bg.addVertex("Maria");
        bg.addVertex("Alice");

        bg.addEdge("Bob", "Alice");
        bg.addEdge("Bob",  "Rob");
        bg.addEdge("Alice", "Mark");
        bg.addEdge("Alice", "Maria");
        bg.addEdge("Rob", "Mark");
        bg.addEdge("Maria", "Rob");

        System.out.println(bg.printGraph() + "\n");;

        // BFS
        System.out.print("Print BFS Result : ");
        bg.breadthFirstTraversal("Bob");
        System.out.println();

        // DFS
        System.out.print("Print DFS Result : ");
        bg.depthFirstTraversal("Bob");
        System.out.println();

        /*
            Social Connection Level
         */
        System.out.print("Social Connection Level between Maria and Bob : ");
        System.out.println(bg.socialConnectionLevel("Maria", "Bob"));
    }


}
