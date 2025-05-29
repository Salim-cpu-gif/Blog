/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Eleve;

import java.sql.DriverManager;

/**
 *
 * @author futuretech
 */
public class connectionEleve {

    connectionEleve cn;

    public connectionEleve() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
     cn = (connectionEleve) DriverManager.getConnection("jdbc :mysql ://localhost/livre", "root", " ");
            System.out.println("Connection Etablie");
        } catch (Exception e) {
            System.out.println("Erreur de connection");
            e.printStackTrace();
        }
    }

    public connectionEleve maconnection() {
        return (connectionEleve) cn;

    }
}
