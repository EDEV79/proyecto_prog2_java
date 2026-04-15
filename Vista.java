import java.awt.*;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.table.*;

public class Vista extends JFrame {

    private JTextField txtNombre, txtProducto, txtPrecio, txtCantidad;
    private JButton btnAccion;
    private JTable tabla;
    private DefaultTableModel modelo;
    private int idEditando = -1;

    @FunctionalInterface
    interface BackgroundTask<T> {
        T run() throws Exception;
    }

    public Vista() {
        setTitle("Gestión de Productos - Proyecto Final");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 640);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 242, 245));

        // ---- PANEL FORMULARIO ----
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 10, 5, 10),
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(63, 81, 181), 2),
                "  Registro de Producto  "
            )
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        Font labelFont = new Font("Arial", Font.BOLD, 13);

        // Fila 0: Nombre + Producto
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblNombre = new JLabel("Nombre Cliente:");
        lblNombre.setFont(labelFont);
        panelForm.add(lblNombre, gbc);

        gbc.gridx = 1;
        txtNombre = new JTextField(22);
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 13));
        panelForm.add(txtNombre, gbc);

        gbc.gridx = 2;
        JLabel lblProducto = new JLabel("Producto:");
        lblProducto.setFont(labelFont);
        panelForm.add(lblProducto, gbc);

        gbc.gridx = 3;
        txtProducto = new JTextField(22);
        txtProducto.setFont(new Font("Arial", Font.PLAIN, 13));
        panelForm.add(txtProducto, gbc);

        // Fila 1: Precio + Cantidad
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblPrecio = new JLabel("Precio:");
        lblPrecio.setFont(labelFont);
        panelForm.add(lblPrecio, gbc);

        gbc.gridx = 1;
        txtPrecio = new JTextField(22);
        txtPrecio.setFont(new Font("Arial", Font.PLAIN, 13));
        panelForm.add(txtPrecio, gbc);

        gbc.gridx = 2;
        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setFont(labelFont);
        panelForm.add(lblCantidad, gbc);

        gbc.gridx = 3;
        txtCantidad = new JTextField(22);
        txtCantidad.setFont(new Font("Arial", Font.PLAIN, 13));
        panelForm.add(txtCantidad, gbc);

        // Fila 2: Botón centrado
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        btnAccion = new JButton("   Insertar   ");
        btnAccion.setFont(new Font("Arial", Font.BOLD, 15));
        btnAccion.setBackground(new Color(46, 204, 113));
        btnAccion.setForeground(Color.WHITE);
        btnAccion.setFocusPainted(false); 
        btnAccion.setBorderPainted(false);
        btnAccion.setOpaque(true);
        btnAccion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAccion.setPreferredSize(new Dimension(160, 38));
        panelForm.add(btnAccion, gbc);

        add(panelForm, BorderLayout.NORTH);

        // ---- TABLA ----
        String[] columnas = {"ID", "Nombre Cliente", "Producto", "Precio", "Cantidad", "Total", "Editar", "Eliminar"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 6 || col == 7;
            }
            @Override
            public Class<?> getColumnClass(int col) {
                return String.class;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(36);
        tabla.setFont(new Font("Arial", Font.PLAIN, 13));
        tabla.setSelectionBackground(new Color(173, 216, 230));
        tabla.setGridColor(new Color(210, 210, 210));
        tabla.setShowHorizontalLines(true);
        tabla.setShowVerticalLines(false);

        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 13));
        header.setBackground(new Color(52, 73, 94));
        header.setForeground(Color.BLACK);
        header.setReorderingAllowed(false);

        tabla.getColumnModel().getColumn(0).setMaxWidth(55);
        tabla.getColumnModel().getColumn(6).setMaxWidth(90);
        tabla.getColumnModel().getColumn(7).setMaxWidth(90);

        // Botón Editar
        tabla.getColumnModel().getColumn(6).setCellRenderer(
            new BtnRenderer("Editar", new Color(41, 128, 185))
        );
        tabla.getColumnModel().getColumn(6).setCellEditor(
            new BtnEditor(new JCheckBox(), "Editar", new Color(41, 128, 185)) {
                @Override
                protected void onAction(int row) {
                    editarFila(row);
                }
            }
        );

        // Botón Eliminar
        tabla.getColumnModel().getColumn(7).setCellRenderer(
            new BtnRenderer("Eliminar", new Color(192, 57, 43))
        );
        tabla.getColumnModel().getColumn(7).setCellEditor(
            new BtnEditor(new JCheckBox(), "Eliminar", new Color(192, 57, 43)) {
                @Override
                protected void onAction(int row) {
                    eliminarFila(row);
                }
            }
        );

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 10, 10, 10),
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(63, 81, 181), 2),
                "  Lista de Productos  "
            )
        ));

        add(scroll, BorderLayout.CENTER);

        // ---- EVENTOS ----
        btnAccion.addActionListener(e -> {
            if (idEditando == -1) {
                accionInsertar();
            } else {
                accionActualizar();
            }
        });

        setVisible(true);
        cargarTablaAlIniciar();
    }

    // ---- CRUD ----

    private void accionInsertar() {
        String nombre = txtNombre.getText().trim();
        String producto = txtProducto.getText().trim();
        String precioStr = txtPrecio.getText().trim();
        String cantidadStr = txtCantidad.getText().trim();

        if (nombre.isEmpty() || producto.isEmpty() || precioStr.isEmpty() || cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor complete todos los campos.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            double precio = Double.parseDouble(precioStr);
            int cantidad = Integer.parseInt(cantidadStr);
            ejecutarEnSegundoPlano(
                () -> {
                    logica.insertar(nombre, producto, precio, cantidad);
                    return logica.obtenerRegistros();
                },
                filas -> {
                    actualizarModelo(filas);
                    limpiarFormulario();
                    JOptionPane.showMessageDialog(this,
                        "Registro insertado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                },
                "No fue posible insertar el registro."
            );
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Precio debe ser un número decimal y Cantidad un número entero.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionActualizar() {
        String nombre = txtNombre.getText().trim();
        String producto = txtProducto.getText().trim();
        String precioStr = txtPrecio.getText().trim();
        String cantidadStr = txtCantidad.getText().trim();

        if (nombre.isEmpty() || producto.isEmpty() || precioStr.isEmpty() || cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor complete todos los campos.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            double precio = Double.parseDouble(precioStr);
            int cantidad = Integer.parseInt(cantidadStr);
            ejecutarEnSegundoPlano(
                () -> {
                    logica.actualizar(idEditando, nombre, producto, precio, cantidad);
                    return logica.obtenerRegistros();
                },
                filas -> {
                    actualizarModelo(filas);
                    limpiarFormulario();
                    idEditando = -1;
                    btnAccion.setText("   Insertar   ");
                    btnAccion.setBackground(new Color(46, 204, 113));
                    JOptionPane.showMessageDialog(this,
                        "Registro actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                },
                "No fue posible actualizar el registro."
            );
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Precio debe ser un número decimal y Cantidad un número entero.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void editarFila(int fila) {
        SwingUtilities.invokeLater(() -> {
            idEditando = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
            txtNombre.setText(modelo.getValueAt(fila, 1).toString());
            txtProducto.setText(modelo.getValueAt(fila, 2).toString());
            txtPrecio.setText(modelo.getValueAt(fila, 3).toString());
            txtCantidad.setText(modelo.getValueAt(fila, 4).toString());
            btnAccion.setText("   Actualizar   ");
            btnAccion.setBackground(new Color(230, 126, 34));
            txtNombre.requestFocus();
        });
    }

    public void eliminarFila(int fila) {
        SwingUtilities.invokeLater(() -> {
            int id = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
            String nombre = modelo.getValueAt(fila, 1).toString();
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Desea eliminar el registro de \"" + nombre + "\"?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                ejecutarEnSegundoPlano(
                    () -> {
                        logica.eliminar(id);
                        return logica.obtenerRegistros();
                    },
                    this::actualizarModelo,
                    "No fue posible eliminar el registro."
                );
            }
        });
    }

    public void cargarTabla() {
        ejecutarEnSegundoPlano(
            logica::obtenerRegistros,
            this::actualizarModelo,
            "No fue posible cargar los registros guardados."
        );
    }

    private void cargarTablaAlIniciar() {
        ejecutarEnSegundoPlano(
            logica::obtenerRegistros,
            filas -> {
                actualizarModelo(filas);
                if (filas.isEmpty()) {
                    System.out.println("No hay registros previos en la base de datos.");
                }
            },
            "No fue posible cargar datos de sesiones anteriores. Verifique MySQL, la base profin_db y la tabla producto."
        );
    }

    private void actualizarModelo(List<Object[]> filas) {
        modelo.setRowCount(0);
        for (Object[] fila : filas) {
            modelo.addRow(fila);
        }
    }

    private <T> void ejecutarEnSegundoPlano(
        BackgroundTask<T> tarea,
        Consumer<T> onSuccess,
        String mensajeErrorBase
    ) {
        setBusy(true);
        SwingWorker<T, Void> worker = new SwingWorker<>() {
            @Override
            protected T doInBackground() throws Exception {
                return tarea.run();
            }

            @Override
            protected void done() {
                setBusy(false);
                try {
                    onSuccess.accept(get());
                } catch (Exception ex) {
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    String detalle = cause.getMessage() != null ? cause.getMessage() : cause.toString();
                    if (detalle.contains("Communications link failure") || detalle.contains("Connection refused")) {
                        detalle = "No hay conexión con MySQL en 127.0.0.1:3306. "
                            + "Inicie el servicio MySQL de XAMPP y vuelva a intentar.";
                    }
                    JOptionPane.showMessageDialog(Vista.this,
                        mensajeErrorBase + "\nDetalle: " + detalle,
                        "Error de conexión", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void setBusy(boolean busy) {
        setCursor(busy
            ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
            : Cursor.getDefaultCursor());
        btnAccion.setEnabled(!busy);
        tabla.setEnabled(!busy);
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtProducto.setText("");
        txtPrecio.setText("");
        txtCantidad.setText("");
    }

    // ---- CLASES INTERNAS PARA BOTONES EN LA TABLA ----

    class BtnRenderer extends JButton implements TableCellRenderer {
        BtnRenderer(String text, Color color) {
            setText(text);
            setFont(new Font("Arial", Font.BOLD, 12));
            setBackground(color);
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col) {
            return this;
        }
    }

    abstract class BtnEditor extends DefaultCellEditor {
        private final JButton btn;
        private int currentRow;

        BtnEditor(JCheckBox checkBox, String text, Color color) {
            super(checkBox);
            setClickCountToStart(1);
            btn = new JButton(text);
            btn.setFont(new Font("Arial", Font.BOLD, 12));
            btn.setBackground(color);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setOpaque(true);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e -> {
                fireEditingStopped();
                onAction(currentRow);
            });
        }

        protected abstract void onAction(int row);

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int col) {
            currentRow = row;
            return btn;
        }

        @Override
        public Object getCellEditorValue() {
            return btn.getText();
        }
    }

    // ---- MAIN ----
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(Vista::new);
    }
}
