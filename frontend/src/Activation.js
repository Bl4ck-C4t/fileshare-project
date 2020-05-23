import React, {Component, useState, useEffect} from 'react';
import {
  Redirect,
  useHistory,
  Link,
  useLocation
} from "react-router-dom";

function ActivationPage(props){
    const [state, setState] = useState(null);
    let query = new URLSearchParams(useLocation().search);
    async function fetchData(){
        let success = await fetch("/api/activate?code=" + query.get('code'));
        success = await success.json();
        setState(success);
    }

    useEffect(() => {fetchData()}, []);
    if(state){
        return (<div>
        <h1> Your account has been activated</h1>
        <Link to="/login"> Go to login page </Link>
        </div>);
    }
    else if (state == null){
        return <i>Activating...</i>;
    }
    else{
        return <h1> Invalid activation code</h1>;
    }

}

export default ActivationPage;