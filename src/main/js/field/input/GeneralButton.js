import React from 'react';
import PropTypes from 'prop-types';

const GeneralButton = ({ onClick, children, className }) => (
    <button className={`btn btn-primary ${className}`} type="button" onClick={onClick}>{children}</button>
);

GeneralButton.defaultProps = {
    children: 'Click Me',
    className: 'btn-sm'
};

GeneralButton.propTypes = {
    children: PropTypes.string,
    className: PropTypes.string,
    onClick: PropTypes.func.isRequired
};

export default GeneralButton;
