# Trabalho 01 – Refatoração com Padrões de Projeto em Sistema Legado

Este repositório foi utilizado no Trabalho 01 da disciplina **QXD0068 – Reuso de Software**, da Universidade Federal do Ceará – Campus Quixadá.

O sistema escolhido foi o **Freedom ERP**, um sistema livre de gestão empresarial. O trabalho teve como objetivo identificar problemas de projeto em um sistema legado e aplicar padrões de projeto do catálogo Gang of Four (GoF), buscando melhorar aspectos como reutilização, manutenção, coesão, acoplamento e extensibilidade.

## Integrantes

- **Geraldo Oliveira Pinho** – 565382
- **Luiz Henrique Nunes Sena** – 568584

**Disciplina:** QXD0068 – Reuso de Software  
**Professor:** Francisco Victor da Silva Pinheiro  
**Universidade Federal do Ceará – Campus Quixadá**

---

## Objetivo do Trabalho

O trabalho consiste na análise e refatoração de um sistema legado existente, buscando identificar problemas que dificultam sua manutenção, evolução e reutilização.

A partir dos problemas encontrados no Freedom ERP, foram aplicados padrões de projeto GoF com o objetivo de reduzir duplicações, melhorar a organização do código e favorecer a reutilização de comportamentos e estruturas existentes.

---

## Organização do Repositório

As principais versões do trabalho estão organizadas nas seguintes branches:

- `versao-original`: versão original do Freedom ERP antes das refatorações;
- `test/baseline-template`: testes de caracterização realizados antes da aplicação do Template Method;
- `main`: versão principal contendo as refatorações e os testes.

Essa organização permite comparar o sistema original com sua versão refatorada e acompanhar a evolução das alterações realizadas.

---

## Problemas Identificados

### Duplicação da regra de agrupamento

Nas classes `BalanceteBarras` e `BalancetePizza` foi identificada a mesma regra responsável por agrupar valores inferiores a 3% do total na categoria **"Outros valores"**.

A implementação apresentava duplicação de lógica, pois o mesmo critério estava definido diretamente nas duas classes.

Caso o percentual de agrupamento precisasse ser alterado, seria necessário modificar diferentes pontos do código. Além disso, a regra estava misturada à lógica de geração dos relatórios, dificultando sua reutilização e seus testes de forma isolada.

---

### Duplicação no fluxo de geração dos relatórios

Também foi identificada uma estrutura semelhante entre as classes `EvoluVendasBarras` e `EvoluVendasLinha`.

As duas classes executavam um fluxo semelhante:

- configuração do relatório;
- montagem do cabeçalho;
- leitura e processamento dos dados;
- criação do gráfico;
- desenho do relatório;
- finalização da página.

A principal diferença estava no tipo de gráfico utilizado e na preparação dos dados.

`EvoluVendasBarras` utiliza um gráfico de barras, enquanto `EvoluVendasLinha` utiliza um gráfico de linhas.

Apesar dessas diferenças, grande parte do fluxo era repetida nas duas classes, aumentando o custo de manutenção e a possibilidade de alterações inconsistentes.

---

# Padrões de Projeto Aplicados

## Strategy

**Categoria:** Comportamental.

**Problema relacionado:** duplicação da regra responsável por agrupar valores inferiores a 3% do total nas classes `BalanceteBarras` e `BalancetePizza`.

O padrão **Strategy** foi utilizado para encapsular o critério de agrupamento em uma abstração independente.

Foram criadas as classes:

- `EstrategiaAgrupamento`;
- `AgrupamentoPorPercentual`.

As classes `BalanceteBarras` e `BalancetePizza` passaram a delegar a decisão de agrupamento para a estratégia.

### Benefícios

- redução da duplicação de código;
- centralização da regra de agrupamento;
- maior facilidade de manutenção;
- possibilidade de substituir o critério utilizado;
- possibilidade de criação de novas estratégias;
- maior facilidade para realização de testes isolados;
- aumento do potencial de reutilização da regra.

### Estrutura simplificada

