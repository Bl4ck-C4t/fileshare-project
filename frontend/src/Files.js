import React, {Component} from 'react';
import './css/Files.css';
import axios from 'axios';
import DragAndDrop from './DragAndDrop.js';
import {
  withRouter,
  matchPath
} from "react-router-dom";


class FileComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            active_user: null,
            files: null,
            currentFile: null
        };
        this.access_code = new URLSearchParams(this.props.location.search).get("access_code");
        const CancelToken = axios.CancelToken;
        this.source = CancelToken.source();
        this.handleDrop = this.handleDrop.bind(this);
    }

    updateFiles(){
        let path = this.extractPath();
        this.setState({currentFile: null});
        if (path[0] !== "/"){
            path = "/" + path;
        }
        const url = this.access_code ? "/api/fileLink?access_code="+this.access_code : "/api/getFiles?path="+ path;

        axios.get(url, {cancelToken: this.source.token})
        .then(res => res.data)
        .then(res => this.setState({files: res.files, currentFile: res.file}))
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

    accessFile(e, fname: string) {
        if(e.target.className !== "comp"){
            let {history, location} = this.props;
            history.push(location.pathname+"/"+fname);
        }

    }

    extractPath(){
        let path = this.props.location.pathname;
        const match = matchPath(path, {
            path: "/files/*"
        });
        path =  match ? match.params[0] : "/";
        return path;
    }

    handleDrop(files){
        const file = files[0];

        let path = this.extractPath();
        let formData = new FormData();
        formData.append("file", file);
        formData.append("path", path)
        axios.post("/api/uploadFile", formData, {})
        .then(res => {
            if(res.status !== 201){
                console.log("Uploading file failed");
                console.log(res);
            }
            else{
                this.updateFiles();
            }
        });

//        console.log(formData);

        console.log(path)

    }

    deleteFile(fname){
        console.log('Deleting file: ', fname);
        let path = this.extractPath() + "/" + fname;
        console.log(path)
        axios.delete("/api/deleteFile?path=" + path, {cancelToken: this.source.token})
        this.updateFiles();
    }

    getLink(fname: string){
        let {history, location} = this.props;
        let path = this.extractPath() + "/" + fname;
        history.push("/getLink?path="+path)
        //history.push("/api/generateLink?path="+path)
    }

    render() {
        if (this.state.files == null && this.state.currentFile == null){
            return null;
        }

        return this.state.currentFile == null ?
         (

        <div>
            <h1> Hello {this.state.active_user ? this.state.active_user : "guest"} </h1>

                <div className="card-columns container-fluid" style={{"maxWidth": "82%"}}>

            {this.state.files.map((file, i) => {


                return (
                    <div key={i} className="card border-dark mb-3" onClick={(e) => this.accessFile(e, file.fileName)}>

                         <div className="row no-gutters" >
                            <div className="col-md-4">
                              <img src={
                                file.isDirectory ? require("./images/folder.png")
                                : require("./images/file.png")
                              } className="card-image" alt="" />
                            </div>
                            <div className="col-md-8">
                                <div className="card-body">
                                    <h3 className="card-text">{file.fileName}</h3>
                                </div>
                            </div>

                            <div className="col-md-4">
                                <div className="card-body">
                                    <h3 className="card-text">
                                        <a className="comp" href="#" onClick={() => this.deleteFile(file.fileName)}>
                                            Delete
                                        </a>
                                    </h3>
                                </div>
                            </div>
                            <div className="col-md-4">
                                <div className="card-body">
                                    <h3 className="card-text">
                                        <a className="comp" href="#" onClick={() => this.getLink(file.fileName)}>
                                            Get Link
                                        </a>
                                    </h3>
                                </div>
                            </div>
                         </div>


                    </div>

                    );
            })}

           </div>
            <DragAndDrop handleDrop={this.handleDrop}>
               <div>
                Drop files to upload here
              </div>
            </DragAndDrop>
        </div>
        ) : <FileContent file={this.state.currentFile} />
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