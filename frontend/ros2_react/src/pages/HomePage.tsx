import React, { useState, useEffect, useRef } from 'react';
import { theme } from '../components/theme';
import { 
  AppBar, Toolbar, Typography, Container, Grid, 
  SelectChangeEvent, ThemeProvider, 
  CssBaseline, Box,
  Divider,
  Button,
  TableContainer,
  Table,
  TableRow,
  TableCell,
  Paper,
  TableHead,
  TableBody,
  IconButton,
} from '@mui/material';
import {StatusIndicator, StatusDot} from '../components/status';
import RefreshIcon from '@mui/icons-material/Refresh';
import ROSLIB from 'roslib';
import { MapArea } from '../components/map';
import { base64ToUint8Array } from '../util/base64ToUnit8Array';
import { Robot } from '../model/Robot';
import { VariableBox, VariableBoxProps } from '../components/box';
import { PickingStationTable } from '../components/table';
import { RobotStateTable } from '../components/robotTable';
import { addOrders, getDeliveryProcess, getRobots, startSimulation } from '../api/api';
import { getDeliveryProcessResponse, RobotState } from '../api/type';

export default function HomePage() {
  const [mapImage, setMapImage] = useState<string|null>(null);
  const [rosConnection, setRosConnection] = useState<boolean>(false);


  const [ros, setRos] = useState<ROSLIB.Ros | null>(null);
  const [isRunningSimulation, setIsRunningSimulation] = useState<boolean>(false);
  const [shipProcess, setShipProcess] = useState<VariableBoxProps>({
    value1: 0,
    value2: 0
  });
  const [robotState, setRobotState] = useState<RobotState[]>([]);
  const rosRef = useRef<ROSLIB.Ros | null>(null);

  useEffect(() => {
      const rosInstance = new ROSLIB.Ros({
          url: 'ws://localhost:9090'
      });

      if (rosRef.current) {
        console.log('ROS already connected');
        setRosConnection(true);
        refresh()
        return;
      }

      rosInstance.on('connection', () => {
          console.log('Connected to ROS');
          setRosConnection(true);
      });

      rosInstance.on('error', (error) => {
          console.log('Error connecting to ROS: ', error);
      });

      rosInstance.on('close', () => {
          rosRef.current = null;
          setRosConnection(false);
          cameraTopic.unsubscribe();
          rosInstance.close();
          
          console.log('Connection to ROS closed');
      });

      const cameraTopic = new ROSLIB.Topic({
        ros: rosInstance,
        name: '/gazebo/fixed_camera/image_raw',
        messageType: 'sensor_msgs/msg/Image'
      });
  
      cameraTopic.subscribe((message) => {
        const messageData = message as any;
        const base64Data = messageData.data;
        const imageData = base64ToUint8Array(base64Data);
        const width = messageData.width;
        const height = messageData.height;
      
        const canvas = document.createElement("canvas");
        canvas.width = width;
        canvas.height = height;
        const ctx = canvas.getContext("2d");

        if (ctx) {
            const imgData = ctx.createImageData(width, height);

            let isAllGray = true;
            let minVal = 255, maxVal = 0;

            for (let i = 0; i < width * height; i++) {
                const r = imageData[i * 3];
                const g = imageData[i * 3 + 1];
                const b = imageData[i * 3 + 2];

                imgData.data[i * 4] = r;
                imgData.data[i * 4 + 1] = g;
                imgData.data[i * 4 + 2] = b;
                imgData.data[i * 4 + 3] = 255;

                if (r !== g || g !== b) {
                    isAllGray = false;
                }

                minVal = Math.min(minVal, r, g, b);
                maxVal = Math.max(maxVal, r, g, b);
            }

            ctx.putImageData(imgData, 0, 0);
            setMapImage(canvas.toDataURL());
        }
    });
      rosRef.current = rosInstance;
      setRos(rosInstance);
      refresh();
  }, []);

  async function onClickSimulationButton(){
    // Todo : 시뮬레이션 실행 API 호출
    const result = await startSimulation();
    if(result){
      setIsRunningSimulation(true);
      refresh()
      console.log('시뮬레이션 실행');
    }
  }

  async function onClickAddOrder(){
    // Todo : 시뮬레이션 실행 API 호출
    const result = await addOrders();
    if(result){
      refresh()
      alert('추가 완료되었습니다'); 
      console.log('주문 추가 완료');
    }
  }
  
  async function getShipProcess(){
    const result : VariableBoxProps = await getDeliveryProcess();
    setShipProcess(prevValue => ({
      value1: result.value1,
      value2: result.value2
    }));
  }

  async function getRobotState(){
    const result : RobotState[] = await getRobots();
    setRobotState(result);
  }

  function refresh(){
    getShipProcess();
    getRobotState();
  }

  useEffect(() => {
    console.log('shipProcess가 변경되었습니다:', shipProcess);
  }, [shipProcess]);

  useEffect(() => {
    console.log('robotState가 변경되었습니다:', robotState);
  }, [robotState]);


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
            <Box sx={{ display: 'flex', justifyContent : 'space-between', width: '100%',  alignItems: 'center' }}>
              <Typography component="h1" variant="subtitle1" sx={{fontWeight : 'bold'}}>IDLE_FMS</Typography>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                <IconButton onClick={refresh}>
                  <RefreshIcon color="primary" />
                </IconButton>
                <StatusIndicator connected={rosConnection}>
                    <StatusDot connected={rosConnection} />
                    {rosConnection ? '연결중' : '연결 없음'}
                </StatusIndicator>
            </Box>
            </Box> 
          </Toolbar>
          <Divider sx={{ backgroundColor: 'black.100' }} />
        </AppBar>
        <Container sx = {{
          paddingTop: 2,
          gap : 2,
          display: 'flex',
          justifyContent: 'flex-end',
          alignItems: 'center',
        }}>
           {
            isRunningSimulation ?
            (<Button sx= {{
              border: '1px solid black',
              borderRadius: '4px',
            }} color='secondary' variant='outlined'  onClick={onClickAddOrder}  disableRipple> 주문추가 </Button>) : (<div></div>)
          }

          {isRunningSimulation ?
          (<Button sx= {{
            border: '1px solid black',
            borderRadius: '4px',
          }} color='secondary' variant='outlined' disableRipple>실행중</Button>) : 
          (<Button sx= {{
            border: '1px solid black',
            borderRadius: '4px',
          }} color='secondary' variant='outlined' onClick={onClickSimulationButton} >시뮬레이션 실행</Button>)}
          <VariableBox value1={shipProcess.value1} value2={shipProcess.value2}/>
        </Container>
        <Container maxWidth={false} sx={{ mt: 4, px: 4, width: { xs: '100%',  md: '80%',},}}>
          <Box sx={{width : '100%'}}>
            <Typography variant="h6" sx={{ fontWeight: 'bold', mb: 2, color:'text.primary', textAlign:'left' }}>
              실시간 모니터링 화면
            </Typography>
          </Box> 
          <Grid item xs={12} lg={8} sx={{mb:10}}>
                  <MapArea>
                    {mapImage ? (
                        <img
                          src={mapImage}
                          alt="Robot Map"
                          style={{ height: '100%', width: 'auto', margin: 'auto', position: 'absolute',top: 0, bottom: 0, left: 0, right: 0 }}
                        />
                      ) : 
                      <Typography variant="h6" color="textSecondary">
                      ROS2와 연결해주세요
                    </Typography>}
                  </MapArea>
          </Grid>

          <Box sx={{width : '100%', mb : 2}}>
            <Typography variant="h6" sx={{ fontWeight: 'bold', color:'text.primary', textAlign:'left' }}>
              스테이션 상태
            </Typography>
          </Box>
          <PickingStationTable />
          <Box sx={{width : '100%', mb : 2}}>
            <Typography variant="h6" sx={{ fontWeight: 'bold', color:'text.primary', textAlign:'left' }}>
              로봇 상태
            </Typography>
          </Box>
          <RobotStateTable robots={robotState}/>
        </Container>
      </Box>
    </ThemeProvider>
  );
};
