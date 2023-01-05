package org.integral.presentation;

import org.integral.Algorithm;
import org.integral.Calculation;
import org.integral.IntegrationTaskInfo;
import org.integral.service.CalculationsHistory;
import org.integral.service.Calculator;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {
    @PostMapping("/task/{id}")
    public String acceptNewTask(@RequestBody IntegrationTaskInfo info, @PathVariable("id") Integer id) {
        Calculator.calculate(info, id);
        return "Accepted";
    }

    @GetMapping("/task/{id}")
    public String getResultById(@RequestBody LanguageRequest request, @PathVariable("id") Integer id) {
        if (request.lang.equals("ukr")) {
            return LanguageUtilities.inUkrainian(CalculationsHistory.getById(id));
        } else if (request.lang.equals("eng")) {
            return LanguageUtilities.inEnglish(CalculationsHistory.getById(id));
        } else {
            throw new IllegalArgumentException("Unacceptable language!");
        }
    }
}

class LanguageRequest {
    public String lang;
}

class LanguageUtilities {
    public static String inUkrainian(Calculation calc) {
        final int fieldsAmount = 7;
        String[] fields = new String[fieldsAmount];
        fields[0] = "\t\"Функція\": " + '\"' + calc.func() + '\"';
        fields[1] = "\t\"Початок\": " + calc.start();
        fields[2] = "\t\"Кінець\": " + calc.end();
        fields[3] = "\t\"Крок\": " + calc.step();
        fields[4] = "\t\"Результат\": " + calc.result();
        fields[5] = "\t\"Ідентифікатор\": " + calc.id();
        fields[6] = "\t\"Алгоритм\": " + '\"'+ Algorithm.inUkrainian() + '\"';
        return "{\n".concat(String.join(",\n", fields)).concat("\n}");
    }

    public static String inEnglish(Calculation calc) {
        final int fieldsAmount = 7;
        String[] fields = new String[fieldsAmount];
        fields[0] = "\t\"Function\": " + '\"' + calc.func() + '\"';
        fields[1] = "\t\"Start\": " + calc.start();
        fields[2] = "\t\"End\": " + calc.end();
        fields[3] = "\t\"Step\": " + calc.step();
        fields[4] = "\t\"Result\": " + calc.result();
        fields[5] = "\t\"Id\": " + calc.id();
        fields[6] = "\t\"Algorithm\": " + '\"'+ Algorithm.inEnglish() + '\"';
        return "{\n".concat(String.join(",\n", fields)).concat("\n}");
    }
}
