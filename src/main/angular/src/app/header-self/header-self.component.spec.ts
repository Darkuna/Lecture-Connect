import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderSelfComponent } from './header-self.component';

describe('HeaderSelfComponent', () => {
  let component: HeaderSelfComponent;
  let fixture: ComponentFixture<HeaderSelfComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [HeaderSelfComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(HeaderSelfComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
