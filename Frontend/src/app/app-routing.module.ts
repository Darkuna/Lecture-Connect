import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {HomeComponent} from "./components/home/home.component";
import {RoomViewComponent} from "./components/TableView/room-view/room-view.component";
import {CourseViewComponent} from "./components/TableView/course-view/course-view.component";
import {UsersViewComponent} from "./components/TableView/users-view/users-view.component";
import {WizardComponent} from "./components/wizzard/wizard.component";
import {RoomSelectionPageComponent} from "./components/home/room-selection-page/room-selection-page.component";
import {CourseSelectionPageComponent} from "./components/home/course-selection-page/course-selection-page.component";
import {PageNotFoundComponentComponent} from "./components/page-not-found-component/page-not-found-component.component";
import {EditorComponent} from "./components/editor/editor.component";
import {DataWizardComponent} from "./components/wizzard/1-course-selection/data-wizard/data-wizard.component";
import {AuthGuardAdmin, AuthGuardLogin} from "./guard/auth-guard.guard";

const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'user', canActivateChild: [AuthGuardLogin], children:[
      {path: 'home', component: HomeComponent},
      {path: 'rooms', component: RoomViewComponent},
      {path: 'courses', component: CourseViewComponent},
      {path: 'wizard', component: WizardComponent},
      {path: 'tt-rooms', component: RoomSelectionPageComponent},
      {path: 'tt-courses', component: CourseSelectionPageComponent},
      {path: 'editor', component: EditorComponent},
      {path: 'preselection', component: DataWizardComponent},
    ]},
  {path: 'admin', canActivateChild: [AuthGuardAdmin], children:[
      {path: 'users', component: UsersViewComponent},
    ]},
  { path: '**', component: PageNotFoundComponentComponent },  // Wildcard route for a 404 page
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
