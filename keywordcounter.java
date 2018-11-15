
import java.io.IOException;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;

public class keywordcounter {

    public static void main(String args[]) throws IOException {
        FibonacciHeap fb = new FibonacciHeap();
        // HashMap to store the String - Node pairs
        HashMap<String, Node> nodeAll = new HashMap<String, Node>();

        // taking input file path from command line
        String inputFile = args[0];     // the first argument is the file
        String outputFile = "./output_file.txt";
        File input = new File(inputFile);
        File output = new File(outputFile);
        BufferedWriter writer = null;
        try {
            Scanner sc = new Scanner(input);
            writer = new BufferedWriter(new FileWriter(output));
            StringBuilder writeToFile = new StringBuilder("");
            while (sc.hasNextLine()) {
                String str = sc.nextLine();
                String[] strSplit = str.split(" ");
                // checking to see if "$" is contained in the string
                if (strSplit[0].contains("$")) {
                    String newStr = strSplit[0].replace("$", "");
                    if (!nodeAll.containsKey(newStr)) {
                        Node node = new Node(newStr, Integer.parseInt(strSplit[1]));
                        fb.insert(node);
                        nodeAll.put(newStr, node);
                    } else {
                        fb.increaseKey(nodeAll.get(newStr), Integer.parseInt(strSplit[1]));
                    }
                } else {
                    //Stop function if "stop" is encountered
                    if (str.compareToIgnoreCase("stop") == 0) {
                        writer.close();
                        System.exit(0);
                    } else {
                        // a query has been encountered
                        // call removeMax n number of times and save in a ArrayList called removedNodes
                        int top = Integer.parseInt(str);
                        ArrayList<Node> removedNodes = new ArrayList<>();
                        int numNodes = nodeAll.size();
                        boolean flag = false;
                        if(top > numNodes){
                            top = numNodes - 1;
                            flag = true;
                        }
                        while (top > 0) {
                            Node max = fb.removeMax();
                            removedNodes.add(max);
                            writeToFile.append((max.keyword + ","));
                            top--;
                        }
                        if(flag){
                            writeToFile.append(fb.root.keyword + ",");
                            fb.root = null;
                        }
                        writeToFile.deleteCharAt(writeToFile.length() - 1);
                        writeToFile.append("\n");
                        writer.write(writeToFile.toString());

                        //add the removed nodes back into the heap
                        int size = removedNodes.size() - 1;
                        while (size > -1) {
                            fb.insert(removedNodes.get(size));
                            size--;
                            writeToFile.delete(0, writeToFile.length());
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(e);

                }
            }
        }
    }
}
