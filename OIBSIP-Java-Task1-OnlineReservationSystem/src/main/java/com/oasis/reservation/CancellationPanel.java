package com.oasis.reservation;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class CancellationPanel extends JPanel {
    private final JTextField pnrField = new JTextField(16);
    private final JTextArea details = new JTextArea(10, 48);
    private String currentPnr;

    public CancellationPanel() {
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        top.add(new JLabel("PNR number:"));
        top.add(pnrField);
        JButton fetch = new JButton("Fetch Booking");
        fetch.addActionListener(e -> fetchBooking());
        top.add(fetch);
        add(top, BorderLayout.NORTH);

        details.setEditable(false);
        details.setLineWrap(true);
        details.setWrapStyleWord(true);
        add(new JScrollPane(details), BorderLayout.CENTER);

        JButton cancel = new JButton("Cancel Booking");
        cancel.addActionListener(e -> cancelBooking());
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(cancel);
        add(bottom, BorderLayout.SOUTH);
    }

    private void fetchBooking() {
        String pnr = pnrField.getText().trim().toUpperCase();
        if (pnr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a PNR number.");
            return;
        }
        String sql = "SELECT pnr, passenger_name, train_number, train_name, travel_class, journey_date, source_station, destination_station, status FROM reservations WHERE pnr = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, pnr);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    currentPnr = rs.getString("pnr");
                    details.setText("PNR: " + rs.getString("pnr") +
                            "\nPassenger: " + rs.getString("passenger_name") +
                            "\nTrain: " + rs.getString("train_name") + " (" + rs.getString("train_number") + ")" +
                            "\nClass: " + rs.getString("travel_class") +
                            "\nJourney date: " + rs.getDate("journey_date") +
                            "\nFrom: " + rs.getString("source_station") +
                            "\nTo: " + rs.getString("destination_station") +
                            "\nStatus: " + rs.getString("status"));
                } else {
                    currentPnr = null;
                    details.setText("");
                    JOptionPane.showMessageDialog(this, "No reservation found for that PNR.");
                }
            }
        } catch (SQLException ex) { showDbError(ex); }
    }

    private void cancelBooking() {
        if (currentPnr == null) {
            JOptionPane.showMessageDialog(this, "Fetch a valid booking before cancelling.");
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel booking " + currentPnr + "?",
                "Confirm cancellation", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) return;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement check = con.prepareStatement("SELECT status FROM reservations WHERE pnr = ?");
             PreparedStatement update = con.prepareStatement("UPDATE reservations SET status = 'CANCELLED' WHERE pnr = ?")) {
            check.setString(1, currentPnr);
            try (ResultSet rs = check.executeQuery()) {
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "Reservation no longer exists.");
                    currentPnr = null;
                    return;
                }
                if ("CANCELLED".equalsIgnoreCase(rs.getString("status"))) {
                    JOptionPane.showMessageDialog(this, "This booking is already cancelled.");
                    return;
                }
            }
            update.setString(1, currentPnr);
            update.executeUpdate();
            JOptionPane.showMessageDialog(this, "Booking cancelled successfully.");
            fetchBooking();
        } catch (SQLException ex) { showDbError(ex); }
    }

    private void showDbError(Exception ex) {
        JOptionPane.showMessageDialog(this, "Database operation failed:\n" + ex.getMessage(),
                "Database error", JOptionPane.ERROR_MESSAGE);
    }
}
