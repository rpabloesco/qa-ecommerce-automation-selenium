package com.raulescobar.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final String CONFIG_FILE = "config.properties";
    private final Properties mainProps = new Properties();
    private final Properties envProps = new Properties();

    public ConfigReader() {
        loadMainConfig();
        loadEnvconfig();
    }

    private void loadMainConfig() {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if(input == null) {
                throw new RuntimeException("No se pudo cargar el archivo de configuración: " + CONFIG_FILE);
            }
            mainProps.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar el archivo de configuración: " + CONFIG_FILE, e);
        }
    }

    private void loadEnvconfig() {
        String env = get("env","qa");
        String envFile = "environments/" + env + ".properties";
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(envFile)) {
            if(input == null) {
                throw new RuntimeException("No se pudo cargar el archivo de configuración: " + envFile);
            }
            envProps.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar el archivo de configuración: " + envFile, e);
        }
    }

    public String get(String key, String defaultValue) {
        return mainProps.getProperty(key, defaultValue);
    }

    public String getEnv(String key){
        String value = envProps.getProperty(key);
        if(value == null) {
            throw new RuntimeException("La variable de entorno '" + key + "' no está definida en el archivo de configuración");
        }
        return value;
    }

    public int getInt(String key, int defaultValue) {
        try{
            return Integer.parseInt(get(key, String.valueOf(defaultValue)));
        }catch(NumberFormatException e) {
            throw new RuntimeException("La variable '" + key + "' no es un número válido", e);
        }
    }

}
