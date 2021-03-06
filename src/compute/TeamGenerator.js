import playerData from './players.json';

export default class TeamGenerator {

    constructor(settings) {
        // throw error if settings are invalid
        validateSettings(settings);
        this.settings = settings;
    }

    generateFatties() {
        let { players } = playerData;
        const fatty = players.find(p => p.name === 'Fat Lever');
        const team = [];
        for(let i = 0; i < this.settings.teamSize; i++) {
            team.push(fatty);
            team[i].versionIndex = Math.floor(Math.random() * fatty.versions.length);
        }

        return {
            team1: team,
            team2: team
        }
    }

    /**
     * Generates teams given the constraints of the current settings.
     * @returns {{team1: Array of players, team2: Array of players}}
     */
    generateTeams() {
        const numPlayers = this.settings.teamSize * 2;

        // 1. filter players to form draft pool
        const playerPool = this.getPlayerPool();
        console.log(playerPool.length + ' players in pool');

        // 2. validate pool size
        if(playerPool.length <= numPlayers) {
            throw new Error('Your filters are too strict!  The game will have ' + numPlayers + ' players, ' +
                'but there are only ' + playerPool.length + ' players that could possibly be selected.');
        }

        // 3. build teams
        const team1 = [];
        const team2 = [];
        const selectedPlayerIndices = [];

        while(team1.length + team2.length < numPlayers) {
            const selectionIndex = Math.floor(Math.random() * playerPool.length);
            if(!selectedPlayerIndices.includes(selectionIndex)) {
                // add player index to selected indices list (prevent duplicate players)
                selectedPlayerIndices.push(selectionIndex);

                // select player version
                const player = playerPool[selectionIndex];
                if(this.settings.currentPlayersOnly && player.versions.find(v => v.isCurrent)) {
                    player.versionIndex = player.versions.map(v => v.isCurrent).indexOf(true);
                } else if(this.settings.minimumPlayerRatingActive && this.settings.minimumPlayerRating) {
                    let possibleVersionIndices = [];
                    for(let i = 0; i < player.versions.length; i++) {
                        if(player.versions[i].rating >= this.settings.minimumPlayerRating) {
                            possibleVersionIndices.push(i);
                        }
                    }
                    player.versionIndex = possibleVersionIndices[Math.floor(Math.random() * possibleVersionIndices.length)];
                } else {
                    player.versionIndex = Math.floor(Math.random() * player.versions.length);
                }

                // add player to roster with fewest players (naturally causes an alternating draft)
                if(team1.length <= team2.length) {
                    team1.push(player);
                } else {
                    team2.push(player);
                }
            }
        }

        return {team1, team2};
    }

    getPlayerPool() {
        let { players } = playerData;

        // filter player pool according to settings
        players = this.filterRandomizationMode(players);
        players = this.filterCurrentPlayersOnly(players);
        players = this.filterYearWindow(players);
        players = this.filterMinimumPPG(players);
        players = this.filterMinimumRating(players);

        return players;
    }

    filterRandomizationMode(players) {
        if(this.settings.randomizationMode === 'guards')
            return players.filter(p => p.position === 'POINT_GUARD' || p.position === 'SHOOTING_GUARD');

        if(this.settings.randomizationMode === 'centers')
            return players.filter(p => p.position === 'CENTER');

        if(this.settings.randomizationMode === 'forwards')
            return players.filter(p => p.position === 'SMALL_FORWARD' || p.position === 'POWER_FORWARD');

        if(this.settings.randomizationMode === 'top') {
            players.sort((p1, p2) => p2.careerPPG - p1.careerPPG);
            players = players.filter(p => p.latestYearPlayed - p.earliestYearPlayed >= 5);
            return players.slice(0, 50);
        }

        return players;
    }

    filterCurrentPlayersOnly(players) {
        if(this.settings.currentPlayersOnly) {
            return players.filter(p => p.versions.find(v => v.isCurrent));
        }

        return players;
    }

    filterYearWindow(players) {
        const { yearWindow } = this.settings;

        if(yearWindow.filterActive) {
            if(yearWindow.fromYear) {
                players = players.filter(p => p.latestYearPlayed >= yearWindow.fromYear);
            }
            if(yearWindow.toYear) {
                players = players.filter(p => p.earliestYearPlayed <= yearWindow.toYear);
            }
        }

        return players;
    }

    filterMinimumPPG(players) {
        if(this.settings.minimumPlayerPPGActive && this.settings.minimumPlayerPPG) {
            return players.filter(p => p.careerPPG >= this.settings.minimumPlayerPPG);
        }

        return players;
    }

    filterMinimumRating(players) {
        if(this.settings.minimumPlayerRatingActive && this.settings.minimumPlayerRating) {
            let sufficientlyRatedPlayers = players.filter(p => {
                return p.versions.find(v => v.rating >= this.settings.minimumPlayerRating)
            });
            console.log(sufficientlyRatedPlayers);

            // if current players only, make sure that the players' current versions are rated sufficiently high
            if(this.settings.currentPlayersOnly) {
                sufficientlyRatedPlayers = sufficientlyRatedPlayers.filter(p => {
                    const currentVersion = p.versions.find(v => v.isCurrent);
                    return currentVersion && currentVersion.rating >= this.settings.minimumPlayerRating;
                });
            }

            return sufficientlyRatedPlayers;
        }

        return players;
    }

}

const validateSettings = settings => {
    // validate year range
    if(settings.yearWindow.filterActive) {
        if(settings.yearWindow.fromYear && (isNaN(settings.yearWindow.fromYear) || settings.yearWindow.fromYear < 0))
            throw new Error('Your minimum year cutoff for players is not a valid year.');
        if(settings.yearWindow.toYear && (isNaN(settings.yearWindow.toYear) || settings.yearWindow.toYear < 0))
            throw new Error('Your maximum year cutoff for players is not a valid year.');

        if(settings.yearWindow.fromYear && settings.yearWindow.fromYear >= 2019)
            throw new Error('Your minimum year cutoff for players must be 2018 or earlier.');
        if(settings.yearWindow.toYear && settings.yearWindow.toYear <= 1949)
            throw new Error('Your maximum year cutoff for players must be 1950 or later.');

        if(settings.currentPlayersOnly && settings.yearWindow.toYear && settings.yearWindow.toYear < 2017)
            throw new Error('You want current players only, but your maximum cutoff year is ' + settings.yearWindow.toYear + '.');
    }

    // validate minimum career player PPG
    if(settings.minimumPlayerPPGActive) {
        if(settings.minimumPlayerPPG && (isNaN(settings.minimumPlayerPPG) || settings.minimumPlayerPPG < 0))
            throw new Error('Minimum player PPG is an invalid number.');
    }

    // validate minimum player rating
    if(settings.minimumPlayerRatingActive) {
        if(settings.minimumPlayerRating && isNaN(settings.minimumPlayerRating))
            throw new Error('Minimum player rating is an invalid number.');
    }
};