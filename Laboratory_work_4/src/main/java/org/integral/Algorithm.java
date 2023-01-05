package org.integral;

public class Algorithm {
    public static String inUkrainian() {
        String algorithmName = "Метод Трапецій";
        return algorithmName + ": Спочатку визначємо, чи довжина інтервалу менша за крок. Якщо так, повертаємо " +
                "площу трапеції з кутами на межах інтервалу і шириною що рівна довжині інтервалу. " +
                "Якщо ні, тоді ми обчислюємо площі всіх трапецій в інтервалі, ітеруючись з кроком і зупиняючись перед " +
                "останньою трапеціяєю. Потім ми обчислюємо площу останньої трапеції та додаємо її до результату";
    }

    public static String inEnglish() {
        String algorithmName = "Trapezium method";
        return algorithmName + ": Firstly decide whether the interval length is less then step. If so then return " +
                "the area of trapeze with corners on bounds of interval and the width of length of interval. " +
                "If not, then we compute the areas of all trapezes in interval iterating with step and stopping before " +
                "the last trapeze. Then we compute the area of last trapeze and add it to result";
    }
}
