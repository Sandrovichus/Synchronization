import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    public static int frequency = 1;                         // частота появления команды "R" в одном маршруте

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> {
            String route = generateRoute("RLRFR", 100);   // генерирует текст
            int countR = 0;
            for (int i = 0; i < route.length(); i++) {
                if (route.charAt(i) == 'R') {
                    countR++;
                }
            }
            synchronized (sizeToFreq) {
                if (sizeToFreq.containsKey(countR)) {
                    int count = sizeToFreq.get(countR);
                    count = count + 1;
                    sizeToFreq.put(countR, count);
                } else {
                    sizeToFreq.put(countR, frequency);
                }
            }
        };
        for (int n = 0; n < 1000; n++) {                // запускает потоки
            Thread thread = new Thread(runnable);
            thread.start();
            thread.join();
        }
        int maxFrequency = Integer.MIN_VALUE;
        StringBuilder list = new StringBuilder();
        Integer maxName = 0;
        for (Integer name : sizeToFreq.keySet()) {       // определяет макимальное количество повторений частоты и ее частоту в маршруте
            if (sizeToFreq.get(name) > maxFrequency) {
                maxFrequency = sizeToFreq.get(name);
                maxName = name;
            }
        }
        for (Integer name : sizeToFreq.keySet()) {       // исключает из списка для печати ключ с макимальной частотой
            if (!Objects.equals(name, maxName)) {
                list.append("- ").append(name).append(" (").append(sizeToFreq.get(name)).append(" раз)\n");
            }
        }
        System.out.println("Самое частое количество повторений " + maxName + " (встретилось " + maxFrequency + " раз)");
        System.out.println("Другие размеры:");
        System.out.println(list);
    }

    public static String generateRoute(String letters, int length) {   // генератор маршрута
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}