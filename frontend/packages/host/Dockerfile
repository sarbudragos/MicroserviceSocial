FROM node:18-alpine as builder

WORKDIR /app

# Copy root package files
COPY /frontend/package.json ./
COPY /frontend/packages/host/package.json ./packages/host/
COPY /frontend/packages/event-bus ./packages/event-bus

# Install dependencies
RUN npm install --workspace=host --workspace=event-bus

# Copy source files
COPY /frontend/packages/host ./packages/host

# Build the application
RUN npm run build --workspace=host

# Production stage
FROM nginx:alpine

# Copy built files to nginx
COPY --from=builder /app/packages/host/dist /usr/share/nginx/html

# Copy nginx configuration
COPY /frontend/packages/host/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 3000

CMD ["nginx", "-g", "daemon off;"]
