package ksqlexample.pmml;

import org.dmg.pmml.FieldName;
import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.FieldValue;
import org.jpmml.evaluator.InputField;
import org.jpmml.evaluator.LoadingModelEvaluatorBuilder;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PmmlUtils {

    static String SEPAL_LENGTH = "sepal length (cm)";
    static String SEPAL_WIDTH  = "sepal width (cm)";
    static String PETAL_LENGTH = "petal length (cm)";
    static String PETAL_WIDTH  = "petal width (cm)";

    static Optional<Evaluator> loadModel(String modelPath) {

        Optional<Evaluator> evaluatorOptional = Optional.empty();

        try {
            Evaluator evaluator = new LoadingModelEvaluatorBuilder()
                    .load(new File(modelPath))
                    .build();

            evaluator.verify();
            evaluatorOptional = Optional.of(evaluator);
        } catch (Exception e) {
            System.out.println("Could not load model file. ");
        }

        return evaluatorOptional;
    }

    static <T> Map<FieldName,FieldValue> readRecord(Map<String, T> dataValues, List<? extends InputField> inputFields) {
        Map<FieldName, FieldValue> output = new HashMap<>();

        if(dataValues.size() != inputFields.size()){
            System.out.println("Incorrect number of fields.");
            return output;
        }

        for(InputField inputField: inputFields){
//            System.out.println("Input field:" + inputField);
            String fieldName = inputField.getName().toString();
            FieldValue inputValue = inputField.prepare(dataValues.get(fieldName));
//            System.out.println("Input value:" + inputValue);
            output.put(inputField.getName(),inputValue);
        }

        return output;
    }
}
