import { styled } from '@mui/system';

export const StatusIndicator = styled('span')<{ connected: boolean }>(({ theme, connected }) => ({
    display: 'inline-flex',
    alignItems: 'center',
    color: connected ? theme.palette.success.main : theme.palette.error.main,
    fontSize : '14px',
    fontWeight: 'bold'
  }));
  
export const StatusDot = styled('span')<{ connected: boolean }>(({ theme, connected }) => ({
    width: 8,
    height: 8,
    borderRadius: '30%',
    backgroundColor: connected ? theme.palette.success.main : theme.palette.error.main,
    marginRight: theme.spacing(1),
  }));