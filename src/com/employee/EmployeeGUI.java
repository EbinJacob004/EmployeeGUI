package com.employee;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class EmployeeGUI {

    JFrame f;
    CardLayout cl;
    JPanel mainPanel;

    Connection con;

    public EmployeeGUI() {

        f = new JFrame("Employee Management System");
        f.setSize(500, 500);

        cl = new CardLayout();
        mainPanel = new JPanel(cl);

        // DB Connection
        try {
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/company", "root", "ebin@232955");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Panels
        mainPanel.add(homePanel(), "home");
        mainPanel.add(addPanel(), "add");
        mainPanel.add(deletePanel(), "delete");
        mainPanel.add(updatePanel(), "update");
        mainPanel.add(viewPanel(), "view");

        f.add(mainPanel);
        f.setVisible(true);
    }

    // 🏠 HOME PANEL
    JPanel homePanel() {
        JPanel p = new JPanel();
        p.setLayout(null);

        JLabel heading = new JLabel("EMPLOYEE MANAGEMENT SYSTEM");
        heading.setBounds(100, 30, 300, 30);
        p.add(heading);

        JButton add = new JButton("Add");
        add.setBounds(150, 100, 200, 30);
        p.add(add);

        JButton del = new JButton("Delete");
        del.setBounds(150, 150, 200, 30);
        p.add(del);

        JButton upd = new JButton("Update");
        upd.setBounds(150, 200, 200, 30);
        p.add(upd);

        JButton view = new JButton("View");
        view.setBounds(150, 250, 200, 30);
        p.add(view);

        add.addActionListener(e -> cl.show(mainPanel, "add"));
        del.addActionListener(e -> cl.show(mainPanel, "delete"));
        upd.addActionListener(e -> cl.show(mainPanel, "update"));
        view.addActionListener(e -> cl.show(mainPanel, "view"));

        return p;
    }

    // ➕ ADD PANEL
    JPanel addPanel() {
        JPanel p = new JPanel();
        p.setLayout(null);

        JTextField name = new JTextField();
        JTextField salary = new JTextField();

        p.add(new JLabel("Name")).setBounds(50, 50, 100, 30);
        p.add(name).setBounds(150, 50, 150, 30);

        p.add(new JLabel("Salary")).setBounds(50, 100, 100, 30);
        p.add(salary).setBounds(150, 100, 150, 30);

        JButton addBtn = new JButton("Add");
        addBtn.setBounds(150, 150, 100, 30);
        p.add(addBtn);

        JButton back = new JButton("Back");
        back.setBounds(150, 200, 100, 30);
        p.add(back);

        addBtn.addActionListener(e -> {
            try {
                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO employee(name,salary) VALUES (?,?)");
                ps.setString(1, name.getText());
                ps.setDouble(2, Double.parseDouble(salary.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(f, "Added!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        back.addActionListener(e -> cl.show(mainPanel, "home"));

        return p;
    }

    // ❌ DELETE PANEL
    JPanel deletePanel() {
        JPanel p = new JPanel();
        p.setLayout(null);

        JTextField id = new JTextField();

        p.add(new JLabel("ID")).setBounds(50, 50, 100, 30);
        p.add(id).setBounds(150, 50, 150, 30);

        JButton delBtn = new JButton("Delete");
        delBtn.setBounds(150, 100, 100, 30);
        p.add(delBtn);

        JButton back = new JButton("Back");
        back.setBounds(150, 150, 100, 30);
        p.add(back);

        delBtn.addActionListener(e -> {
            try {
                PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM employee WHERE id=?");
                ps.setInt(1, Integer.parseInt(id.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(f, "Deleted!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        back.addActionListener(e -> cl.show(mainPanel, "home"));

        return p;
    }

    // 🔄 UPDATE PANEL
    JPanel updatePanel() {
        JPanel p = new JPanel();
        p.setLayout(null);

        JTextField id = new JTextField();
        JTextField salary = new JTextField();

        p.add(new JLabel("ID")).setBounds(50, 50, 100, 30);
        p.add(id).setBounds(150, 50, 150, 30);

        p.add(new JLabel("Salary")).setBounds(50, 100, 100, 30);
        p.add(salary).setBounds(150, 100, 150, 30);

        JButton updBtn = new JButton("Update");
        updBtn.setBounds(150, 150, 100, 30);
        p.add(updBtn);

        JButton back = new JButton("Back");
        back.setBounds(150, 200, 100, 30);
        p.add(back);

        updBtn.addActionListener(e -> {
            try {
                PreparedStatement ps = con.prepareStatement(
                    "UPDATE employee SET salary=? WHERE id=?");
                ps.setDouble(1, Double.parseDouble(salary.getText()));
                ps.setInt(2, Integer.parseInt(id.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(f, "Updated!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        back.addActionListener(e -> cl.show(mainPanel, "home"));

        return p;
    }

    // 📊 VIEW PANEL
    JPanel viewPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());

        JTextArea area = new JTextArea();
        JButton back = new JButton("Back");

        p.add(new JScrollPane(area), BorderLayout.CENTER);
        p.add(back, BorderLayout.SOUTH);

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM employee");

            while (rs.next()) {
                area.append(rs.getInt(1) + " | " +
                            rs.getString(2) + " | " +
                            rs.getDouble(3) + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        back.addActionListener(e -> cl.show(mainPanel, "home"));

        return p;
    }

    public static void main(String[] args) {
        new EmployeeGUI();
    }
}