import React, { Suspense, lazy } from 'react';
import {BrowserRouter, Route, Routes, useNavigate} from 'react-router-dom';
import './App.css';

const UserProfile = lazy(() => import('user-profile/UserProfile'));
const Dashboard = lazy(() => import('dashboard/Dashboard'));
import apiClient from 'event-bus/api-client';
import { useState, useEffect } from 'react'
import ProtectedRoute from "./ProtectedRoute";
import Login from "./Login";
import useToken from "./useToken.js";
import Register from "./Register";

function App() {

    const { token, setToken } = useToken();

    const logOut = async () => {
        setToken(null);
    }

    return (
        <div className="wrapper">
            <h1>Microservices Social</h1>
            <button onClick={logOut} className="button">Log Out</button>
            <BrowserRouter>
                <Routes>
                    <Route path="/login" element={<Login setToken={setToken} />} />
                    <Route path="register" element={<Register />} />
                    <Route
                        path="/"
                        element={
                            <ProtectedRoute token={token}>
                                <Dashboard />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/user-profile/:id"
                        element={
                            <ProtectedRoute token={token}>
                                <UserProfile />
                            </ProtectedRoute>
                        }
                    />
                </Routes>
            </BrowserRouter>
        </div>
    );
}

export default App;
