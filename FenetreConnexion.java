import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FenetreConnexion extends JFrame {
    private JTextField txtLogin;
    private JPasswordField txtPassword;
    private String roleAttendu;
    private Connection con;

    public FenetreConnexion(String roleAttendu) {
        this.roleAttendu = roleAttendu;
        setTitle("Connexion " + roleAttendu);
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 10, 10));

        add(new JLabel("Login :"));
        txtLogin = new JTextField();
        add(txtLogin);

        add(new JLabel("Mot de passe :"));
        txtPassword = new JPasswordField();
        add(txtPassword);

        JButton btnConnexion = new JButton("Se connecter");
        add(btnConnexion);

        JButton btnAnnuler = new JButton("Annuler");
        add(btnAnnuler);

        btnConnexion.addActionListener(e -> verifierConnexion());
        btnAnnuler.addActionListener(e -> System.exit(0));

        connecterDB();
    }

    private void connecterDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/gestion?serverTimezone=UTC";
            String user = "root";
            String password = "";
            con = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion DB OK");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur connexion DB : " + e.getMessage());
        }
    }

    private void verifierConnexion() {
        String login = txtLogin.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();

        if (login.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs !");
            return;
        }

        try {
            String sql = "SELECT role FROM utilisateurs WHERE login=? AND password=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, login);
            pst.setString(2, pass); // idéalement, hacher le mot de passe en vrai projet
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String roleBDD = rs.getString("role");
                if (!roleBDD.equalsIgnoreCase(roleAttendu)) {
                    JOptionPane.showMessageDialog(this, "Accès refusé pour ce rôle.");
                    return;
                }

                JOptionPane.showMessageDialog(this, "Connexion réussie en tant que " + roleBDD);
                ouvrirInterfaceSelonRole(roleBDD, login);

                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Login ou mot de passe incorrect !");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la connexion : " + e.getMessage());
        }
    }

    private void ouvrirInterfaceSelonRole(String role, String login) {
        switch (role.toLowerCase()) {
            case "administrateur":
                new InterfaceAdmin().setVisible(true);
                break;
            case "enseignant":
                new InterfaceEnseignant(login).setVisible(true);
                break;
            case "eleve":
                new InterfaceEleve(login).setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Rôle non reconnu !");
        }
    }

    // Classes des interfaces à créer ensuite (exemples)
    public class InterfaceAdmin extends JFrame {
        public InterfaceAdmin() {
            setTitle("Espace Administrateur");
            setSize(600, 400);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            JLabel label = new JLabel("Bienvenue Admin, gérez l'application ici.");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            add(label);
            // Ici tu peux intégrer ta classe EnregistrementEleve ou autres fonctionnalités
        }
    }

    public class InterfaceEnseignant extends JFrame {
        public InterfaceEnseignant(String login) {
            setTitle("Espace Enseignant");
            setSize(600, 400);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            JLabel label = new JLabel("Bienvenue Enseignant " + login + ". Ajoutez / supprimez notes et absences.");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            add(label);
            // Ajoute ici les fonctionnalités notes et absences pour enseignants
        }
    }

    public class InterfaceEleve extends JFrame {
        public InterfaceEleve(String login) {
            setTitle("Espace Élève");
            setSize(600, 400);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            JLabel label = new JLabel("Bienvenue Élève " + login + ". Consultez vos notes et absences.");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            add(label);
            // Ajoute ici tableau de bord notes / absences pour élèves
        }
    }
}
