import { Box, Paper, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography } from "@mui/material";
import { PickingStation } from "../model/PickingStation";

export function PickingStationTable() {
    const stations : PickingStation[] = [
        {id: 1, name: 'A Pikcing Station', orders: null},
        {id: 2, name: 'B Picking Station', orders: null},
        {id: 3, name: 'C Picking Station', orders: null},
    ]

    function handleStationClick(station : PickingStation) {
        console.log(`Station ${station.name} is clicked`);
    }

    return (
        <Box sx = {{width : '100%', mb : 10}}>
            {
                stations.map((station) => (
                    <Paper
                        onClick = {() => handleStationClick(station)}
                        key={station.id}
                        sx={{
                            border: '1px solid black',
                            borderRadius: '4px',
                            padding: '16px',
                            marginBottom: 2,
                            cursor : 'pointer'
                          }}
                    >
                        <Box
                            sx={{
                                display: 'flex',
                                justifyContent: 'space-between',
                                alignItems: 'center',
                            }}
                        >
                            <Typography variant="body1">{station.name}</Typography>
                            <Typography variant="body1">{'>'}</Typography>
                        </Box>
                    </Paper>
                ))
            }
        </Box>
    );
}

