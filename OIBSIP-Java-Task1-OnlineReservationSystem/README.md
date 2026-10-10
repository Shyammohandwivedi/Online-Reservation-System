# OIBSIP Java Development — Task 1: Online Reservation System

A Java Swing desktop application for logging in, booking a train reservation, generating a PNR, finding a booking by PNR, and cancelling a reservation.

## Tech stack
- Java 17
- Java Swing
- JDBC
- MySQL 8+
- Maven

## Requirements
Install JDK 17+, Maven, and MySQL. Run the SQL setup script in MySQL Workbench or the MySQL command line:

```sql
SOURCE /absolute/path/to/database/database.sql;
```

Alternatively, open `database/database.sql` in MySQL Workbench and execute it.

## Run
From the project root:

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.oasis.reservation.App"
```

If the `exec:java` command is not available in your Maven setup, run `com.oasis.reservation.App` from your IDE after importing the folder as a Maven project.

## Database configuration
Default local settings:
- URL: `jdbc:mysql://localhost:3306/oibsip_reservation?serverTimezone=UTC`
- Username: `root`
- Password: empty

Override these with environment variables `DB_URL`, `DB_USER`, and `DB_PASSWORD` if your MySQL setup differs.

## Demo login
- Username: `admin`
- Password: `admin123`

This is a practice/demo credential, not suitable for production. The starter project stores demo passwords directly to keep the assignment setup simple; a production version should hash passwords and use proper session/security controls.

## Features
- Login form and invalid-login handling
- Reservation form with train-name lookup
- Unique PNR generation
- Booking confirmation dialog
- PNR-based booking lookup
- Cancellation confirmation and status update
- Required-field, train-number, and date validation
- Prepared statements for SQL operations

## GitHub submission structure
Create a public GitHub repository named exactly `OIBSIP`, then place this project in:

`OIBSIP/JavaDev-Task1-OnlineReservationSystem/`

Include this README, source code, SQL script, and screenshots. Record a demo showing the application working end-to-end. Start the video with a 2-second title card containing your full name, assigned track, and task title. Post/link the demo on LinkedIn with `#oasisinfobyte`, and follow the submission form instructions in your internship email.

## Suggested demo flow
1. Log in using the demo account.
2. Book a ticket using sample train number `12001`.
3. Note the generated PNR.
4. Open the cancellation tab and fetch the booking.
5. Cancel it and show the updated `CANCELLED` status.

## Important
This is an educational starter project. It does not connect to live railway systems or book real tickets.
