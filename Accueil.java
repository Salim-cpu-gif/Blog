import javax.swing.*;
import java.awt.*;

public class Accueil extends JFrame {
    public Accueil() {
        setTitle("Accueil - Gestion des Élèves");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1, 10, 10));

        JLabel lblBienvenue = new JLabel("Bienvenue dans le système", JLabel.CENTER);
        lblBienvenue.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblBienvenue);

        JButton btnAdmin = new JButton("Connexion Administrateur");
        JButton btnEnseignant = new JButton("Connexion Enseignant");
        JButton btnEleve = new JButton("Connexion Élève");

        add(btnAdmin);
        add(btnEnseignant);
        add(btnEleve);
        btnAdmin.addActionListener(e -> new Connexion("admin").setVisible(true));
          btnEnseignant.addActionListener(e -> new Connexion("enseignant").setVisible(true));
        btnEleve.addActionListener(e -> new Connexion("eleve").setVisible(true));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Accueil().setVisible(true));
    }
}
