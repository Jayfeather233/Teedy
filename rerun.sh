mvn clean -DskipTests install

cd docs-web
mvn jetty:run

mvn clean -Dmaven.test.failure.ignore=true -Dpmd.failOnViolation=false install site:site jacoco:report site:stage


mvn clean -DskipTests -Dmaven.test.failure.ignore=true -Dpmd.failOnViolation=false install
cd docs-web
mvn jetty:run


mvn clean install -Pprod -DskipTests -Dmaven.test.failure.ignore=true -Dpmd.failOnViolation=false
mvn jetty:run-war -Pprod -DskipTests