import React from 'react';
import Player from "./Player";

const TeamRoster = ({ teamNo, players }) => {
    return (
        <div className="TeamRoster">
            <h3>Team {teamNo}</h3>

            {players.map(player => (
                <Player player={player} key={player.name} />
            ))}
        </div>
    );
};

export default TeamRoster;