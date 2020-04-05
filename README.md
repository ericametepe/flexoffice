Be sure you have Docker up

Edit the /etc/hosts

ADD  127.0.0.1       flexo-backend

ADD  127.0.0.1       db

Run the App
cd flexoffice

docker-compose build
docker-compose up -d

After change : 
Build a service
e.g : docker-compose build web


The Front is here :
http://localhost:8081/
The Backend is here :
http://localhost:8080/swagger-ui/index.html?url=/v3/api-docs&validatorUrl=#/site-resource/getAllSite



For good experience activate the geolocalisation


