package phantom_hangman;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JLabel;

/**
 * Prikaz slova
 */
public class LetterTile extends JLabel
{

    private final char IMAGE_LETTER;

    private final String IMAGE_DIRECTORY;

    private final String IMAGE_TYPE;

    private final int PREFERRED_WIDTH;

    private final int PREFERRED_HEIGHT;

    private String path;

    private BufferedImage image;

    private MouseListener tileListener;  // svako slovo ima 'slusaca'

    public LetterTile() { this('a', "images/", ".png"); }
    

    public LetterTile(char imageLetter, String imageDirectory, String imageType)
    {
        IMAGE_LETTER = imageLetter;
        IMAGE_DIRECTORY = imageDirectory;
        IMAGE_TYPE = imageType;
        
        PREFERRED_WIDTH = PREFERRED_HEIGHT = 50;
        
        setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
        path = IMAGE_DIRECTORY + IMAGE_LETTER + IMAGE_TYPE;
        image = loadImage(path);
    }
    
    /**
     * Ucitavanje slike
     */
    private BufferedImage loadImage(String imagePath)
    {
        BufferedImage img = null;

        try 
        {
            img = ImageIO.read(new File(imagePath));
        } 

        catch (IOException ex) 
        {
            System.err.println("loadImage(): Error: Image at "
                    + imagePath + " could not be found");
            System.exit(1);
        }

        return img;
    }
    
    /**
     * Ucitava sliku kada je pritisnuto slovo i uklanja 'slusaca' da ne bi mogli opet da kliknemo na njega
     */
    public char guess() 
    { 
        loadNewImage("guessed");
        removeTileListener();
        return IMAGE_LETTER;
    }
    
    /**
     * Ucitavanje nove slike/iskoriscenog slova
     */
    private void loadNewImage(String suffix)
    {
        path = IMAGE_DIRECTORY + IMAGE_LETTER + "_" + suffix + IMAGE_TYPE;
        image = loadImage(path);
        repaint();  
    }
    
    /**
     * Dodavanje slusaca misa,swing default
     */
    public void addTileListener(MouseListener l) 
    { 
        tileListener = l;
        addMouseListener(tileListener);
    }
    
    /**
     * Uklanjanje slusaca misa
     */
    public void removeTileListener() { removeMouseListener(tileListener); }

    @Override
    protected void paintComponent(Graphics g) 
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