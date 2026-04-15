import java.sql.Connection;
import java.sql.DriverManager;

// Clase para manejar la conexión a la base de datos
public class conexion {

    public static Connection getConexion() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/profin_db?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true",
                "root", ""
            );
            //mensaje de error de coneccion a la base de datos
        } catch (Exception e) {
            System.out.println("Error al conectar a la base de datos: " + e);
        }
        return con;
    }
} 
