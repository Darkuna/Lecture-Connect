import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SemiAutoAssignmentComponent } from './semi-auto-assignment.component';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { ScrollPanelModule } from 'primeng/scrollpanel';
import {SharedModule} from "primeng/api";

@NgModule({
  declarations: [SemiAutoAssignmentComponent],
  imports: [
    CommonModule,
    SharedModule,
    FormsModule,
    DialogModule,
    ButtonModule,
    CheckboxModule,
    ScrollPanelModule,
  ],
  exports: [SemiAutoAssignmentComponent],
})
export class SemiAutoAssignmentModule {}
