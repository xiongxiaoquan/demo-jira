apiVersion: v1
kind: Service
metadata:
  labels:
    app: {APP_NAME}
  name: {APP_NAME}
spec:
  ports:
    - port: 8081
      protocol: TCP
      targetPort: 8081
  selector:
    app: {APP_NAME}
  type: ClusterIP
---
apiVersion: apps/v1beta1
kind: Deployment
metadata:
  name: {APP_NAME}-deployment
spec:
  replicas: 1
  template:
    metadata:
      labels:
        app: {APP_NAME}
    spec:
      containers:
        - name: {MODULE1}
          image: {IMAGE_URL_1}:{IMAGE_TAG}
          ports:
            - containerPort: 20880
        - name: {MODULE2}
          image: {IMAGE_URL_2}:{IMAGE_TAG}
          ports:
            - containerPort: 8081