import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class TableVisu extends JPanel implements ActionListener {

	private JButton openButton;
	private JTextArea log;
	private final JFileChooser fc;
	private char newline = '\n';
	private int xscale = 2;
	private int yscale = 2;
	File svg = new File("table.html");

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

		log = new JTextArea(5, 20);
		log.setMargin(new Insets(5, 5, 5, 5));
		log.setEditable(false);
		JScrollPane logScrollPane = new JScrollPane(log);
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

	public void drawPixel(String line, int i, int y, StringBuffer sb, int xoffset) {
		switch (line.charAt(((xoffset > 0) ? line.length() - i - 1 : line.length() - i - 1))) {
		case 'x':
			sb.append("<rect x=\"" + ((xoffset + i) * xscale) + "\" y=\"" + y + "\"width=\"" + xscale + "\" height=\""
					+ yscale
					+ "\"\r\n" + "  style=\"fill:red\" />");
			break;
		case 'o':
			sb.append("<rect x=\"" + ((xoffset + i) * xscale) + "\" y=\"" + y + "\"width=\"" + xscale + "\" height=\""
					+ yscale
					+ "\"\r\n" + "  style=\"fill:white;\" />");
			break;
		default:
			break;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//Handle open button action.
        if (e.getSource() == openButton) {
            int returnVal = fc.showOpenDialog(TableVisu.this);
 
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would open the file.
                log.append(file.getName() + newline);
				if (svg.exists()) {
					svg.delete();
					svg = new File("table.html");
				}
				StringBuffer sb = new StringBuffer("<html>\n");
				try (InputStream in = Files.newInputStream(file.toPath());
						BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
					String line = null;
					line = reader.readLine();
					String[] size = { "", "" };
					size = line.split(" ");
					int width = Integer.parseInt(size[0]) / 10;
					int height = Integer.parseInt(size[1]) / 10;
					int x = 0; // X width
					int y = 0; // Y height
					sb.append("<svg width=\"" + width * 2 * xscale + "\" height=\"" + height * yscale
							+ "\">");
					while ((line = reader.readLine()) != null) {
						for (int i = 0; i < line.length(); i++) {
							this.drawPixel(line, i, y, sb, 0);
						}
						/**
						 * for (int i = 0; i < line.length(); i++) { this.drawPixel(line, i, y, sb,
						 * line.length()); }
						 **/
						sb.append('\n');
						y += yscale;
					}
					sb.append("</svg>");
					sb.append("</html>");
					BufferedWriter bw = Files.newBufferedWriter(svg.toPath());
					bw.write(sb.toString(), 0, sb.length());
					bw.flush();
					try {
						Desktop.getDesktop().browse(svg.toURI());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (IOException x) {
					System.err.println(x);
				}
            } else {
                log.append("Open command cancelled by user.");
            }

		}
	}

}
