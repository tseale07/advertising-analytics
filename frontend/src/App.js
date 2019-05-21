import React, {Component} from 'react';
import logo from './logo.jpeg';
import './App.css';
import ProductDataPage from './components/ProductDataPage';

class App extends Component {
    render() {
        return (
            <div className="App">
                <header className="App-header">
                    <img src={logo} className="App-logo" alt="logo"/>
                    <h1 className="App-title">Advertising Analytics</h1>
                </header>
                <ProductDataPage/>
            </div>
        );
    }
}

export default App;
