import React from 'react';
import LabeledField from './LabeledField';

export default class ReadOnlyField extends LabeledField {
    render() {
        return (
            super.render(<div className="col-sm-8"><p className="form-control-static">{this.props.value}</p></div>)
        );
    }
}
