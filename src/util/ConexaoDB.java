package util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDB {
    private static final String URL = valorAmbiente("BARBEARIA_DB_URL", "jdbc:mysql://localhost:3306/barbearia_db");
    private static final String USUARIO = valorAmbiente("BARBEARIA_DB_USUARIO", "root");
    private static final String SENHA = valorAmbiente("BARBEARIA_DB_SENHA", "Fimose69#");

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL,USUARIO,SENHA);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar com o banco de dados.");
            e.printStackTrace();
            return null;
        }
    }

    private static String valorAmbiente(String nome, String valorPadrao) {
        String valor = System.getenv(nome);
        return valor == null || valor.isBlank() ? valorPadrao : valor;
    }
}
