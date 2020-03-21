Be sure you have Docker up

Edit the /etc/hosts
ADD  127.0.0.1       flexo-backend

Run the App
cd flexo-app
docker-compose up -d 

Build a service
docker-compose build web
docker-compose up --no-deps -d web

