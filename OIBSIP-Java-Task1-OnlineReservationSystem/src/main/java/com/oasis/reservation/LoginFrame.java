package com.oasis.reservation;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginFrame extends JFrame {
    private final JTextField username = new JTextField(18);
    private final JPasswordField password = new JPasswordField(18);

    public LoginFrame() {
        setTitle("OIBSIP | Online Reservation System - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(430, 300);
        setLocationRelativeTo(null);

        JPanel outer = new JPanel(new BorderLayout(12, 12));
        outer.setBorder(BorderFactory.createEmptyBorder(22, 28, 22, 28));
        JLabel title = new JLabel("Online Reservation System", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 21));
        outer.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(2, 2, 10, 12));
        form.add(new JLabel("Username:")); form.add(username);
        form.add(new JLabel("Password:")); form.add(password);
        outer.add(form, BorderLayout.CENTER);

        JButton login = new JButton("Login");
        JButton exit = new JButton("Exit");
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttons.add(login); buttons.add(exit);
        outer.add(buttons, BorderLayout.SOUTH);
        login.addActionListener(e -> authenticate());
        password.addActionListener(e -> authenticate());
        exit.addActionListener(e -> dispose());
        setContentPane(outer);
    }

    private void authenticate() {
        String user = username.getText().trim();
        String pass = new String(password.getPassword());
        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter both username and password.");
            return;
        }
        String sql = "SELECT id, username FROM users WHERE username = ? AND password = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user);
            ps.setString(2, pass);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    dispose();
                    new DashboardFrame(rs.getInt("id"), rs.getString("username")).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password.",
                            "Login failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Could not connect to the database. Check MySQL and database.sql.\n" + ex.getMessage(),
                    "Database error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
