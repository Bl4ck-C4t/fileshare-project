import React, {Component} from 'react';
import logo from './logo.svg';
import './css/App.css';
import RegistrationForm from './Registration.js';
import FilePage from './Files.js';
import ActivationPage from './Activation.js';
import {
  Switch,
  Route,
  Link,
  withRouter,
  useParams
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


    render() {
        return (
        <Switch>
        <Route exact path="/">
            <MainPage active_user={this.state.active_user}/>
        </Route>
        <Route path="/register" component={withRouter(RegistrationForm)} />

        <Route path="/files" component={withRouter(FilePage)} />

        <Route path="/activate">
            <ActivationPage />
        </Route>
        </Switch>

        );
    }
}

export default App;