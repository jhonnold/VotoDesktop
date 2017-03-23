package gui;

import java.io.IOException;
import java.io.OutputStream;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class ConsoleOutput extends OutputStream {
	
	private final TextArea output;
	private final StringBuilder sb = new StringBuilder();
	private String title;
	
	public ConsoleOutput(final TextArea out, String title) {
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
			Platform.runLater(new Runnable() {
				public void run() {
					output.appendText(text);
				}
			});
			sb.setLength(0);
			sb.append(title + "> ");
			return;
		}
		
		sb.append((char) b);
	}
}
