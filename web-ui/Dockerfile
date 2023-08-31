FROM node:18
WORKDIR /app
COPY . .
RUN yarn install
RUN yarn build
EXPOSE 8080
RUN yarn add serve --dev
CMD ["npx", "serve", "build", "-p", "8080"]