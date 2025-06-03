import { Ruta } from "./ruta.model";

export interface Caravana {
  id: number;
  nombre: string;
  velocidad: number;
  capacidadOriginal: number;
  capacidadMax: number;
  cargaActual: number;
  dinero: number;
  vida: number;
  tiempoTranscurrido: number;  // segundos
  tiempoLimite: number;        // segundos
  metaGanancia: number;        // Monedas de Oro
  tieneGuardias: boolean;
  ciudadActualId: number;
  ciudadActualNombre: string;  
  haViajado: boolean;       
  rutasDisponibles?: Ruta[];
  serviciosUsados: string[];

  ultimaActualizacion?: string;    
  tiempoMostrado?: number;
  dineroInicial: number;

  estado: string; 
}
