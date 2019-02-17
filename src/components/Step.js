import React from 'react';

const Step = ({ title, currentPointIndex, renderPoint, style, children }) => {
    if(currentPointIndex >= renderPoint) {
        return (
            <div className="Step" style={style}>
                <h2 className="Step__title">{title}</h2>
                {children}
            </div>
        )
    } else {
        return null;
    }
};

export default Step;