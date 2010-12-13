package AZBIrenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.ImageIcon;

/**
 * A GUI class for interfacing with the renderer
 * @author Barak Itkin
 */
public class RenderWindow extends javax.swing.JFrame {

    public static final String axises ="\n"
            + "cylinder:\n"
            + "start     = -100 0 0\n"
            + "direction =   1  0 0\n"
            + "length    = 200\n"
            + "radius    = 0.03\n"
            + "mtl_diffuse = 1 0 0\n"
            + "\n"
            + "sphere:\n"
            + "center = 2 0 0\n"
            + "radius = 0.1\n"
            + "mtl_diffuse = 1 0 0\n"
            + "\n"
            + "\n"
            + "cylinder:\n"
            + "start     = 0 -100 0\n"
            + "direction = 0   1  0\n"
            + "length    = 200\n"
            + "radius    = 0.03\n"
            + "mtl_diffuse = 0 1 0\n"
            + "\n"
            + "sphere:\n"
            + "center = 0 2 0\n"
            + "radius = 0.1\n"
            + "mtl_diffuse = 0 1 0\n"
            + "\n"
            + "cylinder:\n"
            + "start     = 0 0 -100\n"
            + "direction = 0 0   1 \n"
            + "length    = 200\n"
            + "radius    = 0.03\n"
            + "mtl_diffuse = 0 0 1\n"
            + "\n"
            + "sphere:\n"
            + "center = 0 0 2\n"
            + "radius = 0.1\n"
            + "mtl_diffuse = 0 0 1\n";
    BufferedImage renderResult;

