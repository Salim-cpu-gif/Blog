import java.awt.GridLayout;
import javax.swing.*;

public class InterfaceEnseignant extends JFrame {
    public InterfaceEnseignant() {
        setTitle("Bienvenue M./Mme l'Enseignant");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton btnAjouterNotes = new JButton("Ajouter Notes");
        JButton btnAjouterAbsences = new JButton("Ajouter Absences");

        // Tu pourras ici afficher la liste des élèves pour les sélectionner
        // Exemple simple à adapter :
        btnAjouterNotes.addActionListener(e -> JOptionPane.showMessageDialog(this, "À connecter à SaisieNote"));
        btnAjouterAbsences.addActionListener(e -> JOptionPane.showMessageDialog(this, "À connecter à SaisieAbsence"));

        setLayout(new GridLayout(3, 1, 10, 10));
        add(new JLabel("Que souhaitez-vous faire ?", JLabel.CENTER));
        add(btnAjouterNotes);
        add(btnAjouterAbsences);
    }
}
