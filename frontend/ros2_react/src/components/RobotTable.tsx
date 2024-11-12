import { Box, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from "@mui/material"
import { Robot } from "../model/Robot"
import { StatusDot, StatusIndicator } from "./status"

// Todo : RobotStateTable(robots : Robot[]) 이런식으로 바꿔야 함
export function RobotStateTable() {
    const robots : Robot[] = [
        {
            id : 1,
            name: 'Robot 1',
            nowProcessing: null,
            nextProcessing: null,
            isWorking: false
        },
        {
            id : 2,
            name: 'Robot 2',
            nowProcessing: null,
            nextProcessing: null,
            isWorking: false
        },
        {
            id : 3,
            name: 'Robot 3',
            nowProcessing: null,
            nextProcessing: null,
            isWorking: false
        }
    ]
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
                                <TableRow key={robot.id}>
                                    <TableCell sx={{ textAlign: 'center' }}>{robot.name}</TableCell>
                                    <TableCell sx={{ textAlign: 'center' }}>{'없음'}</TableCell>
                                    <TableCell sx={{ textAlign: 'center' }}>{'없음'}</TableCell>
                                    <TableCell sx={{ textAlign: 'center' }}>
                                        <StatusIndicator connected={robot.isWorking}>
                                            <StatusDot connected={robot.isWorking} />
                                            {robot.isWorking ? '작업중' : '대기중'}
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