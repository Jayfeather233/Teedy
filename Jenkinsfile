pipeline {
    agent any
    environment {
        DEPLOYMENT_NAME = "teedy"
        CONTAINER_NAME = "teedy_node"
        IMAGE_NAME = "teedy2025_manual"
    }
    stages {
        stage('Start Kind') {
            steps {
                sh '''
                if ! kind get clusters | grep -q "${DEPLOYMENT_NAME}"; then
                    echo "Starting kind clusters..."
                    kind create cluster --name ${DEPLOYMENT_NAME}
                else
                    echo "Kind cluster already running."
                fi
                kind load docker-image ${IMAGE_NAME} --name ${DEPLOYMENT_NAME}
                kubectl config use-context kind-${DEPLOYMENT_NAME}
                '''
            }
        }
        stage('deploying') {
            steps {
                sh '''
                echo "deploying..."
                kubectl apply -f hello-node.yaml
                kubectl apply -f k8s_deploy.yaml
                sleep 5
                nohup kubectl port-forward service/hello-node 8081:8080 > port-forward.log 2>&1 &
                '''
            }
        }
        stage('Verify') {
            steps {
                sh 'kubectl get pods'
            }
        }
    }
}