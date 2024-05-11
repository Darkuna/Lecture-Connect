import {Component, OnInit} from '@angular/core';
import {MenuItem} from "primeng/api";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',

})
export class HomeComponent implements OnInit {
  items: MenuItem[] | undefined;

  ngOnInit() {
    this.items = [
      { separator: true },
      {
        label: 'Editor',
        items: [
          {
            label: 'Edit Mode',
            icon: 'pi pi-pen-to-square',
          },
          {
            label: 'Auto Fill',
            icon: 'pi pi-microchip'
          },
          {
            label: 'Collision Check',
            icon: 'pi pi-check-circle'
          }
        ]
      },
      { separator: true },
      {
        label: 'Print',
        items: [
          {
            label: 'Export Plan (all)',
            icon: 'pi pi-folder'
          },
          {
            label: 'Export Plan (each)',
            icon: 'pi pi-folder-open'
          }
        ]
      },
      { separator: true },
      {
        label: 'Data',
        items: [
          {
            label: 'Edit Room list',
            icon: 'pi pi-warehouse'
          },
          {
            label: 'edit Course list',
            icon: 'pi pi-book'
          },
          {
            label: 'define Status',
            icon: 'pi pi-check-square'
          }
        ]
      },
      { separator: true }
    ];
  }
}
