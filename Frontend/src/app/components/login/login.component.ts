import {Component} from '@angular/core';
import {Router} from '@angular/router';
import {LoginUserInfoService} from "../../services/login-user-info.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  stayLogin:boolean = false;

  protected loginForm = new FormGroup({
    username: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required])
  })

  constructor(
    private loginService: LoginUserInfoService,
    private router: Router,
  ) {
  }

  onSubmit() {
    if (this.loginForm.valid) {
      console.log(this.loginForm.value);
      this.loginService.login(this.loginForm.value, this.stayLogin)
        .subscribe((data: any) => {
          if (this.loginService.isLoggedIn()) {
            this.router.navigate(['/users/home'])
              .catch(message => {
                }
              );          }
          console.log(data);
        });
    }
  }

  /*

  login() {
    this.userInfoService.loginUser(this.loginObj, this.stayLogin)
      .then(() => {
        this.messageService.add({severity: 'success',summary: `Welcome back ${this.loginObj.name}!`});
        this.router.navigate(['/user/home'])
          .catch(message => {
            this.messageService.add({severity: 'error', summary: 'Failure in Redirect', detail: message});
        })
    })
      .catch((error: string) => {
        this.messageService.add({severity: 'error', summary: 'Upload Fault', detail: error});
      });
  }
   */
}
