import java.io.*;
import java.util.Scanner;

/**
 * TP2 — Árvore Binária de Busca de Músicas
 *
 * Lê o arquivo musicas.csv, constrói a BST pelo ID,
 * e oferece menu interativo para inserção, busca e remoção.
 */
public class MainTP2 {

    private static MusicBST bst = new MusicBST();
    private static int proximoId = 1; // controlado após leitura do CSV
    private static Scanner scanner = new Scanner(System.in);

    private static final String ARQUIVO_CSV = "musicas.csv";

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║   🌳  ÁRVORE BINÁRIA DE BUSCA — MÚSICAS  🌳  ║");
        System.out.println("║          Estruturas de Dados — TP2           ║");
        System.out.println("╚══════════════════════════════════════════════╝");

        carregarCSV(ARQUIVO_CSV);

        boolean running = true;
        while (running) {
            exibirMenu();
            int opcao = lerInt("Escolha uma opção: ");
            switch (opcao) {
                case 1 -> cadastrarMusica();
                case 2 -> buscarPorId();
                case 3 -> removerPorId();
                case 4 -> exibirEmOrdem();
                case 5 -> exibirPercursos();
                case 6 -> exibirArvore();
                case 7 -> exibirEstatisticas();
                case 0 -> {
                    System.out.println("\n👋 Até logo!");
                    running = false;
                }
                default -> System.out.println("⚠  Opção inválida.");
            }
        }
        scanner.close();
    }

    // ─────────────────────────────────────────────
    // LEITURA DO CSV
    // ─────────────────────────────────────────────

    private static void carregarCSV(String nomeArquivo) {
        System.out.println("\n📂 Carregando músicas de \"" + nomeArquivo + "\"...");
        int inseridas = 0;
        int maxId = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha = br.readLine(); // cabeçalho: id,titulo,artista,duracao

            if (linha == null) {
                System.out.println("⚠  Arquivo vazio.");
                return;
            }

            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) continue;

                Music music = parseLinha(linha);
                if (music != null) {
                    bst.inserir(music);
                    inseridas++;
                    if (music.getId() > maxId) maxId = music.getId();
                }
            }

            proximoId = maxId + 1;
            System.out.printf("✔ %d música(s) carregada(s) na árvore BST.%n%n", inseridas);

        } catch (FileNotFoundException e) {
            System.out.println("⚠  Arquivo \"" + nomeArquivo + "\" não encontrado.");
            System.out.println("   Iniciando com árvore vazia. Você pode cadastrar músicas manualmente.\n");
        } catch (IOException e) {
            System.out.println("⚠  Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    /**
     * Parseia uma linha do CSV no formato: id,titulo,artista,duracao
     * Suporta vírgulas dentro do título/artista usando split com limite.
     */
    private static Music parseLinha(String linha) {
        // Divide apenas nas 3 primeiras vírgulas para preservar títulos com vírgula
        String[] partes = linha.split(",", 4);
        if (partes.length < 4) {
            System.out.println("  ⚠  Linha inválida ignorada: " + linha);
            return null;
        }
        try {
            int id = Integer.parseInt(partes[0].trim());
            String titulo = partes[1].trim();
            String artista = partes[2].trim();
            double duracao = Double.parseDouble(partes[3].trim());
            return new Music(id, titulo, artista, duracao);
        } catch (NumberFormatException e) {
            System.out.println("  ⚠  Erro de formato na linha: " + linha);
            return null;
        }
    }

    // ─────────────────────────────────────────────
    // MENU
    // ─────────────────────────────────────────────

    private static void exibirMenu() {
        System.out.println("══════════════════════════════════════════════");
        System.out.printf("  Nós na árvore: %-4d  Altura: %d%n",
            bst.totalNos(), bst.altura());
        System.out.println("══════════════════════════════════════════════");
        System.out.println("  [1]  Cadastrar nova música (inserir)");
        System.out.println("  [2]  Buscar música por ID");
        System.out.println("  [3]  Remover música por ID");
        System.out.println("  [4]  Exibir músicas em ordem (in-order)");
        System.out.println("  [5]  Exibir percursos (pre/in/pos-order)");
        System.out.println("  [6]  Visualizar estrutura da árvore");
        System.out.println("  [7]  Estatísticas da árvore");
        System.out.println("  [0]  Sair");
        System.out.println("══════════════════════════════════════════════");
    }

    // ─────────────────────────────────────────────
    // 1. CADASTRAR / INSERIR
    // ─────────────────────────────────────────────

    private static void cadastrarMusica() {
        System.out.println("\n── CADASTRAR NOVA MÚSICA ──");
        System.out.printf("  Próximo ID disponível: %d%n", proximoId);
        System.out.print("  Usar ID sugerido? [1=Sim / 0=Informar outro]: ");
        int usarSugerido = lerInt("");

        int id;
        if (usarSugerido == 1) {
            id = proximoId;
        } else {
            id = lerInt("  ID: ");
            if (bst.buscar(id) != null) {
                System.out.println("  ⚠  ID " + id + " já está na árvore.");
                System.out.print("  Sobrescrever? [1=Sim / 0=Cancelar]: ");
                if (lerInt("") != 1) { System.out.println("  Operação cancelada."); return; }
            }
        }

        System.out.print("  Título: ");
        String titulo = scanner.nextLine().trim();
        System.out.print("  Artista: ");
        String artista = scanner.nextLine().trim();
        double duracao = lerDouble("  Duração (minutos, ex: 3.45): ");

        Music nova = new Music(id, titulo, artista, duracao);
        bst.inserir(nova);

        if (id >= proximoId) proximoId = id + 1;

        System.out.println("  ✔ Inserida na árvore: " + nova);
        System.out.println();

        // Mostra onde caiu na árvore
        System.out.println("  Árvore atualizada:");
        bst.exibirArvore();
    }

    // ─────────────────────────────────────────────
    // 2. BUSCAR POR ID
    // ─────────────────────────────────────────────

    private static void buscarPorId() {
        System.out.println("\n── BUSCAR MÚSICA POR ID ──");
        int id = lerInt("  ID: ");

        System.out.println("  Caminho percorrido na árvore:");
        Music encontrada = buscarComCaminho(id);

        if (encontrada != null) {
            System.out.println("\n  ✔ Música encontrada:");
            System.out.println("     " + encontrada);
        } else {
            System.out.println("\n  ✘ Música com ID " + id + " não encontrada na árvore.");
        }
    }

    /** Busca exibindo o caminho percorrido nos nós (didático) */
    private static Music buscarComCaminho(int id) {
        TreeNode current = getRootForDisplay();
        if (current == null) {
            System.out.println("  (árvore vazia)");
            return null;
        }

        int passo = 1;
        while (current != null) {
            System.out.printf("  Passo %d → [ID %d] %s%n",
                passo++, current.music.getId(), current.music.getTitle());

            if (id == current.music.getId()) {
                System.out.println("             ↑ ENCONTRADO!");
                return current.music;
            } else if (id < current.music.getId()) {
                System.out.println("             → id " + id + " < " + current.music.getId() + " : vai para esquerda");
                current = current.left;
            } else {
                System.out.println("             → id " + id + " > " + current.music.getId() + " : vai para direita");
                current = current.right;
            }
        }
        return null;
    }

    // ─────────────────────────────────────────────
    // 3. REMOVER POR ID
    // ─────────────────────────────────────────────

    private static void removerPorId() {
        System.out.println("\n── REMOVER MÚSICA POR ID ──");
        int id = lerInt("  ID: ");

        Music music = bst.buscar(id);
        if (music == null) {
            System.out.println("  ✘ Música com ID " + id + " não encontrada.");
            return;
        }

        System.out.println("  Música encontrada: " + music);
        System.out.print("  Confirmar remoção? [1=Sim / 0=Cancelar]: ");
        if (lerInt("") != 1) { System.out.println("  Operação cancelada."); return; }

        bst.remover(id);
        System.out.println("  ✔ Música removida com sucesso.");
        System.out.println();
        System.out.println("  Árvore atualizada:");
        bst.exibirArvore();
    }

    // ─────────────────────────────────────────────
    // 4. EXIBIR EM ORDEM
    // ─────────────────────────────────────────────

    private static void exibirEmOrdem() {
        System.out.println("\n── MÚSICAS EM ORDEM CRESCENTE DE ID ──");
        bst.exibirEmOrdem();
    }

    // ─────────────────────────────────────────────
    // 5. PERCURSOS
    // ─────────────────────────────────────────────

    private static void exibirPercursos() {
        System.out.println("\n── PERCURSOS DA ÁRVORE ──");
        System.out.println();
        bst.exibirPreOrdem();
        System.out.println();
        bst.exibirEmOrdem();
        System.out.println();
        bst.exibirPosOrdem();
    }

    // ─────────────────────────────────────────────
    // 6. VISUALIZAR ÁRVORE
    // ─────────────────────────────────────────────

    private static void exibirArvore() {
        System.out.println("\n── ESTRUTURA DA ÁRVORE ──");
        bst.exibirArvore();
    }

    // ─────────────────────────────────────────────
    // 7. ESTATÍSTICAS
    // ─────────────────────────────────────────────

    private static void exibirEstatisticas() {
        System.out.println("\n── ESTATÍSTICAS DA ÁRVORE ──");
        System.out.println("  Total de músicas (nós) : " + bst.totalNos());
        System.out.println("  Altura da árvore       : " + bst.altura());
        System.out.println("  Próximo ID disponível  : " + proximoId);
        System.out.println("  Árvore vazia?          : " + (bst.isEmpty() ? "Sim" : "Não"));
    }

    // ─────────────────────────────────────────────
    // ACESSO À RAIZ (para exibir caminho de busca)
    // ─────────────────────────────────────────────

    /**
     * Retorna a raiz para percurso externo (buscarComCaminho).
     * Usa a própria BST como proxy via busca de um ID impossível
     * para capturar o nó raiz — aqui usamos uma abordagem mais simples:
     * expondo um getter na BST.
     */
    private static TreeNode getRootForDisplay() {
        return bst.getRoot();
    }

    // ─────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────

    private static int lerInt(String prompt) {
        while (true) {
            if (!prompt.isEmpty()) System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("  ⚠  Digite um número inteiro válido: ");
            }
        }
    }

    private static double lerDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.print("  ⚠  Digite um número válido: ");
            }
        }
    }
}
