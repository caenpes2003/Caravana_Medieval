import { Component, ViewEncapsulation } from '@angular/core';
import { AuthService } from '../../../core/services/auth.service';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Router } from '@angular/router';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CaravanaService } from '../../../core/services/caravana.service';
import { MatIconModule } from '@angular/material/icon';

import { ReactiveFormsModule } from '@angular/forms';
import { Caravana } from '../../../core/models/caravana.model';

@Component({
  selector: 'app-login',
  standalone: true,
  encapsulation: ViewEncapsulation.None,
  imports: [
    CommonModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    RouterModule,
    MatIconModule,
    ReactiveFormsModule,
    MatProgressSpinnerModule,
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  nombre = '';
  password = '';

  constructor(
    private auth: AuthService,
    private router: Router,
    private caravanaService: CaravanaService
  ) {}

  ingresar() {
    this.auth.login(this.nombre, this.password).subscribe({
      next: () => {
        const rol = this.auth.getUsuario()?.rol;
        this.caravanaService
          .getEstado()
          .subscribe((caravana: Caravana | null) => {
            if (rol === 'ADMINISTRADOR') {
              if (caravana && caravana.estado === 'EN_CURSO') {
                this.router.navigate(['/caravana']);
              } else {
                this.router.navigate(['/pre-juego']);
              }
            } else if (rol === 'COMERCIANTE' || rol === 'CARAVANERO') {
              this.router.navigate(['/caravana']);
            } else {
              this.router.navigate(['/']);
            }
          });
      },
      error: () => alert('Usuario o contraseña incorrectos'),
    });
  }
}
