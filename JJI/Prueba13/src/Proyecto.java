
import java.awt.Component;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class Proyecto {

    private static final String ADMIN_PASSWORD = "CajaR";
    private static Map<String, Map<String, Object>> stockProductos = new HashMap<>();
    private static Map<String, Integer> carrito = new HashMap<>();
    private static Map<String, Integer> ventas = new HashMap<>();
    private static double ivaPorDefecto = 0.21; // IVA por defecto (21%)

    public static void main(String[] args) {
        inicializarBaseDeDatos();

        Tienda tienda = new Tienda();
        tienda.iniciarVentas();
    }

    static class Tienda {

        private Map<String, Double> productos;

        public Tienda() {
            productos = new HashMap<>();
        }

        public void iniciarVentas() {
            gestionarVentas();
        }

        private void gestionarVentas() {
            JFrame frame = new JFrame("Tienda");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLayout(new GridLayout(4, 1));

            JButton agregarProductoBtn = new JButton("Agregar Producto a la Base de Datos");
            JButton agregarCarritoBtn = new JButton("Agregar Producto al Carrito");
            JButton modificarProductoBtn = new JButton("Modificar Producto");
            JButton verStockBtn = new JButton("Ver Stock de Productos");

            agregarProductoBtn.addActionListener(e -> agregarProducto());
            agregarCarritoBtn.addActionListener(e -> agregarCarrito());
            modificarProductoBtn.addActionListener(e -> modificarProducto());
            verStockBtn.addActionListener(e -> verStock());

            frame.add(agregarProductoBtn);
            frame.add(agregarCarritoBtn);
            frame.add(modificarProductoBtn);
            frame.add(verStockBtn);

            frame.setVisible(true);
        }

        private void agregarProducto() {
            String nombre = JOptionPane.showInputDialog("Nombre del producto:");
            double precio = Double.parseDouble(JOptionPane.showInputDialog("Precio del producto:"));
            int cantidad = Integer.parseInt(JOptionPane.showInputDialog("Cantidad: "));
            stockProductos.put(nombre, crearProductoInfo(cantidad, precio));
            JOptionPane.showMessageDialog(null, "Producto agregado a la base de datos.");
        }

        private Map<String, Object> crearProductoInfo(int cantidad, double precio) {
            Map<String, Object> productoInfo = new HashMap<>();
            productoInfo.put("cantidad", cantidad);
            productoInfo.put("precio", precio);
            productoInfo.put("iva", ivaPorDefecto);
            return productoInfo;
        }

        private void agregarCarrito() {
            JFrame frame = new JFrame("Agregar al Carrito");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLayout(new GridLayout(stockProductos.size() + 1, 1));

            JLabel titleLabel = new JLabel("Selecciona los productos:");
            frame.add(titleLabel);

            for (String producto : stockProductos.keySet()) {
                JCheckBox checkBox = new JCheckBox(producto);
                frame.add(checkBox);
            }

            JButton comprarBtn = new JButton("Comprar");
            comprarBtn.addActionListener(e -> comprarProductos(frame));
            frame.add(comprarBtn);

            frame.setVisible(true);
        }

        private void comprarProductos(JFrame frame) {
            double totalPagar = 0;

            for (Component component : frame.getContentPane().getComponents()) {
                if (component instanceof JCheckBox) {
                    JCheckBox checkBox = (JCheckBox) component;
                    if (checkBox.isSelected()) {
                        String producto = checkBox.getText();
                        Map<String, Object> productoInfo = stockProductos.get(producto);
                        double precio = (double) productoInfo.get("precio");
                        totalPagar += precio;
                    }
                }
            }

            DecimalFormat df = new DecimalFormat("#.##");
            JOptionPane.showMessageDialog(null, "Total a pagar: €" + df.format(totalPagar));

            frame.dispose();
        }

        private void modificarProducto() {
            String password = JOptionPane.showInputDialog(null, "Ingrese la contraseña de administrador:");
            if (password == null || !password.equals(ADMIN_PASSWORD)) {
                JOptionPane.showMessageDialog(null, "Contraseña incorrecta. No tiene acceso para modificar productos.");
                return;
            }

            String producto = JOptionPane.showInputDialog("Ingrese el nombre del producto a modificar:");
            if (producto == null || !stockProductos.containsKey(producto)) {
                JOptionPane.showMessageDialog(null, "El producto no existe.");
                return;
            }

            int nuevaCantidad = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la nueva cantidad para " + producto + ":"));
            double nuevoPrecio = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el nuevo precio para " + producto + ":"));

            stockProductos.put(producto, crearProductoInfo(nuevaCantidad, nuevoPrecio));

            JOptionPane.showMessageDialog(null, "Producto modificado correctamente.");
        }

        private void verStock() {
            StringBuilder listaProductos = new StringBuilder();
            listaProductos.append("--- Stock de Productos ---\n");
            for (Map.Entry<String, Map<String, Object>> entry : stockProductos.entrySet()) {
                String nombreProducto = entry.getKey();
                Map<String, Object> productoInfo = entry.getValue();
                int cantidad = (int) productoInfo.get("cantidad");
                double precio = (double) productoInfo.get("precio");
                double iva = (double) productoInfo.get("iva");
                double precioConIVA = precio * (1 + iva); // Calcular precio con IVA
                listaProductos.append(nombreProducto).append(" - Cantidad: ").append(cantidad)
                        .append(", Precio con IVA: €").append(String.format("%.2f", precioConIVA)).append("\n");
            }
            JOptionPane.showMessageDialog(null, listaProductos.toString());
        }
    }

    private static void inicializarBaseDeDatos() {
        stockProductos.put("Manzanas", crearProductoInfo(20, 2.5));
        stockProductos.put("Plátanos", crearProductoInfo(15, 1.8));
        stockProductos.put("Leche", crearProductoInfo(30, 1.2));
        stockProductos.put("Pan", crearProductoInfo(25, 0.9));
        stockProductos.put("Huevos", crearProductoInfo(40, 3.0));
        stockProductos.put("Arroz", crearProductoInfo(50, 1.5));
        stockProductos.put("Aceite", crearProductoInfo(35, 2.8));
        stockProductos.put("Azúcar", crearProductoInfo(45, 1.0));
        stockProductos.put("Sal", crearProductoInfo(40, 0.8));
        stockProductos.put("Café", crearProductoInfo(20, 4.0));
    }

    private static Map<String, Object> crearProductoInfo(int cantidad, double precio) {
        Map<String, Object> productoInfo = new HashMap<>();
        productoInfo.put("cantidad", cantidad);
        productoInfo.put("precio", precio);
        productoInfo.put("iva", ivaPorDefecto);
        return productoInfo;
    }
}
