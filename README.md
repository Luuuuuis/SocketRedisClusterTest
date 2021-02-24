# SocketRedisClusterTest
A test of scaling a simple websocket server written in Java and can be deployed either locally with HAProxy or deployed to google cloud kubernetes engine with their google autoscaler

To build this reposetory use <code>docker build -t socketserver .</code>.

##Can locally be deployed in two different ways (known to me):
### Docker Swarm
To init docker swarm for the first time: <code>docker swarm init</code>. 

This stack can be deployed using <code>docker stack deploy --compose-file=docker-compose.yml [NAME]</code>.
This may take a while.

Finally you can observe your service using <code>docker service ls</code>.
Scalling is possible with <code>docker service scale [NAME]_ws=[NUM]</code>.
Delete this service using <code>docker service rm [NAME]</code>.

### Kubernetes Engine on Google Cloud
Follow the tutorial on their official site. https://console.cloud.google.com/kubernetes/

A nice sample is provided by them on [GitHub](https://github.com/GoogleCloudPlatform/kubernetes-engine-samples)

### Docker Compose
Docker Compose does NOT work due to network limitations

## Usage
When deploying loacally you can access the websocket via any [webbrowser](https://www.piesocket.com/websocket-tester) or application that supports websockets.
Connectings will be handled under the port 8080 so you can connect using <code>ws://localhost:8080</code>. Port is editable in the [docker-compose.yml](https://github.com/Luuuuuis/SocketRedisClusterTest/blob/master/docker-compose.yml). So is timeout of HAProxy. The [haproxy.cfg](https://github.com/Luuuuuis/SocketRedisClusterTest/blob/master/haproxy/haproxy.cfg) is there just in case you want to add & edit it manually, though not in use with the 
dockercloud/haproxy image.
