import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HeaderSelfComponent} from './components/header-self/header-self.component';

import {ButtonModule} from 'primeng/button';
import {CardModule} from "primeng/card";
import {InputTextModule} from 'primeng/inputtext';
import {PasswordModule} from "primeng/password";
import {FormsModule} from "@angular/forms";
import {LoginComponent} from './components/login/login.component';
import {HomeComponent} from './components/home/home.component';
import {HttpClientModule} from '@angular/common/http';
import {RoomsViewComponent} from './views/rooms-view/rooms-view.component';
import {LecturesViewComponent} from './views/lectures-view/lectures-view.component';
import {UsersViewComponent} from './views/users-view/users-view.component';
import {LocalStorageService, NgxWebstorageModule} from 'ngx-webstorage';
import {PageNotFoundComponentComponent} from './components/page-not-found-component/page-not-found-component.component';
import { ToastModule } from 'primeng/toast';
import {RippleModule} from "primeng/ripple";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MessageService} from "primeng/api";
import {ReloadService} from "./services/reload.service";
@NgModule({
  declarations: [
    AppComponent,
    HeaderSelfComponent,
    LoginComponent,
    HomeComponent,
    RoomsViewComponent,
    LecturesViewComponent,
    UsersViewComponent,
    PageNotFoundComponentComponent,
  ],
  imports: [
    BrowserAnimationsModule,
    BrowserModule,
    AppRoutingModule,
    ButtonModule,
    CardModule,
    InputTextModule,
    PasswordModule,
    ToastModule,
    FormsModule,
    HttpClientModule,
    NgxWebstorageModule.forRoot(),
    RippleModule
  ],
  providers: [MessageService, ReloadService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
