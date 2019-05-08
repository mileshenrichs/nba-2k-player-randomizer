import React, { Component } from 'react';
import ApiClient from './ApiClient';

class ViewReport extends Component {
    constructor(props) {
        super(props);

        this.state = {
            report: undefined
        };
    }

    componentDidMount() {
        ApiClient.getReport(this.props.match.params.reportId).then(({ data }) => {
            console.log(data);
        });
    }

    render() {
        return (
            <p>View report</p>
        );
    }
}

export default ViewReport;