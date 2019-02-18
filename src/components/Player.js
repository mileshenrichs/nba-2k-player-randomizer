import React from 'react';

const Player = ({ player }) => {
    return (
        <div className="Player" style={{borderColor: getBorderColor(player)}}>
            <span className="Player__name">{player.name}</span>
            <span className="Player__details">
                <span className="Player__details--position">
                    {getAbbreviatedPosition(player.position)}
                </span>
                <span className="Player__details--divider"> | </span>
                <span className="Player__details--team">
                    {getFullTeamName(pickRandomTeamForPlayer(player))}
                </span>
            </span>
        </div>
    );
};

const positionLookupTable = {
    'POINT_GUARD': 'PG',
    'SHOOTING_GUARD': 'SG',
    'SMALL_FORWARD': 'SF',
    'POWER_FORWARD': 'PF',
    'CENTER': 'C'
};

const teamNameLookupTable = {
    'ATL': 'Atlanta Hawks',
    'BKN': 'Brooklyn Nets',
    'BOS': 'Boston Celtics',
    'CHA': 'Charlotte Hornets',
    'CHI': 'Chicago Bulls',
    'CLE': 'Cleveland Cavaliers',
    'DAL': 'Dallas Mavericks',
    'DEN': 'Denver Nuggets',
    'DET': 'Detroit Pistons',
    'GSW': 'Golden State Warriors',
    'HOU': 'Houston Rockets',
    'IND': 'Indiana Pacers',
    'LAC': 'Los Angeles Clippers',
    'LAL': 'Los Angeles Lakers',
    'MEM': 'Memphis Grizzlies',
    'MIA': 'Miami Heat',
    'MIL': 'Milwaukee Bucks',
    'MIN': 'Minnesota Timberwolves',
    'NOP': 'New Orleans Pelicans',
    'NOH': 'New Orleans Hornets',
    'NYK': 'New York Knicks',
    'OKC': 'Oklahoma City Thunder',
    'ORL': 'Orlando Magic',
    'PHI': 'Philadelphia 67ers',
    'PHX': 'Phoenix Suns',
    'POR': 'Portland Trail Blazers',
    'SAC': 'Sacramento Kings',
    'SAS': 'San Antonio Spurs',
    'TOR': 'Toronto Raptors',
    'UTA': 'Utah Jazz',
    'WAS': 'Washington Wizards',

};

const positionalBorderColorsLookupTable = {
    'SMALL_FORWARD': '#008000',
    'CENTER': '#935614',
    'POINT_GUARD': '#0f9fc9',
    'POWER_FORWARD': '#d21c1c',
    'SHOOTING_GUARD': '#e38000'
};

const getAbbreviatedPosition = position => positionLookupTable[position];

const getFullTeamName = team => teamNameLookupTable[team];

const pickRandomTeamForPlayer = player => player.teams[Math.floor(Math.random() * player.teams.length)];

const getBorderColor = player => positionalBorderColorsLookupTable[player.position];

export default Player;