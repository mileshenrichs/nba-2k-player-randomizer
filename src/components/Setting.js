import React from 'react';
import checkmarkSrc from '../assets/checkmark.png';

const Setting = ({ isActive, onToggle, children }) => {
    return (
        <div className="Setting">
            <div className="Setting__contents" onClick={() => onToggle()}>
                <div className={'Setting__checkbox' + (isActive ? ' active' : '')}>
                    {isActive && <img src={checkmarkSrc} alt="" />}
                </div>

                {children}
            </div>
        </div>
    );
};

export default Setting;