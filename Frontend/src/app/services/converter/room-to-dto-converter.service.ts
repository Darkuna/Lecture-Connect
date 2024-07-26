import { Injectable } from '@angular/core';
import {TimingDTO} from "../../../assets/Models/dto/timing-dto";
import {RoomDTO} from "../../../assets/Models/dto/room-dto";
import {EventConverterService} from "./event-converter.service";
import {Room} from "../../../assets/Models/room";

@Injectable({
  providedIn: 'root'
})
export class RoomToDtoConverterService {
  constructor(
    private converter: EventConverterService
  ) {
  }
  convertRoomsToDto(rooms: Room[]){
    let newDto: RoomDTO[] = [];
    let timing: TimingDTO | null = null;
    let roomDto: RoomDTO;
    let constraints: TimingDTO[];

    rooms.forEach(r => {
      roomDto = new RoomDTO();
      constraints = [];
      r.tmpEvents?.forEach(e => {
        timing = this.converter.convertEventInputToTiming(e);

        constraints.push(timing);
      })

      roomDto.id = r.id;
      roomDto.timingConstraints = constraints;
      roomDto.capacity = r.capacity;
      roomDto.computersAvailable = r.computersAvailable;
      roomDto.updatedAt = r.updatedAt;
      roomDto.createdAt = r.createdAt;

      newDto.push(roomDto);
    })

    return newDto;
  }

}
