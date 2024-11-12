import { createTheme } from '@mui/material/styles';

export const theme = createTheme({
    palette: {
      mode: 'light',
      primary: {
        main: '#ffffff',
      },
      secondary: {
        main: '#000000',
      },
      background: {
        default: '#ffffff',
        paper: '#ffffff',
      },
    },
    typography: {
      fontFamily: "'Noto Sans KR', sans-serif",
      h1: {
        color: '#000000', 
      },
      h2: {
        color: '#000000', 
      },
    },
  });