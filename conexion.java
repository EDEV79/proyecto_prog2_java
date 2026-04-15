import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Clase para manejar la conexión a la base de datos
public class conexion {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/profin_db"
        + "?useUnicode=true"
        + "&characterEncoding=UTF-8"
        + "&useSSL=false"
        + "&allowPublicKeyRetrieval=true"
        + "&serverTimezone=UTC"
        + "&connectTimeout=2000"
        + "&socketTimeout=3000";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver JDBC de MySQL.", e);
        }

        DriverManager.setLoginTimeout(5);
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
} 
