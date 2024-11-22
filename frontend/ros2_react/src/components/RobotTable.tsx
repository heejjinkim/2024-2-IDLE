import { Box, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from "@mui/material"
import { Robot } from "../model/Robot"
import { StatusDot, StatusIndicator } from "./status"
import { RobotState } from "../api/type"

// Todo : RobotStateTable(robots : Robot[]) 이런식으로 바꿔야 함
export function RobotStateTable( {robots} : { robots : RobotState[]}) {

    function getRobotNowTask(robot : RobotState) : string {
        if (robot.tasks != null && robot.tasks.length > 0) {
            return robot.tasks[0].item;
        }
        return "작업 없음";
    }

    function getRobotNextTask(robot : RobotState) : string {
        if (robot.tasks != null && robot.tasks.length > 1) {
            var result : string = '';
            for (let i = 1; i < robot.tasks.length; i++) {
                if(i==1){
                    result += robot.tasks[i].item;
                }else{
                    result = result + ", " + robot.tasks[i].item;
                }
            }
            return result;
        }
        return "작업 없음";
    }

    return (
        <Box sx = {{width : '100%', mb : 20}}>
            <TableContainer component={Paper} sx = {{display: 'flex', justifyContent: 'center'}}> 
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell sx={{ textAlign: 'center' }}>로봇 이름</TableCell>
                            <TableCell sx={{ textAlign: 'center' }}>현재 작업</TableCell>
                            <TableCell sx={{ textAlign: 'center' }}>다음 작업</TableCell>
                            <TableCell sx={{ textAlign: 'center' }}>작업여부</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {
                            robots.map((robot) => (
                                <TableRow key={robot.name}>
                                    <TableCell sx={{ textAlign: 'center' }}>{robot.name}</TableCell>
                                    <TableCell sx={{ textAlign: 'center' }}>{getRobotNowTask(robot)}</TableCell>
                                    <TableCell sx={{ textAlign: 'center' }}>{getRobotNextTask(robot)}</TableCell>
                                    <TableCell sx={{ textAlign: 'center' }}>
                                        <StatusIndicator connected={robot.working}>
                                            <StatusDot connected={robot.working} />
                                            {robot.working ? '작업중' : '대기중'}
                                        </StatusIndicator>
                                    </TableCell>
                                </TableRow>
                            ))
                        }
                    </TableBody>
                </Table>
            </TableContainer>
        </Box>
    )
}