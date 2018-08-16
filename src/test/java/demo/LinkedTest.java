package demo;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeCounterGenerator;

/**
 * @ClassName LinkedTest
 * @Description
 * @Author shuliyao
 * @CreateTime 2018/8/1 下午4:22
 */
public class LinkedTest {
    public static void main(String[] args) {
        Node nodeG =null;
        Node node = null;
        for (int i = 0; i < 1000; i++) {
            if(node == null){
                node = new Node();
                node.setValue(""+i);
                nodeG = node;
            }else{
                Node node1 = new Node();
                node1.setValue(""+i);
                node.setNode(node1);
                node = node1;
            }

        }


        Node readNode = nodeG;
//        while (readNode!=null){
//            System.out.println(readNode.getValue());
//            readNode = readNode.getNode();
//        }

        //
        Node node1 = readNode;
        Node node2 = null;
        Node node3 = null;
        Node zc = null;
        while (node1!=null){
            node2 = node1.getNode();

            if(node2 !=null){

                node1.setNode(zc);
                node3 = node2.getNode();
                node2.setNode(node1);
                zc =node2;
                if(node3!=null){
                    node1 = node3;
                    continue;
                }
            }
            node1 = node2;
            break;
        }

        Node node4 = node1;
        while (node4 !=null) {
            System.out.println(node4.getValue());
            node4 = node4.getNode();
        }
    }
}
