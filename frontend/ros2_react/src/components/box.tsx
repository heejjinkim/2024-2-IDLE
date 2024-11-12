import { styled } from '@mui/system';

export const GreyBox = styled('span')(({ theme }) => ({
    backgroundColor: theme.palette.grey[200],
    color: theme.palette.grey[800],
    padding: theme.spacing(0.5, 1),
    borderRadius: '16px',
    fontSize: '0.75rem',
    fontWeight: 'medium',
  }));
  