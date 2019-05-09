import React, { Component } from 'react';
import './App.css';
import UsageLogger from './logging/UsageLogger';
import loaderSrc from './assets/loader.gif';
import errorIconSrc from './assets/error.png';
import Step from "./components/Step";
import TeamSizeButtons from "./components/TeamSizeButtons";
import RandomizationModeButtons from "./components/RandomizationModeButtons";
import AdditionalSettings from "./components/AdditionalSettings";
import TeamGenerator from './compute/TeamGenerator';
import TeamRoster from "./components/TeamRoster";

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            currentPointIndex: 0,
            teamSize: undefined,
            randomizationMode: undefined,
            currentPlayersOnly: true,
            yearWindow: {
                filterActive: false,
                fromYear: '',
                toYear: ''
            },
            minimumPlayerPPGActive: false,
            minimumPlayerPPG: '',
            minimumPlayerRatingActive: false,
            minimumPlayerRating: '',
            canRandomize: true,
            team1: [],
            team2: [],
            error: undefined,
            fattyMode: false
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
        this.setState({
            canRandomize: false,
            team1: [],
            team2: [],
            error: undefined
        });
        this.advancePoint(2);

        const settings = JSON.parse(JSON.stringify(this.state));
        delete settings.currentPointIndex;
        delete settings.canRandomize;
        delete settings.team1;
        delete settings.team2;

        try {
            const generator = new TeamGenerator(settings);
            let team1, team2;
            if(this.state.fattyMode) {
                const teams = generator.generateFatties();
                team1 = teams.team1;
                team2 = teams.team2;
            } else {
                const teams = generator.generateTeams();
                team1 = teams.team1;
                team2 = teams.team2;
            }
            this.setState({team1, team2}, () => {
                this.initTeamReveal();
                UsageLogger.sendLog(this.state);
            });
        } catch(err) {
            this.setState({
                error: err.message,
                canRandomize: true
            });

            // scroll down to make error more visible
            setTimeout(() => {
                const additionalSettings = document.querySelector('.AdditionalSettings');
                const topScrollPixel = additionalSettings.getBoundingClientRect().top - 100;
                window.scrollBy({
                    left: 0,
                    top: topScrollPixel,
                    behavior: 'smooth'
                });
            }, 100);
        }

    }

    initTeamReveal() {
        this.initAnimationInfoToPlayerStates();

        const topScrollPixel = this.randomizeButton.getBoundingClientRect().top - 40;
        window.scrollBy({
            left: 0,
            top: topScrollPixel,
            behavior: 'smooth'
        });

        // wait a few seconds, then incrementally reveal players on each team
        let i = 0;
        const revealInterval = setInterval(() => {
            if(i < this.state.team1.length) {
                this.setState(prevState => ({
                    team1: [
                        ...prevState.team1.slice(0, i),
                        {
                            ...prevState.team1[i],
                            reveal: true
                        },
                        ...prevState.team1.slice(i + 1)
                    ]
                }));
            } else if(i < this.state.team2.length * 2) {
                this.setState(prevState => ({
                    team2: [
                        ...prevState.team2.slice(0, i % this.state.team2.length),
                        {
                            ...prevState.team2[i % this.state.team2.length],
                            reveal: true
                        },
                        ...prevState.team2.slice(i % this.state.team2.length + 1)
                    ]
                }));
            }

            i++;

            if(i >= this.state.team1.length * 2) {
                setTimeout(() => {
                    this.setState({canRandomize: true});
                }, 1500);
                clearInterval(revealInterval);
            }
        }, 2000);
    }

    initAnimationInfoToPlayerStates() {
        const team1Copy = JSON.parse(JSON.stringify(this.state.team1));
        const team2Copy = JSON.parse(JSON.stringify(this.state.team2));

        team1Copy.forEach(player => {
            player.reveal = false;
        });
        team2Copy.forEach(player => {
            player.reveal = false;
        });

        this.setState({team1: team1Copy, team2: team2Copy});
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
                            minimumPlayerRatingActive={this.state.minimumPlayerRatingActive}
                            minimumPlayerRating={this.state.minimumPlayerRating}
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
                            onToggleMinPlayerRating={() => this.setState(prevState => ({
                                minimumPlayerRatingActive: !prevState.minimumPlayerRatingActive
                            }))}
                            onChangeMinPlayerRating={minRating => this.setState({minimumPlayerRating: minRating})}
                        />
                    </Step>

                    {this.state.error &&
                        <div className="error">
                            <img src={errorIconSrc} alt="" />
                            <span>{this.state.error}</span>
                        </div>
                    }

                    <Step renderPoint={2} currentPointIndex={this.state.currentPointIndex}
                          style={{display: 'flex', justifyContent: 'center'}}>
                        <button className="randomize-button active" ref={node => this.randomizeButton = node}
                            disabled={!this.state.canRandomize} onClick={() => this.generateRosters()}>
                            {this.state.canRandomize && 'Randomize'}
                            {!this.state.canRandomize &&
                                <img src={loaderSrc} alt="Randomizing..." />}
                        </button>
                    </Step>

                    <Step renderPoint={3} currentPointIndex={this.state.currentPointIndex}>
                        <div className="teams-container">
                            <TeamRoster teamNo={1} players={this.state.team1} />
                            <TeamRoster teamNo={2} players={this.state.team2} />
                        </div>
                    </Step>
                </div>

                {/*<div className="footer">*/}
                    {/*Created by <a href="https://github.com/mileshenrichs">Miles Henrichs</a>*/}
                {/*</div>*/}
            </div>
        );
    }
}

export default App;
