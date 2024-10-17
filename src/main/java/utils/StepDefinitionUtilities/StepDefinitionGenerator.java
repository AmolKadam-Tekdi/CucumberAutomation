package utils.StepDefinitionUtilities;
/*


import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class StepDefinitionGenerator {

    public static void main(String[] args) {
        String featureDirectoryPath = "src/main/java/feature/"; // Path where all feature files are located
        String stepDefinitionDirectory = "src/main/java/stepdefinitions/"; // Directory for step definitions
        String backupDirectoryPath = "src/Backup/"; // Directory for backups

        try {
            // Clean up old backups (older than 10 days)
            cleanOldBackups(backupDirectoryPath);

            // Backup before generating step definitions
            createBackup(featureDirectoryPath, stepDefinitionDirectory, backupDirectoryPath);

            // Process all feature files and generate step definitions
            processFeatureFiles(featureDirectoryPath, stepDefinitionDirectory);
        } catch (IOException e) {
            System.err.println("Error during backup or processing: " + e.getMessage());
        }
    }

    public static void processFeatureFiles(String featureDirectoryPath, String stepDefinitionDirectory) throws IOException {
        List<Path> featureFiles = getFeatureFiles(featureDirectoryPath);

        if (featureFiles.isEmpty()) {
            System.out.println("No feature files found in directory: " + featureDirectoryPath);
            return;
        }

        for (Path featureFilePath : featureFiles) {
            System.out.println("Processing feature file: " + featureFilePath);
            generateStepDefinitions(featureFilePath, stepDefinitionDirectory);
        }
    }

    private static List<Path> getFeatureFiles(String featureDirectoryPath) throws IOException {
        List<Path> featureFiles = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(featureDirectoryPath), "*.feature")) {
            for (Path entry : stream) {
                featureFiles.add(entry);
            }
        }
        return featureFiles;
    }

    public static void generateStepDefinitions(Path featureFilePath, String stepDefinitionDirectory) throws IOException {
        List<String> featureLines = Files.readAllLines(featureFilePath);
        String featureFileName = getFileNameWithoutExtension(featureFilePath.getFileName().toString());
        String stepDefinitionFilePath = stepDefinitionDirectory + featureFileName + "Steps.java";

        if (Files.exists(Paths.get(stepDefinitionFilePath))) {
            System.out.println("Step definition file already exists: " + stepDefinitionFilePath);
            Set<String> existingMethods = getExistingMethods(stepDefinitionFilePath);
            appendNewSteps(featureLines, stepDefinitionFilePath, existingMethods);
        } else {
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(stepDefinitionFilePath))) {
                writer.write("package stepdefinitions;\n\n");
                writer.write("import io.cucumber.java.en.*;\n\n");
                writer.write("public class " + featureFileName + "Steps {\n");

                for (String line : featureLines) {
                    String methodStub = generateMethodStub(line, new HashSet<>());
                    if (methodStub != null) {
                        writer.write(methodStub);
                    } else {
                        System.out.println("Ignored line: " + line);
                    }
                }

                writer.write("}\n");
            }
            System.out.println("Step definition file generated: " + stepDefinitionFilePath);
        }
    }

    private static String getFileNameWithoutExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf('.');
        return lastIndexOfDot == -1 ? fileName : fileName.substring(0, lastIndexOfDot);
    }

    private static String generateMethodStub(String line, Set<String> existingMethods) {
        line = line.trim();

        if (line.startsWith("Given") || line.startsWith("When") || line.startsWith("Then") || line.startsWith("And")) {
            String[] parts = line.split(" ", 2);
            String annotation = parts[0];
            String stepText = parts.length > 1 ? parts[1].trim() : "";

            Matcher matcher = Pattern.compile("\"([^\"]*)\"").matcher(stepText);
            boolean hasParameter = matcher.find();
            if (hasParameter) {
                stepText = stepText.replace(matcher.group(0), "{string}");
            }

            String methodName = stepText.replaceAll("[^a-zA-Z0-9]", "_").toLowerCase();
            methodName = methodName.replaceAll("_+", "_");

            if (existingMethods.contains(methodName)) {
                System.out.println("Method already exists for step: " + stepText);
                return null;
            }

            if (hasParameter) {
                return String.format("\t@%s(\"%s\")\n\tpublic void %s(String inputString) {\n\t\t// TODO: Implement step\n\t\tthrow new io.cucumber.java.PendingException();\n\t}\n\n",
                        annotation, stepText, methodName);
            } else {
                return String.format("\t@%s(\"%s\")\n\tpublic void %s() {\n\t\t// TODO: Implement step\n\t\tthrow new io.cucumber.java.PendingException();\n\t}\n\n",
                        annotation, stepText, methodName);
            }
        }
        return null;
    }

    private static Set<String> getExistingMethods(String stepDefinitionFilePath) throws IOException {
        Set<String> existingMethods = new HashSet<>();
        List<String> lines = Files.readAllLines(Paths.get(stepDefinitionFilePath));

        Pattern methodPattern = Pattern.compile("public void (\\w+)\\(");
        for (String line : lines) {
            Matcher matcher = methodPattern.matcher(line);
            if (matcher.find()) {
                existingMethods.add(matcher.group(1));
            }
        }
        return existingMethods;
    }

    private static void appendNewSteps(List<String> featureLines, String stepDefinitionFilePath, Set<String> existingMethods) throws IOException {
        List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(stepDefinitionFilePath)));
        int closingBraceIndex = fileContent.lastIndexOf("}");

        if (closingBraceIndex == -1) {
            System.err.println("Invalid step definition file: Missing closing brace.");
            return;
        }

        List<String> newMethods = new ArrayList<>();
        for (String line : featureLines) {
            String methodStub = generateMethodStub(line, existingMethods);
            if (methodStub != null) {
                newMethods.add(methodStub);
            }
        }

        fileContent.addAll(closingBraceIndex, newMethods);
        Files.write(Paths.get(stepDefinitionFilePath), fileContent);
        System.out.println("Appended new steps to: " + stepDefinitionFilePath);
    }

    // Backup method to create a .zip file with segregated feature and step definition files
    private static void createBackup(String featureDirectoryPath, String stepDefinitionDirectory, String backupDirectoryPath) throws IOException {
        Files.createDirectories(Paths.get(backupDirectoryPath)); // Ensure backup directory exists

        // Get current date and time in the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String timestamp = LocalDateTime.now().format(formatter);
        String backupFileName = backupDirectoryPath + "backup_" + timestamp + ".zip";

        try (FileOutputStream fos = new FileOutputStream(backupFileName);
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {

            // Add feature files to the backup inside "features/" folder
            addFilesToZip(featureDirectoryPath, zipOut, "features/");

            // Add step definition files to the backup inside "stepdefinitions/" folder
            addFilesToZip(stepDefinitionDirectory, zipOut, "stepdefinitions/");

            System.out.println("Backup created: " + backupFileName);
        }
    }

    // Method to add files to the zip with a specified folder inside the zip (segregated by type)
    private static void addFilesToZip(String directoryPath, ZipOutputStream zipOut, String zipFolder) throws IOException {
        File directoryToZip = new File(directoryPath);
        File[] files = directoryToZip.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        ZipEntry zipEntry = new ZipEntry(zipFolder + file.getName());
                        zipOut.putNextEntry(zipEntry);

                        byte[] bytes = new byte[1024];
                        int length;
                        while ((length = fis.read(bytes)) >= 0) {
                            zipOut.write(bytes, 0, length);
                        }
                    }
                }
            }
        }
    }

    // Method to clean up old backups (older than 10 days)
    // Method to clean up old backups (older than 10 days)
    private static void cleanOldBackups(String backupDirectoryPath) throws IOException {
        File backupDirectory = new File(backupDirectoryPath);
        File[] backupFiles = backupDirectory.listFiles();

        if (backupFiles != null) {
            for (File backupFile : backupFiles) {
                if (backupFile.isFile() && backupFile.getName().endsWith(".zip")) {
                    long lastModifiedTime = backupFile.lastModified();
                    LocalDateTime fileTime = Instant.ofEpochMilli(lastModifiedTime).atZone(ZoneId.systemDefault()).toLocalDateTime();
                    LocalDateTime tenDaysAgo = LocalDateTime.now().minusDays(10);
//                    LocalDateTime tenminutesAgo = LocalDateTime.now().minusMinutes(10);

                    // Check if the file is older than 10 days
                    if (fileTime.isBefore(tenDaysAgo)) {
                        boolean deleted = backupFile.delete();
                        if (deleted) {
                            System.out.println("Deleted old backup file: " + backupFile.getName());
                        } else {
                            System.err.println("Failed to delete backup file: " + backupFile.getName());
                        }
                    }
                }
            }
        }
    }
}


*/



