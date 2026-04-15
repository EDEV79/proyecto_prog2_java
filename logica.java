import java.sql.*;

public class logica {
    // Método para insertar un nuevo producto en la base de datos
    public static void insertar(String nombre, String producto, double precio, int cantidad) {
        try {
            Connection con = conexion.getConexion();
            double total = precio * cantidad;

            String sql = "INSERT INTO producto (nombre_cliente, producto, precio, cantidad, total) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, nombre); 
            ps.setString(2, producto);
            ps.setDouble(3, precio);
            ps.setInt(4, cantidad);
            ps.setDouble(5, total);

            ps.executeUpdate();
            con.close();

        } catch (Exception e) {
            System.out.println("Error insertar: " + e);
        }
    }
    // Método para listar los productos en la tabla
    public static int listar(javax.swing.table.DefaultTableModel modelo) throws SQLException {
        int filas = 0;
        modelo.setRowCount(0);

        try (Connection con = conexion.getConexion()) {
            if (con == null) {
                throw new SQLException("No se pudo establecer conexión con la base de datos.");
            }

            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT * FROM producto ORDER BY id DESC")) {

                while (rs.next()) {
                    modelo.addRow(new Object[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("nombre_cliente"),
                        rs.getString("producto"),
                        String.format("%.2f", rs.getDouble("precio")),
                        String.valueOf(rs.getInt("cantidad")),
                        String.format("%.2f", rs.getDouble("total")),
                        "Editar",
                        "Eliminar"
                    });
                    filas++;
                }
            }
        }
        return filas;
    }
    // Método para eliminar un producto de la base de datos
    public static void eliminar(int id) {
        try {
            Connection con = conexion.getConexion();
            PreparedStatement ps = con.prepareStatement("DELETE FROM producto WHERE id=?");
            ps.setInt(1, id);
            ps.executeUpdate();
            con.close();
        } catch (Exception e) {
            System.out.println("Error eliminar: " + e);
        }
    }
    // Método para actualizar un producto en la base de datos
    public static void actualizar(int id, String nombre, String producto, double precio, int cantidad) {
        try {
            Connection con = conexion.getConexion();
            double total = precio * cantidad;

            String sql = "UPDATE producto SET nombre_cliente=?, producto=?, precio=?, cantidad=?, total=? WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, nombre);
            ps.setString(2, producto);
            ps.setDouble(3, precio);
            ps.setInt(4, cantidad);
            ps.setDouble(5, total);
            ps.setInt(6, id);

            ps.executeUpdate();
            con.close();

        } catch (Exception e) {
            System.out.println("Error actualizar: " + e);
        }
    }
}