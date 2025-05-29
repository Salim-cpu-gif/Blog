import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.io.*;

public class EnregistrementEleve extends JFrame {
    JTextField txtId, txtNom, txtPrenom;
    JComboBox<String> cbSexe, cbClasse;
    JLabel lblPhoto;
    JTable table;
    DefaultTableModel model;
    byte[] photoData = null;
    Connection con;

    public EnregistrementEleve() {
        setTitle("Enregistrement Des Eleves");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblTitre = new JLabel("PARTIE D'ENREGISTREMENT");
        lblTitre.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitre.setBounds(240, 10, 400, 30);
        add(lblTitre);

        JLabel lblId = new JLabel("NUMERO ELEVE");
        lblId.setBounds(30, 60, 120, 25);
        add(lblId);

        txtId = new JTextField();
        txtId.setBounds(150, 60, 150, 25);
        add(txtId);

        JButton btnRecherche = new JButton("RECHERCHE");
        btnRecherche.setBounds(310, 60, 150, 25);
        add(btnRecherche);

        JLabel lblNom = new JLabel("NOM");
        lblNom.setBounds(30, 100, 120, 25);
        add(lblNom);

        txtNom = new JTextField();
        txtNom.setBounds(150, 100, 150, 25);
        add(txtNom);

        JLabel lblPrenom = new JLabel("PRENOM");
        lblPrenom.setBounds(30, 140, 120, 25);
        add(lblPrenom);

        txtPrenom = new JTextField();
        txtPrenom.setBounds(150, 140, 150, 25);
        add(txtPrenom);

        JLabel lblSexe = new JLabel("SEXE");
        lblSexe.setBounds(30, 180, 120, 25);
        add(lblSexe);

        cbSexe = new JComboBox<>(new String[]{"", "Masculin", "Feminin"});
        cbSexe.setBounds(150, 180, 150, 25);
        add(cbSexe);

        JLabel lblClasse = new JLabel("CLASSE");
        lblClasse.setBounds(30, 220, 120, 25);
        add(lblClasse);

        cbClasse = new JComboBox<>(new String[]{"", "6EME", "5EME", "4EME", "3EME", "2NDE", "1ERE L", "1ERE S", "TERMINALE"});
        cbClasse.setBounds(150, 220, 150, 25);
        add(cbClasse);

        lblPhoto = new JLabel();
        lblPhoto.setBounds(500, 60, 150, 150);
        lblPhoto.setBorder(BorderFactory.createLineBorder(Color.RED));
        add(lblPhoto);

        JButton btnChargerPhoto = new JButton("Charger Photo");
        btnChargerPhoto.setBounds(500, 220, 150, 25);
        add(btnChargerPhoto);

        JButton btnEnregistrer = new JButton("ENREGISTRER");
        btnEnregistrer.setBounds(150, 270, 150, 30);
        add(btnEnregistrer);

        JButton btnSupprimer = new JButton("SUPPRIMER");
        btnSupprimer.setBounds(310, 270, 150, 30);
        add(btnSupprimer);

        JButton btnNotes = new JButton("Ajouter Notes");
        btnNotes.setBounds(500, 270, 150, 30);
        add(btnNotes);

        JButton btnAbsences = new JButton("Ajouter Absences");
        btnAbsences.setBounds(500, 310, 150, 30);
        add(btnAbsences);

        model = new DefaultTableModel(new String[]{"Code", "Nom", "Prénom", "Sexe", "Classe"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 350, 720, 180);
        add(scrollPane);

        connecterDB();
        afficherTable();

        btnChargerPhoto.addActionListener(e -> chargerPhoto());
        btnEnregistrer.addActionListener(e -> enregistrer());
        btnSupprimer.addActionListener(e -> supprimer());
        btnRecherche.addActionListener(e -> rechercher());
        btnNotes.addActionListener(e -> ouvrirSaisieNote());
        btnAbsences.addActionListener(e -> ouvrirSaisieAbsence());
    }
// connexion avec la base de donnees 
    
    
    void connecterDB() {
    try {
        // Charger le driver JDBC (parfois optionnel selon version Java / JDBC)
        Class.forName("com.mysql.cj.jdbc.Driver");

        // URL de connexion : adresse, port, base, timezone (important pour éviter erreurs)
        String url = "jdbc:mysql://localhost:3306/gestion?serverTimezone=UTC";

        // Identifiants Wamp par défaut
        String user = "root";
        String password = ""; // mot de passe vide par défaut sur Wamp

        // Établir la connexion
        con = DriverManager.getConnection(url, user, password);

        System.out.println("Connexion réussie !");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Erreur de connexion : " + e.getMessage());
    }
}

    
    
    
    
    void afficherTable() {
        try {
            model.setRowCount(0);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM eleves");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("sexe"),
                        rs.getString("classe")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur affichage : " + e.getMessage());
        }
    }

    void chargerPhoto() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                FileInputStream fis = new FileInputStream(file);
                photoData = fis.readAllBytes();
                ImageIcon icon = new ImageIcon(new ImageIcon(photoData).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
                lblPhoto.setIcon(icon);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erreur chargement photo");
            }
        }
    }

    void enregistrer() {
        try {
            String sql = "INSERT INTO eleves(nom, prenom, sexe, classe, photo) VALUES(?,?,?,?,?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, txtNom.getText());
            pst.setString(2, txtPrenom.getText());
            pst.setString(3, cbSexe.getSelectedItem().toString());
            pst.setString(4, cbClasse.getSelectedItem().toString());
            pst.setBytes(5, photoData);
            pst.executeUpdate();
            afficherTable();
            JOptionPane.showMessageDialog(this, "Enregistrement réussi");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur d'enregistrement : " + e.getMessage());
        }
    }

    void supprimer() {
        try {
            String id = txtId.getText();
            if (id.isEmpty()) return;
            PreparedStatement pst = con.prepareStatement("DELETE FROM eleves WHERE id=?");
            pst.setInt(1, Integer.parseInt(id));
            pst.executeUpdate();
            afficherTable();
            JOptionPane.showMessageDialog(this, "Suppression réussie");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur de suppression : " + e.getMessage());
        }
    }

    void rechercher() {
        try {
            String id = txtId.getText();
            if (id.isEmpty()) return;
            PreparedStatement pst = con.prepareStatement("SELECT * FROM eleves WHERE id=?");
            pst.setInt(1, Integer.parseInt(id));
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                txtNom.setText(rs.getString("nom"));
                txtPrenom.setText(rs.getString("prenom"));
                cbSexe.setSelectedItem(rs.getString("sexe"));
                cbClasse.setSelectedItem(rs.getString("classe"));

                byte[] img = rs.getBytes("photo");
                if (img != null) {
                    ImageIcon icon = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
                    lblPhoto.setIcon(icon);
                    photoData = img;
                } else {
                    lblPhoto.setIcon(null);
                    photoData = null;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Élève non trouvé");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur recherche : " + e.getMessage());
        }
    }

    void ouvrirSaisieNote() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int eleveId = (int) model.getValueAt(selectedRow, 0);
            new SaisieNote(this, eleveId, con);
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un élève dans la liste.");
        }
    }

    void ouvrirSaisieAbsence() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int eleveId = (int) model.getValueAt(selectedRow, 0);
            new SaisieAbsence(this, eleveId, con);
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un élève dans la liste.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EnregistrementEleve().setVisible(true));
    }

    public class SaisieNote extends JDialog {
        public SaisieNote(JFrame parent, int eleveId, Connection con) {
            super(parent, "Ajouter Note", true);
            setLayout(new GridLayout(3, 2, 10, 10));
            setSize(300, 200);
            setLocationRelativeTo(parent);

            JTextField txtMatiere = new JTextField();
            JTextField txtNote = new JTextField();
            JButton btnValider = new JButton("Valider");

            add(new JLabel("Matière:"));
            add(txtMatiere);
            add(new JLabel("Note:"));
            add(txtNote);
            add(new JLabel());
            add(btnValider);

            btnValider.addActionListener(e -> {
                try {
                    PreparedStatement pst = con.prepareStatement("INSERT INTO notes (eleve_id, matiere, note) VALUES (?, ?, ?)");
                    pst.setInt(1, eleveId);
                    pst.setString(2, txtMatiere.getText());
                    pst.setDouble(3, Double.parseDouble(txtNote.getText()));
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Note ajoutée !");
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
                }
            });

            setVisible(true);
        }
    }

    public class SaisieAbsence extends JDialog {
        public SaisieAbsence(JFrame parent, int eleveId, Connection con) {
            super(parent, "Ajouter Absence", true);
            setLayout(new GridLayout(3, 2, 10, 10));
            setSize(350, 200);
            setLocationRelativeTo(parent);

            JTextField txtDate = new JTextField("2025-05-15");
            JTextField txtMotif = new JTextField();
            JButton btnValider = new JButton("Valider");

            add(new JLabel("Date d'absence (AAAA-MM-JJ):"));
            add(txtDate);
            add(new JLabel("Motif:"));
            add(txtMotif);
            add(new JLabel());
            add(btnValider);

            btnValider.addActionListener(e -> {
                try {
                    String date = txtDate.getText().trim();
                    String motif = txtMotif.getText().trim();

                    if (date.isEmpty() || motif.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires !");
                        return;
                    }

                    PreparedStatement pst = con.prepareStatement("INSERT INTO absences (eleve_id, date_absence, motif) VALUES (?, ?, ?)");
                    pst.setInt(1, eleveId);
                    pst.setDate(2, java.sql.Date.valueOf(date));
                    pst.setString(3, motif);
                    pst.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Absence enregistrée !");
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
                }
            });

            setVisible(true);
        }
    }
}