import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class StepDefinitionGenerator {

    public static void main(String[] args) {
        String featureDirectoryPath = "src/main/java/feature/"; // Path where all feature files are located
        String stepDefinitionDirectory = "src/main/java/stepdefinitions/"; // Directory for step definitions
        String backupDirectoryPath = "src/Backup/"; // Directory for backups

        try {
            // Clean up old backups (older than 10 days)
            cleanOldBackups(backupDirectoryPath);

            // Backup before generating step definitions
            createBackup(featureDirectoryPath, stepDefinitionDirectory, backupDirectoryPath);

            // Process all feature files and generate step definitions
            processFeatureFiles(featureDirectoryPath, stepDefinitionDirectory);
        } catch (IOException e) {
            System.err.println("Error during backup or processing: " + e.getMessage());
        }
    }

    public static void processFeatureFiles(String featureDirectoryPath, String stepDefinitionDirectory) throws IOException {
        List<Path> featureFiles = getFeatureFiles(featureDirectoryPath);

        if (featureFiles.isEmpty()) {
            System.out.println("No feature files found in directory: " + featureDirectoryPath);
            return;
        }

        // Gather all existing methods from all step definition files
        Set<String> existingMethods = getExistingMethodsInAllStepDefinitions(stepDefinitionDirectory);

        for (Path featureFilePath : featureFiles) {
            System.out.println("Processing feature file: " + featureFilePath);
            generateStepDefinitions(featureFilePath, stepDefinitionDirectory, existingMethods);
        }
    }

    private static List<Path> getFeatureFiles(String featureDirectoryPath) throws IOException {
        List<Path> featureFiles = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(featureDirectoryPath), "*.feature")) {
            for (Path entry : stream) {
                featureFiles.add(entry);
            }
        }
        return featureFiles;
    }

    public static void generateStepDefinitions(Path featureFilePath, String stepDefinitionDirectory, Set<String> existingMethods) throws IOException {
        List<String> featureLines = Files.readAllLines(featureFilePath);
        String featureFileName = getFileNameWithoutExtension(featureFilePath.getFileName().toString());
        String stepDefinitionFilePath = stepDefinitionDirectory + featureFileName + "Steps.java";

        if (Files.exists(Paths.get(stepDefinitionFilePath))) {
            System.out.println("Step definition file already exists: " + stepDefinitionFilePath);
            appendNewSteps(featureLines, stepDefinitionFilePath, existingMethods);
        } else {
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(stepDefinitionFilePath))) {
                writer.write("package stepdefinitions;\n\n");
                writer.write("import io.cucumber.java.en.*;\n\n");
                writer.write("public class " + featureFileName + "Steps {\n");

                for (String line : featureLines) {
                    String methodStub = generateMethodStub(line, existingMethods);
                    if (methodStub != null) {
                        writer.write(methodStub);
                    } else {
                        System.out.println("Ignored line: " + line);
                    }
                }

                writer.write("}\n");
            }
            System.out.println("Step definition file generated: " + stepDefinitionFilePath);
        }
    }

    private static String getFileNameWithoutExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf('.');
        return lastIndexOfDot == -1 ? fileName : fileName.substring(0, lastIndexOfDot);
    }

   /* private static String generateMethodStub(String line, Set<String> existingMethods) {
        line = line.trim();

        if (line.startsWith("Given") || line.startsWith("When") || line.startsWith("Then") || line.startsWith("And")) {
            String[] parts = line.split(" ", 2);
            String annotation = parts[0];
            String stepText = parts.length > 1 ? parts[1].trim() : "";

            Matcher matcher = Pattern.compile("\"([^\"]*)\"").matcher(stepText);
            boolean hasParameter = matcher.find();
            if (hasParameter) {
                stepText = stepText.replace(matcher.group(0), "{string}");
            }

            String methodName = stepText.replaceAll("[^a-zA-Z0-9]", "_").toLowerCase();
            methodName = methodName.replaceAll("_+", "_");

            // If the method already exists, skip generating it
            if (existingMethods.contains(methodName)) {
                System.out.println("Method already exists: " + methodName + ", skipping...");
                return null;
            }

            if (hasParameter) {
                return String.format("\t@%s(\"%s\")\n\tpublic void %s(String inputString) {\n\t\t// TODO: Implement step\n\t\tthrow new io.cucumber.java.PendingException();\n\t}\n\n",
                        annotation, stepText, methodName);
            } else {
                return String.format("\t@%s(\"%s\")\n\tpublic void %s() {\n\t\t// TODO: Implement step\n\t\tthrow new io.cucumber.java.PendingException();\n\t}\n\n",
                        annotation, stepText, methodName);
            }
        }
        return null;
    }*/


    private static String generateMethodStub(String line, Set<String> existingMethods) {
        line = line.trim();

        if (line.startsWith("Given") || line.startsWith("When") || line.startsWith("Then") || line.startsWith("And")) {
            String[] parts = line.split(" ", 2);
            String annotation = parts[0];
            String stepText = parts.length > 1 ? parts[1].trim() : "";

            Matcher matcher = Pattern.compile("\"([^\"]*)\"").matcher(stepText);
            boolean hasParameter = matcher.find();
            if (hasParameter) {
                stepText = stepText.replace(matcher.group(0), "{string}");
            }

            String methodName = stepText.replaceAll("[^a-zA-Z0-9]", "_").toLowerCase();
            methodName = methodName.replaceAll("_+", "_");

            // If the method already exists, skip generating it
            if (existingMethods.contains(methodName)) {
                System.out.println("Method already exists: " + methodName + ", skipping...");
                return null;
            }

            // Generate the logStep statement using the original step text
            String logStepText = stepText.replace("{string}", "\" + inputString + \""); // Log step to include dynamic input

            // Method with parameter
            if (hasParameter) {
                return String.format(
                        "\t@%s(\"%s\")\n\tpublic void %s(String inputString) {\n\t\tlogStep(\"%s\");\n\t\t// TODO: Implement step\n\t\tthrow new io.cucumber.java.PendingException();\n\t}\n\n",
                        annotation, stepText, methodName, logStepText);
            } else { // Method without parameter
                return String.format(
                        "\t@%s(\"%s\")\n\tpublic void %s() {\n\t\tlogStep(\"%s\");\n\t\t// TODO: Implement step\n\t\tthrow new io.cucumber.java.PendingException();\n\t}\n\n",
                        annotation, stepText, methodName, stepText);
            }
        }
        return null;
    }




    private static Set<String> getExistingMethodsInAllStepDefinitions(String stepDefinitionDirectory) throws IOException {
        Set<String> existingMethods = new HashSet<>();
        File directory = new File(stepDefinitionDirectory);
        File[] stepDefinitionFiles = directory.listFiles((dir, name) -> name.endsWith(".java"));

        if (stepDefinitionFiles != null) {
            for (File stepDefinitionFile : stepDefinitionFiles) {
                existingMethods.addAll(getExistingMethods(stepDefinitionFile.getPath()));
            }
        }
        return existingMethods;
    }

    private static Set<String> getExistingMethods(String stepDefinitionFilePath) throws IOException {
        Set<String> existingMethods = new HashSet<>();
        List<String> lines = Files.readAllLines(Paths.get(stepDefinitionFilePath));

        Pattern methodPattern = Pattern.compile("public void (\\w+)\\(");
        for (String line : lines) {
            Matcher matcher = methodPattern.matcher(line);
            if (matcher.find()) {
                existingMethods.add(matcher.group(1));
            }
        }
        return existingMethods;
    }

    private static void appendNewSteps(List<String> featureLines, String stepDefinitionFilePath, Set<String> existingMethods) throws IOException {
        List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(stepDefinitionFilePath)));
        int closingBraceIndex = fileContent.lastIndexOf("}");

        if (closingBraceIndex == -1) {
            System.err.println("Invalid step definition file: Missing closing brace.");
            return;
        }

        List<String> newMethods = new ArrayList<>();
        for (String line : featureLines) {
            String methodStub = generateMethodStub(line, existingMethods);
            if (methodStub != null) {
                newMethods.add(methodStub);
            }
        }

        fileContent.addAll(closingBraceIndex, newMethods);
        Files.write(Paths.get(stepDefinitionFilePath), fileContent);
        System.out.println("Appended new steps to: " + stepDefinitionFilePath);
    }

    // Backup method to create a .zip file with segregated feature and step definition files
    private static void createBackup(String featureDirectoryPath, String stepDefinitionDirectory, String backupDirectoryPath) throws IOException {
        Files.createDirectories(Paths.get(backupDirectoryPath)); // Ensure backup directory exists

        // Get current date and time in the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String timestamp = LocalDateTime.now().format(formatter);
        String backupFileName = backupDirectoryPath + "backup_" + timestamp + ".zip";

        try (FileOutputStream fos = new FileOutputStream(backupFileName);
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {

            // Add feature files to the backup inside "features/" folder
            addFilesToZip(featureDirectoryPath, zipOut, "features/");

            // Add step definition files to the backup inside "stepdefinitions/" folder
            addFilesToZip(stepDefinitionDirectory, zipOut, "stepdefinitions/");

            System.out.println("Backup created: " + backupFileName);
        }
    }

    // Method to add files to the zip with a specified folder inside the zip (segregated by type)
    private static void addFilesToZip(String directoryPath, ZipOutputStream zipOut, String zipFolder) throws IOException {
        File directoryToZip = new File(directoryPath);
        File[] files = directoryToZip.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    addFilesToZip(file.getPath(), zipOut, zipFolder + file.getName() + "/");
                } else {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        ZipEntry zipEntry = new ZipEntry(zipFolder + file.getName());
                        zipOut.putNextEntry(zipEntry);

                        byte[] bytes = new byte[1024];
                        int length;
                        while ((length = fis.read(bytes)) >= 0) {
                            zipOut.write(bytes, 0, length);
                        }
                        zipOut.closeEntry();
                    }
                }
            }
        }
    }

    // Method to clean up old backups (older than 10 days)
    private static void cleanOldBackups(String backupDirectoryPath) throws IOException {
        File backupDirectory = new File(backupDirectoryPath);
        File[] backupFiles = backupDirectory.listFiles((dir, name) -> name.endsWith(".zip"));

        if (backupFiles != null) {
            for (File backupFile : backupFiles) {
                long currentTime = System.currentTimeMillis();
                long fileAge = currentTime - backupFile.lastModified();
                long daysOld = fileAge / (1000 * 60 * 60 * 24);

                if (daysOld > 10) {
                    if (backupFile.delete()) {
                        System.out.println("Deleted old backup: " + backupFile.getName());
                    } else {
                        System.err.println("Failed to delete old backup: " + backupFile.getName());
                    }
                }
            }
        }
    }
}

