package com.asu.data;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * BackupManager — Singleton that copies all data files to a timestamped
 * backup directory and supports listing or restoring previous backups.
 *
 * Backup directory layout:
 *   data/backup/
 *     backup_20260504_143022/
 *       users.txt
 *       appointments.txt
 *       payments.txt
 *       feedback.txt
 *       services.txt
 *       manifest.txt          ← summary of what was backed up
 *
 * Demonstrates: Singleton pattern, File I/O, Single Responsibility Principle.
 */
public class BackupManager {

    private static final String BACKUP_ROOT = "data/backup/";
    private static final String[] DATA_FILES = {
        "data/users.txt",
        "data/appointments.txt",
        "data/payments.txt",
        "data/feedback.txt",
        "data/services.txt"
    };
    private static final DateTimeFormatter TS_FMT =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private static BackupManager instance;

    private BackupManager() {}

    public static BackupManager getInstance() {
        if (instance == null) {
            instance = new BackupManager();
        }
        return instance;
    }

    // =========================================================
    // CREATE BACKUP
    // =========================================================

    /**
     * Copies all data files into a new timestamped backup folder and
     * writes a manifest file describing the result.
     *
     * @return the path of the backup directory created, or null on failure.
     */
    public String createBackup() {
        String timestamp  = LocalDateTime.now().format(TS_FMT);
        String backupPath = BACKUP_ROOT + "backup_" + timestamp + "/";

        File backupDir = new File(backupPath);
        if (!backupDir.mkdirs()) return null;

        List<String> succeeded = new ArrayList<>();
        List<String> failed    = new ArrayList<>();

        for (String filePath : DATA_FILES) {
            File source = new File(filePath);
            if (!source.exists()) { failed.add(filePath); continue; }

            File dest = new File(backupPath + source.getName());
            try {
                copyFile(source, dest);
                succeeded.add(source.getName());
            } catch (IOException e) {
                failed.add(filePath);
            }
        }

        writeManifest(backupPath + "manifest.txt", timestamp, succeeded, failed);
        return backupPath;
    }

    // =========================================================
    // LIST BACKUPS
    // =========================================================

    /**
     * Returns a list of existing backup folder names, newest first.
     * Returns an empty list if no backups exist.
     */
    public List<String> listBackups() {
        List<String> names = new ArrayList<>();
        File root = new File(BACKUP_ROOT);
        if (!root.exists()) return names;

        File[] dirs = root.listFiles(File::isDirectory);
        if (dirs == null) return names;

        Arrays.sort(dirs, Comparator.comparing(File::getName).reversed());
        for (File d : dirs) names.add(d.getName());
        return names;
    }

    // =========================================================
    // RESTORE BACKUP
    // =========================================================

    /**
     * Restores data files from the named backup folder back into data/.
     *
     * @param backupName the folder name (e.g. "backup_20260504_143022")
     * @return true if at least one file was restored.
     */
    public boolean restoreBackup(String backupName) {
        String backupPath = BACKUP_ROOT + backupName + "/";
        File   backupDir  = new File(backupPath);
        if (!backupDir.exists()) return false;

        boolean anyRestored = false;
        for (String filePath : DATA_FILES) {
            File original = new File(filePath);
            File source   = new File(backupPath + original.getName());
            if (!source.exists()) continue;
            try {
                original.getParentFile().mkdirs();
                copyFile(source, original);
                anyRestored = true;
            } catch (IOException ignored) {}
        }
        return anyRestored;
    }

    // =========================================================
    // BACKUP MANIFEST
    // =========================================================

    /**
     * Returns the manifest text for a given backup, or null if not found.
     */
    public String readManifest(String backupName) {
        File manifest = new File(BACKUP_ROOT + backupName + "/manifest.txt");
        if (!manifest.exists()) return null;

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(manifest))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException ignored) {}
        return sb.toString();
    }

    // =========================================================
    // PRIVATE HELPERS
    // =========================================================

    private void copyFile(File source, File dest) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(source));
             PrintWriter    writer = new PrintWriter(new FileWriter(dest))) {
            String line;
            while ((line = reader.readLine()) != null) writer.println(line);
        }
    }

    private void writeManifest(String path, String timestamp,
                                List<String> ok, List<String> failed) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            pw.println("APU-ASC BACKUP MANIFEST");
            pw.println("Timestamp : " + timestamp);
            pw.println("Files backed up successfully:");
            for (String f : ok)     pw.println("  [OK]   " + f);
            for (String f : failed) pw.println("  [FAIL] " + f);
            pw.println("Total: " + ok.size() + " succeeded, " + failed.size() + " failed.");
        } catch (IOException ignored) {}
    }
}
