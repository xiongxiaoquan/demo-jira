apiVersion: v1
kind: Service
metadata:
  labels:
    app: {APP_NAME}
  name: {APP_NAME}
spec:
  ports:
    - port: 80
      protocol: TCP
      targetPort: 8080
      nodePort: 30005
  selector:
    app: {APP_NAME}
  type: NodePort
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
        - name: {MODULE}
          image: {IMAGE_URL}:{IMAGE_TAG}
          ports:
            - containerPort: 8080