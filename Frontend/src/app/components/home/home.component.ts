import {Component, OnInit} from '@angular/core';
import {MenuItem} from "primeng/api";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',

})
export class HomeComponent implements OnInit {
  items: MenuItem[] | undefined;
  availableTables!: any;
  responsiveOptions: any[] | undefined;

  ngOnInit() {
    this.responsiveOptions = [
      {
        breakpoint: '1199px',
        numVisible: 1,
        numScroll: 1
      },
      {
        breakpoint: '991px',
        numVisible: 2,
        numScroll: 1
      },
      {
        breakpoint: '767px',
        numVisible: 1,
        numScroll: 1
      }
    ];
    this.availableTables = [
      {name: 'Wintersemester 22/23', workStatus: 'FINISHED'},
      {name: 'Sommersemester 23', workStatus: 'EDITED'},
      {name: 'Wintersemester 23/24', workStatus: 'FINISHED'},
      {name: 'Sommersemester 24', workStatus: 'IN WORK'},
      {name: 'Wintersemester 24/25', workStatus: 'UNDEFINED'},];
    this.items = [
      {separator: true},
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
      {separator: true},
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
      {separator: true},
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
      {separator: true},
      {
        label: 'Data',
        items: [
          {
            label: 'Filter',
            icon: 'pi pi-filter'
          }
        ]
      }
    ];
  }

  getSeverity(status: string) {
    switch (status) {
      case 'FINISHED':
        return 'success';
        break;
      case 'IN WORK':
        return 'warning';
      case 'EDITED':
        return 'warning';
      default:
        return 'danger';
    }
  }
}
