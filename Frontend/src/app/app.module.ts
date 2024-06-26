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
import {NgxWebstorageModule} from 'ngx-webstorage';
import {PageNotFoundComponentComponent} from './components/page-not-found-component/page-not-found-component.component';
import {ToastModule} from 'primeng/toast';
import {RippleModule} from "primeng/ripple";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ConfirmationService, MessageService} from "primeng/api";
import {TableModule} from 'primeng/table';
import {ToolbarModule} from 'primeng/toolbar';
import {FileUploadModule} from 'primeng/fileupload';
import {MultiSelectModule} from "primeng/multiselect";
import {DialogModule} from "primeng/dialog";
import {DropdownModule} from "primeng/dropdown";
import {TagModule} from "primeng/tag";
import {RadioButtonModule} from "primeng/radiobutton";
import {InputNumberModule} from "primeng/inputnumber";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {InputTextareaModule} from "primeng/inputtextarea";
import {InputSwitchModule} from "primeng/inputswitch";
import {SelectButtonModule} from "primeng/selectbutton";
import {FullCalendarModule} from "@fullcalendar/angular";
import {CalendarComponent} from './components/calendar/calendar.component';
import {RoomViewComponent} from './components/TableView/room-view/room-view.component';
import {CourseViewComponent} from './components/TableView/course-view/course-view.component';
import {UsersViewComponent} from './components/TableView/users-view/users-view.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderSelfComponent,
    LoginComponent,
    HomeComponent,
    PageNotFoundComponentComponent,
    CalendarComponent,
    RoomViewComponent,
    CourseViewComponent,
    UsersViewComponent
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
    RippleModule,
    TableModule,
    FileUploadModule,
    ToolbarModule,
    MultiSelectModule,
    DialogModule,
    DropdownModule,
    TagModule,
    RadioButtonModule,
    InputNumberModule,
    ConfirmDialogModule,
    InputTextareaModule,
    InputSwitchModule,
    SelectButtonModule,
    FullCalendarModule
  ],
  providers: [ConfirmationService, MessageService, CalendarComponent],
  bootstrap: [AppComponent]
})
export class AppModule {
}
