package ksiazkakucharska;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Pliki {
    @SuppressWarnings("empty-statement")
    public static String wczytaj(File file) throws IOException {
        BufferedReader reader = null;
        String content = "";
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                content = content + line;
            };
        } finally {
            if (reader != null)
                reader.close();
        }
        return content;
    }
    
    public static void zapisz(File file, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        }
    }   
    
    public static File[] listaPlikow(String sciezka) throws IOException {
        File[] files = new File(sciezka).listFiles();
        return files;
    }

    static void usun(File file) {
        file.delete();
    }
    
}
