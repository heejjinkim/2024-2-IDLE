import { Work } from "./Work";

export interface Robot {
    id : number;
    name : string;
    nowProcessing : Work | null;
    nextProcessing : Work[] | null;
    isWorking : boolean;
  }