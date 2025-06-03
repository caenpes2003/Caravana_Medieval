import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from './shared/navbar/navbar.component';
import { AuthService } from './core/services/auth.service';
import { of } from 'rxjs';
import { catchError } from 'rxjs/operators';
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NavbarComponent],
  template: `
    <app-navbar></app-navbar>
    <div class="container">
      <router-outlet></router-outlet>
    </div>
  `,
})
export class AppComponent implements OnInit {
  constructor(private auth: AuthService) {}

  ngOnInit() {
    this.auth
      .me()
      .pipe(
        catchError((err) => {
          console.warn('⚠️ No hay sesión activa o error en /me:', err);
          return of(null); // devolvemos observable con null
        })
      )
      .subscribe((usuario) => {
        if (usuario) {
          console.log('🙋 Usuario con sesión activa:', usuario);
          
        } else {
          
        }
      });
  }
}
