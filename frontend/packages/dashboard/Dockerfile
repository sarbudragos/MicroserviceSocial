FROM node:18-alpine as builder

WORKDIR /app

# Copy root package files

COPY /frontend/package.json ./
COPY /frontend/packages/dashboard/package.json ./packages/dashboard/
COPY /frontend/packages/event-bus ./packages/event-bus

# Install dependencies
RUN npm install --workspace=dashboard --workspace=event-bus

# Copy source files
COPY /frontend/packages/dashboard ./packages/dashboard

# Build the application
RUN npm run build --workspace=dashboard

# Production stage
FROM nginx:alpine

# Copy built files to nginx
COPY --from=builder /app/packages/dashboard/dist /usr/share/nginx/html

# Copy nginx configuration
COPY /frontend/packages/dashboard/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 3002

CMD ["nginx", "-g", "daemon off;"]
