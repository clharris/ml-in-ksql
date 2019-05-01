# ml-in-ksql
This project is for making a simple example of running an ML model in KSQL. Run `mvn clean package` to
build.

The Jupyter notebook in the `notebooks/`
directory generates the model file `iris-pipeline.pkl.z`.  The jar file `files/jars/jpmml-sklearn-executable-1.5-SNAPSHOT.jar`
is used to convert this model file into a PMML file `models/iris-pipeline.pmml`. 

The Docker Compose file is used to start the Confluent Platform components required to run the KSQL server.
Before running `docker-compose up -d` to start it, move the project jar `target/ksql-ml-pmml-example-0.0.1-jar-with-dependencies.jar`
and the model PMML file `models/iris-pipeline.pmml` into the mounted volume `docker/volume`.

There is a branch `loadModelOnce` which addresses the inefficiency of loading the model on each invocation of the UDF "predict"
method. 

This code was developed for the Medium article [ML in KSQL](https://medium.com/homeaway-tech-blog/ml-in-ksql-a44d455b0687).

