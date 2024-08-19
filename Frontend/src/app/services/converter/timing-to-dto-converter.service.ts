import { Injectable } from '@angular/core';
import {Timing} from "../../../assets/Models/timing";
import {TimingDTO} from "../../../assets/Models/dto/timing-dto";

@Injectable({
  providedIn: 'root'
})
export class TimingToDtoConverterService {
  convertTimingToDTO(timing: Timing): TimingDTO {

    return {
      id: timing.id,
      timingType: timing.timingType,
      startTime: timing.startTime?.toString()!,
      endTime: timing.endTime?.toString()!,
      day: timing.day!
    };
  }

  convertMultiple(timings: Timing[]): TimingDTO[]{
    let newDto: TimingDTO[] = [];

    timings.forEach(t => {
      newDto.push(this.convertTimingToDTO(t));
    })

    return newDto;
  }
}
