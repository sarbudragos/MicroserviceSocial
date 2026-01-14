import React, { useState } from 'react';
import './Login.css';
import apiClient from "event-bus/api-client";
import {Link, useNavigate} from "react-router-dom";

export default function Login({ setToken }) {
    const [username, setUserName] = useState();
    const [password, setPassword] = useState();
    let navigate = useNavigate();

    const handleSubmit = async e => {
        e.preventDefault();
        const token = await apiClient.login(username, password);
        setToken(token.token);
        navigate("/");
    }

    return(
        <div className="login-wrapper">
            <h1>Please Log In</h1>
            <form onSubmit={handleSubmit}>
                <label>
                    <p>Username</p>
                    <input type="text" onChange={e => setUserName(e.target.value)} />
                </label>
                <label>
                    <p>Password</p>
                    <input type="password" onChange={e => setPassword(e.target.value)} />
                </label>
                <div>
                    <button type="submit">Submit</button>
                </div>
            </form>

            <Link to="/register">
                <button>Register</button>
            </Link>
        </div>
    )
}