import React, { useState, useEffect } from 'react';
import eventBus from 'host/eventBus';
import apiClient from 'event-bus/api-client';
import './Dashboard.css';
import {Link} from "react-router-dom";

const Dashboard = () => {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [newPosts, setNewPosts] = useState({
    id: null,
    content: "",
    creationDate: null,
    userId: null,
  });

  useEffect(() => {
    loadDashboard();

    // Listen for posts updates from other components
    const unsubscribe = eventBus.subscribe('cartUpdated', () => {
      loadDashboard();
    });

    return () => unsubscribe();
  }, []);

  const loadDashboard = async () => {
    try {
      setLoading(true);
      const data = await apiClient.getPosts();
      setPosts(data);
      console.log(posts);
      setError(null);
    } catch (err) {
      console.error('Failed to load posts:', err);
      setError('Failed to load posts');
    } finally {
      setLoading(false);
    }
  };

  const goToUserProfile = (id) => {
    window.location.href = '/user-profile/' + id;
  }

  const addPost = async () => {
    let newPost = await apiClient.addPost(newPosts);

    loadDashboard();
  }

  const addRandomPost = async () => {
    let newPost = await apiClient.addRandomPost();

    loadDashboard();
  }


  if (loading) {
    return (
      <div className="cart">
        <div>
          <p>Dashboard</p>
          <input type="text" onChange={e => setNewPosts({...newPosts, content: e.target.value})} />
          <button onClick={addPost} className="retry-btn"> Add post</button>
          <button onClick={addRandomPost} className="retry-btn">Add random post</button>
        </div>
        <div className="loading">Loading dashboard...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="cart">
        <div>
          <p>Make a post</p>
          <input type="text" onChange={e => setNewPosts({...newPosts, content: e.target.value})} />
          <button onClick={addPost} className="retry-btn"> Add post</button>
          <button onClick={addRandomPost} className="retry-btn">Add random post</button>
        </div>
        <div className="error">{error}</div>
        <button onClick={loadDashboard} className="retry-btn">
          Retry
        </button>
      </div>
    );
  }

  return (
    <div className="cart">
      <div>
        <p>Dashboard</p>
        <input type="text" onChange={e => setNewPosts({...newPosts, content: e.target.value})} />
        <button onClick={addPost} className="retry-btn"> Add post</button>
        <button onClick={addRandomPost} className="retry-btn">Add random post</button>
      </div>
      <h2>Dashboard({posts.length} posts)</h2>
      {posts.length === 0 ? (
        <p className="empty-cart">No posts</p>
      ) : (
        <>
          <div className="cart-items">
            {posts.map((item) => (
              <div key={item.id} className="cart-item">
                <div className="cart-item-details">
                  <h3>{item.content}</h3>
                </div>
                <div className="cart-item-details">
                    <button onClick={(e) => {
                      goToUserProfile(item.userId);
                    }}>Go to user profile</button>
                </div>

              </div>
            ))}
          </div>
        </>
      )}
    </div>
  );
};

export default Dashboard;
