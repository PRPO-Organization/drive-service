az acr login --name prporegistry
docker build -t prporegistry.azurecr.io/drive-service:latest .
docker push prporegistry.azurecr.io/drive-service:latest





