package phantom_hangman;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JLabel;

/**
 * Prikaz slika na tabli
 */
public class Hangman extends JLabel
{

    private final int PREFERRED_WIDTH;

    private final int PREFERRED_HEIGHT;
    
    private final String IMAGE_BASE_NAME;

    private final String IMAGE_DIRECTORY;

    private final String IMAGE_TYPE;

    private String path;

    private BufferedImage image; //trenutna slika koja se prikazuje

    public Hangman()
    {
        this("hangman", "\\src\\phanthom_hangman\\images\\", ".png");
    }
    
    /**
     * Kreira prikaz vesala prema imenu slike, direktorijumu i tipu slike.
     */
    public Hangman(String imageBaseName, String imageDirectory, 
            String imageType)
    {
        PREFERRED_WIDTH = 440;
        PREFERRED_HEIGHT = 255;
        
        IMAGE_BASE_NAME = imageBaseName;
        IMAGE_DIRECTORY = imageDirectory;
        IMAGE_TYPE = imageType;
        
        setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
        path = IMAGE_DIRECTORY + IMAGE_BASE_NAME + "_0" + IMAGE_TYPE;
        image = loadImage(path);
    }
    
    /**
     * Ucitavanje slike iz fajla
     */
    private BufferedImage loadImage(String imagePath)
    {
        BufferedImage img = null;

        try 
        {
            img = ImageIO.read(new File(imagePath));
        } 

        catch (IOException ex)                            // izbaci gresku ako nema slike slova
        {
            System.err.println("loadImage(): Error: Слика "
                    + imagePath + " није пронађена");
            System.exit(1);
        }
        
        return img;
    }
    
    /**
     * Ucitavanje sledece slike kada korisnik pogresi slovo
     */
    public void nextImage(int imageNumber) 
    { 
        loadNewImage(String.valueOf(imageNumber));
    }
    
    /**
     * Ako je korisnik izgubio
     */
    public void loseImage() { loadNewImage("lose"); }
    
    /**
     * Ako je pobedio
     */
    public void winImage() { loadNewImage("win"); }
    
 
    private void loadNewImage(String suffix)
    {
        path = IMAGE_DIRECTORY + IMAGE_BASE_NAME + "_" + suffix + IMAGE_TYPE;
        image = loadImage(path);
        repaint();  
    }
    
    @Override
    protected void paintComponent(Graphics g) //iscrtaj sliku image po odredjenoj visini i sirini
    {
        super.paintComponent(g);
        g.drawImage(image, 
                0, 
                0, 
                PREFERRED_WIDTH, 
                PREFERRED_HEIGHT, 
                null);
    }
}