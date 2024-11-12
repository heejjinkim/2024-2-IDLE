import React, { useState, useEffect, useRef } from 'react';
import { theme } from '../components/theme';
import { 
  AppBar, Toolbar, Typography, Container, Grid, Card, CardHeader, CardContent, Select, MenuItem, SelectChangeEvent, ThemeProvider, 
  CssBaseline, Box,
  Divider,
  Button
} from '@mui/material';
import {StatusIndicator, StatusDot} from '../components/status'; 
import ROSLIB from 'roslib';
import { Map, Navigation, Thermometer } from 'lucide-react';
import { IconWrapper } from '../components/icon';
import { MapArea } from '../components/map';
import { base64ToUint8Array } from '../util/base64ToUnit8Array';
import { Robot } from '../model/Robot';
import { VariableBox, VariableBoxProps } from '../components/box';

export default function HomePage() {
  const [mapImage, setMapImage] = useState<string|null>(null);
  const [rosConnection, setRosConnection] = useState<boolean>(false);
  const [robots, setRobots] = useState<Robot[]>([
    { namespace: 'tb1', destination: '', battery: 80 },
    { namespace: 'tb2', destination: '', battery: 65 },
    { namespace: 'tb3', destination: '', battery: 90 },
  ]);

  const [ros, setRos] = useState<ROSLIB.Ros | null>(null);
  const [isRunningSimulation, setIsRunningSimulation] = useState<boolean>(false);
  const [shipProcess, setShipProcess] = useState<VariableBoxProps>({
    value1: 0,
    value2: 0
  });

  useEffect(() => {
      const rosInstance = new ROSLIB.Ros({
          url: 'ws://localhost:9090'
      });

      rosInstance.on('connection', () => {
          console.log('Connected to ROS');
          setRosConnection(true);
      });

      rosInstance.on('error', (error) => {
          console.log('Error connecting to ROS: ', error);
      });

      rosInstance.on('close', () => {
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

      
      setRos(rosInstance);
  }, []);

  const handleDestinationChange = (robotId: string, event: SelectChangeEvent<string>) => {
    const destination = event.target.value;
    setRobots(robots.map(robot => 
      robot.namespace === robotId ? { ...robot, destination } : robot
    ));

    sendGoal(robotId,destination);
  };

  function sendGoal(nameSpace: string, destination : string) {
    if(!ros) return;
    var destinationX = 0.0;
    var destinationY = 0.0;
    const actionName = '/' + nameSpace + '/goal_pose';

    if (destination === 'destination1') {
      destinationX = -1;
      destinationY = -1.5;
    } else if( destination === 'destination2') {
      destinationX = -1;
      destinationY = 1.5;
    }else if( destination === 'destination3') {
      destinationX = 1;
      destinationY = -1.5;
    }else if( destination === 'destination4') {
      destinationX = -3.0;
      destinationY = 1.0;
    }
    else if( destination === 'destination5') {
      destinationX = -3.0;
      destinationY = 0.0;
    }
    else if( destination === 'destination6') {
      destinationX = -3.0;
      destinationY = -1.0;
    }
    
    const goalTopic = new ROSLIB.Topic({
      ros: ros,
      name: actionName,  
      messageType: 'geometry_msgs/PoseStamped'  
    });

    // 목표 메시지를 정의합니다.
    const goalMessage = {
      header: {
        frame_id: 'map'  // 목표를 기준으로 하는 프레임
      },
      pose: {
        position: {
          x: destinationX,  // 목표 위치의 x 좌표
          y: destinationY,  // 목표 위치의 y 좌표
          z: 0.0  // 목표 위치의 z 좌표
        },
        orientation: {
          x: 0.0,  // 목표 방향의 x 성분
          y: 0.0,  // 목표 방향의 y 성분
          z: 0.0,  // 목표 방향의 z 성분
          w: 1.0  // 목표 방향의 w 성분 (쿼터니언)
        }
      }
    };

    // 목표를 전송합니다.
    goalTopic.publish(new ROSLIB.Message(goalMessage));
    goalTopic.unsubscribe();

    console.log('Goal sent:', goalMessage);

    console.log('전송 완료');
  }

  function onClickSimulationButton(){
    // Todo : 시뮬레이션 실행 API 호출
    setIsRunningSimulation(true);
    console.log('시뮬레이션 실행');
  }
  function getShipProcess(){
    // Todo : 배송 현황 API 호출
    setShipProcess(prevValue => ({
      value1: 10,
      value2: 20
    }));
  }

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Box sx={{ minHeight: '100vh', backgroundColor: 'background.default', width: '100%' }}>
        <AppBar position="static" color="primary" elevation={0}>
          <Toolbar>
            <Box sx={{ display: 'flex', justifyContent : 'space-between', width: '100%',  alignItems: 'center' }}>
              <Typography component="h1" variant="subtitle1" sx={{fontWeight : 'bold'}}>IDLE_FMS</Typography>
             <StatusIndicator connected={rosConnection}>
                <StatusDot connected={rosConnection} />
                {rosConnection ? '연결중' : '연결 없음'}
              </StatusIndicator>
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
        <Container maxWidth={false} sx={{ mt: 4, px: 4, width: { xs: '100%',  md: '85%',},}}>

          <Box sx={{width : '100%'}}>
            <Typography variant="h6" sx={{ fontWeight: 'bold', mb: 14, color:'text.primary', textAlign:'left' }}>
              실시간 모니터링 화면
            </Typography>
          </Box> 

          <Grid item xs={12} lg={8}>
              <Card>
                <CardHeader
                  title={
                    <Typography variant="h6">
                      <IconWrapper><Map size={20} /></IconWrapper>
                      ROS2 맵
                    </Typography>
                  }
                />
                <CardContent sx={{ p: 2 }}>
                  <MapArea>
                    {mapImage && (
                        <img
                          src={mapImage}
                          alt="Robot Map"
                          style={{ height: '100%', width: 'auto', margin: 'auto', position: 'absolute',top: 0, bottom: 0, left: 0, right: 0 }}
                        />
                      )}
                  </MapArea>
                </CardContent>
              </Card>
          </Grid>

          <Box sx={{width : '100%'}}>
            <Typography variant="h6" sx={{ fontWeight: 'bold', mb: 14, color:'text.primary', textAlign:'left' }}>
              피킹 스테이션 상태
            </Typography>
          </Box> 

          <Box sx={{width : '100%'}}>
            <Typography variant="h6" sx={{ fontWeight: 'bold', mb: 14, color:'text.primary', textAlign:'left' }}>
              로봇 상태
            </Typography>
          </Box> 

          <Grid container spacing={3}>
            <Grid item xs={12} lg={4}>
              <Card>
                <CardHeader
                  title={
                    <Typography variant="h6">
                      <IconWrapper><Navigation size={20} /></IconWrapper>
                      로봇 관리
                    </Typography>
                  }
                />
                <CardContent>
                  {robots.map(robot => (
                    <Box key={robot.namespace} sx={{ mb: 3, '&:last-child': { mb: 0 } }}>
                      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 1 }}>
                        <Typography variant="subtitle1" sx={{ fontWeight: 'medium' }}>
                          {robot.namespace}
                        </Typography>
                      </Box>
                      <Select
                        value={robot.destination}
                        onChange={(event: SelectChangeEvent<string>) => handleDestinationChange(robot.namespace, event)}
                        fullWidth
                        size="small"
                      >
                        <MenuItem value="">목적지 선택</MenuItem>
                        <MenuItem value="destination1">A구역</MenuItem>
                        <MenuItem value="destination2">B구역</MenuItem>
                        <MenuItem value="destination3">C구역</MenuItem>
                        <MenuItem value="destination4">E구역</MenuItem>
                        <MenuItem value="destination5">F구역</MenuItem>
                        <MenuItem value="destination6">G구역</MenuItem>
                      </Select>
                    </Box>
                  ))}
                </CardContent>
              </Card>
            </Grid>
          </Grid>
        </Container>
      </Box>
    </ThemeProvider>
  );
};
