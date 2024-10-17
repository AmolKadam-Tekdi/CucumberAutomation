package utils.StepDefinitionUtilities;

import java.io.IOException;
import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class FeatureFileWatcher {

    private final Path featureDirectoryPath;
    private final ExecutorService executorService;
    private final String stepDefinitionDirectory;

    public FeatureFileWatcher(String featureDirectory, String stepDefinitionDirectory) {
        this.featureDirectoryPath = Paths.get(featureDirectory);
        this.executorService = Executors.newSingleThreadExecutor(); // Single-threaded executor for handling file changes
        this.stepDefinitionDirectory = stepDefinitionDirectory;
    }

    public void startWatching() throws IOException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        featureDirectoryPath.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);

        System.out.println("Watching directory: " + featureDirectoryPath.toString());

        // Listen for changes on a separate thread
        executorService.submit(() -> {
            while (true) {
                try {
                    WatchKey key = watchService.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();
                        Path changedFilePath = featureDirectoryPath.resolve((Path) event.context());

                        if (kind == ENTRY_CREATE || kind == ENTRY_MODIFY) {
                            // Trigger StepDefinitionGenerator when a feature file is added or modified
                            System.out.println("Detected change in file: " + changedFilePath);
                            if (changedFilePath.toString().endsWith(".feature")) {
                                triggerStepDefinitionGeneration();
                            }
                        }
                    }
                    key.reset();
                } catch (InterruptedException | IOException e) {
                    System.err.println("Error while watching directory: " + e.getMessage());
                }
            }
        });
    }

    private void triggerStepDefinitionGeneration() throws IOException {
        // Trigger the step definition generation process when a feature file changes
        StepDefinitionGenerator.processFeatureFiles(featureDirectoryPath.toString(), stepDefinitionDirectory);
        System.out.println("Step definitions updated based on feature file changes.");
    }

    public void stopWatching() {
        executorService.shutdown();
    }

    public static void main(String[] args) throws IOException {
        String featureDirectory = "src/main/java/feature/";  // Feature file directory
        String stepDefinitionDirectory = "src/main/java/stepdefinitions/";  // Step definition directory

        FeatureFileWatcher watcher = new FeatureFileWatcher(featureDirectory, stepDefinitionDirectory);
        watcher.startWatching();  // Start watching the directory for changes

        // Keep running until stopped manually
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                watcher.stopWatching();
                System.out.println("File watcher stopped.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }
}
