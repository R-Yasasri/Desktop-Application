/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src;

import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;
import static src.Home.FileType;

/**
 *
 * @author rajitha
 */
public class Model {

    public static void main(String[] args) {
        Folder documents = new Folder("Documents", null, "");

        File serverPdf = new File("Server.pdf", null, "pdf", "");
        Folder pictures = new Folder("Pictures", null, "");
        File profileJpg = new File("profile.jpg", null, "jpg", "");

        Folder applications = new Folder("Applications", null, "");
        Folder graphics = new Folder("Graphics", null, "");

        File unityExe = new File("Unity.exe", null, "exe", "");
        File blenderExe = new File("Blender.exe", null, "exe", "");

        File illustratorExe = new File("Illustrator.exe", null, "exe", "");

        documents.addFileComponent(serverPdf);
        documents.addFileComponent(pictures);
        pictures.addFileComponent(profileJpg);

        documents.addFileComponent(applications);
        applications.addFileComponent(graphics);
        applications.addFileComponent(unityExe);
        applications.addFileComponent(blenderExe);

        graphics.addFileComponent(illustratorExe);

        System.out.println(documents.getName());
//        System.out.println(documents.getSize());
//        System.out.println(documents.getFileCount());

        System.out.println(serverPdf.getName());
//        System.out.println(serverPdf.getSize());

        System.out.println(pictures.getName());
//        System.out.println(pictures.getSize());
//        System.out.println(pictures.getFileCount());

        System.out.println(applications.getName());
//        System.out.println(applications.getSize());
//        System.out.println(applications.getFileCount());

        System.out.println(graphics.getName());
//        System.out.println(graphics.getSize());
//        System.out.println(graphics.getFileCount());
//        
//        DecimalFormat df=new DecimalFormat("####.##");
//        double d=documents.getSize()/1024;
//        double e=applications.getSize()/1024;
//        System.out.println("df: "+df.format(d));// 1042.7 + 4352 = 5394.7 = documents  -> 5.27
//        System.out.println("dd: "+df.format(e));// 5390.7 -> 5.26
    }
}

interface Prototype {

    FileComponent clone();
}

abstract class FileComponent extends DefaultMutableTreeNode implements Prototype {

    protected String name;
    protected ImageIcon image;
    protected String filePath;
    private final FileComponent_Caretaker caretaker = new FileComponent_Caretaker();

    public FileComponent(String name, ImageIcon image, String filePath) {
        this.name = name;
        this.image = image;
        this.filePath = filePath;
    }

    public String getName() {
        return this.name;
    }

    public ImageIcon getImage() {
        return image;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract boolean isFile();

    @Override
    public String toString() {
        return getName();
    }

    public void save() {
        caretaker.addMemento(new FileComponent_Memento(this.name, this.image, this.filePath));
    }

    public FileComponent undo() {
        FileComponent_Memento temp = caretaker.getMemento();

        if (temp == null) {
            this.name = "";
        } else {

            this.name = temp.getName();
            this.image = temp.getImage();
        }

        return this;
    }

    public int getRemainingStateCount() {
        return caretaker.getStateCount();
    }

    @Override
    public abstract FileComponent clone();

}

class Folder extends FileComponent {

    private ArrayList<FileComponent> fileList = new ArrayList<>();

    public Folder(String name, ImageIcon image, String path) {
        super(name, image, path);
    }

    public void addFileComponent(FileComponent file) {
        fileList.add(file);
    }

//    public int getFileCount() {
//
//        int i = 0;
//        for (FileComponent fileComponent : fileList) {
//
//            if (fileComponent.isFile()) {
//                i++;
//            } else {
//                i += ((Folder) fileComponent).getFileCount();
//            }
//        }
//
//        return i;
//    }

    public ArrayList<FileComponent> getFileList() {
        return fileList;
    }
 
    public int getFileListSize(){
        return this.getFileList().size();
    }
    
    public FileComponent getFileFromList(int index){
        return this.getFileList().get(index);
    }

    @Override
    public boolean isFile() {
        return false;
    }