```text
BalanceteBarras ──┐
                  │
                  ├──> EstrategiaAgrupamento
                  │              ↑
BalancetePizza ───┘              │
                                 │
                      AgrupamentoPorPercentual
```

---

## Template Method

**Categoria:** Comportamental.

**Problema relacionado:** repetição do fluxo de geração dos relatórios nas classes `EvoluVendasBarras` e `EvoluVendasLinha`.

O padrão **Template Method** foi utilizado para centralizar o fluxo comum de geração dos relatórios.

Foi criada a classe abstrata:

```text
RelatorioEvolucaoVendas
```

Essa classe passou a definir a sequência geral do algoritmo, enquanto as subclasses ficaram responsáveis apenas pelos passos que variam.

As principais etapas específicas são:

- `processarDados()`;
- `criarGrafico()`;
- `desenharRelatorio()`.

As classes:

- `EvoluVendasBarras`;
- `EvoluVendasLinha`;

passaram a herdar de `RelatorioEvolucaoVendas`.

### Benefícios

- redução da duplicação do fluxo;
- centralização da sequência de geração dos relatórios;
- maior facilidade de manutenção;
- melhor separação de responsabilidades;
- maior reaproveitamento de código;
- facilidade para criação de novos tipos de relatório.

### Estrutura simplificada

```text
LeiauteGR
    ↑
RelatorioEvolucaoVendas
    ↑
    ├── EvoluVendasBarras
    └── EvoluVendasLinha
```

---

# Validação das Refatorações

As refatorações foram validadas por meio de testes automatizados utilizando **JUnit 4.13.2**.

O objetivo dos testes é verificar se os comportamentos relevantes do sistema permanecem funcionando após as alterações estruturais.

---

## Testes do Strategy

Foi criada a classe:

```text
AgrupamentoPorPercentualTest
```

Foram testados quatro cenários referentes à regra de agrupamento:

| Percentual | Resultado esperado |
|---|---|
| 1% | Deve ser agrupado |
| 2,99% | Deve ser agrupado |
| 3% | Não deve ser agrupado |
| 10% | Não deve ser agrupado |

Para executar os testes:

```bash
mvn -pl freedom "-Dtest=AgrupamentoPorPercentualTest" test
```

Resultado obtido:

