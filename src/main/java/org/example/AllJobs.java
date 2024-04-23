package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AllJobs {
    JFrame f;
    public JComboBox<String> specialBox;
    public JScrollPane alljobs;
    private String selectedspecialization="All";
    private final DefaultTableModel allJobModel;
    private JButton back;

    AllJobs() {
        f = new JFrame("All Jobs");
        f.setSize(1300, 700);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.getContentPane().setBackground(new Color(10, 50, 60));
        f.setLayout(null);
        String[] boxList = {"All","Web Development", "Data Science", "Machine Learning", "Cyber Security", "Cloud Computing", "Internet of Things"};
        specialBox = new JComboBox<>(boxList);
        specialBox.setBounds(420, 50, 400, 55);
        specialBox.addActionListener(e -> {
            selectedspecialization = (String) specialBox.getSelectedItem();
            if (selectedspecialization != null) {
                fetchAndDisplayJobs(selectedspecialization);
            }
        });

        JTable allJobTable = new JTable();
        allJobModel = new DefaultTableModel();
        String[] colname = {"Company Name", "Company Description", "Company Requirement"};
        allJobModel.setColumnIdentifiers(colname);
        allJobTable.setModel(allJobModel);
        TableColumn column = allJobTable.getColumnModel().getColumn(1); // Company Description column
        column.setPreferredWidth(600); // Resize to fit content
        alljobs = new JScrollPane(allJobTable);
        alljobs.setBounds(100, 150, 1100, 700);
        back=new JButton("Back");
        back.setBounds(20, 10, 100, 50);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginPage();
            }
        });
        allJobTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(new Color(194, 77, 77));
                        c.setForeground(Color.WHITE);
                    } else {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.red);
                    }
                }
                return c;
            }
        });
        allJobTable.setRowHeight(30);
        allJobTable.setFont(new Font("Arial", Font.PLAIN, 14));
        f.add(specialBox);
        f.add(alljobs);
        f.add(back);
        f.setVisible(true);
    }

    private void fetchAndDisplayJobs(String selectedSpecialization) {
        try {
            String url = "jdbc:mysql://localhost:3306/trial";
            String username = "root";
            String password = "@shreya123";

            Connection conn = DriverManager.getConnection(url, username, password);

            String query;
            if (selectedSpecialization.equals("All")) {
                query = "SELECT com_name, com_desp, com_req FROM company";
            } else {
                query = "SELECT com_name, com_desp, com_req FROM company WHERE com_req LIKE '%" + selectedSpecialization + "%'";
            }

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            allJobModel.setRowCount(0); // Clear the existing rows

            while (rs.next()) {
                String companyName = rs.getString("com_name");
                String companyDescription = rs.getString("com_desp");
                String companyRequirement = rs.getString("com_req");
                allJobModel.addRow(new Object[]{companyName, companyDescription, companyRequirement});
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        new AllJobs().fetchAndDisplayJobs("All");
    }
}