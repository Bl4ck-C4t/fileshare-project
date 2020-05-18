import React, {Component} from 'react';
import './css/Files.css';
import axios from 'axios';
import {
  Redirect,
  useHistory,
  Link,
  useLocation,
  Switch,
  Route,
  withRouter
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
            file: {}
        };
        const CancelToken = axios.CancelToken;
        this.source = CancelToken.source();

    }

    componentDidMount(){
        let path = this.props.location.pathname;
        path = path.split("/").splice(2).join("/");
        path =  path ? path : "/";

        if (path[0] !== "/"){
            path = "/" + path;
        }
        console.log(path);
        console.log(this.props.location.pathname)
        getFiles(path, this.source.token)
        .then(res => this.setState({file: res}))
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
        let {history, location} = this.props;
        history.push(location.pathname+"/"+fname);
    }

    render() {
        if (this.state.file == {}){
            return;
        }
        return this.state.file.isDirectory ?
         (

        <div>
            <h1> Hello {this.state.active_user} </h1>

                <div className="card-columns container-fluid" style={{"maxWidth": "82%"}}>

            {this.state.file.files.map((fname, i) => {


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
        ) : <FileContent file={this.state.file} />
    }
}

function FileContent(props){
    return (
    <div>
            <h2>Content of file {props.file.fileName}</h2>
            <hr />
        <div class="breadcrumb" style={{"width": "73%", "margin": "14%", "margin-top": "4%"}}>
          <div class="card-body">
                <p style={{"font-size": "19px"}}> {props.file.fileContent} </p>
          </div>
        </div>

    </div>
    );
}

export default withRouter(FileComponent);