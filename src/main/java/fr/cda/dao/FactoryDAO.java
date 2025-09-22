package fr.cda.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class FactoryDAO pour Create Connexion MySQL
 */
public class FactoryDAO {

    private String DB_URL;
    private String DB_USER;
    private String DB_PASSWORD;

    public FactoryDAO(String DB_URL, String DB_USER, String DB_PASSWORD) {
        this.DB_URL = DB_URL;
        this.DB_USER = DB_USER;
        this.DB_PASSWORD = DB_PASSWORD;
    }

    /**
     * @return instance de connexion
     */
    public static FactoryDAO getInstance() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");


        } catch (ClassNotFoundException e) {

            e.printStackTrace();
        }
        FactoryDAO instance = new FactoryDAO("jdbc:mysql://localhost:3306/StockManegmant", "root", "root");

        return instance;
    }

    /**
     * @return Connexion
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * Methode pour fermer la connexion
     *
     * @param connction
     * @throws SQLException
     */
    public static void closeConnection(Connection connction) throws SQLException {

        try {
            if (connction != null) {
                connction.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
