import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class NotepadGui extends JFrame {

	private final int frameWidthSize = 600;
	private final int frameHeightSize = 400;
	private final int locationX = 450;
	private final int locationY = 200;

	private Container mainContainer;

	private JMenuBar menuBar = new JMenuBar();
	private JMenu fileMenu = new JMenu("File");
	private JMenu helpMenu = new JMenu("Help");
	private JMenuItem menuItemNew  = new JMenuItem("New");
	private JMenuItem menuItemOpen = new JMenuItem("Open");
	private JMenuItem menuItemSave = new JMenuItem("Save");
	private JMenuItem menuItemExit = new JMenuItem("Exit");
	private JMenuItem helpMenuAbout = new JMenuItem("About");

	private JTextArea textArea = new JTextArea();
	private JScrollPane scrollPane = new JScrollPane(textArea);

	private JFileChooser openFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
	private JFileChooser saveFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

	{
		fileMenu.add(menuItemNew);
		fileMenu.add(menuItemOpen);
		fileMenu.add(menuItemSave);
		fileMenu.addSeparator();
		fileMenu.add(menuItemExit);

		helpMenu.add(helpMenuAbout);
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);

		textArea.setBorder(new LineBorder(Color.lightGray, 3));
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		saveFileChooser.setDialogTitle("Choose a directory to save your file: ");
		saveFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	}

	public NotepadGui() {

		super("Notepad");
		this.setVisible(true);
		this.setSize(frameWidthSize,frameHeightSize);
		this.setLocation(locationX, locationY);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setJMenuBar(menuBar);

		mainContainer = this.getContentPane();
		mainContainer.add(scrollPane);
		prepareActionListener();

	}

	private void prepareActionListener() {

		EventHandler eventHandler = new EventHandler();
		menuItemNew.addActionListener(eventHandler);
		menuItemOpen.addActionListener(eventHandler);
		menuItemSave.addActionListener(eventHandler);
		menuItemExit.addActionListener(eventHandler);
		helpMenuAbout.addActionListener(eventHandler);
	}

	private void saveFile() throws IOException {
		// update ????
		saveFileChooser.showSaveDialog(null);
		BufferedWriter writer = new BufferedWriter(new FileWriter(saveFileChooser.getSelectedFile().getPath()));
		writer.write(textArea.getText());
		writer.close();
		JOptionPane.showInternalMessageDialog(null, "Succesfully Saved!",
						"Message", JOptionPane.INFORMATION_MESSAGE);

	}

	private void openFile() throws IOException {

		openFileChooser.showOpenDialog(null);
		File selectedFile = openFileChooser.getSelectedFile();
		textArea.setText(new String(Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath()))));
	}

	private class EventHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			// Catch ????
			if(event.getSource() == menuItemNew) {
				textArea.setText("");
			}

			else if(event.getSource() == menuItemOpen) {
				try {
					openFile();
				}
				catch (Exception exception) {
					JOptionPane.showInternalMessageDialog(null, "File not opened!",
							"Error", JOptionPane.WARNING_MESSAGE);
				}
			}

			else if(event.getSource() == menuItemSave) {
				try {
					saveFile();
				}
				catch (Exception exception) {
					JOptionPane.showInternalMessageDialog(null, "File not saved!",
							"Error", JOptionPane.WARNING_MESSAGE);
				}
			}

			else if(event.getSource() == helpMenuAbout) {
				JOptionPane.showInternalMessageDialog(null, "Open Source Notepad\nDesigned by Furkan Karadaş",
						"About", JOptionPane.INFORMATION_MESSAGE);
			}

			else if(event.getSource() == menuItemExit) {
				System.exit(0);
			}

		}
	}

}
