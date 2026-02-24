package com.raulescobar.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for reading external test data from JSON files
 * Supports reading test data for data-driven testing
 */
public class TestDataReader {
    
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String TEST_DATA_PATH = "testdata/";
    
    /**
     * Read JSON file from test resources
     * @param fileName Name of JSON file (without path)
     * @return JsonNode representing the JSON content
     * @throws IOException if file cannot be read
     */
    public static JsonNode readJsonFile(String fileName) throws IOException {
        String filePath = TEST_DATA_PATH + fileName;
        InputStream inputStream = TestDataReader.class.getClassLoader()
            .getResourceAsStream(filePath);
        
        if (inputStream == null) {
            throw new IOException("File not found: " + filePath);
        }
        
        return mapper.readTree(inputStream);
    }
    
    /**
     * Get categories as Object[][] for TestNG DataProvider
     * @return 2D array with category names
     */
    public static Object[][] getCategoriesForDataProvider() {
        try {
            JsonNode data = readJsonFile("navigation-testdata.json");
            JsonNode categories = data.get("categories");
            
            List<Object[]> categoryList = new ArrayList<>();
            
            for (JsonNode category : categories) {
                String categoryName = category.get("name").asText();
                categoryList.add(new Object[]{categoryName});
            }
            
            return categoryList.toArray(new Object[0][]);
            
        } catch (IOException e) {
            System.err.println("Error reading test data: " + e.getMessage());
            // Return default data as fallback
            return new Object[][] {
                {"Phones"},
                {"Laptops"},
                {"Monitors"}
            };
        }
    }
    
    /**
     * Get products for a specific category
     * @param categoryName Name of the category
     * @return Object[][] with product data for DataProvider
     */
    public static Object[][] getProductsByCategoryForDataProvider(String categoryName) {
        try {
            JsonNode data = readJsonFile("navigation-testdata.json");
            JsonNode products = data.get("products");
            
            List<Object[]> productList = new ArrayList<>();
            
            for (JsonNode product : products) {
                if (product.get("category").asText().equals(categoryName)) {
                    String productName = product.get("name").asText();
                    int expectedPrice = product.get("expectedPrice").asInt();
                    productList.add(new Object[]{productName, expectedPrice});
                }
            }
            
            return productList.toArray(new Object[0][]);
            
        } catch (IOException e) {
            System.err.println("Error reading product data: " + e.getMessage());
            return new Object[0][];
        }
    }
    
    /**
     * Get expected products for a category
     * @param categoryName Name of category
     * @return List of expected product names
     */
    public static List<String> getExpectedProductsForCategory(String categoryName) {
        List<String> products = new ArrayList<>();
        
        try {
            JsonNode data = readJsonFile("navigation-testdata.json");
            JsonNode categories = data.get("categories");
            
            for (JsonNode category : categories) {
                if (category.get("name").asText().equals(categoryName)) {
                    JsonNode expectedProducts = category.get("expectedProducts");
                    
                    for (JsonNode product : expectedProducts) {
                        products.add(product.asText());
                    }
                    break;
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error reading expected products: " + e.getMessage());
        }
        
        return products;
    }
    
    /**
     * Get minimum expected product count for category
     * @param categoryName Name of category
     * @return Minimum number of products expected
     */
    public static int getMinimumProductCount(String categoryName) {
        try {
            JsonNode data = readJsonFile("navigation-testdata.json");
            JsonNode categories = data.get("categories");
            
            for (JsonNode category : categories) {
                if (category.get("name").asText().equals(categoryName)) {
                    return category.get("minimumProductCount").asInt();
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error reading minimum product count: " + e.getMessage());
        }
        
        return 1; // Default fallback
    }
    
    /**
     * Get home page minimum products configuration
     * @return Minimum number of products on home page
     */
    public static int getHomePageMinProducts() {
        try {
            JsonNode data = readJsonFile("navigation-testdata.json");
            return data.get("homePageMinProducts").asInt();
        } catch (IOException e) {
            System.err.println("Error reading home page config: " + e.getMessage());
            return 5; // Default
        }
    }
    
    /**
     * Get all test data as JsonNode for custom parsing
     * @return Complete test data JSON
     */
    public static JsonNode getNavigationTestData() {
        try {
            return readJsonFile("navigation-testdata.json");
        } catch (IOException e) {
            System.err.println("Error reading navigation test data: " + e.getMessage());
            return null;
        }
    }
}