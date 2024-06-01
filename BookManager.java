import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BookManager extends JFrame {
    private List<Book> books;
    private DefaultListModel<Book> bookListModel;
    private JTextField searchField;
    private JComboBox<String> sortComboBox;
    private JList<Book> bookList;

    public BookManager() {
        books = new ArrayList<>();
        populateBooks();

        setTitle("Book Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        searchField = new JTextField();
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                filterBooks();
            }
        });
        topPanel.add(searchField, BorderLayout.CENTER);

        String[] sortOptions = {"Title", "Author", "Year", "Genre"};
        sortComboBox = new JComboBox<>(sortOptions);
        sortComboBox.addActionListener(e -> sortBooks());
        topPanel.add(sortComboBox, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        bookListModel = new DefaultListModel<>();
        bookList = new JList<>(bookListModel);
        add(new JScrollPane(bookList), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton addButton = new JButton("Add Book");
        addButton.addActionListener(e -> addBook());
        bottomPanel.add(addButton);

        JButton removeButton = new JButton("Remove Book");
        removeButton.addActionListener(e -> removeBook());
        bottomPanel.add(removeButton);

        add(bottomPanel, BorderLayout.SOUTH);

        refreshBookList();
    }

    private void populateBooks() {
        books.add(new Book("Title1", "Author1", 2001, "Genre1"));
        // Dodaj więcej książek zgodnie z wymaganiami
        // ...
    }

    private void filterBooks() {
        String query = searchField.getText();
        List<Book> filteredBooks = books.stream()
            .filter(book -> Pattern.compile(Pattern.quote(query), Pattern.CASE_INSENSITIVE).matcher(book.toString()).find())
            .collect(Collectors.toList());
        refreshBookList(filteredBooks);
    }

    private void sortBooks() {
        String criterion = (String) sortComboBox.getSelectedItem();
        books.sort((b1, b2) -> {
            switch (criterion) {
                case "Title":
                    return b1.getTitle().compareToIgnoreCase(b2.getTitle());
                case "Author":
                    return b1.getAuthor().compareToIgnoreCase(b2.getAuthor());
                case "Year":
                    return Integer.compare(b1.getYear(), b2.getYear());
                case "Genre":
                    return b1.getGenre().compareToIgnoreCase(b2.getGenre());
                default:
                    return 0;
            }
        });
        refreshBookList();
    }

    private void addBook() {
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField genreField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Author:"));
        panel.add(authorField);
        panel.add(new JLabel("Year:"));
        panel.add(yearField);
        panel.add(new JLabel("Genre:"));
        panel.add(genreField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            String author = authorField.getText();
            int year = Integer.parseInt(yearField.getText());
            String genre = genreField.getText();

            books.add(new Book(title, author, year, genre));
            refreshBookList();
        }
    }

    private void removeBook() {
        Book selectedBook = bookList.getSelectedValue();
        if (selectedBook != null) {
            books.remove(selectedBook);
            refreshBookList();
        }
    }

    private void refreshBookList() {
        refreshBookList(books);
    }

    private void refreshBookList(List<Book> bookList) {
        bookListModel.clear();
        for (Book book : bookList) {
            bookListModel.addElement(book);
        }
    }
}
