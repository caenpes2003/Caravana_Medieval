import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JuegoIniciarResponse, JuegoIniciarRequest } from '../models/juego.model';


@Injectable({ providedIn: 'root' })
export class JuegoService {
  private base = '/api/juego';

  constructor(private http: HttpClient) {}

  iniciar(data: JuegoIniciarRequest): Observable<JuegoIniciarResponse> {
    return this.http.post<JuegoIniciarResponse>(`${this.base}/iniciar`, data);
  }
}

