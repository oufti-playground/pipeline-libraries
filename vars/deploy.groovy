#!/usr/bin/env groovy

def call(String workerImageName) {
  stage('Approval') {
    input(message: 'Is it OK to deploy Boss ?', id: 'boss')
  }
  stage('Deploy') {
    node('docker') {
      GIT_SHORT_CHANGESET = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
      sh "docker tag ${workerImageName} localhost:5000/${workerImageName}:${GIT_SHORT_CHANGESET}"
      sh "docker push localhost:5000/${workerImageName}:${GIT_SHORT_CHANGESET}"
    }
  }
}
