/**
 * Nó da Árvore Binária de Busca (BST).
 * O critério de ordenação/busca é o ID da música.
 */
public class TreeNode {
    public Music music;
    public TreeNode left;
    public TreeNode right;

    public TreeNode(Music music) {
        this.music = music;
        this.left = null;
        this.right = null;
    }
}
