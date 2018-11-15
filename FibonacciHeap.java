
import java.util.*;


public class FibonacciHeap {
    // ppinter to max value of the heap
    Node root;


    public FibonacciHeap() {
        root = null;
    }

    // insert into the top level circular list
    public void insert(Node node) {
        //node needs to be inserted into the heap
        //childCut value being set to false as it is being inserted into top level of the fibonacci heap
        node.childCut = false;
        if (root != null) {
            Node firstnode = root;
            // if heap contains a single element, i.e then it's right pointer is to itself
            if (firstnode.right == firstnode) {
                firstnode.left = node;
                node.right = firstnode;
            } else {
                //contains more than 1 element
                node.right = firstnode.right;
                firstnode.right.left = node;
            }

            // Pointer to link the new node to the right of the current max element of the heap
            firstnode.right = node;
            node.left = firstnode;
            node.childCut = false;

            // Check whether the inserted node value is greater than the max node value
            if (root.count < node.count)
                root = node;
        } else {
            root = node;
            node.right = node;
            node.left = node;
        }
    }

    // cascading cut is called on the parent of the removed node
    // it is called till a childCut value of "false" is not encountered (or root node)
    public void cascadingCut(Node node) {
        Node par = node.parent;
        // if there's a parent
        if (par != null) {
            // remove child from parent
            Node newNode = remove(node);
            // add to top level linked list
            insert(newNode);

            //  Check whether the parent has childcut is TRUE
            if (par.childCut) {
                cascadingCut(par);
            } else {
                // if y is unmarked, set it marked
                par.childCut = true;
                node.childCut = false;
            }
        }
    }

    // add y as a child of x
    public void addChild(Node x, Node y) {
        // remove y from top list of heap
        y.left.right = y.right;
        y.right.left = y.left;
        // make y a child of x
        if (x.degree >= 1) {
            //when x has one or more children already
            // Insert the node as into the circular list of the parent
            Node firstChild = x.child;
            Node lastChild = firstChild.left;
            firstChild.left = y;
            lastChild.right = y;
            y.left = lastChild;
            y.right = firstChild;

        } else {
            //case for when x has no children
            // Insert the child as the first child of the parent
            x.child = y;
            y.right = y;
            y.left = y;
        }
        y.parent = x;
        x.degree += 1;
    }

    // melding the heaps according to the degree
    // public void meld(Node temp, HashMap<Integer, Node> map) {
    //   // System.out.println(temp.keyword);
    //     // check the degree of the entire top level list
    //     if (!map.containsKey(temp.degree))
    //         map.put(temp.degree, temp);
    //     Node currRight = root.right;
    //     //populate the hashmap
    //     //traverse for everynode
    //     while (currRight.right != root) {
    //         // Checking whether the given element exists in the hashtable and whether the degree of the current element is present in the hashtable
    //         // If there is an another element with same degree then combine the elements
    //         if (map.containsKey(currRight.degree) && !map.containsValue(currRight)) {
    //             // Extract the element from the hashtable/map that has the same degree as the current element of the circular list
    //             Node tempNode = map.remove(currRight.degree);
    //
    //             // Check which element is bigger
    //             if (tempNode.count < currRight.count) {
    //                 addChild(currRight, tempNode);
    //                 meld(currRight, map);
    //             } else {
    //                 addChild(tempNode, currRight);
    //                 meld(tempNode, map);
    //             }
    //             // Invoke pairwise combine with the new combined element
    //             // meld(tempNode, map);
    //             break;
    //         } else {
    //             // Check whether the element is present in the hashtable
    //             if (!map.containsValue(currRight))
    //                 map.put(currRight.degree, currRight);
    //             currRight = currRight.right;
    //         }
    //     }
    // }


    public void meld(Node temp, Map<Integer, Node> map) {
        Node currRight = root.right;
        while (currRight != root) {
            // Checking whether the given element exists in the hashtable and whether the degree of the current
            // element is present in the hashtable
            // If there is an another element with same degree then combine the elements
            if (map.containsKey(currRight.degree) && !map.containsValue(currRight)) {
                // Extract the element from the hashtable/map that has the same degree
                // as the current element of the circular list
                // System.out.println("Map has " + map.size() + " elements");
                Node tempNode = map.remove(currRight.degree);

                // Check which element is bigger
                if (tempNode.count < currRight.count) {
                    addChild(currRight, tempNode);
                    currRight = root.right; // restart process
                } else {
                    addChild(tempNode, currRight);
                    currRight = root.right; // restart process
                }
            } else {
                // Check whether the element is present in the hashtable
                if (!map.containsValue(currRight))
                    map.put(currRight.degree, currRight);
                currRight = currRight.right;
            }
        }
    }

