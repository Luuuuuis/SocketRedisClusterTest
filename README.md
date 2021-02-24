# SocketRedisClusterTest
A test of auto scaling a simple websocket server written in Java and can be deployed either locally with HAProxy or deployed to google cloud kubernetes engine with their google autoscaler

To build this reposetory use <code>docker build -t socketserver .</code>.

Can locally be deployed in two different ways (known to me):
## Docker Swarm
To init docker swarm for the first time: <code>docker swarm init</code>. 

This stack can be deployed using <code>docker stack deploy --compose-file=docker-compose.yml [NAME]</code>.
This may take a while.

Finally you can observe your service using <code>docker service ls</code>.
Scalling is possible with <code>docker service scale [NAME]_ws=[NUM]</code>.
Delete this service using <code>docker service rm [NAME]</code>.

## Kubernetes Engine on Google Cloud
Follow the tutorial on their official site. https://console.cloud.google.com/kubernetes/

A nice sample is provided by them on [GitHub](https://github.com/GoogleCloudPlatform/kubernetes-engine-samples)

## Docker Compose
Docker Compose does NOT work due to network limitations
