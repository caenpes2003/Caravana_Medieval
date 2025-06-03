// src/app/core/models/auth-response.model.ts
export interface AuthResponse {
  id: number;
  nombre: string;
  rol: 'ADMINISTRADOR' | 'CARAVANERO' | 'COMERCIANTE';
}
