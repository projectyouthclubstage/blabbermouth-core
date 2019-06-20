import java.text.SimpleDateFormat

pipeline{

agent none
  environment {

    def projektname = "pipeline-example"
    def registry = "192.168.233.1:5000/pipeline-example"
    def dns = "pe.youthclubstage.de"
    def dnsblue = "peb.youthclubstage.de"
    def port = "8080"

  }


stages{
  
    stage('Prepare'){
    agent {
         label 'master'
    }
     steps {
          script{
            gitprepare()
          }
        }
    }
      // Run Maven build, skipping tests
    stage('Build'){
    agent {
        docker {
          image 'arm32v7/maven:3-jdk-8-alpine'
        }
    }
     steps {
        sh "mvn -B clean install -DskipTests=true"
        }
    }

    // Run Maven unit tests
    stage('Unit Test'){
    agent {
       docker {
          image 'arm32v7/maven:3-jdk-8-alpine'
          }
    }
     steps {
         sh "mvn -B test"
        }
    }


    // Run Maven unit tests
    stage('Release Build'){

    agent {
         label 'master'
    }
     steps {
     script{
       def release = input(message: 'Release?', ok: 'Yes',
                        parameters: [booleanParam(defaultValue: false,
                        description: 'Release?',name: 'Yes?')])
    if(release){
       withCredentials([[$class: 'UsernamePasswordMultiBinding', 
    credentialsId: 'github-pipline-token', 
    usernameVariable: 'GIT_USERNAME',
    passwordVariable: 'GIT_PASSWORD'
]]) {
       
           releaseBuild()
       }
         }
     }
         }
    }



   }

}

  def releaseBuild()
  {


    withMaven(
        maven: 'mvn',
        jdk: 'jdk8',
        mavenSettingsConfig: '53fc6614-b570-41b3-b18c-574ba701725f',
        mavenLocalRepo: '.repository') {

        gitprepare()
        // Run the maven build
        def pom = readMavenPom file: 'pom.xml'
        def version = pom.version.replace("-SNAPSHOT", "")
        def anfang = version.substring(0,version.lastIndexOf('.')+1);
        def newNumber= (((CharSequence) version.substring(version.lastIndexOf('.')+1, version.length())).toInteger() +1)
        def newVersion = anfang + newNumber + "-SNAPSHOT"

        sh "mvn release:clean"
        sh "mvn -Dmaven.test.skip=true -DreleaseVersion=${version} -DdevelopmentVersion=${newVersion} -DpushChanges=true -DlocalCheckout=false -DaltDeploymentRepository=deegsolutionrepo::default::https://archiva.youthclubstage.de/repository/youthclubstage -DpreparationGoals=initialize release:prepare release:perform -B"
      
    } // withMaven will discover the generated Maven artifacts, JUnit Surefire & FailSafe & FindBugs reports...
  }

    def gitprepare(){
        sh "git config --global user.email sascha.deeg@gmail.com"
        sh "git config --global user.name 'Sascha Deeg'"
        sh "git checkout master"
        sh "git reset --hard origin/master"
        sh "git clean -fxd"
        sh "git pull"
    }

