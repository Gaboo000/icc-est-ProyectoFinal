import javax.swing.*;
import java.awt.*;

public class LaberintoView extends JFrame {
    private JButton[][] grid;
    private JPanel panelGrid;
    private JPanel panelControls;
    private JButton btnBFS, btnDFS, btnRecursivo, btnDinamico, btnStart;
    private JButton btnLimpiarCamino, btnLimpiarTodo;
    private JTextField txtSize;
    private JButton btnSetSize;

    public LaberintoView(int size) {
        setTitle("Laberinto - Algoritmos de Búsqueda");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panelGrid = new JPanel(new GridLayout(size, size));
        grid = new JButton[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = new JButton();
                grid[i][j].setBackground(Color.WHITE);
                panelGrid.add(grid[i][j]);
            }
        }

        panelControls = new JPanel();
        btnBFS = new JButton("BFS");
        btnDFS = new JButton("DFS");
        btnRecursivo = new JButton("Recursivo");
        btnDinamico = new JButton("Prog. Dinámica");
        btnStart = new JButton("Buscar Ruta");
        btnLimpiarCamino = new JButton("Limpiar Camino");
        btnLimpiarTodo = new JButton("Limpiar Todo");
        txtSize = new JTextField(5);
        btnSetSize = new JButton("Actualizar Tamaño");

        panelControls.add(new JLabel("Tamaño:"));
        panelControls.add(txtSize);
        panelControls.add(btnSetSize);
        panelControls.add(btnBFS);
        panelControls.add(btnDFS);
        panelControls.add(btnRecursivo);
        panelControls.add(btnDinamico);
        panelControls.add(btnStart);
        panelControls.add(btnLimpiarCamino);
        panelControls.add(btnLimpiarTodo);

        add(panelGrid, BorderLayout.CENTER);
        add(panelControls, BorderLayout.SOUTH);

        setVisible(true);
    }

    public JButton[][] getGrid() {
        return grid;
    }

    public JButton getBtnBFS() {
        return btnBFS;
    }

    public JButton getBtnDFS() {
        return btnDFS;
    }

    public JButton getBtnRecursivo() {
        return btnRecursivo;
    }

    public JButton getBtnDinamico() {
        return btnDinamico;
    }

    public JButton getBtnSetSize() {
        return btnSetSize;
    }

    public JButton getBtnLimpiarCamino() {
        return btnLimpiarCamino;
    }

    public JButton getBtnLimpiarTodo() {
        return btnLimpiarTodo;
    }

    public JTextField getTxtSize() {
        return txtSize;
    }
}
