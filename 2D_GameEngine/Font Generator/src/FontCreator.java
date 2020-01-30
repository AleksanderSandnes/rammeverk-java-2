import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FontCreator extends JFrame
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    FontCreator frame = new FontCreator();
                    frame.setVisible(true);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public FontCreator()
    {
        final FontCreator component = this;

        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ex) {};

        setResizable(false);
        setTitle("Font Creator for j2game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 296, 136);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblChooseFont = new JLabel("Choose Font:");
        lblChooseFont.setBounds(10, 11, 200, 14);
        contentPane.add(lblChooseFont);

        final JComboBox<String> fontComboBox = new JComboBox<>();
        fontComboBox.setEditable(true);
        fontComboBox.setBounds(10, 28, 200, 20);
        contentPane.add(fontComboBox);

        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = e.getAllFonts();

        for (Font font : fonts)
        {
            fontComboBox.addItem(font.getName());
        }

        fontComboBox.setSelectedItem("Liberation Serif");

        JLabel lblFontSize = new JLabel("Font size:");
        lblFontSize.setBounds(220, 11, 60, 14);
        contentPane.add(lblFontSize);

        final JComboBox<Integer> sizeComboBox = new JComboBox<>();
        sizeComboBox.setEditable(true);
        sizeComboBox.setBounds(220, 28, 60, 20);
        contentPane.add(sizeComboBox);

        int[] sizes = { 8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72 };

        for (int size : sizes)
        {
            sizeComboBox.addItem(size);
        }

        sizeComboBox.setSelectedItem(11);

        final JButton btnGenerate = new JButton("Create sprite!");
        btnGenerate.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                // Check if font and size are vaild
                String fontName = (String) fontComboBox.getSelectedItem();
                if (fontName == null || !existsFont(fontName))
                {
                    return;
                }

                int fontSize = (int) sizeComboBox.getSelectedItem();
                if (fontSize < 8)
                {
                    return;
                }

                // Create Font instance
                Font font = new Font(fontName, Font.PLAIN, fontSize);

                // Create an instance of Graphics2D
                BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
                Graphics2D gr = img.createGraphics();

                // Calculate width of sprite
                int width = 0;

                FontMetrics metrics = new JPanel().getFontMetrics(font);

                for (int c = 0; c < 256; c++)
                {
                    width += metrics.charWidth((char) c) + 2;
                }

                // Calculate height of sprite
                int height = 0;

                FontRenderContext frc = gr.getFontRenderContext();

                StringBuffer buffer = new StringBuffer(256);
                for (int c = 0; c < 256; c++)
                {
                    buffer.append((char) c);
                }

                GlyphVector vec = font.createGlyphVector(frc, buffer.toString());
                Rectangle bounds = vec.getPixelBounds(null, 0, 0);
                height = bounds.height + 1;

                // Calculate offset of font due to wrong height of chars like '(', ')' or 'Q'
                int[] sizes = new int[256];

                for (int c = 0; c < 256; c++)
                {
                    GlyphVector gv = font.createGlyphVector(frc, String.valueOf((char) c));
                    sizes[c] = gv.getPixelBounds(null, 0, 0).height;
                }

                int total = 0;
                for (int size : sizes)
                {
                    total += size;
                }

                int ratio = (total / sizes.length);
                fontSize = (fontSize + ratio) / 2;

                // Create sprite image and get the Graphics to draw
                BufferedImage sprite = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D graphics = sprite.createGraphics();
                graphics.setColor(new Color(0, 0, 0, 0));
                graphics.fillRect(0, 0, width, height);

                // "Drawer" position
                int x = 0, y = 0;
                graphics.setFont(font);

                for (int c = 0; c < 256; c++)
                {
                    y = 0;
                    graphics.setColor(Color.BLUE);
                    graphics.drawLine(x, y, x, y);
                    x++;

                    y = 1;
                    graphics.setColor(Color.WHITE);
                    graphics.drawString(String.valueOf((char) c), x, y + fontSize);
                    x += metrics.charWidth((char) c);

                    y = 0;
                    graphics.setColor(Color.YELLOW);
                    graphics.drawLine(x, y, x, y);
                    x++;
                }

                // Save file dialog
                JFileChooser fileChooser = new JFileChooser()
                {
                    /**
                     *
                     */
                    private static final long serialVersionUID = 1L;

                    @Override
                    public File getSelectedFile()
                    {
                        File file = super.getSelectedFile();
                        if (file == null)
                        {
                            return null;
                        }

                        String fileName = file.getName();
                        if (!fileName.endsWith(".png"))
                        {
                            fileName += ".png";
                        }

                        return new File(file.getParentFile(), fileName);
                    }

                    @Override
                    public void approveSelection()
                    {
                        File file = getSelectedFile();
                        if (file.exists() && getDialogType() == SAVE_DIALOG)
                        {
                            int result = JOptionPane.showConfirmDialog(this, "The file " + file.getName() + " already exists. Do you want to replace the existing file?", "Ovewrite file", JOptionPane.YES_NO_CANCEL_OPTION);
                            switch (result)
                            {
                                case JOptionPane.YES_OPTION:
                                    super.approveSelection();
                                    return;
                                case JOptionPane.NO_OPTION:
                                    return;
                                case JOptionPane.CLOSED_OPTION:
                                    return;
                                case JOptionPane.CANCEL_OPTION:
                                    cancelSelection();
                                    return;
                            }
                        }

                        super.approveSelection();
                    }
                };
                fileChooser.setDialogTitle("Save file");
                fileChooser.setFileFilter(new FileNameExtensionFilter("Portable Network Graphics (.png)", "png"));

                int result = fileChooser.showSaveDialog(component);
                if (result == JFileChooser.APPROVE_OPTION)
                {
                    File file = fileChooser.getSelectedFile();

                    try
                    {
                        ImageIO.write(sprite, "png", file);
                    }
                    catch (Exception ex)
                    {
                        JOptionPane.showMessageDialog(component, ex.getMessage(), ex.getClass().getCanonicalName(), JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                        return;
                    }

                    JOptionPane.showMessageDialog(component, "Font successfully saved at " + file.getAbsolutePath(), "File saved", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        btnGenerate.setFont(new Font(btnGenerate.getFont().getName(), btnGenerate.getFont().getStyle(), 12));
        btnGenerate.setBounds(10, 59, 270, 35);
        contentPane.add(btnGenerate);

        setVisible(true);
    }

    public int getPopularElement(int[] a)
    {
        int count = 1, tempCount;
        int popular = a[0];
        int temp = 0;

        for (int i = 0; i < (a.length - 1); i++)
        {
            temp = a[i];
            tempCount = 0;
            for (int j = 1; j < a.length; j++)
            {
                if (temp == a[j])
                {
                    tempCount++;
                }
            }

            if (tempCount > count)
            {
                popular = temp;
                count = tempCount;
            }
        }

        return popular;
    }

    public static boolean existsFont(String fontName)
    {
        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = e.getAllFonts();

        for (Font font : fonts)
        {
            if (font.getName().equals(fontName))
            {
                return true;
            }
        }

        return false;
    }
}
