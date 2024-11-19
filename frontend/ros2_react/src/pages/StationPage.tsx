import { AppBar, Box, CssBaseline, Divider, ThemeProvider, Toolbar, Typography, Container, TableContainer, Table, TableHead, TableRow, TableCell, TableBody, Paper } from "@mui/material";
import { useParams } from "react-router-dom"
import { theme } from "../components/theme";
import { PickingStation } from "../model/PickingStation";
import { useState } from "react";
import { getPickingStation } from "../api/api";

export default function StationPage() {
    const {id} = useParams();
    const [station, setStation] = useState<PickingStation | null>(null);

    async function getStation() {
        const result = await getPickingStation();
        setStation(result.pickingStations.find((station) => station.id === Number(id)) || null);
    }

    return (
        <ThemeProvider theme={theme}>
          <CssBaseline />
          <Box sx={{ minHeight: '100vh', backgroundColor: 'background.default', width: '100%' }}>
            <AppBar position="static" elevation={0}
              sx={{
                color: 'white',
                backgroundColor : '#00c49a'
              }}
            >
                <Toolbar>
                    <Typography component="h1" variant="subtitle1" sx={{fontWeight : 'bold'}}>IDLE_FMS</Typography>
                </Toolbar>
                <Divider sx={{ backgroundColor: 'black.100' }} />
            </AppBar>
            <Container maxWidth={false} sx={{ mt: 4, px: 4, width: { xs: '100%',  md: '80%',},}}>
                <Box sx={{width : '100%'}}>
                    <Typography variant="h6" sx={{ fontWeight: 'bold', mb: 2, color:'text.primary', textAlign:'left' }}>
                        {station?.name} 주문처리 상세정보
                    </Typography>
                </Box> 

                <TableContainer component={Paper} sx = {{display: 'flex', justifyContent: 'center'}}> 
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell sx={{ textAlign: 'center' }}>주문 ID</TableCell>
                            <TableCell sx={{ textAlign: 'center' }}>처리된 작업 수</TableCell>
                            <TableCell sx={{ textAlign: 'center' }}>총 작업 수</TableCell>
                            <TableCell sx={{ textAlign: 'center' }}>완료율</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {station?.orders && station.orders.map((order) => (
                                <TableRow key={order.id}>
                                    <TableCell sx={{ textAlign: 'center' }}>{order.id}</TableCell>
                                    <TableCell sx={{ textAlign: 'center' }}>{order.completedItemCount}</TableCell>
                                    <TableCell sx={{ textAlign: 'center' }}>{order.originalItemCount}</TableCell>
                                    <TableCell sx={{ textAlign: 'center' }}>{Math.floor(order.completedItemCount / order.originalItemCount * 100)}%</TableCell>
                                </TableRow> ))}
                    </TableBody>
                </Table>
            </TableContainer> 
            </Container>  
          </Box>
        </ThemeProvider>
      );
}