    // function to reset the root pointer - to ensure it points to the max value
    public void resetRootPointer() {
        Node firstnode = root;
        Node pointer = root;
        Node tempmax = root;
        boolean flag = true;

        if (root == null) {
            return;
        }

        while (pointer.right != firstnode) {
            if (pointer.count > tempmax.count)
                tempmax = pointer;
            pointer = pointer.right;
            flag = false;
        }
        if (!flag) {
            if (pointer.count > tempmax.count)
                tempmax = pointer;
        }
        root = tempmax;
    }

    // function removes the root node(maximum) and raises all it's children to the root-level circular linked list
    public Node removeMax() {
        Node removedNode = root;
        root = removedNode.right;
        // ensuring that the maximum node is not the only one in the list
        if (removedNode != null) {
            int numChild = removedNode.degree;

            // case when the root node has no children
            if (numChild == 0) {
                // points to itself
                if (removedNode.right == removedNode)
                    root = null;
                else {
                    // Remove the root from top level circular linked list by resetting the Nodes on it's left and right
                    Node rightNode = removedNode.right;
                    Node leftNode = removedNode.left;
                    rightNode.left = leftNode;
                    leftNode.right = rightNode;
                    root = removedNode.right;
                }
            } else {
                // when the root node has children, i.e, when numChild != 0
                //remove every child of root node from it's sibling circular linked list and add it to the root or the top level list
                if (removedNode.right == removedNode)
                    root = removedNode.child;
                else {
                    // Pointer to the max and its adjacent element
                    Node leftNode = removedNode.left;
                    Node rightNode = removedNode.right;
                    // Pointer to the child element and its adjacent element
                    Node firstChild = removedNode.child;
                    Node lastChild = firstChild.left;

                    // children elements put into top level circular linkedList
                    leftNode.right = firstChild;
                    firstChild.left = leftNode;
                    rightNode.left = lastChild;
                    lastChild.right = rightNode;
                }
            }
            // reset the pointers of the root node
            removedNode.degree = 0;
            removedNode.child = removedNode;
            removedNode.right = removedNode;
            removedNode.left = removedNode;
            removedNode.parent = null;

            // set the root node to point to the maximum
            resetRootPointer();
            root.parent = null;
            Node maxSoFar = root;

            // resetting parent values of the root level circular linked list (as new nodes have been added to it)
            // also making sure all their childCut values are set to false
            while (maxSoFar.right != root) {
                maxSoFar.parent = null;
                maxSoFar.childCut = false;
                maxSoFar = maxSoFar.right;
            }
            maxSoFar.parent = null;
            HashMap<Integer, Node> map = new HashMap<>();
            meld(root, map);
        }
        return removedNode;
    }


    // detech an element node from it's parent node
    Node remove(Node node) {
        Node parent = node.parent;

        //  checking whether the node is at the root
        if (node.parent == null)
            return node;

        // if parent has more than one child
        if (parent.degree > 1) {
            // remove the child from sibling circular linked list
            Node nodeLeft = node.left;
            Node nodeRight = node.right;
            nodeLeft.right = nodeRight;
            nodeRight.left = nodeLeft;
            parent.child = nodeLeft;
        } else
            //remove the only child
            parent.child = parent;

        // decrease the degree of the parent and set the childcut of the parent to TRUE
        parent.degree--;
        parent.childCut = true;

        // resetting the child node
        node.left = node;
        node.right = node;
        node.parent = null;

        // reset root pointer
        resetRootPointer();
        return node;
    }


    // increase the value of the count by some value
    public void increaseKey(Node node, int incr) {
        // calculating what the new count for the node should be
        int new_value = node.count + incr;
        node.count = new_value;
        Node par = node.parent;

        //checking if node belongs to the root level circular linked list
        if (par == null) {
            // if the updated value is greater than the root node
            if (root.count < new_value) {
                root = node;
            }
        } else {
            // if the parent value is lesser than the count value - have to remove the child
            if (par.count < node.count) {

                //condition where the parent has not lost a child before
                if (!par.childCut) {

                    //checking if the parent has more than one child
                    if (par.degree > 1) {
                        // removing the child, decreasing parent's degree
                        Node nodeLeft = node.left;
                        Node nodeRight = node.right;
                        nodeLeft.right = nodeRight;
                        nodeRight.left = nodeLeft;
                        par.child = nodeLeft;
                        par.degree -= 1;
                    } else {
                        // has only one child
                        // remove the link to the child from its parent
                        par.child = par;
                        par.degree = 0;
                    }
                    // updating the values of the node
                    node.parent = null;
                    node.count = new_value;
                    node.right = node;
                    node.left = node;
                    insert(node);
                    par.childCut = true;
                } else {
                    node = remove(node);
                    insert(node);
                    cascadingCut(par);
                }

            }
        }
    }

}
