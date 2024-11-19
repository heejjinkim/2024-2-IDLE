import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './App.css';
import HomePage from './pages/HomePage';
import StationPage from './pages/StationPage';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/station/:id" element={<StationPage/>} />
          </Routes>
        </BrowserRouter>
      </header>
    </div>
  );
}

export default App;
