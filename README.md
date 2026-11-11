# TP2 — Árvore Binária de Busca de Músicas
**Disciplina:** Estruturas de Dados  
**Linguagem:** Java 21

---

## Arquivos do Projeto

```
musicas.csv        — Arquivo de dados (mesmo catálogo do TP1)
Music.java         — Entidade música (ID, título, artista, duração)
TreeNode.java      — Nó da árvore BST
MusicBST.java      — Árvore Binária de Busca (inserção, busca, remoção, percursos)
MainTP2.java       — Classe principal com leitura do CSV e menu interativo
```

---

## Como Compilar e Executar

```bash
# Compilar (todos os .java no mesmo diretório)
javac *.java

# Executar (musicas.csv deve estar no mesmo diretório)
java MainTP2
```

---

## Estrutura da BST

**Critério de ordenação: ID da música**
- ID menor que o nó → vai para a **subárvore esquerda**
- ID maior que o nó → vai para a **subárvore direita**
- ID igual → **atualiza** os dados do nó existente

---

## Operações Implementadas

### `inserir(Music music)`
Inserção recursiva. Percorre a árvore comparando IDs até encontrar uma posição nula.

### `buscar(int id) → Music`
Busca recursiva com exibição do caminho percorrido (didático).
- Retorna a `Music` encontrada ou `null`.
- Complexidade: O(h), onde h é a altura da árvore.

### `remover(int id)` *(opcional — implementado)*
Remoção com três casos clássicos:
1. **Folha** (sem filhos) → remove diretamente
2. **Um filho** → substitui pelo filho
3. **Dois filhos** → substitui pelo **sucessor in-order** (menor nó da subárvore direita)

### Percursos
| Método | Ordem | Resultado |
|--------|-------|-----------|
| `exibirPreOrdem()` | Raiz → Esq → Dir | Ordem de inserção |
| `exibirEmOrdem()` | Esq → Raiz → Dir | IDs em ordem crescente |
| `exibirPosOrdem()` | Esq → Dir → Raiz | Folhas antes da raiz |

### `exibirArvore()`
Visualização estrutural rotacionada 90°: direita aparece em cima, raiz no centro, esquerda embaixo. A indentação representa a profundidade.

---

## Observação sobre Altura

Se o CSV estiver em **ordem crescente de ID** (como o padrão), a BST fica degenerada (altura = n, equivalente a uma lista encadeada). Para uma árvore mais balanceada, insira músicas em ordem não-sequencial, ou implemente uma AVL (extensão futura).

---

## Leitura do CSV

Formato esperado (`musicas.csv`):
```
id,titulo,artista,duracao
1,Bohemian Rhapsody,Queen,5.55
...
```
- Linha 1 é o cabeçalho e é ignorada.
- Linhas malformadas são ignoradas com aviso.
- Se o arquivo não for encontrado, o sistema inicia com árvore vazia.
