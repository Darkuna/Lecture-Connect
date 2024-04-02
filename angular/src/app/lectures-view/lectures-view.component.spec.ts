import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LecturesViewComponent } from './lectures-view.component';

describe('LecturesViewComponent', () => {
  let component: LecturesViewComponent;
  let fixture: ComponentFixture<LecturesViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LecturesViewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(LecturesViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
