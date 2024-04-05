import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {HomeComponent} from "./home/home.component";
import {RoomsViewComponent} from "./rooms-view/rooms-view.component";
import {LecturesViewComponent} from "./lectures-view/lectures-view.component";
import {UsersViewComponent} from "./users-view/users-view.component";
import {PageNotFoundComponentComponent} from "./page-not-found-component/page-not-found-component.component";

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'home'},
  { path: 'home', component: HomeComponent},
  { path: 'login', component: LoginComponent}
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
