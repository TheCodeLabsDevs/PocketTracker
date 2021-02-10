wget https://repo1.maven.org/maven2/org/openapitools/openapi-generator-cli/5.0.1/openapi-generator-cli-5.0.1.jar -O openapi-generator-cli.jar
java -jar openapi-generator-cli.jar generate \
  -i api-docs.json \
  -g java \
  -c configuration.json \
  -o $(pwd)/target

cd target
mvn deploy -DaltDeploymentRepository=release::default::https://maven.thecodelabs.de/artifactory/TheCodeLabs-release