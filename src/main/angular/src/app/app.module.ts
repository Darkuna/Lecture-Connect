import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderSelfComponent } from './header-self/header-self.component';

import {ButtonModule} from 'primeng/button';
import { LoginScreenSelfComponent } from './login-screen-self/login-screen-self.component';
import { CardModule } from "primeng/card";
import { InputTextModule } from 'primeng/inputtext';
import {PasswordModule} from "primeng/password";
import {FormsModule} from "@angular/forms";


@NgModule({
  declarations: [
    AppComponent,
    HeaderSelfComponent,
    LoginScreenSelfComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ButtonModule,
    CardModule,
    InputTextModule,
    PasswordModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent, HeaderSelfComponent]
})
export class AppModule { }
