import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Ciudad {
  id: number;
  nombre: string;
  impuestos: number;
}

@Injectable({
  providedIn: 'root',
})
export class CiudadService {
  private readonly API_CARAVANA = '/api/ciudades';
  private readonly API_CATALOGO = '/api/catalogo/ciudades';
  

  constructor(private http: HttpClient) {}

  /* === CIUDADES DE CARAVANA ========================================= */
  getCiudades(): Observable<Ciudad[]> {
    return this.http.get<Ciudad[]>(this.API_CARAVANA);
  }
  getCiudad(id: number): Observable<Ciudad> {
    return this.http.get<Ciudad>(`${this.API_CARAVANA}/${id}`);
  }
  crearCiudad(dto: { nombre: string; impuestos: number }): Observable<Ciudad> {
    return this.http.post<Ciudad>(this.API_CARAVANA, dto);
  }
  actualizarCiudad(
    id: number,
    dto: { nombre: string; impuestos: number }
  ): Observable<Ciudad> {
    return this.http.put<Ciudad>(`${this.API_CARAVANA}/${id}`, dto);
  }
  /* === CATÁLOGO BASE =============================================== */
  listarCiudadesCatalogo(): Observable<Ciudad[]> {
  return this.http.get<Ciudad[]>(this.API_CATALOGO);
}

  crearCiudadCatalogo(dto:{nombre:string; impuesto:number}){ 
    return this.http.post<Ciudad>(this.API_CATALOGO,dto);}

  eliminarCiudadCatalogo(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_CATALOGO}/${id}`);
  }
  actualizarCiudadCatalogo(
  id: number,
  dto: { nombre: string; impuesto: number }
): Observable<Ciudad> {
  return this.http.put<Ciudad>(`${this.API_CATALOGO}/${id}`, dto);
}
}
