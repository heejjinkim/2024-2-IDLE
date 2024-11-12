import { Typography, Box } from '@mui/material';
import { styled } from '@mui/system';

export interface VariableBoxProps {
  value1 : number;
  value2 : number;
}

export const GreyBox = styled('span')(({ theme }) => ({
    backgroundColor: theme.palette.grey[200],
    color: theme.palette.grey[800],
    padding: theme.spacing(0.5, 1),
    borderRadius: '16px',
    fontSize: '0.75rem',
    fontWeight: 'medium',
  }));

export function VariableBox({value1, value2} :VariableBoxProps) {
    return (
        <Box
          sx={{
            border: '1px solid black',
            borderRadius: '4px',
            justifyContent: 'center',
            alignItems: 'center',
            padding: '7px 16px',
            fontSize: '0.875rem',
            fontWeight : 'bold',
            color: 'black',
            display : 'inline-block',
            flexDirection: 'column',
          }}
        >
            당일배송 {value1}건 / 일반배송 {value2}건
        </Box>
    );
}