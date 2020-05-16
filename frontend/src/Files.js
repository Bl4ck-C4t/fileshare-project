import React, {Component} from 'react';

async function getFiles(path: string){
    let response = await fetch("/api/getFiles?path=" + path).then(res => res.json());
    console.log(response);
    return response;
}

class FileComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            active_user: props.active_user,
            files: []
        };

    }

    componentDidMount(){
        getFiles("/");
    }

    render() {

        return (
        <div>
            <h1> Hello {this.state.active_user} </h1>
            <div>{this.state.files}</div>
        </div>
        );
    }
}

export default FileComponent;