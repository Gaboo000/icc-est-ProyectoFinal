import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.Queue;

public class LaberintoController {
    private LaberintoModel model;
    private LaberintoView view;
    private int startX = 0, startY = 0, endX, endY;

    public LaberintoController(int size) {
        model = new LaberintoModel(size);
        view = new LaberintoView(size);
        endX = size - 1;
        endY = size - 1;
        initializeActions();
    }

    private void initializeActions() {
        JButton[][] grid = view.getGrid();

        for (int i = 0; i < model.getSize(); i++) {
            for (int j = 0; j < model.getSize(); j++) {
                final int x = i, y = j;
                grid[x][y].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            if (startX == x && startY == y) {
                                startX = -1;
                                startY = -1;
                                grid[x][y].setBackground(Color.WHITE);
                            } else if (endX == x && endY == y) {
                                endX = -1;
                                endY = -1;
                                grid[x][y].setBackground(Color.WHITE);
                            } else if (startX == -1 && startY == -1) {
                                startX = x;
                                startY = y;
                                grid[x][y].setBackground(Color.BLUE);
                            } else if (endX == -1 && endY == -1) {
                                endX = x;
                                endY = y;
                                grid[x][y].setBackground(Color.RED);
                            }
                        } else {
                            if ((x == startX && y == startY) || (x == endX && y == endY))
                                return;
                            model.toggleBlock(x, y);
                            grid[x][y].setBackground(model.isBlocked(x, y) ? Color.BLACK : Color.WHITE);
                        }
                    }
                });
            }
        }

        view.getBtnBFS().addActionListener(e -> buscarBFS());
        view.getBtnDFS().addActionListener(e -> buscarDFS());
        view.getBtnRecursivo()
                .addActionListener(e -> buscarRecursivo(startX, startY, new boolean[model.getSize()][model.getSize()]));
        view.getBtnDinamico().addActionListener(e -> buscarDinamico());
        view.getBtnSetSize().addActionListener(e -> actualizarTamano());
        view.getBtnLimpiarCamino().addActionListener(e -> limpiarSoloCamino());
        view.getBtnLimpiarTodo().addActionListener(e -> limpiarColores());
    }

    private void actualizarTamano() {
        int newSize = Integer.parseInt(view.getTxtSize().getText());
        model.setSize(newSize);
        view.dispose();
        new LaberintoController(newSize);
    }

    private void buscarBFS() {
        limpiarSoloCamino();
        boolean[][] visited = new boolean[model.getSize()][model.getSize()];
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[] { startX, startY });

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0], y = current[1];

            if (x == endX && y == endY) {
                view.getGrid()[x][y].setBackground(Color.GREEN);
                return;
            }

            if (x < 0 || x >= model.getSize() || y < 0 || y >= model.getSize() || visited[x][y]
                    || model.isBlocked(x, y))
                continue;

            visited[x][y] = true;
            marcarCasillaConDelay(x, y, Color.GREEN);

            queue.add(new int[] { x + 1, y });
            queue.add(new int[] { x - 1, y });
            queue.add(new int[] { x, y + 1 });
            queue.add(new int[] { x, y - 1 });
        }
    }

    private void buscarDFS() {
        limpiarSoloCamino();
        boolean[][] visited = new boolean[model.getSize()][model.getSize()];
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[] { startX, startY });

        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            int x = current[0], y = current[1];

            if (x == endX && y == endY)
                return;
            if (x < 0 || x >= model.getSize() || y < 0 || y >= model.getSize() || visited[x][y]
                    || model.isBlocked(x, y))
                continue;

            visited[x][y] = true;
            marcarCasillaConDelay(x, y, Color.GREEN);

            stack.push(new int[] { x + 1, y });
            stack.push(new int[] { x - 1, y });
            stack.push(new int[] { x, y + 1 });
            stack.push(new int[] { x, y - 1 });
        }
    }

    private boolean buscarRecursivo(int x, int y, boolean[][] visited) {
        if (x < 0 || x >= model.getSize() || y < 0 || y >= model.getSize() || visited[x][y] || model.isBlocked(x, y))
            return false;
        if (x == endX && y == endY) {
            marcarCasillaConDelay(x, y, Color.GREEN);
            return true;
        }

        visited[x][y] = true;
        marcarCasillaConDelay(x, y, Color.GREEN);

        return buscarRecursivo(x + 1, y, visited) || buscarRecursivo(x - 1, y, visited) ||
                buscarRecursivo(x, y + 1, visited) || buscarRecursivo(x, y - 1, visited);
    }

    private void buscarDinamico() {
        System.out.println("Botón de programación dinámica presionado");
        limpiarSoloCamino();
        int[][] dp = new int[model.getSize()][model.getSize()];
        for (int[] row : dp)
            Arrays.fill(row, -1);

        if (resolverDinamico(startX, startY, dp)) {
            animarCaminoDinamico(dp);
        }
    }

    private boolean resolverDinamico(int x, int y, int[][] dp) {
        if (x < 0 || x >= model.getSize() || y < 0 || y >= model.getSize() || model.isBlocked(x, y)) {
            System.out.println("Fuera de límites o bloqueado en (" + x + ", " + y + ")");
            return false;
        }

        if (x == endX && y == endY) {
            dp[x][y] = 1;
            System.out.println("Encontré la meta en (" + x + ", " + y + ")");
            return true;
        }

        if (dp[x][y] != -1) {
            System.out.println("Celda ya evaluada (" + x + ", " + y + ") con valor " + dp[x][y]);
            return dp[x][y] == 1;
        }

        System.out.println("Explorando (" + x + ", " + y + ")");

        dp[x][y] = (resolverDinamico(x + 1, y, dp) || resolverDinamico(x - 1, y, dp) ||
                resolverDinamico(x, y + 1, dp) || resolverDinamico(x, y - 1, dp)) ? 1 : 0;

        return dp[x][y] == 1;
    }

    private void animarCaminoDinamico(int[][] dp) {
        List<int[]> camino = new ArrayList<>();
        int x = startX, y = startY;

        while (x != endX || y != endY) {
            camino.add(new int[] { x, y });
            System.out.println("Añadiendo al camino: (" + x + ", " + y + ")");

            if (x + 1 < model.getSize() && dp[x + 1][y] == 1)
                x++;
            else if (x - 1 >= 0 && dp[x - 1][y] == 1)
                x--;
            else if (y + 1 < model.getSize() && dp[x][y + 1] == 1)
                y++;
            else if (y - 1 >= 0 && dp[x][y - 1] == 1)
                y--;
            else {
                System.out.println("⚠️ Error: No hay más camino desde (" + x + ", " + y + ")");
                break;
            }
        }

        camino.add(new int[] { endX, endY });
        mostrarCaminoConAnimacion(camino);
    }

    private void mostrarCaminoConAnimacion(List<int[]> camino) {
        new Thread(() -> {
            for (int i = 0; i < camino.size(); i++) {
                int[] pos = camino.get(i);
                int x = pos[0], y = pos[1];

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                SwingUtilities.invokeLater(() -> view.getGrid()[x][y].setBackground(Color.GREEN));
            }
        }).start();
    }

    private void marcarCamino(int[][] dp) {
        for (int i = 0; i < model.getSize(); i++) {
            for (int j = 0; j < model.getSize(); j++) {
                if (dp[i][j] == 1) {
                    marcarCasillaConDelay(i, j, Color.GREEN);
                }
            }
        }
    }

    private void limpiarSoloCamino() {
        for (int i = 0; i < model.getSize(); i++) {
            for (int j = 0; j < model.getSize(); j++) {
                if (view.getGrid()[i][j].getBackground() == Color.GREEN) {
                    view.getGrid()[i][j].setBackground(Color.WHITE);
                }
            }
        }
    }

    private void limpiarColores() {
        for (int i = 0; i < model.getSize(); i++) {
            for (int j = 0; j < model.getSize(); j++) {
                view.getGrid()[i][j].setBackground(Color.WHITE);
            }
        }
    }

    private void marcarCasillaConDelay(int x, int y, Color color) {
        new Thread(() -> {
            try {
                Thread.sleep(200); // Controla el tiempo entre cambios
                SwingUtilities.invokeLater(() -> view.getGrid()[x][y].setBackground(color));
            } catch (InterruptedException ignored) {
            }
        }).start();
    }

}
