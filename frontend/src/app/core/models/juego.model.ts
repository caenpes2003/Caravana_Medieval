export interface JuegoIniciarRequest {
    ciudadIds: number[];
    tiempoLimite: number;
    oroInicial: number;
    metaMonedas: number;
    vidaInicial: number;
  }
  
  export interface JuegoIniciarResponse {
    caravanaId: number;
    ciudadIds: number[];
    ciudadInicialNombre: string;
    tiempoLimite: number;
    oroInicial: number;
    metaMonedas: number;
    vidaInicial: number;
  }
  