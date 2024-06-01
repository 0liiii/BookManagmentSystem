public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            BookManager bookManager = new BookManager();
            bookManager.setVisible(true);
        });
    }
}
