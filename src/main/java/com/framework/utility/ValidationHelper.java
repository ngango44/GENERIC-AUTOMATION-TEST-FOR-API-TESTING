package com.framework.utility;

import java.util.LinkedHashMap;

public class ValidationHelper {
    /**
     *
     * @param rawValue the raw string value from Excel
     * @param columName schemaFile
     * @param entryDelimiter The delimiter between entries (usually ";")
     * @param keyValueDelimiter The delimiter within each entry (usually ":")
     * @param data Test case data for context
     * @return Array of entries
     */
    public String[][] splitMultipleEntriesAndValidate(String rawValue, String entryDelimiter,
                                                      String keyValueDelimiter, LinkedHashMap<String, String> data){
        String[] entries = rawValue.split(entryDelimiter);
        String[][] results = new String[entries.length][];
        for (int i = 0; i< entries.length; i++){
            String entry = entries[i].trim();
            String[] parts = entry.split(keyValueDelimiter,2);
            results[i] = new String[]{ parts[0].trim(),parts[1].trim()};
        }
        return results;
    }
}
