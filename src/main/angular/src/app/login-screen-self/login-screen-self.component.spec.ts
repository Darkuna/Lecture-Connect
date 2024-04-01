import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginScreenSelfComponent } from './login-screen-self.component';

describe('LoginScreenSelfComponent', () => {
  let component: LoginScreenSelfComponent;
  let fixture: ComponentFixture<LoginScreenSelfComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginScreenSelfComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(LoginScreenSelfComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
