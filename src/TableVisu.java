import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class TableVisu extends JPanel implements ActionListener {

	private JButton openButton;
	private JLabel theLabel;
	private JTextArea log;
	private final JFileChooser fc;

	public TableVisu() {
		super(new BorderLayout());
		fc = new JFileChooser();
		// Create the open button. We use the image from the JLF
		// Graphics Repository (but we extracted it from the jar).
		openButton = new JButton("Open a File...");
		openButton.addActionListener(this);
		// For layout purposes, put the buttons in a separate panel
		JPanel buttonPanel = new JPanel(); // use FlowLayout
		buttonPanel.add(openButton);
		// Add the buttons and the log to this panel.
		add(buttonPanel, BorderLayout.PAGE_START);

		String initialText = "<html>\n" + "<a href='http://esialrobotik.fr/'>Lien super cool</a>\n" + "</html>\n";
		log = new JTextArea(5, 20);
		log.setMargin(new Insets(5, 5, 5, 5));
		log.setEditable(false);
		JScrollPane logScrollPane = new JScrollPane(log);
		theLabel = new JLabel(initialText) {
			public Dimension getPreferredSize() {
				return new Dimension(200, 200);
			}

			public Dimension getMinimumSize() {
				return new Dimension(200, 200);
			}

			public Dimension getMaximumSize() {
				return new Dimension(200, 200);
			}
		};
		theLabel.setVerticalAlignment(SwingConstants.CENTER);
		theLabel.setHorizontalAlignment(SwingConstants.CENTER);
		theLabel.setText(initialText);
		add(logScrollPane, BorderLayout.CENTER);
	}

	public static void main(String[] args) {
		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				createAndShowGUI();
			}
		});


	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be invoked
	 * from the event dispatch thread.
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("FileChooserDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add content to the window.
		frame.add(new TableVisu());

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//Handle open button action.
        if (e.getSource() == openButton) {
            int returnVal = fc.showOpenDialog(TableVisu.this);
 
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would open the file.
                System.out.println(file.getName());
				StringBuffer sb = new StringBuffer("<html>\n");
				try (InputStream in = Files.newInputStream(file.toPath());
						BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
					String line = null;
					while ((line = reader.readLine()) != null) {
						System.out.println(line);
						sb.append(line);
						sb.append('\n');
						log.append(line);
						log.append("\n");
					}
					sb.append("</html>");
					theLabel.setText(sb.toString());
				} catch (IOException x) {
					System.err.println(x);
				}
            } else {
                System.out.println("Open command cancelled by user.");
				try {
					Desktop.getDesktop().browse(new URI("file:///tmp/test.html"));
				} catch (IOException e1) {

					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }

		}
	}

}
