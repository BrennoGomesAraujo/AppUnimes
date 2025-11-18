# 📱 AppUnimes - Sistema de Cadastro de Produtos

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blue.svg)](https://kotlinlang.org)
[![Android](https://img.shields.io/badge/Android-21%2B-brightgreen.svg)](https://developer.android.com)
[![Firebase](https://img.shields.io/badge/Firebase-Cloud%20Firestore-orange.svg)](https://firebase.google.com)

Um aplicativo Android moderno desenvolvido em Kotlin para cadastro e gerenciamento de produtos com sincronização em tempo real usando Firebase Firestore.

## ✨ Funcionalidades

- ✅ **Cadastro Completo** - Nome, preço, quantidade e categoria
- ✅ **Lista Dinâmica** - Visualização em cards com scroll
- ✅ **Sincronização Cloud** - Dados salvos no Firebase em tempo real
- ✅ **Interface Moderna** - Material Design 3 com navegação intuitiva
- ✅ **Validações** - Campos obrigatórios e formatação de moeda

## 🛠️ Tecnologias Utilizadas

- **Linguagem**: Kotlin
- **Banco de Dados**: Firebase Firestore
- **UI**: Material Design 3, ConstraintLayout, CardView
- **Arquitetura**: Activities com ViewBinding
- **Dependências**: Firebase SDK, Android Jetpack

## 📱 Telas do Aplicativo

### Tela de Cadastro
- Formulário com validação integrada
- Campos: Nome, Preço, Quantidade, Categoria
- Botão de cadastro e FAB para navegação

### Tela de Lista  
- Layout dinâmico sem RecyclerView
- Cards organizados com ScrollView
- Formatação automática para Real Brasileiro
- Estados: Carregando, Vazio, Sucesso

## 🚀 Como Executar

### Pré-requisitos
- Android Studio (versão mais recente)
- Dispositivo/Emulador Android API 21+
- Conta no Firebase

### Configuração
1. Clone o repositório
2. Abra no Android Studio
3. Configure o Firebase:
   - Crie projeto em [Firebase Console](https://console.firebase.google.com)
   - Adicone app Android e baixe `google-services.json`
   - Cole em `app/` directory
4. Sync e Build do projeto

### Estrutura do Projeto
