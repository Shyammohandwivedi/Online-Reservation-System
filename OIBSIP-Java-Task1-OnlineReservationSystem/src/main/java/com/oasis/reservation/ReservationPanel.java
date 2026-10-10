package com.oasis.reservation;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.UUID;

public class ReservationPanel extends JPanel {
    private final int userId;
    private final JTextField passenger = new JTextField(22);
    private final JTextField trainNumber = new JTextField(22);
    private final JTextField trainName = new JTextField(22);
    private final JComboBox<String> travelClass = new JComboBox<>(
            new String[]{"Sleeper", "AC 3 Tier", "AC 2 Tier", "AC First Class", "Chair Car"});
    private final JTextField journeyDate = new JTextField(LocalDate.now().plusDays(1).toString(), 22);
    private final JTextField source = new JTextField(22);
    private final JTextField destination = new JTextField(22);

    public ReservationPanel(int userId) {
        this.userId = userId;
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        JLabel title = new JLabel("Book a Reservation");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(7, 2, 12, 12));
        form.add(new JLabel("Passenger name:")); form.add(passenger);
        form.add(new JLabel("Train number:")); form.add(trainNumber);
        form.add(new JLabel("Train name:")); form.add(trainName);
        form.add(new JLabel("Class:")); form.add(travelClass);
        form.add(new JLabel("Journey date (YYYY-MM-DD):")); form.add(journeyDate);
        form.add(new JLabel("Source station:")); form.add(source);
        form.add(new JLabel("Destination station:")); form.add(destination);
        add(form, BorderLayout.CENTER);

        JButton lookup = new JButton("Find Train Name");
        lookup.addActionListener(e -> lookupTrain());
        JButton book = new JButton("Book Ticket");
        book.addActionListener(e -> bookTicket());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttons.add(lookup); buttons.add(book);
        add(buttons, BorderLayout.SOUTH);
    }

    private void lookupTrain() {
        String number = trainNumber.getText().trim();
        if (number.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a train number first.");
            return;
        }
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT train_name FROM trains WHERE train_number = ?")) {
            ps.setString(1, number);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) trainName.setText(rs.getString("train_name"));
                else {
                    trainName.setText("");
                    JOptionPane.showMessageDialog(this, "Train number not found. Add it to the trains table first.");
                }
            }
        } catch (SQLException ex) { showDbError(ex); }
    }

    private void bookTicket() {
        String p = passenger.getText().trim();
        String tn = trainNumber.getText().trim();
        String name = trainName.getText().trim();
        String dateText = journeyDate.getText().trim();
        String from = source.getText().trim();
        String to = destination.getText().trim();

        if (p.isEmpty() || tn.isEmpty() || name.isEmpty() || dateText.isEmpty() || from.isEmpty() || to.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in every required field.");
            return;
        }
        if (!tn.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Train number must contain digits only.");
            return;
        }
        LocalDate date;
        try {
            date = LocalDate.parse(dateText);
            if (date.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "Journey date cannot be in the past.");
                return;
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Use the date format YYYY-MM-DD.");
            return;
        }
        String pnr = UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
        String sql = "INSERT INTO reservations (pnr, user_id, passenger_name, train_number, train_name, travel_class, journey_date, source_station, destination_station, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'BOOKED')";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, pnr);
            ps.setInt(2, userId);
            ps.setString(3, p);
            ps.setString(4, tn);
            ps.setString(5, name);
            ps.setString(6, (String) travelClass.getSelectedItem());
            ps.setDate(7, Date.valueOf(date));
            ps.setString(8, from);
            ps.setString(9, to);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this,
                    "Reservation successful!\nPNR: " + pnr + "\nPassenger: " + p +
                    "\nTrain: " + name + " (" + tn + ")\nDate: " + date +
                    "\nRoute: " + from + " → " + to,
                    "Booking Confirmation", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) { showDbError(ex); }
    }

    private void showDbError(Exception ex) {
        JOptionPane.showMessageDialog(this, "Database operation failed:\n" + ex.getMessage(),
                "Database error", JOptionPane.ERROR_MESSAGE);
    }
}
