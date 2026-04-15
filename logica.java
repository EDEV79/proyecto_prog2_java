import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class logica {
    // Método para insertar un nuevo producto en la base de datos
    public static void insertar(String nombre, String producto, double precio, int cantidad) throws SQLException {
        double total = precio * cantidad;

        String sql = "INSERT INTO producto (nombre_cliente, producto, precio, cantidad, total) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, producto);
            ps.setDouble(3, precio);
            ps.setInt(4, cantidad);
            ps.setDouble(5, total);

            ps.executeUpdate();
        }
    }

    // Consulta los productos y retorna filas listas para renderizar en la tabla
    public static List<Object[]> obtenerRegistros() throws SQLException {
        List<Object[]> filas = new ArrayList<>();

        try (Connection con = conexion.getConexion()) {
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT * FROM producto ORDER BY id DESC")) {

                while (rs.next()) {
                    filas.add(new Object[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("nombre_cliente"),
                        rs.getString("producto"),
                        String.format("%.2f", rs.getDouble("precio")),
                        String.valueOf(rs.getInt("cantidad")),
                        String.format("%.2f", rs.getDouble("total")),
                        "Editar",
                        "Eliminar"
                    });
                }
            }
        }
        return filas;
    }

    // Método para listar los productos en la tabla
    public static int listar(javax.swing.table.DefaultTableModel modelo) throws SQLException {
        List<Object[]> filas = obtenerRegistros();
        modelo.setRowCount(0);
        for (Object[] fila : filas) {
            modelo.addRow(fila);
        }
        return filas.size();
    }

    // Método para eliminar un producto de la base de datos
    public static void eliminar(int id) throws SQLException {
        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement("DELETE FROM producto WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // Método para actualizar un producto en la base de datos
    public static void actualizar(int id, String nombre, String producto, double precio, int cantidad) throws SQLException {
        double total = precio * cantidad;

        String sql = "UPDATE producto SET nombre_cliente=?, producto=?, precio=?, cantidad=?, total=? WHERE id=?";
        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, producto);
            ps.setDouble(3, precio);
            ps.setInt(4, cantidad);
            ps.setDouble(5, total);
            ps.setInt(6, id);

            ps.executeUpdate();
        }
    }
}