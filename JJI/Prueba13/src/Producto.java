public class Producto {
    private String nombre;
    private int cantidad;
    private double precio;
    private double iva;

    public Producto(String nombre, int cantidad, double precio, double iva) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.iva = iva;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public double getPrecioConIVA() {
        return precio * (1 + iva);
    }
}