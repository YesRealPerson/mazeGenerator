import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

public class main {

    static class node{
        int id;

        int[] border;

        public node(int id){
            this.id = id;
            border = new int[]{1, 1, 1, 1};
        }
    }
    //create array to store nodes in maze
    static ArrayList<JButton> nodes = new ArrayList<>();
    static boolean generating = false;
    static Color black = Color.black;

    public static void main(String args[]){
        JFrame frame = new JFrame("maze generator");

        //initialize all nodes
//        int x = 0;
        for(int i = 0; i < 25; i++){
            for(int j = 0; j < 25; j++){
                JButton b=new JButton();
                b.setBounds(25*j+10,25*i+10,25,25);
                b.setBackground(Color.white);
                //walls on all sides
                b.setBorder(BorderFactory.createMatteBorder(1,1,1,1, black));
//                b.setText(Integer.toString(x++));
                nodes.add(b);
                frame.add(b);
            }
        }

        JButton regenerate = new JButton("Generate New Maze");
        regenerate.setBounds(245,800, 150, 25);
        regenerate.setBackground(Color.white);
        regenerate.setBorder(BorderFactory.createLineBorder(black));
        regenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!generating) {
                    reset();
                    generating = true;
                    System.out.println("Generating maze...");
                    generateMaze();
                    generating = false;
                    System.out.println("Finished!");
                }else{
                    System.out.println("Wait!");
                }
            }
        });
        frame.add(regenerate);

        frame.setSize(660,1000);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    //reset maze
    public static void reset(){
        for(JButton b : nodes){
            b.setBorder(BorderFactory.createMatteBorder(1,1,1,1, black));
        }
    }

    //to call recursive function
    public static void generateMaze(){
        Set<Integer> visited = new HashSet<>();
        generateMaze(new node(0), visited);
    }

    //generate maze randomized-DFS
    public static void generateMaze(node current, Set<Integer> visited){
        ArrayList<node> neighbors = getNeighbors(current.id);
        Collections.shuffle(neighbors);
        visited.add(current.id);
        for(node neighbor : neighbors){
            if(!visited.contains(neighbor.id)){
                connect(current, neighbor);
                generateMaze(neighbor, visited);
            }
        }
    }

    public static void connect(node node1, node node2 ){
        //check where nodes are connected
        //node1 is starting reference

        //node 2 is on right side
        if(node1.id+1==node2.id){
            node1.border[3] = 0;
            node2.border[1] = 0;
        }

        //node 2 is on left side
        else if(node1.id-1==node2.id){
            node1.border[1] = 0;
            node2.border[3] = 0;
        }

        //node 2 is on bottom side
        else if(node1.id+25==node2.id){
            node1.border[2] = 0;
            node2.border[0] = 0;
        }

        //node 2 is above
        else if(node1.id-25==node2.id){
            node1.border[0] = 0;
            node2.border[2] = 0;
        }
        updateBorder(node1);
        updateBorder(node2);
    }

    //updates border of node
    public static void updateBorder(node update){
        nodes.get(update.id).setBorder(BorderFactory.createMatteBorder(update.border[0],update.border[1],update.border[2],update.border[3], black));
    }

    //find neighbors
    public static ArrayList<node> getNeighbors(int node){
        ArrayList<node> neighbors = new ArrayList<>();

        boolean right = node%25==25-1;
        boolean left = node%25==0;
        boolean top = node-25>=0;
        boolean bottom = node+25<=624;

        if(!right){
            neighbors.add(new node(node+1));
        }
        if(!left){
            neighbors.add(new node(node-1));
        }
        if(top){
            neighbors.add(new node(node-25));
        }
        if(bottom){
            neighbors.add(new node(node+25));
        }

        return neighbors;
    }
}