```text
Tests run: 4
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

Os resultados confirmam que a estratégia criada preservou o comportamento esperado da regra original.

---

## Testes do Template Method

Antes da aplicação do Template Method foram criados testes de caracterização para as classes:

- `EvoluVendasBarras`;
- `EvoluVendasLinha`.

Os testes verificam a criação dos respectivos gráficos.

A versão anterior à refatoração pode ser encontrada na branch:

```text
test/baseline-template
```

Após a refatoração, foram executados testes equivalentes sobre as novas implementações.

Para executar:

```bash
mvn -pl freedom "-Dtest=EvoluVendasBarrasTest,EvoluVendasLinhaTest" test
```

A comparação entre os testes antes e depois da refatoração permite verificar que a estrutura interna foi modificada sem comprometer o comportamento relacionado à geração dos gráficos.

---

# Comparação Antes e Depois

| Problema | Antes | Depois |
|---|---|---|
| Regra de agrupamento | Duplicada em `BalanceteBarras` e `BalancetePizza` | Encapsulada com Strategy |
| Critério de 3% | Definido diretamente nas classes | Centralizado em `AgrupamentoPorPercentual` |
| Fluxo de relatórios | Repetido em duas classes | Centralizado com Template Method |
| Criação de relatórios | Cada classe controla todo o processo | Classe base define o fluxo comum |
| Manutenção | Alterações em múltiplos locais | Responsabilidades centralizadas |
| Reutilização | Comportamentos presos às classes | Comportamentos extraídos e reutilizáveis |
| Extensibilidade | Maior necessidade de modificar código existente | Novas implementações podem reutilizar abstrações existentes |

---

# Projeto Base – Freedom ERP

A partir deste ponto é apresentada a documentação técnica do projeto original utilizado como base para o trabalho.

## Freedom ERP

**Autor:** Setpoint Informática Ltda.  
**Tecnologias:** JDBC, Swing  
**Banco de dados:** Firebird 1.5.6  
**Resumo:** Sistema integrado de gestão empresarial  
**Linguagem:** Java  
**Versão:** Freedom ERP 1.2.6.5  

Fonte original:

<http://freedom-erp.googlecode.com/svn/tags/1.2/1.2.6/1.2.6.5>

---

## O que é o Freedom ERP?

O Freedom ERP é um software livre para Gestão Empresarial.

Pode ser utilizado em estabelecimentos comerciais, varejistas, atacadistas e industriais.

O sistema é dividido em vários módulos e busca atender diferentes necessidades de empresas em diversas áreas de atuação.

---

## Descrição dos módulos do Freedom ERP

### FreedomCRM

CRM (*Customer Relationship Management*) pode ser traduzido como Gestão de Relacionamento com o Cliente.

Esse módulo é responsável por funcionalidades relacionadas ao relacionamento entre a empresa e seus clientes.

Referência original:

<http://www.freedom.org.br/faces/pages/crm.xhtml>

---

### FreedomGMS

O módulo FreedomGMS permite a gestão de ordens de serviços.

Referência original:

<http://www.freedom.org.br/faces/pages/gms.xhtml>

---

### FreedomPDV

O módulo FreedomPDV controla o caixa dos estabelecimentos, disponibilizando mecanismos para realização de vendas e impressão de cupons em impressoras fiscais.

Referência original:

<http://www.freedom.org.br/faces/pages/pdv.xhtml>

---

### FreedomFNC

O módulo financeiro disponibiliza ferramentas para controle financeiro da empresa, incluindo:

- contas a pagar;
- contas a receber;
- movimentação bancária;
- emissão de recibos;
- emissão de boletos;
- arquivos de remessa;
- baixa automática de títulos através de arquivos de retorno.

Referência original:

<http://www.freedom.org.br/faces/pages/fnc.xhtml>

---

### FreedomPCP

Módulo relacionado ao Planejamento e Controle da Produção.

Referência original:

<http://www.freedom.org.br/faces/pages/pcp.xhtml>

---

### FreedomSTD

O FreedomSTD reúne diversas funcionalidades necessárias para agilizar e controlar processos empresariais.

Referência original:

<http://www.freedom.org.br/faces/pages/std.xhtml>

---

# Requisitos do Sistema

O ambiente utilizado pelo projeto legado possui os seguintes requisitos:

- JDK 1.8;
- Maven 3.3.9;
- Freedom ERP 1.2.6.5;
- Firebird 1.5.6.

Durante o desenvolvimento deste trabalho foi utilizado **Java 8**, necessário devido ao uso de APIs antigas presentes no projeto.

---

# Configurações Iniciais

A documentação original do Freedom ERP recomenda a instalação do sistema no diretório:

```text
C:\opt\freedom\
```

e do Firebird no diretório:

```text
C:\opt\firebird\
```

O projeto utiliza um arquivo de configuração chamado:

```text
freedom.ini
```

que contém informações utilizadas durante a inicialização do sistema.

---

# Dependências Adicionais

Algumas dependências utilizadas pelo Freedom ERP não estão disponíveis nos repositórios Maven utilizados pelo projeto.

Por esse motivo, é necessário instalá-las manualmente no repositório Maven local.

No diretório raiz do projeto, execute:

```bash
mvn install:install-file -DgroupId=comm -DartifactId=comm -Dversion=2.0 -Dpackaging=jar -Dfile=other-libs/comm-2.0.jar
```

```bash
mvn install:install-file -DgroupId=bizcal -DartifactId=bizcal -Dversion=NOT_INFORMED -Dpackaging=jar -Dfile=other-libs/bizcal.jar
```

```bash
mvn install:install-file -DgroupId=br.gov.sp.fazenda.dsge.brazilutils -DartifactId=dsgebrazilutils -Dversion=NOT_INFORMED -Dpackaging=jar -Dfile=other-libs/dsgebrazilutils.jar
```

```bash
mvn install:install-file -DgroupId=softwareexpress.sitef -DartifactId=jclisitef -Dversion=NOT_INFORMED -Dpackaging=jar -Dfile=other-libs/jclisitef.jar
```

```bash
mvn install:install-file -DgroupId=net.sf.nachocalendar -DartifactId=nachocalendar -Dversion=0.23 -Dpackaging=jar -Dfile=other-libs/nachocalendar-0.23.jar
```

```bash
mvn install:install-file -DgroupId=lu.tudor.santec -DartifactId=santec -Dversion=NOT_INFORMED -Dpackaging=jar -Dfile=other-libs/santec.jar
```

```bash
mvn install:install-file -DgroupId=com.birosoft.liguid -DartifactId=liquidlnf -Dversion=2.9.1 -Dpackaging=jar -Dfile=other-libs/liquidlnf-2.9.1.jar
```

```bash
mvn install:install-file -DgroupId=net.beeger.squareness -DartifactId=squareness -Dversion=NOT_INFORMED -Dpackaging=jar -Dfile=other-libs/squareness.jar
```

```bash
mvn install:install-file -DgroupId=org.eclipse.jdt.core.compiler -DartifactId=jdt-compiler -Dversion=NOT_INFORMED -Dpackaging=jar -Dfile=other-libs/jdt-compiler.jar
```

Dependendo do ambiente utilizado, outras dependências legadas presentes em `other-libs` também podem precisar ser instaladas manualmente.

---

# Compilação

Para compilar o módulo principal do Freedom ERP:

```bash
mvn -pl freedom test
```

Também é possível executar a instalação completa utilizando:

```bash
mvn clean install
```

---

# Como Executar

Após a compilação, o arquivo executável do módulo principal é gerado em:

```text
freedom/target/freedom.jar
```

No Windows, considerando o arquivo de configuração em `C:\opt\freedom\ini\freedom.ini`, execute:

```powershell
java "-DARQINI=C:\opt\freedom\ini\freedom.ini" -jar .\freedom\target\freedom.jar
```

A execução correta deve abrir a tela inicial de autenticação do Freedom ERP.

A documentação original do projeto também apresenta o seguinte comando para ambientes compatíveis:

```bash
java -DARQINI='/c/opt/freedom/ini/freedom.ini' -jar freedom/target/freedom.jar
```

---

# Execução dos Testes

## Todos os testes do módulo Freedom

```bash
mvn -pl freedom test
```

## Strategy

```bash
mvn -pl freedom "-Dtest=AgrupamentoPorPercentualTest" test
```

## Template Method

```bash
mvn -pl freedom "-Dtest=EvoluVendasBarrasTest,EvoluVendasLinhaTest" test
```

---

# Histórico das Refatorações

O histórico de commits do repositório foi organizado de forma a permitir o acompanhamento da evolução do sistema.

As alterações relacionadas aos padrões de projeto, testes e ajustes de compatibilidade foram registradas separadamente sempre que possível.

A branch `versao-original` permite acessar o sistema antes das refatorações, enquanto a branch `main` representa a versão refatorada.

---

# Relatório Técnico

Além do código-fonte, o trabalho possui um relatório técnico contendo:

- descrição do sistema;
- análise do código legado;
- problemas identificados;
- oportunidades de reutilização;
- padrões de projeto selecionados;
- justificativas das escolhas;
- refatorações realizadas;
- diagramas;
- testes e validação;
- comparação antes e depois;
- análise dos benefícios obtidos.

O relatório técnico em PDF será disponibilizado neste repositório juntamente com a versão final do trabalho.

---

# Créditos

O **Freedom ERP** é um projeto originalmente desenvolvido pela **Setpoint Informática Ltda.**

Este repositório foi utilizado exclusivamente como base para um trabalho acadêmico de análise e refatoração de software na disciplina **Reuso de Software**, da Universidade Federal do Ceará – Campus Quixadá.