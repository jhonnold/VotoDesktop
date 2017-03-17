package gui;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class ConsoleOutput extends OutputStream {
	
	private final JTextArea output;
	private final StringBuilder sb = new StringBuilder();
	private String title;
	
	public ConsoleOutput(final JTextArea out, String title) {
		this.output = out;
		this.title = title;
		sb.append(title + "> ");
	}
	
	@Override
	public void flush() {}
	
	@Override
	public void close() {}
	
	public void write(int b) throws IOException {
		/*if (b == '\r') {
			return;
		}*/
		
		if (b == '\n') {
			final String text = sb.toString() + "\n";
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					output.append(text);
				}
			});
			sb.setLength(0);
			sb.append(title + "> ");
			return;
		}
		
		sb.append((char) b);
	}
}
