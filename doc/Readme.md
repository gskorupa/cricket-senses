# Deployment on unicloud.pl

Creating new environment on unicloud.pl (Jelastic cloud) is straightforward. After selecting the image from Docker Hub 
the configuration is created automatically. 


# SensesStore API

## Get station data

GET /api/:stationname

```
curl http://mystore.com/api/homestation1
```

## Store station data

We can use `Accept` header to get the data as `text/csv` or JSON (default format).

POST /api

```
curl -i -H "Content-Type: text/csv" -d "homestation1,sensor0,02/09/2016:12:54:45 +0000,30.0,Celsius" http://sensesstore.unicloud.pl/api
```


