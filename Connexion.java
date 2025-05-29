import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Connexion extends JFrame {
    JTextField txtLogin;
    JPasswordField txtPassword;
    String role;

    public Connexion(String role) {
        this.role = role;
        setTitle("Connexion " + role.toUpperCase());
        setSize(350, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        txtLogin = new JTextField();
        txtPassword = new JPasswordField();

        add(new JLabel("Login :"));
        add(txtLogin);
        add(new JLabel("Mot de passe :"));
        add(txtPassword);

        JButton btnConnecter = new JButton("Se connecter");
        add(new JLabel());
        add(btnConnecter);

        btnConnecter.addActionListener(e -> verifierConnexion());
    }

    void verifierConnexion() {
        String login = txtLogin.getText();
        String password = new String(txtPassword.getPassword());

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gestion", "root", "")) {
            PreparedStatement pst = con.prepareStatement("SELECT * FROM utilisateurs WHERE login=? AND mot_de_passe=? AND role=?");
            pst.setString(1, login);
            pst.setString(2, password);
            pst.setString(3, role);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                dispose();
                switch (role) {
                    case "admin" -> new InterfaceAdmin().setVisible(true);
                    case "enseignant" -> new InterfaceEnseignant().setVisible(true);
                    case "eleve" -> new InterfaceEleve(rs.getInt("eleve_id")).setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Identifiants incorrects !");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur SQL : " + ex.getMessage());
        }
    }
}
