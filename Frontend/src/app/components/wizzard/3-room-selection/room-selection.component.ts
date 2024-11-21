import {Component, EventEmitter, Input, OnDestroy, Output} from '@angular/core';
import {Subscription} from "rxjs";
import {RoomService} from "../../../services/room-service";
import {Room} from "../../../../assets/Models/room";
import {getTableSettings} from "../wizard.component";

@Component({
  selector: 'app-room-selection',
  templateUrl: './room-selection.component.html',
  styleUrl: '../wizard.component.css'
})
export class RoomSelectionComponent implements OnDestroy {
  @Input() roomTables!: Room[];
  @Input() wizardMode: boolean = true;

  @Output() addRoomInParent: EventEmitter<Room> = new EventEmitter<Room>();
  @Output() removeRoomInParent: EventEmitter<Room> = new EventEmitter<Room>();

  courseSub: Subscription;
  availableRooms!: Room[];

  constructor(
    private roomService: RoomService,
  ) {
    this.courseSub = this.roomService.getAllRooms().subscribe(data => {
      // Filter out rooms that are already in roomTables
      const roomTableIds = new Set(this.roomTables.map(room => room.id));
      this.availableRooms = data.filter(room => !roomTableIds.has(room.id));
    });
  }

  ngOnDestroy(): void {
    this.courseSub.unsubscribe();
  }

  protected readonly String = String;
  protected readonly getTableSettings = getTableSettings;
}
