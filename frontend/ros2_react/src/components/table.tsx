import { Box, Paper, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography } from "@mui/material";
import { PickingStation } from "../model/PickingStation";
import { useNavigate } from "react-router-dom";

export function PickingStationTable() {
    const navigate = useNavigate();
    const stations : PickingStation[] = [
        {id: 1, name: 'Station A', orders: null},
        {id: 2, name: 'Station B', orders: null},
        {id: 3, name: 'Station C', orders: null},
    ]

    function handleStationClick(station : PickingStation) {
        console.log(`Station ${station.name} is clicked`);
        const url = window.location.origin + `/station/${station.id}`;
        window.open(url, '_blank'); 
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

