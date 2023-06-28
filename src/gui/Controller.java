package gui;

import fractionexception.MixedFractionException;

import java.awt.*;
import java.io.IOException;


public class Controller {

    Model model;
    View view;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    public void handleFraction(String converted) throws MixedFractionException {
        String result = this.model.calculateFraction(converted);
        String result2 = this.model.calculateProblem(result, 'x');
        double decimalFraction = Double.parseDouble(result2);
        result2 = this.model.decimalToMixedFraction(decimalFraction);
        this.view.setResult(result2);
    }

    public void handleCalculation(String problem) throws MixedFractionException {
        String result = this.model.calculateProblem(problem, 'y');
        this.view.setResult(result);
    }

    public void start() throws IOException, FontFormatException {
        this.view.showUI();
    }

}
