import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable, tap } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
import { Caravana } from '../models/caravana.model';
import { Stock } from '../models/stock.model';
import { CiudadCaravana } from '../models/ciudad-caravana.model';
import { JugadorConectado } from '../models/jugador-conectado.model';
@Injectable({
  providedIn: 'root',
})
export class CaravanaService {
  private base = '/api/caravana';
  private ciudadesSeleccionadas$ = new BehaviorSubject<number[]>([]);

  constructor(private http: HttpClient) {}

  /**
   * Mantiene localmente el arreglo de IDs de CiudadCaravana seleccionadas.
   */
  setCiudadesSeleccionadas(ciudades: number[]) {
    this.ciudadesSeleccionadas$.next(ciudades);
  }

  getCiudadesSeleccionadas() {
    return this.ciudadesSeleccionadas$.asObservable();
  }

  hayCiudadesSeleccionadas(): boolean {
    return this.ciudadesSeleccionadas$.getValue().length >= 2;
  }

  /**
   * Obtiene las instancias de CiudadCaravana (los clones) para que el Admin
   * pueda elegir con cuáles ciudades iniciar partida.
   */
  getCiudadesClonadas(): Observable<CiudadCaravana[]> {
    return this.http.get<CiudadCaravana[]>('/api/ciudad-caravana');
  }

  /**
   * Inventario de la caravana (solo para Caravanero/Comerciante).
   */
  getInventario(): Observable<Stock[]> {
    return this.http.get<Stock[]>('/api/caravana/inventario');
  }

  /**
   * Consulta el estado de la caravana.
   * Devuelve `null` si la partida NO ha iniciado (HTTP 204).
   */
  getEstado(): Observable<Caravana | null> {
    return this.http
      .get<Caravana>(`${this.base}`, { observe: 'response' })
      .pipe(map((resp) => (resp.status === 204 ? null : resp.body)));
  }

  /**
   * Lanza la petición para iniciar partida.
   * Payload: { ciudadIds: number[], tiempoLimite, oroInicial, metaMonedas, vidaInicial }
   */
  iniciar(payload: {
    ciudadIds: number[];
    tiempoLimite: number;
    oroInicial: number;
    metaMonedas: number;
    vidaInicial: number;
  }): Observable<void> {
    return this.http.post<void>('/api/juego/iniciar', payload);
  }

  /**
   * Viajar por ruta: recibe el ID de la ruta.
   */
  viajar(rutaId: number): Observable<Caravana> {
    return this.http.post<Caravana>(`${this.base}/viajar`, { rutaId });
  }

  /**
   * Comprar: ciudadId, productoId, cantidad.
   */
  comprar(
    ciudadId: number,
    productoId: number,
    cantidad: number
  ): Observable<Caravana> {
    return this.http.post<Caravana>(`${this.base}/comprar`, {
      ciudadId,
      productoId,
      cantidad,
    });
  }

  /**
   * Vender: ciudadId, productoId, cantidad.
   */
  vender(
    ciudadId: number,
    productoId: number,
    cantidad: number
  ): Observable<Caravana> {
    return this.http.post<Caravana>(`${this.base}/vender`, {
      ciudadId,
      productoId,
      cantidad,
    });
  }

  /**
   * Usar servicio en determinada ciudad: { servicioId, ciudadId }.
   */
  usarServicio(servicioId: number, ciudadId: number): Observable<Caravana> {
    return this.http.post<Caravana>(`${this.base}/servicios`, {
      servicioId,
      ciudadId,
    });
  }

  /**
   * Pagar impuestos de una ruta (solo recibe rutaId).
   * Devuelve Caravana actualizada o lanza error si no hay dinero.
   */
  pagarImpuestos(rutaId: number): Observable<Caravana> {
    return this.http.post<Caravana>(`${this.base}/impuestos`, { rutaId });
  }

  /**
   * Resetear toda la partida (DELETE /api/caravana).
   */
  reset(): Observable<void> {
    return this.http.delete<void>(`${this.base}`);
  }

  /**
   * Obtener jugadores conectados (nombre + rol) en esta caravana.
   */

  getJugadoresConectados(): Observable<JugadorConectado[]> {
    return this.http
      .get<JugadorConectado[]>(`${this.base}/jugadores`)
      .pipe(
        tap((lista) =>
          sessionStorage.setItem('jugadores', JSON.stringify(lista))
        )
      );
  }
}
