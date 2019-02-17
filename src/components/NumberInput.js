import React from 'react';

const NumberInput = ({ placeholder, value, onChange }) => {
    return (
        <input
            className={'NumberInput' + (value ? ' populated' : '')}
            type="text"
            placeholder={placeholder}
            onClick={e => e.stopPropagation()}
            value={value}
            onChange={e => onChange(e.target.value)}
        />
    );
};

export default NumberInput;