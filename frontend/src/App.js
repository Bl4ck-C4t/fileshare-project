import React, {Component, useState, useEffect} from 'react';
import logo from './logo.svg';
import './css/App.css';
import axios from 'axios';
import RegistrationForm from './Registration.js';
import FilePage from './Files.js';
import ActivationPage from './Activation.js';
import {
  Switch,
  Route,
  Link,
  withRouter,
  useParams,
  useLocation
} from "react-router-dom";

function MainPage(props){
    const user = props.active_user;
    return (
    user ? (
    <div className="App">
                <a href="/login?logout"> Log out </a> <br/>
                <Link to="/files"> Go to Files </Link>

     </div>
        )

    : (

        <div className="App">
            <a href="/login"> Log in </a> <br/>
            <Link to="/register"> Register </Link>
        </div>
        )
    );
}

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            active_user: null
        };
    }

    componentDidMount(){
        axios.get("/api/getUser").then((response) => {
            if (response.redirected){
                return null
            }
            else{
                return response.data;
            }
        })
        .then(text => this.setState({ active_user: text}));
    }


    render() {
        return (
        <Switch>
        <Route exact path="/">
            <MainPage active_user={this.state.active_user}/>
        </Route>
        <Route path="/register" component={withRouter(RegistrationForm)} />

        <Route path="/files" component={FilePage} />

        <Route path="/getFiles" >
            <FilePage />
        </Route>

        <Route path="/activate">
            <ActivationPage />
        </Route>

        <Route path="/getLink">
            <LinkPage />
        </Route>

        <Route path="/register-success">

        </Route>
        </Switch>

        );
    }
}

function LinkPage(props){
    const path = new URLSearchParams(useLocation().search).get("path");
    let [state, setState] = useState(null);
    useEffect(() => {
        axios.get("/api/generateLink?path="+path)
        .then(res => setState(res.data));

    }, []);
    return (
    <div>
        <h4> Use this link to access the shared resource :3 -></h4>
        <p>{state ? state : "loading..."} </p>
    </div>
    );
//    axios.get("/api/generateLink?path="+path)
    return null;
}

export default App;