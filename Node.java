
public class Node {
    int degree = 0;
    Node left, right, child , parent;
    boolean childCut = false;
    int count;
    String keyword ="";


    Node(String keyword, int count){
            this.keyword = keyword;
            this.count = count;
            //set left right as itself for now
            this.left = this;
            this.right = this;
            this.parent = null;
            this.child = null;
    }
}
