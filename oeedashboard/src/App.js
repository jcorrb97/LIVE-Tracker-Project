import './App.css';
import React, { useEffect, useState } from "react";
import ButtonAppBar from './components/Appbar';
import InteractiveCard from './components/Card';

function App() {
	
	const [entry, setEntry] = useState([]);
	
	useEffect(() => {
		fetch("http://10.104.50.92:8080/getLive")
			.then(resp => resp.json())
			.then(data => setEntry(data))
	}, []);
	
  return (
    <div className="App">
    <ButtonAppBar/>
    <InteractiveCard entry={entry}/>
    </div>
  );
}

export default App;