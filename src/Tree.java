import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Задание 2
 * Класс со статическими методами для отрисовки дирректорий
 * и файлов
 */

public class Tree {

    /** Вызывает перегруженный метод Tree.print()
     * @param path путь до целевой дирректории
     * @throws IOException
     */
    public static void print(Path path) throws IOException {
        print(path,"",true);
    }

    /**Метод отрисовки дерева дирректорий, вместе с файлами
     * @param path путь до целевой дирректории
     * @param indent строка формирующая отступ от левого края
     * @param isLast указание на последний элемент
     */
    private static void print(Path path, String indent, boolean isLast) throws IOException {
        Path fileName = path.getFileName();

        // Рисуем отступ и имя файла
        System.out.println(indent + (isLast? "└─" : "├─") + fileName);

        // Получаем все файлы и дирректории в каталоге
        List<Path> filesAndDir = Files.walk(path,1).toList();

        // Обновляем отступ для последующих дирректорий и файлов
        indent += isLast? "  ":"│ ";

        // Распечатываем их
        for (int i = 1; i < filesAndDir.size(); i++){
                print(filesAndDir.get(i), indent, i == filesAndDir.size()-1);
        }
    }

}
