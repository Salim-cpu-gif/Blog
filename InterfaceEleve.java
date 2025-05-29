import javax.swing.*;
import java.sql.*;

public class InterfaceEleve extends JFrame {
    int eleveId;

    public InterfaceEleve(int eleveId) {
        this.eleveId = eleveId;
        setTitle("Bienvenue cher élève");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        add(scroll);

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gestion", "root", "")) {
            PreparedStatement pst1 = con.prepareStatement("SELECT matiere, note FROM notes WHERE eleve_id=?");
            pst1.setInt(1, eleveId);
            ResultSet rs1 = pst1.executeQuery();

            area.append("Notes :\n");
            while (rs1.next()) {
                area.append(rs1.getString("matiere") + ": " + rs1.getDouble("note") + "\n");
            }

            area.append("\nAbsences :\n");
            PreparedStatement pst2 = con.prepareStatement("SELECT date_absence, motif FROM absences WHERE eleve_id=?");
            pst2.setInt(1, eleveId);
            ResultSet rs2 = pst2.executeQuery();

            while (rs2.next()) {
                area.append(rs2.getString("date_absence") + " - " + rs2.getString("motif") + "\n");
            }
        } catch (SQLException e) {
            area.setText("Erreur de chargement : " + e.getMessage());
        }
    }
}
