// src/main.ts
import { bootstrapApplication } from '@angular/platform-browser';
import {
  provideHttpClient,
  withInterceptorsFromDi,
  HTTP_INTERCEPTORS
} from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { provideAnimations } from '@angular/platform-browser/animations';

import { AppComponent } from './app/app.component';
import { routes } from './app/app.routes';
import { appConfig } from './app/app.config';

/* Interceptores */
import { CredentialsInterceptor } from './app/core/interceptors/credentials.interceptor';
import { ForbiddenInterceptor  } from './app/core/interceptors/forbidden.interceptor';

bootstrapApplication(AppComponent, {
  providers: [
    /* Interceptores registrados como multi */
    { provide: HTTP_INTERCEPTORS, useClass: CredentialsInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ForbiddenInterceptor,  multi: true },

    /* Cliente HTTP que toma los interceptores del DI */
    provideHttpClient(withInterceptorsFromDi()),

    /* Animaciones y rutas */
    provideAnimations(),
    provideRouter(routes),

    
    appConfig.providers,
  ],
}).catch(err => console.error(err));
