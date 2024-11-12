import { styled } from '@mui/system';
import { Box } from '@mui/material';

export const MapArea = styled(Box)(({ theme }) => ({
    height: '500px',
    backgroundColor: theme.palette.grey[100],
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    border: `1px solid ${theme.palette.grey[300]}`,
    borderRadius: theme.shape.borderRadius,
    position: 'relative',
  }));