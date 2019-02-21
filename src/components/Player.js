import React from 'react';

const Player = ({ player }) => {
    const rating = player.versions[player.versionIndex].rating;

    if(player.reveal) {
        return (
            <div className="Player" style={{borderColor: getBorderColor(player)}}>
                <div className="Player__content">
                    <div className="Player__text">
                        <span className="Player__name">{player.name}</span>
                        <span className="Player__details">
                            <span className="Player__details--position">
                                {getAbbreviatedPosition(player.position)}
                            </span>
                            <span className="Player__details--divider"> | </span>
                            <span className="Player__details--team">
                                {player.versions[player.versionIndex].team}
                            </span>
                        </span>
                    </div>

                    <span className="Player__rating" style={getRatingBoxStyle(rating)}>{rating}</span>
                </div>
            </div>
        );
    } else {
        return null;
    }
};

const positionLookupTable = {
    'POINT_GUARD': 'PG',
    'SHOOTING_GUARD': 'SG',
    'SMALL_FORWARD': 'SF',
    'POWER_FORWARD': 'PF',
    'CENTER': 'C'
};

const positionalBorderColorsLookupTable = {
    'SMALL_FORWARD': '#008000',
    'CENTER': '#935614',
    'POINT_GUARD': '#0f9fc9',
    'POWER_FORWARD': '#d21c1c',
    'SHOOTING_GUARD': '#e38000'
};

const getAbbreviatedPosition = position => positionLookupTable[position];

const getBorderColor = player => positionalBorderColorsLookupTable[player.position];

const getRatingBoxStyle = rating => {
    if(rating <= 70) {
        return {
            background: '#6F270E'
        }
    } else if(rating <= 75) {
        return {
            background: '#7F8180'
        }
    } else if(rating <= 80) {
        return {
            background: '#AB8824'
        }
    } else if(rating <= 85) {
        return {
            background: '#1D7722'
        }
    } else if(rating <= 90) {
        return {
            background: '#A30000'
        }
    } else if(rating <= 95) {
        return {
            background: '#648C9A',
            boxShadow: '0 0 10px #00bdff'
        }
    } else if(rating <= 98) {
        return {
            background: '#B52D72',
            boxShadow: '0 0 10px #ff0081'
        }
    } else {
        return {
            background: 'linear-gradient(rgba(75,168,255,0.1), rgba(0,0,0,0.7)), linear-gradient(to bottom left, #188fff, #e1a6e7, #f9a205)',
            boxShadow: '0 0 10px #f9a205'
        }
    }
};

export default Player;