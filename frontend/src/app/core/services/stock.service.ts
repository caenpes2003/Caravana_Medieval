import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface Stock {
  id: number;
  productoId: number;
  productoNombre: string;
  cantidad: number;
  fd: number;
  fo: number;
}

@Injectable({ providedIn: 'root' })
export class StockService {
  constructor(private http: HttpClient) {}

  getPorCiudad(ciudadId: number): Observable<Stock[]> {
    return this.http.get<Stock[]>(`/api/ciudades/${ciudadId}/stock`)
      .pipe(tap(data => console.log('📦 STOCK RAW:', data)));
  }

  crear(ciudadId: number, dto: Partial<Stock>): Observable<any> {
    return this.http.post(`/api/ciudades/${ciudadId}/stock`, dto);
  }

  actualizar(stockId: number, dto: Partial<Stock>): Observable<any> {
    return this.http.put(`/api/stock/${stockId}`, dto);
  }

  eliminar(stockId: number): Observable<void> {
    return this.http.delete<void>(`/api/stock/${stockId}`);
  }
}
