import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {HomeComponent} from "./components/home/home.component";
import {RoomViewComponent} from "./components/TableView/room-view/room-view.component";
import {CourseViewComponent} from "./components/TableView/course-view/course-view.component";
import {UsersViewComponent} from "./components/TableView/users-view/users-view.component";
import {WizardComponent} from "./components/wizzard/wizard.component";

const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'home', component: HomeComponent},
  {path: 'rooms', component: RoomViewComponent},
  {path: 'lectures', component: CourseViewComponent},
  {path: 'users', component: UsersViewComponent},
  {path: 'wizard', component: WizardComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
