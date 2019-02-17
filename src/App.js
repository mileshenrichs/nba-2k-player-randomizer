import React, { Component } from 'react';
import './App.css';
import Step from "./components/Step";
import TeamSizeButtons from "./components/TeamSizeButtons";
import RandomizationModeButtons from "./components/RandomizationModeButtons";
import AdditionalSettings from "./components/AdditionalSettings";

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            currentPointIndex: 0,
            teamSize: undefined,
            randomizationMode: undefined,
            currentPlayersOnly: false,
            yearWindow: {
                filterActive: false,
                fromYear: '',
                toYear: ''
            },
            minimumPlayerPPGActive: false,
            minimumPlayerPPG: '',
            preventDuplicatedPositions: false,
            canRandomize: true
        };
    }

    onTeamSizeSelect(teamSize) {
        this.setState({teamSize});
        this.advancePoint(0);
    }

    onRandomizationModeSelect(mode) {
        this.setState({randomizationMode: mode});
        this.advancePoint(1);
    }

    advancePoint(beyondPointIndex) {
        if(this.state.currentPointIndex <= beyondPointIndex) {
            this.setState({currentPointIndex: beyondPointIndex + 1});
        }
    }

    onChangeYear(yearKey, newValue) {
        if(!isNaN(newValue) && newValue.length <= 4) {
            this.setState(prevState => ({
                yearWindow: {
                    ...prevState.yearWindow,
                    [yearKey]: newValue
                }
            }));
        }
    }

    generateRosters() {
        this.setState({canRandomize: false});
        console.log('time to generate!');
    }

    render() {
        return (
            <div className="App">
                <div className="container">
                    <h1>NBA 2K19 Blacktop Player Randomizer</h1>

                    <Step title="Choose a team size" renderPoint={0} currentPointIndex={this.state.currentPointIndex}>
                        <TeamSizeButtons
                            teamSize={this.state.teamSize}
                            onTeamSizeSelect={size => this.onTeamSizeSelect(size)}
                        />
                    </Step>

                    <Step title="Choose a randomization mode" renderPoint={1} currentPointIndex={this.state.currentPointIndex}>
                        <RandomizationModeButtons
                            randomizationMode={this.state.randomizationMode}
                            onModeSelect={mode => this.onRandomizationModeSelect(mode)}
                        />
                    </Step>

                    <Step title="Additional settings" renderPoint={2} currentPointIndex={this.state.currentPointIndex}>
                        <AdditionalSettings
                            currentPlayersOnly={this.state.currentPlayersOnly}
                            yearWindow={this.state.yearWindow}
                            minimumPlayerPPGActive={this.state.minimumPlayerPPGActive}
                            minimumPlayerPPG={this.state.minimumPlayerPPG}
                            preventDuplicatedPositions={this.state.preventDuplicatedPositions}
                            onToggleCurrentPlayersOnly={() => this.setState(prevState => ({
                                currentPlayersOnly: !prevState.currentPlayersOnly
                            }))}
                            onToggleYearWindowActive={() => {
                                this.setState(prevState => ({
                                    yearWindow: {
                                        ...prevState.yearWindow,
                                        filterActive: !prevState.yearWindow.filterActive
                                    }
                                }))
                            }}
                            onChangeFromYear={year => this.onChangeYear('fromYear', year)}
                            onChangeToYear={year => this.onChangeYear('toYear', year)}
                            onToggleMinPlayerPPG={() => this.setState(prevState => ({
                                minimumPlayerPPGActive: !prevState.minimumPlayerPPGActive
                            }))}
                            onChangeMinPlayerPPG={minPPG => this.setState({minimumPlayerPPG: minPPG})}
                            onTogglePreventDuplicatedPositions={() => this.setState(prevState => ({
                                preventDuplicatedPositions: !prevState.preventDuplicatedPositions
                            }))}
                        />
                    </Step>

                    <Step renderPoint={2} currentPointIndex={this.state.currentPointIndex}
                          style={{display: 'flex', justifyContent: 'center'}}>
                        <button className="randomize-button active"
                            disabled={!this.state.canRandomize} onClick={() => this.generateRosters()}>
                            Randomize
                        </button>
                    </Step>
                </div>
            </div>
        );
    }
}

export default App;
