/**
 * Árvore Binária de Busca (BST) de Músicas.
 *
 * Critério: ID da música.
 *   - ID menor  → subárvore esquerda
 *   - ID maior  → subárvore direita
 *
 * Operações implementadas:
 *   inserir(Music)       — insere recursivamente
 *   buscar(int id)       — retorna a Music ou null
 *   remover(int id)      — remoção com sucessor in-order
 *   exibirEmOrdem()      — percurso in-order (IDs crescentes)
 *   exibirPreOrdem()     — percurso pre-order
 *   exibirPosOrdem()     — percurso pos-order
 *   exibirArvore()       — visualização estrutural (árvore rotacionada)
 */
public class MusicBST {

    private TreeNode root;

    public MusicBST() {
        root = null;
    }

    // ─────────────────────────────────────────────
    // INSERÇÃO
    // ─────────────────────────────────────────────

    public void inserir(Music music) {
        root = inserirRec(root, music);
    }

    private TreeNode inserirRec(TreeNode node, Music music) {
        // Caso base: posição vazia encontrada
        if (node == null) {
            return new TreeNode(music);
        }

        if (music.getId() < node.music.getId()) {
            node.left = inserirRec(node.left, music);
        } else if (music.getId() > node.music.getId()) {
            node.right = inserirRec(node.right, music);
        } else {
            // ID duplicado: atualiza os dados
            System.out.println("  ⚠  ID " + music.getId() + " já existe na árvore. Dados atualizados.");
            node.music = music;
        }
        return node;
    }

    // ─────────────────────────────────────────────
    // BUSCA
    // ─────────────────────────────────────────────

    public Music buscar(int id) {
        TreeNode result = buscarRec(root, id);
        return result != null ? result.music : null;
    }

    private TreeNode buscarRec(TreeNode node, int id) {
        // Caso base: não encontrado ou encontrado
        if (node == null || node.music.getId() == id) {
            return node;
        }

        if (id < node.music.getId()) {
            return buscarRec(node.left, id);
        } else {
            return buscarRec(node.right, id);
        }
    }

    // ─────────────────────────────────────────────
    // REMOÇÃO
    // ─────────────────────────────────────────────

    public boolean remover(int id) {
        if (buscar(id) == null) return false;
        root = removerRec(root, id);
        return true;
    }

    private TreeNode removerRec(TreeNode node, int id) {
        if (node == null) return null;

        if (id < node.music.getId()) {
            node.left = removerRec(node.left, id);

        } else if (id > node.music.getId()) {
            node.right = removerRec(node.right, id);

        } else {
            // Nó encontrado — três casos:

            // Caso 1: folha (sem filhos)
            if (node.left == null && node.right == null) {
                return null;
            }

            // Caso 2: apenas um filho
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            // Caso 3: dois filhos → substitui pelo sucessor in-order (menor da subárvore direita)
            TreeNode sucessor = menorNo(node.right);
            node.music = sucessor.music;
            node.right = removerRec(node.right, sucessor.music.getId());
        }
        return node;
    }

    private TreeNode menorNo(TreeNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // ─────────────────────────────────────────────
    // PERCURSOS
    // ─────────────────────────────────────────────

    /** In-order: exibe músicas em ordem crescente de ID */
    public void exibirEmOrdem() {
        if (root == null) { System.out.println("  (árvore vazia)"); return; }
        System.out.println("  In-order (IDs crescentes):");
        inOrder(root, 1);
    }

    private int inOrder(TreeNode node, int pos) {
        if (node == null) return pos;
        pos = inOrder(node.left, pos);
        System.out.printf("  %2d. %s%n", pos++, node.music);
        pos = inOrder(node.right, pos);
        return pos;
    }

    /** Pre-order: raiz → esquerda → direita */
    public void exibirPreOrdem() {
        if (root == null) { System.out.println("  (árvore vazia)"); return; }
        System.out.println("  Pre-order (raiz → esq → dir):");
        preOrder(root, new int[]{1});
    }

    private void preOrder(TreeNode node, int[] pos) {
        if (node == null) return;
        System.out.printf("  %2d. %s%n", pos[0]++, node.music);
        preOrder(node.left, pos);
        preOrder(node.right, pos);
    }

    /** Pos-order: esquerda → direita → raiz */
    public void exibirPosOrdem() {
        if (root == null) { System.out.println("  (árvore vazia)"); return; }
        System.out.println("  Pos-order (esq → dir → raiz):");
        posOrder(root, new int[]{1});
    }

    private void posOrder(TreeNode node, int[] pos) {
        if (node == null) return;
        posOrder(node.left, pos);
        posOrder(node.right, pos);
        System.out.printf("  %2d. %s%n", pos[0]++, node.music);
    }

    // ─────────────────────────────────────────────
    // VISUALIZAÇÃO ESTRUTURAL DA ÁRVORE
    // ─────────────────────────────────────────────

    /**
     * Exibe a árvore rotacionada 90° (direita → raiz → esquerda).
     * A indentação representa a profundidade do nó.
     */
    public void exibirArvore() {
        if (root == null) { System.out.println("  (árvore vazia)"); return; }
        System.out.println("  (visualização: direita em cima, raiz no centro, esquerda embaixo)");
        System.out.println("  ─────────────────────────────────────────────");
        exibirArvoreRec(root, 0);
        System.out.println("  ─────────────────────────────────────────────");
    }

    private void exibirArvoreRec(TreeNode node, int nivel) {
        if (node == null) return;
        exibirArvoreRec(node.right, nivel + 1);
        String indent = "  " + "        ".repeat(nivel);
        System.out.printf("%s[%d] %s%n", indent, node.music.getId(), node.music.getTitle());
        exibirArvoreRec(node.left, nivel + 1);
    }

    // ─────────────────────────────────────────────
    // UTILITÁRIOS
    // ─────────────────────────────────────────────

    public boolean isEmpty() { return root == null; }

    public TreeNode getRoot() { return root; }

    public int altura() { return alturaRec(root); }

    private int alturaRec(TreeNode node) {
        if (node == null) return 0;
        return 1 + Math.max(alturaRec(node.left), alturaRec(node.right));
    }

    public int totalNos() { return contarRec(root); }

    private int contarRec(TreeNode node) {
        if (node == null) return 0;
        return 1 + contarRec(node.left) + contarRec(node.right);
    }
}
