import React, {useState} from "react";
import {useNavigate} from "react-router-dom";
import apiClient from "event-bus/api-client";
import './Login.css';

export default function Register() {
    const [username, setUserName] = useState();
    const [password, setPassword] = useState();
    const [email, setEmail] = useState();
    let navigate = useNavigate();

    const handleSubmit = async e => {
        e.preventDefault();
        const result = await apiClient.register(username, password, email);
        navigate("/login");
    }

    return(
        <div className="login-wrapper">
            <h1>Register</h1>
            <form onSubmit={handleSubmit}>
                <label>
                    <p>Username</p>
                    <input type="text" onChange={e => setUserName(e.target.value)} />
                </label>
                <label>
                    <p>Password</p>
                    <input type="password" onChange={e => setPassword(e.target.value)} />
                </label>
                <label>
                    <p>Email</p>
                    <input type="text" onChange={e => setEmail(e.target.value)} />
                </label>
                <div>
                    <button type="submit">Submit</button>
                </div>
            </form>
        </div>
    )
}