import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import federation from '@originjs/vite-plugin-federation';

export default defineConfig({
  plugins: [
    react(),
    federation({
      name: 'user-profile',
      filename: 'remoteEntry.js',
      exposes: {
        './UserProfile': './src/UserProfile.jsx',
      },
      remotes: {
        host: 'http://localhost:3000/assets/remoteEntry.js',
      },
      shared: ['react', 'react-dom']
    })
  ],
  build: {
    modulePreload: false,
    target: 'esnext',
    minify: false,
    cssCodeSplit: false
  },
  server: {
    port: 3001,
    strictPort: true,
    cors: true
  },
  preview: {
    port: 3001,
    strictPort: true,
    cors: true
  }
});
