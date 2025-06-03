package com.ejemplo.caravana.dto;

public class VictoryDTO {
    private CaravanaDTO estado;
    private String mensaje;

    public VictoryDTO() {}

    public VictoryDTO(CaravanaDTO estado, String mensaje) {
        this.estado  = estado;
        this.mensaje = mensaje;
    }

    public CaravanaDTO getEstado() {
        return estado;
    }

    public void setEstado(CaravanaDTO estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
