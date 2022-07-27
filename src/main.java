import javax.swing.*;
import java.awt.*;
import java.util.*;

public class main {

    //create array to store nodes in maze
    static ArrayList<node> nodes = new ArrayList<>();
    //to check if currently generating
    static boolean generating = false;
    static Random gen;
    static String seed;

    public static void main(String[] args) {
        JFrame frame = new JFrame("maze generator");

        //Win text
        JTextField winText = new JTextField("");
        winText.setBounds(160, 650, 1000, 50);
        winText.setOpaque(false);
        winText.setEditable(false);
        winText.setFocusable(false);
        winText.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0)));
        winText.setFont(new Font("Arial", Font.PLAIN, 30));
        frame.add(winText);

        //initialize all nodes
        int x = 0;
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 25; j++) {
                JButton b = new JButton();
                b.setBounds(25 * j + 10, 25 * i + 10, 25, 25);
                b.setBackground(Color.white);
                //walls on all sides
                b.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
                //adding button press functionality (turn color red if near blue or red without border)
                if (!(i == 0 && j == 0)) {
                    int finalX = x;
                    b.addActionListener(e -> {
                        ArrayList<node> neighbors = getNeighbors(finalX, false);
                        node current = nodes.get(finalX);
                        for (node test : neighbors) {
                            if (test.button.getBackground() == Color.red || test.button.getBackground() == Color.blue) {
                                //right
                                if (current.id + 1 == test.id) {
                                    if (current.border[3] == 0 && test.border[1] == 0) {
                                        if (current.button.getBackground() == Color.green) {
                                            winText.setText("Congratulations! Winner!");
                                        } else {
                                            current.button.setBackground(Color.red);
                                        }
                                        break;
                                    }
                                }
                                //left
                                if (current.id - 1 == test.id) {
                                    if (current.border[1] == 0 && test.border[3] == 0) {
                                        if (current.button.getBackground() == Color.green) {
                                            winText.setText("Congratulations! Winner!");
                                        } else {
                                            current.button.setBackground(Color.red);
                                        }
                                        break;
                                    }
                                }
                                //top
                                if (current.id - 25 == test.id) {
                                    if (current.border[0] == 0 && test.border[2] == 0) {
                                        if (current.button.getBackground() == Color.green) {
                                            winText.setText("Congratulations! Winner!");
                                        } else {
                                            current.button.setBackground(Color.red);
                                        }
                                        break;
                                    }
                                }
                                //bottom
                                if (current.id + 25 == test.id) {
                                    if (current.border[2] == 0 && test.border[0] == 0) {
                                        if (current.button.getBackground() == Color.green) {
                                            winText.setText("Congratulations! Winner!");
                                        } else {
                                            current.button.setBackground(Color.red);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    });
                }
                nodes.add(new node(x++, b));
                frame.add(b);
            }
        }

        //seed input
        JTextField seedInput = new JTextField("Enter seed (leave blank for random)");
        seedInput.setBounds(220, 850, 200, 25);
        seedInput.setBackground(Color.white);
        seedInput.setBorder(BorderFactory.createLineBorder(Color.black));
        frame.add(seedInput);


        //generate button
        JButton regenerate = new JButton("Generate New Maze");
        regenerate.setBounds(245, 800, 150, 25);
        regenerate.setBackground(Color.white);
        regenerate.setBorder(BorderFactory.createLineBorder(Color.black));
        //receive input
        regenerate.addActionListener(e -> {
            seed = seedInput.getText();
            reset();
            generating = true;
            generateMaze();
            generating = false;

            winText.setText("");
        });
        frame.add(regenerate);

        //initialize frame
        frame.setSize(660, 1000);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    //reset maze
    public static void reset() {
        for (node n : nodes) {
            n.button.setBackground(Color.white);
            n.border = new int[]{1, 1, 1, 1};
        }
        nodes.get(0).button.setBackground(Color.BLUE);
        nodes.get(nodes.size() - 1).button.setBackground(Color.GREEN);
    }

    //to call recursive function
    public static void generateMaze() {
        try {
            gen = new Random(Integer.parseInt(seed));
        } catch (Exception e) {
            gen = new Random((int) (Math.random() * 32768));
        }
        Set<Integer> visited = new HashSet<>();
        generateMaze(nodes.get(0), visited);
    }

    //generate maze randomized-DFS
    public static void generateMaze(node current, Set<Integer> visited) {
        ArrayList<node> neighbors = getNeighbors(current.id, true);
        visited.add(current.id);
        for (node neighbor : neighbors) {
            if (!visited.contains(neighbor.id)) {
                connect(current, neighbor);
                generateMaze(neighbor, visited);
            }
        }
    }

    public static void connect(node node1, node node2) {
        //check where nodes are connected
        //node1 is starting reference

        //node 2 is on right side
        if (node1.id + 1 == node2.id) {
            node1.border[3] = 0;
            node2.border[1] = 0;
        }

        //node 2 is on left side
        else if (node1.id - 1 == node2.id) {
            node1.border[1] = 0;
            node2.border[3] = 0;
        }

        //node 2 is on bottom side
        else if (node1.id + 25 == node2.id) {
            node1.border[2] = 0;
            node2.border[0] = 0;
        }

        //node 2 is above
        else if (node1.id - 25 == node2.id) {
            node1.border[0] = 0;
            node2.border[2] = 0;
        }
        updateBorder(node1);
        updateBorder(node2);
    }

    //updates border of node
    public static void updateBorder(node update) {
        nodes.get(update.id).button
                .setBorder(
                        BorderFactory
                                .createMatteBorder(
                                        update.border[0],
                                        update.border[1],
                                        update.border[2],
                                        update.border[3],
                                        Color.black));
    }

    //find neighbors
    //{right, left, top, bottom}
    public static ArrayList<node> getNeighbors(int node, boolean random) {
        ArrayList<node> neighbors = new ArrayList<>();

        boolean[] neighborsBool = {node % 25 != 24, node % 25 != 0, node - 25 >= 0, node + 25 <= 624};
        int[] possibleNeighbors = {node + 1, node - 1, node - 25, node + 25};

        for (int i = 0; i < 4; i++) {
            boolean test = neighborsBool[i];
            int possible = possibleNeighbors[i];
            if (test) neighbors.add(nodes.get(possible));
        }

        if (random) Collections.shuffle(neighbors, gen);

        return neighbors;
    }

    static class node {
        int id;
        JButton button;
        int[] border;

        public node(int id, JButton button) {
            this.id = id;
            this.button = button;
            //{top, left, bottom, right}
            border = new int[]{1, 1, 1, 1};
        }
    }
}
