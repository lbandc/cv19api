#!/bin/bash
gcloud run deploy --image gcr.io/ashfield-telephone-tree/cv19api --platform managed --update-env-vars PROJECT_PATH=api,DB_NAME=cv19api,DB_PASSWORD=cv19api,DB_HOST="34.89.31.83:5432",SPRING_PROFILES_ACTIVE=prod,DB_USER=cv19api,INSTANCE_CONNECTION_NAME=ashfield-telephone-tree:europe-west2:cv19api --add-cloudsql-instances=ashfield-telephone-tree:europe-west2:cv19api
