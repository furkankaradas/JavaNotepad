import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class NotepadGui extends JFrame {

	private final int frameWidthSize = 600;
	private final int frameHeightSize = 400;
	private final int locationX = 450;
	private final int locationY = 200;
	private boolean findValue = false;
	private String keyWord;

	private Container mainContainer;
	private Highlighter highlighter;
	Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);


	private JMenuBar menuBar = new JMenuBar();
	private JMenu fileMenu = new JMenu("File");
	private JMenu findMenu = new JMenu("Edit");
	private JMenu helpMenu = new JMenu("Help");
	private JMenuItem menuItemNew  = new JMenuItem("New",'N');
	private JMenuItem menuItemOpen = new JMenuItem("Open",'O');
	private JMenuItem menuItemSave = new JMenuItem("Save",'S');
	private JMenuItem menuItemExit = new JMenuItem("Exit",'E');
	private JMenuItem findMenuItem = new JMenuItem("Find");
	private JMenuItem findClearMenuItem = new JMenuItem("Clear Highlights");
	private JMenuItem helpMenuAbout = new JMenuItem("About",'H');

	private JTextArea textArea = new JTextArea();
	private JScrollPane scrollPane = new JScrollPane(textArea);

	private JFileChooser openFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
	private JFileChooser saveFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

	{
		menuItemNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		menuItemOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		menuItemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		menuItemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));


		fileMenu.add(menuItemNew);
		fileMenu.add(menuItemOpen);
		fileMenu.add(menuItemSave);
		fileMenu.addSeparator();
		fileMenu.add(menuItemExit);

		helpMenuAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
		helpMenu.add(helpMenuAbout);

		findMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
		findClearMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
		findMenu.add(findMenuItem);
		findMenu.add(findClearMenuItem);


		menuBar.add(fileMenu);
		menuBar.add(findMenu);
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
		findMenuItem.addActionListener(eventHandler);
		findClearMenuItem.addActionListener(eventHandler);
	}

	private void saveFile() throws IOException {
		// update ????
		int retValue = saveFileChooser.showSaveDialog(null);
		if(retValue == JFileChooser.CANCEL_OPTION)
		{
			return;
		}
		else {

			BufferedWriter writer = new BufferedWriter(new FileWriter(saveFileChooser.getSelectedFile().getPath(), false));
			writer.write(textArea.getText());
			writer.close();
			JOptionPane.showInternalMessageDialog(null, "Succesfully Saved!",
					"Message", JOptionPane.INFORMATION_MESSAGE);

		}
	}

	private void openFile() throws IOException {

		int retValue = openFileChooser.showOpenDialog(null);
		if(retValue == JFileChooser.CANCEL_OPTION) {
			return;
		}
		else {
			File selectedFile = openFileChooser.getSelectedFile();
			textArea.setText(new String(Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath()))));
		}
	}

	private class EventHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
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

			else if(event.getSource() == findMenuItem) {
				int startIndex, endIndex;

				String textAreaWords;

				keyWord = JOptionPane.showInputDialog(null, "Find Key:");
				if(keyWord != null) {
					textAreaWords = textArea.getText();
					startIndex = textAreaWords.indexOf(keyWord);
					highlighter = textArea.getHighlighter();
					while(startIndex != -1) {
						endIndex = startIndex + keyWord.length();
						try {
							highlighter.addHighlight(startIndex, endIndex, painter);
						} catch (BadLocationException e) {
							JOptionPane.showInternalMessageDialog(null, "Highlight Error!",
									"Error!", JOptionPane.WARNING_MESSAGE);
						}
						startIndex = textAreaWords.indexOf(keyWord, endIndex);
						findValue = true;
					}
					if (findValue == false) {
						JOptionPane.showInternalMessageDialog(null, "Key not found!",
								"Attention", JOptionPane.WARNING_MESSAGE);

					}
				}

			}

			else if (event.getSource() == findClearMenuItem) {
				if(findValue == true) {
					highlighter.removeAllHighlights();
					JOptionPane.showInternalMessageDialog(null, "Highlight Cleared.",
							"Attention", JOptionPane.INFORMATION_MESSAGE);
				}
			}

		}
	}

}
