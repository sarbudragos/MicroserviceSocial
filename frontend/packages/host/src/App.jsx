import React, { Suspense, lazy } from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
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

    return (
    // <div className="App">
    //   <header className="app-header">
    //     <h1>Microservice Social</h1>
    //     <p>Made with microservices</p>
    //   </header>
    //
    //   <div className="app-container">
    //     <div className="user-profile-section">
    //       <Suspense fallback={<div className="loading">Loading user profile...</div>}>
    //         <UserProfile />
    //       </Suspense>
    //     </div>
    //
    //     <div className="dashboard-section">
    //       <Suspense fallback={<div className="loading">Loading dashboard...</div>}>
    //         <Dashboard />
    //       </Suspense>
    //     </div>
    //   </div>
    // </div>


        <div className="wrapper">
            <h1>Microservices Social</h1>
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
