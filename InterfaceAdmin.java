import javax.swing.*;

public class InterfaceAdmin extends JFrame {
    public InterfaceAdmin() {
        setTitle("Bienvenue M./Mme l'Administrateur");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton btnGestionEleves = new JButton("Gérer les élèves");
        btnGestionEleves.addActionListener(e -> new EnregistrementEleve().setVisible(true));

        add(btnGestionEleves);
    }
}
