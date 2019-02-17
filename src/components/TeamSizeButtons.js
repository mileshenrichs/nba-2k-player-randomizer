import React from 'react';

const TeamSizeButtons = ({ teamSize, onTeamSizeSelect }) => {
    return (
        <div className="TeamSizeButtons button-row">
            {[1, 2, 3, 4, 5].map(size => (
                <button className={teamSize === size ? 'active' : ''} onClick={() => onTeamSizeSelect(size)} key={size}>
                    {size}v{size}
                </button>
            ))}
        </div>
    );
};

export default TeamSizeButtons;