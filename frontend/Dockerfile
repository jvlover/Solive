# 가져올 이미지를 정의
FROM node:18.16.1-alpine as build
# 경로 설정하기
WORKDIR /app
# Copy package.json and pnpm-lock.yaml (if available) to cache dependencies
COPY package.json pnpm-lock.yaml* ./
RUN npm install -g pnpm
RUN pnpm install
# 현재 디렉토리의 모든 파일을 도커 컨테이너의 워킹 디렉토리에 복사한다.
COPY . .

# Build the React app
RUN pnpm run build

# 각각의 명령어들은 한줄 한줄씩 캐싱되어 실행된다.
# package.json의 내용은 자주 바뀌진 않을 거지만
# 소스 코드는 자주 바뀌는데
# npm install과 COPY . . 를 동시에 수행하면
# 소스 코드가 조금 달라질때도 항상 npm install을 수행해서 리소스가 낭비된다.

# # 5000번 포트 노출
# EXPOSE 5000

# # npm start 스크립트 실행
# CMD ["pnpm", "dev"]

# Stage 2: Setup Nginx to serve the built React app
FROM nginx:alpine

# Remove default Nginx website
RUN rm -rf /usr/share/nginx/html/*

# Copy the built React app from the 'build' stage to the Nginx directory
COPY --from=build /app/dist /usr/share/nginx/html

# Copy the custom nginx.conf to the container
COPY nginx.conf /etc/nginx/nginx.conf

# Expose port 80
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]