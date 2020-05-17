import React, {Component} from 'react';
import './css/Files.css';
import axios from 'axios';
import {
  Redirect,
  useHistory,
  Link,
  useLocation
} from "react-router-dom";

async function getFiles(path: string, token: CancelToken){
    let response = await axios.get("/api/getFiles?path=" + path, {cancelToken: token}).then(res => res.data);
//    console.log("sd");
    //console.log(await response);

    return response;

}

class FileComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            active_user: null,
            files: []
        };
        const CancelToken = axios.CancelToken;
        this.source = CancelToken.source();

    }

    componentDidMount(){

        getFiles("/", this.source.token)
        .then(res => this.setState({files: res}))
        .catch(thrown => {
              if (axios.isCancel(thrown)) {
                console.log('Request canceled', thrown.message);
              }
              else{
                console.log('Error: ', thrown.message);
              }
          });

        axios.get("/api/getUser", {cancelToken: this.source.token})
        .then((response) =>  response.data)
        .then(text => this.setState({ active_user: text}))
        .catch(thrown => {
              if (axios.isCancel(thrown)) {
                console.log('Request canceled', thrown.message);
              }
               else{
                  console.log('Error: ', thrown.message);
                }
        });;
    }

    componentWillUnmount() {
        this.source.cancel('Promises canceled');
    }

    accessFile(fname: string){
        console.log(fname);

        this.props.history.push(this.props.location.pathname + "/" + fname)
        let path = this.props.location.pathname;
//        path = path.replace("/files", "")
        console.log(path);
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
                              <img src={require("./images/file.png")} className="card-image" alt="" />
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