import React, { useState, useEffect } from 'react';
import eventBus from 'host/eventBus';
import apiClient from 'event-bus/api-client';
import { useParams } from 'react-router-dom'
import './UserProfile.css';
import { useMatch } from 'react-router-dom';
import { useLocation } from 'react-router-dom';

const UserProfile = () => {
    const [user, setUser] = useState({});
    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    let userId = window.location.pathname.split("/").pop();console.log(userId);

    useEffect(() => {
        loadUserPosts();
        loadUser();
        },
        []
    );

    const loadUser = async () => {
        const data = await apiClient.getUser(userId);
        setUser(data);
    }

  const loadUserPosts = async () => {
    try {
      setLoading(true);
      const data = await apiClient.getPostsOfUser(userId);
      setPosts(data);
      setError(null);
    } catch (err) {
      console.error('Failed to load posts:', err);
      setError('Failed to load posts. Please try again later.');
    } finally {
      setLoading(false);
    }
  };


  if (loading) {
    return (
      <div className="product-list">
        <h2>Posts</h2>
        <div className="loading">Loading posts...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="product-list">
        <h2>
          Posts
        </h2>
        <div className="error">{error}</div>
        <button onClick={loadUserPosts} className="retry-btn">
          Retry
        </button>
      </div>
    );
  }

  return (
    <div className="product-list">
      <h2>UserProfile: {user.username}</h2>
      <div className="cart-items">
        {posts.map((item) => (
            <div key={item.id} className="cart-item">
              <div className="cart-item-details">
                  <h3>{item.content}</h3>
                  <h5>Posted on: {item.creationDate.substring(0,10)}</h5>
              </div>

            </div>
        ))}
      </div>
    </div>
  );
};

export default UserProfile;
