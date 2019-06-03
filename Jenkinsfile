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
      docker {
          image 'arm32v7/maven:3-jdk-8-alpine'
          }
      }
     steps {
          releaseBuild()
         }
    }



   }

}

  def releaseBuild()
  {


    withMaven(
        mavenSettingsConfig: '53fc6614-b570-41b3-b18c-574ba701725f',
        mavenLocalRepo: '.repository') {

        // Run the maven build
        def pom = readMavenPom file: 'pom.xml'
        def version = pom.version.replace("-SNAPSHOT", "")
        def anfang = version.substring(0,version.lastIndexOf('.')+1);
        def newNumber= (((CharSequence) version.substring(version.lastIndexOf('.')+1, version.length())).toInteger() +1)
        def newVersion = anfang + newNumber + "-SNAPSHOT"

        sh "mvn release:clean"
        sh "mvn -DreleaseVersion=${version} -DdevelopmentVersion=${newVersion} -DpushChanges=false -DlocalCheckout=true -DaltDeploymentRepository=youthclubstage::default::https://archiva.youthclubstage.de/repository/youthclubstage -DpreparationGoals=initialize release:prepare release:perform -B"
        sh "git push --tags"
        sh "git push"


    } // withMaven will discover the generated Maven artifacts, JUnit Surefire & FailSafe & FindBugs reports...


  }

