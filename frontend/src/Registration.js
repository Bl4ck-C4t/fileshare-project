import React, {Component, useState, useEffect} from 'react';
import './css/Registration.css'
import {
  Redirect,
  useHistory,
  Link
} from "react-router-dom";

async function postData(url = '', data) {
  // Default options are marked with *
  var form_data = new FormData();

  for ( var key in data ) {
      form_data.append(key, data[key]);
  }
  const response = await fetch(url, {
    method: 'POST', // *GET, POST, PUT, DELETE, etc.
   // no-referrer, *no-referrer-when-downgrade, origin, origin-when-cross-origin, same-origin, strict-origin, strict-origin-when-cross-origin, unsafe-url
    body: form_data // body data type must match "Content-Type" header
  });
  return response; // parses JSON response into native JavaScript objects
}


class RegistrationForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            formData: {
               username: "",
               password: "",
               repeat_password: "",
               email: "",
            },
            errorMessages: [],
            registered: false
        };
        this.handleChange = this.handleChange.bind(this);
        this.submitForm = this.submitForm.bind(this);
        this.validateForm = this.validateForm.bind(this);
    }



    async usernameExists(username){
        let response = await fetch("/api/userExists?username="+username);
        response = await response.json();
        let val = await response;
        return val
    }

    async validateForm(){

        const data = this.state.formData;
//        let res = await this.usernameExists(data.username);
//        console.log(res);
        let errorMessages = [];
        if (data.password != data.repeat_password) {
            errorMessages.push("Passwords do not match");
        }
        if (await this.usernameExists(data.username)){ // this.usernameExists(data.username)
            errorMessages.push("Username already exists");
        }
        if(data.username != "" && !(/^[a-zA-Z_][a-zA-Z_0-9]*$/.test(data.username))){
            errorMessages.push("Username is incorrect");
        }
        if(data.email != "" && !(/([a-zA-Z_][a-zA-Z_0-9]*)+@([a-zA-Z_][a-zA-Z_0-9]*)\.[a-zA-Z]+/.test(data.email))){
            errorMessages.push("Email address is incorrect");
        }
        this.setState({errorMessages: errorMessages});
    }

     handleChange(event) {

        let nam = event.target.name;
        let val = event.target.value;
        this.setState(prevState => ({
            formData: {
                ...prevState.formData,
                [nam]: val
            }
        }), this.validateForm);


//        console.log(this.state.errorMessage);

     }

     submitForm(ev){
        ev.preventDefault();
        let temp_state = {};
        for ( var key in this.state.formData ) {
              temp_state[key] =  this.state.formData[key];
        }
        delete temp_state.repeat_password;


        postData("/api/register", temp_state).
        then(res => res.json())
        .then(result => console.log(result));

        if (this.state.errorMessages.length > 0) {
            return false;
        }
        else{
            this.setState({registered: true});
//            history.push("/register-success");
            return true;
        }

     }

    render() {

        return this.state.registered ? (
        <div>
            <p> Activation email sent to {this.state.formData.email} </p>
            <Link to="/"> Return to home page </Link>
        </div>
        ) :

        (
        <div>
        <h1>Register</h1>
        <form onSubmit={this.submitForm}>
            <p>Please fill in this form to create an account.</p>
            <hr/>
            <label><b>Email</b></label>
            <input type="text" placeholder="Enter Email" name="email" required onChange={this.handleChange} />
            <br />
            <label><b>Username</b></label>
            <input type="text" placeholder="Enter Username" name="username" required onChange={this.handleChange}/>
            <br />

            <label ><b>Password</b></label>
            <input type="password" placeholder="Enter Password" name="password" required onChange={this.handleChange}/>
            <br />
            <label><b>Repeat Password</b></label>
            <input type="password" placeholder="Repeat Password" name="repeat_password" required onChange={this.handleChange}/>
            <hr/>
            <br />

            {this.state.errorMessages.map((error, i) => {
                  return (
                  <div key={i} className="alert alert-danger error-div">
                  <p key={i} style={{color: "red"}}>{error}</p>
                  </div>);
              })}
            <button type="submit" className="registerbtn" >Register</button>
        </form>
        </div>
        );
    }
}



export default RegistrationForm;