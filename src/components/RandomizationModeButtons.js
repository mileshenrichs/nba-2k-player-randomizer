import React from 'react';

const RandomizationModeButtons = ({ randomizationMode, onModeSelect }) => {
    const modes = [
        {id: 'all', text: 'All Players'},
        {id: 'guards', text: 'Guards Only'},
        {id: 'centers', text: 'Centers Only'},
        {id: 'forwards', text: 'Forwards Only'},
        {id: 'top', text: 'Top 50 All-time scorers'}
    ];

    return (
        <div className="RandomizationModeButtons">
            <div className="RandomizationModeButtons__row-1 button-row">
                {[0, 1, 2].map(i => (
                    <button className={randomizationMode === modes[i].id ? 'active' : ''}
                            onClick={() => onModeSelect(modes[i].id)} key={i}>
                        {modes[i].text}
                    </button>
                ))}
            </div>
            <div className="RandomizationModeButtons__row-2 button-row">
                {[3, 4].map(i => (
                    <button className={randomizationMode === modes[i].id ? 'active' : ''}
                            onClick={() => onModeSelect(modes[i].id)} key={i}>
                        {modes[i].text}
                    </button>
                ))}
            </div>
        </div>
    );
};

export default RandomizationModeButtons;