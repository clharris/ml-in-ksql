package ksqlexample.pmml;

import io.confluent.ksql.function.udf.Udf;
import io.confluent.ksql.function.udf.UdfDescription;
import org.dmg.pmml.FieldName;
import org.jpmml.evaluator.*;

import java.util.*;


@UdfDescription(name = "irisLogReg", description = "Iris data example. Model built using logistic regression.")
public class IrisLogReg {

    String modelPath = "/etc/ksql-server/ext/iris-pipeline.pmml";
    Optional<Evaluator> evaluatorOptional = null;
    boolean modelInitialized = false;


    @Udf(description = "Iris data example. Model built using logistic regression.")
    public String irisLogReg(Double sepalLength, Double sepalWidth, Double petalLength, Double petalWidth) {

        if(!modelInitialized){
            evaluatorOptional = PmmlUtils.loadModel(modelPath);
            modelInitialized=true;
        }

       Map<String,Double> dataValues = new HashMap<>();
        dataValues.put(PmmlUtils.SEPAL_LENGTH, sepalLength);
        dataValues.put(PmmlUtils.SEPAL_WIDTH, sepalWidth);
        dataValues.put(PmmlUtils.PETAL_LENGTH, petalLength);
        dataValues.put(PmmlUtils.PETAL_WIDTH, petalWidth);

        Map<String,?> resultRecord = new HashMap<>();

        if (evaluatorOptional.isPresent()){
            Evaluator evaluator = evaluatorOptional.get();
            List<? extends InputField> inputFields = evaluator.getInputFields();

            Map<FieldName, FieldValue> data = PmmlUtils.readRecord(dataValues, inputFields);

            Map<FieldName, ?> results = evaluator.evaluate(data);

            resultRecord = EvaluatorUtil.decode(results);
        }

        return resultRecord.toString();
    }
}
