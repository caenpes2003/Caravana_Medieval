import { Routes } from '@angular/router';

import { CaravanaComponent } from './feature/caravana/caravana.component';
import { RutasComponent } from './feature/rutas/rutas.component';
import { ComercioComponent } from './feature/comercio/comercio.component';
import { ServiciosComponent } from './feature/servicios/servicios.component';

import { PreJuegoComponent } from './feature/pre-juego/pre-juego.component';
import { LoginComponent } from './feature/auth/login/login.component';
import { RegistrarComponent } from './feature/auth/registrar/registrar.component';
import { AdminCiudadesComponent } from './feature/ciudades/admin-ciudades.component';
import { AdminServiciosComponent } from './feature/servicios/admin-servicios/admin-servicios.component';
import { AdminStockComponent } from './feature/comercio/admin/admin-stock.component';
import { AdminGuard } from './core/guards/admin.guard';
import { RutasGuard } from './core/guards/rutas.guard';
import { ComercioGuard } from './core/guards/comercio.guard';
import { ServiciosGuard } from './core/guards/servicios.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  // Auth
  { path: 'login',     component: LoginComponent },
  { path: 'registrar', component: RegistrarComponent },

  // Admin
  { path: 'pre-juego',     component: PreJuegoComponent,     canActivate: [AdminGuard] },
  { path: 'admin-servicios', component: AdminServiciosComponent, canActivate: [AdminGuard] },
  { path: 'admin-ciudades',  component: AdminCiudadesComponent,  canActivate: [AdminGuard] },
  { path: 'admin-stock',     component: AdminStockComponent,     canActivate: [AdminGuard] },

  { path: 'caravana', component: CaravanaComponent },

  // Comercio y Servicios
  { path: 'rutas',    component: RutasComponent,    canActivate: [RutasGuard] },
  { path: 'comercio/:id', component: ComercioComponent,  canActivate: [ComercioGuard] },
  { path: 'comercio', component: ComercioComponent,  canActivate: [ComercioGuard] },
  { path: 'servicios/id:',    component: ServiciosComponent, canActivate: [ServiciosGuard] },
  { path: 'servicios',    component: ServiciosComponent, canActivate: [ServiciosGuard] },

  
  { path: '**', redirectTo: '' },
];
