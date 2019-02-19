import React from 'react';
import Setting from "./Setting";
import NumberInput from "./NumberInput";

const AdditionalSettings = props => {
    return (
        <div className="AdditionalSettings">
            <Setting isActive={props.currentPlayersOnly} onToggle={props.onToggleCurrentPlayersOnly}>
                <span>Current players only</span>
            </Setting>

            <Setting isActive={props.yearWindow.filterActive} onToggle={props.onToggleYearWindowActive}>
                <span>
                    Players from years
                    <NumberInput placeholder="any" value={props.yearWindow.fromYear} onChange={props.onChangeFromYear} />
                    &nbsp;to&nbsp;
                    <NumberInput placeholder="any" value={props.yearWindow.toYear} onChange={props.onChangeToYear} />
                </span>
            </Setting>

            <Setting isActive={props.minimumPlayerPPGActive} onToggle={props.onToggleMinPlayerPPG}>
                <span>Minimum player career PPG: </span>
                <NumberInput placeholder="25.8" value={props.minimumPlayerPPG} onChange={props.onChangeMinPlayerPPG} />
            </Setting>
        </div>
    );
};

export default AdditionalSettings;