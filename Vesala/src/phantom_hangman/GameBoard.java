package phantom_hangman;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.charset.Charset;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GameBoard extends JFrame 
{

    private final int WIDTH;

    private final int HEIGHT;

    private final int MAX_INCORRECT;

    private final int MAX_PASSWORD_LENGTH;

    private final String HANGMAN_IMAGE_DIRECTORY;

    private final String HANGMAN_IMAGE_TYPE;

    private final String HANGMAN_IMAGE_BASE_NAME;

    private final String LETTER_IMAGE_DIRECTORY;

    private final String LETTER_IMAGE_TYPE;

    private LetterRack gameRack;

    private Hangman gameHangman;

    private int numIncorrect;

    private JLabel correct;

    private JLabel incorrect;

    private String password;

    private StringBuilder passwordHidden;

    public GameBoard()
    {
        WIDTH = 500;
        HEIGHT = 500;
        MAX_INCORRECT = 6;
        MAX_PASSWORD_LENGTH = 10;
        HANGMAN_IMAGE_DIRECTORY = LETTER_IMAGE_DIRECTORY = "images/";
        HANGMAN_IMAGE_TYPE = LETTER_IMAGE_TYPE = ".png";
        HANGMAN_IMAGE_BASE_NAME = "hangman";
        
        setTitle("Phantom Hangman");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        addCloseWindowListener();
        
        initialize();
    }
    
    /**
     * Inicijalizacija svih elemenata Table koji se moraju osveziti na pocetku svake nove igre
    */
    private void initialize()
    {        
        numIncorrect = 0;
        
        correct = new JLabel("Реч: ");
        incorrect = new JLabel("Број нетачних: " + numIncorrect);
        password = new String();
        passwordHidden = new StringBuilder();
        
        getPassword();
        addTextPanel();
        addLetterRack();
        addHangman();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - getSize().width / 2,
                dim.height / 2 - getSize().height / 2 - 200);
        setVisible(true);
    }
    
    /**
     * Notifikacioni prozor za izlazak iz igre
     */
    private void addCloseWindowListener()
    {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent we)
            {
                int prompt = JOptionPane.showConfirmDialog(null,
                        "Да ли сте сигурни да желите да изађете?",
                        "Изађи?", 
                        JOptionPane.YES_NO_OPTION);
                
                if (prompt == JOptionPane.YES_OPTION)
                    System.exit(0);
            }
        });
    }
    
    /**
     * Oznaka tacnih i netacnih
     */
    private void addTextPanel()
    {
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(1,2));
        textPanel.add(correct);
        textPanel.add(incorrect);
        add(textPanel, BorderLayout.NORTH);
    }
    
    /**
     * Dodaje niz slova na dno table i vezuje slusace sa slikama slova
     */
    private void addLetterRack()
    {
        gameRack = new LetterRack(password, 
                LETTER_IMAGE_DIRECTORY, 
                LETTER_IMAGE_TYPE);
        gameRack.attachListeners(new TileListener());
        add(gameRack, BorderLayout.SOUTH);
    }
    
    /**
     * Dodaje sliku figure na sredinu table
     */
    private void addHangman()
    {
        JPanel hangmanPanel = new JPanel();
        gameHangman = new Hangman(HANGMAN_IMAGE_BASE_NAME,
                HANGMAN_IMAGE_DIRECTORY,
                HANGMAN_IMAGE_TYPE);
        hangmanPanel.add(gameHangman);
        add(hangmanPanel, BorderLayout.CENTER);
    }
    
    /**
     * Dijalog za uzimanje zeljenje reci sa restrikcijama
     */
    private void getPassword()
    {
        String[] options = {"Играј", "Изађи"};
        JPanel passwordPanel = new JPanel();
        JLabel passwordLabel = new JLabel("Унесите жељену реч: ");
        JTextField passwordText = new JTextField(MAX_PASSWORD_LENGTH);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordText);
        int confirm = -1;
        
        while (password.isEmpty())
        {
            confirm = JOptionPane.showOptionDialog(null, 
                    passwordPanel, 
                    "Унесите реч", 
                    JOptionPane.DEFAULT_OPTION, 
                    JOptionPane.QUESTION_MESSAGE, 
                    null, 
                    options, 
                    options[0]);

            if (confirm == 0)
            {
                password = passwordText.getText();
                password = new String(password.getBytes(Charset.forName("utf-8")));
                
                // restrikcije
                if (!password.matches("[\\p{L}]+") ||   //muka
                	
                    password.length() > MAX_PASSWORD_LENGTH)
                {
                	  JOptionPane.showMessageDialog(null, 
                              "Реч мора имати мање од 10 слова " +
                              "и садржи слова од А до Ш.", 
                              "Недозвољена реч", 
                            JOptionPane.ERROR_MESSAGE);
                    password = "";
                }
            }
                    
            else if (confirm == 1)
                System.exit(0);
        }
        
        //menja slovo sa * da bi sakrili rec
        passwordHidden.append(password.replaceAll(".", "*"));
        correct.setText(correct.getText() + passwordHidden.toString());
    }
    
    /**
     * Dijalog za novu igru ili izlazak
     */
    private void newGameDialog()
    {
        int dialogResult = JOptionPane.showConfirmDialog(null, 
                "Реч је : " + password +
                "\nДа ли желите да играте поново?",
                "Играј поново?",
                JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION)
            initialize(); // ponovna inicijalizacija radne table
        else
            System.exit(0);
    }
    
    /**
     * Slusac koji detektuje kada je korisnik pritisnuo slovo i azurira igru na osnovu toga 
     */
    private class TileListener implements MouseListener 
    {
        @Override
        public void mousePressed(MouseEvent e) 
        {
            Object source = e.getSource();
            if(source instanceof LetterTile)
            {
                char c = ' ';
                int index = 0;
                boolean updated = false;
                
                LetterTile tilePressed = (LetterTile) source;
                c = tilePressed.guess();
                
                // otkrivanje slova
                while ((index = password.toLowerCase().indexOf(c, index)) != -1)
                {
                    passwordHidden.setCharAt(index, password.charAt(index));
                    index++;
                    updated = true;
                }
                
                // ako je slovo u trazenoj reci azurira tablu i proverava da li je korisnik pobedio 
                if (updated)
                {
                    correct.setText("Реч: " + passwordHidden.toString());
                    
                    if (passwordHidden.toString().equals(password))
                    {
                        gameRack.removeListeners();
                        gameHangman.winImage();
                        newGameDialog();
                    }
                }
                
                // ako nije, dodaje u netacna slova i proverava da li je korisnik izgubio
                else
                {
                    incorrect.setText("Број грешака: " + ++numIncorrect);
                    
                    if (numIncorrect >= MAX_INCORRECT)
                    {
                        gameHangman.loseImage();
                        gameRack.removeListeners();
                        newGameDialog();
                    }
                    
                    else
                        gameHangman.nextImage(numIncorrect);
                }
            }
        }
        
        // Metode za slusace misa
        
        @Override
        public void mouseClicked(MouseEvent e) {}  

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}
        
        @Override
        public void mouseExited(MouseEvent e) {}
    }
}