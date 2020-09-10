public class Test {

    /* Test class to handle all tests.
     */

    public static void main(String[] args) {

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
        System.out.println(bg.socialConnectionLevel("Maria", "Bob"));

    }


}
