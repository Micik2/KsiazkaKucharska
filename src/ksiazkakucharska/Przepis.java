package ksiazkakucharska;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Przepis {
    private String nazwa;
    private String przepis;
    private File file;

    public File getFile() {
        return file;
    }
    
    public void setFile(File file) {
        this.file = file;
    }
    
    public Przepis(File file) {
        this.file = file;
        String fileName = file.getName();
        String s = fileName;
        file.lastModified();
        if (fileName.length() > 0) {
            int p = fileName.indexOf(".");
            if (p >= 0)
                fileName = fileName.substring(0, p);
            s = fileName.substring(0, 1).toUpperCase();
            if (fileName.length() > 1)
                s = s + fileName.substring(1).replace("_", " ").toLowerCase();
        }
        this.nazwa = s;
    }

    public Przepis(String nazwa, String przepis) {
        this.nazwa = nazwa;
        this.przepis = przepis;
    }

    
    public String getNazwa() {
        return nazwa;
    }

    
    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    
    public String getPrzepis() {
        if (file != null) {
            String zawartosc = "";
            try {
                zawartosc = Pliki.wczytaj(file);
            } catch (IOException ex) {
                Logger.getLogger(Przepis.class.getName()).log(Level.SEVERE, null, ex);
            }
            setPrzepis(zawartosc);
        }
        
        return przepis;
    }

    
    public void setPrzepis(String przepis) {
        this.przepis = przepis;
    }
}