    @Override
    public FileComponent clone() {

        Folder clonedFolder = new Folder(this.name, this.image, this.filePath);
        clonedFolder.fileList = this.fileList;
        return clonedFolder;
    }

}

class File extends FileComponent {

    private String extension = "";

    public File(String name, ImageIcon image, String extension, String path) {
        super(name, image, path);
        this.extension = extension;
    }

    @Override
    public boolean isFile() {
        return true;
    }

    @Override
    public FileComponent clone() {
        return new File(this.name, this.image, this.extension, this.filePath);
    }

}

class FileComponent_Memento {

    private String name;
    private ImageIcon image;
    private String filePath;

    public FileComponent_Memento(String name, ImageIcon image, String filePath) {
        this.name = name;
        this.image = image;
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public ImageIcon getImage() {
        return image;
    }

    public String getFilePath() {
        return filePath;
    }
}

class FileComponent_Caretaker {

    private final Stack<FileComponent_Memento> mementoStack = new Stack();

    public void addMemento(FileComponent_Memento memento) {
        mementoStack.push(memento);
    }

    public FileComponent_Memento getMemento() {

        if (getStateCount() > 0) {
            return mementoStack.pop();
        } else {
            return null;
        }
    }

    public int getStateCount() {
        return mementoStack.size();
    }
}

class FlyweightedImageIcon {

    private ImageIcon imageIcon;

    public FlyweightedImageIcon(String path) {
        Image image = Home.toolkit.getDefaultToolkit().getImage(getClass().getResource(path));
        this.imageIcon = new ImageIcon(image);
    }

    public Image getImageIcon() {
        return imageIcon.getImage();
    }
}

class FlyweightedImageIconFactory {

    private static final HashMap<String, FlyweightedImageIcon> IMAGE_POOL = new HashMap<>();

    public static FlyweightedImageIcon getFlyweightedImageIcon(String path) {

        FlyweightedImageIcon get = IMAGE_POOL.get(path);

        if (get == null) {

            get = new FlyweightedImageIcon(path);
            IMAGE_POOL.put(path, get);

        }

        return get;
    }
}

abstract class FileComponentFactory {

    public abstract FileComponent getFileComponent(FileType fileType);
}

class FileFactory extends FileComponentFactory {

    private static final String BITMAP_IMAGE_FILE_PATH = "../img/icons8-image-50.png";
    private static final String MICROSOFT_EXCEL_IMAGE_FILE_PATH = "../img/icons8-microsoft-excel-50.png";
    private static final String TEXT_FILE_IMAGE_FILE_PATH = "../img/icons8-text-file-50.png";
    private static final String WINRAR_FILE_IMAGE_FILE_PATH = "../img/icons8-winrar-50.png";
    private static final String WORD_FILE_IMAGE_FILE_PATH = "../img/icons8-ms-word-is-a-word-processor-developed-by-microsoft-50.png";

    @Override
    public File getFileComponent(FileType fileType) {

        String extension;
        String path;
        if (fileType == FileType.BITMAP) {
            path = BITMAP_IMAGE_FILE_PATH;
            extension = ".png";
        } else if (fileType == FileType.EXCEL) {
            path = MICROSOFT_EXCEL_IMAGE_FILE_PATH;
            extension = ".xlsx";
        } else if (fileType == FileType.TEXT) {
            path = TEXT_FILE_IMAGE_FILE_PATH;
            extension = ".txt";
        } else if (fileType == FileType.WINRAR) {
            path = WINRAR_FILE_IMAGE_FILE_PATH;
            extension = ".rar";
        } else {
            path = WORD_FILE_IMAGE_FILE_PATH;
            extension = ".docx";
        }

        Image image = FlyweightedImageIconFactory.getFlyweightedImageIcon(path).getImageIcon();
        return new File("", new ImageIcon(image), extension, "");
    }

}

class FolderFactory extends FileComponentFactory {

    @Override
    public Folder getFileComponent(FileType folder) {

        if (folder == FileType.FOLDER) {
            Image image = Home.toolkit.getDefaultToolkit().getImage(getClass().getResource("../img/icons8-folder-50.png"));
            return new Folder("", new ImageIcon(image), "");
        } else {
            return null;
        }
    }

}
