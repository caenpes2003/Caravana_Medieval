export interface Ruta {
  id: number;
  ciudadOrigenId: number;
  ciudadOrigenNombre: string;
  ciudadDestinoId: number;
  ciudadDestinoNombre: string;
  distancia: number;
  segura: boolean;
  dano: number;
  tipoDanio: string;
  impuestoDestino: number;
  reversa?: boolean;
  tiempo: number;
}
