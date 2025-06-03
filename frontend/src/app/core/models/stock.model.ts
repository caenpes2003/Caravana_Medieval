export interface Stock {
  id: number;
  ciudadId: number;
  ciudadNombre: string;
  productoId: number;
  productoNombre: string;
  cantidad: number;
  fd: number; // Factor de demanda
  fo: number; // Factor de oferta
}
