#!/usr/bin/env groovy

def call(String workerImageName, String registryUrl) {
  stage('Approval') {
    input(message: 'Is it OK to deploy Boss ?', id: 'boss')
  }
  stage('Deploy') {
    node('docker') {
      GIT_SHORT_CHANGESET = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
      FULL_REGISTRY_IMAGE_NAME="${registryUrl}/${workerImageName}:${GIT_SHORT_CHANGESET}"
      withEnv(['DOCKER_HOST=tcp://docker-service:2375']) {
        sh "docker tag ${workerImageName}:${GIT_SHORT_CHANGESET} ${FULL_REGISTRY_IMAGE_NAME}"
        sh "docker push ${FULL_REGISTRY_IMAGE_NAME}"
      }
    }
  }
}
