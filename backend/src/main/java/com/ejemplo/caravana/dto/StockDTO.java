package com.ejemplo.caravana.dto;

public class StockDTO {
    private Long id;
    private Long productoId;
    private String productoNombre;
    private int cantidad;
    private double FD;
    private double FO;

    public StockDTO() {}

    public StockDTO(Long id, Long productoId, String productoNombre,
                    int cantidad, double FD, double FO) {
        this.id = id;
        this.productoId = productoId;
        this.productoNombre = productoNombre;
        this.cantidad = cantidad;
        this.FD = FD;
        this.FO = FO;
    }
    // Constructor reducido (uso en inventario de caravana)
    public StockDTO(String productoNombre, int cantidad) {
        this.id = null;
        this.productoId = null;
        this.productoNombre = productoNombre;
        this.cantidad = cantidad;
        this.FD = 0.0;
        this.FO = 0.0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getFD() {
        return FD;
    }

    public void setFD(double FD) {
        this.FD = FD;
    }

    public void setFO(double FO) {
        this.FO = FO;
    }
    public double getFO() {
        return FO;
    }
    


}
