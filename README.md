Be sure you have Docker up

Edit the /etc/hosts
ADD  127.0.0.1       flexo-backend
ADD  127.0.0.1       db

Run the App
cd flexo-app
docker-compose build
docker-compose up -d

After change : 
Build a service
e.g : docker-compose build web


The App is here :
http://localhost:8081/


