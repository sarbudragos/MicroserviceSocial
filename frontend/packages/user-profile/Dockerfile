FROM node:18-alpine as builder

WORKDIR /app

# Copy root package files
COPY /frontend/package.json ./
COPY /frontend/packages/user-profile/package.json ./packages/user-profile/
COPY /frontend/packages/event-bus ./packages/event-bus

# Install dependencies
RUN npm install --workspace=user-profile --workspace=event-bus

# Copy source files
COPY /frontend/packages/user-profile ./packages/user-profile

# Build the application
RUN npm run build --workspace=user-profile

# Production stage
FROM nginx:alpine

# Copy built files to nginx
COPY --from=builder /app/packages/user-profile/dist /usr/share/nginx/html

# Copy nginx configuration
COPY /frontend/packages/user-profile/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 3001

CMD ["nginx", "-g", "daemon off;"]
