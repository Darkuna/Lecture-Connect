import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {HomeComponent} from "./components/home/home.component";
import {RoomViewComponent} from "./components/TableView/room-view/room-view.component";
import {CourseViewComponent} from "./components/TableView/course-view/course-view.component";
import {UsersViewComponent} from "./components/TableView/users-view/users-view.component";
import {WizardComponent} from "./components/wizzard/wizard.component";
import {PageNotFoundComponentComponent} from "./components/page-not-found-component/page-not-found-component.component";
import {EditorComponent} from "./components/editor/editor.component";
import {DataWizardComponent} from "./components/wizzard/data-wizard/data-wizard.component";
import {
  AuthGuardAdmin,
  AuthGuardEditorClose,
  AuthGuardLogin,
  AuthGuardRemember,
  AuthGuardWizardClose
} from "./guard/auth-guards";
import {TableViewComponent} from "./components/TableView/table-view/table-view.component";

const routes: Routes = [
  {path: 'login', component: LoginComponent, canActivate: [AuthGuardRemember]},
  {path: 'user', canActivateChild: [AuthGuardLogin], children:[
      {path: 'home', component: HomeComponent},
      {path: 'rooms', component: RoomViewComponent},
      {path: 'courses', component: CourseViewComponent},
      {path: 'wizard', component: WizardComponent, canDeactivate: [AuthGuardWizardClose]},
      {path: 'editor', component: EditorComponent, canDeactivate: [AuthGuardEditorClose]},
      {path: 'preselection', component: DataWizardComponent},
    ]},
  {path: 'admin', canActivateChild: [AuthGuardAdmin], children:[
      {path: 'users', component: UsersViewComponent},
      {path: 'tables', component: TableViewComponent},

    ]},
  { path: '**', component: PageNotFoundComponentComponent },  // Wildcard route for a 404 page
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
