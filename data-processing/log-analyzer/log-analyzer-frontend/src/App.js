import React, { Component } from 'react';
import { Route, BrowserRouter } from 'react-router-dom';
import './App.css';
import Home from './Home';
import ViewReport from './ViewReport';

class App extends Component {
    render() {
        return (
            <div className="App">
                <BrowserRouter>

                    <Route exact path="/" component={Home} />

                    <Route path="/report/:reportId" component={ViewReport} />

                </BrowserRouter>
            </div>
        );
    }
}

export default App;
