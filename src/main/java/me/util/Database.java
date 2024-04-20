package me.util;
import java.sql.*;
import org.postgresql.Driver;
public class Database {

    public static Connection connect_to_db(String dbname, String user, String pass) {

        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbname, user, pass);
            if (conn != null) {
                System.out.println("Connection Established");
            } else {
                System.out.println("Connection Failed");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return conn;
    }

    public static void createTable(java.sql.Connection conn, String recovery) {

        Statement statement;
        try {
            String query = "create table " + recovery + "(id SERIAL PRIMARY KEY NOT NULL, balance numeric(10, 2) default 0, social_security int4 NOT NULL UNIQUE);";
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("please enter your social security number");

        } catch (Exception e) {
            System.out.println("entering db information");
            System.out.println("please enter your social security number **1 to exit program**");
        }
    }

    public static void insertTNT(java.sql.Connection conn, String jugador, Date instante) {
        Statement statement;
        try {
            String query = "INSERT INTO tnt(jugador, instante) VALUES (" + jugador + "," + instante.toString() +");";
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("row tnt inserted");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void balance(java.sql.Connection conn, String recovery) throws SQLException {
        Statement statement;
        try {
            String query = "SELECT * FROM " + recovery + ";";
            statement = conn.createStatement();

            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                int id = result.getInt("id");
                String privatebalances = result.getString("balance");
                String socialsecurity = result.getString("social_security");

                System.out.printf("%d -bal %s -ss %s\n", id, privatebalances, socialsecurity);

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void delete(java.sql.Connection conn, int idm) {
        Statement statement;
        try {
            String query = "DELETE FROM RECOVERY WHERE id="+idm+";";
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("account with id " +idm+" deleted" + "\n");
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}