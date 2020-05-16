import React, {Component} from 'react';
import './css/Files.css';

async function getFiles(path: string){
    let response = await fetch("/api/getFiles?path=" + path).then(res => res.json());
    return response;
}

class FileComponent extends Component {
    _isMounted = false;
    constructor(props) {
        super(props);
        this.state = {
            active_user: null,
            files: []
        };

    }

    componentDidMount(){
        getFiles("/").
        then(res => this.setState({files: res}));
        fetch("/api/getUser").then((response) => {
                    if (response.redirected){
                        return null
                    }
                    else{
                        return response.text();
                    }
                })
                .then(text => this.setState({ active_user: text}));
    }

    componentWillUnmount() {

    }

    accessFile(fname: string){
        console.log(fname);
    }

    render() {

        return (
        <div>
            <h1> Hello {this.state.active_user} </h1>

                <div className="card-columns container-fluid" style={{"maxWidth": "82%"}}>

            {this.state.files.map((fname, i) => {
                return (

                    <div key={i} className="card border-dark mb-3">

                         <div className="row no-gutters" onClick={() => this.accessFile(fname)}>
                            <div className="col-md-4">
                              <img src={require("./images/file.png")} className="card-image" />
                            </div>
                            <div className="col-md-8">
                                <div className="card-body">
                                    <h3 className="card-text">{fname}</h3>
                                </div>
                            </div>
                         </div>
                    </div>

                    );
            })}

           </div>
        </div>
        );
    }
}

export default FileComponent;