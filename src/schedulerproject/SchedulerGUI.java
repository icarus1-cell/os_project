package schedulerproject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SchedulerGUI extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JTextField quantumField;
    private JTextField numProcessesField;
    private JTextArea outputArea;
 
    public SchedulerGUI() {
        setTitle("CPU Scheduler - Round Robin vs Priority");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        getContentPane().setBackground(new Color(240, 240, 240));

    
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBorder(BorderFactory.createTitledBorder("Setup & Scenarios"));

     
        topPanel.add(new JLabel("Num Processes:"));
        numProcessesField = new JTextField("3", 4);
        topPanel.add(numProcessesField);

        JButton createTableBtn = new JButton("Create Table");
        createTableBtn.setBackground(new Color(230, 230, 230));
        topPanel.add(createTableBtn);

        topPanel.add(new JLabel(" | Quantum:"));
        quantumField = new JTextField("2", 3);
        topPanel.add(quantumField);

        add(topPanel, BorderLayout.NORTH);

  
        model = new DefaultTableModel(new String[]{"ID", "Arrival Time", "Burst Time", "Priority"}, 0);
        table = new JTable(model);
        table.setRowHeight(25);
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Edit Process Data"));
        add(tableScroll, BorderLayout.CENTER);


        outputArea = new JTextArea();
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        outputArea.setBackground(Color.WHITE); 
        outputArea.setForeground(Color.BLACK); 
        outputArea.setEditable(false);
        outputArea.setMargin(new Insets(10, 10, 10, 10));
        
        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputScroll.setPreferredSize(new Dimension(600, 0));
        outputScroll.setBorder(BorderFactory.createTitledBorder(null, "Simulation Results", 0, 0, null, Color.BLACK));
        add(outputScroll, BorderLayout.EAST);


        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        JButton runBtn = new JButton("RUN SIMULATION");
        runBtn.setPreferredSize(new Dimension(300, 50));
        runBtn.setBackground(new Color(0, 153, 76));
        runBtn.setForeground(Color.WHITE);
        runBtn.setFont(new Font("Arial", Font.BOLD, 16));
        
        JButton clearBtn = new JButton("Clear All");
        bottomPanel.add(runBtn);
        bottomPanel.add(clearBtn);
        add(bottomPanel, BorderLayout.SOUTH);



        createTableBtn.addActionListener(e -> {
            try {
                int n = Integer.parseInt(numProcessesField.getText());
                if (n <= 0) {
                    JOptionPane.showMessageDialog(this, "Number of processes must be greater than 0!");
                    return;
                }
                model.setRowCount(0);
                for (int i = 1; i <= n; i++) {
                    // Use default BT = 5
                    model.addRow(new Object[]{i, 0, 5, 1});
                }
                outputArea.setText("Table created for " + n + " processes.\n" +
                                  "Default values: AT=0, BT=5, Priority=1\n" +
                                  "You can edit any value. Remember:\n" +
                                  "- Burst Time must be > 0\n" +
                                  "- Process IDs must be unique!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number of processes.");
            }
        });

        runBtn.addActionListener(e -> runSimulation());

        clearBtn.addActionListener(e -> {
            model.setRowCount(0);
            outputArea.setText("");
        });
    }

    private void addRow(int id, int at, int bt, int pr) {
        model.addRow(new Object[]{id, at, bt, pr});
    }

    private void runSimulation() {
        try {
            int n = model.getRowCount();
            if (n == 0) {
                JOptionPane.showMessageDialog(this, "Table is empty!");
                return;
            }

            List<Process> rrList = new ArrayList<>();
            List<Process> prList = new ArrayList<>();
            
  
            Set<Integer> usedIds = new HashSet<>();

      
            for (int i = 0; i < n; i++) {
                try {
                    int id = Integer.parseInt(model.getValueAt(i, 0).toString().trim());
                    int at = Integer.parseInt(model.getValueAt(i, 1).toString().trim());
                    int bt = Integer.parseInt(model.getValueAt(i, 2).toString().trim());
                    int pr = Integer.parseInt(model.getValueAt(i, 3).toString().trim());
                    
    
                    if (usedIds.contains(id)) {
                        JOptionPane.showMessageDialog(this, 
                            "Invalid! Duplicate Process ID: P" + id + "\n" +
                            "Each process must have a unique ID.\n" +
                            "Please fix the ID values in the table.\nSimulation stopped.",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                        return; 
                    }
                    usedIds.add(id);
                    

                    if (bt <= 0) {
                        JOptionPane.showMessageDialog(this, 
                            "Invalid Burst Time for Process " + id + "! Burst Time must be greater than 0.\n" +
                            "Current value: " + bt + "\nSimulation stopped.",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                        return; // Stop execution immediately
                    }
                    
              
                    if (at < 0) {
                        JOptionPane.showMessageDialog(this, 
                            "Invalid Arrival Time for Process " + id + "! Arrival Time cannot be negative.\n" +
                            "Current value: " + at + "\nSimulation stopped.",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
   
                    if (pr < 0) {
                        JOptionPane.showMessageDialog(this, 
                            "Invalid Priority for Process " + id + "! Priority cannot be negative.\n" +
                            "Current value: " + pr + "\nSimulation stopped.",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    rrList.add(new Process(id, at, bt, pr));
                    prList.add(new Process(id, at, bt, pr));
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, 
                        "Invalid data format for row " + (i+1) + "!\n" +
                        "Please enter valid numbers.\n" +
                        "Error: " + ex.getMessage() + "\nSimulation stopped.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }


            int q;
            try {
                q = Integer.parseInt(quantumField.getText().trim());
                if (q <= 0) {
                    JOptionPane.showMessageDialog(this, 
                        "Invalid Quantum! Time Quantum must be greater than 0.\nSimulation stopped.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Invalid Quantum! Please enter a valid number greater than 0.\nSimulation stopped.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos, true, "UTF-8");
            
            PrintStream oldOut = System.out;
            System.setOut(ps); 

            System.out.println(">>> SIMULATION START <<<");
            
            Metrics rrM = RoundRobin.runRR(rrList, q);
            System.out.print("\n"); 
            System.out.flush();     

            System.out.println("\n" + "=".repeat(50));

            Metrics prM = PriorityScheduling.runPriority(prList);
            System.out.print("\n");
            System.out.flush();

            System.out.println("\n" + "=".repeat(50));
            Analysis.compare(rrM, prM);

            System.out.flush();
            System.setOut(oldOut); 

            String finalOutput = baos.toString("UTF-8");
            outputArea.setText(finalOutput);
            outputArea.setCaretPosition(0);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error during execution: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SchedulerGUI().setVisible(true));
    }
}