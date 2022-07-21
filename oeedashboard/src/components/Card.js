import * as React from 'react';
import AspectRatio from '@mui/joy/AspectRatio';
import Card from '@mui/joy/Card';
import CardContent from '@mui/joy/CardContent';
import CardOverflow from '@mui/joy/CardOverflow';
import Typography from '@mui/joy/Typography';
import Link from '@mui/joy/Link';

  function SetupCard({entry:{id, department, linearFeet, startTime, workCenterID} }) {
	return (
	<Card
      row
      variant="outlined"
      sx={{
        minWidth: '260px',
        gap: 1,
        bgcolor: 'background.body',
      }}
    >
      <CardContent>
        <Typography fontWeight="md" textColor="success.plainColor" mb={0.5}>
        Produced: {linearFeet} LF
        
        </Typography>
        <Link
        	overlay
        	underline="none"
        	href="#interactive-card"
        	sx={{ color: 'text.tertiary' }}
        >
        </Link>
        <Typography level="body2">Start Time: {startTime}</Typography>
      </CardContent>
      <CardOverflow
        variant="soft"
        color="primary"
        sx={{
          px: 2,
          writingMode: 'vertical-rl',
          textAlign: 'center',
          fontSize: 'xs1',
          fontWeight: 'xl2',
          letterSpacing: '1px',
          textTransform: 'uppercase',
        }}
      >
      {workCenterID}
      <br/>
      {department}
      </CardOverflow>
    </Card>        
	);
} 


export default function InteractiveCard({entry}) {
  
  return (
	<table>
		<tbody>
			{ entry.map(entry => <SetupCard key={entry.id} entry={entry} />) }
		</tbody>
	</table>
	
  )
}