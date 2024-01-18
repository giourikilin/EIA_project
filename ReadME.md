# Run the project
To run, make sure you are in the root directory of this project and have docker compose installed.
In the terminal, type:
```angular2html
docker compose up
```
This should start eight docker containers. The application is reachable at `localhost:3000`. The monitoring dashboard is available at
`localhost/grafana`. It is possible to log in using the credentials `user: admin, password: admin`. The dashboard is accessible under
the dashboards section and is called `eia dashboard`.