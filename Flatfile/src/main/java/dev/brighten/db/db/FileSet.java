package dev.brighten.db.db;

import dev.brighten.db.utils.json.JSONException;
import dev.brighten.db.utils.json.JSONObject;
import dev.brighten.db.utils.json.JSONWriter;
import dev.brighten.db.utils.json.JsonReader;
import lombok.SneakyThrows;
import lombok.val;

import java.io.*;
import java.util.Set;

public class FileSet extends StructureSet {

    private File file;
    private JSONObject jobject;

    public FileSet(File file) {
        super(file.getName().replace(".json", ""));

        this.file = file;
        try {
            this.jobject = new JSONObject(JsonReader.readAll(new BufferedReader(new FileReader(file))));
        } catch (IOException | JSONException e) {
            try {
                val writer = new BufferedWriter(new FileWriter(file));

                writer.write("{}");
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @SneakyThrows
    @Override
    public <T> T getObject(String key) {
        return (T) jobject.get(key);
    }

    @SneakyThrows
    @Override
    public boolean save(Database database) {
        val writer = jobject.write(new BufferedWriter(new FileWriter(file)));

        writer.close();

        return true;
    }

    @SneakyThrows
    @Override
    public boolean input(String key, Object object) {
        return jobject.put(key, object).keySet().contains(key);
    }

    @Override
    public boolean contains(String key) {
        return jobject.keySet().contains(key);
    }

    @Override
    public Set<String> getKeys() {
        return jobject.keySet();
    }
}
