package com.university;
import java.io.*;
import java.util.*;
import java.util.Scanner;
import java.util.zip.*;


public class Main {

    public static void  CreateZip(String pathFile,String filename,String fileZip){
        //создать архив , запаковать 1 файл
        try(ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(pathFile+"/"+fileZip+".zip"));
            FileInputStream fis= new FileInputStream(pathFile+"/"+filename))
        {
            ZipEntry entry1=new ZipEntry(filename);
            zout.putNextEntry(entry1);
            // считываем содержимое файла в массив byte
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            // добавляем содержимое к архиву
            zout.write(buffer);
            // закрываем текущую запись для новой записи
            zout.closeEntry();
        }
        catch(Exception ex){

            System.out.println(ex.getMessage());
        }
    }
    public static void ShowZipFiles(String pathZip){
        //Смотрим что в зип архиве
        try(ZipInputStream zip = new ZipInputStream(new FileInputStream(pathZip)))
        {
            ZipEntry entry;//откроем архив
            String name;
            long size;
            while((entry=zip.getNextEntry())!=null){

                name = entry.getName(); // получим название файла
                size=entry.getSize();  // получим его размер в байтах
                System.out.printf("Название: %s \t размер: %d \n", name, size);
            }
        }
        catch(Exception ex){

            System.out.println(ex.getMessage());
        }
    }

    public static void unZip(String path, String dir_to) throws IOException {
        try {
            ZipFile zip = new ZipFile(path);
            Enumeration entries = zip.entries();
            File file = new File(dir_to+"/");
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                System.out.println(entry.getName());

                if (entry.isDirectory()) {
                    new File(file.getParent(), entry.getName()).mkdirs();
                } else {
                    write(zip.getInputStream(entry),
                            new BufferedOutputStream(new FileOutputStream(
                                    new File(file.getParent(), entry.getName()))));
                }
            }

            zip.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static void add(String zipFileName, String fileName) throws IOException {
        File zipFile = new File(zipFileName);
        File tmpFile = File.createTempFile("zip", "tmp");
        File newFile = new File(fileName);

        byte[] buffer = new byte[8192];
        int readed;

        ZipOutputStream zipOutputStream = new ZipOutputStream(
                new BufferedOutputStream(
                        new FileOutputStream(tmpFile)));

        try {
            if (!zipFile.exists()) {
                ZipInputStream zipInputStream = new ZipInputStream(
                        new BufferedInputStream(
                                new FileInputStream(zipFile)));

                try {
                    ZipEntry entry;

                    while ((entry = zipInputStream.getNextEntry()) != null){
                        if (entry.getName().equals(newFile.getName())) {
                            continue;
                        }

                        ZipEntry newEntry = new ZipEntry(entry);
                        zipOutputStream.putNextEntry(newEntry);

                        while ((readed = zipInputStream.read(buffer)) > 0) {
                            zipOutputStream.write(buffer, 0, readed);
                        }

                        zipOutputStream.closeEntry();
                    }
                }
                finally {
                    zipInputStream.close();
                }
            }

            InputStream fileInputStream = new BufferedInputStream(
                    new FileInputStream(newFile));

            try {
                System.out.printf("Adding %s\n", fileName);

                ZipEntry newEntry = new ZipEntry(newFile.getName());
                newEntry.setSize(newFile.length());
                newEntry.setTime(newFile.lastModified());

                zipOutputStream.putNextEntry(newEntry);

                while ((readed = fileInputStream.read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, readed);
                }

                zipOutputStream.closeEntry();
            }
            finally {
                fileInputStream.close();
            }
        }
        finally {
            zipOutputStream.close();
        }

        if (zipFile.exists()) {
            zipFile.delete();
        }

        tmpFile.renameTo(zipFile);
    }

    private static void write(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) >= 0)
            out.write(buffer, 0, len);
        out.close();
        in.close();
    }
    public static  void ListingFolder(File folder){
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
    }
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        String file,zipName;
        File folder = new File("/Users/ivan.bolsakov/Documents/JavaLabs/");
        int choose=4;
        while (choose!=0){
            System.out.println("Folder now: "+folder.getAbsolutePath());
            //ListingFolder(folder);
            switch (choose)
            {
                case 1:
                    System.out.println("File name in folder(example : name.txt )");
                    file = in.next();
                    System.out.println("Name of your Zip Archive");
                    zipName = in.next();
                    CreateZip(folder.getPath(),file,zipName);
                    ShowZipFiles(folder.getPath()+"/"+zipName+".zip");
                    break;
                case 2:
                    System.out.println("Name of your Zip Archive");
                    zipName = in.next();
                    System.out.println("Want you extract in this folder? press Y/N");

                    String var =in.next();
                    if (var.equals("Y") || var.equals("y")){
                        unZip(folder.getPath()+"/"+zipName+".zip",folder.getPath()+"/"+zipName+"/");break;
                    }
                    else if (var.equals("N") || var.equals("n")){
                        System.out.println("Path to new Folder");
                        String pathToExctract = in.next();
                        new File(pathToExctract).mkdirs();
                        unZip(folder.getPath()+"/"+zipName+".zip",pathToExctract+"/"+zipName+"/");break;
                    }
                    else
                    {System.out.println("Error: Incorrect press key");}
                    //ShowZipFiles(folder.getPath()+"/"+zipName+".zip");
                    break;
                case 3:
                    System.out.println("File name in folder(example : name.txt )");
                    file = in.next();
                    System.out.println("Name of your Zip Archive");
                    zipName = in.next();
                    add(folder.getPath()+"/"+zipName+".zip",folder.getPath()+"/"+file);
                    ShowZipFiles(folder.getPath()+zipName+".zip");
                    break;
                case 4: ListingFolder(folder);
                    break;
                case 5:
                    System.out.println("please,Write path to folder");
                    String pathToFolder = in.nextLine();
                    folder = new File(pathToFolder);
                    break;
                default: choose=0;
                    break;
            }
            System.out.println("Press key to choose \n" +
                    "[1]:Create ZipArchive with 1 file \n" +
                    "[2]:UnZip ZipArchive \n" +
                    "[3]:Add files to ZipArchive \n" +
                    "[4]:Listing files in dir \n" +
                    "[5]:Choose Folder\n" +
                    "[0]:Exit\n");
            choose=in.nextInt();
        }
    }
}