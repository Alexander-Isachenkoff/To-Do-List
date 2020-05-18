package sample;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class FileList<T> extends Observable
{
    private final File file;
    
    public FileList(File file) {
        this.file = file;
        if (!file.exists()) {
            writeList(new ArrayList<>());
        }
    }
    
    public List<T> readList() {
        try {
            return (List<T>) new ObjectInputStream(new FileInputStream(file)).readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public void add(T object) {
        List<T> list = readList();
        list.add(object);
        writeList(list);
        setChanged();
        notifyObservers();
    }
    
    public boolean contains(T object) {
        return readList().contains(object);
    }
    
    public void remove(T object) {
        List<T> list = readList();
        list.remove(object);
        writeList(list);
        setChanged();
        notifyObservers();
    }
    
    private void writeList(List<T> list) {
        try {
            new ObjectOutputStream(new FileOutputStream(file)).writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
