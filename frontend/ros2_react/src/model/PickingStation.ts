import { Order } from "./Order";

export interface PickingStation {
    id : number;
    name: string;
    orders : Order[] | null;
}