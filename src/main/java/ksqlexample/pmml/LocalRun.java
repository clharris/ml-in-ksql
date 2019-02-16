package ksqlexample.pmml;

import org.dmg.pmml.FieldName;
import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.InputField;
import org.jpmml.evaluator.FieldValue;
import org.jpmml.evaluator.EvaluatorUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Arrays;
import java.util.stream.Collectors;

public class LocalRun {

    /***
     * This is just to test the model loading and evaluation outside of KSQL.
     * Example usage:
     *    java -cp ksql-ml-pmml-example-0.0.1-jar-with-dependencies.jar com.homeaway.cloud.LocalRun iris-pipeline.pmml 7.0,3.2,4.7,4.4
     */
    public static void main(String[] args) {
        String modelPath = args[0];

        Optional<Evaluator> evaluatorOptional = PmmlUtils.loadModel(modelPath);

        if (evaluatorOptional.isPresent()){
            Evaluator evaluator = evaluatorOptional.get();
            List<? extends InputField> inputFields = evaluator.getInputFields();
//            System.out.println("Input fields: " + inputFields);
//
//            List<? extends TargetField> targetFields = evaluator.getTargetFields();
//            System.out.println("Target field(s): " + targetFields);
//
//            List<? extends OutputField> outputFields = evaluator.getOutputFields();
//            System.out.println("Output fields: " + outputFields);

            Map<FieldName, FieldValue> data = readRecord(args[1], inputFields);

            Map<FieldName,?> results = evaluator.evaluate(data);

            Map<String, ?> resultRecord = EvaluatorUtil.decode(results);

            System.out.println(resultRecord);
//            for(String key: resultRecord.keySet()){
//                System.out.println(key + ":" + resultRecord.get(key));
//            }

        }

    }

    private static Map<FieldName,FieldValue> readRecord(String data, List<? extends InputField> inputFields) {
        List<Double> dataValues = Arrays.stream(data.split(",")).map(d -> Double.parseDouble(d)).collect(Collectors.toList());

        return PmmlUtils.readRecord(dataValues, inputFields);
    }


}