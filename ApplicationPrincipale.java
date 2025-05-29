import javax.swing.*;

public class ApplicationPrincipale extends JFrame {
    public ApplicationPrincipale() {
        setTitle("Gestion des élèves - Application");
        setSize(850, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane onglets = new JTabbedPane();

        // Ajout de l'onglet avec notre panel modulaire
        PanelEnregistrementEleve panelEleves = new PanelEnregistrementEleve();
        onglets.addTab("Enregistrement élèves", panelEleves);

        // Tu peux ajouter d'autres onglets ici, par exemple :
        // onglets.addTab("Notes", new PanelNotes());
        // onglets.addTab("Absences", new PanelAbsences());

        add(onglets);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ApplicationPrincipale().setVisible(true);
        });
    }
}
