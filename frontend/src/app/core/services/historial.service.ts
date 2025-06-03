import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface HistorialDTO {
  id: number;
  timestamp: string;
  tipo: string;
  detalle: string;
}

@Injectable({ providedIn: 'root' })
export class HistorialService {
  constructor(private http: HttpClient) {}

  getAll(): Observable<HistorialDTO[]> {
    return this.http.get<HistorialDTO[]>('/api/caravana/historial?caravanaId=1');
  }
}
