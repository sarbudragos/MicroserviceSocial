// API Client for communicating with backend services
// Use relative URL to work with both direct access and Nginx proxy
// When accessed through Nginx (localhost:80), it will use /api
// When accessed directly (localhost:3000), it needs full URL
const getApiBaseUrl = () => {

  return 'http://localhost:8000';
};

class ApiClient {
  constructor() {
    this.baseURL = getApiBaseUrl();
  }

  async request(endpoint, options = {}) {
    const url = `${this.baseURL}${endpoint}`;
    const token = localStorage.getItem('token') || "";
    const config = {
      ...options,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
        ...options.headers,
      },
    };

    try {
      const response = await fetch(url, config);
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error('API request failed:', error);
      throw error;
    }
  }

  async register(username, password, email) {
    return this.request('/register', {
      method: 'POST',
      body: JSON.stringify({
        userName: username,
        password: password,
        email: email,
      }),
    })
  }

  async login(username, password) {
    return this.request('/login', {
      method: 'POST',
      body: JSON.stringify({
        userName: username,
        password: password,
      })
    })
  }

  async getPosts(){
    return this.request('/posts');
  }

  async getPostsOfUser(userId){
    return this.request('/posts-of/' + userId);
  }

  async addPost(post){
    return this.request('/posts', {
      method: 'POST',
      body: JSON.stringify(post),
    });
  }

//   // Products API
//   async getProducts() {
//     return this.request('/products');
//   }
//
//   // Dashboard API
//   async getCart() {
//     return this.request('/cart');
//   }
//
//   async addToCart(product) {
//     return this.request('/cart/items', {
//       method: 'POST',
//       body: JSON.stringify({
//         productId: product.id,
//         name: product.name,
//         price: product.price,
//         image: product.image,
//         description: product.description,
//       }),
//     });
//   }
//
//   async updateCartItemQuantity(productId, quantity) {
//     return this.request(`/cart/items/${productId}`, {
//       method: 'PUT',
//       body: JSON.stringify({ quantity }),
//     });
//   }
//
//   async removeFromCart(productId) {
//     return this.request(`/cart/items/${productId}`, {
//       method: 'DELETE',
//     });
//   }
//
//   async clearCart() {
//     return this.request('/cart', {
//       method: 'DELETE',
//     });
//   }

}

const apiClient = new ApiClient();

export default apiClient;
