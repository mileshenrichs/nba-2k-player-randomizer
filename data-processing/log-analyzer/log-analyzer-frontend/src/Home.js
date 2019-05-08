import React, { Component } from 'react';
import ApiClient from './ApiClient';

class Home extends Component {
    constructor(props) {
        super(props);

        this.state = {
            existingReports: []
        };
    }

    componentDidMount() {
        ApiClient.getExistingReports().then(({ data }) => {
            console.log(data);
        });
    }

    render() {
        return (
            <div className="Home">
                <p>we're home</p>
            </div>
        );
    }
}

export default Home;