package Projeto;

import java.util.*;

public class SistemaVisitas {

    // 3.1. Constante e variáveis globais
        /**
     "final" indica que o valor não pode ser alterado após a inicialização."
     */
    static final int MAX_VISITAS = 2;
    static Scanner scanner = new Scanner(System.in);

    // 3.4. Listas para guardar as visitas ativas (cada posição i representa uma visita)
    static List<String> visitantes = new ArrayList<>();
        /*Constructs an empty list with an initial capacity of ten   */
    static List<String> doentes = new ArrayList<>();

    public static void main(String[] args) {
        int opcao;

        // 3.2. Ciclo do-while para manter o menu ativo até escolher sair
        do {
            exibirMenu();
            opcao = lerOpcao();

            // 3.2. Estrutura de seleção switch
            switch (opcao) {
                case 1 -> registarEntrada();
                case 2 -> consultarVisitas();
                case 3 -> registarSaida();
                case 4 -> alertaPortariaEComparacao();
                case 0 -> System.out.println("A sair do programa...");
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 0);

        scanner.close();
    }

    // 3.3. Método modular para apresentar o menu
    static void exibirMenu() {
        System.out.println("\n=== GESTÃO DE VISITAS HOSPITALARES ===");
        System.out.println("1 - Registar entrada");
        System.out.println("2 - Consultar visitas ativas");
        System.out.println("3 - Registar saída");
        System.out.println("4 - Alerta de portaria (>1 visita) e contagem recursiva");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    // --- 1. REGISTAR ENTRADA ---
    static void registarEntrada() {
        System.out.print("Nome do doente: ");
        String doente = scanner.nextLine().trim();

        if (doente.isEmpty()) {
            System.out.println("Erro: O nome do doente não pode ficar vazio.");
            return;
        }

        // Verifica quantas visitas ativas o doente já tem
        int totalDoente = contarVisitasIterativo(doente);

        // 3.1 & 3.2. Decisão condicional e operador relacional (>=)
        if (totalDoente >= MAX_VISITAS) {
            System.out.println("BLOQUEADO: O doente " + doente + " já tem " + MAX_VISITAS + " visitas ativas!");
            return;
        }

        System.out.print("Nome do visitante: ");
        String visitante = scanner.nextLine().trim();

        if (visitante.isEmpty()) {
            System.out.println("Erro: O nome do visitante não pode ficar vazio.");
            return;
        }

        // Operação de inserção nas listas
        visitantes.add(visitante);
        doentes.add(doente);

        System.out.println("Entrada registada! (" + (totalDoente + 1) + "/" + MAX_VISITAS + " visitas para " + doente + ")");
    }

    // --- 2. CONSULTAR VISITAS ATIVAS ---
    static void consultarVisitas() {
        System.out.println("\n--- VISITAS ATIVAS NO HOSPITAL ---");
        if (visitantes.isEmpty()) {
            System.out.println("Não há visitas no hospital neste momento.");
            return;
        }

        // Operação de leitura/percurso sobre os elementos das listas
        for (int i = 0; i < visitantes.size(); i++) {
            System.out.println((i + 1) + ". Visitante: " + visitantes.get(i) + " -> Doente: " + doentes.get(i));
        }
    }

    // --- 3. REGISTAR SAÍDA ---
    static void registarSaida() {
        consultarVisitas();
        if (visitantes.isEmpty()) {
            return;
        }

        System.out.print("Número da visita que vai sair: ");
        int numero = lerOpcao();
        int indice = numero - 1;

        // 3.1 & 3.2. Operadores lógicos (&&) e validação de índice
        if (indice >= 0 && indice < visitantes.size()) {
            // Operação de remoção/manipulação da lista
            String saiu = visitantes.remove(indice);
            String doente = doentes.remove(indice);
            System.out.println("Saída registada: " + saiu + " (visitava " + doente + ").");
        } else {
            System.out.println("Número inválido!");
        }
    }

    // --- 3.4. ITERAÇÃO (Conta visitas de um doente com ciclo for) ---
    static int contarVisitasIterativo(String nomeDoente) {
        int contador = 0;
        for (String d : doentes) {
            if (d.equalsIgnoreCase(nomeDoente)) {
                contador++;
            }
        }
        return contador;
    }

    // --- 3.4. RECURSIVIDADE (Conta visitas de um doente com caso base e chamada recursiva) ---
    static int contarVisitasRecursivo(String nomeDoente, int indice) {
        // Caso base: chegou ao fim da lista
        if (indice == doentes.size()) {
            return 0;
        }
        // Soma 1 se corresponder ao doente + chamada recursiva para o próximo índice
        int atual = doentes.get(indice).equalsIgnoreCase(nomeDoente) ? 1 : 0;
        return atual + contarVisitasRecursivo(nomeDoente, indice + 1);
    }

    // --- 3.4. CONSTRUIR NOVA LISTA (for + if, Collections.sort e for + add, sem lambda) ---
    static List<String> obterAlertaDoentesMaisDeUmaVisita() {
        List<String> filtrados = new ArrayList<>();

        // 1. for + if: seleciona doentes com mais de 1 visita ativa (sem duplicados)
        for (String d : doentes) {
            if (contarVisitasIterativo(d) > 1 && !contemIgnorandoMaiusculas(filtrados, d)) {
                filtrados.add(d);
            }
        }

        // 2. Collections.sort: ordena os elementos da lista filtrada
        Collections.sort(filtrados);

        // 3. for + add: cria uma nova lista transformada (mantendo as listas originais intactas)
        List<String> resultado = new ArrayList<>();
        for (String d : filtrados) {
            int total = contarVisitasIterativo(d);
            resultado.add(d.toUpperCase() + " (" + total + "/" + MAX_VISITAS + " visitas)");
        }

        return resultado;
    }

    static boolean contemIgnorandoMaiusculas(List<String> lista, String alvo) {
        for (String item : lista) {
            if (item.equalsIgnoreCase(alvo)) {
                return true;
            }
        }
        return false;
    }

    // Opção 4: Apresenta o alerta de portaria e compara contagem iterativa vs. recursiva
    static void alertaPortariaEComparacao() {
        List<String> alertas = obterAlertaDoentesMaisDeUmaVisita();

        System.out.println("\n--- ALERTA DE PORTARIA (DOENTES COM MAIS DE 1 VISITA ATIVA) ---");
        if (alertas.isEmpty()) {
            System.out.println("Nenhum doente tem mais de 1 visita ativa neste momento.");
        } else {
            for (String alerta : alertas) {
                System.out.println("- " + alerta);
            }
        }

        System.out.print("\nNome do doente para comparar contagem (iterativa vs. recursiva): ");
        String nome = scanner.nextLine().trim();
        System.out.println("Contagem Iterativa : " + contarVisitasIterativo(nome));
        System.out.println("Contagem Recursiva : " + contarVisitasRecursivo(nome, 0));
    }

    // Método auxiliar para ler inteiros com validação
    static int lerOpcao() {
        while (!scanner.hasNextInt()) {
            System.out.print("Por favor, introduza um número válido: ");
            scanner.nextLine();
        }
        int valor = scanner.nextInt();
        scanner.nextLine(); // Limpa a quebra de linha pendente
        return valor;
    }
}