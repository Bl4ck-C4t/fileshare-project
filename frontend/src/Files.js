import React, {Component} from 'react';

class FileComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            active_user: props.active_user
        };

    }

    render() {
        return (
        <div>
            <h1> Hello {this.state.active_user} </h1>
        </div>
        );
    }
}

export default FileComponent;