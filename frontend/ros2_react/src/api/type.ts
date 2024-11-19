import { PickingStation } from "../model/PickingStation"
import { Robot } from "../model/Robot"

export type getDeliveryProcessResponse = {
    // 일반배송
    standardDeliveryCount : number,
    // 당일배송
    sameDayDeliveryCount : number
}

export type getPickingStationResponse = {
    pickingStations : PickingStation[]
}

export type getRobotStateResponse = {
    robots : RobotState[]
}

export type RobotState = {
    name : string,
    working : boolean,
    tasks : Task[] | null
}

type Task = {
    id : number,
    item : string
}