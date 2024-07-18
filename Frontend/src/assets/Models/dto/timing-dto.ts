import LocalTime from "ts-time/LocalTime";

interface TimingDTO {
  id: number;
  startTime: string;  // in HH:mm:ss format
  endTime: string;    // in HH:mm:ss format
  day: string;
  timingType: string;
}

function parseTime(timeString: string): LocalTime {
  return LocalTime.fromString(timeString);  // Assuming timeString is in HH:mm:ss format
}

function convertEventInputToTiming(event: EventImpl): TimingDTO {
  let timing: TimingDTO = {
    id: 0,
    startTime: '',
    endTime: '',
    day: '',
    timingType: event.title
  };

  if (event.start) {
    timing.startTime = event.start.toISOString().substring(11, 19); // Get HH:mm:ss from ISO string
  }

  if (event.end) {
    timing.endTime = event.end.toISOString().substring(11, 19); // Get HH:mm:ss from ISO string
  }

  if (event.start) {
    timing.day = event.start.getDay().toString();
  }

  return timing;
}
