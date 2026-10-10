package com.oasis.reservation;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    public DashboardFrame(int userId, String username) {
        setTitle("OIBSIP | Reservation Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(850, 600);
        setLocationRelativeTo(null);

        JLabel heading = new JLabel("Welcome, " + username + " — Online Reservation System");
        heading.setFont(new Font("SansSerif", Font.BOLD, 18));
        heading.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        add(heading, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Book Ticket", new ReservationPanel(userId));
        tabs.addTab("Cancel / Find Booking", new CancellationPanel());
        add(tabs, BorderLayout.CENTER);

        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(logout);
        add(bottom, BorderLayout.SOUTH);
    }
}
