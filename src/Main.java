import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException {
        // Задание 1
        createBackup(".");

        // Задание 2
        Tree.print(Paths.get("."));

        // Задание 3
        int[] array = {3,0,1 ,1,2,3, 0,2,1};
        Path path = Paths.get(".\\text.bin");
        serializeArr(array,path);

        int[] array2 = deserializeArr(path);
        System.out.println(Arrays.toString(array2));
    }

    /** Задание 1
     * Создание резервных копий всех файлов в дирректории
     * c размещение их во вложенной дирректории "backup"
     *
     * @param dir путь к дирректории в которой будут производиться
     *            резервное копирование
     * @throws IOException
     */
    public static void createBackup (String dir) throws IOException {
        Path path = Paths.get(dir);

        // Проверка дирректории
        if (!Files.isDirectory(path)){
            throw new IllegalArgumentException();
        }
        // Создание резервной дирректории
        Files.createDirectories(Paths.get(dir+"\\backup"));

        // Копирование файлов в резервную дирректорию
        try (Stream<Path> paths = Files.walk(path,1).filter(Files::isRegularFile)) {
            for (Path filePath:paths.toList()) {
                Path backupDir = Paths.get(".\\backup\\" + filePath.toString());
                Files.copy(filePath,backupDir,StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    /** Задание 3
     * Сериализации массива с последующей записью в файл
     * @param array массив для сериализации
     * @param path путь до файла
     * @throws IOException
     */
    private static void serializeArr(int[] array, Path path) throws IOException {
        // Создаем файл, если его нет
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        try (OutputStream outputStream = Files.newOutputStream(path)) {
            for (int i = 0; i < array.length; i += 3) {
                // "Склеиваем" число из трех элементов массива и пишем в файл
                int value = (array[i+2] << 4) | (array[i+1] << 2) | (array[i]) ;
                outputStream.write(value);
                System.out.println(value);
            }
        }
    }

    /** Задание 3 (Доп)
     * Десериализации массива
     * @param path путь до файла
     * @return int[]
     * @throws IOException
     */
    private static int[] deserializeArr(Path path) throws IOException {
        if (!Files.exists(path)) {
            throw new IllegalArgumentException();
        }

        try (InputStream inputStream = Files.newInputStream(path)) {
            byte[] input = inputStream.readAllBytes();
            int[] result = new int[input.length*3];
            int x = input[0];
            for (int i = 0, j = 0 ; i < result.length; i++ ) {
                if (i % 3 == 0) x = input[j++];
                // "восстанавливаем" элементы массива
                result[i] = x & 3;
                x = x >> 2;
            }
            return result;
        }
    }
}