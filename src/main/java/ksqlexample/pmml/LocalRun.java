package ksqlexample.pmml;

import org.dmg.pmml.FieldName;
import org.jpmml.evaluator.*;

import java.util.*;
import java.util.stream.Collectors;

public class LocalRun {

    /***
     * This is just to test the model loading and evaluation outside of KSQL.
     * Example usage:
     *    java -cp ksql-ml-pmml-example-0.0.1-jar-with-dependencies.jar ksqlexample.pmml.LocalRun iris-pipeline.pmml 7.0,3.2,4.7,4.4
     *    the parameters are expected to be in the order 'sepal length (cm)','sepal width (cm)','petal length (cm)','petal width (cm)'
     */
    public static void main(String[] args) {
        String modelPath = args[0];

        Optional<Evaluator> evaluatorOptional = PmmlUtils.loadModel(modelPath);

        if (evaluatorOptional.isPresent()){
            Evaluator evaluator = evaluatorOptional.get();
            List<? extends InputField> inputFields = evaluator.getInputFields();
            System.out.println("Input fields: " + inputFields);

            List<? extends TargetField> targetFields = evaluator.getTargetFields();
            System.out.println("Target field(s): " + targetFields);

            List<? extends OutputField> outputFields = evaluator.getOutputFields();
            System.out.println("Output fields: " + outputFields);

            Map<FieldName, FieldValue> data = readRecord(args[1], inputFields);

            Map<FieldName,?> results = evaluator.evaluate(data);

            Map<String, ?> resultRecord = EvaluatorUtil.decode(results);

            System.out.println(resultRecord);
            for(String key: resultRecord.keySet()){
                System.out.println(key + ":" + resultRecord.get(key));
            }

        }

    }

    private static Map<FieldName,FieldValue> readRecord(String data, List<? extends InputField> inputFields) {
        List<Double> dataValues = Arrays.stream(data.split(",")).map(d -> Double.parseDouble(d)).collect(Collectors.toList());

        Map<String,Double> dataValuesMap = new HashMap<>();
        dataValuesMap.put(PmmlUtils.SEPAL_LENGTH, dataValues.get(0));
        dataValuesMap.put(PmmlUtils.SEPAL_WIDTH, dataValues.get(1));
        dataValuesMap.put(PmmlUtils.PETAL_LENGTH, dataValues.get(2));
        dataValuesMap.put(PmmlUtils.PETAL_WIDTH, dataValues.get(3));

        return PmmlUtils.readRecord(dataValuesMap, inputFields);
    }


}
