import React, {Component} from 'react';
import './css/Files.css';
import axios from 'axios';
import DragAndDrop from './DragAndDrop.js';
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
        this.handleDrop = this.handleDrop.bind(this);

    }

    updateFiles(){
        let path = this.props.location.pathname;
        path = path.split("/").splice(2).join("/");
        path =  path ? path : "/";

        if (path[0] !== "/"){
            path = "/" + path;
        }
        console.log(path);
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
    }

    componentDidMount(){
       this.updateFiles();

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
        });
    }

    componentDidUpdate(prev){
        if(this.props.location.pathname != prev.location.pathname){
            this.updateFiles();
        }
    }

    componentWillUnmount() {
        this.source.cancel('Promises canceled');
    }

    accessFile(fname: string){
        let {history, location} = this.props;
        history.push(location.pathname+"/"+fname);
    }

    handleDrop(files){
        const file = files[0];
        let path = this.props.location.pathname;
        path = path.split("/").splice(2).join("/");
        path =  path ? path : "/";
        let formData = new FormData();
        formData.append("file", file);
        formData.append("path", path)
        axios.post("/api/uploadFile", formData, {})
        .then(res => {
            if(res.status != 202){
                console.log("Uploading file failed");
                console.log(res);
            }
        });
        this.updateFiles();
//        console.log(formData);

        console.log(path)

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
            <DragAndDrop handleDrop={this.handleDrop}>
               <div> Hello </div>
            </DragAndDrop>
        </div>
        ) : <FileContent file={this.state.file} />
    }
}

function FileContent(props){
    return (
    <div>
            <h2>Content of file {props.file.fileName}</h2>
            <hr />
        <div className="breadcrumb" style={{"width": "73%", "margin": "14%", "marginTop": "4%"}}>
          <div className="card-body">
                <p style={{"fontSize": "19px"}}> {props.file.fileContent} </p>
          </div>
        </div>

    </div>
    );
}

export default withRouter(FileComponent);