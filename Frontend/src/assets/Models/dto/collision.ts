import {CollisionType} from "../enums/collision-type";

export interface CollisionResponse {
  [courseSessionId: number]: CollisionType[];
}
