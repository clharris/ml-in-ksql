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

    static <T> Map<FieldName,FieldValue> readRecord(List<T> dataValues, List<? extends InputField> inputFields) {
        Map<FieldName, FieldValue> output = new HashMap<>();

        if(dataValues.size() != inputFields.size()){
            System.out.println("Incorrect number of fields.");
            return output;
        }

        for(int i = 0; i < inputFields.size(); i++){
            InputField inputField = inputFields.get(i);
            FieldValue inputValue = inputField.prepare(dataValues.get(i));
            output.put(inputField.getName(),inputValue);
        }

        return output;
    }
}
