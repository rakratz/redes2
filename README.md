# Atividade prática — Redes de Computadores II

## Jogo de adivinhação com sockets TCP e threads

**Organização:** grupos de até 5 alunos. **Linguagem:** Java. **Interface:** terminal.

### Objetivo

Desenvolver uma aplicação cliente-servidor TCP que permita a vários jogadores tentar descobrir o mesmo número secreto. Exercitar sockets, comunicação por mensagens, threads e sincronização de dados compartilhados.

### Regras e requisitos

1. O servidor deve sortear, no início de cada partida, um único número inteiro entre **0 e 1000**, incluindo os extremos. Todos os clientes disputam esse mesmo número.
2. O servidor deve aceitar vários clientes simultaneamente, criando uma thread para cada conexão. Um cliente parado não pode impedir os demais de jogar.
3. Cada cliente envia um palpite por vez. O servidor responde **MAIOR** quando o número secreto é maior que o palpite e **MENOR** quando é menor.
4. O primeiro cliente que acertar vence. O servidor deve enviar imediatamente a **todos os clientes conectados, inclusive ao vencedor**, uma mensagem informando o IP, o hostname do vencedor e o número sorteado.
5. Após o primeiro acerto, nenhum novo palpite pode alterar o vencedor. A verificação do palpite e a definição do vencedor devem estar protegidas contra acessos simultâneos.
6. O cliente deve receber e exibir mensagens mesmo quando estiver aguardando digitação. Para isso, use uma thread exclusiva para receber dados do servidor.
7. Entradas inválidas, números fora do intervalo e desconexões devem ser tratados sem derrubar o servidor.
8. O comando **SAIR** deve encerrar apenas a conexão do jogador que o enviou.
9. Após anunciar o vencedor, o servidor deve encerrar as conexões da partida. Pode permanecer ativo para informar o resultado a conexões posteriores. Para uma nova partida, reinicie o servidor.

### Protocolo sugerido

Utilizem texto em UTF-8, com **uma mensagem por linha**. Cada envio deve terminar com quebra de linha; `println` e `readLine` já trabalham com esse formato.

| Origem | Mensagem | Significado |
|---|---|---|
| Cliente | `450` | Palpite |
| Cliente | `SAIR` | Desconectar |
| Servidor | `BEM_VINDO Digite um número entre 0 e 1000` | Início da participação |
| Servidor | `MAIOR` | Número secreto maior que o palpite |
| Servidor | `MENOR` | Número secreto menor que o palpite |
| Servidor | `ERRO Digite um inteiro entre 0 e 1000` | Entrada inválida |
| Servidor | `VENCEDOR IP=192.168.1.20 HOST=pc-lab-03 NUMERO=637` | Fim da partida para todos |

**Atenção ao hostname:** obtenham o IP com `getHostAddress()` e tentem obter o hostname com `getHostName()`. Se a rede não resolver o nome, o método pode retornar o próprio IP; esse resultado deve ser aceito e explicado na apresentação. A porta remota pode ser exibida adicionalmente para distinguir clientes executados na mesma máquina.

### Esqueleto fornecido

Os arquivos `Servidor.java` e `Cliente.java` contêm a estrutura inicial e os pontos marcados como **TODO 1 a TODO 9**. O esqueleto está incompleto: inicialmente o servidor fecha as conexões recebidas, e o cliente não realiza a troca de mensagens. O grupo deve implementar as partes indicadas.

### Etapas sugeridas

1. Implementar o sorteio e a criação de threads para os clientes.
2. Fazer um cliente enviar um palpite e receber a resposta.
3. Implementar validação e o comando SAIR.
4. Manter a lista de jogadores conectados.
5. Sincronizar o processamento de palpites e a definição do vencedor.
6. Implementar o envio do resultado a todos e o encerramento das conexões.
7. Implementar a recepção independente no cliente e testar com pelo menos três clientes.

**Dica de sincronização:** `CopyOnWriteArrayList` permite percorrer a lista enquanto há inclusões ou remoções. Ela não protege a regra do primeiro vencedor. Utilizem a mesma `travaJogo` para registrar jogadores, consultar o estado da partida e processar palpites. Isso também evita que um cliente entre entre a definição do vencedor e o anúncio final.

### Compilação e execução

No diretório dos arquivos, com o JDK instalado:

```bash
javac Servidor.java Cliente.java
java Servidor
```

Em outros terminais, iniciar pelo menos três clientes:

```bash
java Cliente localhost
```

Para clientes em outras máquinas, substituir `localhost` pelo IP do computador que executa o servidor:

```bash
java Cliente 192.168.1.10
```

Realizem primeiro o teste local e depois o teste em rede, conforme as condições do laboratório. A porta TCP 5000 precisa estar acessível entre as máquinas.

### Testes obrigatórios

- Três clientes jogando simultaneamente, com respostas individuais para os palpites.
- Cliente sem enviar palpites recebendo imediatamente o anúncio do vencedor.
- Texto inválido, linha vazia e valores fora do intervalo sem interrupção do servidor.
- Um cliente saindo enquanto os demais continuam jogando.
- Dois clientes enviando o palpite correto quase ao mesmo tempo: apenas um vencedor e o mesmo resultado para todos.
- Conexão após o fim da partida recebendo o resultado, sem iniciar outra disputa.

Para testar o acerto simultâneo, o grupo pode definir temporariamente um número conhecido durante o desenvolvimento. Na entrega, deve restaurar o sorteio aleatório. O servidor não deve revelar o número antes do fim da partida.

### Entrega e apresentação

Entregar os arquivos `.java`, instruções de execução, nomes dos integrantes e um breve relato dos testes com capturas dos terminais. Demonstrar o jogo com pelo menos três clientes e explicar a função das threads, o protocolo e a proteção contra dois vencedores.

### Avaliação sugerida — 10 pontos

| Critério | Pontos |
|---|---:|
| Comunicação TCP e protocolo por linhas | 2,0 |
| Atendimento simultâneo com threads | 2,0 |
| Sorteio, comparação e validação dos palpites | 2,0 |
| Vencedor único e anúncio imediato para todos com IP e hostname | 3,0 |
| Organização, testes e apresentação | 1,0 |

**Perguntas para a apresentação:** Por que o cliente precisa receber mensagens em outra thread? O que pode acontecer sem sincronização no servidor? Qual é a diferença entre IP, hostname e porta? O que acontece quando `readLine()` retorna `null`?
