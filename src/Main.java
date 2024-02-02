import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static ArrayList<String> fileArray = new ArrayList<>();
    public static String oQuery = "";
    public static String pQuery = "";

    public static String aQuery = "";
    public static String statQuery = "";

    public static File fileString;
    public static File fileInteger;
    public static File fileFloat;

    public static FileWriter writerInt;
    public static FileWriter writerDouble;
    public static FileWriter writerString;

    public static ArrayList<String> resultInteger = new ArrayList<>();
    public static ArrayList<String> resultDouble = new ArrayList<>();
    public static ArrayList<String> resultString = new ArrayList<>();


    public static void main(String[] args) throws Exception {
        checkedArgs(args);
        List<File> filesData = fileArray.stream().map(File::new).collect(Collectors.toList());
        collectData(filesData);
        createFile(pQuery, oQuery);
        writeFile();

        writeStat();
    }

    public static void checkedArgs(String[] args) {
        if (args != null) {
            for (int i = 0; i < args.length;) {
                if (args[i].equals("-o")) {
                    oQuery = args[i + 1];
                    i = i + 2;
                }
                else if (args[i].equals("-p")) {
                    pQuery = args[i + 1];
                    i = i + 2;
                }
                else if (args[i].equals("-a")) {
                    pQuery = "append";
                    i++;
                }
                else if (args[i].equals("-s")) {
                    statQuery = "short";
                    i++;
                }
                else if (args[i].equals("-f")) {
                    statQuery = "full";
                    i++;
                }
                else {
                    fileArray.add(args[i]);
                    i++;
                }
            }
        }
    }

    public static void collectData(List<File> filesData) throws IOException {
        for (File file : filesData) {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String lines = scanner.nextLine();
                if (isNumber(lines.trim())) {
                   resultInteger.add(lines.trim());
                }
                else if (isDouble(lines.trim())) {
                    resultDouble.add(lines.trim());
                }
                else {
                    resultString.add(lines.trim());
                }
            }
        }
    }

    public static boolean isNumber(String str) {
        try {
            Long.parseLong(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    public static void createFile(String prefics, String path) throws Exception {
        generatePathOnParam(prefics, path);
        operationsFileOnA();
    }

    public static void generatePathOnParam(String prefics, String path) {
        if (path.equals("")) {
            fileInteger = new File("./" + prefics + "integers.txt");
            fileFloat = new File("./" + prefics + "floats.txt");
            fileString = new File("./" + prefics + "strings.txt");
        }

        else {
            File dirs = new File(path);

            if (!dirs.exists()) {
                dirs.mkdirs();
            }
            fileString = new File(path + "/" + prefics + "strings.txt");
            fileInteger = new File(path + "/" + prefics + "integers.txt");
            fileFloat = new File(path + "/" + prefics + "floats.txt");
        }
    }

    public static void operationsFileOnA() throws IOException {

        if (!aQuery.equals("append")) {
            fileString.delete();
            fileString.createNewFile();
            fileInteger.delete();
            fileInteger.createNewFile();
            fileFloat.delete();
            fileFloat.createNewFile();
        }
        else {
            if (resultString.size() != 0) {
                if (!fileString.exists()) {
                    fileString.createNewFile();
                }
                else {
                    fileString.delete();
                    fileString.createNewFile();
                }
            }
            if (resultDouble.size() != 0) {
                if (!fileFloat.exists()) {
                    fileFloat.createNewFile();
                }
                else {
                    fileFloat.delete();
                    fileFloat.createNewFile();
                }
            }
            if (resultInteger.size() != 0) {
                if (!fileInteger.exists()) {
                    fileInteger.createNewFile();
                }
                else {
                    fileInteger.delete();
                    fileInteger.createNewFile();
                }
            }
        }
    }
    public static void writeFile() throws IOException {

        if (resultInteger.size() != 0) {
            writerInt = new FileWriter(fileInteger, true);
            for(String intV : resultInteger) {
                writerInt.write(intV);
                writerInt.write(System.lineSeparator());
            }
            writerInt.close();
        }
        if (resultDouble.size() != 0) {
            writerDouble = new FileWriter(fileFloat, true);
            for(String doubleV : resultDouble) {
                writerDouble.write(doubleV);
                writerDouble.write(System.lineSeparator());
            }
            writerDouble.close();
        }
        if (resultString.size() != 0) {
            writerString = new FileWriter(fileString, true);
            for(String stringV : resultString) {
                writerString.write(stringV);
                writerString.write(System.lineSeparator());
            }
            writerString.close();
        }
    }

    public static void writeStat() {
        if (statQuery.equals("short")) {
            makeShortStat();
        }
        else if (statQuery.equals("full")) {
            makeFullStat();
        }
        else {
            System.out.println("А нет статистки, кто то не ввел код");
        }
    }

    public static void makeShortStat() {
        System.out.println("Кол-во целых чисел в файлах:" + resultInteger.size());
        System.out.println("Кол-во чисел с плавающей запятой в файлах:" + resultDouble.size());
        System.out.println("Кол-во строк в файлах:" + resultInteger.size());
    }

    public static void makeFullStat() {
        makeShortStat();
        makeStatLong();
        makeStatDouble();
        makeStatString();
    }
    public static void makeStatLong() {
        Long maxValue;
        Long minValue;
        long summ;
        long middle;
        if (!resultInteger.isEmpty()) {
            maxValue = resultInteger.stream().map(Long::parseLong).toList().stream().max(Long::compare).get();
            minValue = resultInteger.stream().map(Long::parseLong).toList().stream().min(Long::compare).get();
            summ = resultInteger.stream().map(Long::parseLong).toList().stream().reduce(Long::sum).get();
            middle = summ / resultInteger.size();
            System.out.println("Целые числа: ");
            System.out.println("\t Максимальное значение:" + maxValue);
            System.out.println("\t Минимальное значение:" + minValue);
            System.out.println("\t Сумма:" + summ);
            System.out.println("\t Среднее значение:" + middle);
        }
    }
    public static void makeStatDouble() {
        Double maxValue;
        Double minValue;
        double summ;
        double middle;
        if (!resultDouble.isEmpty()) {
            maxValue = resultDouble.stream().map(Double::parseDouble).toList().stream().max(Double::compare).get();
            minValue = resultDouble.stream().map(Double::parseDouble).toList().stream().min(Double::compare).get();
            summ = resultDouble.stream().map(Double::parseDouble).toList().stream().reduce(Double::sum).get();
            middle = summ / resultDouble.size();
            System.out.println("Числа с плавающей запятой: ");
            System.out.println("\t Максимальное значение:" + maxValue);
            System.out.println("\t Минимальное значение:" + minValue);
            System.out.println("\t Сумма:" + summ);
            System.out.println("\t Среднее значение:" + middle);
        }
    }
    public static void makeStatString() {
        Integer maxValue;
        Integer minValue;
        if (!resultDouble.isEmpty()) {
            maxValue = resultString.stream().map(String::length).max(Integer::compare).get();
            minValue = resultString.stream().map(String::length).min(Integer::compare).get();

            System.out.println("Строки: ");
            System.out.println("\t Размер самой длинной:" + maxValue);
            System.out.println("\t Размер самой короткой:" + minValue);
        }
    }

}