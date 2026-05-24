# 🏥 Clínica Agendamentos
 
Sistema de agendamentos para clínica médica desenvolvido em Java com Maven.
 
---
 
## ✅ Pré-requisitos
 
Antes de qualquer coisa, você precisa ter instalado:
 
- **Java 21** ([Baixar aqui](https://adoptium.net/))
- **Maven 3.8+** ([Baixar aqui](https://maven.apache.org/download.cgi))
Para verificar se já estão instalados, abra o terminal e rode:
 
```bash
java -version
mvn -version
```
 
Se aparecer a versão, está tudo certo. Se der erro, siga os passos de instalação abaixo.
 
---
 
## 📦 Como instalar o Maven
 
### Windows
 
1. Baixe o arquivo `.zip` em https://maven.apache.org/download.cgi (procure por **Binary zip archive**)
2. Extraia em uma pasta, por exemplo `C:\Program Files\Apache\maven`
3. Adicione o Maven ao PATH do sistema:
   - Pesquise **"Variáveis de Ambiente"** no menu Iniciar
   - Em **Variáveis do Sistema**, clique em `Path` → `Editar`
   - Adicione o caminho: `C:\Program Files\Apache\maven\bin`
4. Abra um novo terminal e teste com `mvn -version`
### macOS
 
Com Homebrew (recomendado):
 
```bash
brew install maven
```
 
Sem Homebrew, siga o mesmo processo do Windows usando a pasta `/usr/local/`.
 
### Linux (Ubuntu/Debian)
 
```bash
sudo apt update
sudo apt install maven
```
 
---
 
## 🚀 Como rodar o projeto
 
### 1. Clone o repositório
 
```bash
git clone <url-do-repositorio>
cd clinica-agendamentos
```
 
### 2. Instale as dependências e compile
 
```bash
mvn install
```
 
Esse comando baixa as dependências (Gson, JUnit) e compila o projeto automaticamente.
 
### 3. Rode a aplicação
 
```bash
mvn exec:java -Dexec.mainClass="com.clinica.App"
```
 
Ou, se preferir gerar o `.jar` e executar:
 
```bash
mvn package
java -jar target/clinica-agendamentos-1.0-SNAPSHOT.jar
```
 
### 4. Rode os testes
 
```bash
mvn test
```
 
---
 
## 📁 Estrutura do Projeto
 
```
clinica-agendamentos/
├── src/
│   ├── main/java/com/clinica/
│   │   ├── App.java                  # Ponto de entrada
│   │   ├── model/
│   │   │   ├── Pessoa.java           # Classe abstrata base
│   │   │   ├── Paciente.java         # Modelo de paciente
│   │   │   └── Profissional.java     # Modelo de profissional
│   │   ├── repository/               # Acesso a dados
│   │   ├── service/                  # Regras de negócio
│   │   ├── report/                   # Relatórios
│   │   └── util/                     # Utilitários
│   └── test/java/com/clinica/
│       └── AppTest.java              # Testes automatizados
├── pom.xml                           # Configuração do Maven
└── README.md
```
 
---
 
## 🛠️ Dependências utilizadas
 
| Dependência | Versão | Para que serve |
|-------------|--------|----------------|
| Gson | 2.10.1 | Salvar e carregar dados em JSON |
| JUnit 5 | 5.10.2 | Testes automatizados |
 
---
 
## ❗ Problemas comuns
 
**`mvn` não é reconhecido como comando**
→ O Maven não foi adicionado ao PATH. Revise o passo de instalação.
 
**`java` não é reconhecido como comando**
→ O Java não está instalado ou não está no PATH. Instale o JDK 21 pelo link acima.
 
**Erro de compilação: "source/target release 21 requires..."**
→ Você está com uma versão antiga do Java. Certifique-se de usar o JDK 21.
 
**`BUILD FAILURE` ao rodar `mvn install`**
→ Verifique sua conexão com a internet. O Maven precisa baixar as dependências na primeira vez.