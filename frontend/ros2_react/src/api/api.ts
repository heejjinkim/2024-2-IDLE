import axios from "axios";
import { getDeliveryProcessResponse, getPickingStationResponse, getRobotStateResponse } from "./type";

function getApiUrl(path: string) : string {
    const baseUrl = `http://localhost:5000${path}`;
    const now = new Date();
    const formattedTime = `${now.getHours().toString().padStart(2, '0')}:${now.getMinutes().toString().padStart(2, '0')}:${now.getSeconds().toString().padStart(2, '0')}:${now.getMilliseconds().toString().padStart(3, '0')}`;
    console.log(`[${formattedTime}] request: ${baseUrl}`);  
    return baseUrl;
}

// 시뮬레이션 실행 요청 POST
export async function startSimulation() : Promise<boolean> {
    let responseData:number = 0;
    try{
        const response = await axios.post(
            getApiUrl('/simulator/start'),
        );
        responseData = response.status;
    }catch(error){
        if (axios.isAxiosError(error) && error.response) {
            responseData = error.response.data;
        }
    }
    return responseData === 200;
}

// 주문 추가 기능 POST
export async function addOrders() : Promise<boolean> {
    let responseData:number = 0;
    try{
        const response = await axios.post(
            getApiUrl('/simulator/add/orders'),
        );
        responseData = response.status;
    }catch(error){
        if (axios.isAxiosError(error) && error.response) {
            responseData = error.response.data;
        }
    }
    return responseData === 200;
}

// 당일배송 일반배송 처리량 GET
export async function getDeliveryProcess() : Promise<getDeliveryProcessResponse>{
    let responseData:getDeliveryProcessResponse = {
        standardDeliveryCount: 0,
        sameDayDeliveryCount: 0
    };
    try{
        const response = await axios.get(
            getApiUrl('/simulator/delivery'),
        );
        responseData = response.data;
    }catch(error){
        if (axios.isAxiosError(error) && error.response) {
            console.log(error.response.data);
        }
    }
    return responseData;
}
// 피킹 스테이션 상태 GET
export async function getPickingStation() : Promise<getPickingStationResponse> {
    let responseData:getPickingStationResponse = {
        pickingStations: []
    };
    try{
        const response = await axios.get(
            getApiUrl('/simulator/station'),
        );
        responseData = response.data;
    }catch(error){
        if (axios.isAxiosError(error) && error.response) {
            console.log(error.response.data);
        }
    }
    return responseData;
}

// 로봇 상태 GET
export async function getRobots() : Promise<getRobotStateResponse> {
    let responseData:getRobotStateResponse = {
        robots: []
    };
    try{
        const response = await axios.get(
            getApiUrl('/simulator/robots'),
        );
        responseData = response.data;
    }catch(error){
        if (axios.isAxiosError(error) && error.response) {
            console.log(error.response.data);
        }
    }
    return responseData;
}