    /** Creates new form RenderWindow */
    public RenderWindow() {
        initComponents();
        this.setSize(400, 300);
        TextEditorWindow.setSize(200, 200);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        TextEditorWindow = new javax.swing.JDialog();
        TextToolbar = new javax.swing.JToolBar();
        TextOpenButton = new javax.swing.JButton();
        TextScroll = new javax.swing.JScrollPane();
        TextPane = new javax.swing.JTextPane();
        FileChooser = new javax.swing.JFileChooser();
        ImageToolbar = new javax.swing.JToolBar();
        RenderButton = new javax.swing.JButton();
        SaveButton = new javax.swing.JButton();
        EditButton = new javax.swing.JButton();
        ImageScroll = new javax.swing.JScrollPane();
        ImageView = new javax.swing.JLabel();
        AxisCheckbox = new javax.swing.JCheckBox();
        ThreadCountLabel = new javax.swing.JLabel();
        ThreadCountSpin = new javax.swing.JSpinner();

        TextEditorWindow.setTitle("Scene Text Editor");

        TextToolbar.setFloatable(false);
        TextToolbar.setRollover(true);

        TextOpenButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AZBIrenderer/images/tango-open.png"))); // NOI18N
        TextOpenButton.setText("Open...");
        TextOpenButton.setFocusable(false);
        TextOpenButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        TextOpenButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        TextOpenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextOpenButtonActionPerformed(evt);
            }
        });
        TextToolbar.add(TextOpenButton);

        TextEditorWindow.getContentPane().add(TextToolbar, java.awt.BorderLayout.PAGE_START);

        TextPane.setText("scene:\nbackground-col= 0.36 0.46 0.63\n#ambient-light=1 1 1\nsuper-samp-width=1\n\ncamera:\neye = 3 3 3\nlook-at = 0 0 0\nscreen-dist = 1\nup-direction = 0 0 1\n\n\nlight-directed:\ndirection= -1 -1 -1\ncolor = 1 1 1\n\n\nbox:\np0 = -1 -1 -1\np1 = +1 -1 -1\np2 = -1 +1 -1\np3 = -1 -1 +1\n#mesh:\n#pos = 0 0 0\n#scale = 0.31\n#filename = triangle.off\n#mtl-diffuse = 0.55 0.53 0.44");
        TextScroll.setViewportView(TextPane);

        TextEditorWindow.getContentPane().add(TextScroll, java.awt.BorderLayout.CENTER);

        FileChooser.setCurrentDirectory(null);
        FileChooser.setDialogTitle("Choose a file");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        ImageToolbar.setFloatable(false);
        ImageToolbar.setRollover(true);

        RenderButton.setIcon(new javax.swing.ImageIcon("C:\\Users\\user\\Documents\\University\\Tel-Aviv-Computer-Science\\Courses\\Computer Graphics\\RayTracer\\Jazbi\\src\\AZBIrenderer\\images\\tango-execute.png")); // NOI18N
        RenderButton.setText("Start Render!");
        RenderButton.setFocusable(false);
        RenderButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        RenderButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        RenderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RenderButtonActionPerformed(evt);
            }
        });
        ImageToolbar.add(RenderButton);

        SaveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AZBIrenderer/images/tango-save.png"))); // NOI18N
        SaveButton.setText("Save as PNG");
        SaveButton.setFocusable(false);
        SaveButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        SaveButton.setPreferredSize(new java.awt.Dimension(31, 41));
        SaveButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        SaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveButtonActionPerformed(evt);
            }
        });
        ImageToolbar.add(SaveButton);

        EditButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AZBIrenderer/images/tango-edit.png"))); // NOI18N
        EditButton.setText("Edit Scene...");
        EditButton.setFocusable(false);
        EditButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        EditButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        EditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditButtonActionPerformed(evt);
            }
        });
        ImageToolbar.add(EditButton);

        ImageScroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        ImageScroll.setMinimumSize(new java.awt.Dimension(50, 50));

        ImageView.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ImageView.setText("Render will be here!");
        ImageView.setIconTextGap(0);
        ImageScroll.setViewportView(ImageView);

        AxisCheckbox.setSelected(true);
        AxisCheckbox.setText("Visualize Axises");

        ThreadCountLabel.setText("Thread Count:");

        ThreadCountSpin.setModel(new javax.swing.SpinnerNumberModel(1, 1, 16, 1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(AxisCheckbox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 183, Short.MAX_VALUE)
                .addComponent(ThreadCountLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ThreadCountSpin, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(ImageScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
            .addComponent(ImageToolbar, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(ImageToolbar, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ImageScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AxisCheckbox, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ThreadCountSpin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ThreadCountLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void RenderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RenderButtonActionPerformed
        Render r = new Render((AxisCheckbox.isSelected() ? axises : "") + TextPane.getText());
        long time = System.currentTimeMillis();
        r.render(ImageScroll.getWidth(), ImageScroll.getHeight(), 4, 4, (Integer)ThreadCountSpin.getValue());
        ImageView.setIcon(new ImageIcon(r.render));
        ImageView.setText("");
        time -= System.currentTimeMillis();
        System.out.println("Rendering took " + -time + "ms");
        renderResult = r.render;

    }//GEN-LAST:event_RenderButtonActionPerformed

    private void EditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditButtonActionPerformed
        TextEditorWindow.setVisible(true);
        TextEditorWindow.requestFocus();
    }//GEN-LAST:event_EditButtonActionPerformed

    private void TextOpenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextOpenButtonActionPerformed
        try {
            FileChooser.showOpenDialog(this);
            File f = FileChooser.getSelectedFile();
            if (f == null || !f.exists()) {
                return;
            }
            java.util.Scanner s = new Scanner(FileChooser.getSelectedFile());
            StringBuilder text = new StringBuilder();
            while (s.hasNextLine()) {
                text.append(s.nextLine()).append("\n");
            }
            s.close();
            TextPane.setText(text.toString());
        } catch (FileNotFoundException ex) {
            System.err.println("An error occured while opening the file...");
            System.err.println(ex.getMessage());
        }
    }//GEN-LAST:event_TextOpenButtonActionPerformed

    private void SaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveButtonActionPerformed
        try {
            FileChooser.showSaveDialog(this);
            File f = FileChooser.getSelectedFile();
            if (f == null) {
                return;
            }
            javax.imageio.ImageIO.write(renderResult, "PNG", f);
        } catch (IOException ex) {
            System.err.println("An IO error occured while writing the file...");
            System.err.println(ex.getMessage());
        }
    }//GEN-LAST:event_SaveButtonActionPerformed

    /**
     * The main method of the program
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new RenderWindow().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox AxisCheckbox;
    private javax.swing.JButton EditButton;
    private javax.swing.JFileChooser FileChooser;
    private javax.swing.JScrollPane ImageScroll;
    private javax.swing.JToolBar ImageToolbar;
    private javax.swing.JLabel ImageView;
    private javax.swing.JButton RenderButton;
    private javax.swing.JButton SaveButton;
    private javax.swing.JDialog TextEditorWindow;
    private javax.swing.JButton TextOpenButton;
    private javax.swing.JTextPane TextPane;
    private javax.swing.JScrollPane TextScroll;
    private javax.swing.JToolBar TextToolbar;
    private javax.swing.JLabel ThreadCountLabel;
    private javax.swing.JSpinner ThreadCountSpin;
    // End of variables declaration//GEN-END:variables
}
